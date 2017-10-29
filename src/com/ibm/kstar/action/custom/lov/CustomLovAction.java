package com.ibm.kstar.action.custom.lov;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.utils.ActionUtil;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.bizopp.IBizoppService;
import com.ibm.kstar.api.common.customlov.ICustomLovInfoService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.bizopp.BusinessOpportunity;
import com.ibm.kstar.entity.common.customlov.Custom;
import com.ibm.kstar.entity.contract.Contract;
import com.ibm.kstar.entity.custom.CustomAddressInfo;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.custom.CustomRelaContact;
import com.ibm.kstar.entity.custom.CustomRelation;
import com.ibm.kstar.entity.product.ProductPriceHead;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/lovcustom")
public class CustomLovAction extends BaseAction {
	@Autowired
	ICustomLovInfoService service;
	
	@Autowired
	ICustomInfoService customInservice;
	
	@Autowired
	IBizoppService bizService;
	
	@Autowired
	ILovMemberService lovService;
	
	@NoRight
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/autocompleteBizOpp")
	public String autocompleteBizOpp(PageCondition condition, HttpServletRequest request) throws Exception {
		ActionUtil.requestToCondition(condition, request);
		List<BusinessOpportunity> businessOpportunitys = new ArrayList<BusinessOpportunity>();;
		if (condition.getConditionMap().containsKey("clientId")) {
			
			if (StringUtils.isEmpty(condition.getStringCondition("clientId"))) {
				businessOpportunitys = (List<BusinessOpportunity>) bizService.query(condition).getList();
			} else {
				CustomInfo customInfo = customInservice.getCustomInfoByCode(condition.getStringCondition("clientId"));
				condition.setCondition("clientId", customInfo.getId());
				businessOpportunitys = service.queryBizOppByUser(condition);
			}
		} else  {
			businessOpportunitys = (List<BusinessOpportunity>) bizService.query(condition).getList();
		}

		
		return sendSuccessMessage(businessOpportunitys);
	}
	@NoRight
	@ResponseBody
	@RequestMapping("/autocompleteBizOppByUser")
	public String autocompleteBizOppByUser(PageCondition condition, HttpServletRequest request) throws Exception {
		ActionUtil.requestToCondition(condition, request);
		
		if (StringUtils.isEmpty(condition.getStringCondition("userId"))) {
			
		}
		List<BusinessOpportunity> businessOpportunitys = service.queryBizOppByUser(condition);

		return sendSuccessMessage(businessOpportunitys);
	}
	@NoRight
	@ResponseBody
	@RequestMapping("/autocompleteContractByUser")
	public String autocompleteContractByUser(PageCondition condition, HttpServletRequest request) throws Exception {
		ActionUtil.requestToCondition(condition, request);
		List<Contract> contracts = service.queryContractByUser(condition);

		return sendSuccessMessage(contracts);
	}
	
	/**
	 * autocomplete_custom:(带模糊查询的客户信息选择录入框). <br/> 
	 */
	@NoRight
	@ResponseBody
	@RequestMapping("/autocomplete_custom")
	public String autoCompleteCustom(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		List<Custom> customInfos = service.getCustomInfoList(condition);
		return sendSuccessMessage(customInfos);
	}

	/**
	 * autocomplete_custom:(带模糊查询的客户信息选择录入框). <br/>
	 */
	@NoRight
	@ResponseBody
	@RequestMapping("/autoCompleteCustomNoAuth")
	public String autoCompleteCustomNoAuth(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		List<Custom> customInfos = service.getCustomInfoListNoAuth(condition);
		return sendSuccessMessage(customInfos);
	}

	/**
	 * autocomplete_custom:(带模糊查询的客户信息选择录入框). <br/> 
	 */
	@NoRight
	@ResponseBody
	@RequestMapping("/autocompleteCustom")
	public String autoCompleteCustomWithCondition(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
//		UserObject user = null;
//		if (!StringUtils.isEmpty(condition.getStringCondition("userId"))) {
//			user = new UserObject(condition.getStringCondition("userId"), condition.getStringCondition("posId"), condition.getStringCondition("orgId"));
//		}
		
		List<CustomInfo> customInfos = service.getCustomInfoListWithCondition(condition, getUserObject());
		return sendSuccessMessage(customInfos);
	}
	
