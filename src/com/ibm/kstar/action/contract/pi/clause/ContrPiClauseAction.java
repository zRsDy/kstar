/**
 * 
 */
package com.ibm.kstar.action.contract.pi.clause;

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

import com.ibm.kstar.api.contract.IContrClauseService;
import com.ibm.kstar.api.contract.IContractService;
import com.ibm.kstar.entity.contract.ContrClause;
import com.ibm.kstar.entity.contract.Contract;
import com.ibm.kstar.interceptor.system.permission.NoRight;

/**
 * @author ibm
 *
 */
@Controller
@RequestMapping("/pi/clause")
//@Scope("prototype")
public class ContrPiClauseAction extends BaseAction {
	Logger logger = LoggerFactory.getLogger(getClass());

	private final static String CONTRACT_PROCESS_KEY = "contrPiClause";
 	
	@Autowired
	private IContractService contractService;
	 
	@Autowired
	private IContrClauseService contrClauseService;
	
	@NoRight
	@ResponseBody
	@RequestMapping(value = "/page")
	public String page(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request); 
		condition.setCondition("contrId", request.getParameter("contrId")); 
		String contrId = condition.getStringCondition("contrId"); 
		
//		String searchKey = condition.getStringCondition("searchKey");
//		condition.getFilterObject().addCondition("deleteFlag", "eq", "N");
		condition.getFilterObject().addCondition("contrId", "eq", contrId); 
//		if(searchKey !=null){
//			condition.getFilterObject().addOrCondition("name", "like", "%"+searchKey+"%");
//			condition.getFilterObject().addOrCondition("code", "like", "%"+searchKey+"%");
//		}
		IPage p = contrClauseService.query(condition);

		return sendSuccessMessage(p);
	}
	
	@RequestMapping("/add")
	public String add(String contrId, Model model){
		Contract contr= contractService.get(contrId);
		model.addAttribute("contr",contr);
		return forward("contrPiClause");
	}

	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(ContrClause contrClause, HttpServletRequest request) { 
		contrClauseService.save(contrClause);
		return sendSuccessMessage();
	}	
	
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(ContrClause contrClause){
		contrClauseService.update(contrClause);
		return sendSuccessMessage();
	}
	
	@RequestMapping("/edit")
	public String edit(String id,Model model){ 
		ContrClause contrClause = contrClauseService.get(id); 
		Contract contr= contractService.get(contrClause.getContrId());
		model.addAttribute("contrClause",contrClause);
		model.addAttribute("contr",contr);
		return forward("contrPiClause");
	}

	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		contrClauseService.delete(id);
		return sendSuccessMessage();
	}
	
	
	
	

}
