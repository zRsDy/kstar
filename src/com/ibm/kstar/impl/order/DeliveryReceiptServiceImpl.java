package com.ibm.kstar.impl.order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.remote.server.Remote;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.dao.HqlUtil;
import org.xsnake.web.dao.utils.FilterObject;
import org.xsnake.web.dao.utils.HqlObject;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.order.IDeliveryReceiptService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.team.ITeamService;
import com.ibm.kstar.entity.order.DeliveryReceipt;

@Service
@Remote
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class DeliveryReceiptServiceImpl implements IDeliveryReceiptService {
	@Autowired
	BaseDao baseDao;
	@Autowired
	private ITeamService teamService;
	
	@Override
	public void saveDeliveryReceipt(DeliveryReceipt deliveryReceipt,UserObject userObject)
			throws AnneException {
	}
	/**
	 * 
	 * TODO 出货单签收. 
	 * @throws Exception 
	 * @see com.ibm.kstar.api.order.IDeliveryReceiptService#updateDeliveryReceipt(com.ibm.kstar.entity.order.DeliveryReceipt, com.ibm.kstar.api.system.permission.UserObject)
	 */
	@Override
	public void updateDeliveryReceipt(DeliveryReceipt deliveryReceipt,UserObject userObject) throws Exception{
		List<Map<Object, Object>> lines = deliveryReceipt.getLines();
		if( lines != null){
			for (Map<Object, Object> map : lines) {
				DeliveryReceipt receipt = (DeliveryReceipt) BeanUtils.convertMap(DeliveryReceipt.class, map);
				if(receipt != null){
					DeliveryReceipt oldDeliveryReceipt = baseDao.get(DeliveryReceipt.class,receipt.getId());
					if (oldDeliveryReceipt == null) {
						throw new AnneException(IDeliveryReceiptService.class.getName()
								+ " updateDeliveryReceipt : 没有找到要更新的数据");
					}
					BeanUtils.copyPropertiesIgnoreNull(receipt, oldDeliveryReceipt);
					
					oldDeliveryReceipt.setStatus(deliveryReceipt.getStatus());
					oldDeliveryReceipt.setLogisticsCompany(deliveryReceipt.getLogisticsCompany());
					oldDeliveryReceipt.setLogisticsNo(deliveryReceipt.getLogisticsNo());
					//预计到达时间
					oldDeliveryReceipt.setEstimateArrivalTime(deliveryReceipt.getEstimateArrivalTime());
					oldDeliveryReceipt.setEstimateTime(deliveryReceipt.getEstimateTime());
					//实际到达时间
					oldDeliveryReceipt.setActualArrivalTime(deliveryReceipt.getActualArrivalTime());
					oldDeliveryReceipt.setActualTime(deliveryReceipt.getActualTime());
					oldDeliveryReceipt.setLogisticsContactsName(deliveryReceipt.getLogisticsContactsName());
					oldDeliveryReceipt.setLogisticsContactsTel(deliveryReceipt.getLogisticsContactsTel());
					oldDeliveryReceipt.setLocalLogisticsNo(deliveryReceipt.getLocalLogisticsNo());
					oldDeliveryReceipt.setRemarks(deliveryReceipt.getRemarks());
					
					oldDeliveryReceipt.setUpdatedAt(new Date());
					oldDeliveryReceipt.setUpdatedById(userObject.getEmployee().getId());
					baseDao.update(oldDeliveryReceipt);
				}
			}
		}
	}

	@Override
	public IPage queryDeliveryReceipts(PageCondition condition,UserObject userObject)
			throws AnneException {
		FilterObject filterObject = condition.getFilterObject(DeliveryReceipt.class);
		filterObject.addOrderBy("updatedAt", "desc");
		
		HqlObject hqlObject = null;
		//如果是物流公司
		if(userObject != null && !userObject.isInner() 
				&& "WL".equals(userObject.getOrg().getOptTxt5())){
			hqlObject = HqlUtil.getHqlObjectNoPermission(filterObject);
		}else{
			hqlObject = HqlUtil.getHqlObject(filterObject);
		}
		
		StringBuffer sb = new StringBuffer(hqlObject.getHql());
		int i = sb.toString().indexOf("order by");
		String salesmanId = condition.getStringCondition("salesmanId");
		String salesmanDep = condition.getStringCondition("salesmanDep");
		String businessManagerId = condition.getStringCondition("businessManagerId");
		if(StringUtil.isNotEmpty(salesmanId) 
				|| StringUtil.isNotEmpty(salesmanDep)
				|| StringUtil.isNotEmpty(businessManagerId)){
			
			StringBuffer hql = new StringBuffer(" and deliveryreceipt.orderCode in ( select o.orderCode from OrderHeader o where 1=1 ");
			if(StringUtil.isNotEmpty(salesmanId)){
				hql.append(" and o.salesmanId = '" + salesmanId + "'");
			}
			if(StringUtil.isNotEmpty(salesmanDep)){
				hql.append(" and o.salesmanDep = '" + salesmanDep + "'");
			}
			if(StringUtil.isNotEmpty(businessManagerId)){
				hql.append(" and o.businessManagerId = '" + businessManagerId + "'");
			}
			hql.append(" )");
			if(i>0){
				sb.insert(i, hql.toString());
			}else{
				sb.append(hql.toString());
			}
		}
		return baseDao.search(sb.toString(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}
	
	@Override
	public void deleteDeliveryReceipt(String deliveryReceiptId)
			throws AnneException {
		baseDao.deleteById(DeliveryReceipt.class, deliveryReceiptId);
	}

	@Override
	public DeliveryReceipt getDeliveryReceipt(String deliveryReceiptId)
			throws AnneException {
		return baseDao.get(DeliveryReceipt.class, deliveryReceiptId);
	}
	

	/**
	 * updateStatus:修改物流签收状态. <br/> 
	 * 
	 * @author liming 
	 * @param id 开票申请ID
	 * @param status 状态
	 * @since JDK 1.7
	 */
	@Override
	public void updateStatus(String id,String status) {
		DeliveryReceipt deliveryReceipt = baseDao.get(DeliveryReceipt.class,id);
		if (deliveryReceipt == null) {
			throw new AnneException(IDeliveryReceiptService.class.getName()
					+ " updateStatus : 没有找到要更新的数据");
		}
		deliveryReceipt.setStatus(status);
		deliveryReceipt.setUpdatedAt(new Date());
		
		baseDao.update(deliveryReceipt);
	}
	
	@Override
	public List<DeliveryReceipt> getDeliveryReceipts(PageCondition condition,UserObject userObject)
			throws AnneException {
		FilterObject filterObject = condition.getFilterObject(DeliveryReceipt.class);
		filterObject.addOrderBy("updatedAt", "desc");
		HqlObject hqlObject = null;
		//如果是物流公司
		if(userObject != null && !userObject.isInner() 
				&& "WL".equals(userObject.getOrg().getOptTxt5())){
			hqlObject = HqlUtil.getHqlObjectNoPermission(filterObject);
		}else{
			hqlObject = HqlUtil.getHqlObject(filterObject);
		}
		
		StringBuffer sb = new StringBuffer(hqlObject.getHql());
		int i = sb.toString().indexOf("order by");
		String salesmanId = condition.getStringCondition("salesmanId");
		String salesmanDep = condition.getStringCondition("salesmanDep");
		String businessManagerId = condition.getStringCondition("businessManagerId");
		if(StringUtil.isNotEmpty(salesmanId) 
				|| StringUtil.isNotEmpty(salesmanDep)
				|| StringUtil.isNotEmpty(businessManagerId)){
			
			StringBuffer hql = new StringBuffer(" and deliveryreceipt.orderCode in ( select o.orderCode from OrderHeader o where 1=1 ");
			if(StringUtil.isNotEmpty(salesmanId)){
				hql.append(" and o.salesmanId = '" + salesmanId + "'");
			}
			if(StringUtil.isNotEmpty(salesmanDep)){
				hql.append(" and o.salesmanDep = '" + salesmanDep + "'");
			}
			if(StringUtil.isNotEmpty(businessManagerId)){
				hql.append(" and o.businessManagerId = '" + businessManagerId + "'");
			}
			hql.append(" )");
			if(i>0){
				sb.insert(i, hql.toString());
			}else{
				sb.append(hql.toString());
			}
		}
		return baseDao.findEntity(sb.toString(), hqlObject.getArgs());
	}
	
	@Override
	public List<List<Object>> getExcelData(List<DeliveryReceipt> deliveryReceipts) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<List<Object>> lstOut = new ArrayList<List<Object>>();
		List<Object> lstHead = new ArrayList<Object>();
		lstHead.add("签收单编号");
		lstHead.add("出货申请编号");
		lstHead.add("订单编号");
		lstHead.add("下单客户");
		lstHead.add("物料编码");
		lstHead.add("产品描述");
		lstHead.add("出货数量");
		lstHead.add("签收数量");
		lstHead.add("单位");
		lstHead.add("收货人");
		lstHead.add("收货人电话");
		lstHead.add("收货地址");
		lstHead.add("备注");
		lstHead.add("出货单编号（外部）");
		lstHead.add("出货单打印日期（外部）");
		lstHead.add("物流公司");
		lstHead.add("物流运单号");
		lstHead.add("ERP订单编号 ");
		lstHead.add("合同/商机编码 ");
		lstHead.add("订单行号 ");
		lstHead.add("状态");
		lstOut.add(lstHead);
		
		if(deliveryReceipts != null && deliveryReceipts.size() > 0 ){
			for(DeliveryReceipt receipt : deliveryReceipts){
				if(receipt != null){
					List<Object> lstIn = new ArrayList<Object>();
					lstIn.add(receipt.getReceiptCode());
					lstIn.add(receipt.getDeliveryCode());
					lstIn.add(receipt.getOrderCode());
					lstIn.add(receipt.getSingleCustName());
					lstIn.add(receipt.getMaterielCode());
					lstIn.add(receipt.getProDesc());
					lstIn.add(receipt.getDeliveryQty());
					lstIn.add(receipt.getReceiptQty());
					lstIn.add(receipt.getUnitLable());
					lstIn.add(receipt.getConsignee());
					lstIn.add(receipt.getConsigneeTel());
					lstIn.add(receipt.getReceAddress());
					lstIn.add(receipt.getRemarks());
					lstIn.add(receipt.getExternalNo());
					lstIn.add(receipt.getInvoicePrintTime() == null ? "" : sdf.format(receipt.getInvoicePrintTime()));
					lstIn.add(receipt.getLogisticsCompanyLable());
					lstIn.add(receipt.getLogisticsNo());
					lstIn.add(receipt.getErpOrderCode());
					lstIn.add(receipt.getSourceCode());
					lstIn.add(receipt.getOrderLineNo());
					lstIn.add(receipt.getStatusLable());

					//lstIn.add(receipt.getEstimateArrivalTime() == null ? "" : sdf.format(receipt.getEstimateArrivalTime()));
					//lstIn.add(receipt.getActualArrivalTime() == null ? "" : sdf.format(receipt.getActualArrivalTime()));
					lstOut.add(lstIn);
				}
			}
		}
		return lstOut;
	}
	
	@Override
	public void setSearchCondition(PageCondition condition , UserObject userObject) {
		
		String searchKey = condition.getStringCondition("searchKey");
		if(StringUtil.isNotEmpty(searchKey)){
			condition.getFilterObject().addOrCondition("orderCode", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("deliveryCode", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("receiptCode", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("logisticsNo", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("consignee", "like", "%"+searchKey+"%");
		}
		
		String receiptCode = condition.getStringCondition("receiptCode");
		if(StringUtil.isNotEmpty(receiptCode)){
			condition.getFilterObject().addCondition("receiptCode", "like", "%"+receiptCode+"%");
		}
		
		String deliveryCode = condition.getStringCondition("deliveryCode");
		if(StringUtil.isNotEmpty(deliveryCode)){
			condition.getFilterObject().addCondition("deliveryCode", "like", "%"+deliveryCode+"%");
		}
		String orderCode = condition.getStringCondition("orderCode");
		if(StringUtil.isNotEmpty(orderCode)){
			condition.getFilterObject().addCondition("orderCode", "like", "%"+orderCode+"%");
		}
		String sourceCode = condition.getStringCondition("sourceCode");
		if(StringUtil.isNotEmpty(sourceCode)){
			condition.getFilterObject().addCondition("sourceCode", "like", "%"+sourceCode+"%");
		}
		String consignee = condition.getStringCondition("consignee");
		if(StringUtil.isNotEmpty(consignee)){
			condition.getFilterObject().addCondition("consignee", "like", "%"+consignee+"%");
		}
		String logisticsNo = condition.getStringCondition("logisticsNo");
		if(StringUtil.isNotEmpty(logisticsNo)){
			condition.getFilterObject().addCondition("logisticsNo", "like", "%"+logisticsNo+"%");
		}
		String externalNo = condition.getStringCondition("externalNo");
		if(StringUtil.isNotEmpty(externalNo)){
			condition.getFilterObject().addCondition("externalNo", "like", "%"+externalNo+"%");
		}
		String consigneeTel = condition.getStringCondition("consigneeTel");
		if(StringUtil.isNotEmpty(consigneeTel)){
			condition.getFilterObject().addCondition("consigneeTel", "like", "%"+consigneeTel+"%");
		}
		String createdById = condition.getStringCondition("createdById");
		if(StringUtil.isNotEmpty(createdById)){
			condition.getFilterObject().addCondition("createdById", "eq", createdById);
		}
		String status = condition.getStringCondition("status");
		if(StringUtil.isNotEmpty(status)){
			condition.getFilterObject().addCondition("status", "eq", status);
		}
		String createTimeStart = condition.getStringCondition("createTimeStart");
		if(StringUtil.isNotEmpty(createTimeStart)){
			condition.getFilterObject().addCondition("createdAt", ">=", createTimeStart);
		}
		String createTimeEnd = condition.getStringCondition("createTimeEnd");
		if(StringUtil.isNotEmpty(createTimeEnd)){
			condition.getFilterObject().addCondition("createdAt", "<=", createTimeEnd);
		}
		String erpOrderCode = condition.getStringCondition("erpOrderCode");
		if(StringUtil.isNotEmpty(erpOrderCode)){
			condition.getFilterObject().addCondition("erpOrderCode", "like", "%"+erpOrderCode+"%");
		}
		String invoicePrintTimeBegin = condition.getStringCondition("invoicePrintTimeBegin");
		if(StringUtil.isNotEmpty(invoicePrintTimeBegin)){
			condition.getFilterObject().addCondition("invoicePrintTime", ">=", invoicePrintTimeBegin);
		}
		String invoicePrintTimeEnd = condition.getStringCondition("invoicePrintTimeEnd");
		if(StringUtil.isNotEmpty(invoicePrintTimeEnd)){
			condition.getFilterObject().addCondition("invoicePrintTime", "<=", invoicePrintTimeEnd);
		}
		//如果是物流公司
		if(userObject != null && !userObject.isInner() 
				&& "WL".equals(userObject.getOrg().getOptTxt5())){
			LovMember org = userObject.getOrg();
			condition.getFilterObject().addCondition("logisticsCompany", "=", org.getCode());
		}else{
			String logisticsCompany = condition.getStringCondition("logisticsCompany");
			if(StringUtil.isNotEmpty(logisticsCompany)){
				condition.getFilterObject().addCondition("logisticsCompany", "eq", logisticsCompany);
			}
		}
		//只显示主表
		condition.getFilterObject().addCondition("isMain", "eq", IConstants.YES);
		condition.getFilterObject().addCondition("deleteFlag", "!=", IConstants.YES);
	}

}