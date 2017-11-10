package com.ibm.kstar.impl.order;


import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.order.IDeliveryService;
import com.ibm.kstar.api.order.IInvoiceService;
import com.ibm.kstar.api.order.IOrderService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.team.ITeamService;
import com.ibm.kstar.entity.order.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
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
import org.xsnake.xflow.api.IProcessService;
import org.xsnake.xflow.api.Participant;
import org.xsnake.xflow.api.workflow.IXflowProcessServiceWrapper;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Remote
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class InvoiceServiceImpl implements IInvoiceService {
	@Autowired
	BaseDao baseDao;
	@Autowired
	IDeliveryService deliveryService;
	@Autowired
	IOrderService orderService;
	@Autowired
	ITeamService teamService;
	@Autowired
	IXflowProcessServiceWrapper xflowProcessServiceWrapper;
	@Autowired
	ILovMemberService lovMemberService;
	@Autowired
	IProcessService processService;

	@Override
	public void saveInvoice(InvoiceMaster invoiceMaster,UserObject userObject) throws Exception{
		if(IConstants.INVOICETYPE_02.equals(invoiceMaster.getInvoiceType()) ){
			//获取当前登录人员销售中心
			String center =lovMemberService.getSaleCenter(userObject.getOrg().getId());
			if(!"P_GJORG_B1_0001".equals(center)){//国内
				if(StringUtil.isEmpty(invoiceMaster.getTaxNo()) 
						|| StringUtil.isEmpty(invoiceMaster.getAddress())){
					throw new AnneException(IInvoiceService.class.getName()
							+ " saveInvoice : 客户地址、税号不能为空");
				}
			}
		}else if(IConstants.INVOICETYPE_01.equals(invoiceMaster.getInvoiceType()) ) {
			if(StringUtil.isEmpty(invoiceMaster.getTelno()) 
					|| StringUtil.isEmpty(invoiceMaster.getTaxNo())
					|| StringUtil.isEmpty(invoiceMaster.getBankact())
					|| StringUtil.isEmpty(invoiceMaster.getActno())
					|| StringUtil.isEmpty(invoiceMaster.getAddress())
					){
				throw new AnneException(IInvoiceService.class.getName()
						+ " saveInvoice : 客户地址、电话、税号、开户行、账号不能为空");
			}
		}
		// 创建字段
		invoiceMaster.setCreatedById(userObject.getEmployee().getId());
		invoiceMaster.setCreatedAt(new Date());
		invoiceMaster.setCreatedPosId(userObject.getPosition().getId());
		invoiceMaster.setCreatedOrgId(userObject.getOrg().getId());
		// 更新字段
		invoiceMaster.setUpdatedById(userObject.getEmployee().getId());
		invoiceMaster.setUpdatedAt(new Date());
		baseDao.save(invoiceMaster);
		//已发货开票校验
		checkInvoice(invoiceMaster.getId());
		//更新所有行
		this.saveOrUpdateInvoiceLines(invoiceMaster,userObject);
		//保存金税明细行
		this.saveOrUpdateGoldenTax(invoiceMaster,userObject);
		
		teamService.addPosition(userObject.getPosition().getId(),userObject.getEmployee().getId(), 
				IConstants.PERMISSION_BUSINESS_TYPE_INVOICE,invoiceMaster.getId());
	}

	@Override
	public void updateInvoice(InvoiceMaster invoiceMaster,UserObject userObject)
			throws Exception {
		InvoiceMaster oldInvoiceMaster = baseDao.get(InvoiceMaster.class,
				invoiceMaster.getId());
		if (oldInvoiceMaster == null) {
			throw new AnneException(IInvoiceService.class.getName()
					+ " updateInvoice : 没有找到要更新的数据");
		}
		if(StringUtil.isEmpty(invoiceMaster.getStatus())){
			invoiceMaster.setStatus(null);
		}
		if(IConstants.INVOICETYPE_02.equals(invoiceMaster.getInvoiceType()) ){
			//获取当前登录人员销售中心
			String center =lovMemberService.getSaleCenter(oldInvoiceMaster.getCreatedOrgId());
			if(!"P_GJORG_B1_0001".equals(center)){//国内
				if(StringUtil.isEmpty(invoiceMaster.getTaxNo())
						|| StringUtil.isEmpty(invoiceMaster.getAddress())){
					throw new AnneException(IInvoiceService.class.getName()
							+ " updateInvoice : 客户地址、税号不能为空");
				}
			}
		}else if(IConstants.INVOICETYPE_01.equals(invoiceMaster.getInvoiceType()) ) {
			if(StringUtil.isEmpty(invoiceMaster.getTelno()) 
					|| StringUtil.isEmpty(invoiceMaster.getTaxNo())
					|| StringUtil.isEmpty(invoiceMaster.getBankact())
					|| StringUtil.isEmpty(invoiceMaster.getActno())
					|| StringUtil.isEmpty(invoiceMaster.getAddress())
					){
				throw new AnneException(IInvoiceService.class.getName()
						+ " updateInvoice : 客户地址、电话、税号、开户行、账号不能为空");
			}
		}
		
		String status = invoiceMaster.getStatus();
		
		BeanUtils.copyPropertiesIgnoreNull(invoiceMaster, oldInvoiceMaster);
		oldInvoiceMaster.setUpdatedAt(new Date());
		oldInvoiceMaster.setUpdatedById(userObject.getEmployee().getId());
		baseDao.update(oldInvoiceMaster);
		
		if(IConstants.ORDER_CONTROL_STATUS_20.equals(status)){
			this.updateStatus(oldInvoiceMaster.getId(), oldInvoiceMaster.getStatus(), userObject);
		}
		//更新所有行
		this.saveOrUpdateInvoiceLines(invoiceMaster,userObject);
		//保存金税明细行
		this.saveOrUpdateGoldenTax(invoiceMaster,userObject);
	}

	@Override
	public IPage queryInvoiceMasters(PageCondition condition)
			throws AnneException {
		FilterObject filterObject = condition.getFilterObject(InvoiceMaster.class);
		filterObject.addOrderBy("updatedAt", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}

	@Override
	public void deleteInvoice(String invoiceMasterId,UserObject userObject)
			throws Exception {
		InvoiceMaster invoiceMaster = baseDao.get(InvoiceMaster.class, invoiceMasterId);
		if(invoiceMaster == null){
			throw new AnneException("没有找到开票申请！");
		}
		if(!IConstants.ORDER_CONTROL_STATUS_10.equals(invoiceMaster.getStatus())
				&& !IConstants.ORDER_CONTROL_STATUS_40.equals(invoiceMaster.getStatus())){
			throw new AnneException("开票申请状态不支持删除！");
		}
		//删除开票行
		this.deleteInvoiceDetailByHid(invoiceMasterId,userObject);
		if(IConstants.ORDER_CONTROL_STATUS_40.equals(invoiceMaster.getStatus())){
			String invoice_audit_app = lovMemberService.getFlowCodeByAppCode(IConstants.ORDER_AUDIT_FLOW_APP_INVOICE_AUDIT);
			processService.closeByBusinessKey(invoice_audit_app, invoiceMaster.getId(),userObject.getParticipant(), "开票申请删除");
		}
		//删除开票头
		baseDao.delete(invoiceMaster);
	}


	@Override
	public void deleteInvoiceDetailByHid(String invoiceMasterId,UserObject userObject) throws Exception {
		
		String hql = "from InvoiceDetail line where line.invoiceId = ?";
		List<InvoiceDetail> invoiceDetails = baseDao.findEntity(hql, new Object[]{invoiceMasterId});
		for(InvoiceDetail detail : invoiceDetails){
			if(IConstants.INVOICE_TYPE_01.equals(detail.getInvoiceType())){
				//如果是提前开票，删除发开票时将订单改为未提前开票
				orderService.updateOrderLinesInvoiceStatus(detail.getOrderLineId(), IConstants.NO_No,userObject);
			}else if(IConstants.INVOICE_TYPE_02.equals(detail.getInvoiceType())){
				//如果是发货后开票，删除开票时更新发货单的开票数量
				deliveryService.updateInvoiceQty(detail.getDeliveryLineId(), 0-detail.getInvoiceQty(),userObject);
				//2.更新订单开票行数据
				orderService.updateOrderInvoiceQty(detail.getOrderLineId(), 0-detail.getInvoiceQty(),userObject);
			}
			baseDao.delete(detail);
		}
	}
	
	@Override
	public InvoiceMaster getInvoiceMaster(String invoiceMasterId)
			throws AnneException {
		return baseDao.get(InvoiceMaster.class, invoiceMasterId);
	}
	@Override
	public InvoiceMaster getInvoiceMasterByCode(String invoiceCode)
			throws AnneException {
		String hql = " from InvoiceMaster m where m.invoiceCode = ? ";
		return baseDao.findUniqueEntity(hql, new Object[]{invoiceCode});
	}
	@Override
	public IPage queryInvoiceDetail(PageCondition condition) {
		
		FilterObject filterObject = condition.getFilterObject(InvoiceDetail.class);
		filterObject.addOrderBy("id", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}
	@Override
	public List<InvoiceDetail> getInvoiceDetailListByMId(String invoiceMasterId,String invoiceType) {
		StringBuffer hql = new StringBuffer("from InvoiceDetail line where line.invoiceId = ? ");
		List<Object> args = new ArrayList<>();
		args.add(invoiceMasterId);
		if(StringUtil.isNotEmpty(invoiceType)){
			hql.append(" and line.invoiceType = ? ");
			args.add(invoiceType);
		}
		return  baseDao.findEntity(hql.toString(), args.toArray());
	}
	
	@Override
	public List<InvoiceDetail> getInvoiceDetailListByMCode(String invoiceMasterCode) {
		String hql = "from InvoiceDetail line where line.invoiceCode = ?";
		return  baseDao.findEntity(hql, new Object[]{invoiceMasterCode});
	}
	
	
	@Override
	public List<InvoiceGoldenTax> getInvoiceGoldenTaxListByMCode(String invoiceCode) {
		String hql = "from InvoiceGoldenTax line where line.invoiceCode = ?";
		return  baseDao.findEntity(hql, new Object[]{invoiceCode});
	}
	
	@Override
	public List<InvoiceDetail> getInvoiceDetailListByOrderLineId(String orderLineId) {
		String hql = "from InvoiceDetail line where line.orderLineId = ? ";
		return  baseDao.findEntity(hql, new Object[]{orderLineId});
	}
	
	
	@Override
	public List<List<Object>> getTaxListExcelData(List<InvoiceGoldenTax> taxList) {
		List<List<Object>> lstOut = new ArrayList<List<Object>>();
		List<Object> lstHead = new ArrayList<Object>();
		
		lstHead.add("发票单号");
		lstHead.add("客户名称");
		lstHead.add("品名");
		lstHead.add("规格型号");
		lstHead.add("单位");
		lstHead.add("数量");
		lstHead.add("单价");
		lstHead.add("金额");
		lstHead.add("备注");
		lstOut.add(lstHead);
		for(InvoiceGoldenTax tax : taxList){
			
			List<Object> lstIn = new ArrayList<Object>();
			lstIn.add(tax.getInvoiceNo());
			lstIn.add(tax.getCustName());
			lstIn.add(tax.getMaterielDesc());
			lstIn.add(tax.getProModel());
			lstIn.add(tax.getUnit());
			lstIn.add(tax.getInvoiceQty());
			lstIn.add(tax.getInvoicePrice());
			lstIn.add(tax.getInvoiceAmount());
			lstIn.add(tax.getRemarks());
			lstOut.add(lstIn);
		}
		
		
		
		return lstOut;
	}
	
	
	
	@Override
	public IPage queryInvoiceGoldenTax(PageCondition condition) {
		FilterObject filterObject = condition.getFilterObject(InvoiceGoldenTax.class);
		filterObject.addOrderBy("updatedAt", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}
	
	/**
	 * 
	 * saveOrUpdateInvoiceLines:(保存/更新开票申请明细). <br/> 
	 * TODO(这里描述这个方法适用条件 – 可选).<br/> 
	 * 
	 * @author liming 
	 * @param invoiceMaster
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	public void saveOrUpdateInvoiceLines(InvoiceMaster  invoiceMaster,UserObject userObject) throws Exception  {
		List<Map<Object, Object>> detailList = invoiceMaster.getDetailList();
		
		List<Object> list = new ArrayList<Object>();
		if(list != null){
			for(Map<Object, Object> map  :  detailList){
				list.add(map.get("id"));
			}
		}
		
		String hql = " from InvoiceDetail d where d.invoiceId = ? ";
	    Object[] args = {invoiceMaster.getId()};
		List<InvoiceDetail> details = baseDao.findEntity(hql, args);
		//行数据不在页面返回的集合中，则将其删除
		for(InvoiceDetail detail : details){
			if(list == null || list.size() <= 0 || !list.contains(detail.getId())){
				if(IConstants.INVOICE_TYPE_01.equals(detail.getInvoiceType())){
					//如果是提前开票，删除发开票时将订单改为未提前开票
					orderService.updateOrderLinesInvoiceStatus(detail.getOrderLineId(), IConstants.NO_No,userObject);
				}else if(IConstants.INVOICE_TYPE_02.equals(detail.getInvoiceType())){
					//如果是发货后开票，
					//1.删除开票时更新发货单的开票数量
					deliveryService.updateInvoiceQty(detail.getDeliveryLineId(), 0-detail.getInvoiceQty(),userObject);
					//2.更新订单开票行数据
					orderService.updateOrderInvoiceQty(detail.getOrderLineId(), 0-detail.getInvoiceQty(),userObject);
				}
				baseDao.deleteById(InvoiceDetail.class, detail.getId());
			}
		}
		
		if( detailList != null){
			for (Map<Object, Object> map : detailList) {
				//将Map集合转成对象
				InvoiceDetail invoiceDetail = (InvoiceDetail) BeanUtils.convertMap(InvoiceDetail.class, map);
				if(invoiceDetail != null){
					
					invoiceDetail.setInvoiceId(invoiceMaster.getId());
					invoiceDetail.setInvoiceCode(invoiceMaster.getInvoiceCode());
					InvoiceDetail oldInvoiceDetail;
					
					if(StringUtils.isEmpty(invoiceDetail.getId())){
						oldInvoiceDetail = invoiceDetail;
					}else{
						oldInvoiceDetail  = baseDao.get(InvoiceDetail.class,invoiceDetail.getId());
						if (oldInvoiceDetail == null) {
							invoiceDetail.setId(null);
							oldInvoiceDetail = invoiceDetail;
						}else{
							//将 invoiceDetail的属性复制到 oldInvoiceDetail
							BeanUtils.copyPropertiesIgnoreNull(invoiceDetail, oldInvoiceDetail);
						}
					}
					if(StringUtils.isEmpty(oldInvoiceDetail.getId())){
						if(IConstants.INVOICE_TYPE_01.equals(oldInvoiceDetail.getInvoiceType())){
							//如果是提前开票，将订单改为提前开票
							orderService.updateOrderLinesInvoiceStatus(oldInvoiceDetail.getOrderLineId(), IConstants.YES_Yes,userObject);
						}else if(IConstants.INVOICE_TYPE_02.equals(oldInvoiceDetail.getInvoiceType())){
							//如果是发货后开票
							//1.更新发货单的开票数量
							deliveryService.updateInvoiceQty(oldInvoiceDetail.getDeliveryLineId(), oldInvoiceDetail.getInvoiceQty(),userObject);
							//2.更新订单开票行数据
							orderService.updateOrderInvoiceQty(oldInvoiceDetail.getOrderLineId(),oldInvoiceDetail.getInvoiceQty(),userObject);
						}
						oldInvoiceDetail.setCreateTime(new Date());
						oldInvoiceDetail.setCreator(userObject.getEmployee().getId());
					}
					oldInvoiceDetail.setUpdatedAt(new Date());
					oldInvoiceDetail.setUpdatedById(userObject.getEmployee().getId());
					baseDao.saveOrUpdate(oldInvoiceDetail);
					
					
				}
			}
		}
		
	}
	
	public void saveOrUpdateGoldenTax(InvoiceMaster  invoiceMaster,UserObject userObject) throws Exception  {
		List<Map<Object, Object>> goldenTaxList = invoiceMaster.getGoldenTaxList();
		List<Object> list = new ArrayList<Object>();
		if(list != null){
			for(Map<Object, Object> map  :  goldenTaxList){
				list.add(map.get("id"));
			}
		}
		String hql = " from InvoiceGoldenTax d where d.invoiceId = ? ";
	    Object[] args = {invoiceMaster.getId()};
		List<InvoiceGoldenTax> goldenTaxs = baseDao.findEntity(hql, args);
		//行数据不在页面返回的集合中，则将其删除
		for(InvoiceGoldenTax goldenTax : goldenTaxs){
			if(list == null || list.size() <= 0 || !list.contains(goldenTax.getId())){
				baseDao.deleteById(InvoiceGoldenTax.class, goldenTax.getId());
			}
		}
		if( goldenTaxList != null){
			for (Map<Object, Object> map : goldenTaxList) {
				//将Map集合转成对象
				InvoiceGoldenTax goldenTax = (InvoiceGoldenTax) BeanUtils.convertMap(InvoiceGoldenTax.class, map);
				if(goldenTax != null){
					goldenTax.setInvoiceId(invoiceMaster.getId());
					goldenTax.setInvoiceCode(invoiceMaster.getInvoiceCode());
					InvoiceGoldenTax oldGoldenTax;
					if(StringUtils.isEmpty(goldenTax.getId())){
						oldGoldenTax = goldenTax;
					}else{
						oldGoldenTax  = baseDao.get(InvoiceGoldenTax.class,goldenTax.getId());
						if (oldGoldenTax == null) {
							goldenTax.setId(null);
							oldGoldenTax = goldenTax;
						}else{
							BeanUtils.copyPropertiesIgnoreNull(goldenTax, oldGoldenTax);
						}
					}
					oldGoldenTax.setUpdatedAt(new Date());
					oldGoldenTax.setUpdatedById(userObject.getEmployee().getId());
					baseDao.saveOrUpdate(oldGoldenTax);
				}
			}
		}
	}
	
	/**
	 * updateStatus:修改开票申请状态. <br/> 
	 * 
	 * @author liming 
	 * @param id 开票申请ID
	 * @param status 状态
	 * @since JDK 1.7
	 */
	@Override
	public void updateStatus(String id,String status,UserObject userObject) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		InvoiceMaster invoiceMaster = baseDao.get(InvoiceMaster.class,id);
		if (invoiceMaster == null) {
			throw new AnneException(IInvoiceService.class.getName()
					+ " updateStatus : 没有找到要更新的数据");
		}
		
		invoiceMaster.setStatus(status);
		invoiceMaster.setUpdatedAt(new Date());
		if(userObject != null){
			invoiceMaster.setUpdatedById(userObject.getEmployee().getId());
		}
		
		//审批通过时，设置审批日期
		if(IConstants.ORDER_CONTROL_STATUS_30.equals(status)){
			invoiceMaster.setInvoiceDate(new Date());
		}
		
		baseDao.update(invoiceMaster);
		
		if(IConstants.ORDER_CONTROL_STATUS_20.equals(status) && userObject != null){
			StringBuffer sb  = this.checkERPShipped(id);
			if(sb.toString().length()>0){
				throw new AnneException("提交失败，ERP未过账，不允许提交！未过帐的订单行号如下:"+sb.toString());
			}
			//已发货开票校验
			checkInvoice(id);
			
			//如果是审核中-20 启动 开票申请审批 工作流
			if(userObject != null){
				Map<String, String> varmap = new HashMap<String, String>();
				varmap.put("title", "开票申请审批 - " + invoiceMaster.getInvoiceCode()+"|"+userObject.getEmployee().getName()+"|"+ sdf.format(new Date()));
				varmap.put("app", IConstants.ORDER_AUDIT_FLOW_APP_INVOICE_AUDIT);
				String salesCenter = lovMemberService.getSaleCenter(invoiceMaster.getCreatedOrgId());
				varmap.put("SalesCenter",salesCenter);
				varmap.put("AdvanceInvoice",this.isAdvanceInvoice(invoiceMaster.getId()));
				String employeeType ="";
				if(userObject != null && userObject.getOrg() != null){
					employeeType = userObject.getOrg().getOptTxt3();
				}
				varmap.put("EmployeeType", employeeType);
				varmap.put("employeeIdInForm",invoiceMaster.getBusinessManagerId());
				varmap.put("employeeNameInForm",invoiceMaster.getBusinessManagerName());
				varmap.put("positionCode", userObject.getPosition().getCode());
				varmap.put("DVComfirmed", "Y");
				
				//添加参数"业务实体"
				String businessEntity ="";
				LovMember lovMember =   lovMemberService.get(invoiceMaster.getBusinessEntity());
				if(lovMember != null){
					businessEntity = lovMember.getCode();
				}
				varmap.put("businessEntity", businessEntity);
				
				String invoice_audit_app = lovMemberService.getFlowCodeByAppCode(IConstants.ORDER_AUDIT_FLOW_APP_INVOICE_AUDIT);
				xflowProcessServiceWrapper.start(invoice_audit_app, id, userObject, varmap);
			}
		}
		
	}
	/**
	 * 
	 * isAdvanceInvoice:是否存在提前开票的行. <br/> 
	 * 
	 * @author liming 
	 * @param id 开票申请主表ID
	 * @return Y：是，N：否
	 * @since JDK 1.7
	 */
	private String isAdvanceInvoice(String id) {
		String isAdvanceInvoice = "N";
		List<InvoiceDetail> invoiceDetails  = this.getInvoiceDetailListByMId(id,IConstants.INVOICE_TYPE_01);
		if(invoiceDetails != null && invoiceDetails.size() >0 ){
			isAdvanceInvoice = "Y";
		}
		return isAdvanceInvoice;
	}
	/**
	 * 
	 * checkInvoiceStatus:检查开票单是否已经审核. <br/> 
	 * 
	 * @author liming 
	 * @param orderLineId
	 * @return 
	 * @since JDK 1.7
	 */
	@Override
	public boolean checkInvoiceErpStatusForOrderSplitLine(String orderLineId) {
		List<InvoiceDetail> invoiceDetails = this.getInvoiceDetailListByOrderLineId(orderLineId);
		if(invoiceDetails == null || invoiceDetails.size() <= 0){
			return true;
		}
		for(InvoiceDetail detail : invoiceDetails){
			if(!IConstants.YES_Yes.equals(detail.getErpImportFlag())){
				return false;
			}
		}
		return true;
	}

	
	/**
	 * 已发货未开票明细Excel导出
	 * @param ids
	 * @return
	 * @throws AnneException
	 */
	@Override
	public List<List<Object>> exportSelectedContrLists(String[] ids) throws AnneException {
		List<List<Object>> lstOut = new ArrayList<List<Object>>();
		addHead(lstOut);
		List<InvoiceDetail> invoiceDetails = getSelectedContrList(ids);
		for (InvoiceDetail invoiceDetail : invoiceDetails) {
			List<Object> lstIn = new ArrayList<Object>();
			lstIn.add(invoiceDetail.getDeliveryCode()); 
			lstIn.add(invoiceDetail.getOrderCode());
			lstIn.add(invoiceDetail.getCustCode());
			lstIn.add(invoiceDetail.getCustName());
			lstIn.add(invoiceDetail.getCustAddr());
			lstIn.add(invoiceDetail.getCustPO());
			lstIn.add(invoiceDetail.getMaterielCode());
			lstIn.add(invoiceDetail.getMaterielDesc());
			lstIn.add(invoiceDetail.getProModel());
			lstIn.add(invoiceDetail.getDeliveryDate());
			lstIn.add(invoiceDetail.getInvoiceQty());
			lstIn.add(invoiceDetail.getInvoicePrice());
			lstIn.add(invoiceDetail.getUnitLable());
			lstIn.add(invoiceDetail.getInvoiceAmount());
			lstIn.add(invoiceDetail.getRemarks());
			lstOut.add(lstIn);
		}
		return lstOut;
	}

	private List<InvoiceDetail> getSelectedContrList(String[] ids) {
		String idsStr = "";
		for(String id : ids){
			idsStr += "'" + id + "',";
		}
		idsStr= idsStr.substring(0, idsStr.length()-1); 
		StringBuffer hql = new StringBuffer(" from InvoiceDetail where 1 = 1 and id in (" +idsStr+")");
		return baseDao.findEntity(hql.toString());
	}

	private void addHead(List<List<Object>> lstOut) {
		List<Object> lstHead = new ArrayList<Object>();
		lstHead.add("出货单编号");
		lstHead.add("订单编号");
		lstHead.add("客户编号");
		lstHead.add("客户名称");
		lstHead.add("客户地址");
		lstHead.add("客户PO");
		lstHead.add("物料编码");
		lstHead.add("物料说明");
		lstHead.add("产品型号");
		lstHead.add("出货日期");
		lstHead.add("开票数量");
		lstHead.add("开票单价");
		lstHead.add("单位");
		lstHead.add("开票金额");
		lstHead.add("备注");
		lstOut.add(lstHead);
		
	}
	
	/**
	 * 提前开票明细Excel导出
	 * @param ids
	 * @return
	 * @throws AnneException
	 */
	@Override
	public List<List<Object>> exportInvoiceLinesFormLists(String[] ids) throws AnneException {
		List<List<Object>> lstOut = new ArrayList<List<Object>>();
		addTitle(lstOut);
		List<InvoiceDetail> invoiceDetails = getSelectedContrList(ids);
		for (InvoiceDetail invoiceDetail : invoiceDetails) {
			List<Object> lstIn = new ArrayList<Object>();
			lstIn.add(invoiceDetail.getOrderCode());
			lstIn.add(invoiceDetail.getCustCode());
			lstIn.add(invoiceDetail.getCustName());
			lstIn.add(invoiceDetail.getCustAddr());
			lstIn.add(invoiceDetail.getCustPO());
			lstIn.add(invoiceDetail.getMaterielCode());
			lstIn.add(invoiceDetail.getMaterielDesc());
			lstIn.add(invoiceDetail.getProModel());
			lstIn.add(invoiceDetail.getDeliveryDate());
			lstIn.add(invoiceDetail.getInvoiceQty());
			lstIn.add(invoiceDetail.getUnitLable());
			lstIn.add(invoiceDetail.getInvoicePrice());
			lstIn.add(invoiceDetail.getInvoiceAmount());
			lstIn.add(invoiceDetail.getRemarks());
			lstOut.add(lstIn);
		}
		return lstOut;
	}

	private void addTitle(List<List<Object>> lstOut) {
		List<Object> lstHead = new ArrayList<Object>();
		lstHead.add("订单编号");
		lstHead.add("客户编号");
		lstHead.add("客户名称");
		lstHead.add("客户地址");
		lstHead.add("客户PO");
		lstHead.add("物料编码");
		lstHead.add("物料说明");
		lstHead.add("产品型号");
		lstHead.add("出货日期");
		lstHead.add("开票数量");
		lstHead.add("单位");
		lstHead.add("开票单价");
		lstHead.add("开票金额");
		lstHead.add("备注");
		lstOut.add(lstHead);
	}

	
	public void setBusinessVariable(String businessKey,String processInstanceId,Participant participant ){
		InvoiceMaster invoiceMaster = baseDao.get(InvoiceMaster.class,businessKey);
		Map<String, String> varmap = new HashMap<String, String>();
		String businessEntity ="";
		LovMember lovMember =   lovMemberService.get(invoiceMaster.getBusinessEntity());
		if(lovMember != null){
			businessEntity = lovMember.getCode();
		}
		varmap.put("businessEntity", businessEntity);
		processService.setBusinessVariable(processInstanceId,participant, varmap);
	}
	/**
	 * 
	 * checkERPShipped:校验开票申请对应的订单ERP是否已发货. <br/> 
	 * 
	 * @author liming 
	 * @param invoiceId 发票头ID
	 * @return 
	 * @since JDK 1.7
	 */
	private StringBuffer checkERPShipped(String invoiceId) {
		//String erpShipped = "Y";
		StringBuffer sb = new StringBuffer();
		List<InvoiceDetail> invoiceDetails = this.getInvoiceDetailListByMId(invoiceId,IConstants.INVOICE_TYPE_02);
		if(invoiceDetails==null||invoiceDetails.size()==0){
			return sb;
		}
		for(InvoiceDetail invoiceDetail : invoiceDetails){
			String orderId = invoiceDetail.getOrderLineId();
			if(StringUtils.isEmpty(orderId)){
				continue;
			}
			
			OrderLinesView orderLines = baseDao.get(OrderLinesView.class, orderId);
			if(orderLines==null){
				continue;
			}
			
			if(!IConstants.YES_Yes.equals(orderLines.getIsErpDelivery())){
				sb.append("单号"+orderLines.getOrderCode());
				sb.append("行号"+orderLines.getErpLineNo());
				sb.append(",");
			}
		}
		return sb;
	}

	/**
	 * 已发货开票校验
	 * @param id
	 */
	@Override
	public void checkInvoice(String id) {
		//首先根据businessKey查出CRM开票行得全部数据
		String sqlToDetail = " from InvoiceDetail t where 1=1 ";
		 	sqlToDetail += " and t.invoiceType = ? and t.invoiceId = ? ";
	    List<InvoiceDetail> details = baseDao.findEntity(sqlToDetail,new Object[]{IConstants.INVOICE_TYPE_02,id});
	    
	    //第二步根据开票行的订单行id查询对应CRM订单行的数量 
	    String sqlToOrder = " from OrderLinesView where id = ? ";
	    Double orderNum = 0d;//订单行总数量
	    Double inoviceNum = 0d;//开票总数量
	    
	    //第三步根据开票行的订单行ID查询出所有总开票数量
	    String sqlByOrderLine = " select nvl(sum(t.n_invoice_qty),0) from crm_t_invoice_detail t ";
	    	sqlByOrderLine += " where t.c_order_line_id = ? ";
	    
//	    //根据开票表头创建人判断是否为国际中心的人，来决定是否校验ERP可开票数量，（国际中心即在仓库没有可开票库存的情况下也能开票）
//    	InvoiceMaster oldInvoiceMaster = baseDao.get(InvoiceMaster.class,id);
	    if(details != null && details.size() > 0){
	    	for(InvoiceDetail iDetail : details){
	    		//获取订单行总数量
	    		OrderLinesView orderLine = baseDao.findUniqueEntity(sqlToOrder,new Object[]{iDetail.getOrderLineId()});
	    		orderNum = orderLine.getProQty();
	    		//获取同订单id下开票行的总开票数量
	    		BigDecimal sum = baseDao.findUniqueBySql(sqlByOrderLine,new Object[]{iDetail.getOrderLineId()});
	    		inoviceNum = sum.doubleValue();
	    		
	    		//CRM校验已发货开票行总数
	    		if(inoviceNum  > orderNum){
	    			throw new AnneException("订单编号为："+iDetail.getOrderCode()+"的物料号为："+iDetail.getMaterielCode()+" 的累计开票数量已大于订单数量，请联系系统管理员！");
	    		}
	    		//ERP校验已发货开票数量必须小于等于仓库实际可开票库存数量
	    		//获取当前登录人员销售中心
				//String center =lovMemberService.getSaleCenter(oldInvoiceMaster.getCreatedOrgId());
	    		if((iDetail.getInvoiceQty() > orderLine.getRequestedQty())){
	    			throw new AnneException("订单编号为："+iDetail.getOrderCode()+"的物料号为："+iDetail.getMaterielCode()+" 的本次开票数量已大于ERP可开票数量，请联系系统管理员！");
	    		}
	    	}
	    }		
	}

}