package com.ibm.kstar.impl.channel;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.remote.server.Remote;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.api.channel.IGiftApplyDetailService;
import com.ibm.kstar.api.system.permission.IEmployeeService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.channel.GiftApplyDetail;
import com.ibm.kstar.entity.channel.GiftManage;
import com.ibm.kstar.impl.BaseServiceImpl;

@Service
@Remote
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class GiftApplyDetailServiceImpl extends BaseServiceImpl implements IGiftApplyDetailService {
	@Autowired
	BaseDao baseDao;
	
	@Autowired
	IEmployeeService employeeService;
	
	@Autowired
	SerialNumberService serialNumberService;

	@Override
	public IPage queryDetails(PageCondition condition) {
		String searchKey = condition.getStringCondition("searchKey");
		String applyId = condition.getStringCondition("applyId");
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer("select"
				+" d.c_pid \"id\",g.c_gift_code \"giftCode\",g.c_description \"giftDesc\""
				+" ,m1.lov_name \"giftType\",m2.lov_name \"giftUnit\",d.c_apply_quantity \"applyQuantity\""
				+" ,g.c_price \"giftPrice\",d.c_apply_quantity*g.c_price \"sumMoney\""
				+" ,d.c_actual_give_quantity \"actualGiveQuantity\",d.c_actual_give_date \"actualGiveDate\""
				+" ,d.c_actual_get_quantity \"actualGetQuantity\",d.c_actual_get_date \"actualGetDate\""
				+" ,d.c_status \"status\",d.c_explain \"explain\""
				+" from crm_t_gift_apply_detail d"
				+" left join crm_t_gift_manage g on d.c_gift_id= g.c_pid"
				+" left join SYS_T_LOV_MEMBER m1 on g.c_type = m1.row_id"
				+" left join SYS_T_LOV_MEMBER m2 on g.c_unit = m2.row_id"
				+" where d.c_apply_id=?");
		args.add(applyId);
		if(StringUtil.isNotEmpty(searchKey)){
			hql.append(" and (g.c_gift_code like ? or g.c_description like ?)");
			args.add("%"+searchKey.trim()+"%");
			args.add("%"+searchKey.trim()+"%");
		}
		hql.append(" order by d.c_pid desc");
		return baseDao.searchBySql4Map(hql.toString(), args.toArray(), condition.getRows(), condition.getPage());
	}

	@Override
	public GiftApplyDetail queryDetail(String id) {
		GiftApplyDetail detail = baseDao.get(GiftApplyDetail.class, id);
		GiftManage giftManage = baseDao.get(GiftManage.class, detail.getGiftId());
		detail.setGiftManage(giftManage);
		return detail;
	}

	@Override
	public void addOrEdit(GiftApplyDetail detail, UserObject user) {
		if(detail.getId() != null){
			GiftApplyDetail detailData = baseDao.get(GiftApplyDetail.class,detail.getId());
			BeanUtils.copyPropertiesIgnoreNull(detail,detailData);
			detailData.setRecordInfor(true, user);
			baseDao.update(detailData);
		}else{
			detail.setStatus("未发货");
			detail.setRecordInfor(false, user);
			baseDao.save(detail);
		}
	}

	@Override
	public void delete(String[] ids) {
		for(String id : ids){
			baseDao.deleteById(GiftApplyDetail.class, id);
		}
	}

	@Override
	public void giveDetails(String[] ids, UserObject user) {
		for(String id : ids){
			GiftApplyDetail detail = baseDao.get(GiftApplyDetail.class, id);
			if(detail.getActualGiveQuantity() == null){
				detail.setActualGiveQuantity(detail.getApplyQuantity());
			}else if(detail.getActualGiveQuantity() > detail.getApplyQuantity()){
				throw new AnneException("实发数量不能大于申请数量！");
			}else if(detail.getActualGiveQuantity() < 0){
				throw new AnneException("实发数量不能小于0！");
			}
			if(detail.getActualGiveDate() == null){
				detail.setActualGiveDate(new Date());
			}
			detail.setStatus("已发货");
			detail.setRecordInfor(true, user);
			baseDao.update(detail);
			dealInventoryQuantity(ids);
		}
	}

	@Override
	public void getDetails(String[] ids, UserObject user) {
		for(String id : ids){
			GiftApplyDetail detail = baseDao.get(GiftApplyDetail.class, id);
			if(detail.getActualGetQuantity() == null){
				detail.setActualGetQuantity(detail.getActualGiveQuantity());
			}else if(detail.getActualGetQuantity() > detail.getActualGiveQuantity()){
				throw new AnneException("实到数量不能大于实发数量！");
			}else if(detail.getActualGetQuantity() < 0){
				throw new AnneException("实到数量不能小于0！");
			}
			if(detail.getActualGetDate() == null){
				detail.setActualGetDate(new Date());
			}
			detail.setStatus("已到货");
			detail.setRecordInfor(true, user);
			baseDao.update(detail);
		}
	}

	@Override
	public void updateColumnValue(String id, String column, String value, UserObject user) {
		GiftApplyDetail detail = baseDao.get(GiftApplyDetail.class, id);
		if("actualGiveQuantity".equals(column)){
			if(StringUtil.isNotEmpty(value)){
				detail.setActualGiveQuantity(Integer.parseInt(value));
			}
		}else if("actualGetQuantity".equals(column)){
			if(StringUtil.isNotEmpty(value)){
				detail.setActualGetQuantity(Integer.parseInt(value));
			}
		}else if("actualGiveDate".equals(column)){
			try {
				detail.setActualGiveDate(new SimpleDateFormat("yyyy-MM-dd").parse(value));
			} catch (ParseException e) {
				throw new AnneException("日期格式不对！");
			}
		}else if("actualGetDate".equals(column)){
			try {
				detail.setActualGetDate(new SimpleDateFormat("yyyy-MM-dd").parse(value));
			} catch (ParseException e) {
				throw new AnneException("日期格式不对！");
			}
		}
		detail.setRecordInfor(true, user);
		baseDao.update(detail);
	}
	
	private void dealInventoryQuantity(String[] ids){
		String sql = "update crm_t_gift_manage m set m.c_inventory_quantity = m.c_inventory_quantity + "
				+"(select nvl(sum(nvl(d.c_apply_quantity-d.c_actual_give_quantity,0)),0) from crm_t_gift_apply_detail d where d.c_gift_id = m.c_pid and d.c_apply_id in (###))";
		if(ids != null && ids.length > 0){
			StringBuilder idInStr = new StringBuilder();
			for(int i=0; i<ids.length; i++){
				idInStr.append("'"+ids[i]+"'");
				if(i < ids.length-1){
					idInStr.append(",");
				}
			}
			sql = sql.replace("###", idInStr.toString());
			baseDao.executeSQL(sql);
		}
	}
}