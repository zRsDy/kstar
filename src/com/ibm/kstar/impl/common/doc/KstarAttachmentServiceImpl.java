package com.ibm.kstar.impl.common.doc;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.dao.HqlUtil;
import org.xsnake.web.dao.utils.FilterObject;
import org.xsnake.web.dao.utils.HqlObject;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.api.common.doc.IKstarAttachmentService;
import com.ibm.kstar.api.quot.IQuotService;
import com.ibm.kstar.entity.common.doc.KstarAttachment;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class KstarAttachmentServiceImpl implements IKstarAttachmentService{
	@Autowired
	BaseDao baseDao;

	@Override
	public IPage query(PageCondition condition) throws Exception  {
		String bizId = condition.getStringCondition("bizId");
		String businessId = condition.getStringCondition("businessId");
		String bizType = condition.getStringCondition("bizType");
		if(StringUtil.isNotEmpty(businessId)){
			bizId = businessId;
		}
		List<Object> args = new ArrayList<Object>();
		if("productComDoc".equals(bizType)){
			String sql = "select distinct" 
					+" a.c_id \"id\",a.c_doc_nm \"docNm\",a.c_doc_tp \"docTp\""
					+" ,m.lov_name \"docTpDesc\",a.c_doc_updr \"docUpdr\",e.name \"docUpdrNm\""
					+" ,a.dt_upd_dt \"dtUpdDt\",a.c_notes \"notes\",a.c_doc_url \"docUrl\""
					+" from CRM_T_ATTACHMENT a"
					+" left join sys_t_lov_member m on a.c_doc_tp = m.row_id" 
					+" left join SYS_T_PERMISSION_EMPLOYEE e on a.c_doc_updr = e.name"
					+" left join crm_t_product_line l on l.c_pro_series = a.c_ext1"
					+" left join crm_t_product_basic b on b.c_pro_line_id = l.c_pid"
					+" where (a.c_biz_type = 'productComDoc' and a.c_biz_id = ?) or (a.c_biz_type = 'proSeriesAttach' and a.c_ext2 = '1' and b.c_pid =?)";
			args.add(bizId);
			args.add(bizId);
			String docNm = condition.getStringCondition("docNm");
			if(StringUtil.isNotEmpty(docNm)){
				sql += " and a.c_doc_nm like ?";
				args.add("%"+docNm+"%");
			}
			String notes = condition.getStringCondition("notes");
			if(StringUtil.isNotEmpty(notes)){
				sql += " and a.c_notes like ?";
				args.add("%"+notes+"%");
			}
			
			return baseDao.searchBySql(sql,args.toArray(),condition.getRows(), condition.getPage());
		}else{
			StringBuffer hql = new StringBuffer(" from KstarAttachment a where 1=1");
			if(StringUtil.isNotEmpty(bizType)){
				hql.append(" and a.bizId = ?");
				args.add(bizId);
			}
			if(StringUtil.isNotEmpty(bizType)){
				hql.append(" and a.bizType = ?");
				args.add(bizType);
			}
			String docNm = condition.getStringCondition("docNm");
			if(StringUtil.isNotEmpty(docNm)){
				hql.append(" and docNm like ?");
				args.add("%"+docNm+"%");
			}
			String notes = condition.getStringCondition("notes");
			if(StringUtil.isNotEmpty(notes)){
				hql.append(" and a.notes like ?");
				args.add("%"+notes+"%");
			}
			String sidx = condition.getStringCondition("sidx");
			if(StringUtil.isNotEmpty(sidx)){
				if("docNm".equals(sidx)) {
					sidx = "c_doc_nm";
				}else if("docTpDesc".equals(sidx)) {
					sidx = "c_doc_tp";
				}else if("notes".equals(sidx)) {
					sidx = "c_notes";
				}else if("dtUpdDt".equals(sidx)) {
					sidx = "dt_upd_dt";
				}else {
					sidx = null;
				}
				if(sidx!=null) {
					hql.append(" order by "+sidx+" ");
					String sord = condition.getStringCondition("sord");
					if(StringUtil.isNotEmpty(sord)){
						hql.append(sord);
					}
				}
			}else {
				hql.append(" order by dt_upd_dt asc ");
			}
			
			return baseDao.search(hql.toString(),args.toArray(),condition.getRows(), condition.getPage());
		}
	}

	@Override
	public IPage queryProductAttachments(PageCondition condition) throws Exception {
		List<Object> args = new ArrayList<>();
		StringBuilder hql = new StringBuilder("from KstarAttachment where bizType=?");
		String bizType = condition.getStringCondition("bizType");
		args.add(bizType);
		String docNm = condition.getStringCondition("docNm");
		if(StringUtil.isNotEmpty(docNm)){
			hql.append(" and docNm like ?");
			args.add("%"+docNm+"%");
		}
		return baseDao.search(hql.toString(),args.toArray(),condition.getRows(), condition.getPage());
	}
	
	@Override
	public List<KstarAttachment> getAttachmentList(Condition condition) throws AnneException {
		FilterObject filterObject = condition.getFilterObject(KstarAttachment.class);
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.findEntity(hqlObject.getHql(),hqlObject.getArgs());
	}

	@Override
	public KstarAttachment get(String id) throws AnneException {
		if(id==null){
			return null;
		}
		return baseDao.findUniqueEntity(" from KstarAttachment where id=?",id );
	}
	@Override
	public void save(KstarAttachment att) {
		baseDao.save(att);
	}
	
	@Override
	public void update(KstarAttachment att) throws AnneException {
		KstarAttachment oldAtt = baseDao.get(KstarAttachment.class, att.getId());
		if(oldAtt == null){
			throw new AnneException(IQuotService.class.getName() + " saveAtt : 没有找到要更新的数据");
		}
				
		BeanUtils.copyPropertiesIgnoreNull(att, oldAtt);
		baseDao.update(oldAtt);
		
		Long count = baseDao.findUniqueEntity("select count(*) from KstarAttachment where id = ? ",att.getId());
		if(count > 1){
			throw new AnneException("附件ID已经存在!"); 
		}
	}


	@Override
	public void delete(String Id) throws AnneException {
		baseDao.deleteById(KstarAttachment.class, Id);
	}


	@Override
	public List<KstarAttachment> getDocCenter() throws AnneException {

		StringBuilder hql = new StringBuilder();
		hql.append(" select s								");
		hql.append(" 	from KstarAttachment s				");
		hql.append(" WHERE s.bizId = (select t.id			");
		hql.append(" from SupportTemplate t					");
		hql.append(" WHERE t.docType = '文件中心')			");

		return baseDao.findEntity(hql.toString());
	}

	@Override
	public List<KstarAttachment> getAttachments(String bizId,String bizType) throws AnneException{
		return baseDao.findEntity("from KstarAttachment where bizId = ? and bizType = ?",new Object[]{bizId,bizType});
	}

	@Override
	public void saveList(List<KstarAttachment> attachments) {
		baseDao.save(attachments);
	}

	@Override
	public void updateExt1(String[] Ids, String value) throws AnneException {
		
	}

}


