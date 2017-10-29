package com.ibm.kstar.impl.contract;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import org.xsnake.web.utils.StringUtil;
import org.xsnake.xflow.api.model.HistoryActivityInstance;
import org.xsnake.xflow.api.workflow.IXflowProcessServiceWrapper;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.contract.IContractEliminateService;
import com.ibm.kstar.api.contract.IContractService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
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
 * 无合同核销申请类接口实现
 * @author wansheng
 *
 */
@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class ContractEliminateServiceImpl implements IContractEliminateService {

	@Autowired
	BaseDao baseDao;
	
	@Autowired
    private IContractService contractService;
	
	@Autowired
    private ILovMemberService lovMemberService;
	
	@Autowired
	private ICustomInfoService customServiceImpl;
	
	@Autowired
    IXflowProcessServiceWrapper xflowProcessServiceWrapper;
	
	public IPage queryEliminateHeaders(PageCondition condition) {
		String contrId = condition.getStringCondition("contrId");
		StringBuffer hql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		hql.append("select ce from ContractEliminate ce where ce.contractId = ?  order by  ce.eliminateNumber desc");
		args.add(contrId);
		return baseDao.search(hql.toString(),args.toArray(),condition.getRows(), condition.getPage());
	}

	public List<ContractEliminate> queryEliminateHeaders(String id) {
		StringBuffer hql = new StringBuffer();
		hql.append("select ce from ContractEliminate ce where ce.contractId = "+"'"+id+"'" );
		return baseDao.findEntity(hql.toString());
	}
	
	public List<ContractEliminate> queryEliminateHeadersByid(String id) {
		StringBuffer hql = new StringBuffer();
		hql.append("select ce from ContractEliminate ce where ce.id = "+"'"+id+"'" );
		return baseDao.findEntity(hql.toString());
	}
	
	public BigDecimal queryEliminateHeadersCountByid(String id) {
		StringBuffer sql = new StringBuffer();
		sql.append("select count(0) from CRM_T_Eliminate ce where ce.C_ID = "+"'"+id+"'" );
		return (BigDecimal)baseDao.findUniqueBySql(sql.toString());
	}
	
	public void saveEliminate(ContractEliminate contractEliminate){
		contractService.checkVeriedNum(contractEliminate.getPrjlst(), contractEliminate.getContractId(), contractEliminate.getId());
		contractService.saveEliminateLines(contractEliminate.getPrjlst(), contractEliminate.getContractId(), contractEliminate.getId(),contractEliminate);
		//baseDao.saveOrUpdate(contractEliminate);
	}

	public void deleteEliminate(String id){
		ContractEliminate  contractEliminate = baseDao.get(ContractEliminate.class, id);
		LovMember lov = lovMemberService.get(contractEliminate.getEliminateType());
		if(contractEliminate!=null&&lov!=null&&("01".equals(lov.getCode())||"05".equals(lov.getCode()))){
			contractService.deleteEliminatePrjLst(id);//删除无合同核销的工程清单
			baseDao.delete(contractEliminate);
		}else {
			throw new AnneException("无核销合同不为新建状态或拒绝状态下无法删除!");
		}
	}
	
	public void startContractEliminateProcess(ContractEliminate contractEliminate,UserObject user,String typ) {
		 String application = contractService.getAppnameByType(IConstants.CONTR_LOAN_WRITEOFF_PROC , typ);//无合同核销审批
		 //String ti = contractService.getTitleByAppName(application);
		 String model = lovMemberService.getFlowCodeByAppCode(application);
		 String number = contractEliminate.getEliminateNumber();
		 Date now = new Date();
         SimpleDateFormat dtFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
         //String title = ti + " - " + number + " - " + user.getEmployee().getName() + " - " + dtFmt.format(now);
         String title =  number + " - " + user.getEmployee().getName() + " - " + dtFmt.format(now);
         Map<String, String> varmap = new HashMap<>();
         varmap.put("title", title);
         varmap.put("app", application);
         LovMember lovReview = lovMemberService.getLovMemberByCode("CONTRACTELIMINATE", "02");
         updateContractTrialStatus(contractEliminate.getId(),lovReview.getId());
         Contract contract = contractService.get(contractEliminate.getContractId());
         if(contract != null){
        	 CustomInfo customInfo = customServiceImpl.getCustomInfo(contract.getCustCode());
        	 LovMember customCategory = lovMemberService.get(customInfo.getCustomCategory());
        	 varmap.put("Contract_customCategory", customCategory.getCode());//客户行业类型
        	 
        	 //下单商务专员
             varmap.put("employeeIdInForm", contract.getOrderer());
    		 varmap.put("employeeNameInForm", contract.getOrdererName());
         }
         
         xflowProcessServiceWrapper.start(model, contractEliminate.getId(), user, varmap);
        /* contractEliminate.setEliminateType("2");
         baseDao.update(contractEliminate);*/
	}

	@Override
	public void updateContractTrialStatus(String id, String status) throws AnneException{ 
		StringBuffer updateSql = new StringBuffer();
		updateSql.append(" update CRM_T_Eliminate set C_ELIMINATE_TYPE='" + status+ "' where C_ID ='"+id+"'");
		baseDao.executeSQL(updateSql.toString());
		StringBuffer hql = new StringBuffer(); 
		hql.append(" select ce from ContractEliminate ce where eliminateType='" + status+ "' and id ='"+id+"'");
		List<ContractEliminate> ContractEliminateList = baseDao.findEntity(hql.toString());
		
		LovMember lovReview = lovMemberService.getLovMemberByCode("CONTRACTELIMINATE", "03");
		if(lovReview.getId().equals(status)) {
			for(ContractEliminate contractEliminate:ContractEliminateList) {
				
				StringBuffer lineSql = new StringBuffer();
				List<Object> lineArgs = new ArrayList<Object>();
				lineSql.append("select lst from KstarPrjLst lst where lst.quotCode = ?  and lst.contractEliminateId is null ");
				lineArgs.add(contractEliminate.getContractId());
				List<KstarPrjLst>  kstarPrjLstLines = baseDao.findEntity(lineSql.toString(), lineArgs.toArray());
				
				StringBuffer eliminateSql = new StringBuffer();
				List<Object> eliminateArgs = new ArrayList<Object>();
				eliminateSql.append("select lst from KstarPrjLst lst where  lst.CType = 'CONTR_LOAN_ELIMINATE'  and lst.contractEliminateId = ? ");
				eliminateArgs.add(contractEliminate.getId());
				List<KstarPrjLst>  eliminateList = baseDao.findEntity(eliminateSql.toString(), eliminateArgs.toArray());
				
				for(KstarPrjLst kstarPrjLst:kstarPrjLstLines) {
					for(KstarPrjLst eliminate:eliminateList) {
						if(kstarPrjLst.getProId().equals(eliminate.getProId())) {
							Double contractVeriedNum = eliminate.getContractVeriedNum();
							if(contractVeriedNum == null) {
								contractVeriedNum=0d;
							}
							kstarPrjLst.setNotVeriNum(kstarPrjLst.getNotVeriNum()-contractVeriedNum);
							kstarPrjLst.setVeriedNum(kstarPrjLst.getVeriedNum()+contractVeriedNum);
							baseDao.saveOrUpdate(kstarPrjLst);
						}
					}
				}
			}
		}
	}
	
	
	
}
