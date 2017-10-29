package com.ibm.kstar.entity.custom.vo;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.entity.custom.CustomAddressInfo;
import com.ibm.kstar.entity.custom.CustomErpInfo;

public class CustomErpAddress implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private CustomErpInfo customErpInfo;
	private CustomAddressInfo customAddressInfo;

	public CustomErpAddress(CustomErpInfo customErpInfo,
			CustomAddressInfo customAddressInfo) {
		this.customErpInfo = customErpInfo;
		this.customAddressInfo = customAddressInfo;
		this.id = customErpInfo.getId();
	}

	public CustomErpAddress(List<Object> list) {
		this.customErpInfo = (CustomErpInfo) list.get(0);
		this.customAddressInfo = (CustomAddressInfo) list.get(1);
		this.id = customErpInfo.getId();
	}

	public String getId() {
		return id;
	}

	public String getCustomId() {
		return customErpInfo.getCustomId();
	}

	public String getCorpErpUnit() {
		return customErpInfo.getCorpErpUnit();
	}
	
	public Object getCorpErpUnitGrid() {
		
		return customErpInfo.getCorpErpUnitGrid();
	}
	
    public String getCorpErpStatusName() {
		
    	return customErpInfo.getCorpErpStatusName();
	}

	public String getCorpLeadedAddress() {
		return customErpInfo.getCorpLeadedAddress();
	}

	public String getCorpLeadedComment() {
		return customErpInfo.getCorpLeadedComment();
	}

	public String getCorpErpStatus() {
		return customErpInfo.getCorpErpStatus();
	}

	public String getArea() {
		StringBuilder sb = new StringBuilder();
		LovMember lov = ((LovMember) CacheData.getInstance().get(
				customAddressInfo.getLayer1()));
		if (lov == null) {
			return sb.toString();
		} else {
			sb.append(lov.getName());
		}

		lov = ((LovMember) CacheData.getInstance().get(
				customAddressInfo.getLayer2()));
		if (lov == null) {
			return sb.toString();
		} else {
			sb.append("/").append(lov.getName());
		}

		lov = ((LovMember) CacheData.getInstance().get(
				customAddressInfo.getLayer3()));
		if (lov == null) {
			return sb.toString();
		} else {
			sb.append("/").append(lov.getName());
		}

		lov = ((LovMember) CacheData.getInstance().get(
				customAddressInfo.getLayer4()));
		if (lov == null) {
			if(StringUtils.isNotEmpty(customAddressInfo.getCustomAddress())){
				sb.append("/").append(customAddressInfo.getCustomAddress());
			}
			return sb.toString();
		} else {
			sb.append("/").append(lov.getName());
		}
		
		if(StringUtils.isNotEmpty(customAddressInfo.getCustomAddress())){
			sb.append("/").append(customAddressInfo.getCustomAddress());
		}
		
		return sb.toString();
	}

	public Object getCustomAddressTypeGrid() {

		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(
				customAddressInfo.getCustomAddressType());
		if (obj != null) {
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		
		return lov;
	}

	public Object getCustomAddressUseGrid() {

		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(
				customAddressInfo.getCustomAddressUse());
		if (obj != null) {
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		return lov;
	}

	public String getCustomAddress() {
		return customAddressInfo.getCustomAddress();
	}

	public String getCustomAddressType() {
		return customAddressInfo.getCustomAddressType();
	}

	public String getCustomAddressPhone() {
		return customAddressInfo.getCustomAddressPhone();
	}

	public String getCustomAddressFax() {
		return customAddressInfo.getCustomAddressFax();
	}

	public String getCustomAddressUse() {
		return customAddressInfo.getCustomAddressUse();
	}

	public String getCustomAddressValid() {
		return customAddressInfo.getCustomAddressValid();
	}

	public String getLayer1() {
		return customAddressInfo.getLayer1();
	}

	public String getLayer2() {
		return customAddressInfo.getLayer2();
	}

	public String getLayer3() {
		return customAddressInfo.getLayer3();
	}

	public String getLayer4() {
		return customAddressInfo.getLayer4();
	}

	public String getLayer5() {
		return customAddressInfo.getLayer5();
	}

	public CustomErpInfo getCustomErpInfo() {
		return customErpInfo;
	}

	public CustomAddressInfo getCustomAddressInfo() {
		return customAddressInfo;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setCustomErpInfo(CustomErpInfo customErpInfo) {
		this.customErpInfo = customErpInfo;
	}

	public void setCustomAddressInfo(CustomAddressInfo customAddressInfo) {
		this.customAddressInfo = customAddressInfo;
	}

}