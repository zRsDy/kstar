package com.ibm.kstar.impl.custom;

import java.util.ArrayList;
import java.util.Date;
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
import com.ibm.kstar.api.custom.IQuotaNoLCZXBInfoService;
import com.ibm.kstar.entity.common.doc.KstarAttachment;
import com.ibm.kstar.entity.custom.NoLcQuotaZXBInfo;
import com.sinosure.exchange.edi.po.Attafile;
import com.sinosure.exchange.edi.po.NoLcQuotaApplyInfoV2;
import com.sinosure.exchange.edi.service.ZXBClient;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class QuotaNoLCZXBInfoServiceImpl implements IQuotaNoLCZXBInfoService {
	@Autowired
	BaseDao baseDao;
	
	@Override
	public IPage query(PageCondition condition) {
		IPage page = null;
		FilterObject filterObject = condition.getFilterObject(NoLcQuotaZXBInfo.class);
		filterObject.addOrderBy("corpSerialNo", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		page = baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
		@SuppressWarnings("unchecked")
		List<NoLcQuotaZXBInfo> list = (List<NoLcQuotaZXBInfo>) page.getList();
		for(NoLcQuotaZXBInfo noLcQuota : list){
			noLcQuota.setStatusDesc();
		}
		return page;
	}

	@Override
	public NoLcQuotaZXBInfo createInfor() {
		NoLcQuotaZXBInfo noLcQuota = new NoLcQuotaZXBInfo();
		noLcQuota.setId(null);
		noLcQuota.setCorpSerialNo(this.getQuotaNoLCSerialNo());
		return noLcQuota;
	}

	private String getQuotaNoLCSerialNo() {
		String sql = "SELECT SEQ_ZXB_NOLCQUT_NO.nextval FROM dual";
		Object no = baseDao.findBySql(sql).get(0);
		return no.toString();
	}

	@Override
	public void saveInfo(NoLcQuotaZXBInfo noLCQuota) throws AnneException {
		noLCQuota.setApply_status("Drfat");
		List<KstarAttachment> list = this.getAttachs(noLCQuota.getId());
		noLCQuota.setFilenum(new Long(list.size()));
		baseDao.save(noLCQuota);
	}

	@Override
	public void updateInfo(NoLcQuotaZXBInfo noLCQuota) throws AnneException {
		NoLcQuotaZXBInfo oldInfo = baseDao.get(NoLcQuotaZXBInfo.class, noLCQuota.getId());
		if ( null == oldInfo ) {
			throw new AnneException(IQuotaNoLCZXBInfoService.class.getName() + " 没有找到要更新的数据");
		}
		BeanUtils.copyPropertiesIgnoreNull(noLCQuota, oldInfo);
		List<KstarAttachment> list = this.getAttachs(noLCQuota.getId());
		oldInfo.setFilenum(new Long(list.size()));
		baseDao.update(oldInfo);
	}

	@Override
	public void deleteInfo(String id) throws AnneException {
		baseDao.executeHQL(" delete NoLcQuotaZXBInfo where id = ? ",new Object[]{id});
		baseDao.executeHQL(" delete KstarAttachment where 1=1 and bizType = 'ZXB_ATTACHS' and bizId = ? ",new Object[]{id});
	}
	
	@Override
	public NoLcQuotaZXBInfo getInfoById(String id) throws AnneException {
		NoLcQuotaZXBInfo noLcQuota = null;
		noLcQuota = baseDao.get(NoLcQuotaZXBInfo.class, id);
		noLcQuota.setStatusDesc();
		return noLcQuota;
	}

	@Override
	public List<NoLcQuotaZXBInfo> getInfoByCode(String code) throws AnneException {
		String c = new StringBuffer().append("%").append(code).append("%").toString();
		List<NoLcQuotaZXBInfo> noLCQuotas = baseDao.findEntity(
				" from NoLcQuotaZXBInfo where corpSerialNo like ? or buyerChnName like ?  buyerEngName like ?",
				new Object[] { c, c, c });
		return noLCQuotas;
	}

	@Override
	public String copyInfo(NoLcQuotaZXBInfo noLCQuota) throws AnneException {
		NoLcQuotaZXBInfo new_nlq = new NoLcQuotaZXBInfo();
		new_nlq = baseDao.get(NoLcQuotaZXBInfo.class, noLCQuota.getId());
		BeanUtils.copyPropertiesIgnoreNull(noLCQuota, new_nlq);
		new_nlq.setCorpSerialNo(this.getQuotaNoLCSerialNo());
		new_nlq.setApply_status("Drfat");
		new_nlq.setApplyTime(null);
		new_nlq.setApproveFlag(null);
		new_nlq.setNotifyTime(null);
		new_nlq.setUnAcceptReason(null);
		new_nlq.setFilenum(null);
		new_nlq.setItem1("中信保买家申请信息复制于源流水单号: " + new_nlq.getCorpSerialNo());
		baseDao.save(new_nlq);
		return new_nlq.getCorpSerialNo();
	}

	@Override
	public String doNoLcQuotaApplyV2(NoLcQuotaZXBInfo noLCQuota) throws AnneException {
		noLCQuota = baseDao.get(NoLcQuotaZXBInfo.class, noLCQuota.getId());
		if(null == noLCQuota.getCorpSerialNo()){
			throw new AnneException(IQuotaNoLCZXBInfoService.class.getName() + " 没有找到要提交的数据");
		}
		List<NoLcQuotaApplyInfoV2> list = new ArrayList<NoLcQuotaApplyInfoV2>();
		NoLcQuotaApplyInfoV2 noa = new NoLcQuotaApplyInfoV2();
		BeanUtils.copyProperties(noLCQuota, noa);
		List<KstarAttachment> attchs = this.getAttachs(noLCQuota.getId());
		if(null != attchs && !attchs.isEmpty()){
			noLCQuota.setFilenum(new Long(attchs.size()));
			noa.setFilenum(new Long(attchs.size()));
			List<Attafile> lafs = new ArrayList<Attafile>();
			for(KstarAttachment attch : attchs){
				Attafile att = new Attafile();
				att.setCorpserialno(this.getQuotaNoLCAttachNo());
				att.setSrcCorpserialno(noLCQuota.getCorpSerialNo());
				att.setFilename(attch.getDocFulnm());
				att.setFileFullName(attch.getDocPath());
				att.setType("010101");
				lafs.add(att);
			}
			noa.setAfs(lafs);
		}
		list.add(noa);
		try {
			ZXBClient client = new ZXBClient();
			client.callNoLcQuotaApplyV2(list);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AnneException(e.getMessage());
		}
		noLCQuota.setApply_status("Sumited");
		noLCQuota.setApplyTime(new Date());
		baseDao.save(noLCQuota);
		return noLCQuota.getCorpSerialNo();
	}
	
	private List<KstarAttachment> getAttachs(String bizId) throws AnneException {
		StringBuffer hql = new StringBuffer(" from KstarAttachment where 1=1 and bizType = 'ZXB_ATTACHS' and bizId = ? ");
		return baseDao.findEntity(hql.toString(), bizId);
	}
	
	private String getQuotaNoLCAttachNo() {
		String sql = "SELECT SEQ_ZXB_NOLCQUT_ATTCH.nextval FROM dual";
		Object no = baseDao.findBySql(sql).get(0);
		return no.toString();
	}
	
}
