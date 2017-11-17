/**
 * 
 */
package com.ibm.kstar.action.contract.loan.attachment;

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
import org.xsnake.web.upload.IUploadFile;
import org.xsnake.web.upload.UploadUtils;
import org.xsnake.web.utils.ActionUtil;

import com.ibm.kstar.api.contract.IContractLoanService;
import com.ibm.kstar.entity.contract.Contract;
import com.ibm.kstar.entity.quot.KstarAtt;

/**
 * @author ibm
 *
 */
@Controller
@RequestMapping("/loan/attachment")
//@Scope("prototype")
public class ContrLoanAttachmentAction extends BaseAction {
	Logger logger = LoggerFactory.getLogger(getClass());

	private final static String CONTRACT_PROCESS_KEY = "contrReview";
	 
	@Autowired
	private IContractLoanService contractLoanService;
	 	 

	@ResponseBody
	@RequestMapping(value = "/page")
	public String page(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request); 
		condition.setCondition("contrId", request.getParameter("contrId")); 
		String contrId = condition.getStringCondition("contrId");  
		condition.getFilterObject().addCondition("contrId", "eq", contrId);
		IPage p = contractLoanService.queryAtt(condition);

		return sendSuccessMessage(p);
	}	

	@RequestMapping("/add")
	public String add(String quotCode, Model model){
		String contrId = quotCode; 
		Contract contr = contractLoanService.get(contrId);
		model.addAttribute("contr",contr);
		return forward("contrLoanAttachment");
	}

	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(KstarAtt att, HttpServletRequest request) {
		List<IUploadFile> list = UploadUtils.uploadFile(request, "/aaaa");
		if(!list.isEmpty()){
			att.setcNotes(list.get(0).getRealPath());
		}
		contractLoanService.saveAtt(att);
		return sendSuccessMessage(att.getQuotCode());
	}
	
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(KstarAtt att){
		contractLoanService.updateAtt(att);
		return sendSuccessMessage();
	}
	
	
	@RequestMapping("/edit")
	public String edit(String id,Model model){ 
		KstarAtt att = contractLoanService.getKstarAtt(id);
		Contract contr = contractLoanService.get(att.getQuotCode()); 
		model.addAttribute("contr",contr);
		model.addAttribute("attInfo",att);
		return forward("contrLoanAttachment");
	}

	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		contractLoanService.deleteAtt(id);
		return sendSuccessMessage();
	}
	
	

}
