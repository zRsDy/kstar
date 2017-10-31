package com.ibm.kstar.impl.order;


import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.bizopp.IBizBaseService;
import com.ibm.kstar.api.contract.IContractService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.order.IDeliveryService;
import com.ibm.kstar.api.order.IInvoiceService;
import com.ibm.kstar.api.order.IOrderService;
import com.ibm.kstar.api.price.IPriceHeadService;
import com.ibm.kstar.api.product.IProductService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.ICorePermissionService;
import com.ibm.kstar.api.system.permission.IEmployeeService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.api.team.ITeamService;
import com.ibm.kstar.entity.bizopp.BusinessOpportunity;
import com.ibm.kstar.entity.bizopp.ProductDetail;
import com.ibm.kstar.entity.bizopp.Rebate;
import com.ibm.kstar.entity.bizopp.RebateLine;
import com.ibm.kstar.entity.contract.Contract;
import com.ibm.kstar.entity.custom.CustomAddressInfo;
import com.ibm.kstar.entity.custom.CustomErpInfo;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.order.*;
import com.ibm.kstar.entity.order.vo.OrderQuantityVo;
import com.ibm.kstar.entity.order.vo.OrderVO;
import com.ibm.kstar.entity.product.KstarProduct;
import com.ibm.kstar.entity.product.ProductPriceDiscount;
import com.ibm.kstar.entity.product.ProductPriceHead;
import com.ibm.kstar.entity.product.ProductPriceLine;
import com.ibm.kstar.entity.quot.KstarPrjLst;
import com.ibm.kstar.log.IMethodLogService;
import com.ibm.kstar.log.MethodLogger;
import com.ibm.kstar.permission.utils.PermissionUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.Condition;
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
import org.xsnake.xflow.api.workflow.IXflowProcessServiceWrapper;

