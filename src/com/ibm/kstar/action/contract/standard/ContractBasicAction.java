/**
 *
 */
package com.ibm.kstar.action.contract.standard;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.action.BaseFlowAction;
import com.ibm.kstar.action.ProcessForm;
import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.bizopp.IBizoppService;
import com.ibm.kstar.api.contract.IContrChangeService;
import com.ibm.kstar.api.contract.IContractService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.org.IOrgTeamService;
import com.ibm.kstar.api.price.IPriceHeadService;
import com.ibm.kstar.api.quot.IQuotService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.EmployeeObject;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.api.team.ITeamService;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.entity.bizopp.BusinessOpportunity;
import com.ibm.kstar.entity.contract.ContrChange;
import com.ibm.kstar.entity.contract.Contract;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.product.ProductPriceHead;
import com.ibm.kstar.entity.quot.KstarAftSale;
import com.ibm.kstar.entity.quot.KstarPrjLst;
import com.ibm.kstar.interceptor.system.permission.NoRight;
import com.ibm.kstar.service.IDemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.ExcelUtil;
import org.xsnake.web.utils.NumberToCN;
import org.xsnake.web.utils.StringUtil;
import org.xsnake.xflow.api.IProcessService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ibm
 */
@Controller
@RequestMapping("/standard")
//@Scope("prototype")
public class ContractBasicAction extends BaseFlowAction {
    Logger logger = LoggerFactory.getLogger(getClass());

    private final static String CONTRACT_PROCESS_KEY = "contrStandardBasic";

    @Autowired
    private IContractService contractService;

    @Autowired
    private ILovMemberService lovMemberService;

    @Autowired
    private ICustomInfoService customerService;

    @Autowired
    private IBizoppService bizOppService;

    @Autowired
    private IPriceHeadService priceHeadService;
    @Autowired
    protected ITeamService teamService;
    @Autowired
    IQuotService quotService;
    @Autowired
    protected IOrgTeamService orgTeamService;
    @Autowired
    private IContrChangeService changeService;
    @Autowired
    IProcessService processService;


    @RequestMapping("/index")
    public String index(String id, Model model) {

        return forward("contractindex");
    }

    @NoRight
    @ResponseBody
    @RequestMapping("/page")
    public String page(PageCondition condition, HttpServletRequest request) {
        ActionUtil.requestToCondition(condition, request);

        ActionUtil.doSearch(condition);
        IPage p = contractService.query(condition);

        return sendSuccessMessage(p);
    }

