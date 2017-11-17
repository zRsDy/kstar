package com.ibm.kstar.action.order.receipts.plan;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.page.PageImpl;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.ExcelUtil;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.api.order.IContractReceiptDetailService;
import com.ibm.kstar.api.order.IReceiptsService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.order.ContractReceiptDetail;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/receipts/plan")
public class ReceiptsPlanAction extends BaseAction {
	@Autowired
	IContractReceiptDetailService contractReceiptDetailService;
	@Autowired
	IReceiptsService receiptsService;
	@Autowired
	ILovMemberService lovMemberService;
	
	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		
		String custId = condition.getStringCondition("custId");
		if(StringUtil.isNotEmpty(custId)){
			condition.getFilterObject().addCondition("custId", "eq", custId);
		}
		String contractCode = condition.getStringCondition("contractCode");
		if(StringUtil.isNotEmpty(contractCode)){
			condition.getFilterObject().addCondition("contractCode", "like", "%"+contractCode+"%");
		}
		String receiptsPlan = condition.getStringCondition("receiptsPlan");
		if(StringUtil.isNotEmpty(receiptsPlan)){
			condition.getFilterObject().addCondition("receiptsPlan", "eq", receiptsPlan);
		}
		String receiptsStage = condition.getStringCondition("receiptsStage");
		if(StringUtil.isNotEmpty(receiptsStage)){
			condition.getFilterObject().addCondition("receiptsStage", "eq", receiptsStage);
		}
		String deliveryCode = condition.getStringCondition("deliveryCode");
		if(StringUtil.isNotEmpty(deliveryCode)){
			condition.getFilterObject().addCondition("deliveryCode", "like", "%"+deliveryCode+"%");
		}
		String salesmanId = condition.getStringCondition("salesmanId");
		if(StringUtil.isNotEmpty(salesmanId)){
			condition.getFilterObject().addCondition("salesmanId", "eq", salesmanId);
		}
		String receiptsType = condition.getStringCondition("receiptsType");
		if(StringUtil.isNotEmpty(receiptsType)){
			condition.getFilterObject().addCondition("receiptsType", "eq", receiptsType);
		}
		String bizDept = condition.getStringCondition("bizDept");
		if(StringUtil.isNotEmpty(bizDept)){
			condition.getFilterObject().addCondition("bizDept", "eq", bizDept);
		}
		String veriAmountMin = condition.getStringCondition("veriAmountMin");
		if(StringUtil.isNotEmpty(veriAmountMin) && StringUtils.isNumeric(veriAmountMin) ){
			condition.getFilterObject().addCondition("veriAmount", ">=", veriAmountMin);
		}
		String veriAmountMax = condition.getStringCondition("veriAmountMax");
		if(StringUtil.isNotEmpty(veriAmountMax) && StringUtils.isNumeric(veriAmountMax) ){
			condition.getFilterObject().addCondition("veriAmount", "<=", veriAmountMax);
		}
		
		String planAmountMin = condition.getStringCondition("planAmountMin");
		if(StringUtil.isNotEmpty(planAmountMin) && StringUtils.isNumeric(planAmountMin) ){
			condition.getFilterObject().addCondition("planAmount", ">=", planAmountMin);
		}
		String planAmountMax = condition.getStringCondition("planAmountMax");
		if(StringUtil.isNotEmpty(planAmountMax) && StringUtils.isNumeric(planAmountMax) ){
			condition.getFilterObject().addCondition("planAmount", "<=", veriAmountMax);
		}
		String salesCenter = condition.getStringCondition("salesCenter");
		if(StringUtil.isNotEmpty(salesCenter)){
			condition.getFilterObject().addCondition("salesCenter", "eq", salesCenter);
		}
		String businessEntity = condition.getStringCondition("businessEntity");
		if(StringUtil.isNotEmpty(businessEntity)){
			condition.getFilterObject().addCondition("businessEntity", "eq", businessEntity);
		}
		
