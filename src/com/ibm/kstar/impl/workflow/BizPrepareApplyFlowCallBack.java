package com.ibm.kstar.impl.workflow;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.utils.DateUtil;
import org.xsnake.xflow.api.Participant;
import org.xsnake.xflow.api.workflow.IXflowInterface;

import com.ibm.kstar.action.common.process.ProcessStatusService;
import com.ibm.kstar.api.bizopp.IBizoppService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.ICorePermissionService;
import com.ibm.kstar.api.system.permission.IEmployeeService;
import com.ibm.kstar.api.system.permission.IUserService;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.entity.bizopp.BusinessOpportunity;
import com.ibm.kstar.entity.common.territory.TerritoryConfig;

/**
 * Created by wangchao on 2017/3/16.
 */
@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class BizPrepareApplyFlowCallBack extends IXflowInterface {

	@Autowired
	BaseDao baseDao;
	
    @Autowired
    ProcessStatusService processStatusService;
    
    @Autowired
    ICorePermissionService corePermissionServicec;
    
    @Autowired
    IUserService userService;
    
    @Autowired
    IEmployeeService employeeService;
    
    @Autowired
   	IBizoppService bizService;
    
    @Override
    public void onComplete(String businessKey, String flowModule, String processInstanceId, String comment) {
        processStatusService.updateProcessStatus("BusinessOpportunity", businessKey, "status", "20");
        processStatusService.updateProcessStatus("BusinessOpportunity", businessKey, "conflictStatus", "40");
        processStatusService.updateProcessStatus("BusinessOpportunity", businessKey, "successDate", new Date());
        processStatusService.updateProcessStatus("BusinessOpportunity", businessKey, "endDate", DateUtil.getDateAfter(new Date(),90));
        
//        try{
//	        //发送邮件通知
//	        BusinessOpportunity bo = baseDao.get(BusinessOpportunity.class, businessKey);
//	        String title = bo == null? "" : "您好！"+bo.getOpportunityName() + "已有"+bo.getEnterpriseName()+"于"+bo.getCreatedAt()+"提交报备，"+bo.getStatusName() ;
//	     	String positionId = baseDao.findUniqueBySql("select v.string_value from xflow_history_variable v where v.variable_key = '__positionId' and v.process_instance_id = ? ",processInstanceId);
//	        if(positionId != null){
//	        	//查找发起岗位的上级岗位
//	        	LovMember approvePostion = corePermissionServicec.getApprovePositionByPositionId(positionId);
//	        	LovMember parentPosition = corePermissionServicec.getPositionByApprovePosition(approvePostion.getParentId());
//	        	List<Employee> employeeList = corePermissionServicec.getPositionEmployeeList(parentPosition.getId());
//	        	for(Employee employee : employeeList){
//	        		userService.sendMail(employee.getEmail(), title, title);
//	        	}
//	        	//给业务创建人发送邮件
//	        	if(bo!=null){
//	        		Employee employee = employeeService.get(bo.getCreatedById());
//	        		userService.sendMail(employee.getEmail(), title, title);
//	        	}
//	        }
//        }catch(Exception e){
//        	e.printStackTrace();
//        }
        
	     load(businessKey);            
    }
    
    @Override
    public void onDestory(String businessKey, String flowModule, String processInstanceId, String comment) {
    	processStatusService.updateProcessStatus("BusinessOpportunity", businessKey, "status", "30");
 	    processStatusService.updateProcessStatus("BusinessOpportunity", businessKey, "conflictStatus", "20");
    }

    @Override
    public void onReject(String businessKey, String flowModule, String processInstanceId, String comment) {
	    processStatusService.updateProcessStatus("BusinessOpportunity", businessKey, "status", "30");
	    processStatusService.updateProcessStatus("BusinessOpportunity", businessKey, "conflictStatus", "20");
	    load(businessKey);
    }

    @Override
    public void onTaskComplete(String businessKey, String flowModule, String processInstanceId,Participant participant, String comment) {
    	BusinessOpportunity entity = bizService.getBizOppEntity(businessKey);
    	if(entity.getStatus().equals("30")){
    		processStatusService.updateProcessStatus("BusinessOpportunity", businessKey, "status", "10");
            processStatusService.updateProcessStatus("BusinessOpportunity", businessKey, "conflictStatus", "05");
    	}
    }

    @Override
    public String processPath() {
        return "/bizopp/edit.html";
    }

    @Override
    public String viewPath() {
        return null;
    }

	@Override
	public String mobileView(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onEvent(String event, String businessKey, String flowModule, String processInstanceId, String comment) {
		// TODO Auto-generated method stub
		
	}
	
	public void load(String businessKey){
		BusinessOpportunity bo = baseDao.get(BusinessOpportunity.class, businessKey);
		String title = bo == null? "" : "您好！"+bo.getOpportunityName() + "已由"+bo.getEnterpriseName()+"于"+bo.getCreatedAt()+"提交报备，"+bo.getStatusName() ;
		//1.发送邮件给所属行业部领导
	    String industry = bo.getIndustry();
	    String hql = "select e from Employee e,"
	    		+ " UserPermission up,LovMember p , "
	    		+ " LovMember po "
	    		+ " where po.id = p.optTxt1 "
	    		+ " and p.id = up.memberId "
	    		+ " and e.id = up.userId and up.type = 'P' "
	    		+ " and po.name like '%部门领导%' "
	    		+ " and po.namePath like ? ";
	    List<Employee> employees = baseDao.findEntity(hql,new Object[]{"%"+industry+"%"});
	    if(employees.size() > 0){
	    	for (Employee employee : employees) {
				userService.sendMail(employee.getEmail(), title, title);
			}
	    }
	    //2.发送邮件给跨区办事处主任
	    StringBuilder sb = new StringBuilder();
		sb.append(" select c from TerritoryConfig c, ");
		sb.append(" TerritoryInfo i ");
	    sb.append(" where c.territory = i.code ");
	    sb.append(" and i.lev3 = ? ");
		List<TerritoryConfig> configs = baseDao.findEntity(sb.toString(),new Object[]{bo.getLayer3()});
		for (TerritoryConfig ter : configs) {
			String sql = "select e from Employee e,"
	    			+ " UserPermission up,LovMember p ,"
	    			+ " LovMember po "
	    			+ " where po.id = p.optTxt1  "
	    			+ " and p.id = up.memberId "
	    			+ " and e.id = up.userId and up.type = 'P' "
	    			+ " and po.name like '%办事处主任%'"
	    			+ " and po.namePath like ? ";
			List<Employee> employee = baseDao.findEntity(sql,new Object[]{"%"+ter.getOrgIdName()+"%"});
			if(employee.size() > 0){
				for (Employee e : employee) {
					userService.sendMail(e.getEmail(), title, title);
				}
			}
		}
		//3.发送邮件给该商机单创建人
		String createHql = "select e from Employee e "
	    		+ " where e.id =  ? ";
	    List<Employee> createdEmployees = baseDao.findEntity(createHql,new Object[]{bo.getCreatedById()});
	    if(createdEmployees.size() > 0){
			for (Employee e : createdEmployees) {
				userService.sendMail(e.getEmail(), title, title);
			}
		}
	}
}
