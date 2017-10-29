package com.ibm.kstar.action.bizopp.newbid;

import com.alibaba.fastjson.JSONObject;
import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.api.bizopp.IBizBaseService;
import com.ibm.kstar.api.bizopp.IBizoppService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.ICorePermissionService;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.entity.bizopp.Bid;
import com.ibm.kstar.entity.bizopp.BusinessOpportunity;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.interceptor.system.permission.NoRight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.log.LogOperate;
import org.xsnake.web.page.IPage;
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.StringUtil;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/newbid")
public class BizBidNewAction extends BaseAction {

    @Autowired
    IBizBaseService bizService;

    @Autowired
    IBizoppService bizoppService;

    @Autowired
    ICustomInfoService customInfoService;

    @Autowired
    ILovMemberService lovMemberService;

    @Autowired
    ICorePermissionService corePermissionService;

    @RequestMapping("/index")
    public String index(String id, Model model) {
        return forward("bidIndex");
    }

    @NoRight
    @ResponseBody
    @RequestMapping("/page")
    public String page(PageCondition condition, HttpServletRequest request) throws Exception {
        ActionUtil.requestToCondition(condition, request);
        String bizOppId = request.getParameter("bizOppId");
        condition.getFilterObject().addCondition("bizOppId", "eq", bizOppId);
        ActionUtil.doSearch(condition);
        IPage p = bizoppService.queryBid(condition);

        return sendSuccessMessage(p);
    }


    @ResponseBody
    @RequestMapping(value = "/updateFeedBack", method = RequestMethod.POST)
    public String updateFeedBack(String cid, HttpServletRequest request) throws Exception {

        String bidResult = request.getParameter("bidResult");
        String itemQuote = request.getParameter("itemQuote");
        String hasAttm = request.getParameter("hasAttm");
        String bidCmpr = request.getParameter("bidCmpr");
        String bidBrd = request.getParameter("bidBrd");
        String allQuote = request.getParameter("allQuote");

        Bid bid = bizoppService.getBidEntity(cid);
        if (bid == null) {
            throw new AnneException("没有找到需要修改的数据");
        }

        bid.setBidResult(bidResult);
        bid.setItemQuote(itemQuote);
        bid.setHasAttm(hasAttm);
        bid.setBidCmpr(bidCmpr);
        bid.setBidBrd(bidBrd);
        bid.setAllQuote(allQuote);

        bizService.update(bid);

        return sendSuccessMessage();
    }


    @RequestMapping("/add")
    public String add(String bid, Model model) {

        Bid entity = new Bid();
        //如果在商机页面新增售前支持，则自动带入客户名称，商机名称
        BusinessOpportunity businessOpportunity = null;
        if (bid != null) {
            businessOpportunity = bizoppService.getBizOppEntity(bid);
            //商机ID
            entity.setBizOppId(bid);
            //商机名称
            entity.setBizOppName(businessOpportunity.getOpportunityName());
            entity.setProjectName(businessOpportunity.getOpportunityName());
            //代理商
            String agencyId = businessOpportunity.getAgentId();
            if (StringUtil.isNotEmpty(agencyId)) {
                CustomInfo customInfo = customInfoService.getCustomInfo(agencyId);
                if (customInfo != null) {
                    entity.setAgencyName(customInfo.getCustomFullName());
                    entity.setAgency(customInfo.getId());
                }
            }
            //项目所在地
            entity.setProjectAddress(businessOpportunity.getBizOppAddressName());
            //终端客户
            entity.setTerminalClient(businessOpportunity.getEnterprise());
            //是否集成包
            entity.setIsIntegrated(String.valueOf(businessOpportunity.getIsIntegreated()));
            //行业
            entity.setIndustry(businessOpportunity.getIndustry());
            entity.setBidEnterprise(businessOpportunity.getBidUnit());
            entity.setBidNumber(businessOpportunity.getBidNo());

        }
        //设置创建人
        entity.fillInit(getUserObject());

        LovMember makDepLov = lovMemberService.getLovMemberByCode("ORG", lovMemberService.getSaleCenter(getUserObject().getOrg().getId()));

        if (makDepLov != null) {
            entity.setSignUsr(makDepLov.getId());
        }
        entity.setStatus(ProcessConstants.PROCESS_STATUS_Pending);

        //生成投标授权编号
        String bidNumber = bizoppService.getSequenceCode("gen_bizoppbid_change_num");
        entity.setBidNumber(bidNumber);

        entity.setPrintStyle("10");
        model.addAttribute("entity", entity);

        TabMain tabMainInfo = new TabMain();
        tabMainInfo.setInitAll(false);
        model.addAttribute("tabMainInfo", tabMainInfo);

        return forward("bidAdd");
    }

