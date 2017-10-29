/**
 * 
 */
package com.ibm.kstar.action.contract.change.prjlst;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.ExcelUtil;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.action.system.lov.member.LovMemberAction;
import com.ibm.kstar.api.contract.IContrChangeService;
import com.ibm.kstar.api.contract.IContractService;
import com.ibm.kstar.api.order.IOrderService;
import com.ibm.kstar.api.product.IProLovService;
import com.ibm.kstar.api.system.lov.entity.LovGroup;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.contract.ContrChange;
import com.ibm.kstar.entity.quot.KstarPrjLst;
import com.ibm.kstar.interceptor.system.permission.NoRight;

/**
 * @author ibm
 *
 */
@Controller
@RequestMapping("/change/prjlst")
//@Scope("prototype")
public class ContrChgPrjlstAction extends LovMemberAction  {
	Logger logger = LoggerFactory.getLogger(getClass());

	private final static String CONTRACT_PROCESS_KEY = "contrPrjlst";
 	
//	@Autowired
//	private IContractService contractService;
	
	@Autowired
	IProLovService proLovService; 
	
	@Autowired
	private IContrChangeService  changeService ;
	@Autowired
	private IContractService contractService;
	@Autowired
	IOrderService orderService;
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/prjLstPage")
	public String pagePrjLst(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		condition.setCondition("contrId", request.getParameter("contrId"));
		IPage p = changeService.queryPrjLst(condition);
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
		return forward("contrChgPrjlst");
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
		prjLst.setUpdtFlag("1");
		prjLst.setUpdatType("新增");
		changeService.savePrjLst(prjLst,cType);
		 
		changeService.updateAvgttl(lovMember, prjLst);
		
		//总金额
		Double totalAmt = changeService.getTotalamount(prjLst.getQuotCode());
		ContrChange contrChg = changeService.get(prjLst.getQuotCode());
		contrChg.setTotalAmt(totalAmt); 
		changeService.update(contrChg);
		
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
		KstarPrjLst prjLst = changeService.getKstarPrjLst(contrId, lvId);
		model.addAttribute("prjLst",prjLst);
		
		return forward("contrChgPrjlst");
	}
	
