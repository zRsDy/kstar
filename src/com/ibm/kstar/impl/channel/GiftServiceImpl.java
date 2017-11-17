package com.ibm.kstar.impl.channel;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.remote.server.Remote;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.BeanUtils;

import com.ibm.kstar.api.channel.IGiftService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.channel.GiftManage;
import com.ibm.kstar.impl.BaseServiceImpl;

@Service
@Remote
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class GiftServiceImpl extends BaseServiceImpl implements IGiftService {
	@Autowired
	BaseDao baseDao;
	
	@Autowired
	SerialNumberService serialNumberService;
	
	@Override
	public IPage queryGifts(PageCondition condition) {
		List<Object> args = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("select g from GiftManage g,LovMember m where g.giftType = m.id");
		this.addQueryCondition(condition, args, hql, "giftCode", "g.giftCode", "like");
		this.addQueryCondition(condition, args, hql, "giftDesc", "g.giftDesc", "like");
		this.addQueryCondition(condition, args, hql, "giftType", "g.giftType", "=");
		hql.append(" order by g.id desc");
		return baseDao.search(hql.toString(), args.toArray(), condition.getRows(), condition.getPage());
	}

	@Override
	public void saveOrUpdateGift(GiftManage gift, UserObject user) {
		if(gift.getId() != null){
			GiftManage giftData = baseDao.get(GiftManage.class, gift.getId());
			BeanUtils.copyPropertiesIgnoreNull(gift,giftData);
			giftData.setRecordInfor(true, user);
			baseDao.update(giftData);
		}else{
			gift.setGiftCode(serialNumberService.getGiftSerial(gift));
			gift.setInventoryQuantity(gift.getInboundQuantity());
			gift.setRecordInfor(false, user);
			baseDao.save(gift);
		}
	}

	@Override
	public GiftManage queryGift(String id) {
		return baseDao.get(GiftManage.class, id);
	}

	@Override
	public void delete(GiftManage giftManage) {
		baseDao.delete(giftManage);
	}
	
}