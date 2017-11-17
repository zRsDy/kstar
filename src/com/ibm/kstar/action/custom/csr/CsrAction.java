package com.ibm.kstar.action.custom.csr;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.log.LogOperate;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.ExcelUtil;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.custom.ICustomNumberService;
import com.ibm.kstar.api.custom.ICustomSatInvestService;
import com.ibm.kstar.api.system.lov.ILovGroupService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovGroup;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.ICorePermissionService;
import com.ibm.kstar.api.system.permission.IEmployeeService;
import com.ibm.kstar.api.system.permission.IMenu;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.entity.custom.CustomSatInvest;
import com.ibm.kstar.entity.custom.vo.InevestVo;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/custom/csr")
public class CsrAction extends BaseAction {
	
	@Autowired
	ICustomSatInvestService service;
	
	@Autowired
	ICustomNumberService numberService;
	
	@Autowired
	protected ILovGroupService lovGroupService;
	
	@Autowired
	protected ILovMemberService lovMemberService;
	
	@Autowired
	IEmployeeService employeeService;

	@RequestMapping("/list")
	public String list(String id,Model model){
		List<String> investQuarterList = new ArrayList<String>();
		
		String dateStr = "2017-06-01";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = format.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		do {
			String str =(date.getYear()+1900)+"年第"+(date.getMonth()/3+1)+"季度";
			investQuarterList.add(str);
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			
			calendar.add(Calendar.MONTH, 3);
			
			date = calendar.getTime();
			
			
		} while (date != null && date.before(new Date()));

		
		model.addAttribute("investQuarterList",investQuarterList);
		return forward("list");
	}
	
	@LogOperate(module="客户管理模块：客户满意度调查",notes="${user}页面：客户满意度调查")
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		
		/*String searchKey = condition.getStringCondition("searchKey");
		if(searchKey !=null){
			condition.getFilterObject().addOrCondition("handoverComment", "like", "%"+searchKey+"%");
		}*/
		
		IPage p = service.querySatInvest(condition);
		
		System.out.println("==================查询完毕==============");
		
