package com.ibm.kstar.action.pdm;

import com.ibm.kstar.api.pdm.PdmFowHistoryService;
import com.ibm.kstar.interceptor.system.permission.NoRight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.ActionUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * Copyright: Copyright 2007-2017 HuangQi All Rights Reserved.
 *
 * @Author 黄奇
 * @Title:
 * @Package
 * @Description:
 * @Date 2017年09月22日 09:35
 * @LastModifier 黄奇
 */
@Controller
@RequestMapping("/pdm/flow")
public class PdmFlowHistoryAction extends BaseAction {
    @Autowired
    private PdmFowHistoryService historyService;

    /**
     * PDM评审历史
     *
     * @param id
     * @return
     * @throws Exception
     */
    @NoRight
    @RequestMapping("/history")
    public String history(String no, String type, Model model) {
        model.addAttribute("no", no);
        return forward("pdmFlowHistory");
    }

    /**
     * PDM评审历史 分页
     *
     * @param condition
     * @return
     */
    @NoRight
    @ResponseBody
    @RequestMapping("/historyPage")
    public String history(PageCondition condition,String no, HttpServletRequest request) {
        ActionUtil.requestToCondition(condition, request);
        ActionUtil.doSearch(condition);
        IPage page = historyService.query(condition,no);
        return sendSuccessMessage(page);
    }

}
