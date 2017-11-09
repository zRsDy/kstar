package com.ibm.kstar.action.order.receipts;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.contract.IContractService;
import com.ibm.kstar.api.order.IContractReceiptDetailService;
import com.ibm.kstar.api.order.IOrderService;
import com.ibm.kstar.api.order.IReceiptsService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.contract.Contract;
import com.ibm.kstar.entity.order.ContractReceiptDetail;
import com.ibm.kstar.entity.order.ContractReceiptProduct;
import com.ibm.kstar.entity.order.Receipts;
import com.ibm.kstar.entity.order.vo.ReceiptsListVO;
import com.ibm.kstar.entity.quot.KstarPrjLst;
import com.ibm.kstar.interceptor.system.permission.NoRight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
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
import org.xsnake.web.utils.ExcelUtil;
import org.xsnake.web.utils.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/receipts")
public class ReceiptsAction extends BaseAction {
	@Autowired
	IReceiptsService receiptsService;
	@Autowired
	ILovMemberService lovMemberService;
	@Autowired
	IOrderService orderService;
	@Autowired
	IContractService contractService;
    @Autowired
    IContractReceiptDetailService receiptDetailService;

	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) throws UnsupportedEncodingException {
		setupQueryCondition(condition, request);
		IPage p = receiptsService.queryReceipts(condition, getUserObject());
		return sendSuccessMessage(p);
	}



	@RequestMapping("/list")
	public String list() {
		return forward("list");
	}
	
	/**
	 * 收款分配
	 * receiptsAllot:收款分配. <br/>  
	 * 
	 * @author liming 
	 * @return 
	 * @since JDK 1.7
	 */
	@RequestMapping("/receipts_allot")
	public String receiptsAllot(Model model) {
	    //默认只显示新建和退回状态的状态的数据
		model.addAttribute("onlyNewAndSendback", true);
		return forward("receipts_allot");
	}

	/**
	 *
	 * @param model
	 * @return
	 */
	@RequestMapping("/receiptsRecevice")
	public String receiptsRecevice(Model model) {
		model.addAttribute("type", "RECEVICE");
		return forward("receipts_allot");
	}
	/**
	 * 
	 * receiptsVerification:收款核销. <br/> 
	 * 
	 * @author liming 
	 * @return 
	 * @since JDK 1.7
	 */
	@RequestMapping("/receipts_verification")
	public String receiptsVerification(Model model) {
		UserObject userObject = getUserObject();
		model.addAttribute("salesmanId", userObject.getEmployee().getId());
		model.addAttribute("salesmanName", userObject.getEmployee().getName());
		String saleCenterCode = lovMemberService.getSaleCenter(userObject.getOrg().getId());
		LovMember lov = lovMemberService.getLovMemberByCode("ORG", saleCenterCode);
		if(lov != null){
			model.addAttribute("picSaleCenter", lov.getId());
		}
		TabMain tabMain = new TabMain();
		tabMain.setInitAll(true);
		tabMain.addTab("合同收款计划明细", "/receipts/contractReceiptList.html");
		tabMain.addTab("核销明细", "/receipts/verificationList.html");
		model.addAttribute("tabMain",tabMain);
		model.addAttribute("userObject",getUserObject());
		return forward("receipts_verification");
	}
	
	/**
	 * 
	 * contractReceiptList:合同收款计划明细. <br/> 
	 * 
	 * @author liming 
	 * @return 
	 * @since JDK 1.7
	 */
	@RequestMapping("/contractReceiptList")
	public String contractReceiptList(Model model) {
		UserObject userObject = getUserObject();
		model.addAttribute("salesmanId", userObject.getEmployee().getId());
		model.addAttribute("salesmanName", userObject.getEmployee().getName());
		return forward("contract_receipts_list");
	}
	
	/**
	 * 
	 * verificationList:核销明细列表. <br/> 
	 * 
	 * @author liming 
	 * @return 
	 * @since JDK 1.7
	 */
	@RequestMapping("/verificationList")
	public String verificationList(Model model) {
		UserObject userObject = getUserObject();
		model.addAttribute("salesmanId", userObject.getEmployee().getId());
		model.addAttribute("salesmanName", userObject.getEmployee().getName());

		//系统管控到款核销了不允许销售取消到款核核销关系。只能允许商务取消！
		boolean isBusiness = false;
		if (userObject.getPosition().getName().contains("商务")) {
			isBusiness = true;
		}

		model.addAttribute("isBusiness", isBusiness);
		return forward("verification_list");
	}
	/**
	 * 
	 * delete:删除收款单. <br/> 
	 * 
	 * @author liming 
	 * @param id
	 * @return 
	 * @since JDK 1.7
	 */
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(String id) {
		receiptsService.deleteReceipts(id);
		return sendSuccessMessage("删除成功！");
	}
	
	/**
	 * 
	 * cancel:取消收款单. <br/> 
	 * 
	 * @author liming 
	 * @param id
	 * @return 
	 * @since JDK 1.7
	 */
	@NoRight
	@ResponseBody
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	public String cancel(String id) {
		receiptsService.cancelReceipts(id, getUserObject());
		return sendSuccessMessage("删除成功！");
	}

	@RequestMapping("/add")
	public String add(Model model) {
		return forward("receipts");
	}

	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(Receipts receipts) {
		receiptsService.saveReceipts(receipts,getUserObject());
		return sendSuccessMessage();
	}



	@NoRight
	@RequestMapping("/edit")
	public String edit(String id,String op, Model model) {

		if (id == null) {
			throw new AnneException("没有找到需要修改的数据");
		}
		Receipts receipts = receiptsService.getReceipts(id);
		if (receipts == null) {
			throw new AnneException("没有找到需要修改的数据");
		}

		UserObject userObject = getUserObject();
		if(StringUtil.isEmpty(receipts.getSalesmanId())){
			receipts.setSalesmanId(userObject.getEmployee().getId());
			receipts.setSalesmanCode(userObject.getEmployee().getNo());
			receipts.setSalesmanName(userObject.getEmployee().getName());
			receipts.setSalesmanPost(userObject.getPosition().getId());
			//获取当前登录人员销售中心和销售部门
			if(StringUtil.isNotEmpty(userObject.getOrg().getId())){
				if(StringUtil.isEmpty(receipts.getSalesCenter())){
					LovMember salesmanCenter = orderService.getOrderSalesmanCenter(userObject.getOrg().getId());
					receipts.setSalesCenter(salesmanCenter == null ? null : salesmanCenter.getId());//营销中心
					if(salesmanCenter != null){
						LovMember salesmanDep = orderService.getOrderSalesmanDep(userObject.getOrg().getId(),salesmanCenter.getId());
						receipts.setBizDept(salesmanDep == null ? null : salesmanDep.getId());//销售部门
					}
				}
			}
		}

		String salesCenterId = receipts.getSalesCenter();
		if (StringUtil.isNotEmpty(salesCenterId)) {
			LovMember salesCenter = lovMemberService.get(salesCenterId);
			model.addAttribute("salesCenter", salesCenter);
		}
		String bizDeptId = receipts.getBizDept();
		if (StringUtil.isNotEmpty(bizDeptId)) {
			LovMember bizDept = lovMemberService.get(bizDeptId);
			model.addAttribute("bizDept", bizDept);
		}

		model.addAttribute("receipts", receipts);
		model.addAttribute("op", op);
		
		return forward("receipts_info");
	}
	@NoRight
	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(Receipts receipts) {
		receiptsService.updateReceipts(receipts,getUserObject());
		return sendSuccessMessage(receipts.getId());
	}

    @NoRight
    @ResponseBody
    @RequestMapping(value = "/getSalesmanCenter", method = RequestMethod.POST)
    public String edit(String salesmanId,String orgId) {
        if(StringUtil.isEmpty(salesmanId)){
            throw new AnneException("未选择销售人员,无法获取所属营销中心和业务部门");
        }
         if(StringUtil.isEmpty(orgId)){
            throw new AnneException("选择销售人员的所属组织为空,请重新销售人员");
        }

        LovMember salesmanCenter = receiptsService.getSaleCenter(orgId);
		LovMember salesmanDep = null;
		if (salesmanCenter != null) {
			salesmanDep = receiptsService.getBizDept(orgId, salesmanCenter.getId());
		}
		Map<String, Object> map = new HashMap<>();
        map.put("salesCenter", salesmanCenter);
        map.put("bizDept", salesmanDep);
        return sendSuccessMessage(map);
    }

	/**
	 * 
	 * getVerificationList:获取收款核销明细. <br/> 
	 * @author liming 
	 * @param condition
	 * @param request
	 * @return 
	 * @since JDK 1.7
	 */
	@NoRight
	@ResponseBody
	@RequestMapping("/getVerificationList")
	public String getVerificationList(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		
		String id = condition.getStringCondition("receId");
		if(id == null){
			id = "";
		}
//		UserObject userObject = getUserObject();
//		condition.getFilterObject().addCondition("salesmanId", "eq", userObject.getEmployee().getId());
		condition.getFilterObject().addCondition("receiptsId", "eq", id);
		
		IPage p = receiptsService.queryVerification(condition);
		return sendSuccessMessage(p);
	}
	
	/**
	 * 
	 * getContractReceiptList:获取合同收款计划明细. <br/> 
	 * @author liming 
	 * @param condition
	 * @param request
	 * @return 
	 * @since JDK 1.7
	 */
	@NoRight
	@ResponseBody
	@RequestMapping("/getContractReceiptList")
	public String getContractReceiptList(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		//condition.getFilterObject().addCondition("salesmanId", "eq", userObject.getEmployee().getId());
		String paymentCustId = condition.getStringCondition("paymentCustId");
		String correctCustId = condition.getStringCondition("correctCustId");
		
		if(StringUtil.isNotEmpty(correctCustId)){
			condition.getFilterObject().addCondition("custId", "eq", correctCustId == null || correctCustId =="null" ? "" : correctCustId );
		}else{
			condition.getFilterObject().addCondition("custId", "eq", paymentCustId == null || paymentCustId =="null" ? "" : paymentCustId );
		}
		String contractCode = condition.getStringCondition("contractCode");
		if(StringUtil.isNotEmpty(contractCode)){
			condition.getFilterObject().addCondition("contractCode", "eq", contractCode);
		}
		String contractCode_seach = condition.getStringCondition("contractCode_seach");
		if(StringUtil.isNotEmpty(contractCode_seach)){
			condition.getFilterObject().addCondition("contractCode", "eq", contractCode_seach);
		}
		
		IPage p = receiptsService.queryContractReceiptDetail(condition);
		return sendSuccessMessage(p);
	}

	/**
	 * 按客户进行核销
	 * @param receiptId
	 * @param request
	 * @return
	 */
	@NoRight
	@ResponseBody
	@RequestMapping("/verificationByCustomerVerify")
	public String verificationByCustomerVerify(String receiptsId, HttpServletRequest request) {
		Receipts receipts = receiptsService.getReceipts(receiptsId);
		if (receipts == null) {
			throw new AnneException("收款不存在");
		}
        receiptsService.receiptsVerificationByCustomInfo(receipts);
        return sendSuccessMessage();
	}

	/**
	 * 
	 * allotReceipts:(收款分配). <br/> 
	 * @author liming 
	 * @param receiptsList
	 * @return
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	@ResponseBody
	@RequestMapping(value = "/allotReceipts", method = RequestMethod.POST)
	public String allotReceipts(ReceiptsListVO receiptsList) throws Exception {
		receiptsService.saveOrUpdateReceiptsList(receiptsList,getUserObject());
		return sendSuccessMessage();
	}
	/**
	 * 
	 * claimReceipts:(收款核销). <br/> 
	 * @author liming 
	 * @param receiptsList
	 * @return
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	@ResponseBody
	@RequestMapping(value = "/verificationReceipts", method = RequestMethod.POST)
	public String verificationReceipts(ReceiptsListVO receiptsList , HttpServletRequest request) throws Exception {
		
		String receiptsId = request.getParameter("receiptsId");
		//收款核销
		receiptsService.receiptsVerification(receiptsId, receiptsList,getUserObject());
		return sendSuccessMessage();
	}
	/**
	 * 
	 * importReceipts:导入收款. <br/> 
	 * 
	 * @author liming 
	 * @param request
	 * @param response
	 * @return 
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	@ResponseBody
	@RequestMapping("/importReceipts")
	public String importReceipts(HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		List<List<Object>> dataList = this.getExcelData(request);
		receiptsService.importReceiptsList(dataList,getUserObject());
		return sendSuccessMessage("导入成功");
	}
	@NoRight
	@RequestMapping("/importTemplet")
	public void importReceiptsTemplet(HttpServletRequest request,HttpServletResponse response) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		List<List<Object>> dataList  = receiptsService.getReceiptsImportTemplet();
		ExcelUtil.exportExcel(response, dataList, "CRM收款导入模板_"+sdf.format(new Date()));
	}
	
	/**
	 * 
	 * exportReceipts:导出收款. <br/> 
	 * @author liming 
	 * @param request
	 * @param response
	 * @return 
	 * @since JDK 1.7
	 */
	@RequestMapping("/exportReceipts")
	public void exportReceipts(PageCondition condition, HttpServletRequest request,HttpServletResponse response){
		try{
			setupQueryCondition(condition, request);
			List<Receipts> receiptsList = receiptsService.getReceiptsList(condition);
			List<List<Object>> dataList  = receiptsService.getExcelData(receiptsList);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
			ExcelUtil.exportExcel(response, dataList, "CRM收款表_"+sdf.format(new Date()));
		}catch(Exception e){
			e.printStackTrace();
			throw new AnneException("导出失败");
		}
	}
	
	/**
	 * 
	 * publish:发布收款单. <br/>  
	 * 
	 * @author liming 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	@ResponseBody
	@RequestMapping("/publish")
	public String publish(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String ids = request.getParameter("ids");
		UserObject userObject = getUserObject();
		String[] receids = ids.split(",");
		for(String id : receids ){
			if(StringUtil.isNotEmpty(id)){
				receiptsService.publishReceipts(id,userObject);
			}
		}
		return sendSuccessMessage("操作成功");
	}
	/**
	 * 
	 * deleteVerification:删除核销明细. <br/> 
	 * 
	 * @author liming 
	 * @param id
	 * @return 
	 * @since JDK 1.7
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteVerification", method = RequestMethod.POST)
	public String deleteVerification(String id) {
		receiptsService.deleteVerification(id,getUserObject());
		return sendSuccessMessage("删除成功！");
	}
	/**
	 * 
	 * returnReceipt:业务将收款单退回商务. <br/> 
	 * 
	 * @author liming 
	 * @param id
	 * @return 
	 * @since JDK 1.7
	 */
	@NoRight
	@ResponseBody
	@RequestMapping(value = "/returnReceipt", method = RequestMethod.POST)
	public String returnReceipt(String id) {
		receiptsService.returnReceipt(id, getUserObject());
		return sendSuccessMessage("操作成功！");
	}
	
	/**
	 * 
	 * splitReceipts:收款单拆分. <br/> 
	 * 
	 * @author liming 
	 * @param id
	 * @param model
	 * @return 
	 * @since JDK 1.7
	 */
	@NoRight
	@RequestMapping("/splitReceipts")
	public String splitReceipts(String id,Model model){
		Receipts receipts = receiptsService.getReceipts(id);
		if(receipts != null){
			model.addAttribute("receipts", receipts);
		}
		return forward("receipts_split");
	}
	/**
	 * 
	 * splitReceiptsSave:收款单拆分保存. <br/> 
	 * @author liming 
	 * @param request
	 * @param response
	 * @return 
	 * @since JDK 1.7
	 */
	@NoRight
	@ResponseBody
	@RequestMapping(value = "/splitReceiptsSave", method = RequestMethod.POST)
	public String splitReceiptsSave(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String id = request.getParameter("id");
		String splitContractCode = request.getParameter("splitContractCode");
		String splitAmount =request.getParameter("splitAmount");
		BigDecimal amount = splitAmount == null ? new BigDecimal(0) : new BigDecimal(splitAmount);
		receiptsService.splitReceiptsSave(id, amount, splitContractCode,getUserObject());
		return sendSuccessMessage("操作成功！");
	}
	
	
	/**
	 * 核销合同明细
	 * @param id
	 * @param contractId
	 * @param model
	 * @return
	 */
	@NoRight
	@RequestMapping("/contractReceipts")
	public String contractReceipts(String id,String receiptsId,String contractId,Model model) {
		
		TabMain tabMain = new TabMain();
		tabMain.setInitAll(true);
		
		tabMain.addTab("附件", "/common/attachment/attachment.html?businessId="+id+"&businessType=ORDER&docGroupCode=FileTypeCode");
		
		model.addAttribute("tabMain",tabMain);
		model.addAttribute("id",id);
		model.addAttribute("contractId",contractId);
		model.addAttribute("receiptsId",receiptsId);
		
		return forward("contract_receipts");
	}
	
	/**
	 * 核销合同明细
	 * @param id
	 * @param contractId
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@NoRight
	@ResponseBody
	@RequestMapping(value = "/contractReceiptsItem")
	public String contractReceipts(String id,String contractId,HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		PageCondition condition = new PageCondition();
		condition.setCondition("contrId", contractId);
		IPage p = contractService.queryPrjLst(condition);
		
		List<ContractReceiptProduct> products = receiptsService.getContractReceiptProduct(id);
		
		List<KstarPrjLst> list = (List<KstarPrjLst>) p.getList();
		
		for (KstarPrjLst kstarPrjLst : list) {
			for (ContractReceiptProduct product : products) {
				if (kstarPrjLst.getId().equals(product.getContrIdLineId())) {
					kstarPrjLst.setItemId(product.getId());
					kstarPrjLst.setQuantity(product.getQuantity());
					kstarPrjLst.setNotes(product.getNotes());
					kstarPrjLst.setQuantityB(new BigDecimal(kstarPrjLst.getAmt()).subtract(product.getQuantity()));
				} 
			}
		}
		
		return sendSuccessMessage(p);
	}
	
	
	
	@NoRight
	@ResponseBody
	@RequestMapping(value = "/contractReceiptsItem" , method = RequestMethod.POST)
	public String contractReceiptsItemPost(@RequestBody ContractReceiptDetail detail,String id,String contractId,String receiptsId,HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		receiptsService.saveOrUpdateContractReceiptProduct(detail,receiptsId);
		
		return sendSuccessMessage();
	}
	
	/**
	 * 是否借货合同
	 * @param contractId
	 * @return
	 * @throws Exception
	 */
	@NoRight
	@ResponseBody
	@RequestMapping(value = "/isLoan" , method = RequestMethod.POST)
	public String isLoan(String contractId) throws Exception {
		LovMember typeLov = lovMemberService.getLovMemberByCode("CONTRACTTYPE", IConstants.CONTR_LOAN_0101);
		Contract contract = contractService.get(contractId);
		
		if (!contract.getContrType().equals(typeLov.getId())) {
			throw new AnneException("借货合同才能核销明细");
		}
		
		return sendSuccessMessage();
	}


	/**
	 * 修复回款计划中业务实体为空的数据,取出货单中业务实体。
	 */
	@RequestMapping("/repairBusinessEntity")
    @ResponseBody
	public String repairBusinessEntityOfReceiptDetail() {
		this.receiptDetailService.repairBusinessEntityOfReceiptDetail();
        return sendSuccessMessage();
    }

	/**
	 * 修复回款计划中销售人员为空的数据,取出货单第一行等订单等销售员。
	 */
	@RequestMapping("/repairSaleman")
	@ResponseBody
	public String repairSalemanOfReceiptDetail() {
		this.receiptDetailService.repairSalemanOfReceiptDetail();
		return sendSuccessMessage();
	}

	/**
	 * 修复回款计划中销售团队
	 */
	@RequestMapping("/repairTeam")
	@ResponseBody
	public String repairTeamOfReceiptDetail() {
		this.receiptDetailService.repairTeamOfReceipt();
		return sendSuccessMessage();
	}



	private void setupQueryCondition(PageCondition condition, HttpServletRequest request) throws UnsupportedEncodingException {
		ActionUtil.requestToCondition(condition, request);
		String paymentCustomerName = condition.getStringCondition("paymentCustomerName");
		String businessCode = condition.getStringCondition("businessCode");
		String currency = condition.getStringCondition("currency");
		String paymentCustomerId = condition.getStringCondition("paymentCustomerId");
		String salesmanId = condition.getStringCondition("salesmanId");
		String erpCust = condition.getStringCondition("erpCust");
		String notVerification = condition.getStringCondition("notVerification");
		String status = condition.getStringCondition("status");
		String picSaleCenter = condition.getStringCondition("picSaleCenter");
		String respDept = condition.getStringCondition("respDept");
		String createdAt_start = condition.getStringCondition("createdAt_start");
		String createdAt_end = condition.getStringCondition("createdAt_end");
		String erpImp = condition.getStringCondition("erpImp");
		String receiptsCode = condition.getStringCondition("receiptsCode");
		String salesCenter = condition.getStringCondition("salesCenter");
		String bizDept = condition.getStringCondition("bizDept");
		String receiptsDate_start = condition.getStringCondition("receiptsDate_start");
		String receiptsDate_end = condition.getStringCondition("receiptsDate_end");
//		String correctCustName = condition.getStringCondition("correctCustName");
		String isAdvancesReceived = condition.getStringCondition("isAdvancesReceived");
//		
//		if(StringUtil.isNotEmpty(correctCustName)){
//			correctCustName = new String(correctCustName.getBytes("iso-8859-1"),"utf-8");
//			condition.getFilterObject().addCondition("correctCustName", "like", "%"+correctCustName+"%");
//		}
		if(StringUtil.isNotEmpty(paymentCustomerName)){
//			paymentCustomerName = new String(paymentCustomerName.getBytes("iso-8859-1"),"utf-8");
			condition.getFilterObject().addOrCondition("paymentCustomerName", "like", "%"+paymentCustomerName+"%");
			condition.getFilterObject().addOrCondition("correctCustName", "like", "%"+paymentCustomerName+"%");
		}
		if(StringUtil.isNotEmpty(businessCode)){
			condition.getFilterObject().addCondition("businessCode", "eq", businessCode);
		}
		if(StringUtil.isNotEmpty(currency)){
			condition.getFilterObject().addCondition("currency", "eq", currency);
		}
		if(StringUtil.isNotEmpty(paymentCustomerId)){
			condition.getFilterObject().addCondition("paymentCustomerId", "eq", paymentCustomerId);
		}
		String erpCustTurn = "";
		if(StringUtil.isNotEmpty(erpCust) && IConstants.YES.equals(erpCust)){
			erpCustTurn = "No";
			condition.getFilterObject().addCondition("erpCust", "eq", erpCustTurn);
		}
		if(StringUtil.isNotEmpty(notVerification) && notVerification.equals("1") ){
			condition.getFilterObject().addCondition("status", "<",IConstants.RECEIPT_STATUS_40 );
		}
		if(StringUtil.isNotEmpty(status)){
			condition.getFilterObject().addCondition("status", "eq", status);
		}

		if(StringUtil.isNotEmpty(receiptsDate_start)){
			condition.getFilterObject().addCondition("receiptsDate", ">=", receiptsDate_start);
		}
		if(StringUtil.isNotEmpty(receiptsDate_end)){
			condition.getFilterObject().addCondition("receiptsDate", "<=", receiptsDate_end);
		}

		if(StringUtil.isNotEmpty(createdAt_start)){
			condition.getFilterObject().addCondition("createdAt", ">=", createdAt_start);
		}
		if(StringUtil.isNotEmpty(createdAt_end)){
			condition.getFilterObject().addCondition("createdAt", "<=", createdAt_end);
		}
		if(StringUtil.isNotEmpty(erpImp)){
			condition.getFilterObject().addCondition("erpImp", "eq", erpImp);
		}
		if(StringUtil.isNotEmpty(receiptsCode)){
			condition.getFilterObject().addCondition("receiptsCode", "eq", receiptsCode);
		}
		if(StringUtil.isNotEmpty(salesCenter)){
			condition.getFilterObject().addCondition("salesCenter", "eq", salesCenter);
		}
		if(StringUtil.isNotEmpty(bizDept)){
			condition.getFilterObject().addCondition("bizDept", "eq", bizDept);
		}
		
		if(StringUtil.isNotEmpty(isAdvancesReceived)){
			condition.getFilterObject().addCondition("isAdvancesReceived", "eq", isAdvancesReceived);
		}

		//收款核销列表不能查询新建收款计划
		String op = condition.getStringCondition("op");
		if("verification".equals(op)){
			condition.getFilterObject().addCondition("status", "!=", IConstants.RECEIPT_STATUS_R10);
			condition.getFilterObject().addCondition("status", "!=", IConstants.RECEIPT_STATUS_10);
			if(StringUtil.isNotEmpty(picSaleCenter)){				
				condition.getFilterObject().addCondition("picSaleCenter",  "eq" , picSaleCenter);
			}
			if(StringUtil.isNotEmpty(salesmanId)){
				condition.getFilterObject().addOrCondition("salesmanId", "eq", salesmanId);
			}

//			if(StringUtil.isNotEmpty(picSaleCenter)){
//				condition.getFilterObject().addOrCondition("salesmanId", "=", null);
//			}
		}else{
			if(StringUtil.isNotEmpty(salesmanId)){
				condition.getFilterObject().addCondition("salesmanId", "eq", salesmanId);
			}
			String saleCenterCode = lovMemberService.getSaleCenter(getUserObject().getOrg().getId());
			LovMember lov = lovMemberService.getLovMemberByCode("ORG", saleCenterCode);
			if(lov != null){
				condition.getFilterObject().addCondition("picSaleCenter",  "eq" , lov.getId());
			}
			if(StringUtil.isNotEmpty(picSaleCenter)){
				condition.getFilterObject().addCondition("picSaleCenter",  "eq" , picSaleCenter);
			}
			if(StringUtil.isNotEmpty(respDept)){
				condition.getFilterObject().addCondition("respDept",  "eq" , respDept);
			}
		}
	}
	
	@RequestMapping("/mappageExport")
	public void mappageExport(PageCondition condition,HttpServletRequest request,HttpServletResponse response){
		ActionUtil.requestToCondition(condition, request);
		List<List<Object>> dataList = receiptsService.mappageExport(condition,getUserObject());
		SimpleDateFormat sdf = new SimpleDateFormat(IConstants.YMDHMS_FORMAT_1);
		ExcelUtil.exportExcel(response, dataList, "收款核销列表-"+sdf.format(new Date()));
	}
}