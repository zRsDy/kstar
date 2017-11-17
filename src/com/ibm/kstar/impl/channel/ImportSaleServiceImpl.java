package com.ibm.kstar.impl.channel;

import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.action.common.process.ProcessStatusService;
import com.ibm.kstar.api.channel.IImportSaleService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.IEmployeeService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.channel.ImportSaleApply;
import com.ibm.kstar.entity.channel.ImportSaleApplyDetail;
import com.ibm.kstar.impl.BaseServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.remote.server.Remote;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.StringUtil;
import org.xsnake.xflow.api.IProcessService;
import org.xsnake.xflow.api.workflow.IXflowProcessServiceWrapper;

import java.math.BigDecimal;
import java.util.*;

@Service
@Remote
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class ImportSaleServiceImpl extends BaseServiceImpl implements IImportSaleService {
    @Autowired
    BaseDao baseDao;
    @Autowired
    IEmployeeService employeeService;
    @Autowired
    private ILovMemberService lovMemberService;
    @Autowired
    IProcessService processService;
    @Autowired
    ProcessStatusService processStatusService;
    @Autowired
    IXflowProcessServiceWrapper xflowProcessServiceWrapper;

    @Override
    public IPage queryApplys(PageCondition condition) {
        StringBuilder hql = new StringBuilder("select s from ImportSaleApply s,LovMember m where s.applyUnit = m.id");
        List<Object> args = new ArrayList<>();
        this.addQueryCondition(condition, args, hql, "applyCode", "s.applyCode", "like");
        this.addQueryCondition(condition, args, hql, "applyUnit", "m.name", "like");
        this.addQueryCondition(condition, args, hql, "applyType", "s.applyType", "=");
        hql.append(" order by s.id desc");
        IPage page = baseDao.search(hql.toString(), args.toArray(), condition.getRows(), condition.getPage());
        @SuppressWarnings("unchecked")
        List<ImportSaleApply> list = (List<ImportSaleApply>) page.getList();
        for (ImportSaleApply apply : list) {
            apply.setApplierName(employeeService.get(apply.getApplier()).getName());
            List<ImportSaleApplyDetail> detailList = baseDao.findEntity("from ImportSaleApplyDetail d where d.applyId = ?", apply.getId());
            BigDecimal applyAmount = new BigDecimal("0");
            for (ImportSaleApplyDetail detail : detailList) {
                applyAmount = applyAmount.add(detail.getImportAmount());
            }
            apply.setApplyAmount(applyAmount);
        }
        return page;
    }

    @Override
    public ImportSaleApply queryApply(String id) {
        ImportSaleApply apply = baseDao.get(ImportSaleApply.class, id);
        apply.setApplierName(employeeService.get(apply.getApplier()).getName());
        return apply;
    }

    @Override
    public void addOrEditApply(ImportSaleApply importApply, UserObject user) {
        if (importApply.getId() != null) {
            ImportSaleApply applyData = baseDao.get(ImportSaleApply.class, importApply.getId());
            BeanUtils.copyPropertiesIgnoreNull(importApply, applyData);
            applyData.setRecordInfor(true, user);
            baseDao.update(applyData);
        } else {
            importApply.setRecordInfor(false, user);
            baseDao.save(importApply);
        }
    }

    @Override
    public void deleteApply(String id) {
        ImportSaleApply applyInfo = baseDao.get(ImportSaleApply.class, id);
        baseDao.delete(applyInfo);
    }

    @Override
    public void submitApply(UserObject user, String id) {
        String model = lovMemberService.getFlowCodeByAppCode(ProcessConstants.IMPORT_SALE_PROC);
        String flowName = null;
        if (StringUtil.isNotEmpty(model)) {
            flowName = new ImportSaleApply().getLovMember("APPLICATION", model).getName();
        }
        ImportSaleApply apply = baseDao.get(ImportSaleApply.class, id);
        LovMember lov = lovMemberService.getLovMemberByCode(ProcessConstants.IMPORT_SALE_PROC, ProcessConstants.PROCESS_STATUS_02);
        processStatusService.updateProcessStatus("ImportSaleApply", id, "status", lov.getId());

        //查询该组织最高岗位
        String sql = "select row_id,lov_name,lov_level from sys_t_lov_member"
                + " where parent_id = ?"
                + " and lov_level = (select min(lov_level) from sys_t_lov_member where group_id = 'ORG' and parent_id = ?)";
        List<Object[]> pLst = baseDao.findBySql(sql, apply.getExportUnit());
        if (pLst.size() == 0) {
            throw new AnneException("该销量转出单位下无岗位，不能提交！");
        } else if (pLst.size() > 1) {
            throw new AnneException("该销量转出单位下最高岗位有多个，不能提交！");
        }
        Map<String, String> varmap = new HashMap<>();
        varmap.put("positionIdInForm", pLst.get(0)[0].toString());
        varmap.put("positionNameInForm", pLst.get(0)[1].toString());
        varmap.put("TODO", "TODO");
        xflowProcessServiceWrapper.start(model, ProcessConstants.IMPORT_SALE_PROC, flowName + "-" + apply.getApplyCode(), id, user, varmap);
    }

    @Override
    public IPage queryDetails(PageCondition condition) {
        String applyId = condition.getStringCondition("applyId");
        List<Object> args = new ArrayList<Object>();
        StringBuilder hql = new StringBuilder("select d from ImportSaleApplyDetail d where d.applyId=?");
        args.add(applyId);
        this.addQueryCondition(condition, args, hql, "orderCode", "d.orderCode", "like");
        this.addQueryCondition(condition, args, hql, "productSeries", "d.productSeries", "like");
        this.addQueryCondition(condition, args, hql, "productKind", "d.productKind", "like");
        hql.append("order by d.id desc");
        return baseDao.search(hql.toString(), args.toArray(), condition.getRows(), condition.getPage());
    }

    @Override
    public ImportSaleApplyDetail queryDetail(String id) {
        return baseDao.get(ImportSaleApplyDetail.class, id);
    }

    @Override
    public void addOrEditDetail(ImportSaleApplyDetail detailInfo, UserObject user) {
        String applyId = detailInfo.getApplyId();
        ImportSaleApply importSaleApply = this.baseDao.get(ImportSaleApply.class, applyId);
        if (importSaleApply == null) {
            throw new AnneException("未找到相应的引入销量申请");
        }
        if (StringUtil.isNotEmpty(detailInfo.getMaterielCode())) {
            //language=HQL
            String proSeries = this.baseDao.findUniqueEntity("select pl.proSeries from KstarProductLine pl,KstarProduct p where p.proLineID=pl.id and p.materCode=?", new Object[]{detailInfo.getMaterielCode()});
            detailInfo.setProductSeries(proSeries);
        } else {
            detailInfo.setProductSeries(null);
        }

        Integer orderQuantity = detailInfo.getOrderQuantity();
        BigDecimal importRatio = importSaleApply.getImportRatio();
        if (orderQuantity != null && importRatio != null) {
            detailInfo.setImportQuantity(importRatio.multiply(new BigDecimal("0.01")).multiply(new BigDecimal(orderQuantity)));
        }else {
            detailInfo.setImportQuantity(null);
        }
        if (detailInfo.getImportPrice() != null && detailInfo.getImportQuantity() != null) {
            detailInfo.setImportAmount(detailInfo.getImportQuantity().multiply(detailInfo.getImportPrice()));
        } else {
            detailInfo.setImportAmount(null);
        }

        detailInfo.setImportDate(new Date());

        if (detailInfo.getId() != null) {
            ImportSaleApplyDetail detailData = baseDao.get(ImportSaleApplyDetail.class, detailInfo.getId());
            BeanUtils.copyPropertiesIgnoreNull(detailInfo, detailData);
            detailData.setRecordInfor(true, user);
            baseDao.update(detailData);
        } else {
            detailInfo.setRecordInfor(false, user);
            baseDao.save(detailInfo);
        }
    }

    @Override
    public void deleteDetail(String id) {
        ImportSaleApplyDetail detailInfo = baseDao.get(ImportSaleApplyDetail.class, id);
        baseDao.delete(detailInfo);
    }

    @Override
    public List<Map<String, Object>> selectOrgOrEmployee(PageCondition condition) {
        //language=SQL
        List<Object> args = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(
                "SELECT *\n" +
                        "FROM (SELECT\n" +
                        "        e.ROW_ID     AS \"employeeId\",\n" +
                        "        e.NO         AS \"employeeNo\",\n" +
                        "        e.NAME       AS \"employeeName\",\n" +
                        "        p.ROW_ID     AS \"positionId\",\n" +
                        "        p.LOV_NAME   AS \"positionName\",\n" +
                        "        org.ROW_ID   AS \"orgId\",\n" +
                        "        org.LOV_NAME AS \"orgName\",\n" +
                        "        'E'          AS \"type\"\n" +
                        "      FROM SYS_T_PERMISSION_EMPLOYEE e, SYS_T_LOV_MEMBER p, SYS_T_PERMISSION_USER_REL ep, SYS_T_LOV_MEMBER org\n" +
                        "      WHERE e.ROW_ID = ep.USER_ID AND ep.MEMBER_ID = p.ROW_ID AND p.GROUP_CODE = 'POSITION' AND p.OPT_TXT1 = org.ROW_ID\n" +
                        "      UNION ALL\n" +
                        "      SELECT\n" +
                        "        NULL,\n" +
                        "        NULL,\n" +
                        "        NULL,\n" +
                        "        NULL,\n" +
                        "        NULL,\n" +
                        "        org.ROW_ID,\n" +
                        "        org.LOV_NAME,\n" +
                        "        'ORG'\n" +
                        "      FROM SYS_T_LOV_MEMBER org\n" +
                        "      WHERE org.GROUP_CODE = 'ORG' AND DELETE_FLAG = 'N' )\n" +
                        "WHERE ROWNUM <= 30 \n");
        String search = condition.getStringCondition("search");
        if (StringUtils.isNotEmpty(search)) {
            sql.append("and (\"employeeNo\" LIKE ? or \"employeeName\" like ? or \"orgName\" LIKE ?) ");
            args.add("%" + search + "%");
            args.add("%" + search + "%");
            args.add("%" + search + "%");
        }
        sql.append(" order by \"orgName\",\"employeeNo\",\"employeeName\"");
        return this.baseDao.findBySql4Map(sql.toString(), args.toArray());
    }

    @Override
    public List<Map<String, Object>> getEmployeeLovForOrg(String id) {
        //language=SQL
        String sql =
                "SELECT\n" +
                        "  e.ROW_ID              AS \"id\",\n" +
                        "  e.NO || ' ' || e.NAME AS \"name\",\n" +
                        "  e.ROW_ID              AS \"employeeId\",\n" +
                        "  e.NO                  AS \"employeeNo\",\n" +
                        "  e.NAME                AS \"employeeName\",\n" +
                        "  p.ROW_ID              AS \"positionId\",\n" +
                        "  p.LOV_NAME            AS \"positionName\",\n" +
                        "  'E'                   AS \"type\"\n" +
                        "FROM SYS_T_PERMISSION_EMPLOYEE e, SYS_T_LOV_MEMBER p, SYS_T_PERMISSION_USER_REL ep\n" +
                        "WHERE e.ROW_ID = ep.USER_ID AND ep.MEMBER_ID = p.ROW_ID AND p.OPT_TXT1 = ?";

        List<Map<String, Object>> employees = this.baseDao.findBySql4Map(sql, new Object[]{id});



        return employees;
    }
}