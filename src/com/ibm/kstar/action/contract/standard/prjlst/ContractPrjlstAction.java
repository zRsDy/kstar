/**
 * 
 */
package com.ibm.kstar.action.contract.standard.prjlst;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.action.system.lov.member.LovMemberAction;
import com.ibm.kstar.api.contract.IContractService;
import com.ibm.kstar.api.product.IProLovService;
import com.ibm.kstar.api.product.IProductSerialService;
import com.ibm.kstar.api.product.IProductService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovGroup;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.contract.Contract;
import com.ibm.kstar.entity.product.KstarProduct;
import com.ibm.kstar.entity.quot.KstarPrjLst;
import com.ibm.kstar.interceptor.system.permission.NoRight;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.ExcelUtil;
import org.xsnake.web.utils.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author ibm
 *
 */
@Controller
@RequestMapping("/standard/prjlst")
//@Scope("prototype")
public class ContractPrjlstAction extends LovMemberAction  {
	Logger logger = LoggerFactory.getLogger(getClass());

	private final static String CONTRACT_PROCESS_KEY = "contrPrjlst";
 	
	@Autowired
	private IContractService contractService;
	
	@Autowired
	IProLovService proLovService; 
	
	@Autowired
	IProductService productService;
	
	@Autowired
	protected ILovMemberService lovMemberService;
	