    @NoRight
    @RequestMapping("/tabs")
    public String tabs(String id, String contrCode, String typ, Model model, HttpServletRequest request) {
        model.addAttribute("id", id);
        model.addAttribute("typ", typ);
        model.addAttribute("bizType", IConstants.CONTR_STAND);
        model.addAttribute("contrCode", contrCode);
        Contract contract = contractService.get(id);
        BusinessOpportunity biz = bizOppService.getBizOppEntity(contract.getProjNo());
        CustomInfo cust = customerService.getCustomInfo(contract.getCustCode());
        ProductPriceHead priceTable = priceHeadService.queryLpcById(contract.getPricNo());
        Contract frameContr = contractService.get(contract.getFrameNo());
        CustomInfo erpCust = customerService.getCustomInfo(contract.getErpOrderCustId());
        Employee ordEmp = ((Employee) CacheData.getInstance().get(contract.getOrderer()));
        LovMember ordPosi = lovMemberService.get(contract.getOrderPosId());
        LovMember ordOrg = lovMemberService.get(contract.getOrderOrgId());
        if (ordEmp != null && ordPosi != null && ordOrg != null) {
            EmployeeObject teamObj = new EmployeeObject(ordOrg, ordPosi, ordEmp);
            if (teamObj != null) {
                model.addAttribute("teamObj", JSON.toJSONString(teamObj));
            }
        }
        if (biz != null) {
            model.addAttribute("project", JSON.toJSONString(biz));
        }
        if (cust != null) {
            model.addAttribute("customer", JSON.toJSONString(cust));
        }
        if (priceTable != null) {
            model.addAttribute("priceTable", JSON.toJSONString(priceTable));
            model.addAttribute("productPriceHead", priceTable);
        }
        if (frameContr != null) {
            model.addAttribute("frameContr", JSON.toJSONString(frameContr));
            model.addAttribute("contract", frameContr);
        }
        if (erpCust != null) {
            model.addAttribute("erpCustomer", JSON.toJSONString(erpCust));
        }
        // 是否国际合同标识
        String ordDivFlg = "Y";
        if (getUserObject().getOrg().getNamePath().contains(IConstants.CONTR_TYPE_X_ORG_STR)) {
            ordDivFlg = "N";
        }
        model.addAttribute("ordDivFlg", ordDivFlg);


        //更新特价审批标志
        quotService.updateCntSprvmrk(getUserObject(), id, contract.getOrg());
        String sprvmrkstatus = quotService.CheckSprvmrkStatus(id);
        //		model.addAttribute("sprvmrkstatus",sprvmrkstatus);

        String pricetableid = contract.getPricNo();

        // 表单按钮状态控制
        String editFlag = "N";
        String conSt = lovMemberService.get(contract.getContrStat()).getCode();

        //判断合同状态是否返回修改中，是否是当前处理人
        String constEditFlag = "Y";
        if (conSt.equals("06")) {
            constEditFlag = contractService.checkContrStat(id, getUserObject());
        } else {
            constEditFlag = "N";
        }

        //		if((contract.getQuotNo() == null || contract.getQuotNo().equalsIgnoreCase("") ) && (conSt.equalsIgnoreCase("01") || conSt.equalsIgnoreCase("06") || (hasPermission("P05ConCONTRACT_PRO_ENABLE_IN_APP_METHOD") && (conSt.equalsIgnoreCase("02") || conSt.equalsIgnoreCase("03") || conSt.equalsIgnoreCase("04"))))){  //01	新建 	06	返回修改中
        if ((conSt.equalsIgnoreCase("01") || "Y".equals(constEditFlag) || (hasPermission("P05ConCONTRACT_PRO_ENABLE_IN_APP_METHOD") &&
                (conSt.equalsIgnoreCase("02") || conSt.equalsIgnoreCase("03") || conSt.equalsIgnoreCase("04")
                )
        )
        )
                ) {  //01	新建 	06	返回修改中
            editFlag = "Y";
        }
        // 工程清单隐藏报价，单品总金额 列
        String showPriceFlg = "N";
        if (hasPermission("P05ConPRICE_COLUMN_ENABLE_METHOD")) {
            showPriceFlg = "Y";
        }
        // 工程清单隐藏 物料号 列
        String showMaterCodeFlg = "N";
        if (hasPermission("P05ConMaterialCode_COLUMN_ENABLE_METHOD")) {
            showMaterCodeFlg = "Y";
        }
        //工程清单核销按钮控制
        String verEditFlag = "N";
        String conLoanChkSt = "";
        LovMember lovLoanChkSt = lovMemberService.get(contract.getLoanChkFlowStat());

        if ((conSt.equalsIgnoreCase("07") || conSt.equalsIgnoreCase("08")) && (lovLoanChkSt == null || lovLoanChkSt.getCode().equalsIgnoreCase("01") || lovLoanChkSt.getCode().equalsIgnoreCase("02"))) {  //08	已签订商务已下单
            verEditFlag = "Y";
        }
        // 合同评审表单按钮状态控制
        String reviewEditFlag = "N";
        //03:待评审 14:初审完成 15待确认评审完成
        if (conSt.equalsIgnoreCase("03") || conSt.equalsIgnoreCase("14") || conSt.equalsIgnoreCase("15")) {
            reviewEditFlag = "Y";
        }
        // 合同文档管理信息维护表单按钮状态控制
        String archiveEditFlag = "N";
        if (conSt.equalsIgnoreCase("07") || conSt.equalsIgnoreCase("08")) {  //07	已签订待商务下单  08	已签订商务已下单
            archiveEditFlag = "Y";
        }
        // 回款计划表单按钮状态控制
        String payPlanEditFlag = "N";
        if (conSt.equalsIgnoreCase("01") || "Y".equals(constEditFlag)) {  //01	新建 	06	返回修改中
            payPlanEditFlag = "Y";
        } else if (hasPermission("P05Con_PaymentEdit_AfterSign")) {
            payPlanEditFlag = "Y";
        }
        // 附件删除按钮权限控制
        boolean unableDelete = true;
        if (conSt.equalsIgnoreCase("01")) {
            unableDelete = false;
        } else if (hasPermission("P05Con_FileDelete_Enable_Method") && (conSt.equalsIgnoreCase("01"))) {
            unableDelete = false;
        } else {
            unableDelete = true;
        }

        String isPrint = "N";
        if (conSt.equalsIgnoreCase("07") || conSt.equalsIgnoreCase("08") || conSt.equalsIgnoreCase("10") || conSt.equalsIgnoreCase("12") || conSt.equalsIgnoreCase("13")) {
            isPrint = "Y";
        }
        model.addAttribute("isPrint", isPrint);

        TabMain tabMainInfo = new TabMain();
        //		tabMainInfo.setInitAll(true);
        tabMainInfo.setInitAll(false);
        if (hasPermission("P05ConT1ProjListTab")) {
            tabMainInfo.addTab("工程清单", "/standard/prjlst.html?contrId=" + id + "&typ=" + typ + "&pricetableid=" + pricetableid + "&sprvmrkstatus=" + sprvmrkstatus + "&editFlag=" + editFlag + "&verEditFlag=" + verEditFlag + "&showPriceFlg=" + showPriceFlg + "&showMaterCodeFlg=" + showMaterCodeFlg);
        }
        if (hasPermission("P05ConT2ProjForm")) {
            tabMainInfo.addTab("工程界面", "/quot/prjpages.html?qid=" + id + "&typ=" + typ);
        }
        //		tabMainInfo.addTab("工程界面", "/standard/prjpage.html?contrId="+id+"&typ="+typ+"&editFlag="+editFlag);
        //		tabMainInfo.addTab("附件", "/standard/att.html?contrId="+id);
        if (hasPermission("P05ConT3FileTab")) {
            tabMainInfo.addTab("附件", "/common/attachment/attachment.html?businessId=" + id + "&businessType=CONTR_STAND&docGroupCode=CONTRACTDOCTYPE&unableDelete=" + unableDelete);
            //		tabMainInfo.addTab("团队成员", "/standard/mem.html?contrId="+id);
        }
        if (hasPermission("P05ConT4TeamTab")) {
            tabMainInfo.addTab("团队成员", "/team/list.html?businessType=CONTR_STAND&businessId=" + id);
        }
        if (hasPermission("P05ConT5OrgList")) {
            tabMainInfo.addTab("组织列表", "/orgTeam/list.html?businessType=CONTR_STAND&businessId=" + id);
        }
        if (hasPermission("P05ConT6ShipAddress")) {
            tabMainInfo.addTab("发货地址", "/standard/addr.html?contrId=" + id + "&typ=" + typ + "&editFlag=" + editFlag);
        }
        if (hasPermission("P05ConT7ProReview")) {
            tabMainInfo.addTab("合同评审", "/standard/review.html?contrId=" + id + "&typ=" + typ + "&editFlag=" + editFlag + "&reviewEditFlag=" + reviewEditFlag);
        }
        if (hasPermission("P05ConT8ReviewHistory")) {
            tabMainInfo.addTab("审批历史", "/standard/history.html?contrId=" + id);
        }
        if (hasPermission("P05ConT9PaymentTab")) {
            tabMainInfo.addTab("回款规则", "/standard/pay.html?contrId=" + id + "&payPlanEditFlag=" + payPlanEditFlag);
        }
        if (hasPermission("P05ConT10ConDocInfoTab")) {
            tabMainInfo.addTab("文档管理信息", "/standard/archive.html?contrId=" + id + "&archiveEditFlag=" + archiveEditFlag);
        }
        if (hasPermission("P05ConT11ConChangeTab")) {
            tabMainInfo.addTab("合同变更", "/standard/changePage.html?contrId=" + id);
        }
        if (hasPermission("P05ConT12ConOrderTab")) {
            tabMainInfo.addTab("订单", "/order/list.html?contrId=" + id);
        }
        model.addAttribute("tabMainInfo", tabMainInfo);
        model.addAttribute("org", JSON.toJSONString(lovMemberService.get(contract.getOrg())));
        model.addAttribute("contr", contract);

        Map<String, String> map = new HashMap<String, String>();
        map.put("constEditFlag", constEditFlag);
        getButtonStatus(map, contract);
        model.addAttribute("btnStatus", map);
        model.addAttribute("sourceType", IConstants.ORDER_SOURCE_CONTRACT);
        setProcessParam(model, request);
        return forward("tabs");
    }

