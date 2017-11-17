/**
 * 
 */
package com.ibm.kstar.action.contract.loan;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.bizopp.IBizoppService;
import com.ibm.kstar.api.contract.IContrChangeService;
import com.ibm.kstar.api.contract.IContractLoanService;
import com.ibm.kstar.api.contract.IContractService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.price.IPriceHeadService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.EmployeeObject;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.entity.bizopp.BusinessOpportunity;
import com.ibm.kstar.entity.contract.ContrChange;
import com.ibm.kstar.entity.contract.Contract;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.product.ProductPriceHead;
import com.ibm.kstar.interceptor.system.permission.NoRight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.ExcelUtil;
import org.xsnake.web.utils.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ibm
 *
 */
@Controller
@RequestMapping("/loan")
//@Scope("prototype")
public class ContractLoanAction extends BaseAction {
	Logger logger = LoggerFactory.getLogger(getClass());

	private final static String CONTRACT_PROCESS_KEY = "contrLoanBasic";
 
	@Autowired
	private ILovMemberService lovMemberService;	
 
	@Autowired
	private IContractLoanService contractLoanService;

	@Autowired
	private ICustomInfoService customerService;
	
	@Autowired
	private IBizoppService bizOppService;

	@Autowired
	private IPriceHeadService priceHeadService;

	@Autowired
	private IContractService contractService;
	@Autowired
	private IContrChangeService  changeService ;

	@RequestMapping("/contractloan")
	public String index(String id, Model model) {
		return forward("contractloan");
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);

//		LovMember typeLov = lovMemberService.getLovMemberByCode("CONTRACTTYPE", "LOAN0101");
//		condition.getFilterObject().addCondition("contrType", "=", typeLov.getId());
//		String searchKey = condition.getStringCondition("searchKey");
//		condition.getFilterObject().addCondition("deleteFlag", "eq", "N");
//		if(searchKey !=null){
//			condition.getFilterObject().addOrCondition("contrNo", "like", "%"+searchKey+"%");
//			condition.getFilterObject().addOrCondition("contrName", "like", "%"+searchKey+"%");
//		}

