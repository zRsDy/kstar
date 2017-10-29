package com.ibm.kstar.action.bizopp.info.proPrice;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.ActionUtil;

import com.ibm.kstar.api.bizopp.IBizoppService;
import com.ibm.kstar.interceptor.system.permission.NoRight;
/**
 * @author Wutw 2017-1-11
 *
 */
@Controller
@RequestMapping("/bizproprice")
public class BizProPriceAction extends BaseAction {

	@Autowired
	IBizoppService bizService;

	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) throws Exception {
		
		ActionUtil.requestToCondition(condition, request);
		//商机编号 查询对应商信的报价单
		String boCode = request.getParameter("boCode");
		
		condition.getFilterObject().addCondition("boCode", "eq", boCode);
		
		IPage p = bizService.queryProPrice(condition);

		return sendSuccessMessage(p);
	}


	
}
