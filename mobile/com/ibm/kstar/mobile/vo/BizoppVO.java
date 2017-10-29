package com.ibm.kstar.mobile.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ibm.kstar.entity.bizopp.BusinessOpportunity;

/**
 * 商机视图
 * @author jian.xu
 *
 */
public class BizoppVO extends BusinessOpportunity implements Serializable {

	private static final long serialVersionUID = 1943071908773109540L;
	private List<?> integrators;
	private List<?> prjlsts;
	
	public BizoppVO(){}

	@SuppressWarnings("rawtypes")
	public List<?> getIntegrators() {
		if(integrators==null)
			integrators = new ArrayList();
		return integrators;
	}

	public void setIntegrators(List<?> integrators) {
		this.integrators = integrators;
	}

	@SuppressWarnings("rawtypes")
	public List<?> getPrjlsts() {
		if(prjlsts==null)
			prjlsts = new ArrayList();
		return prjlsts;
	}

	public void setPrjlsts(List<?> prjlsts) {
		this.prjlsts = prjlsts;
	}
	
	
}