    public void getButtonStatus(Map<String, String> map, Contract contr) {
        /*CONTRACTSTATUS ---合同状态						CONTRACTREVIEWSTATUS  ---合同评审状态
         01	新建                          											02	审批中
		02	待初审                        										03	已审批                            
		03	待评审                        										01	未启动                            
		04	待合同书评审                  										04	已驳回                         
		05	待签订                  
		06	返回修改中              
		07	已签订待商务下单        
		08	已签订商务已下单        
		09	已废弃 
		10	已签订
		11	取消 */
        String conSt = lovMemberService.get(contr.getContrStat()).getCode();
        String trialSt = lovMemberService.get(contr.getTrialStat()).getCode();
        String revSt = lovMemberService.get(contr.getReviewStat()).getCode();
        String finSt = lovMemberService.get(contr.getFinalReviewStat()).getCode();
        String constEditFlag = map.get("constEditFlag");
        String isValid = contr.getIsValid();

        if (isValid != null && isValid.equalsIgnoreCase("0")) {
            map.put("saveBtn", "0");
            map.put("trialBtn", "0");
            map.put("finBtn", "0");
            map.put("signBtn", "0");
            map.put("reviseBtn", "0");
            map.put("chgBtn", "0");
            map.put("genOrderBtn", "0");
            map.put("disContrBtn", "0");

        } else {
            if ((conSt.equalsIgnoreCase("01") && trialSt.equalsIgnoreCase("01")) || "Y".equals(constEditFlag)) {
                map.put("saveBtn", "1");
            } else {
                map.put("saveBtn", "0");
            }
            if ((conSt.equalsIgnoreCase("01") && trialSt.equalsIgnoreCase("01"))) {
                map.put("trialBtn", "1");
            } else {
                map.put("trialBtn", "0");
            }
            if ((conSt.equalsIgnoreCase("04") && revSt.equalsIgnoreCase("03") && finSt.equalsIgnoreCase("01"))) {
                map.put("finBtn", "1");
            } else {
                map.put("finBtn", "0");
            }
            if (conSt.equalsIgnoreCase("05") && finSt.equalsIgnoreCase("03")) {
                map.put("signBtn", "1");
            } else {
                map.put("signBtn", "0");
            }
            if ((!(conSt.equalsIgnoreCase("10") || conSt.equalsIgnoreCase("07") || conSt.equalsIgnoreCase("08"))) && "1".equals(isValid)) {
                map.put("reviseBtn", "1");
            } else {
                map.put("reviseBtn", "0");
            }
            if (conSt.equalsIgnoreCase("10") || conSt.equalsIgnoreCase("07") || conSt.equalsIgnoreCase("08")) {
                map.put("chgBtn", "1");
            } else {
                map.put("chgBtn", "0");
            }
            if ((conSt.equalsIgnoreCase("07") || conSt.equalsIgnoreCase("08"))) {
                map.put("genOrderBtn", "1");
            } else {
                map.put("genOrderBtn", "0");
            }
            if ((conSt.equalsIgnoreCase("01") || "Y".equals(constEditFlag))) {
                map.put("disContrBtn", "1");
            } else {
                map.put("disContrBtn", "0");
            }
        }

    }

    @Autowired
    IDemoService demoService;

    @NoRight
    @ResponseBody
    @RequestMapping(value = "/tabs", method = RequestMethod.POST)
    public String editContr(Contract contract, HttpServletRequest request) {
        CustomInfo cust = customerService.getCustomInfo(contract.getErpOrderCustId());
        if (cust.getErpCode() != null) {
            if (cust.getErpCode().indexOf("KSTAR") != -1) {
                throw new AnneException("未通过ERP审核的客户不允许下单！");
            }
        }
        ProcessForm form = ActionUtil.getProcessForm(request, getUserObject());
        String func = request.getParameter("actFunction");
        String typ = request.getParameter("typ");
        if ("saveOkc".equalsIgnoreCase(func)) {
            updateContr(contract, null);
        } else if ("startTrialProcess".equalsIgnoreCase(func)) {
            startContractTrialProcess(typ, contract);
        } else {
            demoService.todoProcess(form);
        }
        return sendSuccessMessage();
    }

    public String updateContr(Contract contract, ProcessForm form) {

        String flg = checkSaveContract(contract);
        if (flg.equalsIgnoreCase("Y")) {
            contract.setUpdatedById(getUserObject().getEmployee().getId());
            contract.setUpdatedAt(new Date());
            contractService.update(contract, form);
        }
        return sendSuccessMessage();
    }

