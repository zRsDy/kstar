package com.ibm.kstar.action.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;

import com.ibm.kstar.api.product.IDemandService;

@Controller
@RequestMapping("/updateDemand")
public class UpdateDemandAction extends BaseAction {

	@Autowired
	IDemandService demandService;
	
//	select * from xflow_history_process_instance p where p.business_key = '8a8281b75c787100015c7b6a3f2c1ace'
//	select * from xflow_history h where h.process_instance_id = '29A61D62E11742C6B18189DDD86D8FFB' order by h.start_time desc 
	
	//补做同步PDM
	@ResponseBody
	@RequestMapping("/updateDemand")
	public String updateDemand(String businessKey,String userId) {
		try {
			demandService.doMove2Int(businessKey, userId,true);
		} catch (Exception e) {
			return sendErrorMessage(e.getMessage());
		}
		return sendSuccessMessage();
	}
	
}
