package com.ibm.kstar.api.product;

import java.util.List;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.product.KstarProductWorkFlow;

public interface IProductProcesService {
	
	void delete(String processId);
	
	public void save(KstarProductWorkFlow kp, UserObject user);
	
	public List<KstarProductWorkFlow> getList(String processId);
}
