package com.ibm.kstar.impl.workflow;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.xflow.api.Participant;
import org.xsnake.xflow.api.workflow.IXflowInterface;


/**
 * Created by wangchao on 2017/5/26.
 */
@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class BizOppBtnxFlowCallBack extends IXflowInterface {
    @Override
    public void onComplete(String businessKey, String flowModule, String processInstanceId, String comment) {

    }

    @Override
    public void onDestory(String businessKey, String flowModule, String processInstanceId, String comment) {

    }

    @Override
    public void onReject(String businessKey, String flowModule, String processInstanceId, String comment) {

    }

    @Override
    public void onTaskComplete(String businessKey, String flowModule, String processInstanceId, Participant participant, String comment) {

    }

    @Override
    public String processPath() {
        return null;
    }

    @Override
    public String viewPath() {
        return null;
    }

    @Override
    public String mobileView(String id) {
        return null;
    }

    @Override
    public void onEvent(String event, String businessKey, String flowModule, String processInstanceId, String comment) {

    }
}
