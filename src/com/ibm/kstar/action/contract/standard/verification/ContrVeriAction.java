/**
 * 
 */
package com.ibm.kstar.action.contract.standard.verification;

import com.ibm.kstar.api.contract.IContrPayService;
import com.ibm.kstar.api.contract.IContractService;
import com.ibm.kstar.entity.contract.ContrPay;
import com.ibm.kstar.entity.contract.ContrVeriDetlLstVO;
import com.ibm.kstar.entity.contract.Contract;
import com.ibm.kstar.interceptor.system.permission.NoRight;
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
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.log.LogOperate;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.ActionUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author ibm
 *
 */
@Controller
@RequestMapping("/standard/verification")
//@Scope("prototype")
public class ContrVeriAction extends BaseAction {
	Logger logger = LoggerFactory.getLogger(getClass());

	private final static String CONTRACT_PROCESS_KEY = "contrVerification";
 	
	@Autowired
	private IContractService contractService;
	
	@Autowired
	private IContrPayService contrPayService;


	@NoRight
	@LogOperate(module="借货合同核销",notes="${user}查看数据分页信息")
	@ResponseBody
	@RequestMapping(value = "/page")
	public String page(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request); 
//		condition.setCondition("contrId", request.getParameter("contrId")); 
//		String contrId = condition.getStringCondition("contrId"); 
		
//		String searchKey = condition.getStringCondition("searchKey");
//		condition.getFilterObject().addCondition("deleteFlag", "eq", "N");
//		condition.getFilterObject().addCondition("contrId", "eq", contrId); 
//
//		condition.setCondition("loanId", request.getParameter("loanId"));
//		if(searchKey !=null){
//			condition.getFilterObject().addOrCondition("name", "like", "%"+searchKey+"%");
//			condition.getFilterObject().addOrCondition("code", "like", "%"+searchKey+"%");
//		}
		IPage p = contractService.queryVeriDtl(condition);

		return sendSuccessMessage(p);
	}

	@LogOperate(module="借货合同核销",notes="${user}打开新增页面")
	@RequestMapping("/add")
	public String add(String contrId,String oriPrjlstId, String prdPrc,String amt,Model model){
//		Contract contr= contractService.get(contrId);
//		model.addAttribute("contr",contr);
		model.addAttribute("contrId",contrId);
//		model.addAttribute("materCode",materCode);
		model.addAttribute("oriPrjlstId",oriPrjlstId);
		model.addAttribute("oriPrjPrdPrc",prdPrc);
		model.addAttribute("amt",amt);
		Contract contract = contractService.get(contrId);
		if (contract != null) {
			Date createdDt = contract.getCreateTime();
			model.addAttribute("createDt", createdDt);
		}
		return forward("contrVeri");
	}

	@LogOperate(module="借货合同核销",notes="${user}提交了新增数据")
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(String contrId, ContrVeriDetlLstVO loanVeriDtlList,String oriPrjlstId, HttpServletRequest request) throws AnneException, Exception {
//		String contrNo = request.getParameter("contrNo");  
//		contrPayService.save(contrPay, contrNo);
//		return sendSuccessMessage(contrPay.getPayNo());
		if(loanVeriDtlList.getLoanVeriDtlList().size()<1){
			throw new AnneException("请至少选择一条数据进行提交");
		}
		contractService.saveVeriDtlLst(contrId,oriPrjlstId, loanVeriDtlList);
		return sendSuccessMessage();
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
		return forward("contrVeri");
	}

	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String contrVeriDetlId){
		contractService.deleteVeriDtlLst(contrVeriDetlId);
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value="/confirm",method=RequestMethod.POST)
	public String confirm(ContrPay contrPay){
		contrPayService.update(contrPay);
		return sendSuccessMessage();
	}
	
	
	

}
