package com.ibm.kstar.action.http;

import com.ibm.kstar.api.product.ICatelogMatchService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.xsnake.web.action.BaseAction;

import java.util.List;

/**
 * Copyright: Copyright 2007-2017 HuangQi All Rights Reserved.
 *
 * @Author 黄奇
 * @Title:
 * @Package
 * @Description:
 * @Date 2017年08月30日 21:19
 * @LastModifier 黄奇
 */
@Controller
@RequestMapping("/http")
public class CatelogHttpServiceAction extends BaseAction {

    @Autowired
    private ICatelogMatchService catelogMatchService;

    @RequestMapping("/preDefineInCatelog")
    public String preDefineInCatelog(@RequestParam("proId") List<String> productIds) {
        if (productIds == null || productIds.size() == 0) {
            return sendErrorMessage("无需要的产品");
        }
        try {
            catelogMatchService.preDefineInCatelog(productIds);
        } catch (Exception e) {
            String message = e.getMessage();
            e.printStackTrace();
            if (StringUtils.isEmpty(message)) {
                message = "内部错误";
            }
            return sendErrorMessage(message);
        }
        return sendSuccessMessage("成功入目录库" + productIds.size() + "个");
    }

}
