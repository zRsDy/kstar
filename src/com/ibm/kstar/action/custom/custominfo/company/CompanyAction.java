package com.ibm.kstar.action.custom.custominfo.company;

import java.util.ArrayList;
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
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.log.LogOperate;
import org.xsnake.web.page.IPage;
import org.xsnake.web.page.PageImpl;
import org.xsnake.web.utils.ActionUtil;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.custom.CustomRelation;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/custom/baseinfo/company")
public class CompanyAction extends BaseAction {

	@Autowired
	ICustomInfoService service;
	
	// 公司关系
	@NoRight
	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}公司关系一览")
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) {

		ActionUtil.requestToCondition(condition, request);
		String customCode = condition.getStringCondition("id");
		condition.getFilterObject().addOrCondition("customCode", "eq", customCode);
		condition.getFilterObject().addOrCondition("customCompCode", "eq", customCode);
		
		IPage p = service.queryCompany(condition);
		List<CustomRelation> customRelations = (List<CustomRelation>) p.getList();
		List<CustomRelation> rtnList = new ArrayList<CustomRelation>();
		Date sysdate = new Date();
		
		for (CustomRelation customRelation : customRelations) {
			Date to = customRelation.getCustomRelateValidTo();
			if (to != null && sysdate.after(to)) {
				continue;
			}
			
			String tempCustomCode = customRelation.getCustomCode();
			String tempCustomCompCode = customRelation.getCustomCompCode();
			String tempLel = customRelation.getRelationLvl();

			if (StringUtils.equals(customCode, tempCustomCode)) {
				customRelation.setCustomCodeDisp(tempCustomCompCode);

				CustomInfo customInfo = service.getCustomInfoByCode(tempCustomCompCode);
				
				customRelation.setCustomNameDisp(customInfo.getCustomFullName());

				if (StringUtils.equals(IConstants.LEV_DOWN, tempLel)) {
					customRelation.setCustomRelationLvlDisp(IConstants.LEV_DOWN_DISP);
				} else if (StringUtils.equals(IConstants.LEV_UP, tempLel)) {
					customRelation.setCustomRelationLvlDisp(IConstants.LEV_UP_DISP);
				} else if (StringUtils.equals(IConstants.LEV_COMPITER, tempLel)) {
					customRelation.setCustomRelationLvlDisp(IConstants.LEV_COMPITER_DISP);
				} else if (StringUtils.equals(IConstants.LEV_PARTNER, tempLel)) {
					customRelation.setCustomRelationLvlDisp(IConstants.LEV_PARTNER_DISP);
				}
			} else if (StringUtils.equals(customCode, tempCustomCompCode)) {
				customRelation.setCustomCodeDisp(tempCustomCode);

				CustomInfo customInfo = service.getCustomInfoByCode(tempCustomCode);
				
				customRelation.setCustomNameDisp(customInfo.getCustomFullName());

				if (StringUtils.equals(IConstants.LEV_DOWN, tempLel)) {
					customRelation.setCustomRelationLvlDisp(IConstants.LEV_UP_DISP);
				} else if (StringUtils.equals(IConstants.LEV_UP, tempLel)) {
					customRelation.setCustomRelationLvlDisp(IConstants.LEV_DOWN_DISP);
				} else if (StringUtils.equals(IConstants.LEV_COMPITER, tempLel)) {
					customRelation.setCustomRelationLvlDisp(IConstants.LEV_COMPITER_DISP);
				} else if (StringUtils.equals(IConstants.LEV_PARTNER, tempLel)) {
					customRelation.setCustomRelationLvlDisp(IConstants.LEV_PARTNER_DISP);
				}
				
			}
			rtnList.add(customRelation);
		}
		((PageImpl)p).setList(rtnList);
		return sendSuccessMessage(p);
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}点击公司关系添加")
	@RequestMapping("/add")
	public String add(String id, String customCode, Model model) {

		CustomInfo customInfo = service.getCustomInfo(id);
		CustomRelation customRelation = new CustomRelation();
		customRelation.setCustomCode(customInfo.getCustomCode());
		model.addAttribute("customRelation", customRelation);
		return forward("add");
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}实现公司关系添加")
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(CustomRelation customRelation, String customCode,
			HttpServletRequest request) {

		Date from = customRelation.getCustomRelateValidFrom();
		Date to = customRelation.getCustomRelateValidTo();
		Date sysdate = new Date();
		
		if (from == null && to == null) {
			
		} else if (from != null && to == null) {
			
		} else if (from == null && to != null) {
			if (sysdate.after(to)) {
				throw new AnneException("请以未来日期作为有效期终了日！");
			}
		} else if (from != null && to != null) {
			if (sysdate.after(to)) {
				throw new AnneException("请以未来日期作为有效期终了日！");
			} else if (from.after(to)) {
				throw new AnneException("有效期终了日必须大于有效期开始日！");
			}
		}
		
		// 功能字段设值
		// 创建字段
		customRelation.setCreatedById(getUserObject().getEmployee().getId());
		customRelation.setCreatedAt(new Date());
		customRelation.setCreatedPosId(getUserObject().getPosition().getId());
		customRelation.setCreatedOrgId(getUserObject().getOrg().getId());
		// 更新字段
		customRelation.setUpdatedById(getUserObject().getEmployee().getId());
		customRelation.setUpdatedAt(new Date());
		service.saveCompanyInfo(customRelation);

		return sendSuccessMessage(customRelation.getCustomCode());
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}点击公司关系编辑")
	@RequestMapping("/edit")
	public String edit(String id, String customCode, Model model) {
		if (id == null) {
			throw new AnneException("没有找到需要修改的数据");
		}
		CustomRelation customRelation = service.getCompanyInfo(id);
		if (customRelation == null) {
			throw new AnneException("没有找到需要修改的数据");
		}

		String tempCustomCode = customRelation.getCustomCode();
		String tempCustomCompCode = customRelation.getCustomCompCode();
		String tempLel = customRelation.getCustomRelationLvl();

		if (StringUtils.equals(customCode, tempCustomCode)) {
			customRelation.setCustomCodeDisp(tempCustomCompCode);

			CustomInfo customInfo = service.getCustomInfoByCode(tempCustomCompCode);
			
			customRelation.setCustomNameDisp(customInfo.getCustomFullName());

			if (StringUtils.equals(IConstants.LEV_DOWN, tempLel)) {
				customRelation.setCustomRelationLvlDisp(IConstants.LEV_DOWN);
			} else if (StringUtils.equals(IConstants.LEV_UP, tempLel)) {
				customRelation.setCustomRelationLvlDisp(IConstants.LEV_UP);
			} else if (StringUtils.equals(IConstants.LEV_COMPITER, tempLel)) {
				customRelation.setCustomRelationLvlDisp(IConstants.LEV_COMPITER);
			} else if (StringUtils.equals(IConstants.LEV_PARTNER, tempLel)) {
				customRelation.setCustomRelationLvlDisp(IConstants.LEV_PARTNER);
			}
		} else if (StringUtils.equals(customCode, tempCustomCompCode)) {
			customRelation.setCustomCodeDisp(tempCustomCode);
			CustomInfo customInfo = service.getCustomInfoByCode(tempCustomCompCode);
			customRelation.setCustomNameDisp(customInfo.getCustomFullName());

			if (StringUtils.equals(IConstants.LEV_DOWN, tempLel)) {
				customRelation.setCustomRelationLvlDisp(IConstants.LEV_UP);
			} else if (StringUtils.equals(IConstants.LEV_UP, tempLel)) {
				customRelation.setCustomRelationLvlDisp(IConstants.LEV_DOWN);
			} else if (StringUtils.equals(IConstants.LEV_COMPITER, tempLel)) {
				customRelation.setCustomRelationLvlDisp(IConstants.LEV_COMPITER);
			} else if (StringUtils.equals(IConstants.LEV_PARTNER, tempLel)) {
				customRelation.setCustomRelationLvlDisp(IConstants.LEV_PARTNER);
			}
		}
		model.addAttribute("mode", IConstants.ACTION_TYPE_EDIT);
		model.addAttribute("customRelation", customRelation);
		return forward("add");
	}

	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}实现公司关系添加")
	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(CustomRelation customRelation) {
		Date from = customRelation.getCustomRelateValidFrom();
		Date to = customRelation.getCustomRelateValidTo();
		Date sysdate = new Date();
		
		if (from == null && to == null) {
			
		} else if (from != null && to == null) {
			
		} else if (from == null && to != null) {
			if (sysdate.after(to)) {
				throw new AnneException("请以未来日期作为有效期终了日！");
			}
		} else if (from != null && to != null) {
			if (sysdate.after(to)) {
				throw new AnneException("请以未来日期作为有效期终了日！");
			} else if (from.after(to)) {
				throw new AnneException("有效期终了日必须大于有效期开始日！");
			}
		}
		
		// 功能字段设值
		// 更新字段
		customRelation.setUpdatedById(getUserObject().getEmployee().getId());
		customRelation.setUpdatedAt(new Date());
		service.updateCompanyInfo(customRelation);
		return sendSuccessMessage();
	}
	
	
	@LogOperate(module = "客户管理模块：建档报备", notes = "${user}实现公司关系删除")
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(String id) {
		service.deleteCompanyInfo(id);
		return sendSuccessMessage();
	}

}
