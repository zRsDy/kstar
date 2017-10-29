package com.ibm.kstar.action.bizopp.info.competition;

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
import com.ibm.kstar.entity.bizopp.BizCompetitor;
import com.ibm.kstar.interceptor.system.permission.NoRight;

/**
 * Date: Feb 26, 2017 14:38:08 <br/>
 * 
 * @author gaoyuliang
 * @version
 * @since JDK 1.7
 * @see
 */

@Controller
@RequestMapping("/bizCompetitor")
public class BizCompetitortAction extends BaseAction {

//	@Autowired
//	IBizBaseService<BizCompetitor> bizService;

	@Autowired
	IBizoppService bizoppService;
	
	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) throws Exception {
		ActionUtil.requestToCondition(condition, request);
		String bizOppId = request.getParameter("bizOppId");
		condition.getFilterObject().addCondition("bizOppId", "eq", bizOppId);
		
		IPage p = bizoppService.queryBizCompetitor(condition);;

		return sendSuccessMessage(p);
	}


	@RequestMapping("/add")
	public String add(String bizOppId, Model model){
		model.addAttribute("bizOppId", bizOppId);
		return forward("bizCompetitorAdd");
	}
	
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(BizCompetitor entity, HttpServletRequest request) {
		String bizOppId = request.getParameter("bizOppId");
		entity.setBizOppId(bizOppId);
		bizoppService.saveBizCompetitor(entity);
		return sendSuccessMessage(entity.getId());
	}
	
	@RequestMapping("/edit")
	public String edit(String id,Model model){
		if(id == null){
			throw new AnneException("没有找到需要修改的数据");
		}
		BizCompetitor entity = bizoppService.getBizCompetitorEntity(id);
		if(entity == null){
			throw new AnneException("没有找到需要修改的数据");
		}
		model.addAttribute("entity",entity);
		return forward("bizCompetitorAdd");
	}
	
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(BizCompetitor bizopp){
		bizoppService.updateBizCompetitor(bizopp);
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		bizoppService.deleteBizCompetitor(id);
		return sendSuccessMessage();
	}
}
