package com.ibm.kstar.action.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;

import com.ibm.kstar.api.contract.IContractService;

@Controller
@RequestMapping("/http")
public class ContractHttpServiceAction extends BaseAction{

	@Autowired
	IContractService contractService;

	@ResponseBody
	@RequestMapping("/updateContrPrjlstMaterCodeByPrdId")
	public String updateContrPrjlstMaterCodeByPrdId(String proId){
		contractService.updateContrPrjlstMaterCodeByPrdId(proId);
		return sendSuccessMessage();
	}
	
}
