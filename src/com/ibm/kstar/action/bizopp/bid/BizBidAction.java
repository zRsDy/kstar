package com.ibm.kstar.action.bizopp.bid;

import com.alibaba.fastjson.JSONObject;
import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.api.bizopp.IBizBaseService;
import com.ibm.kstar.api.bizopp.IBizoppService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.ICorePermissionService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.entity.bizopp.Bid;
import com.ibm.kstar.entity.bizopp.BusinessOpportunity;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.interceptor.system.permission.NoRight;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.log.LogOperate;
import org.xsnake.web.page.IPage;
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.StringUtil;

import javax.servlet.http.HttpServletRequest;

import java.util.Date;

/**
 * ClassName:BizbidAction <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: Jan 6, 2017 10:07:56 AM <br/>
 * 
 * @author Wutw
 * @version
 * @since JDK 1.7
 * @see
 */

@Controller
@RequestMapping("/bid")
public class BizBidAction extends BaseAction {

	@Autowired
	IBizBaseService bizService;

	@Autowired
	IBizoppService bizoppService;

	@Autowired
	ICustomInfoService customInfoService;

	@Autowired
	ILovMemberService lovMemberService;

	@Autowired
	ICorePermissionService corePermissionService;
	
	@RequestMapping("/index")
	public String index(String id, Model model) {
		return forward("bidIndex");
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) throws Exception {
		ActionUtil.requestToCondition(condition, request);
		String bizOppId = request.getParameter("bizOppId");
		condition.getFilterObject().addCondition("bizOppId", "eq", bizOppId);
		IPage p = bizoppService.queryBid(condition);

		return sendSuccessMessage(p);
	}


	@RequestMapping("/add")
	public String add(String bid, Model model){
		UserObject user = getUserObject();
		String userId = user.getEmployee().getId();
		String userName = user.getEmployee().getName();
		
		Bid entity = new Bid();
		//如果在商机页面新增售前支持，则自动带入客户名称，商机名称
		BusinessOpportunity businessOpportunity = null;
		if (bid != null) {
			businessOpportunity = bizoppService.getBizOppEntity(bid);
			//商机ID
			entity.setBizOppId(bid);
			//商机名称
			entity.setBizOppName(businessOpportunity.getOpportunityName());

			entity.setProjectName(businessOpportunity.getOpportunityName());
			//代理商
			String agencyId = businessOpportunity.getAgentId();
			if (StringUtil.isNotEmpty(agencyId)) {
				CustomInfo customInfo = customInfoService.getCustomInfo(agencyId);
				if (customInfo != null) {
					entity.setAgencyName(customInfo.getCustomFullName());
					entity.setAgency(customInfo.getId());
				}
			}

			//项目所在地
			entity.setProjectAddress(businessOpportunity.getBizOppAddressName());
			//终端客户
			entity.setTerminalClient(businessOpportunity.getEnterprise());
			//是否集成包
			entity.setIsIntegrated(String.valueOf(businessOpportunity.getIsIntegreated()));
			//行业
			entity.setIndustry(businessOpportunity.getIndustry());

			entity.setBidEnterprise(businessOpportunity.getBidUnit());

			entity.setBidNumber(businessOpportunity.getBidNo());

		}
		//设置创建人
		entity.setCreatedById(userId);
		entity.setStatus(ProcessConstants.PROCESS_STATUS_Pending);
//		entity.setApplicanName(userName);
		//生成投标授权编号
		String bidNumber = bizoppService.getSequenceCode("gen_bizoppbid_change_num");
		entity.setBidNumber(bidNumber);
		entity.setCreatedAt(new Date());
		entity.setPrintStyle("10");
		model.addAttribute("entity", entity);


		TabMain tabMainInfo = new TabMain();
		tabMainInfo.setInitAll(false);
		if (hasPermission("P03BidAuthT1OrgPage")) {
			tabMainInfo.addTab("授权单位", "/bid/authManage.html?id=" + entity.getId());
		}
		
		if (hasPermission("P03BidAuthT3ProReviewHistoryPage")) {
			tabMainInfo.addTab("审批历史", "/standard/history.html?contrId=" + entity.getId());
		}
		if (hasPermission("P03BidAuthT2FilePage")) {
			tabMainInfo.addTab("附件", "/common/attachment/attachment.html?businessType=Bid&docGroupCode=BidDOCTYPE&businessId=" + entity.getId());
		}
		model.addAttribute("tabMainInfo",tabMainInfo);

		return forward("bidAdd");
	}
	
	@LogOperate(module = "商机模块", notes = "${user}点击授权单位列表")
	@RequestMapping("/authManage")
	public String authManage(String id, Model model) {
		Bid entity = bizService.getEntity(id, new Bid());
		model.addAttribute("entity",entity);
		return forward("authManage");
	}
	
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(Bid entity) {
		entity.setCreatedById(getUserObject().getEmployee().getId());
		entity.setCreatedPosId(getUserObject().getPosition().getId());
		entity.setCreatedOrgId(getUserObject().getOrg().getId());
		bizService.saveBid(entity,getUserObject());
		return sendSuccessMessage(entity.getId());
	}
	
	@NoRight
	@RequestMapping("/edit")
	public String edit(String id,Model model){
		if(id==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		Bid entity = bizService.getEntity(id, new Bid());
		CustomInfo customInfo = customInfoService.getCustomInfo(entity.getTerminalClient());
		LovMember lovMemberk = (LovMember) CacheData.getInstance().get(entity.getCrossDepartment());
		LovMember lovMembers = (LovMember) CacheData.getInstance().get(entity.getInvolveIndustry());
		Employee personInCharge = (Employee) CacheData.getInstance().get(entity.getPersonInCharge());

		if(lovMemberk!=null){
			model.addAttribute("lovMemberk", JSONObject.toJSONString(lovMemberk));
		}
		if(lovMembers!=null){
			model.addAttribute("lovMembers", JSONObject.toJSONString(lovMembers));
		}
		if(personInCharge != null){
			model.addAttribute("personInCharge",JSONObject.toJSONString(personInCharge));
		}

		if(entity==null){
			throw new AnneException("没有找到需要修改的数据");
		}

		TabMain tabMainInfo = new TabMain();
		tabMainInfo.setInitAll(false);
		if (hasPermission("P03BidAuthT1OrgPage")) {
			tabMainInfo.addTab("授权单位", "/bid/authManage.html?id=" + entity.getId());
		}
		
		if (hasPermission("P03BidAuthT3ProReviewHistoryPage")) {
			tabMainInfo.addTab("审批历史", "/standard/history.html?contrId=" + entity.getId());
		}
		if (hasPermission("P03BidAuthT2FilePage")) {
			tabMainInfo.addTab("附件", "/common/attachment/attachment.html?businessType=Bid&docGroupCode=BidDOCTYPE&businessId=" + entity.getId());
		}
		model.addAttribute("tabMainInfo",tabMainInfo);

		model.addAttribute("customInfo", JSONObject.toJSONString(customInfo));
		model.addAttribute("entity",entity);
		return forward("bidAdd");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(Bid bizopp){
		Bid bidSource = bizService.getEntity(bizopp.getId(),new Bid());

		BeanUtils.copyPropertiesIgnoreNull(bizopp,bidSource);

		bizService.update(bidSource);
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		bizService.delete(id, Bid.class);
		return sendSuccessMessage();
	}

	@ResponseBody
	@RequestMapping(value="/startProcess",method = RequestMethod.POST)
	public String startAuthProcess(String id,String bidNumber){
		bizService.startAuthProcess(getUserObject(),id,bidNumber);
		return sendSuccessMessage();
	}
}