	@ResponseBody
	@RequestMapping(value="/editPrjlst",method=RequestMethod.POST)
	public String editPrjlst(LovMember lovMember,KstarPrjLst prjLst){
		prjLst.setUpdtFlag("1");
		if(prjLst.getUpdatType().equalsIgnoreCase("新增")){
			prjLst.setUpdatType("新增");
		}else{
			if(prjLst.getNewAmt()==0){
				prjLst.setUpdatType("删除");
			}else{
				prjLst.setUpdatType("更新");
			}
		}

//		changeService.updatePrjLst(lovMember,prjLst);
		
		changeService.updateAvgttl(lovMember, prjLst);

		//总金额
		Double totalAmt = changeService.getTotalamount(prjLst.getQuotCode());
		ContrChange contrChg = changeService.get(prjLst.getQuotCode());
		contrChg.setTotalAmt(totalAmt); 
		changeService.update(contrChg);
				
				
		return sendSuccessMessage();
	}
	
	
	@ResponseBody
	@RequestMapping(value="/deletePrjlst",method=RequestMethod.POST)
	public String deletePrjlst(String prjlstId, String typ,String contrId){
		KstarPrjLst prjLst = contractService.getKstarPrjLstById(prjlstId);

		if(prjLst.getAmt()!=null||prjLst.getPrdPrc()!=null) {
			throw new AnneException("原合同产品行只能调整，不允许删除");
		}
//		lovMemberService.delete(lovId);
		
		changeService.deletePrjLst(prjlstId);
//		changeService.updateAllAvgttlByQcode(contrId);

		//总金额
		Double totalAmt = changeService.getTotalamount(contrId);
		ContrChange contrChg = changeService.get(contrId);
		contrChg.setTotalAmt(totalAmt); 
		changeService.update(contrChg);
	
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
		
		
		proLovService.saveCatelog(lovMember);
		prjLst.setLvId(lovMember.getId());
//		prjLst.setUpdtFlag("1");
//		prjLst.setUpdatType("新增");

		ContrChange contrChg = changeService.get(prjLst.getQuotCode());
		prjLst.setBuzCode(contrChg.getChangeNo());

//		contractService.savePrjLst(prjLst,cType);
		changeService.savePrjLst(prjLst,cType);
		
//		contractService.updateAvgttl(lovMember, prjLst);
		changeService.updateAvgttl(lovMember, prjLst);

//		Double totalAmt =(double) 0;
//		if(prjLst.getPrdPrc()!=null){
//			//总金额
//			totalAmt = changeService.getTotalamount(prjLst.getQuotCode());
//		}
//
//		contrChg.setTotalAmt(totalAmt); 
//		changeService.update(contrChg);
//		
		
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
		
		if(StringUtil.isEmpty(prjLst.getQuotCode())){
			throw new AnneException("报价单编号不能为空");
		}else{
			lovMember.setMemo(prjLst.getQuotCode());
		}
		
		if(StringUtil.isEmpty(lovMember.getParentId())){
			String parentId = contractService.getLovememroot(prjLst.getQuotCode());
			lovMember.setParentId(parentId);
		}
		
//		if( prjLst.getPrdSprc()==null || StringUtil.isEmpty(prjLst.getPrdSprc().toString()) ){
//			prjLst.setPrdSprc(0.0);
//		}
//		if( prjLst.getNewPrdPrc()==null || StringUtil.isEmpty(prjLst.getNewPrdPrc().toString())){
//			prjLst.setNewPrdPrc(0.0);
//		}
//		if( prjLst.getNewAmt()==null || StringUtil.isEmpty(prjLst.getNewAmt().toString())){
//			prjLst.setNewAmt(0.0);
//		}
		Date now = new Date();
		SimpleDateFormat dtFmt=new SimpleDateFormat("yyyyMMddHHmmssSSS");

		int r = contractService.buildRandom(2);
		String codeStr = dtFmt.format(now) + r;
		lovMember.setCode(codeStr);
		lovMember.setName(prjLst.getPrdNm());
		lovMember.setLeafFlag("Y");
		
		if(prjLst.getPrdCtg().equals("标准产品")){
			lovMember.setLeafFlag("N");
		}
		
		proLovService.saveCatelog(lovMember);
		prjLst.setLvId(lovMember.getId());
//		prjLst.setUpdtFlag("1");
//		prjLst.setUpdatType("新增");
		

		ContrChange contrChg = changeService.get(prjLst.getQuotCode());
		prjLst.setBuzCode(contrChg.getChangeNo());
		prjLst.setVeriedNum(0.0);
		prjLst.setNotVeriNum(prjLst.getAmt());
//		contractService.savePrjLst(prjLst,cType);
//		if(prjLst.getLineNum() == null){
			prjLst.setLineNum(contractService.getLineNum(prjLst.getQuotCode()));			
//		}
		changeService.savePrjLst(prjLst,cType);

//		contractService.updateAvgttl(lovMember, prjLst);
		// 根据用户需求暂时取消， 2017.4.20
//		changeService.updateAvgttl(lovMember, prjLst);

		Double totalAmt =(double) 0;
		if((Double)prjLst.getNewPrdPrc()!=null){
			//总金额
			totalAmt = changeService.getNewTotalamount(prjLst.getQuotCode());
		}

		contrChg.setTotalAmt(totalAmt); 
		changeService.update(contrChg);
		
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

//		if( prjLst.getPrdSprc()==null || StringUtil.isEmpty(prjLst.getPrdSprc().toString()) ){
//			prjLst.setPrdSprc(0.0);
//		}
//		if( prjLst.getNewPrdPrc()==null || StringUtil.isEmpty(prjLst.getNewPrdPrc().toString())){
//			prjLst.setNewPrdPrc(0.0);
//		}
//		if( prjLst.getNewAmt()==null || StringUtil.isEmpty(prjLst.getNewAmt().toString())){
//			prjLst.setNewAmt(0.0);
//		}
		
//		if(prjLst.getOrdNo()!= null){
//			OrderQuantityVo ordQtyVo= orderService.getContractOrderQty(prjLst.getOrdNo(), prjLst.getLineNum());
//			if(ordQtyVo != null){
//				int ordBillNum= ordQtyVo.getInvoiceQty();// 订单行开票数量
//				int ordDelivNum= ordQtyVo.getDeliveryQty(); // 订单行发运数量
//				if(prjLst.getNewAmt()<ordBillNum){
//					throw new AnneException("变更数量小于已开票数量，无法变更");
//				}else if(prjLst.getNewAmt()<ordDelivNum){
//					throw new AnneException("变更数量小于已发货数量，无法变更");
//				}
//			}else{
//				throw new AnneException("未找到订单行数据");
//			}
//		}
//		prjLst.setUpdtFlag("1");
//		if(prjLst.getUpdatType()==null){
//			prjLst.setUpdatType("更新");
//		}else{
//			if(prjLst.getUpdatType().equalsIgnoreCase("新增")){
//				prjLst.setUpdatType("新增");
//			}else{ 
//				if(prjLst.getNewAmt()==null){
//					prjLst.setUpdatType("更新");
//				}else{
//					if(prjLst.getNewAmt()==0){
//						prjLst.setUpdatType("删除");
//					}else{
//						prjLst.setUpdatType("更新");
//					}
//				}
//			}
//		}

		prjLst.setId(prjLstid);
		LovMember lovMember = lovMemberService.get(lovId);
		lovMember.setName(prjLst.getPrdNm());
		lovMember.setLastUpdatedDate(new Date());
		
		changeService.updatePrjLst(prjLst);
//		changeService.updateAvgttl(lovMember, prjLst);
//		
//		Double totalAmt =(double) 0.0;
//		if(prjLst.getPrdPrc()!=null){
//			//总金额
//			totalAmt = changeService.getTotalamount(prjLst.getQuotCode());
//		}
//
//		ContrChange contrChg = changeService.get(prjLst.getQuotCode());
//		contrChg.setTotalAmt(totalAmt); 
//		changeService.update(contrChg);
		
		// 更新订单行的暂挂状态为YES
//		orderService.updateContractOrderLinePending(prjLst.getOrdNo(), prjLst.getLineNum(), getUserObject());
		
		return sendSuccessMessage();
	}
	
