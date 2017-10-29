package com.ibm.kstar.impl.order;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.contract.IContrPayService;
import com.ibm.kstar.api.order.IContractReceiptDetailService;
import com.ibm.kstar.api.order.IDeliveryService;
import com.ibm.kstar.api.order.IOrderService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.team.ITeamService;
import com.ibm.kstar.entity.contract.ContrPay;
import com.ibm.kstar.entity.contract.Contract;
import com.ibm.kstar.entity.custom.vo.DeliveryHeaderWithAmount;
import com.ibm.kstar.entity.order.*;
import com.ibm.kstar.message.service.MessageAdapter;
import com.ibm.kstar.permission.utils.PermissionUtil;
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
import org.xsnake.web.page.PageImpl;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.StringUtil;
import org.xsnake.xflow.api.IProcessService;
import org.xsnake.xflow.api.workflow.IXflowProcessServiceWrapper;

import java.math.BigDecimal;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Remote
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class DeliveryServiceImpl extends MessageAdapter<String>  implements IDeliveryService {
	@Autowired
	BaseDao baseDao;
	@Autowired
	IOrderService orderService;
	@Autowired
	private IContrPayService contrPayService;
	@Autowired
	private ITeamService teamService;
	@Autowired
	IXflowProcessServiceWrapper xflowProcessServiceWrapper;
	@Autowired
	ILovMemberService lovMemberService;
	@Autowired
	IProcessService processService;

    @Autowired
    IContractReceiptDetailService receiptDetailService;

    @Override
	public void saveDelivery(DeliveryHeader deliveryHeader ,UserObject userObject) throws Exception {
		// 创建字段
		deliveryHeader.setCreatedById(userObject.getEmployee().getId());
		deliveryHeader.setCreatedAt(new Date());
		deliveryHeader.setCreatedPosId(userObject.getPosition().getId());
		deliveryHeader.setCreatedOrgId(userObject.getOrg().getId());
		// 更新字段
		deliveryHeader.setUpdatedById(userObject.getEmployee().getId());
		deliveryHeader.setUpdatedAt(new Date());
		baseDao.save(deliveryHeader);
		//保存发货单行
		this.saveOrUpdateDeliveryLines(deliveryHeader,userObject);
		//保存发货签收单
		this.saveOrUpdateDeliveryReceipt(deliveryHeader,userObject);
		
		teamService.addPosition(userObject.getPosition().getId(),userObject.getEmployee().getId(), 
				IConstants.PERMISSION_BUSINESS_TYPE_DELIVERY,deliveryHeader.getId());
	}

	@Override
	public void updateDelivery(DeliveryHeader deliveryHeader,UserObject userObject) throws Exception {
		DeliveryHeader oldDeliveryHeader = baseDao.get(DeliveryHeader.class,
				deliveryHeader.getId());
		if (oldDeliveryHeader == null) {
			throw new AnneException(IDeliveryService.class.getName()
					+ " saveDeliveryHeader : 没有找到要更新的数据");
		}
		String status = deliveryHeader.getStatus();
		BeanUtils.copyPropertiesIgnoreNull(deliveryHeader, oldDeliveryHeader);
		oldDeliveryHeader.setUpdatedAt(new Date());
		oldDeliveryHeader.setUpdatedById(userObject.getEmployee().getId());
		baseDao.update(oldDeliveryHeader);
		//保存发货单行
		this.saveOrUpdateDeliveryLines(deliveryHeader,userObject);
		//保存发货签收单
		this.saveOrUpdateDeliveryReceipt(deliveryHeader,userObject);
		//保存物流信息
		this.saveOrUpdateDeliveryLogistics(oldDeliveryHeader,userObject);
		
		if(IConstants.ORDER_CONTROL_STATUS_20.equals(status)){
			this.updateDeliveryStatus(oldDeliveryHeader.getId(), status, userObject);
		}
	}

	@Override
	public IPage queryDeliveryHeaders(PageCondition condition) {
		FilterObject filterObject = condition.getFilterObject(DeliveryHeader.class);
		filterObject.addOrderBy("updatedAt", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		StringBuffer sb = new StringBuffer(hqlObject.getHql());
		int i = sb.toString().indexOf("order by");
		String orderCode =condition.getStringCondition("orderCode");
		if(StringUtil.isNotEmpty(orderCode)){
			String hql =" and deliveryheader.id in( select t.deliveryId from DeliveryLines t where t.orderCode like '%"+orderCode+"%' ) "; 
			sb.insert(i, hql);
		}
		int j = sb.toString().indexOf("order by");
		String contrCode =condition.getStringCondition("contrCode");
		if(StringUtil.isNotEmpty(contrCode)){
			String hql =" and deliveryheader.id in( select distinct t.deliveryId from DeliveryLines t,OrderLines o where t.orderId=o.id and o.sourceCode like '%"+contrCode+"%' ) "; 
			sb.insert(j, hql);
		}
		return baseDao.search(sb.toString(), hqlObject.getArgs(),condition.getRows(), condition.getPage());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public IPage queryDeliveryHeadersTtl(PageCondition condition, UserObject user) {
		
		String customCode = condition.getStringCondition("customCode");
		condition.getFilterObject().addCondition("receCustomerId", "eq", customCode);
		
		condition.getFilterObject().addCondition("status", "eq", "30");
		
		List<Object> args = new ArrayList<Object>();
		args.add(customCode);
		String perHql= PermissionUtil.getPermissionHQL(null, "createdById", "createdPosId", "createdOrgId", "id", user, IConstants.PERMISSION_BUSINESS_TYPE_DELIVERY);
		String hql= " from DeliveryHeader where status = '30' and receCustomerId = ? and ";
		String orderBy = "order by updatedAt desc";
		
		IPage p = baseDao.search(hql + perHql + orderBy, args.toArray(), condition.getRows(), condition.getPage());
		
		List<DeliveryHeader> deliveryHeaders = (List<DeliveryHeader>)p.getList();
		List<DeliveryHeaderWithAmount> rtnList = new ArrayList<DeliveryHeaderWithAmount>();
		for (DeliveryHeader deliveryHeader : deliveryHeaders) {
			DeliveryHeaderWithAmount temp = new DeliveryHeaderWithAmount();
			BeanUtils.copyPropertiesIgnoreNull(deliveryHeader, temp);
			
			String id = deliveryHeader.getId();
			
			List<DeliveryLines> lines = getDeliveryLinesByHid(id);
			
			BigDecimal deliveryAmount = new BigDecimal(0);
			for (DeliveryLines line : lines) {
				if(line.getDeliveryAmount() != null) {
					deliveryAmount = deliveryAmount.add(line.getDeliveryAmount());
				}
			}
			temp.setDeliveryAmount(deliveryAmount);
			
			BigDecimal amount = new BigDecimal(0);
			String lineHql = "from VerificationDetail line where line.deliveryId = ?";
			List<VerificationDetail> verificationDetails = baseDao.findEntity(lineHql, new Object[]{id});
			for (VerificationDetail line : verificationDetails) {
				if(line.getAmount() != null) {
					amount = amount.add(line.getAmount());
				}
			}
			temp.setReceiveAmount(amount);
			
			temp.setBalance(deliveryAmount.subtract(amount));
			rtnList.add(temp);
		}
		((PageImpl)p).setList(rtnList);
		return p;
	}
	/**
	 * 逻辑删除发货单
	 * TODO 简单描述该方法的实现功能（可选）. 
	 * @see com.ibm.kstar.api.order.IDeliveryService#deleteDelivery(java.lang.String, com.ibm.kstar.api.system.permission.UserObject)
	 */
	@Override
	public void deleteDelivery(String deliveryHeaderId, UserObject userObject) throws Exception {
		//删除发货单头
		DeliveryHeader deliveryHeader = baseDao.get(DeliveryHeader.class, deliveryHeaderId);
		if(deliveryHeader ==null){
			throw new AnneException("没有找到出货申请！");
		}
		if(!IConstants.ORDER_CONTROL_STATUS_10.equals(deliveryHeader.getStatus())
				&& !IConstants.ORDER_CONTROL_STATUS_40.equals(deliveryHeader.getStatus())){
			throw new AnneException("出货申请状态不支持删除！");
		}
		//删除发货单行
		this.deleteDeliveryLinesByHid(deliveryHeaderId,userObject);
		//删除签收单
		this.deleteDeliveryReceiptByHid(deliveryHeader.getDeliveryCode(),userObject);
		deliveryHeader.setDeleteFlag(IConstants.YES);
		deliveryHeader.setUpdatedAt(new Date());
		
		if(userObject != null){
			deliveryHeader.setUpdatedById(userObject.getEmployee().getId());
		}
		if(IConstants.ORDER_CONTROL_STATUS_40.equals(deliveryHeader.getStatus())){
			String delivery_audit_app = lovMemberService.getFlowCodeByAppCode(IConstants.ORDER_AUDIT_FLOW_APP_DELIVERY_AUDIT);
			processService.closeByBusinessKey(delivery_audit_app, deliveryHeader.getId(),userObject.getParticipant(), "出货申请删除");
		}
		baseDao.update(deliveryHeader);
	}
	
	/**
	 * 删除发货明细
	 * TODO 简单描述该方法的实现功能（可选）. 
	 * @see com.ibm.kstar.api.order.IDeliveryService#deleteDeliveryLinesByHid(java.lang.String, com.ibm.kstar.api.system.permission.UserObject)
	 */
	@Override
	public void deleteDeliveryLinesByHid(String deliveryHeaderId,UserObject userObject) throws Exception {
		
		List<DeliveryLines> deliveryLines = this.getDeliveryLinesByHid(deliveryHeaderId);
		for(DeliveryLines lines : deliveryLines){
			//删除发货单时将订单改为未发货前的状态
			if(IConstants.DELIVERY_LINE_ERP_STATUS_STOCK.equals(lines.getErpStatus())){
				throw new AnneException("出货行在ERP已备货，不允许删除！");
			}
			orderService.updateOrderLinesStatus(lines.getOrderId(),IConstants.ORDER_LINE_STATUS_AWAITING_SHIPPING,userObject);
			lines.setDeleteFlag(IConstants.YES);
			lines.setUpdatedAt(new Date());
			lines.setErpImportFlag(IConstants.NO_No);
			if(userObject != null){
				lines.setUpdatedById(userObject.getEmployee().getId());
			}
			baseDao.update(lines);
		}
	}
	public void deleteDeliveryReceiptByHid(String deliveryCode,UserObject userObject) throws Exception {
		List<DeliveryReceipt> deliveryReceipts = this.getDeliveryReceiptListByDCode(deliveryCode);
		for(DeliveryReceipt receipt : deliveryReceipts){
			//删除发货单时将订单改为未发货前的状态
			receipt.setDeleteFlag(IConstants.YES);
			receipt.setUpdatedAt(new Date());
			receipt.setErpImportFlag(IConstants.NO_No);
			if(userObject != null){
				receipt.setUpdatedById(userObject.getEmployee().getId());
			}
			baseDao.update(receipt);
		}
	}

	@Override
	public DeliveryHeader getDeliveryHeaderById(String deliveryHeaderId) {
		return baseDao.get(DeliveryHeader.class, deliveryHeaderId);
	}
	
	@Override
	public DeliveryHeader getDeliveryHeaderByCode(String code) {
		String hql = "from DeliveryHeader h where h.deliveryCode = ? and h.deleteFlag != ? ";
		return baseDao.findUniqueEntity(hql,new Object[]{code,IConstants.YES});
	}
	
	
	@Override
	public IPage queryDeliveryLines(PageCondition condition) {
		FilterObject filterObject = condition.getFilterObject(DeliveryLines.class);
		filterObject.addOrderBy("updatedAt", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}
	
	@Override
	public IPage getDeliveryLinesByHQL(PageCondition condition) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<Object> args = new ArrayList<Object>();
		
		StringBuffer hql =  new StringBuffer("select distinct new com.ibm.kstar.entity.order.vo.DeliveryVO(header,lines,ol) "
				+ "from DeliveryHeader header , DeliveryLines lines , OrderLines ol, OrderHeader o , LovMember lov ");
		hql.append(" where header.id = lines.deliveryId ");
		hql.append(" and lines.orderCode = o.orderCode ");
		hql.append(" and lines.orderId = ol.id ");
		hql.append(" and o.salesmanCenter = lov.id ");
		hql.append(" and lines.deleteFlag != '1' ");
		hql.append(" and header.deleteFlag != '1' ");
		hql.append(" and ( (ol.isErpDelivery = 'Yes' and lov.code = '10') or lov.code = '20' ) ");
		hql.append(" and lov.groupCode = 'SALES_DEPARTMENT' ");
		hql.append(" and lov.level = 1 ");
		hql.append(" and ol.erpStatus != ? ");
		args.add(IConstants.ORDER_LINE_STATUS_CLOSED);
		
		//合同编号
		String contractCode = condition.getStringCondition("contractCode");
		if(StringUtil.isNotEmpty(contractCode)){
			hql.append(" and o.sourceCode = ? ");
			args.add(contractCode);
		}
		
		//是否提前开票
		String isAdvanceBilling =condition.getStringCondition("isAdvanceBilling");
		if(StringUtil.isNotEmpty(isAdvanceBilling)){
			if(IConstants.NO_No.equals(isAdvanceBilling)){
				hql.append(" and ol.isAdvanceBilling != ? ");
				args.add(IConstants.YES_Yes);
				hql.append(" and ol.proQty - ol.billingQty > 0 ");
			}else{
				hql.append(" and ol.isAdvanceBilling = ? ");
				args.add(isAdvanceBilling);
			}
		}
		
		//业务实体
		String businessEntity = condition.getStringCondition("businessEntity");
		if(StringUtil.isNotEmpty(businessEntity)){
			hql.append(" and header.businessEntity = ? ");
			args.add(businessEntity);
		}
		//模糊查询
		String customerCode = condition.getStringCondition("customerCode");
		hql.append(" and lines.singleCustCode = ?");	
		args.add(customerCode);
		
		String singleCustPO = condition.getStringCondition("singleCustPO");
		if(StringUtil.isNotEmpty(singleCustPO)){
			hql.append(" and lines.singleCustPO = ?");	
			args.add(singleCustPO);
		}
		
		String deliveryDateStart = condition.getStringCondition("deliveryDateStart");
		if(StringUtil.isNotEmpty(deliveryDateStart)){
			hql.append(" and lines.deliveryDate >= ?");	
			args.add(sdf.parse(deliveryDateStart));
		}
		String deliveryDateEnd = condition.getStringCondition("deliveryDateEnd");
		if(StringUtil.isNotEmpty(deliveryDateEnd)){
			hql.append(" and lines.deliveryDate  <= ?");	
			args.add(sdf.parse(deliveryDateEnd));
		}
		String orderCodeStart = condition.getStringCondition("orderCodeStart");
		if(StringUtil.isNotEmpty(orderCodeStart)){
			hql.append(" and lines.orderCode >= ?");	
			args.add(orderCodeStart);
		}
		String orderCodeEnd = condition.getStringCondition("orderCodeEnd");
		if(StringUtil.isNotEmpty(orderCodeEnd)){
			hql.append(" and lines.orderCode <= ?");	
			args.add(orderCodeEnd);
		}

		hql.append(" order by lines desc");
		
		String perHql= PermissionUtil.getPermissionHQL(null, "header.createdById", "header.createdPosId", "header.createdOrgId", "header.id", 
				(UserObject)condition.getCondition("userObject"), IConstants.PERMISSION_BUSINESS_TYPE_DELIVERY);
		hql.append(" and " +perHql);
		
		
		return baseDao.search(hql.toString(),args.toArray(), condition.getRows(), condition.getPage());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public DeliveryLogistics getLogisticByDeliveryCode(String deliveryCode) {
		List args = new ArrayList();
		StringBuffer hql = new StringBuffer(
				" from DeliveryLogistics r where r.deliveryCode = ? ");
		args.add(deliveryCode);
		return baseDao.findUniqueEntity(hql.toString(), args.toArray());
	}

	@Override
	public IPage getDeliveryReceiptByDCode(PageCondition condition) {
		String hql = "from DeliveryReceipt dr "
				+ " where dr.deleteFlag != ? "
				+ " and dr.deliveryCode = ?  "
				+ " order by dr.deliveryCode desc , dr.updatedAt desc";
		List<Object> args = new ArrayList<Object>();
		args.add(IConstants.YES);
		args.add(condition.getStringCondition("deliveryCode"));
		return baseDao.search(hql,args.toArray(), condition.getRows(), condition.getPage());
	}
	/**
	 * 
	 * getDeliveryReceiptListByDCode:根据出货单编号获取签收单. <br/> 
	 * 
	 * @author liming 
	 * @param deliveryCode
	 * @return 
	 * @since JDK 1.7
	 */
	@Override
	public List<DeliveryReceipt> getDeliveryReceiptListByDCode(String deliveryCode) {
		String hql = "from DeliveryReceipt dr where dr.deleteFlag != ? and dr.deliveryCode = ?";
		List<Object> args = new ArrayList<Object>();
		args.add(IConstants.YES);
		args.add(deliveryCode);
		return baseDao.findEntity(hql, args.toArray());
	}
	
	/**
	 * 
	 * saveOrUpdateDeliveryLines:(保存/更新发货单行). <br/> 
	 * TODO(这里描述这个方法适用条件 – 可选).<br/> 
	 * 
	 * @author liming 
	 * @param deliveryHeader
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	public void saveOrUpdateDeliveryLines(DeliveryHeader  deliveryHeader,UserObject userObject) throws Exception  {
		List<Map<Object, Object>> linesList = deliveryHeader.getLinesList();
		List<Object> list = new ArrayList<Object>();
		if(linesList != null){
			for(Map<Object, Object> map  :  linesList){
				list.add(map.get("id"));
			}
		}
		
		String hql = " from DeliveryLines d where d.deliveryCode = ? and deleteFlag != ? ";
	    Object[] args = {deliveryHeader.getDeliveryCode(),IConstants.YES};
		List<DeliveryLines> lines = baseDao.findEntity(hql, args);
		//行数据不在页面返回的集合中，则将其删除
		for(DeliveryLines deliveryLines : lines){
			if(list == null || list.size() <= 0 || !list.contains(deliveryLines.getId())){
				//删除发货单时将订单改为未发货前的状态
				if(IConstants.DELIVERY_LINE_ERP_STATUS_STOCK.equals(deliveryLines.getErpStatus())){
					throw new AnneException("出货行【"+deliveryLines.getLineNum()+"】在ERP已备货，不允许删除！");
				}
				orderService.updateOrderLinesStatus(deliveryLines.getOrderId(),IConstants.ORDER_LINE_STATUS_AWAITING_SHIPPING,userObject);
				deliveryLines.setDeleteFlag(IConstants.YES);
				deliveryLines.setErpImportFlag(IConstants.NO_No);
				deliveryLines.setUpdatedAt(new Date());
				if(userObject != null){
					deliveryLines.setUpdatedById(userObject.getEmployee().getId());
				}
				baseDao.update(deliveryLines);
			}
		}
		
		if( linesList != null){
			for (Map<Object, Object> map : linesList) {
				
				DeliveryLines deliveryLines = (DeliveryLines) BeanUtils.convertMap(DeliveryLines.class, map);
				if(deliveryLines != null){
					
					deliveryLines.setDeliveryCode(deliveryHeader.getDeliveryCode());
					
					DeliveryLines oldDeliveryLines;
					
					if(StringUtils.isEmpty(deliveryLines.getId())){
						oldDeliveryLines = deliveryLines;
					}else{
						//查询是否存在发货单行
						oldDeliveryLines  = baseDao.get(DeliveryLines.class,deliveryLines.getId());
						if (oldDeliveryLines == null) {
							deliveryLines.setId(null);
							oldDeliveryLines = deliveryLines;
						}else{
							//将 deliveryLines的属性复制到 oldDeliveryLines
							BeanUtils.copyPropertiesIgnoreNull(deliveryLines, oldDeliveryLines);
						}
					}
					if(StringUtils.isEmpty(oldDeliveryLines.getId())){
						//保存成功将订单行改为已发货
						orderService.updateOrderLinesStatus(oldDeliveryLines.getOrderId(),IConstants.ORDER_LINE_STATUS_PICKED,userObject);
						oldDeliveryLines.setCreateTime(new Date());
						oldDeliveryLines.setCreator(userObject.getEmployee().getId());
						oldDeliveryLines.setIsPostAccount(IConstants.NO_No);
						oldDeliveryLines.setDeleteFlag(IConstants.NO);
						//oldDeliveryLines.setErpImportFlag(IConstants.NO_No);
					}
					oldDeliveryLines.setDeliveryId(deliveryHeader.getId());
					oldDeliveryLines.setDeliveryCode(deliveryHeader.getDeliveryCode());
					oldDeliveryLines.setUpdatedAt(new Date());
					oldDeliveryLines.setUpdatedById(userObject.getEmployee().getId());
					
					//如果出货单编号（外部）为空
					if(StringUtil.isEmpty(oldDeliveryLines.getExternalNo())){
						oldDeliveryLines.setExternalNo(this.getExternalNo(oldDeliveryLines));
					}
					baseDao.saveOrUpdate(oldDeliveryLines);
					
				}
			}
		}
	}
	
	/**
	 * 
	 * saveOrUpdateDeliveryReceipt:更新/保存发货签收单. <br/> 
	 * TODO(这里描述这个方法适用条件 – 可选).<br/> 
	 * 
	 * @author liming 
	 * @param deliveryHeader
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	public void saveOrUpdateDeliveryReceipt(DeliveryHeader  deliveryHeader,UserObject userObject) throws Exception  {
		List<Map<Object, Object>> receiptList = deliveryHeader.getReceiptList();
		List<Object> list = new ArrayList<Object>();
		if(receiptList != null ){
			for(Map<Object, Object> map  :  receiptList){
				list.add(map.get("id"));
			}
		}
		String hql = " from DeliveryReceipt d where d.deliveryCode = ? and deleteFlag != ? ";
	    Object[] args = {deliveryHeader.getDeliveryCode(),IConstants.YES};
		List<DeliveryReceipt> receipts = baseDao.findEntity(hql, args);
		//行数据不在页面返回的集合中，则将其删除
		for(DeliveryReceipt receipt : receipts){
			if(list == null || list.size() <= 0 || !list.contains(receipt.getId())){
				receipt.setDeleteFlag(IConstants.YES);
				receipt.setUpdatedAt(new Date());
				receipt.setErpImportFlag(IConstants.NO_No);
				if(userObject != null){
					receipt.setUpdatedById(userObject.getEmployee().getId());
				}
				baseDao.update(receipt);
			}
		}
		
		if(receiptList != null){
			List<String> receiptCodes = new ArrayList<>();
			for (Map<Object, Object> map : receiptList) {
				DeliveryReceipt receipt = (DeliveryReceipt) BeanUtils.convertMap(DeliveryReceipt.class, map);
				if(receipt != null){
					receipt.setDeliveryCode(deliveryHeader.getDeliveryCode());
					DeliveryReceipt oldDeliveryReceipt =new DeliveryReceipt(); ;
					oldDeliveryReceipt.setDeliveryCode(deliveryHeader.getDeliveryCode());
					if(StringUtils.isEmpty(receipt.getId())){
						oldDeliveryReceipt = receipt;
					}else{
						//查询是否存在发货单行
						oldDeliveryReceipt  = baseDao.get(DeliveryReceipt.class,receipt.getId());
						if (oldDeliveryReceipt == null) {
							receipt.setId(null);
							oldDeliveryReceipt = receipt;
						}else{
							//将 deliveryLines的属性复制到 oldDeliveryLines
							BeanUtils.copyPropertiesIgnoreNull(receipt, oldDeliveryReceipt);
						}
					}
					if(StringUtils.isEmpty(oldDeliveryReceipt.getId())){
						
						String hql1 = "from DeliveryLines line where line.deliveryCode = ? and line.lineNum = ? and deleteFlag != ?";
						DeliveryLines deliveryLines = baseDao.findUniqueEntity(hql1, 
								new Object[]{oldDeliveryReceipt.getDeliveryCode(),oldDeliveryReceipt.getDeliveryLinesNum(),IConstants.YES});
						
						String receiptCode = "";
						String code = oldDeliveryReceipt.getReceiptCode();
						if(code != null){
							if(code.length() == 1){
								receiptCode = deliveryLines.getExternalNo()+"-0" + code;
							}else if(code.length() == 2 ){
								receiptCode = deliveryLines.getExternalNo()+"-" + code;
							}else {
								receiptCode = deliveryLines.getExternalNo()+"-" + code;
							}
						}
						oldDeliveryReceipt.setReceiptCode(receiptCode);
						// 创建字段
						oldDeliveryReceipt.setCreatedById(userObject.getEmployee().getId());
						oldDeliveryReceipt.setCreatedAt(new Date());
						oldDeliveryReceipt.setCreatedPosId(userObject.getPosition().getId());
						oldDeliveryReceipt.setCreatedOrgId(userObject.getOrg().getId());
						// 更新字段
						oldDeliveryReceipt.setUpdatedById(userObject.getEmployee().getId());
						oldDeliveryReceipt.setUpdatedAt(new Date());
						oldDeliveryReceipt.setStatus(IConstants.LOGISTICS_RECEIPT_STATUS_10);//未签收
						oldDeliveryReceipt.setDeleteFlag(IConstants.NO);
						oldDeliveryReceipt.setErpImportFlag(IConstants.NO_No);
						//设置主单
						if(!receiptCodes.contains(oldDeliveryReceipt.getReceiptCode())){
							oldDeliveryReceipt.setIsMain(IConstants.YES);
							receiptCodes.add(oldDeliveryReceipt.getReceiptCode());
						}else{
							oldDeliveryReceipt.setIsMain(IConstants.NO);
						}
						baseDao.save(oldDeliveryReceipt);
						teamService.addPosition(userObject.getPosition().getId(),userObject.getEmployee().getId(), 
								IConstants.PERMISSION_BUSINESS_TYPE_DELIVERYRECEIPT,oldDeliveryReceipt.getId());
					}else {
						// 更新字段
						oldDeliveryReceipt.setUpdatedById(userObject.getEmployee().getId());
						oldDeliveryReceipt.setUpdatedAt(new Date());
						baseDao.update(oldDeliveryReceipt);
					}
					receiptCodes.add(oldDeliveryReceipt.getReceiptCode());
				}
			}
		}
	}
	
	
	/**
	 * 
	 * saveOrUpdateDeliveryReceipt:更新/保存发货签收单. <br/> 
	 * TODO(这里描述这个方法适用条件 – 可选).<br/> 
	 * 
	 * @author liming 
	 * @param deliveryHeader
	 * @throws Exception 
	 * @since JDK 1.7
	 */
	public void saveOrUpdateDeliveryLogistics(DeliveryHeader  deliveryHeader,UserObject userObject) throws Exception  {
		
		DeliveryLogistics  logistics = deliveryHeader.getLogistics();
		if(logistics!= null){
			DeliveryLogistics oldDeliveryLogistics = baseDao.get(DeliveryLogistics.class,
					logistics.getId());
			if (oldDeliveryLogistics == null) {
				oldDeliveryLogistics = logistics;
				oldDeliveryLogistics.setId(null);
			}else{
				BeanUtils.copyPropertiesIgnoreNull(logistics, oldDeliveryLogistics);
			}
			oldDeliveryLogistics.setDeliveryCode(deliveryHeader.getDeliveryCode());
			oldDeliveryLogistics.setUpdatedAt(new Date());
			oldDeliveryLogistics.setUpdatedById(userObject.getEmployee().getId());
			baseDao.saveOrUpdate(oldDeliveryLogistics);
		}
	}
	
	@Override
	public void updateDeliveryStatus(String id,String status,UserObject userObject) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		DeliveryHeader deliveryHeader = baseDao.get(DeliveryHeader.class,id);
		if (deliveryHeader == null) {
			throw new AnneException("没有找到要更新的数据");
		}
		
		deliveryHeader.setStatus(status);
		deliveryHeader.setUpdatedAt(new Date());
		if(userObject != null){
			deliveryHeader.setUpdatedById(userObject.getEmployee().getId());
		}
		if(IConstants.ORDER_CONTROL_STATUS_30.equals(status)){
			try {
				Map<String, String> retMap  = this.deliveryAuditAfter(deliveryHeader.getDeliveryCode());
				if(!"S".equals(retMap.get("status"))){
					deliveryHeader.setSyncErpLog(retMap.get("msg"));
				}
			} catch (Exception e) {
				String msg = e.getMessage();
				if(msg != null && msg.length() >= 3000){
					msg = e.getMessage().substring(0, 2990);
				}
				deliveryHeader.setSyncErpLog(msg);
				e.printStackTrace();
			}
		}
		
		baseDao.update(deliveryHeader);
		
		
		if(IConstants.ORDER_CONTROL_STATUS_20.equals(status)){
			List<DeliveryReceipt> receipts = this.getDeliveryReceiptListByDCode(deliveryHeader.getDeliveryCode());
			if(receipts == null || receipts.size() <= 0){
				throw new AnneException("提交失败，签收单行不能为空");
			}
			//如果是审核中-20 启动发货单提交审核流
			if(userObject != null){
				Map<String, String> varmap = new HashMap<String, String>();
				varmap.put("title", "发货申请审批 - " + deliveryHeader.getDeliveryCode()+"|"+userObject.getEmployee().getName()+"|"+ sdf.format(new Date()));
				varmap.put("app", IConstants.ORDER_AUDIT_FLOW_APP_DELIVERY_AUDIT);
				String salesCenter = lovMemberService.getSaleCenter(deliveryHeader.getCreatedOrgId());
				varmap.put("SalesCenter",salesCenter);
				varmap.put("DTComfirmed",this.isDTComfirmed(deliveryHeader.getId()));
				varmap.put("employeeIdInForm",deliveryHeader.getBusinessManagerId());
				varmap.put("employeeNameInForm",deliveryHeader.getBusinessManagerName());
				varmap.put("positionCode", userObject.getPosition().getCode());
				
				String employeeType ="";
				if(userObject != null && userObject.getOrg() != null){
					employeeType = userObject.getOrg().getOptTxt3();
				}
				varmap.put("parentOrgId", userObject.getOrg().getParentId());
				varmap.put("EmployeeType", employeeType);
				
				String delivery_audit_app = lovMemberService.getFlowCodeByAppCode(IConstants.ORDER_AUDIT_FLOW_APP_DELIVERY_AUDIT);
				xflowProcessServiceWrapper.start(delivery_audit_app, id, userObject, varmap);
			}
		}
	}
	
	/**
	 * 
	 * updateInvoiceQty:更新发货行开票数量. <br/> 
	 * 
	 * @author liming 
	 * @param id
	 * @param invoiceQty 
	 * @since JDK 1.7
	 */
	@Override
	public void updateInvoiceQty(String id,double invoiceQty,UserObject userObject) {
		
		if(StringUtil.isNotEmpty(id)){
			DeliveryLines deliveryLines = baseDao.get(DeliveryLines.class, id);
			if (deliveryLines != null) {
				double qty = deliveryLines.getInvoiceQty();
				qty += invoiceQty;
				
				if(qty > deliveryLines.getDeliveryQty() || qty < 0){
					throw new AnneException(IDeliveryService.class.getName()
							+ " updateInvoiceQty : 开票总数量不能大于发货数量,且不能小于0");
				}
				deliveryLines.setInvoiceQty(qty);
				deliveryLines.setUpdatedAt(new Date());
				deliveryLines.setUpdatedById(userObject.getEmployee().getId());
				baseDao.update(deliveryLines);
			}
		}
	} 
	
	@Override
	public List<DeliveryLines> getDeliveryLinesByHid(String deliveryHeaderId){
		 String hql = "from DeliveryLines line where line.deliveryId = ? and deleteFlag != ? ";
		 return baseDao.findEntity(hql, new Object[]{deliveryHeaderId,IConstants.YES});
	}
	
	@Override
	public List<DeliveryLines> getDeliveryLinesByHCode(String deliveryCode){
		 String hql = "from DeliveryLines line where line.deliveryCode = ? and deleteFlag != ? ";
		 return baseDao.findEntity(hql, new Object[]{deliveryCode,IConstants.YES});
	}
	
	@Override
	public List<DeliveryLines> getDeliveryLinesByOrderLineId(String orderLineId){
		 String hql = "from DeliveryLines line where line.orderId = ? and deleteFlag != ? ";
		 return baseDao.findEntity(hql, new Object[]{orderLineId,IConstants.YES});
	}

	/**
	 * 
	 * TODO 生成合同收款计划明细. 
	 * @see com.ibm.kstar.api.order.IDeliveryService
	 * createContractReceiptDetail(java.lang.String, com.ibm.kstar.api.system.permission.UserObject)
	 */
	@Override
	public void createContractReceiptDetail(String code){
		if(StringUtil.isEmpty(code)){
			throw new AnneException("操作失败，出货单编码不能为空");
		}
		DeliveryHeader deliveryHeader =  this.getDeliveryHeaderByCode(code);
		if(deliveryHeader == null){
			throw new AnneException("操作失败，没有找到编码为【"+code+"】的出货单");
		}
		List<DeliveryLines> linelist = this.getDeliveryLinesByHid(deliveryHeader.getId());
		BigDecimal deliveryAmount = new BigDecimal(0);
		Map<String, BigDecimal> amountMap = new HashMap<String, BigDecimal>();
		Date printTime = null;
		if(linelist == null){
			return;
		}

		Map<String, OrderHeader> orderMap = new HashMap<String, OrderHeader>();
		for(DeliveryLines  deliveryLines : linelist){
			if(printTime == null && deliveryLines.getPrintTime() != null){
				printTime = deliveryLines.getPrintTime();
			}
			deliveryAmount.add(deliveryLines.getDeliveryAmount());
			OrderHeader orderHeader = orderService.queryOrderHeaderByCode(deliveryLines.getOrderCode());
			
			String sourceId = "sourceId";
			if(orderHeader != null && StringUtil.isNotEmpty(orderHeader.getSourceId())){
				sourceId = orderHeader.getSourceId();
			}
			//使用map传入OrderHeader对象
			orderMap.put(sourceId, orderHeader);
			if(amountMap.get(sourceId) == null ){
				//如果没有找到则新建
				amountMap.put(sourceId, deliveryLines.getDeliveryAmount());
			}else{
				BigDecimal amount = amountMap.get(sourceId);
				amountMap.put(sourceId, amount.add(deliveryLines.getDeliveryAmount()));
			}
			
		}
		
	    
		if(printTime == null){
			throw new AnneException("操作失败，出货单打印日期为空，不允许生成回款计划");
		}else{
			for(String key : amountMap.keySet()){
				//发货总金额
				BigDecimal amount = amountMap.get(key) != null ? amountMap.get(key) : new BigDecimal(0);
				Contract contract = baseDao.get(Contract.class, key);
				List<ContrPay> contrPays = contrPayService.getContrPayByContrId(key);
				OrderHeader order = orderMap.get(key);
				if(contrPays != null && contrPays.size() > 0 ){
					for(ContrPay contrPay : contrPays ){
						//收款计划比例
						double planRatio = contrPay.getPlanRatio() != null ? contrPay.getPlanRatio() * 0.01 : 0 ;
						//合同计划金额
						BigDecimal planAmount =  amount.multiply(new BigDecimal(planRatio));
						
						this.saveContractReceiptDetail(deliveryHeader,order, contrPay, contract, planAmount , printTime);
					}
				}else{
					this.saveContractReceiptDetail(deliveryHeader,order, null, contract, amount, printTime);
				}
			}
		}
	}
	/**
	 * 
	 * saveContractReceiptDetail:合同收款计划明细. <br/> 
	 * 
	 * @author liming 
	 * @param deliveryHeader 发货题头
	 * @param order
	 *@param contrPay  回款计划
	 * @param contract  合同
	 * @param planAmount 计划金额
	 * @param printTime 打印日期     @since JDK 1.7
	 */
	private void saveContractReceiptDetail(DeliveryHeader deliveryHeader, OrderHeader order, ContrPay contrPay, Contract contract, BigDecimal planAmount, Date printTime){
		String contractId = null;
		if(contract != null){
			contractId = contract.getId();
		}
		String contrPayId = null;
		String paySeqId = null;
		String receiptsStage = null;
		if(contrPay != null){
			contrPayId = contrPay.getId();
			paySeqId = contrPay.getPaySeqId();
			receiptsStage = contrPay.getPayPlan();
		}
		StringBuffer hql = new StringBuffer("from ContractReceiptDetail rd where rd.deliveryId = ?") ;
		List<Object> args = new ArrayList<Object>();
		args.add(deliveryHeader.getId());

		if(StringUtil.isNotEmpty(contractId)){
			hql.append(" and  rd.contractId = ? ");
			args.add(contractId);
		}else{
			hql.append(" and rd.contractId is null ");
		}
		if(StringUtil.isNotEmpty(paySeqId)){
			hql.append(" and rd.receiptsPlan = ? ");
			args.add(paySeqId);
		}else{
			hql.append(" and rd.receiptsPlan is null ");
		}
		if(StringUtil.isNotEmpty(receiptsStage)){
			hql.append(" and rd.receiptsStage = ? ");
			args.add(receiptsStage);
		}else{
			hql.append(" and rd.receiptsStage is null ");
		}
		if(StringUtil.isNotEmpty(receiptsStage)){
			hql.append(" and rd.receiptsStage = ? ");
			args.add(receiptsStage);
		}else{
			hql.append(" and rd.receiptsStage is null ");
		}
		if(StringUtil.isNotEmpty(contrPayId)){
			hql.append(" and rd.contrPayId = ? ");
			args.add(contrPayId);
		}else{
			hql.append(" and rd.contrPayId is null ");
		}



		List<ContractReceiptDetail> contractReceiptDetails = baseDao.findEntity(hql.toString(),args.toArray());
		if(contractReceiptDetails != null && contractReceiptDetails.size() > 0){
			//如果存在重复数据
			throw new AnneException("操作失败，不能生成重复的回款计划");
		}
		
		ContractReceiptDetail contractReceiptDetail = new ContractReceiptDetail();
		contractReceiptDetail.setCustId(deliveryHeader.getReceCustomerId()); //客户ID
		contractReceiptDetail.setCustCode(deliveryHeader.getReceCustomerCode());//客户 编号
		contractReceiptDetail.setCustName(deliveryHeader.getReceCustomerName());//客户 名称
		if(contract != null){
			contractReceiptDetail.setContractId(contract.getId());//合同ID
			contractReceiptDetail.setContractCode(contract.getContrNo());//合同编号
		}

		if (order != null) {
			contractReceiptDetail.setSalesmanCode(order.getSalesmanCode());
			contractReceiptDetail.setSalesmanId(order.getSalesmanId());
			contractReceiptDetail.setSalesmanName(order.getSalesmanName());
			contractReceiptDetail.setSalesCenter(order.getSalesmanCenter());
			contractReceiptDetail.setSalesmanPos(order.getSalesmanPos());
		}

		int day = 0 ;
		if(contrPay != null){
			contractReceiptDetail.setContrPayId(contrPay.getId());//合同回款规划ID
			contractReceiptDetail.setReceiptsPlan(contrPay.getPaySeqId());//合同收款计划行、笔数
			contractReceiptDetail.setReceiptsStage(contrPay.getPayPlan());//收款阶段
			contractReceiptDetail.setRemarks(contrPay.getRemark());//备注
			String payTerm = contrPay.getPayTerm();
			if(StringUtil.isNotEmpty(payTerm) && StringUtil.isNumeric(payTerm)){
				day = Integer.parseInt(payTerm);
			}
		}
		if(printTime != null){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(printTime);
			calendar.add(Calendar.DATE, day);
			contractReceiptDetail.setPaymentDate(calendar.getTime());
		}
		
		contractReceiptDetail.setDeliveryId(deliveryHeader.getId());//发货单ID
		contractReceiptDetail.setDeliveryCode(deliveryHeader.getDeliveryCode());// 发货单编号
		contractReceiptDetail.setPlanAmount(planAmount);//合同计划金额
		contractReceiptDetail.setVeriAmount(new BigDecimal(0));//已核销金额
		//币种先去合同上去，如果合同上没有币种则取订单上的币种
		if(contract != null && StringUtil.isNotEmpty(contract.getCurrency())){
			LovMember lovMember  = lovMemberService.get(contract.getCurrency());
			if (lovMember == null && StringUtil.isNotEmpty(order.getCurrency())) {
				lovMember = lovMemberService.get(order.getCurrency());
			}
			if(lovMember != null){
				contractReceiptDetail.setReceiptsType(lovMember.getCode());//币种
			}
		}

        contractReceiptDetail.setBusinessEntity(deliveryHeader.getBusinessEntity());

		contractReceiptDetail.setBizDept(deliveryHeader.getCreatedOrgId());//业务部门
//		contractReceiptDetail.setSalesmanId(deliveryHeader.getProposerId());//销售人员ID
//		contractReceiptDetail.setSalesmanCode(deliveryHeader.getProposerCode());//销售人员编号
//		contractReceiptDetail.setSalesmanName(deliveryHeader.getProposerName());//销售人员名称

		contractReceiptDetail.setStatus(IConstants.RECEIPT_STATUS_10);
		// 创建字段
		contractReceiptDetail.setCreatedById(deliveryHeader.getCreatedById());
		contractReceiptDetail.setCreatedAt(new Date());
		contractReceiptDetail.setCreatedPosId(deliveryHeader.getCreatedPosId());
		contractReceiptDetail.setCreatedOrgId(deliveryHeader.getCreatedOrgId());
		// 更新字段
		contractReceiptDetail.setUpdatedById(deliveryHeader.getCreatedById());//更新人
		contractReceiptDetail.setUpdatedAt(new Date());//更新时间
		baseDao.saveOrUpdate(contractReceiptDetail);
		
		teamService.addPosition(deliveryHeader.getCreatedPosId(),deliveryHeader.getCreatedById(), 
				IConstants.PERMISSION_BUSINESS_TYPE_CONTRACT_RECEIPT_DETAIL,contractReceiptDetail.getId());
		
	}
	
	@Override
	public List<List<Object>> getExcelData(DeliveryHeader deliveryHeader , List<DeliveryLines> deliveryLines) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<List<Object>> lstOut = new ArrayList<List<Object>>();
		List<Object> lstHead = new ArrayList<Object>();
		lstHead.add("出货申请单头编号");
		lstHead.add("出货行行号");
		lstHead.add("订单编号");
		lstHead.add("订单类型");
		lstHead.add("下单客户编号");
		lstHead.add("下单客户名称");
		lstHead.add("下单客户PO");
		lstHead.add("物料编码");
		lstHead.add("物料名称/产品名称");
		lstHead.add("产品型号");
		lstHead.add("单价");
		lstHead.add("单位");
		lstHead.add("出货单数量 ");
		lstHead.add("发货产品金额");
		lstHead.add("剩余数量");
		lstHead.add("签收数量");
		lstHead.add("收货地址");
		lstHead.add("收货人");
		lstHead.add("收货人电话");
		lstHead.add("下单日期");
		lstHead.add("客户要货日期");
		lstHead.add("工厂承诺日期");
		lstHead.add("出货单编号(外部)");
		lstHead.add("出货单打印日期（外部）");
		lstHead.add("是否已过账");
		lstHead.add("备注");
		lstOut.add(lstHead);
		for(DeliveryLines lines : deliveryLines){
			List<Object> lstIn = new ArrayList<Object>();
			lstIn.add(lines.getDeliveryCode());
			lstIn.add(lines.getLineNum());
			lstIn.add(lines.getOrderCode());
			lstIn.add(lines.getOrderTypeLable());
			lstIn.add(lines.getSingleCustCode());
			lstIn.add(lines.getSingleCustName());
			lstIn.add(lines.getSingleCustPO());
			lstIn.add(lines.getMaterielCode());
			lstIn.add(lines.getMaterielName());
			lstIn.add(lines.getProModel());
			lstIn.add(lines.getPrice());
			lstIn.add(lines.getUnit());
			lstIn.add(lines.getDeliveryQty());
			lstIn.add(lines.getDeliveryAmount());
			lstIn.add(lines.getResidualQty());
			lstIn.add(lines.getResidualQty());
			if(deliveryHeader != null ){
				lstIn.add(deliveryHeader.getDeliveryAddress());
				lstIn.add(deliveryHeader.getConsignee());
				lstIn.add(deliveryHeader.getConsigneeTel());
			}else{
				lstIn.add("");
				lstIn.add("");
				lstIn.add("");
			}
			if(lines.getOrderDate() != null){
				lstIn.add(sdf.format(lines.getOrderDate()));
			}else{
				lstIn.add("");
			}
			if(lines.getArrivalDate() != null){
				lstIn.add(sdf.format(lines.getArrivalDate()));
			}else{
				lstIn.add("");
			}
			if(lines.getPromiseDate() != null){
				lstIn.add(sdf.format(lines.getPromiseDate()));
			}else{
				lstIn.add("");
			}
			
			lstIn.add(lines.getExternalNo());
			lstIn.add(lines.getPrintTime());
			lstIn.add(lines.getIsPostAccount());
			lstIn.add(lines.getRemarks());
			lstOut.add(lstIn);
		}
		return lstOut;
	}
	
	@Override 
	public void importDeliveryReceipt(String deliveryCode,List<List<Object>> data ,UserObject userObject){
		
		List<DeliveryLines> deliveryLines = this.getDeliveryLinesByHCode(deliveryCode);
		Map<String, Double> qtyMap = new HashMap<String, Double>();
		Map<String, String> receAddressInfoMap = new HashMap<>();
		int n = 1 ;
		String hql = "select max(rece.receiptCode) from DeliveryReceipt rece where rece.deliveryCode = ? and deleteFlag != ? ";
		String maxReceiptCode = baseDao.findUniqueEntity(hql, new Object[]{deliveryCode,IConstants.YES});
		
		if(maxReceiptCode != null){
			if (StringUtil.isNotEmpty(maxReceiptCode)) {
				if(StringUtil.isEmpty(maxReceiptCode) || maxReceiptCode.indexOf("-") == -1){
					n = 1 ;
				}else{
					int num = maxReceiptCode.lastIndexOf("-");
					n = Integer.parseInt(maxReceiptCode.substring(num + 1, maxReceiptCode.length())) + 1 ;
				}
			} 
		}
		
		if(data !=null){
			//汇总每行的送货数量
			for(int i= 0 ; i < data.size(); i++){
				List<Object> rowData = data.get(i);
				if(rowData != null && rowData.size() >= 18){
					//行号
					String lineNum = rowData.get(1).toString();
					double addrDeliveryQty = 0;
					if(rowData.get(15) != null){
						if(StringUtil.isNumeric(rowData.get(15).toString())){
							addrDeliveryQty = Double.parseDouble(rowData.get(15).toString());
						}else{
							addrDeliveryQty = 0 ;
						}
					}else{
						addrDeliveryQty = 0 ;
					}
					if(qtyMap.get(lineNum) == null ){
						//如果没有找到则新建
						qtyMap.put(lineNum, addrDeliveryQty);
					}else{
						double qty = qtyMap.get(lineNum);
						qtyMap.put(lineNum, qty + addrDeliveryQty);
					}
					
					String receAddress = rowData.get(16) != null ? rowData.get(16).toString() : "";
					String consignee = rowData.get(17) != null ? rowData.get(17).toString() : "";
					String consigneeTel = rowData.get(18) != null ? rowData.get(18).toString() : "";
					String receAddressInfo = receAddress.trim() + consignee.trim() + consigneeTel.trim();
					
					if(StringUtil.isEmpty(receAddressInfoMap.get(receAddressInfo))){
						receAddressInfoMap.put(receAddressInfo, String.valueOf(n));
						n++ ;
					}
				}else{
					throw new AnneException("导入失败,导入模板不匹配");
				}
			}
			
			for(DeliveryLines lines : deliveryLines){
				if(qtyMap.get(lines.getLineNum()) != null){
					//如果行号相同
					if( qtyMap.get(lines.getLineNum()) > lines.getResidualQty()){
						throw new AnneException("导入失败，行号为【"+lines.getLineNum()+"】的总送货数量大于剩余数量");
					}
				}
				
			}
			List<String> receiptCodes = new ArrayList<>();
			for(int i= 0 ; i < data.size(); i++){
				List<Object> rowData = data.get(i);
				//行号
				String lineNum = rowData.get(1).toString();
				double  qtyDouble = Double.parseDouble(rowData.get(15).toString());
				double addrDeliveryQty = qtyDouble;

				String receAddress = rowData.get(16) != null ? rowData.get(16).toString() : "";
				String consignee = rowData.get(17) != null ? rowData.get(17).toString() : "";
				String consigneeTel = rowData.get(18) != null ? rowData.get(18).toString() : "";
				String remarks = rowData.get(25) != null ? rowData.get(25).toString() : "";
				String receAddressInfo = receAddress.trim() + consignee.trim() + consigneeTel.trim();
				
				if(deliveryLines != null){
					for(DeliveryLines lines : deliveryLines){
						//如果行号相同
						if(lines.getLineNum().equals(lineNum)){

							DeliveryReceipt deliveryReceipt = new DeliveryReceipt();
							deliveryReceipt.setOrderCode(lines.getOrderCode());
							deliveryReceipt.setOrderLineNo(lines.getOrderLineNo());
							deliveryReceipt.setMaterielCode(lines.getMaterielCode());
							deliveryReceipt.setMaterielName(lines.getMaterielName());
							deliveryReceipt.setUnit(lines.getUnit());
							deliveryReceipt.setExternalNo(lines.getExternalNo());
							deliveryReceipt.setInvoicePrintTime(lines.getPrintTime());
							
							deliveryReceipt.setDeliveryCode(lines.getDeliveryCode());
							deliveryReceipt.setDeliveryLinesNum(lineNum);
							deliveryReceipt.setDeliveryQty(addrDeliveryQty);
				    		BigDecimal qty = new BigDecimal(addrDeliveryQty);
				    		deliveryReceipt.setDeliveryAmount(qty.multiply(lines.getPrice()));
				    		deliveryReceipt.setReceAddress(receAddress);
				    		deliveryReceipt.setConsignee(consignee);
				    		deliveryReceipt.setConsigneeTel(consigneeTel);
				    		deliveryReceipt.setRemarks(remarks);
				    		deliveryReceipt.setSourceCode(lines.getSourceCode());
				    		deliveryReceipt.setErpOrderCode(lines.getErpOrderCode());
				    		
							String receiptCode = "";
							String code = receAddressInfoMap.get(receAddressInfo);
							if(code != null){
								if(code.length() == 1){
									receiptCode = lines.getExternalNo()+"-0" + code;
								}else if(code.length() == 2 ){
									receiptCode = lines.getExternalNo()+"-" + code;
								}else{
									receiptCode = lines.getExternalNo()+"-" + code;
								}
							}
							deliveryReceipt.setReceiptCode(receiptCode);
							
							// 创建字段
							deliveryReceipt.setCreatedById(userObject.getEmployee().getId());
							deliveryReceipt.setCreatedAt(new Date());
							deliveryReceipt.setCreatedPosId(userObject.getPosition().getId());
							deliveryReceipt.setCreatedOrgId(userObject.getOrg().getId());
							// 更新字段
							deliveryReceipt.setUpdatedById(userObject.getEmployee().getId());
							deliveryReceipt.setUpdatedAt(new Date());
							deliveryReceipt.setStatus(IConstants.LOGISTICS_RECEIPT_STATUS_10);//未签收
							
							if( i == data.size() -1){
				    			deliveryReceipt.setIsMain(IConstants.YES);
							}else{
								deliveryReceipt.setIsMain(IConstants.NO);
							}
							//设置主单
							if(!receiptCodes.contains(deliveryReceipt.getReceiptCode())){
								deliveryReceipt.setIsMain(IConstants.YES);
								receiptCodes.add(deliveryReceipt.getReceiptCode());
							}else{
								deliveryReceipt.setIsMain(IConstants.NO);
							}
							
							deliveryReceipt.setDeleteFlag(IConstants.NO);
							baseDao.save(deliveryReceipt);

							//更新发货行的剩余数量
							double residualQty = lines.getResidualQty() -addrDeliveryQty;
							if(residualQty < 0){
								throw new AnneException("导入失败，签收单数量大于出货数量");
							}
							lines.setResidualQty(residualQty);
							baseDao.update(lines);
							teamService.addPosition(userObject.getPosition().getId(),userObject.getEmployee().getId(), 
									IConstants.PERMISSION_BUSINESS_TYPE_DELIVERYRECEIPT,deliveryReceipt.getId());
						}
					}
				}
			}
		}
		
	}
	/**
	 * 
	 * isDTComfirmed:是否已经确认交期. <br/> 
	 * 
	 * @author liming 
	 * @param id
	 * @return Y:是 ,N:否
	 * @since JDK 1.7
	 */
	private String isDTComfirmed(String id) {
		String ret = "Y";
		List<DeliveryLines>  deliveryLines  = this.getDeliveryLinesByHid(id);
		if(deliveryLines != null ){
			for(DeliveryLines lines : deliveryLines ){
				if( !IConstants.ORDER_ERP_PLAN_STATUS_70.equals(lines.getPlanStatus()) 
						&& !IConstants.ORDER_ERP_PLAN_STATUS_80.equals(lines.getPlanStatus())){
					ret = "N";
					return ret;
				}
			}
		}else{
			ret = "N";
		}
		return ret;
	}
	
	@Override
	public String checkHasReceipt(String id,String op) {
		if(StringUtil.isNotEmpty(id)){
			DeliveryLines deliveryLines = baseDao.get(DeliveryLines.class, id);
			if(deliveryLines!=null) {
				String deliveryId = deliveryLines.getDeliveryId();
				String hql = " from ContractReceiptDetail where deliveryId=? and veriAmount>0 ";
				String[] args = {deliveryId};
				if(baseDao.findUniqueEntity(hql, args)!=null){
					return "Yes";
				}
			}
			
		}
		return "No";
	}
	
	@Override
	public String checkIsErpDelivery(String id,String op) {
		String orderId = null;
		if(StringUtil.isNotEmpty(id)){
			if("delivery".equals(op)){
				DeliveryLines deliveryLines = baseDao.get(DeliveryLines.class, id);
				if(deliveryLines != null && deliveryLines.getInvoiceQty() > 0 ){
					return "Yes";
				}
				orderId = deliveryLines != null ? deliveryLines.getOrderId() : null;
			}else if("receipt".equals(op)){
				DeliveryReceipt deliveryReceipt = baseDao.get(DeliveryReceipt.class, id);
				if(deliveryReceipt != null){
					String hql1 = "from DeliveryLines line where line.deliveryCode = ? and line.lineNum = ? and deleteFlag != ?";
					DeliveryLines deliveryLines = baseDao.findUniqueEntity(hql1, 
							new Object[]{deliveryReceipt.getDeliveryCode(),deliveryReceipt.getDeliveryLinesNum(),IConstants.YES});
					if(deliveryLines != null && deliveryLines.getInvoiceQty() > 0 ){
						return "Yes";
					}
					orderId = deliveryReceipt != null ? deliveryReceipt.getOrderId() : null;
				}
			}
		}
		if(StringUtil.isNotEmpty(orderId)){
			OrderLines orderLines = baseDao.get(OrderLines.class, orderId);
			if(orderLines != null && IConstants.YES_Yes.equals(orderLines.getIsErpDelivery())){
				return "Yes";
			}
//			if(IConstants.YES_Yes.equals(orderLines.getIsAdvanceBilling()) || orderLines.getBillingQty() > 0){
//				return "Yes";
//			}
		}
		return "No";
	}
	
	/**
	 * 
	 * getExternalNo:获取发货单行的出货单编号（外部）. <br/> 
	 * 
	 * @author liming 
	 * @param deliveryLines
	 * @param
	 * @return 
	 * @since JDK 1.7
	 */
	public String getExternalNo(DeliveryLines deliveryLines) {
		String externalNo = null;
		if (deliveryLines == null || deliveryLines.getErpOrderCode() == null) {
			return null;
		}
		
		String erpOrderCode = deliveryLines.getErpOrderCode() + "W";
		String deliveryCode = deliveryLines.getDeliveryCode();
		
		String hql = "select  line.externalNo from DeliveryLines line where line.deliveryCode = ? and line.externalNo like ? and deleteFlag != ? ";
		List<Object> externalNoList = baseDao.findEntity(hql, 
				new Object[] {deliveryCode,erpOrderCode+"%",IConstants.YES});
		
		if(externalNoList != null && externalNoList.size() > 0 ){
			externalNo  = externalNoList.get(0).toString();
			return externalNo ;
		}else{
			String hql1 = "select max(line.externalNo) from DeliveryLines line where line.externalNo like ? and deleteFlag != ? ";
			String maxExternalNo = baseDao.findUniqueEntity(hql1, new Object[] {erpOrderCode+"%",IConstants.YES});
			
			if (StringUtil.isEmpty(maxExternalNo)) {
				externalNo = erpOrderCode + "501";
			} else {
				externalNo = erpOrderCode ;
				if(StringUtil.isEmpty(maxExternalNo) || maxExternalNo.indexOf("W") == -1){
					externalNo = erpOrderCode + "501";
				}else{
					int num = maxExternalNo.lastIndexOf("W");
					String n = String.valueOf(Integer.parseInt(maxExternalNo.substring(
							num + 1, maxExternalNo.length())) + 1);
					if (n.length() == 1) {
						externalNo = externalNo + "50" + n;
					} else if (n.length() == 2) {
						externalNo = externalNo + "5" + n;
					} else if (n.length() == 3) {
						int no = Integer.valueOf(n);
						if(no < 500){
							n = String.valueOf((no + 500));
						}
						externalNo = externalNo  + n;
					}else{
						externalNo = externalNo  + n;
					}
				}
			}
		}
		return externalNo;
	}
	
	@Override
	public void updateDeliveryLineQtyByID(String id, double deliveryQty,UserObject userObject){
		DeliveryLines deliveryLines = baseDao.get(DeliveryLines.class, id);
		if(deliveryLines != null){
			deliveryLines.setDeliveryQty(deliveryQty);
			BigDecimal deliveryAmount = deliveryLines.getPrice().multiply(new BigDecimal(deliveryQty));
			deliveryLines.setDeliveryAmount(deliveryAmount);
			deliveryLines.setUpdatedAt(new Date());
			if(userObject != null){
				deliveryLines.setUpdatedById(userObject.getEmployee().getId());
			}
			baseDao.update(deliveryLines);
		}
	};
	
	@Override
	public String getQueueName() {
		return "ShippingPlan";
	}
	/**
	 * 消费者
	 */
	@Override
	public void consume(String code) {
		this.createContractReceiptDetail(code);
	}
	@Override
	public boolean isProducer() {
		return false;
	}

	@Override
	public boolean isConsumer() {
		return true;
	}
	
	/**
	 * 签收单数据导出
	 * @param ids
	 * @return
	 * @throws AnneException
	 */
	@Override
	public List<List<Object>> exportInvoiceLinesFormLists(String[] ids) throws AnneException {
		List<List<Object>> lstOut = new ArrayList<List<Object>>();
		addHead(lstOut);
		List<DeliveryReceipt> list = getSelectedContrList(ids);
		for (DeliveryReceipt deliveryReceipt : list) {
			List<Object> lstIn = new ArrayList<Object>();
			lstIn.add(deliveryReceipt.getReceiptCode());
			lstIn.add(deliveryReceipt.getDeliveryCode());
			lstIn.add(deliveryReceipt.getOrderCode());
			lstIn.add(deliveryReceipt.getSingleCustName());
			lstIn.add(deliveryReceipt.getMaterielCode());
			lstIn.add(deliveryReceipt.getProDesc());
			lstIn.add(deliveryReceipt.getDeliveryQty());
			lstIn.add(deliveryReceipt.getUnitLable());
			lstIn.add(deliveryReceipt.getDeliveryAmount());
			lstIn.add(deliveryReceipt.getConsignee());
			lstIn.add(deliveryReceipt.getConsigneeTel());
			lstIn.add(deliveryReceipt.getReceAddress());
			lstIn.add(deliveryReceipt.getRemarks());
			lstIn.add(deliveryReceipt.getExternalNo());
			lstIn.add(deliveryReceipt.getInvoicePrintTime());
			lstIn.add(deliveryReceipt.getReceiptQty());
			lstIn.add(deliveryReceipt.getLogisticsCompanyLable());
			lstIn.add(deliveryReceipt.getErpOrderCode());
			lstIn.add(deliveryReceipt.getSourceCode());
			lstIn.add(deliveryReceipt.getOrderLineNo());
			lstOut.add(lstIn);
		}
		return lstOut;
		
	}

    @Override
    public List<String> getNeedDetailDeliveryCodeByExternalNo(String externalNo) {
		//language=SQL
		String sql =
				"SELECT\n" +
						"  DISTINCT dh.C_DELIVERY_CODE\n" +
						"FROM\n" +
						"  CRM_T_DELIVERY_HEADER dh,\n" +
						"  CRM_T_DELIVERY_LINES dl\n" +
						"WHERE\n" +
						"  dh.C_PID = dl.C_DELIVERY_ID\n" +
						"  and dh.C_DELETE_FLAG != ?\n" +
						"  and dl.C_DELETE_FLAG != ?\n" +
						"  AND dl.C_EXTERNAL_NO = ?\n" +
						"  AND NOT exists (\n" +
						"      SELECT 1\n" +
						"      FROM\n" +
						"        CRM_T_DELIVERY_LINES dl1\n" +
						"      WHERE\n" +
						"        dh.C_PID = dl1.C_DELIVERY_ID\n" +
						"        AND dl1.DT_PRINT_TIME IS NULL\n" +
						"  )\n" +
						"  AND NOT exists (\n" +
						"      SELECT 1\n" +
						"      FROM\n" +
						"        CRM_T_CONTRACT_RECEIPT_DETAIL crd\n" +
						"      WHERE dh.C_PID=crd.C_DELIVERY_ID\n" +
						"  )";
		List deliveryNos = this.baseDao.findBySql(sql, new Object[]{IConstants.YES,IConstants.YES,externalNo});
		return deliveryNos;
    }

    private List<DeliveryReceipt> getSelectedContrList(String[] ids) {
		String idsStr = "";
		for(String id : ids){
			idsStr += "'" + id + "',";
		}
		idsStr= idsStr.substring(0, idsStr.length()-1); 
		StringBuffer hql = new StringBuffer(" from DeliveryReceipt where 1 = 1 and id in (" +idsStr+")");
		return baseDao.findEntity(hql.toString());
	}

	private void addHead(List<List<Object>> lstOut) {
		List<Object> lstHead = new ArrayList<Object>();
		lstHead.add("签收单编号");
		lstHead.add("出货申请编号");
		lstHead.add("订单编号");
		lstHead.add("下单客户");
		lstHead.add("物料编码");
		lstHead.add("产品描述");
		lstHead.add("出货数量");
		lstHead.add("单位");
		lstHead.add("出货金额");
		lstHead.add("收货人");
		lstHead.add("收货人电话");
		lstHead.add("收货地址");
		lstHead.add("备注");
		lstHead.add("出货单编号（外部）");
		lstHead.add("签收单打印日期（外部）");
		lstHead.add("签收数量");
		lstHead.add("物流公司");
		lstHead.add("ERP订单编号");
		lstHead.add("合同/商机编码");
		lstHead.add("订单行号");
		lstOut.add(lstHead);	
	}
	/**
	 * 
	 * deliveryAuditAfter:出货申请审核完成调用. <br/> 
	 * 
	 * @author liming 
	 * @param deliveryCode 出货申请编号
	 * @return 
	 * @since JDK 1.7
	 */
	public Map<String, String> deliveryAuditAfter(String deliveryCode) {
		Map<String, String> map = new HashMap<>();
		map.put("status", "S");
		map.put("msg", "");
		StringBuffer sql = new StringBuffer();
		sql.append("{call CUX_CRM_CALL_ERP_PKG.MAIN_PROCESS(?,?,?,?)}");
		Object[] result = baseDao.executeProcedure(sql.toString(),
				new BaseDao.ProcedureParam[] {
						new BaseDao.InProcedureParam(deliveryCode),
						new BaseDao.InProcedureParam("1"),
						new BaseDao.OutProcedureParam(Types.VARCHAR), 
						new BaseDao.OutProcedureParam(Types.VARCHAR)
				});
		map.put("status", (String) result[2]);
		map.put("msg", (String) result[3]);
		return map;
	}
}