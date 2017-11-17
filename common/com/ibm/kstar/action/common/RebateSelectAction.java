package com.ibm.kstar.action.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.page.IPage;
import org.xsnake.web.page.PageImpl;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.api.bizopp.IBizoppService;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/common/rebate")
public class RebateSelectAction extends BaseAction{
	
	@Autowired
	IBizoppService bizoppService;
	@Autowired
	BaseDao baseDao;
	
	@NoRight
	@RequestMapping("/selectList")
	public String selectOrder(String pickerId,String orderType,Model model,HttpServletRequest request){
		model.addAttribute("pickerId",pickerId);
		String customerId = request.getParameter("customerId");
		String customerName = request.getParameter("customerName");
		String productId = request.getParameter("productId");
		model.addAttribute("customerId",customerId);
		model.addAttribute("customerName",customerName);
		model.addAttribute("productId",productId);
		
		return forward("rebateSelectList");
	}
	
	@SuppressWarnings("unchecked")
	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition,HttpServletRequest request) throws Exception{
//		ActionUtil.requestToCondition(condition, request);
//		condition.setCondition("userObject", getUserObject());
//		String customerId = request.getParameter("customerId");
//		String productId = request.getParameter("productId");
//		List<Object>  args = new ArrayList<Object>(); 
//		StringBuffer sb = new StringBuffer("select new com.ibm.kstar.action.common.RebateVO(h,l) "
//				+ " from Rebate h , RebateLine l "
//				+ " where h.id = l.rebateId and h.status = 'Completed' ");
//		sb.append(" and h.startDate <= ? ");
//		args.add(new Date());
//		sb.append(" and h.endDate >= ? ");
//		args.add(new Date());
//		
//		sb.append(" and l.orderQty < l.applyQty ");
//		
//		if(StringUtil.isNotEmpty(customerId)){
//			sb.append(" and h.applyAgent = ? ");
//			args.add(customerId);
//		}
//		if(StringUtil.isNotEmpty(productId)){
//			sb.append(" and l.productId = ? ");
//			args.add(productId);
//		}
//		
//		sb.append(" and not exists (select 1 from RebateChange rc where (rc.status = ? or rc.status = ?) and rc.rebateId = h.id) ");
//		args.add(ProcessConstants.PROCESS_STATUS_Processing);
//		args.add(ProcessConstants.PROCESS_STATUS_Rejected);
//		
//		 IPage p = baseDao.search(sb.toString(), args.toArray(), condition.getRows(), condition.getPage());
		

		ActionUtil.requestToCondition(condition, request);
		condition.setCondition("userObject", getUserObject());
		String customerId = request.getParameter("customerId");
		String productId = request.getParameter("productId");
		List<Object>  args = new ArrayList<Object>(); 
		StringBuffer sql = new StringBuffer("select l.row_id id,");
		sql.append(" h.no no,");
		sql.append(" h.no type,");
		sql.append(" h.apply_Agent  applyAgent,");
		sql.append(" h.apply_Agent_name applyAgentName,");
		sql.append(" h.start_date startDate,");
		sql.append(" h.end_date endDate,");
		sql.append(" l.biz_id bizId,");
		sql.append(" l.biz_name bizName,");
		sql.append(" l.productName productName,");
		sql.append(" l.product_model productModel,");
		sql.append(" l.product_code productCode,");
		sql.append(" l.product_id productId,");
		sql.append(" l.apply_qty applyQty,");
		sql.append(" l.source_price sourcePrice,");
		sql.append(" l.approve_price applyPrice,");
		sql.append(" l.apply_rebate applyRebate,");
		sql.append(" l.approve_rebate approveRebate,");
		sql.append(" l.approve_price approvePrice,");
		sql.append(" l.amount amount,");
		sql.append(" l.client_id clientId,");
		sql.append(" l.client_name clientName,");
		sql.append(" l.remark remark , ");
		sql.append(" NVL(ols.pro_qty,0) + NVL(olcs.pro_qty,0) as orderQty ");
		
	    sql.append(" from crm_t_rebate_header h , crm_t_rebate_line l ");
		sql.append(" LEFT JOIN (select ol.c_special_price_line_id sp_id , sum(ol.n_product_quantity) pro_qty  "
				+ "  from crm_t_order_lines ol "
				+ "  where ol.c_status != 'CANCELLED' "
				+ "  and ol.c_special_price_line_id is not null "
				+ "  and not exists( select 1 from crm_t_order_lines_change olc , crm_t_order_header_change ohc "
				+ "                  where olc.c_from_id = ol.c_pid "
				+ "                  and olc.c_order_change_id = ohc.c_pid "
				+ "                  and ( ohc.c_event_status = '20' or ohc.c_event_status = '40' ) "
				+ "                  and olc.c_status != 'CANCELLED' ) "
				+ "  group by ol.c_special_price_line_id ) ols on l.row_id = ols.sp_id " );
		
		sql.append(" LEFT JOIN (select olc.c_special_price_line_id sp_id, sum(olc.n_product_quantity) pro_qty "
				+ "    from crm_t_order_lines_change olc , crm_t_order_header_change ohc "
				+ "    where olc.c_order_change_id = ohc.c_pid "
				+ "    and ( ohc.c_event_status = '20' or ohc.c_event_status = '40' ) "
				+ "    and olc.c_status != 'CANCELLED' "
				+ "    group by olc.c_special_price_line_id ) olcs on l.row_id = olcs.sp_id ");
		
		sql.append(" where h.row_id = l.rebate_id ");
		sql.append(" and h.status = 'Completed' ");
		sql.append(" and l.apply_qty > NVL(ols.pro_qty,0) + NVL(olcs.pro_qty,0) ");
		sql.append(" and h.start_date <= sysdate ");
		sql.append(" and h.end_date >= sysdate ");
		
		if(StringUtil.isNotEmpty(customerId)){
			sql.append(" and h.apply_Agent = ? ");
			args.add(customerId);
		}
		if(StringUtil.isNotEmpty(productId)){
			sql.append(" and l.product_id = ? ");
			args.add(productId);
		}
		sql.append(" and not exists (select 1 from crm_t_rebate_header_change rc where (rc.status = ? or rc.status = ? ) and rc.rebate_id = h.row_id )  ");
		args.add(ProcessConstants.PROCESS_STATUS_Processing);
		args.add(ProcessConstants.PROCESS_STATUS_Rejected);
		
		IPage page = baseDao.searchBySql4Map(sql.toString(), args.toArray(), condition.getRows(), condition.getPage());
		List<Map<Object, Object>> list = ((List<Map<Object, Object>>)page.getList());
		List<RebateVO> taskList = new ArrayList<RebateVO>();
		for(Map<Object, Object> map :  list){
			Map<Object, Object> map1 = new CaseInsensitiveMap(); 
			for (Object key : map.keySet()) {
				map1.put(key, map.get(key));
			}
			taskList.add((RebateVO)BeanUtils.convertMap(RebateVO.class, map1));
		}
		((PageImpl)page).setList(taskList);
		return sendSuccessMessage(page);
	}
}
