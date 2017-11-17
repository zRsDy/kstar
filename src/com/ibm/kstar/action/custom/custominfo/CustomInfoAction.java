package com.ibm.kstar.action.custom.custominfo;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.log.LogOperate;
import org.xsnake.web.page.IPage;
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.custom.ICustomNumberService;
import com.ibm.kstar.api.system.lov.ILovGroupService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovGroup;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.custom.CustomAddressInfo;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.custom.CustomInfoChange;
import com.ibm.kstar.entity.custom.CustomRelaContact;
import com.ibm.kstar.entity.custom.vo.CustomErpAddress;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/custom/baseinfo")
public class CustomInfoAction extends BaseAction {

	@Autowired
	ICustomInfoService service;
	@Autowired
	protected ILovGroupService lovGroupService;
	@Autowired
	protected ILovMemberService lovMemberService;
	@Autowired
	ICustomNumberService numberService;
	@Autowired
	BaseDao baseDao;

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}打开了客户一览页面")
	@RequestMapping("/list")
	public String list(String id, Model model) {
		return forward("list");
	}
	
	@ResponseBody
	@NoRight
	@RequestMapping(value = "/viewtest")
	public String viewtest(String id) { 
		return sendSuccessMessage(service.getCustomInfo(id));
	}

	@NoRight
	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}打开了客户一览页面，进行客户检索")
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);

