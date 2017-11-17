package com.ibm.kstar.impl.log;


import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.bizopp.IBizBaseService;
import com.ibm.kstar.api.contract.IContractService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.log.IInterFaceLogService;
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

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class InterFaceLogServiceImpl implements IInterFaceLogService {
    @Autowired
    BaseDao baseDao;

	@Override
	public IPage queryMethodLogger(PageCondition condition) {
		FilterObject filterObject = condition.getFilterObject(MethodLogger.class);
        filterObject.addOrderBy("interfaceReportDate", "desc");
        HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
        return baseDao.search(hqlObject.getHql(), hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}
   

}