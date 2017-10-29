/**
 * 
 */
package com.ibm.kstar.entity.contract;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;
/**
 * @author Joe
 *
 */
public class ContrVeriDetlLstVO {
	
	//借货核销明细
	@Transient
	private List<Map<Object, Object>> loanVeriDtlList = new ArrayList<Map<Object,Object>>();

	public List<Map<Object, Object>> getLoanVeriDtlList() {
		return loanVeriDtlList;
	}

	public void setLoanVeriDtlList(List<Map<Object, Object>> loanVeriDtlList) {
		this.loanVeriDtlList = loanVeriDtlList;
	}


}
