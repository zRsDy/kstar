package com.ibm.kstar.action.custom.zxbInt;

import javax.servlet.http.HttpServletRequest;
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
import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.custom.IQuotaNoLCZXBInfoService;
import com.ibm.kstar.entity.custom.NoLcQuotaZXBInfo;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/quotaNoLCZXB")
public class QuotaNOLCInfoZXBAction extends BaseAction {

	@Autowired
	IQuotaNoLCZXBInfoService service;

	@LogOperate(module = "客户管理模块：中信保限额申请(非LC)", notes = "${user}打开了中信保限额申请一览页面")
	@RequestMapping("/quotaNoLCInfo")
	public String index(String id, Model model) {
		return forward("quotaNoLCZXBList");
	}

	@NoRight
	@LogOperate(module = "客户管理模块：中信保限额申请(非LC)", notes = "${user}打开了中信保限额申请一览页面，进行限额申请检索")
	@ResponseBody
	@RequestMapping("/quotaNoLCZXBList")
	public String page(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		String searchKey = condition.getStringCondition("searchKey");
		if (searchKey != null) {
			condition.getFilterObject().addOrCondition("corpSerialNo", "like", "%" + searchKey + "%");
			condition.getFilterObject().addOrCondition("buyerChnName", "like", "%" + searchKey + "%");
			condition.getFilterObject().addOrCondition("buyerEngName", "like", "%" + searchKey + "%");
		}
		IPage p = service.query(condition);
		return sendSuccessMessage(p);
	}

	@LogOperate(module = "客户管理模块：中信保限额申请(非LC)", notes = "${user}打开了中信保限额申请添加页面")
	@RequestMapping("/add")
	public String customMainAdd(String id, Model model) {
		NoLcQuotaZXBInfo nlq = service.createInfor();
		model.addAttribute("mode", IConstants.ACTION_TYPE_ADD);
		model.addAttribute("id", id);
		model.addAttribute("noLcInfor", nlq);
		TabMain tabMain = new TabMain();
		tabMain.setInitAll(false);
		String url = new StringBuffer().append("/common/attachment/attachment.html?businessId=")
									   .append(nlq.getId())
									   .append("&businessType=ZXB_ATTACHS").toString();
		tabMain.addTab("申请附件信息", url);
		model.addAttribute("tabMain", tabMain);
		return forward("quotaNoLCZXBMain");
	}
	
	@LogOperate(module = "客户管理模块：中信保限额申请(非LC)", notes = "${user}点击新增保存功能")
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String customMainAdd(NoLcQuotaZXBInfo nlq, HttpServletRequest request) {
		nlq.setRecordInfor(false, super.getUserObject());
		service.saveInfo(nlq);
		return sendSuccessMessage("创建成功：" + nlq.getCorpSerialNo());
	}
	
	@LogOperate(module = "客户管理模块：中信保限额申请(非LC)", notes = "${user}打开了编辑页面")
	@RequestMapping("/edit")
	public String edit(String id, Model model) {
		if (null == id) {
			throw new AnneException("没有找到对应ID所需要修改的数据");
		}
		NoLcQuotaZXBInfo nlq = service.getInfoById(id);
		if (nlq == null) {
			throw new AnneException("没有找到需要修改的数据");
		}
		model.addAttribute("noLcInfor", nlq);
		TabMain tabMain = new TabMain();
		tabMain.setInitAll(false);
		String url = new StringBuffer().append("/common/attachment/attachment.html?businessId=")
									   .append(nlq.getId())
									   .append("&businessType=ZXB_ATTACHS").toString();
		tabMain.addTab("申请附件信息", url);
		model.addAttribute("tabMain", tabMain);
		return forward("quotaNoLCZXBMain");
	}

	@LogOperate(module = "客户管理模块：中信保限额申请(非LC)", notes = "${user}点击编辑保存功能")
	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(NoLcQuotaZXBInfo nql) {
		nql.setRecordInfor(true, super.getUserObject());
		service.updateInfo(nql);
		return sendSuccessMessage("更新成功：" + nql.getCorpSerialNo());
	}

	@LogOperate(module = "客户管理模块：中信保限额申请(非LC)", notes = "${user}点击限额申请删除功能")
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(String id) {
		service.deleteInfo(id);
		return sendSuccessMessage("删除成功" );
	}

	@LogOperate(module = "客户管理模块：中信保限额申请(非LC)", notes = "${user}点击限额申请复制功能")
	@ResponseBody
	@RequestMapping(value = "/copy", method = RequestMethod.POST)
	public String copy(String id) {
		NoLcQuotaZXBInfo noLCQuota = new NoLcQuotaZXBInfo();
		noLCQuota.setId(id);
		noLCQuota.setRecordInfor(false, super.getUserObject());
		String corpSerialNo = service.copyInfo(noLCQuota);
		return sendSuccessMessage("复制生成新单据流水号:" + corpSerialNo);
	}

	@LogOperate(module = "客户管理模块：中信保限额申请(非LC)", notes = "${user}点击限额申请提交申请功能")
	@ResponseBody
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public String submit(String id) {
		NoLcQuotaZXBInfo noLCQuota = new NoLcQuotaZXBInfo();
		noLCQuota.setId(id);
		noLCQuota.setRecordInfor(true, super.getUserObject());
		String corpSerialNo = service.doNoLcQuotaApplyV2(noLCQuota);
		return sendSuccessMessage("提交申请成功: "+ corpSerialNo);
	}

}
