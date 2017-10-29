package com.ibm.kstar.action.bizopp.info.contact;

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

import com.ibm.kstar.api.bizopp.IBizoppService;
import com.ibm.kstar.entity.bizopp.BizContact;
import com.ibm.kstar.interceptor.system.permission.NoRight;

/**
 * Date: Feb 26, 2017 15:15:06 <br/>
 * 
 * @author gaoyuliang
 * @version
 * @since JDK 1.7
 * @see
 */

@Controller
@RequestMapping("/bizContact")
public class BizContactAction extends BaseAction {

	@Autowired
	IBizoppService bizoppService;

	@RequestMapping("/index")
	public String index(String id, Model model) {
		return forward("bizContactIndex");
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) throws Exception {
		String bizOrgId = request.getParameter("bizOrgId");
		bizOrgId = bizOrgId == null ? "" : bizOrgId;
		ActionUtil.requestToCondition(condition, request);
		
		condition.getFilterObject().addCondition("bizOrgId", "eq", bizOrgId);
        IPage p = bizoppService.queryDecChainContact(condition);
        
		return sendSuccessMessage(p);
	}

	@RequestMapping("/add")
	public String add(String bizOrgId, Model model){
		model.addAttribute("bizOrgId", bizOrgId);
		return forward("bizContactAdd");
	}
	
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(BizContact entity, HttpServletRequest request) {

		bizoppService.saveBizContact(entity);
		return sendSuccessMessage(entity.getId());
	}
	
	@RequestMapping("/edit")
	public String edit(String id,Model model){
		if(id == null){
			throw new AnneException("没有找到需要修改的数据");
		}
		BizContact entity = bizoppService.getBizContactEntity(id);
		if(entity == null){
			throw new AnneException("没有找到需要修改的数据");
		}
		model.addAttribute("entity",entity);
		return forward("bizContactAdd");
	}
	
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(BizContact bizopp){
		bizoppService.updateBizContact(bizopp);
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		bizoppService.deleteBizContact(id);
		return sendSuccessMessage();
	}
}
