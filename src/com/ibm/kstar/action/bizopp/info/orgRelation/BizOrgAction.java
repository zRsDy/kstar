package com.ibm.kstar.action.bizopp.info.orgRelation;

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
import com.ibm.kstar.entity.bizopp.BizOrg;
import com.ibm.kstar.interceptor.system.permission.NoRight;

/**
 * 
 * 
 * @author gaoyuliang
 * @version
 * @since JDK 1.7
 * @see
 */

@Controller
@RequestMapping("/bizOrg")
public class BizOrgAction extends BaseAction {

	@Autowired
	IBizoppService bizoppService;
	
	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) throws Exception {
		ActionUtil.requestToCondition(condition, request);
		String bizOppId = request.getParameter("bizOppId");
		condition.getFilterObject().addCondition("bizOppId", "eq", bizOppId);
		
		IPage p = bizoppService.queryBizOppOrg(condition);

		return sendSuccessMessage(p);
	}


	@RequestMapping("/add")
	public String add(String bid, Model model){
		model.addAttribute("bizOppId", bid);
		return forward("bizOrgAdd");
	}
	
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(BizOrg entity, HttpServletRequest request) {
		String bizOppId = request.getParameter("bid");
		entity.setBizOppId(bizOppId);
		bizoppService.saveBizOrg(entity);
		return sendSuccessMessage(entity.getId());
	}
	
	@RequestMapping("/edit")
	public String edit(String id, Model model){
		if(id == null){
			throw new AnneException("没有找到需要修改的数据");
		}
		BizOrg entity = bizoppService.getBizOrgEntity(id);
		if(entity == null){
			throw new AnneException("没有找到需要修改的数据");
		}
		model.addAttribute("entity",entity);
		return forward("bizOrgAdd");
	}
	
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(BizOrg bizopp){
		bizoppService.updateBizOrg(bizopp);
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		bizoppService.deleteBizOrg(id);
		return sendSuccessMessage();
	}
}