		IPage p = contractReceiptDetailService.queryContractReceiptDetails(condition);
		List<ContractReceiptDetail> contractReceiptDetailList = (List<ContractReceiptDetail>)p.getList();
		contractReceiptDetailService.searchGatheringDateAndCheckDate(contractReceiptDetailList);
		int count = p.getCount();
		int page = condition.getPage();
		int rows = condition.getRows();
		PageImpl pageImpl = new PageImpl(contractReceiptDetailList,page,rows,count);
		return sendSuccessMessage(pageImpl);
	}

	@RequestMapping("/list")
	public String list() {
		return forward("receipts_plan_list");
	}

	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(String id) {
		contractReceiptDetailService.deleteContractReceiptDetail(id);
		return sendSuccessMessage();
	}

	@RequestMapping("/add")
	public String add(Model model) {
		return forward("receipts_plan_info");
	}

	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(ContractReceiptDetail contractReceiptDetail) {
		contractReceiptDetailService
				.saveContractReceiptDetail(contractReceiptDetail,getUserObject());
		return sendSuccessMessage();
	}

	@RequestMapping("/edit")
	public String edit(String id, Model model) {
		if (id == null) {
			throw new AnneException("没有找到需要修改的数据");
		}
		ContractReceiptDetail contractReceiptDetail = contractReceiptDetailService
				.getContractReceiptDetail(id);
		if (contractReceiptDetail == null) {
			throw new AnneException("没有找到需要修改的数据");
		}
		LovMember lovPos = lovMemberService.getLovMemberByPositionId("POSITION",contractReceiptDetail.getSalesmanPos());
		if(lovPos != null){
			contractReceiptDetail.setSalesmanPosName(lovPos.getName());
		}else{
			contractReceiptDetail.setSalesmanPosName("");
		}
		model.addAttribute("contractReceiptDetail", contractReceiptDetail);
		return forward("receipts_plan_info");
	}

	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(ContractReceiptDetail contractReceiptDetail) {
		contractReceiptDetailService.updateContractReceiptDetail(contractReceiptDetail);
		return sendSuccessMessage();
	}
	@NoRight
	@ResponseBody
	@RequestMapping(value = "/importReceiptsPlan", method = RequestMethod.POST)
	public String importReceiptsPlan(HttpServletRequest request,HttpServletResponse response) throws Exception{
		List<List<Object>> dataList = this.getExcelData(request);
		receiptsService.importReceiptPlanList(dataList,getUserObject());
		return sendSuccessMessage("导入成功");
	}
	@NoRight
	@RequestMapping("/importTemplet")
	public void importReceiptsTemplet(HttpServletRequest request,HttpServletResponse response) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		List<List<Object>> dataList  = receiptsService.getReceiptPlanImportTemplet();
		ExcelUtil.exportExcel(response, dataList, "CRM收款导入模板_"+sdf.format(new Date()));
	}
	
	/**
	 * 
	 * 回款计划导出
	 */
	@NoRight
	@RequestMapping(value = "/receiptePlanListExcel")
	public void loanPrjlstExport(PageCondition condition, HttpServletRequest request,HttpServletResponse response) throws Exception{
		ActionUtil.requestToCondition(condition, request);

        ActionUtil.doSearch(condition);

        String idsStr = request.getParameter("idsStr");
        String typ = request.getParameter("typ");
        String[] ids = idsStr.split(",");
		List<List<Object>> dataList  = receiptsService.exportSelectedReceiptLists(condition, getUserObject(), ids, typ);
		ExcelUtil.exportExcel(response, dataList, "回款计划");
	}
	
	/**
	 * 根据合同编号或出货申请单编号校验是否已有回款计划
	 * @param contrNo，deliveryCode
	 * @return
	 */
	@NoRight
	@ResponseBody
	@RequestMapping(value = "/checkedReceiptsPlan")
	public String checkedReceiptsPlan(String contrNo,String deliveryCode){
		if(StringUtils.isNotEmpty(contrNo)||StringUtils.isNotEmpty(deliveryCode)){
			List<ContractReceiptDetail> list = receiptsService.checkedReceiptsPlan(contrNo,deliveryCode);
			if(list != null && list.size() > 0){
				return sendSuccessMessage(false);
			}else {				
				return sendSuccessMessage(true);
			}
		}
		return sendSuccessMessage(true);
	}
	
}