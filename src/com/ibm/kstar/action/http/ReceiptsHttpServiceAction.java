package com.ibm.kstar.action.http;

import com.ibm.kstar.api.order.IDeliveryService;
import com.ibm.kstar.interceptor.system.permission.NoRight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.exception.AnneException;

import java.util.List;

/**
 * Copyright: Copyright 2007-2017 HuangQi All Rights Reserved.
 *
 * @Author 黄奇
 * @Title:
 * @Package
 * @Description:
 * @Date 2017年09月27日 20:44
 * @LastModifier 黄奇
 */
@Controller
@RequestMapping("/http")
public class ReceiptsHttpServiceAction extends BaseAction {
    @Autowired
    private BaseDao baseDao;

    private Logger logger = LoggerFactory.getLogger(ReceiptsHttpServiceAction.class);

    @Autowired
    private IDeliveryService deliveryService;

    @NoRight
    @ResponseBody
    @RequestMapping("/createReceipts")
    public String createReceipts(@RequestParam("externalNos") List<String> externalNos){
        if (externalNos == null || externalNos.size() == 0) {
            return sendErrorMessage("参数为空");
        }

        for (String externalNo : externalNos) {
            List<String> deliveryCodes = deliveryService.getNeedDetailDeliveryCodeByExternalNo(externalNo);
            if (deliveryCodes.size() == 0) {
                logger.info("外部出货单{}无需要成回款计划失败的出货单。", externalNo);
                continue;
            }
            try {
                for (String deliveryCode : deliveryCodes) {
                    deliveryService.createContractReceiptDetail(deliveryCode);
                }
            } catch (Exception e) {
                logger.error("生成外部出货单(" + externalNo +")的回款计划失败。", e);
                throw new AnneException("生成外部出货单("+externalNo+")的回款计划失败。");
            }
        }

        String msg = "操作成功";
        return sendSuccessMessage(msg);
    }
}
