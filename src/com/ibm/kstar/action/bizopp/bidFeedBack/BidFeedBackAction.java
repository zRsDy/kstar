package com.ibm.kstar.action.bizopp.bidFeedBack;

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
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.api.bizopp.IBizoppService;
import com.ibm.kstar.entity.bizopp.BiddingFeedBack;
import com.ibm.kstar.entity.bizopp.BusinessOpportunity;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/bidFeedBack")
public class BidFeedBackAction extends BaseAction{
	@Autowired
	BaseDao baseDao;
	@Autowired
	IBizoppService bizService;
	
	@NoRight
	@RequestMapping("/add")
	public String add(String bizOppId,String id, Model model) {
		//正常申请启动流程 即bizOppId不为空，id为空
		BusinessOpportunity entity = null;
		BiddingFeedBack feedBack = null;
		
		if(StringUtil.isEmpty(bizOppId)&&StringUtils.isNotEmpty(id)){
			feedBack = bizService.getBidFeedBackById(id);
			entity = bizService.getBizOppEntity(feedBack.getBidId());
		}else {
			entity = bizService.getBizOppEntity(bizOppId);
			feedBack = bizService.getBidFeedBack(bizOppId);
		}
		
		if(feedBack == null){
			feedBack = new BiddingFeedBack();
		}
		
		model.addAttribute("feedBack",feedBack);
		model.addAttribute("entity", entity);
		model.addAttribute("userType",getUserObject().getEmployee().getFlag());
		
		TabMain tabMainInfo = new TabMain();
		TabMain tabMainInfo1 = new TabMain();
		TabMain tabMainInfo2 = new TabMain();
		tabMainInfo.setInitAll(false);
		tabMainInfo1.setInitAll(false);
		tabMainInfo2.setInitAll(false);
		if (hasPermission("P03BizOppoT1ConfigPage")) {
			tabMainInfo2.addTab("商机配置", "/bidFeedBack/mainInfo.html?id=" + entity.getId() + "&status=" + entity.getStatus());
		}
		//数据中心1展示“集成商”标签页，光伏2展示“决策链”、“竞争分析”标签。
		if (hasPermission("P03BizOppoT2IntegratorPage")) {
			tabMainInfo1.addTab("授权单位", "/bidFeedBack/integrator.html?id=" + entity.getId());
		}
		
		model.addAttribute("tabMainInfo",tabMainInfo);
		model.addAttribute("tabMainInfo1",tabMainInfo1);
		model.addAttribute("tabMainInfo2",tabMainInfo2);
		
		model.addAttribute("P_GJORG_B1_0001", this.isP_GJORG_B1_0001(this.getUserObject().getOrg().getId()));
		model.addAttribute("P_GNORG_B1_0001", this.P_GNORG_B1_0001(this.getUserObject().getOrg().getId()));
		model.addAttribute("P_GNGFORG_B1_0001", this.P_GNGFORG_B1_0001(this.getUserObject().getOrg().getId()));
		model.addAttribute("P_GNQCORG_B1_0001", this.P_GNQCORG_B1_0001(this.getUserObject().getOrg().getId()));
		return forward("bidFeedBack");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(BiddingFeedBack entity, HttpServletRequest request) {
		bizService.saveBiddingFeedBack(entity,getUserObject());
		return sendSuccessMessage();
	}
	
	/**
	 * 校验提交反馈按钮是否允许被使用 (无效，已禁用)
	 * @param bidId
	 * @param request
	 * @return
	 */
	@NoRight
	@ResponseBody
	@RequestMapping(value = "/checkFeedBack", method = RequestMethod.POST)
	public String checkFeedBack(String bidId, HttpServletRequest request) {
		boolean isStart = true;
		return sendSuccessMessage(isStart);
	}
	
	/**
	 * 启动提交反馈审批流程
	 * @param id
	 * @param request
	 * @return
	 */
	@NoRight
	@ResponseBody
	@RequestMapping(value = "/startFeedBackProcess", method = RequestMethod.POST)
	public String startFeedBackProcess(String id,HttpServletRequest request){
		bizService.startFeedBackProcess(id,getUserObject());
		return sendSuccessMessage();
	}
	
	
	@NoRight
	@RequestMapping("/mainInfo")
	public String bizoppMainInfo(String id, Model model) {
		model.addAttribute("id", id);
		model.addAttribute("P_GNORG_B1_0001", this.P_GNORG_B1_0001(this.getUserObject().getOrg().getId()));
		return forward("mainInfo");
	}
	
	@NoRight
	@RequestMapping("/integrator")
	public String bizoppIntegrator(String id, Model model) {
		model.addAttribute("id", id);
		return forward("integrator");
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
