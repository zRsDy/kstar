package com.ibm.kstar.action.custom.custominfo.customupdate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.xsnake.web.log.LogOperate;
import org.xsnake.web.page.PageImpl;
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.xflow.api.IProcessService;
import org.xsnake.xflow.api.workflow.IXflowProcessServiceWrapper;
import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.entity.custom.CustomAddressInfo;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.custom.CustomInfoChange;
import com.ibm.kstar.entity.order.OrderHeaderChange;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping(value = "/custom/change")
public class ChangeAction extends BaseAction {

	@Autowired
	ICustomInfoService service;
	
	@Autowired
	protected ILovMemberService lovMemberService;
	
	@Autowired
	IProcessService processService;
	
	@Autowired
	IXflowProcessServiceWrapper xflowProcessServiceWrapper;
	
	@Autowired
	BaseDao baseDao;

	@NoRight
	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}打开了客户基本信息变更页面")
	@RequestMapping(value = "/change", method = RequestMethod.GET)
	public String change(String id, String flg, Model model) {
		
		if (StringUtils.equals(IConstants.CUSTOM_CHANGE_PROC, flg)) {
			List<CustomInfoChange> entitys = service.getCustomInfoChangeByCustomId(id);
			if(entitys.size() > 0){
				model.addAttribute("entity", entitys.get(0));
				this.initModel(entitys.get(0).getId(), entitys.get(0).getStatus(), model);
			}
			
			model.addAttribute("P_GJORG_B1_0001", this.isP_GJORG_B1_0001(this.getUserObject().getOrg().getId()));
			model.addAttribute("P_GNORG_B1_0001", this.P_GNORG_B1_0001(this.getUserObject().getOrg().getId()));
			model.addAttribute("P_GNGFORG_B1_0001", this.P_GNGFORG_B1_0001(this.getUserObject().getOrg().getId()));
			model.addAttribute("P_GNQCORG_B1_0001", this.P_GNQCORG_B1_0001(this.getUserObject().getOrg().getId()));
			return forward("change");	
		}
		
		CustomInfo customInfo = service.getCustomInfo(id);
		CustomInfoChange customInfoChange = new CustomInfoChange();
		String addressId = "";  
		List<CustomAddressInfo> addreses = service.getAddrInfoBycode(id);
		
		if(addreses.size() > 0){
			for (CustomAddressInfo customAddressInfo : addreses) {
				LovMember lov =  ((LovMember)CacheData.getInstance().get(customAddressInfo.getCustomAddressType()));
				if (StringUtils.equals("1", lov.getCode())) {
					addressId = customAddressInfo.getId();
					break;
				}
			}
		}
		
		BeanUtils.copyPropertiesIgnoreNull(customInfo, customInfoChange);
		
		customInfoChange.setId(null);
		customInfoChange.setCustomId(id);
		customInfoChange.setAddressId(addressId);
		customInfoChange.setStatus(IConstants.CUSTOM_NORMAL_STATUS_10);
		customInfoChange.setApplierName(getUserObject().getEmployee().getNo().concat("/").concat(getUserObject().getEmployee().getName()));
		customInfoChange.setApplier(getUserObject().getEmployee().getId());
		customInfoChange.setApplierOrg(getUserObject().getOrg().getId());
		customInfoChange.setApplierPos(getUserObject().getPosition().getId());
		SimpleDateFormat sdf = new SimpleDateFormat(IConstants.YMDHMS_FORMAT_1);
		sdf = new SimpleDateFormat(IConstants.YMD_FORMAT_2);
		customInfoChange.setCreateDate(sdf.format(new Date()));
		tempInitnial(customInfo, customInfoChange);
		
		model.addAttribute("P_GJORG_B1_0001", this.isP_GJORG_B1_0001(this.getUserObject().getOrg().getId()));
		model.addAttribute("P_GNORG_B1_0001", this.P_GNORG_B1_0001(this.getUserObject().getOrg().getId()));
		model.addAttribute("P_GNGFORG_B1_0001", this.P_GNGFORG_B1_0001(this.getUserObject().getOrg().getId()));
		model.addAttribute("P_GNQCORG_B1_0001", this.P_GNQCORG_B1_0001(this.getUserObject().getOrg().getId()));
		
		model.addAttribute("entity", customInfoChange);
		
		this.initModel(customInfo.getId(), customInfo.getErpStatus(), model);
		
		return forward("change");
	}
	

	 private void initModel(String id,String status,Model model){
		 
    	 TabMain tabMainInfo1 = new TabMain();
    	 
         tabMainInfo1.setInitAll(false);
         
         tabMainInfo1.addTab("附件", 
        		 "/common/attachment/attachment.html?businessType=ACCOUNT_FILE&docGroupCode=CONTRACTDOCTYPE&businessId="+id+"&unableAdd=false&unableDelete=false");
         
         model.addAttribute("tabMainInfo1",tabMainInfo1);
	 }
	
	/**
	 * 待变更信息初期化
	 * @param from
	 * @param to
	 */
	private void tempInitnial(CustomInfo from, CustomInfoChange to) {
		/** 客户全称 */
		to.setTempFullName(from.getCustomFullName());

		/** 客户别名 */
		to.setTempAliasName(from.getCustomAliasName());

		/** 公司网址 */
		to.setTempWebAddress(from.getCustomWebAddress());

		/** OEM 品牌 */
		to.setTempOem(from.getCustomOem());

		/** 所属区域 */
		to.setTempArea(from.getCustomArea());
		to.setTempAreaSub1(from.getCustomAreaSub1());
		to.setTempAreaSub2(from.getCustomAreaSub2());
		to.setTempAreaSub3(from.getCustomAreaSub3());

		/** 收入规模 */
		to.setTempIncomeScale(from.getCustomIncomeScale());

		/** 客户行业 */
		to.setTempCategory(from.getCustomCategory());
		to.setTempCategorySub(from.getCustomCategorySub());

		/** 客户概况 */
		to.setTempProfile(from.getCustomProfile());

		/** 法定代表人 */
		to.setTempRepresentative(from.getCorpRepresentative());

		/** 纳税登记号 */
		to.setTempTrn(from.getCorpTrn());

		/** 开票名称 */
		to.setTempInvoiceName(from.getCorpInvoiceName());

		/** 开票注意事项 */
		to.setTempInvoiceComment(from.getCorpInvoiceComment());
		
		/** 一般纳税人资格 */
		to.setTempOrdinaryFlg(from.getCorpOrdinaryFlg());
		/** 开票地址 */
		to.setTempAccountAddress(from.getAccountAddress());
		/** 开户行 */
		to.setTempAccountBank(from.getAccountBank());
		/** 开票电话 */
		to.setTempAccountTel(from.getAccountTel());
		/** 开户账号 */
		to.setTempAccountNo(from.getAccountNo());

		/** 注册地址 */
		to.setTempArea(from.getCustomArea());
		to.setTempAreaSub1(from.getCustomAreaSub1());
		to.setTempAreaSub2(from.getCustomAreaSub2());
		to.setTempAreaSub3(from.getCustomAreaSub3());
//		to.setTempLayer5(address.getLayer5());
//		to.setCorpRegAddress(address.getCustomAddress());
		to.setTempRegAddress(from.getCorpRegAddress());
	}

	
	private void tempInitnial(CustomInfoChange from, CustomInfoChange to) {
		/** 客户全称 */
		to.setTempFullName(from.getTempFullName());

		/** 客户别名 */
		to.setTempAliasName(from.getTempAliasName());

		/** 公司网址 */
		to.setTempWebAddress(from.getTempWebAddress());

		/** OEM 品牌 */
		to.setTempOem(from.getTempOem());

		/** 所属区域 */
//		to.setTempArea(from.getCustomArea());
//		to.setTempAreaSub1(from.getCustomAreaSub1());
//		to.setTempAreaSub2(from.getCustomAreaSub2());
//		to.setTempAreaSub3(from.getCustomAreaSub3());

		/** 收入规模 */
		to.setTempIncomeScale(from.getCustomIncomeScale());

		/** 客户行业 */
		to.setTempCategory(from.getCustomCategory());
		to.setTempCategorySub(from.getCustomCategorySub());

		/** 客户概况 */
		to.setTempProfile(from.getCustomProfile());

		/** 法定代表人 */
		to.setTempRepresentative(from.getCorpRepresentative());

		/** 纳税登记号 */
		to.setTempTrn(from.getCorpTrn());

		/** 开票名称 */
		to.setTempInvoiceName(from.getCorpInvoiceName());

		/** 开票注意事项 */
		to.setTempInvoiceComment(from.getCorpInvoiceComment());
		
		/** 一般纳税人资格 */
		to.setTempOrdinaryFlg(from.getCorpOrdinaryFlg());
		/** 开票地址 */
		to.setTempAccountAddress(from.getAccountAddress());
		/** 开户行 */
		to.setTempAccountBank(from.getAccountBank());
		/** 开票电话 */
		to.setTempAccountTel(from.getAccountTel());
		/** 开户账号 */
		to.setTempAccountNo(from.getAccountNo());

		/** 注册地址 */
		to.setTempArea(from.getCustomArea());
		to.setTempAreaSub1(from.getCustomAreaSub1());
		to.setTempAreaSub2(from.getCustomAreaSub2());
		to.setTempAreaSub3(from.getCustomAreaSub3());
//		to.setTempLayer5(address.getLayer5());
//		to.setCorpRegAddress(address.getCustomAddress());
		to.setTempRegAddress(from.getCorpRegAddress());
	}
	
	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}客户基本信息变更申请")
	@ResponseBody
	@RequestMapping(value = "/apply", method = RequestMethod.POST)
	public String apply(CustomInfoChange entity) {
		String area = entity.getTempArea();
		if(StringUtils.isEmpty(area)){
			throw new AnneException("国家、或省市区县不能为空!");
		}
		
		LovMember lov = lovMemberService.get(area);
		if (lov != null && StringUtils.equals(lov.getCode(), IConstants.ADDRESSREGION_CN)) {
			if (StringUtils.isEmpty(entity.getTempAreaSub1()) 
					|| StringUtils.isEmpty(entity.getTempAreaSub1())
					|| StringUtils.isEmpty(entity.getTempAreaSub1())) {
				throw new AnneException("当国家为[中国]时，请输入省市县区!");
			}
		}
		String id = entity.getCustomId();
		CustomInfo customInfo = service.getCustomInfo(id);
		
		String model = lovMemberService.getFlowCodeByAppCode(IConstants.CUSTOM_CHANGE_PROC);
		
		Map<String,String> varmap = new HashMap<>();
		varmap.put("title", "客户基础信息变更 - 客户编号["+customInfo.getCustomCode()+"]");
		varmap.put("app", IConstants.CUSTOM_CHANGE_PROC);
		
		varmap.put("SalesCenter", lovMemberService.getSaleCenter(getUserObject().getOrg().getId()));
		
		service.startChangeProcess(model
				, id
				, getUserObject() 
				,varmap);
		
		entity.setStatus(IConstants.CUSTOM_NORMAL_STATUS_20);
		
		service.saveCustomInfoChange(entity, getUserObject());
		
		return sendSuccessMessage("客户变更启动成功");
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
	
	@NoRight
	@RequestMapping(value = "/customChangeHis")
	public String pageGet(String customInfoId, Model model) {
		model.addAttribute("customInfoId",customInfoId);
		return forward("customChangeHisPage");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value = "/customChangeHisList")
	public String pagePost(String customInfoId, HttpServletRequest request) {
		List<CustomInfoChange> list = service.getCustomInfoChangeByCustomId(customInfoId);
		return sendSuccessMessage(new PageImpl(list, 1, 1, 20));
	}
	
	@NoRight
	@RequestMapping(value = "/changeHis")
	public String changeHisGet(String id, Model model) {
		CustomInfoChange customInfoChange = service.getCustomInfoChange(id);
		
		model.addAttribute("P_GJORG_B1_0001", this.isP_GJORG_B1_0001(this.getUserObject().getOrg().getId()));
		model.addAttribute("P_GNORG_B1_0001", this.P_GNORG_B1_0001(this.getUserObject().getOrg().getId()));
		model.addAttribute("P_GNGFORG_B1_0001", this.P_GNGFORG_B1_0001(this.getUserObject().getOrg().getId()));
		model.addAttribute("P_GNQCORG_B1_0001", this.P_GNQCORG_B1_0001(this.getUserObject().getOrg().getId()));
		
		model.addAttribute("entity", customInfoChange);
		
		this.initModel(customInfoChange.getId(), customInfoChange.getStatus(), model);
		
		return forward("change");
	}
	
}
