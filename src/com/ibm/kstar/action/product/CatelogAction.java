package com.ibm.kstar.action.product;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.action.system.lov.member.LovMemberAction;
import com.ibm.kstar.api.product.ICatelogMatchService;
import com.ibm.kstar.api.product.IProLovService;
import com.ibm.kstar.api.product.IProductService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovGroup;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.product.CatelogMatchBean;
import com.ibm.kstar.entity.product.KstarProduct;
import com.ibm.kstar.interceptor.system.permission.NoRight;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.log.LogOperate;
import org.xsnake.web.page.IPage;
import org.xsnake.web.page.PageImpl;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.ExcelUtil;
import org.xsnake.web.utils.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/product/catalog")
public class CatelogAction extends LovMemberAction {

    @Autowired
    IProductService productService;

    @Autowired
    IProLovService proLovService;

    @Autowired
    ICatelogMatchService catelogMatchService;

    @Autowired
    ILovMemberService lovMemberService;


    @RequestMapping("/catalogadd")
    public String catalogadd(String groupId, String parentId, Model model) {
        LovGroup lovGroup = lovGroupService.get(groupId);
        model.addAttribute("lovGroup", lovGroup);
        if (StringUtil.isNotEmpty(parentId)) {
            LovMember parentLovMember = lovMemberService.get(parentId);
            model.addAttribute("parentLovMember", parentLovMember);
        }

        LovMember lovMember = new LovMember();
        lovMember.setStartDate(new Date());
        model.addAttribute("lovMember", lovMember);
        return forward("catalogMember");
    }

    @ResponseBody
    @RequestMapping(value = "/catalogadd", method = RequestMethod.POST)
    public String catalogadd(LovMember lovMember) {
        if ((new Date()).getTime() - lovMember.getStartDate().getTime() > 24 * 60 * 60 * 1000) {
            throw new AnneException("有效期开始日期不能小于当前日期！");
        }
        if (lovMember.getEndDate() != null) {
            if (lovMember.getEndDate().before(lovMember.getStartDate())) {
                throw new AnneException("有效期结束日期不能小于有效期开始日期！");
            }
        }

        //报备属性跟着上级走
        LovMember parent = lovMemberService.get(lovMember.getParentId());
        if (parent != null) {
            lovMember.setOptTxt2(parent.getOptTxt2());
        }

        lovMember.setCode(StringUtil.getUUID());
        proLovService.saveCatelog(lovMember);
        //		if("1".equals(lovMember.getUpBaoBei())){
        proLovService.updateBaobei(lovMember.getId(), lovMember.getOptTxt2());
        //		}
        return sendSuccessMessage();
    }

    @RequestMapping("/catalogedit")
    public String catalog(String id, Model model) {
        LovMember lovMember = lovMemberService.get(id);
        if (StringUtil.isNotEmpty(lovMember.getOptTxt5())) {
            KstarProduct product = productService.queryPureProductById(lovMember.getOptTxt5());
            model.addAttribute("productJson", JSON.toJSONString(product));
        }
        LovGroup lovGroup = lovGroupService.get(lovMember.getGroupId());
        model.addAttribute("lovGroup", lovGroup);
        model.addAttribute("lovMember", lovMember);
        if (StringUtil.isNotEmpty(lovMember.getParentId())) {
            LovMember parentLovMember = lovMemberService.get(lovMember.getParentId());
            model.addAttribute("parentLovMember", parentLovMember);
        }
        return forward("catalogMember");
    }

    @ResponseBody
    @RequestMapping(value = "/catalogedit", method = RequestMethod.POST)
    public String catalogedit(LovMember lovMember) {
        if (lovMember.getEndDate() != null) {
            if (lovMember.getEndDate().before(lovMember.getStartDate())) {
                throw new AnneException("有效期结束日期不能小于有效期开始日期！");
            }
        }
        if ("Y".equals(lovMember.getLeafFlag())) {
            lovMemberService.update(lovMember);
        } else {
            lovMemberService.updateTree(lovMember);
            //			if("1".equals(lovMember.getUpBaoBei())){
            proLovService.updateBaobei(lovMember.getId(), lovMember.getOptTxt2());
            //			}
        }
        return sendSuccessMessage();
    }

    @RequestMapping("/catalogmove")
    public String catalogMove(String ids, Model model) {
        if (StringUtil.isEmpty(ids)) {
            throw new AnneException("请选择需要移动的目录");
        }
        model.addAttribute("ids", ids);
        return forward("catalogMove");
    }

    @ResponseBody
    @RequestMapping(value = "/catalogmove", method = RequestMethod.POST)
    public String catalogMove(String[] ids, String parentId, Model model) {
        if (ids == null || ids.length == 0 || parentId == null) {
            throw new AnneException("需要移动的目录不能为空");
        }

        LovMember lovMember = lovMemberService.get(parentId);
        if (lovMember == null) {
            throw new AnneException("不存在的LOV：" + parentId);
        }
        for (String id : ids) {
            lovMemberService.move(id, parentId);
        }
        return sendSuccessMessage();
    }