	/**
	 * autocomplete_custom:(带模糊查询的客户信息选择录入框). <br/> 
	 */
	@NoRight
	@ResponseBody
	@RequestMapping("/autoCompleteCustomAll")
	public String autoCompleteCustomAll(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		List<CustomInfo> customInfos = service.getCustomInfoListWithCondition(condition);
		return sendSuccessMessage(customInfos);
	}
	
	/**
	 * autocomplete_custom:(带模糊查询的客户信息选择录入框,根据当前岗位id过滤客户数据). <br/> 
	 */
	@NoRight
	@ResponseBody
	@RequestMapping("/autoCompleteCustomByPosition")
	public String autoCompleteCustomByPosition(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		List<CustomInfo> customInfos =new ArrayList<CustomInfo>();
		
		//根据当前登陆人的信息获取登陆人的所属销售中心进行过滤
		LovMember lovMember = lovService.getSaleCenterLovmember(this.getUserObject().getOrg().getId());
		if(IConstants.CONTR_ORG_GNQCORG_B1_STR.equals(lovMember.getId())
				||IConstants.CONTR_ORG_GNGFORG_B1_STR.equals(lovMember.getId())){			
			customInfos = service.getCustomInfoListWithConditionBySalerCenter(condition,lovMember);
		}else {
			customInfos = service.getCustomInfoListWithConditionAndPos(condition,this.getUserObject());
		}
		return sendSuccessMessage(customInfos);
	}


	/**
	 * autocomplete_custom:(带模糊查询的客户信息选择录入框). <br/>
	 */
	@NoRight
	@ResponseBody
	@RequestMapping("/autocomplete_customAuth")
	public String autoCompleteCustomAuth(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		List<CustomInfo> customInfos = service.getCustomInfoListAuth(condition);
		return sendSuccessMessage(customInfos);
	}

	/**
	 * 
	 * autocompleteCompetitor:(客户竞争对手信息抽取). <br/> 
	 */
	@NoRight
	@ResponseBody
	@RequestMapping("/autocompleteCompetitor")
	public String autocompleteCompetitor(PageCondition condition,HttpServletRequest request){
		
		ActionUtil.requestToCondition(condition, request);
		
		String customCode = condition.getStringCondition("customCode");
		
		List<CustomRelation> customRelations = service.getCompanyList(condition);
		
		List<CustomRelation> rtnCustomRelations = new ArrayList<CustomRelation>();
		
		for (CustomRelation customRelation : customRelations) {
			String tempCustomCode = customRelation.getCustomCode();
			String tempCustomCompCode = customRelation.getCustomCompCode();
			String tempLel = customRelation.getRelationLvl();
			String tempCode = "";
			if (StringUtils.equals(IConstants.LEV_COMPITER, tempLel)) {
				
				if (StringUtils.equals(customCode, tempCustomCode)) {
					customRelation.setCustomCodeDisp(tempCustomCompCode);
					
					tempCode = tempCustomCompCode;
				} else if (StringUtils.equals(customCode, tempCustomCompCode)){
					customRelation.setCustomCodeDisp(tempCustomCode);
					
					tempCode = tempCustomCode;
				}
				CustomInfo customInfo = customInservice.getCustomInfoByCode(tempCode);
				customRelation.setCustomNameDisp(customInfo.getCustomFullName());
				
				rtnCustomRelations.add(customRelation);
			}
			
		}
		
		return sendSuccessMessage(rtnCustomRelations);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/select_contact")
	public String selectContact(Condition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		String customId = condition.getStringCondition("customId");
		condition.getFilterObject().addCondition("customId", "eq", customId);
		List<CustomRelaContact> list = service.getCustomRelaContactList(condition);
		return sendSuccessMessage(list);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/select_addr")
	public String selectAddr(Condition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		String customId = condition.getStringCondition("customId");
		condition.getFilterObject().addCondition("customId", "eq", customId);
		List<CustomAddressInfo> list = service.getCustomAddressInfoList(condition);
		return sendSuccessMessage(list);
	}
	@NoRight
	@ResponseBody
	@RequestMapping("/autocompleteOrg")
	public String autocompleteOrg(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		List<LovMember> orgs = service.getOrgList(condition);
		return sendSuccessMessage(orgs);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/quotedPriceCustom")
	public String quotedPriceCustom(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		UserObject user = getUserObject();
		List<ProductPriceHead> productPriceHead = service.quotedPriceCustom(condition,user);
		return sendSuccessMessage(productPriceHead);
	}
	
}