    public String checkSaveContract(Contract contract) {

        String flg = "Y";

        LovMember typeLov = lovMemberService.get(contract.getContrType());
        if (typeLov.getCode().equalsIgnoreCase(IConstants.CONTR_STAND_0103) && contract.getFrameNo().equalsIgnoreCase("")) {
            flg = "N";
            throw new AnneException("合同类型为框架下合同时，框架协议编号必填！");
        }
        // 营销中心为 国内数据中心 时 而且 合同类型为 标准销售合同 和  框架下合同 时      项目（商机）名称必填
        //		if(contract.getMarketDept().equalsIgnoreCase(IConstants.CONTR_ORG_GN_B1_STR) && (typeLov.getCode().equalsIgnoreCase(IConstants.CONTR_STAND_0101) || typeLov.getCode().equalsIgnoreCase(IConstants.CONTR_STAND_0103))){
        //			if(contract.getProjNo()==null || StringUtil.isEmpty(contract.getProjNo())) {
        //				flg="N";
        //				throw new AnneException("项目名称必填！");
        //			}
        //		}
        if (!typeLov.getCode().contains(IConstants.CONTR_STAND_02)) {
            //国际合同时价格表名称不必填
            if (contract.getPricNo() == null || StringUtil.isEmpty(contract.getPricNo())) {
                flg = "N";
                throw new AnneException("价格表必填！");
            }

            Date delivDate = contract.getDelivDate(); // 要货日期
            Date now = new Date();
            if (delivDate == null) {
                throw new AnneException("要货日期必填");
            }
            if (delivDate.before(now)) {
                flg = "N";
                throw new AnneException("要货日期要在今天之后");
            }

            String strError = "";
            String f = "0";
            if (contract.getIsWholeSet() == null) {
                f = "1";
                strError += "是否成套";
            }
            if (contract.getIsConfList() == null) {
                f = "1";
                strError += "," + "是否有配置清单";
            }
            if (contract.getIsDelivHome() == null) {
                f = "1";
                strError += "," + "是否送货上门";
            }
            if (contract.getIsUnload() == null) {
                f = "1";
                strError += "," + "是否需卸货";
            }
            if (contract.getIsHomeInstall() == null) {
                f = "1";
                strError += "," + "是否上门安装";
            }
            if (contract.getIsAux() == null) {
                f = "1";
                strError += "," + "是否提供安装辅材";
            }
            if (contract.getOrderer() == null || contract.getOrderPosId() == null) {
                f = "1";
                strError += "," + " 下单商务专员 ";
            }

            //			checkBusiEntity(contract.getId() );

            if (f.equalsIgnoreCase("1")) {
                strError += "不能为空！";
                flg = "N";
                throw new AnneException(strError);
            }

        }
        return flg;
    }

