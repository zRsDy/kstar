package com.ibm.kstar.action.bizopp.rebate;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.action.BaseFlowAction;
import com.ibm.kstar.action.ProcessForm;
import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.action.common.process.ProcessStatusService;
import com.ibm.kstar.api.bizopp.IBizBaseService;
import com.ibm.kstar.api.bizopp.IBizoppService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.price.IPriceHeadService;
import com.ibm.kstar.api.product.IProductService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.entity.bizopp.*;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.product.ProductPriceHead;
import com.ibm.kstar.interceptor.system.permission.NoRight;
import com.ibm.kstar.service.IDemoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.utils.FilerRuler;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.page.PageImpl;
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.ExcelUtil;
import org.xsnake.web.utils.StringUtil;
import org.xsnake.xflow.api.ITaskService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by wangchao on 2017/5/10.
 */
@Controller
@RequestMapping("/rebate")
public class RebateAction extends BaseFlowAction {
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
    ILovMemberService lovMemberService;
    @Autowired
    ProcessStatusService processStatusService;
    @Autowired
    ITaskService taskService;
    
    @NoRight
    @RequestMapping("/list")
    public String list(String id, Model model) {
        return forward("list");
    }

    @NoRight
    @ResponseBody
    @RequestMapping("/page")
    public String page(PageCondition condition, HttpServletRequest request) throws Exception {
        ActionUtil.requestToCondition(condition, request);
        ActionUtil.doSearch(condition);
        String bizId = (String) condition.getConditionMap().get("bizId");
        String bizName = (String) condition.getConditionMap().get("bizName");
        condition.getFilterObject().addCondition("status", "!=", ProcessConstants.PROCESS_STATUS_Destroyed);
        if(!StringUtil.isNullOrEmpty(bizId)||!StringUtil.isNullOrEmpty(bizName)) {
        	PageImpl p = bizService.queryByBizName(condition, Rebate.class,bizName,bizId);
        	return sendSuccessMessage(p);
        }else {
        	IPage p = bizService.query(condition, Rebate.class);
        	return sendSuccessMessage(p);
        }
    }

    @NoRight
    @ResponseBody
    @RequestMapping("/rivalPage")
    public String rivalPage(PageCondition condition, HttpServletRequest request) throws Exception {
        ActionUtil.requestToCondition(condition, request);
        String rebateId = condition.getStringCondition("rebateId");
        if (StringUtil.isEmpty(rebateId)) {
            return sendSuccessMessage(new PageImpl(null, condition.getPage(), condition.getRows(), 0));
        } else {
            condition.getFilterObject().addOrCondition("rebateId", "=", rebateId);
        }
        IPage p = bizService.query(condition, RebateRival.class);
        return sendSuccessMessage(p);
    }


    @NoRight
    @ResponseBody
    @RequestMapping("/txPage")
    public String t1Page(PageCondition condition, HttpServletRequest request) throws Exception {
        ActionUtil.requestToCondition(condition, request);
        ActionUtil.doSearch(condition);
        String rebateId = condition.getStringCondition("rebateId");
        String productType = condition.getStringCondition("productType");
        String materCode = condition.getStringCondition("pageSearch_materCode_like");
        
        if(!StringUtil.isEmpty(materCode)) {
        	FilerRuler temp = null;
        	condition.getConditionMap().remove("pageSearch_materCode_like");
        	List<FilerRuler> rulerList = condition.getFilterObject().getRules();
        	for(FilerRuler rule:rulerList) {
        		if("materCode".equals(rule.getField())) {
        			temp = rule;
        		}
        	}
        	rulerList.remove(temp);
        }
        String s = request.getParameter("materCode");
        if (StringUtil.isEmpty(rebateId)) {
            request.getParameter("rebateId");
            return sendSuccessMessage(new PageImpl(null, condition.getPage(), condition.getRows(), 0));
        } else {
            condition.getFilterObject().addCondition("rebateId", "=", rebateId);
            if("1".equals(productType)) {
            	condition.getFilterObject().addOrCondition("productType", "=", productType);
            	condition.getFilterObject().addOrCondition("productType", "=", "3");
            }else {
            	condition.getFilterObject().addCondition("productType", "=", productType);
            }
        }
        IPage p = bizService.query(condition, RebateLine.class);
        PageImpl page = new PageImpl(p.getList(),condition.getPage(),condition.getRows(),p.getCount());
        List<RebateLine> tempList = new ArrayList();
        for (RebateLine line : (List<RebateLine>) page.getList()) {
            if (StringUtil.isNotEmpty(line.getId())) {
                line.setOrderQty(bizoppService.getOrderQtyByLineId(line.getId()));
            }
            if(!StringUtil.isEmpty(materCode)) {
            	if(materCode.equals(line.getMaterCode())) {
            		tempList.add(line);
            	}
            }
        }
        if(tempList.size()>0) {
        	page.setList(tempList);
        }
        return sendSuccessMessage(page);
    }