	/**
	 * 保存合同清单
	 * @param listStr
	 * @param contrId
	 * @param typ
	 * @param request
	 * @return
	 */
	@NoRight
	@ResponseBody
	@RequestMapping(value="/submitLines",method=RequestMethod.POST)
	public String saveLines(String listStr, String contrId,String typ,  HttpServletRequest request){
	
//		PrjLstDtl prjLstVo,
//		List<PrjLstDtl> lines = JSON.parseArray(listStr, PrjLstDtl.class);
////		System.out.println("----630----->" + lines.size()+ "<<<<<<");
////		for(PrjLstDtl prjDtl : lines){
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
//					savePrjLstDtl(lovMember, prjLst, typ,contrId);
//				}else{
//					updateChgPrjlst(lovMember,prjLst);
//				}
//			}
//		}

		UserObject userObject = getUserObject();

		changeService.saveLines(listStr, contrId, typ, userObject);
		
		return sendSuccessMessage();
	}

	
	/**
	 * 合同变更
	 * 工程清单导出
	 */
	@NoRight
	@RequestMapping(value = "/chgPrjlstExport")
	public void loanPrjlstExport(PageCondition condition, HttpServletRequest request,HttpServletResponse response) throws Exception{
		String contrId = request.getParameter("contrId");
		String typ = request.getParameter("typ");	
		condition.setCondition("contrId", contrId);
		condition.setCondition("typ", typ);
		condition.setRows(500);
		List<List<Object>> dataList  = contractService.chgPrjlstExport(condition);
		ExcelUtil.exportExcel(response, dataList, "工程清单");
	}
}
