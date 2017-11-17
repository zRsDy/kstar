/** 
 * Project Name:crm 
 * File Name:QuotAction.java 
 * Package Name:com.ibm.kstar.action.quot 
 * Date:Jan 4, 20175:33:52 PM 
 * Copyright (c) 2017, ZW All Rights Reserved. 
 * 
 */

package com.ibm.kstar.action.quot;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.api.bizopp.IBizoppService;
import com.ibm.kstar.api.contract.IContractLoanService;
import com.ibm.kstar.api.contract.IContractService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.org.IOrgTeamService;
import com.ibm.kstar.api.price.IPriceHeadService;
import com.ibm.kstar.api.product.*;
import com.ibm.kstar.api.quot.IQuotService;
import com.ibm.kstar.api.system.lov.ILovGroupService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovGroup;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.team.ITeamService;
import com.ibm.kstar.entity.bizopp.BusinessOpportunity;
import com.ibm.kstar.entity.contract.Contract;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.product.KstarProduct;
import com.ibm.kstar.entity.product.KstarProductWorkFlow;
import com.ibm.kstar.entity.product.ProductPriceHead;
import com.ibm.kstar.entity.quot.*;
import com.ibm.kstar.interceptor.system.permission.NoRight;
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
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.ui.TabMain.Style;
import org.xsnake.web.upload.IUploadFile;
import org.xsnake.web.upload.UploadUtils;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.ExcelUtil;
import org.xsnake.web.utils.StringUtil;
import org.xsnake.xflow.api.model.HistoryProcessInstance;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;


/**
 * ClassName:QuotAction <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: Jan 4, 2017 5:33:52 PM <br/>
 * 
 * @author ZW
 * @since JDK 1.7
 * @see
 */

@Controller
@RequestMapping("/quot")
public class QuotAction extends BaseAction {

	@Autowired
	IQuotService quotService;
	
	@Autowired
	IProductProcesService productProcess;
	
	@Autowired
	IProductService productService;
	
	@Autowired
	protected ILovMemberService lovMemberService;
	
	@Autowired
	IProductSerialService productSerialService;

	@Autowired
	IProLovService proLovService;
	
	@Autowired
	protected ILovGroupService lovGroupService;
	
	@Autowired
	IDemandService demandService;
	
	@Autowired
	private IContractService contractService;
	
	@Autowired
	IPriceHeadService priceheadservice;
	
	@Autowired
	protected ITeamService teamService;
	
	@Autowired
	protected IOrgTeamService orgTeamService;
	
	@Autowired
	private ICustomInfoService customerService;
	
	@Autowired
	private IBizoppService bizOppService;

	@Autowired
	private IPriceHeadService priceHeadService;
	
	@Autowired
	private IContractLoanService contractLoanService;
	
	@Autowired
	private ICustomInfoService custinfoService;
	
	
	@RequestMapping("/prdtab")
	public String addnonprd(String id,String qid,String ctg,String groupId,String parentId,Model model){
		model.addAttribute("id",id);
		model.addAttribute("qid",qid);
		model.addAttribute("ctg",ctg);
		model.addAttribute("groupId",groupId);		
		model.addAttribute("parentId",parentId);
		
		
		return forward("prdtab");
		//return forward("product");
		
		//return redirect("info1.html");
	}
	
	
	@ResponseBody
	@RequestMapping(value="/prdtab",method=RequestMethod.POST)
	public String addnprd(LovMember lovMember,KstarPrjLst prjLst){
		return sendSuccessMessage();
	}


	
	@RequestMapping("/versioncomp")
	public String versioncomp(String qid,String typ,Model model){
	
		model.addAttribute("qid",qid);
		model.addAttribute("typ",typ);
		
		KstarQuot currQuot = quotService.getKstarQuot(qid);
		KstarQuot preQuot = quotService.getPrequotbyID(qid);
		
		String preqid = preQuot.getId();
		model.addAttribute("preqid",preqid);
		
		return forward("versioncomp");

	}
	
	
	@ResponseBody
	@RequestMapping(value="/versioncomp",method=RequestMethod.POST)
	public String versioncmp(LovMember lovMember,KstarPrjEvl prjLst){
		return sendSuccessMessage();
	}
	
	
	@RequestMapping("/demand")
	public String showDemand(String prodId,String customId,String businessUnit,String custContrNo, Model model){
		TabMain tabMain = new TabMain();
		tabMain.setInitAll(false);
		tabMain.addTab("需求单", "/product/info8.html?id="+prodId+"&customId="+customId+"&businessUnit="+businessUnit+"&custContrNo="+custContrNo);

		model.addAttribute("tabMain",tabMain);
//		model.addAttribute("prodId",prodId);
		return forward("demand");
	}
	
	
	@RequestMapping("/info1")
	public String info(String id,String qid,String ctg,String groupId,String parentId,Model model,HttpServletRequest request){
		
		model.addAttribute("id",id);
		model.addAttribute("qid",qid);
		model.addAttribute("ctg",ctg);
		model.addAttribute("groupId",groupId);
		model.addAttribute("parentId",parentId);
		
		KstarQuot quot = quotService.getKstarQuot(qid);
		
		CustomInfo custInfo = custinfoService.getCustomInfoByCode(quot.getCustomerCode());
		
		String customId = custInfo.getId();
		
		String businessUnit = quot.getMarketDept();
		model.addAttribute("customId",customId);
		model.addAttribute("businessUnit",businessUnit);
		//客户PO号
		String custContrNo= quot.getQuotCode();

		model.addAttribute("custContrNo",custContrNo);
		
		
		KstarProduct kp = null;
		if(StringUtils.isNotEmpty(id)){
			model.addAttribute("id",id);
			 kp =  productService.queryProductById(id);
			 
			 if(StringUtils.isNotBlank(kp.getProOrgId())){
				 
				LovMember org = lovMemberService.get(kp.getProOrgId());
				model.addAttribute("org", JSON.toJSONString(org));
			 }
		}else{
			kp = new KstarProduct();
			
			kp.setPublishStatus(kp.getLovMember(ProcessConstants.PRODUCT_PUBLISH_PROC, ProcessConstants.PROCESS_STATUS_01).getId());
			kp.setSaleStatus(kp.getLovMember(ProcessConstants.PM_PTS_PROC, ProcessConstants.PROCESS_STATUS_01).getId());
			kp.setPriceStatus("notdecideprice");
			
			kp.setLineBean(null);
			
			if(ctg.equals("A")){
				kp.setCrmCategory("0002");
			}else if(ctg.equals("B")){
				kp.setCrmCategory("0005");
			}

			kp.setId(parentId);
		}
		
		
		model.addAttribute("product", kp);
		return forward("info1");
	}
	
	
	@RequestMapping("/info8")
	public String info8(String id,Model model){
		model.addAttribute("id",id);
		return forward("info8");
	}
		
	
	
