package com.ibm.kstar.impl.pdm;

import com.ibm.kstar.api.pdm.PdmFowHistoryService;
import com.ibm.kstar.entity.pdm.PdmFlowHistory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.dao.HqlUtil;
import org.xsnake.web.dao.utils.FilterObject;
import org.xsnake.web.dao.utils.HqlObject;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.page.PageImpl;

/**
 * Copyright: Copyright 2007-2017 HuangQi All Rights Reserved.
 *
 * @Author 黄奇
 * @Title:
 * @Package
 * @Description:
 * @Date 2017年09月22日 09:47
 * @LastModifier 黄奇
 */
@Service
public class PdmFlowHistoryServiceImpl implements PdmFowHistoryService {

    @Autowired
    private BaseDao baseDao;
    
    @Autowired
	SessionFactory sessionFactory;

    @Override
    public IPage query(PageCondition condition,String no) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select b.* from V_WF_FOR_CRM b ");
		sb.append(" where 1=1 ");
		sb.append(" and b.BUSINESSID = '").append(no).append("' ");
		sb.append(" order by b.ASTATIME asc "); 
		
		List<Object[]> list = baseDao.findBySql(sb.toString());
		List<PdmFlowHistory> pdms = new ArrayList<PdmFlowHistory>();
		for(Object[] objects : list){
			PdmFlowHistory pdmFlowHistory = new PdmFlowHistory();
			BigDecimal id = (BigDecimal)objects[0];
			BigDecimal tempId = (BigDecimal)objects[2];
			BigDecimal procId = (BigDecimal)objects[3];
			BigDecimal status = (BigDecimal)objects[5];
			BigDecimal confim = (BigDecimal)objects[10];
			
			pdmFlowHistory.setId(id.toString()+"_"+procId.toString());
			pdmFlowHistory.setName((String)objects[1]);
			pdmFlowHistory.setTempId(tempId.longValue());
			pdmFlowHistory.setProcId(procId.longValue());
			pdmFlowHistory.setProcName((String)objects[4]);
			pdmFlowHistory.setStatus(status.longValue());
			pdmFlowHistory.setStartTime((String)objects[6]);
			pdmFlowHistory.setEndTime((String)objects[7]);
			pdmFlowHistory.setProUsers((String)objects[8]);
			pdmFlowHistory.setOpinions((String)objects[9]);
			pdmFlowHistory.setNeedconfirm(confim.longValue());
			pdmFlowHistory.setFormNo((String)objects[11]);
			pdmFlowHistory.setRowid(id.longValue());
			pdms.add(pdmFlowHistory);
		}
		
		IPage page = new PageImpl(pdms, condition.getPage(), condition.getRows(), 0);
		return page;
		
    }
}
