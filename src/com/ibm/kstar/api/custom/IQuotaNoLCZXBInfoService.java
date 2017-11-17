package com.ibm.kstar.api.custom;

import java.util.List;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import com.ibm.kstar.entity.custom.NoLcQuotaZXBInfo;

public interface IQuotaNoLCZXBInfoService {
	
	IPage query(PageCondition condition);
	
	NoLcQuotaZXBInfo createInfor();
	
	void saveInfo(NoLcQuotaZXBInfo noLCQuota) throws AnneException;
	
	void updateInfo(NoLcQuotaZXBInfo noLCQuota) throws AnneException;
	
	void deleteInfo(String id) throws AnneException;
	
	NoLcQuotaZXBInfo getInfoById(String id) throws AnneException;
	
	List<NoLcQuotaZXBInfo> getInfoByCode(String code) throws AnneException;
	
	String copyInfo(NoLcQuotaZXBInfo noLCQuota) throws AnneException;
	
	String doNoLcQuotaApplyV2(NoLcQuotaZXBInfo noLCQuota) throws AnneException;
}
