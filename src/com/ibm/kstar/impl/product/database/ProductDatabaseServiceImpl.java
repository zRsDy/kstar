package com.ibm.kstar.impl.product.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.page.IPage;
import org.xsnake.web.page.PageImpl;
import org.xsnake.web.upload.IUploadFile;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.api.product.database.IProductDatabaseService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.product.database.CatalogPermissionRel;
import com.ibm.kstar.entity.product.database.ProductDocument;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class ProductDatabaseServiceImpl implements IProductDatabaseService{

	@Autowired
	BaseDao baseDao;
	
	@Override
	public long count(String catalogId) {
		Long count = baseDao.findUniqueEntity("select count(*) from ProductDocument where catalogId = ? ",catalogId);
		return count;
	}

	@Override
	public void saveFile(String catalogId,IUploadFile file,String employeeId) {
		ProductDocument pd = new ProductDocument();
		pd.setCatalogId(catalogId);
		pd.setCreateBy(employeeId);
		pd.setCreateDate(new Date());
		pd.setFileType(file.getSuffix());
		pd.setLastUpdateDate(new Date());
		pd.setLastUpdateBy(employeeId);
		pd.setName(file.getName());
		pd.setPath(file.getWebPath());
		pd.setSize(file.getSize());
		baseDao.save(pd);
	}

	@Override
	public IPage search(PageCondition condition,boolean isAdmin,String positionId) {
		String parentId = condition.getStringCondition("parentId");
		String searchKey = condition.getStringCondition("searchKey");
		String all = condition.getStringCondition("all");
		StringBuffer sql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		if(isAdmin){
			sql.append(" from ProductDocument pd where 1=1 ");
			if(!"Y".equals(all)){
				sql.append("and pd.catalogId = ? ");
				args.add(parentId);
			}
		}else{
			args.add(positionId);
//			args.add(orgId);
			sql.append("select new com.ibm.kstar.entity.product.database.ProductDocument(pd,r.type) from CatalogPermissionRel r , LovMember o ,LovMember p,ProductDocument pd ")
			.append(" where r.productCatalogId = p.id and r.orgId = o.id ")
			.append(" and p.groupId = 'PRODUCT_DATABASE' and o.groupId='ORG' and o.deleteFlag = 'N' and (r.orgId = ?) ")
			.append(" and pd.catalogId = p.id ");
			if(!"Y".equals(all)){
				sql.append(" and pd.catalogId = ? ");
				args.add(parentId);
			}
		}
		if(StringUtil.isNotEmpty(searchKey)){
			sql.append(" and (pd.name like ?) ");
			args.add("%"+searchKey+"%");
		}
		sql.append(" order by pd.name ");
		return baseDao.search(sql.toString(),args.toArray(), condition.getRows(), condition.getPage());
	}

	@Override
	public void deleteFile(String fileId) {
		baseDao.delete(baseDao.load(ProductDocument.class, fileId));
	}
	
	@Override
	public IPage getPermissionPage(PageCondition condition) {
		if(StringUtil.isEmpty(condition.getStringCondition("productCatalogId"))){
			return new PageImpl(new ArrayList<>(), 20, 1, 0);
		}
		String hql = " select new LovMember(o.id,o.name,o.code,r.type,o.memo) from CatalogPermissionRel r , LovMember o ,LovMember p where r.productCatalogId = p.id and r.orgId = o.id and p.groupId = 'PRODUCT_DATABASE' and o.groupId='ORG' and o.deleteFlag = 'N' and p.id = ? ";
		return baseDao.search(hql ,new Object[]{condition.getStringCondition("productCatalogId")},condition.getRows(), condition.getPage());
	}
	
	@Override
	public List<LovMember> getPermissionList(String id,String type) {
		String hql = " select o from CatalogPermissionRel r , LovMember o ,LovMember p where r.productCatalogId = p.id and r.orgId = o.id and p.groupId = 'PRODUCT_DATABASE' and o.groupId='ORG' and o.deleteFlag = 'N' and p.id = ? and r.type = ?";
		return baseDao.findEntity(hql,new Object[]{id,type});
	}
	
	@Override
	public void configPermission(String productCatalogId, String[] permissions, String type) {
		if(!StringUtil.isEmpty(productCatalogId)){
			baseDao.executeHQL(" delete from CatalogPermissionRel rp where rp.productCatalogId = ? and rp.type = ? ",new Object[]{productCatalogId,type});
			for(String permissionId : permissions){
				if(StringUtil.isEmpty(permissionId)){
					continue;
				}
				CatalogPermissionRel rp = new CatalogPermissionRel();
				rp.setProductCatalogId(productCatalogId);
				rp.setOrgId(permissionId);
				rp.setType(type);
				baseDao.save(rp);
			}
		}
	}

	@Override
	public List<LovMember> getMyProductCatalog(String positionId, String orgId) {
		String sql = " select distinct p from CatalogPermissionRel r , LovMember o ,LovMember p where r.productCatalogId = p.id and r.orgId = o.id and p.groupId = 'PRODUCT_DATABASE' and o.groupId='ORG' and o.deleteFlag = 'N' and (r.orgId = ? or r.orgId = ?)";
			sql += " order by p.level,p.no asc ";
		return baseDao.findEntity(sql,new Object[]{positionId,orgId}); 
	}

	@Override
	public void saveFile(String catalogId, List<IUploadFile> files, String employeeId) {
		for(IUploadFile uf : files){
			saveFile(catalogId, uf, employeeId);
		}
	}
	
}
