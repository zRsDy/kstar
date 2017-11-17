package com.ibm.kstar.action.appBizType;

import com.ibm.kstar.api.appBizType.IAppBizTypeService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.interceptor.system.permission.NoRight;
import com.ibm.kstar.service.IAppFlowParamService;
import com.ibm.kstar.service.IAppFlowPowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.ActionUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by wangchao on 2017/3/21.
 */
@Controller
@RequestMapping(value = "/appAuth")
public class AppBizTypeAction extends BaseAction {

    @Autowired
    ILovMemberService lovMemberService;

    @Autowired
    IAppBizTypeService appBizTypeService;


    @RequestMapping("/list")
    public String list() {
        return forward("list");
    }

    @NoRight
    @ResponseBody
    @RequestMapping("/page")
    public String page(PageCondition condition, HttpServletRequest request) {
        ActionUtil.requestToCondition(condition, request);
        //模糊查询
        String searchKey = condition.getStringCondition("searchKey");
        condition.getFilterObject().addCondition("groupCode", "=", "AUTH_APPLICATION");
        if(searchKey != null && !"".equals(searchKey)){
            condition.getFilterObject().addOrCondition("code", "like", "%"+searchKey+"%");
            condition.getFilterObject().addOrCondition("optTxt1", "like", "%"+searchKey+"%");
        }
        IPage p = lovMemberService.query(condition);
        return sendSuccessMessage(p);
    }

    @RequestMapping("/config")
    public String config(String id, Model model) {
        model.addAttribute("lovMember",lovMemberService.get(id));
        return forward("config");
    }

    @ResponseBody
    @RequestMapping(value = "/config", method = RequestMethod.POST)
    public String config(LovMember lov) {
        lovMemberService.update(lov);
        return sendSuccessMessage();
    }

    @ResponseBody
    @RequestMapping("/getAuths")
    public String getFlows(String optTxt1) {

        List<LovMember> list = appBizTypeService.getList(optTxt1);


        return sendSuccessMessage(list);
    }
}
