/** 
 * Project Name:crm 
 * File Name:AnltgtmgtServiceImpl.java 
 * Package Name:com.ibm.kstar.impl.report 
 * Date:Mar 15, 2017 3:36:57 PM 
 * Copyright (c) 2017, ZW All Rights Reserved. 
 * 
 */

package com.ibm.kstar.impl.report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.api.quot.IQuotService;
import com.ibm.kstar.api.report.IAnltgtmgtService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.quot.KstarPrjLst;
import com.ibm.kstar.entity.quot.KstarQuot;
import com.ibm.kstar.entity.report.KstarAnltgt;

/**
 * ClassName:AnltgtmgtServiceImpl <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: Mar 15, 2017 3:36:57 PM <br/>
 * 
 * @author ZW
 * @version
 * @since JDK 1.7
 * @see
 */

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class AnltgtmgtServiceImpl implements IAnltgtmgtService {

	@Autowired
	BaseDao baseDao;
	
	
	
	
	@Override
	public IPage query(PageCondition condition) {
		
		FilterObject filterObject = condition.getFilterObject(KstarAnltgt.class);
		filterObject.addOrderBy("annual", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
		
		//StringBuffer hql = new StringBuffer(" from KstarQuot ");
		//return baseDao.search(hql.toString(),condition.getRows(), condition.getPage());
	}
	
	
	private String getYear(){
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy");

		String reValue = df.format(new Date());
		
		return reValue;
	}
	
	
	@Override
	public void saveKstarAnltgt(KstarAnltgt anltgt) throws AnneException {
		
		String theyear=  getYear();
		
		anltgt.setAnnual(theyear);
		
		baseDao.save(anltgt);
	}
	
	
	@Override
	public KstarAnltgt getKstarAnltgt(String id) throws AnneException {
		return baseDao.get(KstarAnltgt.class, id);
	}
	
	
	
	@Override
	public void updateKstarAnltgt(KstarAnltgt anltgt) throws AnneException {
		KstarAnltgt oldAngtgt = baseDao.get(KstarAnltgt.class, anltgt.getId());
		if(oldAngtgt == null){
			throw new AnneException(IQuotService.class.getName() + " saveKstarAnltgt : 没有找到要更新的数据");
		}
		
		if(!anltgt.getId().equals(oldAngtgt.getId())){
			throw new AnneException("ID不能被修改");
		}
				
		BeanUtils.copyPropertiesIgnoreNull(anltgt, oldAngtgt);

		baseDao.update(oldAngtgt);
		Long count = baseDao.findUniqueEntity("select count(*) from KstarAnltgt where id = ? ",anltgt.getId());
		if(count > 1){
			throw new AnneException("ID已经存在!"); 
		}
	}
	
	
	@Override
	public String checkAngtgt(KstarAnltgt anltgt) throws AnneException {
		
		String result = "Y";
		
		StringBuffer hql = new StringBuffer(" from KstarAnltgt where 1=1 ");
	
		List<KstarAnltgt> anltgtlsts = baseDao.findEntity(hql.toString());
		
		LovMember lov = baseDao.get(LovMember.class,anltgt.getCtype());
		for(KstarAnltgt theanltgt : anltgtlsts) {
			if("20".equals(lov.getCode())) {
				if(theanltgt.getEmpId().equals(anltgt.getEmpId())){
					if(theanltgt.getAnnual().equals(anltgt.getAnnual())){
						result = "N";
					}	
				}
			}else {
				if(theanltgt.getDepid().equals(anltgt.getDepid())){
					if(theanltgt.getEmpId().equals(anltgt.getEmpId())){
						if(theanltgt.getAnnual().equals(anltgt.getAnnual())){
							result = "N";
						}	
					}
				}
			}
		}
		
		return result;
	}
	
	
	@Override
	public void deleteKstarAnltgt(String id) throws AnneException {
		baseDao.executeHQL(" delete KstarAnltgt qt where qt.id = ? ",new Object[]{id});
		//baseDao.deleteById(KstarQuot.class, quotId);
	}
	
	
	
	

}
