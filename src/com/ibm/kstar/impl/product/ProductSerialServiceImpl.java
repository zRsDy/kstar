package com.ibm.kstar.impl.product;

import com.ibm.kstar.api.product.IProductSerialService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.product.KstarProSerial;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.dao.BaseDao;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class ProductSerialServiceImpl implements IProductSerialService {
	
	@Autowired
	BaseDao baseDao;

	@Override
	public synchronized String queryProductCBy(String proType, String dataString, boolean updated) {
		
		String sql = " from KstarProSerial t where t.proType=? and t.dataString=?";
		List<Object> args = new ArrayList<Object>();
		args.add(proType);
		args.add(dataString);

		KstarProSerial serial = baseDao.findUniqueEntity(sql, args.toArray());
		int count;
		String result;
		if (serial == null || serial.getCount() == null) {
			serial = new KstarProSerial();
			serial.setProType(proType);
			serial.setDataString(dataString);
			result = "001";
			count = 1;
		}else {
			count = serial.getCount() + 1;
			
			if(count < 10 ){
				result = "00" + count;
			}else if(count >= 10 && count <100){
				result = "0" + count;
			}else if(count >= 100 && count <= 999){
				result = String.valueOf(count);
			}else{
				result = String.valueOf(count);
			}
		}

		if (updated) {
			serial.setCount(count);
			baseDao.saveOrUpdate(serial);
		}
		return result;
	}

	@Override
	public String queryProductCBy(String proType, String dataString) {
		return this.queryProductCBy(proType, dataString, false);
	}

	@Override
	public void save(String vCode, UserObject user) {

		
		if(StringUtils.isNotEmpty(vCode) && vCode.length() == 12){
			String type = vCode.substring(0, 1);
			String time = vCode.substring(1, 9);
			String count = vCode.substring(9, 12);
			KstarProSerial kp = null;

			if("001".equalsIgnoreCase(count)){
				kp = new KstarProSerial();
				kp.setProType(type);
				kp.setDataString(time);
				kp.setCount(1);
			}else{
				kp = this.queryProductCByC(type, time);
				
				if(kp != null)
					kp.setCount(kp.getCount() + 1);
			}
			
			baseDao.saveOrUpdate(kp);
		}
	}
	
	@Override
	public void ecrSave(String  vCode, UserObject user) {

		if(StringUtils.isNotEmpty(vCode) && vCode.length() == 15){
			String type = vCode.substring(0, 3);
			String time = vCode.substring(4, 12);
			String count = vCode.substring(12, 15);
			KstarProSerial kp = null;

			if("001".equalsIgnoreCase(count)){
				kp = new KstarProSerial();
				kp.setProType(type);
				kp.setDataString(time);
				kp.setCount(1);
			}else{
				kp = this.queryProductCByC(type, time);
				
				if(kp != null)
					kp.setCount(kp.getCount() + 1);
			}
			
			baseDao.saveOrUpdate(kp);
		}
	}
	
	//报价编号保存
	@Override
	public void bjSave(String  vCode, UserObject user) {

		if(StringUtils.isNotEmpty(vCode) && vCode.length() == 19){
			String type = vCode.substring(6, 8);
			String time = vCode.substring(8, 16);
			String count = vCode.substring(16, 19);
			KstarProSerial kp = null;
			
			
			if("001".equalsIgnoreCase(count)){
				kp = new KstarProSerial();
				kp.setProType(type);
				kp.setDataString(time);
				kp.setCount(1);
			}else{
				kp = this.queryProductCByC(type, time);
				
				if(kp != null)
					kp.setCount(kp.getCount() + 1);
			}
			
			baseDao.saveOrUpdate(kp);
		}
	
	}

	private KstarProSerial queryProductCByC(String proType, String dataString){
		
		String sql = " from KstarProSerial t where t.proType=? and t.dataString=?";
		List<Object> args = new ArrayList<Object>();
		args.add(proType);
		args.add(dataString);
		
		KstarProSerial reValue = baseDao.findUniqueEntity(sql,args.toArray());
		
		return reValue;
	}
	
}