		return sendSuccessMessage(p);
	}
	
	@LogOperate(module="客户管理模块：客户满意度调查",notes="${user}页面：客户满意度调查导出")
	@RequestMapping("/mappageRightExport")
	public void mappageRightExport(PageCondition condition,HttpServletRequest request,HttpServletResponse response){
		ActionUtil.requestToCondition(condition, request);
		List<List<Object>> dataList = service.mappageExport(condition);
		SimpleDateFormat sdf = new SimpleDateFormat(IConstants.YMDHMS_FORMAT_1);
		ExcelUtil.exportExcel(response, dataList, "客户满意度调查列表-"+sdf.format(new Date()));
	}
	
	@LogOperate(module="客户管理模块：客户满意度调查",notes="${user}页面：客户满意度调查记录新增")
	@RequestMapping("/add")
	public String add(String id, Model model){
		model.addAttribute("id",id);
		
		CustomSatInvest customSatInvest = new CustomSatInvest();
		customSatInvest.setId(null);
		customSatInvest.setCreateEmployee(getUserObject().getEmployee().getId());
		SimpleDateFormat sdf = new SimpleDateFormat(IConstants.YMDHMS_FORMAT_1);
		sdf = new SimpleDateFormat(IConstants.YMD_FORMAT_1);
		
		customSatInvest.setCreateDate(sdf.format(new Date()));
		customSatInvest.setProcStatusCd(IConstants.CUSTOM_NORMAL_STATUS_10);
		customSatInvest.setAreaOrIndustry("广东光伏");
		customSatInvest.setInvestCode(numberService.getInvestCode());

		customSatInvest.setInvestQuarter(Calendar.getInstance().get(Calendar.YEAR)+"年第"+(Calendar.getInstance().get(Calendar.MONTH)/3+1)+"季度");
		
		model.addAttribute("customSatInvest",customSatInvest);
		
		return forward("add");
	}
	
	@LogOperate(module="客户管理模块：客户满意度调查",notes="${user}页面：客户满意度调查记录新增保存")
	@ResponseBody
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public String add(CustomSatInvest customSatInvest, HttpServletRequest request){
		
		service.saveSatInvestInfo(customSatInvest, getUserObject());
		return sendSuccessMessage(customSatInvest.getId());
	}
	
	@LogOperate(module="客户管理模块：客户满意度调查",notes="${user}页面：客户满意度调查记录编辑")
	@RequestMapping("/edit")
	public String edit(String id,Model model){
		if(id==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		CustomSatInvest customSatInvest = service.getSatInvest(id);
		if(IConstants.CUSTOM_NORMAL_STATUS_40.equals(customSatInvest.getProcStatusCd())){
			return forward("tip");
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try {
			Date effDate = sdf.parse(customSatInvest.getEffDate());
			Date nowDate = new Date();
			
			if(nowDate.after(effDate)){
				return forward("over_date.html");
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		model.addAttribute("customSatInvest",customSatInvest);
		
		return forward("edit");
	}
	
	@LogOperate(module="客户管理模块：客户满意度调查",notes="${user}页面：客户满意度调查记录编辑保存")
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(CustomSatInvest customSatInvest){
		customSatInvest.setProcStatusCd(IConstants.CUSTOM_NORMAL_STATUS_40);
		service.updateSatInvestInfo(customSatInvest);
		return redirect("tip");
	}
	
	@LogOperate(module="客户管理模块：客户满意度调查",notes="${user}页面：客户满意度调查记录查看")
	@RequestMapping("/detail")
	public String detail(String id,Model model){
		if(id==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		CustomSatInvest customSatInvest = service.getSatInvest(id);
		
		model.addAttribute("customSatInvest",customSatInvest);
		
		return forward("detail");
	}
	
	@LogOperate(module="客户管理模块：客户满意度调查",notes="${user}页面：客户满意度调查记录删除")
	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		service.deleteSatInvestInfo(id);
		return sendSuccessMessage();
	}
	
	@LogOperate(module="客户管理模块：客户满意度调查",notes="${user}页面：客户满意度调查记录编辑(流程推进)")
	@RequestMapping("/process")
	public String process(String id,Model model){
		if(id==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		CustomSatInvest customSatInvest = service.getSatInvest(id);
		
		model.addAttribute("customSatInvest",customSatInvest);
		
		return forward("edit2");
	}
	
	@LogOperate(module="客户管理模块：客户满意度调查",notes="${user}页面：客户满意度调查记录编辑保存(流程推进)")
	@ResponseBody
	@RequestMapping(value="/process",method=RequestMethod.POST)
	public String process(CustomSatInvest customSatInvest){
		
		service.updateSatInvestInfo(customSatInvest);
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value="/test",method=RequestMethod.POST)
	public String test(@RequestBody List<CustomSatInvest> list){
		
		return sendSuccessMessage();
	}
	
	@Autowired
	ICorePermissionService corePermissionService;
	
	@RequestMapping("/getMenuList")
	public String getMenuList(){
		
		List<IMenu> menuList = corePermissionService.getSystemMenuList("CRM");
		
		return sendSuccessMessage(menuList);
	}
	
	@LogOperate(module="客户管理模块：客户满意度调查",notes="${user}页面：客户满意度调查对象列表")
	@RequestMapping("/inevest")
	public String inevest(Model model){
		LovGroup lovGroup = lovGroupService.getByCode("CustomSatInvest");
		  
		model.addAttribute("lovGroup",lovGroup);
		
		return forward("inevest_list");
	}
	
	@SuppressWarnings("unchecked")
	@NoRight
	@LogOperate(module="客户管理模块：客户满意度调查",notes="${user}页面：客户满意度调查对象列表")
	@ResponseBody
	@RequestMapping("/inevestPage")
	public String inevestPage(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		String groupId = condition.getStringCondition("groupId");
		String parentId = condition.getStringCondition("parentId");
		String searchKey = condition.getStringCondition("searchKey");
		// condition.getFilterObject().addCondition("deleteFlag", "eq", "N");
		condition.getFilterObject().addCondition("groupId", "eq", groupId);
		if (parentId != null) {
			condition.getFilterObject().addCondition("path", "like", "%" + parentId + "%");
		}
		if (searchKey != null) {
			condition.getFilterObject().addOrCondition("name", "like", "%" + searchKey + "%");
			condition.getFilterObject().addOrCondition("code", "like", "%" + searchKey + "%");
		}

		condition.getFilterObject().addOrderBy("no", "desc");
		IPage p = lovMemberService.query(condition);
		
		
		List<LovMember> list = new ArrayList<LovMember>();
		if(p.getCount() > 0){
			list.addAll((List<LovMember>) p.getList());
			
			p.getList().clear();
			
			List<InevestVo> inevestVos = (List<InevestVo>)p.getList();
			for(LovMember lovMember : list){
				InevestVo inevestVo = new InevestVo();
				inevestVo.setGroupCode(lovMember.getCode());
				inevestVo.setLovMemberId(lovMember.getId());
				
				LovMember org = lovMemberService.get(lovMember.getOptTxt1());
				if(org != null){
					inevestVo.setOrgId(org.getId());
					inevestVo.setOrgName(org.getNamePath());
				}
				
				Employee employee = employeeService.get(lovMember.getOptTxt2());
				if(employee != null){
					inevestVo.setEmployeeId(employee.getId());
					inevestVo.setEmployeeNo(employee.getNo());
					inevestVo.setEmployeeName(employee.getName());
					inevestVo.setEmail(employee.getEmail());
				}
				
				inevestVo.setRemark(lovMember.getMemo());
				
				inevestVos.add(inevestVo);
			}
		}
		return sendSuccessMessage(p);
	}
	
	@RequestMapping("/addInevest")
	public String addInevest(String groupId, Model model) {
		LovGroup lovGroup = lovGroupService.get(groupId);
		InevestVo inevestVo = new InevestVo();
		inevestVo.setGroupCode("CustomSatInvest");
		model.addAttribute("lovGroup", lovGroup);
		model.addAttribute("inevestVo", inevestVo);
		return forward("add_inevest");
	}
	
	@LogOperate(module="客户管理模块：客户满意度调查",notes="${user}页面：客户满意度调查对象-新增")
	@ResponseBody
	@RequestMapping(value = "/addInevest", method = RequestMethod.POST)
	public String addInevest(InevestVo inevestVo, HttpServletRequest request) {
		LovGroup lovGroup = lovGroupService.getByCode("CustomSatInvest");
		LovMember inevest = new LovMember();
		inevest.setGroupCode("CustomSatInvest");
		inevest.setGroupId(lovGroup.getId());
		
		inevest.setCode(inevestVo.getOrgId()+inevestVo.getEmployeeId());
		inevest.setName("客户满意度调查对象");
		inevest.setOptTxt1(inevestVo.getOrgId());
		inevest.setOptTxt2(inevestVo.getEmployeeId());
		inevest.setMemo(inevestVo.getRemark());
		
		lovMemberService.save(inevest);
		
		return sendSuccessMessage();
	}
	
	@LogOperate(module="客户管理模块：客户满意度调查",notes="${user}页面：客户满意度调查对象-新增")
	@ResponseBody
	@RequestMapping(value = "/deleteInevest", method = RequestMethod.POST)
	public String deleteInevest(String lovMemberId, HttpServletRequest request) {
		
		lovMemberService.delete(lovMemberId);
		
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/employeeList", method = RequestMethod.POST)
	public String employeeList(String orgId, HttpServletRequest request) {
		
		List<Employee> employeeList =  employeeService.findEmployeeByOrgId(orgId);
		return sendSuccessMessage(employeeList);
	}
}
