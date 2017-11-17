package com.ibm.kstar.action.product.ecrchange;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.action.BaseFlowAction;
import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.api.product.IEcrService;
import com.ibm.kstar.api.product.IProductProcesService;
import com.ibm.kstar.api.product.IProductSerialService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.product.EcrChangeLine;
import com.ibm.kstar.entity.product.KstarEcrBean;
import com.ibm.kstar.interceptor.system.permission.NoRight;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Copyright: Copyright 2007-2017 HuangQi All Rights Reserved.
 *
 * @Author 黄奇
 * @Title:
 * @Package
 * @Description:
 * @Date 2017年08月18日 16:11
 * @LastModifier 黄奇
 */
@Controller
@RequestMapping("/product/ecrchange")
public class EcrChangeAction extends BaseFlowAction {

    @Autowired
    private IEcrService ecrService;

    @Autowired
    private IProductSerialService productSerialService;

    @Autowired
    IProductProcesService productProcessService;

    @Autowired
    private ILovMemberService lovMemberService;

    @NoRight
    @RequestMapping("list")
    public String list(PageCondition condition, Model model, HttpServletRequest request) {
        ActionUtil.requestToCondition(condition, request);
        String productID = condition.getStringCondition("productID");
        if (StringUtils.isNotEmpty(productID)) {
            model.addAttribute("productID", productID);
        }
        return forward("list");
    }


    //    @NoRight
    //    @SuppressWarnings("unchecked")
    //    @ResponseBody
    //    @RequestMapping("/ecrList")
    //    public String ecrList(PageCondition condition,HttpServletRequest request) throws Exception{
    //        ActionUtil.requestToCondition(condition, request);
    //
    //        String processId = request.getParameter("processId");
    //        if(StringUtils.isNotEmpty(processId)){
    //            List<KstarProductWorkFlow> kwList = productProcessService.getList(processId);
    //            int size  = kwList.size();
    //            if(size == 1){
    //                condition.getFilterObject().addCondition("id", "eq", kwList.get(0).getProductId());
    //            }
    //        }
    //        this.addFilterCondition(condition, "ecrCode", "like");
    //        this.addFilterCondition(condition, "ecrChangeContent", "like");
    //        IPage p = ecrService.query(condition);
    //        List<KstarEcrBean>  kebList =  (List<KstarEcrBean>) p.getList();
    //        for(KstarEcrBean kb:kebList){
    //            if(StringUtils.isNotEmpty(kb.getReferId())){
    //                KstarEcrBean temp = ecrService.get(kb.getReferId());
    //                if (temp != null){
    //                    kb.setReferIdGrid(temp.getEcrCode());
    //                }
    //            }
    //        }
    //        return sendSuccessMessage(p);
    //    }


    @NoRight
    @ResponseBody
    @RequestMapping("page")
    public String page(PageCondition condition, Model model, HttpServletRequest request) {
        ActionUtil.requestToCondition(condition, request);
        String productID = condition.getStringCondition("productID");
        if (StringUtils.isNotEmpty(productID)) {
            condition.getFilterObject().addCondition("productID", "eq", productID);
        }
        IPage page = ecrService.query(condition, getUserObject());
        return sendSuccessMessage(page);
    }



    @NoRight
    @RequestMapping("/edit")
    public String ecrEdit(String id, boolean statusEdit, boolean ableEdit, String productID, Model model) {
        KstarEcrBean ecr = null;
        if (StringUtils.isNotEmpty(id)) {
            ecr = ecrService.get(id);
        } else {
            ecr = new KstarEcrBean();
            String dateTime = prepareTime();
            String tempName = "ECR-" + dateTime + productSerialService.queryProductCBy("ECR", dateTime);
            ecr.setEcrCode(tempName);
            ecr.setProductID(productID);
            ecr.setApplyPerson(this.getUserObject().getEmployee().getName());
            ecr.setEcrStatus(ecr.getLovMember(ProcessConstants.PRODUCT_ECR_PROC, ProcessConstants.PROCESS_STATUS_01).getId());
        }
        model.addAttribute("ecr", ecr);
        TabMain tabMainInfo = new TabMain();
        model.addAttribute("tabMainInfo", tabMainInfo);
        model.addAttribute("statusEdit", statusEdit);
        model.addAttribute("ableEdit", ableEdit);

        if (StringUtils.isEmpty(ecr.getProductID())) {
            String changeId = ecr.getId() == null ? "" : ecr.getId();
            tabMainInfo.addTab("产品列表", "/product/ecrchange/productLine.html?changeId=" + changeId + "&statusEdit" + statusEdit);
        }

        if (StringUtils.isNotEmpty(ecr.getId()) && !Objects.equals(ecr.getEcrStatus(),"01")) {
            tabMainInfo.addTab("审批历史", "/standard/history.html?contrId=" + id);
        }

        if (StringUtils.isNotEmpty(ecr.getId()) && !Objects.equals(ecr.getEcrStatus(), "01")) {
            tabMainInfo.addTab("PDM审批历史", "/pdm/flow/history.html?no=" + ecr.getEcrCode() + "&type=" + "Ecrchange");
        }

        tabMainInfo.addTab("附件", "/common/attachment/attachment.html?businessId=" + id + "&businessType=KstarEcrBean&docGroupCode=");

        if (StringUtil.isNotEmpty(productID)) {
            tabMainInfo.setInitAll(false);
            return forward("edit");
        } else {
            tabMainInfo.setInitAll(true);
            return forward("editWithList");
        }
    }