    @NoRight
    @RequestMapping("/add")
    public String add(Model model, String bizOppId) {
        Rebate entity = new Rebate();
        entity.fillInit(getUserObject());
        entity.setNo(bizoppService.getSpecialPriceApplyNumber());
        entity.setStatus(ProcessConstants.PROCESS_STATUS_Pending);
        //订单初始版本1
        entity.setVersion(IConstants.ORDER_VERSION);
        
        List<LovMember> lovMemberList = lovMemberService.getListByGroupCode("SPECIALOFF");
        for(LovMember lovMember:lovMemberList) {
        	if("01".equals(lovMember.getCode())) {
        		entity.setSpecialOff(lovMember.getId());
        	}
        }
        entity.setApplyAgent(getUserObject().getOrg().getOptTxt4());
        entity.setApplyAgentName(getUserObject().getOrg().getName());
        String customId = getUserObject().getOrg().getOptTxt4();
        CustomInfo customInfo = customInfoService.getCustomInfo(customId);
        if (customInfo != null) {
            entity.setBelongArea(customInfo.getCustomAreaName());
//            entity.setBelongIndustry(customInfo.getCustomCategoryName() + "/" + customInfo.getCustomCategorySubName());
        }
        ProductPriceHead productPriceHead = priceHeadService.getDefaultPriceHead(getUserObject().getOrg().getId());
        if (productPriceHead != null) {
            model.addAttribute("priceTableId", productPriceHead.getId());
        } else {
            throw new AnneException("默认价格表不能为空！");
        }
        model.addAttribute("entity", entity);
        model.addAttribute("flag", getUserObject().isInner());
        TabMain tabMainInfo = new TabMain();
        tabMainInfo.setInitAll(false);
        model.addAttribute("tabMainInfo", tabMainInfo);
        model.addAttribute("specialOffFlag", "N");

        //商机带入
        if (StringUtil.isNotEmpty(bizOppId)) {
            BusinessOpportunity bu = bizoppService.getBizOppEntity(bizOppId);

            if (bu != null) {
                if ("40".equals(bu.getConflictStatus()) || "45".equals(bu.getConflictStatus()) || "60".equals(bu.getConflictStatus())) {
                    entity.setBizIdh(bizOppId);
                    entity.setBizNameh(bu.getBizOppAddress());

                    List<ProductDetail> pds = bizoppService.getProducrDetailByBizId(bizOppId);
                    List<RebateLine> rebateLines_t1 = new ArrayList<>();
                    List<RebateLine> rebateLines_t2 = new ArrayList<>();
                    List<RebateLine> rebateLines_t3 = new ArrayList<>();
                    List<RebateLine> rebateLines_t4 = new ArrayList<>();
                    List<RebateLine> rebateLines_t5 = new ArrayList<>();

                    if (pds != null) {

                        Map<String, Integer> tempMap = bizoppService.getBizProductSurplusSum(bu.getId());
                        Map<String, Integer> bizProductSurplusSum = new HashMap<>();
                        for (Map.Entry<String, Integer> entry : tempMap.entrySet()) {
                            String key = entry.getKey();
                            Integer value = entry.getValue();
                            if (value != null && value > 0) {
                                bizProductSurplusSum.put(key, value);
                            }
                        }
                        for (ProductDetail pd : pds) {
                        	
//                        	Double amt = bizService.getRebateLineApplyQtyDif(pd.getId());
                            String productModel = pd.getProductModel();
                            Integer surplusSum = bizProductSurplusSum.get(productModel);
                            if (surplusSum == null) {
                                continue;
                            }
                            bizProductSurplusSum.put(productModel, 0);
                            RebateLine line = new RebateLine();

                            line.setOrderQty(null);

                            line.setProductType(null);

                            line.setApplyQty(surplusSum.doubleValue());

                            line.setApproveRebate(100d);
                            line.setApproveYo(100d);
                            line.setBizId(bizOppId);
                            line.setBizName(bu.getNumber() + " | " + bu.getOpportunityName());
                            line.setClientId(bu.getClientId());
                            line.setClientName(bu.getClientName());
                            line.setCproPowcap(pd.getCproPowcap());
                            line.setProductId(pd.getProductId());
                            line.setProductName(pd.getProductName());
                            line.setProductModel(productModel);
                            line.setDisplayCatalogName(pd.getDisplayCatalogName());
                            line.setIsBiz(pd.getId());//商机配置行的id
                            line.setBizQty(surplusSum.doubleValue());
                            line.setCatalogPrice(pd.getPlanPrice().doubleValue());

//                        0003	服务产品
//                        0004	商务产品
//                        0001	标准产品	可发布
//                        0002	非标产品	可发布
//                        0005	外购产品

//                        KstarProductLine pdline = productService.getProductLineByProductId(pd.getProductId());
                            if(productModel == null){
                                throw new AnneException(pd.getProductName() + " 产品型号为空，请先维护产品型号！");
                            }
                            if(productModel.startsWith("YDC")|| productModel.startsWith("YDE")){
                                //TODO 分销
                                //TODO 非标
                                line.setSourcePrice(pd.getPublicPrice().doubleValue());
                                line.setApplyPrice(pd.getPublicPrice().doubleValue());
                                line.setApprovePrice(pd.getPublicPrice().doubleValue());
                                line.setApplyRebate(0d);
                                line.setApproveRebate(0d);
                                line.setAmount(pd.getPublicPrice().doubleValue() * line.getApplyQty());

                                line.setApplyRebate(100d);
                                line.setApproveRebate(100d);
                                line.setApplyAmount(line.getApplyPrice() * line.getApplyQty());
                                line.setProductType("5");
                                rebateLines_t5.add(line);
                            }
                            if ("电池".equals(pd.getCproCategory())) {
                                //TODO 电池
                                line.setSourcePrice(pd.getPublicPrice().doubleValue());
                                line.setApplyPrice(pd.getPublicPrice().doubleValue());

                                line.setApprovePrice(pd.getPublicPrice().doubleValue());
                                line.setAmount(line.getApprovePrice() * line.getApplyQty());
                                line.setApplyAmount(line.getApplyQty() * line.getApplyPrice());
                                line.setProductType("2");

                                rebateLines_t2.add(line);
                            } else {
                                if ("0001".equals(pd.getCrmCategory())||"0002".equals(pd.getCrmCategory())&&!(productModel.startsWith("YDC")|| productModel.startsWith("YDE"))) {
                                    //TODO 标准
                                    line.setSourcePrice(pd.getPublicPrice().doubleValue());
                                    line.setApplyPrice(pd.getPublicPrice().doubleValue());
                                    line.setApprovePrice(pd.getPublicPrice().doubleValue());
                                    line.setApplyRebate(0d);
                                    line.setApproveRebate(0d);
                                    line.setAmount(pd.getPublicPrice().doubleValue() * line.getApplyQty());
                                    line.setProductSortId(pd.getCrmCategory());
                                    line.setProductSortName(pd.getCrmCategoryLable());
                                    line.setApplyRebate(100d);
                                    line.setApproveRebate(100d);
                                    line.setApplyAmount(pd.getPublicPrice().doubleValue() * line.getApplyQty());
                                    line.setProductType("1");
                                    rebateLines_t1.add(line);
                                } /*else if ("0002".equals(pd.getCrmCategory())&&!(pd.getProductModel().startsWith("YDC")||pd.getProductModel().startsWith("YDE"))) {
                                    //TODO 非标
                                    line.setSourcePrice(pd.getPublicPrice().doubleValue());
                                    line.setApplyPrice(pd.getPublicPrice().doubleValue());
                                    line.setApprovePrice(pd.getPublicPrice().doubleValue());
                                    line.setApplyRebate(0d);
                                    line.setApproveRebate(0d);
                                    line.setAmount(pd.getPublicPrice().doubleValue() * line.getApplyQty());

                                    line.setApplyRebate(100d);
                                    line.setApproveRebate(100d);
                                    line.setApplyAmount(line.getApplyPrice() * line.getApplyQty());
                                    line.setProductType("3");
                                    rebateLines_t3.add(line);
                                }*/
                            }
                            //TODO 入围
                            RebateLine otLine = new RebateLine();
                            BeanUtils.copyProperties(line, otLine);
                            otLine.setSourcePrice(0d);
                            otLine.setApplyPrice(0d);
                            otLine.setApprovePrice(0d);
                            otLine.setApplyYo(0d);
                            otLine.setApproveYo(0d);
                            otLine.setAmount(line.getApplyPrice() * line.getApplyQty());

                            otLine.setProductType("4");
                            rebateLines_t4.add(otLine);

                        }

                        if (rebateLines_t1.size() == 0 && rebateLines_t2.size() == 0 && rebateLines_t3.size() == 0 && rebateLines_t4.size() == 0 && rebateLines_t5.size() == 0) {
							throw new AnneException(bu.getNumber()+" 商机明细已全部转特价，请选择其他商机！");
						}
                        
                        model.addAttribute("rebateLines_t1", JSON.toJSONString(rebateLines_t1));
                        model.addAttribute("rebateLines_t2", JSON.toJSONString(rebateLines_t2));
                        model.addAttribute("rebateLines_t3", JSON.toJSONString(rebateLines_t3));
                        model.addAttribute("rebateLines_t4", JSON.toJSONString(rebateLines_t4));
                        model.addAttribute("rebateLines_t5", JSON.toJSONString(rebateLines_t5));
                    }
                }else{
                    throw new AnneException("商机状态不在【报备生效】【即将失效】【开放授权】之中！无法发起特价申请！");
                }
            }
        }
        return forward("add");
    }

