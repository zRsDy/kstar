package com.ibm.kstar.action.bizopp.protoApplyInfo;

import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.api.bizopp.IBizBaseService;
import com.ibm.kstar.api.bizopp.IBizoppService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.bizopp.BusinessOpportunity;
import com.ibm.kstar.entity.bizopp.PrototypeApplyReturn;
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

/**
 * ClassName:BizProApplyAction <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: Jan 6, 2017 10:07:56 AM <br/>
 * 
 * @author Wutw
 * @version
 * @since JDK 1.7
 * @see
 */

@Controller
@RequestMapping("/proApply")
public class BizProApplyAction extends BaseAction {

	@Autowired
	IBizBaseService bizService;
	
	@Autowired
	IBizoppService bizoppService;

	@RequestMapping("/index")
	public String index(String id, Model model) {
		return forward("proApplyIndex");
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) throws Exception {
		ActionUtil.requestToCondition(condition, request);
		String bizOppId = request.getParameter("bizOppId");
		condition.getFilterObject().addCondition("bizOppId", "eq", bizOppId);
		IPage p = bizoppService.queryPrototypeApplyReturn(condition);

		return sendSuccessMessage(p);
	}


	@RequestMapping("/add")
	public String add(String bid, Model model){
		PrototypeApplyReturn entity = new PrototypeApplyReturn();
		
		UserObject user = getUserObject();

		String userId = user.getEmployee().getId();
		String userName = user.getEmployee().getName();
		String departmentName = user.getOrg().getName();
		String department = user.getOrg().getId();
		
		BusinessOpportunity businessOpportunity = null;
		
		//如果在商机页面新增售前支持，则自动带入客户名称，商机名称
		if (bid != null) {
			businessOpportunity = bizoppService.getBizOppEntity(bid);
			entity.setBizOppId(bid);
			entity.setBizoppName(businessOpportunity.getOpportunityName());
			entity.setClientId(businessOpportunity.getClientId());
			entity.setClientName(businessOpportunity.getClientName());
		}
		
		//设置申请人
		entity.setApplicantId(userId);
		entity.setApplicantName(userName);
		//生成商机支持编号
		String bizOppProtoNumber = bizoppService.getBizOppProtoNumber();
//		entity.setNumber(bizOppSupportNumber);
		//申请单位
		entity.setApplyUnit(departmentName);
		entity.setDepartmentName(departmentName);
//		//申请部门
		entity.setDepartment(department);
		entity.setNumber(bizOppProtoNumber);
		//初始化创建日期
		entity.setCreatedAt(new Date());
		//初始化商机状态
		entity.setStatus(ProcessConstants.PROCESS_STATUS_Pending);
		entity.setPrototypeStatus("10");//样机状态
		model.addAttribute("entity", entity);

		entity.fillInit(getUserObject());

		TabMain tabMainInfo = new TabMain();
		model.addAttribute("tabMainInfo",tabMainInfo);

		return forward("proApplyAdd");
	}
	
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(PrototypeApplyReturn entity) {
		if(StringUtil.isEmpty(entity.getStatus())){
			entity.setStatus(ProcessConstants.PROCESS_STATUS_Pending);
			entity.setPrototypeStatus("10");
		}
		entity.fillInit(getUserObject());
		bizService.savePrototypeApplyReturn(entity,getUserObject());
		return sendSuccessMessage(entity.getId());
	}
	
	@NoRight
	@RequestMapping("/edit")
	public String edit(String id,Model model){
		if(id==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		PrototypeApplyReturn entity = bizService.getEntity(id, new PrototypeApplyReturn());
		if(entity==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		TabMain tabMainInfo = new TabMain();
		tabMainInfo.setInitAll(false);
		if (hasPermission("P03SampleT1AppealItemPage")) {
			tabMainInfo.addTab("样机列表", "/proApply/applyInfo.html?id=" + entity.getId());
		}
//		tabMainInfo.addTab("实物退还", "/proApply/backInfo?id="+entity.getId());
		if (hasPermission("P03SampleT3TeamPosPage")) {
			tabMainInfo.addTab("团队成员", "/team/list.html?businessType=PrototypeApplyReturn&businessId=" + id);
		}
		if (hasPermission("P03SampleT4ProReviewHistoryPage")) {
			tabMainInfo.addTab("审批历史", "/standard/history.html?contrId=" + id);
		}
		if (hasPermission("P03SampleT2FilePage")) {
			tabMainInfo.addTab("附件", "/common/attachment/attachment.html?businessType=PrototypeApplyReturn&docGroupCode=PrototypeApplyReturnDOCTYPE&businessId=" + id);
		}


		model.addAttribute("tabMainInfo",tabMainInfo);
		model.addAttribute("entity",entity);
		return forward("proApplyAdd");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(PrototypeApplyReturn bizopp){
		bizService.update(bizopp);
		return sendSuccessMessage(bizopp.getId());
	}
	
	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		bizService.delete(id, PrototypeApplyReturn.class);
		return sendSuccessMessage();
	}
	
	@RequestMapping("/applyInfo")
	public String applyInfo(String id, Model model) {
		model.addAttribute("systemPid",id);
		return forward("applyInfo");
	}
	
	@RequestMapping("/backInfo")
	public String backInfo(String id, Model model) {
		return forward("backInfo");
	}

	@ResponseBody
	@RequestMapping(value="/startProcess",method = RequestMethod.POST)
	public String startModelProcess(String id,String orderNumber){
		bizService.startModelProcess(getUserObject(),id,orderNumber);
		return sendSuccessMessage();
	}

	@ResponseBody
	@RequestMapping(value = "/changeStatus",method = RequestMethod.POST)
	public String changeStatus(String id,String status){

		bizService.changeStatus(id,status);

		return sendSuccessMessage();
	}
}
