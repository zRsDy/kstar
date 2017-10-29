package com.ibm.kstar.api.product.database;

import java.util.List;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;
import org.xsnake.web.upload.IUploadFile;

import com.ibm.kstar.api.system.lov.entity.LovMember;

public interface IProductDatabaseService {

	long count(String catalogId);
	
	void saveFile(String catalogId,IUploadFile file,String employeeId);
	
	void saveFile(String catalogId,List<IUploadFile> files,String employeeId);
	
	void deleteFile(String fileId);

	List<LovMember> getPermissionList(String id,String type);
	
	IPage search(PageCondition condition,boolean isAdmin,String positionId);
	
	IPage getPermissionPage(PageCondition condition);
	
	void configPermission(String productCatalogId, String[] permissions , String type);

	List<LovMember> getMyProductCatalog(String positionId, String orgId);
	
}
