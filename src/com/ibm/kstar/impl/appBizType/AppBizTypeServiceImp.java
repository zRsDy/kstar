package com.ibm.kstar.impl.appBizType;

import com.ibm.kstar.api.appBizType.IAppBizTypeService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.AppFlowParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

import java.util.List;

/**
 * Created by wangchao on 2017/3/21.
 */
@Service
@Transactional
public class AppBizTypeServiceImp implements IAppBizTypeService{

    @Autowired
    BaseDao baseDao;

    @Override
    public IPage query(PageCondition condition) throws AnneException {
        return null;
    }

    @Override
    public AppFlowParam get(String id) throws AnneException {
        return null;
    }

    @Override
    public void addOrUpdate(AppFlowParam param) throws AnneException {

    }

    @Override
    public void delete(String id) throws AnneException {

    }

    @Override
    public List<LovMember> getList(String optTxt1) throws AnneException {
        String hql = "from LovMember m where m.groupCode='BUSINESS_AUTH_TYPE' and m.deleteFlag='N' ";
        return baseDao.findEntity(hql);
    }
}
