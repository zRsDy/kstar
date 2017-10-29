/** 
 * Project Name:crm 
 * File Name:PrjLstCatAction.java 
 * Package Name:com.ibm.kstar.action.quot 
 * Date:Feb 3, 20175:38:14 PM 
 * Copyright (c) 2017, ZW All Rights Reserved. 
 * 
 */

package com.ibm.kstar.action.quot;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.utils.StringUtil;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.action.system.lov.member.LovMemberAction;
import com.ibm.kstar.api.product.IProLovService;
import com.ibm.kstar.api.quot.IQuotService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.quot.KstarPrjLst;
import com.ibm.kstar.entity.quot.KstarQuot;
import com.ibm.kstar.interceptor.system.permission.NoRight;

/**
 * ClassName:PrjLstCatAction <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: Feb 3, 2017 5:38:14 PM <br/>
 * 
 * @author ZW
 * @version
 * @since JDK 1.7
 * @see
 */

@Controller
@RequestMapping("/quot/catalog")
public class PrjLstCatAction extends LovMemberAction {

	@Autowired
	IProLovService proLovService;
	
	@Autowired
	IQuotService quotService;
	
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/submitLines",method=RequestMethod.POST)
	public String saveLinesa(String listStr, String quotID,String typ,  HttpServletRequest request){
	
//		PrjLstDtl prjLstVo,
//		List<PrjLstDtl> lines = JSON.parseArray(listStr, PrjLstDtl.class);
//		System.out.println("----630----->" + lines.size()+ "<<<<<<");
//		for(PrjLstDtl prjDtl : lines){
		
		List<KstarPrjLst> lines = JSON.parseArray(listStr, KstarPrjLst.class);
		if(hasPermission("P04T1ProjListSubmitEdit")){
			for (KstarPrjLst kstarPrjLst : lines) {
				if((kstarPrjLst.getApproveDiscount()==null)
						||(kstarPrjLst.getApproveDiscount()==0)
						||(kstarPrjLst.getApproveDiscount()==100)){
					throw new AnneException("批复折扣不能为空，不能为0，不能为100%");
				}
			}
		}
		Double default_applyDiscount = 22.0;
		
		for(KstarPrjLst prjLst: lines){
			
			prjLst.setQuotCode(quotID);
			prjLst.setCType(typ);	
			
			if(prjLst.getPrdPrc()==null){
				prjLst.setPrdcd("0");
			}
			
			if(prjLst.getId() == null || prjLst.getId().equalsIgnoreCase("")){
				
				String cType ="0003";
				
				if(StringUtil.isEmpty(prjLst.getQuotCode())){
					throw new AnneException("报价单编号不能为空");
				}
				
				prjLst.setLineNum(quotService.getLineNum(prjLst.getQuotCode()));
				
				
//				if(prjLst.getPrdPrc()==null){
//					throw new AnneException(prjLst.getPrdNm()+"产品未报价，请先报价后再提交!");
//				}
				
//				//申请价格
//				if(prjLst.getApplyDiscount()!=null){
//					if(prjLst.getPrdSprc()!=null){
//						Double prdPrc = prjLst.getPrdSprc()*prjLst.getApplyDiscount()/100;
//						prjLst.setPrdPrc(prdPrc);
//					}
//				}
//				
//				//批复价格
//				if(prjLst.getApproveDiscount()!=null){
//					if(prjLst.getPrdSprc()!=null){
//						Double approvePrc = prjLst.getPrdSprc()*prjLst.getApproveDiscount()/100;
//						prjLst.setApprovePrc(approvePrc);
//					}	
//				}
				
//				String reg=".*销售经理.*";
//				String manager = "";
//				
//				String posname = getUserObject().getPosition().getName();
//				
//				if(getUserObject().getPosition().getName().matches(reg)){
//					 manager = "Manager";
//				}
//				
//				
//				if(manager.equals("Manager")){
//					if(prjLst.getApplyDiscount()!=null){
//						if(prjLst.getApplyDiscount()<default_applyDiscount){
//							throw new AnneException("销售经理申请折扣不能低于22%，请重新填写申请折扣!");
//						}	
//					}
//				}
				

				quotService.savePrjLst(prjLst,cType);
				

			}else{
				
//				// 申请价格
//				if(prjLst.getApplyDiscount()!=null){
//					if(prjLst.getPrdSprc()!=null){
//						Double prdPrc = prjLst.getPrdSprc()*prjLst.getApplyDiscount()/100;
//						prjLst.setPrdPrc(prdPrc);
//					}
//				}
//				
//				//批复价格
//				if(prjLst.getApproveDiscount()!=null){
//					if(prjLst.getPrdSprc()!=null){
//						Double approvePrc = prjLst.getPrdSprc()*prjLst.getApproveDiscount()/100;
//						prjLst.setApprovePrc(approvePrc);
//					}	
//				}
				
				
//				String reg=".*销售经理.*";
//				String manager = "";
//				
//				String posname = getUserObject().getPosition().getName();
//				
//				if(getUserObject().getPosition().getName().matches(reg)){
//					 manager = "Manager";
//				}
//				
//				
//				if(manager.equals("Manager")){
//					if(prjLst.getApplyDiscount() !=null ){
//						if(prjLst.getApplyDiscount()<default_applyDiscount){
//							throw new AnneException("销售经理申请折扣不能低于22%，请重新填写申请折扣!");
//						}
//					}
//				}
				
				quotService.updatePrjLst(prjLst);
				
			}
			
			//更新特价审批标志
			quotService.updateprjLstSprvmrkstart(getUserObject(), prjLst);
					
		}
		
		String totalAmount="";
		
		totalAmount = quotService.getTotalamount(quotID);

		KstarQuot quot = quotService.getKstarQuot(quotID);
		quot.setAmount(totalAmount);
		quotService.updateQuot(quot);
		
		return sendSuccessMessage();
	}
	
	
	
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/submitLinesa",method=RequestMethod.POST)
	public String saveLinesb(String listStr, String quotID,String typ,  HttpServletRequest request){
	
//		PrjLstDtl prjLstVo,
//		List<PrjLstDtl> lines = JSON.parseArray(listStr, PrjLstDtl.class);
//		System.out.println("----630----->" + lines.size()+ "<<<<<<");
//		for(PrjLstDtl prjDtl : lines){
		
		UserObject userObject = getUserObject();
		
		List<KstarPrjLst> lines = JSON.parseArray(listStr, KstarPrjLst.class);
		if(hasPermission("P04T1ProjListSubmitEdit")){
			for (KstarPrjLst kstarPrjLst : lines) {
				if((kstarPrjLst.getApproveDiscount()==null)
						||(kstarPrjLst.getApproveDiscount()==0)
						||(kstarPrjLst.getApproveDiscount()==100)){
					//throw new AnneException("批复折扣不能为空，不能为0，不能为100%");
					return sendErrorMessage("批复折扣不能为空，不能为0，不能为100%");
				}
			}
		}
		quotService.saveLinesb(lines, quotID, typ, userObject);
		return sendSuccessMessage();
	}
	
	
	
	
	
	
	
	
	
//	@NoRight
//	@ResponseBody
//	@RequestMapping(value="/submitLines",method=RequestMethod.POST)
//	public String saveLines(String listStr, String quotID,String typ,  HttpServletRequest request){
//	
////		PrjLstDtl prjLstVo,
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
//				prjLst.setQuotCode(quotID);
//				prjLst.setCType(typ);	
//				
//				if(prjLst.getPrdPrc()==null){
//					prjLst.setPrdcd("0");
//				}
//				
//				if(prjLst.getId() == null || prjLst.getId().equalsIgnoreCase("")){
//					
//					String cType ="0003";
//					
//					if(StringUtil.isEmpty(prjLst.getQuotCode())){
//						throw new AnneException("报价单编号不能为空");
//					}else{
//						lovMember.setMemo(prjLst.getQuotCode());
//					}
//					
//					if(StringUtil.isEmpty(lovMember.getParentId())){
//						String parentId = quotService.getLovememroot(prjLst.getQuotCode());
//						lovMember.setParentId(parentId);
//					}
//					
//					lovMember.setCode(StringUtil.getUUID());
//					lovMember.setName(prjLst.getPrdNm());
//					lovMember.setLeafFlag("Y");
//					
//					if(prjLst.getPrdCtg().equals("标准产品")){
//						lovMember.setLeafFlag("N");
//					}
//					
//					
////					if(prjLst.getPrdPrc()==null){
////						throw new AnneException(prjLst.getPrdNm()+"产品未报价，请先报价后再提交!");
////					}
//					
////					//申请价格
////					if(prjLst.getApplyDiscount()!=null){
////						if(prjLst.getPrdSprc()!=null){
////							Double prdPrc = prjLst.getPrdSprc()*prjLst.getApplyDiscount()/100;
////							prjLst.setPrdPrc(prdPrc);
////						}
////					}
////					
////					//批复价格
////					if(prjLst.getApproveDiscount()!=null){
////						if(prjLst.getPrdSprc()!=null){
////							Double approvePrc = prjLst.getPrdSprc()*prjLst.getApproveDiscount()/100;
////							prjLst.setApprovePrc(approvePrc);
////						}	
////					}
//					
//					
//					proLovService.saveCatelog(lovMember);
//					prjLst.setLvId(lovMember.getId());
//					quotService.savePrjLst(prjLst,cType);
//					
//
//				}else{
//					
////					// 申请价格
////					if(prjLst.getApplyDiscount()!=null){
////						if(prjLst.getPrdSprc()!=null){
////							Double prdPrc = prjLst.getPrdSprc()*prjLst.getApplyDiscount()/100;
////							prjLst.setPrdPrc(prdPrc);
////						}
////					}
////					
////					//批复价格
////					if(prjLst.getApproveDiscount()!=null){
////						if(prjLst.getPrdSprc()!=null){
////							Double approvePrc = prjLst.getPrdSprc()*prjLst.getApproveDiscount()/100;
////							prjLst.setApprovePrc(approvePrc);
////						}	
////					}
//					
//					quotService.updatePrjLst(lovMember,prjLst);
//					
//				}
//				
//				quotService.updateprjLstSprvmrk(getUserObject(), prjLst);
//
//			}
//		}
//		
//
//	
//		String totalAmount="";
//		
//		totalAmount = quotService.getTotalamount(quotID);
//
//		KstarQuot quot = quotService.getKstarQuot(quotID);
//		quot.setAmount(totalAmount);
//		quotService.updateQuot(quot);
//		
//		return sendSuccessMessage();
//	}
	
	
	
//	
//	@RequestMapping("/catalogadd")
//	public String catalogadd(String qid, String typ, String groupId, String parentId, Model model) {
//		model.addAttribute("qid", qid);
//		model.addAttribute("typ", typ);
//		LovGroup lovGroup = lovGroupService.get(groupId);
//		model.addAttribute("lovGroup", lovGroup);
//		
//		LovMember parentLovMember;
//		
//		if (StringUtil.isNotEmpty(parentId)) {
//			parentLovMember = lovMemberService.get(parentId);
//			
//		}else{
//			parentLovMember = lovMemberService.get(quotService.getLovememroot(qid));
//		}
//		
//		model.addAttribute("parentLovMember", parentLovMember);
//		
//		return forward("addPrjLstCat");
//	}
//	
//	@ResponseBody
//	@RequestMapping(value="/catalogadd",method=RequestMethod.POST)
//	public String catalogadd(LovMember lovMember,KstarPrjLst prjLst, HttpServletRequest request){
//		
//		String cType = request.getParameter("typ");	
//		
//		
//		if(StringUtil.isEmpty(prjLst.getQuotCode())){
//			throw new AnneException("报价单编号不能为空");
//		}else{
//			lovMember.setMemo(prjLst.getQuotCode());
//		}
//		
//		if(StringUtil.isEmpty(lovMember.getParentId())){
//			String parentId = quotService.getLovememroot(prjLst.getQuotCode());
//			lovMember.setParentId(parentId);
//		}
//		
//		lovMember.setCode(StringUtil.getUUID());
//		lovMember.setName(prjLst.getPrdNm());
//		lovMember.setLeafFlag("N");
//		
//		
//		proLovService.saveCatelog(lovMember);
//		prjLst.setLvId(lovMember.getId());
//		quotService.savePrjLst(prjLst,cType);
//		
//		//quotService.updateAvgttl(lovMember, prjLst);
//		
//		String totalAmount="";
//		
//		if(prjLst.getPrdPrc()!=null){
//			//总金额
//			totalAmount = quotService.getTotalamount(prjLst.getQuotCode());
//		}
//		
//
//		KstarQuot quot = quotService.getKstarQuot(prjLst.getQuotCode());
//		quot.setAmount(totalAmount);
//		quotService.updateQuot(quot);
//		
//		return sendSuccessMessage();
//	}
//	
//		
//	
//	@RequestMapping("/prdadd")
//	public String prdadd(String qid, String typ,String pricetableid,String groupId, String parentId, Model model) {
//		model.addAttribute("qid", qid);
//		model.addAttribute("typ", typ);
//		model.addAttribute("pricetableid", pricetableid);
//		
//		LovGroup lovGroup = lovGroupService.get(groupId);
//		model.addAttribute("lovGroup", lovGroup);
//		if (StringUtil.isNotEmpty(parentId)) {
//			LovMember parentLovMember = lovMemberService.get(parentId);
//			model.addAttribute("parentLovMember", parentLovMember);
//		}
//		return forward("addPrjLstPrd");
//	}
//	
//	@ResponseBody
//	@RequestMapping(value="/prdadd",method=RequestMethod.POST)
//	public String prdadd(LovMember lovMember,KstarPrjLst prjLst, HttpServletRequest request){
//		
//		String cType = request.getParameter("typ");		
//		//String qid = request.getParameter("qid");
//			
//		if(StringUtil.isEmpty(prjLst.getQuotCode())){
//			throw new AnneException("报价单编号不能为空");
//		}else{
//			lovMember.setMemo(prjLst.getQuotCode());
//		}
//		
//		if(StringUtil.isEmpty(lovMember.getParentId())){
//			String parentId = quotService.getLovememroot(prjLst.getQuotCode());
//			lovMember.setParentId(parentId);
//		}
//		
//		lovMember.setCode(StringUtil.getUUID());
//		lovMember.setName(prjLst.getPrdNm());
//		lovMember.setLeafFlag("Y");
//		
//		if(prjLst.getPrdCtg().equals("标准产品")){
//			lovMember.setLeafFlag("N");
//		}
//		
//		
////		if(prjLst.getPrdPrc()==null){
////			throw new AnneException(prjLst.getPrdNm()+"产品未报价，请先报价后再提交!");
////		}
//		
//		proLovService.saveCatelog(lovMember);
//		prjLst.setLvId(lovMember.getId());
//		quotService.savePrjLst(prjLst,cType);
//		
//		//KstarQuot quot = quotService.getKstarQuot(qid);
//		KstarQuot quot = quotService.getKstarQuot(prjLst.getQuotCode());
//		String qid = prjLst.getQuotCode();
//		
////		//更新特价审批标志
////		quotService.updateSprvmrk(getUserObject(), quot);
////		//特价审批标识
////		String sprvmrkstatus = quotService.CheckSprvmrkStatus(qid);
//	
//		//quotService.updateAvgttl(lovMember, prjLst);
//		
//		String totalAmount="";
//		
//		if(prjLst.getPrdPrc()!=null){
//			//总金额
//			totalAmount = quotService.getTotalamount(prjLst.getQuotCode());
//		}
//		
//
//		
//		quot.setAmount(totalAmount);
//		quotService.updateQuot(quot);
//		
//		//更新特价审批标志
////		quotService.updateSprvmrk(getUserObject(), quot);
//		
//		return sendSuccessMessage("");
//	}
//	
	
	
	@RequestMapping("/catalogedit")
	public String catalog(String id,Model model,String qid,String pricetableid,String typ){
		model.addAttribute("qid", qid);
		model.addAttribute("typ", typ);
		model.addAttribute("pricetableid",pricetableid);
//		LovMember lovMember = lovMemberService.get(id);
//		LovGroup lovGroup = lovGroupService.get(lovMember.getGroupId());
//		model.addAttribute("lovGroup",lovGroup);
//		model.addAttribute("lovMember",lovMember);
//		if(StringUtil.isNotEmpty(lovMember.getParentId())){
//			LovMember parentLovMember = lovMemberService.get(lovMember.getParentId());
//			model.addAttribute("parentLovMember",parentLovMember);
//		}
		
//		String lvId = lovMember.getId();
		KstarPrjLst prjLst = quotService.getKstarPrjLst(id);
		model.addAttribute("prjLst",prjLst);
		
		return forward("addPrjLstPrd");
	}
	
//	@ResponseBody
//	@RequestMapping(value="/catalogedit",method=RequestMethod.POST)
//	public String catalogedit(LovMember lovMember,KstarPrjLst prjLst){
//
//		quotService.updatePrjLst(lovMember,prjLst);
//
//		//quotService.updateAvgttl(lovMember, prjLst);
//		
//		String totalAmount="";
//		
//		if(prjLst.getPrdPrc()!=null){
//			//总金额
//			totalAmount = quotService.getTotalamount(prjLst.getQuotCode());
//		}
//		
//		
//		KstarQuot quot = quotService.getKstarQuot(prjLst.getQuotCode());
//		quot.setAmount(totalAmount);
//		
//		//更新特价审批标志
//		quotService.updateSprvmrk(getUserObject(), quot);
//		//特价审批标识
//		String sprvmrkstatus = quotService.CheckSprvmrkStatus(quot.getId());
//		
//		quotService.updateQuot(quot);
//		
//		
//		return sendSuccessMessage(sprvmrkstatus);
//	}
	
	
	@ResponseBody
	@RequestMapping(value="/catalogedit",method=RequestMethod.POST)
	public String catalogedit(LovMember lovMember,KstarPrjLst prjLst){

		quotService.updatePrjLst(prjLst);

		//quotService.updateAvgttl(lovMember, prjLst);
		
		String totalAmount="";
		
		if(prjLst.getPrdPrc()!=null){
			//总金额
			totalAmount = quotService.getTotalamount(prjLst.getQuotCode());
		}
		
		
		KstarQuot quot = quotService.getKstarQuot(prjLst.getQuotCode());
		quot.setAmount(totalAmount);
		
		
		quotService.updateQuot(quot);
		
		
		return sendSuccessMessage("");
	}
	
	
	
