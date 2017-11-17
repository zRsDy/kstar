package com.ibm.kstar.action.bizopp.specialPrice;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.api.bizopp.IBizBaseService;
import com.ibm.kstar.api.bizopp.IBizoppService;
import com.ibm.kstar.entity.bizopp.SpecialPrice;
import com.ibm.kstar.entity.bizopp.SpecialPriceLine;
import com.ibm.kstar.interceptor.system.permission.NoRight;

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
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * ClassName:BizspePriceAction <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: Jan 9, 2017 14:06:23 <br/>
 * 
 * @author Wutw
 * @version
 * @since JDK 1.7
 * @see
 */

@Controller
@RequestMapping("/spePrice")
public class BizSpecialPriceAction extends BaseAction {

	@Autowired
	IBizBaseService bizService;
	
	@Autowired
	IBizoppService bizoppService;

	@RequestMapping("/index")
	public String index(String id, Model model) {
		return forward("spePriceIndex");
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) throws Exception {
		ActionUtil.requestToCondition(condition, request);
		IPage p = bizService.query(condition, SpecialPrice.class);

		return sendSuccessMessage(p);
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/line/page")
	public String linePage(PageCondition condition, String spId) throws Exception {
		IPage p = bizService.querySpecialPriceLine(condition, spId);
		return sendSuccessMessage(p);
	}

	@ResponseBody
	@RequestMapping("/line/edit")
	public String lineEdit(String id, String applyCount, String applyDiscount, String approvedDiscount) throws Exception {
//		bizService.update();
		return sendSuccessMessage();
	}


	@NoRight
	@ResponseBody
	@RequestMapping("/product/page")
	public String productPage(PageCondition condition,HttpServletRequest request) throws Exception{
		ActionUtil.requestToCondition(condition, request);
		IPage p = bizoppService.queryBizOppProductSelectList(condition,getUserObject());
		return sendSuccessMessage(p);
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/bizs")
	public String bizs(PageCondition condition,HttpServletRequest request,String _bizIds,String _productId) throws Exception{
		IPage p = bizoppService.findBizs(condition,_bizIds,_productId);
		return sendSuccessMessage(p);
	}
	
	@RequestMapping("/multiSelectList")
	public String multiSelectOrder(String pickerId, String orderType,Model model){
		model.addAttribute("pickerId",pickerId);
		return forward("productMultiSelectList");
	}
	
	@RequestMapping("/add")
	public String add(Model model){
		SpecialPrice sp = new SpecialPrice();
		sp.setApplyNumber(bizoppService.getSpecialPriceApplyNumber());
		sp.setApplyStatus(ProcessConstants.PROCESS_STATUS_Pending);
		sp.setApplyDate(new Date());
		sp.setApplicant(getUserObject().getEmployee().getId());
		sp.setCreatedById(getUserObject().getEmployee().getId());
		sp.setApplicantName(getUserObject().getEmployee().getName());
		sp.setApplyUnitName(getUserObject().getOrg().getName());
		sp.setApplyUnit(getUserObject().getOrg().getId());
		TabMain tabMainInfo = new TabMain();
		tabMainInfo.setInitAll(false);
		model.addAttribute("tabMainInfo",tabMainInfo);
		model.addAttribute("entity",sp);


		return forward("spePriceAdd");
	}
	
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(SpecialPrice entity) {

		entity.setCreatedById(getUserObject().getEmployee().getId());
		entity.setCreatedOrgId(getUserObject().getOrg().getId());
		entity.setCreatedPosId(getUserObject().getPosition().getId());
		entity.setApplyDate(new Date());
		entity.setApplyUnit(getUserObject().getOrg().getId());
		entity.setApplyStatus(ProcessConstants.PROCESS_STATUS_Pending);
		bizService.saveSpecialPrice(entity,getUserObject());

//		System.out.println(entity.getSelectedList());
		List<SpecialPriceLine> lines = JSON.parseArray(entity.getSelectedList(), SpecialPriceLine.class);

		for (SpecialPriceLine line : lines) {
			line.setSpePriceId(entity.getId());
			if (StringUtil.isEmpty(line.getId())) {
				bizService.saveSpecialPriceLine(line);
			} else {
				bizService.updateSpecialPriceLine(line);
			}
		}
		return sendSuccessMessage(entity.getId());
	}
	
	@NoRight
	@RequestMapping("/edit")
	public String edit(String id,Model model){
		if(id==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		SpecialPrice entity = bizService.getEntity(id, new SpecialPrice());
		if(entity==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		model.addAttribute("entity",entity);
		TabMain tabMainInfo = new TabMain();
		tabMainInfo.setInitAll(false);
		tabMainInfo.addTab("附件","/common/attachment/attachment.html?businessId="+id+"&businessType=ACCOUNT_FILE&docGroupCode=APPLYINFOATTM");
		tabMainInfo.addTab("团队成员", "/team/list.html?businessType=PrototypeApplyReturn&businessId="+id);
		tabMainInfo.addTab("审批历史", "/standard/history.html?contrId="+id);
		model.addAttribute("tabMainInfo",tabMainInfo);
		return forward("spePriceAdd");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(SpecialPrice entity) {

		bizService.update(entity);

		List<SpecialPriceLine> lines = JSON.parseArray(entity.getSelectedList(), SpecialPriceLine.class);

		for (SpecialPriceLine line : lines) {
			if (StringUtil.isEmpty(line.getId())) {
				bizService.saveSpecialPriceLine(line);
			} else {
				bizService.updateSpecialPriceLine(line);
			}
		}

		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		bizService.delete(id, SpecialPrice.class);
		return sendSuccessMessage();
	}

	@ResponseBody
	@RequestMapping(value = "/startProcess",method = RequestMethod.POST)
	public String startProcess(String id, String applyNumber){
		bizService.startApplyProcess(getUserObject(),id,applyNumber);
		return sendSuccessMessage();
	}

	@RequestMapping("/line/editRow")
	public String editRow(String id, Model model) {
		SpecialPriceLine spl = bizService.getSpecialPriceLine(id);
		model.addAttribute("entity", spl);
		return forward("editRow");
	}

	@ResponseBody
	@RequestMapping(value = "/line/editRow", method = RequestMethod.POST)
	public String editRow_save(SpecialPriceLine specialPriceLine) {
		SpecialPriceLine spl = bizService.getSpecialPriceLine(specialPriceLine.getId());
		spl.setApplyCount(specialPriceLine.getApplyCount());
		spl.setApplyDiscount(specialPriceLine.getApplyDiscount());
		spl.setApprovedDiscount(specialPriceLine.getApprovedDiscount());
		bizService.editSpecialPriceLine(spl);
		return sendSuccessMessage();
	}

	@ResponseBody
	@RequestMapping(value = "/line/addRow", method = RequestMethod.POST)
	public String addRow_save(SpecialPriceLine specialPriceLine) {
		bizService.saveSpecialPriceLine(specialPriceLine);
		return sendSuccessMessage();
	}
}
