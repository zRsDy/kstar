/** 
 * Project Name:crm 
 * File Name:PrjLstDtl.java 
 * Package Name:com.ibm.kstar.entity.quot 
 * Date:Feb 3, 20175:03:32 PM 
 * Copyright (c) 2017, ZW All Rights Reserved. 
 * 
 */  
      
package com.ibm.kstar.entity.quot;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import com.ibm.kstar.entity.quot.KstarPrjLst;
import com.ibm.kstar.api.system.lov.entity.LovMember;

/** 
 * ClassName:PrjLstDtl <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     Feb 3, 2017 5:03:32 PM <br/> 
 * @author   ZW 
 * @version   
 * @since    JDK 1.7 
 * @see       
 */
public class PrjLstDtl implements Serializable {

	/** 
	 * serialVersionUID:TODO. 
	 * @since JDK 1.7 
	 */
	private static final long serialVersionUID = 1L;
	
	public PrjLstDtl(){}
	
	private String id;
	private KstarPrjLst prjlst;
	private LovMember lvmenber;
	
	@Transient
	private List<Map<Object, Object>> linesList;
	
	
	public List<Map<Object, Object>> getLinesList() {
		return linesList;
	}

	public void setLinesList(List<Map<Object, Object>> linesList) {
		this.linesList = linesList;
	}

	public PrjLstDtl(List<Object> list) {
		this.prjlst = (KstarPrjLst)list.get(0);
		this.lvmenber = (LovMember)list.get(1);
		this.id = lvmenber.getId();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public KstarPrjLst getPrjlst() {
		return prjlst;
	}

	public void setPrjlst(KstarPrjLst prjlst) {
		this.prjlst = prjlst;
	}

	public LovMember getLvmenber() {
		return lvmenber;
	}

	public void setLvmenber(LovMember lvmenber) {
		this.lvmenber = lvmenber;
	}
	
}
  
