package com.ibm.kstar.impl.report;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.report.IReportDelivelyDetailService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.entity.report.ReportDelivelyDetail;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class ReportDelivelyDetailServiceImpl implements IReportDelivelyDetailService {

	@Autowired
	BaseDao baseDao;
	
	/**
	 * 调用LovService类
	 */
	@Autowired
	ILovMemberService lovService;


	@Override
	public IPage getPage(PageCondition condition, String orgId) throws AnneException {
		String salesManName = condition.getStringCondition("salesManName");
		String customerName = condition.getStringCondition("customerName");
		String customClass = condition.getStringCondition("customClass");
		String customType = condition.getStringCondition("customType");
		String customTypeSub = condition.getStringCondition("customTypeSub");
		String printTime = condition.getStringCondition("printTime");
		String endTime = condition.getStringCondition("endTime");
		String materielCode = condition.getStringCondition("materielCode");
		String proCategory = condition.getStringCondition("proCategory");
		String proSeries = condition.getStringCondition("proSeries");
		String salesmanCenter = condition.getStringCondition("salesmanCenter");
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
			sql.append(" select t.id,")
				.append(" t.salesman_no,")
				.append(" t.salesman_id,")
				.append(" t.salesman_name,")
				.append(" t.customer_code,")
				.append(" t.customer_name,")
				.append(" t.custom_class,")
				.append(" t.custom_type,")
				.append(" t.custom_type_sub,")
				.append(" t.salesman_position_id,")
				.append(" t.salesman_position_name,")
				.append(" t.salesman_org_path,")
				.append(" t.salesman_org_id,")
				.append(" t.salesman_org_name,")
				.append(" t.parent_org_id,")
				.append(" (select a.lov_name from sys_t_lov_member a")
				.append(" where a.row_id = t.parent_org_id ")
				.append(" and a.group_code='ORG' ")
				.append(" ) as sales_center, ")
				.append(" t.dt_print_time,")
				.append(" t.invoice_quantity,")
				.append(" t.delivery_quantity,")
				.append(" t.province_name,")
				.append(" t.city_name,")
				.append(" t.county_name,")
				.append(" t.currency,")
				.append(" t.materiel_code,")
				.append(" t.pro_category,")
				.append(" t.pro_series,")
				.append(" t.rmb_amount,")
				.append(" t.usd_amount,")
				.append(" t.C_ERP_ORDER_CODE,")
				.append(" t.C_EXTERNAL_NO,")
				.append(" t.C_ORDER_CODE,")
				.append(" t.C_DELIVERY_CODE")
				.append(" from kstat_sale_performace_v_1 t")
				.append(" where 1=1")
				.append(" and t.salesman_org_path like ? ");
			args.add("%"+orgId+"%");
			
			if(StringUtils.isNotEmpty(salesManName)){
				sql.append(" and t.salesman_name like ? ");
				args.add("%"+salesManName+"%");
			}
			if(StringUtils.isNotEmpty(customerName)){
				sql.append(" and t.customer_name like ? ");
				args.add("%"+customerName+"%");
			}
			if(StringUtils.isNotEmpty(customClass)){
				sql.append(" and t.custom_class like ? ");
				args.add("%"+customClass+"%");
			}
			if(StringUtils.isNotEmpty(customType)){
				sql.append(" and t.custom_type like ? ");
				args.add("%"+customType+"%");
			}
			if(StringUtils.isNotEmpty(customTypeSub)){
				sql.append(" and t.custom_type_sub like ? ");
				args.add("%"+customTypeSub+"%");
			}
			
			if(StringUtils.isNotEmpty(printTime)){
				sql.append(" and to_char(t.dt_print_time,'yyyy-MM-dd') >= ? ");
				args.add(printTime);
			}
			if(StringUtils.isNotEmpty(endTime)){
				sql.append(" and to_char(t.dt_print_time,'yyyy-MM-dd') <= ? ");
				args.add(endTime);
			}
			if(StringUtils.isNotEmpty(materielCode)){
				sql.append(" and t.materiel_code = ? ");
				args.add(materielCode);
			}
			if(StringUtils.isNotEmpty(proCategory)){
				sql.append(" and t.pro_category like ? ");
				args.add("%"+proCategory+"%");
			}
			if(StringUtils.isNotEmpty(proSeries)){
				sql.append(" and t.pro_series like ? ");
				args.add("%"+proSeries+"%");
			}
			if(StringUtils.isNotEmpty(salesmanCenter)){
				sql.append(" and t.parent_org_id like ? ");
				args.add("%"+salesmanCenter+"%");
			}
			sql.append(" order by t.dt_print_time desc");
		IPage page = baseDao.searchBySql(sql.toString(), args.toArray(), condition.getRows(), condition.getPage());
		return page;
	}


	@Override
	public List<ReportDelivelyDetail> getList(PageCondition condition, String orgId) throws AnneException {
		String salesManName = condition.getStringCondition("salesManName");
		String customerName = condition.getStringCondition("customerName");
		String customClass = condition.getStringCondition("customClass");
		String customType = condition.getStringCondition("customType");
		String customTypeSub = condition.getStringCondition("customTypeSub");
		String printTime = condition.getStringCondition("printTime");
		String endTime = condition.getStringCondition("endTime");
		String materielCode = condition.getStringCondition("materielCode");
		String proCategory = condition.getStringCondition("proCategory");
		String proSeries = condition.getStringCondition("proSeries");
		String salesmanCenter = condition.getStringCondition("salesmanCenter");
		List<Object> args = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
			sql.append(" select t.id,")
				.append(" t.salesman_no,")
				.append(" t.salesman_id,")
				.append(" t.salesman_name,")
				.append(" t.customer_code,")
				.append(" t.customer_name,")
				.append(" t.custom_class,")
				.append(" t.custom_type,")
				.append(" t.custom_type_sub,")
				.append(" t.salesman_position_id,")
				.append(" t.salesman_position_name,")
				.append(" t.salesman_org_path,")
				.append(" t.dt_print_time,")
				.append(" t.invoice_quantity,")
				.append(" t.delivery_quantity,")
				.append(" t.province_name,")
				.append(" t.city_name,")
				.append(" t.county_name,")
				.append(" t.currency,")
				.append(" t.materiel_code,")
				.append(" t.pro_category,")
				.append(" t.pro_series,")
				.append(" t.rmb_amount,")
				.append(" t.usd_amount,")
				.append(" t.salesman_org_id,")
				.append(" t.salesman_org_name,")
				.append(" t.C_ERP_ORDER_CODE,")
				.append(" t.C_EXTERNAL_NO,")
				.append(" t.C_ORDER_CODE,")
				.append(" t.C_DELIVERY_CODE,")
				.append(" t.parent_org_id")
				.append(" from kstat_sale_performace_v_1 t")
				.append(" where 1=1")
				.append(" and t.salesman_org_path like ? ");
			args.add("%"+orgId+"%");
			
			if(StringUtils.isNotEmpty(salesManName)){
				sql.append(" and t.salesman_name like ? ");
				args.add("%"+salesManName+"%");
			}
			if(StringUtils.isNotEmpty(customerName)){
				sql.append(" and t.customer_name like ? ");
				args.add("%"+customerName+"%");
			}
			if(StringUtils.isNotEmpty(customClass)){
				sql.append(" and t.custom_class like ? ");
				args.add("%"+customClass+"%");
			}
			if(StringUtils.isNotEmpty(customType)){
				sql.append(" and t.custom_type like ? ");
				args.add("%"+customType+"%");
			}
			if(StringUtils.isNotEmpty(customTypeSub)){
				sql.append(" and t.custom_type_sub like ? ");
				args.add("%"+customTypeSub+"%");
			}
			
			if(StringUtils.isNotEmpty(printTime)){
				sql.append(" and to_char(t.dt_print_time,'yyyy-MM-dd') >= ? ");
				args.add(printTime);
			}
			if(StringUtils.isNotEmpty(endTime)){
				sql.append(" and to_char(t.dt_print_time,'yyyy-MM-dd') <= ? ");
				args.add(endTime);
			}
			if(StringUtils.isNotEmpty(materielCode)){
				sql.append(" and t.materiel_code = ? ");
				args.add(materielCode);
			}
			if(StringUtils.isNotEmpty(proCategory)){
				sql.append(" and t.pro_category like ? ");
				args.add("%"+proCategory+"%");
			}
			if(StringUtils.isNotEmpty(proSeries)){
				sql.append(" and t.pro_series like ? ");
				args.add("%"+proSeries+"%");
			}
			if(StringUtils.isNotEmpty(salesmanCenter)){
				sql.append(" and t.parent_org_id like ? ");
				args.add("%"+salesmanCenter+"%");
			}
			sql.append(" order by t.dt_print_time desc");
		List<Object[]> list = baseDao.findBySql(sql.toString(),args.toArray());
		List<ReportDelivelyDetail> rdList = new ArrayList<ReportDelivelyDetail>();
		if(null != list && list.size() > 0){
			for(Object[] obj:list){
				ReportDelivelyDetail rd = new ReportDelivelyDetail();
				rd.setId((String)obj[0]);
				rd.setSalesManNo((String)obj[1]);
				rd.setSalesManId((String)obj[2]);
				rd.setSalesManName((String)obj[3]);
				rd.setCustomerCode((String)obj[4]);
				rd.setCustomerName((String)obj[5]);
				rd.setCustomClass((String)obj[6]);
				rd.setCustomType((String)obj[7]);
				rd.setCustomTypeSub((String)obj[8]);
				rd.setSalesmanPosId((String)obj[9]);
				rd.setSalesmanPosName((String)obj[10]);
				rd.setSalesmanOrgPath((String)obj[11]);
				rd.setPrintTime((Date)obj[12]);
				BigDecimal invoQ = (BigDecimal)obj[13];
				rd.setInvoiceQuantity(invoQ != null?invoQ.toString():"0.0");
				BigDecimal deliQ = (BigDecimal)obj[14];
				rd.setDeliveryQuantity(deliQ != null?deliQ.toString():"0.0");
				rd.setProvinceName((String)obj[15]);
				rd.setCityName((String)obj[16]);
				rd.setCountyName((String)obj[17]);
				rd.setCurrency((String)obj[18]);
				rd.setMaterielCode((String)obj[19]);
				rd.setProCategory((String)obj[20]);
				rd.setProSeries((String)obj[21]);
				BigDecimal rmbAmount = (BigDecimal)obj[22];
				rd.setRmbAmount(rmbAmount != null?rmbAmount.toString():"0");
				BigDecimal usdAmount = (BigDecimal)obj[23];
				rd.setUsdAmount(usdAmount != null?usdAmount.toString():"0");
				rd.setSalesmanOrgId((String)obj[24]);
				rd.setSalesmanOrgName((String)obj[25]);
				rd.setErpOrderCode((String)obj[26]);
				rd.setExternalNo((String)obj[27]);
				rd.setOrderCode((String)obj[28]);
				rd.setDeliveryCode((String)obj[29]);
				rd.setParentOrgId((String) obj[30]);
				rdList.add(rd);
			}
		}
				
		return rdList;
	}
	
	@Override
	public List<List<Object>> getExcelData(List<ReportDelivelyDetail> delivelyDetailList) throws AnneException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        List<List<Object>> lstOut = new ArrayList<List<Object>>();
        List<Object> lstHead = new ArrayList<Object>();
        lstHead.add("销售员工号");
        lstHead.add("销售员名称");
        lstHead.add("客户编号");
        lstHead.add("客户名称");
        lstHead.add("客户类别");
        lstHead.add("行业大类");
        lstHead.add("行业小类");
        lstHead.add("销售员岗位名称");
        lstHead.add("所属部门");
        lstHead.add("营销中心");
        lstHead.add("出货单打印日期");
        lstHead.add("开票数量");
        lstHead.add("发货数量");
        lstHead.add("省份");
        lstHead.add("城市");
        lstHead.add("区/县");
        lstHead.add("币种");
        lstHead.add("物料编码");
        lstHead.add("产品类别");
        lstHead.add("产品系列");
        lstHead.add("本位币金额");
        lstHead.add("美元金额");
        lstHead.add("ERP订单号");
        lstHead.add("ERP外部出货单号");
        lstHead.add("CRM订单号");
        lstHead.add("CRM出货申请单号");
        lstOut.add(lstHead);
        for(ReportDelivelyDetail rd:delivelyDetailList){
        	List<Object> lstIn = new ArrayList<Object>();
        	lstIn.add(StringUtils.isNotEmpty(rd.getSalesManNo())?rd.getSalesManNo():"");
        	lstIn.add(StringUtils.isNotEmpty(rd.getSalesManName())?rd.getSalesManName():"");
        	lstIn.add(StringUtils.isNotEmpty(rd.getCustomerCode())?rd.getCustomerCode():"");
        	lstIn.add(StringUtils.isNotEmpty(rd.getCustomerName())?rd.getCustomerName():"");
        	lstIn.add(StringUtils.isNotEmpty(rd.getCustomClass())?rd.getCustomClass():"");
        	lstIn.add(StringUtils.isNotEmpty(rd.getCustomType())?rd.getCustomType():"");
        	lstIn.add(StringUtils.isNotEmpty(rd.getCustomTypeSub())?rd.getCustomTypeSub():"");
        	lstIn.add(StringUtils.isNotEmpty(rd.getSalesmanPosName())?rd.getSalesmanPosName():"");
        	lstIn.add(StringUtils.isNotEmpty(rd.getSalesmanOrgName())?rd.getSalesmanOrgName():"");
        	lstIn.add(StringUtils.isNotEmpty(rd.getSalesmanCenter())?rd.getSalesmanCenter():"");
        	if(null != rd.getPrintTime()){
        		lstIn.add(sdf.format(rd.getPrintTime()));
        	}else {
				lstIn.add("");
			}
        	lstIn.add(StringUtils.isNotEmpty(rd.getInvoiceQuantity())?rd.getInvoiceQuantity():"");
        	lstIn.add(StringUtils.isNotEmpty(rd.getDeliveryQuantity())?rd.getDeliveryQuantity():"");
        	lstIn.add(StringUtils.isNotEmpty(rd.getProvinceName())?rd.getProvinceName():"");
        	lstIn.add(StringUtils.isNotEmpty(rd.getCityName())?rd.getCityName():"");
        	lstIn.add(StringUtils.isNotEmpty(rd.getCountyName())?rd.getCountyName():"");
        	lstIn.add(StringUtils.isNotEmpty(rd.getCurrency())?rd.getCurrency():"");
        	lstIn.add(StringUtils.isNotEmpty(rd.getMaterielCode())?rd.getMaterielCode():"");
        	lstIn.add(StringUtils.isNotEmpty(rd.getProCategory())?rd.getProCategory():"");
        	lstIn.add(StringUtils.isNotEmpty(rd.getProSeries())?rd.getProSeries():"");
        	lstIn.add(StringUtils.isNotEmpty(rd.getRmbAmount())?rd.getRmbAmount():"");
        	lstIn.add(StringUtils.isNotEmpty(rd.getUsdAmount())?rd.getUsdAmount():"");
        	lstIn.add(StringUtils.isNotEmpty(rd.getErpOrderCode())?rd.getErpOrderCode():"");
        	lstIn.add(StringUtils.isNotEmpty(rd.getExternalNo())?rd.getExternalNo():"");
        	lstIn.add(StringUtils.isNotEmpty(rd.getOrderCode())?rd.getOrderCode():"");
        	lstIn.add(StringUtils.isNotEmpty(rd.getDeliveryCode())?rd.getDeliveryCode():"");
        	lstOut.add(lstIn);
        }
		return lstOut;
	}
	
	
	
}
