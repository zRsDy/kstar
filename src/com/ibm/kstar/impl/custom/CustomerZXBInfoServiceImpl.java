package com.ibm.kstar.impl.custom;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.dao.HqlUtil;
import org.xsnake.web.dao.utils.FilterObject;
import org.xsnake.web.dao.utils.HqlObject;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.BeanUtils;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.custom.ICustomerZXBInfoService;
import com.ibm.kstar.entity.custom.CustomZXBInfo;
import com.sinosure.exchange.edi.po.BuyerCodeApplyInfo;
import com.sinosure.exchange.edi.service.ZXBClient;


@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class CustomerZXBInfoServiceImpl implements ICustomerZXBInfoService {
	@Autowired
	BaseDao baseDao;
	
	@Override
	public IPage query(PageCondition condition) {
		IPage page = null;
		FilterObject filterObject = condition.getFilterObject(CustomZXBInfo.class);
		filterObject.addOrderBy("corpSerialNo", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		page = baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
		@SuppressWarnings("unchecked")
		List<CustomZXBInfo> list = (List<CustomZXBInfo>) page.getList();
		for(CustomZXBInfo c : list){
			c.setStatusDesc();
		}
		return page;
	}
	
	@Override
	public CustomZXBInfo createCustomInfor() {
		CustomZXBInfo customInfo = new CustomZXBInfo();
		customInfo.setId(null);
		customInfo.setCorpSerialNo(this.getSerialNo());
		return customInfo;
	}
	
	@Override
	public void saveCustomInfo(CustomZXBInfo customInfo) throws AnneException {
		customInfo.setApply_status("Drfat");
		baseDao.save(customInfo);
	}

	@Override
	public void updateCustomInfo(CustomZXBInfo customInfo) throws AnneException {
		CustomZXBInfo oldCustomInfo = baseDao.get(CustomZXBInfo.class, customInfo.getId());
		if ( null == oldCustomInfo ) {
			throw new AnneException(ICustomInfoService.class.getName() + " 没有找到要更新的数据");
		}
		BeanUtils.copyPropertiesIgnoreNull(customInfo, oldCustomInfo);
		baseDao.update(oldCustomInfo);
	}

	@Override
	public void deleteCustomInfo(String id) throws AnneException {
		baseDao.executeHQL(" delete CustomZXBInfo where id = ? ",new Object[]{id});
	}

	@Override
	public CustomZXBInfo getCustomInfoById(String id) throws AnneException {
		CustomZXBInfo custom = null;
		custom = baseDao.get(CustomZXBInfo.class, id);
		custom.setStatusDesc();
		return custom;
	}

	@Override
	public List<CustomZXBInfo> getCustomInfoByCode(String code) throws AnneException {
		String c = new StringBuffer().append("%").append(code).append("%").toString();
		List<CustomZXBInfo> cfs = baseDao.findEntity(
				" from CustomZXBInfo where corpSerialNo like ? or chnName like ?  engName like ?",
				new Object[] { c, c, c });
		return cfs;
	}
	
	private String getSerialNo() {
		String sql = "SELECT SEQ_ZXB_SERIAL_NO.nextval FROM dual";
		Object no = baseDao.findBySql(sql).get(0);
		return no.toString();
	}

	@Override
	public String copyCustomInfo(CustomZXBInfo customInfo) throws AnneException {
		CustomZXBInfo newCustom = new CustomZXBInfo();
		newCustom = baseDao.get(CustomZXBInfo.class, customInfo.getId());
		BeanUtils.copyPropertiesIgnoreNull(customInfo, newCustom);
		newCustom.setCorpSerialNo(this.getSerialNo());
		newCustom.setApply_status("Drfat");
		newCustom.setAppr_status(null);
		newCustom.setAppr_date(null);
		newCustom.setAppr_comments(null);
		newCustom.setZxb_buyer_code(null);
		newCustom.setItem1("中信保买家申请信息复制于源流水单号: " + customInfo.getCorpSerialNo());
		newCustom.setItem4(null);
		newCustom.setItem5(null);
		baseDao.save(newCustom);
		return newCustom.getCorpSerialNo();
	}

	@Override
	public String doBuyerCodeApply(CustomZXBInfo customInfo) throws AnneException {
		customInfo = baseDao.get(CustomZXBInfo.class, customInfo.getId());
		if(null == customInfo){
			throw new AnneException(ICustomInfoService.class.getName() + " 没有找到要提交的数据");
		}
		List<BuyerCodeApplyInfo> list = new ArrayList<BuyerCodeApplyInfo>();
		BuyerCodeApplyInfo bci = new BuyerCodeApplyInfo();
		BeanUtils.copyProperties(customInfo, bci);
		list.add(bci);
		try {
			ZXBClient client =  new ZXBClient();
			client.callBuyerCodeApply(list);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AnneException(e.getMessage());
		}
		customInfo.setApply_status("Sumited");
		baseDao.save(customInfo);
		return customInfo.getCorpSerialNo();
	}
	
}
