package com.ibm.kstar.api.custom;

import java.util.List;
import java.util.Map;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.action.datatrace.TraceDetailVo;

public interface IDataTraceService {
	
	/**
	 * 查询系统注册类
	 * @return
	 */
	List<Map<String,Object>> getSysClass();
	
	/**
	 * 获取数据追踪
	 * @param condition
	 * @return
	 */
	IPage queryDatatrace(PageCondition condition);
	
	/**
	 * 获取数据变更详情
	 * @param condition
	 * @return
	 */
	List<TraceDetailVo> queryDatatraceDetail(PageCondition condition);
}
