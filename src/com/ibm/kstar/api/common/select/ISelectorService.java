package com.ibm.kstar.api.common.select;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.entity.Employee;
import org.xsnake.web.action.Condition;

import java.util.List;

/**
 * Created by wangchao on 2017/4/20.
 */
public interface ISelectorService {

    List<Employee> getEmployees(Condition condition,String type);

    List<LovMember> findDeptsByEmployeeId(Condition condition,String employeeId);
}
