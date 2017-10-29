package com.ibm.kstar.action.demo;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;
import org.xsnake.web.page.PageImpl;
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.ActionUtil;

import com.ibm.kstar.api.product.IProductService;

@Controller
@RequestMapping("/demo")
public class DemoAction extends BaseAction {

	@Autowired
	IProductService productService;
	
	@RequestMapping("/index")
	public String index(String id,Model model){
		return forward("index");
	}
	
	@RequestMapping("/index2")
	public String index2(String id,Model model){
		return forward("index2");
	}
	
	
	@RequestMapping("/info3")
	public String info3(String id,Model model){
		TabMain tabMain = new TabMain();
		tabMain.addTab("test1", "/demo/info2.html");
		tabMain.addTab("test2", "/demo/index.html");
		tabMain.addTab("test3", "/demo/info4.html");
		
		model.addAttribute("tabMain",tabMain);
		return forward("info3");
	}
	
	@RequestMapping("/info1")
	public String info(String id,Model model){
		model.addAttribute("id",id);
		return forward("info1");
	}
	
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
//		IPage p = productService.query(condition);
		IPage p = new PageImpl(new ArrayList<>(), 1, 20, 2);
		return sendSuccessMessage(p);
	}
	
	@RequestMapping("/info2")
	public String info2(String id,Model model){
		model.addAttribute("id",id);
		return forward("info2");
	}
	
	@RequestMapping("/edit")
	public String edit(String id,Model model){
		model.addAttribute("id",id);
		return forward("demo");
	}
	  
	@RequestMapping("/add")
	public String add(String id,Model model){
		model.addAttribute("id",id);
		return forward("demo");
	}
	  
	@ResponseBody
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public String add(){
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(){
		productService.todo();
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value="/autoComplete.html" ,method = RequestMethod.POST)
	public String autoComplete(Condition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		return null;
	}

	@RequestMapping("/demo1")
	public String demo1(Model model){
		model.addAttribute("ts","297edff85a7e1704015a7e3cd0e1000a");
		return forward("demo1");
	}


}