    @NoRight
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String add_post(Rebate entity) {
        entity.fillInit(getUserObject());
        if(!StringUtil.isNullOrEmpty(entity.getType())&&"分销".equals(entity.getType())) {
    		String[] specialOff = entity.getSpecialOff().split(",");
    		entity.setSpecialOff(specialOff[0]);
    	}
        bizService.saveRebate(entity, getUserObject());
        return sendSuccessMessage();
    }

    @NoRight
    @RequestMapping("/edit")
    public String edit(String id, Model model,HttpServletRequest request) {
        Rebate entity = bizService.getRebate(id);
        ProductPriceHead productPriceHead = priceHeadService.getDefaultPriceHead(getUserObject().getOrg().getId());
        String specialOffFlag = "N";
        List<LovMember> lovMemberList = lovMemberService.getListByGroupCode("SPECIALOFF");
        for(LovMember lovMember:lovMemberList) {
        	if("02".equals(lovMember.getCode())) {
        		if(lovMember.getId().equals(entity.getSpecialOff())) {
        			specialOffFlag = "Y";
        		}
        	}
        }
        CustomInfo customInfo =new CustomInfo();
        if(entity.getBelongOperator()!=null) {
        	customInfo = customInfoService.getCustomInfo(entity.getBelongOperator());
        	productPriceHead.setClientId(entity.getBelongOperator());
        	productPriceHead.setClientName(customInfo.getCustomFullName());
        }
        if (productPriceHead != null) {
            model.addAttribute("priceTableId", productPriceHead.getId());
        } else {
            throw new AnneException("默认价格表不能为空！");
        }
        TabMain tabMainInfo = new TabMain();
        tabMainInfo.setInitAll(false);

        tabMainInfo.addTab("销售团队", "/team/list.html?businessType=Rebate&businessId=" + entity.getId());
        tabMainInfo.addTab("审批历史", "/standard/history.html?contrId=" + entity.getId());
        if ("Pending".equals(entity.getStatus()) || "Rejected".equals(entity.getStatus())) {
        	tabMainInfo.addTab("附件", "/common/attachment/attachment.html?businessId="+entity.getId()+"&businessType=SPECIAL_OFFER_FILE&docGroupCode=ATTACHMENTTYPEGROUP");
		}else{
			tabMainInfo.addTab("附件", "/common/attachment/attachment.html?businessId="+entity.getId()+"&businessType=SPECIAL_OFFER_FILE&docGroupCode=ATTACHMENTTYPEGROUP&unableAdd=true&unableDelete=true");
		}
        
        tabMainInfo.addTab("特价变更", "/rebate/change/changePage.html?rebateId=" + entity.getId());
//        if(hasPermission("P03SpecialPriceT2ChangePage")){  
//        }
        
        model.addAttribute("flag", getUserObject().isInner());
        model.addAttribute("tabMainInfo", tabMainInfo);
        model.addAttribute("entity", entity);
        model.addAttribute("specialOffFlag", specialOffFlag);
        
        Employee employee =  ((Employee)CacheData.getInstance().get(entity.getBusinessExecutive()));
        if(employee!=null){
    		model.addAttribute("employee",JSON.toJSONString(employee));
    	}
        if(productPriceHead!=null){
    		model.addAttribute("productPriceHead",JSON.toJSONString(productPriceHead));
    		model.addAttribute("productPriceHeadFlag","N");
    	}
        setProcessParam(model, request);
        return forward("add");
    }
    