	@ResponseBody
	@RequestMapping(value="/mainInfoSave",method=RequestMethod.POST)
	public String mainInfoSave(KstarProduct product, HttpServletRequest request){
		
		boolean newFlag = false;
		
		if(product.getId() == null || StringUtils.isEmpty(product.getId())){
			product.setId(null);
			newFlag = true;
		}
		
		
		String qid = request.getParameter("qid");
		String parentId = request.getParameter("parentId");
		String groupId = request.getParameter("groupId");
		
		
		LovGroup lovGroup = lovGroupService.get(groupId);

		
		//new point
		KstarPrjLst prjLst = new KstarPrjLst();
		LovMember lovMem = new LovMember();
		
		if (StringUtil.isNotEmpty(parentId)) {
			LovMember parentLovMember = lovMemberService.get(parentId);			
			lovMem.setParentId(parentLovMember.getId());
		}else{
			lovMem.setParentId(quotService.getLovememroot(qid));
		}

		
		String cType = "0003";
		prjLst.setQuotCode(qid);
		
		
		if(StringUtil.isEmpty(prjLst.getQuotCode())){
			throw new AnneException("报价单编号不能为空");
		}else{
			lovMem.setMemo(prjLst.getQuotCode());
		}
		
		lovMem.setCode(StringUtil.getUUID());
		lovMem.setGroupId(groupId);
		lovMem.setName(product.getProductName());
		prjLst.setPrdNm(product.getProductName());
		prjLst.setPrdTyp(product.getProModel());
		prjLst.setNstRq("是");
		
		if ((product.getCrmCategory()).equals("0002")){
			prjLst.setPrdCtg("非标产品");
		}else if ((product.getCrmCategory()).equals("0005")){
			prjLst.setPrdCtg("外购产品");
		}
		
		
		
	
		//
		
		if(newFlag){
			String vCode = product.getVmaterCode();
			productSerialService.save(vCode,this.getUserObject());
		}
		
		productService.save(product,this.getUserObject());
		proLovService.saveCatelog(lovMem);
		prjLst.setLvId(lovMem.getId());
		quotService.savePrjLst(prjLst,cType);
		


		return sendSuccessMessage(product.getId());
	}
	
	
	
	
	@RequestMapping("/index")
	public String index(String id, Model model) {
		return forward("index");
	}
	
	
	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		//模糊查询
		String searchKey = condition.getStringCondition("searchKey");
		if(searchKey != null){
			condition.getFilterObject().addOrCondition("quotCode", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("quotName", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("customerName", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("salesRepid", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("salesRep", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("salDep", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("boName", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("isBidPro", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("status", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("bidAuditStatus", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("proReviewStatus", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("bidResults", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("bidResReason", "like", "%"+searchKey+"%");
			
		}
		
		String quotCode = condition.getStringCondition("quotCode");
		if(StringUtil.isNotEmpty(quotCode)){
			condition.getFilterObject().addCondition("quotCode", "=", quotCode);
		}
		
		String quotName = condition.getStringCondition("quotName");
		if(StringUtil.isNotEmpty(quotName)){
			condition.getFilterObject().addCondition("quotName", "=", quotName);
		}
				
		String customerName = condition.getStringCondition("customerName");
		if(StringUtil.isNotEmpty(customerName)){
			condition.getFilterObject().addCondition("customerName", "=", customerName);
		}
		
		String salesRepid = condition.getStringCondition("salesRepid");
		if(StringUtil.isNotEmpty(salesRepid)){
			condition.getFilterObject().addCondition("salesRepid", "=", salesRepid);
		}
		
		String salesRep = condition.getStringCondition("salesRep");
		if(StringUtil.isNotEmpty(salesRep)){
			condition.getFilterObject().addCondition("salesRep", "=", salesRep);
		}
		
		String salDep = condition.getStringCondition("salDep");
		if(StringUtil.isNotEmpty(salDep)){
			condition.getFilterObject().addCondition("salDep", "=", salDep);
		}
		
		String boName = condition.getStringCondition("boName");
		if(StringUtil.isNotEmpty(boName)){
			condition.getFilterObject().addCondition("boName", "=", boName);
		}
		
		String amount_begin = condition.getStringCondition("amount_begin");
		if(StringUtil.isNotEmpty(amount_begin)){
			condition.getFilterObject().addCondition("amount", ">=", amount_begin);
		}
		String amount_end = condition.getStringCondition("amount_end");
		if(StringUtil.isNotEmpty(amount_end)){
			condition.getFilterObject().addCondition("amount", "<=", amount_end);
		}
		
		String createTime_begin = condition.getStringCondition("createTime_begin");
		if(StringUtil.isNotEmpty(createTime_begin)){
			condition.getFilterObject().addCondition("createTime", ">=", createTime_begin);
		}
		String createTime_end = condition.getStringCondition("createTime_end");
		if(StringUtil.isNotEmpty(createTime_end)){
			condition.getFilterObject().addCondition("createTime", "<=", createTime_end);
		}
		
		String isBidPro = condition.getStringCondition("isBidPro");
		if(StringUtil.isNotEmpty(isBidPro)){
			condition.getFilterObject().addCondition("isBidPro", "=", isBidPro);
		}
		
		String status = condition.getStringCondition("status");
		if(StringUtil.isNotEmpty(status)){
			condition.getFilterObject().addCondition("status", "=", status);
		}
		
		String bidAuditStatus = condition.getStringCondition("bidAuditStatus");
		if(StringUtil.isNotEmpty(bidAuditStatus)){
			condition.getFilterObject().addCondition("bidAuditStatus", "=", bidAuditStatus);
		}
		
		String proReviewStatus = condition.getStringCondition("proReviewStatus");
		if(StringUtil.isNotEmpty(proReviewStatus)){
			condition.getFilterObject().addCondition("proReviewStatus", "=", proReviewStatus);
		}
		
		String bidResults = condition.getStringCondition("bidResults");
		if(StringUtil.isNotEmpty(bidResults)){
			condition.getFilterObject().addCondition("bidResults", "=", bidResults);
		}
		
		String bidResReason = condition.getStringCondition("bidResReason");
		if(StringUtil.isNotEmpty(bidResReason)){
			condition.getFilterObject().addCondition("bidResReason", "=", bidResReason);
		}
		
		
		
		IPage p = quotService.query(condition);

		return sendSuccessMessage(p);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/processpage")
	public String processpage(PageCondition condition, HttpServletRequest request) throws Exception {
		ActionUtil.requestToCondition(condition, request);
		String processID = request.getParameter("processID");

		IPage p = null;

		if (StringUtils.isNotEmpty(processID)) {
			
			List<KstarProductWorkFlow> kwList = productProcess.getList(processID);
			int size = kwList.size();

			if (size == 1) {

				KstarPrjEvl prjevl = quotService.getKstarPrjEvl(kwList.get(0).getProductId());

				KstarQuot quot;

				if (prjevl!= null) {
					quot = quotService.getKstarQuot(prjevl.getQuotCode());
				} else {
					quot = quotService.getKstarQuot(kwList.get(0).getProductId());
				}

				//condition.getFilterObject().addCondition("id", "eq", quot.getId());
				
				p = quotService.queryQuot(quot.getId());

			}else{
				
				KstarQuot quot;
				
				KstarBiddcevl bidevl = quotService.getBiddcevl(processID);
				
				if(bidevl!=null){
					quot = quotService.getKstarQuot(bidevl.getQuotCode());
					p = quotService.queryQuot(quot.getId());
				}
				
				
			}
		}
		return sendSuccessMessage(p);
	}
	
	
	
	@RequestMapping("/contract")
	public String contract(String qid,Model model) {
		model.addAttribute("qid",qid);
		return forward("contract");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/contractPage")
	public String pageCnt(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		condition.setCondition("qid", request.getParameter("qid"));
		IPage p = quotService.queryCnt(condition);

		return sendSuccessMessage(p);
	}

	@RequestMapping("/mem")
	public String members(String qid,Model model) {
		model.addAttribute("qid",qid);
		return forward("mem");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/memPage")
	public String pageMem(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		condition.setCondition("qid", request.getParameter("qid"));
		IPage p = quotService.queryMem(condition);

		return sendSuccessMessage(p);
	}
	
	
	@RequestMapping("/att")
	public String atts(String qid,Model model) {
		model.addAttribute("qid",qid);
		return forward("att");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/attPage")
	public String pageAtt(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		condition.setCondition("qid", request.getParameter("qid"));
		IPage p = quotService.queryAtt(condition);

		return sendSuccessMessage(p);
	}
	
	
	@RequestMapping("/prjevl")
	public String prjevls(String qid,String typ,String processpage,String ifProReviewStatus,String checkver,String ckevlstatus,String ckevlststatus,Model model) {
		model.addAttribute("checkver",checkver);
		model.addAttribute("ckevlstatus",ckevlstatus);
		
		model.addAttribute("processpage",processpage);
		model.addAttribute("ifProReviewStatus",ifProReviewStatus);
		
		//未启动状态
		ckevlststatus = Checkevlststatus(qid, typ);
		
		model.addAttribute("ckevlststatus",ckevlststatus);
		model.addAttribute("qid",qid);
		model.addAttribute("typ",typ);
		return forward("prjevl");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/prjevlPage")
	public String pagePrjevl(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		condition.setCondition("qid", request.getParameter("qid"));
		condition.setCondition("typ", request.getParameter("typ"));
		IPage p = quotService.queryPrjevl(condition);

		return sendSuccessMessage(p);
	}
	
	
	
	@RequestMapping("/prjLst")
	public String prjLsts(String qid,String typ,String processpage,String ifProReviewStatus,String pricetableid,String sprvmrkstatus,String quotsubmitstatus,String applyPrcStatus,Model model) {
		model.addAttribute("qid",qid);
		model.addAttribute("typ",typ);
		model.addAttribute("processpage",processpage);
		model.addAttribute("ifProReviewStatus",ifProReviewStatus);
		model.addAttribute("pricetableid",pricetableid);
		model.addAttribute("sprvmrkstatus",sprvmrkstatus);
		model.addAttribute("quotsubmitstatus",quotsubmitstatus);
		model.addAttribute("applyPrcStatus",applyPrcStatus);
		
		KstarQuot quot = quotService.getKstarQuot(qid);
		String ifbidresult = "Y";
		if(quot.getIsValid().equals("0")){
			ifbidresult = "N";
		}
		model.addAttribute("ifbidresult",ifbidresult);
		
		return forward("prjLst");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/prjLstPage")
	public String pagePrjLst(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		condition.setCondition("qid", request.getParameter("qid"));
		condition.setCondition("typ", request.getParameter("typ"));
		IPage p = quotService.queryPrjLst(condition);
		return sendSuccessMessage(p);
	}
	
	
	
	@RequestMapping("/pginf")
	public String pginf(String qid,String typ,Model model) {
		model.addAttribute("qid",qid);	
		model.addAttribute("typ",typ);	
		String cType = typ;
		
		this.initPrjdtl(qid, cType);
//		//generate baseinf 
//		KstarBaseInf baseinf = quotService.getKstarBaseInf(qid,cType);
//		if(baseinf==null){
//			quotService.saveBaseinf(baseinf,qid,cType);			
//		}
//		
//		//generate idu 
//		KstarIdu idu = quotService.getKstarIdu(qid,cType);
//		if(idu==null){
//			quotService.saveIdu(idu, qid, cType);			
//		}
//		
//		//generate idm 
//		KstarIdm idm = quotService.getKstarIdm(qid,cType);
//		if(idm==null){
//			quotService.saveIdm(idm, qid,cType);			
//		}
//		
//		//generate sngups 
//		KstarSngUps sngups = quotService.getKstarSngUps(qid,cType);
//		if(sngups==null){
//			quotService.saveSngUps(sngups, qid,cType);		
//		}
//		
//		
//		//generate sngbty 
//		KstarSngBty sngbty = quotService.getKstarSngBty(qid,cType);
//		if(sngbty==null){
//			quotService.saveSngbty(sngbty, qid,cType);			
//		}
//		
//		//generate sngelec 
//		KstarSngElec sngelec = quotService.getKstarSngElec(qid,cType);
//		if(sngelec==null){
//			quotService.saveSngelec(sngelec, qid,cType);			
//		}
//		
//		//generate sngclr 
//		KstarSngClr sngclr = quotService.getKstarSngClr(qid,cType);
//		if(sngclr==null){
//			quotService.saveSngclr(sngclr, qid,cType);			
//		}
//		
//		//generate sngrck 
//		KstarSngRck sngrck = quotService.getKstarSngRck(qid,cType);
//		if(sngrck==null){
//			quotService.saveSngrck(sngrck, qid,cType);	
//		}
//		
//		//generate sngmnt 
//		KstarSngMnt sngmnt = quotService.getKstarSngMnt(qid,cType);
//		if(sngmnt==null){
//			quotService.saveSngmnt(sngmnt, qid,cType);	
//		}
		
		
		return forward("pginf");
	}
	@NoRight
	@ResponseBody
	@RequestMapping(value="/pginfPage")
	public String pagePginf(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		condition.setCondition("qid", request.getParameter("qid"));
		condition.setCondition("cType", request.getParameter("typ"));
		
		IPage p = quotService.queryPginf(condition);

		return sendSuccessMessage(p);
	}
	
	
	@RequestMapping("/befsale")
	public String befsale(String qid,String typ,String bocode,String processpage,String ifProReviewStatus,Model model) {
		
		model.addAttribute("ifProReviewStatus",ifProReviewStatus);
		model.addAttribute("processpage",processpage);
		
		TabMain tabMain1 = new TabMain(Style.top);
		tabMain1.setInitAll(false);
		
		if(this.hasPermission("P04T2ProjFormT3PresaleT1IDUSave")){
			tabMain1.addTab("IDU", "/quot/idu.html?qid="+qid+"&typ="+typ+"&bocode="+bocode+"&processpage="+processpage+"&ifProReviewStatus="+ifProReviewStatus);//z
		}
		
		if(this.hasPermission("P04T2ProjFormT3PresaleT2IDMSave")){
			tabMain1.addTab("IDM", "/quot/idm.html?qid="+qid+"&typ="+typ+"&bocode="+bocode+"&processpage="+processpage+"&ifProReviewStatus="+ifProReviewStatus);
		}
		
		if(this.hasPermission("P04T2ProjFormT3PresaleT3UPSSave")){
			tabMain1.addTab("单UPS", "/quot/sngups.html?qid="+qid+"&typ="+typ+"&bocode="+bocode+"&processpage="+processpage+"&ifProReviewStatus="+ifProReviewStatus);
		}
		
		if(this.hasPermission("P04T2ProjFormT3PresaleT4BatterySave")){
			tabMain1.addTab("单电池", "/quot/sngbty.html?qid="+qid+"&typ="+typ+"&bocode="+bocode+"&processpage="+processpage+"&ifProReviewStatus="+ifProReviewStatus);//z
		}
		
		if(this.hasPermission("P04T2ProjFormT3PresaleT5AirConSave")){
			tabMain1.addTab("单空调", "/quot/sngclr.html?qid="+qid+"&typ="+typ+"&bocode="+bocode+"&processpage="+processpage+"&ifProReviewStatus="+ifProReviewStatus);
		}
		
		if(this.hasPermission("P04T2ProjFormT3PresaleT6PowerSave")){
			tabMain1.addTab("单配电", "/quot/sngelec.html?qid="+qid+"&typ="+typ+"&bocode="+bocode+"&processpage="+processpage+"&ifProReviewStatus="+ifProReviewStatus);
		}
		
		if(this.hasPermission("P04T2ProjFormT3PresaleT7CabinetSave")){
			tabMain1.addTab("单机柜", "/quot/sngrck.html?qid="+qid+"&typ="+typ+"&bocode="+bocode+"&processpage="+processpage+"&ifProReviewStatus="+ifProReviewStatus);
		}
		
		if(this.hasPermission("P04T2ProjFormT3PresaleT8MonitorSave")){
			tabMain1.addTab("单监控", "/quot/sngmnt.html?qid="+qid+"&typ="+typ+"&bocode="+bocode+"&processpage="+processpage+"&ifProReviewStatus="+ifProReviewStatus);
		}
		

		model.addAttribute("tabMain1",tabMain1);
		
		return forward("befsale");
	}
	
	
	@RequestMapping("/idu")
	public String idu(String qid,String typ,String bocode,String processpage,String ifProReviewStatus,Model model) {
		model.addAttribute("qid",qid);
		model.addAttribute("typ",typ);
		model.addAttribute("bocode",bocode);
		model.addAttribute("processpage",processpage);
		model.addAttribute("ifProReviewStatus",ifProReviewStatus);
		
		String cType = typ;
		
		if(qid==null){
			throw new AnneException("没有找到id数据");
		}
		KstarIdu idu = quotService.getKstarIdu(qid,cType);
		if(idu==null){
			this.initPrjdtl(qid, cType);
		}
		model.addAttribute("iduInf",idu);
		
		return forward("idu");
	}
	
	
	@ResponseBody
	@RequestMapping(value="/idu",method=RequestMethod.POST)
	public String editIdu(KstarIdu idu, HttpServletRequest request){
		String cType = request.getParameter("typ");
		
		List<IUploadFile> list = UploadUtils.uploadFile(request, "/aaaa");
		
		if(!(list.isEmpty())){
			idu.setPrjtchRq(list.get(0).getWebPath());
			idu.setPrjtchRqnm(list.get(0).getName());
		}
		
		quotService.updateIdu(idu,cType);
		return sendSuccessMessage();
	}
	
	
	@RequestMapping("/idm")
	public String idm(String qid,String typ,String bocode,String processpage,String ifProReviewStatus,Model model) {
		model.addAttribute("qid",qid);
		model.addAttribute("typ",typ);
		model.addAttribute("bocode",bocode);
		model.addAttribute("processpage",processpage);
		model.addAttribute("ifProReviewStatus",ifProReviewStatus);
		
		String cType = typ;
		
		if(qid==null){
			throw new AnneException("没有找到id数据");
		}
		KstarIdm idm = quotService.getKstarIdm(qid,cType);
		if(idm==null){
			this.initPrjdtl(qid, cType);
		}
		model.addAttribute("idmInf",idm);
		
		return forward("idm");
	}
	
	
	@ResponseBody
	@RequestMapping(value="/idm",method=RequestMethod.POST)
	public String editIdm(KstarIdm idm, HttpServletRequest request){
		String cType = request.getParameter("typ");
		
		List<IUploadFile> list = UploadUtils.uploadFile(request, "/aaaa");
		
		if(!(list.isEmpty())){
			idm.setPrjtchRq(list.get(0).getWebPath());
			idm.setPrjtchRqnm(list.get(0).getName());
		}
		
		quotService.updateIdm(idm,cType);
		return sendSuccessMessage();
	}
	
	
	@RequestMapping("/sngmnt")
	public String sngmnt(String qid,String typ,String bocode,String processpage,String ifProReviewStatus,Model model) {
		model.addAttribute("qid",qid);
		model.addAttribute("typ",typ);
		model.addAttribute("bocode",bocode);
		model.addAttribute("processpage",processpage);
		model.addAttribute("ifProReviewStatus",ifProReviewStatus);
		
		String cType = typ;
		
		if(qid==null){
			throw new AnneException("没有找到id数据");
		}
		KstarSngMnt sngmnt = quotService.getKstarSngMnt(qid,cType);
		if(sngmnt==null){
			this.initPrjdtl(qid, cType);
		}
		model.addAttribute("sngmntInf",sngmnt);
		
		return forward("sngmnt");
	}
	
	@ResponseBody
	@RequestMapping(value="/sngmnt",method=RequestMethod.POST)
	public String editSngmnt(KstarSngMnt sngmnt, HttpServletRequest request){
		String cType = request.getParameter("typ");
		
		List<IUploadFile> list = UploadUtils.uploadFile(request, "/aaaa");
		
		if(!(list.isEmpty())){
			sngmnt.setPrjtchRq(list.get(0).getWebPath());
			sngmnt.setPrjtchRqnm(list.get(0).getName());
		}

		quotService.updateSngMnt(sngmnt,cType);
		return sendSuccessMessage();
	}
	
	
	@RequestMapping("/sngrck")
	public String sngrck(String qid,String typ,String bocode,String processpage,String ifProReviewStatus,Model model) {
		model.addAttribute("qid",qid);
		model.addAttribute("typ",typ);
		model.addAttribute("bocode",bocode);
		model.addAttribute("processpage",processpage);
		model.addAttribute("ifProReviewStatus",ifProReviewStatus);
		
		String cType = typ;
		
		if(qid==null){
			throw new AnneException("没有找到id数据");
		}
		KstarSngRck sngrck = quotService.getKstarSngRck(qid,cType);
		if(sngrck==null){
			this.initPrjdtl(qid, cType);
		}
		model.addAttribute("sngrckInf",sngrck);
		
		return forward("sngrck");
	}
	
	
	@ResponseBody
	@RequestMapping(value="/sngrck",method=RequestMethod.POST)
	public String editSngRck(KstarSngRck sngrck, HttpServletRequest request){
		String cType = request.getParameter("typ");
		
		List<IUploadFile> list = UploadUtils.uploadFile(request, "/aaaa");
		
		if(!(list.isEmpty())){
			sngrck.setPrjtchRq(list.get(0).getWebPath());
			sngrck.setPrjtchRqnm(list.get(0).getName());
		}
		
		quotService.updateSngRck(sngrck,cType);
		return sendSuccessMessage();
	}
	
	
	@RequestMapping("/sngelec")
	public String sngelec(String qid,String typ,String bocode,String processpage,String ifProReviewStatus,Model model) {
		model.addAttribute("qid",qid);
		model.addAttribute("typ",typ);
		model.addAttribute("bocode",bocode);
		model.addAttribute("processpage",processpage);
		model.addAttribute("ifProReviewStatus",ifProReviewStatus);
		
		String cType = typ;
		
		if(qid==null){
			throw new AnneException("没有找到id数据");
		}
		KstarSngElec sngelec = quotService.getKstarSngElec(qid,cType);
		if(sngelec==null){
			this.initPrjdtl(qid, cType);
		}
		model.addAttribute("sngelecInf",sngelec);
		
		return forward("sngelec");
	}
	
	
	@ResponseBody
	@RequestMapping(value="/sngelec",method=RequestMethod.POST)
	public String editSngelec(KstarSngElec sngelec, HttpServletRequest request){
		String cType = request.getParameter("typ");
		quotService.updateSngelec(sngelec,cType);
		return sendSuccessMessage();
	}
	
	@RequestMapping("/sngclr")
	public String sngclr(String qid,String typ,String bocode,String processpage,String ifProReviewStatus,Model model) {
		model.addAttribute("qid",qid);
		model.addAttribute("typ",typ);
		model.addAttribute("bocode",bocode);
		model.addAttribute("processpage",processpage);
		model.addAttribute("ifProReviewStatus",ifProReviewStatus);
		
		String cType = typ;
		
		if(qid==null){
			throw new AnneException("没有找到id数据");
		}
		KstarSngClr sngclr = quotService.getKstarSngClr(qid,cType);
		if(sngclr==null){
			this.initPrjdtl(qid, cType);
		}
		model.addAttribute("sngclrInf",sngclr);
		
		return forward("sngclr");
	}
	
	@ResponseBody
	@RequestMapping(value="/sngclr",method=RequestMethod.POST)
	public String editSngclr(KstarSngClr sngclr, HttpServletRequest request){
		String cType = request.getParameter("typ");
		
		List<IUploadFile> list = UploadUtils.uploadFile(request, "/aaaa");
		
		if(!(list.isEmpty())){
			sngclr.setPrjtchRq(list.get(0).getWebPath());
			sngclr.setPrjtchRqnm(list.get(0).getName());
		}
		
		quotService.updateSngclr(sngclr,cType);
		return sendSuccessMessage();
	}
	
	
	@RequestMapping("/sngups")
	public String sngups(String qid,String typ,String bocode,String processpage,String ifProReviewStatus,Model model) {
		model.addAttribute("qid",qid);
		model.addAttribute("typ",typ);
		model.addAttribute("bocode",bocode);
		model.addAttribute("processpage",processpage);
		model.addAttribute("ifProReviewStatus",ifProReviewStatus);
		
		String cType = typ;
		
		if(qid==null){
			throw new AnneException("没有找到id数据");
		}
		KstarSngUps sngups = quotService.getKstarSngUps(qid,cType);
		if(sngups==null){
			this.initPrjdtl(qid, cType);
		}
		model.addAttribute("sngupsInf",sngups);
		
		return forward("sngups");
	}
	
	@ResponseBody
	@RequestMapping(value="/sngups",method=RequestMethod.POST)
	public String editSngups(KstarSngUps sngups, HttpServletRequest request){
		String cType = request.getParameter("typ");
		
		List<IUploadFile> list = UploadUtils.uploadFile(request, "/aaaa");
		
		if(!(list.isEmpty())){
			sngups.setElecDrf(list.get(0).getWebPath());
			sngups.setElecDrfnm(list.get(0).getName());
			if(list.size() > 1){
				sngups.setPrjtchRq(list.get(1).getWebPath());
				sngups.setPrjtchRqnm(list.get(1).getName());
			}
		}
		
		quotService.updateSngups(sngups,cType);
		return sendSuccessMessage();
	}
	
	
	@RequestMapping("/sngbty")
	public String sngbty(String qid,String typ,String bocode,String processpage,String ifProReviewStatus,Model model) {
		model.addAttribute("qid",qid);
		model.addAttribute("typ",typ);
		model.addAttribute("bocode",bocode);
		model.addAttribute("processpage",processpage);
		model.addAttribute("ifProReviewStatus",ifProReviewStatus);
		
		String cType = typ;
		
		if(qid==null){
			throw new AnneException("没有找到id数据");
		}
		KstarSngBty sngbty = quotService.getKstarSngBty(qid,cType);
		if(sngbty==null){
			this.initPrjdtl(qid, cType);
		}
		model.addAttribute("sngbtyInf",sngbty);
		
		return forward("sngbty");
	}
	
	
	@ResponseBody
	@RequestMapping(value="/sngbty",method=RequestMethod.POST)
	public String editSngbty(KstarSngBty sngbty, HttpServletRequest request){
		String cType = request.getParameter("typ");
		
		List<IUploadFile> list = UploadUtils.uploadFile(request, "/aaaa");
		
		if(!(list.isEmpty())){
			sngbty.setPrjtchRq(list.get(0).getWebPath());
			sngbty.setPrjtchRqnm(list.get(0).getName());
			if(list.size() > 1){
				sngbty.setEnvlytDrt(list.get(1).getWebPath());
				sngbty.setEnvlytDrtnm(list.get(1).getName());
			}
		}
		
		quotService.updateSngbty(sngbty,cType);
		return sendSuccessMessage();
	}
	
	
	@RequestMapping("/baseInf")
	public String baseInf(String qid,String typ,String processpage,String ifProReviewStatus,Model model) {
		model.addAttribute("qid",qid);
		model.addAttribute("typ",typ);
		
		model.addAttribute("processpage",processpage);
		model.addAttribute("ifProReviewStatus",ifProReviewStatus);
		
		String cType = typ;
		
		if(qid==null){
			throw new AnneException("没有找到数据");
		}
		KstarBaseInf baseinf = quotService.getKstarBaseInf(qid,cType);
		
		if(baseinf==null){
			this.initPrjdtl(qid, cType);
		}
		
		model.addAttribute("baseinfInf",baseinf);
		

		return forward("baseInf");

	}
	
	@ResponseBody
	@RequestMapping(value="/baseInf",method=RequestMethod.POST)
	public String editBaseInf(KstarBaseInf baseinf, HttpServletRequest request){
		String cType = request.getParameter("typ");
		String qid = request.getParameter("qid");
		baseinf.setQuotCode(qid);
		
		List<IUploadFile> list = UploadUtils.uploadFile(request, "/aaaa");
		
		if(!(list.isEmpty())){
			baseinf.setDraft(list.get(0).getWebPath());
			baseinf.setDraftNm(list.get(0).getName());
			if(list.size() > 1){
				baseinf.setMhLyt(list.get(1).getWebPath());
				baseinf.setMhLytNm(list.get(1).getName());
				if(list.size() > 2){
					baseinf.setOrdRq(list.get(2).getWebPath());
					baseinf.setOrdRqnm(list.get(2).getName());
				}
			}
		}
		

		
		
		
		//
		KstarPgInf pginf =  new KstarPgInf();
		pginf.setQuotCode(baseinf.getQuotCode());
		
		Long baseinfcount;
		
		baseinfcount = quotService.countKstarPgInf(cType, "UPS", baseinf.getQuotCode());
		
		if(baseinf.getIfups()==null){
			baseinf.setIfups("0");
			
			if (baseinfcount>0){
				quotService.deleteKstarPgInf(cType, "UPS", baseinf.getQuotCode());
			}
			
		}else{
			
			if (baseinfcount<1){
				pginf.setPgTyp("售前界面");
				pginf.setRelPrd("UPS");
				quotService.savePgInf(pginf, cType);
			}

		}
		
		baseinfcount = quotService.countKstarPgInf(cType, "电池", baseinf.getQuotCode());
		
		if(baseinf.getIfbattry()==null){
			baseinf.setIfbattry("0");
		
			if (baseinfcount>0){
				quotService.deleteKstarPgInf(cType, "电池", baseinf.getQuotCode());
			}
		}else{
			
			if (baseinfcount<1){
				pginf.setPgTyp("售前界面");
				pginf.setRelPrd("电池");
				quotService.savePgInf(pginf, cType);
			}

		}
		
		
		baseinfcount = quotService.countKstarPgInf(cType, "精密空调", baseinf.getQuotCode());
		
		if(baseinf.getIfclr()==null){
			baseinf.setIfclr("0");

			if (baseinfcount>0){
				quotService.deleteKstarPgInf(cType, "精密空调", baseinf.getQuotCode());
			}
		}else{
			
			if (baseinfcount<1){
				pginf.setPgTyp("售前界面");
				pginf.setRelPrd("精密空调");
				quotService.savePgInf(pginf, cType);
			}

		}
		
		baseinfcount = quotService.countKstarPgInf(cType, "配电", baseinf.getQuotCode());
		
		if(baseinf.getIfelec()==null){
			baseinf.setIfelec("0");

			if (baseinfcount>0){
				quotService.deleteKstarPgInf(cType, "配电", baseinf.getQuotCode());
			}
		}else{
			
			if (baseinfcount<1){
				pginf.setPgTyp("售前界面");
				pginf.setRelPrd("配电");
				quotService.savePgInf(pginf, cType);
			}

		}
		
		baseinfcount = quotService.countKstarPgInf(cType, "机柜", baseinf.getQuotCode());
		
		if(baseinf.getIfrck()==null){
			baseinf.setIfrck("0");

			if (baseinfcount>0){
				quotService.deleteKstarPgInf(cType, "机柜", baseinf.getQuotCode());
			}
		}else{
			
			if (baseinfcount<1){
				pginf.setPgTyp("售前界面");
				pginf.setRelPrd("机柜");
				quotService.savePgInf(pginf, cType);
			}

		}
		
		baseinfcount = quotService.countKstarPgInf(cType, "监控", baseinf.getQuotCode());
		
		if(baseinf.getIfmnt()==null){
			baseinf.setIfmnt("0");

			if (baseinfcount>0){
				quotService.deleteKstarPgInf(cType, "监控", baseinf.getQuotCode());
			}
		}else{
			
			if (baseinfcount<1){
				pginf.setPgTyp("售前界面");
				pginf.setRelPrd("监控");
				quotService.savePgInf(pginf, cType);
			}

		}
		
		baseinfcount = quotService.countKstarPgInf(cType, "IDM", baseinf.getQuotCode());
		
		if(baseinf.getIfidm()==null){
			baseinf.setIfidm("0");

			if (baseinfcount>0){
				quotService.deleteKstarPgInf(cType, "IDM", baseinf.getQuotCode());
			}
		}else{
			
			if (baseinfcount<1){
				pginf.setPgTyp("售前界面");
				pginf.setRelPrd("IDM");
				quotService.savePgInf(pginf, cType);
			}

		}
		
		baseinfcount = quotService.countKstarPgInf(cType, "IDU", baseinf.getQuotCode());
		
		if(baseinf.getIfidu()==null){
			baseinf.setIfidu("0");

			if (baseinfcount>0){
				quotService.deleteKstarPgInf(cType, "IDU", baseinf.getQuotCode());
			}
		}else{
			
			if (baseinfcount<1){
				pginf.setPgTyp("售前界面");
				pginf.setRelPrd("IDU");
				quotService.savePgInf(pginf, cType);
			}

		}
		
		baseinfcount = quotService.countKstarPgInf(cType, "IDR", baseinf.getQuotCode());
		
		if(baseinf.getIfidr()==null){
			baseinf.setIfidr("0");

			if (baseinfcount>0){
				quotService.deleteKstarPgInf(cType, "IDR", baseinf.getQuotCode());
			}
		}else{
			
			if (baseinfcount<1){
				pginf.setPgTyp("售前界面");
				pginf.setRelPrd("IDR");
				quotService.savePgInf(pginf, cType);
			}

		}
		
		quotService.updateBaseInf(baseinf,cType);

		return sendSuccessMessage();
	}

		
	
	@RequestMapping("/aftsale")
	public String aftsale(String qid,String typ,String processpage,String ifProReviewStatus,Model model) {
		model.addAttribute("qid",qid);
		model.addAttribute("processpage",processpage);
		model.addAttribute("ifProReviewStatus",ifProReviewStatus);
		model.addAttribute("typ",typ);

		String cType = typ;
		
		
		if(qid==null){
			throw new AnneException("没有找到数据");
		}
		KstarAftSale aftsale = quotService.getKstarAftSale(qid,cType);

		model.addAttribute("aftsaleInf",aftsale);
		
		return forward("aftsale");
	}
	
	@ResponseBody
	@RequestMapping(value="/aftsale",method=RequestMethod.POST)
	public String editAftsale(KstarAftSale aftsale, HttpServletRequest request){
		String cType = request.getParameter("typ");
		
		List<IUploadFile> list = UploadUtils.uploadFile(request, "/aaaa");
		
		if(!(list.isEmpty())){
			aftsale.setDvcDrt(list.get(0).getWebPath());
			aftsale.setDvcDrtnm(list.get(0).getName());
			if(list.size() > 1){
				aftsale.setUmbDrf(list.get(1).getWebPath());
				aftsale.setUmbDrfnm(list.get(1).getName());
			}
		}
		
		quotService.updateAftsale(aftsale,cType);
		return sendSuccessMessage();
	}
	
	
	@RequestMapping("/biddcevl")
	public String biddcevl(String qid,Model model) {
		model.addAttribute("qid",qid);
		return forward("biddcevl");
	}
	@NoRight
	@ResponseBody
	@RequestMapping(value="/biddcevlPage")
	public String pageBiddcevl(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		condition.setCondition("qid", request.getParameter("qid"));
		IPage p = quotService.queryBiddcevl(condition);

		return sendSuccessMessage(p);
	}
	
	
	@RequestMapping("/prjpages")
	public String prjpages(String qid,String typ,String processpage,String ifProReviewStatus,Model model) {
		String cType = typ;
		String bocode = "";
		
		model.addAttribute("processpage",processpage);
		model.addAttribute("ifProReviewStatus",ifProReviewStatus);
		
		//generate aftsale 
		KstarAftSale aftsale = quotService.getKstarAftSale(qid,cType);
		if(aftsale==null){
			quotService.saveAftsale(aftsale,qid,cType);			
		}
		
		this.initPrjdtl(qid, cType);
		
		KstarQuot quot = quotService.getKstarQuot(qid);
		
		if(cType.equals("0003")){
			if(quot.getBoCode()!=null){
				bocode = quot.getBoCode();
			}
		}

		
		model.addAttribute("bocode",bocode);
		
		TabMain tabMain = new TabMain();
		tabMain.setInitAll(false);
		
		if(this.hasPermission("P04T2ProjFormT1OverView")){
			tabMain.addTab("界面总览", "/quot/pginf.html?qid="+qid+"&typ="+typ);
		}
		
		if(this.hasPermission("P04T2ProjFormT2BasicInfoSave")){
			tabMain.addTab("项目基本信息", "/quot/baseInf.html?qid="+qid+"&typ="+typ+"&processpage="+processpage+"&ifProReviewStatus="+ifProReviewStatus);
		}
		
		if(this.hasPermission("P04T2ProjFormT3Presale")){
			tabMain.addTab("售前部分", "/quot/befsale.html?qid="+qid+"&typ="+typ+"&bocode="+bocode+"&processpage="+processpage+"&ifProReviewStatus="+ifProReviewStatus);
		}
		
		if(this.hasPermission("P04T2ProjFormT4AftersaleSave")){
			tabMain.addTab("售后部分", "/quot/aftsale.html?qid="+qid+"&typ="+typ+"&processpage="+processpage+"&ifProReviewStatus="+ifProReviewStatus);
		}

		
		model.addAttribute("tabMain",tabMain);
		
		return forward("prjpages");
	}
	
	
	public void initPrjdtl(String qid,String typ){
		
		String cType = typ;
		
		//generate baseinf 
		KstarBaseInf baseinf = quotService.getKstarBaseInf(qid,cType);
		if(baseinf==null){
			quotService.saveBaseinf(baseinf,qid,cType);			
		}
		
		//generate idu 
		KstarIdu idu = quotService.getKstarIdu(qid,cType);
		if(idu==null){
			quotService.saveIdu(idu, qid, cType);			
		}
		
		//generate idm 
		KstarIdm idm = quotService.getKstarIdm(qid,cType);
		if(idm==null){
			quotService.saveIdm(idm, qid,cType);			
		}
		
		//generate sngups 
		KstarSngUps sngups = quotService.getKstarSngUps(qid,cType);
		if(sngups==null){
			quotService.saveSngUps(sngups, qid,cType);		
		}
		
		
		//generate sngbty 
		KstarSngBty sngbty = quotService.getKstarSngBty(qid,cType);
		if(sngbty==null){
			quotService.saveSngbty(sngbty, qid,cType);			
		}
		
		//generate sngelec 
		KstarSngElec sngelec = quotService.getKstarSngElec(qid,cType);
		if(sngelec==null){
			quotService.saveSngelec(sngelec, qid,cType);			
		}
		
		//generate sngclr 
		KstarSngClr sngclr = quotService.getKstarSngClr(qid,cType);
		if(sngclr==null){
			quotService.saveSngclr(sngclr, qid,cType);			
		}
		
		//generate sngrck 
		KstarSngRck sngrck = quotService.getKstarSngRck(qid,cType);
		if(sngrck==null){
			quotService.saveSngrck(sngrck, qid,cType);	
		}
		
		//generate sngmnt 
		KstarSngMnt sngmnt = quotService.getKstarSngMnt(qid,cType);
		if(sngmnt==null){
			quotService.saveSngmnt(sngmnt, qid,cType);	
		}
		
		
	}
	
	
	public String Checkversion(String qid,String typ){
		String result = "Y";
		KstarQuot quot = quotService.getKstarQuot(qid);
		
		if(Integer.parseInt(quot.getQuotVersion())<2){
			result = "N";
		}
		
		return result;
	}
	
	
	public String Checkevlstatus(String qid,String typ){
		String result = quotService.Checkprjevlstatus(qid);
		return result;
	}
	
	public String Checkevlststatus(String qid,String typ){
		String result = quotService.Checkprjevlststatus(qid);
		return result;
	}
	
	
	public String CheckSprvmrk(KstarQuot quot){
		String qid = quot.getId();
		String result = quotService.CheckSprvmrkStatus(qid);
		return result;
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/unlockSpProcess", method = RequestMethod.POST)
	public String unlockSpProcess(String qid,HttpServletRequest request) throws Exception{
		
		if(qid==null){
			throw new AnneException("没有找到数据");
		}
		KstarQuot quot = quotService.getKstarQuot(qid);
		
		//是否投标项目字段判断 如果非投标项目则只允许申请特价审批一次
		if("0".equals(quot.getIsBidPro())){
			String processName = "特价审批流程";
			List<KstarProductWorkFlow> wfs;
			wfs = quotService.getKstarProductWorkFlowList(qid, processName);
			if(wfs.size()>0){
				for(KstarProductWorkFlow wf: wfs){
					String processid = wf.getProcessID();
					List<HistoryProcessInstance> his = quotService.getHistoryProInstance(processid);
					if(his != null && his.size() > 0){
						throw new AnneException("报价单已申请过特价审批,非投标项目只能特价审批一次！");
					}
				}
			}
		}
		
		quot.setSpUlkstatus("Y");
		
		quotService.updateQuot(quot);
		
		return sendSuccessMessage();
	}
	
	
	
	
	@RequestMapping("/tabs")
	public String tabs(String qnm,String qid,String typ,Model model) {
		model.addAttribute("qnm",qnm);
		model.addAttribute("qid",qid);
		model.addAttribute("typ",typ);
				
		model.addAttribute("org",JSON.toJSONString(lovMemberService.get(getUserObject().getOrg().getId())));
		
		String checkver = Checkversion(qid, typ);
		String ckevlstatus = Checkevlstatus(qid, typ);
		//未启动状态
		String ckevlststatus = Checkevlststatus(qid, typ);
		String pricetableid = "";
		
		//非审批流程处理页面
		String processpage = "N";
		
		
		if(qnm==null){
			throw new AnneException("没有找到数据");
		}
		KstarQuot quot = quotService.getKstarQuot(qnm);
		if(quot==null){
			throw new AnneException("没有找到数据");
		}
		model.addAttribute("quotInf",quot);
		
		String orgid =  lovMemberService.get(getUserObject().getOrg().getId()).getId();
		
		quot.setSalDep(orgid);
		
		LovMember makDepLov = lovMemberService.getLovMemberByCode("ORG", lovMemberService.getSaleCenter(getUserObject().getOrg().getId()));
		
		if(makDepLov!=null){
			quot.setMarketDept(makDepLov.getId());
		}
		
		
		if(quot.getPriceListid()!=null){
			
			ProductPriceHead pricehead = priceheadservice.queryLpcById(quot.getPriceListid());
			
			if(pricehead.getCurrency()!=null){
				quot.setCurrency(pricehead.getCurrency());
			}
			
			pricetableid = quot.getPriceListid();
		}
		
		model.addAttribute("pricetableid",pricetableid);
		
		
		//投标结果反馈后，不能再“修订”报价单
		String ifbidresult = "Y";
		if(quot.getBidResults()!=null){
			ifbidresult = "N";
		}
		
		if(quot.getIsValid().equals("0")){
			ifbidresult = "N";
			processpage = "P3";
		}
		
		model.addAttribute("ifbidresult",ifbidresult);
		
		//提交报价标志
		String ifsubmitted = "N";
		if(quot.getIfsubmitted()!=null){
			if(quot.getIfsubmitted().equals("Y")){
				ifsubmitted = "Y";
			}
		}
		
		model.addAttribute("ifsubmitted",ifsubmitted);
		
		//工程评审完成后，报价头信息（除“投标结果”字段）与工程清单、工程界面、工程评审信息只读，不允许新增、修改、删除
		String ifProReviewStatus = "N";
		if(quot.getProReviewStatus().equals("S02")){
			ifProReviewStatus = "Y";
		}
		model.addAttribute("ifProReviewStatus",ifProReviewStatus);
		
		
		//更新特价审批标志
		//quotService.updateSprvmrk(getUserObject(), quot);
		
		String sprvmrkstatus = CheckSprvmrk(quot);

		model.addAttribute("sprvmrkstatus",sprvmrkstatus);

		
		//检查提交报价标志
		String quotsubmitstatus = quotService.CheckQuotsubmitStatus(quot.getId());
		
		model.addAttribute("quotsubmitstatus",quotsubmitstatus);
		
		
		//this.initPrjdtl(qnm, typ);

		
		
		/**
		 * processpage参数值:
		 * 
		 * 报价状态-新建 && 工程评审状态-审批中 && 标书审核状态-未启动：P1
		 * 报价状态-新建 && 工程评审状态-已驳回 && 标书审核状态-未启动：P2
		 * 报价状态-新建 && 工程评审状态-已审批 && 标书审核状态-未启动：P3
		 * 
		 *
		 * 报价状态-新建 && 工程评审状态-已审批 && 标书审核状态-审批中：P5
		 * 报价状态-新建 && 工程评审状态-已审批 && 标书审核状态-已驳回：P6
		 * 报价状态-新建 && 工程评审状态-已审批 && 标书审核状态-已审批：P7
		 * 
		 * 报价状态-赢标 && 工程评审状态-已审批 && 标书审核状态-已审批: P8
		 * 报价状态-丢标: P9
		 * 报价状态-关闭: P10
		 * 
		 * 
		 */
		
		//报价状态-新建 && 工程评审状态-审批中 && 标书审核状态-未启动：P1
		if(quot.getStatus().equals("S01")){
			if(quot.getBidAuditStatus().equals("B03")){
				if(quot.getProReviewStatus().equals("S01")){
					processpage = "P1";
				}
			}
		}
		
		//报价状态-新建 && 工程评审状态-已驳回 && 标书审核状态-未启动：P2
		if(quot.getStatus().equals("S01")){
			if(quot.getProReviewStatus().equals("S04")){
				if(quot.getBidAuditStatus().equals("B03")){
					processpage = "P2";
				}
			}
		}
		
		//报价状态-新建 && 工程评审状态-已审批 && 标书审核状态-未启动：P3
		if(quot.getStatus().equals("S01")){
			if(quot.getProReviewStatus().equals("S02")){
				if(quot.getBidAuditStatus().equals("B03")){
					processpage = "P3";
				}
			}
		}
		
		
		//报价状态-新建 && 工程评审状态-已审批 && 标书审核状态-审批中：P5
		if(quot.getStatus().equals("S01")){
			if(quot.getProReviewStatus().equals("S02")){
				if(quot.getBidAuditStatus().equals("B01")){
					processpage = "P5";
				}
			}
		}
		
		//报价状态-新建 && 工程评审状态-已审批 && 标书审核状态-已驳回：P6
		if(quot.getStatus().equals("S01")){
			if(quot.getProReviewStatus().equals("S02")){
				if(quot.getBidAuditStatus().equals("B04")){
					processpage = "P6";
				}
			}
		}
		
		//报价状态-新建 && 工程评审状态-已审批 && 标书审核状态-已审批：P7
		if(quot.getStatus().equals("S01")){
			if(quot.getProReviewStatus().equals("S02")){
				if(quot.getBidAuditStatus().equals("B02")){
					processpage = "P7";
				}
			}
		}
		
		//报价状态-赢标 && 工程评审状态-已审批 && 标书审核状态-已审批: P8
		if(quot.getStatus().equals("S02")){
			if(quot.getProReviewStatus().equals("S02")){
				if(quot.getBidAuditStatus().equals("B02")){
					processpage = "P8";
				}
			}
		}
		
		//报价状态-丢标: P9
		if(quot.getStatus().equals("S03")){
			processpage = "P9";
		}
		
		//报价状态-关闭: P10
		if(quot.getStatus().equals("S04")){
			processpage = "P10";
		}
		
		//价格评审状态-审批中 || 价格评审状态-已审批
		if(quot.getPrcAdtstatus().equals("S01")||quot.getPrcAdtstatus().equals("S02")){
			processpage = "P11";
		}
		
		//特价审批状态-审批中 || 特价审批状态-已审批
		if(quot.getSpAuditStatus().equals("P01")||quot.getSpAuditStatus().equals("P02")){
			processpage = "P11";
		}
			
		
		//特价申请解锁字段： 特价申请按钮已点击  && 特价申请解锁字段状态-Y && 特价审核状态-未启动  && 价格评审状态-已审批
		
		
		
		//特价申请解锁字段： 特价申请按钮已点击  && 特价申请解锁字段状态-Y && 特价审核状态-未启动 
		if(quot.getSpUlkstatus()!=null){
			if(quot.getSpUlkstatus().equals("Y")){
//				if(quot.getPrcAdtstatus().equals("S02")){
					if(quot.getSpAuditStatus().equals("P03")){
						processpage = "P12";
					}
//				}
			}
		}

		
		
		model.addAttribute("processpage",processpage);
		

		
		String applycont = "Y";
		
		//业务单元=”国内数据中心“ and 投标项目=”是“ and 技术评审状态 <>"已审批" and 标书审核状态<>"已审批" 
		
		if("国内数据中心".equals(quot.getMarketDeptName())){
			if(quot.getIsBidPro().equals("1")){
				if(!quot.getTchAdtstatus().equals("S02")||!quot.getBidAuditStatus().equals("B02")){
					applycont = "N";
				}
			}
		}
		
		
		model.addAttribute("applycont",applycont);
		
		
		
		//业务单元=”国内数据中心“ and 投标项目=”是“  and 产品行中公开价格为空 and 价格评审状态<>"已审批" 
		
		String applycontb = "Y";
		
		String ifprdSprc = quotService.checkKstarPrjLstwithprdSprc(qid);
		
		if("国内数据中心".equals(quot.getMarketDeptName())){
			if(quot.getIsBidPro().equals("1")){
				if(ifprdSprc.equals("Y")){
					if(!quot.getPrcAdtstatus().equals("S02")){
						applycontb = "N";
					}
				}
			}
		}
		
		model.addAttribute("applycontb",applycontb);
		
		
		
		String bidstatus = "";
		
		if(quot.getBidAuditStatus().equals("B01")){
			bidstatus = "bid";
		}
		
		model.addAttribute("bidstatus",bidstatus);
		
		
		//申请合同时，若该报价单已关联合同，且合同的状态不为“已废弃”时，弹出报错提示：
		//当前报价单已申请过合同，若需要修改报价信息请将合同：XXX(合同编码)废弃后再申请新合同
		String contractStatus = quotService.CheckContractStatus(qid);
		String cntId = "";
		
		if(contractStatus.equals("Y")){
			cntId = quotService.getContractID(qid);
		}
		
		//cntId= "zzz";
		
		model.addAttribute("contractStatus",contractStatus);
		model.addAttribute("cntId",cntId);
		
		
		String isprorview = "";
		if(quot.getIsProReview().equals("0")){
			isprorview = "N" ;
		}
		
		
		model.addAttribute("isprorview",isprorview);
		
		String applyPrcStatus="";
		
		PageCondition condition = new PageCondition();
		condition.setCondition("qid", qid);
		condition.setCondition("typ", typ);
		IPage p = quotService.queryPrjLst(condition);
		List<KstarPrjLst> kstarPrjLsts = (List<KstarPrjLst>) p.getList();
		KstarQuot kstarQuot = quotService.getKstarQuot(qid);
		boolean needRebateReview = false;
		if(kstarPrjLsts.size()>0){
			for (KstarPrjLst kstarPrjLst : kstarPrjLsts) {
				if(kstarPrjLst.getApplyPrc()!=null&&kstarPrjLst.getGoldPrc()!=null){
					if((kstarPrjLst.getApplyPrc()>kstarPrjLst.getGoldPrc())){
						applyPrcStatus = "1";
						model.addAttribute(applyPrcStatus,"1");
					}else{
						applyPrcStatus = "2";
						model.addAttribute(applyPrcStatus,"2");
					}
				}else{
					model.addAttribute("status","3");
				}
				if(kstarPrjLst.getApplyPrc()!=null && kstarPrjLst.getGoldPrc()!=null){
					if("P02".equals(kstarQuot.getSpAuditStatus())){
						model.addAttribute("status","3");
					}else if(kstarPrjLst.getApplyPrc()<kstarPrjLst.getGoldPrc()){
						model.addAttribute("status","1");
						needRebateReview = true;
					}else{
						model.addAttribute("status","3");
					}
				}
			}
		}else{
			model.addAttribute("status","2");
			applyPrcStatus = "2";
			model.addAttribute(applyPrcStatus,"2");
		}

		if (needRebateReview) {
			model.addAttribute("status", "1");
		}
		
		TabMain tabMainbgn = new TabMain();
		tabMainbgn.setInitAll(false);
		
		if(this.hasPermission("P04T1ProjList1")){
			tabMainbgn.addTab("工程清单", "/quot/prjLst.html?qid="+qid+"&typ="+typ+"&pricetableid="+pricetableid+"&sprvmrkstatus="+sprvmrkstatus+"&quotsubmitstatus="+quotsubmitstatus+"&processpage="+processpage+"&ifProReviewStatus="+ifProReviewStatus+"&applyPrcStatus="+applyPrcStatus);
		}

		if(this.hasPermission("P04T2ProjForm")){
			tabMainbgn.addTab("工程界面", "/quot/prjpages.html?qid="+qid+"&typ="+typ+"&processpage="+processpage+"&ifProReviewStatus="+ifProReviewStatus);
		}
		
		if(this.hasPermission("P04T3File1")){
			tabMainbgn.addTab("附件", "/common/attachment/attachment.html?businessId="+qid+"&businessType=QUOTE_FILE&docGroupCode=quotatttyp");
		}
		
		//tabMainbgn.addTab("附件", "/quot/att.html?qid="+qid+"&typ="+typ);
		//tabMainbgn.addTab("团队成员", "/quot/mem.html?qid="+qid+"&typ="+typ);
		
		if(this.hasPermission("P04T4TeamPos1")){
			tabMainbgn.addTab("团队成员", "/team/list.html?businessType=QUOTINF"+"&businessId="+qid);
		}
		
		if(this.hasPermission("P04T5OrgList")){
			tabMainbgn.addTab("组织列表", "/orgTeam/list.html?businessType=QUOTINF&businessId="+qid);
		}
		
//		if(this.hasPermission("P04T6ProReview1")){
//			tabMainbgn.addTab("工程评审", "/quot/prjevl.html?qid="+qid+"&typ="+typ+"&checkver="+checkver+"&ckevlstatus="+ckevlstatus+"&ckevlststatus="+ckevlststatus+"&processpage="+processpage+"&ifProReviewStatus="+ifProReviewStatus);
//		}
		
		if(this.hasPermission("P04T7ReviewHistory1")){
			tabMainbgn.addTab("审批历史", "/quot/biddcevl.html?qid="+qid+"&typ="+typ);
		}
		
		if(this.hasPermission("P04T8Contract1")){
			tabMainbgn.addTab("合同", "/quot/contract.html?qid="+qid+"&typ="+typ);
		}

		model.addAttribute("tabMainbgn",tabMainbgn);

		//权限字段
		quot.setUpdatedAt(new Date());
		quot.setUpdatedById(getUserObject().getEmployee().getId());

		
		quotService.updateQuot(quot);
		
			
//		//add new lovmem root
//		String groupId = "PRJLSTPRDCAT";
//		String rootexists = quotService.Checklovroot(qid);
		
//		if(rootexists.equals("Y")){
//			String lovmenid = quotService.addlovroot(qid, typ, groupId);
//		}
		
		
		
		return forward("tabs");
	}
	

	@ResponseBody
	@RequestMapping(value="/tabs",method=RequestMethod.POST)
	public String editTabs(KstarQuot quot){
		
		if(quot.getBidResults()!=null){
			if(quot.getIsBidPro()!=null){
				if(quot.getIsBidPro().equals("1")){
					if(quot.getBidResults().equals("R01")){
						quot.setStatus("S02");
					}
					
					if(quot.getBidResults().equals("R02")){
						quot.setStatus("S03");
					}
				}
			}
		}
		
		quotService.updateQuot(quot);
		return sendSuccessMessage();
	}
	
	
	
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(KstarQuot quot, HttpServletRequest request) {
		
		if(quot.getProReviewStatus()==null){
			quot.setProReviewStatus("S03");
		}
		
		if(quot.getBidAuditStatus()==null){
			quot.setBidAuditStatus("B03");
		}
		
		if(quot.getSpAuditStatus()==null){
			quot.setSpAuditStatus("P03");
		}
		
		if(quot.getPrcAdtstatus()==null){
			quot.setPrcAdtstatus("S03");
		}
		
		if(quot.getTchAdtstatus()==null){
			quot.setTchAdtstatus("S03");
		}
		
		if(quot.getBidRspstatus()==null){
			quot.setBidRspstatus("S03");
		}
		
		if(quot.getMtrReqstatus()==null){
			quot.setMtrReqstatus("S03");
		}
		
		
		quot.setQuotVersion("1");
		quot.setIsValid("1");
		quot.setStatus("S01");
		quot.setSalesRep(getUserObject().getEmployee().getName());
		quot.setSalesRepid(getUserObject().getEmployee().getId());
		quot.setCreator(getUserObject().getEmployee().getName());
		quot.setCreatedById(getUserObject().getEmployee().getId());
		quot.setCreateTime(new Date());
		
		LovMember makDepLov = lovMemberService.getLovMemberByCode("ORG", lovMemberService.getSaleCenter(getUserObject().getOrg().getId()));
		
		quot.setMarketDept(makDepLov.getId());
		
		
//		//权限字段
//		quot.setCreatedOrgId(getUserObject().getOrg().getId());
//		quot.setCreatedById(getUserObject().getEmployee().getId());
//		quot.setCreatedPosId(getUserObject().getPosition().getId());
//		quot.setCreatedAt(new Date());
		
		UserObject current_user = getUserObject();
		
		quotService.saveQuot(quot,current_user);
		
		
//		teamService.addPosition(
//				getUserObject().getPosition().getId(),
//				getUserObject().getEmployee().getId(), 
//				"0003",
//				quot.getId());

		
		return sendSuccessMessage();
	}

	@RequestMapping("/add")
	public String add(Model model){
		return forward("add");
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/addContract", method = RequestMethod.POST)
	public String addCnt(Contract contract, HttpServletRequest request) {
		String qid = request.getParameter("qid");

		KstarQuot quot = quotService.getKstarQuot(qid);
		
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
		
		
		contract.setContrType("ff8080815b1d1242015b1d2dfaee0007");//默认标准合同
		contract.setCreateTime(new Date());
		contract.setCustLinkId(getUserObject().getEmployee().getId());
		contract.setCreatedAt(new Date());
		contract.setCreatedById(getUserObject().getEmployee().getId());
		contract.setCreatedPosId(getUserObject().getPosition().getId());
		contract.setCreatedOrgId(getUserObject().getOrg().getId());
		contractService.save(contract,getUserObject());

//		//权限控制
//		teamService.addPosition(
//				getUserObject().getPosition().getId(),
//				getUserObject().getEmployee().getId(), 
//				IConstants.CONTR_STAND,
//				contract.getId());
//		
//		orgTeamService.configPrimaryOrg(contract.getId(), IConstants.CONTR_STAND, getUserObject().getOrg().getId());

		quotService.updatePrjLstwithlineNum(qid);
		
		quotService.copyPrjdtl(qid, contract.getId(), "Y");
		//quotService.copyLovMemberByMemo(qid, contract.getId());
		
		quotService.copyKstarPrjLstforCont(qid, contract.getId());
		
		contractService.updatePrjlstTypeByContr(contract.getId(),"CONTR_STAND",contract.getContrNo());
		
		return sendSuccessMessage();
	}
	
	
	@RequestMapping("/addContract")
	public String addCnt(String qid,Model model){
		model.addAttribute("qid", qid);
		KstarQuot quot = quotService.getKstarQuot(qid);
		
		
//		String iscont = "N";
//		
//		if(quot.getBidPrj()!=null){
//			if(quot.getBidResults()!=null){
//				if(quot.getBidPrj().equals("0")){
//					if(quot.getBidResults().equals("R03")){
//						iscont = "Y";
//					}
//				}
//				
//				if(quot.getBidPrj().equals("1")){
//					if(quot.getBidResults().equals("R01")){
//						iscont = "Y";
//					}
//				}
//				
//			}
//			
//		}
//		
		
		
		KstarCntr cnt = new KstarCntr();
		cnt.setQuotCode(qid);
		String contrNo = contractService.getContractNumber(); 
		cnt.setCntId(contrNo);
		cnt.setCntTyp("C01");
		quotService.saveCnt(cnt);

		
		
		//
		Contract contract = new Contract();
		contract.setQuotNo(quot.getQuotCode());
		
		contract.setProjNo(quot.getBoCode());
		contract.setProjName(quot.getBoName());
		
		CustomInfo custInfo = custinfoService.getCustomInfoByCode(quot.getCustomerCode());
		
		contract.setCustCode(custInfo.getId());
		contract.setCustName(quot.getCustomerName());
		
		contract.setPricNo(quot.getPriceListid());
		contract.setPricTable(quot.getPriceList());
		
		contract.setContrNo(contrNo);
		contract.setContrVer("1");
		/*
		 * 2017.4.29 增加 合同总金额 
		 */
		contract.setTotalAmt(Double.parseDouble(quot.getAmount()==null?"0":quot.getAmount()));
		//contract.setCreator(getUserObject().getEmployee().getName());
		contract.setCreator(getUserObject().getEmployee().getId());

		LovMember currencyLov = lovMemberService.getLovMemberByCode("CURRENCY", "CNY");
		contract.setCurrency(currencyLov.getId());
		
//		LovMember lov =  ((LovMember)CacheData.getInstance().get(payStat));
		LovMember lov = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "01");
		contract.setReviewStat(lov.getId());
		contract.setTrialStat(lov.getId());
		contract.setFinalReviewStat(lov.getId());
		LovMember statLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "01");
		contract.setContrStat(statLov.getId());
//		contract.setOrg(getUserObject().getOrg().getId());
		LovMember payLov = lovMemberService.getLovMemberByCode("CONTRACTPAYSTAT", "01");
		contract.setPayStat(payLov.getId());
		contract.setIsValid("1");	
		LovMember orgLov = lovMemberService.get(getUserObject().getOrg().getId());
		contract.setOrg(orgLov.getId());
		
		BusinessOpportunity biz = quotService.getBizOppbyId(contract.getProjNo());
		CustomInfo cust = customerService.getCustomInfo(contract.getCustCode()); 
		ProductPriceHead priceTable = priceHeadService.queryLpcById(contract.getPricNo());
		
		contract.setMarketDept(quot.getMarketDept());
		
		//contractService.updatePrjlstTypeByContr(contract.getId(),"CONTR_STAND",contract.getContrNo());
		
//		LovMember makDepLov = lovMemberService.getLovMemberByCode("ORG", lovMemberService.getSaleCenter(getUserObject().getOrg().getId()));
//		contract.setMarketDept(makDepLov.getId());
		
		if(biz!=null){
		model.addAttribute("project",JSON.toJSONString(biz));
		}
		if(cust!=null){
		model.addAttribute("customer",JSON.toJSONString(cust));
		}
		if(priceTable!=null){
		model.addAttribute("priceTable",JSON.toJSONString(priceTable));
		}
		
		model.addAttribute("contr", contract);
		model.addAttribute("org",JSON.toJSONString(lovMemberService.get(getUserObject().getOrg().getId())));
		
		// 是否国际合同标识
		String ordDivFlg = "Y";
		if(getUserObject().getOrg().getNamePath().contains(IConstants.CONTR_TYPE_X_ORG_STR)){
			ordDivFlg ="N";
		}
		model.addAttribute("ordDivFlg", ordDivFlg);
		LovMember defaultContractType = lovMemberService.getLovMemberByCode("CONTRACTTYPE", IConstants.CONTR_STAND_0101);
		model.addAttribute("defaultContractType", defaultContractType.getId());
		contract.setContrType(defaultContractType.getId());

		//
//		if (iscont.equals("Y")){
//			return forward("contractpg");
//		}else{
//			return forward("addcontracterr");
//		}
		
		//return forward("contractpg");
		return forward("../contract/standard/contract");
	}
	
	
	
	
	@RequestMapping("/addloancontract")
	public String loancontract(String qid,Model model) {
//		String dep="X";
		String contrNo = contractLoanService.getContractLoanNumber(); 
		Contract contract = new Contract();
		
		//
		KstarCntr cnt = new KstarCntr();
		cnt.setQuotCode(qid);
		cnt.setCntId(contrNo);
		//
		cnt.setCntTyp("C05");
		quotService.saveCnt(cnt);
		
		model.addAttribute("qid", qid);
		KstarQuot quot = quotService.getKstarQuot(qid);

		contract.setQuotNo(quot.getQuotCode());
		
		contract.setProjNo(quot.getBoCode());
		contract.setProjName(quot.getBoName());
		
		CustomInfo custInfo = custinfoService.getCustomInfoByCode(quot.getCustomerCode());
		
		contract.setCustCode(custInfo.getId());
		contract.setCustName(quot.getCustomerName());
		
		contract.setPricNo(quot.getPriceListid());
		contract.setPricTable(quot.getPriceList());

		
		contract.setContrNo(contrNo);
		contract.setContrVer("1");
		contract.setCreator(getUserObject().getEmployee().getId());
//		LovMember lov =  ((LovMember)CacheData.getInstance().get(payStat));
		LovMember lov = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "01");
//		contract.setReviewStat(lov.getId());
		contract.setTrialStat(lov.getId());
//		contract.setFinalReviewStat(lov.getId());
		LovMember statLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "01");
		contract.setContrStat(statLov.getId());
//		contract.setOrg(getUserObject().getOrg().getId());
		LovMember payLov = lovMemberService.getLovMemberByCode("CONTRACTPAYSTAT", "01");
		contract.setPayStat(payLov.getId());
		LovMember contrTypeLov = lovMemberService.getLovMemberByCode("CONTRACTTYPE", IConstants.CONTR_LOAN_0101); // 借货合同申请
		contract.setContrType(contrTypeLov.getId());
		contract.setIsValid("1");	
		/*
		 * 2017.4.29 增加 合同总金额 
		 */
		contract.setTotalAmt(Double.parseDouble(quot.getAmount()==null?"0":quot.getAmount()));
		contract.setCreateTime(new Date());
		LovMember orgLov = lovMemberService.get(getUserObject().getOrg().getId());
		contract.setOrg(orgLov.getId());
		
		contract.setMarketDept(quot.getMarketDept());
		
		//contractService.updatePrjlstTypeByContr(contract.getId(),"CONTR_LOAN",contract.getContrNo());
		
		model.addAttribute("contr", contract);
		
		BusinessOpportunity biz = quotService.getBizOppbynumber(contract.getProjNo());
		CustomInfo cust = customerService.getCustomInfo(contract.getCustCode()); 
		ProductPriceHead priceTable = priceHeadService.queryLpcById(contract.getPricNo());
		
		if(biz!=null){
		model.addAttribute("project",JSON.toJSONString(biz));
		}
		if(cust!=null){
		model.addAttribute("customer",JSON.toJSONString(cust));
		}
		if(priceTable!=null){
		model.addAttribute("priceTable",JSON.toJSONString(priceTable));
		}
		
		
//		model.addAttribute("org",JSON.toJSONString(lovMemberService.get(getUserObject().getOrg().getId())));
//		model.addAttribute("project",JSON.toJSONString(lovMemberService.get(contract.getProjNo())));
		
		//return forward("loancontract");
		return forward("../contract/loan/loan");
	}

	@ResponseBody
	@RequestMapping(value="/addloancontract",method=RequestMethod.POST)
	public String loancontract(Contract contract,HttpServletRequest request) {
		
		String qid = request.getParameter("qid");
		
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
		if(expSignDT.after(subTime)){
			contractLoanService.save(contract,getUserObject());
			
			//quotService.copyPrjdtl(qid, contract.getId(), "Y");
			
			quotService.updatePrjLstwithlineNum(qid);
			//quotService.copyLovMemberByMemo(qid, contract.getId());
			
			quotService.copyKstarPrjLstforCont(qid, contract.getId());
			
			contractService.updatePrjlstTypeByContr(contract.getId(),"CONTR_LOAN",contract.getContrNo());
			
			return sendSuccessMessage();			
		}else{
			throw new AnneException("合同预计签订时间应该在借货时间之后");
		}
		
	}
	
	
	
	
	
	
	@ResponseBody
	@RequestMapping(value = "/addMem", method = RequestMethod.POST)
	public String addMem(KstarMemInfo mem, HttpServletRequest request) {

		String qid = request.getParameter("qid");
		mem.setQuotCode(qid);
		quotService.saveMem(mem);
		return sendSuccessMessage(mem.getQuotCode());
	}
	
	
	@RequestMapping("/addMem")
	public String addMem(Model model){
		return forward("addMem");
	}
	
	
	@ResponseBody
	@RequestMapping(value="/editMem",method=RequestMethod.POST)
	public String editMem(KstarMemInfo mem){
		quotService.updateMem(mem);
		return sendSuccessMessage();
	}
	
	
	@RequestMapping("/editMem")
	public String editMem(String id,Model model){
		if(id==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		KstarMemInfo mem = quotService.getKstarMemInfo(id);
		if(mem==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		model.addAttribute("memInfo",mem);
		return forward("addMem");
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/addBiddcevl", method = RequestMethod.POST)
	public String addBiddcevl(KstarBiddcevl biddcevl, HttpServletRequest request) {
		String qid = request.getParameter("qid");
		biddcevl.setQuotCode(qid);
		List<IUploadFile> list = UploadUtils.uploadFile(request, "/aaaa");
		if(!list.isEmpty()){
			biddcevl.setDocUrl(list.get(0).getRealPath());}
		quotService.saveBiddcevl(biddcevl);
		return sendSuccessMessage(biddcevl.getQuotCode());
	}
	
	
	@RequestMapping("/addBiddcevl")
	public String addBiddcevl(Model model,String qid){
		model.addAttribute("qid", qid);
		return forward("addBiddcevl");
	}
	
	@ResponseBody
	@RequestMapping(value="/editBiddcevl",method=RequestMethod.POST)
	public String editBiddcevl(KstarBiddcevl biddcevl){
		quotService.updateBiddcevl(biddcevl);
		return sendSuccessMessage();
	}
	
	
	@RequestMapping("/editBiddcevl")
	public String editBiddcevl(String id,Model model,String qid){
		model.addAttribute("qid", qid);
		
		if(id==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		KstarBiddcevl biddcevl = quotService.getKstarBiddcevl(id);
		if(biddcevl==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		model.addAttribute("biddcevlInfo",biddcevl);
		return forward("addBiddcevl");
	}
	
	
	
	
	@ResponseBody
	@RequestMapping(value = "/addAtt", method = RequestMethod.POST)
	public String addAtt(KstarAtt att, HttpServletRequest request) {
		
		String qid = request.getParameter("qid");
		att.setQuotCode(qid);
		UserObject user = getUserObject();
		att.setUpdr(user.getEmployee().getName());
		
		List<IUploadFile> list = UploadUtils.uploadFile(request, "/aaaa");
		if(!list.isEmpty()){
			att.setcNotes(list.get(0).getRealPath());}
		quotService.saveAtt(att);
		return sendSuccessMessage(att.getQuotCode());
	}
	
	
	@RequestMapping("/addAtt")
	public String addAtt(String id,String qid,Model model){
		model.addAttribute("qid", qid);
		return forward("addAtt");
	}
	
	@ResponseBody
	@RequestMapping(value="/editAtt",method=RequestMethod.POST)
	public String editAtt(KstarAtt att, HttpServletRequest request){
		
		UserObject user = getUserObject();
		att.setUpdr(user.getEmployee().getName());
		List<IUploadFile> list = UploadUtils.uploadFile(request, "/aaaa");
		if(!list.isEmpty()){
			att.setcNotes(list.get(0).getRealPath());}
		
		quotService.updateAtt(att);
		return sendSuccessMessage();
	}
	
	
	@RequestMapping("/editAtt")
	public String editAtt(String id,Model model){
		if(id==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		KstarAtt att = quotService.getKstarAtt(id);
		if(att==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		model.addAttribute("attInfo",att);
		return forward("addAtt");
	}
	
	@ResponseBody
	@RequestMapping(value = "/addPrjevl", method = RequestMethod.POST)
	public String addPrjevl(KstarPrjEvl prjevl, HttpServletRequest request) {
		String qid = request.getParameter("qid");
		String ctype = request.getParameter("typ");
		
		UserObject user = getUserObject();
		prjevl.setEvlMm(user.getEmployee().getName());
		
		prjevl.setEvlSt("S03");
		prjevl.setSbmDt(new Date());
		prjevl.setCType(ctype);
		
		prjevl.setQuotCode(qid);
		quotService.savePrjevl(prjevl);
		
		//未启动状态
		String ckevlststatus = Checkevlststatus(qid, ctype);
		
		return sendSuccessMessage(ckevlststatus);
	}
	
	
	@RequestMapping("/addPrjevl")
	public String addPrjevl(String qid,String typ,String ckevlststatus,Model model){
		model.addAttribute("qid", qid);
		model.addAttribute("typ", typ);
		model.addAttribute("ckevlststatus",ckevlststatus);
		return forward("addPrjevl");
	}
	
	@ResponseBody
	@RequestMapping(value="/editPrjevl",method=RequestMethod.POST)
	public String editPrjevl(KstarPrjEvl prjevl){
		
		UserObject user = getUserObject();
		prjevl.setEvlMm(user.getEmployee().getName());
		prjevl.setSbmDt(new Date());
		
		quotService.updatePrjEvl(prjevl);
		return sendSuccessMessage();
	}
	
	
	@RequestMapping("/editPrjevl")
	public String editPrjevl(String id,String typ,Model model){
		if(id==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		KstarPrjEvl prjevl = quotService.getKstarPrjEvl(id);
		if(prjevl==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		model.addAttribute("prjevlInfo",prjevl);
		return forward("addPrjevl");
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/addPginf", method = RequestMethod.POST)
	public String addPginf(KstarPgInf pginf, HttpServletRequest request) {
		String cType = request.getParameter("typ");
		quotService.savePgInf(pginf,cType);
		return sendSuccessMessage(pginf.getQuotCode());
	}
	
	
	@RequestMapping("/addPginf")
	public String addPginf(Model model){
		return forward("addPginf");
	}
	
	
	@ResponseBody
	@RequestMapping(value="/editPginf",method=RequestMethod.POST)
	public String editPginf(KstarPgInf pginf){
		quotService.updatePgInf(pginf);
		return sendSuccessMessage();
	}
	
	
	@RequestMapping("/editPginf")
	public String editPginf(String id,Model model){
		if(id==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		KstarPgInf pginf = quotService.getKstarPgInf(id);
		if(pginf==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		model.addAttribute("pginfInfo",pginf);
		return forward("addPginf");
	}
	
	
	
	@RequestMapping("/edit")
	public String edit(String id,Model model){
		if(id==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		KstarQuot quot = quotService.getKstarQuot(id);
		if(quot==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		
//		String version =  String.valueOf(Integer.parseInt(quot.getQuotVersion())+1);
//		quot.setQuotVersion(version);
		//quotService.updateQuot(quot);
		
		model.addAttribute("quotInfo",quot);
		return forward("add");
	}
	
	
	@ResponseBody
	@RequestMapping(value="/editContract",method=RequestMethod.POST)
	public String editCnt(KstarCntr cnt){
		quotService.updateCnt(cnt);
		return sendSuccessMessage();
	}
	
	
	@RequestMapping("/editContract")
	public String editCnt(String id,Model model){
		if(id==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		KstarCntr cnt = quotService.getKstarCntr(id);
		if(cnt==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		model.addAttribute("cntInfo",cnt);
		return forward("addContract");
	}
	
	
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(KstarQuot quot){
		quotService.updateQuot(quot);
		return sendSuccessMessage();
	}
	
	
	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		quotService.deleteQuot(id);
		return sendSuccessMessage();
	}
	
	
	@ResponseBody
	@RequestMapping(value="/deleteContract",method=RequestMethod.POST)
	public String deleteCnt(String id){
		quotService.deleteCnt(id);
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value="/deleteMem",method=RequestMethod.POST)
	public String deleteMem(String id){
		quotService.deleteMem(id);
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value="/deleteAtt",method=RequestMethod.POST)
	public String deleteAtt(String id){
		quotService.deleteAtt(id);
		return sendSuccessMessage();
	}
	
	
	@ResponseBody
	@RequestMapping(value="/deletePrjevl",method=RequestMethod.POST)
	public String deletePrjevl(String id){
		quotService.deletePrjevl(id);
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value="/deletePgInf",method=RequestMethod.POST)
	public String deletePgInf(String id){
		quotService.deletePgInf(id);
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value="/deleteBiddcevl",method=RequestMethod.POST)
	public String deleteBiddcevl(String id){
		quotService.deleteBiddcevl(id);
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/reviseQuot", method = RequestMethod.POST)
	public String reviseQuot(String qid,HttpServletRequest request) throws Exception{		
		quotService.reviseQuot(getUserObject(), qid);
		return sendSuccessMessage();
	}
	
	/**
	 * 报价列表
	 * 工程清单导出
	 */
	@NoRight
	@RequestMapping(value = "/quotPrjlstExport")
	public void quotPrjlstExport(PageCondition condition, HttpServletRequest request,HttpServletResponse response) throws Exception{
		String qid = request.getParameter("qid");
		String typ = request.getParameter("typ");	
		condition.setCondition("qid", qid);
		condition.setCondition("typ", typ);
		List<KstarPrjLst> prjLstList = (List<KstarPrjLst>)quotService.queryPrjLst(condition).getList();
		List<List<Object>> dataList  = contractService.quotPrjlstExport(prjLstList);
		ExcelUtil.exportExcel(response, dataList, "工程清单");
	}
	
	
	
}
