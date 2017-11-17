package com.ibm.kstar.action.bizopp.supportFeedBack;

import com.ibm.kstar.api.bizopp.IBizoppService;
import com.ibm.kstar.entity.bizopp.SpecialPrice;
import com.ibm.kstar.entity.bizopp.SupportFeedBack;
import com.ibm.kstar.interceptor.system.permission.NoRight;

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

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by wangchao on 2017/3/27.
 */
@Controller
@RequestMapping("/sfb")
public class SupportFeedBackAction extends BaseAction {

    @Autowired
    IBizoppService bizoppService;

    @RequestMapping("/add")
    public String add(String businessId,Model model) {

        SupportFeedBack sfb = new SupportFeedBack();
        sfb.setBusinessId(businessId);
        sfb.setDistributeDate(new Date());

        model.addAttribute("entity",sfb);
        return forward("add");
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public String add_post(SupportFeedBack sfb) {
//      if(sfb.getSupportBegin().getTime() < new Date().getTime() || sfb.getSupportEnd().getTime() <= sfb.getSupportBegin().getTime()){
//            throw new AnneException("支持开始与结束时间错误！");
//      }
    	if(sfb.getSupportBegin().getTime()>sfb.getSupportEnd().getTime()){
    		throw new AnneException("支持结束日期应大于或等于支持开始日期！");
    	}else if(sfb.getSupportBegin().getTime()<sfb.getDistributeDate().getTime()){
    		throw new AnneException("支持日期应该大于或等于分配日期");
    	}
        bizoppService.save(sfb);
        return sendSuccessMessage();
    }

    @NoRight
    @ResponseBody
    @RequestMapping("/page")
    public String page(PageCondition condition, HttpServletRequest request) throws Exception {
        ActionUtil.requestToCondition(condition, request);
        String bizId = request.getParameter("businessId");
        condition.getFilterObject().addCondition("businessId", "eq", bizId);
        IPage p = bizoppService.querySupportFeedBack(condition);

        return sendSuccessMessage(p);
    }

    @RequestMapping("/edit")
    public String edit(String id,Model model) {
        if(id==null){
            throw new AnneException("没有找到需要修改的数据");
        }
        SupportFeedBack entity = bizoppService.getSupportFeedBack(id);
        if(entity==null){
            throw new AnneException("没有找到需要修改的数据");
        }
        model.addAttribute("entity",entity);
        return forward("add");
    }

    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    @ResponseBody
    public String edit_post(SupportFeedBack supportFeedBack) {
//        if(supportFeedBack.getSupportBegin().getTime() < new Date().getTime() || supportFeedBack.getSupportEnd().getTime() <= supportFeedBack.getSupportBegin().getTime()){
//            throw new AnneException("支持开始与结束时间错误！");
//        }
        if(supportFeedBack.getSupportBegin().getTime()>supportFeedBack.getSupportEnd().getTime()){
    		throw new AnneException("支持结束日期应大于或等于支持开始日期！");
    	}else if(supportFeedBack.getSupportBegin().getTime()<supportFeedBack.getDistributeDate().getTime()){
    		throw new AnneException("支持日期应该大于或等于分配日期");
    	}
        bizoppService.update(supportFeedBack);
        return sendSuccessMessage();
    }

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public void delete(String id){
        bizoppService.deleteSfb(id);
    }


    @RequestMapping("/sfbList")
    public String sfbList(String id,Model model) {
        model.addAttribute("businessId",id);
        return forward("sfbList");
    }

}
