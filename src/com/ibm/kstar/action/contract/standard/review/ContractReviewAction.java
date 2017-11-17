/**
 * 
 */
package com.ibm.kstar.action.contract.standard.review;

import com.ibm.kstar.api.contract.IContractService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.contract.Contract;
import com.ibm.kstar.entity.quot.KstarPrjEvl;
import com.ibm.kstar.interceptor.system.permission.NoRight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.page.PageImpl;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.StringUtil;
import org.xsnake.xflow.api.IHistoryService;
import org.xsnake.xflow.api.model.HistoryActivityInstance;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author ibm
 *
 */
@Controller
@RequestMapping("/standard/review")
//@Scope("prototype")
public class ContractReviewAction extends BaseAction {
	Logger logger = LoggerFactory.getLogger(getClass());

	private final static String CONTRACT_PROCESS_KEY = "contrReview";

	@Autowired
	private IContractService contractService;
	@Autowired
	private ILovMemberService lovMemberService;
	@Autowired
	IHistoryService historyService;

	/**
	 * 合同评审成员
	 *
	 * @param id
	 * @return
	 * @throws Exception
	 */ 	

	@NoRight
	@ResponseBody
	@RequestMapping(value="/page")
	public String page(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request); 
		condition.setCondition("contrId", request.getParameter("contrId")); 
		String contrId = condition.getStringCondition("contrId");  
		condition.getFilterObject().addCondition("contrId", "eq", contrId);
		IPage p = contractService.queryPrjevl(condition);

		return sendSuccessMessage(p);
	}
	

	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(KstarPrjEvl prjevl, HttpServletRequest request) {
//		String typ=request.getParameter("typ");	
		String ctyp=request.getParameter("contrRevFormCTp");	
		String typ=ctyp;
		LovMember lov = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "01"); // 未启动
		prjevl.setEvlSt(lov.getId());
		contractService.savePrjevl(prjevl, typ);
		return sendSuccessMessage(prjevl.getQuotCode());
	}
	
	
	@RequestMapping("/add")
	public String add(String quotCode, String typ, Model model){
		String contrId = quotCode; 
		Contract contr= contractService.get(contrId);
		model.addAttribute("contr",contr);
		model.addAttribute("typ", typ);
		return forward("contractReview");
	}
	
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(KstarPrjEvl prjevl){
		contractService.updatePrjEvl(prjevl);
		return sendSuccessMessage();
	}
	
	
	@RequestMapping("/edit")
	public String edit(String id,Model model){
		if(id==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		KstarPrjEvl prjevl = contractService.getKstarPrjEvl(id);
		if(prjevl==null){
			throw new AnneException("没有找到需要修改的数据");
		}

//		ContrPay contrPay = contrPayService.get(id); 
		Contract contr= contractService.get(prjevl.getQuotCode()); 
		model.addAttribute("contr",contr);
		model.addAttribute("prjevlInfo",prjevl);
		return forward("contractReview");
	}
	

	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(@RequestParam("ids[]") String[] ids){
		contractService.deletePrjevl(ids);
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/startContractReviewProcess", method = RequestMethod.POST)
	public String startContractReviewProcess( @RequestParam("ids[]") String[] ids,String typ,String contrRevFormCTp , HttpServletRequest request) throws Exception{
//		String[] _ids = ids.split(",");

		String contrId = request.getParameter("contractId");
		if (StringUtil.isEmpty(contrId)) {
			return sendErrorMessage("合同Id不能为空");
		}

		Contract contract = contractService.get(contrId);
		if (contract == null) {
			return sendErrorMessage("合同不存在");
		}
		contractService.startContractReviewProcess(getUserObject(), ids, contrRevFormCTp);
		return sendSuccessMessage();
	}
	
	/**
	 * 评审完成
	 * @param contrId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/completeContrReviewProcess", method = RequestMethod.POST)
	public String completeContrReviewProcess( String contrId,HttpServletRequest request) throws Exception{
//		String[] _ids = ids.split(","); 		
		contractService.completeContrReviewProcess(getUserObject(), contrId);
		return sendSuccessMessage();
	}

	@NoRight
	@ResponseBody
	@RequestMapping(value="/hisItem")
	public String hisItem(String contrId, HttpServletRequest request) {
		
		List<HistoryActivityInstance> list = contractService.getHistoryActivityInstanceAllByContrId(contrId);

		return sendSuccessMessage(new PageImpl(list, 1, 100, list.size()));
	}
	
	
}
