package com.ibm.kstar.action.contract.lov;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.contract.IContractService;
import com.ibm.kstar.api.price.IPriceHeadService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.bizopp.BusinessOpportunity;
import com.ibm.kstar.entity.contract.Contract;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/lovcontract")
public class ContractLovAction extends BaseAction {


	@Autowired
	private IContractService contractService;	

	@Autowired
	private ILovMemberService lovMemberService;
	@Autowired
	private IPriceHeadService priceHeadService;
	
	/**
	 * 
	 * autocomplete_custom:(带模糊查询的客户信息选择录入框). <br/> 
	 * TODO(提供公共的查询下拉选择框).
	 * 
	 * @author liming 
	 * @param condition
	 * @param request
	 * @return 
	 * @since JDK 1.7
	 */
	@NoRight
	@ResponseBody
	@RequestMapping("/autocompleteproject")
	public String autoProject(PageCondition condition,HttpServletRequest request){
		
		ActionUtil.requestToCondition(condition, request);
		String search = condition.getStringCondition("search");
		String clientId = condition.getStringCondition("custCode");
		if(clientId!=null){
			condition.getFilterObject().addCondition("clientId", "=", clientId);
		}
		if(search !=null){
			condition.getFilterObject().addOrCondition("number", "like", "%"+search+"%");
			condition.getFilterObject().addOrCondition("opportunityName", "like", "%"+search+"%");
		}
		List<BusinessOpportunity> projectInfos = contractService.getProjectInfoList(condition);
		
		return sendSuccessMessage(projectInfos);
	}
	@NoRight
	@ResponseBody
	@RequestMapping("/autocompletecustomer")
	public String autoCustom(PageCondition condition,HttpServletRequest request){
		
		ActionUtil.requestToCondition(condition, request);
		String search = condition.getStringCondition("search");
		
		if(search !=null){
			condition.getFilterObject().addOrCondition("customCode", "like", "%"+search+"%");
			condition.getFilterObject().addOrCondition("customFullName", "like", "%"+search+"%");
		}
		List<CustomInfo> customInfos = contractService.getCustomInfoList(condition);
		
		return sendSuccessMessage(customInfos);
	} 
	@NoRight
	@ResponseBody
	@RequestMapping("/autocompleteFrameno")
	public String autoFrameno(PageCondition condition,HttpServletRequest request){
		
		ActionUtil.requestToCondition(condition, request);
		String search = condition.getStringCondition("search");

		LovMember statLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "10");//10	已签订
		condition.getFilterObject().addCondition("contrStat", "=", statLov.getId());
		LovMember contrTypeLov = lovMemberService.getLovMemberByCode("CONTRACTTYPE", IConstants.CONTR_STAND_0102); // 
		condition.getFilterObject().addCondition("contrType", "=", contrTypeLov.getId());
		condition.getFilterObject().addCondition("isValid", "=", "1");
		if(search !=null){
			condition.getFilterObject().addOrCondition("contrNo", "like", "%"+search+"%");
			condition.getFilterObject().addOrCondition("contrName", "like", "%"+search+"%");
		}
		List<Contract> contractNos = contractService.getFramenoInfoListByUser(condition);
		
