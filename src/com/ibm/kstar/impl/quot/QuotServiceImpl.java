/** 
 * Project Name:crm 
 * File Name:QuotServiceImpl.java 
 * Package Name:com.ibm.kstar.impl.quot 
 * Date:Jan 4, 20175:18:24 PM 
 * Copyright (c) 2017, ZW All Rights Reserved. 
 * 
 */

package com.ibm.kstar.impl.quot;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.contract.IContractService;
import com.ibm.kstar.api.order.IOrderService;
import com.ibm.kstar.api.org.IOrgTeamService;
import com.ibm.kstar.api.price.IPriceHeadService;
import com.ibm.kstar.api.product.IProLovService;
import com.ibm.kstar.api.product.IProductProcesService;
import com.ibm.kstar.api.product.IProductSerialService;
import com.ibm.kstar.api.quot.IQuotService;
import com.ibm.kstar.api.system.lov.ILovGroupService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.team.ITeamService;
import com.ibm.kstar.entity.bizopp.BusinessOpportunity;
import com.ibm.kstar.entity.product.*;
import com.ibm.kstar.entity.quot.*;
import com.ibm.kstar.entity.team.vo.TeamVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.dao.HqlUtil;
import org.xsnake.web.dao.utils.FilterObject;
import org.xsnake.web.dao.utils.HqlObject;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.StringUtil;
import org.xsnake.xflow.api.IHistoryService;
import org.xsnake.xflow.api.IProcessService;
import org.xsnake.xflow.api.model.HistoryActivityInstance;
import org.xsnake.xflow.api.model.HistoryProcessInstance;
import org.xsnake.xflow.api.model.ProcessInstance;
import org.xsnake.xflow.api.workflow.IXflowProcessServiceWrapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;


/** 
 * ClassName: QuotServiceImpl <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason: TODO ADD REASON(可选). <br/> 
 * date: Feb 25, 2017 4:28:39 PM <br/> 
 * 
 * @author ZW 
 * @version  
 * @since JDK 1.7 
 */ 
