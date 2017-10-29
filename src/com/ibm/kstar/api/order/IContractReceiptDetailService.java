package com.ibm.kstar.api.order;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.order.ContractReceiptDetail;

import java.util.List;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

public interface IContractReceiptDetailService {
	void saveContractReceiptDetail(ContractReceiptDetail contractReceiptDetail,UserObject userObject)
			throws AnneException;

	void updateContractReceiptDetail(ContractReceiptDetail contractReceiptDetail)
			throws AnneException;

	IPage queryContractReceiptDetails(PageCondition condition)
			throws AnneException;

	void deleteContractReceiptDetail(String contractReceiptDetailId)
			throws AnneException;

	ContractReceiptDetail getContractReceiptDetail(
			String contractReceiptDetailId) throws AnneException;

	void repairBusinessEntityOfReceiptDetail();

	void repairSalemanOfReceiptDetail();

	void repairTeamOfReceipt();
	
	void searchGatheringDateAndCheckDate(List<ContractReceiptDetail> contractReceiptDetailList);
}