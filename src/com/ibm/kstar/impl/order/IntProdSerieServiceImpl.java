package com.ibm.kstar.impl.order;

import com.ibm.kstar.api.order.IIntProdSerieService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.order.DeliveryHeader;
import com.ibm.kstar.entity.order.IntProdSerie;
import com.ibm.kstar.permission.utils.Permission;
import com.ibm.kstar.permission.utils.PermissionUtil;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@Remote
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class IntProdSerieServiceImpl implements IIntProdSerieService {
	@Autowired
	BaseDao baseDao;

	@Override
	public void saveIntProdSerie(IntProdSerie intProdSerie)
			throws AnneException {
		baseDao.save(intProdSerie);
	}

	
	
	@Override
	public void updateIntProdSerie(IntProdSerie intProdSerie)
			throws AnneException {
		IntProdSerie oldIntProdSerie = baseDao.get(IntProdSerie.class,
				intProdSerie.getId());
		if (oldIntProdSerie == null) {
			throw new AnneException(IIntProdSerieService.class.getName()
					+ " saveIntProdSerie : 没有找到要更新的数据");
		}
		BeanUtils.copyPropertiesIgnoreNull(intProdSerie, oldIntProdSerie);
		baseDao.update(oldIntProdSerie);
	}

	@Override
	public IPage queryIntProdSeries(PageCondition condition,UserObject userObject)
			throws AnneException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		StringBuffer sb = new StringBuffer();
		/*sb.append(" select p from IntProdSerie p,");
		sb.append(" DeliveryReceipt d ");
		sb.append(" where p.receiveNumber = d.receiptCode ");
		sb.append(" and d.createdOrgId = ? ");*/

		Permission permission = DeliveryHeader.class.getAnnotation(Permission.class);
		String businessType = permission.businessType();
		String permissionHQL = PermissionUtil.getPermissionHQL(null, "de.createdById", "de.createdPosId", "de.createdOrgId", "de.id", userObject, businessType);

		//language=HQL
		sb.append("select p from IntProdSerie p where p.receiveNumber in ( "
				+ "select d.receiptCode from DeliveryReceipt d where d.deliveryCode in("
				+ "select de.deliveryCode from DeliveryHeader de where " + permissionHQL + " )"
				+ ") ");

		String seriesName = condition.getStringCondition("seriesName");
		String receiveNumber = condition.getStringCondition("receiveNumber");
		String itemMode = condition.getStringCondition("itemMode");
		String itemNumber = condition.getStringCondition("itemNumber");
		String shipToCustomer = condition.getStringCondition("shipToCustomer");
		String externalShipmentNumber = condition.getStringCondition("externalShipmentNumber");
		String orderNumber = condition.getStringCondition("orderNumber");
		String anzhuangCompletedDateStart = condition.getStringCondition("anzhuangCompletedDateStart");
		String anzhuangCompletedDateEnd = condition.getStringCondition("anzhuangCompletedDateEnd");
		String receivePrtDateStart = condition.getStringCondition("receivePrtDateStart");
		String receivePrtDateEnd = condition.getStringCondition("receivePrtDateEnd");
		String creationDateStart = condition.getStringCondition("creationDateStart");
		String creationDateEnd = condition.getStringCondition("creationDateEnd");

		List<Object> args = new ArrayList<>();
		if(StringUtil.isNotEmpty(seriesName)){
			sb.append(" and p.seriesName like ? ");
			args.add("%"+seriesName+"%");
		}
		if(StringUtil.isNotEmpty(receiveNumber)){
			sb.append(" and p.receiveNumber like ? ");
			args.add("%" +receiveNumber+ "%");
		}
		if(StringUtil.isNotEmpty(itemMode)){
			sb.append(" and p.itemMode like ? ");
			args.add("%" +itemMode+ "%");
		}
		if(StringUtil.isNotEmpty(itemNumber)){
			sb.append(" and p.itemNumber like ? ");
			args.add("%"+itemNumber+"%");
		}
		if(StringUtil.isNotEmpty(itemNumber)){
			sb.append(" and p.shipToCustomer like ? ");
			args.add("%" +shipToCustomer+ "%");
		}
		if(StringUtil.isNotEmpty(externalShipmentNumber)){
			sb.append(" and p.externalShipmentNumber like ? ");
			args.add("%" +externalShipmentNumber+ "%");
		}
		if(StringUtil.isNotEmpty(orderNumber)){
			sb.append(" and p.orderNumber = ? ");
			args.add(Integer.parseInt(orderNumber));
		}
		if(StringUtil.isNotEmpty(anzhuangCompletedDateStart)){
			sb.append(" and p.anzhuangCompletedDate >= ? ");
			try {
				args.add(sdf.parse(anzhuangCompletedDateStart));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(StringUtil.isNotEmpty(anzhuangCompletedDateEnd)){
			sb.append(" and p.anzhuangCompletedDate <= ? ");
			try {
				args.add(sdf.parse(anzhuangCompletedDateEnd));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(StringUtil.isNotEmpty(receivePrtDateStart)){
			sb.append(" and p.receivePrtDate >= ? ");
			try {
				args.add(sdf.parse(receivePrtDateStart));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(StringUtil.isNotEmpty(receivePrtDateEnd)){
			sb.append(" and p.receivePrtDate <= ? ");
			try {
				args.add(sdf.parse(receivePrtDateEnd));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(StringUtil.isNotEmpty(creationDateStart)){
			sb.append(" and p.creationDate >= ? ");
			try {
				args.add(sdf.parse(creationDateStart));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(StringUtil.isNotEmpty(creationDateEnd)){
			sb.append(" and p.creationDate <= ? ");
			try {
				args.add(sdf.parse(creationDateEnd));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		sb.append(" order by p.lastUpdateDate desc ");
		return baseDao.search(sb.toString(),args.toArray(), condition.getRows(), condition.getPage());
		
//		FilterObject filterObject = condition.getFilterObject(IntProdSerie.class);
//		filterObject.addOrderBy("lastUpdateDate", "desc");
//		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
//		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());

	}

	@Override
	public void deleteIntProdSerie(String intProdSerieId) throws AnneException {
		baseDao.deleteById(IntProdSerie.class, intProdSerieId);
	}

	@Override
	public IntProdSerie getIntProdSerie(String intProdSerieId)
			throws AnneException {
		return baseDao.get(IntProdSerie.class, intProdSerieId);
	}

	@Override
	public List<List<Object>> exportSelectedContrLists(String[] ids) throws AnneException {
		List<List<Object>> lstOut = new ArrayList<List<Object>>();
		addHead(lstOut);
		List<IntProdSerie> intProdSeries = getSelectedContrList(ids);
		for (IntProdSerie intProdSerie : intProdSeries) {
			List<Object> lstIn = new ArrayList<Object>();
			lstIn.add(intProdSerie.getSeriesName());
			lstIn.add(intProdSerie.getItemNumber());              
			lstIn.add(intProdSerie.getItemMode());
			lstIn.add(intProdSerie.getItemDesc());
			lstIn.add(intProdSerie.getReceiveNumber());
			lstIn.add(intProdSerie.getConsignee());
			lstIn.add(intProdSerie.getShipToCustomer());
			lstIn.add(intProdSerie.getReceAddress());
			lstIn.add(intProdSerie.getOrderNumber());
			lstIn.add(intProdSerie.getExternalShipmentNumber());
			lstIn.add(intProdSerie.getAnzhuangCompletedDate());
			lstIn.add(intProdSerie.getReceivePrtDate());
			lstIn.add(intProdSerie.getUom());
			lstIn.add(intProdSerie.getComments());
			lstOut.add(lstIn);
		} 
		return lstOut;
	}

	private List<IntProdSerie> getSelectedContrList(String[] ids) {
		String idsStr = "";
		for(String id : ids){
			idsStr += "'" + id + "',";
		}
		idsStr= idsStr.substring(0, idsStr.length()-1); 
		StringBuffer hql = new StringBuffer(" from IntProdSerie where 1 = 1 and id in (" +idsStr+")");
		return baseDao.findEntity(hql.toString());
	}

	private void addHead(List<List<Object>> lstOut) {
		List<Object> lstHead = new ArrayList<Object>();
		lstHead.add("序列号");
		lstHead.add("物料编号");
		lstHead.add("产品型号");
		lstHead.add("产品描述");
		lstHead.add("签收单号");
		lstHead.add("收货人");
		lstHead.add("客户名称");
		lstHead.add("收货地址");
		lstHead.add("订单编号");
		lstHead.add("出货单号");
		lstHead.add("实际出货日期");
		lstHead.add("签收单打印日期");
		lstHead.add("单位");
		lstHead.add("备注");
		lstOut.add(lstHead);	
	}	
}