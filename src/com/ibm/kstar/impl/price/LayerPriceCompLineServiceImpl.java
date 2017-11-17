package com.ibm.kstar.impl.price;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

import com.ibm.kstar.api.price.ILayerPriceCompLineService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.product.PriceLayCompareLine;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class LayerPriceCompLineServiceImpl implements
		ILayerPriceCompLineService {
	@Autowired
	BaseDao baseDao;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public IPage query(PageCondition condition) throws Exception {
		FilterObject filterObject = condition.getFilterObject();
		filterObject.setClazz(PriceLayCompareLine.class);
		filterObject.addOrderBy("id", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		IPage page = baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
		Collections.sort(page.getList(), new Comparator(){
			@Override
			public int compare(Object o1, Object o2) {
				PriceLayCompareLine l1 = (PriceLayCompareLine) o1;
				PriceLayCompareLine l2 = (PriceLayCompareLine) o2;
				return l1.getLayLineNameCode().compareTo(l2.getLayLineNameCode());
			}
			
		});
		return page;
	}

	@Override
	public void save(PriceLayCompareLine doc, UserObject user) {
		//lst不可能为null，至少为[]
		List<PriceLayCompareLine> lst = baseDao.findEntity("select t from PriceLayCompareLine t,LovMember m where t.layLineName = m.id and t.headerId = ? order by m.code", new Object[]{doc.getHeaderId()});
		int size = lst.size();
		if(doc.getId() == null){	//新增时，size >= 0
			if(size > 0){
				BigDecimal maxRate = lst.get(size-1).getDiscountRate();
				if(doc.getDiscountRate().compareTo(maxRate) > -1){
					throw new AnneException("默认折扣率必须低于"+maxRate);
				}
			}
			doc.setRecordInfor(false, user);
			baseDao.save(doc);
		}else{//修改时， size > 0
			for(int i=0; i<size; i++){  
				PriceLayCompareLine oldDoc = lst.get(i);
				if(doc.getId().equals(lst.get(i).getId())){
					if(i-1 >= 0){
						BigDecimal maxRate = lst.get(i-1).getDiscountRate();
						if(doc.getDiscountRate().compareTo(maxRate) > -1){
							throw new AnneException("默认折扣率必须低于"+maxRate);
						}
					}
					if(i+1 < size){
						BigDecimal minRate = lst.get(i+1).getDiscountRate();
						if(doc.getDiscountRate().compareTo(minRate) < 1){
							throw new AnneException("默认折扣率必须高于"+minRate);
						}
					}
					BeanUtils.copyPropertiesIgnoreNull(doc,oldDoc);
					oldDoc.setRecordInfor(true, user);
					baseDao.update(oldDoc);
					break;
				}
			}
		}
	}

	@Override
	public PriceLayCompareLine queryLpcById(String id) {
		PriceLayCompareLine kp = baseDao.get(PriceLayCompareLine.class, id);
		return kp;
	}

	@Override
	public List<PriceLayCompareLine> queryLpcByHeadId(String headerId) {
		List<PriceLayCompareLine> reValue = baseDao.findEntity(" from PriceLayCompareLine where headerId = ?", headerId);
		return reValue;
	}

	@Override
	public void delete(String id) {
		PriceLayCompareLine kp = baseDao.get(PriceLayCompareLine.class, id);
		baseDao.delete(kp);
	}
	
	@Override
	public List<LovMember> selectApproveLayLine(Condition condition) {
		String sql = "select distinct m.opt_txt5 from sys_t_lov_member m where m.opt_txt5 is not null and m.group_code = 'APPROVE_POSITION_LEVEL' order by m.opt_txt5";
		Object findData =(Object) baseDao.findBySql(sql);
		@SuppressWarnings("unchecked")
		List<Object> r = (List<Object>) findData;
		List<LovMember> result = new ArrayList<LovMember>();
		for(Object obj : r){
			if(obj != null){
				String id = (String)obj;
				LovMember lov = new LovMember();
				lov.setId(id);
				lov.setName(id);
				result.add(lov);
			}
		}
		return result;
	}
}