	@ResponseBody
	@RequestMapping(value="/deletecatalog",method=RequestMethod.POST)
	public String deletePrjLst(String id,String qid){

		quotService.deletePrjLst(id);
		//quotService.updateAllAvgttlByQcode(qid);
		
		//总金额
		String totalAmount = quotService.getTotalamount(qid);
		KstarQuot quot = quotService.getKstarQuot(qid);
		quot.setAmount(totalAmount);
		quotService.updateQuot(quot);
	
		return sendSuccessMessage();
	}
	
//	@ResponseBody
//	@RequestMapping(value="/deletecatalog",method=RequestMethod.POST)
//	public String deletePrjLst(String id,String qid){
//		lovMemberService.delete(id);
//		quotService.deletePrjLst(id,qid);
//		//quotService.updateAllAvgttlByQcode(qid);
//		
//		//总金额
//		String totalAmount = quotService.getTotalamount(qid);
//		KstarQuot quot = quotService.getKstarQuot(qid);
//		quot.setAmount(totalAmount);
//		quotService.updateQuot(quot);
//	
//		return sendSuccessMessage();
//	}
	
	
//	
//	@ResponseBody
//	@RequestMapping(value="/catalogmove",method=RequestMethod.POST)
//	public String catalogmove(String qid,String dragNodeId,String newParentNodeId){
//		lovMemberService.move(dragNodeId,newParentNodeId);
//		//quotService.updateAllAvgttlByQcode(qid);
//		return sendSuccessMessage();
//	}
//	
	
	
	
//	@ResponseBody
//	@RequestMapping("/treecatalog")
//	public String tree(Condition condition,HttpServletRequest request){
//		ActionUtil.requestToCondition(condition, request);
//		String groupCode = condition.getStringCondition("groupCode");
//		String groupId = condition.getStringCondition("groupId");
//		
//		String qid = condition.getStringCondition("qid");
//		
//		if(StringUtil.isEmpty(groupId) && StringUtil.isEmpty(groupCode)){
//			throw new AnneException("无效的参数访问");
//		}
//		if(StringUtil.isEmpty(groupId)){
//			LovGroup group = lovGroupService.getByCode(groupCode);
//			if(group == null){
//				throw new AnneException("无效的参数访问");
//			}
//			groupId = group.getId();
//		}
//		String parentId = condition.getStringCondition("id");
//		condition.getFilterObject().addCondition("groupId", "=", groupId);
//		condition.getFilterObject().addCondition("deleteFlag", "=", "N");
//		if(parentId == null){
//			parentId = "ROOT";
//		}
//		condition.getFilterObject().addCondition("parentId", "=", parentId);
//		
//		condition.getFilterObject().addCondition("memo", "=", qid);
//		
//		List<LovMember> list = lovMemberService.getList(condition);
//		return sendSuccessMessage(list);
//	}
	
}
