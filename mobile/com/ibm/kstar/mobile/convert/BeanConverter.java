package com.ibm.kstar.mobile.convert;


import org.apache.commons.beanutils.PropertyUtils;

import com.ibm.kstar.entity.bizopp.BusinessOpportunity;
import com.ibm.kstar.entity.contract.Contract;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.mobile.vo.BizoppVO;
import com.ibm.kstar.mobile.vo.ContractVO;
import com.ibm.kstar.mobile.vo.CustomerVO;

public class BeanConverter {
	public static ContractVO convertContract(Contract bo){
		ContractVO vo = new ContractVO();
		try {
			//PropertyUtils.copyProperties(vo, vo);
			PropertyUtils.copyProperties(vo, bo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vo;
	}
	
	public static BizoppVO convertBizopp(BusinessOpportunity bo){
		BizoppVO vo = new BizoppVO();
		try {
			PropertyUtils.copyProperties(vo, bo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vo;
	}
	
	public static CustomerVO convertCustomer(CustomInfo bo){
		CustomerVO vo = new CustomerVO();
		try {
			PropertyUtils.copyProperties(vo, bo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vo;
	}
}