    @RequestMapping("/productmove")
    public String productMove(String ids, Model model) {
        model.addAttribute("ids", ids);
        return forward("productMove");
    }

    @ResponseBody
    @RequestMapping(value = "/productmove", method = RequestMethod.POST)
    public String productMove(String[] ids, String parentId) {
        if (ids == null || ids.length == 0 || parentId == null) {
            throw new AnneException("参数不正确！");
        }
        LovMember lovMember = lovMemberService.get(parentId);
        if (lovMember == null) {
            throw new AnneException("不存在的LOV：" + parentId);
        }
        for (String id : ids) {
            lovMemberService.move(id, parentId);
        }
        return sendSuccessMessage();
    }

    @NoRight
    @ResponseBody
    @RequestMapping("/productchoose")
    public String productchoose(Condition condition, HttpServletRequest request) {
        ActionUtil.requestToCondition(condition, request);
        List<KstarProduct> list = productService.getList(condition);
        return sendSuccessMessage(list);
    }

    @NoRight
    @ResponseBody
    @RequestMapping("/catelogMatch")
    public String catelogMatch(PageCondition condition, HttpServletRequest request) throws Exception {
        ActionUtil.requestToCondition(condition, request);

        String parentId = condition.getStringCondition("parentId");

        IPage p = catelogMatchService.query(condition, parentId);
        for (Object o : p.getList()) {
            if (!(o instanceof CatelogMatchBean)) {
                continue;
            }
            CatelogMatchBean cmb = (CatelogMatchBean) o;
            String crmCategory = cmb.getCrmCategory();
            if (StringUtils.isNotEmpty(crmCategory)) {
                LovMember lov = lovMemberService.getLovMemberByCode("crmCategory", crmCategory);
                if (lov != null) {
                    cmb.setCrmCategoryLabel(lov.getName());
                }
            }
            String matchType = cmb.getMatchType();
            if (StringUtils.isNotEmpty(matchType)) {
                LovMember lov = lovMemberService.getLovMemberByCode("MatchType", matchType);
                if (lov != null) {
                    cmb.setMatchTypeLabel(lov.getName());
                }
            }
        }
        return sendSuccessMessage(p);
    }


    @RequestMapping("/addCatelogMatch")
    public String addCatelogMatch(String id, String parentId, Model model) {

        CatelogMatchBean kp = null;
        if (StringUtils.isNotEmpty(id)) {
            model.addAttribute("id", id);
            kp = catelogMatchService.queryById(id);
        } else {
            if (StringUtils.isNotEmpty(parentId)) {
                LovMember lovMember = lovMemberService.get(parentId);
                kp = new CatelogMatchBean();
                kp.setDirectID(parentId);
                kp.setDirectName(lovMember.getName());
            }
        }
        model.addAttribute("cmb", kp);
        model.addAttribute("now", new Date());
        return forward("addCatalogMatch");
    }

    @ResponseBody
    @RequestMapping("/deleteCatelogMatch")
    public String deleteCatelogMatch(String id, Model model) {
        if (StringUtils.isEmpty(id)) {
            throw new AnneException("请选择要删除的产品");
        }
        this.catelogMatchService.delete(id);
        return sendSuccessMessage();
    }

    @NoRight
    @RequestMapping("/selectDepartment")
    public String selectDepartment() {
        return forward("selectDepartment");
    }

    @ResponseBody
    @RequestMapping(value = "/addCatelogMatch", method = RequestMethod.POST)
    public String addCatelogMatch(CatelogMatchBean cmb) {
        boolean firstTime = true;
        if (StringUtils.isEmpty(cmb.getEffective())) {
            cmb.setEffective("N");
        }
        if (StringUtils.isNotEmpty(cmb.getId())) {
            firstTime = false;
        }
        if (StringUtil.isEmpty(cmb.getCrmCategory())) {
            throw new AnneException("CRM产品类别不能为空");
        }
        String[] crmCategorys = cmb.getCrmCategory().trim().split(",");
        for (int i = 0; i < crmCategorys.length; i++) {
            String crmCategory = crmCategorys[i];
            if (i == 0) {
                cmb.setCrmCategory(crmCategory);
                catelogMatchService.save(cmb, this.getUserObject());
            } else {
                CatelogMatchBean bean = new CatelogMatchBean();
                org.springframework.beans.BeanUtils.copyProperties(cmb, bean);
                bean.setId(null);
                bean.setCrmCategory(crmCategory);
                catelogMatchService.save(bean, this.getUserObject());
            }
            if (firstTime && "Y".equalsIgnoreCase(cmb.getEffective())) {
                List<LovMember> cmList = catelogMatchService.queryByDirectID(cmb.getDirectID());
                for (LovMember lm : cmList) {
                    lovMemberService.saveProduct(lm);
                }
            } else if (!firstTime && "Y".equalsIgnoreCase(cmb.getEffective())) {
//                lovMemberService.deleteByMatchID(cmb.getId());
                List<LovMember> cmList = catelogMatchService.queryByDirectID(cmb.getDirectID());
                for (LovMember lm : cmList) {
                    lovMemberService.saveProduct(lm);
                }
            }
            // 已经入销售目录的不删除
            //            else if (!firstTime && "N".equalsIgnoreCase(cmb.getEffective())) {
            //                lovMemberService.deleteByMatchID(cmb.getId());
            //            }
        }
        return sendSuccessMessage();
    }

