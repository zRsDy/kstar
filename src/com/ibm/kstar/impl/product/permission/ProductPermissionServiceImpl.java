package com.ibm.kstar.impl.product.permission;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.page.PageImpl;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.api.product.permission.IProductPermissionService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.product.permission.PermissionOptRel;
import com.ibm.kstar.entity.product.permission.PermissionRel;
import com.ibm.kstar.entity.report.KstarPositionDep;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class ProductPermissionServiceImpl implements IProductPermissionService{

	@Autowired
	BaseDao baseDao;
	
	@Override
	public List<LovMember> getPermissionList(String id) {
		String hql = " select o from PermissionRel r , LovMember o ,LovMember p where r.productCatalogId = p.id and r.orgId = o.id and p.groupId = 'procatalog' and o.groupId='ORG' and o.deleteFlag = 'N' and p.id = ? ";
		return baseDao.findEntity(hql,new Object[]{id});
	}
	
	@Override
	public List<LovMember> getOptPermissionList(String id) {
		String hql = " select o from PermissionOptRel r , LovMember o ,LovMember p where r.productCatalogId = p.id and r.orgId = o.id and p.groupId = 'procatalog' and o.groupId='ORG' and o.deleteFlag = 'N' and p.id = ? ";
		return baseDao.findEntity(hql,new Object[]{id});
	}
	
	@Override
	public IPage getPermissionPage(PageCondition condition) {
		if(StringUtil.isEmpty(condition.getStringCondition("productCatalogId"))){
			return new PageImpl(new ArrayList<>(), 20, 1, 0);
		}
		String hql = " select o from PermissionRel r , LovMember o ,LovMember p where r.productCatalogId = p.id and r.orgId = o.id and p.groupId = 'procatalog' and o.groupId='ORG' and o.deleteFlag = 'N' and p.id = ? ";
		return baseDao.search(hql ,new Object[]{condition.getStringCondition("productCatalogId")},condition.getRows(), condition.getPage());
	}
	
	@Override
	public IPage getOptPermissionPage(PageCondition condition) {
		if(StringUtil.isEmpty(condition.getStringCondition("productCatalogId"))){
			return new PageImpl(new ArrayList<>(), 20, 1, 0);
		}
		String hql = " select o from PermissionOptRel r , LovMember o ,LovMember p where r.productCatalogId = p.id and r.orgId = o.id and p.groupId = 'procatalog' and o.groupId='ORG' and o.deleteFlag = 'N' and p.id = ? ";
		return baseDao.search(hql ,new Object[]{condition.getStringCondition("productCatalogId")},condition.getRows(), condition.getPage());
	}

	@Override
	public List<LovMember> getAllPermissionList() {
		List<LovMember> list = baseDao.findEntity(" select o from LovMember o where o.groupId = 'ORG' and o.deleteFlag = 'N' ");
		return list;
	}

	@Override
	public List<LovMember> getJobSelectPermissionList(String id) {
		String hql = " select p from PermissionRel r , LovMember o ,LovMember p where r.productCatalogId = p.id and r.orgId = o.id and p.groupId = 'procatalog' and o.groupId='ORG' and o.deleteFlag = 'N' and r.orgId = ? ";
		List<LovMember> list = baseDao.findEntity(hql,new Object[]{id});
		return list;
	}
	
	@Override
	public List<LovMember> getReportSelectPermissionList(String id) {
		String hql = " select p from KstarPositionDep r , LovMember o ,LovMember p where r.depId = p.id and r.posId = o.id and p.groupId = 'ORG' and o.groupId='POSITION' and o.deleteFlag = 'N' and r.posId = ? ";
		List<LovMember> list = baseDao.findEntity(hql,new Object[]{id});
		return list;
	}
	
	@Override
	public void configPermission(String[] productCatalogIds, String[] permissions) {
		/*
		for(String productCatalogId : productCatalogIds){
			if(!StringUtil.isEmpty(productCatalogId)){
				baseDao.executeHQL(" delete from PermissionRel rp where rp.productCatalogId = ? ",new Object[]{productCatalogId});
			}
		}*/
		ArrayList productCatalogIdList = new ArrayList();
		
		/*for(String pid:permissions){
			if (StringUtil.isEmpty(pid)) {
				continue;
			}
			productCatalogIdList.add(pid);
		}
		for(String productCatalogId : productCatalogIds){
			if(!StringUtil.isEmpty(productCatalogId)&&productCatalogIdList.size()>0){
				String hql = "delete from PermissionRel rp where rp.productCatalogId = '"+productCatalogId+"' and rp.orgId in (:pid)";  
			    baseDao.getSessionFactory().getCurrentSession().createQuery(hql).setParameterList("pid", productCatalogIdList).executeUpdate();  
					//baseDao.executeHQL(" delete from PermissionRel rp where rp.productCatalogId = "+productCatalogId+" and rp.orgId in  ?  ",productCatalogIdList.toArray());
			}
		}*/
		
		ArrayList productCatalogIdsList = new ArrayList();
		
		for(String productCatalogId : productCatalogIds){
			if(!StringUtil.isEmpty(productCatalogId)){
				productCatalogIdsList.add(productCatalogId);
			}
		}
		if(productCatalogIdsList.size()>0) {
			for(String pid:permissions){
				if (StringUtil.isEmpty(pid)) {
					continue;
				}
				String hql = "delete from PermissionRel rp where rp.productCatalogId in (:productCatalogId) and rp.orgId = '"+pid+"')";  
			    baseDao.getSessionFactory().getCurrentSession().createQuery(hql).setParameterList("productCatalogId", productCatalogIdsList).executeUpdate();
			}
		}
		
		
		/* jian.xj 2017/10/11调整,为了解决设置目录权限不出现断层的问题*/
		/*for(String productCatalogId : productCatalogIds){
			if(!StringUtil.isEmpty(productCatalogId)){
				for(String pid:permissions){
					if (StringUtil.isEmpty(pid)) {
						continue;
					}
					baseDao.executeHQL(" delete from PermissionRel rp where rp.productCatalogId = "+productCatalogId+" and rp.orgId=? ",new Object[]{productCatalogId,pid});
				}
			}
		}*/
		
		this.baseDao.getSessionFactory().getCurrentSession().flush();
		for (String productCatalogId : productCatalogIds) {
			if (!StringUtil.isEmpty(productCatalogId)) {
				for (String permissionId : permissions) {
					if (StringUtil.isEmpty(permissionId)) {
						continue;
					}
					PermissionRel rp = new PermissionRel();
					rp.setProductCatalogId(productCatalogId);
					rp.setOrgId(permissionId);
					baseDao.save(rp);
				}
			}
		}
	}

	@Override
	public void configOptPermission(String[] productCatalogIds, String[] permissions) {
		for(String productCatalogId : productCatalogIds){
			if(!StringUtil.isEmpty(productCatalogId)){
				baseDao.executeHQL(" delete from PermissionOptRel rp where rp.productCatalogId = ? ",new Object[]{productCatalogId});
				for(String permissionId : permissions){
					if(StringUtil.isEmpty(permissionId)){
						continue;
					}
					PermissionOptRel rp = new PermissionOptRel();
					rp.setProductCatalogId(productCatalogId);
					rp.setOrgId(permissionId);
					baseDao.save(rp);
				}
			}
		}
	}

	/**
	 * 更新目录权限
	 */
	@Override
	public void updateProductPermission(String rightzTreeId, String leftzTreeId) throws AnneException{
		if(StringUtil.isNullOrEmpty(leftzTreeId)) {
			throw new AnneException("缺失岗位ID");
		}
		String[] rightzTreeIds = rightzTreeId.split(",");
		String hql = "delete from PermissionRel p where p.orgId = ? and  p.productCatalogId in (select lov.id from LovMember lov where  lov.groupId = 'procatalog') "; 
		baseDao.executeHQL(hql,new Object[]{leftzTreeId});
		for(String id:rightzTreeIds) {
			PermissionRel permissionRel = new PermissionRel();
			permissionRel.setOrgId(leftzTreeId);
			permissionRel.setProductCatalogId(id);
			baseDao.save(permissionRel);
		}
	}

	/**
	 * 更新报表权限
	 */
	@Override
	public void updateReportPermission(String rightzTreeId, String leftzTreeId) throws AnneException{
		if(StringUtil.isNullOrEmpty(leftzTreeId)) {
			throw new AnneException("缺失岗位ID");
		}
		String[] rightzTreeIds = rightzTreeId.split(",");
		String hql = "delete from KstarPositionDep p where p.posId = ? and  p.depId in (select lov.id from LovMember lov where  lov.groupId = 'ORG') "; 
		baseDao.executeHQL(hql,new Object[]{leftzTreeId});
		for(String id:rightzTreeIds) {
			KstarPositionDep kstarPositionDep = new KstarPositionDep();
			kstarPositionDep.setPosId(leftzTreeId);
			kstarPositionDep.setDepId(id);
			baseDao.save(kstarPositionDep);
		}
	}
}
