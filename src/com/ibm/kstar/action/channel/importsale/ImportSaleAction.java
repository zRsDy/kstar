package com.ibm.kstar.action.channel.importsale;


import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.action.system.lov.member.LovMemberAction;
import com.ibm.kstar.api.channel.IImportSaleService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.IEmployeeService;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.entity.channel.ImportSaleApply;
import com.ibm.kstar.entity.channel.ImportSaleApplyDetail;
import com.ibm.kstar.impl.channel.SerialNumberService;
import com.ibm.kstar.interceptor.system.permission.NoRight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;
import org.xsnake.web.page.PageImpl;
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/importSale")
public class ImportSaleAction extends BaseAction {
    @Autowired
    IImportSaleService importSaleService;
    @Autowired
    private ILovMemberService lovMemberService;
    @Autowired
    SerialNumberService serialNumberService;

    @Autowired
    LovMemberAction lovMemberAction;

    @Autowired
    IEmployeeService employeeService;

    private void init(List<ImportSaleApply> importSaleApplyList) {
        for (ImportSaleApply saleApply : importSaleApplyList) {
            init(saleApply);
        }
    }

    private void init(ImportSaleApply applyInfo) {
        if (StringUtil.isNotEmpty(applyInfo.getImportEmployeeId()) && StringUtil.isNotEmpty(applyInfo.getImportPositionId())) {
            Employee importEmployee = employeeService.get(applyInfo.getImportEmployeeId());
            LovMember importPosition = lovMemberService.get(applyInfo.getImportPositionId());
            applyInfo.setImportName((importEmployee != null ? importEmployee.getName() : "") + " | " + (importPosition != null ? importPosition.getName() : ""));
        } else if (StringUtil.isNotEmpty(applyInfo.getImportUnit())) {
            LovMember importUnit = lovMemberService.get(applyInfo.getImportUnit());
            applyInfo.setImportName(importUnit != null ? importUnit.getName() : "");
        }

        if (StringUtil.isNotEmpty(applyInfo.getExportEmployeeId()) && StringUtil.isNotEmpty(applyInfo.getExportPositionId())) {
            Employee exportEmployee = employeeService.get(applyInfo.getExportEmployeeId());
            LovMember exportPosition = lovMemberService.get(applyInfo.getExportPositionId());
            applyInfo.setExportName((exportEmployee != null ? exportEmployee.getName() : "") + " | " + (exportPosition != null ? exportPosition.getName() : ""));
        } else if (StringUtil.isNotEmpty(applyInfo.getExportUnit())) {
            LovMember exportUnit = lovMemberService.get(applyInfo.getExportUnit());
            applyInfo.setExportName(exportUnit != null ? exportUnit.getName() : "");
        }
    }

    @RequestMapping("/list")
    public String list(Model model) {
        outQueryCondition(model);
        return forward("import_sale_list");
    }

    @NoRight
    @ResponseBody
    @RequestMapping("/page")
    public String page(PageCondition condition, HttpServletRequest request) {
        ActionUtil.requestToCondition(condition, request);
        IPage p = importSaleService.queryApplys(condition);
        init((List<ImportSaleApply>) p.getList());
        return sendSuccessMessage(p);
    }


    /**
     * 选择组织或人员
     *
     * @return
     */
    @NoRight
    @RequestMapping("/selectOrgOrEmployee")
    public String selectDepartment() {
        return forward("selectOrgOrEmployee");
    }

    @NoRight
    @ResponseBody
    @RequestMapping("/orgOrEmployeeTree")
    public String orgOrEmployeeTree(Condition condition, HttpServletRequest request) {
        ActionUtil.requestToCondition(condition, request);
        String id = condition.getStringCondition("id");
        if (StringUtil.isEmpty(id)) {
            return lovMemberAction.tree(condition, request);
        }
        LovMember lovMember = lovMemberService.get(id);
        if (lovMember == null) {
            return sendSuccessMessage(new ArrayList<>());
        }
        if ("Y".equals(lovMember.getLeafFlag())) {
            List<Map<String, Object>> employees = this.importSaleService.getEmployeeLovForOrg(id);
            return sendSuccessMessage(employees);
        } else {
            return lovMemberAction.tree(condition, request);
        }
    }

