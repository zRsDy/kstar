/**
 *
 */
package com.ibm.kstar.action.contract.change;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.api.bizopp.IBizoppService;
import com.ibm.kstar.api.contract.IContrChangeService;
import com.ibm.kstar.api.contract.IContractService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.order.IOrderService;
import com.ibm.kstar.api.price.IPriceHeadService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.bizopp.BusinessOpportunity;
import com.ibm.kstar.entity.contract.ContrChange;
import com.ibm.kstar.entity.contract.Contract;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.order.vo.OrderQuantityVo;
import com.ibm.kstar.entity.product.ProductPriceHead;
import com.ibm.kstar.entity.quot.KstarPrjLst;
import com.ibm.kstar.interceptor.system.permission.NoRight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.ExcelUtil;
import org.xsnake.web.utils.StringUtil;
import org.xsnake.xflow.api.IProcessService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ibm
 */ 
@Controller
@RequestMapping("/change")
//@Scope("prototype")
public class ContractChangAction extends BaseAction {
    Logger logger = LoggerFactory.getLogger(getClass());

    private final static String CONTRACT_PROCESS_KEY = "contrReview";

    @Autowired
    private IContractService contractService;

    @Autowired
    private IContrChangeService changeService;

    @Autowired
    private ILovMemberService lovMemberService;
    @Autowired
    private ICustomInfoService customerService;

    @Autowired
    private IBizoppService bizOppService;

    @Autowired
    private IPriceHeadService priceHeadService;

    @Autowired
    IProcessService processService;
    @Autowired
    IOrderService orderService;


    @RequestMapping("/contractchange")
    public String index(String id, String contrId, Model model) {
        model.addAttribute("contrId", contrId);
        return forward("contractchange");
    }

    @NoRight
    @ResponseBody
    @RequestMapping(value = "/page")
    public String page(PageCondition condition, HttpServletRequest request) {
        ActionUtil.requestToCondition(condition, request);
        //		condition.setCondition("contrId", request.getParameter("contrId"));
        //		condition.getFilterObject().addCondition("deleteFlag", "eq", "N");

        ActionUtil.doSearch(condition);
        String contrId = request.getParameter("contrId"); // condition.getStringCondition("contrId");
        if (contrId != null && contrId != "") {
            condition.getFilterObject().addCondition("contrId", "eq", contrId);
        }

        String searchKey = condition.getStringCondition("searchKey");
        if (!StringUtil.isNullOrEmpty(searchKey)) {
            condition.getFilterObject().addOrCondition("changeNo", "like", "%" + searchKey + "%");
            condition.getFilterObject().addOrCondition("contrNo", "like", "%" + searchKey + "%");
            condition.getFilterObject().addOrCondition("contrName", "like", "%" + searchKey + "%");
        }
        IPage p = changeService.query(condition);

        return sendSuccessMessage(p);
    }