        ActionUtil.doSearch(condition);
		IPage p = contractLoanService.query(condition);
		List<Contract> contractlist = (List<Contract>)p.getList();
		List<Contract> list = contractLoanService.countTolRecdAmt(contractlist);
		PageImpl page = new PageImpl(list,condition.getPage(),condition.getRows(),p.getCount());
		return sendSuccessMessage(page);
	}

	
	@NoRight
	@RequestMapping("/tabs")
	public String tabs(String id, String contrCode, String typ, Model model) {
		model.addAttribute("id", id);
		model.addAttribute("typ", typ);
		model.addAttribute("contrCode", contrCode);
		Contract contract = contractLoanService.get(id); 
		String ContrNo = contract.getContrNo();
		BusinessOpportunity biz= bizOppService.getBizOppEntity(contract.getProjNo());
		CustomInfo cust = customerService.getCustomInfo(contract.getCustCode()); 
		CustomInfo erpCust = customerService.getCustomInfo(contract.getErpOrderCustId());
		ProductPriceHead priceTable = priceHeadService.queryLpcById(contract.getPricNo());
		model.addAttribute("contr", contract);
		model.addAttribute("org",JSON.toJSONString(lovMemberService.get(contract.getOrg())));
		
		Employee ordEmp =  ((Employee)CacheData.getInstance().get(contract.getOrderer()));
		LovMember ordPosi = lovMemberService.get(contract.getOrderPosId());
		LovMember ordOrg = lovMemberService.get(contract.getOrderOrgId());
		if(ordEmp !=null && ordPosi!=null  && ordOrg!=null){
			EmployeeObject teamObj = new EmployeeObject(ordOrg,ordPosi, ordEmp);
			if(teamObj!=null){
			model.addAttribute("teamObj",JSON.toJSONString(teamObj));
			}
		}

		if(biz!=null){
		model.addAttribute("project",JSON.toJSONString(biz));
		}
		if(cust!=null){
		model.addAttribute("customer",JSON.toJSONString(cust));
		}
		if(priceTable!=null){
		model.addAttribute("priceTable",JSON.toJSONString(priceTable));
		}
		if(erpCust!=null){
		model.addAttribute("erpCustomer",JSON.toJSONString(erpCust));
		}

		//判断合同状态是否返回修改中，是否是当前处理人
		String conSt= lovMemberService.get(contract.getContrStat()).getCode();
		String constEditFlag = "Y";
		if(conSt.equals("06")) {
			constEditFlag = contractService.checkContrStat(id, getUserObject());
		}else {
			constEditFlag = "N";
		}
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("constEditFlag", constEditFlag);
		getButtonStatus(map,contract);
		model.addAttribute("btnStatus",map);
		// 表单按钮状态控制
		String editFlag="N";
		if(conSt.equalsIgnoreCase("01") || "Y".equals(constEditFlag)  || (hasPermission("P05ConCONTRACT_PRO_ENABLE_IN_APP_METHOD") && (conSt.equalsIgnoreCase("02") || conSt.equalsIgnoreCase("03") || conSt.equalsIgnoreCase("04")))){  //01	新建 	06	返回修改中
			editFlag="Y";
		}

		// 工程清单隐藏报价，单品总金额 列
		String showPriceFlg="N";
		if(hasPermission("P05ConPRICE_COLUMN_ENABLE_METHOD")){
			showPriceFlg="Y";
		}
		// 工程清单隐藏 物料号 列
		String showMaterCodeFlg="N";
		if(hasPermission("P05ConMaterialCode_COLUMN_ENABLE_METHOD")){
			showMaterCodeFlg="Y";
		}
		// 回款计划表单按钮状态控制
		String payPlanEditFlag="N";
		if(conSt.equalsIgnoreCase("01") ||"Y".equals(constEditFlag)){  //01	新建 	06	返回修改中
			payPlanEditFlag="Y";
		}else if ( (conSt.equalsIgnoreCase("12") || conSt.equalsIgnoreCase("13")) && hasPermission("P05Con_PaymentEdit_AfterSign")){
			payPlanEditFlag="Y";
		}
		String pricetableid = contract.getPricNo();
		TabMain tabMain = new TabMain();
		tabMain.setInitAll(false); 
		if(hasPermission("P05LoanT1ProjListTab")){
		tabMain.addTab("工程清单", "/loan/prjlst.html?contrId="+id+"&typ="+typ+"&pricetableid="+pricetableid+"&editFlag="+editFlag+"&showPriceFlg="+showPriceFlg+"&showMaterCodeFlg="+showMaterCodeFlg); 
//		tabMain.addTab("附件", "/loan/att.html?contrId="+id);   
		}
		if(hasPermission("P05LoanT2FileTab")){
		tabMain.addTab("附件", "/common/attachment/attachment.html?businessId="+id+"&businessType=CONTR_LOAN&docGroupCode=CONTRACTDOCTYPE");
//		tabMain.addTab("发货地址", "/loan/addr.html?contrId="+id);
		}
		if(hasPermission("P05LoanT3ShipAddressTab")){
		tabMain.addTab("发货地址", "/standard/addr.html?contrId="+id+"&typ="+typ+"&editFlag="+editFlag); 
//		tabMain.addTab("申请状态", "/loan/history.html?contrId="+id);  
		}
		if(hasPermission("P05LoanT4TeamPosTab")){
		tabMain.addTab("团队成员", "/team/list.html?businessType=CONTR_LOAN&businessId="+id);
		}
		if(hasPermission("P05LoanT5OrgListTab")){
		tabMain.addTab("组织列表", "/orgTeam/list.html?businessType=CONTR_LOAN&businessId=" + id);
		}
		if(hasPermission("P05LoanT8PaymentTab")){
			tabMain.addTab("回款规则", "/standard/pay.html?contrId="+id+"&payPlanEditFlag="+payPlanEditFlag);
		}
		if(hasPermission("P05LoanT6ReviewHistory")){
		tabMain.addTab("审批历史", "/standard/history.html?contrId="+id+"&editFlag="+editFlag);  
		}
		if(hasPermission("P05LoanT7ConChangeTab")){
		tabMain.addTab("合同变更", "/standard/changePage.html?contrId="+id);
		}
		if(hasPermission("P05LoanT7ConOrderTab")){
		tabMain.addTab("订单", "/order/list.html?contrId="+id);
		}
		if(hasPermission("P05LoanT7ConOrderTab")){//2017年8月8日ZSY
		tabMain.addTab("无合同核销申请", "/loan/eliminate/list.html?contrId="+id+"&typ="+typ+"&pricetableid="+pricetableid+"&ContrNo="+ContrNo);
		}
		model.addAttribute("tabMain",tabMain);

		model.addAttribute("sourceType", IConstants.ORDER_SOURCE_CONTRACT);
		return forward("tabs");
	}


	public void getButtonStatus(Map<String,String> map,Contract contr){
		/*CONTRACTSTATUS ---合同状态						CONTRACTREVIEWSTATUS  ---合同评审状态	
 		01	新建                          											02	审批中                            
		02	待初审                        										03	已审批                            
		03	待评审                        										01	未启动                            
		04	待合同书评审                  										04	已驳回                         
		05	待签订                  
		06	返回修改中              
		07	已签订待商务下单        
		08	已签订商务已下单        
		09	已废弃  */
		String conSt= lovMemberService.get(contr.getContrStat()).getCode();
		String trialSt= lovMemberService.get(contr.getTrialStat()).getCode();
//		String revSt= lovMemberService.get(contr.getReviewStat()).getCode();
//		String finSt= lovMemberService.get(contr.getFinalReviewStat()).getCode();
		String constEditFlag = map.get("constEditFlag");
		String isValid = contr.getIsValid();

		if(isValid != null && isValid.equalsIgnoreCase("0")){
			map.put("saveBtn", "0");
			map.put("trialBtn", "0");
			map.put("finBtn", "0");	
			map.put("signBtn", "0");
			map.put("reviseBtn", "0");
			map.put("chgBtn", "0");	
			map.put("genOrderBtn", "0");
			map.put("disContrBtn", "0");
			
		}else{

			if((conSt.equalsIgnoreCase("01") && trialSt.equalsIgnoreCase("01")) ||"Y".equals(constEditFlag) ){
				map.put("saveBtn", "1");
			}else{
				map.put("saveBtn", "0");
			}
			if((conSt.equalsIgnoreCase("01") && trialSt.equalsIgnoreCase("01")) ){
				map.put("trialBtn", "1");
			}else{
				map.put("trialBtn", "0");
			}
			if(conSt.equalsIgnoreCase("12")|| conSt.equalsIgnoreCase("13")){
				map.put("chgBtn", "1");				
			}else{
				map.put("chgBtn", "0");			
			}
			if(conSt.equalsIgnoreCase("12")|| conSt.equalsIgnoreCase("13")){
				map.put("genOrderBtn", "1");				
			}else{
				map.put("genOrderBtn", "0");			
			}
			if((conSt.equalsIgnoreCase("01")|| "Y".equals(constEditFlag))){
				map.put("disContrBtn", "1");				
			}else{
				map.put("disContrBtn", "0");			
			}
		}
		
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value = "/tabs", method = RequestMethod.POST)
	public String editContr(Contract contract,HttpServletRequest request) { 
		CustomInfo cust = customerService.getCustomInfo(contract.getErpOrderCustId()); 
		if(cust.getErpCode()!=null) {
	   		 if(cust.getErpCode().indexOf("KSTAR")!=-1){
	   			 throw new AnneException("未通过ERP审核的客户不允许下单！");
	   		 }
		}
		String func = request.getParameter("actFunction");
		String typ = request.getParameter("typ");
		 if(func.equalsIgnoreCase("saveOkc")){
			 updateContr(contract);
		 }else if(func.equalsIgnoreCase("startTrialProcess")){
			 startContrTrialProcess(typ, contract);
		 }
		return sendSuccessMessage();
	}

	public String updateContr(Contract contract) { 
		updtContract(contract);
		return sendSuccessMessage();
	}
	
	// version 1.0
