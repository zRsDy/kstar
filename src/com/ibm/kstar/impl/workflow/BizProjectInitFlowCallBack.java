package com.ibm.kstar.impl.workflow;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.xflow.api.Participant;
import org.xsnake.xflow.api.workflow.IXflowInterface;

import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.action.common.process.ProcessStatusService;
import com.ibm.kstar.api.bizopp.IBizoppService;
import com.ibm.kstar.api.system.permission.IEmployeeService;
import com.ibm.kstar.api.system.permission.IUserService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.entity.bizopp.BusinessOpportunity;
import com.ibm.kstar.entity.team.Team;
import com.ibm.kstar.entity.team.vo.TeamVo;

/**
 * Created by wangchao on 2017/3/18.
 */
@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class BizProjectInitFlowCallBack extends IXflowInterface {

    @Autowired
    IBizoppService bizService;

    @Autowired
    ProcessStatusService processStatusService;
    
    @Autowired
    IUserService userService;
    
    @Autowired
	BaseDao baseDao;
    
    @Autowired
    IEmployeeService employeeService;

    @Override
    public void onComplete(String businessKey, String flowModule, String processInstanceId, String comment) {

        BusinessOpportunity bizOppEntity = bizService.getBizOppEntity(businessKey);
//        //报价单
//        if ("1".equals(bizOppEntity.getProjectType())) {

            bizService.generateQuot(bizOppEntity.getId(), bizOppEntity.getProjectType(), bizOppEntity.getIsBidProject(),
                    bizOppEntity.getQoutName(), bizOppEntity.getIsProReview(),
                    new UserObject(bizOppEntity.getCreatedById(), bizOppEntity.getCreatedPosId(),bizOppEntity.getCreatedOrgId()));
//        }

//        Contract contract = new Contract();
//        //框架类型
//        if ("2".equals(bizOppEntity.getProjectType())) {
//            if (StringUtil.isEmpty(bizOppEntity.getContrname())) {
//                throw new AnneException("合同名称不能为空");
//            }
//            contract.setCreateTime(new Date());
//            contract.setCustLinkId(bizOppEntity.getCreatedById());
//            contract.setCreatedAt(new Date());
//            contract.setCreatedById(bizOppEntity.getCreatedById());
//            contract.setCreatedPosId(bizOppEntity.getCreatedPosId());
//            contract.setCreatedOrgId(bizOppEntity.getCreatedOrgId());
//
//            bizService.generateFrameContract(contract, bizOppEntity.getId(), bizOppEntity.getQoutName());
//        }

        processStatusService.updateProcessStatus("BizOppChange", businessKey, "auditStatus", ProcessConstants.PROCESS_STATUS_Completed);
        processStatusService.updateProcessStatus("BusinessOpportunity", businessKey, "status", "120");
        
        //发送邮件
//        String hql = "  from Team t  where t.businessId = ? ";
//        List<Team> list= baseDao.findEntity(hql,new Object[]{businessKey});
//        
//        for (Team team : list) {
//        	String title = "您有报价单:"+bizOppEntity.getNumber()+"需制作标书，请处理";
//        	Employee employee = employeeService.get(team.getMasterEmployeeId());
//        	userService.sendMail(employee.getEmail(), title, title);
//		}
        
    }

    @Override
    public void onDestory(String businessKey, String flowModule, String processInstanceId, String comment) {
        processStatusService.updateProcessStatus("BizOppChange", businessKey, "auditStatus", ProcessConstants.PROCESS_STATUS_Destroyed);
        processStatusService.updateProcessStatus("BusinessOpportunity", businessKey, "status", "110");
    }

    @Override
    public void onReject(String businessKey, String flowModule, String processInstanceId, String comment) {
        processStatusService.updateProcessStatus("BizOppChange", businessKey, "auditStatus", ProcessConstants.PROCESS_STATUS_Rejected);
        processStatusService.updateProcessStatus("BusinessOpportunity", businessKey, "status", "110");
    }

    @Override
    public void onTaskComplete(String businessKey, String flowModule, String processInstanceId, Participant participant,String comment) {

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
}
