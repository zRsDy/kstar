/**
 * 
 */
package com.ibm.kstar.action.contract.standard.shipaddr;

import java.util.Date;

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
import org.xsnake.web.log.LogOperate;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.ActionUtil;

import com.ibm.kstar.api.contract.IContrAddrService;
import com.ibm.kstar.api.contract.IContractService;
import com.ibm.kstar.entity.contract.ContrAddr;
import com.ibm.kstar.entity.contract.Contract;
import com.ibm.kstar.interceptor.system.permission.NoRight;

/**
 * @author ibm
 *
 */
@Controller
@RequestMapping("/standard/shipaddr")
//@Scope("prototype")
public class ContractAddressAction extends BaseAction {
	Logger logger = LoggerFactory.getLogger(getClass());

	private final static String CONTRACT_PROCESS_KEY = "contrReview";

	@Autowired
	private IContractService contractService;
	
	@Autowired
	private IContrAddrService contractAddrService;

	@NoRight
	@ResponseBody
	@RequestMapping(value = "/page")
	public String pageMem(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request); 
		condition.setCondition("contrId", request.getParameter("contrId")); 
		String contrId = condition.getStringCondition("contrId");  
		condition.getFilterObject().addCondition("contrId", "eq", contrId);
		
		IPage p = contractAddrService.query(condition);

		return sendSuccessMessage(p);
	}

	@LogOperate(module = "合同管理：发货地址", notes = "${user}点击地址添加功能")
	@RequestMapping("/add")
	public String add(String contrId, String typ,Model model){
		Contract contr= contractService.get(contrId);
		model.addAttribute("contr",contr);
		model.addAttribute("typ", typ);
		return forward("contractAddress");
	}

	@LogOperate(module = "合同管理：发货地址", notes = "${user}保存发货地址")
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(ContrAddr contrAddr, HttpServletRequest request) {  
		String typ= request.getParameter("typ");
		contrAddr.setCType(typ);
		contrAddr.setCreatedById(getUserObject().getEmployee().getId());
		contrAddr.setCreatedAt(new Date());
		contrAddr.setCreatedPosId(getUserObject().getPosition().getId());
		contrAddr.setCreatedOrgId(getUserObject().getOrg().getId());
		contrAddr.setUpdatedById(getUserObject().getEmployee().getId());
		contrAddr.setUpdatedAt(new Date());
		contractAddrService.save(contrAddr);
		return sendSuccessMessage();
	}	
	
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(ContrAddr contrAddr){
		contractAddrService.update(contrAddr);
		return sendSuccessMessage();
	}
	
	@RequestMapping("/edit")
	public String edit(String id,Model model){ 
		ContrAddr contrAddr = contractAddrService.get(id); 
		Contract contr= contractService.get(contrAddr.getContrId());
		model.addAttribute("contrAddr",contrAddr);
		model.addAttribute("contr",contr);
		return forward("contractAddress");
	}

	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		contractAddrService.delete(id);
		return sendSuccessMessage();
	}
	
	
	
	

}