    @NoRight
    @ResponseBody
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String ecrEditDo(KstarEcrBean ecr,String ecrChangeLines) {

        boolean isNew = StringUtils.isEmpty(ecr.getId());
//        if (isNew) {
//            ecr.setId(null);
//            ecr.setRecordInfor(false, getUserObject());
//        } else {
//            ecr.setRecordInfor(true, getUserObject());
//        }


        UserObject userObject = this.getUserObject();
        if (StringUtil.isNotEmpty(ecr.getProductID())) {
            if (isNew) {
                ecrService.save(ecr, userObject);
                productSerialService.ecrSave(ecr.getEcrCode(), userObject);
            } else {
                ecrService.update(ecr, userObject);
            }
        } else {
            if (StringUtils.isEmpty(ecrChangeLines)) {
                throw new AnneException("明细行不能为空");
            }
            List<EcrChangeLine> changeLines = JSON.parseArray(ecrChangeLines, EcrChangeLine.class);
            if (changeLines == null || changeLines.isEmpty()) {
                throw new AnneException("明细行不能为空");
            }
            if (isNew) {
                ecrService.save(ecr, changeLines, userObject);
                productSerialService.ecrSave(ecr.getEcrCode(), userObject);
            } else {
                ecrService.update(ecr, changeLines, userObject);
            }
        }

        return sendSuccessMessage();
    }

    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(String id) {

        ecrService.delete(id);
        return sendSuccessMessage();
    }

    @NoRight
    @RequestMapping("/productLine")
    public String productLines(String changeId, boolean statusEdit, Model model) throws Exception {
        outQueryCondition(model);
        model.addAttribute("changeId", changeId);
        model.addAttribute("statusEdit", statusEdit);
        return forward("productLine");
    }

    @NoRight
    @ResponseBody
    @RequestMapping("/productLinePage")
    public String productLinePage(PageCondition condition, HttpServletRequest request) throws Exception{
        ActionUtil.requestToCondition(condition, request);
        IPage p = ecrService.queryProductLines(condition, getUserObject());
        return sendSuccessMessage(p);
    }

    /**
     * 根据产品Id获取产品
     * @param productIds
     * @param model
     * @return
     */
    @NoRight
    @RequestMapping("/products")
    @ResponseBody
    public String products(@RequestParam("productIds[]") List<String> productIds, Model model) {
        List<Map<String, Object>> productList = ecrService.getProductList(productIds);
        return sendSuccessMessage(productList);
    }


    private void outQueryCondition(Model model){
        List<LovMember> crmCategoryLst = lovMemberService.getListByGroupCode("crmCategory");
        model.addAttribute("crmCategoryLst",crmCategoryLst);
        List<LovMember> erpCategoryLst = lovMemberService.getListByGroupCode("erpCategory");
        model.addAttribute("erpCategoryLst",erpCategoryLst);
        List<LovMember> publishStatus = lovMemberService.getListByGroupCode("PRODUCT_PUBLISH_PROC");
        model.addAttribute("publishStatus",publishStatus);
        List<LovMember> priceStatus = lovMemberService.getListByGroupCode("proPriceSpecify");
        model.addAttribute("priceStatus",priceStatus);
        List<LovMember> saleStatus = lovMemberService.getListByGroupCode("PM_PTS_PROC");
        model.addAttribute("saleStatus",saleStatus);
    }

    private String prepareTime() {

        //设置日期格式
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

        // new Date()为获取当前系统时间
        String reValue = df.format(new Date());

        return reValue;
    }

}