    @NoRight
    @ResponseBody
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String edit_post(Rebate entity,HttpServletRequest request) {
    	entity.setUpdatedById(getUserObject().getEmployee().getId());
    	entity.setUpdatedAt(new Date());
    	if(!StringUtil.isNullOrEmpty(entity.getType())&&"分销".equals(entity.getType())) {
    		String[] specialOff = entity.getSpecialOff().split(",");
    		entity.setSpecialOff(specialOff[0]);
    	}
    	boolean newProcessTypeUpdateFlag = false;//判断流程中是否保存数据
    	String newProcessType = request.getParameter("newProcessType");
    	String entityCreatedById = entity.getCreatedById();
		String userId = getUserObject().getEmployee().getId();
    	if(!StringUtil.isNullOrEmpty(newProcessType)&&"Y".equals(newProcessType)
				&&((entityCreatedById.equals(userId)&&("Rejected".equals(entity.getStatus()))||hasPermission("P09SpecialChangEdit")))		
				) {
			newProcessTypeUpdateFlag = true; 
		}
    	ProcessForm form = ActionUtil.getProcessForm(request,getUserObject());
    	if(StringUtil.isNullOrEmpty(newProcessType)) {
    		bizService.updateRebate(entity,form, getUserObject(),newProcessType,newProcessTypeUpdateFlag);
    	}else if(newProcessTypeUpdateFlag = true){
    		bizService.updateRebate(entity,form, getUserObject(),newProcessType,newProcessTypeUpdateFlag);
    	}
        return sendSuccessMessage();
    }

