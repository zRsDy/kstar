package com.ibm.kstar.cache;

import java.util.List;

import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.conf.ApplicationContextUtil;
import com.ibm.kstar.entity.contract.Contract;
import com.ibm.kstar.entity.custom.CustomAddressInfo;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.order.DeliveryReceipt;
import com.ibm.kstar.entity.order.OrderHeader;
import com.ibm.kstar.entity.product.KstarProduct;


public class CacheUtils {

	static BaseDao baseDao;
	
	static {
		baseDao = (BaseDao)ApplicationContextUtil.getBean("baseDao");
	}
	
	/**
	 * 根据合同ID获取合同信息
	 * @param id
	 * @return
	 */
	public static Contract getContractById(String id){
		if (StringUtil.isNullOrEmpty(id)) {
			return null;
		}
		return baseDao.get(Contract.class, id);
	}
	
	
	/**
	 * 根据地址ID获取客户地址
	 * @param id
	 * @return
	 */
	public static CustomAddressInfo getCustomAddressInfoById(String id){
		if (StringUtil.isNullOrEmpty(id)) {
			return null;
		}
		return baseDao.get(CustomAddressInfo.class, id);
	}
	
	/**
	 * 根据合同ID获取关联的订单
	 * @param contrId
	 * @return
	 */
	public static OrderHeader getOrderHeaderByContrId(String contrId){
		if (StringUtil.isNullOrEmpty(contrId)) {
			return null;
		}
		List<OrderHeader> list = baseDao.findEntity("from OrderHeader where sourceId = ? ",contrId);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 根据产品ID获取产品对象
	 * @param id 产品id
	 * @return 产品对象
	 */
	public static KstarProduct getProduct(String id){
		if (StringUtil.isNullOrEmpty(id)) {
			return null;
		}
		KstarProduct obj = null;
		/*
		obj = (KstarProduct)CacheData.getInstance().get(id);
		if(obj!=null)
			return obj;
		 */
		obj = baseDao.get(KstarProduct.class, id);
		/*
		if(obj!=null)
			CacheData.getInstance().set(id, obj);
		*/
		return obj;
	}
	/**
	 * 根据产品ID获取产品名称
	 * @param id 产品id
	 * @return 产品名称
	 */
	public static String getProductName(String id){
		KstarProduct p = getProduct(id);
		if(p==null)
			return null;
		return p.getProductName();
	}
	/**
	 * 根据产品ID获取产品型号
	 * @param id 产品id
	 * @return 产品型号
	 */
	public static String getProductModel(String id){
		KstarProduct p = getProduct(id);
		if(p==null)
			return null;
		return p.getProModel();
	}
	
	/**
	 * 根据客户ID获取客户对象 
	 * @param id 客户id
	 * @return 客户对象
	 */
	public static CustomInfo getCustomer(String id){
		if (StringUtil.isNullOrEmpty(id)) {
			return null;
		}
		CustomInfo obj = null;
		/*
		obj = (CustomInfo)CacheData.getInstance().get(id);
		if(obj!=null)
			return obj;
		*/
		obj = baseDao.get(CustomInfo.class, id);
		/*
		if(obj!=null)
			CacheData.getInstance().set(id, obj);
		*/
		return obj;
	}
	/**
	 * 根据客户ID获取客户名称
	 * @param id 客户id
	 * @return 客户名称
	 */
	public static String getCustomerName(String id){
		CustomInfo c = getCustomer(id);
		if(c==null)
			return null;
		return c.getCustomFullName();
	}
	
	
	/**
	 * 产品序列号取值
	 * @param receiptCode
	 * @return
	 */
	public static DeliveryReceipt getDeliveryReceipt(String receiptCode){
		if (StringUtil.isNullOrEmpty(receiptCode)) {
			return null;
		}
		List<DeliveryReceipt> list = baseDao.findEntity("from DeliveryReceipt where receiptCode = ? ",receiptCode);
		if(list.size() > 0){
			return list.get(0);
		}
		return null;
	}
	
}
