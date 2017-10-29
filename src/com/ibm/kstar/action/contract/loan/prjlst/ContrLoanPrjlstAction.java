/**
 * 
 */
package com.ibm.kstar.action.contract.loan.prjlst;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import org.xsnake.web.log.LogOperate;
import org.xsnake.web.page.IPage;
import org.xsnake.web.page.PageImpl;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.action.system.lov.member.LovMemberAction;
import com.ibm.kstar.api.contract.IContractLoanService;
import com.ibm.kstar.api.contract.IContractService;
import com.ibm.kstar.api.product.IProLovService;
import com.ibm.kstar.api.system.lov.entity.LovGroup;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.contract.Contract;
import com.ibm.kstar.entity.quot.KstarPrjLst;

/**
 * @author ibm
 *
 */
@Controller
@RequestMapping("/loan/prjlst")
public class ContrLoanPrjlstAction extends LovMemberAction  {
	Logger logger = LoggerFactory.getLogger(getClass());

	private final static String CONTRACT_PROCESS_KEY = "contrPrjlst";
 	
	@Autowired
	private IContractService contractService;

	@Autowired
	private IContractLoanService contractLoanService;
	
	@Autowired
	IProLovService proLovService; 
	
	@ResponseBody
	@RequestMapping(value="/prjLstPage")
	public String pagePrjLst(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		condition.setCondition("contrId", request.getParameter("contrId"));
		IPage p = contractLoanService.queryPrjLst(condition);
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
		return forward("contrLoanPrjlst");
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
		contractLoanService.savePrjLst(prjLst,cType);
		
		contractLoanService.updateAllAvgttl(lovMember, prjLst); 
		
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
		KstarPrjLst prjLst = contractLoanService.getKstarPrjLst(contrId, lvId);
		model.addAttribute("prjLst",prjLst);
		
		return forward("contrLoanPrjlst");
	}
	
	@ResponseBody
	@RequestMapping(value="/editPrjlst",method=RequestMethod.POST)
	public String editPrjlst(LovMember lovMember,KstarPrjLst prjLst){

		contractLoanService.updatePrjLst(lovMember,prjLst);
		contractLoanService.updateAllAvgttl(lovMember, prjLst); 
		
		return sendSuccessMessage();
	}
	
	
	@ResponseBody
	@RequestMapping(value="/deletePrjlst",method=RequestMethod.POST)
	public String deletePrjlst(String id,String contrId){
		lovMemberService.delete(id);
		contractLoanService.deletePrjLst(id,contrId);
		contractLoanService.updateAllAvgttlByQcode(contrId);
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
	

	@LogOperate(module="借货合同核销",notes="${user}查看对应客户产品工程清单数据分页信息")
	@ResponseBody
	@RequestMapping(value="/veriPrjLstPage")
	public String pageVeriPrjLst(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
//		condition.setCondition("contrId", request.getParameter("contrId"));
		String contrId =  request.getParameter("contrId");
		Contract contr= contractService.get(contrId);
		String custCode=contr.getCustCode();
		String oriPrjlstId=request.getParameter("oriPrjlstId");
		String prdPrc=request.getParameter("prdPrc")==null?"0":request.getParameter("prdPrc");
		condition.setCondition("oriPrjlstId", oriPrjlstId);
		condition.setCondition("custCode", custCode);
		
		IPage p = contractLoanService.queryVeriPrjLst(condition);
		return sendSuccessMessage(p);
	}
	

	@LogOperate(module="借货合同认款核销",notes="${user}查看对应客户产品工程清单数据分页信息")
	@ResponseBody
	@RequestMapping(value="/veriOrderPrjLstPage")
	public String pageVeriOrderPrjLst(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
//		condition.setCondition("contrId", request.getParameter("contrId"));
		String contrId =  request.getParameter("contrId");
		Contract contr= contractService.get(contrId);
		condition.setCondition("contrId", contrId);
		
		IPage p = contractLoanService.queryVeriOrderPrjLst(condition);
		return sendSuccessMessage(p);
	}
	
	
	

}
