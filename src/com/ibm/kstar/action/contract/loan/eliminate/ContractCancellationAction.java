/**
 * 
 */
package com.ibm.kstar.action.contract.loan.eliminate;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.xsnake.web.utils.ExcelUtil;
import org.xsnake.web.utils.StringUtil;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.bizopp.IBizoppService;
import com.ibm.kstar.api.contract.IContrChangeService;
import com.ibm.kstar.api.contract.IContractEliminateService;
import com.ibm.kstar.api.contract.IContractLoanService;
import com.ibm.kstar.api.contract.IContractService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.price.IPriceHeadService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.EmployeeObject;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.entity.bizopp.BusinessOpportunity;
import com.ibm.kstar.entity.contract.ContrChange;
import com.ibm.kstar.entity.contract.Contract;
import com.ibm.kstar.entity.contract.ContractEliminate;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.product.ProductPriceHead;
import com.ibm.kstar.interceptor.system.permission.NoRight;

/**
 * @author ibm
 *	无合同核销
 */
@Controller
@RequestMapping("/loan/eliminate")
public class ContractCancellationAction extends BaseAction {
	Logger logger = LoggerFactory.getLogger(getClass());

	//private final static String CONTRACT_PROCESS_KEY = "contrLoanBasic";
 
	@Autowired
	private ILovMemberService lovMemberService;	
	
	@Autowired
	private IContractEliminateService contractEliminateService;	
 
	@Autowired
	private IContractLoanService contractLoanService;

	@Autowired
	private ICustomInfoService customerService;
	
	@Autowired
	private IBizoppService bizOppService;

	@Autowired
	private IPriceHeadService priceHeadService;

	@Autowired
	private IContractService contractService;
	@Autowired
	private IContrChangeService  changeService ;

	@NoRight
	@RequestMapping("/list")
	public String list(Model model,HttpServletRequest request) {
		String contrId = request.getParameter("contrId");
		String typ = request.getParameter("typ");
		String ContrNo = request.getParameter("ContrNo");
		String pricetableid = request.getParameter("pricetableid");
		String flag = request.getParameter("flag");
		model.addAttribute("contr",contractLoanService.get(contrId));
		model.addAttribute("contrId",contrId);
		model.addAttribute("ContrNo",ContrNo);
		model.addAttribute("typ",typ);
		model.addAttribute("flag",flag);
		model.addAttribute("pricetableid",pricetableid);
		return forward("nonContractCancellationList");
	}

	@NoRight
	@RequestMapping("/addtabs")
	public String addtabs(Model model,HttpServletRequest request,String id) {
		String contrId = request.getParameter("contrId");
		String typ = request.getParameter("typ");
		String ContrNo = request.getParameter("ContrNo");
		String flag = request.getParameter("flag");
		String buttonType = "01";
		
		model.addAttribute("contrId",contrId);
		model.addAttribute("typ",typ);
		model.addAttribute("flag",flag);
		ContractEliminate contractEliminate = new ContractEliminate();
		if("Y".equals(flag)) {//如果是新增无合同核销申请
			List<ContractEliminate> list = contractEliminateService.queryEliminateHeaders(contrId);
			contractEliminate.setEliminateNumber(ContrNo+"-"+(list.size()+1));
			contractEliminate.setContractId(contrId);
			contractEliminate.setEliminateUser(getUserObject().getEmployee().getName());
			contractEliminate.setEliminateUserId(getUserObject().getEmployee().getId());
			contractEliminate.setEliminateDate(new Date());
			
			LovMember statLov = lovMemberService.getLovMemberByCode("CONTRACTELIMINATE", "01");
			contractEliminate.setEliminateType(statLov.getId());
			String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			contractEliminate.setId(uuid);
			id = uuid;
		}else {
			if(StringUtil.isNullOrEmpty(id)) {
				id = request.getParameter("eliminateId");
			}
			
			List<ContractEliminate> list = contractEliminateService.queryEliminateHeadersByid(id);
			contractEliminate  = list.get(0);
			buttonType= lovMemberService.get(contractEliminate.getEliminateType()).getCode();
			LovMember lov =  ((LovMember)CacheData.getInstance().get(contractEliminate.getEliminateType()));
			LovMember statLov = lovMemberService.getLovMemberByCode("CONTRACTELIMINATE",buttonType);
			contractEliminate.setEliminateType(statLov.getId());
			contractEliminate.setId(id);
		}
		
		
		model.addAttribute("contractEliminate",contractEliminate);
		model.addAttribute("buttonType",buttonType);
		
		TabMain tabMain = new TabMain();
		tabMain.setInitAll(false); 
		tabMain.addTab("附件", "/common/attachment/attachment.html?businessId="+id+"&businessType=CONTR_LOAN&docGroupCode=CONTRACTDOCTYPE");
		tabMain.addTab("审批历史", "/standard/history.html?contrId="+id+"&editFlag"+flag);
		model.addAttribute("tabMain",tabMain);
		return forward("addtabs");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		IPage p = contractEliminateService.queryEliminateHeaders(condition);
		return sendSuccessMessage(p);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(String id) {
		contractEliminateService.deleteEliminate(id);
		return sendSuccessMessage();
	}
	
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/addtabs",method=RequestMethod.POST)
	public String add(ContractEliminate contractEliminate,String listStr,HttpServletRequest request) {
		contractEliminateService.saveEliminate(contractEliminate);
		return sendSuccessMessage();
	}
	
	/**
	 * 提交评审
	 * @param contractEliminate
	 * @param request
	 * @param typ
	 * @return
	 */
	@NoRight
	@ResponseBody
	@RequestMapping(value="/startContractEliminateProcess",method=RequestMethod.POST)
	public String startContractEliminateProcess(ContractEliminate contractEliminate,HttpServletRequest request,String typ) {
		String id = contractEliminate.getId();
		List<ContractEliminate> list = contractEliminateService.queryEliminateHeadersByid(id);
		if(list.size()<1 ) {
			throw new AnneException("请先进行保存再提交评审!");
		}
		contractEliminateService.startContractEliminateProcess(contractEliminate,getUserObject(),typ);
		return sendSuccessMessage();
	}
}
