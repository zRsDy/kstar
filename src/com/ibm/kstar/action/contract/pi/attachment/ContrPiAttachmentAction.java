/**
 * 
 */
package com.ibm.kstar.action.contract.pi.attachment;

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

import com.ibm.kstar.api.contract.IContractPiService;
import com.ibm.kstar.entity.contract.Contract;
import com.ibm.kstar.entity.quot.KstarAtt;
import com.ibm.kstar.interceptor.system.permission.NoRight;

/**
 * @author ibm
 *
 */
@Controller
@RequestMapping("/pi/attachment")
//@Scope("prototype")
public class ContrPiAttachmentAction extends BaseAction {
	Logger logger = LoggerFactory.getLogger(getClass());

	private final static String CONTRACT_PROCESS_KEY = "contrReview";

	@Autowired
	private IContractPiService contractPiService; 
	 	 
	@NoRight
	@ResponseBody
	@RequestMapping(value = "/page")
	public String page(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request); 
		condition.setCondition("contrId", request.getParameter("contrId")); 
		String contrId = condition.getStringCondition("contrId");  
		condition.getFilterObject().addCondition("contrId", "eq", contrId);
		IPage p = contractPiService.queryAtt(condition);

		return sendSuccessMessage(p);
	}	

	@RequestMapping("/add")
	public String add(String quotCode, Model model){
		String contrId = quotCode; 
		Contract contr = contractPiService.get(contrId);
		model.addAttribute("contr",contr);
		return forward("contrPiAttachment");
	}

	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(KstarAtt att, HttpServletRequest request) {
		List<IUploadFile> list = UploadUtils.uploadFile(request, "/aaaa");
		if(!list.isEmpty()){
			att.setcNotes(list.get(0).getRealPath());
		}
		contractPiService.saveAtt(att);
		return sendSuccessMessage(att.getQuotCode());
	}
	
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(KstarAtt att){
		contractPiService.updateAtt(att);
		return sendSuccessMessage();
	}
	
	
	@RequestMapping("/edit")
	public String edit(String id,Model model){ 
		KstarAtt att = contractPiService.getKstarAtt(id);
		Contract contr = contractPiService.get(att.getQuotCode()); 
		model.addAttribute("contr",contr);
		model.addAttribute("attInfo",att);
		return forward("contrPiAttachment");
	}

	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		contractPiService.deleteAtt(id);
		return sendSuccessMessage();
	}
	
	

}
