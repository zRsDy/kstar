package com.ibm.kstar.action.bizopp;

import com.ibm.kstar.api.bizopp.IBizOppLovInfoService;
import com.ibm.kstar.entity.bizopp.BusinessOpportunity;
import com.ibm.kstar.interceptor.system.permission.NoRight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.utils.ActionUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * ClassName:BizOppAction <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: Jan 6, 2017 10:07:56 AM <br/>
 * 
 * @author gaoyuliang
 * @version
 * @since JDK 1.7
 * @see
 */

@Controller
@RequestMapping("/lovBizopp")
public class BizOppLovAction extends BaseAction {

	@Autowired
	IBizOppLovInfoService bizOppLovInfoService;

	/**
	 * 
	 * autocomplete_custom:(带模糊查询的客户信息选择录入框). <br/> 
	 * TODO(提供公共的查询下拉选择框).
	 * 
	 * @author liming 
	 * @param condition
	 * @param request
	 * @return 
	 * @since JDK 1.7
	 */
	@NoRight
	@ResponseBody
	@RequestMapping("/autocomplete_bizopp")
	public String autocomplete_custom(PageCondition condition,HttpServletRequest request) {

		ActionUtil.requestToCondition(condition, request);
		List<BusinessOpportunity> bizOppInfos = bizOppLovInfoService.getBizOppNameInfoList(condition, getUserObject());

		return sendSuccessMessage(bizOppInfos);
	}
	
}