    @LogOperate(module = "商机模块", notes = "${user}点击授权单位列表")
    @RequestMapping("/authManage")
    public String authManage(String id, Model model) {
        Bid entity = bizService.getEntity(id, new Bid());
        model.addAttribute("entity", entity);
        return forward("authManage");
    }

    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(Bid entity) {
        bizService.saveBid(entity, getUserObject());
        return sendSuccessMessage(entity.getId());
    }

    @NoRight
    @RequestMapping("/edit")
    public String edit(String id, Model model) {
        if (id == null) {
            throw new AnneException("没有找到需要修改的数据");
        }
        Bid entity = bizService.getEntity(id, new Bid());

        if (entity == null) {
            throw new AnneException("没有找到需要修改的数据");
        }

        CustomInfo customInfo = customInfoService.getCustomInfo(entity.getTerminalClient());
        LovMember lovMemberk = (LovMember) CacheData.getInstance().get(entity.getCrossDepartment());
        LovMember lovMembers = (LovMember) CacheData.getInstance().get(entity.getInvolveIndustry());
        Employee personInCharge = (Employee) CacheData.getInstance().get(entity.getPersonInCharge());

        if (lovMemberk != null) {
            model.addAttribute("lovMemberk", JSONObject.toJSONString(lovMemberk));
        }
        if (lovMembers != null) {
            model.addAttribute("lovMembers", JSONObject.toJSONString(lovMembers));
        }
        if (personInCharge != null) {
            model.addAttribute("personInCharge", JSONObject.toJSONString(personInCharge));
        }

//        LovMember makDepLov = lovMemberService.getLovMemberByCode("ORG", lovMemberService.getSaleCenter(getUserObject().getOrg().getId()));
//
//        entity.setSignUsr(makDepLov.getId());


        TabMain tabMainInfo = new TabMain();
        tabMainInfo.setInitAll(false);
        if (!("特约经销商".equals(entity.getSltType())
                || "物流类".equals(entity.getSltType())
                || "财务类".equals(entity.getSltType())
                || "报价类".equals(entity.getSltType())
                || "售后类".equals(entity.getSltType())
                || "售前类".equals(entity.getSltType())
                || "日常业务类".equals(entity.getSltType()))) {
            if (hasPermission("P03BidAuthT1OrgPage")) {
                tabMainInfo.addTab("授权单位", "/newbid/authManage.html?id=" + entity.getId());
            }
        }
        if (hasPermission("P03BidAuthT2FilePage")) {
        	if("Completed".equals(entity.getStatus()) || ("Processing".equals(entity.getStatus()) && !hasPermission("P09ApprovingAttachmentUpload")) ){
        		tabMainInfo.addTab("附件", "/common/attachment/attachment.html?businessType=Bid&docGroupCode=BidDOCTYPE&businessId=" + entity.getId() + "&unableAdd=true&unableDelete=true");
        	}else{
        		tabMainInfo.addTab("附件", "/common/attachment/attachment.html?businessType=Bid&docGroupCode=BidDOCTYPE&businessId=" + entity.getId() + "&unableAdd=false&unableDelete=false");
        	}
        }

        if (hasPermission("P03BidAuthT4TeamPosPage")) {
            tabMainInfo.addTab("销售团队", "/team/list.html?businessType=" + "bid" + "&businessId=" + entity.getId());
        }
        if (hasPermission("P03BidAuthT3ProReviewHistoryPage")) {
            tabMainInfo.addTab("审批历史", "/standard/history.html?contrId=" + entity.getId());
        }
        
        model.addAttribute("tabMainInfo", tabMainInfo);
        model.addAttribute("customInfo", JSONObject.toJSONString(customInfo));
        model.addAttribute("entity", entity);
        return forward("bidAdd");
    }

    @NoRight
    @ResponseBody
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String edit(Bid bizopp) {
        Bid bidSource = bizService.getEntity(bizopp.getId(), new Bid());

        BeanUtils.copyPropertiesIgnoreNull(bizopp, bidSource);
        
        bidSource.setUpdatedById(getUserObject().getEmployee().getId());
        bidSource.setUpdatedAt(new Date());

        bizService.update(bidSource);
        return sendSuccessMessage(bizopp.getId());
    }

    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(String id) {
		if(StringUtil.isEmpty(id)){
			throw new AnneException("id is null");
		}
		Bid bid = bizService.getEntity(id, new Bid());
		if(!bid.getStatus().equals("Pending")){
			throw new AnneException("业务用章不为新建状态下无法删除！");	
		}
        bizService.delete(id, Bid.class);
        return sendSuccessMessage();
    }

    @ResponseBody
    @RequestMapping(value = "/startProcess", method = RequestMethod.POST)
    public String startAuthProcess(String id, String bidNumber) {
        bizService.startNewAuthProcess(getUserObject(), id, bidNumber);
        return sendSuccessMessage();
    }
}
