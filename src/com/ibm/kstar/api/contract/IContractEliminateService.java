package com.ibm.kstar.api.contract;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.xflow.api.model.HistoryActivityInstance;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.bizopp.BusinessOpportunity;
import com.ibm.kstar.entity.contract.ContrAddr;
import com.ibm.kstar.entity.contract.Contract;
import com.ibm.kstar.entity.contract.ContractEliminate;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.order.OrderHeader;
import com.ibm.kstar.entity.product.ProductPriceHead;
import com.ibm.kstar.entity.quot.KstarAftSale;
import com.ibm.kstar.entity.quot.KstarAtt;
import com.ibm.kstar.entity.quot.KstarBaseInf;
import com.ibm.kstar.entity.quot.KstarBiddcevl;
import com.ibm.kstar.entity.quot.KstarIdm;
import com.ibm.kstar.entity.quot.KstarIdu;
//import com.ibm.kstar.entity.quot.KstarBiddcevl;
import com.ibm.kstar.entity.quot.KstarMemInfo;
import com.ibm.kstar.entity.quot.KstarPgInf;
import com.ibm.kstar.entity.quot.KstarPrjEvl;
import com.ibm.kstar.entity.quot.KstarPrjLst;
import com.ibm.kstar.entity.quot.KstarSngBty;
import com.ibm.kstar.entity.quot.KstarSngClr;
import com.ibm.kstar.entity.quot.KstarSngElec;
import com.ibm.kstar.entity.quot.KstarSngMnt;
import com.ibm.kstar.entity.quot.KstarSngRck;
import com.ibm.kstar.entity.quot.KstarSngUps;


/**
 * 无合同核销申请类接口
 * @author wansheng
 *
 */
public interface IContractEliminateService {
	
	IPage queryEliminateHeaders(PageCondition condition);
	
	List<ContractEliminate> queryEliminateHeaders(String id);
	
	List<ContractEliminate> queryEliminateHeadersByid(String id);
	
	void saveEliminate(ContractEliminate contractEliminate);
	
	void deleteEliminate(String id);
	
	void startContractEliminateProcess(ContractEliminate contractEliminate,UserObject user,String typ);
	
	void updateContractTrialStatus(String id, String status) throws AnneException;
	
	BigDecimal queryEliminateHeadersCountByid(String id);
}