    @NoRight
    @ResponseBody
    @RequestMapping(value = "/startProcess", method = RequestMethod.POST)
    public String startProcess(String id, String no) {
        bizService.startApplyProcessRebate(getUserObject(), id, no);
        return sendSuccessMessage();
    }


    @NoRight
    @ResponseBody
    @RequestMapping(value = "/deleteRival")
    public String deleteRival(String id) {
        if (StringUtil.isEmpty(id)) {
            throw new AnneException("id is null");
        }
        bizService.delete(id, RebateRival.class);
        return sendSuccessMessage();
    }

    @NoRight
    @ResponseBody
    @RequestMapping(value = "/deleteLine")
    public String deleteLine(String id) {
        if (StringUtil.isEmpty(id)) {
            throw new AnneException("id is null");
        }
        bizService.delete(id, RebateLine.class);
        return sendSuccessMessage();
    }

    @NoRight
    @ResponseBody
    @RequestMapping(value = "/delete")
    public String delete(String id) {
        if (StringUtil.isNotEmpty(id)) {
            bizService.destoryRebateChange("Rebate", id, "status", ProcessConstants.PROCESS_STATUS_Destroyed);
        }
        return sendSuccessMessage();
    }

    @NoRight
    @ResponseBody
    @RequestMapping("/getBizs")
    public String getBizs(PageCondition condition, HttpServletRequest request) {
        ActionUtil.requestToCondition(condition, request);
        ActionUtil.doSearch(condition);
        String number = (String)condition.getConditionMap().get("search");
        if(!StringUtil.isNullOrEmpty(number)) {
        	condition.getFilterObject().addCondition("number", "=", number);
        }
        condition.getFilterObject().addCondition("createdOrgId", "=", getUserObject().getOrg().getId());
        condition.getFilterObject().addCondition("status", "=", "20");
        IPage p = bizoppService.query(condition);
        return sendSuccessMessage(p.getList());
    }