		return sendSuccessMessage(contractNos);
	} 	
	@NoRight
	@ResponseBody
	@RequestMapping("/autocompleteOrg")
	public String autoOrg(PageCondition condition,HttpServletRequest request){
		
		ActionUtil.requestToCondition(condition, request);
		String search = condition.getStringCondition("search");
		condition.getFilterObject().addCondition("groupCode", "=", "ORG");// 组织
		if(search !=null){
			condition.getFilterObject().addOrCondition("id", "like", "%"+search+"%");
			condition.getFilterObject().addOrCondition("code", "like", "%"+search+"%");
			condition.getFilterObject().addOrCondition("name", "like", "%"+search+"%");
		}
		List<LovMember> org = contractService.getOrgInfoList(condition);
		
		return sendSuccessMessage(org);
	} 
	@NoRight
	@ResponseBody
	@RequestMapping("/autocompletePriceTable")
	public String autoPrice(PageCondition condition,HttpServletRequest request){
		
		ActionUtil.requestToCondition(condition, request);
		String search = condition.getStringCondition("search");
		String custCode = condition.getStringCondition("custCode");
		String orgId = getUserObject().getOrg().getId();
//		condition.getFilterObject().addOrCondition("groupCode", "=", "ORG");// 组织
		if(search !=null){
			condition.getFilterObject().addOrCondition("priceTableName", "like", "%"+search+"%");
			condition.getFilterObject().addOrCondition("comments", "like", "%"+search+"%");
		}
//		List<ProductPriceHead> priceTable = contractService.getPriceTableInfoList(condition);
		List<Map<String, Object>> priceTable = priceHeadService.queryPrice(custCode, orgId, search); //
		
		
		return sendSuccessMessage(priceTable);
	} 
	

	@NoRight
	@ResponseBody
	@RequestMapping("/selectDirectSellNo")
	public String selectDirectSellNo(PageCondition condition,HttpServletRequest request){
		
		ActionUtil.requestToCondition(condition, request);
		String search = condition.getStringCondition("search");

//		LovMember contrTypeLov = lovMemberService.getLovMemberByCode("CONTRACTTYPE", "INTER0101"); // 直销
//		
//		condition.getFilterObject().addOrCondition("contrType", "=", contrTypeLov.getId());
		if(search !=null){
			condition.getFilterObject().addOrCondition("contrNo", "like", "%"+search+"%");
//			condition.getFilterObject().addOrCondition("customFullName", "like", "%"+search+"%");
		}
		List<Contract> contractNos = contractService.getFramenoInfoList(condition);
		
		return sendSuccessMessage(contractNos);
	} 

	@NoRight
	@ResponseBody
	@RequestMapping("/selectContrType")
	public String selectContrType(Condition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		String code = condition.getStringCondition("code");
//		String parentId = condition.getStringCondition("parentCode");
		String level = condition.getStringCondition("level");
//		String contrTpCd = condition.getStringCondition("contrTpCd");
		String genContrBy = condition.getStringCondition("genContrBy");
		String quotNo = condition.getStringCondition("quotNo");
		String contrTpId = condition.getStringCondition("contrTpId");
		String marketDeptId = condition.getStringCondition("marketDeptId");
		String parentCD= "";
		String lovcode= "";
		String parentId ="";
		String contrTpCd = "";
		UserObject user = (UserObject)condition.getCondition("userObject");
//		IConstants.CONTR_TYPE_X_ORG_STR;
//		contract.setContrStat(statLov.getId());
//		8	CONTRACTDOMESTICTYPE	国内合同类型
//		9	CONTRACTINTERTYPE	国际合同类型
		
//		P_GNORG_B1_0001	国内数据中心
//		P_GJORG_B1_0001	国际营销中心
//		P_GNQCORG_B1_0001	新能源汽车行业部
//		P_GNGFORG_B1_0001	新能源国内营销

		LovMember contrTpLov = lovMemberService.get(contrTpId);
		if(contrTpLov!=null){
			contrTpCd = contrTpLov.getCode();
		}
				
		LovMember makDepLov ; 
		if(marketDeptId !=null && !marketDeptId.equalsIgnoreCase("")){
			makDepLov = lovMemberService.get(marketDeptId);
		}else{
			makDepLov = lovMemberService.getLovMemberByCode("ORG", lovMemberService.getSaleCenter(getUserObject().getOrg().getId()));
		}
		
		if(makDepLov != null){
//			contract.setMarketDept(makDepLov.getId());
			if(makDepLov.getCode().equalsIgnoreCase(IConstants.CONTR_ORG_GJ_B1_STR)){
				parentCD=IConstants.CONTR_STAND_02;
			}else if(makDepLov.getCode().equalsIgnoreCase(IConstants.CONTR_ORG_GNQCORG_B1_STR) || makDepLov.getCode().equalsIgnoreCase(IConstants.CONTR_ORG_GNGFORG_B1_STR)){
				parentCD=IConstants.CONTR_STAND_01; 
			}else if(makDepLov.getCode().equalsIgnoreCase(IConstants.CONTR_ORG_GN_B1_STR)){
				if((genContrBy!=null && genContrBy.equalsIgnoreCase(IConstants.CONTR_STAND)) || (( contrTpCd.equalsIgnoreCase(IConstants.CONTR_STAND_0103)) && (quotNo == null || quotNo.equalsIgnoreCase("")))){
					lovcode = IConstants.CONTR_STAND_0103;  //CONTR_STAND-0103	框架下合同
				}else{
					parentCD=IConstants.CONTR_STAND_01; 
				}
			}
		}else{
			// 合同管理部部门领导，合同专员 搜索显示国内合同类型
			if(user.getPosition() != null && ("P_POSPUREAGREE_B1_0002".equals(user.getPosition().getCode()) || "P_POSPUREAGREE_B1_0001".equals(user.getPosition().getCode()))){
				parentCD=IConstants.CONTR_STAND_01; 
			}else{
				throw new AnneException("未找到您所属的营销部门，请联系管理员！");
			}
		}
//		if (contrTpCd.equalsIgnoreCase(IConstants.CONTR_STAND)){
//			if(user.getOrg().getNamePath().contains(IConstants.CONTR_TYPE_X_ORG_STR)){
//				parentCD=IConstants.CONTR_STAND_02; //"CONTRACTINTERTYPE";国际合同类型
//			}else{
//				parentCD=IConstants.CONTR_STAND_01; //"CONTRACTDOMESTICTYPE"; 国内合同类型
//			}
//		}
		if(!parentCD.equalsIgnoreCase("")){
			LovMember pLov= lovMemberService.getLovMemberByCode(code, parentCD);
			parentId= pLov.getId();	
			condition.getFilterObject().addCondition("parentId", "=", parentId);	
		}else if(!lovcode.equalsIgnoreCase("")){
			condition.getFilterObject().addOrCondition("code", "=", lovcode);
			condition.getFilterObject().addOrCondition("code", "=",  IConstants.CONTR_STAND_0102);
		}
		condition.getFilterObject().addCondition("groupCode", "=", code);	
		condition.getFilterObject().addCondition("level", "=", level);
		List<LovMember> list = lovMemberService.getList(condition);
		return sendSuccessMessage(list);
	}
	
	
	@NoRight
	@ResponseBody
	@RequestMapping("/selectPrjevlType")
	public String selectPrjevlType(Condition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		String code = condition.getStringCondition("code");
		condition.getFilterObject().addCondition("groupCode", "=", code);
		condition.getFilterObject().addCondition("code", "!=", "E07");
		
		List<LovMember> list = lovMemberService.getList(condition);
		return sendSuccessMessage(list);
	}
	/**
	 * 
	 * autoContract:合同搜索录入框. <br/> 
	 * 
	 * @author liming 
	 * @param condition
	 * @param request
	 * @return 
	 * @since JDK 1.7
	 */
	@NoRight
	@ResponseBody
	@RequestMapping("/autocompContract")
	public String autoContract(PageCondition condition,HttpServletRequest request){
		
		ActionUtil.requestToCondition(condition, request);
		String customerId = condition.getStringCondition("customerId");
		if(StringUtil.isNotEmpty(customerId)){
			condition.getFilterObject().addCondition("custCode", "=", customerId);
		}
		String businessId = condition.getStringCondition("businessId");
		if(StringUtil.isNotEmpty(businessId)){
			condition.getFilterObject().addCondition("bussEnity", "=", businessId);
		}
		
		String search = condition.getStringCondition("search");
		condition.getFilterObject().addCondition("isValid", "=", "1");
		if(search !=null){
			condition.getFilterObject().addOrCondition("contrNo", "like", "%"+search+"%");
			condition.getFilterObject().addOrCondition("contrName", "like", "%"+search+"%");
		}
		List<Contract> contractNos = contractService.getContractListForAutocomp(condition);
		
		return sendSuccessMessage(contractNos);
	} 	
	
}