//	@RequestMapping("/add")
//	public String add(Model model) {
////		String dep="X";
//		String contrNo = contractLoanService.getContractLoanNumber(); 
//		Contract contract = new Contract();
//		contract.setContrNo(contrNo);
//		contract.setContrVer("1");
//		contract.setCreator(getUserObject().getEmployee().getId());
////		LovMember lov =  ((LovMember)CacheData.getInstance().get(payStat));
//		LovMember lov = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "01");
////		contract.setReviewStat(lov.getId());
//		contract.setTrialStat(lov.getId());
////		contract.setFinalReviewStat(lov.getId());
//		LovMember statLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "01");
//		contract.setContrStat(statLov.getId());
////		contract.setOrg(getUserObject().getOrg().getId());
//		LovMember payLov = lovMemberService.getLovMemberByCode("CONTRACTPAYSTAT", "01");
//		contract.setPayStat(payLov.getId());
//		LovMember contrTypeLov = lovMemberService.getLovMemberByCode("CONTRACTTYPE", IConstants.CONTR_LOAN_0101); // 借货合同申请
//		contract.setContrType(contrTypeLov.getId());
//		contract.setIsValid("1");
////		Date now = new Date();
////		SimpleDateFormat dtFmt=new SimpleDateFormat("yyyy-MM-dd");
//		contract.setSubmitTime(new Date());
//		contract.setCreateTime(new Date());
//		LovMember entityLov = lovMemberService.getLovMemberByCode("OPERATION_UNIT", "101");
//		contract.setBussEnity(entityLov.getId());
//		LovMember orgLov = lovMemberService.get(getUserObject().getOrg().getId());
//		contract.setOrg(orgLov.getId()); 
//		LovMember makDepLov = lovMemberService.getLovMemberByCode("ORG", lovMemberService.getSaleCenter(getUserObject().getOrg().getId()));
//		if(makDepLov !=null){
//		contract.setMarketDept(makDepLov.getId());
//		}else{
//			throw new AnneException("未找您所在的营销中心，请联系系统管理员");			
//		}
//		model.addAttribute("contr", contract);
////		model.addAttribute("org",JSON.toJSONString(lovMemberService.get(getUserObject().getOrg().getId())));
////		model.addAttribute("project",JSON.toJSONString(lovMemberService.get(contract.getProjNo())));
//		
//		return forward("loan");
//	}
	
	
	@RequestMapping("/add")
	public String add(String contrId,Model model) {

		UserObject userObject = getUserObject();
		
		Contract contract = new Contract();
		
		LovMember makDepLov = lovMemberService.getLovMemberByCode("ORG", lovMemberService.getSaleCenter(userObject.getOrg().getId()));
		if(makDepLov !=null){
			contract.setMarketDept(makDepLov.getId());
		}else{
			throw new AnneException("未找您所在的营销中心，请联系系统管理员");			
		}
		
		contract.setContrVer("1");
		contract.setIsValid("1");
		
		contract.setCreator(userObject.getEmployee().getId());
		
		LovMember lov = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "01");
		contract.setTrialStat(lov.getId());

		LovMember statLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "01");
		contract.setContrStat(statLov.getId());

		LovMember payLov = lovMemberService.getLovMemberByCode("CONTRACTPAYSTAT", "01");
		contract.setPayStat(payLov.getId());
		
		LovMember contrTypeLov = lovMemberService.getLovMemberByCode("CONTRACTTYPE", IConstants.CONTR_LOAN_0101); // 借货合同申请
		contract.setContrType(contrTypeLov.getId());
		
		contract.setSubmitTime(new Date());
		contract.setCreateTime(new Date());
		
		LovMember entityLov = lovMemberService.getLovMemberByCode("OPERATION_UNIT", "101");
		contract.setBussEnity(entityLov.getId());
		
		LovMember orgLov = lovMemberService.get(userObject.getOrg().getId());
		contract.setOrg(orgLov.getId()); 
		
		if(!StringUtil.isNullOrEmpty(contrId)){
			Contract contractF = contractService.get(contrId);
			contract.setCustCode(contractF.getCustCode());
			contract.setCustName(contractF.getCustName());
			contract.setOrderer(contractF.getOrderer());
			contract.setOrderOrgId(contractF.getOrderOrgId());
			contract.setOrderPosId(contractF.getOrderPosId());
			contract.setProjName(contractF.getProjName());
			contract.setProjNo(contractF.getProjNo());
			contract.setErpOrderCustName(contractF.getErpOrderCustName());
			contract.setErpOrderCustId(contractF.getErpOrderCustId());
			contract.setOrg(contractF.getOrg());
			contract.setMarketDept(contractF.getMarketDept());
			
			model.addAttribute("frameContr",JSON.toJSONString(contractF));
			
		}
		
		model.addAttribute("contr", contract);
		
		initJson(contract, model);
		
		return forward("add");
	}
	
	/**
	 * 初始化值
	 * @param contract
	 * @param model
	 */
	private void initJson(Contract contract,Model model){
		
		BusinessOpportunity biz= bizOppService.getBizOppEntity(contract.getProjNo());
		CustomInfo cust = customerService.getCustomInfo(contract.getCustCode()); 
		CustomInfo erpCust = customerService.getCustomInfo(contract.getErpOrderCustId());
		ProductPriceHead priceTable = priceHeadService.queryLpcById(contract.getPricNo());
		
		if (biz != null) {
			model.addAttribute("project", JSON.toJSONString(biz));
		}
		if (cust != null) {
			model.addAttribute("customer", JSON.toJSONString(cust));
		}
		if (priceTable != null) {
			model.addAttribute("priceTable", JSON.toJSONString(priceTable));
		}
		if (erpCust != null) {
			model.addAttribute("erpCustomer", JSON.toJSONString(erpCust));
		}
		
		Employee ordEmp =  ((Employee)CacheData.getInstance().get(contract.getOrderer()));
		LovMember ordPosi = lovMemberService.get(contract.getOrderPosId());
		LovMember ordOrg = lovMemberService.get(contract.getOrderOrgId());
		if(ordEmp !=null && ordPosi!=null  && ordOrg!=null){
			EmployeeObject teamObj = new EmployeeObject(ordOrg,ordPosi, ordEmp);
			if(teamObj!=null){
				model.addAttribute("teamObj",JSON.toJSONString(teamObj));
			}
		}
		
		model.addAttribute("org",JSON.toJSONString(lovMemberService.get(contract.getOrg())));
	}
	

	@ResponseBody
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public String add(Contract contract,HttpServletRequest request) {
		
		String contrNo = contractLoanService.getContractLoanNumber(); 
		contract.setContrNo(contrNo);
		String strError= "";
		String f="0";
		if(contract.getIsWholeSet() == null ){
			f="1";
			strError += "是否成套";
		}
		if(contract.getIsConfList() == null ){
			f="1";
			strError += ","+"是否有配置清单";
		}
		if(contract.getIsDelivHome() == null ){
			f="1";
			strError += ","+"是否送货上门";
		}
		if(contract.getIsUnload() == null ){
			f="1";
			strError += ","+"是否需卸货";
		}
		if(contract.getIsHomeInstall() == null ){
			f="1";
			strError += ","+"是否上门安装";
		}
		if(contract.getIsAux() == null ){
			f="1";
			strError += ","+"是否提供安装辅材";
		}
		if(f.equalsIgnoreCase("1")){
			strError += "不能为空！"; 
			throw new AnneException(strError);
		}
		Date expSignDT= contract.getExpectSignDate();// 合同预计签订时间
		Date subTime = contract.getSubmitTime(); // 借货日期
		Date delivDate = contract.getDelivDate(); // 要货日期
		Date now = new Date();
		if(delivDate.before(now)){
			throw new AnneException("要货日期要在今天之后");
		}
		if(expSignDT.before(subTime)){
			throw new AnneException("合同预计签订时间应该在借货时间之后");
		}
		contractLoanService.save(contract,getUserObject());	
		
		return sendSuccessMessage(contract.getId());	
	}
	
	@RequestMapping("/edit")
	public String edit(String id, Model model) { 
		Contract contract = contractLoanService.get(id); 
//		Contract dirSellContr=  contractLoanService.get(contract.getDirectSellNo());
////		String dirSellNo= dirSellContr.getContrNo(); 
//		Contract frameContr=  contractLoanService.get(contract.getFrameNo());
////		String frameNo= frameContr.getContrNo(); 
		BusinessOpportunity biz= bizOppService.getBizOppEntity(contract.getProjNo());
		CustomInfo cust = customerService.getCustomInfo(contract.getCustCode()); 
		ProductPriceHead priceTable = priceHeadService.queryLpcById(contract.getPricNo());
		model.addAttribute("contr", contract);
		model.addAttribute("org",JSON.toJSONString(lovMemberService.get(contract.getOrg())));
		Employee ordEmp =  ((Employee)CacheData.getInstance().get(contract.getOrderer()));
		LovMember ordPosi = lovMemberService.get(contract.getOrderPosId());
		LovMember ordOrg = lovMemberService.get(contract.getOrderOrgId());
		if(ordEmp !=null && ordPosi!=null  && ordOrg!=null){
			EmployeeObject teamObj = new EmployeeObject(ordOrg,ordPosi, ordEmp);
			if(teamObj!=null){
			model.addAttribute("teamObj",JSON.toJSONString(teamObj));
			}
		}
		
		if(biz!=null){
		model.addAttribute("project",JSON.toJSONString(biz));
		}
		if(cust!=null){
		model.addAttribute("customer",JSON.toJSONString(cust));
		}
		if(priceTable!=null){
		model.addAttribute("priceTable",JSON.toJSONString(priceTable));
		}
		return forward("loan");
	}

	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(Contract contract) { 
		updtContract(contract);
		return sendSuccessMessage();
	}

	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(String id) {
		Contract contr= contractService.get(id);
		if(!lovMemberService.get(contr.getContrStat()).getCode().equalsIgnoreCase("01")){
			throw new AnneException("合同不为新建状态下无法删除");			
		}
		contractLoanService.delete(id);
		return sendSuccessMessage();
	}

	@RequestMapping("/att")
	public String atts(String contrId,Model model) {
		model.addAttribute("contrId",contrId);
		return forward("attachment/loanAttachment");
	}

	/**
	 * 发货地址
	 *
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/addr")
	public String address(String contrId,String typ,String editFlag,Model model) {
		model.addAttribute("contrId",contrId);
		model.addAttribute("typ",typ);
		model.addAttribute("editFlag",editFlag);
		return forward("shipaddr/loanAddress");
	}
	 	
	/**
	 * 合同评审
	 *
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/review")
	public String review(String contrId,String editFlag,Model model) {
		model.addAttribute("contrId",contrId);
		model.addAttribute("editFlag",editFlag);
		return forward("review/review");
	}

	/**
	 * 合同评审历史
	 *
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/history")
	public String history(String contrId,Model model) {
		model.addAttribute("contrId",contrId);
		return forward("history/loanHistory");
	} 
	/**
	 * 工程清单
	 *
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/prjlst")
	public String prjlst(String contrId,String typ, String pricetableid,String editFlag,String showPriceFlg, String showMaterCodeFlg,Model model) {
		//add new lovmem root
//		String groupId = "PRJLSTPRDCAT";
//		String rootexists = contractService.Checklovroot(contrId);
//		
//		if(rootexists.equals("Y")){
//			contractService.addlovroot(contrId, typ, groupId);
//		}
		model.addAttribute("contr",contractLoanService.get(contrId));
		model.addAttribute("contrId",contrId);
		model.addAttribute("typ",typ);
		model.addAttribute("pricetableid",pricetableid);
		model.addAttribute("editFlag",editFlag);
		model.addAttribute("showPriceFlg",showPriceFlg);
		model.addAttribute("showMaterCodeFlg",showMaterCodeFlg);
		//return forward("prjlst/loanPrjlst");
		return forward("prjlst/eliminateLoanPrjlst");
	}
	
	/**
	 * 合同变更
	 *
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/changePage")
	public String changePage(String id, String contrId,Model model) {
		model.addAttribute("contrId", contrId);
		return forward("../../standard/change/change");
	}
	
	@ResponseBody
	@RequestMapping(value = "/startContractTrialProcess", method = RequestMethod.POST)
	public String startContractTrialProcess(String typ,Contract contract,HttpServletRequest request) throws Exception{
		String contrId = contract.getId(); 
		if(!contractService.checkPayPlanListNull(contrId)){
			throw new AnneException("请添加回款规则");
		}
		updtContract(contract);
		contractLoanService.startContractTrialProcess(getUserObject(), contrId,typ);
		return sendSuccessMessage();
	}

	public String startContrTrialProcess(String typ,Contract contract){
		String contrId = contract.getId(); 
		if(!contractService.checkPayPlanListNull(contrId)){
			throw new AnneException("请添加回款规则");
		}
//		 checkBusiEntity(contrId );
		updtContract(contract);
		contractLoanService.startContractTrialProcess(getUserObject(), contrId,typ);
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/reviseContract", method = RequestMethod.POST)
	public String reviseContract(String contrId,HttpServletRequest request) throws Exception{		
		contractLoanService.reviseContract(getUserObject(), contrId);
		return sendSuccessMessage();
	}

	/**
	 * 签订
	 * @param contrId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/signUpContract", method = RequestMethod.POST)
	public String signUpContract(String contrId,HttpServletRequest request) throws Exception{		
		contractLoanService.signUpContract(getUserObject(), contrId);
		return sendSuccessMessage();
	}

	/**
	 * 合同变更
	 *
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/change")
	public String changeContr(String contrId,Model model) {  
		List<ContrChange> chgLst = changeService.queryChangeListByContrId(contrId);
		// 合同状态
		LovMember sigLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "10");// 10 已签订
		LovMember calLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "11");// 11 已取消
		String flag = "N";
		String chgNo = "";
		if(chgLst!= null && chgLst.size()>0){
			for(ContrChange chg : chgLst){
				if(chg.getChangeStat().equalsIgnoreCase(sigLov.getId()) || chg.getChangeStat().equalsIgnoreCase(calLov.getId())){
					continue;
				}else{
					flag="Y";
					chgNo = chg.getChangeNo();
				}
			}
			if(flag=="Y") {
				throw new AnneException("已存在进行中的变更单：" + chgNo);
			}
		}
		model.addAttribute("contrId",contrId);
		return redirect("/change/add.html");
	}

	/**
	 * 借货核销明细报表 导出
	 * @param condition
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "exportLoanverification")
	public void exportLoanverification(PageCondition condition, HttpServletRequest request,HttpServletResponse response){
		ActionUtil.requestToCondition(condition, request);
		ActionUtil.doSearch(condition);
		List<List<Object>> dataList = contractLoanService.exportLoanverification(condition);
		ExcelUtil.exportExcel(response, dataList, "借货核销明细报表-" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
	}

	/**
	 * 借货核销报表(按客户名称) 导出
	 * @param condition
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "exportLoanVerificationByCustomer")
	public void exportLoanVerificationByCustomer(PageCondition condition, HttpServletRequest request,HttpServletResponse response){
        ActionUtil.requestToCondition(condition, request);
        ActionUtil.doSearch(condition);
	    List<List<Object>> lists = contractLoanService.exportLoanVerificationByCustomer(condition);
        ExcelUtil.exportExcel(response, lists, "借货核销报表-按客户-" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
	}

    /**
     * 借货核销报表(按销售人员) 导出
     * @param condition
     * @param request
     * @param response
     */
    @RequestMapping(value = "exportLoanVerificationBySalesman")
    public void exportLoanVerificationBySalesman(PageCondition condition, HttpServletRequest request,HttpServletResponse response){
        ActionUtil.requestToCondition(condition, request);
        ActionUtil.doSearch(condition);
        List<List<Object>> lists = contractLoanService.exportLoanVerificationBySalesman(condition);
        ExcelUtil.exportExcel(response, lists, "借货核销报表-按销售人员-" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }

    /**
     * 借货核销报表(按销售部门) 导出
     * @param condition
     * @param request
     * @param response
     */
    @RequestMapping(value = "exportLoanVerificationBySalesDepartmant")
    public void exportLoanVerificationBySalesDepartmant(PageCondition condition, HttpServletRequest request,HttpServletResponse response){
		ActionUtil.requestToCondition(condition, request);
		ActionUtil.doSearch(condition);
    	List<List<Object>> lists = contractLoanService.exportLoanVerificationBySalesDepartmant(condition);
        ExcelUtil.exportExcel(response, lists, "借货核销报表-按销售部门-" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }

    /**
     * 未核销借货明细 导出
     * @param condition
     * @param request
     * @param response
     */
    @RequestMapping(value = "exportUnverificationLoan")
    public void exportUnverificationLoan(PageCondition condition, HttpServletRequest request,HttpServletResponse response){
        ActionUtil.requestToCondition(condition, request);
        ActionUtil.doSearch(condition);
        List<List<Object>> lists = contractLoanService.exportUnverificationLoan(condition);
        ExcelUtil.exportExcel(response, lists, "未核销借货明细报表-" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }

	@RequestMapping(value="/loanContrExport")
	public void exportLoanContracts(PageCondition condition, HttpServletRequest request,HttpServletResponse response){

		ActionUtil.requestToCondition(condition, request);

        ActionUtil.doSearch(condition);

		String idsStr = request.getParameter("idsStr");
		String typ = request.getParameter("typ");
		String[] ids = idsStr.split(",");

		List<List<Object>> dataList = contractLoanService.exportContractsHead(condition,typ,ids);
		ExcelUtil.exportExcel(response, dataList, "借货列表");
	}
	
	/**
	 * 借货产品明细
	 * @param condition
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/reportExportLoan")
	public void reportExportLoanContracts(PageCondition condition, HttpServletRequest request,HttpServletResponse response){

		ActionUtil.requestToCondition(condition, request);

        ActionUtil.doSearch(condition);

		String idsStr = request.getParameter("idsStr");
		String typ = request.getParameter("typ");
		String[] ids = idsStr.split(",");

		List<List<Object>> dataList = contractLoanService.reportExportContractsHead(condition,typ,ids);
		ExcelUtil.exportExcel(response, dataList, "借货产品明细");
	}
	
	public String checkSaveContract(Contract contract){

		String flg="Y";
		
		String strError= "";
		String f="0";
		if(contract.getIsWholeSet() == null ){
			f="1";
			strError += "是否成套";
		}
		if(contract.getIsConfList() == null ){
			f="1";
			strError += ","+"是否有配置清单";
		}
		if(contract.getIsDelivHome() == null ){
			f="1";
			strError += ","+"是否送货上门";
		}
		if(contract.getIsUnload() == null ){
			f="1";
			strError += ","+"是否需卸货";
		}
		if(contract.getIsHomeInstall() == null ){
			f="1";
			strError += ","+"是否上门安装";
		}
		if(contract.getIsAux() == null ){
			f="1";
			strError += ","+"是否提供安装辅材";
		}
		if(f.equalsIgnoreCase("1")){
			strError += "不能为空！";
			flg="N";
			throw new AnneException(strError);
		}
		Date expSignDT= contract.getExpectSignDate();// 合同预计签订时间
		Date subTime = contract.getSubmitTime(); // 借货日期
		Date delivDate = contract.getDelivDate(); // 要货日期
		Date now = new Date();
		if(delivDate.before(now)){
			flg="N";
			throw new AnneException("要货日期要在今天之后");			
		}
		if(expSignDT.before(subTime)){ 
			flg="N";
			throw new AnneException("合同预计签订时间应该在借货时间之后");
		}
		
		return flg;
	}
	
	public void updtContract(Contract contract) { 
		if(checkSaveContract(contract)=="Y"){
			contract.setUpdatedById(getUserObject().getEmployee().getId());
			contract.setUpdatedAt(new Date());
			contractLoanService.update(contract);
		}
	}
	
	public void checkBusiEntity(String contrId ) {
		Contract contr = contractService.get(contrId);
		String flg = contractService.checkContrBusiEntityForOrder(contr.getCustCode(), contr.getBussEnity());
		if(flg=="N"){
			throw new AnneException("该客户对应的业务实体在ERP审核状态未完成");			
		}
		String flg1 = contractService.checkGenOrdLinesByContract(contrId);
		if(flg1=="N"){
			throw new AnneException("合同中没有可生成订单的行");			
		}
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/changeHighRiskFlag")
	public String page(String id,String isHighRisk, HttpServletRequest request){
		// Contract contract = this.contractLoanService.get(id);
		this.contractLoanService.updateHighRiskFlag(id, isHighRisk);
		return sendSuccessMessage();
	}

}