//		String searchKey = condition.getStringCondition("searchKey");
//		if(!StringUtils.isEmpty(searchKey)){
//			condition.getFilterObject().addOrCondition("customCode", "like", "%" + searchKey + "%");
//			condition.getFilterObject().addOrCondition("customFullName", "like", "%" + searchKey + "%");
//		}
		
		String erpCode = condition.getStringCondition("erpCode");
		if(StringUtil.isNotEmpty(erpCode)){
			condition.getFilterObject().addCondition("erpCode", "like", "%" + erpCode + "%");
		}

		String customFullName = condition.getStringCondition("customFullName");
		if(StringUtil.isNotEmpty(customFullName)){
			condition.getFilterObject().addCondition("customFullName", "like", "%" + customFullName + "%");
		}
		
		String customAliasName = condition.getStringCondition("customAliasName");
		if(StringUtil.isNotEmpty(customAliasName)){
			condition.getFilterObject().addCondition("customAliasName", "like", "%" + customAliasName + "%");
		}
		
		String customSource = condition.getStringCondition("customSource");
		if(StringUtil.isNotEmpty(customSource)){
			condition.getFilterObject().addCondition("customSource", "=", customSource);
		}
		
		String customClass = condition.getStringCondition("customClass");
		if(StringUtil.isNotEmpty(customClass)){
			condition.getFilterObject().addCondition("customClass", "=", customClass);
		}
		
		String customStatus = condition.getStringCondition("customStatus");
		if(StringUtil.isNotEmpty(customStatus)){
			condition.getFilterObject().addCondition("customStatus", "=", customStatus);
		}
		
		String customArea = condition.getStringCondition("customArea");
		if(StringUtil.isNotEmpty(customArea)){
			condition.getFilterObject().addCondition("customArea", "=", customArea);
		}
		
		String customGrade = condition.getStringCondition("customGrade");
		if(StringUtil.isNotEmpty(customGrade)){
			condition.getFilterObject().addCondition("customGrade", "=", customGrade);
		}
		
		String customAreaSub1 = condition.getStringCondition("customAreaSub1");
		if(StringUtil.isNotEmpty(customAreaSub1)){
			condition.getFilterObject().addCondition("customAreaSub1", "=", customAreaSub1);
		}
		
		String customCategory = condition.getStringCondition("customCategory");
		if(StringUtil.isNotEmpty(customCategory)){
			condition.getFilterObject().addCondition("customCategory", "=", customCategory);
		}
		
		String customReportedFlg = condition.getStringCondition("customReportedFlg");
		if(StringUtil.isNotEmpty(customReportedFlg)){
			condition.getFilterObject().addCondition("customReportedFlg", "=", customReportedFlg);
		}
		
		String customAreaSub2 = condition.getStringCondition("customAreaSub2");
		if(StringUtil.isNotEmpty(customAreaSub2)){
			condition.getFilterObject().addCondition("customAreaSub2", "=", customAreaSub2);
		}
		
		String customCategorySub = condition.getStringCondition("customCategorySub");
		if(StringUtil.isNotEmpty(customCategorySub)){
			condition.getFilterObject().addCondition("customCategorySub", "=", customCategorySub);
		}
		
		String customAreaSub3 = condition.getStringCondition("customAreaSub3");
		if(StringUtil.isNotEmpty(customAreaSub3)){
			condition.getFilterObject().addCondition("customAreaSub3", "=", customAreaSub3);
		}
		
		String createdById = condition.getStringCondition("createdById");
		if(StringUtil.isNotEmpty(createdById)){
			condition.getFilterObject().addCondition("createdById", "=", createdById );
		}
		
		IPage p = service.query(condition);

		return sendSuccessMessage(p);
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}打开了客户添加页面")
	@RequestMapping("/add")
	public String add(String id, Model model) {
		model.addAttribute("id", id);
		model.addAttribute("mode", IConstants.ACTION_TYPE_ADD);
		
		CustomInfo customInfo = new CustomInfo();
		customInfo.setId(null);
		customInfo.setCustomType("1");
		// 新增记录编码为空，待ERP返回
		customInfo.setCustomCode(numberService.getReportNumber());
		customInfo.setErpCode(customInfo.getCustomCode());
		
		customInfo.setCustomStatus("CUSTOMSTATUS_10");// 客户状态
		
		LovMember lov = lovMemberService.getLovMemberByCode(IConstants.ADDRESSREGION,IConstants.ADDRESSREGION_CN);
		customInfo.setCustomArea(lov.getId());
		
		customInfo.setCustomControlStatus(IConstants.CONTROLSTATUS_01);//
		
		customInfo.setErpStatus(IConstants.CUSTOM_NORMAL_STATUS_10);
		customInfo.setCustomReportedFlg(IConstants.CUSTOM_NORMAL_STATUS_10);

		TabMain tabMain = new TabMain();
		tabMain.setInitAll(false);
		model.addAttribute("tabMain", tabMain);
		model.addAttribute("personCnt","0");
		model.addAttribute("customInfo", customInfo);
		model.addAttribute("P_GJORG_B1_0001", this.isP_GJORG_B1_0001(this.getUserObject().getOrg().getId()));
		model.addAttribute("P_GNORG_B1_0001", this.P_GNORG_B1_0001(this.getUserObject().getOrg().getId()));
		model.addAttribute("P_GNGFORG_B1_0001", this.P_GNGFORG_B1_0001(this.getUserObject().getOrg().getId()));
		model.addAttribute("P_GNQCORG_B1_0001", this.P_GNQCORG_B1_0001(this.getUserObject().getOrg().getId()));
		return forward("add");
	}

	

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}点击新增保存功能")
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(CustomInfo customInfo,
			HttpServletRequest request) {
		String area = customInfo.getCustomArea();
		
		LovMember lov = lovMemberService.get(area);
		if (lov != null && StringUtils.equals(lov.getCode(), IConstants.ADDRESSREGION_CN)) {
			if (StringUtils.isEmpty(customInfo.getCustomAreaSub1()) 
					|| StringUtils.isEmpty(customInfo.getCustomAreaSub2())) {
				throw new AnneException("当国家为[中国]时，请输入省市!");
			}
		}
		
		customInfo.setPrmFlg(IConstants.CRM_FLAG);
		
		// main info
		// 功能字段设值
		// 创建字段
		customInfo.setCreatedById(getUserObject().getEmployee().getId());
		customInfo.setCreatedAt(new Date());
		customInfo.setCreatedPosId(getUserObject().getPosition().getId());
		customInfo.setCreatedOrgId(getUserObject().getOrg().getId());
		// 更新字段
		customInfo.setUpdatedById(getUserObject().getEmployee().getId());
		customInfo.setUpdatedAt(new Date());
		service.saveCustomInfo(customInfo, getUserObject());
		
		return sendSuccessMessage(customInfo.getId());
	}

	@NoRight
	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}打开了编辑页面")
	@RequestMapping("/edit")
	public String edit(String id, String flg, Model model, PageCondition condition, HttpServletRequest request) {
		if (id == null) {
			throw new AnneException("没有找到需要修改的数据");
		}

		CustomInfo customInfo = service.getCustomInfo(id);
		if (customInfo == null) {
			throw new AnneException("没有找到需要修改的数据");
		}
		
		model.addAttribute("customInfo", customInfo);

		TabMain tabMain = new TabMain();
		tabMain.setInitAll(false);
		
		if(hasPermission("P02CusT1BasePage")) {
			tabMain.addTab("地址信息", "/custom/baseinfo/customBase.html?id=" + customInfo.getId());	
		}
		
		if(hasPermission("P02CusT2FinancePage")) {
			tabMain.addTab("ERP引入", "/custom/baseinfo/customFinance.html?id=" + customInfo.getId());
		}

		if(hasPermission("P02CusT3RelationPage")) {
			tabMain.addTab("客户关系", "/custom/baseinfo/customReleation.html?id=" + customInfo.getId());
		}	

		if(hasPermission("P02CusT4DealerInfoPage")) {
			tabMain.addTab("经销商信息", "/custom/baseinfo/customDistrabutor.html?id=" + customInfo.getId());
		}

		if(hasPermission("P02CusT5CompetitorReportPage")) {
			tabMain.addTab("竞争对手信息", "/custom/baseinfo/customCompetitor.html?id=" + customInfo.getId());
		}
		
		if(hasPermission("P02CusT6FilePage")) {
			if(customInfo.getErpStatus() != null){
				if(customInfo.getErpStatus().equals("CUSTOM_NORMAL_STATUS_20")){
					tabMain.addTab("附件信息","/common/attachment/attachment.html?businessType=ACCOUNT_FILE&docGroupCode=CONTRACTDOCTYPE&businessId="+customInfo.getId()+"&unableAdd=true&unableDelete=true");
				}else if(customInfo.getErpStatus().equals("CUSTOM_NORMAL_STATUS_30")){
					tabMain.addTab("附件信息","/common/attachment/attachment.html?businessType=ACCOUNT_FILE&docGroupCode=CONTRACTDOCTYPE&businessId="+customInfo.getId()+"&unableAdd=false&unableDelete=true");
				}else{
					tabMain.addTab("附件信息","/common/attachment/attachment.html?businessType=ACCOUNT_FILE&docGroupCode=CONTRACTDOCTYPE&businessId="+customInfo.getId()+"&unableAdd=false&unableDelete=false");
				}
			}
		}
		
		if(hasPermission("P02CusT7TeamPage")) {
			tabMain.addTab("销售团队", "/team/list.html?businessType=" + IConstants.CUSTOM_REPORT_PROC + "&businessId=" + customInfo.getId());
		}
		
		if(hasPermission("P02CusT8OrgListPage")) {
			tabMain.addTab("组织列表", "/orgTeam/list.html?businessType=" + IConstants.CUSTOM_REPORT_PROC + "&businessId=" + customInfo.getId());
		}
		
		if(hasPermission("P02CusT9ProReviewHistoryPage")) {
			tabMain.addTab("审批历史", "/standard/history.html?contrId="+customInfo.getId());
		}
		
		if(hasPermission("P02CusT10360ViewPage")) {
			tabMain.addTab("360°业务查询", "/custom/baseinfo/custom360.html?id=" + customInfo.getId());
		}
		tabMain.addTab("客户变更历史", "/custom/change/customChangeHis.html?customInfoId=" + customInfo.getId());
		
		String applyStatus = customInfo.getCustomReportedFlg();
		
		boolean saveFlg = true;// 保存
		boolean updateFlg = true;// 提交变更
		boolean applyFlg = true;// 启动报备
		boolean erpApplyFlg = true;// 提交引入申请
		
		CustomInfoChange customInfoChange = new CustomInfoChange();
		List<CustomInfoChange> entitys = service.getCustomInfoChangeByCustomId(id);

		if (entitys != null && entitys.size() > 0) {
			customInfoChange =  entitys.get(0);
		} 
		String updateStatus = customInfoChange.getStatus();
		
		String[] statusErp20 = new String[]{IConstants.CUSTOM_NORMAL_STATUS_20};
		List<CustomErpAddress> customErpAddress20 =  service.getErpInfoByIdStatus(id, statusErp20);
		
		String[] statusErp40 = new String[]{IConstants.CUSTOM_NORMAL_STATUS_40};
		List<CustomErpAddress> customErpAddress40 =  service.getErpInfoByIdStatus(id, statusErp40);
		
		String[] statusErp1030 = new String[]{IConstants.CUSTOM_NORMAL_STATUS_10, IConstants.CUSTOM_NORMAL_STATUS_30};
		List<CustomErpAddress> customErpAddress1030 =  service.getErpInfoByIdStatus(id, statusErp1030);
		
		if((customErpAddress20 == null || customErpAddress20.size() <= 0) 
				&& !StringUtils.equals(IConstants.CUSTOM_NORMAL_STATUS_20, applyStatus) 
				&& !StringUtils.equals(IConstants.CUSTOM_NORMAL_STATUS_20, updateStatus) ) {
			saveFlg = false;
		} 
		
		if(((customErpAddress20 == null || customErpAddress20.size() <= 0) 
				&& !StringUtils.equals(IConstants.CUSTOM_NORMAL_STATUS_20, applyStatus) 
				&& !StringUtils.equals(IConstants.CUSTOM_NORMAL_STATUS_20, updateStatus))
				
			&& (StringUtils.equals(IConstants.CUSTOM_NORMAL_STATUS_40, applyStatus)
				 || (customErpAddress40 != null && customErpAddress40.size() > 0))) {
			updateFlg = false;
		} 
		
		if(((customErpAddress20 == null || customErpAddress20.size() <= 0) 
				&& !StringUtils.equals(IConstants.CUSTOM_NORMAL_STATUS_20, applyStatus) 
				&& !StringUtils.equals(IConstants.CUSTOM_NORMAL_STATUS_20, updateStatus))
				
			&& (StringUtils.equals(IConstants.CUSTOM_NORMAL_STATUS_10, applyStatus)
				 || StringUtils.equals(IConstants.CUSTOM_NORMAL_STATUS_30, applyStatus))) {
			applyFlg = false;
		} 
		
		if(((customErpAddress20 == null || customErpAddress20.size() <= 0) 
				&& !StringUtils.equals(IConstants.CUSTOM_NORMAL_STATUS_20, applyStatus) 
				&& !StringUtils.equals(IConstants.CUSTOM_NORMAL_STATUS_20, updateStatus))
				
			&& (customErpAddress1030 != null && customErpAddress1030.size() >= 0)) {
			erpApplyFlg = false;
		} 
		
		model.addAttribute("saveFlg", Boolean.valueOf(saveFlg).toString());
		model.addAttribute("updateFlg", Boolean.valueOf(updateFlg).toString());
		model.addAttribute("applyFlg", Boolean.valueOf(applyFlg).toString());
		model.addAttribute("erpApplyFlg", Boolean.valueOf(erpApplyFlg).toString());
		
		List<CustomRelaContact> customRelaContacts =  service.getContactInfoBycode(id);
		if(customRelaContacts == null || customRelaContacts.size() <= 0) {
			model.addAttribute("personCnt", "0");
		} else {
			model.addAttribute("personCnt", customRelaContacts.size());
		}
		model.addAttribute("mode", IConstants.ACTION_TYPE_EDIT);
		model.addAttribute("tabMain", tabMain);
		model.addAttribute("P_GJORG_B1_0001", this.isP_GJORG_B1_0001(this.getUserObject().getOrg().getId()));
		model.addAttribute("P_GNORG_B1_0001", this.P_GNORG_B1_0001(this.getUserObject().getOrg().getId()));
		model.addAttribute("P_GNGFORG_B1_0001", this.P_GNGFORG_B1_0001(this.getUserObject().getOrg().getId()));
		model.addAttribute("P_GNQCORG_B1_0001", this.P_GNQCORG_B1_0001(this.getUserObject().getOrg().getId()));
		model.addAttribute("isERPCustom",service.isErpCustom(customInfo.getId()));
		
		return forward("add");
	}

	@NoRight
	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}点击编辑保存功能")
	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(CustomInfo customInfo) {
		String area = customInfo.getCustomArea();
		
		LovMember lov = lovMemberService.get(area);
		if (lov != null && StringUtils.equals(lov.getCode(), IConstants.ADDRESSREGION_CN)) {
			if (StringUtils.isEmpty(customInfo.getCustomAreaSub1()) 
					|| StringUtils.isEmpty(customInfo.getCustomAreaSub2())) {
				throw new AnneException("当国家为[中国]时，请输入省市!");
			}
		}
		
		List<CustomAddressInfo> customAddressInfoList = service.getCustomAddressInfoBycustomId(customInfo.getId());
		for (CustomAddressInfo customAddressInfo : customAddressInfoList) {
			customAddressInfo.setLayer1(customInfo.getCustomArea());
			customAddressInfo.setLayer2(customInfo.getCustomAreaSub1());
			customAddressInfo.setLayer3(customInfo.getCustomAreaSub2());
			customAddressInfo.setLayer4(customInfo.getCustomAreaSub3());
			customAddressInfo.setCustomAddress(customInfo.getCorpRegAddress());
			service.updateAddrInfo(customAddressInfo);
		}
		
		// 功能字段设值
		// 更新字段
		customInfo.setUpdatedById(getUserObject().getEmployee().getId());
		customInfo.setUpdatedAt(new Date());
		service.updateCustomInfo(customInfo);
		
		return sendSuccessMessage(customInfo.getId());
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}点击客户删除功能")
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(String id) {
		service.deleteCustomInfo(id);
		return sendSuccessMessage();
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}点击基础信息列表")
	@RequestMapping("/customBase")
	public String customBase(String id, Model model) {
		CustomInfo customInfo = new CustomInfo();
		if (!StringUtils.isEmpty(id)) {
			customInfo = service.getCustomInfo(id);
		}

		model.addAttribute("customInfo", customInfo);
		return forward("customBase");
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}点击财务信息列表")
	@RequestMapping("/customFinance")
	public String customFinance(String id, Model model) {
		CustomInfo customInfo = new CustomInfo();
		if (!StringUtils.isEmpty(id)) {
			customInfo = service.getCustomInfo(id);
		}

		model.addAttribute("customInfo", customInfo);
		return forward("customFinance");
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}点击客户关系列表")
	@RequestMapping("/customReleation")
	public String customReleation(String id, Model model) {
		CustomInfo customInfo = new CustomInfo();
		if (!StringUtils.isEmpty(id)) {
			customInfo = service.getCustomInfo(id);
		}

		model.addAttribute("customInfo", customInfo);
		return forward("customReleation");
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}点击经销商信息列表")
	@RequestMapping("/customDistrabutor")
	public String customDistrabutor(String id, Model model) {
		CustomInfo customInfo = new CustomInfo();
		if (!StringUtils.isEmpty(id)) {
			customInfo = service.getCustomInfo(id);
		}

		model.addAttribute("customInfo", customInfo);
		return forward("customDistrabutor");
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}点击竞争对手信息列表")
	@RequestMapping("/customCompetitor")
	public String customCompetitor(String id, Model model) {
		CustomInfo customInfo = new CustomInfo();
		if (!StringUtils.isEmpty(id)) {
			customInfo = service.getCustomInfo(id);
		}

		model.addAttribute("customInfo", customInfo);
		return forward("customCompetitor");
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}展示360业务查询信息")
	@RequestMapping("/custom360")
	public String custom360(String id, Model model) {
		CustomInfo customInfo = new CustomInfo();
		if (!StringUtils.isEmpty(id)) {
			customInfo = service.getCustomInfo(id);
		}

		TabMain tabMain = new TabMain();
		tabMain.setInitAll(false);
		if(hasPermission("P02CusT10360ViewReceptionPage")) {
			tabMain.addTab("来访接待", "/custom/baseinfo/info360/event360.html?id=" + customInfo.getId());
		}
		if (hasPermission("P02CusT10360ViewBizOppoPage")) {
			tabMain.addTab("商机", "/custom/baseinfo/info360/bizopp360.html?id=" + customInfo.getId());
		}
		if (hasPermission("P02CusT10360ViewQuotePage")) {
			tabMain.addTab("报价", "/custom/baseinfo/info360/quot360.html?id=" + customInfo.getId());
		}

		if (hasPermission("P02CusT10360ViewContractPage")) {
			tabMain.addTab("合同", "/custom/baseinfo/info360/contract360.html?id=" + customInfo.getId());
		}
		if (hasPermission("P02CusT10360ViewOrderPage")) {
			tabMain.addTab("订单", "/custom/baseinfo/info360/order360.html?id=" + customInfo.getId());
		}
		if (hasPermission("P02CusT10360ViewOrderPage")) {
			tabMain.addTab("应收", "/custom/baseinfo/info360/receive360.html?id=" + customInfo.getId());
		}

		model.addAttribute("subTabMain", tabMain);
		model.addAttribute("customInfo", customInfo);
		return forward("custom360");
	}


	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}打开了组织结构树页面")
	@RequestMapping("/tree")
	public String tree(String groupId, Model model) {
		LovGroup lovGroup = lovGroupService.get(IConstants.CUSTOM_ORG_TREE);
		model.addAttribute("lovGroup", lovGroup);
		return forward("tree");
	}
	
	@NoRight
	@LogOperate(module="客户管理模块：建档报备",notes="${user}查看组织结构树分页信息")
	@ResponseBody
	@RequestMapping("/treePage")
	public String treePage(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		String groupId = condition.getStringCondition("groupId");
		String parentId = condition.getStringCondition("parentId");
		String searchKey = condition.getStringCondition("searchKey");
		String optTxt1 = condition.getStringCondition("optTxt1");
		condition.getFilterObject().addCondition("deleteFlag", "eq", "N");
		condition.getFilterObject().addCondition("groupId", "eq", groupId);
		if(parentId !=null){
			condition.getFilterObject().addCondition("path", "like", "%"+parentId+"%");
		}
		if(searchKey !=null){
			condition.getFilterObject().addOrCondition("name", "like", "%"+searchKey+"%");
			condition.getFilterObject().addOrCondition("code", "like", "%"+searchKey+"%");
		}
		if(StringUtil.isNotEmpty(optTxt1)){
			condition.getFilterObject().addCondition("optTxt1", "=", optTxt1);
		}
		IPage p = lovMemberService.query(condition);
		return sendSuccessMessage(p);
	}
	
	@ResponseBody
	@RequestMapping("/treeSet")
	public String treeSet(Condition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		String groupCode = condition.getStringCondition("groupCode");
		String groupId = condition.getStringCondition("groupId");
		String optTxt1 = condition.getStringCondition("optTxt1");
		String leafFlag = condition.getStringCondition("leafFlag");
		if(StringUtil.isEmpty(groupId) && StringUtil.isEmpty(groupCode)){
			throw new AnneException("无效的参数访问");
		}
		if(StringUtil.isEmpty(groupId)){
			LovGroup group = lovGroupService.getByCode(groupCode);
			if(group == null){
				throw new AnneException("无效的参数访问");
			}
			groupId = group.getId();
		}
		String parentId = condition.getStringCondition("id");
		condition.getFilterObject().addCondition("groupId", "=", groupId);
		condition.getFilterObject().addCondition("deleteFlag", "=", "N");
		if(parentId == null){
			parentId = "ROOT";
		}
		
		if(StringUtil.isNotEmpty(leafFlag)){
			condition.getFilterObject().addCondition("leafFlag", "=", leafFlag);
		}
		
		if(StringUtil.isNotEmpty(optTxt1)){
			condition.getFilterObject().addCondition("optTxt1", "=", optTxt1);
		}
		
		condition.getFilterObject().addCondition("parentId", "=", parentId);
		List<LovMember> list = lovMemberService.getList(condition);
		return sendSuccessMessage(list);
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}打开了维度树页面")
	@RequestMapping("/itemShow")
	public String itemShow(String id, Model model) {
		LovMember lovMember = lovMemberService.get(id);

		CustomInfo customInfo = service.getCustomInfoByCodeWithAuth(lovMember.getCode(), getUserObject());

		model.addAttribute("customInfo", customInfo);
		
		TabMain tabMain = new TabMain();
		tabMain.setInitAll(false);
		if(hasPermission("P02CusT1BasePage")) {
			tabMain.addTab("地址信息", "/custom/baseinfo/customBase.html?id=" + customInfo.getId());	
		}
		
		if(hasPermission("P02CusT2FinancePage")) {
			tabMain.addTab("ERP引入", "/custom/baseinfo/customFinance.html?id=" + customInfo.getId());
		}

		if(hasPermission("P02CusT3RelationPage")) {
			tabMain.addTab("客户关系", "/custom/baseinfo/customReleation.html?id=" + customInfo.getId());
		}	

		if(hasPermission("P02CusT4DealerInfoPage")) {
			tabMain.addTab("经销商信息", "/custom/baseinfo/customDistrabutor.html?id=" + customInfo.getId());
		}

		if(hasPermission("P02CusT5CompetitorReportPage")) {
			tabMain.addTab("竞争对手信息", "/custom/baseinfo/customCompetitor.html?id=" + customInfo.getId());
		}
		
		if(hasPermission("P02CusT6FilePage")) {
			tabMain.addTab("附件信息","/common/attachment/attachment.html?businessType=ACCOUNT_FILE&docGroupCode=CONTRACTDOCTYPE&businessId="+customInfo.getId());
		}
		
		if(hasPermission("P02CusT7TeamPage")) {
			tabMain.addTab("销售团队", "/team/list.html?businessType=" + IConstants.CUSTOM_REPORT_PROC + "&businessId=" + customInfo.getId());
		}
		
		if(hasPermission("P02CusT8OrgListPage")) {
			tabMain.addTab("组织列表", "/orgTeam/list.html?businessType=" + IConstants.CUSTOM_REPORT_PROC + "&businessId=" + customInfo.getId());
		}
		
		if(hasPermission("P02CusT9ProReviewHistoryPage")) {
			tabMain.addTab("审批历史", "/standard/history.html?contrId="+customInfo.getId());
		}
		
		if(hasPermission("P02CusT10360ViewPage")) {
			tabMain.addTab("360°业务查询", "/custom/baseinfo/custom360.html?id=" + customInfo.getId());
		}

		model.addAttribute("tabMain", tabMain);
		return forward("add");
	}
	
	//1.数据中心   P_GNORG_B1_0001
	//2.光伏          P_GNGFORG_B1_0001
	//3.充电桩      P_GNQCORG_B1_0001
	//4.国际          P_GJORG_B1_0001
	public boolean isP_GJORG_B1_0001(String orgId){
		String sql = "select m.row_id,m.lov_name from sys_t_lov_member m where m.row_id = 'P_GJORG_B1_0001'"
				+ " START WITH m.row_id = ? CONNECT BY PRIOR m.parent_id = m.row_id";
		List<Object[]> lst = baseDao.findBySql(sql, orgId);
		if(lst != null && lst.size() > 0){
			return true;
		}
		return false;
	}
	
	public boolean P_GNORG_B1_0001(String orgId){
		String sql = "select m.row_id,m.lov_name from sys_t_lov_member m where m.row_id = 'P_GNORG_B1_0001'"
				+ " START WITH m.row_id = ? CONNECT BY PRIOR m.parent_id = m.row_id";
		List<Object[]> lst = baseDao.findBySql(sql, orgId);
		if(lst != null && lst.size() > 0){
			return true;
		}
		return false;
	}
	
	public boolean P_GNGFORG_B1_0001(String orgId){
		String sql = "select m.row_id,m.lov_name from sys_t_lov_member m where m.row_id = 'P_GNGFORG_B1_0001'"
				+ " START WITH m.row_id = ? CONNECT BY PRIOR m.parent_id = m.row_id";
		List<Object[]> lst = baseDao.findBySql(sql, orgId);
		if(lst != null && lst.size() > 0){
			return true;
		}
		return false;
	}
	
	public boolean P_GNQCORG_B1_0001(String orgId){
		String sql = "select m.row_id,m.lov_name from sys_t_lov_member m where m.row_id = 'P_GNQCORG_B1_0001'"
				+ " START WITH m.row_id = ? CONNECT BY PRIOR m.parent_id = m.row_id";
		List<Object[]> lst = baseDao.findBySql(sql, orgId);
		if(lst != null && lst.size() > 0){
			return true;
		}
		return false;
	}

}
