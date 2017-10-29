package com.ibm.kstar.api.appBizType;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.AppFlowParam;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

import java.util.List;

/**
 * Created by wangchao on 2017/3/21.
 */

public interface IAppBizTypeService {
    IPage query(PageCondition condition) throws AnneException;

    AppFlowParam get(String id) throws AnneException;

    void addOrUpdate(AppFlowParam param) throws AnneException;

    void delete(String id) throws AnneException;

    List<LovMember> getList(String optTxt1) throws AnneException;
}
