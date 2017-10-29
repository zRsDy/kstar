package com.ibm.kstar.mobile.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.log.LogOperate;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.StringUtil;
import org.xsnake.xflow.api.IHistoryService;
import org.xsnake.xflow.api.ITaskService;
import org.xsnake.xflow.api.Participant;
import org.xsnake.xflow.api.RejectPath;
import org.xsnake.xflow.api.model.HistoryActivityInstance;
import org.xsnake.xflow.api.model.HistoryProcessInstance;
import org.xsnake.xflow.api.model.Task;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ibm.kstar.action.ProcessForm;
import com.ibm.kstar.api.bizopp.IBizoppService;
import com.ibm.kstar.api.contract.IContrPayService;
import com.ibm.kstar.api.contract.IContractService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.ICorePermissionService;
import com.ibm.kstar.api.system.permission.IEmployeeService;
import com.ibm.kstar.api.system.permission.IUserService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.entity.bizopp.BusinessOpportunity;
import com.ibm.kstar.entity.contract.Contract;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.interceptor.system.permission.NoRight;
import com.ibm.kstar.mobile.convert.BeanConverter;
import com.ibm.kstar.mobile.enums.OptEnum;
import com.ibm.kstar.mobile.vo.BizoppVO;
import com.ibm.kstar.mobile.vo.ContractVO;
import com.ibm.kstar.mobile.vo.CustomerVO;
import com.ibm.kstar.mobile.vo.LoginVO;
import com.ibm.kstar.mobile.vo.OptVO;
import com.ibm.kstar.mobile.vo.ProcessVO;
import com.ibm.kstar.service.IDemoService;

/**
 * 科士达APP HTTP REST接口
 * @author ibm
 * @date 2017/09/11
 */
@Controller
@RequestMapping("/mobile")
public class MobileAction extends BaseAction {
	
	@Autowired
    private IContractService contractService;
	
	@Autowired
	IBizoppService bizService;
	
	@Autowired
	ICustomInfoService customInfoService;
	
	@Autowired
	private IContrPayService contrPayService;
	
	@Autowired
    IBizoppService bizoppService;
	
	@Autowired
	ITaskService taskService;
	
	@Autowired
	IUserService userService;
	
	@Autowired
	IDemoService workflowService;
	
	@Autowired
	IHistoryService historyService;
	
	@Autowired
	IEmployeeService employeeService;
	
	@Autowired
	ICorePermissionService corePermissionService;
	
	/*
	@NoRight
	@ResponseBody
	@RequestMapping(value="/login",method = RequestMethod.POST)
	public String login(@ModelAttribute LoginVO vo,HttpServletRequest request){
		return this.sendSuccessMessage();
	}
	*/
	
	/**
	 * 查询合同
	 * @param condition 合同查询条件,keyword:查询关键字,可以是合同名称和合同编号
	 * @param request
	 * @return 合同列表
	 */
	@NoRight
    @ResponseBody
    @RequestMapping("/getContracts")
    public String getContracts(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		String keyword = condition.getStringCondition("keyword");
		if(!StringUtils.isEmpty(keyword)){
			condition.getFilterObject().addOrCondition("contrName", "like", "%" + keyword + "%");
			condition.getFilterObject().addOrCondition("contrNo", "like", "%" + keyword + "%");
		}
        ActionUtil.doSearch(condition);
        IPage p = contractService.query(condition);

        return sendSuccessMessage(p);
    }
	
	/**
	 * 获取合同信息
	 * @param id 合同id
	 * @return json格式的合同信息
	 */
	@NoRight
	@ResponseBody
	@RequestMapping(value="/contract")
	public String getContract(@RequestParam(value = "id", required = false) String id){
		if(StringUtils.isEmpty(id)){
			return this.sendErrorMessage("参数id不存在");
		}
		/*
		 * 获取合同头信息
		 */
		Contract bo = contractService.get(id);
		ContractVO vo = BeanConverter.convertContract(bo);
		//获取合同工程清单
		vo.setPrjlsts(contractService.getPrjlstListByContrId(id));
		//获取合同回款规则
		vo.setPays(contrPayService.getContrPayByContrId(id));
		
		return this.sendSuccessMessage(vo);
	}
	
