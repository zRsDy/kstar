package com.ibm.kstar.action.common.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.dao.BaseDao;

import java.util.ArrayList;

/**
 * Created by wangchao on 2017/3/15.
 */
@Service
@Transactional
public class ProcessStatusService {

    @Autowired
    BaseDao baseDao;

    public void updateProcessStatus(String entityName,String businessId,
                                    String processStatusColumn,String processStatusValue,
                                    String processIdColumn,String processIdValue){
        String hql = "update "+entityName+" set "+processStatusColumn+" = ? , "+processIdColumn+" = ? where id = ? ";
        ArrayList<Object> args = new ArrayList<>();
        args.add(processStatusValue);
        args.add(processIdValue);
        args.add(businessId);

        baseDao.executeHQL(hql,args.toArray());

    }

    public void updateProcessStatus(String entityName,String businessId,
                                    String processStatusColumn,Object processStatusValue){
        String hql = "update "+entityName+" set "+processStatusColumn+" = ?  where id = ? ";
        ArrayList<Object> args = new ArrayList<>();
        args.add(processStatusValue);
        args.add(businessId);

        baseDao.executeHQL(hql,args.toArray());

    }
}
