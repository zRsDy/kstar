package com.ibm.kstar.action.bizopp.info.integrator;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.StringUtil;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.api.bizopp.IBizoppService;
import com.ibm.kstar.entity.bizopp.GridVo;
import com.ibm.kstar.entity.bizopp.Integrator;
import com.ibm.kstar.interceptor.system.permission.NoRight;

/**
 * 授权单位
 * Created by wangchao on 2017/3/22.
 */
@Controller
@RequestMapping("/integrator")
public class BizOppIntegratorAction extends BaseAction{

    @Autowired
    IBizoppService bizoppService;

    @NoRight
    @ResponseBody
    @RequestMapping("/page")
    public String page(PageCondition condition, HttpServletRequest request) throws Exception {
        ActionUtil.requestToCondition(condition, request);
        String bizOppId = request.getParameter("bizOppId");
        condition.getFilterObject().addCondition("bizOppId", "eq", bizOppId);
        IPage p = bizoppService.queryBizOppIntegrator(condition);
        return sendSuccessMessage(p);
    }

    @NoRight
    @ResponseBody
    @RequestMapping("/pageChange")
    public String pageChange(PageCondition condition, HttpServletRequest request) throws Exception {
        ActionUtil.requestToCondition(condition, request);
        String bizOppId = request.getParameter("bizOppId");
        condition.getFilterObject().addCondition("bizOppId", "eq", bizOppId);
        IPage p = bizoppService.queryBizOppIntegrator(condition);
        return sendSuccessMessage(p);
    }

    @RequestMapping(value = "/add",method = RequestMethod.GET)
    public String add(String bizOppId_, Model model){
        Integrator integrator = new Integrator();
        integrator.setBizOppId(bizOppId_);
        model.addAttribute("entity",integrator);
        return forward("add");
    }

    @ResponseBody
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public String add_post(Integrator integrator){

        bizoppService.save(integrator);

        return sendSuccessMessage();
    }

    @RequestMapping("/edit")
    public String edit(String id,Model model){
        if(id==null){
            throw new AnneException("没有找到需要修改的数据");
        }
        Integrator entity = bizoppService.getBizOppIntegrator(id);
        if(entity==null){
            throw new AnneException("没有找到需要修改的数据");
        }
        model.addAttribute("entity",entity);
        return forward("add");
    }

    @ResponseBody
    @RequestMapping(value="/edit",method=RequestMethod.POST)
    public String edit(Integrator integrator){
        bizoppService.update(integrator);
        return sendSuccessMessage();
    }

    @NoRight
    @ResponseBody
    @RequestMapping(value="/edit2",method=RequestMethod.POST)
    public String edit2( GridVo gridVo){

        List<Integrator> list = JSON.parseArray(gridVo.getJsonStr(),Integrator.class);
        for (Integrator integrator:list) {
            if(StringUtil.isEmpty(integrator.getId())){
                if(StringUtil.isEmpty(integrator.getContact())
                        && StringUtil.isEmpty(integrator.getIntegrator())
                        && StringUtil.isEmpty(integrator.getPhone())) {
                }else{
                    integrator.setId(null);
                    bizoppService.save(integrator);
                }
            }else {
                bizoppService.update(integrator);
            }
        }
        return sendSuccessMessage();
    }

    @ResponseBody
    @RequestMapping(value="/delete",method=RequestMethod.POST)
    public String delete(String id){
        if(StringUtil.isEmpty(id)){
            throw new AnneException("id is null");
        }
        bizoppService.deleteIntegrator(id);
        return sendSuccessMessage();
    }

}