    /**
     * 查询产品型号可以关联的特价单
     * 特价单中存在改产品型号的可转特价数量
     * @param condition
     * @param request
     * @return
     */
    @NoRight
    @ResponseBody
    @RequestMapping("/getBizsWithProduct")
    public String getBizsWithProduct(PageCondition condition, HttpServletRequest request) {
        ActionUtil.requestToCondition(condition, request);

        UserObject userObject = getUserObject();
        condition.getFilterObject().addCondition("createdOrgId", "=", userObject.getOrg().getId());
        condition.getFilterObject().addCondition("status", "=", "20");

        String productModel = condition.getStringCondition("productModel");
        if (StringUtils.isEmpty(productModel)) {
            throw new AnneException("产品型号不能为空");
        }

        String rebateNo = condition.getStringCondition("rebateNo");
        if (StringUtils.isNotEmpty(rebateNo)) {
            Rebate rebate = this.bizService.getRebateByNo(rebateNo);
            if (rebate != null) {

                userObject = new UserObject(rebate.getCreatedById(), rebate.getCreatedPosId(), rebate.getCreatedOrgId());
                LovMember org = new LovMember();
                org.setId(rebate.getCreatedOrgId());
                userObject.setOrg(org);
                LovMember position = new LovMember();
                position.setId(rebate.getCreatedPosId());
                userObject.setPosition(position);
                Employee employee = new Employee();
                employee.setId(rebate.getCreatedById());
                userObject.setEmployee(employee);
            }
        }

        List<BusinessOpportunity> p = bizoppService.queryUsableBizsWithProduct(condition, userObject);
        return sendSuccessMessage(p);
    }

    @NoRight
    @ResponseBody
    @RequestMapping("/getBizById")
    public String getBizById(String bizId) {
        BusinessOpportunity bu = null;
        /*if ("KSTAR-SJ-XN01".equals(bizId)) {
            bu = new BusinessOpportunity("xuni");
        } else {
            bu = bizoppService.getBizOppEntity(bizId);
        }*/
        bu = bizoppService.getBizOppEntity(bizId);
        return sendSuccessMessage(bu);
    }

    @NoRight
    @ResponseBody
    @RequestMapping("/getBizMainInfo")
    public String getBizMainInfo(String bizNo) {
        BusinessOpportunity bu = bizoppService.getBusinessOpportunityByNumberAndOrg(bizNo,getUserObject().getEmployee().getNo());
        if (bu != null) {
            return sendSuccessMessage(bu.getId());
        }
        throw new AnneException("无权访问该商机信息");
    }
    
    @NoRight
	@RequestMapping(value = "/exportRebateLine")
	public void exportRebateLineFormLists(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String idsStr = request.getParameter("idsStr");
		String[] ids = idsStr.split(",");
		List<List<Object>> dataList = bizoppService.exportRebateLineFormLists(ids);
		ExcelUtil.exportExcel(response, dataList, "标准产品");
	}
    
    @NoRight
	@RequestMapping(value = "/export")
	public void export(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String idsStr = request.getParameter("idsStr");
		String[] ids = idsStr.split(",");
		List<List<Object>> dataList = bizoppService.export(ids);
		ExcelUtil.exportExcel(response, dataList, "非标准产品");
	}
    
    @NoRight
	@RequestMapping(value = "/exportData")
	public void exportData(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String idsStr = request.getParameter("idsStr");
		String[] ids = idsStr.split(",");
		List<List<Object>> dataList = bizoppService.exportData(ids);
		ExcelUtil.exportExcel(response, dataList, "分销产品");
	}

    @NoRight
   	@RequestMapping(value = "/chekcBiz",method=RequestMethod.POST)
   	public String chekcBiz(HttpServletRequest request,HttpServletResponse response) throws Exception{
    	String datas = request.getParameter("data");
    	String changeFlag = request.getParameter("changeFlag");
    	//String dataT3 = request.getParameter("dataT3");
    	String type = request.getParameter("type");
    	List<RebateLine> rebateLineList = JSON.parseArray(datas, RebateLine.class);
    	/*if(!StringUtil.isNullOrEmpty(dataT3)&&"常规".equals(type)) {
    		List<RebateLine> rebateLineListT3 = JSON.parseArray(dataT3, RebateLine.class);
    		rebateLineList.addAll(rebateLineListT3);
    	}*/
        String rebateNo = request.getParameter("rebateNo");
        String report = bizoppService.checkBiz(rebateNo, rebateLineList, type, changeFlag);
        if (StringUtils.isNotEmpty(report)&&StringUtils.isNotEmpty(changeFlag)&&"Y".equals(changeFlag)) {
            return sendSuccessMessage(report);
        }else if(StringUtils.isNotEmpty(report)) {
        	return sendErrorMessage(report);
        }
        return sendSuccessMessage();
   	}
}