	/**
	 * 查询商机
	 * @param condition 合同查询条件,keyword:查询关键字,可以商机名称和编号
	 * @return 商机列表
	 */
	@NoRight
	@ResponseBody
	@RequestMapping("/getBizopps")
	public String getBizopps(PageCondition condition, HttpServletRequest request){
		
		ActionUtil.requestToCondition(condition, request);
		String keyword = condition.getStringCondition("keyword");
		if(!StringUtils.isEmpty(keyword)){
			condition.getFilterObject().addOrCondition("opportunityName", "like", "%" + keyword + "%");
			condition.getFilterObject().addOrCondition("number", "like", "%" + keyword + "%");
		}
        ActionUtil.doSearch(condition);
        IPage p = bizService.query(condition, getUserObject());
		return sendSuccessMessage(p);
	}
	
	/**
	 * 获取商机信息
	 * @param id 商机id
	 * @return json格式的商机信息
	 */
	@NoRight
	@ResponseBody
	@RequestMapping(value="/bizopp")
	public String getBizopp(@RequestParam(value = "id", required = false) String id){
		if(StringUtils.isEmpty(id)){
			return this.sendErrorMessage("参数id不存在");
		}
		/*
		 * 获取商机头信息
		 */
		BusinessOpportunity bo = bizService.get(id);
		BizoppVO vo = BeanConverter.convertBizopp(bo);
		//获取商机授权单位
		vo.setIntegrators(bizoppService.getBizOppIntegratorList(id));
		//获取商机项目配置信息
		vo.setPrjlsts(bizoppService.getProducrDetailByBizId(id));
		
		return this.sendSuccessMessage(vo);
	}
	
	/**
	 * 查询客户
	 * @param condition 查询条件,keyword:关键字，可以是客户名称和编号
	 * @param request
	 * @return 客户列表
	 */
	@NoRight
	@ResponseBody
	@RequestMapping("/getCustomers")
	public String getCustomers(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);

		String searchKey = condition.getStringCondition("keyword");
		if(!StringUtils.isEmpty(searchKey)){
			condition.getFilterObject().addOrCondition("customCode", "like", "%" + searchKey + "%");
			condition.getFilterObject().addOrCondition("customFullName", "like", "%" + searchKey + "%");
		}

		ActionUtil.doSearch(condition);
		IPage p = customInfoService.query(condition);