    @NoRight
    @ResponseBody
    @RequestMapping("/selectModel")
    public String selectModel(String category,String search, HttpServletRequest request) {
        List<LovMember> list = catelogMatchService.queryProModel(search);
        return sendSuccessMessage(list);
    }

    @NoRight
    @ResponseBody
    @RequestMapping("/selectCCategory")
    public String selectCCategory(HttpServletRequest request) {
        List<LovMember> list = catelogMatchService.queryProCategory();
        return sendSuccessMessage(list);
    }

    @ResponseBody
    @RequestMapping("/maptree")
    public String maptree(Condition condition, HttpServletRequest request) {
        ActionUtil.requestToCondition(condition, request);
        String groupCode = condition.getStringCondition("groupCode");
        String groupId = condition.getStringCondition("groupId");
        if (StringUtil.isEmpty(groupId) && StringUtil.isEmpty(groupCode)) {
            throw new AnneException("无效的参数访问");
        }
        if (StringUtil.isEmpty(groupId)) {
            LovGroup group = lovGroupService.getByCode(groupCode);
            if (group == null) {
                throw new AnneException("无效的参数访问");
            }
            groupId = group.getId();
        }
        String parentId = condition.getStringCondition("id");
        condition.getFilterObject().addCondition("groupId", "=", groupId);
        condition.getFilterObject().addCondition("deleteFlag", "=", "N");
        if (parentId == null) {
            parentId = "ROOT";
        }
        condition.getFilterObject().addCondition("parentId", "=", parentId);
        List<LovMember> list = lovMemberService.getList(condition);

        if (StringUtils.isNotEmpty(parentId)) {
            List<LovMember> cmList = catelogMatchService.queryByDirectID(parentId);

            if (!cmList.isEmpty()) {
                list.addAll(cmList);
            }
        }

        return sendSuccessMessage(list);
    }

    @NoRight
    @ResponseBody
    @RequestMapping("/demandSelectProduct")
    public String demandSelectProduct(PageCondition condition, HttpServletRequest request) {
        ActionUtil.requestToCondition(condition, request);
        IPage p = proLovService.demandSelectProduct(condition, this.getUserObject());
        return sendSuccessMessage(p);
    }

    @NoRight
    @LogOperate(module = "销售目录查询", notes = "${user}查看数据分页信息")
    @ResponseBody
    @RequestMapping("/mappageRight")
    public String mappageRight(PageCondition condition, HttpServletRequest request) {
        ActionUtil.requestToCondition(condition, request);
        String parentId = condition.getStringCondition("parentId");
        IPage p = new PageImpl(null, condition.getPage(), condition.getRows(), 0);
        if (StringUtil.isNotEmpty(parentId)) {
            p = proLovService.mappage(condition, this.getUserObject());
        }
        return sendSuccessMessage(p);
    }

    @LogOperate(module = "销售目录查询", notes = "${user}查看数据分页信息")
    @RequestMapping("/mappageRightExport")
    public void mappageRightExport(PageCondition condition, HttpServletRequest request, HttpServletResponse response) {
        ActionUtil.requestToCondition(condition, request);
        List<List<Object>> dataList = proLovService.mappageExport(condition, this.getUserObject());
        ExcelUtil.exportExcel(response, dataList, "价格表行");
    }

    @NoRight
    @LogOperate(module = "销售目录", notes = "${user}查看数据分页信息")
    @ResponseBody
    @RequestMapping("/mappage")
    public String mappage(PageCondition condition, HttpServletRequest request) {
        ActionUtil.requestToCondition(condition, request);
        IPage p = proLovService.mappage(condition, null);
        return sendSuccessMessage(p);
    }

    @LogOperate(module = "销售目录", notes = "${user}查看数据分页信息")
    @RequestMapping("/mappageExport")
    public void mappageExport(PageCondition condition, HttpServletRequest request, HttpServletResponse response) {
        ActionUtil.requestToCondition(condition, request);
        List<List<Object>> dataList = proLovService.mappageExport(condition, null);
        ExcelUtil.exportExcel(response, dataList, "价格表行");
    }

}
