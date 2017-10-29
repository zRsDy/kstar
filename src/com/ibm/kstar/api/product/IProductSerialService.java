package com.ibm.kstar.api.product;

import com.ibm.kstar.api.system.permission.UserObject;

public interface IProductSerialService {

	public String queryProductCBy(String proType, String dataString);

	String queryProductCBy(String p, String dateTime, boolean update);
	
	public void save(String vCode, UserObject user);
	
	public void ecrSave(String vCode, UserObject user);

	void bjSave(String vCode, UserObject user);


}
