package com.ibm.kstar.action.common;


import com.ibm.kstar.api.bizopp.IBizoppService;
import com.ibm.kstar.interceptor.system.permission.NoRight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.xsnake.web.action.BaseAction;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/common/bizOpp")
public class BizOppSelectAction extends BaseAction {

    @Autowired
    IBizoppService bizoppService;

    @NoRight
    @RequestMapping("/selectList")
    public String selectOrder(String pickerId, String orderType, Model model, HttpServletRequest request) {
        model.addAttribute("pickerId", pickerId);
        String createdOrgId = request.getParameter("createdOrgId");
        String customerName = request.getParameter("customerName");
        String productId = request.getParameter("productId");
        String productModel = request.getParameter("productModel");
        String usable = request.getParameter("usable");
        model.addAttribute("createdOrgId", createdOrgId);
        model.addAttribute("customerName", customerName);
        model.addAttribute("productId", productId);
        
        String orderNo = request.getParameter("orderNo");
        model.addAttribute("orderNo", orderNo);

        model.addAttribute("productModel", productModel);
        //usable 仅查询商机${productModel}产品剩余数量大于0的商机
        model.addAttribute("usable", usable);

        return forward("bizOppSelectList");
    }
}
