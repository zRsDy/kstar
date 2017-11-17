package com.ibm.kstar.api.log;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;


/**
 * ClassName:IOrderService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年12月16日 上午11:09:12 <br/>
 * 
 * @author liming
 * @version
 * @since JDK 1.7
 * @see
 */
public interface IInterFaceLogService {
	
	/**
	 * 
	 * queryOrderHeaders:查询接口日志列表. <br/> 
	 * 
	 * @author liming 
	 * @param condition
	 * @return 
	 * @since JDK 1.7
	 */
	IPage queryMethodLogger(PageCondition condition);
	
}
