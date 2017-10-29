package com.ibm.kstar.impl.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.product.IPriceService;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class PriceServiceImpl implements IPriceService{
	@Autowired
	BaseDao baseDao;
	
	@Autowired
	ICustomInfoService customInfoService;

	@Override
	public IPage querySql(PageCondition condition) throws Exception  {
		StringBuffer hql = new StringBuffer("select");
		hql.append(" l.c_pid \"id\",");
		hql.append(" h.c_price_head_name \"priceTableName\",");
		hql.append(" c.c_custom_full_name \"clientName\",");
		hql.append(" mc.lov_name \"currencyName\",");
		hql.append(" h.dt_start_date \"startDate\",");
		hql.append(" h.dt_end_date \"endDate\",");
		hql.append(" mo.lov_name \"organizationName\",");
		hql.append(" l.n_catalog_price \"catalogPrice\"");
		hql.append(" from CRM_T_PRICE_LINE l");
		hql.append(" left join CRM_T_PRICE_HEAD h on l.c_price_head_id = h.c_pid");
		hql.append(" left join crm_t_custom_info c on h.c_client_id = c.c_pid");
		hql.append(" left join sys_t_lov_member mc on h.c_currency = mc.row_id");
		hql.append(" left join sys_t_lov_member mo on h.c_currency = mo.row_id");
		hql.append(" where l.c_pro_id = '");
		hql.append((String)condition.getCondition("id"));
		hql.append( "' ");
		hql.append(" order by l.c_pid desc");
		IPage ip = baseDao.searchBySql4Map(hql.toString(), condition.getRows(), condition.getPage());
		return	ip;
	}
}


