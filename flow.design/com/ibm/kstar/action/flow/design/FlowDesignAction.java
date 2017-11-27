package com.ibm.kstar.action.flow.design;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.Condition;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.StringUtil;
import org.xsnake.xflow.api.IDefinitionService;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.api.system.lov.ILovGroupService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovGroup;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.ICorePermissionService;
import com.ibm.kstar.conf.Configuration;
import com.ibm.kstar.service.IDemoService;

@Controller
@RequestMapping("/flow")
public class FlowDesignAction extends BaseAction{
	
	@Autowired
	ILovGroupService lovGroupService;
	
	@Autowired
	ILovMemberService lovMemberService;
	
	@Autowired
	ICorePermissionService corePermissionService;
	
	@Autowired
	IDefinitionService definitionService;
	
	@Autowired
	IDemoService demoService ;
	
	@Autowired
	Configuration configuration;
	
//	@ResponseBody
//	@RequestMapping("/test")
//	public String test(Model model,HttpServletRequest request) throws IOException{
//		URL url =FlowDesignAction.class.getClassLoader().getResource("test.xml");
//		File f = new File(url.getFile());
//		String xml = new String(Files.readAllBytes(Paths.get(f.getPath())));
//		System.out.println(xml);
//		return definitionService.create(xml);
//	}
	
	@RequestMapping("/design")
	public String design(String applicationCode,Model model,HttpServletRequest request) throws Exception{
		String json = demoService.getProcessJSON(applicationCode);
		model.addAttribute("json",json);
		LovMember application = lovMemberService.getLovMemberByCode("APPLICATION",applicationCode);
		model.addAttribute("processModule",application.getCode());
		model.addAttribute("processName",application.getName());
		return forward("design");
	}
	
	@ResponseBody
	@RequestMapping(value="/design",method=RequestMethod.POST)
	public String design(String json,HttpServletRequest request) throws Exception{
		System.out.println(json);
		String xml = FlowUtils.jsonToXml(json,configuration.getServerAddress());
		definitionService.create(xml);
		return sendSuccessMessage();
	}
	
	@RequestMapping("/showTaskAttr")
	public String showTaskAttr(String type,String code,Model model,HttpServletRequest request){
		List<LovMember> lovOrg = lovMemberService.getOrgAllList();
		List<LovMember> lovOrgAndEmployee = lovMemberService.getAllTreeByOrgList(lovOrg);
		model.addAttribute("lovOrg",JSON.toJSONString(lovOrgAndEmployee));
		model.addAttribute("type",type);
		//获取到code，通过code拿到设置的应用，及变量、权限域信息
		return forward("showTaskAttr");
	}
	
	@RequestMapping("/showDecisionAttr")
	public String showDecisionAttr(String type,String code,Model model,HttpServletRequest request){
		return forward("showDecisionAttr");
	}
	
//	@RequestMapping("/taskSettingAction")
//	public String taskSettingAction(Model model,HttpServletRequest request){
//		return forward("taskSettingAction");
//	}
//	
//	@RequestMapping("/taskSettingParticipant")
//	public String taskSettingParticipant(Model model,HttpServletRequest request){
//		return forward("taskSettingParticipant");
//	}
//	
//	@RequestMapping("/taskSettingMessage")
//	public String taskSettingMessage(Model model,HttpServletRequest request){
//		return forward("taskSettingMessage");
//	}
	
	@ResponseBody
	@RequestMapping("/orgTree")
	public String tree(Condition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		String parentId = condition.getStringCondition("id");
		List<LovMember> list;
		if(StringUtil.isNotEmpty(parentId)){
			LovMember parent = lovMemberService.get(parentId);
			if(parent != null && "Y".equals(parent.getLeafFlag())){
				list = corePermissionService.getUserList(parentId);
				return sendSuccessMessage(list);
			}
		}
		if(parentId == null){
			parentId = "ROOT";
		}
		condition.getFilterObject().addCondition("groupId", "=", "ORG");
		condition.getFilterObject().addCondition("deleteFlag", "=", "N");
		condition.getFilterObject().addCondition("parentId", "=", parentId);
		list = lovMemberService.getList(condition);
		return sendSuccessMessage(list);
	}
}
