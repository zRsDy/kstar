/** 
 * Project Name:crm 
 * File Name:IAnltgtmgtService.java 
 * Package Name:com.ibm.kstar.api.report 
 * Date:Mar 15, 20173:34:42 PM 
 * Copyright (c) 2017, ZW All Rights Reserved. 
 * 
 */  
      
 package com.ibm.kstar.api.report;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.entity.quot.KstarQuot;
import com.ibm.kstar.entity.report.KstarAnltgt;

/** 
 * ClassName:IAnltgtmgtService <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     Mar 15, 2017 3:34:42 PM <br/> 
 * @author   ZW 
 * @version   
 * @since    JDK 1.7 
 * @see       
 */
public interface IAnltgtmgtService {

	IPage query(PageCondition condition);

	void saveKstarAnltgt(KstarAnltgt anltgt) throws AnneException;

	KstarAnltgt getKstarAnltgt(String id) throws AnneException;

	void updateKstarAnltgt(KstarAnltgt anltgt) throws AnneException;

	void deleteKstarAnltgt(String id) throws AnneException;

	String checkAngtgt(KstarAnltgt anltgt) throws AnneException;

}
  
