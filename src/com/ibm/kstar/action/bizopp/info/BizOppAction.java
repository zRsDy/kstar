package com.ibm.kstar.action.bizopp.info;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.api.bizopp.IBizoppService;
import com.ibm.kstar.api.common.doc.IKstarAttachmentService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.price.IPriceHeadService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.permission.IEmployeeService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.entity.bizopp.BizOppChange;
import com.ibm.kstar.entity.bizopp.BusinessOpportunity;
import com.ibm.kstar.entity.bizopp.ProductDetail;
import com.ibm.kstar.entity.common.doc.KstarAttachment;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.product.ProductPriceHead;
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
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.StringUtil;
import org.xsnake.xflow.api.IHistoryService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ClassName:BizOppAction <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: Jan 6, 2017 10:07:56 AM <br/>
 * 
 * @author gaoyuliang
 * @version
 * @since JDK 1.7
 * @see
 */

@Controller
@RequestMapping("/bizopp")
public class BizOppAction extends BaseAction {

	@Autowired
	IBizoppService bizService;

	@Autowired
	ILovMemberService lovMemberService;

	@Autowired
	ICustomInfoService customerService;

	@Autowired
	IPriceHeadService priceHeadService;

	@Autowired
	IHistoryService historyService;
	
	@Autowired
	private IKstarAttachmentService attachmentService;
	
	@Autowired
	IBizoppService bizoppService;
	
	@Autowired
	BaseDao baseDao;
	
	@RequestMapping("list")
	public String index(String id, Model model) {
		return forward("list");
	}
	
	@ResponseBody
	@NoRight
	@RequestMapping(value = "/viewtest")
	public String viewtest(String id) { 
		return sendSuccessMessage(bizService.get(id));
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);

		String searchKey = condition.getStringCondition("search");
		if(!StringUtils.isEmpty(searchKey)){
			condition.getFilterObject().addOrCondition("opportunityName", "like", "%" + searchKey + "%");
			condition.getFilterObject().addOrCondition("number", "like", "%" + searchKey + "%");
		}

        ActionUtil.doSearch(condition);
        
        String select = condition.getStringCondition("pageSearch_select");
//        condition.setCondition("pageSearch_conflictStatus", "40"); // 已提交申诉
        
        IPage p;
        if (!StringUtil.isNullOrEmpty(select)) {
        	p = bizService.queryBo(condition, getUserObject());
		}else{
			p = bizService.query(condition, getUserObject());
		}

