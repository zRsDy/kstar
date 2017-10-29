/** 
 * Project Name:crm 
 * File Name:AnltgtmgtServiceImpl.java 
 * Package Name:com.ibm.kstar.impl.report 
 * Date:Mar 15, 2017 3:36:57 PM 
 * Copyright (c) 2017, ZW All Rights Reserved. 
 * 
 */

package com.ibm.kstar.impl.repaire;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.exception.AnneException;

import com.ibm.kstar.api.repaire.IRepaireService;
import com.ibm.kstar.api.system.lov.entity.LovMember;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class RepaireServiceImpl implements IRepaireService {

	@Autowired
	BaseDao baseDao;
	
	@Override
	public void updateEn(String id,String en) throws AnneException{
		baseDao.executeHQL("update LovMember t set t.optTxt7=? where t.id=?",new String[]{en,id});
	}

	@Override
	public List<LovMember> getDictionarys(String codes) throws AnneException{
		String sql = "  "
				+ " from LovMember t "
				+ " where "
				+ "t.groupCode not in "
				+ "('PERMISSION','ORG','NEWMENU','procatalog','PRODUCTMODE',"
				+ "'TERRITORY','POSITION','CUSTOMORGTREE','ROLE',"
				+ "'ADDRESSREGION','APPROVE_POSITION_LEVEL')";
		List<LovMember> ms = baseDao.findEntity(sql);
		return ms;
	}

}
