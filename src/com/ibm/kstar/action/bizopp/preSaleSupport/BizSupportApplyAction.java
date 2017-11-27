package com.ibm.kstar.action.bizopp.preSaleSupport;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
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
import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.api.bizopp.IBizoppService;
import com.ibm.kstar.api.common.doc.IKstarAttachmentService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.bizopp.BusinessOpportunity;
import com.ibm.kstar.entity.bizopp.ProductDetail;
import com.ibm.kstar.entity.bizopp.SupportApply;
import com.ibm.kstar.entity.common.doc.KstarAttachment;
import com.ibm.kstar.interceptor.system.permission.NoRight;

/**
 * ClassName:BizSupportApplyAction <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: Jan 10, 2017 10:12:16 AM <br/>
 * 
 * @author Wutw
 * @version
 * @since JDK 1.7
 * @see
 */

@Controller
@RequestMapping("/supportApply")
public class BizSupportApplyAction extends BaseAction {
	
	@Autowired
	IBizoppService bizoppService;
	
	@Autowired
	private IKstarAttachmentService attachmentService;
	
	@Autowired
	BaseDao baseDao;
	
	@RequestMapping("/index")
	public String index(String id, Model model) {
		return forward("supportApplyIndex");
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) throws Exception {
		ActionUtil.requestToCondition(condition, request);
		String bizOppId = request.getParameter("bizOppId");
		condition.getFilterObject().addCondition("bizOppId", "eq", bizOppId);
		ActionUtil.doSearch(condition);
		
		IPage p = bizoppService.querySupportApply(condition);

		return sendSuccessMessage(p);
	}

	@NoRight
	@RequestMapping("/add")
	public String add(String bid, Model model){
		UserObject user = getUserObject();

		String userId = user.getEmployee().getId();
		String userName = user.getEmployee().getName();
//		String orgId = user.getOrg().getId();
		String orgName = user.getOrg().getName();
		String department = user.getOrg().getGroupId();
		
		SupportApply entity = new SupportApply();
		BusinessOpportunity businessOpportunity = null;
		
		//如果在商机页面新增售前支持，则自动带入客户名称，商机名称
		if (StringUtil.isNotEmpty(bid)) {
			businessOpportunity = bizoppService.getBizOppEntity(bid);
			entity.setBizOppId(bid);
			entity.setBizOppName(businessOpportunity.getOpportunityName());
			entity.setClientId(businessOpportunity.getClientId());
			entity.setClientName(businessOpportunity.getClientName());
			entity.setCompetitor(businessOpportunity.getCompetitiveBrands());
			entity.setBizOppProgress(businessOpportunity.getSaleStage());
			entity.setBizOppProgressName(businessOpportunity.getSaleStageName());
		}
		
		//设置申请人
		entity.setApplicantId(userId);
		entity.setApplicant(userName);
		//生成商机支持编号
		String bizOppSupportNumber = bizoppService.getBizOppSupportNumber();
		entity.setNumber(bizOppSupportNumber);
		//初始化商机状态
		entity.setStatus(ProcessConstants.PROCESS_STATUS_Pending);
		//申请单位
		entity.setApplyEnterprise(orgName);
		//申请部门
		entity.setDepartment(department);
		//初始化创建日期
//		model.addAttribute("bizOppId", bizOppId);
		model.addAttribute("entity", entity);
		entity.fillInit(getUserObject());

		TabMain tabMainInfo = new TabMain();
		model.addAttribute("P_GNGFORG_B1_0001", this.P_GNGFORG_B1_0001(this.getUserObject().getOrg().getId()));
		model.addAttribute("P_GNORG_B1_0001", this.P_GNORG_B1_0001(this.getUserObject().getOrg().getId()));
		model.addAttribute("tabMainInfo",tabMainInfo);

		return forward("supportApplyAdd");
	}
	
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(SupportApply entity, HttpServletRequest request) {
		entity.fillInit(getUserObject());
		entity.setStatus(ProcessConstants.PROCESS_STATUS_Pending);
		bizoppService.saveSupportApply(entity,getUserObject());


		return sendSuccessMessage(entity.getId());
	}
	
