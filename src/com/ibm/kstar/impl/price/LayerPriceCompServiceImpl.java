package com.ibm.kstar.impl.price;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.api.price.ILayerPriceCompService;
import com.ibm.kstar.api.system.permission.IEmployeeService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.api.team.ITeamService;
import com.ibm.kstar.entity.product.PriceLayCompareHeader;
import com.ibm.kstar.entity.product.PriceLayCompareLine;
import com.ibm.kstar.impl.BaseServiceImpl;
import com.ibm.kstar.permission.utils.PermissionUtil;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class LayerPriceCompServiceImpl extends BaseServiceImpl implements ILayerPriceCompService {

	@Autowired
	BaseDao baseDao;
	@Autowired
	IEmployeeService employeeService;
	@Autowired
	protected ITeamService teamService;
	
	@Override
	public IPage query(PageCondition condition, UserObject user) throws Exception {
		List<Object> args = new ArrayList<>();
		StringBuilder sb = new StringBuilder("select p from PriceLayCompareHeader p,LovMember m where p.organization = m.id");
		
		String phql = PermissionUtil.getPermissionHQL(null, "p.createdById", "p.createdPosId", "p.createdOrgId", "p.id", user, ProcessConstants.LAYER_PRICE_COMP);
		sb.append(" and ").append(phql).append(" ");
		
		this.addQueryCondition(condition, args, sb, "layCompName", "p.layCompName", "like");
		this.addQueryCondition(condition, args, sb, "organization", "m.name", "like");
		this.addQueryCondition(condition, args, sb, "startDate", "p.startDate", ">=");
		this.addQueryCondition(condition, args, sb, "endDate", "p.endDate", "<=");
		sb.append(" order by p.id desc");
		IPage page = baseDao.search(sb.toString(), args.toArray(), condition.getRows(), condition.getPage());
		@SuppressWarnings("unchecked")
		List<PriceLayCompareHeader> list = (List<PriceLayCompareHeader>) page.getList();
		for(PriceLayCompareHeader compare : list){
			if(compare.getCreater() != null){
				Employee employee = employeeService.get(compare.getCreater());
				if(employee != null){
					compare.setCreaterName(employee.getName());
				}
			}
		}
		return page;
	}

	@Override
	public void save(PriceLayCompareHeader doc, UserObject user) {
		if(doc.getId() == null){
			doc.setRecordInfor(false, user);
			baseDao.save(doc);
			//加销售团队
			teamService.addPosition(user.getPosition().getId(),user.getEmployee().getId(),ProcessConstants.LAYER_PRICE_COMP,doc.getId());
		}else{
			doc.setRecordInfor(true, user);
			baseDao.update(doc);
		}
	}

	@Override
	public PriceLayCompareHeader queryLpcById(String id) {
		PriceLayCompareHeader kp = baseDao.get(PriceLayCompareHeader.class, id);
		if(kp != null && kp.getCreater() != null){
			kp.setCreaterName(employeeService.get(kp.getCreater()).getName());
		}
		return kp;
	}

	@Override
	public PriceLayCompareHeader queryLpcHeadByOrg(String organization) {
		return baseDao.findUniqueEntity(" from PriceLayCompareHeader where organization = ?", organization);
	}

	@Override
	public void delete(String id) {
		PriceLayCompareHeader kp = baseDao.get(PriceLayCompareHeader.class, id);
		List<PriceLayCompareLine> lineLst = baseDao.findEntity("from PriceLayCompareLine l where l.headerId = ?",kp.getId());
		baseDao.deleteAll(lineLst);
		baseDao.delete(kp);
	}

}
