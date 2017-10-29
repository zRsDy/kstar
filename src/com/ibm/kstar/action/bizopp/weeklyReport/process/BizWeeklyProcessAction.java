package com.ibm.kstar.action.bizopp.weeklyReport.process;

import java.util.Iterator;

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
import org.xsnake.web.page.IPage;
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.ActionUtil;

import com.ibm.kstar.api.bizopp.IBizBaseService;
import com.ibm.kstar.api.bizopp.IBizoppService;
import com.ibm.kstar.entity.bizopp.InternationWeekly;
import com.ibm.kstar.entity.bizopp.InternationWeeklyReport;
import com.ibm.kstar.interceptor.system.permission.NoRight;


/**
 * ClassName:BizweeklyAction <br/>
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
@RequestMapping("/process")
public class BizWeeklyProcessAction extends BaseAction {

	@Autowired
	IBizBaseService bizService;
	
	@Autowired
	IBizoppService bizService1;

	@RequestMapping("/index")
	public String index(String id, Model model) {
		return forward("weeklyIndex");
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) throws Exception {
		ActionUtil.requestToCondition(condition, request);
		String fkWeeklyId = request.getParameter("fkWeeklyId");
		fkWeeklyId = fkWeeklyId == null ? "" : fkWeeklyId;
		condition.getFilterObject().addCondition("fkWeeklyId", "eq", fkWeeklyId);
		
		IPage p = bizService1.queryWeeklyReport(condition);

		dealWithWeeklyReport(p);
		return sendSuccessMessage(p);
	}


	@RequestMapping("/add")
	public String add(String fkWeeklyId, Model model){
		model.addAttribute("fkWeeklyId", fkWeeklyId);
		TabMain tabMainInfo = new TabMain();
		model.addAttribute("tabMainInfo",tabMainInfo);
		return forward("weeklyProcessAdd");
	}
	
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(InternationWeeklyReport entity, HttpServletRequest request) {

		bizService1.savetWeeklyReport(entity);
		return sendSuccessMessage(entity.getId());
	}
	
	@RequestMapping("/edit")
	public String edit(String id, Model model){
		if(id==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		InternationWeeklyReport entity = bizService1.getWeeklyReport(id);
//		InternationWeekly entity = bizService.getEntity(id, new InternationWeekly());
		if(entity==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		model.addAttribute("entity",entity);
		return forward("weeklyProcessAdd");
	}
	
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(InternationWeeklyReport internationWeeklyReport){
		bizService1.updateWeeklyReport(internationWeeklyReport);
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		bizService1.deleteWeeklyReport(id);
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value="/line", method=RequestMethod.POST)
	public String dealCurrentLine(InternationWeeklyReport internationWeeklyReport) {
		bizService1.updateWeeklyReportLine(internationWeeklyReport);
		return sendSuccessMessage();
	}
	
	@SuppressWarnings("rawtypes")
	private void dealWithWeeklyReport(IPage p) {
		StringBuilder clientType = new StringBuilder();
		StringBuilder productNeed = new StringBuilder();
		StringBuilder contactWay = new StringBuilder();
		Iterator it = p.getList().listIterator();
		while (it.hasNext()) {
			InternationWeeklyReport entity = (InternationWeeklyReport) it.next();
			//处理客户类型
			if (entity.getIsNewClient() != null && entity.getIsNewClient() == 1) {
				clientType.append("新客户");
				clientType.append(";");
			}
			if (entity.getIsOldClient() != null && entity.getIsOldClient() == 1) {
				clientType.append("老客户");
			}
			
			//处理产品需求
			//后备机
			if (entity.getIsBackupMachine() != null && entity.getIsBackupMachine() == 1) {
				productNeed.append("后备机");
				productNeed.append(";");
			}
			//家用逆变器
			if (entity.getIsHomeInverter() != null && entity.getIsHomeInverter() == 1) {
				productNeed.append("家用逆变器");
				productNeed.append(";");
			}
			//工频机
			if (entity.getIsPowerFrequencyMachine() != null && entity.getIsPowerFrequencyMachine() == 1) {
				productNeed.append("工频机");
				productNeed.append(";");
			}
			//电池
			if (entity.getIsBattery() != null && 1 == entity.getIsBattery()) {
				productNeed.append("电池");
				productNeed.append(";");
			}
			//>20K高频
			if (entity.getIsHighFrequency() != null && entity.getIsHighFrequency() == 1) {
				productNeed.append(">20K高频");
				productNeed.append(";");
			}
			//IDU/IDM
			if (entity.getIsIduIdm() != null && entity.getIsIduIdm() == 1) {
				productNeed.append("IDU/IDM");
				productNeed.append(";");
			}
			//单相光伏逆变器
			if (entity.getIsSinglePhaseInverter() != null && entity.getIsSinglePhaseInverter() == 1) {
				productNeed.append("单相光伏逆变器");
				productNeed.append(";");
			}
			//三相光伏逆变器
			if (entity.getIsThreePhaseInverter() != null && entity.getIsThreePhaseInverter() == 1) {
				productNeed.append("三相光伏逆变器");
				productNeed.append(";");
			}
			//集中型逆变器
			if (entity.getIsCentralInverter() != null && entity.getIsCentralInverter() == 1) {
				productNeed.append("集中型逆变器");
				productNeed.append(";");
			}
			//MW房
			if (entity.getIsMwHouse() != null && entity.getIsMwHouse() == 1) {
				productNeed.append("MW房");
				productNeed.append(";");
			}
			//储能
			if (entity.getIsStoredEnergy() != null && entity.getIsStoredEnergy() == 1) {
				productNeed.append("储能");
				productNeed.append(";");
			}
			//空调
			if (entity.getIsAirCondition() != null && entity.getIsAirCondition() == 1) {
				productNeed.append("空调");
				productNeed.append(";");
			}
			
			//处理客户沟通维护方式
			//邮件
			if (entity.getIsContactMethodMail() != null && entity.getIsContactMethodMail() == 1) {
				contactWay.append("邮件");
				contactWay.append(";");
			}
			//电话
			if (entity.getIsContactMethodPhone() != null && entity.getIsContactMethodPhone() == 1) {
				contactWay.append("电话");
				contactWay.append(";");
			}
			//拜访
			if (entity.getIsContactMethodVisit() != null && entity.getIsContactMethodVisit() == 1) {
				contactWay.append("拜访");
				contactWay.append(";");
			}
			//来访
			if (entity.getIsContactComeVisit() != null && entity.getIsContactComeVisit() == 1) {
				contactWay.append("来访");
				contactWay.append(";");
			}
		
			entity.setClientType(clientType.toString());
			entity.setProductNeed(productNeed.toString());
			entity.setContactWay(contactWay.toString());
		}
	}
}
