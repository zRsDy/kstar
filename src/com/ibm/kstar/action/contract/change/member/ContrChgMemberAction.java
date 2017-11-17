/**
 * 
 */
package com.ibm.kstar.action.contract.change.member;

import javax.servlet.http.HttpServletRequest;

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
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.ActionUtil;

import com.ibm.kstar.api.contract.IContrChangeService;
import com.ibm.kstar.api.contract.IContractService;
import com.ibm.kstar.entity.contract.ContrChange;
import com.ibm.kstar.entity.quot.KstarMemInfo;
import com.ibm.kstar.interceptor.system.permission.NoRight;

/**
 * @author ibm
 *
 */
@Controller
@RequestMapping("/change/member")
//@Scope("prototype")
public class ContrChgMemberAction extends BaseAction {
	Logger logger = LoggerFactory.getLogger(getClass());

	private final static String CONTRACT_PROCESS_KEY = "contrReview";

	@Autowired
	private IContractService contractService;

	@Autowired
	private IContrChangeService  changeService ;

	/**
	 * 团队成员
	 *
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@NoRight
	@ResponseBody
	@RequestMapping(value = "/page")
	public String pageMem(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request); 
		condition.setCondition("contrId", request.getParameter("contrId")); 
		String contrId = condition.getStringCondition("contrId");  
		condition.getFilterObject().addCondition("contrId", "eq", contrId);
		IPage p = contractService.queryMem(condition);

		return sendSuccessMessage(p);
	}
	
	@RequestMapping("/add")
	public String addMem(String quotCode, Model model){
		String contrId = quotCode; 
		ContrChange contrChg = changeService.get(contrId);
		model.addAttribute("contrChg",contrChg);
		return forward("contrChgMember");
	}

	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addMem(KstarMemInfo mem, HttpServletRequest request) { 
		contractService.saveMem(mem);
		return sendSuccessMessage(mem.getQuotCode());
	}	
	
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String editMem(KstarMemInfo mem){
		contractService.updateMem(mem);
		return sendSuccessMessage();
	}
	
	@RequestMapping("/edit")
	public String editMem(String id,Model model){
//		if(id==null){
//			throw new AnneException("没有找到需要修改的数据");
//		}
		KstarMemInfo mem = contractService.getKstarMemInfo(id);
//		if(mem==null){
//			throw new AnneException("没有找到需要修改的数据");
//		}
		ContrChange contrChg = changeService.get(mem.getQuotCode()); 
		model.addAttribute("contrChg",contrChg);
		model.addAttribute("memInfo",mem);
		return forward("contrChgMember");
	}

	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String deleteMem(String id){
		contractService.deleteMem(id);
		return sendSuccessMessage();
	}
	
	
	
	

}
