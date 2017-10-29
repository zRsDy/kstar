package com.ibm.kstar.entity.order.vo;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;
/**
 * 
 * ClassName: ReceiptsListVO <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason: TODO ADD REASON(可选). <br/> 
 * date: 2017年2月16日 下午4:08:43 <br/> 
 * 
 * @author liming 
 * @version  
 * @since JDK 1.7
 */


public class ReceiptsListVO{
	
	//收款分配
	@Transient
	private List<Map<Object, Object>> allotList = new ArrayList<Map<Object,Object>>();
	//收款核销
	@Transient
	private List<Map<Object, Object>> verificationList = new ArrayList<Map<Object,Object>>();
	
	//合同收款计划
	@Transient
	private List<Map<Object, Object>> contractReceiptsList = new ArrayList<Map<Object,Object>>();

	public List<Map<Object, Object>> getAllotList() {
		return allotList;
	}

	public void setAllotList(List<Map<Object, Object>> allotList) {
		this.allotList = allotList;
	}

	public List<Map<Object, Object>> getVerificationList() {
		return verificationList;
	}

	public void setVerificationList(List<Map<Object, Object>> verificationList) {
		this.verificationList = verificationList;
	}

	public List<Map<Object, Object>> getContractReceiptsList() {
		return contractReceiptsList;
	}

	public void setContractReceiptsList(
			List<Map<Object, Object>> contractReceiptsList) {
		this.contractReceiptsList = contractReceiptsList;
	}

}