package com.ibm.kstar.action.bizopp.protoApplyInfo.apply;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.ActionUtil;

import com.ibm.kstar.api.bizopp.IBizBaseService;
import com.ibm.kstar.entity.bizopp.Prototype;
import com.ibm.kstar.interceptor.system.permission.NoRight;
import org.xsnake.web.utils.StringUtil;

/**
 * Date: Jan 12, 2017 14:38:08 <br/>
 * 
 * @author Wutw
 * @version
 * @since JDK 1.7
 * @see
 */

@Controller
@RequestMapping("/bizPrototype")
public class BizPrototypeAction extends BaseAction {

	@Autowired
	IBizBaseService bizService;

//	@RequestMapping("/index")
//	public String index(String id, Model model) {
//		return forward("bizPrototypeIndex");
//	}

	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(String systemId, PageCondition condition) throws Exception {

		IPage p = bizService.queryPrototype(condition, systemId);

		return sendSuccessMessage(p);
	}


	@RequestMapping("/add")
	public String add(String systemId, Model model){

		Prototype prototype = new Prototype();
//		prototype.setType("10");
		prototype.setSystemPid(systemId);

		model.addAttribute("entity",prototype);
		return forward("bizPrototypeAdd");
	}
	
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(Prototype entity, HttpServletRequest request) {
		if(StringUtil.isEmpty(entity.getProductPrice())){
			entity.setProductPrice("0");
		}
		if(StringUtil.isEmpty(entity.getApplyCount())){
			entity.setApplyCount("0");
		}
		entity.setApplyAmount(String.valueOf(Integer.parseInt(entity.getApplyCount())*Double.parseDouble(entity.getProductPrice())));
		bizService.save(entity);
		return sendSuccessMessage(entity.getId());
	}
	
	@RequestMapping("/edit")
	public String edit(String id,Model model){
		if(id==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		Prototype entity = bizService.getEntity(id, new Prototype());
		if(entity==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		model.addAttribute("entity",entity);
		return forward("bizPrototypeAdd");
	}
	
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(Prototype bizopp){
		bizService.update(bizopp);
		return sendSuccessMessage(bizopp.getId());
	}
	
	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		bizService.delete(id, Prototype.class);
		return sendSuccessMessage();
	}
}
