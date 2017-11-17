package com.ibm.kstar.mobile.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ibm.kstar.entity.contract.Contract;

/**
 * 合同视图
 * @author jian.xu
 *
 */
public class ContractVO extends Contract implements Serializable {
	
	private static final long serialVersionUID = -2422038784184395295L;
	private List<?> prjlsts;
	private List<?> pays;
	
	public ContractVO(){}
	
	@SuppressWarnings("rawtypes")
	public List<?> getPrjlsts() {
		if(prjlsts==null)
			prjlsts = new ArrayList();
		return prjlsts;
	}
	public void setPrjlsts(List<?	> prjlsts) {
		this.prjlsts = prjlsts;
	}

	@SuppressWarnings("rawtypes")
	public List<?> getPays() {
		if(pays==null)
			pays = new ArrayList();
		return pays;
	}

	public void setPays(List<?> pays) {
		this.pays = pays;
	}
	
}
