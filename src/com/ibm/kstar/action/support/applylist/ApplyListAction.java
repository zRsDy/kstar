package com.ibm.kstar.action.support.applylist;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.log.LogOperate;

@Controller
@RequestMapping("/applyList")
public class ApplyListAction extends BaseAction {
	
	@LogOperate(module = "支持管理模块：技术支持申请", notes = "${user}跳转技术支持申请页面")
	@RequestMapping("/tecSuptApply")
	public String tecSuptApply(String id,Model model){
		
		
		return redirect("/supportApply/index");
	}
	
	@LogOperate(module = "支持管理模块：市场活动申请", notes = "${user}跳转市场活动申请页面")
	@RequestMapping("/martEvtApply")
	public String martEvtApply(String id,Model model){
		
		
		return redirect("/activityApply/activity");
	}
	
	@LogOperate(module = "支持管理模块：培训申请", notes = "${user}跳转培训申请页面")
	@RequestMapping("/trainApply")
	public String trainApply(String id,Model model){
		
		
		return redirect("/activityApply/train");
	}
	
	@LogOperate(module = "支持管理模块：服务申请", notes = "${user}跳转服务申请页面")
	@RequestMapping("/serviceApply")
	public String serviceApply(String id,Model model){
		
		
		return redirect("/serviceApply/list");
	}
	
	
	
}
