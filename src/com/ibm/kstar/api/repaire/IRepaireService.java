/** 
 * Project Name:crm 
 * File Name:IAnltgtmgtService.java 
 * Package Name:com.ibm.kstar.api.report 
 * Date:Mar 15, 20173:34:42 PM 
 * Copyright (c) 2017, ZW All Rights Reserved. 
 * 
 */  
      
 package com.ibm.kstar.api.repaire;

import java.util.List;

import org.xsnake.web.exception.AnneException;

import com.ibm.kstar.api.system.lov.entity.LovMember;


public interface IRepaireService {

	void updateEn(String id,String en) throws AnneException;

	List<LovMember> getDictionarys(String codes) throws AnneException;
}
  
