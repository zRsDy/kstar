package com.ibm.kstar.impl.channel;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.remote.server.Remote;
import org.xsnake.web.dao.BaseDao;

import com.ibm.kstar.entity.channel.GiftManage;
import com.ibm.kstar.entity.channel.SerialNumber;
import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;

@Service
@Remote
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class SerialNumberService {
	@Autowired
	BaseDao baseDao;
	
	public synchronized String getSerialNumber3(String module){
		SerialNumber serialObj = (SerialNumber) baseDao.findUniqueEntity("select s from SerialNumber s where s.module = ?", module);
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		String currentDate = format.format(new Date());
		if(serialObj == null){
			serialObj = new SerialNumber();
			serialObj.setModule(module);
			serialObj.setDate(currentDate);
			serialObj.setSerial(1L);
		}else if(currentDate.equals(serialObj.getDate())){
			long tempSerial = serialObj.getSerial();
			tempSerial++;
			serialObj.setSerial(tempSerial);
		}else{
			serialObj.setDate(currentDate);
			serialObj.setSerial(1L);
		}
		baseDao.saveOrUpdate(serialObj);
		String serialStr = "00"+serialObj.getSerial();
		int serialLength = serialStr.length();
		String serialNumber = serialObj.getDate() + serialStr.substring(serialLength-3, serialLength);
		return serialNumber;
	}
	
	public synchronized String getSerialNumber4(String module){
		SerialNumber serialObj = (SerialNumber) baseDao.findUniqueEntity("select s from SerialNumber s where s.module = ?", module);
		SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
		String currentDate = format.format(new Date());
		if(serialObj == null){
			serialObj = new SerialNumber();
			serialObj.setModule(module);
			serialObj.setDate(currentDate);
			serialObj.setSerial(1L);
		}else if(currentDate.equals(serialObj.getDate())){
			long tempSerial = serialObj.getSerial();
			tempSerial++;
			serialObj.setSerial(tempSerial);
		}else{
			serialObj.setDate(currentDate);
			serialObj.setSerial(1L);
		}
		baseDao.saveOrUpdate(serialObj);
		String serialStr = "000"+serialObj.getSerial();
		int serialLength = serialStr.length();
		String serialNumber = serialObj.getDate() + serialStr.substring(serialLength-4, serialLength);
		return serialNumber;
	}
	
	public synchronized String getGiftSerial(GiftManage gift){
		SerialNumber serialObj = (SerialNumber) baseDao.findUniqueEntity("select s from SerialNumber s where s.module = 'giftManage' and s.code = ?", gift.getGiftType());
		if(serialObj != null){
			long tempSerial = serialObj.getSerial();
			tempSerial++;
			serialObj.setSerial(tempSerial);
		}else{
			serialObj = new SerialNumber();
			serialObj.setModule("giftManage");
			serialObj.setCode(gift.getGiftType());
			serialObj.setSerial(1L);
		}
		baseDao.saveOrUpdate(serialObj);
		String serialStr = "00000"+serialObj.getSerial();
		int serialLength = serialStr.length();
		LovMember lov = new LovMember();
		Object obj = CacheData.getInstance().get(gift.getGiftType());
		if(obj!=null){
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
		}
		String serialNumber = lov.getCode() + serialStr.substring(serialLength-6, serialLength);
		return serialNumber;
	}
}