    @RequestMapping("/add")
    public String add(String genContrBy, Model model) {
        String contrNo = contractService.getContractNumber();
        Contract contract = new Contract();
        contract.setContrNo(contrNo);
        contract.setContrVer("1");
        contract.setCreator(getUserObject().getEmployee().getId());
        LovMember currencyLov = lovMemberService.getLovMemberByCode("CURRENCY", "CNY");
        contract.setCurrency(currencyLov.getId());
        //		LovMember lov =  ((LovMember)CacheData.getInstance().get(payStat));
        LovMember lov = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "01");
        contract.setReviewStat(lov.getId());
        contract.setTrialStat(lov.getId());
        contract.setFinalReviewStat(lov.getId());
        contract.setLoanChkFlowStat(lov.getId());
        LovMember statLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "01");
        contract.setContrStat(statLov.getId());
        //		contract.setOrg(getUserObject().getOrg().getId());
        LovMember payLov = lovMemberService.getLovMemberByCode("CONTRACTPAYSTAT", "01");
        contract.setPayStat(payLov.getId());
        contract.setIsValid("1");
        contract.setCreateTime(new Date());
        LovMember entityLov = lovMemberService.getLovMemberByCode("OPERATION_UNIT", "101");
        contract.setBussEnity(entityLov.getId());
        LovMember orgLov = lovMemberService.get(getUserObject().getOrg().getId());
        contract.setOrg(orgLov.getId());
        LovMember makDepLov = lovMemberService.getLovMemberByCode("ORG", lovMemberService.getSaleCenter(getUserObject().getOrg().getId()));
        if (makDepLov != null) {
            contract.setMarketDept(makDepLov.getId());
            if (makDepLov.getCode().equalsIgnoreCase(IConstants.CONTR_ORG_GN_B1_STR)) {
                contract.setContrType(lovMemberService.getLovMemberByCode("CONTRACTTYPE", IConstants.CONTR_STAND_0103).getId());   //CONTR_STAND-0103	框架下合同
            }
        } else {
            throw new AnneException("未找到您所属的营销部门，请联系管理员！");
        }
        model.addAttribute("contr", contract);
        //		model.addAttribute("org",JSON.toJSONString(lovMemberService.get(getUserObject().getOrg().getId())));

        // 是否国际合同标识
        String ordDivFlg = "Y";
        if (getUserObject().getOrg().getNamePath().contains(IConstants.CONTR_TYPE_X_ORG_STR)) {
            ordDivFlg = "N";
        }
        model.addAttribute("ordDivFlg", ordDivFlg);
        model.addAttribute("genContrBy", genContrBy);

        return forward("contract");
    }

    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(Contract contract, HttpServletRequest request) {
        CustomInfo cust = customerService.getCustomInfo(contract.getErpOrderCustId());
        if (cust.getErpCode() != null) {
            if (cust.getErpCode().indexOf("KSTAR") != -1) {
                throw new AnneException("未通过ERP审核的客户不允许下单！");
            }
        }
        String flg = checkSaveContract(contract);
        if (flg.equalsIgnoreCase("Y")) {

            contract.setCreateTime(new Date());
            contract.setCustLinkId(getUserObject().getEmployee().getId());
            contract.setCreatedAt(new Date());
            contract.setCreatedById(getUserObject().getEmployee().getId());
            contract.setCreatedPosId(getUserObject().getPosition().getId());
            contract.setCreatedOrgId(getUserObject().getOrg().getId());
            contractService.save(contract, getUserObject());
        }
        return sendSuccessMessage(contract.getId());
    }
    
    @ResponseBody
	@RequestMapping(value = "/viewtest")
	public String viewtest(String id) { 
		return sendSuccessMessage(contractService.get(id));
	}

    @RequestMapping("/edit")
    public String edit(String id, Model model) {
        Contract contract = contractService.get(id);
        BusinessOpportunity biz = bizOppService.getBizOppEntity(contract.getProjNo());
        CustomInfo cust = customerService.getCustomInfo(contract.getCustCode());
        ProductPriceHead priceTable = priceHeadService.queryLpcById(contract.getPricNo());
        Contract frameContr = contractService.get(contract.getFrameNo());
        model.addAttribute("contr", contract);
        model.addAttribute("org", JSON.toJSONString(lovMemberService.get(contract.getOrg())));
        CustomInfo erpCust = customerService.getCustomInfo(contract.getErpOrderCustId());
        Employee ordEmp = ((Employee) CacheData.getInstance().get(contract.getOrderer()));
        LovMember ordPosi = lovMemberService.get(contract.getOrderPosId());
        LovMember ordOrg = lovMemberService.get(contract.getOrderOrgId());
        if (ordEmp != null && ordPosi != null && ordOrg != null) {
            EmployeeObject teamObj = new EmployeeObject(ordOrg, ordPosi, ordEmp);
            if (teamObj != null) {
                model.addAttribute("teamObj", JSON.toJSONString(teamObj));
            }
        }

        if (biz != null) {
            model.addAttribute("project", JSON.toJSONString(biz));
        }
        if (cust != null) {
            model.addAttribute("customer", JSON.toJSONString(cust));
        }
        if (priceTable != null) {
            model.addAttribute("priceTable", JSON.toJSONString(priceTable));
        }
        if (frameContr != null) {
            model.addAttribute("frameContr", JSON.toJSONString(frameContr));
        }
        if (erpCust != null) {
            model.addAttribute("erpCustomer", JSON.toJSONString(erpCust));
        }
        return forward("contract");
    }

    @ResponseBody
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String edit(Contract contract) {
        String flg = checkSaveContract(contract);
        CustomInfo cust = customerService.getCustomInfo(contract.getErpOrderCustId());
        if (cust.getErpCode() != null) {
            if (cust.getErpCode().indexOf("KSTAR") != -1) {
                throw new AnneException("未通过ERP审核的客户不允许下单！");
            }
        }
        if (flg.equalsIgnoreCase("Y")) {
            contract.setUpdatedById(getUserObject().getEmployee().getId());
            contract.setUpdatedAt(new Date());
            contractService.update(contract, null);
        }
        return sendSuccessMessage();
    }

    @ResponseBody
    @RequestMapping(value = "/updateArchiveInfo", method = RequestMethod.POST)
    public String updateArchiveInfo(Contract contract) {
        contract.setUpdatedById(getUserObject().getEmployee().getId());
        contract.setUpdatedAt(new Date());
        contractService.update(contract, null);
        return sendSuccessMessage();
    }


    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(String id) {
        Contract contr = contractService.get(id);
        if (!lovMemberService.get(contr.getContrStat()).getCode().equalsIgnoreCase("01")) {
            throw new AnneException("合同不为新建状态下无法删除");
        }

        contractService.delete(id);
        return sendSuccessMessage();
    }

    @RequestMapping(value = "/exportStdContrs")
    public void exportStdContracts(PageCondition condition, HttpServletRequest request, HttpServletResponse response) {
        ActionUtil.requestToCondition(condition, request);
        List<List<Object>> dataList = contractService.exportContractsHead(condition);
        ExcelUtil.exportExcel(response, dataList, "合同列表");
    }

    /**
     * 合同导出
     *
     * @param condition
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/stdContrExport")
    //	public void exportStdContractLists( @RequestParam("ids[]") String[] ids, HttpServletRequest request,HttpServletResponse response) throws Exception{
    public void exportStdContractLists(PageCondition condition, HttpServletRequest request, HttpServletResponse response) throws Exception {

        ActionUtil.requestToCondition(condition, request);

        ActionUtil.doSearch(condition);

        String idsStr = request.getParameter("idsStr");
        String typ = request.getParameter("typ");
        String[] ids = idsStr.split(",");

        List<List<Object>> dataList = contractService.exportSelectedContrLists(condition, getUserObject(), ids, typ);
        ExcelUtil.exportExcel(response, dataList, "合同列表");
    }

    /**
     * 合同产品明细导出
     *
     * @param condition
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/reportExport")
    //	public void exportStdContractLists( @RequestParam("ids[]") String[] ids, HttpServletRequest request,HttpServletResponse response) throws Exception{
    public void reportExportContractLists(PageCondition condition, HttpServletRequest request, HttpServletResponse response) throws Exception {

        ActionUtil.requestToCondition(condition, request);

        ActionUtil.doSearch(condition);

        String idsStr = request.getParameter("idsStr");
        String typ = request.getParameter("typ");
        String[] ids = idsStr.split(",");

        List<List<Object>> dataList = contractService.reportExportContrLists(condition, getUserObject(), ids, typ);
        ExcelUtil.exportExcel(response, dataList, "合同产品明细");
    }
    
    @RequestMapping("/att")
    public String atts(String contrId, Model model) {
        model.addAttribute("contrId", contrId);
        return forward("attachment/attachment");
    }

    //	@ResponseBody
    //	@RequestMapping(value = "/attPage")
    //	public String pageAtt(PageCondition condition, HttpServletRequest request) {
    //		ActionUtil.requestToCondition(condition, request);
    //		condition.setCondition("id", request.getParameter("id"));
    //		IPage p = contractService.queryAtt(condition);
    //
    //		return sendSuccessMessage(p);
    //	}

    //	@ResponseBody
    //	@RequestMapping(value = "/reviewPage")
    //	public String reviewPage(String processInstanceId, PageCondition condition, HttpServletRequest request) throws Exception {
    //		ActionUtil.requestToCondition(condition, request);
    //
    //		IPage p = WorkflowService.getHistoryById(processInstanceId, condition);
    //
    //		return sendSuccessMessage(p);
    //	}
    //
    //	/**
    //	 * 启动流程
    //	 *
    //	 * @param contract
    //	 * @return
    //	 * @throws IOException
    //	 *
    //	 */
    //	@RequestMapping(value = "/startFlow")
    //	public String startflow(String id) throws IOException {
    //
    //		List<Map<String, Object>> variables = new ArrayList<Map<String, Object>>();
    //
    //		Map<String, Object> map1 = new HashMap<String, Object>();
    //		map1.put("name", "preFlag");
    //		map1.put("value", "1");
    //
    //		Map<String, Object> map2 = new HashMap<String, Object>();
    //		map2.put("name", "serverFlag");
    //		map2.put("value", "1");
    //
    //		Map<String, Object> map3 = new HashMap<String, Object>();
    //		map3.put("name", "presale");
    //		map3.put("value", "sales");
    //
    //		Map<String, Object> map4 = new HashMap<String, Object>();
    //		map4.put("name", "server");
    //		map4.put("value", "marketing");
    //
    //
    //		variables.add(map1);
    //		variables.add(map2);
    //		variables.add(map3);
    //		variables.add(map4);
    //
    //		String processInstanceId = WorkflowService.startWorkflow(CONTRACT_PROCESS_KEY, id, variables);
    //
    //		if (processInstanceId != null) {
    //
    //			Contract contract = contractService.get(id);
    //			contract.setProcessInstanceId(processInstanceId);
    //
    //			contract.setCreator("admin");
    //
    //			// contractService.update(contract);
    //
    //		}
    //
    //		return sendSuccessMessage("成功启动合同流程,流程ID是" + processInstanceId);
    //	}
    //
    //	/**
    //	 * 任务列表
    //	 *
    //	 * @param leave
    //	 * @throws Exception
    //	 */
    //	@ResponseBody
    //	@RequestMapping("/candidateTaskPage")
    //	public String candidateTaskPage(PageCondition condition, HttpServletRequest request) throws Exception {
    //
    //		ActionUtil.requestToCondition(condition, request);
    //
    //		List<Contract> contrList = new ArrayList<Contract>();
    //
    //		String userId = "gonzo";
    //
    //		PageImpl page = (PageImpl) WorkflowService.getCandidateTasks(CONTRACT_PROCESS_KEY, userId, condition);
    //
    //		if (page != null) {
    //
    //
    //		List<Task> tasks = (List<Task>) page.getList();
    //
    //		System.out.println("----------" + tasks.size());
    //
    //		for (Task task : tasks) {
    //			String processInstanceId = task.getProcessInstanceId();
    //			ProcessInstance processInstance = WorkflowService.getInstanceById(processInstanceId);
    //
    //			String businessKey = processInstance.getBusinessKey();
    //
    //			System.out.println("-----------------" + businessKey);
    //
    //			if (businessKey.equals("null")) {
    //				throw new AnneException("没有找到业务数据");
    //
    //			}
    //
    //			Contract contract = contractService.get(businessKey);
    //			contract.setTask(task);
    //			contract.setProcessInstance(processInstance);
    //			// contract.setProcessDefinition(WorkflowService.getDefinitionById(processInstance.getProcessDefinitionId()));
    //			contrList.add(contract);
    //		}
    //
    //		page.setList(contrList);
    //
    //	}
    //
    //		return sendSuccessMessage(page);
    //	}
    //
    //	/**
    //	 * 任务列表
    //	 *
    //	 * @param leave
    //	 * @throws Exception
    //	 */
    //	@ResponseBody
    //	@RequestMapping("/assigneeTaskPage")
    //	public String assigneeTaskPage(PageCondition condition, HttpServletRequest request) throws Exception {
    //
    //		ActionUtil.requestToCondition(condition, request);
    //
    //		List<Contract> contrList = new ArrayList<Contract>();
    //
    //		String userId = "fozzie";
    //
    //		PageImpl page = (PageImpl) WorkflowService.getAssigneeTasks(CONTRACT_PROCESS_KEY, userId, condition);
    //
    //		if (page != null) {
    //
    //			List<Task> tasks = (List<Task>) page.getList();
    //
    //			System.out.println("----------" + tasks.size());
    //
    //			for (Task task : tasks) {
    //				String processInstanceId = task.getProcessInstanceId();
    //				ProcessInstance processInstance = WorkflowService.getInstanceById(processInstanceId);
    //
    //				String businessKey = processInstance.getBusinessKey();
    //
    //				System.out.println("-----------------" + businessKey);
    //
    //				if (businessKey.equals("null")) {
    //					throw new AnneException("没有找到业务数据");
    //
    //				}
    //
    //				Contract contract = contractService.get(businessKey);
    //				contract.setTask(task);
    //				contract.setProcessInstance(processInstance);
    //				// contract.setProcessDefinition(WorkflowService.getDefinitionById(processInstance.getProcessDefinitionId()));
    //				contrList.add(contract);
    //			}
    //
    //			page.setList(contrList);
    //
    //		}
    //
    //		return sendSuccessMessage(page);
    //	}
    //
    //	/**
    //	 * 签收任务
    //	 *
    //	 * @throws Exception
    //	 */
    //	@RequestMapping("/claim")
    //	public String claim(String id, String taskId, Model model) throws Exception {
    //		model.addAttribute("id", id);
    //		model.addAttribute("taskId", taskId);
    //
    //		Log.error("-------------"+taskId);
    //
    //		String userId = "kermit";
    //
    //		if (taskId == null) {
    //			throw new AnneException("没有找到任务");
    //		}
    //
    //		int returnCode = WorkflowService.claim(taskId, userId);
    //		String message;
    //
    //		if (returnCode == 200) {
    //			message = "签收成功";
    //		} else {
    //			message = "签收失败";
    //		}
    //
    //		return sendSuccessMessage(message);
    //	}
    //
    //	/**
    //	 * 签收任务
    //	 *
    //	 * @throws Exception
    //	 */
    //	@RequestMapping("/handle")
    //	public String handle(String id, String taskId, String taskKey, String processInstanceId, Model model) throws Exception {
    //		model.addAttribute("id", id);
    //		model.addAttribute("taskId", taskId);
    //		model.addAttribute("taskKey", taskKey);
    //		model.addAttribute("processInstanceId", processInstanceId);
    //
    //		String userId = "gonzo";
    //
    //		if (taskId == null) {
    //			throw new AnneException("没有找到任务");
    //		}
    //
    //		if (id == null) {
    //			throw new AnneException("没有找到合同ID");
    //		}
    //
    //		Contract contract = contractService.get(id);
    //
    //		if (contract == null) {
    //			throw new AnneException("没有找到合同");
    //		}
    //
    //		model.addAttribute("contr", contract);
    //
    //		return forward("handle");
    //	}
    //
    //	/**
    //	 * 完成任务
    //	 *
    //	 * @param id
    //	 * @return
    //	 * @throws Exception
    //	 */
    //	@RequestMapping(value = "/complete")
    //	public String complete(String id, String taskId, Model model) throws Exception {
    //
    //		String userId = "kermit";
    //
    //		List<Map<String, Object>> variables = new ArrayList<Map<String, Object>>();
    //		Map<String, Object> variable = new HashMap<String, Object>();
    //		variable.put("name", "isPass");
    //		variable.put("value", "true");
    //		variables.add(variable);
    //
    //		int returnCode = WorkflowService.complete(taskId, userId, variables);
    //
    //		String message;
    //
    //		if (returnCode == 200) {
    //			message = "审批成功";
    //		} else {
    //			message = "审批失败";
    //		}
    //
    //		return sendSuccessMessage(message);
    //
    //	}
    //

    /**
     * 团队成员
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/mem")
    public String members(String contrId, Model model) {
        model.addAttribute("contrId", contrId);
        return forward("member/member");
    }

    /**
     * 团队成员
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/addr")
    public String address(String contrId, String typ, String editFlag, Model model) {
        model.addAttribute("typ", typ);
        model.addAttribute("contrId", contrId);
        model.addAttribute("editFlag", editFlag);
        return forward("shipaddr/address");
    }

    /**
     * 合同评审
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/review")
    public String review(String contrId, String typ, String editFlag, String reviewEditFlag, Model model) {

        LovMember CONTR_STAND_0103 = lovMemberService.getLovMemberByCode("CONTRACTTYPE", IConstants.CONTR_STAND_0103);
        model.addAttribute("CONTR_STAND_0103", CONTR_STAND_0103);
        model.addAttribute("contrId", contrId);
        model.addAttribute("typ", typ);
        model.addAttribute("editFlag", editFlag);
        model.addAttribute("reviewEditFlag", reviewEditFlag);
        return forward("review/review");
    }

    /**
     * 回款计划
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/pay")
    public String pay(String contrId, String payPlanEditFlag, Model model) {
        model.addAttribute("contrId", contrId);
        model.addAttribute("payPlanEditFlag", payPlanEditFlag);
        return forward("pay/pay");
    }

    /**
     * 合同书评审历史
     *
     * @param id
     * @return
     * @throws Exception
     */
    @NoRight
    @RequestMapping("/history")
    public String history(String contrId, Model model) {
        model.addAttribute("contrId", contrId);
        return forward("history/standardHistory");
    }

    /**
     * 工程清单
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/prjlst")
    public String prjlst(String contrId, String typ, String pricetableid, String sprvmrkstatus, String editFlag, String verEditFlag, String showPriceFlg, String showMaterCodeFlg, Model model) {
        //add new lovmem root
        //		String groupId = "PRJLSTPRDCAT";
        //		String rootexists = contractService.Checklovroot(contrId);
        //
        //		if(rootexists.equals("Y")){
        //			contractService.addlovroot(contrId, typ, groupId);
        //		}
        model.addAttribute("contrId", contrId);
        model.addAttribute("typ", typ);
        model.addAttribute("pricetableid", pricetableid);
        model.addAttribute("sprvmrkstatus", sprvmrkstatus);
        model.addAttribute("editFlag", editFlag);
        model.addAttribute("verEditFlag", verEditFlag);
        model.addAttribute("showPriceFlg", showPriceFlg);
        model.addAttribute("showMaterCodeFlg", showMaterCodeFlg);

        return forward("prjlst/prjlst");
    }

    /**
     * 工程界面
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/prjpage")
    public String prjpage(String contrId, String typ, String editFlag, Model model) {

        String cType = typ; //"0005";

        //generate aftsale
        KstarAftSale aftsale = contractService.getKstarAftSale(contrId, typ);
        if (aftsale == null) {
            contractService.saveAftsale(aftsale, contrId, cType);
        }


        TabMain tabMain = new TabMain();
        tabMain.setInitAll(true);

        //		tabMain.addTab("界面完成状态", "/inf.html");
        tabMain.addTab("界面完成状态", "/standard/prjpage/pginf.html?contrId=" + contrId + "&typ=" + typ);
        tabMain.addTab("项目基本信息", "/standard/prjpage/baseInf.html?contrId=" + contrId + "&typ=" + typ);
        tabMain.addTab("售前部分", "/standard/prjpage/befsale.html?contrId=" + contrId + "&typ=" + typ);
        tabMain.addTab("售后部分", "/standard/prjpage/aftsale.html?contrId=" + contrId + "&typ=" + typ);

        model.addAttribute("tabMain", tabMain);

        model.addAttribute("contrId", contrId);
        model.addAttribute("typ", typ);
        model.addAttribute("editFlag", editFlag);

        return forward("prjpage/prjpage");
    }

    /**
     * 合同文档管理信息
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/archive")
    public String archive(String contrId, String archiveEditFlag, Model model) {

        Contract contract = contractService.get(contrId);
        model.addAttribute("contr", contract);
        model.addAttribute("archiveEditFlag", archiveEditFlag);
        return forward("archinfo/archinfo");
    }

    /**
     * 合同变更
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/changePage")
    public String changePage(String id, String contrId, Model model) {
        model.addAttribute("contrId", contrId);
        return forward("change/change");
    }

    //	@ResponseBody
    //	@RequestMapping(value = "/startContractTrialProcess", method = RequestMethod.POST)
    //	public String startContractTrialProcess(String typ,Contract contract,HttpServletRequest request) throws Exception{
    public String startContractTrialProcess(String typ, Contract contract) {
        String contrId = contract.getId();
        if (!contractService.checkPayPlanListNull(contrId)) {
            throw new AnneException("请添加回款规则");
        }
        updateContr(contract, null);
        contractService.startContractTrialProcess(getUserObject(), contrId, typ);
        return sendSuccessMessage();
    }

    @ResponseBody
    @RequestMapping(value = "/startContractFinalProcess", method = RequestMethod.POST)
    public String startContractFinalProcess(String contrId, String typ, HttpServletRequest request) throws Exception {
        contractService.startContractFinalProcess(getUserObject(), contrId, typ);
        return sendSuccessMessage();
    }

    @ResponseBody
    @RequestMapping(value = "/startContrStdPriceProcess", method = RequestMethod.POST)
    public String startContrStdPriceProcess(String contrId, HttpServletRequest request) throws Exception {
        contractService.startContrStdPriceProcess(getUserObject(), contrId);
        return sendSuccessMessage();
    }

    @ResponseBody
    @RequestMapping(value = "/reviseContract", method = RequestMethod.POST)
    public String reviseContract(String contrId, HttpServletRequest request) throws Exception {
        Contract contract = contractService.reviseContract(getUserObject(), contrId);

        //权限控制
        //		teamService.addPosition(
        //				getUserObject().getPosition().getId(),
        //				getUserObject().getEmployee().getId(),
        //				IConstants.CONTR_STAND,
        //				contract.getId());
        //		orgTeamService.configPrimaryOrg(contract.getId(), IConstants.CONTR_STAND, getUserObject().getOrg().getId());

        return sendSuccessMessage();
    }

    /**
     * 合同签订
     *
     * @param contrId
     * @param typ
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/signUpContract", method = RequestMethod.POST)
    public String signUpContract(String contrId, String typ, HttpServletRequest request) throws Exception {
        contractService.signUpContract(getUserObject(), contrId, typ);
        return sendSuccessMessage();
    }

    /**
     * 合同变更
     *
     * @param id
     * @return
     * @throws Exception
     */
    @NoRight
    @ResponseBody
    @RequestMapping(value = "/change", method = RequestMethod.POST)
    public String changeContr(String contrId, HttpServletRequest request) {
        List<ContrChange> chgLst = changeService.queryChangeListByContrId(contrId);
        // 合同状态
        LovMember sigLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "10");// 10 已签订
        LovMember calLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "11");// 11 已取消
        String flag = "N";
        String chgNo = "";
        if (chgLst != null && chgLst.size() > 0) {
            for (ContrChange chg : chgLst) {
                if (chg.getChangeStat().equalsIgnoreCase(sigLov.getId()) || chg.getChangeStat().equalsIgnoreCase(calLov.getId())) {
                    continue;
                } else {
                    flag = "Y";
                    chgNo = chg.getChangeNo();
                    break;
                }
            }
            if (flag.equalsIgnoreCase("Y")) {
                throw new AnneException("已存在进行中的变更单：" + chgNo);
            }
        }
        return sendSuccessMessage();
    }

    /**
     * 检查 客户， 业务实体的状态
     * 合同下单验证是否全部生成订单
     *
     * @param id
     * @return
     * @throws Exception
     */
    @NoRight
    @ResponseBody
    @RequestMapping(value = "/checkCustBusiForOrder", method = RequestMethod.POST)
    public String checkCustBusiForOrder(String contrId, HttpServletRequest request) {

        checkBusiEntity(contrId);

        return sendSuccessMessage();
    }

    /**
     * 检查 客户是否已经进入ERP
     *
     * @param id
     * @return
     * @throws Exception
     */
    @NoRight
    @ResponseBody
    @RequestMapping(value = "/checkCustBusiERPForOrder", method = RequestMethod.POST)
    public String checkCustBusiERPForOrder(String contrId, HttpServletRequest request) {

        checkBusiERPEntity(contrId);

        return sendSuccessMessage();
    }

    public void checkBusiERPEntity(String contrId) {
        Contract contr = contractService.get(contrId);
        String custId = (contr.getErpOrderCustId() == null || contr.getErpOrderCustId().equalsIgnoreCase("")) ? contr.getCustCode() : contr.getErpOrderCustId();
        String flg = contractService.checkContrBusiEntityForOrder(custId, contr.getBussEnity());
        if (flg.equalsIgnoreCase("N")) {
            throw new AnneException("该客户对应的业务实体在ERP审核状态未完成");
        }
    }

    public void checkBusiEntity(String contrId) {
        Contract contr = contractService.get(contrId);
        String custId = (contr.getErpOrderCustId() == null || contr.getErpOrderCustId().equalsIgnoreCase("")) ? contr.getCustCode() : contr.getErpOrderCustId();
        String flg = contractService.checkContrBusiEntityForOrder(custId, contr.getBussEnity());
        if (flg.equalsIgnoreCase("N")) {
            throw new AnneException("该客户对应的业务实体在ERP审核状态未完成");
        }
        String flg1 = contractService.checkGenOrdLinesByContract(contrId);
        if (flg1.equalsIgnoreCase("N")) {
            throw new AnneException("合同中没有可生成订单的行");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/discardContr", method = RequestMethod.POST)
    public String discardContr(String contrId, String typ, HttpServletRequest request) throws Exception {
        contractService.discardContr(contrId, typ, getUserObject());
        return sendSuccessMessage();
    }


    @NoRight
    @ResponseBody
    @RequestMapping(value = "/testContr", method = RequestMethod.POST)
    public String testContrTime(HttpServletRequest request) throws Exception {

        long curTime = System.nanoTime();
        long curTime1 = System.currentTimeMillis();
        Date now = new Date(curTime);
        Date now1 = new Date(curTime);
        Date now2 = new Date();
        SimpleDateFormat dtFmt = new SimpleDateFormat("yyyyMMddHHmmssSSS");

        System.out.println(dtFmt.format(now));
        System.out.println(dtFmt.format(now2));
        return sendSuccessMessage();
    }


    @RequestMapping("/print")
    public String print(String id, String typ, Model model) {

        Contract contract = contractService.get(id);
        model.addAttribute("contract", contract);

        Condition condition = new Condition();
        condition.getFilterObject().addCondition("quotCode", "=", id);
        List<KstarPrjLst> list = contractService.getPrjlstList(condition);

        BigDecimal totalAmount = new BigDecimal(0.00);
        if (contract.getTotalAmtDesc() != null) {
            totalAmount = new BigDecimal(contract.getTotalAmtDesc());
        }

        model.addAttribute("toUpper", NumberToCN.number2CNMontrayUnit(totalAmount));
        model.addAttribute("list", list);

        return forward("print");
    }

    @NoRight
    @ResponseBody
    @RequestMapping("/changeHighRiskFlag")
    public String page(String id,String isHighRisk, HttpServletRequest request){
        // Contract contract = this.contractLoanService.get(id);
        this.contractService.updateHighRiskFlag(id, isHighRisk);
        return sendSuccessMessage();
    }
}
