package com.ibm.kstar.impl.product.maintain;

import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.dao.HqlUtil;
import org.xsnake.web.dao.utils.FilterObject;
import org.xsnake.web.dao.utils.HqlObject;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.StringUtil;
import org.xsnake.xflow.api.workflow.IXflowProcessServiceWrapper;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.product.maintain.IProdMaintainService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.team.ITeamService;
import com.ibm.kstar.entity.product.maintain.ProdAttrModLine;
import com.ibm.kstar.entity.product.maintain.ProdCatalogModLine;
import com.ibm.kstar.entity.product.maintain.ProdInfoMaintain;
import com.ibm.kstar.entity.product.maintain.ProdSaleStatusModLine;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class ProdMaintainServiceImpl implements IProdMaintainService{
	@Autowired
	BaseDao baseDao;
	
	@Autowired
    ITeamService teamService;
	
	@Autowired
	private ILovMemberService lovMemberService;
	
	@Autowired
	IXflowProcessServiceWrapper xflowProcessServiceWrapper;
	
	@Override
	public String getMaintainCode(){
		String connum="";

		String sql = "{ ? = call CRM_P_PRODUCT_PUB.gen_maintenance_code(?)}";

		Object[] result = baseDao.executeProcedure(sql, new BaseDao.ProcedureParam[]{
				new BaseDao.OutProcedureParam(Types.VARCHAR),
				new BaseDao.InProcedureParam(-1),
		});
		
		connum = (String)result[0];
		return connum;
	}

	@Override
	public IPage getProdMaintainPage(PageCondition condition) {
		FilterObject filterObject = condition.getFilterObject(ProdInfoMaintain.class);
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}

	@Override
	public IPage getProdAttrModLinePage(PageCondition condition) {
		String hql = "select a from ProdAttrModLine a where a.maintainId = ?";
		
		return baseDao.search(hql, new Object[]{condition.getCondition("maintainId")}, condition.getRows(), condition.getPage());
	}

	@Override
	public IPage getProdCatalogModLinePage(PageCondition condition) {
		String hql = "select a from ProdCatalogModLine a where a.maintainId = ?";
		
		return baseDao.search(hql, new Object[]{condition.getCondition("maintainId")}, condition.getRows(), condition.getPage());
	}

	@Override
	public void saveProdMaintain(ProdInfoMaintain maintain, UserObject userObject) throws Exception {
		if (userObject != null) {
			maintain.setCreatedById(userObject.getEmployee().getId());
			maintain.setCreatedPosId(userObject.getPosition().getId());
			maintain.setCreatedOrgId(userObject.getOrg().getId());
			maintain.setUpdatedById(userObject.getEmployee().getId());
		}
		maintain.setCreatedAt(new Date());
		maintain.setUpdatedAt(new Date());
		maintain.setStatus("1000");
		baseDao.save(maintain);
		
		teamService.addPosition(userObject.getPosition().getId(), userObject.getEmployee().getId(),
                IConstants.PROD_INFO_MAINTAIN, maintain.getId());
		
		this.saveProdMaintainDetail(maintain, userObject);
	}
	
	/**
	 * 保存产品维护申请单详情
	 * @param maintain
	 * @param userObject
	 * @throws Exception
	 */
	private void saveProdMaintainDetail(ProdInfoMaintain maintain, UserObject userObject) throws Exception {
		Object[] args = {maintain.getId()};
		List<Object> list = new ArrayList<Object>();
		
		//保存产品属性变更
		List<Map<Object, Object>> attrModLinesList = maintain.getAttrModLinesList();
        if (attrModLinesList != null) {
            for (Map<Object, Object> map : attrModLinesList) {
                list.add(map.get("id"));
            }
        }
		String hql1 = "select a from ProdAttrModLine a where a.maintainId = ?";
		List<ProdAttrModLine> attrModLines = baseDao.findEntity(hql1, args);
		for(ProdAttrModLine attrModLine : attrModLines){
			if (list == null || list.size() <= 0 || !list.contains(attrModLine.getId())) {
				baseDao.deleteById(ProdAttrModLine.class, attrModLine.getId());
			}
		}
		if (attrModLinesList != null) {
            for (Map<Object, Object> map : attrModLinesList) {
            	ProdAttrModLine attrModLine = (ProdAttrModLine)BeanUtils.convertMap(ProdAttrModLine.class,map);
            	attrModLine.setMaintainId(maintain.getId());
            	attrModLine.setUpdatedAt(new Date());
            	attrModLine.setUpdatedById(userObject.getEmployee().getId());
            	
            	ProdAttrModLine oldAttrModLine;
            	
            	if (StringUtil.isEmpty(attrModLine.getId())) {
            		attrModLine.setCreatedById(userObject.getEmployee().getId());
            		attrModLine.setCreatedPosId(userObject.getPosition().getId());
            		attrModLine.setCreatedOrgId(userObject.getOrg().getId());
            		
            		oldAttrModLine = attrModLine;
            	}else{
            		oldAttrModLine = baseDao.get(ProdAttrModLine.class, attrModLine.getId());
            		if (oldAttrModLine == null) {
            			attrModLine.setId(null);
            			oldAttrModLine = attrModLine;
                    } else {
                        //将 orderLines的属性复制到 oldOrderLines
                        BeanUtils.copyPropertiesIgnoreNull(attrModLine, oldAttrModLine);
                    }
            	}
            	
            	baseDao.saveOrUpdate(oldAttrModLine);
            }
		}
		
		
		list = new ArrayList<Object>();
		
		//保存产品所属目录变更
		List<Map<Object, Object>> cataLogModLinesList= maintain.getCataLogModLinesList();
		if (cataLogModLinesList != null) {
            for (Map<Object, Object> map : cataLogModLinesList) {
                list.add(map.get("id"));
            }
        }
		String hql2 = "select a from ProdCatalogModLine a where a.maintainId = ?";
		List<ProdCatalogModLine> catalogModLines = baseDao.findEntity(hql2, args);
		for(ProdCatalogModLine catalogModLine : catalogModLines){
			if (list == null || list.size() <= 0 || !list.contains(catalogModLine.getId())) {
				baseDao.deleteById(ProdCatalogModLine.class, catalogModLine.getId());
			}
		}
		if (cataLogModLinesList != null) {
            for (Map<Object, Object> map : cataLogModLinesList) {
            	ProdCatalogModLine catalogModLine = (ProdCatalogModLine)BeanUtils.convertMap(ProdCatalogModLine.class,map);
            	catalogModLine.setMaintainId(maintain.getId());
            	catalogModLine.setUpdatedAt(new Date());
            	catalogModLine.setUpdatedById(userObject.getEmployee().getId());
            	
            	ProdCatalogModLine oldCatalogModLine;
            	
            	if (StringUtil.isEmpty(catalogModLine.getId())) {
            		catalogModLine.setCreatedById(userObject.getEmployee().getId());
            		catalogModLine.setCreatedPosId(userObject.getPosition().getId());
            		catalogModLine.setCreatedOrgId(userObject.getOrg().getId());
            		
            		oldCatalogModLine = catalogModLine;
            	}else{
            		oldCatalogModLine = baseDao.get(ProdCatalogModLine.class, catalogModLine.getId());
            		if (oldCatalogModLine == null) {
            			catalogModLine.setId(null);
            			catalogModLine.setCreatedById(userObject.getEmployee().getId());
                		catalogModLine.setCreatedPosId(userObject.getPosition().getId());
                		catalogModLine.setCreatedOrgId(userObject.getOrg().getId());
            			oldCatalogModLine = catalogModLine;
                    } else {
                        //将 orderLines的属性复制到 oldOrderLines
                        BeanUtils.copyPropertiesIgnoreNull(catalogModLine, oldCatalogModLine);
                    }
            	}
            	
            	baseDao.saveOrUpdate(oldCatalogModLine);
            }
		}
		
		list = new ArrayList<Object>();
		
		//保存产品所属目录变更
		List<Map<Object, Object>> saleStatusModLinesList= maintain.getSaleStatusModLinesList();
		if (saleStatusModLinesList != null) {
            for (Map<Object, Object> map : saleStatusModLinesList) {
                list.add(map.get("id"));
            }
        }
		String hql3 = "select a from ProdSaleStatusModLine a where a.maintainId = ?";
		List<ProdSaleStatusModLine> saleStatusModLines = baseDao.findEntity(hql3, args);
		for(ProdSaleStatusModLine saleStatusModLine : saleStatusModLines){
			if (list == null || list.size() <= 0 || !list.contains(saleStatusModLine.getId())) {
				baseDao.deleteById(ProdSaleStatusModLine.class, saleStatusModLine.getId());
			}
		}
		if (saleStatusModLinesList != null) {
            for (Map<Object, Object> map : saleStatusModLinesList) {
            	ProdSaleStatusModLine saleStatusModLine = (ProdSaleStatusModLine)BeanUtils.convertMap(ProdSaleStatusModLine.class,map);
            	saleStatusModLine.setMaintainId(maintain.getId());
            	saleStatusModLine.setUpdatedAt(new Date());
            	saleStatusModLine.setUpdatedById(userObject.getEmployee().getId());
            	
            	ProdSaleStatusModLine oldSaleStatusModLine;
            	
            	if (StringUtil.isEmpty(saleStatusModLine.getId())) {
            		saleStatusModLine.setCreatedById(userObject.getEmployee().getId());
            		saleStatusModLine.setCreatedPosId(userObject.getPosition().getId());
            		saleStatusModLine.setCreatedOrgId(userObject.getOrg().getId());
            		
            		oldSaleStatusModLine = saleStatusModLine;
            	}else{
            		oldSaleStatusModLine = baseDao.get(ProdSaleStatusModLine.class, saleStatusModLine.getId());
            		if (oldSaleStatusModLine == null) {
            			saleStatusModLine.setId(null);
            			oldSaleStatusModLine = saleStatusModLine;
                    } else {
                        //将 orderLines的属性复制到 oldOrderLines
                        BeanUtils.copyPropertiesIgnoreNull(saleStatusModLine, oldSaleStatusModLine);
                    }
            	}
            	
            	baseDao.saveOrUpdate(oldSaleStatusModLine);
            }
		}
	}

	@Override
	public ProdInfoMaintain getProdInfoMaintain(String maintainId) {
		// TODO Auto-generated method stub
		return baseDao.get(ProdInfoMaintain.class, maintainId);
	}

	@Override
	public void updateProdMaintain(ProdInfoMaintain maintain, UserObject userObject) throws Exception {
		// TODO Auto-generated method stub
		ProdInfoMaintain oldMaintain = this.getProdInfoMaintain(maintain.getId());
		if(oldMaintain == null){
			throw new AnneException(ProdInfoMaintain.class.getName() + " 没有找到要更新的数据");
		}
		
		 BeanUtils.copyPropertiesIgnoreNull(maintain, oldMaintain);
		
		if (userObject != null) {
			oldMaintain.setUpdatedById(userObject.getEmployee().getId());
		}
		oldMaintain.setUpdatedAt(new Date());
		baseDao.update(oldMaintain);
		
		
		this.saveProdMaintainDetail(oldMaintain, userObject);
	}

	@Override
	public IPage getSaleStatusModLinePage(PageCondition condition) {
		String hql = "select a from ProdSaleStatusModLine a where a.maintainId = ?";
		
		return baseDao.search(hql, new Object[]{condition.getCondition("maintainId")}, condition.getRows(), condition.getPage());
	}

	@Override
	public void updateProcessStatus(String businessKey, String processStatus) {
		// TODO Auto-generated method stub
		ProdInfoMaintain maintain = this.getProdInfoMaintain(businessKey);
		if(maintain == null){
			throw new AnneException(ProdInfoMaintain.class.getName() + " 没有找到要更新的数据");
		}
		maintain.setProcessStatus(processStatus);
		baseDao.update(maintain);
	}

	@Override
	public void startMaintainProcess(ProdInfoMaintain maintain, UserObject userObject) throws Exception {
		maintain.setProcessStatus(IConstants.PROD_INFO_MAINTAIN_PROC_STUTAS_20);
		ProdInfoMaintain oldMaintain = this.getProdInfoMaintain(maintain.getId());
		if(oldMaintain == null){
			throw new AnneException(ProdInfoMaintain.class.getName() + " 没有找到要更新的数据");
		}
		
		 BeanUtils.copyPropertiesIgnoreNull(maintain, oldMaintain);
		
		if (userObject != null) {
			oldMaintain.setUpdatedById(userObject.getEmployee().getId());
		}
		oldMaintain.setUpdatedAt(new Date());
		baseDao.update(oldMaintain);
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Map<String, String> varmap = new HashMap<String, String>();
        varmap.put("title", "产品信息维护申请 - " + maintain.getMaintainCode() + "|" + userObject.getEmployee().getName() + "|" + dateFormat.format(new Date()));
        varmap.put("app", IConstants.PROD_INFO_MAINTAIN_PROC);
        varmap.put("SalesCenter", lovMemberService.getSaleCenter(userObject.getOrg().getId()));
        
        String model = lovMemberService.getFlowCodeByAppCode(IConstants.PROD_INFO_MAINTAIN_PROC);
        
        xflowProcessServiceWrapper.start(model, maintain.getId(), userObject, varmap);
	}

	@Override
	public void deleteProdInfoMaintain(String id, UserObject userObject) {
		ProdInfoMaintain maintain = this.getProdInfoMaintain(id);
		if(maintain == null){
			throw new AnneException(ProdInfoMaintain.class.getName() + " 没有找到要删除的数据");
		}
		if(!IConstants.PROD_INFO_MAINTAIN_PROC_STUTAS_10.equals(maintain.getProcessStatus())){
			throw new AnneException("表单有未完流程,不能删除!");
		}
		
		if (userObject != null) {
			maintain.setUpdatedById(userObject.getEmployee().getId());
		}
		maintain.setUpdatedAt(new Date());
		maintain.setStatus("1100");
		baseDao.update(maintain);
	}

	@Override
	public List<ProdAttrModLine> getProdAttrModLineList(String maintainId) {
		String hql = "select a from ProdAttrModLine a where a.maintainId = ?";
		
		return baseDao.findEntity(hql, new Object[]{maintainId});
	}
}
