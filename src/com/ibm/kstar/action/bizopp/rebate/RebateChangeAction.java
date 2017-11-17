package com.ibm.kstar.action.bizopp.rebate;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.page.PageImpl;
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.StringUtil;
import org.xsnake.xflow.api.ITaskService;
import org.xsnake.xflow.api.model.Task;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.action.BaseFlowAction;
import com.ibm.kstar.action.ProcessForm;
import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.api.bizopp.IBizBaseService;
import com.ibm.kstar.api.bizopp.IBizoppService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.order.IOrderService;
import com.ibm.kstar.api.price.IPriceHeadService;
import com.ibm.kstar.api.product.IProductService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.entity.bizopp.Rebate;
import com.ibm.kstar.entity.bizopp.RebateChange;
import com.ibm.kstar.entity.bizopp.RebateLine;
import com.ibm.kstar.entity.order.vo.OrderQuantityVo;
import com.ibm.kstar.entity.product.ProductPriceHead;
import com.ibm.kstar.interceptor.system.permission.NoRight;
import com.ibm.kstar.service.IDemoService;
import com.mysql.jdbc.StringUtils;

/**
 * 特价变更
 * 
 * @author IBM
 *
 */
@Controller
@RequestMapping("/rebate/change")
public class RebateChangeAction extends BaseFlowAction {

	@Autowired
	IBizBaseService bizService;
	@Autowired
	IBizoppService bizoppService;
	@Autowired
	IPriceHeadService priceHeadService;
	@Autowired
	ICustomInfoService customInfoService;
	@Autowired
	IProductService productService;
	@Autowired
	IDemoService demoService;
	@Autowired
	ITaskService taskService;
	@Autowired
	IOrderService orderService;
	@Autowired
    ILovMemberService lovMemberService;
	
	@NoRight
    @RequestMapping("/changeList")
    public String list(Model model) throws Exception{
        return forward("rebate_change_list");
    }

    @NoRight
    @ResponseBody
    @RequestMapping("/rebateChangeList")
    public String page(PageCondition condition, HttpServletRequest request) throws Exception {
        ActionUtil.requestToCondition(condition, request);
        ActionUtil.doSearch(condition);
        String bizId = (String) condition.getConditionMap().get("bizId");
        String bizName = (String) condition.getConditionMap().get("bizName");
        condition.getFilterObject().addCondition("status", "!=", ProcessConstants.PROCESS_STATUS_Destroyed);
        if(!StringUtil.isNullOrEmpty(bizId)||!StringUtil.isNullOrEmpty(bizName)) {
        	PageImpl p = bizService.queryRebateChangeByBizName(condition, RebateChange.class,bizName,bizId);
        	return sendSuccessMessage(p);
        }else {
        	IPage p = bizService.query(condition, RebateChange.class);
        	return sendSuccessMessage(p);
        }
    }
    
	@NoRight
	@RequestMapping(value = "/changePage")
	public String pageGet(String rebateId, Model model) {
		model.addAttribute("rebateId",rebateId);
		return forward("changePage");
	}
	
	
	@NoRight
	@ResponseBody
	@RequestMapping(value = "/page")
	public String pagePost(String rebateId, HttpServletRequest request) {
		List<RebateChange> list = bizService.getRebateChanges(rebateId);
		return sendSuccessMessage(new PageImpl(list, 1, 1, 20));
	}
	
	
	
