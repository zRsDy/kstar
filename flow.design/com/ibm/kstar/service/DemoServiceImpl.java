package com.ibm.kstar.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.context.MessageContext;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.page.PageImpl;
import org.xsnake.web.utils.StringUtil;
import org.xsnake.xflow.api.IDefinitionService;
import org.xsnake.xflow.api.IHistoryService;
import org.xsnake.xflow.api.IProcessService;
import org.xsnake.xflow.api.ITaskService;
import org.xsnake.xflow.api.Participant;
import org.xsnake.xflow.api.model.HistoryActivityInstance;
import org.xsnake.xflow.api.model.HistoryProcessInstance;
import org.xsnake.xflow.api.model.ProcessInstance;
import org.xsnake.xflow.api.model.Task;
import org.xsnake.xflow.api.workflow.IXflowInterface;

import com.ibm.kstar.action.ProcessForm;
import com.ibm.kstar.action.flow.design.FlowUtils;
import com.ibm.kstar.api.order.IInvoiceService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.ICorePermissionService;
import com.ibm.kstar.api.system.permission.IEmployeeService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.conf.Configuration;
import com.ibm.kstar.entity.pdm.PdmFlowHistory;


@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class DemoServiceImpl implements IDemoService{

	@Autowired
	IProcessService processService;
	
	@Autowired
	ITaskService taskService;
	
	@Autowired
	IDefinitionService definitionService;

	@Autowired
	Configuration configuration;
	
	@Autowired
	IHistoryService historyService;
	
	@Autowired
	IEmployeeService employeeService;
	
	@Autowired
	BaseDao baseDao;
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	IInvoiceService invoiceService;
		
	private void chooseNext(String taskId,String transitionId,Participant participant,String comment,Map<String,String> varmap){
		Task task = taskService.getTask(taskId);
		ProcessInstance pi = processService.get(task.getProcessInstanceId());
		taskService.complete(taskId,transitionId,participant,comment,new HashMap<String,String>());
		String app = pi.getApplication();
		String beanName = configuration.getAppMap().get(app);
		IXflowInterface xflowService = (IXflowInterface)MessageContext.getBean(beanName);
		xflowService.onEvent(transitionId,pi.getBusinessKey(), pi.getModule(), pi.getId(),comment);
		
		HistoryProcessInstance hpi = historyService.get(pi.getId());
		if("end".equals(hpi.getStatus())){
			xflowService.onCompletex(pi.getBusinessKey(), pi.getModule(), pi.getId(), comment);
		}
		
	}
	
	private Map<String,String> resetVaraiable(HistoryProcessInstance processInstance){
		/*
		String module = processInstance.getModule();
		Map<String,String> varaiables = null;
		//TODO 流程代码需要修改为动态值
		if("P06.15.20.OrderIssue".equalsIgnoreCase(module)){
			varaiables = new HashMap<String,String>();
			String sql = "select ol.c_erp_plan_status from CRM_T_DELIVERY_LINES dl,CRM_V_ORDER_LINES ol "
					+ "where dl.c_order_id=ol.c_pid and dl.c_delivery_id='"+processInstance.getBusinessKey()+"' and dl.C_delete_flag='0' and ol.c_erp_plan_status!='已确认交期' and rownum=1";
			String status = "N";
			if(baseDao.findUniqueBySql(sql)==null){
				//该出货申请的订单行全部是"已确认交期"状态;
				status = "Y";
			}
			//varaiables.put("DTComfirmed", status);
			if(status.equals("N"))
				throw new AnneException("该出货申请存在未确认交期的订单行,不能提交");
		}else if("P06.25.30.Invoice".equalsIgnoreCase(module)){
			varaiables = new HashMap<String,String>();
			String sql = "select ol.c_is_erp_delivery from crm_t_invoice_detail dl,CRM_V_ORDER_LINES ol "
					+ "where dl.c_order_line_id=ol.c_pid and dl.c_invoice_id='"+processInstance.getBusinessKey()+"' and ol.c_is_erp_delivery!='Yes' and dl.c_invoice_type='02' and rownum=1";
			String status = "N";
			if(baseDao.findUniqueBySql(sql)==null){
				//该开票申请的订单行全部是"已发货"状态;
				status = "Y";
			}
			//varaiables.put("DVComfirmed", status);
			if(status.equals("N"))
				throw new AnneException("该开票申请存在未发货的订单行,不能提交");
			
			invoiceService.checkInvoice(processInstance.getBusinessKey());
		}
		return varaiables;
		*/
		return null;
	}
	
	public void doCallback(String processInstanceId) throws AnneException{
		HistoryProcessInstance processInstance = historyService.get(processInstanceId);
		if(!"end".equals(processInstance.getStatus())){
			throw new AnneException("流程未结束,不能触发回调");
		}
		String app = processInstance.getApplication();
		String beanName = configuration.getAppMap().get(app);
		IXflowInterface xflowService = (IXflowInterface)MessageContext.getBean(beanName);
		
		String comment = "";
		List<HistoryActivityInstance> hts = historyService.findHistoryActivityInstance(processInstanceId);
		if(hts.size()>1){
			comment = hts.get(hts.size()-2).getComment();
		}
		xflowService.onComplete(processInstance.getBusinessKey(), processInstance.getModule(), processInstance.getId(),comment);
	}
	
	private void complete(String taskId, String comment,String submitType,String activityId,Participant p) {
//		Participant p = new Participant(user.getEmployee().getId(),user.getEmployee().getName(),"EMPLOYEE");
		Task task = taskService.getTask(taskId);
		if(task == null){
			throw new AnneException("任务已经被处理");
		}
		HistoryProcessInstance processInstance = historyService.get(task.getProcessInstanceId());
		
		String app = processInstance.getApplication();
		String beanName = configuration.getAppMap().get(app);
		IXflowInterface xflowService = (IXflowInterface)MessageContext.getBean(beanName);
		if("1".equals(submitType)){
			if(task.getToIds()!=null && "Y".equals(task.getIsNormalTask())){
				//异常情况
			}else{
				Map<String,String> varaiables = resetVaraiable(processInstance);
				boolean isComplete = taskService.complete(taskId, p, comment,varaiables);
				if(!isComplete){
					HistoryProcessInstance hpi = historyService.get(processInstance.getId());
					if("end".equals(hpi.getStatus())){
						isComplete = true;
					}
				}
				if(isComplete){
					xflowService.onCompletex(processInstance.getBusinessKey(), processInstance.getModule(), processInstance.getId(),comment);
				}else{
					xflowService.onTaskComplete(processInstance.getBusinessKey(), processInstance.getModule(), processInstance.getId(),p,comment);
				}
			}
		}else if("0".equals(submitType)){
			if(StringUtil.isEmpty(comment)){
				throw new AnneException("驳回操作必须说明原因");
			}
			taskService.reject(taskId, activityId, p, comment);
			
			xflowService.onReject(processInstance.getBusinessKey(), processInstance.getModule(), processInstance.getId(), comment);
		}
		else{
			processService.close(task.getProcessInstanceId(), p, comment);
			xflowService.onDestory(processInstance.getBusinessKey(), processInstance.getModule(), processInstance.getId(), comment);
		}
//		taskService.complete(taskId, new Participant(user.getEmployee().getId(),user.getEmployee().getName(),"EMPLOYEE"), comment);
	}

	@Autowired
	ICorePermissionService corePermissionService;
	
	@Override
	public IPage taskList(UserObject user,List<LovMember> positions,int size,int pageno) {
		List<Participant> list = new ArrayList<Participant>();
		
		list.add(new Participant(user.getEmployee().getId(),user.getEmployee().getName(),"EMPLOYEE"));
		for(LovMember position : positions){
			LovMember positionInOrg = corePermissionService.getOrgByPositionId(position.getId());
			LovMember positionInApprove = corePermissionService.getApprovePositionByPositionId(position.getId());
			list.add(new Participant(position.getId(),position.getName(),"POSITION"));
			list.add(new Participant(positionInOrg.getId(),positionInOrg.getName(),"POSITION"));
			if(positionInApprove != null){
				list.add(new Participant(positionInApprove.getId(),positionInApprove.getName(),"POSITION"));
			}
		}
		IPage page = taskService.myTask(list, size, pageno);
		return page;
	}

	@Override
	public String getProcessJSON(String code) throws Exception {
		String xml = definitionService.getLastVersionXML(code);
		String json = null;
		if(xml!=null){
			json = FlowUtils.xmlToJson(xml);
		}
		return json;
	}
	
	@Override
	public String getProcessJSONByProcessDefinitionId(String processDefinitionId) throws Exception {
		String xml = definitionService.getXML(processDefinitionId);
		String json = null;
		if(xml!=null){
			json = FlowUtils.xmlToJson(xml);
		}
		return json;
	}
	
	@Override
	public void todoProcess(ProcessForm form) {
		if(form == null){
			return ;
		}
		
		if(StringUtil.isEmpty(form.getTaskId())){
			return ;
		}
		
		Task t = taskService.getTask(form.getTaskId()); 
		if(t == null){
			throw new AnneException("任务已经不存在");
		}
		
		if(StringUtil.isEmpty(form.getComment())){
			throw new AnneException("意见建议不能为空");
		}
		
		Employee employee = employeeService.get(form.getEmployeeId());
		Participant participant = new Participant(employee.getId(),employee.getName(),"EMPLOYEE");
		
		if("1".equals(form.getSubmitType()) && "Y".equals(t.getRuntimeAssigneFlag()) ){
			Employee assignee = employeeService.get(form.getAppointUserId());
			if(assignee == null){
				throw new AnneException("请指派下一步办理人");
			}
			if(assignee != null){
				Participant assigneeParticipant = new Participant(assignee.getId(),assignee.getName(),"EMPLOYEE");
				//指派下一岗办理人
				taskService.complete(form.getTaskId(),participant,form.getComment(),assigneeParticipant);
			}
		}else if("1".equals(form.getSubmitType()) && StringUtil.isNotEmpty(form.getToActivityId())){
			chooseNext(form.getTaskId(),form.getToActivityId(),participant,form.getComment(),new HashMap<String,String>());
		}else{
			complete(form.getTaskId(), form.getComment(), form.getSubmitType(), form.getActivityId(), participant);
		}
	}

	/**
	 * 非标PDM待办流程
	 */
	@Override
	public IPage pdmTaskList(UserObject user,int size,int pageno) {
//		if(1 == 1){			
//			List<PdmFlowHistory> pdms = new ArrayList<PdmFlowHistory>();
//			return new PageImpl(pdms, pageno, size, 0);
//		}
//		try{
			StringBuffer sb = new StringBuffer();
				sb.append(" select b.* from V_WF_FOR_CRM b ");
				sb.append(" where 1=1 ");
				sb.append(" and b.BUSINESSID in ( ");
				sb.append("  	select a.c_demand_code from CRM_T_PRODUCT_DEMAND a ");
				sb.append("  	where 1=1 ");
				sb.append("  	and a.c_created_pos_id  = '"+user.getPosition().getId()+"'");
//				sb.append("  	and a.c_created_pos_id in ( ");
//				sb.append("  		select p.ROW_ID from SYS_T_LOV_MEMBER p  ");
//				sb.append("  		where p.GROUP_ID = 'POSITION' ");
//				sb.append("  		and p.LOV_PATH like '%").append(user.getPosition().getId()).append("%' ");
//				sb.append("  	) ");
				sb.append(" ) ");
				sb.append(" and b.needconfirm = 2 ");
				sb.append(" and b.STAT = 4 ");
			
//			session = sessionFactory.openSession();
//			tx = session.beginTransaction();
//			query = session.createSQLQuery(sb.toString());
			List<Object[]> list = baseDao.findBySql(sb.toString());
//			List<Object[]> list = query.list();
			List<PdmFlowHistory> pdms = new ArrayList<PdmFlowHistory>();
			for(Object[] objects : list){
				PdmFlowHistory pdmFlowHistory = new PdmFlowHistory();
				BigDecimal id = (BigDecimal)objects[0];
				BigDecimal tempId = (BigDecimal)objects[2];
				BigDecimal procId = (BigDecimal)objects[3];
				BigDecimal status = (BigDecimal)objects[5];
				BigDecimal confim = (BigDecimal)objects[10];
				
				pdmFlowHistory.setId(id.toString()+"_"+procId.toString());
				pdmFlowHistory.setName((String)objects[1]);
				pdmFlowHistory.setTempId(tempId.longValue());
				pdmFlowHistory.setProcId(procId.longValue());
				pdmFlowHistory.setProcName((String)objects[4]);
				pdmFlowHistory.setStatus(status.longValue());
				pdmFlowHistory.setStartTime((String)objects[6]);
				pdmFlowHistory.setEndTime((String)objects[7]);
				pdmFlowHistory.setProUsers((String)objects[8]);
				pdmFlowHistory.setOpinions((String)objects[9]);
				pdmFlowHistory.setNeedconfirm(confim.longValue());
				pdmFlowHistory.setFormNo((String)objects[11]);
				pdmFlowHistory.setRowid(id.longValue());
				pdms.add(pdmFlowHistory);
			}
			IPage page = new PageImpl(pdms, pageno, size, list.size());
//			tx.commit();
//			
//			try{
//				String closeDBlink = " alter session close database link TO_PDM_VIEW ";
//				query = session.createSQLQuery(closeDBlink);
//				query.executeUpdate();	
//			}catch(Exception ex){
//				ex.printStackTrace();
//				throw new AnneException(ex.getMessage());
//			}
			
			return page;
//		}catch(Exception e){
////			tx.rollback();
//			e.printStackTrace();
//			throw new AnneException(e.getMessage());
//		}finally {
////			if(session!=null&&session.isOpen())
////				session.close();
//		}
	}
}