		return sendSuccessMessage(p);
	}


    @RequestMapping("/add")
	public String add(Model model, String customId){

		BusinessOpportunity entity = new BusinessOpportunity();
		
		if(!StringUtils.isEmpty(customId)) {
			CustomInfo customInfo = customerService.getCustomInfo(customId);
			if (customInfo == null) {
				throw new AnneException("没有找到需要报备商机的客户！");
			}
			entity.setClientId(customId);
			entity.setClientName(customInfo.getCustomFullName());
			entity.setIndustry(customInfo.getCustomCategoryName());
			entity.setIndustrySub(customInfo.getCustomCategorySubName());
			
			entity.setIndustry(customInfo.getCustomCategoryName());
			entity.setIndustrySub(customInfo.getCustomCategorySubName());
			
			entity.setIndustryCode(customInfo.getCustomCategory());
			entity.setIndustrySubCode(customInfo.getCustomCategorySub());
			
			entity.setComContact(customInfo.getContactName());
			entity.setComDept(customInfo.getContactRole());
			
			entity.setLayer2(customInfo.getCustomAreaSub1());
			entity.setLayer3(customInfo.getCustomAreaSub2());
			entity.setLayer4(customInfo.getCustomAreaSub3());
			entity.setAddress(customInfo.getCorpRegAddress());
		}
		
		UserObject userObject = getUserObject();
		String userName = userObject.getEmployee().getName();

		Object[] res = lovMemberService.getSaleMethod(getUserObject().getOrg().getId());
		if (res != null && res.length > 0) {
			String saleMethod = String.valueOf(res[2]);// 这里要取当前登录人所属营销中心。再从配置里取营销中心对应的销售方法。暂时取不到，留下：）
			entity.setSaleMethod(saleMethod);
			String sbu = String.valueOf(res[1]);
			entity.setSbu_flag(sbu);
		}


		//商机编号
		String bizOppId = bizService.getBizOppNumber();
		entity.setNumber(bizOppId);
		entity.setCreatedById(getUserObject().getEmployee().getId());
		entity.setEnterprise(getUserObject().getOrg().getId());
		entity.setEnterpriseName(getUserObject().getOrg().getNamePath());
		entity.setCreatedAt(new Date());

		String statusDefault = "NEW";
		String currencyDefalt = lovMemberService.getLovMemberByCode("CURRENCY", "CNY").getId();
		
		entity.setStatus(statusDefault);
		entity.setConflictStatus("00");
		entity.setCurrency(currencyDefalt);

		model.addAttribute("entity", entity);
		model.addAttribute("userType",getUserObject().getEmployee().getFlag());


		ProductPriceHead productPriceHead = priceHeadService.getDefaultPriceHead(getUserObject().getOrg().getId());
		if (productPriceHead != null) {
			model.addAttribute("priceTableId", productPriceHead.getId());
		} else {
			throw new AnneException("默认价格表不能为空！");
		}

		model.addAttribute("changeStatus", false);

		TabMain tabMainInfo = new TabMain();
		TabMain tabMainInfo1 = new TabMain();
		TabMain tabMainInfo2 = new TabMain();
		model.addAttribute("tabMainInfo",tabMainInfo);
		model.addAttribute("tabMainInfo1",tabMainInfo1);
		model.addAttribute("tabMainInfo2",tabMainInfo2);
		model.addAttribute("customId",customId);
		
		model.addAttribute("P_GJORG_B1_0001", this.isP_GJORG_B1_0001(this.getUserObject().getOrg().getId()));
		model.addAttribute("P_GNORG_B1_0001", this.P_GNORG_B1_0001(this.getUserObject().getOrg().getId()));
		model.addAttribute("P_GNGFORG_B1_0001", this.P_GNGFORG_B1_0001(this.getUserObject().getOrg().getId()));
		model.addAttribute("P_GNQCORG_B1_0001", this.P_GNQCORG_B1_0001(this.getUserObject().getOrg().getId()));
		
		return forward("bizoppAdd");
	}
	
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(BusinessOpportunity entity, HttpServletRequest request) {
		
		if("10".equals(entity.getBidMethod())){
			if(StringUtil.isEmpty(entity.getPostAddress()) 
					|| StringUtil.isEmpty(entity.getPerson()) 
					|| StringUtil.isEmpty(entity.getTel())
					){
				throw new AnneException("邮寄地址,联系人,电话 不能为空");
			}
		}
		
		entity.fillInit(getUserObject());
		bizService.save(entity,getUserObject());
		return sendSuccessMessage(entity.getId());
	}

	@Autowired
	IEmployeeService employeeService;

	@NoRight
	@RequestMapping("/edit")
	public String edit(String id,Model model){

		if(id==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		BusinessOpportunity entity = bizService.getBizOppEntity(id);
		if(entity==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		CustomInfo cf = customerService.getCustomInfo(entity.getClientId());
		if (cf != null) {
			entity.setCustomClassName(cf.getCustomClassName());
		}

		ProductPriceHead productPriceHead = priceHeadService.getDefaultPriceHead(getUserObject().getOrg().getId());
		if (productPriceHead != null) {
			model.addAttribute("priceTableId", productPriceHead.getId());
		} else {
			throw new AnneException("默认价格表不能为空！");
		}

		List<BizOppChange> has = bizService.getBizOppChange(id, ProcessConstants.PROCESS_STATUS_Completed);
		if (has != null || has.size() != 0) {
			model.addAttribute("changeStatus", true);
		} else {
			model.addAttribute("changeStatus", false);
		}

		model.addAttribute("entity",entity);
		Employee employee = employeeService.get(entity.getCreatedById());
		model.addAttribute("userType",employee.getFlag());
		TabMain tabMainInfo1 = new TabMain();
		TabMain tabMainInfo2 = new TabMain();

		TabMain tabMainInfo = new TabMain();
		tabMainInfo.setInitAll(false);
		tabMainInfo1.setInitAll(false);
		tabMainInfo2.setInitAll(false);
		if (hasPermission("P03BizOppoT1ConfigPage")) {
			tabMainInfo2.addTab("商机配置", "/bizopp/mainInfo.html?id=" + entity.getId() + "&status=" + entity.getStatus());
		}
		//数据中心1展示“集成商”标签页，光伏2展示“决策链”、“竞争分析”标签。
		if (hasPermission("P03BizOppoT2IntegratorPage")) {
			tabMainInfo1.addTab("授权单位", "/bizopp/integrator.html?id=" + entity.getId());
		}
		if (hasPermission("P03BizOppoT3DecisionOrgPage")) {
			tabMainInfo.addTab("决策链", "/bizopp/decisionChain.html?id=" + entity.getId());
		}
		if (hasPermission("P03BizOppoT4CompetitorAnalisisPage")) {
			tabMainInfo.addTab("竞争分析", "/bizopp/competitionAnalysis.html?id=" + entity.getId());
		}
		if (hasPermission("P03BizOppoT9FilePage")) {
			if("NEW".equals(entity.getStatus()) 
					|| "30".equals(entity.getStatus())){
				tabMainInfo.addTab("附件", "/common/attachment/attachment.html?businessType=BusinessOpportunity&docGroupCode=BusinessOpportunity&businessId=" + entity.getId() + "&unableAdd=false&unableDelete=false");
			}else{
				tabMainInfo.addTab("附件", "/common/attachment/attachment.html?businessType=BusinessOpportunity&docGroupCode=BusinessOpportunity&businessId=" + entity.getId()+ "&unableAdd=true&unableDelete=true");
			}
		}
		if (hasPermission("P03BizOppoT5ProjFormT1OverViewPage")) {
			tabMainInfo.addTab("工程信息", "/quot/prjpages.html?typ=0008&qid=" + entity.getId());
		}
		if (hasPermission("P03BizOppoT6RelatedBizPage")) {
			tabMainInfo.addTab("关联业务", "/bizopp/relatedBusiness.html?id=" + entity.getId());
		}
		if (hasPermission("P03BizOppoT7TeamMemberPage")) {
			tabMainInfo.addTab("销售团队", "/team/list.html?businessType=BusinessOpportunity&businessId=" + entity.getId());
		}
		if (hasPermission("P03BizOppoT8ProReviewHistoryPage")) {
			tabMainInfo.addTab("审批历史", "/bizopp/listById.html?keyId=" + entity.getId());
		}
		if (hasPermission("P03BizOppoT10ReportDeskPage")) {
			tabMainInfo.addTab("报备工作台", "/bizopp/adjust.html?id=" + entity.getId());
		}
		model.addAttribute("tabMainInfo",tabMainInfo);
		model.addAttribute("tabMainInfo1",tabMainInfo1);
		model.addAttribute("tabMainInfo2",tabMainInfo2);
		
		model.addAttribute("P_GJORG_B1_0001", this.isP_GJORG_B1_0001(this.getUserObject().getOrg().getId()));
		model.addAttribute("P_GNORG_B1_0001", this.P_GNORG_B1_0001(this.getUserObject().getOrg().getId()));
		model.addAttribute("P_GNGFORG_B1_0001", this.P_GNGFORG_B1_0001(this.getUserObject().getOrg().getId()));
		model.addAttribute("P_GNQCORG_B1_0001", this.P_GNQCORG_B1_0001(this.getUserObject().getOrg().getId()));
		
		return forward("bizoppAdd");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(BusinessOpportunity bizopp){
		bizService.update(bizopp,getUserObject());
		return sendSuccessMessage(bizopp.getId());
	}

	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		if(StringUtil.isEmpty(id)){
			throw new AnneException("id is null");
		}
		BusinessOpportunity contr = bizService.get(id);
		if(!contr.getStatus().equals("NEW")){
			throw new AnneException("商机不为新建状态下无法删除");	
		}
		bizService.delete(id);
		return sendSuccessMessage();
	}
	
	@RequestMapping("/competitionAnalysis")
	public String bizoppAddTab3(String id, Model model) {
		model.addAttribute("id", id);
		return forward("competitionAnalysis");
	}
	
	@RequestMapping("/mainInfo")
	public String bizoppMainInfo(String id, Model model) {
		model.addAttribute("id", id);
		model.addAttribute("P_GNORG_B1_0001", this.P_GNORG_B1_0001(this.getUserObject().getOrg().getId()));
		return forward("mainInfo");
	}
	
	@RequestMapping("/decisionChain")
	public String bizoppDecisionChain(String id, Model model) {
		model.addAttribute("id", id);
		return forward("decisionChain");
	}

	@RequestMapping("/integrator")
	public String bizoppIntegrator(String id, Model model) {
		model.addAttribute("id", id);
		return forward("integrator");
	}
	
	@RequestMapping("/integratorChange")
	public String bizoppIntegratorChange(String id, Model model) {
		model.addAttribute("id", id);
		return forward("integratorChange");
	}
	
	@RequestMapping("/adjust")
	public String adjust(String id, Model model) {
		model.addAttribute("id", id);
		return forward("adjust");
	}
	
	@RequestMapping("/relatedBusiness")
	public String bizRelatedBusiness(String id, Model model) {
		model.addAttribute("id", id);
		return forward("relatedBusiness");
	}
	
	@RequestMapping("/saleTeam")
	public String bizsaleTeam(String id, Model model) {
		model.addAttribute("id", id);
		return forward("saleTeam");
	}
	
	@RequestMapping("/areaSelect")
	public String customAreaSelect(String pickerId, Model model, String customCategory){
		model.addAttribute("pickerId",pickerId);
		
		return forward("popup/selectArea");
	}
	
	@RequestMapping("/buildAndApprove")
	public String buildAndApprove() {
		return forward("buildAndApprove");
	}

	@ResponseBody
	@RequestMapping(value = "/buildAndApprove", method = RequestMethod.POST)
	public String buildAndApprove(PageCondition condition,String bizOppId, HttpServletRequest request) throws Exception {
//		//立项类型
//		String projectType = request.getParameter("projectType");
//		//是否投标项目
//		String isBidProject = request.getParameter("isBidProjectBox");
//
//		String quotName = request.getParameter("quotName");
//
//		String isProReview = request.getParameter("isProReviewBox");
//
//		String contrName = request.getParameter("contrName");
		
		// 更新立项类型等数据，暂存用于审批结束后生成报价单/合同
//		bizService.updateProjectType(projectType, bizOppId,isBidProject,quotName,isProReview,contrName);
		
		ActionUtil.requestToCondition(condition, request); 
		String bizId = condition.getStringCondition("bizOppId");
		condition.getFilterObject().addCondition("bizId", "=", bizId);
		List<KstarAttachment>  list = attachmentService.getAttachmentList(condition);
		if(list.size() == 0){
			throw new AnneException("请先上传附件后再进行立项审批");
		}
		
		// 调用审批流 审批流通过后，调用下面的代码生成报价单或者框架协议合同
		BusinessOpportunity bu = bizService.getBizOppEntity(bizOppId);
		bizService.startProjectInitProcess(bizOppId,bu.getNumber(),getUserObject());

		return sendSuccessMessage();
	}

	/**
	 * 提交报备
	 * @param condition
	 * @param request
	 * @param id
	 * @param number
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/startPrepareProcess",method = RequestMethod.POST)
	public String startPrepareProcess(PageCondition condition,HttpServletRequest request,String id ,String number){
		
		ActionUtil.requestToCondition(condition, request);
		String bizOppId = request.getParameter("id");
		String bizOppIdProduct = condition.getStringCondition("bizOppIdProduct");
		bizOppId = bizOppIdProduct != null ? bizOppIdProduct : bizOppId;condition.getFilterObject().addCondition("bizOppId", "eq", id);
		IPage p = bizoppService.queryProductDetail(condition);
		List<ProductDetail> list = (List<ProductDetail>)p.getList();
		if(list.size()==0){
			throw new AnneException("项目配置明细列表不能为空!");
		}else{
			for(ProductDetail productDetail : list) {
				if(productDetail.getPlanCount() == 0|| "".equals(productDetail.getPlanCount())||productDetail.getPlanCount().equals(null)){
					throw new AnneException("商机配置中数量存在为0或者为空的产品！");
				}
			}
		}
		
		bizService.startPrepareProcess(id,number,getUserObject());
		
		bizService.sendEmail(id);
		
		return sendErrorMessage();
	}

	@ResponseBody
	@RequestMapping(value = "/appealSubmit",method = RequestMethod.POST)
	public String appealSubmit(String id){
		bizService.appealSubmit(id);
		return sendSuccessMessage();
	}

	@ResponseBody
	@RequestMapping(value = "/confirmConf",method = RequestMethod.POST)
	public String confirmConf(String id){
		bizService.confirmConf(id);
		return sendSuccessMessage();
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/autocomplete_bizOppAuth")
	public String autocomplete_bizOppAuth(PageCondition condition,String clientId,String userId,HttpServletRequest request){
 		ActionUtil.requestToCondition(condition, request);
		List<BusinessOpportunity> bu = bizService.getBizOppSelectAuth(condition,clientId,userId);
		return sendSuccessMessage(bu);
	}

	@NoRight
	@RequestMapping("/getCustomersByName")
	@ResponseBody
	public String getCustomersByName(String customerName){

		if(StringUtil.isNotEmpty(customerName)){
			CustomInfo customInfo = bizService.findCustomInfoByName(customerName);
			String str =JSON.toJSONString(customInfo);
			return sendSuccessMessage(str);
		}
		return sendErrorMessage("客户名称为空");
	}


	/**
	 * 合同书评审历史
	 *
	 * @return
	 * @throws Exception
	 */
	@NoRight
	@RequestMapping("/listById")
	public String history(String keyId, Model model) {
		model.addAttribute("keyId",keyId);
		return forward("processHistory");
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/getHistoryByList")
	public String getHistoryByList(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		String keyId = condition.getStringCondition("keyId");
		UserObject user = getUserObject();
		List<String> ids = new ArrayList<>();

		if (StringUtil.isNotEmpty(keyId)) {
			ids.add(keyId);

			List<BizOppChange> change = bizService.getBizOppChange(keyId, null);
			if (change != null) {
				if (change.size() > 0) {
					for (BizOppChange c : change) {
						ids.add(c.getId());
					}
				}
			}
		}

		IPage p = historyService.findProcessInstanceByList(ids, condition.getRows(), condition.getPage());
		return sendSuccessMessage(p);
	}
	
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