	@NoRight
	@RequestMapping(value = "/change", method = RequestMethod.GET)
	public String add(String id,String typ, Model model, HttpServletRequest request) {
		
		RebateChange rebateChange = null;
		if ("BIZOPP_REBATE_PRICE_CHANGE_PROC".equals(typ)) {
			// 审批
			rebateChange = bizService.getRebateChangeById(id);
		}else{
			// 查找变更的历史数据
			rebateChange = bizService.getRebateChangeByRebateId(id);
			// 如果变更历史是已审批，表示当前操作是新增变更
			if(rebateChange != null && (ProcessConstants.PROCESS_STATUS_Completed.equals(rebateChange.getStatus())
					||ProcessConstants.PROCESS_STATUS_Destroyed.equals(rebateChange.getStatus()))){
				rebateChange = null;
			}
		}

		if (rebateChange != null) {
			// 存在变更
			Employee employee =  ((Employee)CacheData.getInstance().get(rebateChange.getBusinessExecutive()));
	        if(employee!=null){
	    		model.addAttribute("employee",JSON.toJSONString(employee));
	    	}
			
			ProductPriceHead productPriceHead = priceHeadService.getDefaultPriceHead(getUserObject().getOrg().getId());
			if (productPriceHead != null) {
				model.addAttribute("priceTableId", productPriceHead.getId());
			} else {
				throw new AnneException("默认价格表不能为空！");
			}

			initModel(rebateChange.getId(), rebateChange.getStatus(), model,false);
			model.addAttribute("entity", rebateChange);
		} else {
			// 新增变更

			Rebate entity = bizService.getRebate(id);
			ProductPriceHead productPriceHead = priceHeadService.getDefaultPriceHead(getUserObject().getOrg().getId());
			if (productPriceHead != null) {
				model.addAttribute("priceTableId", productPriceHead.getId());
			} else {
				throw new AnneException("默认价格表不能为空！");
			}
			entity.setVersion(entity.getVersion()+1);
			entity.setStatus(ProcessConstants.PROCESS_STATUS_Pending);
			model.addAttribute("entity", entity);
			
			initModel(entity.getId(), entity.getStatus(), model,true);

		}
		if(rebateChange!=null) {
			String specialOffFlag = "N";
	        List<LovMember> lovMemberList = lovMemberService.getListByGroupCode("SPECIALOFF");
	        for(LovMember lovMember:lovMemberList) {
	        	if("02".equals(lovMember.getCode())) {
	        		if(lovMember.getId().equals(rebateChange.getSpecialOff())) {
	        			specialOffFlag = "Y";
	        		}
	        	}
	        }
	        model.addAttribute("specialOffFlag", specialOffFlag);
		}
		model.addAttribute("flag", getUserObject().isInner());
		model.addAttribute("newProcessType", request.getParameter("newProcessType"));

		String taskId = request.getParameter("taskId");
		if (StringUtil.isNotEmpty(taskId)) {
			Task task = taskService.getTask(taskId);
			if (task == null) {
				throw new AnneException("任务已经不存在");
			}
			model.addAttribute("task", task);
			model.addAttribute("taskId", task.getId());
			try {
				getHistory(taskId, model);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return forward("change");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value = "/change", method = RequestMethod.POST)
	public String edit_post(RebateChange entity, HttpServletRequest request) {
		entity.fillInit(getUserObject());
		String newProcessType = request.getParameter("newProcessType");
		ProcessForm form = ActionUtil.getProcessForm(request, getUserObject());
		String saveOrUpdate = request.getParameter("saveOrUpdate");
		String pid = request.getParameter("pid");
		boolean newProcessTypeUpdateFlag = false;//判断流程中是否保存数据
		if ("update".equals(saveOrUpdate)) {
			entity.setId(pid);
		}
		String entityCreatedById = entity.getCreatedById();
		String userId = getUserObject().getEmployee().getId();
		if(!StringUtil.isNullOrEmpty(newProcessType)&&"Y".equals(newProcessType)
				&&((entityCreatedById.equals(userId)&&("Rejected".equals(entity.getStatus()))||hasPermission("P09SpecialChangEdit")))		
				) {
			newProcessTypeUpdateFlag = true; 
		}
		if(StringUtil.isNullOrEmpty(newProcessType)) {
			bizService.updateRebate(entity,form,getUserObject(),saveOrUpdate,newProcessTypeUpdateFlag);
		}else{
			bizService.updateRebate(entity,form,getUserObject(),saveOrUpdate,newProcessTypeUpdateFlag);
		}
		return sendSuccessMessage();
	}

	/**
	 * 特价变更删除行
	 * @param no
	 * @param status
	 * @param ids
	 * @return
	 */
	@NoRight
    @ResponseBody
    @RequestMapping(value = "/deleteLine")
    public String deleteLine(String no,String status,String ids) {
        
        if (!StringUtil.isNullOrEmpty(ids)) {
        	String[] idstr = ids.split(",");
            for (String string : idstr) {
            	if (StringUtil.isNullOrEmpty(string)) {
					continue;
				}
            	RebateLine line = bizService.getRebateLine(string);
            	if(line==null)
            		continue;
            	String oldId = line.getOldId();
            	if(StringUtils.isNullOrEmpty(line.getOldId()))
            		oldId = line.getId();
            	OrderQuantityVo quantityVo = orderService.getRebateOrderQty(no, oldId);
            	if (quantityVo.getProQty() > 0 || quantityVo.getCancelQty() > 0 || quantityVo.getDeliveryQty() > 0 || quantityVo.getInvoiceQty() > 0) {
					throw new AnneException("存在下单数量不允许删除！");
				}
            	if (!status.equals(ProcessConstants.PROCESS_STATUS_Pending)) {
            		bizService.delete(string, RebateLine.class);
				}
    		}
		}
        
        return sendSuccessMessage();
    }
	
	
	private void initModel(String id, String status, Model model,boolean history) {
		TabMain tabMainInfo = new TabMain();

		tabMainInfo.setInitAll(false);
		if (!history) {
			tabMainInfo.addTab("销售团队", "/team/list.html?businessType=Rebate&businessId=" + id);
			if (history) {
				tabMainInfo.addTab("审批历史", "/standard/history.html?contrId=1" + id); // 新建没有审批历史
			}else{
				tabMainInfo.addTab("审批历史", "/standard/history.html?contrId=" + id);
			}
			
			if ("Pending".equals(status) || "Rejected".equals(status)) {
				tabMainInfo.addTab("附件","/common/attachment/attachment.html?businessId=" + id + "&businessType=SPECIAL_OFFER_FILE&docGroupCode=ATTACHMENTTYPEGROUP");
			} else {
				tabMainInfo.addTab("附件", "/common/attachment/attachment.html?businessId=" + id + "&businessType=SPECIAL_OFFER_FILE&docGroupCode=ATTACHMENTTYPEGROUP&unableAdd=true&unableDelete=true");
			}
		}
		model.addAttribute("tabMainInfo", tabMainInfo);
	}

	@NoRight
	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(String id,String typ, Model model, HttpServletRequest request) {
		
		RebateChange rebateChange = bizService.getRebateChangeById(id);
		
		if (rebateChange != null) {
			// 存在变更
			Employee employee =  ((Employee)CacheData.getInstance().get(rebateChange.getBusinessExecutive()));
	        if(employee!=null){
	    		model.addAttribute("employee",JSON.toJSONString(employee));
	    	}
			
			ProductPriceHead productPriceHead = priceHeadService.getDefaultPriceHead(getUserObject().getOrg().getId());
			if (productPriceHead != null) {
				model.addAttribute("priceTableId", productPriceHead.getId());
			} else {
				throw new AnneException("默认价格表不能为空！");
			}

			initModel(rebateChange.getId(), rebateChange.getStatus(), model,false);
			model.addAttribute("entity", rebateChange);
		} 
		
		model.addAttribute("flag", getUserObject().isInner());
		model.addAttribute("newProcessType", request.getParameter("newProcessType"));

		String taskId = request.getParameter("taskId");
		if (StringUtil.isNotEmpty(taskId)) {
			Task task = taskService.getTask(taskId);
			if (task == null) {
				throw new AnneException("任务已经不存在");
			}
			model.addAttribute("task", task);
			model.addAttribute("taskId", task.getId());
			try {
				getHistory(taskId, model);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return forward("change");
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(String id) {
		bizService.destoryRebateChange("RebateChange", id, "status", ProcessConstants.PROCESS_STATUS_Destroyed);
		return sendSuccessMessage();
	}
	
}
