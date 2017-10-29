package com.ibm.kstar.participant.action;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.xflow.api.Participant;

import com.ibm.kstar.service.IParticipantService;

@Controller
@RequestMapping("/participant")
public class ParticipantAction extends BaseAction{
	
	@Autowired
	IParticipantService participantService;
	
	@ResponseBody
	@RequestMapping("/directLeadership")
	public String directLeadership(String creatorId,HttpServletResponse response){
		List<Participant> list = participantService.getCreatorDirectLeadership(creatorId);
		return sendSuccessMessage(list);
	}
	
	@ResponseBody
	@RequestMapping("/level5")
	public String level5(String creatorId,HttpServletResponse response){
		List<Participant> list = participantService.getByLevel(creatorId,5);
		return sendSuccessMessage(list);
	}
	
	@ResponseBody
	@RequestMapping("/level4")
	public String level4(String creatorId,HttpServletResponse response){
		List<Participant> list = participantService.getByLevel(creatorId,4);
		return sendSuccessMessage(list);
	}
	
	@ResponseBody
	@RequestMapping("/level3")
	public String level3(String creatorId,HttpServletResponse response){
		List<Participant> list = participantService.getByLevel(creatorId,3);
		return sendSuccessMessage(list);
	}
	
	@ResponseBody
	@RequestMapping("/level2")
	public String level2(String creatorId,HttpServletResponse response){
		List<Participant> list = participantService.getByLevel(creatorId,2);
		return sendSuccessMessage(list);
	}
	
	@ResponseBody
	@RequestMapping("/level1")
	public String level1(String creatorId,HttpServletResponse response){
		List<Participant> list = participantService.getByLevel(creatorId,1);
		return sendSuccessMessage(list);
	}
	
}