import java.math.BigDecimal;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class OrderServiceImpl implements IOrderService {
    @Autowired
    BaseDao baseDao;
    @Autowired
    IContractService contractService;
    @Autowired
    ITeamService teamService;
    @Autowired
    ICorePermissionService corePermissionService;
    @Autowired
    IXflowProcessServiceWrapper xflowProcessServiceWrapper;
    @Autowired
    ILovMemberService lovMemberService;
    @Autowired
    ICustomInfoService customInfoService;
    @Autowired
    IEmployeeService employeeService;
    @Autowired
    IDeliveryService deliveryService;
    @Autowired
    IPriceHeadService priceHeadService;
    @Autowired
    IInvoiceService invoiceService;
    @Autowired
    IProcessService processService;
    @Autowired
    IBizBaseService bizService;
    @Autowired
    IProductService productService;
    @Autowired
    IMethodLogService methodLogService;
    
    @Override
    public void saveOrder(OrderHeader orderHeader, UserObject userObject) throws Exception {
        if (orderHeader.getCustomerErpCode() != null) {
            if (orderHeader.getCustomerErpCode().indexOf("KSTAR") != -1) {
                throw new AnneException("未通过ERP审核的客户不允许下单！");
            }
        }
        //保存订单头
        this.save(orderHeader, userObject);
        //保存订单行
        this.saveOrderLines(orderHeader, userObject);
        //如果订单来源是合同更新合同状态
        if (IConstants.ORDER_SOURCE_CONTRACT.equals(orderHeader.getSourceType())
                && StringUtil.isNotEmpty(orderHeader.getSourceId())) {
            //contractService.updateContrStaForOrderByContrID(orderHeader.getSourceId());
            contractService.updateContractStatByAmt(orderHeader.getId(), orderHeader.getSourceId());
        }
        teamService.addPosition(userObject.getPosition().getId(), userObject.getEmployee().getId(),
                IConstants.PERMISSION_BUSINESS_TYPE_ORDER, orderHeader.getId());

        //如果来源类型不为代理商，销售员添加到销售团队
        if(!IConstants.ORDER_SOURCE_BIZ.equals(orderHeader.getSourceType())){
        	List<LovMember> mems = corePermissionService.getUserPositionList(orderHeader.getSalesmanId());
        	if (mems != null) {
        		for (LovMember lovMember : mems) {
        			teamService.addPosition(lovMember.getId(), orderHeader.getSalesmanId(),
        					IConstants.PERMISSION_BUSINESS_TYPE_ORDER, orderHeader.getId());
        		}
        	}
        }
    }

    /**
     * 更新订单头和订单行
     * TODO 简单描述该方法的实现功能（可选）.
     *
     * @see com.ibm.kstar.api.order.IOrderService
     * updateOrder(com.ibm.kstar.entity.order.OrderHeader)
     */
    @Override
    public void updateOrder(OrderHeader orderHeader, UserObject userObject) throws Exception {
        if (orderHeader.getCustomerErpCode() != null) {
            if (orderHeader.getCustomerErpCode().indexOf("KSTAR") != -1) {
                throw new AnneException("未通过ERP审核的客户不允许下单！");
            }
        }
        OrderHeader oldOrderHeader = baseDao.get(OrderHeader.class,
                orderHeader.getId());
        if (oldOrderHeader == null) {
            throw new AnneException(IOrderService.class.getName()
                    + " saveOrderHeader : 没有找到要更新的数据");
        }
        BeanUtils.copyPropertiesIgnoreNull(orderHeader, oldOrderHeader);
        this.update(oldOrderHeader, userObject);

        //如果订单来源是合同更新合同状态
        if (IConstants.ORDER_SOURCE_CONTRACT.equals(oldOrderHeader.getSourceType())
                && StringUtil.isNotEmpty(oldOrderHeader.getSourceId())) {
            contractService.updateContractStatByAmt(oldOrderHeader.getId(), oldOrderHeader.getSourceId());
        }

//        //如果是外部用户
//        if (userObject != null && !userObject.isInner() && StringUtil.isNotEmpty(orderHeader.getSalesmanId())) {
//            //销售员添加到销售团队
//            List<LovMember> mems = corePermissionService.getUserPositionList(orderHeader.getSalesmanId());
//            if (mems != null) {
//                for (LovMember lovMember : mems) {
//                    teamService.addPosition(lovMember.getId(), orderHeader.getSalesmanId(),
//                            IConstants.PERMISSION_BUSINESS_TYPE_ORDER, orderHeader.getId());
//                }
//            }
//        }

        //更新订单行
        this.saveOrderLines(orderHeader, userObject);
    }

    /**
     * 保存订单变更信息
     * TODO 简单描述该方法的实现功能（可选）.
     *
     * @see com.ibm.kstar.api.order.IOrderService#saveOrderChange(com.ibm.kstar.entity.order.OrderHeaderChange, com.ibm.kstar.api.system.permission.UserObject)
     */
    @Override
    public void saveOrderChange(OrderHeader orderHeader, UserObject userObject) throws Exception {
    	MethodLogger methodLogger = methodLogService.getMethodLogger("com.ibm.kstar.impl.order.OrderServiceImpl.saveOrderChange",orderHeader.getOrderCode());
    	Exception exception = new Exception();
    	
        //如果存在在途的变更单
        if (this.checkOrderHeaderChangeInApproval(orderHeader.getOrderCode())) {
            throw new AnneException("该订单存在未审核的变更申请，请审核完以后再申请变更！");
        }
        OrderHeaderChange orderHeaderChange = new OrderHeaderChange();
        OrderHeader oldOrderHeader = this.queryOrderHeaderByCode(orderHeader.getOrderCode());

        BeanUtils.copyPropertiesIgnoreNull(oldOrderHeader, orderHeaderChange);

        //获取订单变更记录总数
        int count = this.countByChange(oldOrderHeader.getId());
        orderHeaderChange.setVersion(count+1);

        LovMember lov = lovMemberService.getLovMemberByCode("ORDER_PREFIX_RULE", "ORDER");
        String code = "";
        String prefix = "KST-OC-";
        if (lov != null) {
            prefix = lov.getName();
        }
        
        try {
        	methodLogService.setFunctionNameAndParameter(methodLogger, "baseDao.get(OrderHeader.class, orderId)", 1, "gen_order_change_code", prefix);
        	code = this.getSequenceCode("gen_order_change_code", prefix);
        }catch(Exception e) {
    		e.printStackTrace();
    		exception = e;
    		throw e;
    	}finally {
    		methodLogService.setReturnDataNotes(false,methodLogger,exception,1,code);
    	}
        
        try {
        	methodLogService.setFunctionNameAndParameter(methodLogger, "baseDao.save(orderHeaderChange)||saveOrderLinesChange(orderHeaderChange, userObject,null)", 2,orderHeaderChange);
	        orderHeaderChange.setChangeCode(code);
	        orderHeaderChange.setAmount(orderHeader.getAmount());
	        orderHeaderChange.setOrderCode(oldOrderHeader.getOrderCode());
	        orderHeaderChange.setId(null);
	        orderHeaderChange.setEventStatus(IConstants.ORDER_CONTROL_STATUS_10);
	        orderHeaderChange.setFromId(oldOrderHeader.getId());
	        orderHeaderChange.setCreatedById(userObject.getEmployee().getId());
	        orderHeaderChange.setCreatedAt(new Date());
	        orderHeaderChange.setCreatedPosId(userObject.getPosition().getId());
	        orderHeaderChange.setCreatedOrgId(userObject.getOrg().getId());
	        // 更新字段
	        orderHeaderChange.setUpdatedById(userObject.getEmployee().getId());
	        orderHeaderChange.setUpdatedAt(new Date());
	        baseDao.save(orderHeaderChange);
	
	        orderHeaderChange.setLinesList(orderHeader.getLinesList());
	        //保存变更订单行
	        saveOrderLinesChange(orderHeaderChange, userObject,null);
        }catch(Exception e) {
    		e.printStackTrace();
    		exception = e;
    		throw e;
    	}finally {
    		methodLogService.setReturnDataNotes(false,methodLogger,exception,2,"void");
    	}
        
        
        //启动工作流
        String model = lovMemberService.getFlowCodeByAppCode(IConstants.ORDER_AUDIT_FLOW_APP_ORDER_CHANGE);
        Map<String, String> varmap = new HashMap<>();
        varmap.put("title", "订单变更 - 订单编号[" + orderHeaderChange.getOrderCode() + "]");
        varmap.put("app", IConstants.ORDER_AUDIT_FLOW_APP_ORDER_CHANGE);

        String saleCenter = lovMemberService.getSaleCenter(userObject.getOrg().getId());
        varmap.put("SalesCenter", saleCenter);
        varmap.put("employeeIdInForm", orderHeader.getBusinessManagerId());
        varmap.put("employeeNameInForm", orderHeader.getBusinessManagerName());
        String employeeType = "";
        if (userObject != null && userObject.getOrg() != null) {
            employeeType = userObject.getOrg().getOptTxt3();
        }
        varmap.put("EmployeeType", employeeType);
        
        try {    
        	methodLogService.setFunctionNameAndParameter(methodLogger, "this.updateOrderChangeStatus(orderHeaderChange.getId(), IConstants.ORDER_CONTROL_STATUS_20, userObject)",
        			3,orderHeaderChange.getId(), IConstants.ORDER_CONTROL_STATUS_20, userObject);
	        xflowProcessServiceWrapper.start(model, orderHeaderChange.getId(), userObject, varmap);
	        this.updateOrderChangeStatus(orderHeaderChange.getId(), IConstants.ORDER_CONTROL_STATUS_20, userObject);
        }catch(Exception e) {
    		e.printStackTrace();
    		exception = e;
    		throw e;
    	}finally {
    		methodLogService.setReturnDataNotes(false,methodLogger,exception,3,"void");
    	}
        
        try {
	        methodLogService.setFunctionNameAndParameter(methodLogger, "teamService.addPosition(userObject.getPosition().getId(), userObject.getEmployee().getId()," + 
	        		"                IConstants.PERMISSION_BUSINESS_TYPE_ORDER_CHANGE, orderHeaderChange.getId())",
	        		4,userObject.getPosition().getId(), userObject.getEmployee().getId(),
	                IConstants.PERMISSION_BUSINESS_TYPE_ORDER_CHANGE, orderHeaderChange.getId());
	        
	        teamService.addPosition(userObject.getPosition().getId(), userObject.getEmployee().getId(),
	                IConstants.PERMISSION_BUSINESS_TYPE_ORDER_CHANGE, orderHeaderChange.getId());
	        
        }catch(Exception e) {
    		e.printStackTrace();
    		exception = e;
    		throw e;
    	}finally {
    		methodLogService.setReturnDataNotes(false,methodLogger,exception,4,"void");
    	}
        
        //销售员添加到销售团队
        List<LovMember> mems = corePermissionService.getUserPositionList(orderHeaderChange.getSalesmanId());
        if (mems != null) {
            for (LovMember lovMember : mems) {
            	try {
            		methodLogService.setFunctionNameAndParameter(methodLogger, "teamService.addPosition(lovMember.getId(), orderHeaderChange.getSalesmanId(),\r\n" + 
            				"            				IConstants.PERMISSION_BUSINESS_TYPE_ORDER_CHANGE, orderHeaderChange.getId())", 5, lovMember.getId(), orderHeaderChange.getSalesmanId(),
            				IConstants.PERMISSION_BUSINESS_TYPE_ORDER_CHANGE, orderHeaderChange.getId());
            		
            		teamService.addPosition(lovMember.getId(), orderHeaderChange.getSalesmanId(),
            				IConstants.PERMISSION_BUSINESS_TYPE_ORDER_CHANGE, orderHeaderChange.getId());
            		
            	}catch(Exception e) {
            		e.printStackTrace();
            		exception = e;
            		throw e;
            	}finally {
            		methodLogService.setReturnDataNotes(true,methodLogger,exception,5,"void");
            	}
            }
        }
    }

    /**
     * 审批完成后，保存原数据到订单变更作为初始元数据
     */
    @Override
    public void saveCopyOrderChange(String businessKey){
    	OrderHeader oldOrderHeader = baseDao.get(OrderHeader.class, businessKey);
    	MethodLogger methodLogger = methodLogService.getMethodLogger("com.ibm.kstar.impl.order.OrderServiceImpl.saveCopyOrderChange",oldOrderHeader.getOrderCode());
		Exception exception = new Exception();
    	
    	OrderHeaderChange orderHeaderChange = new OrderHeaderChange();
    	BeanUtils.copyPropertiesIgnoreNull(oldOrderHeader, orderHeaderChange);
        LovMember lov = lovMemberService.getLovMemberByCode("ORDER_PREFIX_RULE", "ORDER");
        String code = "";
        String prefix = "KST-OC-";
        if (lov != null) {
            prefix = lov.getName();
        }
        
        try {
        	methodLogService.setFunctionNameAndParameter(methodLogger, "this.getSequenceCode('gen_order_change_code', prefix)", 1, prefix);
        	code = this.getSequenceCode("gen_order_change_code", prefix);
        }catch(Exception e) {
    		e.printStackTrace();
    		exception = e;
    		throw e;
    	}finally {
    		methodLogService.setReturnDataNotes(false,methodLogger,exception,1,code);
    	}
        
        try {
        	orderHeaderChange.setId(null);
            orderHeaderChange.setChangeCode(code);
            orderHeaderChange.setFromId(oldOrderHeader.getId());
            orderHeaderChange.setUpdatedAt(new Date());
            orderHeaderChange.setCreatedAt(new Date());
            methodLogService.setFunctionNameAndParameter(methodLogger, "baseDao.save(orderHeaderChange)",2, orderHeaderChange);
            baseDao.save(orderHeaderChange);
        }catch(Exception e) {
    		e.printStackTrace();
    		exception = e;
    		throw e;
    	}finally {
    		methodLogService.setReturnDataNotes(false,methodLogger,exception,2,"void");
    	}
        

        List<OrderLines> oldOrderLinesList = getOrderLinesOrderId(businessKey);
        for(OrderLines oldOrderLines:oldOrderLinesList){
        	try {
	        	OrderLinesChange newOrderLines = new OrderLinesChange();
	        	BeanUtils.copyPropertiesIgnoreNull(oldOrderLines, newOrderLines);
	        	newOrderLines.setId(null);
	        	newOrderLines.setFromId(oldOrderLines.getId());
	        	newOrderLines.setOrderId(businessKey);
	        	newOrderLines.setOrderChangeId(orderHeaderChange.getId());
	        	methodLogService.setFunctionNameAndParameter(methodLogger, "baseDao.save(newOrderLines)",3, newOrderLines);
	        	baseDao.save(newOrderLines);
        	}catch(Exception e) {
        		e.printStackTrace();
        		exception = e;
        		throw e;
        	}finally {
        		methodLogService.setReturnDataNotes(true,methodLogger,exception,3,"void");
        	}	
        }

    };

    /**
     * saveOrderLinesChange:保存订单变更行. <br/>
     * TODO(这里描述这个方法适用条件 – 可选).<br/>
     * TODO(这里描述这个方法的执行流程 – 可选).<br/>
     * TODO(这里描述这个方法的使用方法 – 可选).<br/>
     * TODO(这里描述这个方法的注意事项 – 可选).<br/>
     *
     * @param orderHeaderChange
     * @param userObject
     * @throws Exception
     * @author liming
     * @since JDK 1.7
     */
    public void saveOrderLinesChange(OrderHeaderChange orderHeaderChange, UserObject userObject,String orderChangeFlag) throws Exception {
        List<Map<Object, Object>> linesList = orderHeaderChange.getLinesList();
        List<OrderLinesChange> delectOldOrderLinesList = getOrderLinesChangeOrderId(orderHeaderChange.getId());
        if (linesList != null) {
            List<OrderLinesChange> bizOrderLineChanges = new ArrayList<>();
            for (Map<Object, Object> map : linesList) {
                //将Map集合转成对象
            	OrderLinesChange changeLines = (OrderLinesChange) BeanUtils.convertMap(OrderLinesChange.class, map);
            	OrderLinesChange oldChangeLines = baseDao.get(OrderLinesChange.class, changeLines.getId());
                delectOldOrderLinesList.remove(oldChangeLines);
            	if(StringUtil.isNullOrEmpty(orderChangeFlag)) {
	            	OrderLines Lines = (OrderLines) BeanUtils.convertMap(OrderLines.class, map);
	            	changeLines.setFromId(Lines.getId());
	                changeLines.setId(null);
                }
                changeLines.setOrderChangeId(orderHeaderChange.getId());
                changeLines.setOrderCode(orderHeaderChange.getOrderCode());
                // 更新字段
                changeLines.setUpdatedById(userObject.getEmployee().getId());
                changeLines.setUpdatedAt(new Date());


                if(StringUtil.isNullOrEmpty(orderChangeFlag)) {
                	//如果行号为空
                    if (StringUtil.isEmpty(changeLines.getLineNo())) {
                    	changeLines.setLineNo(this.getLineNo(orderHeaderChange.getFromId()));
                    }
                	baseDao.save(changeLines);
                }else {
                	//如果行号为空
                    if (StringUtil.isEmpty(changeLines.getLineNo())) {
                    	changeLines.setLineNo(this.getChangeLineNo(orderHeaderChange.getId()));
                    }
                	if(oldChangeLines != null) {
                        baseDao.delete(oldChangeLines);
                	}
                     baseDao.save(changeLines);
                }
                if (IConstants.ORDER_SOURCE_BIZ.equals(orderHeaderChange.getSourceType())) {
                    if (StringUtils.isNotEmpty(changeLines.getSourceId())) {
                        bizOrderLineChanges.add(changeLines);
                    }
                }
            }
            if(delectOldOrderLinesList.size()>0) {
            	baseDao.deleteAll((delectOldOrderLinesList));
            }
            if (IConstants.ORDER_SOURCE_BIZ.equals(orderHeaderChange.getSourceType())) {
                if (bizOrderLineChanges.size() > 0) {
                    this.checkOrderLineChangesBiz(orderHeaderChange.getOrderCode(), bizOrderLineChanges);
                }
            }

        }
    }

    @Override
    public void updateOrderChangeStatus(String orderId, String status, UserObject userObject) {

        OrderHeaderChange orderHeaderChange = baseDao.get(OrderHeaderChange.class, orderId);
        if (orderHeaderChange == null) {
            throw new AnneException(IOrderService.class.getName()
                    + " updateOrderChangeStatus : 没有找到要更新的数据");
        }


        orderHeaderChange.setEventStatus(status);
        baseDao.update(orderHeaderChange);
        String orderCode = orderHeaderChange.getOrderCode();
        OrderHeader orderHeader = queryOrderHeaderByCode(orderCode);
        if (orderHeader == null) {
            throw new AnneException(IOrderService.class.getName()
                    + " updateOrderChangeStatus : 没有找到原始订单");
        }
        if (IConstants.ORDER_CONTROL_STATUS_20.equals(status)) {
            orderHeader.setControlStatus(IConstants.ORDER_CONTROL_STATUS_50);
        } else if (IConstants.ORDER_CONTROL_STATUS_30.equals(status)) {
        	orderHeaderChangeSetOrderHeader(orderHeaderChange,orderHeader);
            this.update(orderHeader, userObject);

            List<OrderLines> OrderLines = this.getOrderLinesOrderCode(orderCode);
            List<OrderLinesChange> orderLinesChanges = this.getOrderLinesChangeByChangeHID(orderHeaderChange.getId());
            if (orderLinesChanges != null) {
                for (OrderLinesChange linesChange : orderLinesChanges) {
                    boolean b = false;
                    if (OrderLines != null) {
                        for (OrderLines lines : OrderLines) {
                            if (lines.getId().equals(linesChange.getFromId())) {
                                if (IConstants.ORDER_LINE_STATUS_CANCELLED.equals(linesChange.getStatus())
                                        && !IConstants.ORDER_LINE_STATUS_CANCELLED.equals(lines.getStatus())) {
                                    this.cancelOrderLine(lines.getId(), userObject);
                                } else {
                                    lines.setProQty(linesChange.getProQty());
                                    lines.setPrice(linesChange.getPrice());
                                    lines.setErpSettPrice(linesChange.getPrice());
                                    lines.setAmount(linesChange.getAmount());
                                    lines.setSpCode(linesChange.getSpCode());
                                    lines.setSpLineId(linesChange.getSpLineId());
                                    lines.setSourceCode(linesChange.getSourceCode());
                                    lines.setSourceId(linesChange.getSourceId());
                                    lines.setRemark(linesChange.getRemark());
                                    lines.setShipOrg(linesChange.getShipOrg());
                                    lines.setShipOrgLable(linesChange.getShipOrgLable());
                                    lines.setProId(linesChange.getProId());
                                    lines.setErpSettPrice(linesChange.getErpSettPrice());
                                    lines.setConfirmDeliveryDate(linesChange.getConfirmDeliveryDate());
                                    lines.setRemark(linesChange.getRemark());
                                    this.update(lines, userObject);
                                    //更新出货单金额
                                    this.updateDeliveryByOrderPriceChange(lines, userObject);
                                }
                                b = true;
                                break;
                            }
                        }
                    }
                    if (!b) {
                        OrderLines newLines = new OrderLines();
                        BeanUtils.copyPropertiesIgnoreNull(linesChange, newLines);
                        newLines.setId(null);
                        newLines.setLineNo(this.getLineNo(orderHeader.getId()));
                        newLines.setOrderCode(orderHeader.getOrderCode());
                        newLines.setOrderId(orderHeader.getId());
                        if (IConstants.ORDER_EXECUTE_STATUS_BOOKED.equals(orderHeader.getExecuteStatus())) {
                            newLines.setStatus(IConstants.ORDER_LINE_STATUS_AWAITING_SHIPPING);
                            newLines.setErpPlanStatus(IConstants.ORDER_LINE_STATUS_AWAITING_SHIPPING);
                        } else {
                            newLines.setStatus(IConstants.ORDER_LINE_STATUS_ENTERED);
                            newLines.setErpPlanStatus(IConstants.ORDER_LINE_STATUS_ENTERED);

                        }
                        this.initOrderLines(newLines);
                        this.save(newLines, userObject);
                    }
                }
            }
        }
        orderHeader.setControlStatus(IConstants.ORDER_CONTROL_STATUS_30);
        baseDao.update(orderHeader);
    }

    /**
     * 获取订单变更记录总数
     * @param fromId
     * @return
     */
    public int countByChange(String fromId) {
    	List<Object> args = new ArrayList<Object>();
		String hql = " from OrderHeaderChange where 1=1 ";
			hql += " and fromId = ? ";
			hql += " and (eventStatus != ? or eventStatus is null )";
			hql += " order by createdAt desc ";
		args.add(fromId);
		args.add(IConstants.ORDER_CONTROL_STATUS_70);
    	List<OrderHeaderChange> list = baseDao.findEntity(hql,args.toArray());
    	return list.size();
	}

	@Override
    public List<OrderHeaderChange> getOrderChangeByOrderId(String orderId) throws AnneException {
	    List<Object> args = new ArrayList<Object>();
		String hql = " from OrderHeaderChange where 1=1 ";
			hql += " and fromId = ? ";
			hql += " and (eventStatus != ? or eventStatus is null )";
			hql += " order by createdAt desc ";
		args.add(orderId);
		args.add(IConstants.ORDER_CONTROL_STATUS_70);
		List<OrderHeaderChange> list = baseDao.findEntity(hql, args.toArray());

		return list;
    }

    @Override
    public IPage queryOrderLinesChange(PageCondition condition) {
        FilterObject filterObject = condition.getFilterObject(OrderLinesChange.class);
        filterObject.addOrderBy("lineNo", "desc");
        HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
        return baseDao.search(hqlObject.getHql(), hqlObject.getArgs(), condition.getRows(), condition.getPage());

    }

    /**
     * saveOrUpdateOrderLines:(保存/更新订单行). <br/>
     * TODO(这里描述这个方法适用条件 – 可选).<br/>
     *
     * @param orderHeader
     * @throws Exception
     * @author liming
     * @since JDK 1.7
     */
    public void saveOrderLines(OrderHeader orderHeader, UserObject userObject) throws Exception {
        List<Map<Object, Object>> linesList = orderHeader.getLinesList();
        List<Object> list = new ArrayList<Object>();
        if (linesList != null) {
            for (Map<Object, Object> map : linesList) {
                list.add(map.get("id"));
            }
        }
        String hql = " from OrderLines d where d.orderId = ? ";
        Object[] args = {orderHeader.getId()};
        List<OrderLines> lines = baseDao.findEntity(hql, args);
        //行数据不在页面返回的集合中，则将其删除
        for (OrderLines orderLines : lines) {
            if (list == null || list.size() <= 0 || !list.contains(orderLines.getId())) {
                if (IConstants.ORDER_EXECUTE_STATUS_BOOKED.equals(orderHeader.getExecuteStatus())) {
                    throw new AnneException("订单已登记不允许删除行");
                }
                if (orderLines.getBillingQty() > 0) {
                    throw new AnneException("订单已开票不允许删除行");
                }
                if (orderLines.getDeliveryQty() > 0) {
                    throw new AnneException("订单已发货不允许删除行");
                }
                baseDao.deleteById(OrderLines.class, orderLines.getId());
            }
        }
        List<OrderLines> bizOrderLines = new ArrayList<>();
        if (linesList != null) {
            for (Map<Object, Object> map : linesList) {
                //将Map集合转成对象
                OrderLines orderLines = (OrderLines) BeanUtils.convertMap(OrderLines.class, map);
                if (orderLines != null && !IConstants.ORDER_LINE_STATUS_CANCELLED.equals(orderLines.getStatus())) {

                    if (StringUtil.isNullOrEmpty(orderLines.getMaterielCode())) {
                        throw new AnneException("物料编号为空，不允许下单！");
                    }

                    double price = orderLines.getPrice() == null ? 0 : orderLines.getPrice().doubleValue();
                    if (price < 0) {
                        throw new AnneException(IOrderService.class.getName() + "saveOrderLines : 订单行产品单价不能小于0");
                    }

                    KstarProduct product = baseDao.get(KstarProduct.class, orderLines.getProId());
                    String clientCategory = null;//产品外部型号
                    if (product != null) {
                        clientCategory = product.getClientCategory();//产品外部型号
                    }

                    //如果是代理商订单， 而且是国内数据中心（P_GNORG_B1_0001） 且不是YDC33系列 检查商机是否为空
                    //修改代理商订单报备控制（只针对代理商）：当订单选择报备类产品时，必须关联商机，通过新逻辑进行判断（需要考虑国内光伏代理商的情况：不关联特价不做报备控制）
                    //代理商订单
                    if (IConstants.ORDER_SOURCE_BIZ.equals(orderHeader.getSourceType())) {
                        boolean isDistributionPlatform = isDistributionPlatform(orderHeader);
                        boolean isYDC33 = "YDC3360H".equals(clientCategory) || "YDC3380H".equals(clientCategory);
                        if (isDistributionPlatform && isYDC33) {

                        } else {
                            if (!isDistributionPlatform) {
                                //非分销平台
                                if (StringUtils.isNotEmpty(clientCategory)) {
                                    boolean isYDC = clientCategory.startsWith("YDC");
                                    boolean isYDE = clientCategory.startsWith("YDE");
                                    if (isYDC || isYDE) {
                                        if (StringUtil.isEmpty(orderLines.getSpCode())) {
                                            throw new AnneException(IOrderService.class.getName() + "saveOrderLines : 订单行产品【" + orderLines.getMaterielCode() + "】是Y系列产品，非分销平台下单特价编号不能为空！");
                                        }
                                    }
                                }
                            }

                            //创建人营销中心
                            String saleCenter = lovMemberService.getSaleCenter(orderHeader.getCreatedOrgId());
                            if (IConstants.CONTR_ORG_GN_B1_STR.equals(saleCenter)) {
                                //国内数据中心
                                if (StringUtil.isNotEmpty(orderLines.getProModel()) && this.checkProIsReport(orderLines.getProModel())) {
                                    if (StringUtil.isEmpty(orderLines.getSourceCode())) {
                                        throw new AnneException(IOrderService.class.getName()
                                                + "saveOrderLines : 订单行产品【" + orderLines.getMaterielCode() + "】已报备，商机编号不能为空！");
                                    }
                                }
                            } else if (IConstants.CONTR_ORG_GNGFORG_B1_STR.equals(saleCenter)) {
                                //新能源国内营销
                                if (StringUtils.isNotEmpty(orderLines.getSpCode())) {
                                    if (StringUtil.isNotEmpty(orderLines.getProModel()) && this.checkProIsReport(orderLines.getProModel())) {
                                        if (StringUtil.isEmpty(orderLines.getSourceCode())) {
                                            throw new AnneException(IOrderService.class.getName() + "saveOrderLines : 订单行产品【" + orderLines.getMaterielCode() + "】已报备，商机编号不能为空！");
                                        }
                                    }
                                }
                            }

                        }
                    }

                    if (IConstants.ORDER_SOURCE_CONTRACT.equals(orderHeader.getSourceType())) {
                        double orderQty = orderLines.getProQty(); //当前数量
                        //获取已下单数量
                        OrderQuantityVo quantityVo = this.getContractOrderQty(orderHeader.getSourceCode(), orderLines.getSourceId());
                        if (quantityVo != null) {
                            orderQty = orderQty + quantityVo.getProQty();
                        }
                        if (StringUtil.isNotEmpty(orderLines.getId())) {
                            OrderLines line = baseDao.get(OrderLines.class, orderLines.getId());
                            if (line != null) {
                                orderQty = orderQty - line.getProQty();
                            }
                        }
                        KstarPrjLst kstarPrjLst = contractService.getPrjLstByContractID(orderHeader.getSourceId(), orderLines.getSourceId());
                        if (kstarPrjLst != null) {
                            double amt = 0;
                            if (kstarPrjLst.getNotVeriNum() != null) {
                                amt = kstarPrjLst.getNotVeriNum();
                            } else {
                                amt = kstarPrjLst.getAmt() != null ? kstarPrjLst.getAmt() : 0;
                            }

                            if (amt < orderQty) {
                                throw new AnneException(IOrderService.class.getName()
                                        + "saveOrderLines : 保存失败，订单行【" + orderLines.getLineNo() + "】累计下单数量大于合同数量，请检查");
                            }
                        } else {
                            throw new AnneException(IOrderService.class.getName()
                                    + "saveOrderLines : 保存失败，没找到合同【" + orderHeader.getSourceCode() + "】的工程清单行【" + orderLines.getSourceId() + "】");
                        }
                    }
                    orderLines.setOrderId(orderHeader.getId());
                    orderLines.setOrderCode(orderHeader.getOrderCode());
                    OrderLines oldOrderLines;

                    if (StringUtil.isEmpty(orderLines.getId())) {
                        oldOrderLines = orderLines;
                    } else {
                        oldOrderLines = baseDao.get(OrderLines.class, orderLines.getId());
                        if (oldOrderLines == null) {
                            orderLines.setId(null);
                            oldOrderLines = orderLines;
                        } else {
                            //将 orderLines的属性复制到 oldOrderLines
                            BeanUtils.copyPropertiesIgnoreNull(orderLines, oldOrderLines);
                        }
                    }
                    if (oldOrderLines.getProQty() == 0
                            || oldOrderLines.getErpSettPrice() == null
                            || oldOrderLines.getErpSettPrice().doubleValue() == 0) {
                        oldOrderLines.setAmount(new BigDecimal(0));
                    }
                    if (StringUtil.isEmpty(oldOrderLines.getId())) {
                        oldOrderLines.setCreator(userObject.getEmployee().getId());
                        oldOrderLines.setCreateTime(new Date());
                        //初始化值
                        initOrderLines(orderHeader,oldOrderLines);
                        //如果是合同这更新合同的orderCode
                        if (IConstants.ORDER_SOURCE_CONTRACT.equals(orderHeader.getSourceType())) {
                            contractService.updatePrjLstOrderNoByContractID(orderHeader.getSourceId(), oldOrderLines.getSourceId(), "add", orderHeader.getOrderCode());
                        }

                    } else {
                        //更新签收单和出货单价格
                        this.updateDeliveryByOrderPriceChange(oldOrderLines, userObject);
                    }

                    if (StringUtil.isNotEmpty(oldOrderLines.getSpCode())) {
                        oldOrderLines.setIsSp(IConstants.YES_Yes);
                        String spLineId = oldOrderLines.getSpLineId();
                        if (!this.checkRebateLineOrderQty(oldOrderLines.getId(), oldOrderLines.getProQty(), spLineId, null)) {
                            throw new AnneException(IOrderService.class.getName()
                                    + "saveOrderLines : 保存失败，料号【" + orderLines.getMaterielCode() + "】,特价编号【"
                                    + orderLines.getSpCode() + "】的订单行，累计下单数量大于特价申请数量，请检查");
                        }
                    }

                    //如果是外部用户
                    if (userObject != null && !userObject.isInner()) {

                        String remark = "";
                        if (StringUtil.isNotEmpty(oldOrderLines.getSourceCode())) {
                            remark = " 商机编号【" + oldOrderLines.getSourceCode() + "】";
                        }

                        if (StringUtil.isNotEmpty(oldOrderLines.getSpCode())) {
                            String SpecialOffFalg = null;
                            List<LovMember> lovMemberList = lovMemberService.getListByGroupCode("SPECIALOFF");
                            for (LovMember lovMember : lovMemberList) {
                                if ("01".equals(lovMember.getCode())) {
                                    SpecialOffFalg = lovMember.getId();
                                }
                            }

                            List<Object> arg = new ArrayList<Object>();
                            StringBuffer sb = new StringBuffer();
                            sb.append(" select r from Rebate r where r.no = ? ");
                            arg.add(oldOrderLines.getSpCode());
                            List<Rebate> rebateList = baseDao.findEntity(sb.toString(), arg.toArray());
                            if (rebateList.size() > 0) {
                                for (Rebate rebate : rebateList) {
                                    if (SpecialOffFalg.equals(rebate.getSpecialOff())) {//如果是普通特价，显示特价编号
                                        remark = remark + " 特价编号【" + oldOrderLines.getSpCode() + "】";
                                    }
                                }
                            }
                        }
                        oldOrderLines.setRemark(remark);
                    }
                    oldOrderLines.setUpdatedById(userObject.getEmployee().getId());
                    oldOrderLines.setUpdatedAt(new Date());
                    //如果行号为空
                    if (StringUtil.isEmpty(oldOrderLines.getLineNo())) {
                        oldOrderLines.setLineNo(this.getLineNo(orderHeader.getId()));
                    }
                    baseDao.saveOrUpdate(oldOrderLines);

                    if (IConstants.ORDER_SOURCE_BIZ.equals(orderHeader.getSourceType())) {
                        if (StringUtils.isNotEmpty(oldOrderLines.getSourceId())) {
                            bizOrderLines.add(oldOrderLines);
                        }
                    }
                }
            }
            if (IConstants.ORDER_SOURCE_BIZ.equals(orderHeader.getSourceType())) {
                if (bizOrderLines.size() > 0) {
                    this.checkOrderLinesBiz(orderHeader.getOrderCode(), bizOrderLines);
                }
                }
            }
        }

    //商机已关联订单数量控制：通过产品关联型号扣减
    private void checkOrderLineChangesBiz(String orderNo, List<OrderLinesChange> orderLinesChangesList) {
        StringBuilder msg = new StringBuilder();

        Map<String, OrderLinesChange> orderLineMap = new HashMap<>();
        for (int i = 0; i < orderLinesChangesList.size(); i++) {
            OrderLinesChange change = orderLinesChangesList.get(i);
            String bizId = change.getSourceId();
            orderLineMap.put(bizId, change);
        }

        Map<String, Map<String, Integer>> bizSurplusSum = this.getBizSurplusSum(orderNo, orderLineMap.keySet());
        Map<String, Map<String, Integer>> orderBizSum = this.getOrderChangeBizSum(orderLinesChangesList);
        for (Map.Entry<String, Map<String, Integer>> entry : orderBizSum.entrySet()) {
            String bizId = entry.getKey();
            Map<String, Integer> productSumMap = entry.getValue();
            Map<String, Integer> bizSumMap = bizSurplusSum.get(bizId);
            OrderLinesChange orderLinesChange = orderLineMap.get(bizId);
            if (bizSumMap == null) {
                msg.append("商机").append(orderLinesChange.getSourceCode()).append("无可转特价数量;");
                continue;
            }
            for (Map.Entry<String, Integer> productSum : productSumMap.entrySet()) {
                String productModel = productSum.getKey();
                Integer sum = productSum.getValue();
                if (sum == 0) {
                    continue;
                }
                Integer bizProductSum = bizSumMap.get(productModel);
                if (bizProductSum == null || bizProductSum <= 0) {
                    msg.append("商机").append(orderLinesChange.getSourceCode()).append("中").append(productModel).append("无可转特价数量;");
                } else {
                    if (sum > bizProductSum) {
                        msg.append(productModel).append("的申请数量(").append(sum).append(")大于")
                                .append("商机").append(orderLinesChange.getSourceCode()).append("中的").append("可转特价数量(").append(bizProductSum).append(");");
                    }
                }
            }
        }
        if (msg.length() > 0) {
            throw new AnneException(msg.toString());
        }

    }

    //商机已关联订单数量控制：通过产品关联型号扣减
    private void checkOrderLinesBiz(String orderNo, List<OrderLines> orderLineList) {
        StringBuilder msg = new StringBuilder();

        Map<String, OrderLines> orderLineMap = new HashMap<>();
        for (int i = 0; i < orderLineList.size(); i++) {
            OrderLines orderLine = orderLineList.get(i);
            String bizId = orderLine.getSourceId();
            orderLineMap.put(bizId, orderLine);
        }

        Map<String, Map<String, Integer>> bizSurplusSum = this.getBizSurplusSum(orderNo, orderLineMap.keySet());
        Map<String, Map<String, Integer>> rebateBizSum = this.getOrderBizSum(orderLineList);
        for (Map.Entry<String, Map<String, Integer>> entry : rebateBizSum.entrySet()) {
            String bizId = entry.getKey();
            Map<String, Integer> productSumMap = entry.getValue();
            Map<String, Integer> bizSumMap = bizSurplusSum.get(bizId);
            OrderLines orderLines = orderLineMap.get(bizId);
            if (bizSumMap == null) {
                msg.append("商机").append(orderLines.getSourceCode()).append("无可转特价数量;");
                continue;
            }
            for (Map.Entry<String, Integer> productSum : productSumMap.entrySet()) {
                String productModel = productSum.getKey();
                Integer sum = productSum.getValue();
                if (sum == 0) {
                    continue;
                }
                Integer bizProductSum = bizSumMap.get(productModel);
                if (bizProductSum == null || bizProductSum <= 0) {
                    msg.append("商机").append(orderLines.getSourceCode()).append("中").append(productModel).append("无可转特价数量;");
                } else {
                    if (sum > bizProductSum) {
                        msg.append(productModel).append("的申请数量(").append(sum).append(")大于")
                                .append("商机").append(orderLines.getSourceCode()).append("中的").append("可转特价数量(").append(bizProductSum).append(");");
                    }
                }
            }
        }
        if (msg.length() > 0) {
            throw new AnneException(msg.toString());
        }
    }

    /**
     * 获取订单商机产品数量
     *
     * @param orderLinesList
     * @return
     */
    public Map<String, Map<String, Integer>> getOrderBizSum(List<OrderLines> orderLinesList) {
        Map<String, Map<String, Integer>> rebateBizSumMap = new HashMap<>();
        for (OrderLines orderLines : orderLinesList) {
            Double _proQty = orderLines.getProQty();
            if (_proQty == null || Objects.equals(_proQty, 0)) {
                continue;
            }
            String productModel = orderLines.getProModel();
            if (StringUtils.isEmpty(productModel)) {
                continue;
            }
            String bizId = orderLines.getSourceId();
            int applyQty = _proQty.intValue();

            if (!rebateBizSumMap.containsKey(bizId)) {
                rebateBizSumMap.put(bizId, new HashMap<String, Integer>());
            }
            Map<String, Integer> productSumMap = rebateBizSumMap.get(bizId);
            Integer sum = productSumMap.get(productModel);
            if (sum == null) {
                sum = applyQty;
            } else {
                sum += applyQty;
            }
            productSumMap.put(productModel, sum);
        }
        return rebateBizSumMap;
    }

    /**
     * 获取订单商机产品数量
     *
     * @param orderChangeLineList
     * @return
     */
    public Map<String, Map<String, Integer>> getOrderChangeBizSum(List<OrderLinesChange> orderChangeLineList) {
        Map<String, Map<String, Integer>> rebateBizSumMap = new HashMap<>();
        for (OrderLinesChange orderLinesChange : orderChangeLineList) {
            Double _proQty = orderLinesChange.getProQty();
            if (_proQty == null || Objects.equals(_proQty, 0)) {
                continue;
            }
            String productModel = orderLinesChange.getProModel();
            if (StringUtils.isEmpty(productModel)) {
                continue;
            }
            String bizId = orderLinesChange.getSourceId();
            int applyQty = _proQty.intValue();

            if (!rebateBizSumMap.containsKey(bizId)) {
                rebateBizSumMap.put(bizId, new HashMap<String, Integer>());
            }
            Map<String, Integer> productSumMap = rebateBizSumMap.get(bizId);
            Integer sum = productSumMap.get(productModel);
            if (sum == null) {
                sum = applyQty;
            } else {
                sum += applyQty;
            }
            productSumMap.put(productModel, sum);
        }
        return rebateBizSumMap;
    }


    /**
     * 获取商机剩余产品数量
     *
     * @param orderId
     * @param bizIds
     * @return
     */
    private Map<String, Map<String, Integer>> getBizSurplusSum(String orderNo, Set<String> bizIds) {

        StringBuilder inSql = new StringBuilder("");
        List<Object> inArgs = new ArrayList<>();
        for (String bizId : bizIds) {
            inSql.append("?").append(",");
            inArgs.add(bizId);
        }
        inSql.delete(inSql.length() - 1, inSql.length());

        List<Object> bizOppSumArgs = new ArrayList<>();
        //language=SQL
        String bizOppSumSql = "SELECT C_BIZOPP_ID as bizId,C_PRODUCT_MODEL as productModel,sum(N_PLAN_COUNT) as bizQty " +
                "from CRM_T_BIZOPP_PRODUCTS_DETAIL " +
                "WHERE C_BIZOPP_ID in ( " + inSql + ")  GROUP BY C_BIZOPP_ID,C_PRODUCT_MODEL";
        bizOppSumArgs.addAll(inArgs);

        List<Object> orderSumArgs = new ArrayList<>();
        //language=SQL
        String orderSumSql = "SELECT\n" +
                "        bizId,\n" +
                "        productModel,\n" +
                "        sum(orderQty) AS orderQty\n" +
                "      FROM (WITH Order_Line AS (SELECT ol.C_ORDER_ID as orderId, ol.C_PID as orderLineId\n" +
                "                                 FROM CRM_T_ORDER_LINES ol,CRM_T_ORDER_HEADER oh\n" +
                "                                 WHERE oh.C_PID=ol.C_ORDER_ID and oh.C_SOURCE_TYPE='20' and ol.C_SOURCE_ID in (" + inSql + ")";
        orderSumArgs.addAll(inArgs);
        if (StringUtils.isNotEmpty(orderNo)) {
            orderSumSql += " AND oh.C_ORDER_CODE != ? ";
            orderSumArgs.add(orderNo);
        }

        orderSumSql += "),\n" +
                "          Order_Change AS (SELECT\n" +
                "                       ohc.C_FROM_ID          AS orderId,\n" +
                "                       max(ohc.C_VERSION) AS C_VERSION\n" +
                "                     FROM CRM_T_ORDER_HEADER_CHANGE ohc,CRM_T_ORDER_LINES_CHANGE olc\n" +
                "                     WHERE ohc.C_EVENT_STATUS != '40'\n" +
                "                           and olc.C_SOURCE_ID in (" + inSql + ")";
        orderSumArgs.addAll(inArgs);
        if (StringUtils.isNotEmpty(orderNo)) {
            orderSumSql += " and ohc.C_ORDER_CODE != ?\n";
            orderSumArgs.add(orderNo);
        }
        orderSumSql +=
                "                     GROUP BY ohc.C_FROM_ID),\n" +
                        "          Order_Product_Count AS (SELECT\n" +
                        "                                    ol.C_ORDER_ID         AS orderId,\n" +
                        "                                    ol.C_PRO_MODEL        AS productModel,\n" +
                        "                                    ol.N_PRODUCT_QUANTITY AS orderQty,\n" +
                        "                                    ol.C_SOURCE_ID        AS bizId\n" +
                        "                                   FROM CRM_T_ORDER_HEADER oh, CRM_T_ORDER_LINES ol\n" +
                        "                                   WHERE oh.C_PID=ol.C_ORDER_ID\n" +
                        "                                         AND oh.C_CONTROL_STATUS != '40'\n" +
                        "                                         AND ol.C_ORDER_ID NOT IN (SELECT orderId FROM Order_Change)\n" +
                        "                                         AND ol.C_ORDER_ID IN (SELECT orderId FROM Order_Line)),\n" +
                        "          Order_Change_Product_Count AS (SELECT\n" +
                        "                                            olc.C_ORDER_ID         AS orderId,\n" +
                        "                                            olc.C_PRO_MODEL        AS productModel,\n" +
                        "                                            olc.N_PRODUCT_QUANTITY AS orderQty,\n" +
                        "                                            olc.C_SOURCE_ID        AS bizId\n" +
                        "                                          FROM CRM_T_ORDER_HEADER_CHANGE ohc, CRM_T_ORDER_LINES_CHANGE olc\n" +
                        "                                          WHERE ohc.C_PID = olc.C_ORDER_CHANGE_ID\n" +
                        "                                                AND (ohc.C_FROM_ID,ohc.C_VERSION) IN (SELECT orderId,C_VERSION FROM Order_Change))\n" +
                        "      SELECT * FROM Order_Product_Count r WHERE orderId NOT IN (SELECT orderId FROM Order_Change)\n" +
                        "      UNION ALL SELECT * FROM Order_Change_Product_Count)\n" +
                        "      GROUP BY bizId, productModel";
        List<Object> args = new ArrayList<>();
        //language=SQL
        String sql = "SELECT pd.bizId, pd.productModel,  nvl(pd.bizQty, 0) - nvl(rl.orderQty, 0) as surplusSum " +
                "FROM (" + bizOppSumSql + ") pd  " +
                "LEFT JOIN (" + orderSumSql + ") rl ON pd.bizId = rl.bizId AND pd.productModel = rl.productModel";
        args.addAll(bizOppSumArgs);
        args.addAll(orderSumArgs);
        List<Map<String, Object>> maps = this.baseDao.findBySql4Map(sql, args.toArray());
        Map<String, Map<String, Integer>> surplusSumMap = new HashMap<>();
        for (Map<String, Object> map : maps) {
            String bizId = (String) map.get("bizId".toUpperCase());
            String productModel = (String) map.get("productModel".toUpperCase());
            BigDecimal surplusSum = (BigDecimal) map.get("surplusSum".toUpperCase());
            if (!surplusSumMap.containsKey(bizId)) {
                surplusSumMap.put(bizId, new HashMap<String, Integer>());
            }
            Map<String, Integer> productSurplusSumMap = surplusSumMap.get(bizId);
            productSurplusSumMap.put(productModel, surplusSum.intValue());
        }

        return surplusSumMap;
    }


    /**
     * 判断是否是代理商平台代理商
     *
     * @param orderHeader
     * @return
     */
    private boolean isDistributionPlatform(OrderHeader orderHeader) {
        //是否分销平台代理商
        boolean isCust = false;
        String customerCode = StringUtil.isEmpty(orderHeader.getCustomerCode()) ? "" : orderHeader.getCustomerCode();

        List<LovMember> custLovs = lovMemberService.getListByGroupCode("CUSTOMERAMOUNT");
        if (custLovs != null && custLovs.size() > 0) {
            for (LovMember cust : custLovs) {
                if (customerCode.equals(cust.getCode())) {
                    isCust = true;
                    break;
                }
            }
        }
        return isCust;
    }


    @Override
    public IPage queryOrderHeaders(PageCondition condition) {

        FilterObject filterObject = condition.getFilterObject(OrderHeader.class);
        filterObject.addOrderBy("updatedAt", "desc");
        HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
        return baseDao.search(hqlObject.getHql(), hqlObject.getArgs(), condition.getRows(), condition.getPage());
    }

    @Override
    public OrderHeader queryOrderHeaderByCode(String orderCode) {
        String hql = "from OrderHeader o where o.orderCode = ?";
        return baseDao.findUniqueEntity(hql, new Object[]{orderCode});
    }

    /**
     * queryOrderHeaderChangeInApproval:查询在途的变更单. <br/>
     *
     * @param orderCode 订单编号
     * @return
     * @author liming
     * @since JDK 1.7
     */
    private boolean checkOrderHeaderChangeInApproval(String orderCode) {
        StringBuffer hql = new StringBuffer("from OrderHeaderChange o where o.orderCode = ? ");
        hql.append(" and ( o.eventStatus = ? or o.eventStatus = ? or o.eventStatus = ? ) ");
        List<Object> args = new ArrayList<Object>();
        args.add(orderCode);
        args.add(IConstants.ORDER_CONTROL_STATUS_10);
        args.add(IConstants.ORDER_CONTROL_STATUS_20);
        args.add(IConstants.ORDER_CONTROL_STATUS_40);
        List<OrderHeaderChange> orderHeaderChanges = baseDao.findEntity(hql.toString(), args.toArray());
        if (orderHeaderChanges != null && orderHeaderChanges.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public OrderHeader queryOrderHeaderBySourceId(String sourceId) {
        String hql = " from OrderHeader o where o.sourceId = ? order by o.createdAt";
        List<OrderHeader> orderHeaders = baseDao.findEntity(hql, new Object[]{sourceId});
        if (orderHeaders != null && orderHeaders.size() > 0) {
            return orderHeaders.get(0);
        } else {
            return null;
        }
    }

    @Override
    public void deleteOrder(String orderHeaderId, UserObject userObject) {
        OrderHeader orderHeader = baseDao.get(OrderHeader.class, orderHeaderId);

        if ((IConstants.ORDER_EXECUTE_STATUS_ENTERED.equals(orderHeader.getExecuteStatus())
                && !IConstants.ORDER_CONTROL_STATUS_10.equals(orderHeader.getControlStatus())
                && !IConstants.ORDER_CONTROL_STATUS_40.equals(orderHeader.getControlStatus()))
                || IConstants.ORDER_EXECUTE_STATUS_BOOKED.equals(orderHeader.getExecuteStatus())) {
            throw new AnneException("订单状态已不支持删除");
        }

        String hql = " from OrderLines line where line.orderId = ?";
        List<OrderLines> lines = baseDao.findEntity(hql, new Object[]{orderHeaderId});
        if (lines != null) {
            for (OrderLines line : lines) {
                if (line.getBillingQty() > 0) {
                    throw new AnneException("订单已开票不允许删除");
                }
                if (line.getDeliveryQty() > 0) {
                    throw new AnneException("订单已发货不允许删除");
                }

                //如果是合同这更新合同的orderCode
                if (IConstants.ORDER_SOURCE_CONTRACT.equals(orderHeader.getSourceType())) {
                    contractService.updatePrjLstOrderNoByContractID(orderHeader.getSourceId(), line.getSourceId(), "del", orderHeader.getOrderCode());
                }
                //删除订单行
                baseDao.delete(line);

            }
        }
        if (IConstants.ORDER_CONTROL_STATUS_40.equals(orderHeader.getExecuteStatus())) {
            String order_audit_app = lovMemberService.getFlowCodeByAppCode(IConstants.ORDER_AUDIT_FLOW_APP_ORDER_AUDIT);
            processService.closeByBusinessKey(order_audit_app, orderHeader.getId(), userObject.getParticipant(), "订单删除");
        }

        //删除订单头
        baseDao.delete(orderHeader);

    }

    @Override
    public void deleteOrderChange(String OrderHeaderChangeId, UserObject userObject) {
        OrderHeaderChange orderHeaderChange = baseDao.get(OrderHeaderChange.class, OrderHeaderChangeId);

        if ((IConstants.ORDER_EXECUTE_STATUS_ENTERED.equals(orderHeaderChange.getExecuteStatus())
                && !IConstants.ORDER_CONTROL_STATUS_10.equals(orderHeaderChange.getEventStatus())
                && !IConstants.ORDER_CONTROL_STATUS_40.equals(orderHeaderChange.getEventStatus()))
                || (IConstants.ORDER_EXECUTE_STATUS_BOOKED.equals(orderHeaderChange.getExecuteStatus())
                && !IConstants.ORDER_CONTROL_STATUS_10.equals(orderHeaderChange.getEventStatus())
                && !IConstants.ORDER_CONTROL_STATUS_40.equals(orderHeaderChange.getEventStatus()))) {
            throw new AnneException("变更订单状态已不支持删除");
        }

        String hql = " from OrderLinesChange line where line.orderChangeId = ?";
        List<OrderLinesChange> lines = baseDao.findEntity(hql, new Object[]{OrderHeaderChangeId});
        if (lines != null) {
            for (OrderLinesChange line : lines) {
                if (line.getBillingQty() > 0) {
                    throw new AnneException("订单已开票不允许删除");
                }
                if (line.getDeliveryQty() > 0) {
                    throw new AnneException("订单已发货不允许删除");
                }

                //如果是合同这更新合同的orderCode
                if (IConstants.ORDER_SOURCE_CONTRACT.equals(orderHeaderChange.getSourceType())) {
                    contractService.updatePrjLstOrderNoByContractID(orderHeaderChange.getSourceId(), line.getSourceId(), "del", orderHeaderChange.getOrderCode());
                }
                //删除订单行
                baseDao.delete(line);

            }
        }
        if (IConstants.ORDER_CONTROL_STATUS_40.equals(orderHeaderChange.getExecuteStatus())) {
            String order_audit_app = lovMemberService.getFlowCodeByAppCode(IConstants.ORDER_AUDIT_FLOW_APP_ORDER_AUDIT);
            processService.closeByBusinessKey(order_audit_app, orderHeaderChange.getId(), userObject.getParticipant(), "订单删除");
        }

        //删除订单头
        baseDao.delete(orderHeaderChange);

    }

    @Override
    public OrderHeader queryOrderHeaderById(String orderHeaderId) {
        return baseDao.get(OrderHeader.class, orderHeaderId);
    }

    @Override
    public IPage queryOrderLines(PageCondition condition) {
        FilterObject filterObject = condition.getFilterObject(OrderLines.class);
        filterObject.addOrderBy("erpLineNo", "asc");
        HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
        return baseDao.search(hqlObject.getHql(), hqlObject.getArgs(), condition.getRows(), condition.getPage());

    }

    @Override
    public List<Object[]> getKstarProductByOrderId(String orderId) {
        List<Object> args = new ArrayList<Object>();
        StringBuffer hql = new StringBuffer("select order, pro from OrderLines order , KstarProduct pro "
                + "where order.proId=pro.id and order.orderId = ? ");
        args.add(orderId);
        List<Object[]> ordreLines = baseDao.findEntity(hql.toString(), args.toArray());
        return ordreLines;

    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public List<OrderLines> getOrderLinesOrderId(String orderId) {
        List args = new ArrayList();
        StringBuffer hql = new StringBuffer(" from OrderLines order where order.orderId = ? ");
        args.add(orderId);
        List<OrderLines> ordreLines = baseDao.findEntity(hql.toString(), args.toArray());
        return ordreLines;

    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public List<OrderLinesChange> getOrderLinesChangeOrderId(String orderId) {
        List args = new ArrayList();
        StringBuffer hql = new StringBuffer(" from OrderLinesChange order where order.orderChangeId = ? ");
        args.add(orderId);
        List<OrderLinesChange> ordreLines = baseDao.findEntity(hql.toString(), args.toArray());
        return ordreLines;

    }

    @Override
    public List<OrderLines> getOrderLinesOrderCode(String orderCode) {
        List<Object> args = new ArrayList<Object>();
        StringBuffer hql = new StringBuffer(" from OrderLines orderLines where orderLines.orderCode = ? ");
        args.add(orderCode);
        List<OrderLines> ordreLines = baseDao.findEntity(hql.toString(), args.toArray());
        return ordreLines;

    }

    @Override
    public List<OrderLines> getOrderLinesErpOrderCode(String erpOrderCode) {
        List<Object> args = new ArrayList<Object>();
        StringBuffer hql = new StringBuffer("select distinct line from OrderHeader h, OrderLines line "
                + "	where h.id = line.orderId and h.erpOrderCode = ? ");
        args.add(erpOrderCode);
        List<OrderLines> ordreLines = baseDao.findEntity(hql.toString(), args.toArray());
        return ordreLines;

    }

    @Override
    public IPage queryOrderSelectList(PageCondition condition) {
        String customerId = condition.getStringCondition("customerId");
        String contractCode = condition.getStringCondition("contractCode");
        String orderCode = condition.getStringCondition("orderCode");
        String proModel = condition.getStringCondition("proModel");
        String isAdvanceBilling = condition.getStringCondition("isAdvanceBilling");
        String lineStatus = condition.getStringCondition("lineStatus");
        String businessEntity = condition.getStringCondition("businessEntity");
        String isErpDelivery = condition.getStringCondition("isErpDelivery");
        String currency = condition.getStringCondition("currency");
        //我司合同编号
        String sourceCode = condition.getStringCondition("sourceCode");
        String erpOrderCode = condition.getStringCondition("erpOrderCode");

        String business = condition.getStringCondition("business");

        List<Object> args = new ArrayList<Object>();
        StringBuffer hql = new StringBuffer(" select new  com.ibm.kstar.entity.order.vo.OrderVO( ");
        hql.append(" l.id,");
        hql.append(" l.proId,");
        hql.append(" h.businessEntity,");
        hql.append(" h.orderCode,");
        hql.append(" h.orderType,");
        hql.append(" h.customerId,");
        hql.append(" h.customerCode,");
        hql.append(" h.customerName,");
        hql.append(" h.customerPo,");
        hql.append(" h.deliveryAddressId,");
        hql.append(" h.deliveryAddress,");
        hql.append(" h.sourceType,");
        hql.append(" h.sourceCode,");
        hql.append(" h.sourceName,");
        hql.append(" l.erpSettPrice,");//将ERP结算价格作为产品单价
        hql.append(" l.proModel,");
        hql.append(" l.materielCode,");
        hql.append(" l.itemDescription,");
        hql.append(" l.proDesc,");
        hql.append(" l.unit,");
        hql.append(" l.proQty,");
        hql.append(" l.deliveryQty,");
        hql.append(" (l.proQty - l.deliveryQty) as nonDeliveryQty,");
        hql.append(" l.billingQty as billingQty,");
        hql.append(" l.createTime as orderDate,");
        hql.append(" l.requestDate ,");
        hql.append(" l.promiseDate,");
        hql.append(" l.status,");
        hql.append(" l.lineNo,");
        hql.append(" l.amount,");
        hql.append(" l.shipOrg,");
        hql.append(" h.erpOrderCode");
        hql.append(" )");
        hql.append(" from OrderHeader h , OrderLines l ");
        hql.append(" where h.id = l.orderId ");
        //已登记状态
        hql.append(" and (h.executeStatus = ? or h.executeStatus = ?) ");
        args.add(IConstants.ORDER_EXECUTE_STATUS_BOOKED);
        args.add(IConstants.ORDER_EXECUTE_STATUS_CLOSED);
        //订单控制状态不能为暂挂状态
        hql.append(" and h.controlStatus != ? ");
        args.add(IConstants.ORDER_CONTROL_STATUS_50);
        //订单数量必须大于0
        hql.append(" and l.proQty > 0 ");
        //订单行不能为暂挂状态
        hql.append(" and l.isPending != ? ");
        args.add(IConstants.YES_Yes);
        //订单控制状态不能为已取消
        hql.append(" and l.status != ? ");
        args.add(IConstants.ORDER_LINE_STATUS_CANCELLED);

        if (StringUtil.isEmpty(customerId)) {
            customerId = "";
        }

        if (!"ImportSale".equals(business)) {
            hql.append(" and h.customerId = ?");
            args.add(customerId);
        }

        if (StringUtil.isNotEmpty(businessEntity)) {
            hql.append(" and h.businessEntity = ? ");
            args.add(businessEntity);
        }

        if (StringUtil.isNotEmpty(currency)) {
            hql.append(" and h.currency = ?");
            args.add(currency);
        }

        if (StringUtil.isNotEmpty(lineStatus)) {
            if (IConstants.ORDER_LINE_STATUS_AWAITING_SHIPPING.equals(lineStatus)) {
                hql.append(" and l.deliveryQty = 0 ");
                //				hql.append(" and l.status = ? ");
                //				args.add(lineStatus);
                //				hql.append(" and l.erpStatus = ? ");
                //				args.add(lineStatus);
            } else if (IConstants.ORDER_LINE_STATUS_CLOSED.equals(lineStatus)) {
                hql.append(" and l.billingQty > 0 ");
                hql.append(" and l.status = ? ");
                args.add(lineStatus);
                hql.append(" and l.erpStatus = ? ");
                args.add(lineStatus);
            }
        }

        if (StringUtil.isNotEmpty(isAdvanceBilling)) {
            if (IConstants.NO_No.equals(isAdvanceBilling)) {
                hql.append(" and l.isAdvanceBilling != ? ");
                args.add(IConstants.YES_Yes);
                hql.append(" and l.billingQty = 0 ");
            } else {
                hql.append(" and l.isAdvanceBilling = ?");
                args.add(isAdvanceBilling);
            }
        }
        if (StringUtil.isNotEmpty(isErpDelivery)) {
            if (IConstants.NO_No.equals(isErpDelivery)) {
                hql.append(" and l.isErpDelivery != ? ");
                args.add(IConstants.YES_Yes);
            } else {
                hql.append(" and l.isErpDelivery = ?");
                args.add(isErpDelivery);
            }
        }
        if (StringUtil.isNotEmpty(contractCode)) {
            hql.append(" and h.sourceCode = ?");
            args.add(contractCode);
        }
        if (StringUtil.isNotEmpty(sourceCode)) {
            hql.append(" and h.sourceCode = ?");
            args.add(sourceCode);
        }
        if (StringUtil.isNotEmpty(orderCode)) {
            hql.append(" and h.orderCode like ? ");
            args.add("%" + orderCode + "%");
        }
        if (StringUtil.isNotEmpty(proModel)) {
            hql.append(" and l.proModel like ?");
            args.add("%" + proModel + "%");
        }
        if (StringUtil.isNotEmpty(erpOrderCode)) {
            hql.append(" and h.erpOrderCode like ? ");
            args.add("%" + erpOrderCode + "%");
        }
        hql.append(" and h.erpOrderCode is not null ");

        if ("ImportSale".equals(business)) {
            String type = condition.getStringCondition("type");
            if ("E".equals(type)) {
                String employeeId = condition.getStringCondition("employeeId");
                String positionId = condition.getStringCondition("positionId");
                hql.append(" and h.createdById=? and h.createdPosId=? ");
                args.add(employeeId);
                args.add(positionId);
            } else if ("ORG".equals(type)) {
                String orgId = condition.getStringCondition("orgId");
                hql.append(" and h.createdOrgId in (")
                        //language=HQL
                        .append("select org.id from LovMember org where org.path like ?")
                        .append(")");
                args.add("%" + orgId + "%");
            } else {
                hql.append(" and 1 !=1 ");
            }

            String importSaleId = condition.getStringCondition("importSaleId");
            if (StringUtil.isNotEmpty(importSaleId)) {
                hql.append(" and not exists (")
                        //language=HQL
                        .append("select 1 from ImportSaleApply isa,ImportSaleApplyDetail isad where isa.id=isad.applyId and isad.orderCode=h.orderCode and isad.orderLineNo=l.lineNo")
                        .append(") ");
            }
        } else {
            String perHql = PermissionUtil.getPermissionHQL(null, "h.createdById", "h.createdPosId", "h.createdOrgId", "h.id",
                    (UserObject) condition.getCondition("userObject"), IConstants.PERMISSION_BUSINESS_TYPE_ORDER);
            hql.append(" AND " + perHql);
        }
        hql.append(" order by h.orderCode desc , l.updatedAt desc ");

        return baseDao.search(hql.toString(), args.toArray(), condition.getRows(), condition.getPage());

    }

    /**
     * TODO 更新订单状态.
     *
     * @throws Exception
     * @see com.ibm.kstar.api.order.IOrderService#updateOrderStatus(java.lang.String, java.lang.String)
     */
    @Override
    public void updateOrderExecuteStatus(String orderId, String status, UserObject userObject) throws Exception {
    	OrderHeader orderHeader = baseDao.get(OrderHeader.class, orderId);
    	Exception exception = new Exception();
    	MethodLogger methodLogger = new MethodLogger();
    	try {
            if (orderHeader == null) {
                throw new AnneException("没有找到要更新的数据");
            }
            methodLogger = methodLogService.getMethodLogger("com.ibm.kstar.impl.order.OrderServiceImpl.updateOrderExecuteStatus",orderHeader.getOrderCode());
            methodLogService.setFunctionNameAndParameter(methodLogger, "baseDao.get(OrderHeader.class, orderId)", 1, orderId);
    	}catch(Exception e) {
    		e.printStackTrace();
    		exception = e;
    		throw e;
    	}finally {
    		methodLogService.setReturnDataNotes(false,methodLogger,exception,1,orderHeader);
    	}
        
        if (IConstants.ORDER_EXECUTE_STATUS_BOOKED.equals(status)) {
            //订单登记
        	methodLogService.setFunctionNameAndParameter(methodLogger, "this.checkOrderBook(orderHeader.getOrderCode())", 2, orderHeader.getOrderCode());
        	Map<String, String> ret = new HashMap<String, String>();
        	try {
	            ret = this.checkOrderBook(orderHeader.getOrderCode());
	            if (!"S".equals(ret.get("status"))) {
	                throw new AnneException("操作失败，调用ERP接口校验失败: " + ret.get("msg"));
	            }
        	}catch(Exception e) {
        		e.printStackTrace();
        		exception = e;
        		throw e;
        	}finally {
        		methodLogService.setReturnDataNotes(false,methodLogger,exception,2,ret);
        	}
        	
        	methodLogService.setFunctionNameAndParameter(methodLogger, "this.updateOrderLinesStatusByHeaderId(orderHeader.getId(), IConstants.ORDER_LINE_STATUS_AWAITING_SHIPPING, userObject)",
        			3, orderHeader.getId(), IConstants.ORDER_LINE_STATUS_AWAITING_SHIPPING, userObject);
        	try {
	            if (IConstants.ORDER_EXECUTE_STATUS_ENTERED.equals(orderHeader.getExecuteStatus())
	                    && IConstants.ORDER_CONTROL_STATUS_30.equals(orderHeader.getControlStatus())) {
	                orderHeader.setExecuteStatus(status);
	                //如果是已登记审核通过，将订单行状态改为改为待发运
	                this.updateOrderLinesStatusByHeaderId(orderHeader.getId(), IConstants.ORDER_LINE_STATUS_AWAITING_SHIPPING, userObject);
	            } else {
	                //如果不是已录入状态
	                throw new AnneException("订单状态不正确");
	            }
        	}catch(Exception e) {
        		e.printStackTrace();
        		exception = e;
        		throw e;
        	}finally {
        		methodLogService.setReturnDataNotes(false,methodLogger,exception,3,"void");
        	}    
        }

        methodLogService.setFunctionNameAndParameter(methodLogger, "this.update(orderHeader, userObject)",
    			4, orderHeader, userObject);
        try {
        	this.update(orderHeader, userObject);
        }catch(Exception e) {
    		e.printStackTrace();
    		exception = e;
    		throw e;
    	}finally {
    		methodLogService.setReturnDataNotes(true,methodLogger,exception,4,"void");
    	}    
    }

    /**
     * TODO 修改订单控制状态.
     *
     * @see com.ibm.kstar.api.order.IOrderService#updateOrderControlStatus(java.lang.String, java.lang.String)
     */
    @Override
    public void updateOrderControlStatus(String orderId, String status, UserObject userObject) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        OrderHeader orderHeader = baseDao.get(OrderHeader.class, orderId);
        if (orderHeader == null) {
            throw new AnneException(IOrderService.class.getName()
                    + " saveOrderHeader : 没有找到要更新的数据");
        }
        orderHeader.setControlStatus(status);
        this.update(orderHeader, userObject);

        if (IConstants.ORDER_EXECUTE_STATUS_ENTERED.equals(orderHeader.getExecuteStatus())
                && IConstants.ORDER_CONTROL_STATUS_20.equals(status) && userObject != null) {
            //如果是已录入状态，审核通过，启动订单提交工作流
            if (userObject != null) {
                Map<String, String> varmap = new HashMap<String, String>();
                varmap.put("title", "订单提交 - " + orderHeader.getOrderCode() + "|" + userObject.getEmployee().getName() + "|" + sdf.format(new Date()));
                varmap.put("app", IConstants.ORDER_AUDIT_FLOW_APP_ORDER_AUDIT);
                String salesCenter = orderHeader.getSalesmanCenter();
                if (StringUtil.isNotEmpty(salesCenter)) {
                    salesCenter = orderHeader.getLovCode(salesCenter);
                }
                varmap.put("SalesCenter", salesCenter);
                varmap.put("employeeIdInForm", orderHeader.getBusinessManagerId());
                varmap.put("employeeNameInForm", orderHeader.getBusinessManagerName());

                String employeeType = "";
                if (userObject != null && userObject.getOrg() != null) {
                    employeeType = userObject.getOrg().getOptTxt3();
                }
                varmap.put("EmployeeType", employeeType);

                String order_audit_app = lovMemberService.getFlowCodeByAppCode(IConstants.ORDER_AUDIT_FLOW_APP_ORDER_AUDIT);
                //Participant participant= userObject.getParticipant();
                xflowProcessServiceWrapper.start(order_audit_app, orderId, userObject, varmap);
            }
        }
    }

    /**
     * TODO 根据订单行ID更新订单行状态.
     *
     * @see com.ibm.kstar.api.order.IOrderService#updateOrderLinesStatus(com.ibm.kstar.entity.order.OrderHeader)
     */
    @Override
    public void updateOrderLinesStatus(String id, String status, UserObject userObject) throws Exception {
        OrderLines orderLines = baseDao.get(OrderLines.class, id);
        if (orderLines != null) {
            orderLines.setStatus(status);
            //根据订单状态更新更新发货数量
            if (IConstants.ORDER_LINE_STATUS_PICKED.equals(status)) {
                orderLines.setDeliveryQty(orderLines.getProQty());
            } else if (IConstants.ORDER_LINE_STATUS_AWAITING_SHIPPING.equals(status)) {
                orderLines.setDeliveryQty(0);
            }
            this.update(orderLines, userObject);
        }
    }

    /**
     * TODO 根据订单头ID更新所有订单行状态
     *
     * @see com.ibm.kstar.api.order.IOrderService#updateOrderLinesStatus(com.ibm.kstar.entity.order.OrderHeader)
     */
    @Override
    public void updateOrderLinesStatusByHeaderId(String orderHeaderId, String status, UserObject userObject) throws Exception {

        String hql = "from OrderLines line where line.orderId = ?";
        List<OrderLines> orderLinesList = baseDao.findEntity(hql, new Object[]{orderHeaderId});
        for (OrderLines orderLines : orderLinesList) {
            if (orderLines == null) {
                throw new AnneException(IOrderService.class.getName()
                        + " updateOrderLinesStatusByHeaderId : 没有找到要更新的数据");
            } else {
                // 0单价产品订单的签订逻辑;2017-10-09  黄奇
                // if (orderLines.getErpSettPrice() == null || orderLines.getErpSettPrice().doubleValue() == 0) {
                //     throw new AnneException("存在ERP结算价格为0的订单行，不允许登记");
                // }
            }
            orderLines.setStatus(status);
            this.update(orderLines, userObject);
        }
    }

    /**
     * TODO 更新订单行开票状态.
     *
     * @see com.ibm.kstar.api.order.IOrderService#updateOrderLinesInvoiceStatus(com.ibm.kstar.entity.order.OrderHeader)
     */
    @Override
    public void updateOrderLinesInvoiceStatus(String id, String status, UserObject userObject) throws Exception {
        if (StringUtil.isNotEmpty(id)) {
            OrderLines orderLines = baseDao.get(OrderLines.class, id);
            if (orderLines != null) {
                orderLines.setIsAdvanceBilling(status);
                if (IConstants.YES_Yes.equals(status)) {
                    orderLines.setBillingQty(orderLines.getProQty());
                } else if (IConstants.NO_No.equals(status)) {
                    orderLines.setBillingQty(0);
                }
                this.update(orderLines, userObject);
            }
        }
    }

    @Override
    public void splitLine(String op, String orderLineId, String deliveryLineId, double quantity, UserObject userObject) throws Exception {
        if (orderLineId != null) {
        	MethodLogger methodLogger = new MethodLogger();
        	Exception exception = new Exception();
        	OrderLines orderLine = new OrderLines();
        	try {
        		orderLine = baseDao.get(OrderLines.class, orderLineId);
        		OrderHeader orderHeader = baseDao.get(OrderHeader.class, orderLine.getOrderId());
	            if (orderLine == null) {
	                throw new AnneException(IOrderService.class.getName()
	                        + " splitLine : 拆分失败，没有找到拆分订单!");
	            }
	            methodLogger = methodLogService.getMethodLogger("com.ibm.kstar.impl.order.OrderServiceImpl.splitLine",orderHeader.getOrderCode());
	            methodLogService.setFunctionNameAndParameter(methodLogger, "baseDao.get(OrderLines.class, orderLineId)", 1, orderLineId);
	            if ((orderLine.getProQty() - orderLine.getCancelQty()) <= quantity) {
	                throw new AnneException(IOrderService.class.getName()
	                        + " splitLine : 拆分失败，本次拆分数量大于订单行可拆分数量!");
	            }
	            if (IConstants.YES_Yes.equals(orderLine.getIsPending())) {
	                throw new AnneException(IOrderService.class.getName()
	                        + " splitLine : 拆分失败，订单行已暂挂不允许拆分!");
	            }
	            if (IConstants.YES_Yes.equals(orderLine.getIsErpDelivery())) {
	                throw new AnneException(IOrderService.class.getName()
	                        + " splitLine : 拆分失败，订单ERP已发货不允许拆分!");
	            }
	            /*if("delivery".equals(op) && (IConstants.YES_Yes.equals(orderLine.getIsAdvanceBilling()) || orderLine.getBillingQty() > 0) ){
	                throw new AnneException(IOrderService.class.getName()
							+ " splitLine : 拆分失败，订单已经开票，不允许拆分!");
				}*/
	            if ("order".equals(op) && !IConstants.ORDER_LINE_STATUS_ENTERED.equals(orderLine.getStatus())
	                    && !IConstants.ORDER_LINE_STATUS_AWAITING_SHIPPING.equals(orderLine.getStatus())) {
	                throw new AnneException(IOrderService.class.getName()
	                        + " splitLine : 拆分失败，订单行当前状态不支持拆分！");
	            }
	            if (IConstants.YES_Yes.equals(orderLine.getIsAdvanceBilling()) || orderLine.getBillingQty() > 0) {
	                if (!invoiceService.checkInvoiceStatus(orderLineId)) {
	                    throw new AnneException(IOrderService.class.getName()
	                            + " splitLine : 拆分失败，订单行存在未审核的开票申请，不允许拆分!");
	                }
	            }
        	}catch(Exception e) {
        		e.printStackTrace();
        		exception = e;
        		throw e;
        	}finally {
        		methodLogService.setReturnDataNotes(false,methodLogger,exception,1,orderLine);
        	}
	            
            //计算剩余产品数量
            double proQty = orderLine.getProQty() - quantity;
            if (proQty > 0) {
                if (IConstants.ORDER_LINE_STATUS_PICKED.equals(orderLine.getStatus())) {
                    double deliveryQty = orderLine.getDeliveryQty() - quantity;
                    orderLine.setDeliveryQty(deliveryQty);
                    methodLogService.setFunctionNameAndParameter(methodLogger, "deliveryService.updateDeliveryLineQtyByID(deliveryLineId, deliveryQty, userObject)", 2, deliveryLineId, deliveryQty, userObject);
                    try {
	                    if (StringUtil.isNotEmpty(deliveryLineId)) {
	                    		//更新发货单行数量
		                        deliveryService.updateDeliveryLineQtyByID(deliveryLineId, deliveryQty, userObject);
	                    }else {
	                    	List<DeliveryLines> deliveryLinesList = deliveryService.getDeliveryLinesByOrderLineId(orderLineId);
	                    	if(deliveryLinesList.size()>0) {
	                    		throw new AnneException("订单拆行失败，所拆行已有出货申请单，请在出货申请单上进行拆行！");
	                    	}
	                    }
                    }catch(Exception e) {
                		e.printStackTrace();
                		exception = e;
                		throw e;
                	}finally {
                		methodLogService.setReturnDataNotes(false,methodLogger,exception,2,"void");
                	}  
                }
                
                methodLogService.setFunctionNameAndParameter(methodLogger, "this.update(orderLine, null)",3,orderLine);
                BigDecimal erpSettPrice = new BigDecimal(0);
                try {
                	erpSettPrice = orderLine.getErpSettPrice() == null ? new BigDecimal(0) : orderLine.getErpSettPrice();
	                orderLine.setAmount(erpSettPrice.multiply(new BigDecimal(proQty)));
	                orderLine.setProQty(proQty);
	                if (IConstants.YES_Yes.equals(orderLine.getIsAdvanceBilling())) {
	                    orderLine.setBillingQty(orderLine.getProQty());
	                }
	                this.update(orderLine, null);
                }catch(Exception e) {
            		e.printStackTrace();
            		exception = e;
            		throw e;
            	}finally {
            		methodLogService.setReturnDataNotes(false,methodLogger,exception,3,"void");
            	}  
	            
                
                OrderLines newOrderLine = new OrderLines();
                methodLogService.setFunctionNameAndParameter(methodLogger, " this.save(newOrderLine, null)",4,newOrderLine);
                try {
	                BeanUtils.copyPropertiesIgnoreNull(orderLine, newOrderLine);
	                newOrderLine.setId(null);
	                newOrderLine.setAmount(erpSettPrice.multiply(new BigDecimal(quantity)).setScale(6, BigDecimal.ROUND_HALF_UP));
	                newOrderLine.setProQty(quantity);
	                /**-----------------新单号规则-------------------**/
	                //					newOrderLine.setOriginalLineId(orderLine.getId());
	                String OriginalLineId = "";
	                String rootLineNum = "";
	                if (StringUtil.isEmpty(orderLine.getOriginalLineId())) {
	                    OriginalLineId = orderLine.getId();
	                    rootLineNum = orderLine.getLineNo();
	                } else {
	                    OriginalLineId = orderLine.getOriginalLineId();
	                    rootLineNum = orderLine.getRootLineNum();
	                }
	                newOrderLine.setOriginalLineId(OriginalLineId);
	                /**-----------------新单号规则-------------------**/
	                newOrderLine.setLineNo(this.getLineNumber(OriginalLineId, orderLine));
	
	                newOrderLine.setParentLineNum(orderLine.getLineNo());
	                newOrderLine.setRootLineNum(rootLineNum);
	                /**-----------------订单时间在切换时间之前用原来的规则------------------**/
	                //初始化值
	                newOrderLine.setCancelQty(0);
	                newOrderLine.setDeliveryQty(0);
	                newOrderLine.setBillingQty(0);
	
	                if (IConstants.ORDER_LINE_STATUS_PICKED.equals(newOrderLine.getStatus())) {
	                    newOrderLine.setStatus(IConstants.ORDER_LINE_STATUS_AWAITING_SHIPPING);
	                    newOrderLine.setErpStatus(IConstants.ORDER_LINE_STATUS_AWAITING_SHIPPING);
	                }
	                newOrderLine.setIsErpDelivery(IConstants.NO_No);
	
	                if (IConstants.YES_Yes.equals(newOrderLine.getIsAdvanceBilling())) {
	                    newOrderLine.setBillingQty(newOrderLine.getProQty());
	                }
	
	                this.save(newOrderLine, null);
                }catch(Exception e) {
            		e.printStackTrace();
            		exception = e;
            		throw e;
            	}finally {
            		methodLogService.setReturnDataNotes(false,methodLogger,exception,4,"void");
            	}      
                
                
                methodLogService.setFunctionNameAndParameter(methodLogger, " this.checkOrderSplitLineSave(orderLine.getOrderCode(), orderLine.getLineNo(),\r\n" + 
                		"	                        newOrderLine.getLineNo(), newOrderLine.getProQty())",5,orderLine.getOrderCode(), orderLine.getLineNo(),
                        newOrderLine.getLineNo(), newOrderLine.getProQty());
                Map<String, String> ret = new HashMap<String,String>();
                try {
	                //保存前校验
	                ret = this.checkOrderSplitLineSave(orderLine.getOrderCode(), orderLine.getLineNo(),
	                        newOrderLine.getLineNo(), newOrderLine.getProQty());
	                
	                if (!"S".equals(ret.get("status"))) {
	                    throw new AnneException(IOrderService.class.getName()
	                            + " splitLine : 拆分失败，ERP操作失败:" + ret.get("msg"));
	                }
                }catch(Exception e) {
            		e.printStackTrace();
            		exception = e;
            		throw e;
            	}finally {
            		methodLogService.setReturnDataNotes(true,methodLogger,exception,5,ret);
            	}       

            } else {
                throw new AnneException(IOrderService.class.getName()
                        + " splitLine : 拆分失败，本次拆分数量超过订单产品数量！");
            }
        }
    }

    public String getLineNumber(String getOriginalLineId, OrderLines orderLine) {
        StringBuilder sql = new StringBuilder();

        sql.append(" select  MAX(SUBSTR(line.c_Line_No,0,INSTR(LINE.C_LINE_NO,'.',1))) " +
                "|| (MAX(TO_NUMBER(substr(line.c_line_no, instr(line.c_line_no, '.', 1) + 1, 1000)))+1)		");
        sql.append(" from crm_t_order_lines line																");
        sql.append(" where line.c_original_line_id = ?															");

        String lineNo = baseDao.findUniqueBySql(sql.toString(), getOriginalLineId);
        if (lineNo == null) {
            return orderLine.getLineNo() + ".1";
        }

        return lineNo;
    }

    /**
     * getLineNo:获取订单行号. <br/> . <br/>
     *
     * @param orderId 订单主表ID
     * @return
     * @author liming
     * @since JDK 1.7
     */
    public String getLineNo(String orderId) {
        if (StringUtil.isEmpty(orderId)) {
            return "1";
        }
        String hql = "select line.lineNo from OrderLines line where line.orderId = ? ";
        String lineNo = "";
        List<Integer> lineNoIntLst = new ArrayList<Integer>();
        List<String> lineNoList = baseDao.findEntity(hql, new Object[]{orderId});
        if (lineNoList == null || lineNoList.size() <= 0) {
            lineNo = "1";
        } else {
            for (String it : lineNoList) {
                if (it == null || it == "") {
                    it = "0";
                }
                int index = it.indexOf(".");
                if (index > 0) {
                    it = it.substring(0, index);
                }
                lineNoIntLst.add(Integer.parseInt(it));
            }
            int maxLineNo = Collections.max(lineNoIntLst);
            int n = maxLineNo + 1;
            lineNo = String.valueOf(n);
        }
        return lineNo;
    }


    /**
     * getLineNo:获取订单变更行号. <br/> . <br/>
     *
     * @param orderId 订单主表ID
     * @return
     * @author liming
     * @since JDK 1.7
     */
    public String getChangeLineNo(String orderId) {
        if (StringUtil.isEmpty(orderId)) {
            return "1";
        }
        String hql = "select line.lineNo from OrderLinesChange line where line.orderChangeId = ? ";
        String lineNo = "";
        List<Integer> lineNoIntLst = new ArrayList<Integer>();
        List<String> lineNoList = baseDao.findEntity(hql, new Object[]{orderId});
        if (lineNoList == null || lineNoList.size() <= 0) {
            lineNo = "1";
        } else {
            for (String it : lineNoList) {
                if (it == null || it == "") {
                    it = "0";
                }
                int index = it.indexOf(".");
                if (index > 0) {
                    it = it.substring(0, index);
                }
                lineNoIntLst.add(Integer.parseInt(it));
            }
            int maxLineNo = Collections.max(lineNoIntLst);
            int n = maxLineNo + 1;
            lineNo = String.valueOf(n);
        }
        return lineNo;
    }


    @SuppressWarnings("unused")
    public BigDecimal getContractOrderAmount(String contractId) {
        String hql = "select distinct linefrom OrderHeader h , OrderLines line "
                + " where h.id = line.orderId and h.sourceType = ? and h.sourceId = ? ";
        List<Object> args = new ArrayList<Object>();

        return null;
    }

    /**
     * TODO 合同创建订单时调用此方法生成订单头.
     *
     * @see com.ibm.kstar.api.order.IOrderService#createOrderHeaderByContract(java.lang.String, com.ibm.kstar.api.system.permission.UserObject)
     */
    @Override
    public OrderHeader createOrderHeaderByContract(OrderHeader orderHeader, String contractId) {

        Contract contract = baseDao.get(Contract.class, contractId);
        if (contract != null) {
            String custId = contract.getErpOrderCustId();
            if (StringUtil.isEmpty(custId)) {
                custId = contract.getCustCode();
            }
            CustomInfo customInfo = customInfoService.getCustomInfo(custId);
            orderHeader.setSourceType(IConstants.ORDER_SOURCE_CONTRACT);
            orderHeader.setSourceId(contract.getId());//合同ID
            orderHeader.setSourceCode(contract.getContrNo());//合同编号
            orderHeader.setSourceName(contract.getContrName());//合同名称
            orderHeader.setCustomerPo(contract.getCustContrNo());//客户PO

            if (customInfo != null) {
                orderHeader.setCustomerId(customInfo.getId());
                orderHeader.setCustomerCode(customInfo.getCustomCode());//客户编码
                orderHeader.setCustomerErpCode(customInfo.getErpCode());//客户ERP编码
                orderHeader.setCustomerName(customInfo.getCustomFullName());//客户名称
                orderHeader.setPaymentCustId(customInfo.getId());//付款客户ID
                orderHeader.setPaymentCustName(customInfo.getCustomFullName());//付款客户名称
                String hql1 = "from CustomErpInfo c where c.customId = ? and c.corpErpUnit = ? order by updatedAt desc";
                List<CustomErpInfo> customErpInfo = baseDao.findEntity(hql1, new Object[]{customInfo.getId(), contract.getBussEnity()});
                if (customErpInfo != null && customErpInfo.size() > 0) {
                    CustomAddressInfo customAddressInfo = baseDao.get(CustomAddressInfo.class, customErpInfo.get(0).getCorpLeadedAddress());
                    if (customAddressInfo != null) {
                        orderHeader.setDeliveryAddressId(customAddressInfo.getId());//收货地址ID
                        orderHeader.setDeliveryAddress(customAddressInfo.getArea());//收货地址
                    }
                }
            }
            orderHeader.setCustAttnCode(contract.getCustLinkId());//客户联系人ID
            orderHeader.setCustAttnName(contract.getCustLink());//客户联系人名称
            orderHeader.setBusinessEntity(contract.getBussEnity());//业务实体ID
            orderHeader.setIsInstall(contract.getIsHomeInstall());//是否上门安装
            orderHeader.setIsAm(contract.getIsAux());//是否提供所需辅材
            orderHeader.setIsHomeDelivery(contract.getIsDelivHome());//是否送货上门
            orderHeader.setIsDestinationDelivery(contract.getIsUnload());//是否卸货到指定地点
            if (StringUtil.isNotEmpty(contract.getPricNo())) {
                ProductPriceHead priceHead = baseDao.get(ProductPriceHead.class, contract.getPricNo());
                if (priceHead != null) {
                    orderHeader.setPriceTableId(contract.getPricNo());//价目表ID
                    orderHeader.setPriceTableName(priceHead.getPriceTableName());//价目表名称
                }
            }
            orderHeader.setRequestDate(contract.getDelivDate());//请求日期、合同要货日期
            //订单类型
            LovMember contractType = lovMemberService.get(contract.getContrType());
            if (contractType != null && (IConstants.CONTR_PI_0101.equals(contractType.getCode())
                    || IConstants.CONTR_STAND_0201.equals(contractType.getCode())
                    || IConstants.CONTR_STAND_0202.equals(contractType.getCode()))) {
                orderHeader.setOrderType("10120");//国际销售订单A
            } else {
                orderHeader.setOrderType("10110");//国内销售不成套
            }
            //订单类型
            LovMember currency = lovMemberService.get(contract.getCurrency());
            if (currency != null) {
                orderHeader.setCurrency(currency.getCode());
            }
            //创建人
            if (StringUtil.isNotEmpty(contract.getCreator())) {
                Employee employee = employeeService.get(contract.getCreator());
                if (employee != null) {
                    orderHeader.setSalesmanId(employee.getId());//销售人员ID
                    orderHeader.setSalesmanCode(employee.getNo());//销售人员编码
                    orderHeader.setSalesmanName(employee.getName());//销售人员名称
                }
            }
            //获取当前登录人员销售中心和销售部门
            if (StringUtil.isNotEmpty(contract.getOrg())) {
                LovMember salesmanCenter = this.getOrderSalesmanCenter(contract.getOrg());
                orderHeader.setSalesmanCenter(salesmanCenter == null ? null : salesmanCenter.getId());//营销中心
                if (salesmanCenter != null) {
                    LovMember salesmanDep = this.getOrderSalesmanDep(contract.getOrg(), salesmanCenter.getId());
                    orderHeader.setSalesmanDep(salesmanDep == null ? null : salesmanDep.getId());//销售部门
                }
            }
            orderHeader.setRemark(contract.getRemark());

        }
        return orderHeader;
    }

    /**
     * TODO 商机生成订单.
     *
     * @see com.ibm.kstar.api.order.IOrderService#createOrderHeaderByBizopp(java.lang.String, com.ibm.kstar.api.system.permission.UserObject)
     */
    @Override
    public OrderHeader createOrderHeaderByBizopp(OrderHeader orderHeader, String bizappId, UserObject userObject) {

        BusinessOpportunity bizOpp = baseDao.get(BusinessOpportunity.class, bizappId);
        if (bizOpp != null) {

            orderHeader.setSourceType(IConstants.ORDER_SOURCE_BIZ);
            orderHeader.setSourceId(bizOpp.getId());//商机ID
            orderHeader.setSourceCode(bizOpp.getNumber());//商机编号
            orderHeader.setSourceName(bizOpp.getOpportunityName());//商机名称
            LovMember lovOrg = lovMemberService.getLovMemberByCode("INVENTORY", "101");
            orderHeader.setShipOrg(lovOrg.getId());//发货组织
            LovMember lovBusinessEntity = lovMemberService.getLovMemberByCode("OPERATION_UNIT", "101");
            orderHeader.setBusinessEntity(lovBusinessEntity.getId());//业务实体

            Employee employee = employeeService.get(bizOpp.getCreatedById());


            orderHeader.setSalesmanId(bizOpp.getCreatedById());//销售人员ID
            orderHeader.setSalesmanCode(employee.getNo());//销售人员编码
            orderHeader.setSalesmanName(employee.getName());//销售人员名称

            //获取当前登录人员销售中心和销售部门
            LovMember salesmanCenter = this.getOrderSalesmanCenter(bizOpp.getCreatedOrgId());
            orderHeader.setSalesmanCenter(salesmanCenter == null ? null : salesmanCenter.getId());//营销中心
            if (salesmanCenter != null) {
                LovMember salesmanDep = this.getOrderSalesmanDep(bizOpp.getCreatedOrgId(), salesmanCenter.getId());
                orderHeader.setSalesmanDep(salesmanDep == null ? null : salesmanDep.getId());//销售部门
            }
            orderHeader.setRequestDate(new Date());//请求日期

            //LovMember orgLovMember = lovMemberService.get(bizOpp.getCreatedOrgId());
            if (userObject != null && !userObject.isInner() && userObject.getOrg() != null) {
                CustomInfo customInfo = customInfoService.getCustomInfo(userObject.getOrg().getOptTxt4());
                if (customInfo != null) {
                    orderHeader.setCustomerId(customInfo.getId());
                    orderHeader.setCustomerCode(customInfo.getCustomCode());//客户编码
                    orderHeader.setCustomerErpCode(customInfo.getErpCode());//客户ERP编码
                    orderHeader.setCustomerName(customInfo.getCustomFullName());//客户名称
                    orderHeader.setPaymentCustId(customInfo.getId());//付款客户ID
                    orderHeader.setPaymentCustName(customInfo.getCustomFullName());//付款客户名称
                }
            }
            //最终用户
            CustomInfo finalCustomInfo = customInfoService.getCustomInfo(bizOpp.getClientId());
            if (finalCustomInfo != null) {
                orderHeader.setFinalCustId(finalCustomInfo.getId());//最终用户ID
                orderHeader.setFinalCustName(finalCustomInfo.getCustomFullName());//最终用户名称
                //取行业大小类
                LovMember lovFinalCustTradeS = lovMemberService.get(finalCustomInfo.getCustomCategory());
                if (lovFinalCustTradeS != null) {
                    if (!"ROOT".equals(lovFinalCustTradeS.getParentId())) {
                        orderHeader.setFinalCustTradeS(lovFinalCustTradeS.getId());
                        LovMember lovFinalCustTradeB = lovMemberService.get(lovFinalCustTradeS.getParentId());
                        if (lovFinalCustTradeB != null) {
                            orderHeader.setFinalCustTradeB(lovFinalCustTradeB.getId());
                        }
                    } else {
                        orderHeader.setFinalCustTradeB(lovFinalCustTradeS.getId());
                    }
                }
            }
        }

        return orderHeader;
    }


    /**
     * TODO 合同创建订单时调用此方法生成订单行.
     *
     * @throws Exception
     * @see com.ibm.kstar.api.order.IOrderService#createOrderLinesByContract(java.lang.String)
     */
    @Override
    public List<OrderLines> createOrderLinesByContract(String contractId, UserObject userObject) throws Exception {
        List<OrderLines> orderLines = null;
        Contract contract = baseDao.get(Contract.class, contractId);
        if (contract != null) {
            String hql = "from KstarPrjLst k where k.quotCode = ?";
            List<KstarPrjLst> kstarPrjLsts = baseDao.findEntity(hql, new Object[]{contract.getId()});
            orderLines = this.createOrderLinesByKstarPrjLst(kstarPrjLsts, contract);
        }
        return orderLines;
    }

    /**
     * TODO 根据商机ID生成订单行.
     *
     * @see com.ibm.kstar.api.order.IOrderService#createOrderLinesByBizopp(java.lang.String, com.ibm.kstar.api.system.permission.UserObject)
     */
    @Override
    public List<OrderLines> createOrderLinesByBizopp(String bizOppId, UserObject userObject) throws Exception {
        List<OrderLines> orderLines = new ArrayList<>();
        if (StringUtil.isNotEmpty(bizOppId)) {
            BusinessOpportunity bizopp = baseDao.get(BusinessOpportunity.class, bizOppId);
            if (bizopp != null) {
                String hql = "from ProductDetail p where p.bizOppId = ?";
                List<ProductDetail> productDetails = baseDao.findEntity(hql, new Object[]{bizopp.getId()});
                for (ProductDetail productDetail : productDetails) {
                    KstarProduct product = baseDao.get(KstarProduct.class, productDetail.getProductId());
                    if (product != null) {
                        OrderLines lines = new OrderLines();
                        lines.setProId(product.getId());//产品ID
                        lines.setItemDescription(product.getProDesc());//项目说明
                        lines.setProModel(product.getProModel());//产品型号
                        lines.setSourceId(productDetail.getId()); //项目配置明细ID
                        lines.setProQty(productDetail.getPlanCount());//产品数量
                        lines.setPrice(productDetail.getPlanPrice());//产品价格
                        lines.setErpSettPrice(productDetail.getPlanPrice());//ERP结算价格
                        lines.setAmount(productDetail.getPlanTotal());//产品金额
                        lines.setUnit(product.getUnitName());//单位
                        lines.setMaterielCode(product.getMaterCode());//物流编码
                        lines.setRequestDate(new Date());//请求日期
                        lines.setPromiseDate(new Date());//承诺日期
                        //初始化值
                        initOrderLines(lines);

                        orderLines.add(lines);
                    }
                }
            }
        }

        return orderLines;
    }

    private List<OrderLines> createOrderLinesByKstarPrjLst(List<KstarPrjLst> kstarPrjLsts, Contract contract) {
        List<OrderLines> orderLines = new ArrayList<OrderLines>();
        for (KstarPrjLst kstarPrjLst : kstarPrjLsts) {

            //产品物料号为空无法下单
            if (StringUtil.isEmpty(kstarPrjLst.getMaterCode())) {
                continue;
            }

            String proId = kstarPrjLst.getProId();
            if (StringUtil.isNotEmpty(proId) && kstarPrjLst.getMaterCode() != null) {
                KstarProduct product = baseDao.get(KstarProduct.class, proId);
                if (product != null) {
                    //获取订单数量
                    OrderQuantityVo quantityVo = this.getContractOrderQty(contract.getContrNo(), kstarPrjLst.getLineNum());
                    double orderQty = 0; //已下单数量
                    if (quantityVo != null) {
                        orderQty = quantityVo.getProQty();
                    }

                    double proQty = 0;
                    if (kstarPrjLst.getNotVeriNum() != null) {
                        proQty = kstarPrjLst.getNotVeriNum() - orderQty;
                    } else {
                        proQty = kstarPrjLst.getAmt() == null ? 0 : kstarPrjLst.getAmt() - orderQty;
                    }
                    if (proQty <= 0) {
                        continue;
                    }

                    OrderLines lines = new OrderLines();
                    lines.setMaterielCode(product.getMaterCode());//物料编码
                    lines.setProId(proId);//产品ID
                    lines.setItemDescription(product.getProDesc());//产品名称
                    lines.setProDesc(product.getProDesc());//产品描述
                    lines.setProModel(product.getProModel());//产品型号
                    //产品价格
                    BigDecimal price = kstarPrjLst.getPrdPrc() == null ? new BigDecimal(0) : new BigDecimal(kstarPrjLst.getPrdPrc());

                    //BigDecimal amount = price.multiply(new BigDecimal(proQty));
                    Double writeOff = kstarPrjLst.getTtlUnt() - (kstarPrjLst.getVeriedNum() * kstarPrjLst.getPrdPrc());
                    BigDecimal amount = kstarPrjLst.getTtlUnt() == null ? new BigDecimal(0) : new BigDecimal(writeOff);

                    lines.setSourceId(kstarPrjLst.getLineNum()); //工程清单行号
                    lines.setSourceCode(contract.getContrNo());//来源编号、合同编号
                    lines.setProQty(proQty);//产品数量
                    lines.setPrice(price);//产品价格
                    lines.setErpSettPrice(price);//ERP结算价格
                    lines.setAmount(amount);//产品金额
                    lines.setUnit(product.getUnitName());//单位
                    lines.setRequestDate(contract.getDelivDate() == null ? new Date() : contract.getDelivDate());//请求日期
                    lines.setStatus(IConstants.ORDER_LINE_STATUS_ENTERED);
                    lines.setRemark(kstarPrjLst.getNotes());//备注

                    //初始化值
                    initOrderLines(lines);

                    orderLines.add(lines);
                }
            }
        }
        return orderLines;
    }


    /**
     * TODO 根据来源编号、来源ID获取订单发货、开票、取消数量. <br/>
     *
     * @see com.ibm.kstar.api.order.IOrderService#getContractOrderQty(java.lang.String, java.lang.String)
     */
    @Override
    public OrderQuantityVo getContractOrderQty(String sourceCode, String sourceId) {
        OrderQuantityVo orderQuantityVo = new OrderQuantityVo(0, 0, 0, 0);
        List<Object> args = new ArrayList<Object>();
        StringBuffer hql = new StringBuffer("select new com.ibm.kstar.entity.order.vo.OrderQuantityVo( sum(line.proQty) as proQty, sum(line.deliveryQty) as  deliveryQty,sum(line.billingQty) as billingQty,sum(line.cancelQty) as cancelQty) "
                + " from OrderLines  line where 1=1 ");
        if (StringUtil.isNotEmpty(sourceCode)) {
            args.add(sourceCode);
            hql.append(" and line.sourceCode = ?");
        }
        if (StringUtil.isNotEmpty(sourceId)) {
            hql.append(" and line.sourceId = ?");
            args.add(sourceId);
        }

        orderQuantityVo = (OrderQuantityVo) baseDao.findUniqueEntity(hql.toString(), args.toArray());

        return orderQuantityVo;
    }


    /**
     * TODO 根据特价编号、特价行ID获取订单发货、开票、取消数量. <br/>
     *
     * @see com.ibm.kstar.api.order.IOrderService#getContractOrderQty(java.lang.String, java.lang.String)
     */
    @Override
    public OrderQuantityVo getRebateOrderQty(String spCode, String spLineId) {
        OrderQuantityVo orderQuantityVo = new OrderQuantityVo(0, 0, 0, 0);
        List<Object> args = new ArrayList<Object>();
        StringBuffer hql = new StringBuffer("select new com.ibm.kstar.entity.order.vo.OrderQuantityVo( sum(line.proQty) as proQty, sum(line.deliveryQty) as  deliveryQty,sum(line.billingQty) as billingQty,sum(line.cancelQty) as cancelQty) "
                + " from OrderLines  line where 1=1 ");
        if (StringUtil.isNotEmpty(spCode)) {
            hql.append(" and line.spCode = ?");
            args.add(spCode);
        }
        if (StringUtil.isNotEmpty(spLineId)) {
            hql.append(" and line.spLineId = ?");
            args.add(spLineId);
        }

        orderQuantityVo = (OrderQuantityVo) baseDao.findUniqueEntity(hql.toString(), args.toArray());

        return orderQuantityVo;
    }


    /**
     * TODO 根据合同ID 查询 是否存在有效订单 <br/>
     *
     * @see com.ibm.kstar.api.order.IOrderService#hasValidOrder(java.lang.String)
     */
    @Override
    public boolean hasValidOrder(String contractId) {
        boolean b = false;
        if (StringUtil.isNotEmpty(contractId)) {
            String hql = "from OrderHeader h where h.sourceId = ? and h.executeStatus != ? ";
            List<Object> args = new ArrayList<Object>();
            args.add(contractId);
            args.add(IConstants.ORDER_EXECUTE_STATUS_CANCELLED);
            List<OrderHeader> headers = baseDao.findEntity(hql, args.toArray());
            if (headers != null && headers.size() > 0) {
                b = true;
            }
        }

        return b;
    }

    /**
     * 合同变更将订单行改为暂挂
     * TODO 简单描述该方法的实现功能（可选）.
     *
     * @see com.ibm.kstar.api.order.IOrderService#updateContractOrderLinePending(java.lang.String, java.lang.String, com.ibm.kstar.api.system.permission.UserObject)
     */
    @Override
    public void updateContractOrderLinePending(String contractId, String contractLineNo, UserObject userObject) {
        String hql = "select distinct line from OrderLines line , OrderHeader h "
                + " where line.orderId = h.id "
                + " and h.sourceCode = ? "
                + " and line.sourceId = ? ";
        List<Object> args = new ArrayList<Object>();
        args.add(contractId);
        args.add(contractLineNo);
        List<OrderLines> lines = baseDao.findEntity(hql, args.toArray());
        if (lines != null) {
            for (OrderLines line : lines) {
                line.setIsPending(IConstants.YES_Yes);
                this.update(line, userObject);
            }
        }
    }

    /**
     * 特价变更将订单行改为暂挂
     * TODO 简单描述该方法的实现功能（可选）.
     *
     * @see com.ibm.kstar.api.order.IOrderService#updateContractOrderLinePending(java.lang.String, java.lang.String, com.ibm.kstar.api.system.permission.UserObject)
     */
    @Override
    public void updateOrderLinePendingBySP(String spCode, String spLineId, String op, UserObject userObject) {
        if (!IConstants.YES_Yes.equals(op) && !IConstants.NO_No.equals(op)) {
            op = "No";
        }
        StringBuffer hql = new StringBuffer(" from OrderLines line  where line.spCode = ? ");
        List<Object> args = new ArrayList<Object>();
        args.add(spCode);
        if (StringUtil.isNotEmpty(spLineId)) {
            hql.append(" and line.spLineId = ? ");
            args.add(spLineId);
        }
        List<OrderLines> lines = baseDao.findEntity(hql.toString(), args.toArray());
        if (lines != null) {
            for (OrderLines line : lines) {
                line.setIsPending(op);
                this.update(line, userObject);
            }
        }
    }


    /**
     * TODO 合同变更、更新订单信息.
     *
     * @see com.ibm.kstar.api.order.IOrderService#updateOrderLinesByContractChange(java.lang.String, java.util.List, com.ibm.kstar.api.system.permission.UserObject)
     */
    @Override
    public void updateOrderLinesByContractChange(String contractId, List<KstarPrjLst> kstarPrjLsts, UserObject userObject, String changeType) {
        if (kstarPrjLsts == null) {
            return;
        }

        //根据合同ID获取合同
        Contract contract = baseDao.get(Contract.class, contractId);

        if (contract == null) {
            return;
        }

        for (KstarPrjLst kstarPrjLst : kstarPrjLsts) {

            String updtFlag = kstarPrjLst.getUpdtFlag();
            LovMember lv09 = lovMemberService.getLovMemberByCode("CONTRACTCHANGETYPE", "09");//09	发货后变更

            if (!StringUtil.isNullOrEmpty(changeType) && changeType.toLowerCase().contains(lv09.getId().toLowerCase())) {
                if (updtFlag == null) {
                    throw new AnneException("历史数据,请重新重新保存明细行后再提交");
                }
                //改行没有更改,直接跳过。
                if (Objects.equals(updtFlag, "4")) {
                    continue;
                }
            }

            if (StringUtil.isNotEmpty(kstarPrjLst.getQuotCode()) && StringUtil.isNotEmpty(kstarPrjLst.getLineNum())) {

                String hql = "select line from OrderLines line where line.sourceId = ? and line.sourceCode = ? and line.status != ?";
                List<Object> args = new ArrayList<Object>();
                args.add(kstarPrjLst.getLineNum());
                args.add(contract.getContrNo());//合同ID
                args.add(IConstants.ORDER_LINE_STATUS_CANCELLED);
                List<OrderLines> lines = baseDao.findEntity(hql, args.toArray());
                //产品价格
                BigDecimal price = kstarPrjLst.getPrdPrc() == null ? new BigDecimal(0) : new BigDecimal(kstarPrjLst.getPrdPrc());
                //订单行产品数量
                double orderProQty = 0;
                if (lines != null && lines.size() > 0) {
                    for (OrderLines line : lines) {
                        if (line.getPrice().compareTo(price) != 0 || line.getErpSettPrice().compareTo(price) != 0) {
                            line.setPrice(price);//产品价格
                            line.setErpSettPrice(price);//ERP结算价格
                            BigDecimal amount = price.multiply(new BigDecimal(line.getProQty()));
                            line.setAmount(amount);//产品金额
                            this.update(line, userObject);
                            //更新订单头的金额
                            this.updateOrderHeaderAmount(line.getOrderCode(), userObject);
                            //更新出货单金额
                            this.updateDeliveryByOrderPriceChange(line, userObject);
                        }
                        if (!IConstants.ORDER_LINE_STATUS_CANCELLED.equals(line.getStatus())) {
                            orderProQty += line.getProQty();
                        }
                    }

                    //合同产品行产品数量
                    double proQty = kstarPrjLst.getAmt() == null ? 0 : kstarPrjLst.getAmt();
                    //计算变更数量 qty
                    double qty = proQty - orderProQty;
                    if (qty > 0) {
                        //数量增加
                        //如果变更后的产品数量大于订单产品数量
                        String proId = kstarPrjLst.getProId();
                        KstarProduct product = baseDao.get(KstarProduct.class, proId);
                        if (product != null) {
                            OrderLines orderLines = new OrderLines();
                            OrderLines oldOrderLines = lines.get(0);

                            BeanUtils.copyPropertiesIgnoreNull(oldOrderLines, orderLines);
                            orderLines.setId(null);
                            orderLines.setProId(product.getId());//产品ID
                            orderLines.setItemDescription(product.getProDesc());//项目说明
                            orderLines.setProModel(product.getProModel());//产品型号
                            BigDecimal amount = price.multiply(new BigDecimal(qty));
                            orderLines.setSourceId(kstarPrjLst.getLineNum()); //工程清单行ID
                            orderLines.setSourceCode(contract.getContrNo()); //合同code
                            orderLines.setProQty(qty);//产品数量
                            orderLines.setPrice(price);//产品价格
                            orderLines.setErpSettPrice(price);//ERP结算价格
                            orderLines.setAmount(amount);//产品金额
                            orderLines.setUnit(product.getUnitName());//单位
                            orderLines.setMaterielCode(product.getMaterCode());
                            orderLines.setRequestDate(contract.getDelivDate() == null ? new Date() : contract.getDelivDate());//请求日期
                            orderLines.setPromiseDate(contract.getDelivDate() == null ? new Date() : contract.getDelivDate());//承诺日期
                            OrderHeader orderHeader = baseDao.get(OrderHeader.class, orderLines.getOrderId());
                            if (orderHeader == null) {
                                throw new AnneException(IOrderService.class.getName()
                                        + "更新订单失败，没有找到对应的订单头");
                            }
                            orderLines.setLineNo(this.getLineNo(orderHeader.getId()));
                            orderLines.setStatus(IConstants.ORDER_LINE_STATUS_AWAITING_SHIPPING);
                            //初始化
                            initOrderLines(orderLines);
                            this.save(orderLines, userObject);//保存订单行
                            //更新订单头的金额
                            this.updateOrderHeaderAmount(orderLines.getOrderCode(), userObject);
                        }

                    } else if (qty < 0) {
                        //数量减少
                        for (OrderLines line : lines) {
                            if (qty >= 0) {
                                break;
                            }
                            double noDeliveryQty = line.getProQty() - line.getDeliveryQty();//未发货数量
                            if (noDeliveryQty > 0) {
                                double newqty = noDeliveryQty + qty;
                                if (newqty >= 0) { //如果本次能扣完
                                    line.setPrice(price);//产品价格
                                    line.setErpSettPrice(price);//ERP结算价格
                                    double proqty = line.getProQty() + qty;
                                    BigDecimal amount = price.multiply(new BigDecimal(proqty));
                                    line.setAmount(amount);//产品金额
                                    line.setProQty(proqty);
                                    line.setIsPending(IConstants.NO_No);
                                    if (proqty == 0) {
                                        line.setStatus(IConstants.ORDER_LINE_STATUS_CANCELLED);
                                        line.setCancelQty(line.getCancelQty() - qty);
                                    }
                                    this.update(line, userObject);//更新订单的价格
                                    //更新订单头的金额
                                    this.updateOrderHeaderAmount(line.getOrderCode(), userObject);
                                    break;
                                } else if (newqty < 0) { //如果扣不完
                                    line.setPrice(price);//产品价格
                                    line.setErpSettPrice(price);//ERP结算价格
                                    double proqty = line.getProQty() - noDeliveryQty;
                                    BigDecimal amount = price.multiply(new BigDecimal(proqty));
                                    line.setAmount(amount);//产品金额
                                    line.setProQty(proqty);
                                    line.setIsPending(IConstants.NO_No);
                                    if (proqty == 0) {
                                        line.setStatus(IConstants.ORDER_LINE_STATUS_CANCELLED);
                                        line.setCancelQty(line.getCancelQty() + noDeliveryQty);
                                    }
                                    this.update(line, userObject);//更新订单的价格
                                    //更新订单头的金额
                                    this.updateOrderHeaderAmount(line.getOrderCode(), userObject);
                                    qty = qty + noDeliveryQty;
                                }
                            }
                        }
                    }
                } else {
                    //找出修改的订单行
                    OrderLines oldOrderLine = null;
                    if ("1".equals(updtFlag)) {
                        for (KstarPrjLst t : kstarPrjLsts) {
                            if (t.equals(kstarPrjLst)) {
                                break;
                            }
                            if ("2".equals(t.getUpdtFlag()) && Objects.equals(t.getMaterCode(), kstarPrjLst.getMaterCode())) {
                                String tHql = "select line from OrderLines line where line.sourceId = ? and line.sourceCode = ? and line.status != ?";
                                List<Object> tArgs = new ArrayList<>();
                                tArgs.add(t.getLineNum());
                                tArgs.add(contract.getContrNo());//合同ID
                                tArgs.add(IConstants.ORDER_LINE_STATUS_CANCELLED);
                                List<OrderLines> tLines = baseDao.findEntity(tHql, tArgs.toArray());
                                if (tLines.size() > 0) {
                                    oldOrderLine = tLines.get(0);
                                }
                                break;
                            }

                        }
                    }

                    if (!StringUtil.isNullOrEmpty(changeType) && changeType.toLowerCase().contains(lv09.getId().toLowerCase())) {
                        if (updtFlag == null) {
                            continue;
                        }

                        OrderLines lines2 = new OrderLines();
                        KstarProduct product = baseDao.get(KstarProduct.class, kstarPrjLst.getProId());
                        lines2.setId(null);
                        lines2.setProId(product.getId());//产品ID
                        lines2.setItemDescription(product.getProDesc());//项目说明
                        lines2.setProModel(product.getProModel());//产品型号
                        BigDecimal amount = price.multiply(new BigDecimal(kstarPrjLst.getAmt() == null ? 0 : kstarPrjLst.getAmt()));
                        lines2.setSourceId(kstarPrjLst.getLineNum()); //工程清单行ID
                        lines2.setSourceCode(contract.getContrNo()); //合同code
                        lines2.setProQty(kstarPrjLst.getAmt() == null ? 0 : kstarPrjLst.getAmt());//产品数量
                        lines2.setPrice(price);//产品价格
                        lines2.setErpSettPrice(price);//ERP结算价格
                        lines2.setAmount(amount);//产品金额
                        lines2.setUnit(product.getUnitName());//单位
                        lines2.setMaterielCode(product.getMaterCode());
                        lines2.setRequestDate(contract.getDelivDate() == null ? new Date() : contract.getDelivDate());//请求日期
                        lines2.setPromiseDate(contract.getDelivDate() == null ? new Date() : contract.getDelivDate());//承诺日期
                        if (oldOrderLine != null) {
                            lines2.setErpStatus(oldOrderLine.getErpStatus());
                            lines2.setShipOrg(oldOrderLine.getShipOrg());
                            lines2.setItemDescription(oldOrderLine.getItemDescription());
                            lines2.setProDesc(oldOrderLine.getProDesc());
                        }
                        if (StringUtil.isNullOrEmpty(kstarPrjLst.getNotes())) {
                            continue;
                        }
                        OrderHeader orderHeader = queryOrderHeaderByCode(kstarPrjLst.getNotes());
                        if (orderHeader == null) {
                            throw new AnneException(IOrderService.class.getName()
                                    + "更新订单失败，没有找到对应的订单头");
                        }
                        lines2.setOrderCode(orderHeader.getOrderCode());
                        lines2.setOrderId(orderHeader.getId());
                        lines2.setLineNo(this.getLineNo(orderHeader.getId()));
                        lines2.setStatus(IConstants.ORDER_LINE_STATUS_AWAITING_SHIPPING);
                        //初始化
                        initOrderLines(lines2);
                        this.save(lines2, userObject);//保存订单行
                        //更新订单头的金额
                        this.updateOrderHeaderAmount(lines2.getOrderCode(), userObject);
                    }
                }
            } else {
                throw new AnneException(IOrderService.class.getName()
                        + "更新订单失败，工程清单行合同ID或者行号为空");
            }
        }
    }


    /**
     * 保存订单行
     * saveOrderLines:(这里用一句话描述这个方法的作用). <br/>
     *
     * @param orderLine
     * @param userObject
     * @author liming
     * @since JDK 1.7
     */
    private void save(OrderLines orderLine, UserObject userObject) {
        orderLine.setCreateTime(new Date());
        orderLine.setUpdatedAt(new Date());
        if (userObject != null) {
            orderLine.setCreator(userObject.getEmployee().getId());
            orderLine.setUpdatedById(userObject.getEmployee().getId());
        }
        baseDao.save(orderLine);
    }

    /**
     * 更新订单行
     * updateOrderHeader:(这里用一句话描述这个方法的作用). <br/>
     *
     * @param orderHeader
     * @param userObject
     * @author liming
     * @since JDK 1.7
     */
    private void update(OrderLines orderLine, UserObject userObject) {
        orderLine.setUpdatedAt(new Date());
        if (userObject != null) {
            orderLine.setUpdatedById(userObject.getEmployee().getId());
        }
        baseDao.update(orderLine);
    }

    /**
     * 更新订单头
     * updateOrderHeader:(这里用一句话描述这个方法的作用). <br/>
     *
     * @param orderHeader
     * @param userObject
     * @author liming
     * @since JDK 1.7
     */
    private void save(OrderHeader orderHeader, UserObject userObject) {
        if (userObject != null) {
            orderHeader.setCreatedById(userObject.getEmployee().getId());
            orderHeader.setCreatedPosId(userObject.getPosition().getId());
            orderHeader.setCreatedOrgId(userObject.getOrg().getId());
            orderHeader.setUpdatedById(userObject.getEmployee().getId());
        }
        orderHeader.setCreatedAt(new Date());
        orderHeader.setUpdatedAt(new Date());
        baseDao.save(orderHeader);
    }

    /**
     * 更新订单头
     * updateOrderHeader:(这里用一句话描述这个方法的作用). <br/>
     *
     * @param orderHeader
     * @param userObject
     * @author liming
     * @since JDK 1.7
     */
    private void update(OrderHeader orderHeader, UserObject userObject) {
        orderHeader.setUpdatedAt(new Date());
        if (userObject != null) {
            orderHeader.setUpdatedById(userObject.getEmployee().getId());
        }
        baseDao.update(orderHeader);
    }

    @Override
    public String getSequenceCode(String docType, String prefix) {
        String number = "";
        StringBuffer sql = new StringBuffer();
        sql.append("{ ? = call CRM_P_ORDER_PUB.gen_order_num");
        sql.append("(?,?)}");
        Object[] result = baseDao.executeProcedure(sql.toString(),
                new BaseDao.ProcedureParam[]{new BaseDao.OutProcedureParam(Types.VARCHAR),
                        new BaseDao.InProcedureParam(docType),
                        new BaseDao.InProcedureParam(prefix)
                });
        number = (String) result[0];
        return number;
    }

    /**
     * TODO 更新订单行发货数量.
     *
     * @see com.ibm.kstar.api.order.IOrderService#updateOrderInvoiceQty(java.lang.String, int, com.ibm.kstar.api.system.permission.UserObject)
     */
    @Override
    public void updateOrderInvoiceQty(String id, double invoiceQty, UserObject userObject) {

        if (StringUtil.isNotEmpty(id)) {
            OrderLines orderLines = baseDao.get(OrderLines.class, id);
            if (orderLines != null) {
                double qty = orderLines.getBillingQty();
                qty += invoiceQty;

                if (qty > orderLines.getProQty() || qty < 0) {
                    throw new AnneException(IOrderService.class.getName()
                            + " updateOrderInvoiceQty : 开票总数量不能大于产品数量,且不能小于0");
                }
                orderLines.setBillingQty(qty);
                if (orderLines.getProQty() == orderLines.getBillingQty()) {
                    orderLines.setStatus(IConstants.ORDER_LINE_STATUS_CLOSED);
                }
                this.update(orderLines, userObject);
            }
        }
    }


    @Override
    public void cancelOrderLine(String lineId, UserObject userObject) {
        if (StringUtil.isNotEmpty(lineId)) {
            OrderLines orderLines = baseDao.get(OrderLines.class, lineId);
            if (orderLines != null) {
                if (orderLines.getDeliveryQty() == 0 && orderLines.getBillingQty() == 0
                        && IConstants.ORDER_LINE_STATUS_AWAITING_SHIPPING.equals(orderLines.getStatus())
                        && IConstants.NO_No.equals(orderLines.getIsAdvanceBilling())) {
                    orderLines.setCancelQty(orderLines.getProQty());
                    orderLines.setProQty(0);
                    orderLines.setAmount(new BigDecimal(0));
                    orderLines.setStatus(IConstants.ORDER_LINE_STATUS_CANCELLED);
                    this.update(orderLines, userObject);
                    OrderHeader orderHeader = baseDao.get(OrderHeader.class, orderLines.getOrderId());
                    List<OrderLines> orderLinesList = this.getOrderLinesOrderCode(orderLines.getOrderCode());
                    //如果行全部取消则将订单头改成已取消
                    boolean cancelled = true;
                    for (OrderLines lines : orderLinesList) {
                        if (!IConstants.ORDER_LINE_STATUS_CANCELLED.equals(lines.getStatus())) {
                            cancelled = false;
                            break;
                        }
                    }
                    if (cancelled) {
                        orderHeader.setAmount(new BigDecimal(0));
                        orderHeader.setExecuteStatus(IConstants.ORDER_EXECUTE_STATUS_CANCELLED);
                    } else {
                        BigDecimal amt = new BigDecimal(0);
                        for (OrderLines lines : orderLinesList) {
                            if (!IConstants.ORDER_LINE_STATUS_CANCELLED.equals(lines.getStatus())) {
                                amt = amt.add(lines.getAmount());
                            }
                        }
                        orderHeader.setAmount(amt);
                    }
                    this.update(orderHeader, userObject);

                } else {
                    throw new AnneException(IOrderService.class.getName()
                            + " cancelOrderLine : 取消失败，订单行必须是待发运状态，且开票、发票数量必须为0");
                }
            } else {
                throw new AnneException(IOrderService.class.getName()
                        + " cancelOrderLine : 订单行不存在");
            }
        }
    }

    /**
     * getOrderSalesmanCenter:获取当前登录人员订单的销售中心. <br/>
     *
     * @param userObject
     * @return
     * @author liming
     * @since JDK 1.7
     */
    @Override
    public LovMember getOrderSalesmanCenter(String oid) {
        LovMember saleCenter = new LovMember();
        String center = lovMemberService.getSaleCenter(oid);
        if (StringUtil.isNotEmpty(center)) {
            LovMember saleCenter_org = lovMemberService.getLovMemberByCode("ORG", center);
            saleCenter = saleCenter_org;

          //目前因需求，暂时不做变更转换为ERP销售中心，如之后有需求重新放开，可放开注释代码
            if (saleCenter_org != null) {
                saleCenter = baseDao.findUniqueEntity(" from LovMember m where m.groupCode = ? and m.code = ? and m.level = 1 ",
                        new Object[]{"SALES_DEPARTMENT", saleCenter_org.getOptTxt5()});
            }
        }
        return saleCenter;

    }

    /**
     * getOrderSalesmanCenterG:获取当前登录人员回款计划的销售中心. <br/>
     *
     * @param userObject
     * @return
     * @author liming
     * @since JDK 1.7
     */
    @Override
    public LovMember getOrderSalesmanCenterG(String oid) {
        LovMember saleCenter = new LovMember();
        String center = lovMemberService.getSaleCenter(oid);
        if (StringUtil.isNotEmpty(center)) {
            LovMember saleCenter_org = lovMemberService.getLovMemberByCode("ORG", center);
            saleCenter = saleCenter_org;

          //目前因需求，暂时不做变更转换为ERP销售中心，如之后有需求重新放开，可放开注释代码
//            if (saleCenter_org != null) {
//                saleCenter = baseDao.findUniqueEntity(" from LovMember m where m.groupCode = ? and m.code = ? and m.level = 1 ",
//                        new Object[]{"SALES_DEPARTMENT", saleCenter_org.getOptTxt5()});
//            }
        }
        return saleCenter;

    }

    /**
     * getOrderSalesmanDep:获取当前登录人员订单的销售部门. <br/>
     *
     * @param userObject
     * @return
     * @author liming
     * @since JDK 1.7
     */
    @Override
    public LovMember getOrderSalesmanDep(String oid, String parentId) {
        LovMember salesmanDep = new LovMember();
        LovMember salesmanDep_org = lovMemberService.get(oid);
        salesmanDep = salesmanDep_org;

        //目前因需求，暂时不做变更转换为ERP部门,如之后有需求重新放开，可放开注释代码
        if (salesmanDep_org != null && parentId != null) {
            salesmanDep = baseDao.findUniqueEntity(" from LovMember m where m.parentId= ? and m.groupCode = ? and m.code = ? and m.level = 2 ",
                    new Object[]{parentId, "SALES_DEPARTMENT", salesmanDep_org.getOptTxt5()});
        }
        return salesmanDep;
    }

    /**
     * getOrderSalesmanDepG:获取当前登录人员回款计划的销售部门. <br/>
     *
     * @param userObject
     * @return
     * @author liming
     * @since JDK 1.7
     */
    @Override
    public LovMember getOrderSalesmanDepG(String oid, String parentId) {
        LovMember salesmanDep = new LovMember();
        LovMember salesmanDep_org = lovMemberService.get(oid);
        salesmanDep = salesmanDep_org;

        //目前因需求，暂时不做变更转换为ERP部门,如之后有需求重新放开，可放开注释代码
//        if (salesmanDep_org != null && parentId != null) {
//            salesmanDep = baseDao.findUniqueEntity(" from LovMember m where m.parentId= ? and m.groupCode = ? and m.code = ? and m.level = 2 ",
//                    new Object[]{parentId, "SALES_DEPARTMENT", salesmanDep_org.getOptTxt5()});
//        }
        return salesmanDep;
    }

    @Override
    public Map<String, String> salesmanChange(String orgId) {
        Map<String, String> retMap = new HashMap<>();
        LovMember salesmanCenter = this.getOrderSalesmanCenter(orgId);
        retMap.put("salesmanCenter", salesmanCenter == null ? null : salesmanCenter.getId());
        if (salesmanCenter != null) {
            LovMember salesmanDep = this.getOrderSalesmanDep(orgId, salesmanCenter.getId());
            retMap.put("salesmanDep", salesmanDep == null ? null : salesmanDep.getId());
        }
        ProductPriceHead productPriceHead = priceHeadService.getDefaultPriceHead(orgId);
        retMap.put("priceTableId", productPriceHead == null ? null : productPriceHead.getId());
        retMap.put("priceTableName", productPriceHead == null ? null : productPriceHead.getPriceTableName());

        return retMap;
    }

    /**
     * 回款计划根据选择销售员ID获取相应岗位和销售中心
     */
    @Override
    public Map<String, String> salesmanChangeG(String orgId) {
        Map<String, String> retMap = new HashMap<>();
        LovMember salesmanCenter = this.getOrderSalesmanCenterG(orgId);
        retMap.put("salesmanCenter", salesmanCenter == null ? null : salesmanCenter.getId());
        if (salesmanCenter != null) {
            LovMember salesmanDep = this.getOrderSalesmanDepG(orgId, salesmanCenter.getId());
            retMap.put("salesmanDep", salesmanDep == null ? null : salesmanDep.getId());
        }
        ProductPriceHead productPriceHead = priceHeadService.getDefaultPriceHead(orgId);
        retMap.put("priceTableId", productPriceHead == null ? null : productPriceHead.getId());
        retMap.put("priceTableName", productPriceHead == null ? null : productPriceHead.getPriceTableName());

        return retMap;
    }

    private void initOrderLines(OrderHeader orderHeader,OrderLines orderLines) {
        if (StringUtil.isEmpty(orderLines.getStatus())) {
        	OrderHeader oldOrderHeader = baseDao.get(OrderHeader.class,
                    orderHeader.getId());
        	if("BOOKED".equals(oldOrderHeader.getExecuteStatus())) {
        		orderLines.setStatus(IConstants.ORDER_LINE_STATUS_AWAITING_SHIPPING);
        	}else {
        		orderLines.setStatus(IConstants.ORDER_LINE_STATUS_ENTERED);
        	}
        }
        if (StringUtil.isEmpty(orderLines.getIsSp())) {
            orderLines.setIsSp(IConstants.NO_No);
        }
        if (StringUtil.isEmpty(orderLines.getConfirmDeliveryDate())) {
            orderLines.setConfirmDeliveryDate(IConstants.YES_Yes);
        }
        if (StringUtil.isEmpty(orderLines.getIsAdvanceBilling())) {
            orderLines.setIsAdvanceBilling(IConstants.NO_No);//是否提前开票为No
        }
        if (StringUtil.isEmpty(orderLines.getIsPending())) {
            orderLines.setIsPending(IConstants.NO_No);//是否暂挂为否
        }
        if (StringUtil.isEmpty(orderLines.getErpPlanStatus())) {
            orderLines.setErpPlanStatus(IConstants.ORDER_ERP_PLAN_STATUS_10);
        }
        if (StringUtil.isEmpty(orderLines.getIsErpDelivery())) {
            orderLines.setIsErpDelivery(IConstants.NO_No);
        }
        orderLines.setCancelQty(0);
        orderLines.setDeliveryQty(0);
        orderLines.setBillingQty(0);

    }

    private void initOrderLines(OrderLines orderLines) {
        if (StringUtil.isEmpty(orderLines.getStatus())) {
            orderLines.setStatus(IConstants.ORDER_LINE_STATUS_ENTERED);
        }
        if (StringUtil.isEmpty(orderLines.getIsSp())) {
            orderLines.setIsSp(IConstants.NO_No);
        }
        if (StringUtil.isEmpty(orderLines.getConfirmDeliveryDate())) {
            orderLines.setConfirmDeliveryDate(IConstants.YES_Yes);
        }
        if (StringUtil.isEmpty(orderLines.getIsAdvanceBilling())) {
            orderLines.setIsAdvanceBilling(IConstants.NO_No);//是否提前开票为No
        }
        if (StringUtil.isEmpty(orderLines.getIsPending())) {
            orderLines.setIsPending(IConstants.NO_No);//是否暂挂为否
        }
        if (StringUtil.isEmpty(orderLines.getErpPlanStatus())) {
            orderLines.setErpPlanStatus(IConstants.ORDER_ERP_PLAN_STATUS_10);
        }
        if (StringUtil.isEmpty(orderLines.getIsErpDelivery())) {
            orderLines.setIsErpDelivery(IConstants.NO_No);
        }
        orderLines.setCancelQty(0);
        orderLines.setDeliveryQty(0);
        orderLines.setBillingQty(0);

    }

    /**
     * TODO 0价格订单计算备件金额.
     *
     * @see com.ibm.kstar.api.order.IOrderService#calculateSparePrice(java.lang.String, com.ibm.kstar.api.system.permission.UserObject)
     */
    @Override
    public BigDecimal calculateSparePrice(String orderCode, UserObject userObject) {
        BigDecimal amount = new BigDecimal(0);
        List<OrderLines> orderLines = this.getOrderLinesOrderCode(orderCode);
        if (orderLines == null || orderLines.size() <= 0) {
            return new BigDecimal(0);
        }

        for (OrderLines orderLine : orderLines) {
            BigDecimal price = orderLine.getPrice() == null ? new BigDecimal(0) : orderLine.getPrice();

            if (price.doubleValue() > 0
                    && orderLine.getDeliveryQty() == 0
                    && orderLine.getBillingQty() == 0
                    && orderLine.getCancelQty() == 0
                //&& IConstants.ORDER_LINE_STATUS_AWAITING_SHIPPING.equals(orderLine.getStatus())
                    ) {

                BigDecimal oldAmount = orderLine.getAmount();
                BigDecimal proQty = new BigDecimal(orderLine.getProQty());
                BigDecimal oldErpSettPrice = orderLine.getErpSettPrice();
                //每次扣除0.4%
                BigDecimal erpSettPrice = price.multiply(new BigDecimal(4).divide(new BigDecimal(1000))).setScale(6, BigDecimal.ROUND_HALF_UP);
                BigDecimal newErpSettPrice = oldErpSettPrice.subtract(erpSettPrice);
                BigDecimal newAmount = newErpSettPrice.multiply(proQty);

                orderLine.setErpSettPrice(newErpSettPrice);
                orderLine.setAmount(newAmount);
                this.update(orderLine, userObject);

                amount = amount.add(oldAmount.subtract(newAmount));
            }
        }
        OrderHeader orderHeader = this.queryOrderHeaderByCode(orderCode);
        BigDecimal hAmount = orderHeader.getAmount().subtract(amount);
        orderHeader.setAmount(hAmount);
        this.update(orderHeader, userObject);

        return amount;
    }

    /**
     * 订单价格修改后，更新对应的出货、签收行:(这里用一句话描述这个方法的作用). <br/>
     *
     * @param orderLines
     * @param userObject
     * @author liming
     * @since JDK 1.7
     */
    private void updateDeliveryByOrderPriceChange(OrderLines orderLines, UserObject userObject) {
        List<DeliveryLines> deliveryLines = deliveryService.getDeliveryLinesByOrderLineId(orderLines.getId());
        if (deliveryLines != null && deliveryLines.size() > 0) {
            for (DeliveryLines deliveryLine : deliveryLines) {
                if (deliveryLine.getPrice().compareTo(orderLines.getErpSettPrice()) != 0) {
                    //将出货单价格改成订单结算价格
                    deliveryLine.setPrice(orderLines.getErpSettPrice());
                    BigDecimal deliveryQty = new BigDecimal(deliveryLine.getDeliveryQty());
                    deliveryLine.setDeliveryAmount(deliveryLine.getPrice().multiply(deliveryQty).setScale(6, BigDecimal.ROUND_HALF_UP));
                    deliveryLine.setUpdatedById(userObject.getEmployee().getId());
                    deliveryLine.setUpdatedAt(new Date());
                    baseDao.saveOrUpdate(deliveryLine);//更新出货单

                    List<DeliveryReceipt> deliveryReceipts = deliveryService.getDeliveryReceiptListByDCode(deliveryLine.getDeliveryCode());
                    if (deliveryReceipts != null && deliveryReceipts.size() > 0) {
                        for (DeliveryReceipt deliveryReceipt : deliveryReceipts) {
                            BigDecimal receiptQty = new BigDecimal(deliveryReceipt.getDeliveryQty());
                            deliveryReceipt.setDeliveryAmount(deliveryLine.getPrice().multiply(receiptQty).setScale(6, BigDecimal.ROUND_HALF_UP));
                            deliveryReceipt.setUpdatedById(userObject.getEmployee().getId());
                            deliveryReceipt.setUpdatedAt(new Date());
                            baseDao.saveOrUpdate(deliveryReceipt); //更新签收单
                        }
                    }
                }
            }
        }
    }

    /**
     * updateOrderHeaderAmount:更新订单头的金额 <br/>
     *
     * @param orderCode
     * @param userObject
     * @author liming
     * @since JDK 1.7
     */
    private void updateOrderHeaderAmount(String orderCode, UserObject userObject) {
        //更新订单头的金额
        OrderHeader orderHeader = this.queryOrderHeaderByCode(orderCode);
        List<OrderLines> orderLinesList = this.getOrderLinesOrderCode(orderCode);
        BigDecimal amt = new BigDecimal(0);
        for (OrderLines lines : orderLinesList) {
            if (!IConstants.ORDER_LINE_STATUS_CANCELLED.equals(lines.getStatus())) {
                amt = amt.add(lines.getAmount());
            }
        }
        orderHeader.setAmount(amt);
        this.update(orderHeader, userObject);
    }


    @Override
    public List<List<Object>> exportSelectedContrLists(String[] ids) throws AnneException {
        List<List<Object>> lstOut = new ArrayList<List<Object>>();
        addHead(lstOut);
        List<OrderLines> lines = getSelectedContrList(ids);
        for (OrderLines order : lines) {
            List<Object> lstIn = new ArrayList<Object>();
            lstIn.add(order.getLineNo());
            lstIn.add(order.getAct());
            lstIn.add(order.getMaterielCode());
            lstIn.add(order.getProModel());
            lstIn.add(order.getProBrand());
            lstIn.add(order.getProQty());
            lstIn.add(order.getUnit());
            lstIn.add(order.getPrice());
            lstIn.add(order.getErpSettPrice());
            lstIn.add(order.getAmount());
            lstIn.add(order.getRequestDate());
            lstIn.add(order.getPromiseDate());
            lstIn.add(order.getErpStatusLable());
            lstIn.add(order.getConfirmDeliveryDate());
            lstIn.add(order.getShipOrgLable());
            lstIn.add(order.getIsPending());
            lstIn.add(order.getIsAdvanceBilling());
            lstIn.add(order.getCancelQty());
            lstIn.add(order.getDeliveryQty());
            lstIn.add(order.getBillingQty());
            lstIn.add(order.getStatusLable());
            lstIn.add(order.getErpPlanStatus());
            lstIn.add(order.getIsErpDelivery());
            lstIn.add(order.getRemark());
            lstOut.add(lstIn);
        }
        return lstOut;
    }

    private List<OrderLines> getSelectedContrList(String[] ids) {
        String idsStr = "";
        for (String id : ids) {
            idsStr += "'" + id + "',";
        }
        idsStr = idsStr.substring(0, idsStr.length() - 1);
        StringBuffer hql = new StringBuffer(" from OrderLines where 1 = 1 and id in (" + idsStr + ")");
        return baseDao.findEntity(hql.toString());
    }

    private void addHead(List<List<Object>> lstOut) {
        List<Object> lstHead = new ArrayList<Object>();
        lstHead.add("行号");
        lstHead.add("产品名称");
        lstHead.add("订购项目");
        lstHead.add("产品型号");
        lstHead.add("品牌");
        lstHead.add("数量");
        lstHead.add("单位");
        lstHead.add("销售单价");
        lstHead.add("ERP结算单价");
        lstHead.add("金融");
        lstHead.add("要货日期");
        lstHead.add("工厂承诺日期");
        lstHead.add("ERP状态");
        lstHead.add("启动交货确认");
        lstHead.add("发货组织");
        lstHead.add("是否暂挂");
        lstHead.add("提前开票");
        lstHead.add("取消数量");
        lstHead.add("发运数量");
        lstHead.add("开票数量");
        lstHead.add("CRM状态");
        lstHead.add("ERP计划状态");
        lstHead.add("ERP是否发货");
        lstHead.add("备注");
        lstOut.add(lstHead);
    }

    /**
     * checkRebateLineOrderQty:检查特价申请已下单数量. <br/>
     * 暂未实现订单变更特价数量校验。
     * @param orderLineId
     * @param orderQty        当地订单行数量
     * @param rebateLineId              特价ID
     * @param changeOrderCode 变更订单code
     * @return
     * @author liming
     * @since JDK 1.7
     */
    private boolean checkRebateLineOrderQty(String orderLineId, double orderQty, String rebateLineId, String changeOrderCode) {

        if (StringUtil.isNotEmpty(changeOrderCode)) {
            throw new AnneException("暂未实现订单变更特价数量校验");
        } else {
            RebateLine rl = baseDao.get(RebateLine.class, rebateLineId);
            List<Object> args = new ArrayList<>();
            //language=HQL
            String hql = "select sum(proQty) from OrderLines where spLineId=? and status !='CANCELLED'";
            args.add(rebateLineId);
            if (StringUtil.isNotEmpty(orderLineId)) {
                hql += " and id !=? ";
                args.add(orderLineId);
            }

            Double orderAmt = this.baseDao.findUniqueEntity(hql, args.toArray());
            if (orderAmt == null) {
                orderAmt = 0.d;
            }
            orderAmt += orderQty;
            double rebateQty = rl.getApplyQty() != null ? rl.getApplyQty() : 0;
            if (orderAmt <= rebateQty) {
                return true;
            }
        }
        return false;
    }

    /**
     * getSPOrderQty:获取特价单在订单中的产品数量. <br/>
     *
     * @param spId
     * @return
     * @author liming
     * @since JDK 1.7
     */
    private double getSPOrderQty(String spId, String changeOrderCode) {

        double orderQty = 0;
        List<Object> args = new ArrayList<Object>();
        StringBuffer hql = new StringBuffer("from OrderLines l where l.spLineId = ? and l.status != 'CANCELLED' ");
        args.add(spId);
        if (StringUtil.isNotEmpty(changeOrderCode)) {
            hql.append(" and l.orderCode != ?"); //订单变更时，排除当前订单
            args.add(changeOrderCode);
        }

        List<OrderLines> orderLines = baseDao.findEntity(hql.toString(), args.toArray());

        if (orderLines != null && orderLines.size() > 0) {
            for (OrderLines lines : orderLines) {
                double qty = lines.getProQty();
                StringBuffer hql1 = new StringBuffer("select sum(l.proQty) from OrderLinesChange l , OrderHeaderChange h "
                        + " where h.id = l.orderChangeId "
                        + " and (h.eventStatus = ? or h.eventStatus = ? ) "
                        + " and l.status != 'CANCELLED' "
                        + " and l.fromId = ? ");
                args = new ArrayList<Object>();
                args.add(IConstants.ORDER_CONTROL_STATUS_20);
                args.add(IConstants.ORDER_CONTROL_STATUS_40);
                args.add(lines.getId());
                Double proQtys = baseDao.findUniqueEntity(hql1.toString(), args.toArray());
                if (proQtys != null) {
                    qty = proQtys;
                }
                orderQty += qty;
            }
        }
        return orderQty;
    }

    /**
     * getProWholesalePriceTable:获取产品对应的批发价格表ID,并分组计算数量合计. <br/>
     *
     * @param lines
     * @return
     * @author liming
     * @since JDK 1.7
     */
    public Map<String, BigDecimal> getProWholesalePriceTable(List<Map<Object, Object>> lines) {
        Map<String, BigDecimal> qtyMap = new HashMap<String, BigDecimal>();
        if (lines != null) {
            for (Map<Object, Object> line : lines) {
                String proId = line.get("proId") == null ? "" : line.get("proId").toString();
                BigDecimal proQty = new BigDecimal(line.get("proQty") == null ? "0" : line.get("proQty").toString());
                String hql = "select distinct ph from ProductPriceHead ph , ProductPriceLine pl "
                        + " where pl.headId  = ph.id "
                        + " and  ph.isWholesale  = '1' "
                        + " and  pl.productID = ? ";
                List<Object> args = new ArrayList<Object>();
                args.add(proId);
                List<ProductPriceHead> priceHeads = baseDao.findEntity(hql, args.toArray());
                if (priceHeads != null && priceHeads.size() > 0) {
                    line.put("priceTableId", priceHeads.get(0).getId());
                }
                String priceTableId = line.get("priceTableId") == null ? null : line.get("priceTableId").toString();
                if (StringUtil.isNotEmpty(priceTableId)) {
                    if (qtyMap.get(priceTableId) == null) {
                        qtyMap.put(priceTableId, proQty);
                    } else {
                        BigDecimal qty = qtyMap.get(priceTableId);
                        qtyMap.put(priceTableId, proQty.add(qty));
                    }
                }
            }
        }
        return qtyMap;
    }

    /**
     * calculateProPrice:根据计算产品批发价格. <br/>
     *
     * @param priceTableId 标准价格表ID
     * @return lines 产品行
     * @throws Exception
     * @author liming
     * @since JDK 1.7
     */
    @Override
    public List<Map<Object, Object>> calculateProPrice(String customerCode, String priceTableId, List<Map<Object, Object>> lines) throws Exception {

        ProductPriceHead ph = baseDao.get(ProductPriceHead.class, priceTableId);
        if (lines == null || ph == null) {
            return lines;
        }
        //获取产品对应的批发价格表ID,并分组计算数量合计
        Map<String, BigDecimal> qtyMap = this.getProWholesalePriceTable(lines);
        //Y系列
        List<String> yList = this.getProModeleLovMemberList("Y_CLIENTCATEGORY");
        BigDecimal yAmount = new BigDecimal(0);
        BigDecimal yTempAmount = new BigDecimal(0);//Y系列最低折扣价格
        //YDC系列
        List<String> ydcList = this.getProModeleLovMemberList("YDC_CLIENTCATEGORY");
        BigDecimal ydcAmount = new BigDecimal(0);

        for (Map<Object, Object> proMap : lines) {
            String proId = proMap.get("proId") == null ? "" : proMap.get("proId").toString();
            if (StringUtil.isEmpty(proId)) {
                continue;
            }
            KstarProduct product = baseDao.get(KstarProduct.class, proId);
            String proModel = "";
            if (product != null) {
                proModel = product.getClientCategory();//产品外部型号
                proMap.put("proModel", proModel);
            }

            BigDecimal proQty = new BigDecimal(proMap.get("proQty") == null ? "0" : proMap.get("proQty").toString());
            BigDecimal price = new BigDecimal(proMap.get("price") == null ? "0" : proMap.get("price").toString());
            BigDecimal amount = new BigDecimal(proMap.get("amount") == null ? "0" : proMap.get("amount").toString());
            BigDecimal tempAmount = new BigDecimal(proMap.get("amount") == null ? "0" : proMap.get("amount").toString());
            //目录价格
            BigDecimal catalogPrice = new BigDecimal(0);
            //获取标准价格
            ProductPriceLine priceLine = this.getProductPriceLine(priceTableId, proId);
            if (priceLine != null) {
                price = priceLine.getLayer1Price();
                amount = price.multiply(proQty);
                catalogPrice = priceLine.getCatalogPrice();//目录价格
                tempAmount = price.multiply(proQty);

            } else {
                price = new BigDecimal(0);
                amount = new BigDecimal(0);
                tempAmount = new BigDecimal(0);
            }
            proMap.put("catalogPrice", catalogPrice);
            proMap.put("price", price);
            proMap.put("amount", amount);

            //批发价格表ID
            String wholesalePriceTableId = proMap.get("priceTableId") == null ? null : proMap.get("priceTableId").toString();
            if (StringUtil.isNotEmpty(wholesalePriceTableId)) {
                //价格表对应的总数量
                BigDecimal totalQty = qtyMap.get(wholesalePriceTableId);
                ProductPriceDiscount priceDiscount = this.getProductPriceDiscount(wholesalePriceTableId, proId, totalQty);
                if (priceDiscount != null) {
                    BigDecimal prive = priceDiscount.getPrive() == null ? new BigDecimal(0) : priceDiscount.getPrive();
                    BigDecimal retio = priceDiscount.getRetio() == null ? new BigDecimal(0) : priceDiscount.getRetio();
                    if (prive.doubleValue() > 0) {
                        price = prive;
                    } else {
                        price = catalogPrice.multiply(retio);
                    }
                    amount = price.multiply(proQty);
                    proMap.put("price", price);
                    proMap.put("amount", amount);
                }
            }
            if (yList.contains(proModel)) {

            	//批发价格表ID
                String wholesalePriceTableTempId = proMap.get("priceTableId") == null ? null : proMap.get("priceTableId").toString();
                if (StringUtil.isNotEmpty(wholesalePriceTableTempId)) {
                    //价格表对应的总数量
                    BigDecimal totalQty = qtyMap.get(wholesalePriceTableTempId);
                    ProductPriceDiscount priceDiscount = this.getProductPriceDiscount(wholesalePriceTableTempId, proId, null);
                    if (priceDiscount != null) {
                        BigDecimal prive = priceDiscount.getPrive() == null ? new BigDecimal(0) : priceDiscount.getPrive();
                        BigDecimal retio = priceDiscount.getRetio() == null ? new BigDecimal(0) : priceDiscount.getRetio();
                        if (prive.doubleValue() > 0) {
                            price = prive;
                        } else {
                            price = catalogPrice.multiply(retio);
                        }
                        tempAmount = price.multiply(proQty);
                    }
                }

                proMap.put("type", "Y");
                yAmount = yAmount.add(amount);
                yTempAmount = yTempAmount.add(tempAmount);//如果是Y系列计算其最低折扣看是否满足客户优惠金额
            }
            if (ydcList.contains(proModel)) {
                proMap.put("type", "YDC");
                ydcAmount = ydcAmount.add(amount);
            }
        }
        boolean isCust = false; //是否是优惠客户
        BigDecimal custAmout = new BigDecimal(0); //客户满金额
        BigDecimal custRetio = new BigDecimal(0); //客户满金额后的折扣
        List<LovMember> custLovs = lovMemberService.getListByGroupCode("CUSTOMERAMOUNT");
        if (custLovs != null && custLovs.size() > 0) {
            for (LovMember cust : custLovs) {
                if (customerCode.equals(cust.getCode())) {
                    isCust = true;
                    custAmout = new BigDecimal(StringUtil.isNotEmpty(cust.getOptTxt1()) ? cust.getOptTxt1() : "0");
                    custRetio = new BigDecimal(StringUtil.isNotEmpty(cust.getOptTxt2()) ? cust.getOptTxt2() : "0");
                }
            }
        }
        BigDecimal totalAmout = yAmount.add(ydcAmount);
        BigDecimal totalAmoutTemp = yTempAmount.add(ydcAmount);
        totalAmout = totalAmout.multiply(custRetio);
        totalAmoutTemp = totalAmoutTemp.multiply(custRetio);
        if (isCust) {
        	//如果Y系列最低折扣和YDC系列产品总金额 满足 客户优惠金额
            if (totalAmoutTemp.compareTo(custAmout) > 0) {
                for (Map<Object, Object> proMap : lines) {
                    if ("Y".equals(proMap.get("type"))) {
                        //批发价格表ID
                        String proId = proMap.get("proId") == null ? "" : proMap.get("proId").toString();
                        BigDecimal price = new BigDecimal(proMap.get("price") == null ? "0" : proMap.get("price").toString());
                        BigDecimal amount = new BigDecimal(proMap.get("amount") == null ? "0" : proMap.get("amount").toString());
                        BigDecimal proQty = new BigDecimal(proMap.get("proQty") == null ? "0" : proMap.get("proQty").toString());
                        String wholesalePriceTableId = proMap.get("priceTableId") == null ? null : proMap.get("priceTableId").toString();
                        if (StringUtil.isEmpty(wholesalePriceTableId) || StringUtil.isEmpty(proId)) {
                            continue;
                        }
                        //计算批发价格后的折扣价格
                        ProductPriceDiscount priceDiscount = this.getProductPriceDiscount(wholesalePriceTableId, proId, null);
                        if (priceDiscount != null) {
                            BigDecimal prive = priceDiscount.getPrive() == null ? new BigDecimal(0) : priceDiscount.getPrive();
                            BigDecimal retio = priceDiscount.getRetio() == null ? new BigDecimal(0) : priceDiscount.getRetio();
                            if (prive.doubleValue() > 0) {
                                price = prive;
                            } else {
                                BigDecimal catalogPrice = new BigDecimal(proMap.get("catalogPrice") == null ? "0" : proMap.get("catalogPrice").toString());
                                price = catalogPrice.multiply(retio);
                            }
                            price = price.multiply(custRetio);//计算客户折扣
                            amount = price.multiply(proQty);
                            proMap.put("price", price);
                            proMap.put("amount", amount);
                        }
                    } else if ("YDC".equals(proMap.get("type"))) {
                        BigDecimal price = new BigDecimal(proMap.get("price") == null ? "0" : proMap.get("price").toString());
                        //计算批发价格后的折扣价格
                        price = price.multiply(custRetio);
                        BigDecimal proQty = new BigDecimal(proMap.get("proQty") == null ? "0" : proMap.get("proQty").toString());
                        BigDecimal amount = price.multiply(proQty);
                        proMap.put("price", price);
                        proMap.put("amount", amount);
                    }
                }
            }
        }
        return lines;
    }

    /**
     * getProductPriceLine:获取对应的价格行表. <br/>
     *
     * @param priceTableId
     * @param proId
     * @return
     * @author liming
     * @since JDK 1.7
     */
    private ProductPriceLine getProductPriceLine(String priceTableId, String proId) {
        String hql = "select distinct pl from ProductPriceHead ph , ProductPriceLine pl "
                + " where pl.headId  = ph.id "
                + " and  ph.id = ? "
                + " and  pl.productID = ? ";
        List<Object> args = new ArrayList<Object>();
        args.add(priceTableId);
        args.add(proId);
        List<ProductPriceLine> productPriceLines = baseDao.findEntity(hql, args.toArray());
        if (productPriceLines != null && productPriceLines.size() > 0) {
            return productPriceLines.get(0);
        } else {
            return null;
        }
    }

    /**
     * getProductPriceDiscount:获取产品的折扣价格行. <br/>
     *
     * @param priceTableId
     * @param proId
     * @param totalQty
     * @return
     * @author liming
     * @since JDK 1.7
     */
    private ProductPriceDiscount getProductPriceDiscount(String priceTableId, String proId, BigDecimal totalQty) {
        List<Object> args = new ArrayList<Object>();
        //获取批发价格
        StringBuffer hql = new StringBuffer("select distinct pd from ProductPriceDiscount pd , ProductPriceHead ph , ProductPriceLine pl ");
        hql.append(" where pd.headId  = ph.id ");
        hql.append(" and  pl.headId  = ph.id ");
        hql.append(" and  ph.isWholesale  = '1' ");
        hql.append(" and  pd.type  = 'wholesale' ");
        hql.append(" and  ph.id = ? ");
        hql.append(" and  pl.productID = ? ");
        args.add(priceTableId);
        args.add(proId);
        if (totalQty != null && totalQty.doubleValue() > 0) {
            hql.append(" and  pd.quantity <= ? ");
            args.add(totalQty);
        } else {
            hql.append(" and  pd.quantity > 0 ");
        }
        hql.append(" order by pd.quantity desc");

        List<ProductPriceDiscount> discounts = baseDao.findEntity(hql.toString(), args.toArray());
        if (discounts != null && discounts.size() > 0) {
            return discounts.get(0);
        } else {
            return null;
        }
    }

    /**
     * getProModeleLovMemberList:获取产品系列型号编码. <br/>
     *
     * @param groupCode
     * @return
     * @author liming
     * @since JDK 1.7
     */
    private List<String> getProModeleLovMemberList(String groupCode) {
        List<String> list = new ArrayList<String>();
        List<LovMember> lovMembers = lovMemberService.getListByGroupCode(groupCode);
        if (lovMembers != null && lovMembers.size() > 0) {
            for (LovMember lov : lovMembers) {
                if (lov != null) {
                    list.add(lov.getCode());
                }
            }
        }
        return list;
    }


    /**
     * calculateProWholesalePrice:根据计算产品批发价格. <br/>
     *
     * @param priceTableId 价格表ID
     * @param proId        产品ID
     * @param proQty       产品数量
     * @return
     * @author liming
     * @since JDK 1.7
     */
    public boolean checkProDiscountPrice(String priceTableId, String proId, double proQty) {
        boolean ret = false;
        String hql = "select distinct pd from ProductPriceDiscount pd , ProductPriceHead ph , ProductPriceLine pl "
                + " where pd.headId  = ph.id "
                + " and  pl.headId  = ph.id "
                + " and  ph.isDiscount  = '1' "
                + " and  pd.type  = 'discount' "
                + " and  ph.id = ? "
                + " and  pl.productID = ? ";

        List<Object> args = new ArrayList<Object>();
        args.add(priceTableId);
        args.add(proId);
        List<ProductPriceDiscount> discounts = baseDao.findEntity(hql, args.toArray());
        if (discounts != null && discounts.size() > 0) {
            ret = true;
        }
        return ret;
    }

    @Override
    public Map<String, String> checkOrderBook(String orderCode) {
        Map<String, String> map = new HashMap<>();
        map.put("status", "S");
        map.put("msg", "");
        StringBuffer sql = new StringBuffer();
        sql.append("{call CUX_CRM_CALL_ERP_PKG.VALIDATE_ORDER(?,?,?) }");
        Object[] result = baseDao.executeProcedure(sql.toString(),
                new BaseDao.ProcedureParam[]{
                        new BaseDao.InProcedureParam(orderCode),
                        new BaseDao.OutProcedureParam(Types.VARCHAR),
                        new BaseDao.OutProcedureParam(Types.VARCHAR)
                });
        map.put("status", (String) result[1]);
        map.put("msg", (String) result[2]);
        return map;
    }

    /**
     * 订单拆行前校验
     * TODO 简单描述该方法的实现功能（可选）.
     *
     * @see com.ibm.kstar.api.order.IOrderService#checkOrderSplitLine(java.lang.String, java.lang.String)
     */
    @Override
    public Map<String, String> checkOrderSplitLine(String orderCode, String lineNum, double proQty) {
        Map<String, String> map = new HashMap<>();
        map.put("status", "S");
        map.put("msg", "");
        StringBuffer sql = new StringBuffer();
        sql.append("{call CUX_CRM_CALL_ERP_PKG.GET_SPLIT_LINE_STATUS(?,?,?,?,?)}");
        Object[] result = baseDao.executeProcedure(sql.toString(),
                new BaseDao.ProcedureParam[]{
                        new BaseDao.InProcedureParam(orderCode),
                        new BaseDao.InProcedureParam(lineNum),
                        new BaseDao.InProcedureParam(proQty),
                        new BaseDao.OutProcedureParam(Types.VARCHAR),
                        new BaseDao.OutProcedureParam(Types.VARCHAR)
                });
        map.put("status", (String) result[3]);
        map.put("msg", (String) result[4]);
        return map;
    }

    /**
     * 拆行保存前校验
     * TODO 简单描述该方法的实现功能（可选）.
     *
     * @see com.ibm.kstar.api.order.IOrderService#checkOrderSplitLineSave(java.lang.String, java.lang.String)
     */
    @Override
    public Map<String, String> checkOrderSplitLineSave(String orderCode, String sourceLineNum, String newLineNum, double proQty) {
        Map<String, String> map = new HashMap<>();
        map.put("status", "S");
        map.put("msg", "");
        StringBuffer sql = new StringBuffer();
        sql.append("{call CUX_CRM_CALL_ERP_PKG.SPLIT_LINE(?,?,?,?,?,?)}");
        Object[] result = baseDao.executeProcedure(sql.toString(),
                new BaseDao.ProcedureParam[]{
                        new BaseDao.InProcedureParam(orderCode),
                        new BaseDao.InProcedureParam(sourceLineNum),
                        new BaseDao.InProcedureParam(newLineNum),
                        new BaseDao.InProcedureParam(proQty),
                        new BaseDao.OutProcedureParam(Types.VARCHAR),
                        new BaseDao.OutProcedureParam(Types.VARCHAR)
                });
        map.put("status", (String) result[4]);
        map.put("msg", (String) result[5]);
        return map;
    }

    /**
     * checkProIsReport:(检查产品的目录是否报备). <br/>
     *
     * @param proId
     * @return
     * @author liming
     * @since JDK 1.7
     */
    private boolean checkProIsReport(String productModel) {
        return productService.isReport(productModel);
    }


    @Override
    public List<OrderLinesChange> getOrderLinesChangeByChangeHID(String changeId) {
        List<Object> args = new ArrayList<Object>();
        StringBuffer hql = new StringBuffer(" from OrderLinesChange order where order.orderChangeId = ? ");
        args.add(changeId);
        List<OrderLinesChange> orderLinesChanges = baseDao.findEntity(hql.toString(), args.toArray());
        return orderLinesChanges;

    }

    @Override
    public IPage queryOrderHeaderChanges(PageCondition condition) {

        FilterObject filterObject = condition.getFilterObject(OrderHeaderChange.class);
        filterObject.addOrderBy("updatedAt", "desc");
        HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
        return baseDao.search(hqlObject.getHql(), hqlObject.getArgs(), condition.getRows(), condition.getPage());
    }

    @Override
    public List<OrderVO> queryOrderListByCondition(Condition condition) {

        List<Object> args = new ArrayList<Object>();
        StringBuffer hql = new StringBuffer(" select distinct new com.ibm.kstar.entity.order.vo.OrderVO(h,l)");
        hql.append(" from OrderHeader h , OrderLines l ");
        hql.append(" where h.id = l.orderId ");
        String customerId = condition.getStringCondition("customerId");
        if (StringUtil.isNotEmpty(customerId)) {
            hql.append(" and h.customerId = ? ");
            args.add(customerId);
        }
        String businessEntity = condition.getStringCondition("businessEntity");
        if (StringUtil.isNotEmpty(businessEntity)) {
            hql.append(" and h.businessEntity = ? ");
            args.add(businessEntity);
        }
        String currency = condition.getStringCondition("currency");
        if (StringUtil.isNotEmpty(currency)) {
            hql.append(" and h.currency = ?");
            args.add(currency);
        }
        String isAdvanceBilling = condition.getStringCondition("isAdvanceBilling");
        if (StringUtil.isNotEmpty(isAdvanceBilling)) {
            if (IConstants.NO_No.equals(isAdvanceBilling)) {
                hql.append(" and l.isAdvanceBilling != ? ");
                args.add(IConstants.YES_Yes);
                hql.append(" and l.billingQty = 0 ");
            } else {
                hql.append(" and l.isAdvanceBilling = ?");
                args.add(isAdvanceBilling);
            }
        }
        String isErpDelivery = condition.getStringCondition("isErpDelivery");
        if (StringUtil.isNotEmpty(isErpDelivery)) {
            if (IConstants.NO_No.equals(isErpDelivery)) {
                hql.append(" and l.isErpDelivery != ? ");
                args.add(IConstants.YES_Yes);
            } else {
                hql.append(" and l.isErpDelivery = ?");
                args.add(isErpDelivery);
            }
        }
        String contractCode = condition.getStringCondition("contractCode");
        if (StringUtil.isNotEmpty(contractCode)) {
            hql.append(" and h.sourceCode = ?");
            args.add(contractCode);
        }
        String sourceCode = condition.getStringCondition("sourceCode");
        if (StringUtil.isNotEmpty(sourceCode)) {
            hql.append(" and h.sourceCode = ?");
            args.add(sourceCode);
        }
        String orderCode = condition.getStringCondition("orderCode");
        if (StringUtil.isNotEmpty(orderCode)) {
            hql.append(" and h.orderCode like ? ");
            args.add("%" + orderCode + "%");
        }
        String proModel = condition.getStringCondition("proModel");
        if (StringUtil.isNotEmpty(proModel)) {
            hql.append(" and l.proModel like ?");
            args.add("%" + proModel + "%");
        }
        String erpOrderCode = condition.getStringCondition("erpOrderCode");
        if (StringUtil.isNotEmpty(erpOrderCode)) {
            hql.append(" and h.erpOrderCode like ? ");
            args.add("%" + erpOrderCode + "%");
        }
        String orderType = condition.getStringCondition("orderType");
        if (StringUtil.isNotEmpty(orderType)) {
            hql.append(" and h.orderType = ? ");
            args.add(orderType);
        }
        String salesmanId = condition.getStringCondition("salesmanId");
        if (StringUtil.isNotEmpty(salesmanId)) {
            hql.append(" and h.salesmanId = ? ");
            args.add(salesmanId);
        }
        String customerPo = condition.getStringCondition("customerPo");
        if (StringUtil.isNotEmpty(customerPo)) {
            hql.append(" and h.customerPo like ? ");
            args.add("%" + customerPo + "%");
        }

        String executeStatus = condition.getStringCondition("executeStatus");
        if (StringUtil.isNotEmpty(executeStatus)) {
            hql.append(" and h.executeStatus = ? ");
            args.add(executeStatus);
        }
        String sourceType = condition.getStringCondition("sourceType");
        if (StringUtil.isNotEmpty(sourceType)) {
            hql.append(" and h.sourceType = ? ");
            args.add(sourceType);
        }
        String createdById = condition.getStringCondition("createdById");
        if (StringUtil.isNotEmpty(createdById)) {
            hql.append(" and h.createdById = ? ");
            args.add(createdById);
        }
        String orderDate_begin = condition.getStringCondition("orderDate_begin");
        if (StringUtil.isNotEmpty(orderDate_begin)) {
            hql.append(" and h.orderDate >= ? ");
            args.add(orderDate_begin);
        }
        String orderDate_end = condition.getStringCondition("orderDate_end");
        if (StringUtil.isNotEmpty(orderDate_end)) {
            hql.append(" and h.orderDate <= ? ");
            args.add(orderDate_end);
        }
        hql.append(" order by l desc");

        String perHql = PermissionUtil.getPermissionHQL(null, "h.createdById", "h.createdPosId", "h.createdOrgId", "h.id",
                (UserObject) condition.getCondition("userObject"), IConstants.PERMISSION_BUSINESS_TYPE_ORDER);
        hql.append(" AND " + perHql);

        return baseDao.findEntity(hql.toString(), args.toArray());

    }

    @Override
    public List<List<Object>> getExcelData(List<OrderVO> orders) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<List<Object>> lstOut = new ArrayList<List<Object>>();
        List<Object> lstHead = new ArrayList<Object>();
        lstHead.add("订单编号");
        lstHead.add("ERP订单编号");
        lstHead.add("我司合同编号");
        lstHead.add("CRM订单状态");
        lstHead.add("下单日期");
        lstHead.add("客户名称");
        lstHead.add("客户所属营销一级部门");
        lstHead.add("客户所属营销二级部门");
        lstHead.add("销售员");
        lstHead.add("商务专员");
        lstHead.add("物料编号");
        lstHead.add("产品描述");
        lstHead.add("数量");
        lstHead.add("取消数量");
        lstHead.add("单位");
        lstHead.add("单价");
        lstHead.add("金额");
        lstHead.add("要货日期");
        lstHead.add("工厂承诺日期");
        lstHead.add("发货组织");
        lstHead.add("是否启动交期");
        lstHead.add("提前开票");
        lstHead.add("开票数量");
        lstHead.add("特价编号");
        lstHead.add("商机编号");
        lstHead.add("是否特价");
        lstHead.add("ERP状态");
        lstHead.add("ERP是否已过账");
        lstHead.add("备注");
        lstHead.add("业务实体");
        lstHead.add("订单类型");
        lstOut.add(lstHead);
        if (orders != null && orders.size() > 0) {
            for (OrderVO vo : orders) {
                if (vo != null) {
                    List<Object> lstIn = new ArrayList<Object>();
                    lstIn.add(vo.getOrderCode());//订单编号
                    lstIn.add(vo.getErpOrderCode());//ERP订单编号
                    lstIn.add(vo.getSourceCode());//我司合同编号
                    lstIn.add(vo.getStatusLable());//CRM订单状态
                    lstIn.add(vo.getOrderDate() == null ? "" : sdf.format(vo.getOrderDate()));//下单日期
                    lstIn.add(vo.getCustomerName());//客户名称
                    lstIn.add(vo.getSalesmanCenterLable());//客户所属营销一级部门
                    lstIn.add(vo.getSalesmanDepLable());//客户所属营销二级部门
                    lstIn.add(vo.getSalesmanName());//销售员
                    lstIn.add(vo.getBusinessManagerName());//商务专员
                    lstIn.add(vo.getMaterielCode());//物料编号
                    lstIn.add(vo.getProDesc());//产品描述
                    lstIn.add(vo.getProQty());//数量
                    lstIn.add(vo.getCancelQty());//取消数量
                    lstIn.add(vo.getUnitLable());//单位
                    lstIn.add(vo.getPrice());//单价
                    lstIn.add(vo.getAmount());//金额
                    lstIn.add(vo.getRequestDate() == null ? "" : sdf.format(vo.getRequestDate()));//要货日期
                    lstIn.add(vo.getPromiseDate() == null ? "" : sdf.format(vo.getPromiseDate()));//工厂承诺日期
                    lstIn.add(vo.getShipOrgLable());//发货组织
                    lstIn.add(vo.getConfirmDeliveryDate());//是否启动交期
                    lstIn.add(vo.getIsAdvanceBilling());//提前开票
                    lstIn.add(vo.getBillingQty());//开票数量
                    lstIn.add(vo.getSpCode());//特价编号
                    lstIn.add(vo.getSourceCode());//商机编号
                    lstIn.add(vo.getIsSp());//是否特价
                    lstIn.add(vo.getErpStatusLable());//ERP状态
                    lstIn.add(vo.getIsErpDelivery());//ERP是否已过账
                    lstIn.add(vo.getRemark());//备注
                    lstIn.add(vo.getBusinessEntityLable());//业务实体
                    lstIn.add(vo.getOrderTypeLable());//订单类型
                    lstOut.add(lstIn);
                }
            }
        }
        return lstOut;
    }


    /**
     * 更新驳回后或商务修改时订单变更信息
     *
     */
    @Override
    public void updateOrderChange(OrderHeaderChange orderHeaderChange, UserObject userObject,String orderChangeFlag) throws Exception {
    	orderHeaderChange.setUpdatedById(userObject.getEmployee().getId());
        orderHeaderChange.setUpdatedAt(new Date());
        OrderHeaderChange oldOrderHeaderChange = baseDao.get(OrderHeaderChange.class, orderHeaderChange.getId());
        BeanUtils.copyPropertiesIgnoreNull(orderHeaderChange, oldOrderHeaderChange);
        baseDao.update(oldOrderHeaderChange);
        saveOrderLinesChange(orderHeaderChange,userObject,orderChangeFlag);
    }

    /**
     * 订单产品List保存或更新时检查
     *
     */
    private void checkOrderLinesChange(OrderHeaderChange orderHeaderChange,OrderLinesChange changeLines,UserObject userObject) {
    	 if (StringUtil.isNotEmpty(changeLines.getSpCode())) {
             changeLines.setIsSp(IConstants.YES_Yes);
             String spLineId = changeLines.getSpLineId();
             if (!this.checkRebateLineOrderQty(null, changeLines.getProQty(), spLineId, orderHeaderChange.getOrderCode())) {
                 throw new AnneException(IOrderService.class.getName()
                         + "saveOrderLinesChange : 保存失败，料号【" + changeLines.getMaterielCode()
                         + "】,特价编号【" + changeLines.getSpCode() + "】的订单行，累计下单数量大于特价申请数量，请检查");
             }
         }
         OrderLines orderLines = baseDao.get(OrderLines.class, changeLines.getId());
         if (orderLines != null) {
             double oldQty = orderLines.getProQty();
             if (oldQty < changeLines.getProQty()) {
                 throw new AnneException(IOrderService.class.getName()
                         + "saveOrderLinesChange : 保存失败，订单行【" + changeLines.getLineNo() + "】的产品数量不能大于原订单行的产品数量！");
             }
         }

         if (StringUtil.isEmpty(changeLines.getStatus())) {
             changeLines.setStatus(IConstants.ORDER_LINE_STATUS_ENTERED);
         }
         if (StringUtil.isEmpty(changeLines.getIsSp())) {
             changeLines.setIsSp(IConstants.NO_No);
         }
         if (StringUtil.isEmpty(changeLines.getConfirmDeliveryDate())) {
             changeLines.setConfirmDeliveryDate(IConstants.YES_Yes);
         }
         if (StringUtil.isEmpty(changeLines.getIsAdvanceBilling())) {
             changeLines.setIsAdvanceBilling(IConstants.NO_No);//是否提前开票为No
         }
         if (StringUtil.isEmpty(changeLines.getIsPending())) {
             changeLines.setIsPending(IConstants.NO_No);//是否暂挂为否
         }
         if (StringUtil.isEmpty(changeLines.getErpPlanStatus())) {
             changeLines.setErpPlanStatus(IConstants.ORDER_ERP_PLAN_STATUS_10);
         }
         if (StringUtil.isEmpty(changeLines.getIsErpDelivery())) {
             changeLines.setIsErpDelivery(IConstants.NO_No);
         }
    }

    private void orderHeaderChangeSetOrderHeader(OrderHeaderChange orderHeaderChange,OrderHeader orderHeader) {
    	//更新总金额
        orderHeader.setAmount(orderHeaderChange.getAmount());
        //更新版本号
        orderHeader.setVersion(orderHeaderChange.getVersion());
        orderHeader.setCustomerId(orderHeaderChange.getCustomerId());
        orderHeader.setCustomerCode(orderHeaderChange.getCustomerCode());
        orderHeader.setCustomerErpCode(orderHeaderChange.getCustomerErpCode());
        orderHeader.setCustomerName(orderHeaderChange.getCustomerName());
        orderHeader.setSalesmanName(orderHeaderChange.getSalesmanName());
        orderHeader.setSalesmanId(orderHeaderChange.getSalesmanId());
        orderHeader.setSalesmanCode(orderHeaderChange.getSalesmanCode());
        orderHeader.setSalesmanPos(orderHeaderChange.getSalesmanPos());
        orderHeader.setSalesmanCenter(orderHeaderChange.getSalesmanCenter());
        orderHeader.setBusinessEntity(orderHeaderChange.getBusinessEntity());
        orderHeader.setCustomerPo(orderHeaderChange.getCustomerPo());
        orderHeader.setOrderType(orderHeaderChange.getOrderType());
        orderHeader.setSalesmanDep(orderHeaderChange.getSalesmanDep());
        orderHeader.setCustAttnCode(orderHeaderChange.getCustAttnCode());
        orderHeader.setOrderDate(orderHeaderChange.getOrderDate());
        orderHeader.setBusinessManagerName(orderHeaderChange.getBusinessManagerName());
        orderHeader.setBusinessManagerCode(orderHeaderChange.getBusinessManagerCode());
        orderHeader.setBusinessManagerId(orderHeaderChange.getBusinessManagerId());
        orderHeader.setSpareOrderNo(orderHeaderChange.getSpareOrderNo());
        orderHeader.setDeliveryAddressId(orderHeaderChange.getDeliveryAddressId());
        orderHeader.setDeliveryAddress(orderHeaderChange.getDeliveryAddress());
        orderHeader.setRequestDate(orderHeaderChange.getRequestDate());
        orderHeader.setPriceTableId(orderHeaderChange.getPriceTableId());
        orderHeader.setPriceTableName(orderHeaderChange.getPriceTableName());
        orderHeader.setSourceTypeLable(orderHeaderChange.getSourceTypeLable());
        orderHeader.setSourceType(orderHeaderChange.getSourceType());
        orderHeader.setSourceCode(orderHeaderChange.getSourceCode());
        orderHeader.setCurrency(orderHeaderChange.getCurrency());
        orderHeader.setFinalCustName(orderHeaderChange.getFinalCustName());
        orderHeader.setCustAttnName(orderHeaderChange.getCustAttnName());
        orderHeader.setCustAttnTel(orderHeaderChange.getCustAttnTel());
        orderHeader.setRemark(orderHeaderChange.getRemark());
    }

	@Override
	public void cancelAdvanceBilling(String lineId, UserObject userObject) {
		 if (StringUtil.isNotEmpty(lineId)) {
	            OrderLines orderLines = baseDao.get(OrderLines.class, lineId);
	            orderLines.setIsAdvanceBilling("No");
	            orderLines.setBillingQty(0);
	            if(orderLines.getDeliveryQty()>0) {
	            	orderLines.setStatus("PICKED");
	            }else {
	            	orderLines.setStatus("AWAITING_SHIPPING");
	            }
	            baseDao.update(orderLines);
		 }
	}
	
	/*private void testMethod(OrderHeader orderHeader,UserObject userObject) {
		MethodLogger methodLogger = methodLogService.getMethodLogger("test",null);
    	Exception exception = new Exception();
    	methodLogService.setFunctionNameAndParameter(methodLogger,"test",1,orderHeader,userObject);
    	try {
    		methodLogger.setUserName("B");
    		int a = 0/0;
    		methodLogger.setUserName("C");
    	}catch(Exception e) {
    		e.printStackTrace();
    		exception = e;
    		throw e;
    	}finally {
    		//methodLogService.exceptionLog(methodLogger, exception);
    	}
	}*/
}