    @RequestMapping("/add")
    public String add(String contrId, Model model) {
        //		LovMember signLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "02");

        Contract contr = contractService.get(contrId);
        //		if(!contr.getContrStat().equalsIgnoreCase(signLov.getId())){
        //			throw new AnneException("合同当前状态为："+contr.getContrStatName()+" 无法变更！");
        //		}
        String chgnum = changeService.getContracChangetNumber();
        ContrChange contrChg = new ContrChange();
        contrChg.setChangeNo(chgnum);
        contrChg.setContrId(contr.getId());
        contrChg.setContrName(contr.getContrName());
        contrChg.setContrNo(contr.getContrNo());
        contrChg.setProjNo(contr.getProjNo());
        contrChg.setProjName(contr.getProjName());
        contrChg.setCustCode(contr.getCustCode());
        contrChg.setCustName(contr.getCustName());
        contrChg.setPayWay(contr.getPayWay());
        //		contrChg.setPricNo();
        contrChg.setPricTable(contr.getPricTable());
        contrChg.setTotalAmt(contr.getTotalAmt());
        contrChg.setOrg(contr.getOrg());
        contrChg.setBussEnity(contr.getBussEnity());
        contrChg.setDelivDate(contr.getDelivDate());
        contrChg.setCreateTime(new Date());
        contrChg.setCurrency(contr.getCurrency());
        contrChg.setIsSealFirst(contr.getIsSealFirst());

        contrChg.setCreator(getUserObject().getEmployee().getId());
        //		LovMember lov =  ((LovMember)CacheData.getInstance().get(payStat));
        LovMember lov = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "01");
        contrChg.setTrialStat(lov.getId());
        contrChg.setReviewStat(lov.getId());
        //		contract.setFinalReviewStat(lov.getId());
        LovMember statLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "01");
        contrChg.setChangeStat(statLov.getId());
        //		contract.setOrg(getUserObject().getOrg().getId());
        contrChg.setIsValid("1");
        LovMember orgLov = lovMemberService.get(getUserObject().getOrg().getId());
        contrChg.setOrg(orgLov.getId());


        BusinessOpportunity biz = bizOppService.getBizOppEntity(contrChg.getProjNo());
        CustomInfo cust = customerService.getCustomInfo(contrChg.getCustCode());
        ProductPriceHead priceTable = priceHeadService.queryLpcById(contrChg.getPricNo());

        if (biz != null) {
            model.addAttribute("project", JSON.toJSONString(biz));
        }
        if (cust != null) {
            model.addAttribute("customer", JSON.toJSONString(cust));
        }
        if (priceTable != null) {
            model.addAttribute("priceTable", JSON.toJSONString(priceTable));
        }
        model.addAttribute("contrChg", contrChg);
        //		model.addAttribute("org",JSON.toJSONString(lovMemberService.get(getUserObject().getOrg().getId())));

        return forward("change");
    }

    @NoRight
    @RequestMapping("/tabs")
    public String tabs(String id, String changeNo, String typ, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("typ", typ);
        //		model.addAttribute("contrCode", contrCode);
        ContrChange contrChg = changeService.get(id);
        model.addAttribute("org", JSON.toJSONString(lovMemberService.get(contrChg.getOrg())));
        model.addAttribute("contrChg", contrChg);
        Contract contract = contractService.get(contrChg.getContrId());
        model.addAttribute("contract", contract);

        Contract frameContr = contractService.get(contract.getFrameNo());
        if (frameContr != null) {
            model.addAttribute("frameContr", JSON.toJSONString(frameContr));
        }

        String pricetableid = contrChg.getPricNo();
        //判断合同状态是否返回修改中，是否是当前处理人
		String conSt = lovMemberService.get(contrChg.getChangeStat()).getCode();
		String constEditFlag = "Y";
		if(conSt.equals("06")) {
			constEditFlag = contractService.checkContrStat(id, getUserObject());
		}else {
			constEditFlag = "N";
		}

        Map<String, String> map = new HashMap<String, String>();
        map.put("constEditFlag", constEditFlag);
        getButtonStatus(map, contrChg);
        model.addAttribute("btnStatus", map);

        // 表单按钮状态控制
        String editFlag = "N";
       
        if (conSt.equalsIgnoreCase("01") || "Y".equals(constEditFlag) || (hasPermission("P05ConCONTRACT_PRO_ENABLE_IN_APP_METHOD") && (conSt.equalsIgnoreCase("02") || conSt.equalsIgnoreCase("03") || conSt.equalsIgnoreCase("04")))) {  //01	新建 	06	返回修改中
            editFlag = "Y";
        }
        LovMember lv09 = lovMemberService.getLovMemberByCode("CONTRACTCHANGETYPE", "09");//09	发货后变更
        if (contrChg.getChangeType().toLowerCase().contains(lv09.getId().toLowerCase()) && map.get("signBtn").equals("0")) {
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
        // 合同评审表单按钮状态控制
        String reviewEditFlag = "N";
        if (conSt.equalsIgnoreCase("03")) {  //03	待评审
            reviewEditFlag = "Y";
        }
        //		model.addAttribute("contrId", id);
        TabMain tabMainInfo = new TabMain();
        tabMainInfo.setInitAll(false);
        if (hasPermission("P05ChangeT1ProjListTab")) {
            tabMainInfo.addTab("工程清单", "/change/prjlst.html?contrId=" + id + "&typ=" + typ + "&pricetableid=" + pricetableid + "&editFlag=" + editFlag + "&showPriceFlg=" + showPriceFlg + "&showMaterCodeFlg=" + showMaterCodeFlg);
            //		tabMainInfo.addTab("工程界面", "/standard/prjpage.html?contrId="+id+"&typ="+typ+"&editFlag="+editFlag);
            //		tabMainInfo.addTab("团队成员", "/team/list.html?businessType=CONTR_CHANGE&businessId="+id);
            //		tabMainInfo.addTab("附件列表", "/change/att.html?contrId="+id);
        }
        if (hasPermission("P05ChangeT2FileTab")) {
            tabMainInfo.addTab("附件", "/common/attachment/attachment.html?businessId=" + id + "&businessType=CONTR_CHANGE&docGroupCode=ATTACHMENTTYPEGROUP");
        }
        if (hasPermission("P05ChangeT3TeamPosTab")) {
            tabMainInfo.addTab("团队成员", "/team/list.html?businessType=CONTR_CHANGE&businessId=" + id);
        }
        if (hasPermission("P05ChangeT4OrgListTab")) {
            tabMainInfo.addTab("组织列表", "/orgTeam/list.html?businessType=CONTR_CHANGE&businessId=" + id);
        }
        if (hasPermission("P05ChangeT5ProReviewTab")) {
            tabMainInfo.addTab("变更评审", "/standard/review.html?contrId=" + id + "&typ=" + typ + "&editFlag=" + editFlag + "&reviewEditFlag=" + reviewEditFlag);
        }
        if (hasPermission("P05ChangeT6ReviewHistory")) {
            tabMainInfo.addTab("审批历史", "/standard/history.html?contrId=" + id);
        }
        model.addAttribute("tabMainInfo", tabMainInfo);
        return forward("tabs");
    }

    public void getButtonStatus(Map<String, String> map, ContrChange contrChg) {
        /*CONTRACTSTATUS ---合同状态						CONTRACTREVIEWSTATUS  ---合同评审状态
 		01	新建                          											02	审批中                            
		02	待初审                        										03	已审批                            
		03	待评审                        										01	未启动                            
		04	待合同书评审                  										04	已驳回                         
		05	待签订                  
		06	返回修改中              
		07	已签订待商务下单        
		08	已签订商务已下单        
		09	已废弃  */
        String conSt = lovMemberService.get(contrChg.getChangeStat()).getCode();
        String trialSt = lovMemberService.get(contrChg.getTrialStat()).getCode();
        String revSt = lovMemberService.get(contrChg.getReviewStat()).getCode();
        String constEditFlag = map.get("constEditFlag");
        String isValid = contrChg.getIsValid();
        
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

            if ((conSt.equalsIgnoreCase("01") && trialSt.equalsIgnoreCase("01")) || ("Y".equals(constEditFlag))) {
                map.put("saveBtn", "1");
            } else {
                map.put("saveBtn", "0");
            }
            if ((conSt.equalsIgnoreCase("01") && trialSt.equalsIgnoreCase("01"))) {
                map.put("trialBtn", "1");
            } else {
                map.put("trialBtn", "0");
            }
            if (conSt.equalsIgnoreCase("05") && revSt.equalsIgnoreCase("03")) {
                map.put("signBtn", "1");
            } else {
                map.put("signBtn", "0");
            }
            if (!conSt.equalsIgnoreCase("10")) {
                map.put("cancelBtn", "1");
            } else {
                map.put("cancelBtn", "0");
            }
        }

    }

    @NoRight
    @ResponseBody
    @RequestMapping(value = "/tabs", method = RequestMethod.POST)
    public String editContrChg(ContrChange contrChg, HttpServletRequest request) {
        String func = request.getParameter("actFunction");
        String typ = request.getParameter("typ");
        validateChangeType(contrChg.getChangeType());
        validataContrChange(contrChg);
        if (func.equalsIgnoreCase("saveOkc")) {
            // 保存
            updateContrChg(contrChg);
        } else if (func.equalsIgnoreCase("startTrialProcess")) {
            // 提交初审
            startContrChangeTrialProcess(typ, contrChg);
        }
        return sendSuccessMessage();
    }

    public String updateContrChg(ContrChange contrChg) {
        edit(contrChg);
        return sendSuccessMessage();
    }

    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(ContrChange contrChg, HttpServletRequest request) {

        validateDelivDate(contrChg.getDelivDate()); // 要货日期
        validateChangeType(contrChg.getChangeType()); // 变更类型
        validataContrChange(contrChg);
        //		/** 订单执行状态-已取消 */
        //		public static final String ORDER_EXECUTE_STATUS_CANCELLED = "CANCELLED";// 订单执行状态-已取消
        //		List<OrderHeader> ordLst = contractService.queryOrderListByContrId(contrChg.getContrId());
        //		LovMember chgTplov = lovMemberService.getLovMemberByCode("CONTRACTCHANGETYPE", "08");//08	作废
        //		if(contrChg.getChangeType().equalsIgnoreCase(chgTplov.getId())){
        //			Condition condition = new Condition();
        //			condition.getFilterObject().addCondition("quotCode", "=", contrChg.getContrId());
        //			List<KstarPrjLst> plist = contractService.getPrjlstList(condition);
        //			for(KstarPrjLst prj : plist){
        //				OrderHeader ord= orderService.queryOrderHeaderByCode(prj.getOrdNo());
        //				if(ord.getExecuteStatus().equalsIgnoreCase(IConstants.ORDER_EXECUTE_STATUS_CANCELLED)){
        //					throw new AnneException("当前合同存在在途的订单： "+ord.getOrderCode() +"，请取消订单后再进行合同变更");
        //				}
        //			}
        //			if(orderService.hasValidOrder(contrChg.getContrId())){
        //				throw new AnneException("当前合同存在在途的订单,请取消订单后再进行合同变更");
        //			}
        //		}
        changeService.save(contrChg, getUserObject());
        return sendSuccessMessage(contrChg.getChangeNo());
    }


    /**
     * 表更类型作废验证，不能存在订单发货数量，开票数量
     *
     * @param contrChg
     */
    private void validataContrChange(ContrChange contrChg) {

        LovMember lv08 = lovMemberService.getLovMemberByCode("CONTRACTCHANGETYPE", "08");//08	作废

        if (contrChg.getChangeType().toLowerCase().contains(lv08.getId().toLowerCase())) {
            Condition condition = new Condition();
            condition.getFilterObject().addCondition("quotCode", "=", contrChg.getContrId());
            List<KstarPrjLst> plist = contractService.getPrjlstList(condition);
            for (KstarPrjLst prj : plist) {

                OrderQuantityVo qrderQuantityVo = orderService.getContractOrderQty(contrChg.getContrNo(), prj.getLineNum());

                if (qrderQuantityVo.getDeliveryQty() > 0) {
                    throw new AnneException("当前合同明细行已存在发货数据，不能进行合同作废变更");
                }

                if (qrderQuantityVo.getInvoiceQty() > 0) {
                    throw new AnneException("当前合同明细行已存在开票数据，不能进行合同作废变更");
                }

            }
        }
    }


    @ResponseBody
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String edit(ContrChange contrChg) {
        validateDelivDate(contrChg.getDelivDate()); // 要货日期
        validateChangeType(contrChg.getChangeType());
        validataContrChange(contrChg);
        // 更新字段
        contrChg.setUpdatedById(getUserObject().getEmployee().getId());
        contrChg.setUpdatedAt(new Date());
        changeService.update(contrChg);
        return sendSuccessMessage();
    }

    @RequestMapping("/edit")
    public String edit(String id, Model model) {
        ContrChange contrChg = changeService.get(id);
        //		Contract contr= contractService.get(contrChg.getContrId());
        BusinessOpportunity biz = bizOppService.getBizOppEntity(contrChg.getProjNo());
        CustomInfo cust = customerService.getCustomInfo(contrChg.getCustCode());
        ProductPriceHead priceTable = priceHeadService.queryLpcById(contrChg.getPricNo());
        model.addAttribute("org", JSON.toJSONString(lovMemberService.get(contrChg.getOrg())));
        model.addAttribute("contrChg", contrChg);

        if (biz != null) {
            model.addAttribute("project", JSON.toJSONString(biz));
        }
        if (cust != null) {
            model.addAttribute("customer", JSON.toJSONString(cust));
        }
        if (priceTable != null) {
            model.addAttribute("priceTable", JSON.toJSONString(priceTable));
        }
        return forward("change");
    }

    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(String id) {
        ContrChange contr = changeService.get(id);

        if (!lovMemberService.get(contr.getChangeStat()).getCode().equalsIgnoreCase("01")) {
            throw new AnneException("不为新建状态下无法删除");
        }
        changeService.delete(id);
        return sendSuccessMessage();
    }

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
        return forward("member/chgMember");
    }

    /**
     * 附件
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/att")
    public String atts(String contrId, Model model) {
        model.addAttribute("contrId", contrId);
        return forward("attachment/chgAttachment");
    }

    /**
     * 工程清单
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/prjlst")
    public String prjlst(String contrId, String typ, String pricetableid, String editFlag, String showPriceFlg, String showMaterCodeFlg, Model model) {
        //add new lovmem root
        //		String groupId = "PRJLSTPRDCAT";
        //		String rootexists = contractService.Checklovroot(contrId);
        //
        //		if(rootexists.equals("Y")){
        //			contractService.addlovroot(contrId, typ, groupId);
        //		}
        ContrChange contrChg = changeService.get(contrId);
        Contract contract = contractService.get(contrChg.getContrId());
        model.addAttribute("contract", contract);
        model.addAttribute("contrId", contrId);
        model.addAttribute("typ", typ);
        model.addAttribute("pricetableid", pricetableid);
        model.addAttribute("editFlag", editFlag);
        model.addAttribute("showPriceFlg", showPriceFlg);
        model.addAttribute("showMaterCodeFlg", showMaterCodeFlg);
        return forward("prjlst/chgPrjlst");
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
        model.addAttribute("contrId", contrId);
        model.addAttribute("typ", typ);
        model.addAttribute("editFlag", editFlag);
        return forward("prjpage/prjpage");
    }

    /**
     * 变更签订
     *
     * @param changeId
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/signUpContract", method = RequestMethod.POST)
    public String signUpContract(String changeId, HttpServletRequest request) throws Exception {

        //		LovMember signLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "03");// 已审批
        //		ContrChange contrChg = changeService.get(changeId);
        //		if(!contrChg.getContrStat().equalsIgnoreCase(signLov.getId())){
        //			throw new AnneException("合同当前状态为："+contrChg.getContrStatName()+" 无法签订！");
        //		}

        ContrChange contrChg = changeService.get(changeId);

        validataContrChange(contrChg);

        changeService.signUpContract(getUserObject(), changeId);
        return sendSuccessMessage();
    }

    @ResponseBody
    @RequestMapping(value = "/startContrChgTrialProcess", method = RequestMethod.POST)
    public String startContrChgTrialProcess(String typ, ContrChange contrChg, HttpServletRequest request) throws Exception {
        String contrId = contrChg.getId();

        validateDelivDate(contrChg.getDelivDate()); // 要货日期

        // 更新字段
        contrChg.setUpdatedById(getUserObject().getEmployee().getId());
        contrChg.setUpdatedAt(new Date());
        changeService.update(contrChg);

        changeService.startContrChgTrialProcess(getUserObject(), contrId, typ);
        return sendSuccessMessage();
    }

    public String startContrChangeTrialProcess(String typ, ContrChange contrChg) {
        String contrId = contrChg.getId();

        changeService.validataNewAmt(contrChg);

        validateDelivDate(contrChg.getDelivDate()); // 要货日期

        // 更新字段
        contrChg.setUpdatedById(getUserObject().getEmployee().getId());
        contrChg.setUpdatedAt(new Date());
        changeService.update(contrChg);

        changeService.startContrChgTrialProcess(getUserObject(), contrId, typ);
        return sendSuccessMessage();
    }

    @ResponseBody
    @RequestMapping(value = "/cancelChange", method = RequestMethod.POST)
    public String cancelChange(String contrId, String typ, HttpServletRequest request) throws Exception {
        ////		changeService.startContrChgTrialProcess(getUserObject(), contrId,typ);
        //		ContrChange contrChg = changeService.get(contrId);
        //		String chgSta = lovMemberService.get(contrChg.getChangeStat()).getCode();
        //		String application="";
        //		String model = "";
        //		if(chgSta.equalsIgnoreCase("02")){ // 待初审
        //			application = contractService.getAppnameByType(IConstants.CONTR_CHANGE_TRIAL_PROC,typ);
        //			model = lovMemberService.getFlowCodeByAppCode(application);
        //			processService.closeByBusinessKey(model, contrId, getUserObject().getParticipant(), "变更单取消");
        //		}else if(chgSta.equalsIgnoreCase("03")){  // 待评审
        //
        ////			application = contractService.getAppnameByType(IConstants.CONTR_CHANGE_PRESALE_PROC,typ);
        //////			model = lovMemberService.getFlowCodeByAppCode(application);
        ////			ProcessInstance pi = processService.getByBusinessKey(application, contrId);
        ////			if(pi!=null){
        ////				processService.close(pi.getId(), getUserObject().getParticipant(), "变更单取消");
        ////			}
        //			List<KstarPrjEvl> prjevlLst	=contractService.queryContrPrjevlListByContrId(contrId);
        //			if(prjevlLst.size()>0){
        //				for(KstarPrjEvl evl: prjevlLst){
        //					ProcessInstance pi = processService.get(evl.getEvlRs());
        //					if(pi!=null){
        //						processService.close(pi.getId(), getUserObject().getParticipant(), "变更单取消");
        //					}
        //				}
        //			}
        //		}
        //
        //		// 合同状态更新
        //		LovMember stdLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "11");// 11	已取消
        //		changeService.updateContrChgStatus(contrId, stdLov.getId());
        //

        changeService.cancelChange(contrId, typ, getUserObject());
        return sendSuccessMessage();
    }

    /**
	 * 合同变更导出
	 * @param condition
	 * @param request
	 * @param response
	 * @throws Exception
	 */
    @NoRight
	@RequestMapping(value = "/changeContrExport")
	public void exportChangeContractLists(PageCondition condition, HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		ActionUtil.requestToCondition(condition, request);

        ActionUtil.doSearch(condition);
		
		String idsStr = request.getParameter("idsStr");
		String typ = request.getParameter("typ");
		String[] ids = idsStr.split(","); 
		
		List<List<Object>> dataList = changeService.exportContractsHead(condition,typ,ids);
		ExcelUtil.exportExcel(response, dataList, "合同变更列表");
	}
    
    
    private void validateDelivDate(Date delivDate) {
        if (delivDate.before(new Date())) {
            throw new AnneException("要货日期要在今天之后");
        }
    }

    private void validateChangeType(String changeType) {
        if (org.xsnake.web.utils.StringUtil.isEmpty(changeType)) {
            throw new AnneException("请选择变更类型");
        }
    }

}