    @NoRight
    @RequestMapping("/addOrEdit")
    public String addOrEdit(String id, boolean statusEdit, boolean ableEdit, Model model) {
        if (id != null) {
            ImportSaleApply applyInfo = importSaleService.queryApply(id);
            model.addAttribute("applyInfo", applyInfo);
            init(applyInfo);
        } else {
            ImportSaleApply applyInfo = new ImportSaleApply();
            applyInfo.setApplyCode(serialNumberService.getSerialNumber3("importSaleApply"));
            applyInfo.setApplier(this.getUserObject().getEmployee().getId());
            applyInfo.setApplierName(this.getUserObject().getEmployee().getName());
            applyInfo.setApplyUnit(this.getUserObject().getOrg().getId());
            applyInfo.setOrganization(this.getUserObject().getOrg().getId());
            applyInfo.setCurrency(applyInfo.getLovMember("CURRENCY", "CNY").getId());
            applyInfo.setStatus(applyInfo.getLovMember(ProcessConstants.IMPORT_SALE_PROC, ProcessConstants.PROCESS_STATUS_01).getId());
            applyInfo.setApplyDate(new Date());
            model.addAttribute("applyInfo", applyInfo);
        }
        TabMain tabMainInfo = new TabMain();
        tabMainInfo.setInitAll(false);
        tabMainInfo.addTab("审批历史", "/standard/history.html?contrId=" + id);
        model.addAttribute("tabMainInfo", tabMainInfo);
        model.addAttribute("statusEdit", statusEdit);
        model.addAttribute("ableEdit", ableEdit);
        return forward("import_sale_add");
    }

    @NoRight
    @ResponseBody
    @RequestMapping(value = "/addOrEdit", method = RequestMethod.POST)
    public String addOrEdit(ImportSaleApply importApply) {
        importSaleService.addOrEditApply(importApply, this.getUserObject());
        return sendSuccessMessage();
    }

    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(String id) {
        importSaleService.deleteApply(id);
        return sendSuccessMessage();
    }

    @ResponseBody
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public String submit(String id) {
        importSaleService.submitApply(this.getUserObject(), id);
        return sendSuccessMessage();
    }

    @NoRight
    @ResponseBody
    @RequestMapping("/pageDetail")
    public String pageDetail(PageCondition condition, HttpServletRequest request) {
        ActionUtil.requestToCondition(condition, request);
        //模糊查询
        String applyId = condition.getStringCondition("applyId");
        IPage p = new PageImpl(null, condition.getPage(), condition.getRows(), 0);
        if (StringUtil.isNotEmpty(applyId)) {
            p = importSaleService.queryDetails(condition);
        }
        return sendSuccessMessage(p);
    }

    @RequestMapping("/addOrEditDetail")
    public String addOrEditDetail(String applyId, String id, boolean statusEdit, Model model) {
        if (id != null) {
            ImportSaleApplyDetail detailInfo = importSaleService.queryDetail(id);
            model.addAttribute("detailInfo", detailInfo);
        } else {
            ImportSaleApplyDetail detailInfo = new ImportSaleApplyDetail();
            detailInfo.setApplyId(applyId);
            detailInfo.setImportDate(new Date());
            model.addAttribute("detailInfo", detailInfo);
        }
        model.addAttribute("statusEdit", statusEdit);
        return forward("import_sale_detail_add");
    }

    @ResponseBody
    @RequestMapping(value = "/addOrEditDetail", method = RequestMethod.POST)
    public String addOrEditDetail(ImportSaleApplyDetail detailInfo) {
        importSaleService.addOrEditDetail(detailInfo, this.getUserObject());
        return sendSuccessMessage(detailInfo);
    }

    @ResponseBody
    @RequestMapping(value = "/deleteDetail", method = RequestMethod.POST)
    public String deleteDetail(String id) {
        importSaleService.deleteDetail(id);
        return sendSuccessMessage();
    }

    private void outQueryCondition(Model model) {
        List<LovMember> importTypeLst = lovMemberService.getListByGroupCode("IMPORTTYPE");    //申请类型
        model.addAttribute("importTypeLst", importTypeLst);
    }

    @NoRight
    @RequestMapping(value = "/selectOrder")
    public String selectOrder(Condition condition, HttpServletRequest request, Model model) {
        ActionUtil.requestToCondition(condition, request);
        String type = condition.getStringCondition("type");
        String employeeId = condition.getStringCondition("employeeId");
        String positionId = condition.getStringCondition("positionId");
        String orgId = condition.getStringCondition("orgId");
        String id = condition.getStringCondition("id");
        String pickerId = condition.getStringCondition("pickerId");
        model.addAttribute("id", id);
        model.addAttribute("type", type);
        model.addAttribute("employeeId", employeeId);
        model.addAttribute("positionId", positionId);
        model.addAttribute("orgId", orgId);
        model.addAttribute("pickerId", pickerId);
        return forward("orderSelectList");
    }
}