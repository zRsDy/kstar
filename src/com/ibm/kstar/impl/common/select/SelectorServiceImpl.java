package com.ibm.kstar.impl.common.select;

import com.ibm.kstar.api.common.select.ISelectorService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.xsnake.web.action.Condition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangchao on 2017/4/20.
 */
@Controller
@RequestMapping("/selector")
public class SelectorServiceImpl implements ISelectorService {

    @Autowired
    BaseDao baseDao;

    @Override
    public List<Employee> getEmployees(Condition condition,String type) {

        String search = condition.getStringCondition("search");

        List<Object> args = new ArrayList<>();
        String hql = "from Employee where 1=1";
        if (StringUtil.isNotEmpty(type)) {
            if ("E".equals(type)) {
                hql = hql + " and flag = ? ";
                args.add(type);
            } else {
                hql = hql + " and flag <> 'E' ";
            }
        }
        if(StringUtil.isNotEmpty(search)){
            hql = hql + " and ( no like ? or name like ? or id = ? ) ";
            args.add("%"+search+"%");
            args.add("%"+search+"%");
            args.add(search);
        }

        return baseDao.findEntity(hql, args.toArray());

    }

    @Override
    public List<LovMember> findDeptsByEmployeeId(Condition condition,String employeeId) {

        String search = condition.getStringCondition("search");
        List<Object> args = new ArrayList<>();
        String hql = " select org from LovMember o , LovMember org , LovMember p , Employee e , " +
                "UserPermission up where o.groupId = 'ORG' " +
                "and p.groupId = 'POSITION' " +
                "and p.optTxt1 = o.id " +
                "and up.userId = e.id " +
                "and up.memberId = p.id " +
                "and o.parentId = org.id " +
                "and e.id = ? " ;

        args.add(employeeId);
        if(StringUtil.isNotEmpty(search)){
            hql = hql + " and ( no like ? or name like ? ) ";
            args.add("%"+search+"%");
            args.add("%"+search+"%");
        }

        return baseDao.findEntity(hql,args.toArray());
    }
}
