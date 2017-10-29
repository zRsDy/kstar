package com.ibm.kstar.action.common.select;

import com.ibm.kstar.api.common.select.ISelectorService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.EmployeeObject;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.interceptor.system.permission.NoRight;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.Condition;
import org.xsnake.web.utils.ActionUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by wangchao on 2017/4/20.
 */
@Controller
@RequestMapping("/selector")
public class SelectorAction extends BaseAction{

    @Autowired
    ISelectorService selectorService;

    /**
     * 根据人员类型获取人员列表
     * @param condition
     * @param type
     * @param request
     * @return
     */
    @NoRight
    @RequestMapping("/employeeSelector")
    @ResponseBody
    public String getEmployees(Condition condition,String type,HttpServletRequest request){
        ActionUtil.requestToCondition(condition, request);
        List<Employee> employees = selectorService.getEmployees(condition,type);

        return sendSuccessMessage(employees);
    }

    /**
     * 根据人员id获取人员所属的部门列表（因为一个人可能有多个部门）
     * @param condition
     * @param employeeId
     * @param request
     * @return
     */
    @NoRight
    @ResponseBody
    @RequestMapping("/selectDepartmentByEmployeeId")
    public String selectProductList(Condition condition,String employeeId,HttpServletRequest request){
        ActionUtil.requestToCondition(condition, request);
        List<LovMember> lovMembers = selectorService.findDeptsByEmployeeId(condition,employeeId);
        return sendSuccessMessage(lovMembers);
    }
}
