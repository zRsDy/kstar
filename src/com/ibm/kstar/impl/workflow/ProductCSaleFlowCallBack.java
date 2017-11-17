package com.ibm.kstar.impl.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.dao.BaseDao;

import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.entity.product.KstarProduct;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class ProductCSaleFlowCallBack extends BaseFlowCallBack {
	
	@Autowired
	BaseDao baseDao;
	
	@Override
	public String processPath() {
		return "/product/csalePocess.html";
	}
	
	@Override
	String getEntityName() {
		return "KstarProduct";
	}

	@Override
	String getStatusName() {
		return "csaleProcessStatus";
	}

	@Override
	String getGroupCode() {
		return ProcessConstants.PRODUCT_CSALE_PROC;
	}
	
    @Override
    public void onComplete(String businessKey, String flowModule, String processInstanceId, String comment) {
    	KstarProduct kstarProduct = baseDao.get(KstarProduct.class, businessKey);
    	kstarProduct.setCsaleProcessStatus(getLovId(ProcessConstants.PROCESS_STATUS_03));
    	kstarProduct.setCsaleStatus(kstarProduct.getNewCsaleStatus());
        baseDao.update(kstarProduct);
    }
}
