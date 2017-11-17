/**
 * 
 */
package com.ibm.kstar.action.contract.standard.pay;

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
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.ActionUtil;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.api.contract.IContrPayService;
import com.ibm.kstar.api.contract.IContractService;
import com.ibm.kstar.entity.contract.ContrPay;
import com.ibm.kstar.entity.contract.Contract;
import com.ibm.kstar.interceptor.system.permission.NoRight;

/**
 * @author ibm
 *
 */
@Controller
@RequestMapping("/standard/pay")
//@Scope("prototype")
public class ContractPayAction extends BaseAction {
	Logger logger = LoggerFactory.getLogger(getClass());

	private final static String CONTRACT_PROCESS_KEY = "contrReview";
 	
	@Autowired
	private IContractService contractService;
	
	@Autowired
	private IContrPayService contrPayService;

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
		IPage p = contrPayService.query(condition);

		return sendSuccessMessage(p);
	}
	
	@RequestMapping("/add")
	public String add(String contrId, Model model){
		Contract contr= contractService.get(contrId);
		model.addAttribute("contr",contr);
		return forward("contractPay");
	}

	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(ContrPay contrPay, HttpServletRequest request) {
		String contrNo = request.getParameter("contrNo");  
		contrPayService.save(contrPay, contrNo);
		return sendSuccessMessage(contrPay.getPayNo());
	}	
	
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(ContrPay contrPay){
		contrPayService.update(contrPay);
		return sendSuccessMessage();
	}
	
	@RequestMapping("/edit")
	public String edit(String id,Model model){ 
		ContrPay contrPay = contrPayService.get(id); 
		Contract contr= contractService.get(contrPay.getContrId());
		model.addAttribute("contrPay",contrPay);
		model.addAttribute("contr",contr);
		return forward("contractPay");
	}

	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		contrPayService.delete(id);
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value="/refFrameNoPay",method=RequestMethod.POST)
	public String copyPayPlanByContrId(String qid){
		contrPayService.copyPayPlanByContrId(qid);
		return sendSuccessMessage();
	}

	@ResponseBody
	@RequestMapping(value="/submitLins",method=RequestMethod.POST)
	public String saveLines(String listStr, String contrId){
		List<ContrPay> lines = JSON.parseArray(listStr, ContrPay.class);
//		System.out.println("------122---->"+lines.size()+"<-- NAME=-->"+lines.get(0).getOrigClause()+"<---");
		contrPayService.saveOrUpdateContrPayList(lines, contrId);
		return sendSuccessMessage();
	}
	
	

}
