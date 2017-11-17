package com.ibm.kstar.impl.order;

import com.ibm.kstar.log.IMethodLogService;
import com.ibm.kstar.log.MethodLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright: Copyright 2007-2017 HuangQi All Rights Reserved.
 *
 * @Author 黄奇
 * @Title:
 * @Package
 * @Description:
 * @Date 2017年11月07日 16:07
 * @LastModifier 黄奇
 */

public class MethodLoggerStep {


    /**
     * 业务相关操作
     *
     * @author Qi Created on 2017/11/7
     */
    public interface IBusinessAction {
        /**
         * @param logedObject 需要记录到日志中的对象,在业务逻辑中需要手动将需要记录到日志的对象添加到这个list中
         */
        void doAction(List<Object> logedObject);
    }

    private IMethodLogService logService;
    private int i = 1;
    private MethodLogger logger;
    private boolean isEnded = false;

    public MethodLoggerStep(IMethodLogService logService, String fromFunction, String no) {
        this.logService = logService;
        this.logger = logService.getMethodLogger(fromFunction, no);
    }

    public static MethodLoggerStep setup(IMethodLogService logService, String fromFunction, String no) {
        return new MethodLoggerStep(logService, fromFunction, no);
    }


    public MethodLoggerStep doLog(String functionName, Object[] args, IBusinessAction businessAction) {
        this.logService.setFunctionNameAndParameter(this.logger, functionName, this.i, args);
        List<Object> returnObjects = new ArrayList<>();
        Exception exception = null;
        try {
            businessAction.doAction(returnObjects);
        } catch (Exception e) {
            e.printStackTrace();
            isEnded = true;
            exception = e;
            throw e;
        } finally {
            this.logService.setReturnDataNotes(false, logger, exception, this.i, returnObjects);
            if (isEnded) {
                this.end();
            }
        }
        this.i++;
        return this;
    }

    public MethodLoggerStep doLog(String functionName, IBusinessAction businessAction) {
        return this.doLog(functionName, null, businessAction);
    }

    public void end() {
        logService.log(this.logger);
    }
}