		return sendSuccessMessage(p);
	}
	
	/**
	 * 获取客户信息
	 * @param id 客户id
	 * @return json格式的客户信息
	 */
	@NoRight
	@ResponseBody
	@RequestMapping(value="/customer")
	public String getCustomer(@RequestParam(value = "id", required = false) String id){
		if(StringUtils.isEmpty(id)){
			return this.sendErrorMessage("参数id不存在");
		}
		/*
		 * 获取客户头信息
		 */
		CustomInfo bo = customInfoService.getCustomInfo(id);
		CustomerVO vo = BeanConverter.convertCustomer(bo);
		//获取客户ERP信息
		String[] status = {};
		vo.setErps(customInfoService.getErpInfoByIdStatus(id, status));
		
		return this.sendSuccessMessage(vo);
	}
	
	/**
	 * 获取当前任务的操作列表获取合同信息
	 * @param taskId 任务id
	 * @return json格式的操作列表，操作分为：同意、驳回、沟通、转交等
	 */
	@NoRight
	@ResponseBody
	@RequestMapping(value="/getOpts")
	public String getOpts(@RequestParam(value = "taskId", required = false) String taskId){
		if(StringUtils.isEmpty(taskId)){
			return this.sendErrorMessage("参数taskId不存在");
		}
		Task task = taskService.getTask(taskId);
		if(task==null)
			return this.sendErrorMessage("该任务已经不存在了");
		/*
		List<RejectPath> rejectPathList = null;
		if("Y".equals(task.getRejectFlag())){
			rejectPathList = taskService.getRejectPathList(taskId);
		}else{
			rejectPathList = new ArrayList<RejectPath>();
		}
		*/
		List<OptVO> opts = new ArrayList<OptVO>();
		
		if("Y".equals(task.getRuntimeAssigneFlag())){
			//指派
			opts.add(new OptVO(OptEnum.DISPATCH.value(),OptEnum.DISPATCH.getName(),true,"employee",false));
		}
		if(!StringUtils.isEmpty(task.getToIds())&&"Y".equals(task.getIsNormalTask())){
			for(String name:task.getToIds().split(";")){
				if(StringUtils.isEmpty(name))
					continue;
				//当前任务后面有分支，需要选择的操作
				opts.add(new OptVO(OptEnum.CHOOSE.value(),name,false,"",false));
			}
		}else{
			//同意
			opts.add(new OptVO(OptEnum.AGREEN.value(),OptEnum.AGREEN.getName(),false,"",false));
		}
		
		if("Y".equals(task.getIsNormalTask())&&"task".equals(task.getActivityType())){
			if("Y".equals(task.getRejectFlag())){
				//驳回
				opts.add(new OptVO(OptEnum.REJECT.value(),OptEnum.REJECT.getName(),true,"activity",false));
			}
			if("Y".equals(task.getCloseFlag())){
				//销毁
				opts.add(new OptVO(OptEnum.DESTROY.value(),OptEnum.DESTROY.getName(),false,"",false));
				
			}
			//委托
			opts.add(new OptVO(OptEnum.ENTRUST.value(),OptEnum.ENTRUST.getName(),true,"employee",false));
			//沟通
			opts.add(new OptVO(OptEnum.COMMUNICATE.value(),OptEnum.COMMUNICATE.getName(),true,"employee",true));
		}
		return this.sendSuccessMessage(opts);
	}
	
	/**
	 * 获取下一步任务参与人
	 * @param taskId 任务id
	 * @param keyword 查询关键字
	 * @return json格式的人员列表
	 * @deprecated 该接口作废
	 */
	@NoRight
	@ResponseBody
	@RequestMapping(value="/getParticipants")
	public String getParticipants(
			@RequestParam(value = "taskId", required = false) String taskId,
			@RequestParam(value = "keyword", required = false) String keyword){
		if(StringUtils.isEmpty(taskId)){
			return this.sendErrorMessage("参数taskId不存在");
		}
		
		return this.sendSuccessMessage(null);
	}
	
	/**
	 * 查询人员
	 * @param keyword 查询关键字
	 * @return json格式的活动列表
	 */
	@NoRight
	@ResponseBody
	@RequestMapping(value="/getEmployees")
	public String getEmployees(
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "rows", required = false) Integer rows,
			@RequestParam(value = "page", required = false) Integer page){
		PageCondition condition = new PageCondition();
		if(rows!=null){
			condition.setRows(rows);	
		}
		if(page!=null){
			condition.setPage(page);
		}
		if(!StringUtils.isEmpty(keyword)){
			condition.getFilterObject().addOrCondition("name", "like", "%"+keyword+"%");
			condition.getFilterObject().addOrCondition("code", "like", "%"+keyword+"%");
		}
		return this.sendSuccessMessage(userService.query(condition));
	}
	
	/**
	 * 获取下一步活动列表
	 * @param taskId 任务id
	 * @return json格式的活动列表
	 */
	@NoRight
	@ResponseBody
	@RequestMapping(value="/getActivitys")
	public String getActivitys(@RequestParam(value = "taskId", required = false) String taskId){
		if(StringUtils.isEmpty(taskId)){
			return this.sendErrorMessage("参数taskId不存在");
		}
		Task task = taskService.getTask(taskId);
		List<RejectPath> rejectPathList = null;
		if("Y".equals(task.getRejectFlag())){
			rejectPathList = taskService.getRejectPathList(taskId);
		}else{
			rejectPathList = new ArrayList<RejectPath>();
		}
		return this.sendSuccessMessage(rejectPathList);
	}
	
	/**
	 * 提交任务
	 * @param processVO 任务数据
	 * @return
	 */
	@NoRight
	@ResponseBody
	@RequestMapping(value="/process")
	public String process(ProcessVO vo){
		OptEnum opt = OptEnum.getOptEnum(vo.getOptType());
		if(opt==null)
			return this.sendErrorMessage("当前的操作["+vo.getOptType()+"]是不被允许的");
		if (opt == OptEnum.DISPATCH || 
			opt == OptEnum.AGREEN || 
			opt == OptEnum.CHOOSE || 
			opt == OptEnum.REJECT) {
			return processNormalTask(vo);
		} else if (opt == OptEnum.COMMUNICATE) {
			//沟通
			UserObject user = getUserObject();
			String taskId = vo.getTaskId();
			String userIds = vo.getSelectIds();
			String comment = vo.getComment();
			String[] ids = userIds.split(",");
			List<Participant> participantList = new ArrayList<Participant>();
			for(String userId : ids){
				Employee employee = employeeService.get(userId);
				Participant assignee = new Participant(employee.getId(),employee.getName(),"EMPLOYEE");
				participantList.add(assignee);
			}
			taskService.support(taskId,user.getParticipant(),participantList,comment);
			return sendSuccessMessage("操作成功");
		}else if(opt==OptEnum.ENTRUST){
			//委托
			UserObject user = getUserObject();
			String taskId = vo.getTaskId();
			String userId = vo.getSelectIds();
			String comment = vo.getComment();
			
			Employee employee = employeeService.get(userId);
			Participant assignee = new Participant(employee.getId(),employee.getName(),"EMPLOYEE");
			
			taskService.transfer(taskId,user.getParticipant(), assignee,comment);
			return sendSuccessMessage("操作成功");
		}
		return this.sendSuccessMessage("没做任何操作");
	}
	
	/**
	 * 根据流程实例查询流程审批记录
	 * @param processInstanceId 实例id
	 * @return 审批记录
	 * @throws Exception
	 */
	@NoRight
	@RequestMapping("/getHistorys")
	@ResponseBody
	public String getHistorys(String processInstanceId) throws Exception{
		HistoryProcessInstance pi = historyService.get(processInstanceId);
		List<HistoryActivityInstance> historyList = historyService.findHistoryActivityInstance(pi.getId());
		return this.sendSuccessMessage(historyList);
	}
	
	private String processNormalTask(ProcessVO vo){
		OptEnum opt = OptEnum.getOptEnum(vo.getOptType());
		String submitType = null;
		String activityId = null;
		String comment = vo.getComment();
		String taskId = vo.getTaskId();
		String toActivityId = null;
		//String appointUserId = null;
		if(opt==OptEnum.DISPATCH){
			//指派
			//TODO 当前没有这种情况
			submitType = "1";
		}else if(opt==OptEnum.AGREEN){
			//同意
			submitType = "1";
		}else if(opt==OptEnum.CHOOSE){
			//选择分支
			submitType = "1";
			toActivityId = vo.getOptName();
		}else if(opt==OptEnum.REJECT){
			//驳回
			submitType = "0";
			activityId = vo.getSelectIds();
		}else if(opt==OptEnum.DESTROY){
			//销毁
			//TODO 可能会有问题
			submitType = "2";
		}
		
		ProcessForm form = new ProcessForm();
		form.setSubmitType(submitType);
		form.setActivityId(activityId);
		form.setComment(comment);
		form.setTaskId(taskId);
		//form.setAppointUserId(appointUserId);
		form.setToActivityId(toActivityId);
		form.setEmployeeId(getUserObject().getEmployee().getId());
		
		try{
			workflowService.todoProcess(form);
		}catch(Exception e){
			return this.sendErrorMessage(e.getMessage());
		}
		return this.sendSuccessMessage("操作成功");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/changePosition")
	public String changePosition(String positionId,HttpServletRequest request){
		//List<LovMember> positions = (List<LovMember>)getSession().getAttribute("positions");
		UserObject user = getUserObject();
		try{
			if("admin".equals(user.getEmployee().getNo())){
				LovMember position = corePermissionService.getPositionById(positionId);
				user.setPosition(position);
				user.setOrg(corePermissionService.getOrg(user.getPosition().getId()));
			}else{
				boolean ok = false;
				LovMember position = corePermissionService.getPositionById(positionId);
				if(position!=null){
					user.setPosition(position);
					user.setOrg(corePermissionService.getOrg(user.getPosition().getId()));
					userService.changePosition(user.getEmployee().getId(),positionId);
					user.initPermission(corePermissionService.getPermissionListByPosition("CRM", user.getPosition().getPositionInOrgId()));
					ok = true;
				}
				if(!ok){
					throw new AnneException("未找到切换的岗位");
				}
			}
		}catch(Exception e){
			return this.sendErrorMessage(e.getMessage());
		}
		/*
		try{
			if("admin".equals(user.getEmployee().getNo())){
				LovMember position = corePermissionService.getPositionById(positionId);
				user.setPosition(position);
				user.setOrg(corePermissionService.getOrg(user.getPosition().getId()));
			}else{
				boolean ok = false;
				for(LovMember lov : positions){
					if(lov.getId().equals(positionId)){
						user.setPosition(lov);
						user.setOrg(corePermissionService.getOrg(user.getPosition().getId()));
						userService.changePosition(user.getEmployee().getId(),positionId);
						user.initPermission(corePermissionService.getPermissionListByPosition("CRM", user.getPosition().getPositionInOrgId()));
						ok = true;
						break;
					}
				}
				if(!ok){
					throw new AnneException("未找到切换的岗位");
				}
			}
		}catch(Exception e){
			return this.sendErrorMessage(e.getMessage());
		}*/
		return sendSuccessMessage();
	}
}
