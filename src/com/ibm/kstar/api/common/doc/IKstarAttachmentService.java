package com.ibm.kstar.api.common.doc;

import java.util.List;

import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.entity.common.doc.KstarAttachment; 


public interface IKstarAttachmentService {

	IPage query(PageCondition condition) throws Exception ;
	
	IPage queryProductAttachments(PageCondition condition) throws Exception ;
	
	public void save(KstarAttachment att);
	
	KstarAttachment get(String id) throws AnneException;
	
	void update(KstarAttachment att) throws AnneException;
	
	void delete(String Id) throws AnneException;

	List<KstarAttachment> getAttachmentList(Condition condition) throws AnneException;

	List<KstarAttachment> getDocCenter() throws AnneException;

	List<KstarAttachment> getAttachments(String bizId,String bizType) throws AnneException;

    void saveList(List<KstarAttachment> attachments);
    
    void updateExt1(String[] Ids, String value) throws AnneException;
}
