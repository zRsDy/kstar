package com.ibm.kstar.impl.cost;


import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.dao.HqlUtil;
import org.xsnake.web.dao.utils.FilterObject;
import org.xsnake.web.dao.utils.HqlObject;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.api.cost.ICostService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.cost.Cost;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class CostServiceImpl implements ICostService {

	@Autowired
	BaseDao baseDao;

	@Override
	public IPage queryCost(PageCondition condition) {
		FilterObject filterObject = condition.getFilterObject(Cost.class);
        filterObject.addOrderBy("creationDate", "desc");
        HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
        return baseDao.search(hqlObject.getHql(), hqlObject.getArgs(),condition.getRows(), condition.getPage());
	}

	/**
	 * 导出所选的费用查询列表
	 */
	@Override
	public List<List<Object>> exportCostList(String[] ids) throws Exception {
		List<List<Object>> lstOut = new ArrayList<List<Object>>();
        addTitle(lstOut);
        List<Cost> lines = getSelectedCostList(ids);
        for (Cost cost : lines) {
            List<Object> lstIn = new ArrayList<Object>();
            lstIn.add(cost.getBatchNo());
            lstIn.add(cost.getInvoiceNo());
            lstIn.add(cost.getEmployeeNo());
            lstIn.add(cost.getClaimPersonName());
            lstIn.add(cost.getDescription());
            lstIn.add(cost.getInvoiceDate());
            lstIn.add(cost.getInvoiceAmount());
            lstIn.add(cost.getInvoiceLineAmount());
            lstIn.add(cost.getBusinessEntity());
            lstIn.add(cost.getSubmitDate());
            lstIn.add(cost.getAccountName());
            lstIn.add(cost.getAccountDept());
            lstIn.add(cost.getCrmPositionName());
            lstIn.add(cost.getCrmOrgName());
            lstIn.add(cost.getImportFlag());
            lstIn.add(cost.getLineDesc());
            lstOut.add(lstIn);
        }
        return lstOut;
	}
	

	@Override
	public List<List<Object>> getOaExpensesClaimTemplet() {
		List<List<Object>> lstOut = new ArrayList<List<Object>>();
		List<Object> lstHead = new ArrayList<Object>();
		lstHead.add("批号");
		lstHead.add("发票编号");
		lstHead.add("工号");
		lstHead.add("员工姓名");
		lstHead.add("描述");
		lstHead.add("发票时间");
		lstHead.add("发票总金额");
		lstHead.add("费用金额");
		lstHead.add("业务实体");
		lstHead.add("提交日期");
		lstHead.add("科目名称");
		lstHead.add("部门段");
		lstHead.add("行备注");
		lstOut.add(lstHead);
		return lstOut;
	}

	@Override
	public void importOaExpensesClaimList(List<List<Object>> dataList, UserObject userObject) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		int rowIndex = 1;
		for (List<Object> rowData : dataList) {
			rowIndex ++;

			Cost cost = new Cost();
            String batchNo = (String) rowData.get(0);
            cost.setBatchNo(batchNo);

            String invoiceNo = (String) rowData.get(1);
            cost.setInvoiceNo(invoiceNo);

            String employeeNo = (String) rowData.get(2);
            cost.setEmployeeNo(employeeNo);

            String claimPersonName = (String) rowData.get(3);
            cost.setClaimPersonName(claimPersonName);

            String description = (String) rowData.get(4);
            cost.setDescription(description);

            // 发票时间
			try {
                String invoiceDateString = (String) rowData.get(5);
                cost.setInvoiceDate(dateFormat.parse(invoiceDateString));
			} catch (Exception e) {
                throw new AnneException("第" + rowIndex + "行发票时间格式不正确, 日期格式应该为yyyy/MM/dd");
			}

            // 发票总金额
            String invoiceAmout = (String) rowData.get(6);
            if (StringUtil.isEmpty(invoiceAmout)) {
                throw new AnneException("第" + rowIndex + "行发票总金额为空。");
            }
			cost.setInvoiceAmount(new BigDecimal(invoiceAmout));

            // 费用金额
            String invoiceLineAmount = (String) rowData.get(7);
            if (StringUtil.isEmpty(invoiceLineAmount)) {
                throw new AnneException("第" + rowIndex + "行费用金额为空。");
            }
            cost.setInvoiceLineAmount(new BigDecimal(invoiceLineAmount));

            // 业务实体
            String businessEntity = (String) rowData.get(8);
            if (StringUtil.isEmpty(businessEntity)) {
                throw new AnneException("第" + rowIndex + "行业务实体为空。");
            }
			cost.setBusinessEntity(businessEntity);

            // 提交日期
            try {
                String submitDateString = (String) rowData.get(9);
                cost.setSubmitDate(dateFormat.parse(submitDateString));
            } catch (Exception e) {
                // throw new AnneException("第" + rowIndex + "行发票时间格式不正确, 日期格式应该为yyyy/MM/dd");
            }

            // 科目名称
            String accountName = (String) rowData.get(10);
            if (StringUtil.isEmpty(accountName)) {
                throw new AnneException("第" + rowIndex + "行科目名称为空。");
            }
            cost.setAccountName(accountName);


            // 部门段
            String accountDept = (String) rowData.get(11);
            if (StringUtil.isEmpty(accountName)) {
                throw new AnneException("第" + rowIndex + "行部门段为空。");
            }
            cost.setAccountDept(accountDept);

            // 行备注
            String lineDesc = (String)rowData.get(12);
            cost.setLineDesc(lineDesc);

            cost.setImportFlag("EXCEL");



            this.baseDao.save(cost);
		}
	}
	
	private void addTitle(List<List<Object>> lstOut) {
        List<Object> lstHead = new ArrayList<Object>();
        lstHead.add("批号");
        lstHead.add("发票编号");
        lstHead.add("工号");
        lstHead.add("员工姓名");
        lstHead.add("描述");
        lstHead.add("发票日期");
        lstHead.add("发票总金额");
        lstHead.add("费用金额");
        lstHead.add("业务实体");
        lstHead.add("提交日期");
        lstHead.add("科目名称");
        lstHead.add("部门段");
        lstHead.add("CRM岗位");
        lstHead.add("CRM部门");
        lstHead.add("导入来源");
        lstHead.add("行备注");
        lstOut.add(lstHead);
    }
	
	 private List<Cost> getSelectedCostList(String[] ids) {
	        String idsStr = "";
	        for (String id : ids) {
	            idsStr += "'" + id + "',";
	        }
	        idsStr = idsStr.substring(0, idsStr.length() - 1);
	        StringBuffer hql = new StringBuffer(" from Cost where 1 = 1 and id in (" + idsStr + ")");
	        return baseDao.findEntity(hql.toString());
	    }

	@Override
	public IPage queryCostByReportParameter(PageCondition condition, String orgIdOrEmployeeId, String reportType, String name,
			String year, String month) {
		StringBuffer hql = new StringBuffer();
		
		if(StringUtil.isNotEmpty(orgIdOrEmployeeId)&&StringUtil.isNotEmpty(reportType)){
			if("ORG".equals(reportType)) {
				condition.getFilterObject().addCondition("crmOrgPath", "like", "%"+orgIdOrEmployeeId+"%");
			}else {
				condition.getFilterObject().addCondition("crmPositionId", "=", orgIdOrEmployeeId);
			}
		}
		
		if(StringUtil.isNotEmpty(name)){
			condition.getFilterObject().addCondition("accountName", "=", name);
		}
		
		if(StringUtil.isNotEmpty(year)){
			condition.getFilterObject().addCondition("invoiceDate", ">=", year+"-"+month+"-"+01);
		}
		
		if(StringUtil.isNotEmpty(month)){
			condition.getFilterObject().addCondition("invoiceDate", "<=", year+"-"+month+"-"+31);
		}
		
		FilterObject filterObject = condition.getFilterObject(Cost.class);
        HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
        filterObject.addOrderBy("creationDate", "desc");
        /*hql.append(hqlObject.getHql());
        
        if(StringUtil.isNotEmpty(year)){
        	hql.append(" to_char(cost.invoiceDate,'yyyy') = '"+year+"' ");
		}
		
		if(StringUtil.isNotEmpty(month)){
			hql.append(" to_char(cost.invoiceDate,'mm') = '"+month+"' ");
		}
		hql.append(" order by cost.creationDate desc ");*/
        return baseDao.search(hqlObject.getHql(), hqlObject.getArgs(),condition.getRows(), condition.getPage());
	}
}