@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class QuotServiceImpl implements IQuotService,IConstants {
	@Autowired
	BaseDao baseDao;

	@Autowired
	ILovMemberService lovMemberService;
	
	@Autowired
	IProductProcesService productProcess;
	
	@Autowired
	IProductSerialService productSerialService;
	
	@Autowired
	IHistoryService historyService;
	
	@Autowired
	ILovGroupService lovGroupService;
	
	@Autowired
	IProLovService proLovService;
	
	@Autowired
	IPriceHeadService priceheadService;
	
	@Autowired
	protected ITeamService teamService;
	
	@Autowired
	protected IOrgTeamService orgTeamService;
	
	@Autowired
	IXflowProcessServiceWrapper processService;
	
	@Autowired
	IProcessService iprocessService;
	@Autowired
	private IContractService contractService;
	
	private String getDateTime(){
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

		String reValue = df.format(new Date());
		
		return reValue;
	}
	
	
	
	@Override
	public IPage query(PageCondition condition) {
		
		FilterObject filterObject = condition.getFilterObject(KstarQuot.class);
		filterObject.addOrderBy("quotCode", "desc");
		//filterObject.addCondition("isValid", "=", "1");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
		
		//StringBuffer hql = new StringBuffer(" from KstarQuot ");
		//return baseDao.search(hql.toString(),condition.getRows(), condition.getPage());
	}
	
	
	@Override
	public IPage queryQuot(String qid) {
		
		StringBuffer sql = new StringBuffer("select");
		sql.append(" c_pid \"id\",");
		sql.append(" c_qid \"quotCode\",");
		sql.append(" c_name \"quotName\",");
		sql.append(" c_status \"status\",");
		sql.append(" c_status \"quotVersion\",");
		sql.append(" c_customer_name \"customerName\",");
		sql.append(" c_salesrep \"salesRep\",");
		sql.append(" c_bo_name \"boName\",");
		sql.append(" c_price_list \"priceList\",");
		sql.append(" c_currency \"currency\",");
		sql.append(" n_amount \"amount\",");
		sql.append(" c_creator \"creator\",");
		sql.append(" dt_create_date \"createTime\",");
		sql.append(" c_bid_results \"bidResults\",");
		sql.append(" c_bid_results_reason \"bidResReason\" ");
		sql.append(" from crm_t_quotation_basic");
		sql.append(" where c_pid = ? ");
		
		
		List<Object> args = new ArrayList<Object>();
		args.add(qid);
		
		return baseDao.searchBySql4Map(sql.toString(), args.toArray(), 1, 1);
		

	}
	
	
	@Override
	public IPage queryCnt(PageCondition condition) {
		//String quotCode = baseDao.findUniqueEntity("select trim(quotCode) from KstarQuot where rownum =1 and id = ? ",condition.getStringCondition("qid"));
		String quotCode = condition.getStringCondition("qid");
		StringBuffer hql = new StringBuffer(" from KstarCntr where quotCode = ? ");
		List<Object> args = new ArrayList<Object>();
		args.add(quotCode);
		return baseDao.search(hql.toString(),args.toArray(),condition.getRows(), condition.getPage());
	}
	
	
	@Override
	public IPage queryMem(PageCondition condition) {
		//String quotCode = baseDao.findUniqueEntity("select trim(quotCode) from KstarQuot where rownum =1 and id = ? ",condition.getStringCondition("qid"));
		String quotCode = condition.getStringCondition("qid");
		StringBuffer hql = new StringBuffer(" from KstarMemInfo where CType = '0003' and quotCode = ? ");
		List<Object> args = new ArrayList<Object>();
		args.add(quotCode);
		return baseDao.search(hql.toString(),args.toArray(),condition.getRows(), condition.getPage());
	}
	
	@Override
	public IPage queryBiddcevl(PageCondition condition) {
		String quotCode = condition.getStringCondition("qid");
		StringBuffer hql = new StringBuffer(" from KstarBiddcevl where CType = '0003' and quotCode = ? ");
		List<Object> args = new ArrayList<Object>();
		args.add(quotCode);
		return baseDao.search(hql.toString(),args.toArray(),condition.getRows(), condition.getPage());
	}
	
	
	@Override
	public IPage queryAtt(PageCondition condition) {
		//String quotCode = baseDao.findUniqueEntity("select trim(quotCode) from KstarQuot where rownum =1 and id = ? ",condition.getStringCondition("qid"));
		String quotCode = condition.getStringCondition("qid");
		StringBuffer hql = new StringBuffer(" from KstarAtt where CType = '0003' and quotCode = ? ");
		List<Object> args = new ArrayList<Object>();
		args.add(quotCode);
		return baseDao.search(hql.toString(),args.toArray(),condition.getRows(), condition.getPage());
	}
	
	@Override
	public IPage queryPrjevl(PageCondition condition) {
		//String quotCode = baseDao.findUniqueEntity("select trim(quotCode) from KstarQuot where rownum =1 and id = ? ",condition.getStringCondition("qid"));
		String quotCode = condition.getStringCondition("qid");
		String ctype = condition.getStringCondition("typ");
		StringBuffer hql = new StringBuffer(" from KstarPrjEvl where CType = ? and quotCode = ? ");
		List<Object> args = new ArrayList<Object>();
		args.add(ctype);
		args.add(quotCode);
		return baseDao.search(hql.toString(),args.toArray(),condition.getRows(), condition.getPage());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public IPage queryPrjLst(PageCondition condition) {
		String quotid = condition.getStringCondition("qid");
		String parentId = condition.getStringCondition("parentId");
		String searchKey = condition.getStringCondition("searchKey");
		String ctype = condition.getStringCondition("typ");
		
		StringBuffer hql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		//StringBuffer hql = new StringBuffer(" from KstarPrjEvl where CType = '0003' and quotCode = ? ");
		//hql.append("select lst,lov from KstarPrjLst lst,LovMember lov where lov.groupId ='PRJLSTPRDCAT' and lov.id = lst.lvId and lst.CType = '0003' and lst.quotCode = ? ");
		//hql.append("select lst,lov from KstarPrjLst lst,LovMember lov where lov.groupId ='PRJLSTPRDCAT' and lov.leafFlag = 'Y' and lov.id = lst.lvId and lst.CType = '0003' and lov.memo = ? and lst.quotCode = ? ");
		//hql.append("select lst,lov from KstarPrjLst lst,LovMember lov where lov.groupId ='PRJLSTPRDCAT' and lov.id = lst.lvId and lst.CType = ? and lov.memo = ? and lst.quotCode = ? ");
		//args.add(ctype);
		
//		hql.append("select lst,lov from KstarPrjLst lst,LovMember lov where lov.groupId ='PRJLSTPRDCAT' and lov.id = lst.lvId and lov.memo = ? and lst.quotCode = ? ");
		hql.append("select lst from KstarPrjLst lst where lst.quotCode = ? ");
		
		args.add(quotid);
//		args.add(quotid);
		
//		if (searchKey!=null){
//			hql.append("  and ( lst.prdNm like ? or lst.prdTyp like ? ) ");
//			args.add("%"+searchKey+"%");
//			args.add("%"+searchKey+"%");
//		}
		
//		if (parentId!=null){
//			hql.append(" and lov.path like ? ");
//			args.add("%"+parentId+"%");
//		}
//		if (searchKey!=null){
//			hql.append("  and ( lov.name like ? or lov.code like ? ) ");
//			args.add("%"+searchKey+"%");
//			args.add("%"+searchKey+"%");
//		}
		
		IPage page = baseDao.search(hql.toString(),args.toArray(),condition.getRows(), condition.getPage());
//		List<Object[]> list = (List<Object[]>) page.getList();
//		((PageImpl)page).setList(BeanUtils.castList(PrjLstDtl.class,list));
		
//		((PageImpl)page).setList(BeanUtils.castList(KstarPrjLst.class,list));
		
		
		

		
		return page;
	}
	
	
	@Override
	public IPage queryPginf(PageCondition condition) {
		//String quotCode = baseDao.findUniqueEntity("select trim(quotCode) from KstarQuot where rownum =1 and id = ? ",condition.getStringCondition("qid"));
		String quotCode = condition.getStringCondition("qid");
		String cType = condition.getStringCondition("cType");
		List<Object> args = new ArrayList<Object>();
		args.add(cType);
		args.add(quotCode);
		
		StringBuffer hql = new StringBuffer(" from KstarPgInf where CType = ? and quotCode = ? ");
		return baseDao.search(hql.toString(),args.toArray(),condition.getRows(), condition.getPage());
	}
	
	
	
	@Override
	public void saveQuot(KstarQuot quot,UserObject current_user) throws AnneException {

		
		String dateTime =  getDateTime();
		String tempName = "KSTAR-BJ" + dateTime + productSerialService.queryProductCBy("BJ", dateTime);
		if(StringUtil.isNullOrEmpty(quot.getQuotCode())){
			quot.setQuotCode(tempName);
			productSerialService.bjSave(quot.getQuotCode(),current_user);
		}
		quot.setCreateTime(new Date());
		
		if(StringUtil.isEmpty(quot.getQuotCode())){
			throw new AnneException("报价单编号不能为空");
		}
		if(StringUtil.isEmpty(quot.getQuotVersion())){
			throw new AnneException("报价单版本不能为空");
		}
		if(StringUtil.isEmpty(quot.getIsProReview())){
			throw new AnneException("是否需工程评审不能为空");
		}
		if(StringUtil.isEmpty(quot.getIsBidPro())){
			throw new AnneException("是否投标项目不能为空");
		}
		
		//权限字段
		quot.setCreatedOrgId(StringUtil.isNotEmpty(current_user.getOid())? current_user.getOid(): current_user.getOrg().getId());
		quot.setCreatedById(StringUtil.isNotEmpty(current_user.getEid())? current_user.getEid(): current_user.getEmployee().getId());
		quot.setCreatedPosId(StringUtil.isNotEmpty(current_user.getPid())? current_user.getPid(): current_user.getPosition().getId());
		quot.setCreatedAt(new Date());

		baseDao.save(quot);

		teamService.addPosition(
				StringUtil.isNotEmpty(current_user.getPid())? current_user.getPid(): current_user.getPosition().getId(),
				StringUtil.isNotEmpty(current_user.getEid())? current_user.getEid(): current_user.getEmployee().getId(),
				"QUOTINF",
				quot.getId());
		
		orgTeamService.configPrimaryOrg(quot.getId(), "QUOTINF", quot.getCreatedOrgId());
		
	}
	
	
	@Override
	public void saveBaseinf(KstarBaseInf baseinf,String qid,String cType) throws AnneException {
		
		baseinf = new KstarBaseInf();
		
		if(StringUtil.isEmpty(baseinf.getQuotCode())){
			baseinf.setQuotCode(qid);
		}
		if(StringUtil.isEmpty(baseinf.getCType())){
			baseinf.setCType(cType);
		}
		//baseinf.setIfups("0");
		
		baseDao.save(baseinf);
		
	}
	
	
	@Override
	public void saveAftsale(KstarAftSale aftsale,String qid,String cType) throws AnneException {
		
		aftsale = new KstarAftSale();
		
		if(StringUtil.isEmpty(aftsale.getQuotCode())){
			aftsale.setQuotCode(qid);
		}
		if(StringUtil.isEmpty(aftsale.getCType())){
			aftsale.setCType(cType);
		}
		baseDao.save(aftsale);
		
	}
	
	@Override
	public void saveIdu(KstarIdu idu,String qid,String cType) throws AnneException {
		
		idu = new KstarIdu();
		
		if(StringUtil.isEmpty(idu.getQuotCode())){
			idu.setQuotCode(qid);
		}
		if(StringUtil.isEmpty(idu.getCType())){
			idu.setCType(cType);
		}
		baseDao.save(idu);
		
	}	
	
	
	@Override
	public void saveIdm(KstarIdm idm,String qid,String cType) throws AnneException {
		
		idm = new KstarIdm();
		
		if(StringUtil.isEmpty(idm.getQuotCode())){
			idm.setQuotCode(qid);
		}
		if(StringUtil.isEmpty(idm.getCType())){
			idm.setCType(cType);
		}
		baseDao.save(idm);
		
	}
	
	
	@Override
	public void saveSngUps(KstarSngUps sngups,String qid,String cType) throws AnneException {
		
		sngups = new KstarSngUps();
		
		if(StringUtil.isEmpty(sngups.getQuotCode())){
			sngups.setQuotCode(qid);
		}
		if(StringUtil.isEmpty(sngups.getCType())){
			sngups.setCType(cType);
		}
		baseDao.save(sngups);
		
	}
	
	
	//复制报价单
	@Override
	public KstarQuot copyKstarQuot(KstarQuot newQuot, KstarQuot oldQuot,UserObject user){
		
		BeanUtils.copyProperties(oldQuot, newQuot);
		newQuot.setId(null);
		String version =  String.valueOf(Integer.parseInt(oldQuot.getQuotVersion())+1);
		newQuot.setQuotVersion(version);
		newQuot.setCreator(user.getEmployee().getName());
		newQuot.setCreateTime(new Date());
		newQuot.setIsValid("1");
		newQuot.setIfsubmitted("N");
		newQuot.setBidAuditStatus("B03");
		newQuot.setProReviewStatus("S03");
		newQuot.setPrcAdtstatus("S03");
		newQuot.setSpAuditStatus("P03");
		newQuot.setTchAdtstatus("S03");
		newQuot.setBidRspstatus("S03");
		newQuot.setSpUlkstatus("");
		
		baseDao.save(newQuot);  
		
		teamService.addPosition(
				StringUtil.isNotEmpty(user.getPid())? user.getPid(): user.getPosition().getId(),
				StringUtil.isNotEmpty(user.getEid())? user.getEid(): user.getEmployee().getId(),
				"QUOTINF",
				newQuot.getId());
		
		orgTeamService.configPrimaryOrg(newQuot.getId(), "QUOTINF", user.getOrg().getId());
		
		
		return newQuot;
	}	
	
	
	
	//复制工程清单
	@Override
	public void copyKstarPrjLst(String oldQuotId, String newQuotId){
		
		String hql = "select p from KstarPrjLst p where p.quotCode = ? ";
		
		List<KstarPrjLst> plist = baseDao.findEntity(hql,oldQuotId);
		
		for(KstarPrjLst p : plist){

			KstarPrjLst newlst = new KstarPrjLst();
			BeanUtils.copyPropertiesIgnoreNull(p, newlst);
			newlst.setQuotCode(newQuotId);
			
			baseDao.save(newlst);
			
		}

	}
	
	
	//复制工程清单
	@Override
	public void copyKstarPrjLstforCont(String oldQuotId, String newQuotId){
		
		String hql = "select p from KstarPrjLst p where p.quotCode = ? ";
		
		List<KstarPrjLst> plist = baseDao.findEntity(hql,oldQuotId);
		
		for(KstarPrjLst p : plist){

			KstarPrjLst newlst = new KstarPrjLst();
			BeanUtils.copyPropertiesIgnoreNull(p, newlst);
			newlst.setQuotCode(newQuotId);
			
			if(p.getApprovePrc()!=null){
				newlst.setPrdPrc(p.getApprovePrc());
			}else if(p.getApplyPrc()!=null){
				newlst.setPrdPrc(p.getApplyPrc());
			}
			
			
			
			baseDao.save(newlst);
			
		}

	}
	
	
	
	
	/** 
	 * 复制报价工程界面数据到合同模块
	 * 
	 */
	
	@Override
	public void copyPrjdtl(String quotcode,String contractcode,String ifcntr) throws AnneException {
		
		String quottype;
		String cntrtype;
		
		
		if (ifcntr.equals("Y")){
			quottype = "0003";
			cntrtype = "0005";
		}else{
			quottype = "0003";
			cntrtype = "0003";
		}
		

		
		//sngups
		KstarSngUps quot_sngups = getKstarSngUps(quotcode, quottype);
		
		KstarSngUps cntrt_sngups = new KstarSngUps();
		
		if(quot_sngups!=null){
			BeanUtils.copyPropertiesIgnoreNull(quot_sngups, cntrt_sngups);
			cntrt_sngups.setQuotCode(contractcode);
			cntrt_sngups.setCType(cntrtype);
			
			baseDao.save(cntrt_sngups);
		}
		
		
		
		//baseinf
		KstarBaseInf quot_baseinf = getKstarBaseInf(quotcode, quottype);
		
		KstarBaseInf cntrt_baseinf = new KstarBaseInf();
		
		if(quot_baseinf!=null){
			BeanUtils.copyPropertiesIgnoreNull(quot_baseinf, cntrt_baseinf);
			cntrt_baseinf.setQuotCode(contractcode);
			cntrt_baseinf.setCType(cntrtype);
			
			baseDao.save(cntrt_baseinf);
		}
		

		
		//idu 
		KstarIdu quot_idu = getKstarIdu(quotcode, quottype);
		
		KstarIdu cntrt_idu = new KstarIdu();
		
		if(quot_idu!=null){
			BeanUtils.copyPropertiesIgnoreNull(quot_idu, cntrt_idu);
			cntrt_idu.setQuotCode(contractcode);
			cntrt_idu.setCType(cntrtype);
			
			baseDao.save(cntrt_idu);
		}
		

		
		//idm 
		KstarIdm quot_idm = getKstarIdm(quotcode, quottype);
		
		KstarIdm cntrt_idm = new KstarIdm();
		
		if(quot_idm!=null){
			BeanUtils.copyPropertiesIgnoreNull(quot_idm, cntrt_idm);
			cntrt_idm.setQuotCode(contractcode);
			cntrt_idm.setCType(cntrtype);
			
			baseDao.save(cntrt_idm);
		}
		
		

		
		//sngbty 
		KstarSngBty quot_sngbty = getKstarSngBty(quotcode, quottype);
		
		KstarSngBty cntrt_sngbty = new KstarSngBty();
		
		if(quot_sngbty!=null){
			BeanUtils.copyPropertiesIgnoreNull(quot_sngbty, cntrt_sngbty);
			cntrt_sngbty.setQuotCode(contractcode);
			cntrt_sngbty.setCType(cntrtype);
			
			baseDao.save(cntrt_sngbty);
		}
		

		
		//sngelec 
		KstarSngElec quot_sngelec = getKstarSngElec(quotcode, quottype);
		
		KstarSngElec cntrt_sngelec = new KstarSngElec();
		
		if(quot_sngelec!=null){
			BeanUtils.copyPropertiesIgnoreNull(quot_sngelec, cntrt_sngelec);
			cntrt_sngelec.setQuotCode(contractcode);
			cntrt_sngelec.setCType(cntrtype);
			
			baseDao.save(cntrt_sngelec);
		}
		

		
		//sngclr 
		KstarSngClr quot_sngclr = getKstarSngClr(quotcode, quottype);
		
		KstarSngClr cntrt_sngclr = new KstarSngClr();
		
		if(quot_sngclr!=null){
			BeanUtils.copyPropertiesIgnoreNull(quot_sngclr, cntrt_sngclr);
			cntrt_sngclr.setQuotCode(contractcode);
			cntrt_sngclr.setCType(cntrtype);
			
			baseDao.save(cntrt_sngclr);
		}
		

		
		//sngrck 
		KstarSngRck quot_sngrck = getKstarSngRck(quotcode, quottype);
		
		KstarSngRck cntrt_sngrck = new KstarSngRck();
		
		if(quot_sngrck!=null){
			BeanUtils.copyPropertiesIgnoreNull(quot_sngrck, cntrt_sngrck);
			cntrt_sngrck.setQuotCode(contractcode);
			cntrt_sngrck.setCType(cntrtype);
			
			baseDao.save(cntrt_sngrck);
			
		}
		

		
		//sngmnt 
		KstarSngMnt quot_sngmnt = getKstarSngMnt(quotcode, quottype);
		
		KstarSngMnt cntrt_sngmnt = new KstarSngMnt();
		
		if(quot_sngmnt!=null){
			BeanUtils.copyPropertiesIgnoreNull(quot_sngmnt, cntrt_sngmnt);
			cntrt_sngmnt.setQuotCode(contractcode);
			cntrt_sngmnt.setCType(cntrtype);
			
			baseDao.save(cntrt_sngmnt);
		}
		

		
		//aftsale 
		KstarAftSale quot_aftsale = getKstarAftSale(quotcode, quottype);
		
		KstarAftSale cntrt_aftsale = new KstarAftSale();
		
		if(quot_aftsale!=null){
			BeanUtils.copyPropertiesIgnoreNull(quot_aftsale, cntrt_aftsale);
			cntrt_aftsale.setQuotCode(contractcode);
			cntrt_aftsale.setCType(cntrtype);
			
			baseDao.save(cntrt_aftsale);
			
		}
		

		
	}
	
	
	
	//更新行号
	@Override
	public void updatePrjLstwithlineNum(String qid){
		
		String hql1 = " from KstarPrjLst where prdNm != '工程包' and quotCode = ? ";
		List<KstarPrjLst> plist = baseDao.findEntity(hql1,qid);
		
		int i = 1;
		
		for(KstarPrjLst p : plist){
			String linenum = String.valueOf(i) ;
			p.setLineNum(linenum);
			i=i+1;
			baseDao.update(p);
		}
		
		
	}
	
	
	
	
	
	
	//工程清单复制
	@Override
	public void copyLovMemberByMemo(String oldMemo,String newMemo){
		String hql = " from LovMember l where l.memo = ? and l.groupCode = 'PRJLSTPRDCAT' order by level ";
		String hql2 = "select p from KstarPrjLst p,LovMember l where l.memo = ? and l.groupCode = 'PRJLSTPRDCAT' and p.lvId = l.id ";
		List<LovMember> list = baseDao.findEntity(hql,oldMemo);
		List<KstarPrjLst> plist = baseDao.findEntity(hql2,oldMemo);
		
		Map<String,LovMember> map = new HashMap<String,LovMember>();
		Map<String,KstarPrjLst> map2 = new HashMap<String,KstarPrjLst>();
		for(KstarPrjLst p : plist){
			map2.put(p.getLvId(), p);
		}
		
		for(LovMember lov : list){
			LovMember nlov = new LovMember();
			BeanUtils.copyPropertiesIgnoreNull(lov, nlov);
			
			Date now = new Date();
			SimpleDateFormat dtFmt=new SimpleDateFormat("yyyyMMddHHmmssSSS");
			int r = contractService.buildRandom(2);
			String codeStr = dtFmt.format(now) + r;
			nlov.setCode(codeStr);
			
//			nlov.setCode(StringUtil.getUUID());
			nlov.setId(null);
			nlov.setMemo(newMemo);
			map.put(lov.getId(), nlov);
			baseDao.save(nlov);
			
			KstarPrjLst np = map2.get(lov.getId());
			if(np!=null){
				KstarPrjLst oldP = new KstarPrjLst();
				BeanUtils.copyPropertiesIgnoreNull(np, oldP);
				oldP.setId(null);
				oldP.setLvId(nlov.getId());
				oldP.setQuotCode(newMemo);
				oldP.setSprvMrk("N");
				oldP.setSprvLvl("N");
				oldP.setCurLvl("N");
				baseDao.save(oldP);
			}
		}
		
		for(LovMember lov : list){
			if("ROOT".equals(lov.getParentId())){
				map.get(lov.getId()).setParentId("ROOT");
			}else{
				LovMember parent = map.get(lov.getParentId());
				map.get(lov.getId()).setParentId(parent.getId());
			}
			lovMemberService.update(map.get(lov.getId()));
		}
	}
	
	

	
	
	
	@Override
	public void saveSngbty(KstarSngBty sngbty,String qid,String cType) throws AnneException {
		
		sngbty = new KstarSngBty();
		
		if(StringUtil.isEmpty(sngbty.getQuotCode())){
			sngbty.setQuotCode(qid);
		}
		if(StringUtil.isEmpty(sngbty.getCType())){
			sngbty.setCType(cType);
		}
		baseDao.save(sngbty);
		
	}	
	
	
	@Override
	public void saveSngelec(KstarSngElec sngelec,String qid,String cType) throws AnneException {
		
		sngelec = new KstarSngElec();
		
		if(StringUtil.isEmpty(sngelec.getQuotCode())){
			sngelec.setQuotCode(qid);
		}
		if(StringUtil.isEmpty(sngelec.getCType())){
			sngelec.setCType(cType);
		}
		baseDao.save(sngelec);
		
	}
	
	
	@Override
	public void saveSngclr(KstarSngClr sngclr,String qid,String cType) throws AnneException {
		
		sngclr = new KstarSngClr();
		
		if(StringUtil.isEmpty(sngclr.getQuotCode())){
			sngclr.setQuotCode(qid);
		}
		if(StringUtil.isEmpty(sngclr.getCType())){
			sngclr.setCType(cType);
		}
		baseDao.save(sngclr);
		
	}
	
	@Override
	public void saveSngrck(KstarSngRck sngrck,String qid,String cType) throws AnneException {
		
		sngrck = new KstarSngRck();
		
		if(StringUtil.isEmpty(sngrck.getQuotCode())){
			sngrck.setQuotCode(qid);
		}
		if(StringUtil.isEmpty(sngrck.getCType())){
			sngrck.setCType(cType);
		}
		baseDao.save(sngrck);
		
	}
	
	
	@Override
	public void saveSngmnt(KstarSngMnt sngmnt,String qid,String cType) throws AnneException {
		
		sngmnt = new KstarSngMnt();
		
		if(StringUtil.isEmpty(sngmnt.getQuotCode())){
			sngmnt.setQuotCode(qid);
		}
		if(StringUtil.isEmpty(sngmnt.getCType())){
			sngmnt.setCType(cType);
		}
		baseDao.save(sngmnt);
		
	}
	
	
	
	@Override
	public void saveCnt(KstarCntr cnt) throws AnneException {
		if(StringUtil.isEmpty(cnt.getQuotCode())){
			throw new AnneException("报价单编号不能为空");
		}
		if(StringUtil.isEmpty(cnt.getCntId())){
			throw new AnneException("合同编号不能为空");
		}

		
		cnt.setCntDt(new Date());
		baseDao.save(cnt);
		
	}
	
	
	@Override
	public void saveMem(KstarMemInfo mem) throws AnneException {
		if(StringUtil.isEmpty(mem.getQuotCode())){
			throw new AnneException("报价单编号不能为空");
		}
		if(StringUtil.isEmpty(mem.getMemName())){
			throw new AnneException("团队成员不能为空");
		}
		mem.setCType("0003");
		baseDao.save(mem);
	}
	
	
	@Override
	public void saveBiddcevl(KstarBiddcevl biddcevl) throws AnneException {
		if(StringUtil.isEmpty(biddcevl.getQuotCode())){
			throw new AnneException("报价单编号不能为空");
		}
		if(StringUtil.isEmpty(biddcevl.getCType())){
			biddcevl.setCType("0003");
		}
		baseDao.save(biddcevl);
	}
	
	
	@Override
	public void saveAtt(KstarAtt att) throws AnneException {
		if(StringUtil.isEmpty(att.getQuotCode())){
			throw new AnneException("报价单编号不能为空");
		}
		if(StringUtil.isEmpty(att.getDocNm())){
			throw new AnneException("文档名称不能为空");
		}
		att.setCType("0003");
		att.setUpdDt(new Date());
		baseDao.save(att);
	}
	
	@Override
	public void savePrjevl(KstarPrjEvl prjevl) throws AnneException {
		if(StringUtil.isEmpty(prjevl.getQuotCode())){
			throw new AnneException("报价单编号不能为空");
		}
		if(StringUtil.isEmpty(prjevl.getEvlTyp())){
			throw new AnneException("评审类别不能为空");
		}

		List<Object> args = new ArrayList<Object>();
		args.add(prjevl.getQuotCode());
		args.add(prjevl.getEvlTyp());
		
//		int oldseqNo = 1;
//		
//		Long count = baseDao.findUniqueEntity("select count(*) from KstarPrjEvl where quotCode = ? and evlTyp = ? ",args.toArray());
//		if(count > 0){
//			oldseqNo = baseDao.findUniqueEntity("select max(seqno) from KstarPrjEvl where quotCode = ? and evlTyp = ? ",args.toArray());
//			oldseqNo = oldseqNo+1;
//		}	
//		
//		prjevl.setSeqno(oldseqNo);
//		prjevl.setCType("0003");
		baseDao.save(prjevl);
	}
	
	
	@Override
	public void savePgInf(KstarPgInf pginf,String cType) throws AnneException {
		if(StringUtil.isEmpty(pginf.getQuotCode())){
			throw new AnneException("报价单编号不能为空");
		}
		if(StringUtil.isEmpty(pginf.getPgTyp())){
			throw new AnneException("界面类型不能为空");
		}
		pginf.setCType(cType);
		baseDao.save(pginf);
	}
	
	
	@Override
	public KstarQuot getKstarQuot(String quotID) throws AnneException {
		return baseDao.get(KstarQuot.class, quotID);
	}
	
	@Override
	public KstarQuot getQuotbyquotcode(String quotcode) throws AnneException {
		List<Object> args = new ArrayList<Object>();
		args.add(quotcode);
		
		KstarQuot quot = baseDao.findUniqueEntity("from KstarQuot where isValid = '1' and quotCode = ? ", args.toArray());
		return quot;
	}
	
	@Override
	public KstarQuot getPrequotbyID(String id) throws AnneException {
		List<Object> args = new ArrayList<Object>();
		args.add(id);
		KstarQuot quot = baseDao.findUniqueEntity("from KstarQuot where id = ? ", args.toArray());
		
		List<Object> args1 = new ArrayList<Object>();
		args1.add(quot.getQuotCode());
		
		String preversion;
		
		if(Integer.parseInt(quot.getQuotVersion())>1){
			preversion =  String.valueOf(Integer.parseInt(quot.getQuotVersion())-1);
		}else{
			preversion = "1";
		}
		
		args1.add(preversion);
		
		KstarQuot preQuot = baseDao.findUniqueEntity("from KstarQuot where quotCode = ? and quotVersion = ? ", args1.toArray());
		
		return preQuot;
	}
	
	@Override
	public KstarAftSale getKstarAftSale(String aftsaleID,String cType) throws AnneException {
		List<Object> args = new ArrayList<Object>();
		args.add(cType);
		args.add(aftsaleID);
		
		KstarAftSale aftsale = baseDao.findUniqueEntity("from KstarAftSale where CType = ? and quotCode = ? ", args.toArray());
		return aftsale;
	}
	
	@Override
	public KstarBaseInf getKstarBaseInf(String baseinfID,String cType) throws AnneException {
		List<Object> args = new ArrayList<Object>();
		args.add(cType);
		args.add(baseinfID);
		
		KstarBaseInf baseinf = baseDao.findUniqueEntity("from KstarBaseInf where CType = ? and quotCode = ? ", args.toArray());
		return baseinf;
	}
	
	@Override
	public KstarIdu getKstarIdu(String iduID,String cType) throws AnneException {
		List<Object> args = new ArrayList<Object>();
		args.add(cType);
		args.add(iduID);
		
		KstarIdu idu = baseDao.findUniqueEntity("from KstarIdu where CType = ? and quotCode = ? ", args.toArray());
		return idu;
	}

	@Override
	public KstarBiddcevl getBiddcevl(String quotId,String cType) throws AnneException {
		List<Object> args = new ArrayList<Object>();
		args.add(cType);
		args.add(quotId);
		
		KstarBiddcevl biddcevl = baseDao.findUniqueEntity("from KstarBiddcevl where CType = ? and quotCode = ? ", args.toArray());
		return biddcevl;
	}
	
	
	@Override
	public List<KstarBiddcevl> getBiddcevlList(String quotId,String cType) throws AnneException {
		
		List<KstarBiddcevl> biddcevls;
		
		List<Object> args = new ArrayList<Object>();
		args.add(cType);
		args.add(quotId);
		
		biddcevls = baseDao.findEntity("from KstarBiddcevl where CType = ? and quotCode = ? ", args.toArray());
		return biddcevls;
	}
	
	
	@Override
	public KstarBiddcevl getBiddcevl(String processid) throws AnneException {
		List<Object> args = new ArrayList<Object>();
		args.add(processid);
		
		KstarBiddcevl biddcevl = baseDao.findUniqueEntity("from KstarBiddcevl where snPnt = ? ", args.toArray());
		return biddcevl;
	}
	
	
	@Override
	public KstarIdm getKstarIdm(String idmID,String cType) throws AnneException {
		List<Object> args = new ArrayList<Object>();
		args.add(cType);
		args.add(idmID);
		
		KstarIdm idm = baseDao.findUniqueEntity("from KstarIdm where CType = ? and quotCode = ? ", args.toArray());
		return idm;
	}
	
	
	@Override
	public KstarSngUps getKstarSngUps(String sngupsID,String cType) throws AnneException {
		List<Object> args = new ArrayList<Object>();
		args.add(cType);
		args.add(sngupsID);
		
		KstarSngUps sngups = baseDao.findUniqueEntity("from KstarSngUps where CType = ? and quotCode = ? ", args.toArray());
		return sngups;
	}
	
	
	@Override
	public KstarSngBty getKstarSngBty(String sngbtyID,String cType) throws AnneException {
		List<Object> args = new ArrayList<Object>();
		args.add(cType);
		args.add(sngbtyID);
		
		KstarSngBty sngbty = baseDao.findUniqueEntity("from KstarSngBty where CType = ? and quotCode = ? ", args.toArray());
		return sngbty;
	}
	
	
	@Override
	public KstarSngElec getKstarSngElec(String sngelecID,String cType) throws AnneException {
		List<Object> args = new ArrayList<Object>();
		args.add(cType);
		args.add(sngelecID);
		
		KstarSngElec sngelec = baseDao.findUniqueEntity("from KstarSngElec where CType = ? and quotCode = ? ", args.toArray());
		return sngelec;
	}
	
	@Override
	public KstarSngClr getKstarSngClr(String sngclrID,String cType) throws AnneException {
		List<Object> args = new ArrayList<Object>();
		args.add(cType);
		args.add(sngclrID);
		
		KstarSngClr sngclr = baseDao.findUniqueEntity("from KstarSngClr where CType = ? and quotCode = ? ", args.toArray());
		return sngclr;
	}
	
	@Override
	public KstarSngRck getKstarSngRck(String sngrckID,String cType) throws AnneException {
		List<Object> args = new ArrayList<Object>();
		args.add(cType);
		args.add(sngrckID);
		
		KstarSngRck sngrck = baseDao.findUniqueEntity("from KstarSngRck where CType = ? and quotCode = ? ", args.toArray());
		return sngrck;
	}
	
	@Override
	public KstarSngMnt getKstarSngMnt(String sngmntID,String cType) throws AnneException {
		List<Object> args = new ArrayList<Object>();
		args.add(cType);
		args.add(sngmntID);
		
		KstarSngMnt sngmnt = baseDao.findUniqueEntity("from KstarSngMnt where CType = ? and quotCode = ? ", args.toArray());
		return sngmnt;
	}
	
	@Override
	public KstarCntr getKstarCntr(String cntID) throws AnneException {
		return baseDao.get(KstarCntr.class, cntID);
	}
	
	@Override
	public KstarMemInfo getKstarMemInfo(String memID) throws AnneException {
		return baseDao.get(KstarMemInfo.class, memID);
	}
	
	@Override
	public KstarBiddcevl getKstarBiddcevl(String biddcevlID) throws AnneException {
		return baseDao.get(KstarBiddcevl.class, biddcevlID);
	}
	
	@Override
	public KstarAtt getKstarAtt(String attID) throws AnneException {
		return baseDao.get(KstarAtt.class, attID);
	}
	
	@Override
	public KstarPrjLst getKstarPrjLst(String prjlstID) throws AnneException {
		return baseDao.get(KstarPrjLst.class, prjlstID);
	}
	
	@Override
	public KstarPrjEvl getKstarPrjEvl(String prjevlID) throws AnneException {
		return baseDao.get(KstarPrjEvl.class, prjevlID);
	}
	
	@Override
	public KstarPgInf getKstarPgInf(String pginfID) throws AnneException {
		return baseDao.get(KstarPgInf.class, pginfID);
	}
	
	@Override
	public void updateQuot(KstarQuot quot) throws AnneException {
		KstarQuot oldQuot = baseDao.get(KstarQuot.class, quot.getId());
		if(oldQuot == null){
			throw new AnneException(IQuotService.class.getName() + " saveQuot : 没有找到要更新的数据");
		}
		
		if(!quot.getId().equals(oldQuot.getId())){
			throw new AnneException("报价单ID不能被修改");
		}
		
//		//赢标：若投标项目为“是”，投标结果修改为“赢标”时，此时“赢标/丢标/关闭原因”必填，系统自动修改为赢标
//		if(quot.getIsBidPro()!=null){
//			if(quot.getBidResults()!=null){
//				if(quot.getIsBidPro().equals("1")){
//					if(quot.getBidResults().equals("R01")){
//						if(StringUtil.isEmpty(quot.getBidResReason())){
//							throw new AnneException("赢标/丢标/关闭原因不能为空");
//						}
//					}
//				}
//			}
//		}
//		
//		
//		
//		//丢标:   若投标项目为“是”，投标结果修改为“丢标”时，此时“赢标/丢标/关闭原因”必填，系统自动修改为丢标
//		if(quot.getIsBidPro()!=null){
//			if(quot.getBidResults()!=null){
//				if(quot.getIsBidPro().equals("1")){
//					if(quot.getBidResults().equals("R02")){
//						if(StringUtil.isEmpty(quot.getBidResReason())){
//							throw new AnneException("赢标/丢标/关闭原因不能为空");
//						}
//					}
//				}
//			}
//		}
		

		
		BeanUtils.copyPropertiesIgnoreNull(quot, oldQuot);
		//oldQuot.setCreateTime(new Date());
		baseDao.update(oldQuot);
		Long count = baseDao.findUniqueEntity("select count(*) from KstarQuot where id = ? ",quot.getId());
		if(count > 1){
			throw new AnneException("报价单ID已经存在!"); 
		}
	}
	
	
	@Override
	public void updateBaseInf(KstarBaseInf baseinf,String cType) throws AnneException {
		KstarBaseInf oldbsinf = baseDao.get(KstarBaseInf.class, baseinf.getId());
		//KstarBaseInf oldbsinf = baseDao.findUniqueEntity("from KstarBaseInf where CType = '0003' and quotCode = ? ", baseinf.getQuotCode());
		
		
		if(oldbsinf == null){
			oldbsinf = new KstarBaseInf();
			
			//throw new AnneException(IQuotService.class.getName() + " saveBaseInf : 没有找到要更新的数据");
		}else{
		
			/*
			 * if(!baseinf.getId().equals(oldbsinf.getId())){ throw new
			 * AnneException("ID不能被修改"); }
			 */

			BeanUtils.copyPropertiesIgnoreNull(baseinf, oldbsinf);
			oldbsinf.setCType(cType);
			baseDao.update(oldbsinf);

			List<Object> args = new ArrayList<Object>();
			args.add(cType);
			args.add(baseinf.getId());
			
			
			Long count = baseDao.findUniqueEntity("select count(*) from KstarBaseInf where CType = ? and id = ? ",args.toArray());
			if (count > 1) {
				throw new AnneException("ID已经存在!");
			}
		}
	}
	
	
	@Override
	public void updateAftsale(KstarAftSale aftsale,String cType) throws AnneException {

		KstarAftSale oldaftsale = baseDao.get(KstarAftSale.class, aftsale.getId());

		if (oldaftsale == null) {
			throw new AnneException(IQuotService.class.getName() + " saveAftsale : 没有找到要更新的数据");
		}

		if (!aftsale.getId().equals(oldaftsale.getId())) {
			throw new AnneException("ID不能被修改");
		}

		BeanUtils.copyPropertiesIgnoreNull(aftsale, oldaftsale);
		oldaftsale.setCType(cType);
		baseDao.update(oldaftsale);

		List<Object> args = new ArrayList<Object>();
		args.add(cType);
		args.add(aftsale.getId());

		Long count = baseDao.findUniqueEntity("select count(*) from KstarAftSale where CType = ? and id = ? ",
				args.toArray());
		if (count > 1) {
			throw new AnneException("ID已经存在!");
		}

	}

	
	
	@Override
	public void updateSngbty(KstarSngBty sngbty,String cType) throws AnneException {
		KstarSngBty oldsngbty = baseDao.get(KstarSngBty.class, sngbty.getId());
		
		if(oldsngbty == null){
			throw new AnneException(IQuotService.class.getName() + " saveSngbty : 没有找到要更新的数据");
		}
		
		if(!sngbty.getId().equals(oldsngbty.getId())){
			throw new AnneException("ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(sngbty, oldsngbty);
		oldsngbty.setCType(cType);
		baseDao.update(oldsngbty);
		
		List<Object> args = new ArrayList<Object>();
		args.add(cType);
		args.add(sngbty.getId());
		
		Long count = baseDao.findUniqueEntity("select count(*) from KstarSngBty where CType = ? and id = ? ",args.toArray());
		if(count > 1){
			throw new AnneException("ID已经存在!"); 
		}
	}
	
	
	@Override
	public void updateIdu(KstarIdu idu,String cType) throws AnneException {
		KstarIdu oldidu = baseDao.get(KstarIdu.class, idu.getId());
		
		if(oldidu == null){
			throw new AnneException(IQuotService.class.getName() + " saveIdu : 没有找到要更新的数据");
		}
		
		if(!idu.getId().equals(oldidu.getId())){
			throw new AnneException("ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(idu, oldidu);
		oldidu.setCType(cType);
		baseDao.update(oldidu);
		
		List<Object> args = new ArrayList<Object>();
		args.add(cType);
		args.add(idu.getId());
		
		Long count = baseDao.findUniqueEntity("select count(*) from KstarIdu where CType = ? and id = ? ",args.toArray());
		if(count > 1){
			throw new AnneException("ID已经存在!"); 
		}
	}
	
	
	@Override
	public void updateIdm(KstarIdm idm,String cType) throws AnneException {
		KstarIdm oldidm = baseDao.get(KstarIdm.class, idm.getId());
		
		if(oldidm == null){
			throw new AnneException(IQuotService.class.getName() + " saveIdm : 没有找到要更新的数据");
		}
		
		if(!idm.getId().equals(oldidm.getId())){
			throw new AnneException("ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(idm, oldidm);
		oldidm.setCType(cType);
		baseDao.update(oldidm);
		
		List<Object> args = new ArrayList<Object>();
		args.add(cType);
		args.add(idm.getId());
		
		Long count = baseDao.findUniqueEntity("select count(*) from KstarIdm where CType = ? and id = ? ",args.toArray());
		if(count > 1){
			throw new AnneException("ID已经存在!"); 
		}
	}
	
	
	@Override
	public void updateSngMnt(KstarSngMnt sngmnt,String cType) throws AnneException {
		KstarSngMnt oldsngmnt = baseDao.get(KstarSngMnt.class, sngmnt.getId());
		
		if(oldsngmnt == null){
			throw new AnneException(IQuotService.class.getName() + " saveSngmnt : 没有找到要更新的数据");
		}
		
		if(!sngmnt.getId().equals(oldsngmnt.getId())){
			throw new AnneException("ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(sngmnt, oldsngmnt);
		oldsngmnt.setCType(cType);
		baseDao.update(oldsngmnt);
		
		List<Object> args = new ArrayList<Object>();
		args.add(cType);
		args.add(sngmnt.getId());
		
		Long count = baseDao.findUniqueEntity("select count(*) from KstarSngMnt where CType = ? and id = ? ",args.toArray());
		if(count > 1){
			throw new AnneException("ID已经存在!"); 
		}
	}
	
	
	@Override
	public void updateSngRck(KstarSngRck sngrck,String cType) throws AnneException {
		KstarSngRck oldsngrck = baseDao.get(KstarSngRck.class, sngrck.getId());
		
		if(oldsngrck == null){
			throw new AnneException(IQuotService.class.getName() + " saveSngRck : 没有找到要更新的数据");
		}
		
		if(!sngrck.getId().equals(oldsngrck.getId())){
			throw new AnneException("ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(sngrck, oldsngrck);
		oldsngrck.setCType(cType);
		baseDao.update(oldsngrck);
		
		List<Object> args = new ArrayList<Object>();
		args.add(cType);
		args.add(sngrck.getId());
		
		Long count = baseDao.findUniqueEntity("select count(*) from KstarSngRck where CType = ? and id = ? ",args.toArray());
		if(count > 1){
			throw new AnneException("ID已经存在!"); 
		}
	}
	
	
	@Override
	public void updateSngelec(KstarSngElec sngelec,String cType) throws AnneException {
		KstarSngElec oldsngelec = baseDao.get(KstarSngElec.class, sngelec.getId());
		
		if(oldsngelec == null){
			throw new AnneException(IQuotService.class.getName() + " saveSngelec : 没有找到要更新的数据");
		}
		
		if(!sngelec.getId().equals(oldsngelec.getId())){
			throw new AnneException("ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(sngelec, oldsngelec);
		oldsngelec.setCType(cType);
		baseDao.update(oldsngelec);
		
		List<Object> args = new ArrayList<Object>();
		args.add(cType);
		args.add(sngelec.getId());
		
		Long count = baseDao.findUniqueEntity("select count(*) from KstarSngElec where CType = ? and id = ? ",args.toArray());
		if(count > 1){
			throw new AnneException("ID已经存在!"); 
		}
	}
	
	
	@Override
	public void updateSngclr(KstarSngClr sngclr,String cType) throws AnneException {
		KstarSngClr oldsngclr = baseDao.get(KstarSngClr.class, sngclr.getId());
		
		if(oldsngclr == null){
			throw new AnneException(IQuotService.class.getName() + " saveSngclr : 没有找到要更新的数据");
		}
		
		if(!sngclr.getId().equals(oldsngclr.getId())){
			throw new AnneException("ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(sngclr, oldsngclr);
		oldsngclr.setCType(cType);
		baseDao.update(oldsngclr);
		
		List<Object> args = new ArrayList<Object>();
		args.add(cType);
		args.add(sngclr.getId());
		
		Long count = baseDao.findUniqueEntity("select count(*) from KstarSngClr where CType = ? and id = ? ",args.toArray());
		if(count > 1){
			throw new AnneException("ID已经存在!"); 
		}
	}
	
	
	@Override
	public void updateSngups(KstarSngUps sngups,String cType) throws AnneException {
		KstarSngUps oldsngups = baseDao.get(KstarSngUps.class, sngups.getId());
		
		if(oldsngups == null){
			throw new AnneException(IQuotService.class.getName() + " savesngups : 没有找到要更新的数据");
		}
		
		if(!sngups.getId().equals(oldsngups.getId())){
			throw new AnneException("ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(sngups, oldsngups);
		oldsngups.setCType(cType);
		baseDao.update(oldsngups);
		
		List<Object> args = new ArrayList<Object>();
		args.add(cType);
		args.add(sngups.getId());
		
		Long count = baseDao.findUniqueEntity("select count(*) from KstarSngUps where CType = ? and id = ? ",args.toArray());
		if(count > 1){
			throw new AnneException("ID已经存在!"); 
		}
	}
	
	
	@Override
	public void updateCnt(KstarCntr cnt) throws AnneException {
		KstarCntr oldCnt = baseDao.get(KstarCntr.class, cnt.getId());
		if(oldCnt == null){
			throw new AnneException(IQuotService.class.getName() + " saveCnt : 没有找到要更新的数据");
		}
		
		if(!cnt.getId().equals(oldCnt.getId())){
			throw new AnneException("合同ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(cnt, oldCnt);
		oldCnt.setCntDt(new Date());
		baseDao.update(oldCnt);
		Long count = baseDao.findUniqueEntity("select count(*) from KstarCntr where id = ? ",cnt.getId());
		if(count > 1){
			throw new AnneException("合同ID已经存在!"); 
		}
	}
	
	
	@Override
	public void updateMem(KstarMemInfo mem) throws AnneException {
		KstarMemInfo oldMem = baseDao.get(KstarMemInfo.class, mem.getId());
		if(oldMem == null){
			throw new AnneException(IQuotService.class.getName() + " saveMem : 没有找到要更新的数据");
		}
		
		if(!mem.getId().equals(oldMem.getId())){
			throw new AnneException("成员ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(mem, oldMem);
		baseDao.update(oldMem);
		Long count = baseDao.findUniqueEntity("select count(*) from KstarMemInfo where id = ? ",mem.getId());
		if(count > 1){
			throw new AnneException("成员ID已经存在!"); 
		}
	}
	
	
	@Override
	public void updatePrjLst(LovMember lovMember,KstarPrjLst prjLst) throws AnneException {
		
		lovMemberService.update(lovMember);
		String lvId = lovMember.getId();
		
		String currQuotId = prjLst.getQuotCode();
		
		List<Object> args = new ArrayList<Object>();
		args.add(currQuotId);
		args.add(lvId);
		String prjLstId = baseDao.findUniqueEntity("select id from KstarPrjLst where quotCode = ? and lvId = ? ",args.toArray());
		
		KstarPrjLst oldPrjLst = baseDao.get(KstarPrjLst.class, prjLstId);
		
		if(oldPrjLst == null){
			throw new AnneException(IQuotService.class.getName() + " savePrjLst : 没有找到要更新的数据");
		}
		
		if((prjLst.getAmt()!=null)&&(prjLst.getPrdPrc()!=null)){
			Double tmpttlUnt = prjLst.getAmt() * prjLst.getPrdPrc();
			prjLst.setTtlUnt(tmpttlUnt);
		}
		
		prjLst.setVeriedNum(0.0);
		prjLst.setNotVeriNum(prjLst.getAmt());
		
		BeanUtils.copyPropertiesIgnoreNull(prjLst, oldPrjLst);
		oldPrjLst.setId(prjLstId);
		oldPrjLst.setLvId(lvId);
		baseDao.update(oldPrjLst);
		

		
		Long count = baseDao.findUniqueEntity("select count(*) from KstarPrjLst where id = ? ",prjLstId);
		if(count > 1){
			throw new AnneException("ID已经存在!"); 
		}
	}
	
	
	
	@Override
	public void updateAllAvgttl(LovMember lovMember,KstarPrjLst prjLst) throws AnneException {
		
		String currQuotId = prjLst.getQuotCode();
		
		List<Object> args = new ArrayList<Object>();
		args.add(currQuotId);
		
		StringBuffer hql = new StringBuffer(" from KstarPrjLst where prdCtg = '标准产品' and quotCode = ? ");
	
		List<KstarPrjLst> prjlsts = baseDao.findEntity(hql.toString(), args.toArray()); 
		
		for(KstarPrjLst theprjlst : prjlsts) {

			LovMember thelovmember = baseDao.get(LovMember.class, theprjlst.getLvId());
			theprjlst.setAvgTtl(getAvgttl(thelovmember, theprjlst));
			updatePrjLst(thelovmember,theprjlst);
		}
		
	}
	
	
	
	@Override
	public void updateAllAvgttlByQcode(String quotcode) throws AnneException {
		
		String currQuotId = quotcode;
		
		List<Object> args = new ArrayList<Object>();
		args.add(currQuotId);
		
		StringBuffer hql = new StringBuffer(" from KstarPrjLst where prdCtg = '标准产品' and quotCode = ? ");
	
		List<KstarPrjLst> prjlsts = baseDao.findEntity(hql.toString(), args.toArray()); 
		
		
		for(KstarPrjLst theprjlst : prjlsts) {

			LovMember thelovmember = baseDao.get(LovMember.class, theprjlst.getLvId());
			//theprjlst.setAvgTtl(getAvgttl(thelovmember, theprjlst));
			updateAvgttl(thelovmember, theprjlst);
			
			//updatePrjLst(thelovmember,theprjlst);
		}
		
	}
	
	
	
	
	@Override
	public String getLovememroot(String qid) throws AnneException {
		String rootid = "";
		
		String hql = " select l.id from LovMember l where l.memo = ? and l.groupCode = 'PRJLSTPRDCAT' and l.parentId = 'ROOT' order by level ";
		List<Object> args = new ArrayList<Object>();
		args.add(qid);
		
		rootid = baseDao.findUniqueEntity(hql, args.toArray()); 
		
		return rootid;
	}
	
	
	
	
	
	@Override
	public String getTotalamount(String qid) throws AnneException {
		
		String totalamount="";
		
		String hql = " select sum(p.ttlUnt) from KstarPrjLst p where p.quotCode = ? ";
		List<Object> args = new ArrayList<Object>();
		args.add(qid);
		
		if(this.CheckttlUnt(qid).equals("Y")){
			Double theamount = baseDao.findUniqueEntity(hql, args.toArray()); 
			totalamount = Double.toString(theamount);
		}
		
		return totalamount;
	}
	
	
	
	@Override
	public String CheckttlUnt(String qid) throws AnneException {
		String result = "N";
		String hql = " select count(*) from KstarPrjLst p where p.ttlUnt > 0 and p.quotCode = ? ";
		List<Object> args = new ArrayList<Object>();
		args.add(qid);
		
		Long count = baseDao.findUniqueEntity(hql, args.toArray()); 
		if(count>0){
			result = "Y";
		}
		
		return result;
	}
	
	
	
	@Override
	public String Checklovroot(String qid) throws AnneException {
		String result = "Y";
		String hql = " select count(*) from LovMember l where l.parentId = 'ROOT' and l.memo = ? ";
		List<Object> args = new ArrayList<Object>();
		args.add(qid);
		
		Long count = baseDao.findUniqueEntity(hql, args.toArray()); 
		if(count>0){
			result = "N";
		}
		
		return result;
	}
	
	
	@Override
	public String CheckSprvmrkStatus(String qid) throws AnneException {
		String result = "N";
		String hql = " select count(*) from KstarPrjLst p where p.sprvMrk = 'Y' and p.quotCode = ? ";
		List<Object> args = new ArrayList<Object>();
		args.add(qid);
		
		Long count = baseDao.findUniqueEntity(hql, args.toArray()); 
		if(count>0){
			result = "Y";
		}
		
		return result;
	}
	
	
	
	@Override
	public String CheckContractStatus(String qid) throws AnneException {
		String result = "N";
		
		LovMember statLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "09");
		String statusId = statLov.getId();
		
		
		String hql = " select count(*) from Contract p where p.contrStat != ? and p.quotNo = ? ";
		List<Object> args = new ArrayList<Object>();
		args.add(statusId);
		args.add(qid);
		
		Long count = baseDao.findUniqueEntity(hql, args.toArray()); 
		if(count>0){
			result = "Y";
		}
		
		return result;
	}
	
	
	@Override
	public String getContractID(String qid) throws AnneException {
		String result = "";
		
		LovMember statLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "09");
		String statusId = statLov.getId();
		
		
		String hql = " select p.cntId from Contract p where p.contrStat != ? and p.quotNo = ? ";
		List<Object> args = new ArrayList<Object>();
		args.add(statusId);
		args.add(qid);
		
		String cid = baseDao.findUniqueEntity(hql, args.toArray()); 
		if(cid!=null){
			result = cid;
		}
		
		return result;
	}
	
	
	@Override
	public String CheckQuotsubmitStatus(String qid) throws AnneException {
		String result = "N";
		String hql = " select count(*) from KstarQuot q where q.ifsubmitted = 'Y' and q.id = ? ";
		List<Object> args = new ArrayList<Object>();
		args.add(qid);
		
		Long count = baseDao.findUniqueEntity(hql, args.toArray()); 
		if(count>0){
			result = "Y";
		}
		
		return result;
	}
	
	
	
	@Override
	public String Checkprjevlstatus(String qid) throws AnneException {
		String result = "Y";
		
		List<Object> args = new ArrayList<Object>();
		args.add(qid);
		
		StringBuffer hql1 = new StringBuffer(" select count(*) from KstarPrjEvl where quotCode = ? ");
		
		Long count = baseDao.findUniqueEntity(hql1.toString(), args.toArray()); 
		
		if(count<1){
			result = "N";
		}else{
			
			StringBuffer hql = new StringBuffer(" from KstarPrjEvl where quotCode = ? ");
			
			List<KstarPrjEvl> prjevls = baseDao.findEntity(hql.toString(), args.toArray()); 
			
			for(KstarPrjEvl theprjevl : prjevls) {
				if(theprjevl.getEvlSt().equals("S01")){
					result = "N";	
				}else if(theprjevl.getEvlSt().equals("S03")){
					result = "N";
				}else if(theprjevl.getEvlSt().equals("S04")){
					result = "N";
				}
			}
			
		}
		
		return result;
	}
	
	
	@Override
	public String Checkprjevlststatus(String qid) throws AnneException {
		String result = "N";
		
		List<Object> args = new ArrayList<Object>();
		args.add(qid);
		
		StringBuffer hql1 = new StringBuffer(" select count(*) from KstarPrjEvl where evlSt='S03' and quotCode = ? ");
		
		Long count = baseDao.findUniqueEntity(hql1.toString(), args.toArray()); 
		
		KstarQuot quot = this.getKstarQuot(qid);
		
		
		if(count>0){
			if(quot.getStatus().equals("S01")){
				result = "Y";
			}
		}
		
		return result;
	}
	
	
	
	@Override
	public String addlovroot(String qid,String ctype, String groupId) throws AnneException {
		
		LovMember lovMember = new LovMember();
		KstarPrjLst prjLst = new KstarPrjLst();
		prjLst.setQuotCode(qid);
		
		if(StringUtil.isEmpty(prjLst.getQuotCode())){
			throw new AnneException("报价单编号不能为空");
		}else{
			lovMember.setMemo(prjLst.getQuotCode());
		}
		
		lovMember.setCode(StringUtil.getUUID());
		lovMember.setName("工程包");
		lovMember.setGroupId(groupId);
		lovMember.setLeafFlag("N");
		
		
		proLovService.saveCatelog(lovMember);
		
		prjLst.setPrdNm("工程包");
		prjLst.setLvId(lovMember.getId());
		savePrjLst(prjLst,ctype);
		
		return lovMember.getId();
		
	}
	
	//入参1：V_Approve_Type      值：ALL/PART/NONE
	@Override
	public String checkApprovedType(String qid) throws AnneException {
	
		String approvedType = "NONE";
		
		//总产品行数
		Long count = baseDao.findUniqueEntity("select count(*) from KstarPrjLst where proId !=null and quotCode = ? ",qid);

		
		//特价审批产品行数
		Long sprvcount = baseDao.findUniqueEntity("select count(*) from KstarPrjLst where proId !=null and sprvMrk = 'Y' and quotCode = ? ",qid);
		
		if(sprvcount.equals(count)){
			approvedType = "ALL";
		}
		
		if(sprvcount < count){
			approvedType = "PART";
		}
		
		if(sprvcount<1){
			approvedType = "NONE";
		}
		
		return approvedType;
	}
	
	
	
	
	//入参2：V_CUR_LEVEL      值：当前层级
	@Override
	public String checkcurrentLevel(String qid) throws AnneException {
		
		String highlevel = "层一";
		int curlevel = 1;
		
		String hql = "select p from KstarPrjLst p where p.quotCode = ? ";
		
		List<KstarPrjLst> plist = baseDao.findEntity(hql,qid);
		
		for(KstarPrjLst p : plist){
			
			if(p.getCurLvl()!=null){
				
				if(p.getCurLvl().equals("层六")){
					if(curlevel<6){
						curlevel = 6;
						highlevel = "层六";
					}
					
				}
				
				if(p.getCurLvl().equals("层五")){
					if(curlevel<5){
						curlevel = 5;
						highlevel = "层五";
					}
				}
				
				if(p.getCurLvl().equals("层四")){
					if(curlevel<4){
						curlevel = 4;
						highlevel = "层四";
					}
				}
				
				if(p.getCurLvl().equals("层三")){
					if(curlevel<3){
						curlevel = 3;
						highlevel = "层三";
					}
				}
				
				if(p.getCurLvl().equals("层二")){
					if(curlevel<2){
						curlevel = 2;
						highlevel = "层二";
					}
				}
				
			}
			
		}
		
		return highlevel;
	}
	
	
	
	
	
	//入参3：V_HIGH_LEVEL      值：最高层级
	@Override
	public String checkHighLevel(String qid) throws AnneException {
		
		String highlevel = "层一";
		int curlevel = 1;
		
		String hql = "select p from KstarPrjLst p where p.quotCode = ? ";
		
		List<KstarPrjLst> plist = baseDao.findEntity(hql,qid);
		
		for(KstarPrjLst p : plist){
			
			if(p.getSprvLvl()!=null){
				
				if(p.getSprvLvl().equals("层六")){
					if(curlevel<6){
						curlevel = 6;
						highlevel = "层六";
					}
					
				}
				
				if(p.getSprvLvl().equals("层五")){
					if(curlevel<5){
						curlevel = 5;
						highlevel = "层五";
					}
				}
				
				if(p.getSprvLvl().equals("层四")){
					if(curlevel<4){
						curlevel = 4;
						highlevel = "层四";
					}
				}
				
				if(p.getSprvLvl().equals("层三")){
					if(curlevel<3){
						curlevel = 3;
						highlevel = "层三";
					}
				}
				
				if(p.getSprvLvl().equals("层二")){
					if(curlevel<2){
						curlevel = 2;
						highlevel = "层二";
					}
				}
				
			}
			
		}
		
		
		return highlevel;
	}
	
	
	
	
	
	@Override
	public void updateAvgttl(LovMember lovMember,KstarPrjLst prjLst) throws AnneException {
		

		String hql1 = "select l from KstarPrjLst p,LovMember l where p.quotCode = ? and l.groupCode = 'PRJLSTPRDCAT' and p.lvId = l.id and p.quotCode = l.memo order by l.level ";
		String hql2 = "select p from KstarPrjLst p,LovMember l where p.quotCode = ? and l.groupCode = 'PRJLSTPRDCAT' and p.lvId = l.id and p.quotCode = l.memo ";
		
		String currQuotId = prjLst.getQuotCode();
		
		List<LovMember> vlist = baseDao.findEntity(hql1,currQuotId);
		List<KstarPrjLst> plist = baseDao.findEntity(hql2,currQuotId);
		
		Map<String,LovMember> lvmap = new HashMap<String,LovMember>();
		Map<String,KstarPrjLst> prdmap = new HashMap<String,KstarPrjLst>();
		Map<String,KstarPrjLst> stprdmap = new HashMap<String,KstarPrjLst>();
		
		String rootPrjlvid = "";
		//全部单(标准)产品定价总和
		Double prjstdprd_prdSprc = 0.0;
		
		
		for(KstarPrjLst p : plist){
			prdmap.put(p.getLvId(), p);
			//add standard product
			if (p.getPrdCtg()!=null){
				if(p.getPrdCtg().equals("标准产品")){
					stprdmap.put(p.getLvId(), p);
				}
				
				if(p.getPrdCtg().equals("非标产品")){
					stprdmap.put(p.getLvId(), p);
				}
				
				if(p.getPrdCtg().equals("外购产品")){
					stprdmap.put(p.getLvId(), p);
				}
				
			}
		}
		
		for(LovMember lov : vlist){
			lvmap.put(lov.getId(), lov);
			// root
			if (lov.getParentId()!=null) {
				if ((lov.getParentId()).equals("ROOT")) {
					rootPrjlvid = lov.getId();
				}
			}
		}
		
		
		Iterator it1 = stprdmap.entrySet().iterator();
		while (it1.hasNext()) {
			Map.Entry entry1 = (Map.Entry) it1.next();
			String key1 = (entry1.getKey()).toString();
			KstarPrjLst stdprd1 = (KstarPrjLst) (entry1.getValue());
			
			if(stdprd1.getPrdSprc()!=null){
				//全部单(标准)产品定价总和
				prjstdprd_prdSprc = prjstdprd_prdSprc + stdprd1.getPrdSprc();
			}

			
		}
		
		
		
		Iterator it = stprdmap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String key = (entry.getKey()).toString();
			KstarPrjLst stdprd = (KstarPrjLst) (entry.getValue());
			
			//标准产品平均总单价
			Double avgTtl = 0.0;
			//项目类服务产品报价总和
			Double prjserprd_ttlunt = 0.0;
			//项目类商务产品报价总和
			Double prjbizprd_ttlunt = 0.0;

			// 单产品行汇总金额
			Double std_hhTtl = stdprd.getTtlUnt();

			
			
			for (LovMember lov : vlist) {
				KstarPrjLst np = prdmap.get(lov.getId());

				// 单产品行汇总金额
				if(lov.getParentId()!=null){
					if(lov.getParentId().equals(stdprd.getLvId())){
						std_hhTtl = std_hhTtl + np.getTtlUnt();
					}
				}
				
				//项目类服务产品报价总和
				if(lov.getParentId()!=null){
					 if(np.getPrdCtg()!=null){
							if(lov.getParentId().equals(rootPrjlvid)){
								 if(np.getPrdCtg().equals("服务产品")){
									 prjserprd_ttlunt = prjserprd_ttlunt + np.getTtlUnt();
								 } 
							}
					 } 
				}
				
				

				
				//项目类商务产品报价总和
				if(lov.getParentId()!=null){
					 if(np.getPrdCtg()!=null){
							if(lov.getParentId().equals(rootPrjlvid)){
								 if(np.getPrdCtg().equals("商务产品")){
									 prjbizprd_ttlunt = prjbizprd_ttlunt + np.getTtlUnt();
								 } 
							}
					 } 
				}
				
			}

			// 单产品行汇总金额
			stdprd.setHhTtl(std_hhTtl);
			
			if(stdprd.getAmt()!=null){
				
				if ((prjstdprd_prdSprc>0)&&(stdprd.getAmt()>0)) {
					
					//标准产品平均总单价 = 【单产品行汇总金额 + （项目类服务产品报价总和 + 项目类商务产品报价总和） * 定价 / (全部单产品定价总和)】/【数量】
					avgTtl = (stdprd.getHhTtl() + (prjserprd_ttlunt+prjbizprd_ttlunt)* stdprd.getPrdSprc()/prjstdprd_prdSprc)/stdprd.getAmt() ;
					
				}
				//标准产品平均总单价
				stdprd.setAvgTtl(avgTtl);
				
			}
			

			
			//更新标准产品
			baseDao.update(stdprd);
		}
			
	}
	
	
	
	
	
	@Override
	public Double getAvgttl(LovMember lovMember,KstarPrjLst prjLst) throws AnneException {
		Double avgTtl = 0.0;
		
		String lvId = lovMember.getId();
		
		String currQuotId = prjLst.getQuotCode();
		
		List<Object> args = new ArrayList<Object>();
		args.add(currQuotId);
		args.add(lvId);
		
		String prjLstId = baseDao.findUniqueEntity("select id from KstarPrjLst where quotCode = ? and lvId = ? ",args.toArray());
		
		KstarPrjLst currPrjLst = baseDao.get(KstarPrjLst.class, prjLstId);
		
		//非标产品单品总金额
		Double nsttlUnt = baseDao.findUniqueEntity("select sum(pl.ttlUnt) from KstarPrjLst pl,LovMember lv where pl.lvId = lv.id and pl.quotCode = ? and lv.parentId = ? ",args.toArray());
		
		//行汇总单价
		if ((currPrjLst.getTtlUnt()!=null)&&(nsttlUnt!=null)){
			avgTtl = currPrjLst.getTtlUnt()+nsttlUnt;
		}else if ((nsttlUnt!=null)&&(currPrjLst.getTtlUnt()==null)) {
			avgTtl = nsttlUnt;
		}else if (currPrjLst.getTtlUnt()!=null){
			avgTtl = currPrjLst.getTtlUnt();
		}
		
		
		
		String rootId = baseDao.findUniqueEntity("select id from LovMember where groupId = 'PRJLSTPRDCAT' and parentId = 'ROOT' and memo = ? ",currQuotId);
		
		List<Object> args1 = new ArrayList<Object>();
		args1.add(currQuotId);
		args1.add(rootId);
		
		//全部单产品(标准产品)定价总和
		Double ttlstprdSprc = baseDao.findUniqueEntity("select sum(pl.prdSprc) from KstarPrjLst pl,LovMember lv where pl.prdCtg = '标准产品' and pl.lvId = lv.id and pl.quotCode = ? and lv.parentId = ? ",args1.toArray());
		
		//全部单产品(非标准产品)报价总和
		Double ttlnsprdPrc = baseDao.findUniqueEntity("select sum(pl.prdPrc) from KstarPrjLst pl,LovMember lv where pl.prdCtg != '标准产品' and pl.lvId = lv.id and pl.quotCode = ? and lv.parentId = ? ",args1.toArray());
		
		if ((currPrjLst.getPrdSprc()== null)||(ttlnsprdPrc==null)){
			avgTtl = avgTtl;
		}else{
			avgTtl = avgTtl + ttlnsprdPrc* currPrjLst.getPrdSprc() /ttlstprdSprc;
		}
		
		
		return avgTtl;
	}
	
	
	
	
	@Override
	public KstarPrjLst getKstarPrjLst(String qid, String lvId,String ctype) throws AnneException {
		
		List<Object> args = new ArrayList<Object>();
		args.add(qid);
		args.add(lvId);
		args.add(ctype);
		String prjLstId = baseDao.findUniqueEntity("select id from KstarPrjLst where quotCode = ? and lvId = ? and CType = ? ",args.toArray());
		
		KstarPrjLst prjLst = baseDao.get(KstarPrjLst.class, prjLstId);
		
		return prjLst;
	}
	
	
	@Override
	public void savePrjLst(KstarPrjLst prjLst,String cType) throws AnneException {
		if(StringUtil.isEmpty(prjLst.getQuotCode())){
			throw new AnneException("报价单编号不能为空");
		}
		prjLst.setCType(cType);
		prjLst.setVeriedNum(0.0);

		if(prjLst.getAmt()!=null){
			prjLst.setNotVeriNum(prjLst.getAmt());
		}
		
		if((prjLst.getAmt()!=null)&&(prjLst.getPrdPrc()!=null)){
			Double tmpttlUnt = prjLst.getAmt() * prjLst.getPrdPrc();
			prjLst.setTtlUnt(tmpttlUnt);
		}
		
		
		baseDao.save(prjLst);
	}
	
	
	
	@Override
	public void updateBiddcevl(KstarBiddcevl biddcevl) throws AnneException {
		KstarBiddcevl oldbiddcevl = baseDao.get(KstarBiddcevl.class, biddcevl.getId());
		if(oldbiddcevl == null){
			throw new AnneException(IQuotService.class.getName() + " saveBiddcevl : 没有找到要更新的数据");
		}
		
		if(!biddcevl.getId().equals(oldbiddcevl.getId())){
			throw new AnneException("成员ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(biddcevl, oldbiddcevl);
		baseDao.update(oldbiddcevl);
		Long count = baseDao.findUniqueEntity("select count(*) from KstarBiddcevl where id = ? ",biddcevl.getId());
		if(count > 1){
			throw new AnneException("成员ID已经存在!"); 
		}
	}
	
	
	@Override
	public void updateAtt(KstarAtt att) throws AnneException {
		KstarAtt oldAtt = baseDao.get(KstarAtt.class, att.getId());
		if(oldAtt == null){
			throw new AnneException(IQuotService.class.getName() + " saveAtt : 没有找到要更新的数据");
		}
		
		if(!att.getId().equals(oldAtt.getId())){
			throw new AnneException("附件ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(att, oldAtt);
		baseDao.update(oldAtt);
		Long count = baseDao.findUniqueEntity("select count(*) from KstarAtt where id = ? ",att.getId());
		if(count > 1){
			throw new AnneException("附件ID已经存在!"); 
		}
	}
	
	@Override
	public void updatePrjEvl(KstarPrjEvl prjevl) throws AnneException {
		KstarPrjEvl oldPrjevl = baseDao.get(KstarPrjEvl.class, prjevl.getId());
		if(oldPrjevl == null){
			throw new AnneException(IQuotService.class.getName() + " savePrjevl : 没有找到要更新的数据");
		}
		
		if(!prjevl.getId().equals(oldPrjevl.getId())){
			throw new AnneException("评审ID不能被修改");
		}
		
//		int seqno = oldPrjevl.getSeqno();
		
		BeanUtils.copyPropertiesIgnoreNull(prjevl, oldPrjevl);
//		oldPrjevl.setSeqno(seqno);

		baseDao.update(oldPrjevl);
		Long count = baseDao.findUniqueEntity("select count(*) from KstarPrjEvl where id = ? ",prjevl.getId());
		if(count > 1){
			throw new AnneException("评审ID已经存在!"); 
		}
	}
	
	
	@Override
	public void updatePgInf(KstarPgInf pginf) throws AnneException {
		KstarPgInf oldPgInf = baseDao.get(KstarPgInf.class, pginf.getId());
		if(oldPgInf == null){
			throw new AnneException(IQuotService.class.getName() + " savePgInf : 没有找到要更新的数据");
		}
		
		if(!pginf.getId().equals(oldPgInf.getId())){
			throw new AnneException("页面信息ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(pginf, oldPgInf);
		baseDao.update(oldPgInf);
		Long count = baseDao.findUniqueEntity("select count(*) from KstarPgInf where id = ? ",pginf.getId());
		if(count > 1){
			throw new AnneException("页面信息ID已经存在!"); 
		}
	}
	
	
	
	@Override
	public void deleteQuot(String quotId) throws AnneException {
		baseDao.executeHQL(" delete KstarQuot qt where qt.id = ? ",new Object[]{quotId});
		//baseDao.deleteById(KstarQuot.class, quotId);
	}
	
	
	@Override
	public KstarPrjLst getPrjLst(String quotCode,String orgPrdNm) throws AnneException {
	
		List<Object> args = new ArrayList<Object>();
		args.add(quotCode);
		args.add(orgPrdNm);
		String prjLstId = baseDao.findUniqueEntity("select id from KstarPrjLst where quotCode = ? and prdNm = ? ",args.toArray());
		
		KstarPrjLst prjLst = baseDao.get(KstarPrjLst.class, prjLstId);
		
		return prjLst;
	
	}
	
	
	@Override
	public List<KstarPrjLst> getPrjLstPrd(String quotCode) throws AnneException {
	
		List<Object> args = new ArrayList<Object>();
		args.add(quotCode);
		List<KstarPrjLst> KstarPrjLsts = baseDao.findEntity(" from KstarPrjLst where quotCode = ? and prdTyp !=null ",args.toArray());
	
		return KstarPrjLsts;
	
	}
	
	
	@Override
	public BusinessOpportunity getBizOppbynumber(String number) throws AnneException {
	
		List<Object> args = new ArrayList<Object>();
		args.add(number);
		
		BusinessOpportunity biz = baseDao.findUniqueEntity(" from BusinessOpportunity where number = ? ", args.toArray());
		
		return biz;
	}
	
	
	@Override
	public BusinessOpportunity getBizOppbyId(String Id) throws AnneException {
	
		List<Object> args = new ArrayList<Object>();
		args.add(Id);
		
		BusinessOpportunity biz = baseDao.findUniqueEntity(" from BusinessOpportunity where id = ? ", args.toArray());
		
		return biz;
	}
	
	
	
	@Override
	public void deletePrjLst(String lvId, String quotCode) throws AnneException {
		
		List<Object> args = new ArrayList<Object>();
		args.add(quotCode);
		args.add(lvId);
		
		baseDao.executeHQL(" delete KstarPrjLst pl where pl.quotCode = ? and pl.lvId = ? ",args.toArray());
	}
	
	@Override
	public void deletePrjLst(String id) throws AnneException {
		
		List<Object> args = new ArrayList<Object>();
		args.add(id);

		baseDao.executeHQL(" delete KstarPrjLst pl where pl.id = ? ",args.toArray());
	}
	

	
	
	@Override
	public Long countKstarPgInf(String Ctype,String relPrd,String quotCode) throws AnneException {
		List<Object> args = new ArrayList<Object>();
		args.add(quotCode);
		args.add(Ctype);
		args.add(relPrd);
		
		Long count = baseDao.findUniqueEntity("select count(*) from KstarPgInf pi where pi.quotCode = ? and pi.CType = ? and pi.relPrd = ? ",args.toArray());
	
		return	count;
	}
	
	@Override
	public void deleteKstarPgInf(String Ctype,String relPrd,String quotCode) throws AnneException {
		
		List<Object> args = new ArrayList<Object>();
		args.add(quotCode);
		args.add(Ctype);
		args.add(relPrd);
		
		baseDao.executeHQL(" delete KstarPgInf pi where pi.quotCode = ? and pi.CType = ? and pi.relPrd = ? ",args.toArray());
	}
	
	
	
	@Override
	public void deleteCnt(String cntId) throws AnneException {
		baseDao.deleteById(KstarCntr.class, cntId);
	}
	
	@Override
	public void deleteMem(String memId) throws AnneException {
		baseDao.deleteById(KstarMemInfo.class, memId);
	}
	
	@Override
	public void deleteBiddcevl(String biddcevlId) throws AnneException {
		baseDao.deleteById(KstarBiddcevl.class, biddcevlId);
	}
	
	@Override
	public void deleteAtt(String attId) throws AnneException {
		baseDao.deleteById(KstarAtt.class, attId);
	}
	
	@Override
	public void deletePrjevl(String prjevlId) throws AnneException {
		baseDao.deleteById(KstarPrjEvl.class, prjevlId);
	}
	
	@Override
	public void deletePgInf(String pginfId) throws AnneException {
		baseDao.deleteById(KstarPgInf.class, pginfId);
	}
	
	
	@Override
	public ArrayList<String> getPrjevlids(String qid){
		ArrayList<String> prjevlids = new ArrayList<String>();
		
		List<Object> args = new ArrayList<Object>();
		args.add(qid);
		
		int minseq = baseDao.findUniqueEntity("select min(seqno) from KstarPrjEvl where evlSt = 'S03' and quotCode = ? ",args.toArray());
		
		
		List<Object> args1 = new ArrayList<Object>();
		args1.add(minseq);
		args1.add(qid);
		
		String hql = "select p from KstarPrjEvl p where p.evlSt = 'S03' and p.seqno = ? and p.quotCode = ? ";
		List<KstarPrjEvl> list = baseDao.findEntity(hql,args1.toArray());
		
		for(KstarPrjEvl theprjevl : list) {
			prjevlids.add(theprjevl.getId());
		}
		
		
		return prjevlids;
	}
	
	
	
	@Override
	public ArrayList<String> getevlTypbyids(ArrayList<String> ids){
		ArrayList<String> evlTyparry = new ArrayList<String>();
			
		for(int i=0;i< ids.size();i++){
			List<Object> args = new ArrayList<Object>();
			args.add(ids.get(i));
			String evlTyp = baseDao.findUniqueEntity("select evlTyp from KstarPrjEvl where id = ? ",args.toArray());
			evlTyparry.add(evlTyp);
		}
		
		return evlTyparry;
	}
	
	
	
	
	
	@Override
	public ArrayList<String> getevlTyp(String[] ids){
		ArrayList<String> evlTyparry = new ArrayList<String>();
			
		for(String id:ids){
			List<Object> args = new ArrayList<Object>();
			args.add(id);
			String evlTyp = baseDao.findUniqueEntity("select evlTyp from KstarPrjEvl where evlSt = 'S03' and id = ? ",args.toArray());
			evlTyparry.add(evlTyp);
		}
		
		return evlTyparry;
	}
	
	
	@Override
	public ArrayList<String> getevlTypbyArraylist(ArrayList<String> ids){
		ArrayList<String> evlTyparry = new ArrayList<String>();
			
		for(String id:ids){
			List<Object> args = new ArrayList<Object>();
			args.add(id);
			String evlTyp = baseDao.findUniqueEntity("select evlTyp from KstarPrjEvl where evlSt = 'S03' and id = ? ",args.toArray());
			evlTyparry.add(evlTyp);
		}
		
		return evlTyparry;
	}
	
	
	
	@Override
	public ArrayList<String> getevlIds(String[] ids){
		ArrayList<String> idsarry = new ArrayList<String>();
			
		for(String id:ids){
			List<Object> args = new ArrayList<Object>();
			args.add(id);
			String evlTyp = baseDao.findUniqueEntity("select id from KstarPrjEvl where evlSt = 'S03' and id = ? ",args.toArray());
			idsarry.add(evlTyp);
		}
		
		return idsarry;
	}
	
	
	
	
	@Override
	public String [] splitlovpath(String lovpath){
		
		String [] path = lovpath.split("\\");

		return path;
	}
	
	
	
	
	//更新合同特价审批标志
	
	@Override
	public void updateCntSprvmrk(UserObject currentUser,String cntId, String orgid) throws AnneException {
		
		Map<String,KstarPrjLst> prdmap = new HashMap<String,KstarPrjLst>();
		Map<String,ProductPriceLine> prclnmap = new HashMap<String,ProductPriceLine>();
		
		//当前登录用户
		//currentUser
		
		//当前提交人的岗位在具体的哪一个价格层级名称（例如：层一）
		String currentLaylinenamenm = "";
		//当前提交人的岗位在具体的哪一个价格层级（例如：0）
		int currentLaylineNo = 0;
		
		//特价审批标志
		String sprvmrk = "N";
		//需要特价审批申请的层次
		String ntdprvlevel = "";
		
		
		
		//销售部门 orgid
		String quotuserOrgid = "";
				
		if(orgid!=null){
			quotuserOrgid = orgid;
		}
				

		List<Object> args0 = new ArrayList<Object>();
		args0.add(cntId);
		
		
		//PrjLst
		StringBuffer hql = new StringBuffer(" from KstarPrjLst where quotCode = ? ");
		List<KstarPrjLst> KstarPrjLsts = baseDao.findEntity(hql.toString(), args0.toArray());
		
		for(KstarPrjLst prjLst : KstarPrjLsts) {
			//产品ID
			
			if(prjLst.getProId()!=null){
				prdmap.put(prjLst.getProId(), prjLst);
				}

		}
		

		List<Object> args = new ArrayList<Object>();
		args.add(quotuserOrgid);
		
		
		

		
		
		
		
		//组织/价格表
		StringBuffer hql2 = new StringBuffer(" from ProductPriceHead where id = ? ");
		
		List<ProductPriceHead> ProductPriceHeads = baseDao.findEntity(hql2.toString(), args.toArray());
		
		for(ProductPriceHead priceheader : ProductPriceHeads) {
			
			List<Object> args9 = new ArrayList<Object>();
			args9.add(priceheader.getOrganization());
			
			
			//组织/层级对照表
			StringBuffer hql1 = new StringBuffer(" from PriceLayCompareHeader where organization = ? ");
			
			List<PriceLayCompareHeader> PriceLayCompareHeaders = baseDao.findEntity(hql1.toString(), args9.toArray());
			
			for(PriceLayCompareHeader pricecompheader : PriceLayCompareHeaders) {
				
				//层级行表
				StringBuffer hql4 = new StringBuffer(" from PriceLayCompareLine where headerId = ? ");
				List<Object> args4 = new ArrayList<Object>();
				args4.add(pricecompheader.getId());
				
				List<PriceLayCompareLine> PriceLayCompareLines = baseDao.findEntity(hql4.toString(), args4.toArray());
				
				for(PriceLayCompareLine prclycmln : PriceLayCompareLines) {
					
					String businessType ="layerpricecomp";
					String businessId = prclycmln.getId();
					String orgId = pricecompheader.getOrganization(); 
					
					
					//当前登录用户岗位ID
					String curruserposid = currentUser.getPosition().getId();
					
					//岗位
					String hql5 = " select new com.ibm.kstar.entity.team.vo.TeamVo(t,e,p,org) from Team t ,Employee e ,LovMember p,LovMember o ,LovMember org where t.positionId = p.id and p.groupId = 'POSITION' and o.groupId='ORG' and t.masterEmployeeId = e.id and p.optTxt1 = o.id and o.parentId = org.id and t.businessId = ? and t.businessType = ? ";
					
					List<Object> args5 = new ArrayList<Object>();
					args5.add(businessId);
					args5.add(businessType);
					
					List<TeamVo> positionDtls = baseDao.findEntity(hql5.toString(), args5.toArray());
					
					//若不匹配，则向上追溯组织,读取父组织“层级对照表”组织路径示例：科士达股份有限公司/国内营销中心/销售部/广东办事处
					
					if (positionDtls.size()<1){
						
						
						//parent lov path
						String hql8 = " select p.path from Team t ,Employee e ,LovMember p,LovMember o ,LovMember org where t.positionId = p.id and p.groupId = 'POSITION' and o.groupId='ORG' and t.masterEmployeeId = e.id and p.optTxt1 = o.id and o.parentId = org.id and t.businessId = ? and t.businessType = ? ";
						
						List<Object> args8 = new ArrayList<Object>();
						args8.add(businessId);
						args8.add(businessType);
						
						String lovpath = baseDao.findUniqueEntity(hql8.toString(), args8.toArray());
						
						if(lovpath!=null){
							String [] parentpath = splitlovpath(lovpath);
							
							int pathlen = parentpath.length;
							
							int i = 0;
							
							if(pathlen>=2){
								i = pathlen-2;
							}
						
							
							do{
								//岗位
								StringBuffer hql7 = new StringBuffer(" select new com.ibm.kstar.entity.team.vo.TeamVo(t,e,p,org) from Team t ,Employee e ,LovMember p,LovMember o ,LovMember org where t.positionId = p.id and p.groupId = 'POSITION' and o.groupId='ORG' and t.masterEmployeeId = e.id and p.optTxt1 = o.id and o.parentId = org.id and t.businessType = ? and t.businessId = ? ");
								
								List<Object> args7 = new ArrayList<Object>();
								args7.add(businessType);
								args7.add(parentpath[i]);

								positionDtls = baseDao.findEntity(hql7.toString(), args7.toArray());
								
								if(positionDtls.size()>0){
									break;
								}
										
								i--;
							}while(i>=0);
							
							
						}
						
						
					}
					
					
					for(TeamVo position : positionDtls){
						
						//匹配当前提交人的岗位在具体的哪一个价格层级
						if(curruserposid.equals(position.getId())){
							currentLaylinenamenm = prclycmln.getLayLineNameName().trim();
						}
						
					}
						
						
					
				}

			}
			
			
			if(currentLaylinenamenm.equals("层一")){
				currentLaylineNo=1;
			}
			if(currentLaylinenamenm.equals("层二")){
				currentLaylineNo=2;
			}
			if(currentLaylinenamenm.equals("层三")){
				currentLaylineNo=3;
			}
			if(currentLaylinenamenm.equals("层四")){
				currentLaylineNo=4;
			}
			if(currentLaylinenamenm.equals("层五")){
				currentLaylineNo=5;
			}
			if(currentLaylinenamenm.equals("层六")){
				currentLaylineNo=6;
			}
			
			
			
			
			//价格行表			
			StringBuffer hql3 = new StringBuffer(" from ProductPriceLine where headId = ? ");
			List<Object> args3 = new ArrayList<Object>();
			args3.add(priceheader.getId());
			
			List<ProductPriceLine> ProductPriceLines = baseDao.findEntity(hql3.toString(), args3.toArray());
			
			for(ProductPriceLine priceLine : ProductPriceLines) {
				
				//产品ID
				prclnmap.put(priceLine.getProductID(), priceLine);
				
			}
		
		}
		
		
		// PrjLst
		Iterator it = prdmap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String key = (entry.getKey()).toString();
			KstarPrjLst prjLstprd = (KstarPrjLst) (entry.getValue());

			//// 价格行表
			Iterator priceLine_it = prclnmap.entrySet().iterator();
			while (priceLine_it.hasNext()) {
				Map.Entry priceLine_entry = (Map.Entry) priceLine_it.next();
				String priceLine_key = (priceLine_entry.getKey()).toString();
				ProductPriceLine priceline = (ProductPriceLine) (priceLine_entry.getValue());

				// 产品ID相同
				if (prjLstprd.getProId().equals(priceline.getProductID())) {
					
					//PrjLst数量
					if(prjLstprd.getAmt()!=null){
						
						Double layer1Amt = 0.0;
						Double layer2Amt = 0.0;
						Double layer3Amt = 0.0;
						Double layer4Amt = 0.0;
						Double layer5Amt = 0.0;
						Double layer6Amt = 0.0;

						Double layer1price = 0.0;
						Double layer2price = 0.0;
						Double layer3price = 0.0;
						Double layer4price = 0.0;
						Double layer5price = 0.0;
						Double layer6price = 0.0;
						
						
						if(priceline.getLayer1Number()!=null){
							
							//层级数量
							layer1Amt = Double.parseDouble(String.valueOf(priceline.getLayer1Number()));
							layer2Amt = Double.parseDouble(String.valueOf(priceline.getLayer2Number()));
							layer3Amt = Double.parseDouble(String.valueOf(priceline.getLayer3Number()));
							layer4Amt = Double.parseDouble(String.valueOf(priceline.getLayer4Number()));
							layer5Amt = Double.parseDouble(String.valueOf(priceline.getLayer5Number()));
							layer6Amt = Double.parseDouble(String.valueOf(priceline.getLayer6Number()));

						}
						

						if(priceline.getCatalogPrice()!=null){
							
							//层级价格
							if(priceline.getLayer1Discount()!=null){
								layer1price = priceline.getCatalogPrice().multiply(priceline.getLayer1Discount()).doubleValue();
							}
							
							
							if(priceline.getLayer2Discount()!=null){
								layer2price = priceline.getCatalogPrice().multiply(priceline.getLayer2Discount()).doubleValue();
							}
							
							if(priceline.getLayer3Discount()!=null){
								layer3price = priceline.getCatalogPrice().multiply(priceline.getLayer3Discount()).doubleValue();
							}
							
							if(priceline.getLayer4Discount()!=null){
								layer4price = priceline.getCatalogPrice().multiply(priceline.getLayer4Discount()).doubleValue();
							}
							
							if(priceline.getLayer5Discount()!=null){
								layer5price = priceline.getCatalogPrice().multiply(priceline.getLayer5Discount()).doubleValue();
							}
							
							if(priceline.getLayer6Discount()!=null){
								layer6price = priceline.getCatalogPrice().multiply(priceline.getLayer6Discount()).doubleValue();
							}

					}
						

						
						
						
						//层一
						//折扣率最小
						//数量最少
						//价格最高
						
						
						if(prjLstprd.getPrdPrc()!=null){
							if(priceline.getCatalogPrice()!=null){
								if(priceline.getLayer1Discount()!=null){
								
									
									//层6
									
									// 报价价格小于层价格
									if(prjLstprd.getPrdPrc() < layer6price){
										
										//报价数量大于层数量
										if(layer6Amt!=0.0){
											if (prjLstprd.getAmt() >= layer6Amt) {
													
												//比较当前提交人的岗位在具体的哪一个价格层级名称与需要申请特价审批的层次（例如：层一）
												
												if(currentLaylinenamenm!=""){
													if(currentLaylinenamenm.equals("层六")){
														sprvmrk = "Y";
													}
													
													ntdprvlevel = "level6";
													
													//修改定价
													//prjLstprd.setPrdSprc(layer6price);
												}
												


											}
										}else{
											//比较当前提交人的岗位在具体的哪一个价格层级名称与需要申请特价审批的层次（例如：层一）
											
											if(currentLaylinenamenm!=""){
												if(currentLaylinenamenm.equals("层六")){
													sprvmrk = "Y";
												}
												
												ntdprvlevel = "level6";
												
												//修改定价
												//prjLstprd.setPrdSprc(layer6price);
											}
											

											
										}
										

									}
									
									
									
									
									//层5
									
									// 报价价格小于层价格
									if(prjLstprd.getPrdPrc() < layer5price){
										
										//报价数量大于层数量
										if(layer5Amt!=0.0){
											if ((prjLstprd.getAmt() >= layer5Amt)&&(prjLstprd.getAmt() < layer6Amt)) {
													
												//比较当前提交人的岗位在具体的哪一个价格层级名称与需要申请特价审批的层次（例如：层一）
												
												if(currentLaylinenamenm!=""){
													if(currentLaylinenamenm.equals("层五")){
														sprvmrk = "Y";
													}
													
													ntdprvlevel = "level6";
													
													//修改定价
													//prjLstprd.setPrdSprc(layer5price);
												}

											}
										}else{
											//比较当前提交人的岗位在具体的哪一个价格层级名称与需要申请特价审批的层次（例如：层一）
											
											if(currentLaylinenamenm!=""){
												if(currentLaylinenamenm.equals("层五")){
													sprvmrk = "Y";
												}
												
												ntdprvlevel = "level6";
												
												//修改定价
												//prjLstprd.setPrdSprc(layer5price);
											}

											
										}
										

									}
									
									
									
									//层4
									
									// 报价价格小于层价格
									if(prjLstprd.getPrdPrc() < layer4price){
										
										//报价数量大于层数量
										if(layer4Amt!=0.0){
											if ((prjLstprd.getAmt() >= layer4Amt)&&(prjLstprd.getAmt() < layer5Amt)) {
													
												//比较当前提交人的岗位在具体的哪一个价格层级名称与需要申请特价审批的层次（例如：层一）
												
												if(currentLaylinenamenm!=""){
													if(currentLaylinenamenm.equals("层四")){
														sprvmrk = "Y";
													}
													
													ntdprvlevel = "level5";
													
													//修改定价
													//prjLstprd.setPrdSprc(layer4price);
												}
												


											}
										}else{
											//比较当前提交人的岗位在具体的哪一个价格层级名称与需要申请特价审批的层次（例如：层一）
											
											if(currentLaylinenamenm!=""){
												if(currentLaylinenamenm.equals("层四")){
													sprvmrk = "Y";
												}
												
												ntdprvlevel = "level5";
												
												//修改定价
												//prjLstprd.setPrdSprc(layer4price);
											}
											

											
										}
										

									}
									
									
									
									//层3
									
									// 报价价格小于层价格
									if(prjLstprd.getPrdPrc() < layer3price){
										
										//报价数量大于层数量
										if(layer3Amt!=0.0){
											if ((prjLstprd.getAmt() >= layer3Amt)&&(prjLstprd.getAmt() < layer4Amt)) {
													
												//比较当前提交人的岗位在具体的哪一个价格层级名称与需要申请特价审批的层次（例如：层一）
												
												if(currentLaylinenamenm!=""){
													if(currentLaylinenamenm.equals("层三")){
														sprvmrk = "Y";
													}
													
													ntdprvlevel = "level4";
													
													//修改定价
													//prjLstprd.setPrdSprc(layer3price);

												}
												

											}
										}else{
											//比较当前提交人的岗位在具体的哪一个价格层级名称与需要申请特价审批的层次（例如：层一）
											
											if(currentLaylinenamenm!=""){
												if(currentLaylinenamenm.equals("层三")){
													sprvmrk = "Y";
												}
												
												ntdprvlevel = "level4";
												
												//修改定价
												//prjLstprd.setPrdSprc(layer3price);
											}

											
										}
										

									}
									
									
									
									//层2
									
									// 报价价格小于层价格
									if(prjLstprd.getPrdPrc() < layer2price){
										
										//报价数量大于层数量
										if(layer2Amt!=0.0){
											if ((prjLstprd.getAmt() >= layer2Amt)&&(prjLstprd.getAmt() < layer3Amt)) {
													
												//比较当前提交人的岗位在具体的哪一个价格层级名称与需要申请特价审批的层次（例如：层一）
												
												if(currentLaylinenamenm!=""){
													if(currentLaylinenamenm.equals("层二")){
														sprvmrk = "Y";
													}
													
													ntdprvlevel = "level3";
													
													//修改定价
													//prjLstprd.setPrdSprc(layer2price);
												}

											}
										}else{
											//比较当前提交人的岗位在具体的哪一个价格层级名称与需要申请特价审批的层次（例如：层一）
											
											if(currentLaylinenamenm!=""){
												if(currentLaylinenamenm.equals("层二")){
													sprvmrk = "Y";
												}
												
												ntdprvlevel = "level3";
												
												//修改定价
												//prjLstprd.setPrdSprc(layer2price);
											}
											

											
										}
										

									}
									
									
									//层1
									
									// 报价价格小于层价格
									if(prjLstprd.getPrdPrc() < layer1price){
										
										//报价数量大于层数量
										if(layer2Amt!=0.0){
											if ((prjLstprd.getAmt() >= layer1Amt)&&(prjLstprd.getAmt() < layer2Amt)) {
													
												//比较当前提交人的岗位在具体的哪一个价格层级名称与需要申请特价审批的层次（例如：层一）
												
												if(currentLaylinenamenm!=""){
													if(currentLaylinenamenm.equals("层一")){
														sprvmrk = "Y";
													}
													
													ntdprvlevel = "level2";
													
													//修改定价
													//prjLstprd.setPrdSprc(layer1price);
												}


											}
										}else{
											//比较当前提交人的岗位在具体的哪一个价格层级名称与需要申请特价审批的层次（例如：层一）
											
											if(currentLaylinenamenm!=""){
												if(currentLaylinenamenm.equals("层一")){
													sprvmrk = "Y";
												}
												
												ntdprvlevel = "level2";
												
												//修改定价
												//prjLstprd.setPrdSprc(layer1price);
											}

											
										}
										

									}
									
									
									
									
									
									
									
									
									
									
									
											
									
								
								}
							}
							
						}
						
						

						

					}
				}

			}

			//更新特价审批标志
			prjLstprd.setSprvMrk(sprvmrk);
			prjLstprd.setCurLvl(currentLaylinenamenm);
			prjLstprd.setSprvLvl(ntdprvlevel);
			//更新prjlst
			baseDao.update(prjLstprd);
		}
		
		
		
	}
	
	
	
	
	
	
	//更新特价审批标志
	
	@Override
	public void updateprjLstSprvmrk(UserObject currentUser,KstarPrjLst prjLst) throws AnneException {
		
		//当前登录用户
		//currentUser
		
		//当前提交人的岗位在具体的哪一个价格层级名称（例如：层一）
		String currentLaylinenamenm = "";
		//当前提交人的岗位在具体的哪一个价格层级（例如：0）
		int currentLaylineNo = 0;
		
		//当前提交人的所属层级的默认折扣率
		Double currentLaydefaultdiscountrt = 0.0;
		
		//特价审批标志
		String sprvmrk = "N";
		//需要特价审批申请的层次 ：最高层次
		String ntdprvlevel = "";
		
		String qid = prjLst.getQuotCode();
		
		KstarQuot quot = this.getKstarQuot(qid);
		
		//价格表 orgid
		String quotuserOrgid = "";
		
		if(quot.getPriceListid()!=null){
			quotuserOrgid = quot.getPriceListid();
		}
		
		List<Object> args = new ArrayList<Object>();
		args.add(quotuserOrgid);
		
		
		//当前登录用户岗位ID
		String curruserposid = currentUser.getPosition().getId();
		
		String hql0 = " select c from Team t,PriceLayCompareLine c where t.businessId = c.id and t.positionId = ? ";

		String hql1 = " select c from PriceLayCompareLine c where c.headerId = ? and c.discountRate = ? ";
	
		List<Object> args0 = new ArrayList<Object>();
		
		args0.add(curruserposid);
		
		List<PriceLayCompareLine> pricelayline = baseDao.findEntity(hql0.toString(), args0.toArray());
		
		if(pricelayline.size()<1){
			
		}else{
			
			PriceLayCompareLine thepricelayline = pricelayline.get(0);
			
			if(thepricelayline.getDiscountRate()!=null){
				currentLaydefaultdiscountrt = thepricelayline.getDiscountRate().doubleValue()*100;
			}
			
			if(thepricelayline.getLayLineNameName()!=null){
				currentLaylinenamenm = thepricelayline.getLayLineNameName();
			}
			
			
			if(thepricelayline.getHeaderId()!=null){
				
				if(prjLst.getApplyDiscount()!=null){
					
					List<Object> args1 = new ArrayList<Object>();
					
					BigDecimal applyDiscount = new BigDecimal((prjLst.getApplyDiscount()/100));
					
					applyDiscount = applyDiscount.setScale(2, RoundingMode.HALF_UP);
					
					args1.add(thepricelayline.getHeaderId());
					args1.add(applyDiscount);
					
					List<PriceLayCompareLine> pricelaylines = baseDao.findEntity(hql1.toString(), args1.toArray());
					
					if(pricelaylines.size()<1){
						
					}else{
						ntdprvlevel = pricelaylines.get(0).getLayLineNameName();
						sprvmrk = "Y";
					}
					
				}

			}
			
			if(prjLst.getApproveDiscount()!=null){
				if(prjLst.getApproveDiscount()<currentLaydefaultdiscountrt){
					
					prjLst.setApproveDiscount(0.0);
					baseDao.update(prjLst);
					
					throw new AnneException("审批人员填写的批复折扣需大于等于其所属层级的折扣率，不能小于 "+currentLaydefaultdiscountrt+"% !");
				}
			}
				
//				//更新特价审批标志
//				prjLst.setSprvMrk(sprvmrk);
//				prjLst.setCurLvl(currentLaylinenamenm);
//				prjLst.setDfDisrt(currentLaydefaultdiscountrt);
//				prjLst.setSprvLvl(ntdprvlevel);
//				//更新prjlst
//				baseDao.update(prjLst);
			
		}
}
			
		

		
		

	
	//特价审批启动时更新特价审批标志
	
	@Override
	public void updateprjLstSprvmrkstart(UserObject currentUser,KstarPrjLst prjLst) throws AnneException {
		
		
		
		//当前登录用户
		//currentUser
		
		//当前提交人的岗位在具体的哪一个价格层级名称（例如：层一）
		String currentLaylinenamenm = "";
		//当前提交人的岗位在具体的哪一个价格层级（例如：0）
		int currentLaylineNo = 0;
		
		
		
		//当前提交人的所属层级的默认折扣率
		Double currentLaydefaultdiscountrt = 0.0;
		
		
		//特价审批标志
		String sprvmrk = "N";
		//需要特价审批申请的层次 ：最高层次
		String ntdprvlevel = "";
		
		
		
		String qid = prjLst.getQuotCode();
		
		KstarQuot quot = this.getKstarQuot(qid);
		
		//价格表 orgid
		String quotuserOrgid = "";
		
		if(quot.getPriceListid()!=null){
			quotuserOrgid = quot.getPriceListid();
		}
		
		
		

		List<Object> args = new ArrayList<Object>();
		args.add(quotuserOrgid);
		
		
		//当前登录用户岗位ID
		String curruserposid = currentUser.getPosition().getId();
		
		String hql0 = " select c from Team t,PriceLayCompareLine c where t.businessId = c.id and t.positionId = ? ";

		String hql1 = " select c from PriceLayCompareLine c where c.headerId = ? and c.discountRate = ? ";
	
		List<Object> args0 = new ArrayList<Object>();
		
		args0.add(curruserposid);
		
		List<PriceLayCompareLine> pricelayline = baseDao.findEntity(hql0.toString(), args0.toArray());
		
		if(pricelayline.size()<1){
			
		}else{
			
			PriceLayCompareLine thepricelayline = pricelayline.get(0);
			
			if(thepricelayline.getDiscountRate()!=null){
				currentLaydefaultdiscountrt = thepricelayline.getDiscountRate().doubleValue()*100;
			}
			
			if(thepricelayline.getLayLineNameName()!=null){
				currentLaylinenamenm = thepricelayline.getLayLineNameName();
			}
			
			
			if(thepricelayline.getHeaderId()!=null){
				
				if(prjLst.getApplyDiscount()!=null){
					
					List<Object> args1 = new ArrayList<Object>();
					
					BigDecimal applyDiscount = new BigDecimal((prjLst.getApplyDiscount()/100));
					
					applyDiscount = applyDiscount.setScale(2, RoundingMode.HALF_UP);
					
					args1.add(thepricelayline.getHeaderId());
					args1.add(applyDiscount);
					
					List<PriceLayCompareLine> pricelaylines = baseDao.findEntity(hql1.toString(), args1.toArray());
					
					if(pricelaylines.size()<1){
						
					}else{
						ntdprvlevel = pricelaylines.get(0).getLayLineNameName();
						sprvmrk = "Y";
					}
					
				}

			}
			
			
	
				

				
				
				//更新特价审批标志
				prjLst.setSprvMrk(sprvmrk);
				prjLst.setCurLvl(currentLaylinenamenm);
				prjLst.setDfDisrt(currentLaydefaultdiscountrt);
				prjLst.setSprvLvl(ntdprvlevel);
				//更新prjlst
				baseDao.update(prjLst);
			
		}
		

}	
	
	
	
	
	
	
	
	//批量更新特价审批标志
	
	@Override
	public void updateSprvmrk(UserObject currentUser,KstarQuot quot) throws AnneException {
		
		Map<String,KstarPrjLst> prdmap = new HashMap<String,KstarPrjLst>();
		Map<String,ProductPriceLine> prclnmap = new HashMap<String,ProductPriceLine>();
		
		//当前登录用户
		//currentUser
		
		//当前提交人的岗位在具体的哪一个价格层级名称（例如：层一）
		String currentLaylinenamenm = "";
		//当前提交人的岗位在具体的哪一个价格层级（例如：0）
		int currentLaylineNo = 0;
		
		//当前提交人的所属层级的默认折扣率
		Double currentLaydefaultdiscountrt = 0.0;
		
		
		//特价审批标志
		String sprvmrk = "N";
		//需要特价审批申请的层次
		String ntdprvlevel = "";
		
		
		
		
		//销售部门 orgid
//		String quotuserOrgid = "";
//				
//		if(quot.getSalDep()!=null){
//			quotuserOrgid = quot.getSalDep();
//		}
				
		
		//价格表 orgid
		String quotuserOrgid = "";
		
		if(quot.getPriceListid()!=null){
			quotuserOrgid = quot.getPriceListid();
		}
		
		

		List<Object> args0 = new ArrayList<Object>();
		args0.add(quot.getId());
		
		//PrjLst
		StringBuffer hql = new StringBuffer(" from KstarPrjLst where quotCode = ? ");
		List<KstarPrjLst> KstarPrjLsts = baseDao.findEntity(hql.toString(), args0.toArray());
		
		for(KstarPrjLst prjLst : KstarPrjLsts) {
			//产品ID
			
			if(prjLst.getProId()!=null){
				prdmap.put(prjLst.getProId(), prjLst);
				}

		}
		

		List<Object> args = new ArrayList<Object>();
		args.add(quotuserOrgid);
		
		
		

		
		
		
		
		//组织/价格表
		StringBuffer hql2 = new StringBuffer(" from ProductPriceHead where id = ? ");
		
		List<ProductPriceHead> ProductPriceHeads = baseDao.findEntity(hql2.toString(), args.toArray());
		
		for(ProductPriceHead priceheader : ProductPriceHeads) {
			
			List<Object> args9 = new ArrayList<Object>();
			args9.add(priceheader.getOrganization());
			
			
			//组织/层级对照表
			StringBuffer hql1 = new StringBuffer(" from PriceLayCompareHeader where organization = ? ");
			
			List<PriceLayCompareHeader> PriceLayCompareHeaders = baseDao.findEntity(hql1.toString(), args9.toArray());
			
			for(PriceLayCompareHeader pricecompheader : PriceLayCompareHeaders) {
				
				//层级行表
				StringBuffer hql4 = new StringBuffer(" from PriceLayCompareLine where headerId = ? ");
				List<Object> args4 = new ArrayList<Object>();
				args4.add(pricecompheader.getId());
				
				List<PriceLayCompareLine> PriceLayCompareLines = baseDao.findEntity(hql4.toString(), args4.toArray());
				
				for(PriceLayCompareLine prclycmln : PriceLayCompareLines) {
					
					String businessType ="layerpricecomp";
					String businessId = prclycmln.getId();
					String orgId = pricecompheader.getOrganization(); 
					
					
					//当前登录用户岗位ID
					String curruserposid = currentUser.getPosition().getId();
					
					//岗位
					String hql5 = " select new com.ibm.kstar.entity.team.vo.TeamVo(t,e,p,org) from Team t ,Employee e ,LovMember p,LovMember o ,LovMember org where t.positionId = p.id and p.groupId = 'POSITION' and o.groupId='ORG' and t.masterEmployeeId = e.id and p.optTxt1 = o.id and o.parentId = org.id and t.businessId = ? and t.businessType = ? ";
					
					List<Object> args5 = new ArrayList<Object>();
					args5.add(businessId);
					args5.add(businessType);
					
					List<TeamVo> positionDtls = baseDao.findEntity(hql5.toString(), args5.toArray());
					
					//若不匹配，则向上追溯组织,读取父组织“层级对照表”组织路径示例：科士达股份有限公司/国内营销中心/销售部/广东办事处
					
					if (positionDtls.size()<1){
						
						
						//parent lov path
						String hql8 = " select p.path from Team t ,Employee e ,LovMember p,LovMember o ,LovMember org where t.positionId = p.id and p.groupId = 'POSITION' and o.groupId='ORG' and t.masterEmployeeId = e.id and p.optTxt1 = o.id and o.parentId = org.id and t.businessId = ? and t.businessType = ? ";
						
						List<Object> args8 = new ArrayList<Object>();
						args8.add(businessId);
						args8.add(businessType);
						
						String lovpath = baseDao.findUniqueEntity(hql8.toString(), args8.toArray());
						
						if(lovpath!=null){
							String [] parentpath = splitlovpath(lovpath);
							
							int pathlen = parentpath.length;
							
							int i = 0;
							
							if(pathlen>=2){
								i = pathlen-2;
							}
						
							
							do{
								//岗位
								StringBuffer hql7 = new StringBuffer(" select new com.ibm.kstar.entity.team.vo.TeamVo(t,e,p,org) from Team t ,Employee e ,LovMember p,LovMember o ,LovMember org where t.positionId = p.id and p.groupId = 'POSITION' and o.groupId='ORG' and t.masterEmployeeId = e.id and p.optTxt1 = o.id and o.parentId = org.id and t.businessType = ? and t.businessId = ? ");
								
								List<Object> args7 = new ArrayList<Object>();
								args7.add(businessType);
								args7.add(parentpath[i]);

								positionDtls = baseDao.findEntity(hql7.toString(), args7.toArray());
								
								if(positionDtls.size()>0){
									break;
								}
										
								i--;
							}while(i>=0);
							
							
						}
						
						
					}
					
					
					for(TeamVo position : positionDtls){
						
						//匹配当前提交人的岗位在具体的哪一个价格层级
						if(curruserposid.equals(position.getId())){
							currentLaylinenamenm = prclycmln.getLayLineNameName().trim();
							
							if(prclycmln.getDiscountRate()!=null){
								currentLaydefaultdiscountrt = prclycmln.getDiscountRate().doubleValue();
							}
							
						}
						
					}
						
						
					
				}

			}
			
			
			if(currentLaylinenamenm.equals("层一")){
				currentLaylineNo=1;
			}
			if(currentLaylinenamenm.equals("层二")){
				currentLaylineNo=2;
			}
			if(currentLaylinenamenm.equals("层三")){
				currentLaylineNo=3;
			}
			if(currentLaylinenamenm.equals("层四")){
				currentLaylineNo=4;
			}
			if(currentLaylinenamenm.equals("层五")){
				currentLaylineNo=5;
			}
			if(currentLaylinenamenm.equals("层六")){
				currentLaylineNo=6;
			}
			
			
			
			
			//价格行表			
			StringBuffer hql3 = new StringBuffer(" from ProductPriceLine where headId = ? ");
			List<Object> args3 = new ArrayList<Object>();
			args3.add(priceheader.getId());
			
			List<ProductPriceLine> ProductPriceLines = baseDao.findEntity(hql3.toString(), args3.toArray());
			
			for(ProductPriceLine priceLine : ProductPriceLines) {
				
				//产品ID
				prclnmap.put(priceLine.getProductID(), priceLine);
				
			}
		
		}
		
		
		// PrjLst
		Iterator it = prdmap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String key = (entry.getKey()).toString();
			KstarPrjLst prjLstprd = (KstarPrjLst) (entry.getValue());

			//// 价格行表
			Iterator priceLine_it = prclnmap.entrySet().iterator();
			while (priceLine_it.hasNext()) {
				Map.Entry priceLine_entry = (Map.Entry) priceLine_it.next();
				String priceLine_key = (priceLine_entry.getKey()).toString();
				ProductPriceLine priceline = (ProductPriceLine) (priceLine_entry.getValue());

				// 产品ID相同
				if (prjLstprd.getProId().equals(priceline.getProductID())) {
					
					//PrjLst数量
					if(prjLstprd.getAmt()!=null){
						
						Double layer1Amt = 0.0;
						Double layer2Amt = 0.0;
						Double layer3Amt = 0.0;
						Double layer4Amt = 0.0;
						Double layer5Amt = 0.0;
						Double layer6Amt = 0.0;

						Double layer1price = 0.0;
						Double layer2price = 0.0;
						Double layer3price = 0.0;
						Double layer4price = 0.0;
						Double layer5price = 0.0;
						Double layer6price = 0.0;
						
						
						if(priceline.getLayer1Number()!=null){
							
							//层级数量
							layer1Amt = Double.parseDouble(String.valueOf(priceline.getLayer1Number()));
							layer2Amt = Double.parseDouble(String.valueOf(priceline.getLayer2Number()));
							layer3Amt = Double.parseDouble(String.valueOf(priceline.getLayer3Number()));
							layer4Amt = Double.parseDouble(String.valueOf(priceline.getLayer4Number()));
							layer5Amt = Double.parseDouble(String.valueOf(priceline.getLayer5Number()));
							layer6Amt = Double.parseDouble(String.valueOf(priceline.getLayer6Number()));

						}
						

						if(priceline.getCatalogPrice()!=null){
								
								//层级价格
								if(priceline.getLayer1Discount()!=null){
									layer1price = priceline.getCatalogPrice().multiply(priceline.getLayer1Discount()).doubleValue();
								}
								
								
								if(priceline.getLayer2Discount()!=null){
									layer2price = priceline.getCatalogPrice().multiply(priceline.getLayer2Discount()).doubleValue();
								}
								
								if(priceline.getLayer3Discount()!=null){
									layer3price = priceline.getCatalogPrice().multiply(priceline.getLayer3Discount()).doubleValue();
								}
								
								if(priceline.getLayer4Discount()!=null){
									layer4price = priceline.getCatalogPrice().multiply(priceline.getLayer4Discount()).doubleValue();
								}
								
								if(priceline.getLayer5Discount()!=null){
									layer5price = priceline.getCatalogPrice().multiply(priceline.getLayer5Discount()).doubleValue();
								}
								
								if(priceline.getLayer6Discount()!=null){
									layer6price = priceline.getCatalogPrice().multiply(priceline.getLayer6Discount()).doubleValue();
								}

						}
						

						
						
						
						//层一
						//折扣率最小
						//数量最少
						//价格最高
						
						
									
									
									//层6
									
									// 报价价格小于层价格
									if(prjLstprd.getPrdPrc() < layer6price){
										
										//报价数量大于层数量
										if(layer6Amt!=0.0){
											if (prjLstprd.getAmt() >= layer6Amt) {
													
												//比较当前提交人的岗位在具体的哪一个价格层级名称与需要申请特价审批的层次（例如：层一）
												
												if(currentLaylinenamenm!=""){
													if(currentLaylinenamenm.equals("层六")){
														sprvmrk = "Y";
													}
													ntdprvlevel = "level6";
													
													//修改定价
													//prjLstprd.setPrdSprc(layer6price);
												}
												


											}
										}else{
											//比较当前提交人的岗位在具体的哪一个价格层级名称与需要申请特价审批的层次（例如：层一）
											
											if(currentLaylinenamenm!=""){
												if(currentLaylinenamenm.equals("层六")){
													sprvmrk = "Y";
												}
												
												ntdprvlevel = "level6";
												
												//修改定价
												//prjLstprd.setPrdSprc(layer6price);
											}
											

											
										}
										

									}
									
									
									
									//层5
									
									// 报价价格小于层价格
									if(prjLstprd.getPrdPrc() < layer5price){
										
										//报价数量大于层数量
										if(layer5Amt!=0.0){
											if ((prjLstprd.getAmt() >= layer5Amt)&&(prjLstprd.getAmt() < layer6Amt)) {
													
												//比较当前提交人的岗位在具体的哪一个价格层级名称与需要申请特价审批的层次（例如：层一）
												
												if(currentLaylinenamenm!=""){
													if(currentLaylinenamenm.equals("层五")){
														sprvmrk = "Y";
													}
													
													ntdprvlevel = "level6";
													
													//修改定价
													//prjLstprd.setPrdSprc(layer5price);
												}

											}
										}else{
											//比较当前提交人的岗位在具体的哪一个价格层级名称与需要申请特价审批的层次（例如：层一）
											
											if(currentLaylinenamenm!=""){
												if(currentLaylinenamenm.equals("层五")){
													sprvmrk = "Y";
												}
												
												ntdprvlevel = "level6";
												
												//修改定价
												//prjLstprd.setPrdSprc(layer5price);
											}

											
										}
										

									}
									
									
									
									
									//层4
									
									// 报价价格小于层价格
									if(prjLstprd.getPrdPrc() < layer4price){
										
										//报价数量大于层数量
										if(layer4Amt!=0.0){
											if ((prjLstprd.getAmt() >= layer4Amt)&&(prjLstprd.getAmt() < layer5Amt)) {
													
												//比较当前提交人的岗位在具体的哪一个价格层级名称与需要申请特价审批的层次（例如：层一）
												
												if(currentLaylinenamenm!=""){
													if(currentLaylinenamenm.equals("层四")){
														sprvmrk = "Y";
													}
													
													ntdprvlevel = "level5";
													
													//修改定价
													//prjLstprd.setPrdSprc(layer4price);
												}
												


											}
										}else{
											//比较当前提交人的岗位在具体的哪一个价格层级名称与需要申请特价审批的层次（例如：层一）
											
											if(currentLaylinenamenm!=""){
												if(currentLaylinenamenm.equals("层四")){
													sprvmrk = "Y";
												}
												
												ntdprvlevel = "level5";
												
												//修改定价
												//prjLstprd.setPrdSprc(layer4price);
											}
											

											
										}
										

									}
										
									
									
									//层3
									
									// 报价价格小于层价格
									if(prjLstprd.getPrdPrc() < layer3price){
										
										//报价数量大于层数量
										if(layer3Amt!=0.0){
											if ((prjLstprd.getAmt() >= layer3Amt)&&(prjLstprd.getAmt() < layer4Amt)) {
													
												//比较当前提交人的岗位在具体的哪一个价格层级名称与需要申请特价审批的层次（例如：层一）
												
												if(currentLaylinenamenm!=""){
													if(currentLaylinenamenm.equals("层三")){
														sprvmrk = "Y";
													}
													
													ntdprvlevel = "level4";
													
													//修改定价
													//prjLstprd.setPrdSprc(layer3price);

												}
												

											}
										}else{
											//比较当前提交人的岗位在具体的哪一个价格层级名称与需要申请特价审批的层次（例如：层一）
											
											if(currentLaylinenamenm!=""){
												if(currentLaylinenamenm.equals("层三")){
													sprvmrk = "Y";
												}
												
												ntdprvlevel = "level4";
												
												//修改定价
												//prjLstprd.setPrdSprc(layer3price);
											}

											
										}
										

									}
									
									
									
									
									//层2
									
									// 报价价格小于层价格
									if(prjLstprd.getPrdPrc() < layer2price){
										
										//报价数量大于层数量
										if(layer2Amt!=0.0){
											if ((prjLstprd.getAmt() >= layer2Amt)&&(prjLstprd.getAmt() < layer3Amt)) {
													
												//比较当前提交人的岗位在具体的哪一个价格层级名称与需要申请特价审批的层次（例如：层一）
												
												if(currentLaylinenamenm!=""){
													if(currentLaylinenamenm.equals("层二")){
														sprvmrk = "Y";
													}
													
													ntdprvlevel = "level3";
													
													//修改定价
													//prjLstprd.setPrdSprc(layer2price);
												}

											}
										}else{
											//比较当前提交人的岗位在具体的哪一个价格层级名称与需要申请特价审批的层次（例如：层一）
											
											if(currentLaylinenamenm!=""){
												if(currentLaylinenamenm.equals("层二")){
													sprvmrk = "Y";
												}
												
												ntdprvlevel = "level3";
												
												//修改定价
												//prjLstprd.setPrdSprc(layer2price);
											}
											

											
										}
										

									}
									
									
									
									//层1
									
									// 报价价格小于层价格
									if(prjLstprd.getPrdPrc() < layer1price){
										
										//报价数量大于层数量
										if(layer2Amt!=0.0){
											if ((prjLstprd.getAmt() >= layer1Amt)&&(prjLstprd.getAmt() < layer2Amt)) {
													
												//比较当前提交人的岗位在具体的哪一个价格层级名称与需要申请特价审批的层次（例如：层一）
												
												if(currentLaylinenamenm!=""){
													if(currentLaylinenamenm.equals("层一")){
														sprvmrk = "Y";
													}
													
													ntdprvlevel = "level2";
													
													//修改定价
													//prjLstprd.setPrdSprc(layer1price);
												}


											}
										}else{
											//比较当前提交人的岗位在具体的哪一个价格层级名称与需要申请特价审批的层次（例如：层一）
											
											if(currentLaylinenamenm!=""){
												if(currentLaylinenamenm.equals("层一")){
													sprvmrk = "Y";
												}
												
												ntdprvlevel = "level2";
												
												//修改定价
												//prjLstprd.setPrdSprc(layer1price);
											}

											
										}
										

									}
									
									
//									if(prjLstprd.getPrdPrc()!=null){
//										if(priceline.getCatalogPrice()!=null){
//											if(priceline.getLayer1Discount()!=null){
//											
//												//层1						
//												if (prjLstprd.getPrdPrc() < layer1price) {
//													
//													if(layer1Amt!=0.0){
//														if(prjLstprd.getAmt() < layer1Amt){
//															
//															//比较当前提交人的岗位在具体的哪一个价格层级名称与需要申请特价审批的层次（例如：层一）
//															if(currentLaylinenamenm!=""){
//																if(currentLaylinenamenm.equals("层一")){
//																	sprvmrk = "N";
//																}else{
//																	sprvmrk = "Y";
//																}
//															}
//															
//															ntdprvlevel = "level1";
//															//修改定价
//															//prjLstprd.setPrdSprc(layer1price);
//
//														}
//													}else{
//														//比较当前提交人的岗位在具体的哪一个价格层级名称与需要申请特价审批的层次（例如：层一）
//														if(currentLaylinenamenm!=""){
//															if(currentLaylinenamenm.equals("层一")){
//																sprvmrk = "N";
//															}else{
//																sprvmrk = "Y";
//															}
//															
//															ntdprvlevel = "level1";
//															//修改定价
//															//prjLstprd.setPrdSprc(layer1price);
//														}
//
//													}
//																							
//
//												}
//				
//								
//								}
//							}
//							
//						}
						
						

						

					}
				}

			}

			//更新特价审批标志
			prjLstprd.setSprvMrk(sprvmrk);
			prjLstprd.setCurLvl(currentLaylinenamenm);
			prjLstprd.setDfDisrt(currentLaydefaultdiscountrt);
			prjLstprd.setSprvLvl(ntdprvlevel);
			//更新prjlst
			baseDao.update(prjLstprd);
		}
			
		
		//启动特价审批流程
//		if(sprvmrk.equals("Y")){
//			startSpProcess(currentUser, quot.getId());
//		}
//		
		
	}
	
	
	
	
	
	@Override
	public Double saveWithDouble(Double number) throws AnneException { 
		
		BigDecimal num = new BigDecimal(number);
		double result = num.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
		
		return result;
	}
	
	
	
	
	
	
	
	
	@Override
	public void reviseQuot(UserObject user,String quotID) throws AnneException { 
		
		KstarQuot newQuot = new KstarQuot();
		KstarQuot oldQuot = getKstarQuot(quotID);
		
		if(oldQuot == null){
			throw new AnneException(IQuotService.class.getName() + " reviseQuot : 没有找到要修订的数据");
		}
		
		newQuot = this.copyKstarQuot(newQuot, oldQuot, user);
		oldQuot.setIsValid("0");
		baseDao.update(oldQuot);
		
		copyAttByQuot(newQuot, oldQuot, user);
		copyMemByQuot(newQuot, oldQuot, user);
		copyPrjdtl(quotID,newQuot.getId(), "N");
		copyPginfByQuot(newQuot, oldQuot, user);
		//copyLovMemberByMemo(oldQuot.getId(), newQuot.getId());
		copyKstarPrjLst(oldQuot.getId(), newQuot.getId());
		
	}
	
	
	@Override
	public List<KstarPgInf> getPginfList(Condition condition) throws AnneException {
		FilterObject filterObject = condition.getFilterObject(KstarPgInf.class);
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.findEntity(hqlObject.getHql(),hqlObject.getArgs());
	}
	
	@Override
	public List<KstarAtt> getAttList(Condition condition) throws AnneException {
		FilterObject filterObject = condition.getFilterObject(KstarAtt.class);
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.findEntity(hqlObject.getHql(),hqlObject.getArgs());
	}

	@Override
	public List<KstarMemInfo> getMemList(Condition condition) throws AnneException {
		FilterObject filterObject = condition.getFilterObject(KstarMemInfo.class);
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.findEntity(hqlObject.getHql(),hqlObject.getArgs());
	}
	
	@Override
	public void copyAttByQuot(KstarQuot newQuot, KstarQuot oldQuot,UserObject user){
		Condition condition = new Condition();
		condition.getFilterObject().addCondition("quotCode", "=", oldQuot.getId());
		List<KstarAtt> attList = getAttList(condition);
		for (KstarAtt att: attList){
			KstarAtt newAtt = new KstarAtt();
			BeanUtils.copyProperties(att, newAtt);
			newAtt.setId(null);
			newAtt.setQuotCode(newQuot.getId());
			baseDao.save(newAtt);
		}
	}

	@Override
	public void copyMemByQuot(KstarQuot newQuot, KstarQuot oldQuot,UserObject user){
		Condition condition = new Condition();
		condition.getFilterObject().addCondition("quotCode", "=", oldQuot.getId());
		List<KstarMemInfo> memList = getMemList(condition);
		for (KstarMemInfo mem: memList){
			KstarMemInfo newObj = new KstarMemInfo();
			BeanUtils.copyProperties(mem, newObj);
			newObj.setId(null);
			newObj.setQuotCode(newQuot.getId());
			baseDao.save(newObj);
		}
	}
	
	
	@Override
	public void copyPginfByQuot(KstarQuot newQuot, KstarQuot oldQuot,UserObject user){
		Condition condition = new Condition();
		condition.getFilterObject().addCondition("quotCode", "=", oldQuot.getId());
		List<KstarPgInf> pginfList = getPginfList(condition);
		for (KstarPgInf pginf: pginfList){
			KstarPgInf newObj = new KstarPgInf();
			BeanUtils.copyProperties(pginf, newObj);
			newObj.setId(null);
			newObj.setReqNo(null);
			newObj.setcSupporter(null);
			newObj.setcStatus(null);
			newObj.setQuotCode(newQuot.getId());
			baseDao.save(newObj);
		}
	}
	
	
	//process
	@Override
	public void startPreSaleProcess(UserObject user,String id){
		//prjevl id
		String businessId = id;
		
		String module = lovMemberService.getFlowCodeByAppCode(this.QUOT_PRESALE);  
		
		KstarPrjEvl prjevl = this.getKstarPrjEvl(id);
		
		KstarQuot quot = this.getKstarQuot(prjevl.getQuotCode());
		
		String SalesCenter = quot.getMarketDept();
		String Quote_TotalAmt = quot.getAmount();
		
		Map<String,String> varmap = new HashMap<>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		String d_str = sdf.format(d);

		
		varmap.put("title", "报价售前工程评审流程 - "+user.getEmployee().getName()+" - "+d_str);
		varmap.put("app", "quot_presale");
		
		varmap.put("SalesCenter", SalesCenter);
		varmap.put("Quote_TotalAmt", Quote_TotalAmt);
		
		//ProcessInstance pi = processService.start(module, businessId, new Participant(user.getEmployee().getId(),user.getEmployee().getName(),"EMPLOYEE"),varmap);
		ProcessInstance pi = processService.start(module, businessId, user, varmap);
		
		prjevl.setEvlRs(pi.getId());
		this.updatePrjEvl(prjevl);

		KstarProductWorkFlow kf = new KstarProductWorkFlow();
		kf.setProcessID(pi.getId());
		kf.setProcessName("报价售前工程评审流程");
		kf.setCreateBy(user.getEmployee().getId());
		kf.setProductId(id);
		productProcess.save(kf,user);

	}
	
	@Override
	public void startAftSaleProcess(UserObject user,String id){
		//prjevl id
		String businessId = id;
		
		String module = lovMemberService.getFlowCodeByAppCode(this.QUOT_AFTSALE);  
		
		KstarPrjEvl prjevl = this.getKstarPrjEvl(id);
		
		KstarQuot quot = this.getKstarQuot(prjevl.getQuotCode());
		
		String SalesCenter = quot.getMarketDept();
		String Quote_TotalAmt = quot.getAmount();
		
		
		Map<String,String> varmap = new HashMap<>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		String d_str = sdf.format(d);
		
		
		varmap.put("title", "报价售后工程评审流程 - "+user.getEmployee().getName()+" - "+d_str);
		varmap.put("app", "quot_aftsale");
		
		varmap.put("SalesCenter", SalesCenter);
		varmap.put("Quote_TotalAmt", Quote_TotalAmt);
		
		//ProcessInstance pi = processService.start(module, businessId, new Participant(user.getEmployee().getId(),user.getEmployee().getName(),"EMPLOYEE"),varmap);
		ProcessInstance pi = processService.start(module, businessId, user, varmap);
		
		
		prjevl.setEvlRs(pi.getId());
		this.updatePrjEvl(prjevl);
		
		
		KstarProductWorkFlow kf = new KstarProductWorkFlow();
		kf.setProcessID(pi.getId());
		kf.setProcessName("报价售后工程评审流程");
		kf.setCreateBy(user.getEmployee().getId());
		kf.setProductId(id);
		productProcess.save(kf,user);
		
	}
	
	
	@Override
	public void startBusinessProcess(UserObject user,String id){
		//prjevl id
		String businessId = id;
		
		String module = lovMemberService.getFlowCodeByAppCode(this.QUOT_BUSPRC);
		
		KstarPrjEvl prjevl = this.getKstarPrjEvl(id);
		
		KstarQuot quot = this.getKstarQuot(prjevl.getQuotCode());
		
		String SalesCenter = quot.getMarketDept();
		String Quote_TotalAmt = quot.getAmount();
			
		Map<String,String> varmap = new HashMap<>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		String d_str = sdf.format(d);		
		
		varmap.put("title", "报价商务工程评审流程 - "+user.getEmployee().getName()+" - "+d_str);
		varmap.put("app", "quot_busprc");
		
		varmap.put("SalesCenter", SalesCenter);
		varmap.put("Quote_TotalAmt", Quote_TotalAmt);
		
		//ProcessInstance pi = processService.start(module, businessId, new Participant(user.getEmployee().getId(),user.getEmployee().getName(),"EMPLOYEE"),varmap);
		ProcessInstance pi = processService.start(module, businessId, user, varmap);
		
		
		
		prjevl.setEvlRs(pi.getId());
		this.updatePrjEvl(prjevl);
		
		
		KstarProductWorkFlow kf = new KstarProductWorkFlow();
		kf.setProcessID(pi.getId());
		kf.setProcessName("报价商务工程评审流程");
		kf.setCreateBy(user.getEmployee().getId());
		kf.setProductId(id);
		productProcess.save(kf,user);
	}
	
	
	@Override
	public void startDecisionProcess(UserObject user,String id){
		//prjevl id
		String businessId = id;
		
		String module = lovMemberService.getFlowCodeByAppCode(this.QUOT_DECPRC);
		
		KstarPrjEvl prjevl = this.getKstarPrjEvl(id);
		
		KstarQuot quot = this.getKstarQuot(prjevl.getQuotCode());
		
		String SalesCenter = quot.getMarketDept();
		String Quote_TotalAmt = quot.getAmount();
		
		Map<String,String> varmap = new HashMap<>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		String d_str = sdf.format(d);
		
		varmap.put("title", "报价决策工程评审流程 - "+user.getEmployee().getName()+" - "+d_str);
		varmap.put("app", "quot_decprc");
		
		varmap.put("SalesCenter", SalesCenter);
		varmap.put("Quote_TotalAmt", Quote_TotalAmt);
		
		//ProcessInstance pi = processService.start(module, businessId, new Participant(user.getEmployee().getId(),user.getEmployee().getName(),"EMPLOYEE"),varmap);
		ProcessInstance pi = processService.start(module, businessId, user, varmap);
		
		
		prjevl.setEvlRs(pi.getId());
		this.updatePrjEvl(prjevl);
		
		
		KstarProductWorkFlow kf = new KstarProductWorkFlow();
		kf.setProcessID(pi.getId());
		kf.setProcessName("报价决策工程评审流程");
		kf.setCreateBy(user.getEmployee().getId());
		kf.setProductId(id);
		productProcess.save(kf,user);
	}
	
	
	@Override
	public void startPriceProcess(UserObject user,String id){
		//prjevl id
		String businessId = id;
		
		String module = lovMemberService.getFlowCodeByAppCode(this.QUOT_PRCPRC);
		
		KstarPrjEvl prjevl = this.getKstarPrjEvl(id);
		
		KstarQuot quot = this.getKstarQuot(prjevl.getQuotCode());
		
		String SalesCenter = quot.getMarketDept();
		String Quote_TotalAmt = quot.getAmount();
		
		Map<String,String> varmap = new HashMap<>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		String d_str = sdf.format(d);
		
		varmap.put("title", "报价价格工程评审流程 - "+user.getEmployee().getName()+" - "+d_str);
		varmap.put("app", "quot_prcprc");
		
		varmap.put("SalesCenter", SalesCenter);
		varmap.put("Quote_TotalAmt", Quote_TotalAmt);
		
		//ProcessInstance pi = processService.start(module, businessId, new Participant(user.getEmployee().getId(),user.getEmployee().getName(),"EMPLOYEE"),varmap);
		ProcessInstance pi = processService.start(module, businessId, user, varmap);
		
		
		prjevl.setEvlRs(pi.getId());
		this.updatePrjEvl(prjevl);
		
		
		KstarProductWorkFlow kf = new KstarProductWorkFlow();
		kf.setProcessID(pi.getId());
		kf.setProcessName("报价价格工程评审流程");
		kf.setCreateBy(user.getEmployee().getId());
		kf.setProductId(id);
		productProcess.save(kf,user);
	}
	
	
	@Override
	public void endQuotPrjAdtProcess(String processId){
		
		List<KstarProductWorkFlow> kw = productProcess.getList(processId);
		List<String> idsA = new ArrayList<String>(kw.size());
		for(KstarProductWorkFlow kf:kw){
			idsA.add(kf.getProductId());
		}
		
		for(String id:idsA){
			
			if("报价售前工程评审流程".equalsIgnoreCase(kw.get(0).getProcessName())){
				KstarPrjEvl prjevl = getKstarPrjEvl(id);
				prjevl.setEvlSt("S02");
			}
			
			if("报价售后工程评审流程".equalsIgnoreCase(kw.get(0).getProcessName())){
				KstarPrjEvl prjevl = getKstarPrjEvl(id);
				prjevl.setEvlSt("S02");
			}
			
			if("报价商务工程评审流程".equalsIgnoreCase(kw.get(0).getProcessName())){
				KstarPrjEvl prjevl = getKstarPrjEvl(id);
				prjevl.setEvlSt("S02");
			}
			
			if("报价决策工程评审流程".equalsIgnoreCase(kw.get(0).getProcessName())){
				KstarPrjEvl prjevl = getKstarPrjEvl(id);
				prjevl.setEvlSt("S02");
			}
			
			if("报价价格工程评审流程".equalsIgnoreCase(kw.get(0).getProcessName())){
				KstarPrjEvl prjevl = getKstarPrjEvl(id);
				prjevl.setEvlSt("S02");
			}
			
		}

		
	}
	
	

	
	@Override
	public void startBiddcAdtProcess(UserObject user,String qid){
		
		String businessId = qid;
		
		String module = lovMemberService.getFlowCodeByAppCode(this.QUOT_BIDADT);
		
		KstarQuot quot = getKstarQuot(qid);
		
		String SalesCenter = quot.getMarketDept();
		String Quote_TotalAmt = quot.getAmount();
		
		
		Map<String,String> varmap = new HashMap<>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		String d_str = sdf.format(d);
		
		varmap.put("title", "报价标书评审流程 - "+user.getEmployee().getName()+" - "+d_str);
		varmap.put("app", "quot_bidadt");
		
		varmap.put("SalesCenter", SalesCenter);
		varmap.put("Quote_TotalAmt", Quote_TotalAmt);
		
		String sql = "select * from sys_t_lov_member a where 1=1 and a.group_Code = 'ORG' and a.opt_Txt3 in('C','B') "
        		+ "and a.leaf_Flag='N' start with a.row_id = ? connect by prior a.parent_Id = a.row_id";
        String orgType = "C";
        List<Map<String, Object>> positions = baseDao.findBySql4Map(sql,new Object[]{user.getOrg().getId()});
        if(positions != null && !positions.isEmpty()){
        	orgType = positions.get(0).get("OPT_TXT3").toString();
        }
        varmap.put("orgType", orgType);
		
		//ProcessInstance pi = processService.start(module, businessId, new Participant(user.getEmployee().getId(),user.getEmployee().getName(),"EMPLOYEE"),varmap);
		ProcessInstance pi = processService.start(module, businessId, user, varmap);
		

		quot.setBidAuditStatus("B01");
		quot.setTchAdtstatus("S01");
		this.saveQuot(quot, user);
		
		
		KstarProductWorkFlow kf = new KstarProductWorkFlow();
		kf.setProcessID(pi.getId());
		kf.setProcessName("报价标书评审流程 ");
		kf.setCreateBy(user.getEmployee().getId());
		kf.setProductId(qid);
		productProcess.save(kf,user);
		
		
		
		KstarBiddcevl biddc = new KstarBiddcevl();
		biddc.setQuotCode(qid);
		biddc.setCType("0003"); 
		biddc.setCurPrsr(user.getEmployee().getId());
		biddc.setCurStts("B01");
		biddc.setSnPnt(pi.getId());
		baseDao.save(biddc);

	}
	
	
	@Override
	public void startsubmitProcess(UserObject user,String qid){
		
		String businessId = qid;
		
		String module = lovMemberService.getFlowCodeByAppCode(this.QUOT_SBMROC);
		
		KstarQuot quot = getKstarQuot(qid);
		
		String SalesCenter = quot.getMarketDept();
		String Quote_TotalAmt = quot.getAmount();
		
		Map<String,String> varmap = new HashMap<>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		String d_str = sdf.format(d);
		
		varmap.put("title", "报价工程评审提交流程 - "+user.getEmployee().getName()+" - "+d_str);
		varmap.put("app", "quot_sbmroc");
		
		varmap.put("SalesCenter", SalesCenter);
		varmap.put("Quote_TotalAmt", Quote_TotalAmt);
		
		//ProcessInstance pi = processService.start(module, businessId, new Participant(user.getEmployee().getId(),user.getEmployee().getName(),"EMPLOYEE"),varmap);
		ProcessInstance pi = processService.start(module, businessId, user, varmap);

		
		KstarProductWorkFlow kf = new KstarProductWorkFlow();
		kf.setProcessID(pi.getId());
		kf.setProcessName("报价工程评审提交流程");
		kf.setCreateBy(user.getEmployee().getId());
		kf.setProductId(qid);
		productProcess.save(kf,user);
		
		quot.setProReviewStatus("S01");
		quot.setIfsubmitted("Y");
		this.saveQuot(quot, user);
		
	}
	
	
	
	@Override
	public void startquotpriceProcess(UserObject user,String qid){
		
		String businessId = qid;
		
		String module = lovMemberService.getFlowCodeByAppCode(this.QUOT_PRCPRC);
		
		KstarQuot quot = getKstarQuot(qid);
		
		String SalesCenter = quot.getMarketDept();
		String Quote_TotalAmt = quot.getAmount();
		
		
		Map<String,String> varmap = new HashMap<>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		String d_str = sdf.format(d);
		
		varmap.put("title", "报价价格工程评审流程 - "+user.getEmployee().getName()+" - "+d_str);
		varmap.put("app", "quot_prcprc");
		
		varmap.put("SalesCenter", SalesCenter);
		varmap.put("Quote_TotalAmt", Quote_TotalAmt);
		
		//ProcessInstance pi = processService.start(module, businessId, new Participant(user.getEmployee().getId(),user.getEmployee().getName(),"EMPLOYEE"),varmap);
		ProcessInstance pi = processService.start(module, businessId, user, varmap);
		
		KstarProductWorkFlow kf = new KstarProductWorkFlow();
		kf.setProcessID(pi.getId());
		kf.setProcessName("报价价格工程评审流程");
		kf.setCreateBy(user.getEmployee().getId());
		kf.setProductId(qid);
		productProcess.save(kf,user);

	}
	
	
	//技术评审流程
	@Override
	public void startquottchProcess(UserObject user,String qid){
		
		String businessId = qid;
		
		String module = lovMemberService.getFlowCodeByAppCode(this.QUOT_PRESALE);
		
		KstarQuot quot = getKstarQuot(qid);
		
		String SalesCenter = quot.getMarketDept();
		String Quote_TotalAmt = quot.getAmount();
		
		
		Map<String,String> varmap = new HashMap<>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		String d_str = sdf.format(d);
		
		varmap.put("title", "报价技术评审流程 - "+user.getEmployee().getName()+" - "+d_str);
		varmap.put("app", "quot_tchproc");
		
		varmap.put("SalesCenter", SalesCenter);
		varmap.put("Quote_TotalAmt", Quote_TotalAmt);
		
		//ProcessInstance pi = processService.start(module, businessId, new Participant(user.getEmployee().getId(),user.getEmployee().getName(),"EMPLOYEE"),varmap);
		ProcessInstance pi = processService.start(module, businessId, user, varmap);
		
		KstarProductWorkFlow kf = new KstarProductWorkFlow();
		kf.setProcessID(pi.getId());
		kf.setProcessName("报价技术评审流程");
		kf.setCreateBy(user.getEmployee().getId());
		kf.setProductId(qid);
		productProcess.save(kf,user);

	}
	
	
	
	//备料申请流程
	@Override
	public void startquotmrProcess(UserObject user,String qid){
		
		String businessId = qid;
		
		String module = lovMemberService.getFlowCodeByAppCode(this.QUOT_MRPROC);
		
		KstarQuot quot = getKstarQuot(qid);
		
		String SalesCenter = quot.getMarketDept();
		String Quote_TotalAmt = quot.getAmount();
		
		
		Map<String,String> varmap = new HashMap<>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		String d_str = sdf.format(d);
		
		varmap.put("title", "报价备料申请流程 - "+user.getEmployee().getName()+" - "+d_str);
		varmap.put("app", "quot_mrproc");
		
		varmap.put("SalesCenter", SalesCenter);
		varmap.put("Quote_TotalAmt", Quote_TotalAmt);
		
		//ProcessInstance pi = processService.start(module, businessId, new Participant(user.getEmployee().getId(),user.getEmployee().getName(),"EMPLOYEE"),varmap);
		ProcessInstance pi = processService.start(module, businessId, user, varmap);
		
		KstarProductWorkFlow kf = new KstarProductWorkFlow();
		kf.setProcessID(pi.getId());
		kf.setProcessName("报价备料申请流程");
		kf.setCreateBy(user.getEmployee().getId());
		kf.setProductId(qid);
		productProcess.save(kf,user);

	}
	
	
	
	//投标反馈流程
	@Override
	public void startquotbrProcess(UserObject user,String qid){
		
		String businessId = qid;
		
		String module = lovMemberService.getFlowCodeByAppCode(this.QUOT_BRPROC);
		
		KstarQuot quot = getKstarQuot(qid);
		
		String SalesCenter = quot.getMarketDept();
		String Quote_TotalAmt = quot.getAmount();
		
		
		Map<String,String> varmap = new HashMap<>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		String d_str = sdf.format(d);
		
		varmap.put("title", "报价投标反馈流程 - "+user.getEmployee().getName()+" - "+d_str);
		varmap.put("app", "quot_brproc");
		
		varmap.put("SalesCenter", SalesCenter);
		varmap.put("Quote_TotalAmt", Quote_TotalAmt);
		
		//ProcessInstance pi = processService.start(module, businessId, new Participant(user.getEmployee().getId(),user.getEmployee().getName(),"EMPLOYEE"),varmap);
		ProcessInstance pi = processService.start(module, businessId, user, varmap);
		
		KstarProductWorkFlow kf = new KstarProductWorkFlow();
		kf.setProcessID(pi.getId());
		kf.setProcessName("报价投标反馈流程");
		kf.setCreateBy(user.getEmployee().getId());
		kf.setProductId(qid);
		productProcess.save(kf,user);

	}
	
	
	//特价审批流程
	@Override
	public void startSpProcess(UserObject user,String qid){

		//prjevl id
		String businessId = qid;
		
		String module = lovMemberService.getFlowCodeByAppCode(this.QUOT_SPPROC);
		
		KstarQuot quot = getKstarQuot(qid);
		
		String SalesCenter = quot.getMarketDept();
		String Quote_TotalAmt = quot.getAmount();
		
		Map<String,String> varmap = new HashMap<>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		String d_str = sdf.format(d);


//		//更新特价审批标志
//
//		List<KstarPrjLst> KstarPrjLsts = this.getKstarPrjLsts(qid);
//
//		if(KstarPrjLsts!=null){
//			for(KstarPrjLst prjLst : KstarPrjLsts) {
//				this.updateprjLstSprvmrkstart(user, prjLst);
//			}
//		}

		String sql = "select * from sys_t_lov_member a where 1=1 and a.group_Code = 'ORG' and a.opt_Txt3 in('C','B') "
        		+ "and a.leaf_Flag='N' start with a.row_id = ? connect by prior a.parent_Id = a.row_id";
        String orgType = "C";
        List<Map<String, Object>> positions = baseDao.findBySql4Map(sql,new Object[]{user.getOrg().getId()});
        if(positions != null && !positions.isEmpty()){
        	orgType = positions.get(0).get("OPT_TXT3").toString();
        }
        varmap.put("orgType", orgType);
		
		varmap.put("title", "特价审批流程 - "+user.getEmployee().getName()+" - "+d_str);
		varmap.put("app", "quot_spproc");
		
		varmap.put("SalesCenter", SalesCenter);
		varmap.put("Quote_TotalAmt", Quote_TotalAmt);
		
		
		//
		String approvedType = this.checkApprovedType(qid);
		String currentlevel = this.checkcurrentLevel(qid);
		String highlevel = this.checkHighLevel(qid);
		
		varmap.put("V_Approve_Type", approvedType);
		varmap.put("V_CUR_LEVEL", currentlevel);
		varmap.put("V_HIGH_LEVEL", highlevel);
		varmap.put("orgType", user.getOrg().getOptTxt3());

		//ProcessInstance pi = processService.start(module, businessId, new Participant(user.getEmployee().getId(),user.getEmployee().getName(),"EMPLOYEE"),varmap);
		ProcessInstance pi = processService.start(module, businessId, user, varmap);
		
		
		quot.setSpAuditStatus("P01");
		this.saveQuot(quot, user);
		
		
		KstarBiddcevl biddc = new KstarBiddcevl();
		biddc.setQuotCode(qid);
		biddc.setCType("0003"); 
		biddc.setCurPrsr(user.getEmployee().getId());
		biddc.setCurStts("P01");
		biddc.setSnPnt(pi.getId());
		baseDao.save(biddc);
		
		
		KstarProductWorkFlow kf = new KstarProductWorkFlow();
		kf.setProcessID(pi.getId());
		kf.setProcessName("特价审批流程");
		kf.setCreateBy(user.getEmployee().getId());
		kf.setProductId(qid);
		productProcess.save(kf,user);

	}
	
	
	
	
	@Override
	public List<KstarProductWorkFlow> getKstarProductWorkFlowList(String quotId,String processName) throws AnneException {
		
		List<KstarProductWorkFlow> wf;
		
		List<Object> args = new ArrayList<Object>();
		args.add(processName);
		args.add(quotId);
		
		wf = baseDao.findEntity("from KstarProductWorkFlow where processName = ? and productId = ? ", args.toArray());
		return wf;
	}
	
	
	
	
	//
	@Override
	public List<HistoryActivityInstance> getContrFinReviewHisLst(String module, String quotId) throws AnneException {
		List<HistoryActivityInstance> list = getHistoryByBusinessKey(module, quotId);
		return list;
	}
	
	@Override
	public List<HistoryActivityInstance> getBiddcevlReviewHisLst(String processid, String quotId) throws AnneException {
		
		ProcessInstance pi = iprocessService.get(processid);
		List<HistoryActivityInstance> list = null;
		
		if(pi!=null){
			list = historyService.findHistoryActivityInstance(pi.getId());
		}
		
		return list;
	}
	
	
	/**
	 * 通过业务主键获取流程历史
	 * @param module
	 * @param businessKey
	 * @return
	 */
	@Override
	public List<HistoryActivityInstance> getHistoryByBusinessKey(String module ,String businessKey){
		ProcessInstance pi = iprocessService.getByBusinessKey(module, businessKey);
		List<HistoryActivityInstance> list;
		
		if(pi!=null){
			list = historyService.findHistoryActivityInstance(pi.getId());
			return list;
		}else{
			return null;
		}
		
		
		
	}
	
	/**
	 * 通过 KstarProductWorkFlow流程ID获取工作流流程历史
	 * @param businessKey
	 * @return
	 */
	@Override
	public List<HistoryProcessInstance> getHistoryProInstance(String processid){
		String hql = " select * from XFLOW_HISTORY_PROCESS_INSTANCE where 1=1 ";
			hql += " and ROW_ID = ? ";
			hql += " and status <> 'close' ";
		List<Object[]> objects = baseDao.findBySql(hql,new Object[]{processid});
		List<HistoryProcessInstance> his = new ArrayList<HistoryProcessInstance>();
		if(objects != null && objects.size() > 0){
			for(Object[] obj: objects){
				HistoryProcessInstance hi = new HistoryProcessInstance();
				hi.setId((String)obj[0]);
				hi.setBusinessKey((String)obj[1]);
				hi.setCreatorId((String)obj[2]);
				hi.setCreatorName((String)obj[3]);
				hi.setCreatorType((String)obj[4]);
				hi.setEndTime((Date)obj[5]);
				hi.setModule((String)obj[6]);
				hi.setName((String)obj[7]);
				hi.setParentId((String)obj[8]);
				hi.setProcessDefinitionId((String)obj[9]);
				hi.setProcessDefinitionName((String)obj[10]);
				hi.setStartTime((Date)obj[11]);
				hi.setStatus((String)obj[12]);
				his.add(hi);
			}
		}
		return his;
	}
	
	
	@Override
	public KstarPrjLst getKstarPrjLst(String qid, String lvId) throws AnneException {
		
		List<Object> args = new ArrayList<Object>();
		args.add(qid);
		args.add(lvId);
		String prjLstId = baseDao.findUniqueEntity("select id from KstarPrjLst where quotCode = ? and lvId = ? ",args.toArray());
		
		KstarPrjLst prjLst = baseDao.get(KstarPrjLst.class, prjLstId);
		
		return prjLst;
	}
	
	
	
	@Override
	public List<KstarPrjLst> getKstarPrjLsts(String qid) throws AnneException {
		
		List<Object> args = new ArrayList<Object>();
		args.add(qid);

		List<KstarPrjLst> prjLsts = baseDao.findEntity("select p from KstarPrjLst p where p.quotCode = ? ",args.toArray());
			
		return prjLsts;
	}
	
	
	
	@Override
	public String checkKstarPrjLstwithprdSprc(String qid) throws AnneException {
		
		String result = "N";
		
		List<Object> args = new ArrayList<Object>();
		args.add(qid);

		Long count = baseDao.findUniqueEntity("select count(*) from KstarPrjLst where prdSprc is null and quotCode = ? ",args.toArray());
		
		if(count>0){
			result = "Y";
		}
		
		return result;
	}
	
	
	
	@Override
	public void updatePrjLst(KstarPrjLst prjLst) throws AnneException {
		
		String prjLstId = prjLst.getId();
		KstarPrjLst oldPrjLst = baseDao.get(KstarPrjLst.class, prjLstId);
		
		if(oldPrjLst == null){
			throw new AnneException( " savePrjLst : 没有找到要更新的数据");
		}
		
		if((prjLst.getAmt()!=null)&&(prjLst.getPrdPrc()!=null)){
			Double tmpttlUnt = prjLst.getAmt() * prjLst.getPrdPrc();
			prjLst.setTtlUnt(tmpttlUnt);
		}
		
		BeanUtils.copyPropertiesIgnoreNull(prjLst, oldPrjLst);
		//oldPrjLst.setId(prjLstId);
		baseDao.update(oldPrjLst);
	}
	
	
	@Override
	public String getLineNum(String contrId) throws AnneException{
		String hql = "select line.lineNum from KstarPrjLst line where line.lineNum is not null and  line.quotCode = ? ";
		String lineNo = "";
		try {
			List<String> lineNoList = baseDao.findEntity(hql,
					new Object[] { contrId });
			if (lineNoList == null || lineNoList.size() <= 0) {
				lineNo = "1";
			} else {
				String maxLineNo = Collections.max(lineNoList);
				int n = Integer.parseInt(maxLineNo);
				lineNo += n + 1;

			}
		} catch (Exception e) {
			throw new AnneException(IOrderService.class.getName()
					+ " getLineNo : 系统出现异常，请联系管理员");
		}
		return lineNo;
	}
	
	
	@Override
	public void saveLinesb(List<KstarPrjLst> lines, String quotID, String typ, UserObject userObject) {
		for (KstarPrjLst prjLst : lines) {

			prjLst.setQuotCode(quotID);
			prjLst.setCType(typ);

			if (prjLst.getPrdPrc() == null) {
				prjLst.setPrdcd("0");
			}

			if (prjLst.getId() == null || prjLst.getId().equalsIgnoreCase("")) {

				String cType = "0003";

				if (StringUtil.isEmpty(prjLst.getQuotCode())) {
					throw new AnneException("报价单编号不能为空");
				}

				prjLst.setLineNum(this.getLineNum(prjLst.getQuotCode()));

				this.savePrjLst(prjLst, cType);

			} else {
				this.updatePrjLst(prjLst);
			}
			this.updateprjLstSprvmrk(userObject, prjLst);
		}

		String totalAmount = "";

		totalAmount = this.getTotalamount(quotID);

		KstarQuot quot = this.getKstarQuot(quotID);
		quot.setAmount(totalAmount);
		this.updateQuot(quot);
	}
	
	
}