	@Autowired
	IProductSerialService productSerialService;
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/prjLstPage")
	public String pagePrjLst(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		condition.setCondition("contrId", request.getParameter("contrId"));
		IPage p = contractService.queryPrjLst(condition);
		for (Object o : p.getList()) {
			if (o instanceof KstarPrjLst) {
				KstarPrjLst prjLst = (KstarPrjLst) o;
				if (prjLst.getPrdCtg() == null) {
					prjLst.setPrdCtg("");
				}
			}
		}
		return sendSuccessMessage(p);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/eliminatePrjLstPage")
	public String eliminatePrjLstPage(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		condition.setCondition("contrId", request.getParameter("contrId"));
		condition.setCondition("eliminateId", request.getParameter("eliminateId"));
		IPage p = contractService.queryEliminatePrjLst(condition);
		return sendSuccessMessage(p);
	}
	
	@RequestMapping("/addPrjlst")
	public String addPrjlst(String contrId, String groupId, String parentId, Model model) {
		model.addAttribute("contrId", contrId);
		LovGroup lovGroup = lovGroupService.get(groupId);
		model.addAttribute("lovGroup", lovGroup);
		if (StringUtil.isNotEmpty(parentId)) {
			LovMember parentLovMember = lovMemberService.get(parentId);
			model.addAttribute("parentLovMember", parentLovMember);
		}
		return forward("contractPrjlst");
	}
	
	@ResponseBody
	@RequestMapping(value="/addPrjlst",method=RequestMethod.POST)
	public String addPrjlst(LovMember lovMember,KstarPrjLst prjLst){
		String cType = "0005";
		
		if(StringUtil.isEmpty(prjLst.getQuotCode())){
			throw new AnneException("合同编号不能为空");
		}else{
			lovMember.setMemo(prjLst.getQuotCode());
		}
		
		lovMember.setCode(StringUtil.getUUID());
		lovMember.setName(prjLst.getPrdNm());
		
		proLovService.saveCatelog(lovMember);
		prjLst.setLvId(lovMember.getId());
		contractService.savePrjLst(prjLst,cType);
		
//		contractService.updateAllAvgttl(lovMember, prjLst); 
		contractService.updateAvgttl(lovMember, prjLst);
		
		Double totalAmount=(double) 0;
		
		if(prjLst.getPrdPrc()!=null){
			//总金额
			totalAmount = contractService.getTotalamount(prjLst.getQuotCode());
		}
		

//		KstarQuot quot = contractService.getKstarQuot(prjLst.getQuotCode());
		Contract contract = contractService.get(prjLst.getQuotCode());
		contract.setTotalAmt(totalAmount);
		contractService.update(contract,null);
		
		return sendSuccessMessage();
	}
	
	@RequestMapping("/editPrjlst")
	public String editPrjlst(String id,Model model,String contrId){
		model.addAttribute("contrId", contrId);
		LovMember lovMember = lovMemberService.get(id);
		LovGroup lovGroup = lovGroupService.get(lovMember.getGroupId());
		model.addAttribute("lovGroup",lovGroup);
		model.addAttribute("lovMember",lovMember);
		if(StringUtil.isNotEmpty(lovMember.getParentId())){
			LovMember parentLovMember = lovMemberService.get(lovMember.getParentId());
			model.addAttribute("parentLovMember",parentLovMember);
		}
		
		String lvId = lovMember.getId();
		KstarPrjLst prjLst = contractService.getKstarPrjLst(contrId, lvId);
		model.addAttribute("prjLst",prjLst);
		
		return forward("contractPrjlst");
	}
	
	@ResponseBody
	@RequestMapping(value="/editPrjlst",method=RequestMethod.POST)
	public String editPrjlst(LovMember lovMember,KstarPrjLst prjLst){

//		contractService.updatePrjLst(prjLst);
////		contractService.updateAllAvgttl(lovMember, prjLst);
//		contractService.updateAvgttl(lovMember, prjLst);
//		
//		Double totalAmount=0.0;
//		
//		if(prjLst.getPrdPrc()!=null){
//			//总金额
//			totalAmount = contractService.getTotalamount(prjLst.getQuotCode());
//		}
//
//		Contract contract = contractService.get(prjLst.getQuotCode());
//		contract.setTotalAmt(totalAmount);
//		contractService.update(contract);
//		
		return sendSuccessMessage();
	}
	
	
	@ResponseBody
	@RequestMapping(value="/deletePrjlst",method=RequestMethod.POST)
	public String deletePrjlst(String prjlstId, String typ,String contrId){

		
//		lovMemberService.delete(id);
		contractService.deletePrjLst(prjlstId);
		//contractService.updateAllAvgttlByQcode(contrId);
		
		
		//总金额
		Double totalAmount = contractService.getTotalamount(contrId);
		Contract contract = contractService.get(contrId);
		contract.setTotalAmt(totalAmount);
		contractService.update(contract,null);
	
		
		return sendSuccessMessage();
	}
	
	
	@ResponseBody
	@RequestMapping("/treePrjlst")
	public String treePrjlst(Condition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		String groupCode = condition.getStringCondition("groupCode");
		String groupId = condition.getStringCondition("groupId");
		
		String contrId = condition.getStringCondition("contrId");
		
		if(StringUtil.isEmpty(groupId) && StringUtil.isEmpty(groupCode)){
			throw new AnneException("无效的参数访问");
		}
		if(StringUtil.isEmpty(groupId)){
			LovGroup group = lovGroupService.getByCode(groupCode);
			if(group == null){
				throw new AnneException("无效的参数访问");
			}
			groupId = group.getId();
		}
		String parentId = condition.getStringCondition("id");
		condition.getFilterObject().addCondition("groupId", "=", groupId);
		condition.getFilterObject().addCondition("deleteFlag", "=", "N");
		if(parentId == null){
			parentId = "ROOT";
		}
		condition.getFilterObject().addCondition("parentId", "=", parentId);
		
		condition.getFilterObject().addCondition("memo", "=", contrId);
		
		List<LovMember> list = lovMemberService.getList(condition);
		return sendSuccessMessage(list);
	}
	
	@ResponseBody
	@RequestMapping(value="/prdtab",method=RequestMethod.POST)
	public String addnprd(LovMember lovMember,KstarPrjLst prjLst){
		return sendSuccessMessage();
	}

	@RequestMapping("/prdtab")
	public String addnonprd(String id,String contrId,String typ, String ctg,String groupId,String parentId,Model model){
		model.addAttribute("id",id);
		model.addAttribute("contrId",contrId);
		model.addAttribute("ctg",ctg);
		model.addAttribute("groupId",groupId);		
		model.addAttribute("parentId",parentId);
		model.addAttribute("typ",typ);
		TabMain tb = new TabMain();
		tb.setInitAll(false);
		tb.addTab("基本信息", "/standard/prjlst/info1.html?contrId="+contrId+"&typ="+typ+"&ctg="+ctg+"&groupId=PRJLSTPRDCAT&parentId="+parentId);

		model.addAttribute("tabMainPrd",tb);
		return forward("prdtab");
		//return forward("product");
		
		//return redirect("info1.html");
	}
	
	
	
	@RequestMapping("/info1")
	public String info(String id,String contrId,String typ,String ctg,String groupId,String parentId,Model model,HttpServletRequest request){
		
		model.addAttribute("id",id);
		model.addAttribute("contrId",contrId);
		model.addAttribute("ctg",ctg);
		model.addAttribute("groupId",groupId);
		model.addAttribute("parentId",parentId);
		model.addAttribute("typ",typ);
		Contract contr = contractService.get(contrId);
		String customId = contr.getCustCode(); 
		String businessUnit = contr.getMarketDept();
		model.addAttribute("customId",customId);
		model.addAttribute("businessUnit",businessUnit);
		//客户PO号
		String custContrNo= "";
		if(typ.equalsIgnoreCase(IConstants.CONTR_PI)){
			custContrNo= contr.getCustContrNo();
		}else{
			custContrNo= contr.getContrNo();
		}
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
	

	@RequestMapping("/demand")
	public String showDemand(String prodId,String customId,String businessUnit,String custContrNo, Model model){
		TabMain tabMain = new TabMain();
		tabMain.setInitAll(false);
		tabMain.addTab("需求单", "/product/info8.html?id="+prodId+"&customId="+customId+"&businessUnit="+businessUnit+"&custContrNo="+custContrNo);

		model.addAttribute("tabMain",tabMain);
//		model.addAttribute("prodId",prodId);
		return forward("demand");
	}
	
	@ResponseBody
	@RequestMapping(value="/mainInfoSave",method=RequestMethod.POST)
	public String mainInfoSave(KstarProduct product, HttpServletRequest request){
		
		boolean newFlag = false;
		
		if(product.getId() == null || StringUtils.isEmpty(product.getId())){
			product.setId(null);
			newFlag = true;
		}
		
		
		String contrId = request.getParameter("contrId");
		String parentId = request.getParameter("parentId");
		String groupId = request.getParameter("groupId");
		String typ = request.getParameter("typ");
		
		
		LovGroup lovGroup = lovGroupService.get(groupId);

		
		//new point
		KstarPrjLst prjLst = new KstarPrjLst();
		LovMember lovMem = new LovMember();
		
		if (StringUtil.isNotEmpty(parentId)) {
			LovMember parentLovMember = lovMemberService.get(parentId);			
			lovMem.setParentId(parentLovMember.getId());
		}else{
			lovMem.setParentId(contractService.getLovememroot(contrId));
		}

		
		String cType = typ; //"0003";
		prjLst.setQuotCode(contrId);
		
		
		if(StringUtil.isEmpty(prjLst.getQuotCode())){
			throw new AnneException("无法找到该合同");
		}else{
			lovMem.setMemo(prjLst.getQuotCode());
		}

		Date now = new Date();
		SimpleDateFormat dtFmt=new SimpleDateFormat("yyyyMMddHHmmssSSS");

		int r = contractService.buildRandom(2);
		String codeStr = dtFmt.format(now) + r;
		lovMem.setCode(codeStr);
		lovMem.setGroupId(groupId);
		lovMem.setName(product.getProductName());
		lovMem.setCreationDate(new Date());
		lovMem.setLastUpdatedDate(new Date());
		prjLst.setPrdNm(product.getProductName());
		prjLst.setPrdTyp(product.getProModel());
		prjLst.setNstRq("是");
		prjLst.setLineNum(contractService.getLineNum(prjLst.getQuotCode()));
		
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
		contractService.savePrjLst(prjLst,cType);
		


		return sendSuccessMessage(product.getId());
	}
	
	@ResponseBody
	@RequestMapping(value="/catalogmove",method=RequestMethod.POST)
	public String catalogmove(String contrId,String dragNodeId,String newParentNodeId){
		lovMemberService.move(dragNodeId,newParentNodeId);
		contractService.updateAllAvgttlByQcode(contrId);
		return sendSuccessMessage();
	}
	

	@RequestMapping("/catalogadd")
	public String catalogadd(String contrId, String typ, String groupId, String parentId, Model model) {
		model.addAttribute("contrId", contrId);
		model.addAttribute("typ", typ);
		LovGroup lovGroup = lovGroupService.get(groupId);
		model.addAttribute("lovGroup", lovGroup);
		
		LovMember parentLovMember;
		
		if (StringUtil.isNotEmpty(parentId)) {
			parentLovMember = lovMemberService.get(parentId);
			
		}else{
			parentLovMember = lovMemberService.get(contractService.getLovememroot(contrId));
		}
		
		model.addAttribute("parentLovMember", parentLovMember);
		
		return forward("addPrjLstCat");
	}
	
	@ResponseBody
	@RequestMapping(value="/catalogadd",method=RequestMethod.POST)
	public String catalogadd(LovMember lovMember,KstarPrjLst prjLst, HttpServletRequest request){
		
		String cType = request.getParameter("typ");	
		
		
		if(StringUtil.isEmpty(prjLst.getQuotCode())){
			throw new AnneException("报价单编号不能为空");
		}else{
			lovMember.setMemo(prjLst.getQuotCode());
		}
		
		if(StringUtil.isEmpty(lovMember.getParentId())){
			String parentId = contractService.getLovememroot(prjLst.getQuotCode());
			lovMember.setParentId(parentId);
		}
		Date now = new Date();
		SimpleDateFormat dtFmt=new SimpleDateFormat("yyyyMMddHHmmssSSS");

		int r = contractService.buildRandom(2);
		String codeStr = dtFmt.format(now) + r;
		lovMember.setCode(codeStr);
		lovMember.setName(prjLst.getPrdNm());
		lovMember.setLeafFlag("N");
		lovMember.setCreationDate(new Date());
		lovMember.setLastUpdatedDate(new Date());
		
		
		proLovService.saveCatelog(lovMember);
		prjLst.setLvId(lovMember.getId());

		Contract contract = contractService.get(prjLst.getQuotCode());
		prjLst.setBuzCode(contract.getContrNo());
		
		contractService.savePrjLst(prjLst,cType);
		
		//contractService.updateAllAvgttl(lovMember, prjLst); 
		contractService.updateAvgttl(lovMember, prjLst);

//		Double totalAmount=(double) 0;
//		
//		if(prjLst.getPrdPrc()!=null){
//			//总金额
//			totalAmount = contractService.getTotalamount(prjLst.getQuotCode());
//		}
//		
//
////		KstarQuot quot = contractService.getKstarQuot(prjLst.getQuotCode());
//		contract.setTotalAmt(totalAmount);
//		contractService.update(contract);;
		
		
		return sendSuccessMessage();
	}
	
	
	@RequestMapping("/prdadd")
	public String prdadd(String contrId, String typ,String pricetableid, String groupId, String parentId, Model model) {
		model.addAttribute("contrId", contrId);
		model.addAttribute("typ", typ);
		model.addAttribute("pricetableid", pricetableid);
		LovGroup lovGroup = lovGroupService.get(groupId);
		model.addAttribute("lovGroup", lovGroup);
		if (StringUtil.isNotEmpty(parentId)) {
			LovMember parentLovMember = lovMemberService.get(parentId);
			model.addAttribute("parentLovMember", parentLovMember);
		}
		return forward("addPrjLstPrd");
	}
	
	@ResponseBody
	@RequestMapping(value="/prdadd",method=RequestMethod.POST)
	public String prdadd(LovMember lovMember,KstarPrjLst prjLst, HttpServletRequest request){
		
		String cType = request.getParameter("typ");		
		// PI 合同 的报价，数量必填
		if(cType.equalsIgnoreCase(IConstants.CONTR_PI) && prjLst.getPrdPrc() == null ){
			throw new AnneException("报价不能为空");			
		}
		if(cType.equalsIgnoreCase(IConstants.CONTR_PI) && prjLst.getAmt() == null ){
			throw new AnneException("数量不能为空");			
		}
		if(StringUtil.isEmpty(prjLst.getQuotCode())){
			throw new AnneException("该合同无法找到");
		}else{
			lovMember.setMemo(prjLst.getQuotCode());
		}

		if( prjLst.getPrdPrc()==null || StringUtil.isEmpty(prjLst.getPrdPrc().toString())){
			prjLst.setPrdPrc(0.0);
		}
		if( prjLst.getPrdSprc()==null || StringUtil.isEmpty(prjLst.getPrdSprc().toString()) ){
			prjLst.setPrdSprc(0.0);
		}
		if( prjLst.getAmt()==null || StringUtil.isEmpty(prjLst.getAmt().toString())){
			prjLst.setAmt(0.0);
		}
		
		if(StringUtil.isEmpty(lovMember.getParentId())){
			String parentId = contractService.getLovememroot(prjLst.getQuotCode());
			lovMember.setParentId(parentId);
		}

		Date now = new Date();
		SimpleDateFormat dtFmt=new SimpleDateFormat("yyyyMMddHHmmssSSS");

		int r = contractService.buildRandom(2);
		String codeStr = dtFmt.format(now) + r;
		lovMember.setCode(codeStr);
		lovMember.setName(prjLst.getPrdNm());
		lovMember.setLeafFlag("Y");
		lovMember.setCreationDate(new Date());
		lovMember.setLastUpdatedDate(new Date());
		
		if(prjLst.getPrdCtg().equals("标准产品")){
			lovMember.setLeafFlag("N");
		}
		
		proLovService.saveCatelog(lovMember);
		prjLst.setLvId(lovMember.getId());
		

		Contract contract = contractService.get(prjLst.getQuotCode());
		prjLst.setBuzCode(contract.getContrNo());
		prjLst.setVeriedNum(0.0);
		prjLst.setNotVeriNum(prjLst.getAmt());
		prjLst.setLineNum(contractService.getLineNum(prjLst.getQuotCode()));
		contractService.savePrjLst(prjLst,cType);
		
		//contractService.updateAllAvgttl(lovMember, prjLst); 
		// 根据用户需求暂时取消， 2017.4.20
//		contractService.updateAvgttl(lovMember, prjLst);
		
		Double totalAmount=0.0;
		
		if((Double)prjLst.getPrdPrc()!=null){
			//总金额
			totalAmount = contractService.getTotalamount(prjLst.getQuotCode());
		}

		contract.setTotalAmt(totalAmount);
		contractService.update(contract,null);
		
		return sendSuccessMessage();
	}

	@RequestMapping("/catalogedit")
	public String catalog(String id,Model model,String contrId,String typ,String pricetableid){
		model.addAttribute("contrId", contrId);
		model.addAttribute("typ", typ);
		model.addAttribute("pricetableid", pricetableid);
		LovMember lovMember = lovMemberService.get(id);
		LovGroup lovGroup = lovGroupService.get(lovMember.getGroupId());
		model.addAttribute("lovGroup",lovGroup);
		model.addAttribute("lovMember",lovMember);
		if(StringUtil.isNotEmpty(lovMember.getParentId())){
			LovMember parentLovMember = lovMemberService.get(lovMember.getParentId());
			model.addAttribute("parentLovMember",parentLovMember);
		}
		
		String lvId = lovMember.getId();
		KstarPrjLst prjLst = contractService.getKstarPrjLst(contrId, lvId);
		model.addAttribute("prjLst",prjLst);
//		if(prjLst.getAmt()==null){
//		//if(prjLst.getAmt()==null&&prjLst.getMaterCode()==null&&prjLst.getPrdCtg()==null&&prjLst.getPrdTyp()==null){
//		if(prjLst.getPrdCtg()==null || prjLst.getPrdCtg().equalsIgnoreCase("")){
			return forward("addPrjLstCat");
//		}else{
//			return forward("addPrjLstPrd");			
//		}
	}
	
	@ResponseBody
	@RequestMapping(value="/catalogedit",method=RequestMethod.POST)
	public String catalogedit(String prjLstid,KstarPrjLst prjLst, String lovId){

//		if(prjLst.getLineNum()!=null){
//			if( prjLst.getPrdPrc()==null || StringUtil.isEmpty(prjLst.getPrdPrc().toString())){
//				prjLst.setPrdPrc(0.0);
//			}
//			if( prjLst.getPrdSprc()==null || StringUtil.isEmpty(prjLst.getPrdSprc().toString()) ){
//				prjLst.setPrdSprc(0.0);
//			}
//			if( prjLst.getAmt()==null || StringUtil.isEmpty(prjLst.getAmt().toString())){
//				prjLst.setAmt(0.0);
//			}
//		}

//		LovMember lovMember = lovMemberService.get(lovId);
//		lovMember.setName(prjLst.getPrdNm());
//		lovMember.setLastUpdatedDate(new Date());
//		prjLst.setId(prjLstid);
//		contractService.updatePrjLst(lovMember,prjLst);
		//contractService.updateAllAvgttl(lovMember, prjLst); 
//		contractService.updateAvgttl(lovMember, prjLst);
//		
//		Double totalAmount=0.0;
//		
//		if(prjLst.getPrdPrc()!=null){
//			//总金额
//			totalAmount = contractService.getTotalamount(prjLst.getQuotCode());
//		}
//
//		Contract contract = contractService.get(prjLst.getQuotCode());
//		contract.setTotalAmt(totalAmount);
//		contractService.update(contract);
		
		
		return sendSuccessMessage();
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/submitLines",method=RequestMethod.POST)
	public String saveLines(String listStr, String contrId,String typ,  HttpServletRequest request){
	
//		PrjLstDtl prjLstVo,
		
//		System.out.println("----630----->" + lines.size()+ "<<<<<<");
//		for(PrjLstDtl prjDtl : lines){
//		for(int i = 0 ; i<lines.size(); i++){
//			PrjLstDtl prjDtl = lines.get(i);
//			if(prjDtl.getLvmenber()!=null && prjDtl.getPrjlst()!=null){
//				LovMember lovMember = prjDtl.getLvmenber();
//				lovMember.setGroupId(IConstants.CONTR_PRJLST_LOV_GROUP_ID);
//				lovMember.setGroupCode(IConstants.CONTR_PRJLST_LOV_GROUP_ID);
//				KstarPrjLst prjLst = prjDtl.getPrjlst();
//				prjLst.setQuotCode(contrId);
//				prjLst.setCType(typ);			
//				if(prjLst.getId() == null || prjLst.getId().equalsIgnoreCase("")){
//					savePrjLstDtl(prjLst, typ,contrId);
//				}else{
//					contractService.updatePrjLst(prjLst);
//				}
//			}
//		}
		
		contractService.saveLines(listStr, contrId, typ);
		
		return sendSuccessMessage();
	}
		
	/**
	 * 无合同核销——工程清单保存
	 * @param listStr
	 * @param contrId
	 * @param typ
	 * @param request
	 * @return
	 */
	@NoRight
	@ResponseBody
	@RequestMapping(value="/submitEliminateLines",method=RequestMethod.POST)
	public String saveEliminateLines(String listStr, String contrId,String typ,String eliminateid,HttpServletRequest request){
		String checkPeport = contractService.checkVeriedNum(listStr, contrId, eliminateid);
		if(StringUtil.isNullOrEmpty(checkPeport)) {
			//contractService.saveEliminateLines(listStr, contrId,eliminateid,typ);
			return sendSuccessMessage(checkPeport);
		}else {
			return sendSuccessMessage(checkPeport);
		}
	}
	
	
	
	/**
	 * 工程清单
	 *
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@NoRight
	@RequestMapping("/frameContrSelectLst")
	public String getFrameContrSelectLst(String pickerId, Model model,HttpServletRequest request){
		//add new lovmem root
//		String groupId = "PRJLSTPRDCAT";
//		String rootexists = contractService.Checklovroot(contrId);
//		
//		if(rootexists.equals("Y")){
//			contractService.addlovroot(contrId, typ, groupId);
//		}
		String contrId = request.getParameter("contrId");
		Contract oriContr = contractService.get(contrId);
		Contract contr = contractService.get(oriContr.getFrameNo());
		if(contr != null){
			model.addAttribute("oriContrId",contr.getId());			
		}else{
			throw new AnneException("未找到框架协议");
		}
		
		model.addAttribute("pickerId",pickerId);
		
		return forward("frameContrSelectLst");
	}
	
	/**
	 * 合同申请
	 * 工程清单导出
	 */
	@NoRight
	@RequestMapping(value = "/prjlstExport")
	public void prjlstExport(PageCondition condition, HttpServletRequest request,HttpServletResponse response) throws Exception{
		String contrId = request.getParameter("contrId");
		String typ = request.getParameter("typ");	
		condition.setCondition("contrId", contrId);
		condition.setCondition("typ", typ);
		condition.setRows(500);
		List<List<Object>> dataList  = contractService.exportPrjlstList(condition);
		ExcelUtil.exportExcel(response, dataList, "工程清单");
	}
	
	/**
	 * 借货申请
	 * 工程清单导出
	 */
	@NoRight
	@RequestMapping(value = "/loanPrjlstExport")
	public void loanPrjlstExport(PageCondition condition, HttpServletRequest request,HttpServletResponse response) throws Exception{
		String contrId = request.getParameter("contrId");
		String typ = request.getParameter("typ");	
		condition.setCondition("contrId", contrId);
		condition.setCondition("typ", typ);
		condition.setRows(500);
		List<List<Object>> dataList  = contractService.loanPrjlstExport(condition);
		ExcelUtil.exportExcel(response, dataList, "工程清单");
	}
	
	/**
	 * PI申请
	 * 工程清单导出
	 */
	@NoRight
	@RequestMapping(value = "/piPrjlstExport")
	public void piPrjlstExport(PageCondition condition, HttpServletRequest request,HttpServletResponse response) throws Exception{
		String contrId = request.getParameter("contrId");
		String typ = request.getParameter("typ");	
		condition.setCondition("contrId", contrId);
		condition.setCondition("typ", typ);
		condition.setRows(500);
		List<List<Object>> dataList  = contractService.piPrjlstExport(condition);
		ExcelUtil.exportExcel(response, dataList, "工程清单");
	}

	@NoRight
    @ResponseBody
	@RequestMapping(value="canVerificationNum")
	public String canVerificationNum(String prjlstId){
        if (StringUtils.isEmpty(prjlstId)) {
            throw new AnneException("产品工程清单Id不能为空");
        }
        return sendSuccessMessage(contractService.canVerificationNum(prjlstId));
    }
}
