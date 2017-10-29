package com.ibm.kstar.main;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;

import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;

@Controller
@RequestMapping("/import")
public class TestImportAction extends BaseAction{

	@Autowired
	ILovMemberService lovMemberService;

	@ResponseBody
	@RequestMapping(value="/import",method=RequestMethod.POST)
	public String importLov(String groupId,HttpServletRequest request){
		List<List<Object>> list = getExcelData(request);
		List<LovMember> lovs = new ArrayList<>();
		for(List<Object> item : list){
			String id= (String)item.get(0);
			String code = (String)item.get(1);
			String name = (String)item.get(2);
			String parentId = (String)item.get(3);
			String path = (String)item.get(4);
			LovMember lov = new LovMember();
			lov.setId(id.trim());
			lov.setName(name.trim());
			lov.setCode(code.trim());
			lov.setParentId(parentId.trim());
			lov.setOptTxt1(path.trim());
			lov.setLeafFlag("N");
			lov.setGroupId(groupId.trim());
			lovs.add(lov);
		}
		lovMemberService.saveList(lovs);
		return "OK";
	}
	
	@RequestMapping(value="/import",method=RequestMethod.GET)
	public String importLov(HttpServletRequest request){
		return forward("importLov");
	}
	
}