	@NoRight
	@RequestMapping("/edit")
	public String edit(String id, Model model){
		if(id == null){
			throw new AnneException("没有找到需要修改的数据");
		}
		SupportApply entity = bizoppService.getSupportApplyEntity(id);
		if(entity == null){
			throw new AnneException("没有找到需要修改的数据");
		}
		model.addAttribute("P_GNGFORG_B1_0001", this.P_GNGFORG_B1_0001(this.getUserObject().getOrg().getId()));
		model.addAttribute("P_GNORG_B1_0001", this.P_GNORG_B1_0001(this.getUserObject().getOrg().getId()));
		model.addAttribute("entity", entity);
		TabMain tabMainInfo = new TabMain();
		tabMainInfo.setInitAll(false);
		if(hasPermission("P03PresSupportT3OppoConfigPage")){
			tabMainInfo.addTab("商机配置", "/supportApply/bizConfig.html?bizOppId="+entity.getBizOppId());
		}
		if(hasPermission("P03PresSupportT2FeebackPage")){
			tabMainInfo.addTab("支持反馈", "/sfb/sfbList.html?id="+id);
		}
		if(hasPermission("P03PresSupportT5FilePage")){
			if("Completed".equals(entity.getStatus()) || ("Processing".equals(entity.getStatus()))){
				tabMainInfo.addTab("附件", "/common/attachment/attachment.html?businessType=SupportApply&docGroupCode=SupportApplyDOCTYPE&businessId=" + entity.getId()+ "&unableAdd=true&unableDelete=true");
			}else{
				tabMainInfo.addTab("附件", "/common/attachment/attachment.html?businessType=SupportApply&docGroupCode=SupportApplyDOCTYPE&businessId=" + entity.getId()+ "&unableAdd=false&unableDelete=false");
			}
		}
		if(hasPermission("P03PresSupportT1TeamMemberPage")){
			tabMainInfo.addTab("团队成员", "/team/list.html?businessType=SupportApply&businessId="+id);
		}
		if(hasPermission("P03PresSupportT2ProReviewHistoryPage")){
			tabMainInfo.addTab("审批历史", "/standard/history.html?contrId="+id);
		}
		model.addAttribute("tabMainInfo",tabMainInfo);
		return forward("supportApplyAdd");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/edit", method=RequestMethod.POST)
	public String edit(SupportApply bizopp){
		bizoppService.updateSupportApply(bizopp);
		return sendSuccessMessage(bizopp.getId());
	}
	
	@ResponseBody
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public String delete(String id){
		bizoppService.deleteSupportApply(id);
		return sendSuccessMessage();
	}

	@NoRight
	@ResponseBody
	@RequestMapping(value="/startProcess",method = RequestMethod.POST)
	public String startProcess(PageCondition condition,String id,String bizOppName,String is_10, HttpServletRequest request) throws Exception{
		
		ActionUtil.requestToCondition(condition, request); 
		if(this.P_GNGFORG_B1_0001(this.getUserObject().getOrg().getId())){
			if(is_10.equals("1")){
				condition.getFilterObject().addCondition("bizId", "=", id);
				List<KstarAttachment> list = attachmentService.getAttachmentList(condition);
				if(list.size()==0){
					throw new AnneException("请先上传附件后再进行提交");
				}
			}
			List<ProductDetail> list = bizoppService.getProducrDetailByBizId(id);
			if(list.size() == 0){
				throw new AnneException("商机配置不能为空！");
			}
		}
		bizoppService.startProcess(getUserObject(),id,bizOppName,getUserObject(),is_10);
		return sendSuccessMessage();
	}


	@RequestMapping("/bizConfig")
	public String bizConfig(String bizOppId,Model model){
		model.addAttribute("bizOppId",bizOppId);
		model.addAttribute("P_GNORG_B1_0001", this.P_GNORG_B1_0001(this.getUserObject().getOrg().getId()));
		return forward("bizConfig");
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
	
	public boolean P_GNORG_B1_0001(String orgId){
		String sql = "select m.row_id,m.lov_name from sys_t_lov_member m where m.row_id = 'P_GNORG_B1_0001'"
				+ " START WITH m.row_id = ? CONNECT BY PRIOR m.parent_id = m.row_id";
		List<Object[]> lst = baseDao.findBySql(sql, orgId);
		if(lst != null && lst.size() > 0){
			return true;
		}
		return false;
	}
	
	

}
