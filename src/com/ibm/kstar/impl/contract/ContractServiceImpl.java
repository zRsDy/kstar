package com.ibm.kstar.impl.contract;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.action.ProcessForm;
import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.common.doc.IKstarAttachmentService;
import com.ibm.kstar.api.contract.IContrChangeService;
import com.ibm.kstar.api.contract.IContrPayService;
import com.ibm.kstar.api.contract.IContractEliminateService;
import com.ibm.kstar.api.contract.IContractService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.order.IOrderService;
import com.ibm.kstar.api.org.IOrgTeamService;
import com.ibm.kstar.api.product.IProLovService;
import com.ibm.kstar.api.quot.IQuotService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.ICorePermissionService;
import com.ibm.kstar.api.system.permission.IEmployeeService;
import com.ibm.kstar.api.system.permission.IOrgService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.api.system.permission.entity.Position;
import com.ibm.kstar.api.team.ITeamService;
import com.ibm.kstar.entity.bizopp.BusinessOpportunity;
import com.ibm.kstar.entity.common.doc.KstarAttachment;
import com.ibm.kstar.entity.contract.*;
import com.ibm.kstar.entity.custom.CustomErpInfo;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.order.OrderHeader;
import com.ibm.kstar.entity.order.OrderLines;
import com.ibm.kstar.entity.order.vo.OrderQuantityVo;
import com.ibm.kstar.entity.product.ProductPriceHead;
import com.ibm.kstar.entity.quot.*;
import com.ibm.kstar.entity.team.Team;
import com.ibm.kstar.permission.utils.PermissionUtil;
import com.ibm.kstar.service.IDemoService;
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
import org.xsnake.web.utils.MathUtils;
import org.xsnake.web.utils.StringUtil;
import org.xsnake.xflow.api.IHistoryService;
import org.xsnake.xflow.api.IProcessService;
import org.xsnake.xflow.api.model.HistoryActivityInstance;
import org.xsnake.xflow.api.model.HistoryProcessInstance;
import org.xsnake.xflow.api.model.ProcessInstance;
import org.xsnake.xflow.api.workflow.IXflowProcessServiceWrapper;

import java.math.BigDecimal;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author Joe
 *
 */
/**
 * @author Joe
 *
 */
/**
 * @author Joe
 *
 */
@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class ContractServiceImpl implements IContractService {

	@Autowired
	BaseDao baseDao;

	@Autowired
	IProcessService processService;
	@Autowired
	IHistoryService historyService;
	@Autowired
	private ILovMemberService lovMemberService;
	@Autowired
	private IKstarAttachmentService attachmentService;
	@Autowired
	IProLovService proLovService;
	@Autowired
	private IContrChangeService  changeService ;
	@Autowired
	ITeamService teamService;
	@Autowired
	protected IOrgTeamService orgTeamService;
	@Autowired
	IXflowProcessServiceWrapper xflowProcessServiceWrapper;
	@Autowired
	IOrderService orderService;
	@Autowired
	private ICustomInfoService customerService;
	@Autowired
	private IEmployeeService employeeService;
	@Autowired
	private ICorePermissionService corePermissionService;

	@Autowired
	private IContractEliminateService contractEliminateService;	
	
	@Autowired
	IOrgService orgService;
	
	@Autowired
	IContrPayService contrPayService;

	@Autowired
	IContrChangeService contrChangeService;


	@Override
	public IPage query(PageCondition condition) {

		UserObject user = (UserObject)condition.getCondition("userObject");
//		IConstants.CONTR_TYPE_X_ORG_STR;
//		8	CONTRACTDOMESTICTYPE	国内合同类型
//		9	CONTRACTINTERTYPE	国际合同类型
//		if(user.getOrg().getNamePath().contains(IConstants.CONTR_TYPE_X_ORG_STR)){
//			strContrTp=IConstants.CONTR_STAND_02; //"CONTRACTINTERTYPE";
//		}else{
//			strContrTp=IConstants.CONTR_STAND_01; //"CONTRACTDOMESTICTYPE";
//		}

//		StringBuffer hql = new StringBuffer(" from Contract contr where 1=1 and contr.isValid ='1'  and contr.contrType in (" +tpIds+")");
//		StringBuffer hql = new StringBuffer(" from Contract contr where 1=1 and contr.contrType in (" +tpIds+")");


		FilterObject fo = condition.getFilterObject(Contract.class);
		HqlObject ho = HqlUtil.getHqlObject(fo);
		String hql_search = ho.getHql();
		String sel_hql = "";
		if(hql_search.indexOf("order by")>0){
			sel_hql = hql_search.substring(0, hql_search.indexOf("order by")-1);
		}

		StringBuffer hql = new StringBuffer();
		hql.append(sel_hql);

		relevanceHql(condition, hql, ho,user,IConstants.CONTR_STAND);

		return baseDao.search(hql.toString(),ho.getArgs(),condition.getRows(), condition.getPage());
	}

	@Override
	public void save(Contract contract,UserObject user) throws AnneException {
		if(StringUtil.isEmpty(contract.getContrNo())){
			throw new AnneException("合同单编号不能为空");
		}
		if(StringUtil.isEmpty(contract.getContrVer())){
			throw new AnneException("合同单版本不能为空");
		}

		String pid;
		String eid;
		String oid;

		if(StringUtil.isNotEmpty(user.getEid())){
			pid = user.getPid();
			eid = user.getEid();
			oid = user.getOid();
		}else{
			pid = user.getPosition().getId();
			eid = user.getEmployee().getId();
			oid = user.getOrg().getId();
		}

		baseDao.save(contract);
		//权限控制
		//当前登录人权限
		teamService.addPosition(pid,eid,IConstants.CONTR_STAND,contract.getId());
		orgTeamService.configPrimaryOrg(contract.getId(), IConstants.CONTR_STAND, oid);
		//下单商务专员加入到团队成员
		teamService.addPosition(contract.getOrderPosId(),contract.getOrderer(),IConstants.CONTR_STAND,contract.getId());

	}


	@Override
	public Contract get(String id) throws AnneException {
		if(id==null){
			return null;
		}
		return baseDao.get(Contract.class, id);
	}


	public void saveLines(String listStr, String contrId,String typ) throws AnneException {
		List<KstarPrjLst> lines = JSON.parseArray(listStr, KstarPrjLst.class);

		for(KstarPrjLst prjLst: lines){
			prjLst.setQuotCode(contrId);
			prjLst.setCType(typ);

			if(prjLst.getId() == null || prjLst.getId().equalsIgnoreCase("")){
				savePrjLstDtl(prjLst, typ,contrId);
			}else{
				this.updatePrjLst(prjLst,typ,contrId);
			}

		}

		Double totalAmount = this.getTotalamount(contrId);
		Contract contract = this.get(contrId);
		contract.setTotalAmt(totalAmount);
		this.update(contract,null);

		// 更新回款规则
		contrPayService.updateContrPayAmt(contrId, totalAmount);

	}

	@Override
	public void share0Price(String contrId){
		List<KstarPrjLst> list = baseDao.findEntity("from KstarPrjLst where quotCode = ? ",contrId);

		int count = 0; // 单价为0的产品行数
		Double quantity = 0d;// 单价为0的产品总数
		for (KstarPrjLst kstarPrjLst : list) {
			if (kstarPrjLst.getPrdPrc() == null) {
				continue;
			}
			if (kstarPrjLst.getPrdPrc().compareTo(0d) == 0) {
				count = count+1;
				quantity = MathUtils.add(quantity, kstarPrjLst.getAmt());
			}
		}
		Double sum = new Double(0); // 平摊金额
		if (count > 0) {
			// 计算需要平摊的金额
			for (KstarPrjLst kstarPrjLst : list) {
				if (kstarPrjLst.getPrdPrc() == null) {
					continue;
				}
				if (kstarPrjLst.getPrdPrc().compareTo(0d) != 0) {
					sum = MathUtils.add(MathUtils.mul(kstarPrjLst.getTtlUnt(), 0.004d), sum.doubleValue());
					kstarPrjLst.setPrdPrc(MathUtils.mul(kstarPrjLst.getPrdPrc(), 0.996d));
					kstarPrjLst.setTtlUnt(MathUtils.mul(kstarPrjLst.getTtlUnt(), 0.996d));
					baseDao.update(kstarPrjLst);
				}
			}
			// 平摊后的单价
			Double price = MathUtils.div6(sum, quantity);
			for (KstarPrjLst kstarPrjLst : list) {
				if (kstarPrjLst.getPrdPrc() == null) {
					continue;
				}
				if (kstarPrjLst.getPrdPrc().compareTo(0d) == 0) {
					kstarPrjLst.setPrdPrc(price);
					kstarPrjLst.setTtlUnt(MathUtils.mul(price, kstarPrjLst.getAmt()));
					baseDao.update(kstarPrjLst);
				}
			}
		}
	}


	private void savePrjLstDtl(KstarPrjLst prjLst, String typ, String contrId){

		String cType = typ;
		// PI 合同 的报价，数量必填
		if(cType.equalsIgnoreCase(IConstants.CONTR_PI) && prjLst.getPrdPrc() == null ){
			throw new AnneException("报价不能为空");
		}
		if(cType.equalsIgnoreCase(IConstants.CONTR_PI) && prjLst.getAmt() == null ){
			throw new AnneException("数量不能为空");
		}

		Contract contract = this.get(contrId);
		prjLst.setBuzCode(contract.getContrNo());
		prjLst.setVeriedNum(0.0);
		prjLst.setNotVeriNum(prjLst.getAmt());
		prjLst.setLineNum(this.getLineNum(contrId));
		this.savePrjLst(prjLst,cType);
	}

	@Autowired
	IDemoService demoService;
	@Override
	public void update(Contract contract,ProcessForm form) throws AnneException {

		Contract old = baseDao.get(Contract.class, contract.getId());
		if(old == null){
			throw new AnneException(IContractService.class.getName() + " saveQuot : 没有找到要更新的数据");
		}

		if(!contract.getId().equals(old.getId())){
			throw new AnneException("合同单ID不能被修改");
		}

		BeanUtils.copyPropertiesIgnoreNull(contract, old);
		old.setIsArchive(contract.getIsArchive());
		//old.setCreateTime(new Time(0));
		baseDao.update(old);
		Long count = baseDao.findUniqueEntity("select count(*) from Contract where C_ID = ? ",contract.getId());
		if(count > 1){
			throw new AnneException("合同单ID已经存在!");
		}

		List<Team> teams = baseDao.findEntity("select t from Team t where t.businessType = ? and t.businessId = ? and t.positionId = ? and t.masterEmployeeId = ? " ,
				new Object[]{IConstants.CONTR_STAND,contract.getId(),contract.getOrderPosId(),contract.getOrderer()});
		if(teams.size()<=0){
			//下单商务专员加入到团队成员
			teamService.addPosition(contract.getOrderPosId(),contract.getOrderer(),IConstants.CONTR_STAND,contract.getId());
		}

		demoService.todoProcess(form);
	}

	@Override
	public void delete(String id) throws AnneException {
		baseDao.executeHQL(" delete Contract contr where contr.id = ? ",new Object[]{id});

	}

	/**
	 * 合同导出数据
	 * @param condition
	 * @param user
	 * @param ids
	 * @param typ
	 * @return
	 */
	public List<Contract> getSelectedContrList(PageCondition condition,UserObject user, String[] ids,String typ) {

		FilterObject fo = condition.getFilterObject(Contract.class);
		HqlObject ho = HqlUtil.getHqlObject(fo);
		String hql_search = ho.getHql();

		String sel_hql = "";
		if(hql_search.indexOf("order by")>0){
			sel_hql = hql_search.substring(0, hql_search.indexOf("order by")-1);
		}

		StringBuffer hql = new StringBuffer(sel_hql);

		String idsStr = "";
		for(String id : ids){
			idsStr += "'" + id + "',";
		}
		idsStr= idsStr.substring(0, idsStr.length()-1);
		if (!StringUtil.isNullOrEmpty(idsStr) && !"''".equals(idsStr)) {
			hql.append(" AND contract.id in ("+ idsStr +")");
		}

		relevanceHql(condition, hql, ho,user,typ);

		return baseDao.findEntity(hql.toString(),ho.getArgs());

	}



	/**
	 * 模糊查询
	 * 查询和导出，管理明细查询
	 * @param condition
	 * @param hql
	 * @param ho
	 */
	private void relevanceHql(PageCondition condition,StringBuffer hql,HqlObject ho,UserObject user,String typ){

		String strContrTp01 = IConstants.CONTR_STAND_01;
		String strContrTp02 = IConstants.CONTR_STAND_02;

		LovMember typeLov01 = lovMemberService.getLovMemberByCode("CONTRACTTYPE", strContrTp01);
		LovMember typeLov02 = lovMemberService.getLovMemberByCode("CONTRACTTYPE", strContrTp02);
		Condition lovCon =  new Condition();
		lovCon.getFilterObject().addOrCondition("parentId", "=", typeLov01.getId());
		lovCon.getFilterObject().addOrCondition("parentId", "=", typeLov02.getId());

		List<LovMember> contrTpLst = lovMemberService.getList(lovCon);

		String tpIds= "";
		for(LovMember lv: contrTpLst){
			tpIds+="'"+lv.getId()+"',";
		}
		tpIds= tpIds.substring(0, tpIds.length()-1);

		hql.append(" and contract.contrType in (" +tpIds+")");

		//模糊查询
		String searchKey = condition.getStringCondition("searchKey");
		if(searchKey != null){
			hql.append(" AND ( (contract.contrNo like '%"+searchKey+"%' ) or ( contract.contrName like '%"+searchKey+"%'))");
		}

		String prdTyp = condition.getStringCondition("prdTyp");
		String materCode = condition.getStringCondition("materCode");
		if(!StringUtil.isNullOrEmpty(prdTyp) || !StringUtil.isNullOrEmpty(materCode)){
			hql.append(" and exists (select 1 from KstarPrjLst lst where lst.quotCode = contract.id ");

			if (!StringUtil.isNullOrEmpty(prdTyp)) {
				hql.append(" and lst.prdTyp = ? ");
				ho.addArgs(prdTyp);
			}
			if (!StringUtil.isNullOrEmpty(materCode)) {
				hql.append(" and lst.materCode = ? ");
				ho.addArgs(materCode);
			}

			hql.append(" ) ");
		}

		String perHql= PermissionUtil.getPermissionHQL(null, "contract.createdById", "contract.createdPosId", "contract.createdOrgId", "contract.id", user, typ);
		hql.append(" AND " +perHql);
		hql.append(" order by contract.createTime desc , contract.contrNo desc, contract.contrVer desc ");

	}

	public List<Contract> getContractList(PageCondition condition) {

		UserObject user = (UserObject)condition.getCondition("userObject");
//		IConstants.CONTR_TYPE_X_ORG_STR;
		String strContrTp = "";
		if(user.getOrg().getNamePath().contains(IConstants.CONTR_TYPE_X_ORG_STR)){
			strContrTp=IConstants.CONTR_STAND_02; //"CONTRACTINTERTYPE";
		}else{
			strContrTp=IConstants.CONTR_STAND_01; //"CONTRACTDOMESTICTYPE";
		}

		LovMember typeLov = lovMemberService.getLovMemberByCode("CONTRACTTYPE", strContrTp);

		Condition lovCon =  new Condition();
		lovCon.getFilterObject().addCondition("parentId", "=", typeLov.getId());
		List<LovMember> contrTpLst = lovMemberService.getList(lovCon);
		String tpIds= "";
		for(LovMember lv: contrTpLst){
			tpIds+="'"+lv.getId()+"',";
		}
		tpIds= tpIds.substring(0, tpIds.length()-1);
//		StringBuffer hql = new StringBuffer(" from Contract contr where 1=1 and contr.isValid ='1' and contr.isChange ='0' and contr.contrType in (" +tpIds+")");
		StringBuffer hql = new StringBuffer(" from Contract contr where 1=1 and contr.contrType in (" +tpIds+")");
		//模糊查询
		String searchKey = condition.getStringCondition("searchKey");
		if(searchKey != null){
//			condition.getFilterObject().addOrCondition("contrNo", "like", "%"+searchKey+"%");
//			condition.getFilterObject().addOrCondition("contrName", "like", "%"+searchKey+"%");
			hql.append(" AND ( (contrNo like '%"+searchKey+"%' ) or ( contrName like '%"+searchKey+"%'))");
		}

		String perHql= PermissionUtil.getPermissionHQL(null, "contr.createdById", "contr.createdPosId", "contr.createdOrgId", "contr.id", user, IConstants.CONTR_STAND);
		hql.append(" AND " +perHql);
		hql.append(" order by createTime desc , contrNo desc, contrVer desc ");

		return baseDao.findEntity(hql.toString());
	}


	@Override
	public List<List<Object>> exportContractsHead(PageCondition condition) throws AnneException{

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<List<Object>> lstOut = new ArrayList<List<Object>>();
		addHead(lstOut);
		List<Contract> headerLst= getContractList(condition);
		int i=1;
		for(Contract con: headerLst){
//			LovMember typeLov = lovMemberService.get(con.getContrType())
			List<Object> lstIn = new ArrayList<Object>();
			lstIn.add(i);
			lstIn.add(con.getContrNo());
			lstIn.add(con.getContrTypeName());
			lstIn.add(con.getCreatedAt());
			lstIn.add(con.getCustName());
			lstIn.add(con.getCreator());
			LovMember orgLov = lovMemberService.get(con.getOrg());//销售部门
			if(orgLov!=null){
				lstIn.add(orgLov.getName());
			}else{
				lstIn.add("");
			}
			LovMember mdLov = lovMemberService.get(con.getMarketDept()); // 营销部门
			if(mdLov != null){
				lstIn.add(mdLov.getName());
			}else{
				lstIn.add("");
			}
			lstIn.add(con.getProjName());
			lstIn.add(con.getContrStatName());
			lstIn.add(con.getTotalAmt());
			lstIn.add(con.getContrVer());
			lstIn.add(con.getIsValid());
			lstIn.add(con.getIsChange());
			lstIn.add(con.getTolRecdAmt());//"总已收金额
			lstIn.add(con.getNotTolRecAmt());// 总未收金额
			Contract frameContr=  get(con.getFrameNo());
			if(frameContr!=null){
				lstIn.add(frameContr.getContrNo());
			}else{
				lstIn.add("");
			}
			lstIn.add(con.getContrName());
			lstIn.add(con.getCustContrNo());
			lstIn.add(con.getIsArchive());
			lstIn.add(con.getAchiveRemark());
			lstIn.add(con.getPostDate());
			lstIn.add(con.getArchiveDate());
			lstIn.add(con.getActSignDate());
			lstIn.add(con.getBussEnityName());
			lstIn.add(con.getErpOrderCustName());
			lstIn.add(con.getOrderNo());
			lstIn.add(con.getOrderedAmt());
			lstIn.add(con.getDeliveredAmt());
			lstIn.add(con.getInvoicedAmt());
			lstIn.add(con.getOrdererName());
			lstIn.add(con.getPayStatDesc());
			lstOut.add(lstIn);
			i++;
		}
		return lstOut;



//		FilterObject filterObject = condition.getFilterObject(Contract.class);
//		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
//		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}


	@Override
	public List<List<Object>> exportSelectedContrLists(PageCondition condition,UserObject user,String[] ids,String typ) throws AnneException{

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<List<Object>> lstOut = new ArrayList<List<Object>>();
		addHead(lstOut);
//		List<Contract> headerLst= getContractList(condition);
		List<Contract> headerLst= getSelectedContrList(condition,user,ids,typ);
		int i=1;
		for(Contract con: headerLst){
//			LovMember typeLov = lovMemberService.get(con.getContrType())
			List<Object> lstIn = new ArrayList<Object>();
			lstIn.add(i);
			lstIn.add(con.getContrNo());
			lstIn.add(con.getContrTypeName());
			lstIn.add(con.getCreatedAt());
			lstIn.add(con.getCustName());
			lstIn.add(con.getCreatorName());
			LovMember orgLov = lovMemberService.get(con.getOrg());//销售部门
			if(orgLov!=null){
				lstIn.add(orgLov.getName());
			}else{
				lstIn.add("");
			}
			LovMember mdLov = lovMemberService.get(con.getMarketDept()); // 营销部门
			if(mdLov != null){
				lstIn.add(mdLov.getName());
			}else{
				lstIn.add("");
			}
			lstIn.add(con.getProjName());
			lstIn.add(con.getContrStatName());
			lstIn.add(con.getTotalAmt());
			lstIn.add(con.getContrVer());
			lstIn.add(con.getIsValidName());
			lstIn.add(con.getIsChangeName());
			lstIn.add(con.getIsPassName());
			lstIn.add(con.getIsHighRiskName());
			lstIn.add(con.getTolRecdAmt());//"总已收金额
			lstIn.add(con.getNotTolRecAmt());// 总未收金额
			Contract frameContr=  get(con.getFrameNo());
			if(frameContr!=null){
				lstIn.add(frameContr.getContrNo());
			}else{
				lstIn.add("");
			}
			lstIn.add(con.getContrName());
			lstIn.add(con.getCustContrNo());
			
			String isArchive=  con.getIsArchive();
			if(StringUtil.isNullOrEmpty(isArchive)||"0".equals(isArchive)){
				lstIn.add("否");
			}else{
				lstIn.add("是");
			}
			lstIn.add(con.getAchiveRemark());
			lstIn.add(con.getPostDate());
			lstIn.add(con.getArchiveDate());
			lstIn.add(con.getSysSignDate());
			lstIn.add(con.getBussEnityName());
			lstIn.add(con.getErpOrderCustName());
			lstIn.add(con.getErpNo());
			lstIn.add(con.getOrderedAmt());
			lstIn.add(con.getDeliveredAmt());
			lstIn.add(con.getInvoicedAmt());
			lstIn.add(con.getOrdererName());
			lstIn.add(con.getPayStatDesc());
			lstOut.add(lstIn);
			i++;
		}
		return lstOut;



//		FilterObject filterObject = condition.getFilterObject(Contract.class);
//		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
//		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}

	private void  addHead(List<List<Object>> lstOut){
		List<Object> lstHead = new ArrayList<Object>();
		lstHead.add("序号");
		lstHead.add("合同编号");
		lstHead.add("合同类型");
		lstHead.add("创建日期");
		lstHead.add("客户名称");
		lstHead.add("销售人员");
		lstHead.add("销售部门");
		lstHead.add("营销部门");
		lstHead.add("项目名称");
		lstHead.add("审核状态");
		lstHead.add("合同总金额");
		lstHead.add("合同版本");
		lstHead.add("有效标识");
		lstHead.add("变更标识");
		lstHead.add("业务是否已通过核销流程");
		lstHead.add("高风险标识");
		lstHead.add("总已收金额");
		lstHead.add("总未收金额");
		lstHead.add("框架协议编号");
		lstHead.add("合同名称");
		lstHead.add("客户合同编号");
		lstHead.add("原件是否归档");
		lstHead.add("归档备注");
		lstHead.add("合同邮寄日期");
		lstHead.add("归档时间");
		lstHead.add("系统签订日期");
		lstHead.add("业务实体");
		lstHead.add("ERP下单客户");
		lstHead.add("ERP订单编号");
		lstHead.add("已下单金额");
		lstHead.add("已发货金额");
		lstHead.add("已开票金额");
		lstHead.add("下单商务专员");
		lstHead.add("收款计划维护状态");
		lstOut.add(lstHead);
	}

	@Override
	public IPage queryAtt(PageCondition condition) {

		String contrId = condition.getStringCondition("contrId");
		StringBuffer hql = new StringBuffer(" from KstarAtt where CType = '0005' and quotCode = ? ");
		List<Object> args = new ArrayList<Object>();
		args.add(contrId);
		return baseDao.search(hql.toString(),args.toArray(),condition.getRows(), condition.getPage());
	}

	@Override
	public IPage queryMem(PageCondition condition) {
		//String quotCode = baseDao.findUniqueEntity("select trim(quotCode) from KstarQuot where rownum =1 and id = ? ",condition.getStringCondition("qid"));

		String contrId = condition.getStringCondition("contrId");
		StringBuffer hql = new StringBuffer(" from KstarMemInfo where CType = '0005' and quotCode = ? ");
		List<Object> args = new ArrayList<Object>();
		args.add(contrId);
		return baseDao.search(hql.toString(),args.toArray(),condition.getRows(), condition.getPage());
	}


	@Override
	public void saveMem(KstarMemInfo mem) throws AnneException {
		if(StringUtil.isEmpty(mem.getQuotCode())){
			throw new AnneException("合同编号不能为空");
		}
		if(StringUtil.isEmpty(mem.getMemName())){
			throw new AnneException("团队成员不能为空");
		}
		mem.setCType("0005");
		baseDao.save(mem);
	}

	@Override
	public void saveAtt(KstarAtt att) throws AnneException {
		if(StringUtil.isEmpty(att.getQuotCode())){
			throw new AnneException("合同编号不能为空");
		}
		if(StringUtil.isEmpty(att.getDocNm())){
			throw new AnneException("文档名称不能为空");
		}
		att.setCType("0005");
		att.setUpdDt(new Date());
		att.setUpdr("w");
		baseDao.save(att);
	}

	@Override
	public KstarMemInfo getKstarMemInfo(String memID) throws AnneException {
		return baseDao.get(KstarMemInfo.class, memID);
	}

	@Override
	public KstarAtt getKstarAtt(String attID) throws AnneException {
		return baseDao.get(KstarAtt.class, attID);
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

//		@SuppressWarnings("deprecation")
//		Connection conn = baseDao.getSessionFactory().openSession().connection();

		Long count = baseDao.findUniqueEntity("select count(*) from KstarAtt where id = ? ",att.getId());
		if(count > 1){
			throw new AnneException("附件ID已经存在!");
		}
	}

	@Override
	public void deleteMem(String memId) throws AnneException {
		baseDao.deleteById(KstarMemInfo.class, memId);
	}

	@Override
	public void deleteAtt(String attId) throws AnneException {
		baseDao.deleteById(KstarAtt.class, attId);
	}


	@Override
	public String getContractNumber() throws AnneException {
//		return baseDao.get(KstarMemInfo.class);
		String connum="";
		@SuppressWarnings("deprecation")

			String sql = "{ ? = call CRM_P_CONTRACT_PUB.gen_contract_num(?)}";
//	        CallableStatement sta = conn.prepareCall(sql);
//	        sta.registerOutParameter(1, OracleTypes.VARCHAR);
//            sta.setInt(2, -1);
//	        sta.execute();
//	        connum = sta.getString(1);
			Object[] result = baseDao.executeProcedure(sql, new BaseDao.ProcedureParam[]{
					new BaseDao.OutProcedureParam(Types.VARCHAR),
					new BaseDao.InProcedureParam(-1),
			});
			connum = (String)result[0];

//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
		return connum;
	}

	@Override
	public List<CustomInfo> getCustomInfoList(Condition condition) throws AnneException {
		FilterObject filterObject = condition.getFilterObject(CustomInfo.class);
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.findEntity(hqlObject.getHql(),hqlObject.getArgs());
	}

	@Override
	public List<BusinessOpportunity> getProjectInfoList(Condition condition) throws AnneException {
		FilterObject filterObject = condition.getFilterObject(BusinessOpportunity.class);
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.findEntity(hqlObject.getHql(),hqlObject.getArgs());
	}

	@Override
	public List<Contract> getFramenoInfoList(Condition condition) throws AnneException {
		FilterObject filterObject = condition.getFilterObject(Contract.class);
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.findEntity(hqlObject.getHql(),hqlObject.getArgs());
	}

	@Override
	public List<Contract> getFramenoInfoListByUser(Condition condition) throws AnneException {
		FilterObject filterObject = condition.getFilterObject(Contract.class);

		UserObject user = (UserObject)condition.getCondition("userObject");

		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		String hql_search = hqlObject.getHql();
		String sel_hql = "";
		if(hql_search.indexOf("order by")>0){
			sel_hql = hql_search.substring(0, hql_search.indexOf("order by")-1);
		}
		StringBuffer hql = new StringBuffer(sel_hql);
		String perHql= PermissionUtil.getPermissionHQL(null, "contract.createdById", "contract.createdPosId", "contract.createdOrgId", "contract.id", user, IConstants.CONTR_STAND);
		hql.append(" AND " +perHql);
		hql.append(" order by contract.createdAt desc , contract.contrNo desc, contract.contrVer desc ");

		return baseDao.findEntity(hql.toString(),hqlObject.getArgs());
	}

	@Override
	public List<LovMember> getOrgInfoList(Condition condition) throws AnneException {
		FilterObject filterObject = condition.getFilterObject(LovMember.class);
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.findEntity(hqlObject.getHql(),hqlObject.getArgs());
	}


	@Override
	public List<ProductPriceHead> getPriceTableInfoList(Condition condition) throws AnneException {
		FilterObject filterObject = condition.getFilterObject(ProductPriceHead.class);
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.findEntity(hqlObject.getHql(),hqlObject.getArgs());
	}


	@Override
	public IPage queryPrjevl(PageCondition condition) {
		//String quotCode = baseDao.findUniqueEntity("select trim(quotCode) from KstarQuot where rownum =1 and id = ? ",condition.getStringCondition("qid"));
		String contrId = condition.getStringCondition("contrId");
		StringBuffer hql = new StringBuffer(" from KstarPrjEvl where quotCode = ? ");
		List<Object> args = new ArrayList<Object>();
		args.add(contrId);
		return baseDao.search(hql.toString(),args.toArray(),condition.getRows(), condition.getPage());

//		FilterObject filterObject = condition.getFilterObject(ContrPay.class);
//		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
//		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());

	}

	@Override
	public void savePrjevl(KstarPrjEvl prjevl, String typ) throws AnneException {
		if(StringUtil.isEmpty(prjevl.getQuotCode())){
			throw new AnneException("报价单编号不能为空");
		}
		if(StringUtil.isEmpty(prjevl.getEvlTyp())){
			throw new AnneException("评审类别不能为空");
		}
//		LovMember lov = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "01"); // 未启动
//		prjevl.setEvlSt(lov.getId());
		prjevl.setSbmDt(new Date());
//		prjevl.setCType("CONTR_STAND");
		prjevl.setCType(typ);
		baseDao.save(prjevl);
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

		BeanUtils.copyPropertiesIgnoreNull(prjevl, oldPrjevl);
		baseDao.update(oldPrjevl);
		Long count = baseDao.findUniqueEntity("select count(*) from KstarPrjEvl where id = ? ",prjevl.getId());
		if(count > 1){
			throw new AnneException("评审ID已经存在!");
		}
	}

	@Override
	public KstarPrjEvl getKstarPrjEvl(String prjevlID) throws AnneException {
		return baseDao.get(KstarPrjEvl.class, prjevlID);
	}


	@Override
	public KstarPrjEvl getKstarPrjevlByProcessId(String contrId, String pi) throws AnneException {
		String hql= "from KstarPrjEvl evl where evl.quotCode=? and evl.evlRs=?";
		return baseDao.findUniqueEntity(hql, new Object[]{contrId,pi});
	}
	@Override
	public void deletePrjevl(String[] prjevlIds) throws AnneException {
		for (String prjevlId : prjevlIds) {
			KstarPrjEvl prjevl = baseDao.get(KstarPrjEvl.class, prjevlId);
			if(prjevl==null ){
				throw new AnneException("未找到要删除的数据！");
			}
			LovMember proN = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "01");
			if(prjevl.getEvlSt().equalsIgnoreCase(proN.getId())){
				baseDao.deleteById(KstarPrjEvl.class, prjevlId);
			}else{
				throw new AnneException("该流程状态无法删除！");
			}
		}

	}

	private void updatePrjevlReviewStatus(String id, String status) {
//		02	审批中	8a828dee5a11fc73015a12a80643000c
//		03	已审批	8a828dee5a11fc73015a12a86421000d
//		01	未启动	8a828dee5a11fc73015a12a77ffe000b
//		04	已驳回	8a828dee5a11fc73015a12a8ce48000e

		StringBuffer updateSql = new StringBuffer();
		updateSql.append(" update CRM_T_PRJ_EVL set C_EVL_ST='" + status+ "' where C_PID ='"+id+"'");
		baseDao.executeSQL(updateSql.toString());
	}

	private void updateContractReviewStatus(String id, String status) {

		StringBuffer hql = new StringBuffer();
		hql.append(" update Contract contr set contr.reviewStat='" + status+ "' where contr.id ='"+id+"'");
		baseDao.executeHQL(hql.toString());
	}

	@Override
	public void updateContractTrialStatus(String id, String status) throws AnneException{
		StringBuffer updateSql = new StringBuffer();
		updateSql.append(" update crm_t_contr_basic set C_TRIAL_STAT='" + status+ "' where C_ID ='"+id+"'");
		baseDao.executeSQL(updateSql.toString());
	}

	@Override
	public void updateContrLoanCheckStatus(String id, String status) throws AnneException{
		StringBuffer updateSql = new StringBuffer();
		updateSql.append(" update crm_t_contr_basic set C_LOAN_CHECK_FLOW_STAT='" + status+ "' where C_ID ='"+id+"'");
		baseDao.executeSQL(updateSql.toString());
	}

	@Override
	public void updateContractFinalStatus(String id, String status) throws AnneException {

		StringBuffer updateSql = new StringBuffer();
		updateSql.append(" update crm_t_contr_basic set C_FINAL_REVIEW_STAT='" + status+ "' where C_ID ='"+id+"'");
		baseDao.executeSQL(updateSql.toString());
	}

	@Override
	public void updateContractStatus(String id, String status) throws AnneException {
		StringBuffer updateSql = new StringBuffer();
		updateSql.append(" update crm_t_contr_basic set C_CONTR_STAT='" + status+ "' where C_ID ='"+id+"'");
		baseDao.executeSQL(updateSql.toString());
	}

	@Override
	public void updateContractProcessCloseDate(String id) throws AnneException {
		StringBuffer updateSql = new StringBuffer();
		updateSql.append(" update crm_t_contr_basic set DT_PROCESS_CLOSE_DATE= sysdate  where C_ID ='"+id+"'");
		baseDao.executeSQL(updateSql.toString());
	}

//	/**
//	 * 通过业务主键获取流程历史
//	 * @param module
//	 * @param businessKey
//	 * @return
//	 */
//	public List<HistoryActivityInstance> getHistoryByBusinessKey(String module ,String businessKey) {
//		ProcessInstance pi = processService.getByBusinessKey(module, businessKey);
//		if(pi!=null){
//			List<HistoryActivityInstance> list = historyService.findHistoryActivityInstance(pi.getId());
//			return list;
//		}else{
//			return null;
//		}
//	}
//
//	@Override
//	public List<HistoryActivityInstance> getContrFinReviewHisLst(String module, String contrId) throws AnneException {
////		module = "cc";
//		List<HistoryActivityInstance> list = getHistoryByBusinessKey(module, contrId);
//		return list;
//	}

	public void startContractReviewProcess(UserObject user,String[] ids,String typ){
		List<KstarPrjEvl> prjEvlList = new ArrayList<KstarPrjEvl>();
		for(int i=0;i<ids.length; i++){

			KstarPrjEvl prj= baseDao.get(KstarPrjEvl.class, ids[i].toString());
			prjEvlList.add(prj);
		}

		String flg="false";
		String contrId = "X";

//		String salesCenter = lovMemberService.getSaleCenter(user.getOrg().getId());
		String salesCenter = "";

		// 审批状态为未启动（01）
		LovMember proN = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "01");
		// 审批状态为审批中（02）
		LovMember proIng = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "02");
		String staN = proN.getId();   // 01	未启动
		String staIng = proIng.getId();   // 02	审批中
		String bizTp="";
		for(KstarPrjEvl ite : prjEvlList){
			if(ite.getEvlSt().equalsIgnoreCase(staN)||ite.getEvlSt()==null||ite.getEvlSt()==""){
				flg= "true";
				contrId=ite.getQuotCode();
				String tolAmt ="";
				String createrId="";
				String createrName="";
				String bizNo="";
				String bizId=ite.getId();
				String appName= "";
				String title="";
				String model="";
				bizTp = ite.getCType();
				Date now = new Date();
				SimpleDateFormat dtFmt=new SimpleDateFormat("yyyy-MM-dd HH:mm");
				appName = getAppnameByType(ite.getEvlTyp(),bizTp);
				Contract contract = null;
				if(typ.equalsIgnoreCase(IConstants.CONTR_CHANGE)){
					ContrChange chg = changeService.get(contrId);
					tolAmt = chg.getTotalAmtDesc();
					bizNo=chg.getChangeNo();
					contract = get(chg.getContrId());
					salesCenter = lovMemberService.getSaleCenter(contract.getMarketDept());
					createrId= appName==IConstants.CONTR_CHANGE_BUSINESS_PROC ?contract.getOrderer() :contract.getCreator();
					createrName=appName==IConstants.CONTR_CHANGE_BUSINESS_PROC ?contract.getOrdererName() :contract.getCreatorName();
				}else{
					contract = get(contrId);
					tolAmt = contract.getTotalAmtDesc();
					bizNo=contract.getContrNo();
					salesCenter = lovMemberService.getSaleCenter(contract.getMarketDept());
					createrId= appName==IConstants.CONTR_STAND_BUSINESS_PROC ?contract.getOrderer() :contract.getCreator();
					createrName=appName==IConstants.CONTR_STAND_BUSINESS_PROC ?contract.getOrdererName() :contract.getCreatorName();
				}

				String ti = getTitleByAppName(appName);
//				title= bizNo+ti+" - "+user.getEmployee().getName()+" - "+dtFmt.format(now);
				title= ti+" - "+bizNo+" - "+contract.getCustName()+"-"+ contract.getCreatorName() +" - "+dtFmt.format(now);
				model= lovMemberService.getFlowCodeByAppCode(appName);

				Map<String,String> varmap = new HashMap<>();
				varmap.put("title", title);
				varmap.put("app", appName);
				varmap.put("typ", typ);
				varmap.put("prjevlId", bizId);
				varmap.put("SalesCenter", salesCenter);
				varmap.put("Contract_TotalAmt", tolAmt);
				//表单管理的员工
				varmap.put("employeeIdInForm", createrId);
				varmap.put("employeeNameInForm", createrName);

				varmap.put("employeeIdInForm2",  contract.getOrderer());
				varmap.put("employeeNameInForm2", contract.getOrdererName());
				varmap.put("contractType", contract.getContrTypeName());

				if(StringUtil.isNotEmpty(contract.getCreator())){
					Employee emp =  employeeService.get(contract.getCreator());
					user = getContrMgrEmp(emp.getNo());
				}
				
				String sql = "select * from sys_t_lov_member a where 1=1 and a.group_Code = 'ORG' and a.opt_Txt3 in('C','B') "
		        		+ "and a.leaf_Flag='N' start with a.row_id = ? connect by prior a.parent_Id = a.row_id";
		        String orgType = "C";
		        List<Map<String, Object>> positions = baseDao.findBySql4Map(sql,new Object[]{user.getOrg().getId()});
		        if(positions != null && !positions.isEmpty()){
		        	orgType = positions.get(0).get("OPT_TXT3").toString();
		        }
		        varmap.put("orgType", orgType);
//				ProcessInstance pi = startContrReviewSubProcess(user,bizId,appName, title,model,typ,contrId);
				ProcessInstance pi = startContrReviewSubProcess(user,contrId,model,varmap);
				//更新 售前评审类型的合同评审 状态为 审批中
//				updatePrjevlReviewStatus(bizId,staIng);
				ite.setEvlSt(staIng);
				ite.setEvlRs(pi.getId());
			}

		}
		// 合同状态更新
		LovMember stdLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "03");// 03	待评审
		//if(flg.equalsIgnoreCase("true")){
		if(bizTp.equalsIgnoreCase(IConstants.CONTR_STAND)|| bizTp.equalsIgnoreCase(IConstants.CONTR_LOAN)||bizTp.equalsIgnoreCase(IConstants.CONTR_PI) ){
			updateContractReviewStatus(contrId,proIng.getId());

            //初审完成 14
            LovMember status14 = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "14");
            Contract contract = get(contrId);
            if (status14.getId().equals(contract.getContrStat())) {
                //待评审 03
                updateContractStatus(contrId, stdLov.getId());
            }
		}else if(bizTp.equalsIgnoreCase(IConstants.CONTR_CHANGE)){
			changeService.updateContrChgReviewStatus(contrId,proIng.getId());
			changeService.updateContrChgStatus(contrId, stdLov.getId());
		}
//			// 更新合同状态
//			LovMember conlov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "03");//03	待评审
//			updateContractStatus(contrId, conlov.getId());
		//}

	}

	/**
	 * 合同评审完成
	 */
	public void completeContrReviewProcess(UserObject user,String contrId) throws AnneException {
		// 合同审批状态
		LovMember unstartStatus = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "01");//未启动
		LovMember comStatus = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "03");//已审批
		LovMember rejStatus = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "04");//已驳回
		LovMember desStatus = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "05");//已销毁

		// 合同状态更新
		LovMember staLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "04");//待合同书评审
		// 合同变更状态更新
		LovMember chgLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "05");//05	待签订
		// 合同变更状态更新
		LovMember piLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "07");//07	已签订待商务下单
		String flg="true";
		String bizTp="";
		List<KstarPrjEvl> prjLst = queryContrPrjevlListByContrId(contrId);

        Contract contract = this.get(contrId);
        LovMember contracttype = lovMemberService.getLovMemberByCode("CONTRACTTYPE", IConstants.CONTR_STAND_0103);

		for(KstarPrjEvl pevl : prjLst){
			bizTp = pevl.getCType();
			if (contract != null && Objects.equals(contract.getContrType(), contracttype.getId())) {
				//框架下合同允许部分合同审批完成的情况下进行审批完成操作
				if(pevl.getEvlSt().equalsIgnoreCase(comStatus.getId()) || pevl.getEvlSt().equalsIgnoreCase(unstartStatus.getId())){
					continue;
				}
			} else {
				if(pevl.getEvlSt().equalsIgnoreCase(comStatus.getId())
						|| pevl.getEvlSt().equalsIgnoreCase(rejStatus.getId())
						|| pevl.getEvlSt().equalsIgnoreCase(desStatus.getId())){
					continue;
				}
			}
			flg="false";
			break;
		}

		if (prjLst.size() == 0) {
			if (contracttype.getId().equals(contract.getContrType())) {
				bizTp = "CONTR_STAND";
			}
		}

		if(flg.equalsIgnoreCase("true")){
			if(bizTp.equalsIgnoreCase(IConstants.CONTR_STAND)){
				updateContractReviewStatus(contrId,comStatus.getId());
	//			// 合同状态
				updateContractStatus(contrId, staLov.getId());
				// 合同评审完成自动完成 合同书评审
				startContractFinalProcess(user, contrId, IConstants.CONTR_STAND);
			}else if(bizTp.equalsIgnoreCase(IConstants.CONTR_CHANGE)){
				changeService.updateContrChgReviewStatus(contrId,comStatus.getId());
				// 合同变更状态
				changeService.updateContrChgStatus(contrId,chgLov.getId());
			}else if(bizTp.equalsIgnoreCase(IConstants.CONTR_PI)){
				updateContractReviewStatus(contrId,comStatus.getId());
	//			// 合同状态
				updateContractStatus(contrId, piLov.getId());
			}
		}else{
			throw new AnneException("当前合同评审还未完成，请在全部完成之后再点击按钮！");
		}

	}

	@Override
	public List<KstarPrjEvl> queryContrPrjevlListByContrId(String contrId){
		 Condition condition = new Condition();
		 condition.getFilterObject().addCondition("quotCode", "eq", contrId);
		FilterObject filterObject = condition.getFilterObject(KstarPrjEvl.class);
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.findEntity(hqlObject.getHql(),hqlObject.getArgs());
	}

	public void startContractTrialProcess(UserObject user,String contrId, String typ){
		String businessId = contrId; //StringUtil.getUUID();
		Contract contract = get(contrId);
		String tolAmt = contract.getTotalAmtDesc();
		String application = getAppnameByType(IConstants.CONTR_STAND_TRIAL_PROC,typ);
		String model = lovMemberService.getFlowCodeByAppCode(application);
		String bizNo=contract.getContrNo();
		Date now = new Date();
		SimpleDateFormat dtFmt=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String ti = getTitleByAppName(application);
		String title =ti+" -  "+bizNo+" - "+ contract.getCustName() + "-"+user.getEmployee().getName()+" - "+dtFmt.format(now);
		String salesCenter = lovMemberService.getSaleCenter(contract.getMarketDept());//lovMemberService.getSaleCenter(user.getOrg().getId());
		Map<String,String> varmap = new HashMap<>();
		varmap.put("title", title);//"合同申请初审流程 - "+user.getEmployee().getName()+" - "+dtFmt.format(now));
		varmap.put("app", application);
		varmap.put("typ", typ);
		varmap.put("SalesCenter", salesCenter);
		varmap.put("Contract_TotalAmt", tolAmt);
		varmap.put("CONTRACTTYPE", contract.getContrTypeName());

		CustomInfo customInfo = customerService.getCustomInfo(contract.getCustCode());
		if(customInfo != null){
			varmap.put("customCategorySubName", customInfo.getCustomCategorySubName());
		}

//		processService.start(model, businessId, new Participant(user.getEmployee().getId(),user.getEmployee().getName(),"EMPLOYEE"),varmap);
		ProcessInstance pi = xflowProcessServiceWrapper.start(model, businessId, user, varmap);
		// 合同初审状态
		LovMember lov = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "02");
//		updateContractTrialStatus(contrId, lov.getId());
		// 合同状态
		LovMember conlov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "02");  //02	待初审
//		updateContractStatus(contrId, conlov.getId());

		Contract contr = get(contrId);
		contr.setContrStat(conlov.getId());
		contr.setTrialStat(lov.getId());
		contr.setProcessInstanceId(pi.getId());
	}

	public void startContractFinalProcess(UserObject user,String contrId, String typ){
		String businessId = contrId; //StringUtil.getUUID();
		Contract contract = get(contrId);
		String tolAmt = contract.getTotalAmtDesc();
		String application = getAppnameByType(IConstants.CONTR_STAND_FINAL_PROC,typ);
		String model = lovMemberService.getFlowCodeByAppCode(application);
		String bizNo=contract.getContrNo();
		String createrId= contract.getOrderer();
		String createrName=contract.getOrdererName();
		Date now = new Date();
		SimpleDateFormat dtFmt=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String salesCenter = lovMemberService.getSaleCenter(contract.getMarketDept());//lovMemberService.getSaleCenter(user.getOrg().getId());
		String ti = getTitleByAppName(application);
		String title =ti+" - "+bizNo+" - "+contract.getCustName() + "-"+user.getEmployee().getName()+" - "+dtFmt.format(now);
		Map<String,String> varmap = new HashMap<>();
		varmap.put("title", title);//"合同申请初审流程 - "+user.getEmployee().getName()+" - "+dtFmt.format(now));
		varmap.put("app", application);
		varmap.put("typ", typ);
		varmap.put("SalesCenter", salesCenter);
		varmap.put("Contract_TotalAmt", tolAmt);
		//表单管理的员工
		varmap.put("employeeIdInForm", createrId);
		varmap.put("employeeNameInForm", createrName);
		varmap.put("contractType", contract.getContrTypeName());

//		processService.start(model, businessId, new Participant(user.getEmployee().getId(),user.getEmployee().getName(),"EMPLOYEE"),varmap);
		ProcessInstance pi = xflowProcessServiceWrapper.start(model, businessId, user, varmap);
		// 合同书评审状态
		LovMember lov = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "02");
//		updateContractFinalStatus(contrId, lov.getId());
		// 合同状态
		LovMember conlov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "04");//04	待合同书评审
//		updateContractStatus(contrId, conlov.getId());


		Contract contr = get(contrId);
		contr.setContrStat(conlov.getId());
		contr.setFinalReviewStat(lov.getId());
		contr.setProcessInstanceId(pi.getId());
	}

	@Override
	public void startContrStdPriceProcess(UserObject user,String contrId){

		String businessId = contrId; //StringUtil.getUUID();
		// 合同申请提交报价， 在合同评审中添加 价格评审 类型的记录
//		LovMember lov = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "02");
//		updateContractTrialStatus(contrId, lov.getId());
		LovMember lovEvlTp = lovMemberService.getLovMemberByCode("CONTRACT_EVLTYPE", "E06");// 价格评审
		LovMember lovReview = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "01");  // 未启动
		KstarPrjEvl prjevl = new KstarPrjEvl();
		prjevl.setSeqno(1);
		prjevl.setQuotCode(businessId);
		prjevl.setEvlTyp(lovEvlTp.getId());
		prjevl.setEvlSt(lovReview.getId());
		prjevl.setSbmDt(new Date());
		String typ = IConstants.CONTR_STAND;
		savePrjevl(prjevl, typ);

		String[] ids = new String[1];
		ids[0]=prjevl.getId();
		startContractReviewProcess(user, ids,typ);

//		Map<String,String> varmap = new HashMap<>();
//		varmap.put("title", "合同申请提交报价 - "+businessId);
//		varmap.put("app", "CONTR_STAND_PRICE_PROC");
//
//		startContrReviewSubProcess(user,prjevl.getId(),"CONTR_STAND_PRICE_PROC", "合同申请价格评审流程","合同_价格评审流程");
	}

	public ProcessInstance startContrReviewSubProcess(UserObject user,String bizId, String flowName,Map<String,String> varmap){
		String businessId = bizId; //StringUtil.getUUID();
//		ProcessInstance pi = processService.start(flowName, businessId, new Participant(user.getEmployee().getId(),user.getEmployee().getName(),"EMPLOYEE"),varmap);
		ProcessInstance pi = xflowProcessServiceWrapper.start(flowName, businessId, user, varmap);
		return pi;
		// 合同评审状态
//		LovMember lov = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "02");
//		updateContractReviewStatus(contrId, lov.getId());
	}


	public void startContrStdNotifyProcess(UserObject user,String contrId, String typ){
		String businessId = contrId; //StringUtil.getUUID();
		Contract contract = get(contrId);
		String tolAmt = contract.getTotalAmtDesc();
		String application = getAppnameByType(IConstants.CONTR_STAND_NOTIFY_PROC,typ);
		String model = lovMemberService.getFlowCodeByAppCode(application);
		String bizNo=contract.getContrNo();
		String createrId= contract.getCreator();
		String createrName=contract.getCreatorName();
		String orderId= contract.getOrderer();
		String orderName=contract.getOrdererName();
		Date now = new Date();
		SimpleDateFormat dtFmt=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String ti = getTitleByAppName(application);
		String title =ti+" -  "+bizNo+" - "+contract.getCustName()+"-"+user.getEmployee().getName()+" - "+dtFmt.format(now);
		String salesCenter = lovMemberService.getSaleCenter(contract.getMarketDept());//lovMemberService.getSaleCenter(user.getOrg().getId());
		Map<String,String> varmap = new HashMap<>();
		varmap.put("title", title);//"合同申请初审流程 - "+user.getEmployee().getName()+" - "+dtFmt.format(now));
		varmap.put("app", application);
		varmap.put("typ", typ);
		varmap.put("SalesCenter", salesCenter);
		varmap.put("Contract_TotalAmt", tolAmt);
		varmap.put("contractType", contract.getContrTypeName());

		//表单管理的员工
		varmap.put("employeeIdInForm", createrId);
		varmap.put("employeeNameInForm", createrName);

		//表单管理的员工2
		varmap.put("employeeIdInForm2", orderId);
		varmap.put("employeeNameInForm2", orderName);

//		processService.start(model, businessId, new Participant(user.getEmployee().getId(),user.getEmployee().getName(),"EMPLOYEE"),varmap);
		ProcessInstance pi = xflowProcessServiceWrapper.start(model, businessId, user, varmap);
		 //借货核销审批状态
		LovMember lov = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "02");
////		updateContractTrialStatus(contrId, lov.getId());
//		// 合同状态
//		LovMember conlov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "02");  //02	待初审
////		updateContractStatus(contrId, conlov.getId());
//
		Contract contr = get(contrId);
		contr.setLoanChkFlowStat(lov.getId());
//		contr.setContrStat(conlov.getId());
//		contr.setTrialStat(lov.getId());
//		contr.setProcessInstanceId(pi.getId());
	}

	@Override
	public String getAppnameByType(String tp, String bizTp) throws AnneException {
		String appName="";

		//合同初审
		String strTrialLov=IConstants.CONTR_STAND_TRIAL_PROC;
		//无合同核销
		String strWriteoffLov=IConstants.CONTR_LOAN_WRITEOFF_PROC;
		// 合同书评审
		String strFinalLov=IConstants.CONTR_STAND_FINAL_PROC;
		//合同变更初审
		String strChgTrialLov=IConstants.CONTR_CHANGE_TRIAL_PROC;
		//借货初审
		String strLoanTrialLov=IConstants.CONTR_LOAN_TRIAL_PROC;
		//PI初审
		String strPITrialLov=IConstants.CONTR_PI_TRIAL_PROC;
		//合同签订通知流程
		String strStdNotifyLov=IConstants.CONTR_STAND_NOTIFY_PROC;

		// 评审类型
		LovMember presalevlLov	 = lovMemberService.getLovMemberByCode("CONTRACT_EVLTYPE", "E01");//E01	售前评审
		LovMember aftsalevlLov	 = lovMemberService.getLovMemberByCode("CONTRACT_EVLTYPE", "E02");//E02	售后评审
		LovMember businessevlLov	 = lovMemberService.getLovMemberByCode("CONTRACT_EVLTYPE", "E03");//E03	商务评审
		LovMember legalevlLov	 = lovMemberService.getLovMemberByCode("CONTRACT_EVLTYPE", "E04");//E04	法务评审
		LovMember riskevlLov	 = lovMemberService.getLovMemberByCode("CONTRACT_EVLTYPE", "E05");//E05	风控评审
		LovMember pricevlLov	 = lovMemberService.getLovMemberByCode("CONTRACT_EVLTYPE", "E06");//E06	价格评审
		LovMember sumevlLov	 = lovMemberService.getLovMemberByCode("CONTRACT_EVLTYPE", "E07");//E07	综合评审
		LovMember desicionevlLov	 = lovMemberService.getLovMemberByCode("CONTRACT_EVLTYPE", "E08");//E08	决策评审

		if (tp.equalsIgnoreCase(presalevlLov.getId()) && bizTp.equalsIgnoreCase(IConstants.CONTR_STAND)){ //E01	售前评审
			appName=IConstants.CONTR_STAND_PRESALE_PROC;
		}else if (tp.equalsIgnoreCase(aftsalevlLov.getId()) && bizTp.equalsIgnoreCase(IConstants.CONTR_STAND)){ // E02	售后评审
			appName=IConstants.CONTR_STAND_AFTSALE_PROC;
		}else if (tp.equalsIgnoreCase(businessevlLov.getId()) && bizTp.equalsIgnoreCase(IConstants.CONTR_STAND)){ // E03	商务评审
			appName=IConstants.CONTR_STAND_BUSINESS_PROC;
		}else if (tp.equalsIgnoreCase(legalevlLov.getId()) && bizTp.equalsIgnoreCase(IConstants.CONTR_STAND)){ // E04	法务评审
			appName=IConstants.CONTR_STAND_LEGALE_PROC;
		}else if (tp.equalsIgnoreCase(riskevlLov.getId()) && bizTp.equalsIgnoreCase(IConstants.CONTR_STAND)){ // E05	风控评审
			appName=IConstants.CONTR_STAND_RISK_PROC;
		}else if (tp.equalsIgnoreCase(pricevlLov.getId()) && bizTp.equalsIgnoreCase(IConstants.CONTR_STAND)){ // E06	价格评审
			appName=IConstants.CONTR_STAND_PRICE_PROC;
		}else if (tp.equalsIgnoreCase(sumevlLov.getId()) && bizTp.equalsIgnoreCase(IConstants.CONTR_STAND)){ // E07	综合评审
			appName=IConstants.CONTR_STAND_SUM_PROC;
		}else if (tp.equalsIgnoreCase(desicionevlLov.getId()) && bizTp.equalsIgnoreCase(IConstants.CONTR_STAND)){ // E08	决策评审
			appName=IConstants.CONTR_STAND_DESICION_PROC;
		}else if (tp.equalsIgnoreCase(strTrialLov) && bizTp.equalsIgnoreCase(IConstants.CONTR_STAND)){ // 合同初审
			appName=IConstants.CONTR_STAND_TRIAL_PROC;
		}else if (tp.equalsIgnoreCase(strFinalLov) && bizTp.equalsIgnoreCase(IConstants.CONTR_STAND)){ // 合同书评审
			appName=IConstants.CONTR_STAND_FINAL_PROC;
		}else if (tp.equalsIgnoreCase(presalevlLov.getId()) && bizTp.equalsIgnoreCase(IConstants.CONTR_CHANGE)){ //E01	售前评审
			appName=IConstants.CONTR_CHANGE_PRESALE_PROC;
		}else if (tp.equalsIgnoreCase(aftsalevlLov.getId()) && bizTp.equalsIgnoreCase(IConstants.CONTR_CHANGE)){ // E02	售后评审
			appName=IConstants.CONTR_CHANGE_AFTSALE_PROC;
		}else if (tp.equalsIgnoreCase(businessevlLov.getId()) && bizTp.equalsIgnoreCase(IConstants.CONTR_CHANGE)){ // E03	商务评审
			appName=IConstants.CONTR_CHANGE_BUSINESS_PROC;
		}else if (tp.equalsIgnoreCase(legalevlLov.getId()) && bizTp.equalsIgnoreCase(IConstants.CONTR_CHANGE)){ // E04	法务评审
			appName=IConstants.CONTR_CHANGE_LEGALE_PROC;
		}else if (tp.equalsIgnoreCase(riskevlLov.getId()) && bizTp.equalsIgnoreCase(IConstants.CONTR_CHANGE)){ // E05	风控评审
			appName=IConstants.CONTR_CHANGE_RISK_PROC;
		}else if (tp.equalsIgnoreCase(pricevlLov.getId()) && bizTp.equalsIgnoreCase(IConstants.CONTR_CHANGE)){ // E06	价格评审
			appName=IConstants.CONTR_CHANGE_PRICE_PROC;
		}else if (tp.equalsIgnoreCase(sumevlLov.getId()) && bizTp.equalsIgnoreCase(IConstants.CONTR_CHANGE)){ // E07	综合评审
			appName=IConstants.CONTR_CHANGE_SUM_PROC;
		}else if (tp.equalsIgnoreCase(desicionevlLov.getId()) && bizTp.equalsIgnoreCase(IConstants.CONTR_CHANGE)){ // E08	决策评审
			appName=IConstants.CONTR_CHANGE_DESICION_PROC;
		}else if (tp.equalsIgnoreCase(strChgTrialLov) && bizTp.equalsIgnoreCase(IConstants.CONTR_CHANGE)){ // 合同初审
			appName=IConstants.CONTR_CHANGE_TRIAL_PROC;
		}else if (tp.equalsIgnoreCase(strLoanTrialLov) && bizTp.equalsIgnoreCase(IConstants.CONTR_LOAN)){ // 合同变更初审
			appName=IConstants.CONTR_LOAN_TRIAL_PROC;
		}else if (tp.equalsIgnoreCase(strWriteoffLov) && bizTp.equalsIgnoreCase(IConstants.CONTR_LOAN)){ // 无合同核销
			appName=IConstants.CONTR_LOAN_WRITEOFF_PROC ;
		}else if (tp.equalsIgnoreCase(presalevlLov.getId()) && bizTp.equalsIgnoreCase(IConstants.CONTR_PI)){ //E01	售前评审
			appName=IConstants.CONTR_PI_PRESALE_PROC;
		}else if (tp.equalsIgnoreCase(aftsalevlLov.getId()) && bizTp.equalsIgnoreCase(IConstants.CONTR_PI)){ // E02	售后评审
			appName=IConstants.CONTR_PI_AFTSALE_PROC;
		}else if (tp.equalsIgnoreCase(businessevlLov.getId()) && bizTp.equalsIgnoreCase(IConstants.CONTR_PI)){ // E03	商务评审
			appName=IConstants.CONTR_PI_BUSINESS_PROC;
		}else if (tp.equalsIgnoreCase(legalevlLov.getId()) && bizTp.equalsIgnoreCase(IConstants.CONTR_PI)){ // E04	法务评审
			appName=IConstants.CONTR_PI_LEGALE_PROC;
		}else if (tp.equalsIgnoreCase(riskevlLov.getId()) && bizTp.equalsIgnoreCase(IConstants.CONTR_PI)){ // E05	风控评审
			appName=IConstants.CONTR_PI_RISK_PROC;
		}else if (tp.equalsIgnoreCase(pricevlLov.getId()) && bizTp.equalsIgnoreCase(IConstants.CONTR_PI)){ // E06	价格评审
			appName=IConstants.CONTR_PI_PRICE_PROC;
		}else if (tp.equalsIgnoreCase(sumevlLov.getId()) && bizTp.equalsIgnoreCase(IConstants.CONTR_PI)){ // E07	综合评审
			appName=IConstants.CONTR_PI_SUM_PROC;
		}else if (tp.equalsIgnoreCase(desicionevlLov.getId()) && bizTp.equalsIgnoreCase(IConstants.CONTR_PI)){ // E08	决策评审
			appName=IConstants.CONTR_PI_DESICION_PROC;
		}else if (tp.equalsIgnoreCase(strPITrialLov) && bizTp.equalsIgnoreCase(IConstants.CONTR_PI)){ // 合同初审
			appName=IConstants.CONTR_PI_TRIAL_PROC;
		}else if (tp.equalsIgnoreCase(strStdNotifyLov) && bizTp.equalsIgnoreCase(IConstants.CONTR_STAND)){ // 合同签订通知流程
			appName=IConstants.CONTR_STAND_NOTIFY_PROC;
		}
		return appName;
	}

	@Override
	public String getTitleByAppName(String appName) throws AnneException {
		String title="";

//		//合同初审
//		String strTrialLov=IConstants.CONTR_STAND_TRIAL_PROC;
//		// 合同书评审
//		String strFinalLov=IConstants.CONTR_STAND_FINAL_PROC;
//		//合同变更初审
//		String strChgTrialLov=IConstants.CONTR_CHANGE_TRIAL_PROC;
//		//借货初审
//		String strLoanTrialLov=IConstants.CONTR_LOAN_TRIAL_PROC;
//		//PI初审
//		String strPITrialLov=IConstants.CONTR_PI_TRIAL_PROC;
//
//		if (appName.equalsIgnoreCase(IConstants.CONTR_STAND_PRESALE_PROC)){ //E01	售前评审
//			title =ProcessConstants.CONTR_STAND_PRESALE_PROC;
//		}else if (appName.equalsIgnoreCase(IConstants.CONTR_STAND_AFTSALE_PROC)){ // E02	售后评审
//			title =ProcessConstants.CONTR_STAND_AFTSALE_PROC;
//		}else if (appName.equalsIgnoreCase(IConstants.CONTR_STAND_BUSINESS_PROC)){ // E03	商务评审
//			title =ProcessConstants.CONTR_STAND_BUSINESS_PROC;
//		}else if (appName.equalsIgnoreCase(IConstants.CONTR_STAND_LEGALE_PROC)){ // E04	法务评审
//			title =ProcessConstants.CONTR_STAND_LEGALE_PROC;
//		}else if (appName.equalsIgnoreCase(IConstants.CONTR_STAND_RISK_PROC)){ // E05	风控评审
//			title =ProcessConstants.CONTR_STAND_RISK_PROC;
//		}else if (appName.equalsIgnoreCase(IConstants.CONTR_STAND_PRICE_PROC)){ // E06	价格评审
//			title =ProcessConstants.CONTR_STAND_PRICE_PROC;
//		}else if (appName.equalsIgnoreCase(IConstants.CONTR_STAND_SUM_PROC)){ // E07	综合评审
//			title =ProcessConstants.CONTR_STAND_SUM_PROC;
//		}else if (appName.equalsIgnoreCase(IConstants.CONTR_STAND_DESICION_PROC)){ // E08	决策评审
//			title =ProcessConstants.CONTR_STAND_DESICION_PROC;
//		}else if (appName.equalsIgnoreCase(strTrialLov)){ // 合同初审
//			title =ProcessConstants.CONTR_STAND_TRIAL_PROC;
//		}else if (appName.equalsIgnoreCase(strFinalLov)){ // 合同书评审
//			title =ProcessConstants.CONTR_STAND_FINAL_PROC;
//		}else if (appName.equalsIgnoreCase(IConstants.CONTR_CHANGE_PRESALE_PROC)){ //E01	售前评审
//			title =ProcessConstants.CONTR_CHANGE_PRESALE_PROC;
//		}else if (appName.equalsIgnoreCase(IConstants.CONTR_CHANGE_AFTSALE_PROC)){ // E02	售后评审
//			title =ProcessConstants.CONTR_CHANGE_AFTSALE_PROC;
//		}else if (appName.equalsIgnoreCase(IConstants.CONTR_CHANGE_BUSINESS_PROC)){ // E03	商务评审
//			title =ProcessConstants.CONTR_CHANGE_BUSINESS_PROC;
//		}else if (appName.equalsIgnoreCase(IConstants.CONTR_CHANGE_LEGALE_PROC)){ // E04	法务评审
//			title =ProcessConstants.CONTR_CHANGE_LEGALE_PROC;
//		}else if (appName.equalsIgnoreCase(IConstants.CONTR_CHANGE_RISK_PROC)){ // E05	风控评审
//			title =ProcessConstants.CONTR_CHANGE_RISK_PROC;
//		}else if (appName.equalsIgnoreCase(IConstants.CONTR_CHANGE_PRICE_PROC)){ // E06	价格评审
//			title =ProcessConstants.CONTR_CHANGE_PRICE_PROC;
//		}else if (appName.equalsIgnoreCase(IConstants.CONTR_CHANGE_SUM_PROC)){ // E07	综合评审
//			title =ProcessConstants.CONTR_CHANGE_SUM_PROC;
//		}else if (appName.equalsIgnoreCase(IConstants.CONTR_CHANGE_DESICION_PROC)){ // E08	决策评审
//			title =ProcessConstants.CONTR_CHANGE_DESICION_PROC;
//		}else if (appName.equalsIgnoreCase(strChgTrialLov)){ // 合同初审
//			title =ProcessConstants.CONTR_CHANGE_TRIAL_PROC;
//		}else if (appName.equalsIgnoreCase(strLoanTrialLov)){ // 合同初审
//			title =ProcessConstants.CONTR_LOAN_TRIAL_PROC;
//		}else if (appName.equalsIgnoreCase(IConstants.CONTR_PI_PRESALE_PROC)){ //E01	售前评审
//			title =ProcessConstants.CONTR_PI_PRESALE_PROC;
//		}else if (appName.equalsIgnoreCase(IConstants.CONTR_PI_AFTSALE_PROC)){ // E02	售后评审
//			title =ProcessConstants.CONTR_PI_AFTSALE_PROC;
//		}else if (appName.equalsIgnoreCase(IConstants.CONTR_PI_BUSINESS_PROC)){ // E03	商务评审
//			title =ProcessConstants.CONTR_PI_BUSINESS_PROC;
//		}else if (appName.equalsIgnoreCase(IConstants.CONTR_PI_LEGALE_PROC)){ // E04	法务评审
//			title =ProcessConstants.CONTR_PI_LEGALE_PROC;
//		}else if (appName.equalsIgnoreCase(IConstants.CONTR_PI_RISK_PROC)){ // E05	风控评审
//			title =ProcessConstants.CONTR_PI_RISK_PROC;
//		}else if (appName.equalsIgnoreCase(IConstants.CONTR_PI_PRICE_PROC)){ // E06	价格评审
//			title =ProcessConstants.CONTR_PI_PRICE_PROC;
//		}else if (appName.equalsIgnoreCase(IConstants.CONTR_PI_SUM_PROC)){ // E07	综合评审
//			title =ProcessConstants.CONTR_PI_SUM_PROC;
//		}else if (appName.equalsIgnoreCase(IConstants.CONTR_PI_DESICION_PROC)){ // E08	决策评审
//			title =ProcessConstants.CONTR_PI_DESICION_PROC;
//		}else if (appName.equalsIgnoreCase(strPITrialLov)){ // 合同初审
//			title =ProcessConstants.CONTR_PI_TRIAL_PROC;
//		}

		LovMember lov = lovMemberService.getLovMemberByCode(IConstants.FLOW_APPLICATION, appName);
		title = lov.getName();
		return title;
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
	public List<ContrAddr> getAddrList(Condition condition) throws AnneException {
		FilterObject filterObject = condition.getFilterObject(ContrAddr.class);
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.findEntity(hqlObject.getHql(),hqlObject.getArgs());
	}

	public List<ContrPay> getPayPlanList(Condition condition) throws AnneException {
		FilterObject filterObject = condition.getFilterObject(ContrPay.class);
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.findEntity(hqlObject.getHql(),hqlObject.getArgs());
	}

	public List<KstarPrjLst> getPrjlstList(Condition condition) throws AnneException {
		FilterObject filterObject = condition.getFilterObject(KstarPrjLst.class);
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.findEntity(hqlObject.getHql(),hqlObject.getArgs());
	}

	@Override
	public Contract reviseContract(UserObject user,String contrId) throws AnneException {
		Contract contract = new Contract();
		Contract old = get(contrId);
		if(old == null){
			throw new AnneException(IContractService.class.getName() + " saveQuot : 没有找到要修订的数据");
		}
		contract = copyContract(contract,old, user);

//		teamService.addPosition(
//				user.getPosition().getId(),
//				user.getEmployee().getId(),
//				IConstants.CONTR_STAND,
//				contract.getId());
//		orgTeamService.configPrimaryOrg(contract.getId(), IConstants.CONTR_STAND, user.getOrg().getId());

		String sourceBizType=getContrBusinessTypeById(old.getId());
		String targetBizType=getContrBusinessTypeById(contract.getId());
		//复制权限信息
		teamService.copyPositionNoAuth(old.getId(),sourceBizType,contract.getId(),targetBizType,user.getEmployee().getId());
		orgTeamService.copyPrimaryOrg(old.getId(),sourceBizType,contract.getId(),targetBizType,user.getEmployee().getId());

		old.setIsValid("0");
		baseDao.update(old);
		copyAddrById(old.getId(),contract.getId(),"");
//		copyMemById(old.getId(), contract.getId(),"");
		copyAttachmentById(old.getId(),contract.getId(),"");
		copyPayPlanById(old.getId(), contract.getId());
//		copyLovMemberByMemo(old.getId(), contract.getId());
		copyPrjlstById(old.getId() , contract.getId(), "");
		copyPrjdtl(old.getId(),contract.getId(),"");

		contractXflowClose(contrId, user);

		return contract;

	}

	@Override
	public void signUpContract(UserObject user, String contrId,String typ) throws AnneException {
		Contract old = baseDao.get(Contract.class, contrId);
		if(old == null){
			throw new AnneException(IContractService.class.getName() + " saveQuot : 没有找到要更新的数据");
		}

//		BeanUtils.copyPropertiesIgnoreNull(contract, old);
//		CONTR_STAND-0102	框架协议
//		CONTR_STAND-0202	框架协议
//		10	已签订

		String conStatCode = "";
		String conTpCode = lovMemberService.get(old.getContrType()).getCode();
		if(conTpCode.equalsIgnoreCase("CONTR_STAND-0102") || conTpCode.equalsIgnoreCase("CONTR_STAND-0202")){
			conStatCode ="10";
		}else{
			conStatCode="07";
		}

		//old.setCreateTime(new Time(0));
		LovMember statLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", conStatCode);	// 	07	已签订待商务下单
		old.setContrStat(statLov.getId());
//		old.setActSignDate(new Date());
		old.setSysSignDate(new Date());
		baseDao.update(old);
		Long count = baseDao.findUniqueEntity("select count(*) from Contract where C_ID = ? ",contrId);
		if(count > 1){
			throw new AnneException("合同单ID已经存在!");
		}

		LovMember makDepLov = lovMemberService.get(old.getMarketDept());
		if(makDepLov.getCode().equalsIgnoreCase(IConstants.CONTR_ORG_GN_B1_STR) && (conTpCode.equalsIgnoreCase("CONTR_STAND-0101") || conTpCode.equalsIgnoreCase("CONTR_STAND-0103")) ){
			startContrStdNotifyProcess(user,contrId,typ);
		}


		// 取消签订时0单价的价格平摊;2017-10-09  黄奇
		// 价格平摊
		// share0Price(contrId);

	}

	@Override
	public Contract copyContract(Contract contract, Contract old,UserObject user) throws AnneException{
		BeanUtils.copyProperties(old, contract);
		contract.setId(null);
		String oldVer=old.getContrVer();
		Integer newVer = Integer.parseInt(oldVer)+1;
		contract.setContrVer(newVer.toString());
		LovMember lov = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "01");
		contract.setReviewStat(lov.getId());
		contract.setTrialStat(lov.getId());
		contract.setFinalReviewStat(lov.getId());
		LovMember statLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "01");
		contract.setContrStat(statLov.getId());
		LovMember payLov = lovMemberService.getLovMemberByCode("CONTRACTPAYSTAT", "01");
		contract.setPayStat(payLov.getId());
		contract.setIsValid("1");
//		contract.setOrg(user.getOrg().getId());
		contract.setCreateTime(new Date());

		baseDao.save(contract);
		return contract;
	}

	@Override
	public void copyAttachmentById(String oId, String nId, String typ) throws AnneException{
		Condition condition = new Condition();
		condition.getFilterObject().addCondition("bizId", "=", oId);
		List<KstarAttachment> attList = attachmentService.getAttachmentList(condition);
		for (KstarAttachment att: attList){
			KstarAttachment newAtt = new KstarAttachment();
			BeanUtils.copyProperties(att, newAtt);
			newAtt.setId(null);
			newAtt.setBizId(nId);
			if(typ!="") newAtt.setBizType(typ);
			baseDao.save(newAtt);
		}
	}

	@Override
	public void copyMemById(String oldId, String newId, String typ) throws AnneException{
		Condition condition = new Condition();
		condition.getFilterObject().addCondition("quotCode", "=", oldId);
		List<KstarMemInfo> memList = getMemList(condition);
		for (KstarMemInfo mem: memList){
			KstarMemInfo newObj = new KstarMemInfo();
			BeanUtils.copyProperties(mem, newObj);
			newObj.setId(null);
			newObj.setQuotCode(newId);
			baseDao.save(newObj);
		}
	}

	@Override
	public void copyAddrById(String oId, String nId, String typ) throws AnneException{
		Condition condition = new Condition();
		condition.getFilterObject().addCondition("contrId", "=", oId);
		List<ContrAddr> addrList = getAddrList(condition);
		for (ContrAddr addr: addrList){
			ContrAddr newObj = new ContrAddr();
			BeanUtils.copyProperties(addr, newObj);
			newObj.setId(null);
			newObj.setContrId(nId);
			if(typ!="") newObj.setCType(typ);
			baseDao.save(newObj);
		}
	}

	@Override
	public void copyPayPlanById(String oId,String nId) throws AnneException{
		Condition condition = new Condition();
		condition.getFilterObject().addCondition("contrId", "=", oId);
		List<ContrPay> payList = getPayPlanList(condition);
		for (ContrPay pay: payList){
			ContrPay newObj = new ContrPay();
			BeanUtils.copyProperties(pay, newObj);
			newObj.setId(null);
			newObj.setContrId(nId);
//			if(typ!="") newObj.set
			baseDao.save(newObj);
		}
	}


	@Override
	public void copyPrjlstById(String sourceId,String targetId, String targetTyp) throws AnneException{
		Condition condition = new Condition();
		condition.getFilterObject().addCondition("quotCode", "=", sourceId);
		List<KstarPrjLst> pList = getPrjlstList(condition);
		if(pList.size() >0 ){
			for (KstarPrjLst prl: pList){
				KstarPrjLst newObj = new KstarPrjLst();
				BeanUtils.copyProperties(prl, newObj);
				newObj.setId(null);
				newObj.setQuotCode(targetId);
//				newObj.setBuzCode(buzCode);
				if(!targetTyp.equalsIgnoreCase("")){
					newObj.setCType(targetTyp);
				}

				baseDao.save(newObj);
			}
		}
	}

	/*
	 * 根据新旧合同复制工程清单树结构
	 *
	 */
	@Override
	public void copyLovMemberByMemo(String oldMemo,String newMemo) throws AnneException{
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
//			nlov.setCode(StringUtil.getUUID());
			Date now = new Date();
			SimpleDateFormat dtFmt=new SimpleDateFormat("yyyyMMddHHmmssSSS");
			int r = buildRandom(2);
			String codeStr = dtFmt.format(now) + r;
			nlov.setCode(codeStr);
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
	public int buildRandom(int length) throws AnneException{
        int num = 1;
        double random = Math.random();
        if (random < 0.1) {
             random = random + 0.1;
        } for (int i = 0; i < length; i++) {
             num = num * 10;
        }
        return (int) ((random * num));
 }
	/**
	 * 复制工程界面数据
	 *
	 */
	@Override
	public void copyPrjdtl(String oldId,String newId, String typ) throws AnneException {
//		String quottype = "0005";
		String cntrtype = typ;
		//sngups
		KstarSngUps quot_sngups = getKstarSngUps(oldId, typ);
		if(quot_sngups!=null){
			KstarSngUps cntrt_sngups = new KstarSngUps();
			BeanUtils.copyPropertiesIgnoreNull(quot_sngups, cntrt_sngups);
			cntrt_sngups.setQuotCode(newId);
			if(typ!="") cntrt_sngups.setCType(cntrtype);
			baseDao.save(cntrt_sngups);
		}
		//baseinf
		KstarBaseInf quot_baseinf = getKstarBaseInf(oldId, typ);
		if(quot_baseinf!=null){
			KstarBaseInf cntrt_baseinf = new KstarBaseInf();
			BeanUtils.copyPropertiesIgnoreNull(quot_baseinf, cntrt_baseinf);
			cntrt_baseinf.setQuotCode(newId);
			if(typ!="") cntrt_baseinf.setCType(cntrtype);

			baseDao.save(cntrt_baseinf);
		}
		//idu
		KstarIdu quot_idu = getKstarIdu(oldId, typ);
		if(quot_idu!=null){
			KstarIdu cntrt_idu = new KstarIdu();
			BeanUtils.copyPropertiesIgnoreNull(quot_idu, cntrt_idu);
			cntrt_idu.setQuotCode(newId);
			if(typ!="") cntrt_idu.setCType(cntrtype);

			baseDao.save(cntrt_idu);
		}
		//idm
		KstarIdm quot_idm = getKstarIdm(oldId, typ);
		if(quot_idm!=null){
		KstarIdm cntrt_idm = new KstarIdm();
		BeanUtils.copyPropertiesIgnoreNull(quot_idm, cntrt_idm);
		cntrt_idm.setQuotCode(newId);
		if(typ!="") cntrt_idm.setCType(cntrtype);

		baseDao.save(cntrt_idm);
		}
		//sngbty
		KstarSngBty quot_sngbty = getKstarSngBty(oldId, typ);
		if(quot_sngbty!=null){
		KstarSngBty cntrt_sngbty = new KstarSngBty();
		BeanUtils.copyPropertiesIgnoreNull(quot_sngbty, cntrt_sngbty);
		cntrt_sngbty.setQuotCode(newId);
		if(typ!="") cntrt_sngbty.setCType(cntrtype);

		baseDao.save(cntrt_sngbty);
		}
		//sngelec
		KstarSngElec quot_sngelec = getKstarSngElec(oldId, typ);
		if(quot_sngelec!=null){
		KstarSngElec cntrt_sngelec = new KstarSngElec();
		BeanUtils.copyPropertiesIgnoreNull(quot_sngelec, cntrt_sngelec);
		cntrt_sngelec.setQuotCode(newId);
		if(typ!="") cntrt_sngelec.setCType(cntrtype);

		baseDao.save(cntrt_sngelec);
		}
		//sngclr
		KstarSngClr quot_sngclr = getKstarSngClr(oldId, typ);
		if(quot_sngclr!=null){
		KstarSngClr cntrt_sngclr = new KstarSngClr();
		BeanUtils.copyPropertiesIgnoreNull(quot_sngclr, cntrt_sngclr);
		cntrt_sngclr.setQuotCode(newId);
		if(typ!="") cntrt_sngclr.setCType(cntrtype);

		baseDao.save(cntrt_sngclr);
		}
		//sngrck
		KstarSngRck quot_sngrck = getKstarSngRck(oldId, typ);
		if(quot_sngrck!=null){
		KstarSngRck cntrt_sngrck = new KstarSngRck();
		BeanUtils.copyPropertiesIgnoreNull(quot_sngrck, cntrt_sngrck);
		cntrt_sngrck.setQuotCode(newId);
		if(typ!="") cntrt_sngrck.setCType(cntrtype);

		baseDao.save(cntrt_sngrck);
		}
		//sngmnt
		KstarSngMnt quot_sngmnt = getKstarSngMnt(oldId, typ);
		if(quot_sngmnt!=null){
		KstarSngMnt cntrt_sngmnt = new KstarSngMnt();
		BeanUtils.copyPropertiesIgnoreNull(quot_sngmnt, cntrt_sngmnt);
		cntrt_sngmnt.setQuotCode(newId);
		if(typ!="") cntrt_sngmnt.setCType(cntrtype);

		baseDao.save(cntrt_sngmnt);
		}

	}


	/*
	 * 工程清单
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public IPage queryPrjLst(PageCondition condition) {
//		FilterObject filterObject = condition.getFilterObject(PrjLstDtl.class);
//		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
//		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());


//		String quotCode = condition.getStringCondition("qid");
		String quotCode = condition.getStringCondition("contrId");
//		String parentId = condition.getStringCondition("parentId");
		String searchKey = condition.getStringCondition("searchKey");

		StringBuffer hql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		//StringBuffer hql = new StringBuffer(" from KstarPrjEvl where CType = '0003' and quotCode = ? ");
		//hql.append("select lst,lov from KstarPrjLst lst,LovMember lov where lov.groupId ='PRJLSTPRDCAT' and lov.id = lst.lvId and lst.CType = '0003' and lst.quotCode = ? ");
		//hql.append("select lst,lov from KstarPrjLst lst,LovMember lov where lov.groupId ='PRJLSTPRDCAT' and lov.leafFlag = 'Y' and lov.id = lst.lvId and lst.CType = '0003' and lov.memo = ? and lst.quotCode = ? ");
//		hql.append("select lst,lov from KstarPrjLst lst,LovMember lov where lov.groupId ='PRJLSTPRDCAT' and lov.id = lst.lvId and lst.CType = '0005' and lov.memo = ? and lst.quotCode = ? ");
//		hql.append("select lst,lov from KstarPrjLst lst,LovMember lov where lov.groupId ='PRJLSTPRDCAT' and lov.id = lst.lvId and lov.memo = ? and lst.quotCode = ? ");
		hql.append("select lst from KstarPrjLst lst where lst.quotCode = ?  and lst.contractEliminateId is null ");
		args.add(quotCode);
//		args.add(quotCode);

//		if (parentId!=null){
//			hql.append(" and lov.path like ? ");
//			args.add("%"+parentId+"%");
//		}
		if (searchKey!=null){
			hql.append("  and ( lst.prdNm like ? or lst.prdTyp like ? ) ");
			args.add("%"+searchKey+"%");
			args.add("%"+searchKey+"%");
		}
		hql.append(" order by lst.prdCtgid, CAST(lst.lineNum as integer) ");
		IPage page = baseDao.search(hql.toString(),args.toArray(),condition.getRows(), condition.getPage());
//		List<Object[]> list = (List<Object[]>) page.getList();
//		((PageImpl)page).setList(BeanUtils.castList(PrjLstDtl.class,list));
		return page;
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
	public void updatePrjLst(KstarPrjLst prjLst,String typ, String contrId) throws AnneException {

		String prjLstId = prjLst.getId();

		if((prjLst.getAmt()!=null)&&(prjLst.getPrdPrc()!=null)){
			Double tmpttlUnt = prjLst.getAmt() * prjLst.getPrdPrc();
			prjLst.setTtlUnt(tmpttlUnt);
			Double tmpNotVeriNum = prjLst.getAmt()-(prjLst.getVeriedNum()==null?0:prjLst.getVeriedNum());
			prjLst.setNotVeriNum(tmpNotVeriNum);
		}

		KstarPrjLst oldPrjLst = baseDao.get(KstarPrjLst.class, prjLstId);
		if(oldPrjLst == null){
//			throw new AnneException( " savePrjLst : 没有找到要更新的数据");

			Contract contract = this.get(contrId);
			prjLst.setBuzCode(contract.getContrNo());
			prjLst.setVeriedNum(0.0);
			prjLst.setLineNum(this.getLineNum(contrId));
			prjLst.setId(null);
			baseDao.saveOrUpdate(prjLst);
		}else{
			BeanUtils.copyPropertiesIgnoreNull(prjLst, oldPrjLst);
			oldPrjLst.setId(prjLstId);
			baseDao.saveOrUpdate(oldPrjLst);
		}

	}

	@Override
	public void savePrjLst(KstarPrjLst prjLst,String cType) throws AnneException {
		if(StringUtil.isEmpty(prjLst.getQuotCode())){
			throw new AnneException("报价单编号不能为空");
		}
		prjLst.setCType(cType);

		if((prjLst.getAmt()!=null)&&(prjLst.getPrdPrc()!=null)){
			Double tmpttlUnt = prjLst.getAmt() * prjLst.getPrdPrc();
			prjLst.setTtlUnt(tmpttlUnt);
		}
//		if((prjLst.getNewAmt()!=null)&&(prjLst.getNewPrdPrc()!=null)){
//			Double tmpttlUnt = prjLst.getNewAmt() * prjLst.getNewPrdPrc();
//			prjLst.setNewTtlUnt(tmpttlUnt);
//		}
//		prjLst.setLineNum(getLineNum(prjLst.getQuotCode()));
		baseDao.save(prjLst);
	}

	/**
	 *
	 * getLineNum:根据合同获取行号. <br/>
	 *
	 * @author Joe
	 * @param contrId
	 * @return
	 */
	@Override
	public String getLineNum(String contrId) throws AnneException{
		String hql = "select line.lineNum from KstarPrjLst line where line.lineNum is not null and  line.quotCode = ? ";
		String lineNo = "";
		List<Integer> lineNoIntLst = new ArrayList();
		try {
			List<String> lineNoList = baseDao.findEntity(hql,
					new Object[] { contrId });
			if (lineNoList == null || lineNoList.size() <= 0) {
				lineNo = "1";
			} else {
				for(String it : lineNoList){
					if(it==null || it==""){
						it="0";
					}
					lineNoIntLst.add(Integer.parseInt(it));
				}
				int maxLineNo = Collections.max(lineNoIntLst);
				int n = maxLineNo + 1;
				lineNo = String.valueOf(n) ;

			}
		} catch (Exception e) {
			throw new AnneException(IOrderService.class.getName()
					+ " getLineNo : 系统出现异常，请联系管理员");
		}
		return lineNo;
	}

	@Override
	public void deletePrjLst( String quotCode) throws AnneException {
//		lovMemberService.delete(lvId);

		List<Object> args = new ArrayList<Object>();
		args.add(quotCode);
//		args.add(lvId);

		baseDao.executeHQL(" delete KstarPrjLst pl where pl.id = ?  ",args.toArray());
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
	public String getLovememroot(String qid) throws AnneException {
		String rootid = "";

		String hql = " select l.id from LovMember l where l.memo = ? and l.groupCode = 'PRJLSTPRDCAT' and l.parentId = 'ROOT' order by level ";
		List<Object> args = new ArrayList<Object>();
		args.add(qid);

		rootid = baseDao.findUniqueEntity(hql, args.toArray());

		return rootid;
	}





	@Override
	public Double getTotalamount(String qid) throws AnneException {

		Double totalamount=(double) 0;

		String hql = " select sum(p.ttlUnt) from KstarPrjLst p where p.quotCode = ? ";
		List<Object> args = new ArrayList<Object>();
		args.add(qid);

		if(this.CheckttlUnt(qid).equals("Y")){
			Double theamount = baseDao.findUniqueEntity(hql, args.toArray());
			totalamount = theamount; // Double.toString(theamount);
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
	public String addlovroot(String qid,String ctype, String groupId) throws AnneException {

		LovMember lovMember = new LovMember();
		KstarPrjLst prjLst = new KstarPrjLst();
		prjLst.setQuotCode(qid);

		if(StringUtil.isEmpty(prjLst.getQuotCode())){
			throw new AnneException("找不到该变更单");
		}else{
			lovMember.setMemo(prjLst.getQuotCode());
		}

		lovMember.setCode(StringUtil.getUUID());
		lovMember.setName(IConstants.CONTR_PRJLST_ROOT_NAME);
		lovMember.setGroupId(groupId);
		lovMember.setLeafFlag("N");


		proLovService.saveCatelog(lovMember);

		prjLst.setPrdNm(IConstants.CONTR_PRJLST_ROOT_NAME);
		prjLst.setLvId(lovMember.getId());
		savePrjLst(prjLst,ctype);

		return lovMember.getId();

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

			if ((prjstdprd_prdSprc>0)&&(stdprd.getAmt()>0)) {

				//标准产品平均总单价 = 【单产品行汇总金额 + （项目类服务产品报价总和 + 项目类商务产品报价总和） * 定价 / (全部单产品定价总和)】/【数量】
//				avgTtl = (stdprd.getTtlUnt() + (prjserprd_ttlunt+prjbizprd_ttlunt)* stdprd.getPrdSprc()/prjstdprd_prdSprc)/stdprd.getAmt() ;
				avgTtl = (stdprd.getHhTtl() + (prjserprd_ttlunt+prjbizprd_ttlunt)* stdprd.getPrdSprc()/prjstdprd_prdSprc)/stdprd.getAmt() ;
			}
			//标准产品平均总单价
			stdprd.setAvgTtl(avgTtl);

			//更新标准产品
			baseDao.update(stdprd);
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
			updatePrjLst(theprjlst,null,currQuotId);
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
	public IPage queryPginf(PageCondition condition) {
		//String quotCode = baseDao.findUniqueEntity("select trim(quotCode) from KstarQuot where rownum =1 and id = ? ",condition.getStringCondition("qid"));
		String quotCode = condition.getStringCondition("contrId");
//		String cType = condition.getStringCondition("cType");
		List<Object> args = new ArrayList<Object>();
//		args.add(cType);
		args.add(quotCode);

//		StringBuffer hql = new StringBuffer(" from KstarPgInf where CType = ? and quotCode = ? ");
		StringBuffer hql = new StringBuffer(" from KstarPgInf where quotCode = ? ");
		return baseDao.search(hql.toString(),args.toArray(),condition.getRows(), condition.getPage());
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
	public KstarPgInf getKstarPgInf(String pginfID) throws AnneException {
		return baseDao.get(KstarPgInf.class, pginfID);
	}

	@Override
	public void deletePgInf(String pginfId) throws AnneException {
		baseDao.deleteById(KstarPgInf.class, pginfId);
	}

	@Override
	public KstarAftSale getKstarAftSale(String aftsaleID, String typ) throws AnneException {
		List<Object> args = new ArrayList<Object>();
		args.add(typ);
		args.add(aftsaleID);

		KstarAftSale aftsale = baseDao.findUniqueEntity("from KstarAftSale where  CType = ? and  quotCode = ? ", args.toArray());
		return aftsale;
	}

	@Override
	public KstarBaseInf getKstarBaseInf(String baseinfID, String typ) throws AnneException {
		List<Object> args = new ArrayList<Object>();
		args.add(typ);
		args.add(baseinfID);

		KstarBaseInf baseinf = baseDao.findUniqueEntity("from KstarBaseInf where  CType = ? and  quotCode = ? ", args.toArray());
		return baseinf;
	}

	@Override
	public KstarIdu getKstarIdu(String iduID, String typ) throws AnneException {
		List<Object> args = new ArrayList<Object>();
		args.add(typ);
		args.add(iduID);

		KstarIdu idu = baseDao.findUniqueEntity("from KstarIdu where  CType = ? and  quotCode = ? ", args.toArray());
		return idu;
	}

	@Override
	public KstarIdm getKstarIdm(String idmID, String typ) throws AnneException {
		List<Object> args = new ArrayList<Object>();
		args.add(typ);
		args.add(idmID);

		KstarIdm idm = baseDao.findUniqueEntity("from KstarIdm where  CType = ? and  quotCode = ? ", args.toArray());
		return idm;
	}

	@Override
	public KstarSngUps getKstarSngUps(String sngupsID, String typ) throws AnneException {
		List<Object> args = new ArrayList<Object>();
		args.add(typ);
		args.add(sngupsID);

		KstarSngUps sngups = baseDao.findUniqueEntity("from KstarSngUps where  CType = ? and  quotCode = ? ", args.toArray());
		return sngups;
	}

	@Override
	public KstarSngBty getKstarSngBty(String sngbtyID, String typ) throws AnneException {
		List<Object> args = new ArrayList<Object>();
		args.add(typ);
		args.add(sngbtyID);

		KstarSngBty sngbty = baseDao.findUniqueEntity("from KstarSngBty where  CType = ? and  quotCode = ? ", args.toArray());
		return sngbty;
	}


	@Override
	public KstarSngElec getKstarSngElec(String sngelecID, String typ) throws AnneException {
		List<Object> args = new ArrayList<Object>();
		args.add(typ);
		args.add(sngelecID);

		KstarSngElec sngelec = baseDao.findUniqueEntity("from KstarSngElec where  CType = ? and  quotCode = ? ", args.toArray());
		return sngelec;
	}

	@Override
	public KstarSngClr getKstarSngClr(String sngclrID, String typ) throws AnneException {
		List<Object> args = new ArrayList<Object>();
		args.add(typ);
		args.add(sngclrID);

		KstarSngClr sngclr = baseDao.findUniqueEntity("from KstarSngClr where  CType = ? and  quotCode = ? ", args.toArray());
		return sngclr;
	}

	@Override
	public KstarSngRck getKstarSngRck(String sngrckID, String typ) throws AnneException {
		List<Object> args = new ArrayList<Object>();
		args.add(typ);
		args.add(sngrckID);

		KstarSngRck sngrck = baseDao.findUniqueEntity("from KstarSngRck where  CType = ? and  quotCode = ? ", args.toArray());
		return sngrck;
	}

	@Override
	public KstarSngMnt getKstarSngMnt(String sngmntID, String typ) throws AnneException {
		List<Object> args = new ArrayList<Object>();
		args.add(typ);
		args.add(sngmntID);

		KstarSngMnt sngmnt = baseDao.findUniqueEntity("from KstarSngMnt where  CType = ? and  quotCode = ? ", args.toArray());
		return sngmnt;
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
	public void updateBaseInf(KstarBaseInf baseinf) throws AnneException {
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
//			oldbsinf.setCType(cType);
			baseDao.update(oldbsinf);

			List<Object> args = new ArrayList<Object>();
//			args.add(cType);
			args.add(baseinf.getId());


			Long count = baseDao.findUniqueEntity("select count(*) from KstarBaseInf where id = ? ",args.toArray());
			if (count > 1) {
				throw new AnneException("ID已经存在!");
			}
		}
	}


	@Override
	public void updateAftsale(KstarAftSale aftsale) throws AnneException {
		KstarAftSale oldaftsale = baseDao.get(KstarAftSale.class, aftsale.getId());

		if(oldaftsale == null){
			throw new AnneException(IQuotService.class.getName() + " saveAftsale : 没有找到要更新的数据");
		}

		if(!aftsale.getId().equals(oldaftsale.getId())){
			throw new AnneException("ID不能被修改");
		}

		BeanUtils.copyPropertiesIgnoreNull(aftsale, oldaftsale);
//		oldaftsale.setCType(cType);
		baseDao.update(oldaftsale);

		List<Object> args = new ArrayList<Object>();
//		args.add(cType);
		args.add(aftsale.getId());

		Long count = baseDao.findUniqueEntity("select count(*) from KstarAftSale where id = ? ",args.toArray());
		if(count > 1){
			throw new AnneException("ID已经存在!");
		}
	}


	@Override
	public void updateSngbty(KstarSngBty sngbty) throws AnneException {
		KstarSngBty oldsngbty = baseDao.get(KstarSngBty.class, sngbty.getId());

		if(oldsngbty == null){
			throw new AnneException(IQuotService.class.getName() + " saveSngbty : 没有找到要更新的数据");
		}

		if(!sngbty.getId().equals(oldsngbty.getId())){
			throw new AnneException("ID不能被修改");
		}

		BeanUtils.copyPropertiesIgnoreNull(sngbty, oldsngbty);
//		oldsngbty.setCType(cType);
		baseDao.update(oldsngbty);

		List<Object> args = new ArrayList<Object>();
//		args.add(cType);
		args.add(sngbty.getId());

		Long count = baseDao.findUniqueEntity("select count(*) from KstarSngBty where id = ? ",args.toArray());
		if(count > 1){
			throw new AnneException("ID已经存在!");
		}
	}


	@Override
	public void updateIdu(KstarIdu idu) throws AnneException {
		KstarIdu oldidu = baseDao.get(KstarIdu.class, idu.getId());

		if(oldidu == null){
			throw new AnneException(IQuotService.class.getName() + " saveIdu : 没有找到要更新的数据");
		}

		if(!idu.getId().equals(oldidu.getId())){
			throw new AnneException("ID不能被修改");
		}

		BeanUtils.copyPropertiesIgnoreNull(idu, oldidu);
//		oldidu.setCType(cType);
		baseDao.update(oldidu);

		List<Object> args = new ArrayList<Object>();
//		args.add(cType);
		args.add(idu.getId());

		Long count = baseDao.findUniqueEntity("select count(*) from KstarIdu where id = ? ",args.toArray());
		if(count > 1){
			throw new AnneException("ID已经存在!");
		}
	}


	@Override
	public void updateIdm(KstarIdm idm) throws AnneException {
		KstarIdm oldidm = baseDao.get(KstarIdm.class, idm.getId());

		if(oldidm == null){
			throw new AnneException(IQuotService.class.getName() + " saveIdm : 没有找到要更新的数据");
		}

		if(!idm.getId().equals(oldidm.getId())){
			throw new AnneException("ID不能被修改");
		}

		BeanUtils.copyPropertiesIgnoreNull(idm, oldidm);
//		oldidm.setCType(cType);
		baseDao.update(oldidm);

		List<Object> args = new ArrayList<Object>();
//		args.add(cType);
		args.add(idm.getId());

		Long count = baseDao.findUniqueEntity("select count(*) from KstarIdm where id = ? ",args.toArray());
		if(count > 1){
			throw new AnneException("ID已经存在!");
		}
	}


	@Override
	public void updateSngMnt(KstarSngMnt sngmnt) throws AnneException {
		KstarSngMnt oldsngmnt = baseDao.get(KstarSngMnt.class, sngmnt.getId());

		if(oldsngmnt == null){
			throw new AnneException(IQuotService.class.getName() + " saveSngmnt : 没有找到要更新的数据");
		}

		if(!sngmnt.getId().equals(oldsngmnt.getId())){
			throw new AnneException("ID不能被修改");
		}

		BeanUtils.copyPropertiesIgnoreNull(sngmnt, oldsngmnt);
//		oldsngmnt.setCType(cType);
		baseDao.update(oldsngmnt);

		List<Object> args = new ArrayList<Object>();
//		args.add(cType);
		args.add(sngmnt.getId());

		Long count = baseDao.findUniqueEntity("select count(*) from KstarSngMnt where id = ? ",args.toArray());
		if(count > 1){
			throw new AnneException("ID已经存在!");
		}
	}


	@Override
	public void updateSngRck(KstarSngRck sngrck) throws AnneException {
		KstarSngRck oldsngrck = baseDao.get(KstarSngRck.class, sngrck.getId());

		if(oldsngrck == null){
			throw new AnneException(IQuotService.class.getName() + " saveSngRck : 没有找到要更新的数据");
		}

		if(!sngrck.getId().equals(oldsngrck.getId())){
			throw new AnneException("ID不能被修改");
		}

		BeanUtils.copyPropertiesIgnoreNull(sngrck, oldsngrck);
//		oldsngrck.setCType(cType);
		baseDao.update(oldsngrck);

		List<Object> args = new ArrayList<Object>();
//		args.add(cType);
		args.add(sngrck.getId());

		Long count = baseDao.findUniqueEntity("select count(*) from KstarSngRck where id = ? ",args.toArray());
		if(count > 1){
			throw new AnneException("ID已经存在!");
		}
	}


	@Override
	public void updateSngelec(KstarSngElec sngelec) throws AnneException {
		KstarSngElec oldsngelec = baseDao.get(KstarSngElec.class, sngelec.getId());

		if(oldsngelec == null){
			throw new AnneException(IQuotService.class.getName() + " saveSngelec : 没有找到要更新的数据");
		}

		if(!sngelec.getId().equals(oldsngelec.getId())){
			throw new AnneException("ID不能被修改");
		}

		BeanUtils.copyPropertiesIgnoreNull(sngelec, oldsngelec);
//		oldsngelec.setCType(cType);
		baseDao.update(oldsngelec);

		List<Object> args = new ArrayList<Object>();
//		args.add(cType);
		args.add(sngelec.getId());

		Long count = baseDao.findUniqueEntity("select count(*) from KstarSngElec where  id = ? ",args.toArray());
		if(count > 1){
			throw new AnneException("ID已经存在!");
		}
	}


	@Override
	public void updateSngclr(KstarSngClr sngclr) throws AnneException {
		KstarSngClr oldsngclr = baseDao.get(KstarSngClr.class, sngclr.getId());

		if(oldsngclr == null){
			throw new AnneException(IQuotService.class.getName() + " saveSngclr : 没有找到要更新的数据");
		}

		if(!sngclr.getId().equals(oldsngclr.getId())){
			throw new AnneException("ID不能被修改");
		}

		BeanUtils.copyPropertiesIgnoreNull(sngclr, oldsngclr);
//		oldsngclr.setCType(cType);
		baseDao.update(oldsngclr);

		List<Object> args = new ArrayList<Object>();
//		args.add(cType);
		args.add(sngclr.getId());

		Long count = baseDao.findUniqueEntity("select count(*) from KstarSngClr where id = ? ",args.toArray());
		if(count > 1){
			throw new AnneException("ID已经存在!");
		}
	}


	@Override
	public void updateSngups(KstarSngUps sngups) throws AnneException {
		KstarSngUps oldsngups = baseDao.get(KstarSngUps.class, sngups.getId());

		if(oldsngups == null){
			throw new AnneException(IQuotService.class.getName() + " savesngups : 没有找到要更新的数据");
		}

		if(!sngups.getId().equals(oldsngups.getId())){
			throw new AnneException("ID不能被修改");
		}

		BeanUtils.copyPropertiesIgnoreNull(sngups, oldsngups);
//		oldsngups.setCType(cType);
		baseDao.update(oldsngups);

		List<Object> args = new ArrayList<Object>();
//		args.add(cType);
		args.add(sngups.getId());

		Long count = baseDao.findUniqueEntity("select count(*) from KstarSngUps where id = ? ",args.toArray());
		if(count > 1){
			throw new AnneException("ID已经存在!");
		}
	}


	@Override
	public IPage queryVeriDtl(PageCondition condition) throws AnneException {
		//String quotCode = baseDao.findUniqueEntity("select trim(quotCode) from KstarQuot where rownum =1 and id = ? ",condition.getStringCondition("qid"));

//		String cType = condition.getStringCondition("cType");

		List<Object> args = new ArrayList<Object>();
		String contrId = condition.getStringCondition("contrId");
		String loanId = condition.getStringCondition("loanId");
		String prjlstId = condition.getStringCondition("prjlstId");
		String loanPrjlstId = condition.getStringCondition("loanPrjlstId");

//		args.add(cType);
//		args.add(contrId);
//		StringBuffer hql = new StringBuffer(" from ContrVeriDetail where 1=1 and contrId = ? and prjlstId = ? order by creationDate  ");
//		StringBuffer hql = new StringBuffer(" from ContrVeriDetail where 1=1 and loanId = ?  order by creationDate  ");
		StringBuffer hql = new StringBuffer(" from ContrVeriDetail where 1=1  ");
		if(contrId != null){
			hql.append(" and contrId = ? ");
			args.add(contrId);
		}
		if(loanId != null){
			hql.append(" and loanId = ? ");
			args.add(loanId);
		}
		if(prjlstId != null){
			hql.append(" and prjlstId = ? ");
			args.add(prjlstId);
		}
		if(loanPrjlstId != null){
			hql.append(" and loanPrjlstId = ? ");
			args.add(loanPrjlstId);			
		}

		hql.append(" order by creationDate  ");
//		args.add(loanId);
//		args.add(contrId);
//		args.add(prjlstId);

		return baseDao.search(hql.toString(),args.toArray(),condition.getRows(), condition.getPage());
	}

	@Override
	public void saveVeriDtlLst(String contrId,String oriPrjlstId,ContrVeriDetlLstVO loanVeriDtlList) throws AnneException, Exception {
		Contract contract = get(contrId);
		KstarPrjLst oriPrj = getKstarPrjLstById(oriPrjlstId);
		Double curTotalNum = oriPrj.getVeriedNum();
		String flg="N";
		//核销明细
		List<Map<Object, Object>> verificationList = loanVeriDtlList.getLoanVeriDtlList();
		for(Map<Object, Object> map : verificationList){

			ContrVeriDetail contrVeriDetail = (ContrVeriDetail) BeanUtils.convertMap(ContrVeriDetail.class, map);
//			ContrVeriDetail oldContrVeriDetail = baseDao.get(ContrVeriDetail.class, contrVeriDetail.getId());
//			if (oldContrVeriDetail == null) {
//				throw new AnneException(IReceiptsService.class.getName()
//						+ " saveOrUpdateVerificationList : 没有找到要更新的数据");
//			}
//			BeanUtils.copyPropertiesIgnoreNull(contrVeriDetail, oldContrVeriDetail);
//			baseDao.update(oldContrVeriDetail);
			if(contrVeriDetail.getCurVeriNum()!=null && contrVeriDetail.getCurVeriNum()>0.0){
				contrVeriDetail.setId(StringUtil.getUUID());
				contrVeriDetail.setContrId(contrId);
				contrVeriDetail.setContrCode(contract.getContrNo());
				contrVeriDetail.setPrjlstId(oriPrjlstId);
				contrVeriDetail.setCustId(contract.getCustCode());
				contrVeriDetail.setCreationDate(new Date());
				baseDao.save(contrVeriDetail);
				KstarPrjLst prj = getKstarPrjLstById(contrVeriDetail.getLoanPrjlstId());
				double notVerSum = prj.getNotVeriNum()== null ? 0 : prj.getNotVeriNum();
				double veriedNum = prj.getVeriedNum()==null ? 0 : prj.getVeriedNum();
                notVerSum = notVerSum - contrVeriDetail.getCurVeriNum();
                prj.setNotVeriNum(notVerSum);
                veriedNum = veriedNum + contrVeriDetail.getCurVeriNum();
                prj.setVeriedNum(veriedNum);
				baseDao.update(prj);
                updateChangePrjVeriedNum(contrId , prj);

                curTotalNum += contrVeriDetail.getCurVeriNum();
				flg="Y";
			}
		}
		if(flg=="Y"){
            double veriedNum = curTotalNum;
            double notVerSum = oriPrj.getAmt() - curTotalNum;
            oriPrj.setVeriedNum(veriedNum);
            oriPrj.setNotVeriNum(notVerSum);
            updateChangePrjVeriedNum(contrId, oriPrj);
			baseDao.saveOrUpdate(oriPrj);
		}

		updateContrctStatByVeriedNum(contrId);
	}

	/**
	 * 判断合同已核销数量，如果全部行核销完毕 把合同从 已签订待商务下单(07)  改成 已签订商务已下单(08)
	 * @param contrId
	 */
	private void updateContrctStatByVeriedNum(String contrId){
		if (StringUtil.isNullOrEmpty(contrId)) {
			return;
		}

		boolean isEnd = false;

		Condition condition = new Condition();
		condition.getFilterObject().addCondition("quotCode", "=", contrId);
		List<KstarPrjLst> list = getPrjlstList(condition);
		for (KstarPrjLst kstarPrjLst : list) {
			// 已核销数量 != 合同数量
			if (kstarPrjLst.getVeriedNum().compareTo(kstarPrjLst.getAmt()) != 0) {
				isEnd = true;
				break;
			}
		}
		Contract contract = get(contrId);
		LovMember lovMember = null;
		if (isEnd) {
			lovMember = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "07");
		}else{
			lovMember = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "08");
		}
		contract.setContrStat(lovMember.getId());
		baseDao.update(contract);
	}



	@Override
	public void deleteVeriDtlLst(String contrVeriDetlId) throws AnneException {
		ContrVeriDetail contrVeriDetail = getContrVeriDtl(contrVeriDetlId);
		if(contrVeriDetail == null){
			throw new AnneException("未找到需要的数据");
		}

		KstarPrjLst oriPrj = getKstarPrjLstById(contrVeriDetail.getPrjlstId());
		KstarPrjLst loanPrj = getKstarPrjLstById(contrVeriDetail.getLoanPrjlstId());
		Double curVeriNum = contrVeriDetail.getCurVeriNum();

		Double oriveriedNum = oriPrj.getVeriedNum();
		Double orinotveriedNum = oriPrj.getNotVeriNum();
        double veriedNum = oriveriedNum - curVeriNum;
        double notVeriNum = orinotveriedNum + curVeriNum;
        oriPrj.setVeriedNum(veriedNum);
        oriPrj.setNotVeriNum(notVeriNum);
		baseDao.saveOrUpdate(oriPrj);

        updateChangePrjVeriedNum(contrVeriDetail.getContrId(),oriPrj);

        Double loanVeridNum= loanPrj.getVeriedNum();
		Double loanNotVeriNum= loanPrj.getNotVeriNum();
		loanPrj.setVeriedNum(loanVeridNum-curVeriNum);
		loanPrj.setNotVeriNum(loanNotVeriNum+curVeriNum);
		baseDao.saveOrUpdate(loanPrj);
		baseDao.delete(contrVeriDetail);

		updateContrctStatByVeriedNum(contrVeriDetail.getContrId());

	}

    /**
     * 更新变更单中工程项目的核销数量
     * @param contrId
     * @param veriedNum
     *@param notVeriNum @return
     */
    private void updateChangePrjVeriedNum(String contrId, KstarPrjLst prj) {
        //language=HQL
        String hql = " update KstarPrjLst prj set prj.veriedNum=?,prj.notVeriNum=? " +
                "where prj.quotCode in (select c.id from ContrChange c where c.contrId=?) " +
                "and prj.lineNum=? and prj.materCode=?";
        this.baseDao.executeHQL(hql, new Object[]{prj.getVeriedNum(), prj.getNotVeriNum(), contrId, prj.getLineNum(), prj.getMaterCode()});
    }

    @Override
	public ContrVeriDetail getContrVeriDtl(String id) throws AnneException {
		return baseDao.findUniqueEntity("from ContrVeriDetail where id = ? ", id);
	}

	@Override
	public KstarPrjLst getKstarPrjLstById(String id) throws AnneException {
		return baseDao.findUniqueEntity("from KstarPrjLst where id = ? ", id);
	}

	public void confirm(String id) throws AnneException{
		ContrVeriDetail veriDtl = getContrVeriDtl(id);
		if(veriDtl == null){
			throw new AnneException(IContractService.class.getName() + " 确认核销明细ContrVeriDetail confirm : 没有找到要更新的数据");
		}
		KstarPrjLst prjlst = getKstarPrjLstById(veriDtl.getPrjlstId());
		if(prjlst == null){
			throw new AnneException(IContractService.class.getName() + " 确认核销明细KstarPrjLst confirm : 没有找到要更新的数据");
		}
		prjlst.setNotVeriNum(veriDtl.getNotVeriedNum());
		prjlst.setVeriedNum(veriDtl.getVeriedNum());

		baseDao.update(prjlst);

	}

	@Override
	public void updatePrjLstOrderNoByContractID(String contractId,String lineNum, String op,String orderCode) throws AnneException {

		List<Object> args = new ArrayList<Object>();
		args.add(contractId);
		args.add(lineNum);
		KstarPrjLst prjlst= baseDao.findUniqueEntity(" from KstarPrjLst where quotCode = ? and lineNum = ?",args.toArray());
		if(prjlst != null && StringUtil.isNotEmpty(prjlst.getMaterCode())){
			if("add".equals(op)){
				prjlst.setOrdNo(orderCode);
			}else if("del".equals(op)){
				prjlst.setOrdNo("");
			}
			baseDao.update(prjlst);
		}
	}

	@Override
	public void updatePayAmtByContrID(String contrId,Double  tolRecdAmt,Double notTolRecAmt) throws AnneException{
		Contract contract = get(contrId);
		contract.setTolRecdAmt(tolRecdAmt);
		contract.setNotTolRecAmt(notTolRecAmt);
		update(contract,null);
	}

	@Override
	public void updateOrderInfoByContrID(String contrId,String orderNo , Double orderedAmt) throws AnneException{
		Contract contract = get(contrId);
		contract.setOrderNo(orderNo);
		contract.setOrderedAmt(orderedAmt);
		update(contract,null);
	}

	@Override
	public void updateDeliverAmtByContrID(String contrId,Double deliveredAmt) throws AnneException{
		Contract contract = get(contrId);
		contract.setDeliveredAmt(deliveredAmt);
		update(contract,null);
	}

	@Override
	public void updateInvoicedAmtByContrID(String contrId,Double invoicedAmt) throws AnneException{
		Contract contract = get(contrId);
		contract.setInvoicedAmt(invoicedAmt);
		update(contract,null);
	}

	@Override
	public void updateContrStaForOrderByContrID(String contrId) throws AnneException{
		Condition con = new Condition();
		con.getFilterObject().addCondition("quotCode", "eq", contrId);
//		con.getFilterObject().addCondition("ordNo", op, " ")
//		List<KstarPrjLst> prjlst=  getPrjlstList(con);
//		String flg = "N";
//		for(KstarPrjLst prj : prjlst){
//			if(prj.getOrdNo() == null || prj.getOrdNo().toString()==""){
//				flg="Y";
//				break;
//			}
//		}
		String contrTypeCd = getContrBusinessTypeById(contrId);
		// 更新合同状态
		LovMember conlov;
//		if(flg=="Y"){
			if(contrTypeCd.equalsIgnoreCase(IConstants.CONTR_LOAN) || contrTypeCd.equalsIgnoreCase(IConstants.CONTR_PI)){
				conlov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "13");//13	审批通过商务已下单
			}else{
				conlov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "08");//08	已签订商务已下单
			}
//		}else{
//			if(contrTypeCd.equalsIgnoreCase(IConstants.CONTR_LOAN)){
//				conlov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "13");//13	审批通过商务已下单
//			}else{
//				conlov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "08");//08	已签订商务已下单
//			}
//		}
		updateContractStatus(contrId, conlov.getId());
	}

	@Override
	public String getContrBusinessTypeById(String contrId) throws AnneException{
		String bizType="";
		Contract contract = get(contrId);
		LovMember conTypelov = lovMemberService.get(contract.getContrType());
		if(conTypelov.getCode().contains(IConstants.CONTR_STAND)){
			bizType=IConstants.CONTR_STAND;
		}else if(conTypelov.getCode().contains(IConstants.CONTR_PI)){
			bizType=IConstants.CONTR_PI;
		}else if(conTypelov.getCode().contains(IConstants.CONTR_LOAN)){
			bizType=IConstants.CONTR_LOAN;
		}else{
			bizType=IConstants.CONTR_CHANGE;
		}
		return bizType;
	}

	@Override
	public List<Contract> selectContract(PageCondition condition) {
		String search = condition.getStringCondition("search");
		String hql = "from Contract c where 1=1";
		List<Object> args = new ArrayList<>();
		if(StringUtil.isNotEmpty(search)){
			hql += " and c.contrNo like ?";
			args.add("%"+search.trim()+"%");
		}
		hql += " and rownum < 200";
		return baseDao.findEntity(hql, args.toArray());
	}

	@Override
	public Contract getContractByNo(String contrNo) {
		String hql = "from Contract c where c.contrNo = ?";
		List<Contract> lst = baseDao.findEntity(hql, contrNo);
		if(lst != null && lst.size() > 0){
			return lst.get(0);
		}
		return null;
	}

	@Override
	public Contract getContractByNoForContrVer(String contrNo) {
		String hql = "from Contract c where c.contrNo = ? order by CAST(c.contrVer as integer) desc ";
		List<Contract> lst = baseDao.findEntity(hql, new Object[]{contrNo});
		if(lst != null && lst.size() > 0){
			return lst.get(0);
		}
		return null;
	}

	/**
	 * 检查客户，业务实体 在erp引入的状态为"审批完成"状态
	 * @param custId
	 * @param businessEntityId
	 * @return
	 */
	@Override
	public String checkContrBusiEntityForOrder(String custId, String businessEntityId) throws AnneException{
		String rtnStr="N";;
		String st = IConstants.CUSTOM_NORMAL_STATUS_40;// 工作流状态审批完成
		List<Object> args = new ArrayList<Object>();
		args.add(st);
		args.add(custId);
		args.add(businessEntityId);

		String hql = " from CustomErpInfo ce where ce.corpErpStatus=? and  ce.customId= ? and ce.corpErpUnit= ? ";
		List<CustomErpInfo> lst = baseDao.findEntity(hql, args.toArray() );
		if(lst!=null && lst.size()>0){
			rtnStr = "Y";
		}
		return rtnStr;
	}

	/**
	 * 检查合同清单是否全部下单，合同清单数量=订单数量+已经核销的数量
	 * @param custId
	 * @param businessEntityId
	 * @return
	 */
	@Override
	public String checkGenOrdLinesByContract(String contrId) throws AnneException{
		String flg="N";
//		StringBuffer hql = new StringBuffer();
//		List<Object> args = new ArrayList<Object>();
//		hql.append("select lst from KstarPrjLst lst where 1=1 and lst.quotCode = ? and lst.ordNo = null and lst.materCode != null ");
//		args.add(contrId);
//		List<KstarPrjLst> list = baseDao.findEntity(hql.toString(), args.toArray());
		Contract contr = get(contrId);
		Condition condition = new Condition();
		condition.getFilterObject().addCondition("quotCode", "=", contrId);
		List<KstarPrjLst> plist = getPrjlstList(condition);
		for (KstarPrjLst prl: plist){
			if(prl.getAmt()!=null && prl.getPrdPrc()!=null ){
				if(prl.getPrdCtg()!=null && !prl.getPrdCtg().equalsIgnoreCase("") && prl.getLineNum() != null && !prl.getLineNum().equalsIgnoreCase("")){
					OrderQuantityVo ordQtyVo= orderService.getContractOrderQty(contr.getContrNo(), prl.getLineNum());
					if(ordQtyVo != null ){
						if(prl.getAmt()> (ordQtyVo.getProQty()+prl.getVeriedNum())){
							flg="Y";
							break;
						}
					}
				}
			}
		}
		return flg;
	}


	@Override
	public void updatePrjlstTypeByContr(String buzId, String cType, String buzCode)  throws AnneException {
		Condition condition = new Condition();
		condition.getFilterObject().addCondition("quotCode", "=", buzId);
		List<KstarPrjLst> plist = getPrjlstList(condition);
		for (KstarPrjLst prl: plist){
			prl.setCType(cType);
			prl.setBuzCode(buzCode);
			if(prl.getProId() != null){
				prl.setVeriedNum(0.0);
				prl.setNotVeriNum(prl.getAmt());
			}
			baseDao.update(prl);
		}
	}

	@Override
	public Boolean checkPayPlanListNull(String buzId)  throws AnneException {
		Boolean flg=true;
		Condition condition = new Condition();
		condition.getFilterObject().addCondition("contrId", "=", buzId);
		List<ContrPay> plist = getPayPlanList(condition);
		if(plist.size()<=0 ) flg=false;
		return flg;
	}

	@Override
	public void discardContr(String contrId,String typ,UserObject  user) throws AnneException{
		Contract contr = get(contrId);
		String contrSta = lovMemberService.get(contr.getContrStat()).getCode();
		String instId = contr.getProcessInstanceId();
		if(contrSta.equalsIgnoreCase("06") || contrSta.equalsIgnoreCase("01")){
			if(  contrSta.equalsIgnoreCase("06")){ //06	返回修改中
				LovMember revLov = lovMemberService.get(contr.getReviewStat());
	//			String trialSt = lovMemberService.get(contr.getTrialStat()).getCode();
//				String reviewSt = lovMemberService.get(contr.getReviewStat()).getCode();
	//			String finalSt = lovMemberService.get(contr.getFinalReviewStat()).getCode();
				if(typ.equalsIgnoreCase(IConstants.CONTR_STAND)  && revLov != null && (revLov.getCode().equalsIgnoreCase("02") || revLov.getCode().equalsIgnoreCase("04")) ){ //02	审批中,04 已驳回
					List<KstarPrjEvl> prjevlLst	=queryContrPrjevlListByContrId(contrId);
					if(prjevlLst.size()>0){
						for(KstarPrjEvl evl: prjevlLst){
							ProcessInstance pi = processService.get(evl.getEvlRs());
							if(pi!=null){
								processService.close(pi.getId(), user.getParticipant(), "合同作废");
							}
						}
					}
				}else{
					ProcessInstance pi = processService.get(instId);
					if(pi != null ){
						processService.close(pi.getId(), user.getParticipant(), "合同作废");
					}
				}
			}

			// 合同状态更新
			LovMember stdLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "09");//09	已废弃
	//		changeService.updateContrChgStatus(contrId, stdLov.getId());
			contr.setContrStat(stdLov.getId());
			contr.setIsValid("0");
		}else{
			throw new AnneException("该状态下合同无法作废");
		}

	}

	/**
	 * 终止合同流程
	 * @param contrId
	 * @param user
	 */
	private void contractXflowClose(String contrId,UserObject  user){
		Contract contr = get(contrId);
		String instId = contr.getProcessInstanceId();

		List<KstarPrjEvl> prjevlLst	=queryContrPrjevlListByContrId(contrId);
		if(prjevlLst.size()>0){
			for(KstarPrjEvl evl: prjevlLst){
				ProcessInstance pi = processService.get(evl.getEvlRs());
				if(pi!=null){
					processService.close(pi.getId(), user.getParticipant(), "合同修订");
					LovMember lov = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "05");//05	已销毁
					evl.setEvlSt(lov.getId());
					evl.setEvlMm(user.getPosition().getName());
					evl.setEvlSg("合同修订-销毁");
					baseDao.update(evl);
				}
			}
		}

		ProcessInstance pi = processService.get(instId);
		if(pi != null ){
			processService.close(pi.getId(), user.getParticipant(), "合同修订");
		}
	}


	@Override
	public KstarPrjLst getPrjLstByContractID(String contractId,String lineNum){
		List<Object> args = new ArrayList<Object>();
		args.add(contractId);
		args.add(lineNum);
		List<KstarPrjLst> prjlst= baseDao.findEntity(" from KstarPrjLst where quotCode = ? and lineNum = ?",args.toArray());
		if(prjlst != null && prjlst.size() > 0 ){
			return prjlst.get(0);
		}
		return null;
	}

	@Override
	public void createPrjevlForContr(String contrId) throws AnneException{
		// 合同评审时系统自动创建售前、售后、商务评审
//		E01	售前评审
//		E02	售后评审
//		E03	商务评审

		String businessId = contrId; //StringUtil.getUUID();
		String typ = getContrBusinessTypeById(contrId);

		LovMember lov1EvlTp = lovMemberService.getLovMemberByCode("CONTRACT_EVLTYPE", "E01");// E01	售前评审
		LovMember lov2EvlTp = lovMemberService.getLovMemberByCode("CONTRACT_EVLTYPE", "E02");// E02	售后评审
		LovMember lov3EvlTp = lovMemberService.getLovMemberByCode("CONTRACT_EVLTYPE", "E03");// E03	商务评审
		LovMember lovReview = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "01");  // 未启动

		KstarPrjEvl prjevl1 = new KstarPrjEvl();
		prjevl1.setSeqno(1);
		prjevl1.setQuotCode(businessId);
		prjevl1.setEvlTyp(lov1EvlTp.getId());
		prjevl1.setEvlSt(lovReview.getId());
		prjevl1.setSbmDt(new Date());
//		String typ = IConstants.CONTR_STAND;
		savePrjevl(prjevl1, typ);

		KstarPrjEvl prjevl2 = new KstarPrjEvl();
		prjevl2.setSeqno(1);
		prjevl2.setQuotCode(businessId);
		prjevl2.setEvlTyp(lov2EvlTp.getId());
		prjevl2.setEvlSt(lovReview.getId());
		prjevl2.setSbmDt(new Date());
//		String typ = IConstants.CONTR_STAND;
		savePrjevl(prjevl2, typ);

		KstarPrjEvl prjevl3 = new KstarPrjEvl();
		prjevl3.setSeqno(1);
		prjevl3.setQuotCode(businessId);
		prjevl3.setEvlTyp(lov3EvlTp.getId());
		prjevl3.setEvlSt(lovReview.getId());
		prjevl3.setSbmDt(new Date());
//		String typ = IConstants.CONTR_STAND;
		savePrjevl(prjevl3, typ);

	}

	/*
	 * 通过客户ID获取客户的所有上下级
	 *
	 */
	@Override
	public String getAllRelationCustByCustId(String custId) throws AnneException{
		String custAllCodeStr = "";
		String custAllIdStr = "";
		CustomInfo cust = customerService.getCustomInfo(custId);
//		String custCode = cust.getCustomCode();
//		LovMember lovCust = lovMemberService.getLovMemberByCode("CUSTOMORGTREE", custCode);
//		if(lovCust != null){
//			String codePathStr = lovCust.getCodePath();
//			String[] pList = codePathStr.split("/");
//			if(pList.length > 2){
//				for(String cu : pList){
//					if(cu != null && !cu.equalsIgnoreCase("")){
//						custAllCodeStr += "'" + cu + "',";
//					}
//				}
//			}
//
//		Condition condition = new Condition();
//		condition.getFilterObject().addOrCondition("codePath", "like", "%"+custCode+"%");
//		List<LovMember> childcust = lovMemberService.getList(condition);

		String hql = "";
		hql = " from LovMember t where t.groupCode='CUSTOMORGTREE' and t.codePath like '%"+cust.getCustomCode()+"%' and t.level = (" +
			  " select max(l.level)   from LovMember l  where l.groupCode='CUSTOMORGTREE' and l.codePath like '%"+cust.getCustomCode()+"%' )";
//		sql = "select * from sys_t_lov_member t where t.group_code = 'CUSTOMORGTREE'  and t.code_path like '%"+custCode+"%' and  t.lov_level = ( " +
//			  " select max(l.lov_level) lov_level from  sys_t_lov_member l where l.group_code = 'CUSTOMORGTREE'  and l.code_path like '"+custCode+"' )";
		List<LovMember> list = baseDao.findEntity(hql);
		if(list.size()>0){
			LovMember lovCust = list.get(0);
			if(lovCust != null){
				String codePathStr = lovCust.getCodePath();
				String[] pList = codePathStr.split("/");
//				if(pList.length > 2){
					for(String cu : pList){
						if(cu != null && !cu.equalsIgnoreCase("")){
							custAllCodeStr += "'" + cu + "',";
						}
					}
//				}
			}
			if(custAllCodeStr.length()>2){
				custAllCodeStr = custAllCodeStr.substring(0, custAllCodeStr.length()-1);
				String hql1 = "";
				hql1 = " from CustomInfo t where t.customCode in (" + custAllCodeStr + ") " ;
				List<CustomInfo> custAllList = baseDao.findEntity(hql1);
				for(CustomInfo i : custAllList){
					custAllIdStr += "'"+ i.getId() + "',";
				}
				if(custAllIdStr.length() > 0){
					custAllIdStr = custAllIdStr.substring(0, custAllIdStr.length()-1);
				}
			}
		}else{
			custAllIdStr ="'"+custId+"'";
		}
		return custAllIdStr;
	}


	/*
	 * 获取报价单关联合同的数量
	 *
	 */
	@Override
	public Long getContractNumtByQutoNo(String qutoNo) throws AnneException{
		String hql = "select count(*) from Contract c where c.contrStat <> ? and c.qutoNo = ?";
		String conStaStr = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "09").getId(); //09	已废弃
		List<Object> args = new ArrayList<Object>();
		args.add(conStaStr);
		args.add(qutoNo);
		Long count = baseDao.findUniqueEntity(hql,args.toArray());
		return count;
	}

	/*
	 * 获取合同管理专员， 作为合同评审中综合评审流程的发起人
	 *
	 *
	 */
	public UserObject getContrMgrEmp(String empNo) throws AnneException{
//		String empId = "13202"; //  合同管理专员 员工ID :ff8080815bb92742015bb9c515dd000f	张家芳	13202

		Employee emp =  employeeService.getEmployeeByNo(empNo);
		if(emp== null){
			throw new AnneException("未找到合同管理专员，流程启动失败");

		}else {

			List<LovMember> posList = corePermissionService.getUserPositionList(emp.getId());
			Position pos = new Position(posList.get(0));
			LovMember org = corePermissionService.getOrg(pos.getId());

	//		Participant par = new Participant(emp.getId(),emp.getName(),"EMPLOYEE");
			UserObject user = new UserObject(emp.getId(),pos.getId(),org.getId());
			user.setEmployee(emp);
			user.setPosition(posList.get(0));
			user.setOrg(org);
			return user;
		}
	}

	@Override
	public void startContrSumReviewFlow(String contrId, String typ) throws AnneException{

		// 合同审批状态
		LovMember unStartStatus = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "01");//未启动
		LovMember ingStatus = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "02");//审批中
		LovMember comStatus = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "03");//已审批
		LovMember rejStatus = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "04");//已驳回
		LovMember desStatus = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "05");//05	已销毁

		// 合同状态更新
		LovMember staLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "04");//待合同书评审
		// 合同变更状态更新
		LovMember chgLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "05");//05	待签订
		// 合同变更状态更新
		LovMember piLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "07");//07	已签订待商务下单
		String flg="true";
		String hasSumFlowFlg = "false";
		String sumFlowEvlId = "";
		String newSumFlowEvl = "false";
		String ingSumFlowEvl = "false";//审批中
		int c = 0;
		List<KstarPrjEvl> prjLst = queryContrPrjevlListByContrId(contrId);
		for(KstarPrjEvl pevl : prjLst){
			// 合同状态更新
			LovMember peLov = lovMemberService.get( pevl.getEvlTyp());

			if(peLov.getCode().equalsIgnoreCase("E08")){  //E08	决策评审
				continue;
			}else if(peLov.getCode().equalsIgnoreCase("E07")){  // E07	综合评审
//				flg="false";
				if( newSumFlowEvl.equalsIgnoreCase("false") && (pevl.getEvlSt().equalsIgnoreCase(unStartStatus.getId()) ) ){//未启动
					hasSumFlowFlg = "true";
					newSumFlowEvl= "true";
					sumFlowEvlId = pevl.getId();
				}else if( newSumFlowEvl.equalsIgnoreCase("false") && (pevl.getEvlSt().equalsIgnoreCase(comStatus.getId()) || pevl.getEvlSt().equalsIgnoreCase(rejStatus.getId()) || pevl.getEvlSt().equalsIgnoreCase(desStatus.getId())  )){
					hasSumFlowFlg = "true";
				}else if( ingSumFlowEvl.equalsIgnoreCase("false") && (pevl.getEvlSt().equalsIgnoreCase(ingStatus.getId()))  ){
					ingSumFlowEvl ="true";
				}
//				break;
				continue;
			}else{
				if(pevl.getEvlSt().equalsIgnoreCase(comStatus.getId()) || pevl.getEvlSt().equalsIgnoreCase(rejStatus.getId())){
					c =+ 1;
					continue;
				}else{
					flg="false";
					break;
				}
			}
		}

		/**
		 * 如果合同评审中，无审批中或已驳回,自动开启综合评审
		 */
		String redaySumFlowEvl = "true";
		for(KstarPrjEvl pevl : prjLst) {
			LovMember peLov = lovMemberService.get( pevl.getEvlTyp());
			if(pevl.getEvlSt().equalsIgnoreCase(ingStatus.getId())||pevl.getEvlSt().equalsIgnoreCase(rejStatus.getId())) {
				redaySumFlowEvl = "false";
			}
		}
		
		if((flg.equalsIgnoreCase("true") && c >0 && hasSumFlowFlg.equalsIgnoreCase("false") && ingSumFlowEvl.equalsIgnoreCase("false")) || (flg.equalsIgnoreCase("true") && hasSumFlowFlg.equalsIgnoreCase("true") && newSumFlowEvl.equalsIgnoreCase("false") )||(redaySumFlowEvl.equalsIgnoreCase("true"))){

			String ctyp = typ.equalsIgnoreCase("")? getContrBusinessTypeById(contrId) : typ;
			LovMember lov7EvlTp = lovMemberService.getLovMemberByCode("CONTRACT_EVLTYPE", "E07");// E07	综合评审

			LovMember lovReview = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "01");  // 未启动

			KstarPrjEvl prjevl7 = new KstarPrjEvl();
			prjevl7.setSeqno(1);
			prjevl7.setQuotCode(contrId);
			prjevl7.setEvlTyp(lov7EvlTp.getId());
			prjevl7.setEvlSt(lovReview.getId());
			prjevl7.setSbmDt(new Date());
//			String typ = IConstants.CONTR_STAND;
			savePrjevl(prjevl7, ctyp);

			String[] ids = new String[1];
			ids[0]=prjevl7.getId();

			UserObject user =getContrMgrEmp("13202");
			startContractReviewProcess(user, ids,ctyp);

		}else if((flg.equalsIgnoreCase("true") && c ==0 && hasSumFlowFlg.equalsIgnoreCase("true") && newSumFlowEvl.equalsIgnoreCase("true") )){

			String ctyp = typ.equalsIgnoreCase("")? getContrBusinessTypeById(contrId) : typ;
			String[] ids = new String[1];
			ids[0]=sumFlowEvlId;

			UserObject user =getContrMgrEmp("13202");
			startContractReviewProcess(user, ids,ctyp);

		}

	}

	@Override
	public void updateContrPrjlstMaterCodeByPrdId(String proId) throws AnneException {
		StringBuffer updateSql = new StringBuffer();
		updateSql.append("update CRM_T_PRJ_LST pl set (pl.c_mater_code , pl.c_pro_desc )=( " +
			" select p.c_mater_code, p.c_pro_desc from crm_t_product_basic p where pl.c_pro_id = p.c_pid  ) where  pl.c_pro_id= '"+ proId +"' "
			 );
		baseDao.executeSQL(updateSql.toString());
	}

	@Override
	public List<Contract> getContractListForAutocomp(Condition condition) throws AnneException {
		FilterObject filterObject = condition.getFilterObject(Contract.class);
		filterObject.addOrderBy("contrNo", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.findEntity(hqlObject.getHql(),hqlObject.getArgs(),0,50);
	}


	/**
	 * 根据订单明细数量和合同明细数量判断合同是否已全部下单
	 * @param orderId
	 * @param contractId
	 * @throws AnneException
	 */
	public void updateContractStatByAmt(String orderId,String contractId) throws AnneException {

		if (StringUtil.isNullOrEmpty(orderId) || StringUtil.isNullOrEmpty(contractId)) {
			return;
		}

		Contract contract = get(contractId);
		if (contract == null || StringUtil.isNullOrEmpty(contract.getContrStat())) {
			return;
		}

		OrderHeader orderHeader = baseDao.get(OrderHeader.class, orderId);
		if(orderHeader == null){
			return;
		}

		LovMember lv07 = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "07");  // 已签订待商务下单
		LovMember lv08 = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "08");  // 已签订商务已下单
		LovMember lv13 = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "13");  // 审批通过商务已下单
		LovMember lv12 = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "12");  // 审批通过待商务下单

		List<KstarPrjLst> prjLsts = baseDao.findEntity("from KstarPrjLst where quotCode = ? ",contractId);

		if (prjLsts.size() < 1) {
			return;
		}

		Map<String, BigDecimal> contractMap = prjLstsMap(prjLsts);
		Map<String, BigDecimal> orderMap = linesMap(contract.getContrNo(),prjLsts);

		// 已签订待商务下单,已全部下单
		if (contract.getContrStat().equals(lv07.getId()) || contract.getContrStat().equals(lv12.getId())) {
			boolean isEnd = false;
			for (String proId : contractMap.keySet()) {
				if (orderMap.get(proId) == null) {
					isEnd = true;
					break;
				}else if(contractMap.get(proId).compareTo(orderMap.get(proId)) != 0){
					isEnd = true;
					break;
				}
			}
			if (!isEnd) {
				if(contract.getContrStat().equals(lv07.getId())){
					contract.setContrStat(lv08.getId());
				}else{
					contract.setContrStat(lv13.getId());
				}
				contract.setOrderedAmt(orderHeader.getAmount().doubleValue());
				contract.setOrderNo(orderHeader.getId());
				baseDao.update(contract);
			}

		}else if(contract.getContrStat().equals(lv08.getId()) || contract.getContrStat().equals(lv13.getId())){
			// 已签订商务已下单,部分下单
			boolean isEnd = false;
			for (String proId : contractMap.keySet()) {
				if (orderMap.get(proId) == null) {
					isEnd = true;
					break;
				}else if(contractMap.get(proId).compareTo(orderMap.get(proId)) != 0){
					isEnd = true;
					break;
				}
			}
			if (isEnd) {
				if (contract.getContrStat().equals(lv08.getId())) {
					contract.setContrStat(lv07.getId());
				}else{
					contract.setContrStat(lv12.getId());
				}
				contract.setOrderedAmt(orderHeader.getAmount().doubleValue());
				contract.setOrderNo(orderHeader.getId());
				baseDao.update(contract);
			}
		}

	}


	private Map<String, BigDecimal> prjLstsMap(List<KstarPrjLst> prjLsts){
		Map<String, BigDecimal> map = new HashMap<>();
		for (KstarPrjLst kstarPrjLst : prjLsts) {
			if (map.get(kstarPrjLst.getProId()) == null) {
				map.put(kstarPrjLst.getProId(), new BigDecimal(MathUtils.sub(kstarPrjLst.getAmt(), kstarPrjLst.getVeriedNum())));
			}else{
				map.put(kstarPrjLst.getProId(), map.get(kstarPrjLst.getProId()).add(new BigDecimal(MathUtils.sub(kstarPrjLst.getAmt(), kstarPrjLst.getVeriedNum()))));
			}
		}
		return map;
	}


	private Map<String, BigDecimal> linesMap(String contrNo,List<KstarPrjLst> lines){
		Map<String, BigDecimal> map = new HashMap<>();
		for (KstarPrjLst line : lines) {
			OrderQuantityVo quantityVo = orderService.getContractOrderQty(contrNo,line.getLineNum());
			if (quantityVo == null) {
				continue;
			}
			if (map.get(line.getProId()) == null) {
				map.put(line.getProId(), new BigDecimal(String.valueOf(quantityVo.getProQty())));
			}else{
				map.put(line.getProId(), map.get(line.getProId()).add(new BigDecimal(String.valueOf(quantityVo.getProQty()))));
			}
		}
		return map;
	}

	/**
	 * 根据合同ID获取全部的审批历史明细
	 * @param contrId
	 * @return
	 */
	@Override
	public List<HistoryActivityInstance> getHistoryActivityInstanceAllByContrId(String contrId){

		List<HistoryActivityInstance> historyAll = new ArrayList<>();

        Map<String,String> varmap = new HashMap<>();
        varmap.put("businessKey", contrId);
        IPage p = historyService.findProcessInstance(varmap,1000,1);
        if (p.getList() == null) {
            return historyAll;
        }

        for (Object o : p.getList()) {
            HistoryProcessInstance historyProcess = (HistoryProcessInstance) o;
            List<HistoryActivityInstance> historyActivitys = historyService.findHistoryActivityInstance(historyProcess.getId());
            for (HistoryActivityInstance activity : historyActivitys) {
                if (activity.getActivityType()!=null && !"decision".equals(activity.getActivityType())) {
                    String[] split = activity.getProcessDefinitionName().split("-");
                    if (split.length > 0) {
                        activity.setProcessDefinitionName(split[split.length - 1]);
                    }
                    historyAll.add(activity);
                }
            }
        }

		return historyAll;
	}

	/**
	 * 合同申请
	 * 工程清单导出
	 */
	public List<List<Object>> exportPrjlstList(PageCondition condition) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<List<Object>> lstOut = new ArrayList<List<Object>>();
		addPrjlstHead(lstOut);
		List<KstarPrjLst> prjlstList = (List<KstarPrjLst>) this.queryPrjLst(condition).getList();
		int i=1;
		for(KstarPrjLst kp: prjlstList){
			List<Object> lstIn = new ArrayList<Object>();
			lstIn.add(i);
			lstIn.add(kp.getPrdNm());
			lstIn.add(kp.getSalCtg());
			lstIn.add(kp.getPrdCtg());
			lstIn.add(kp.getPrdTyp());
			lstIn.add(kp.getMaterCode());
			lstIn.add(kp.getPrdUnt());
			lstIn.add(kp.getPrdSprc());
			lstIn.add(kp.getGoldPrc());
			lstIn.add(kp.getPrdPrc());
			lstIn.add(kp.getAmt());
			lstIn.add(kp.getTtlUnt());
			lstIn.add(kp.getVeriedNum());
			lstIn.add(kp.getNotVeriNum());
			lstIn.add(kp.getApplyDiscount());
			lstIn.add(kp.getApplyPrc());
			lstIn.add(kp.getApproveDiscount());
			lstIn.add(kp.getApprovePrc());
			lstIn.add(kp.getNotes());
			lstIn.add(kp.getProDesc());

			lstOut.add(lstIn);
			i++;
		}
		return lstOut;
	}

	private void  addPrjlstHead(List<List<Object>> lstOut){
		List<Object> lstHead = new ArrayList<Object>();
		lstHead.add("序号");
		lstHead.add("产品名称");
		lstHead.add("销售产品分类");
		lstHead.add("CRM产品分类");
		lstHead.add("产品型号");
		lstHead.add("物料号");
		lstHead.add("单位");
		lstHead.add("公开价格");
		lstHead.add("金牌价格");
		lstHead.add("合同价格");
		lstHead.add("数量");
		lstHead.add("单品总金额");
		lstHead.add("核销数量");
		lstHead.add("可下单数量");
		lstHead.add("申请折扣(%)");
		lstHead.add("申请价格");
		lstHead.add("批复折扣(%)");
		lstHead.add("批复价格");
		lstHead.add("备注");
		lstHead.add("产品描述");
		lstOut.add(lstHead);
	}

	/**
	 * 借货申请
	 * 工程清单导出
	 */
	public List<List<Object>> loanPrjlstExport(PageCondition condition) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<List<Object>> lstOut = new ArrayList<List<Object>>();
		addLoanPrjlstHead(lstOut);
		List<KstarPrjLst> prjlstList = (List<KstarPrjLst>) this.queryPrjLst(condition).getList();
		int i=1;
		for(KstarPrjLst kp: prjlstList){
			List<Object> lstIn = new ArrayList<Object>();
			lstIn.add(i);
			lstIn.add(kp.getPrdNm());
			lstIn.add(kp.getPrdCtg());
			lstIn.add(kp.getPrdTyp());
			lstIn.add(kp.getMaterCode());
			lstIn.add(kp.getPrdUnt());
			lstIn.add(kp.getPrdSprc());
			lstIn.add(kp.getPrdPrc());
			lstIn.add(kp.getAmt());
			lstIn.add(kp.getTtlUnt());
			lstIn.add(kp.getNotVeriNum());
			lstIn.add(kp.getVeriedNum());
			lstIn.add(kp.getNotes());
			lstIn.add(kp.getProDesc());

			lstOut.add(lstIn);
			i++;
		}
		return lstOut;
	}

	private void  addLoanPrjlstHead(List<List<Object>> lstOut){
		List<Object> lstHead = new ArrayList<Object>();
		lstHead.add("序号");
		lstHead.add("产品名称");
		lstHead.add("产品分类");
		lstHead.add("产品型号");
		lstHead.add("物料号");
		lstHead.add("单位");
		lstHead.add("定价");
		lstHead.add("合同单价");
		lstHead.add("数量");
		lstHead.add("单品总金额");
		lstHead.add("未核销数量");
		lstHead.add("已核销数量");
		lstHead.add("备注");
		lstHead.add("产品描述");
		lstOut.add(lstHead);
	}

	/**
	 * 合同变更
	 * 工程清单导出
	 */
	public List<List<Object>> chgPrjlstExport(PageCondition condition) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<List<Object>> lstOut = new ArrayList<List<Object>>();
		addChgPrjlstHead(lstOut);
		List<KstarPrjLst> prjlstList = (List<KstarPrjLst>) this.queryPrjLst(condition).getList();
		int i=1;
		for(KstarPrjLst kp: prjlstList){
			List<Object> lstIn = new ArrayList<Object>();
			lstIn.add(i);
			lstIn.add(kp.getPrdNm());
			lstIn.add(kp.getPrdCtg());
			lstIn.add(kp.getPrdTyp());
			lstIn.add(kp.getMaterCode());
			lstIn.add(kp.getPrdUnt());
			lstIn.add(kp.getPrdSprc());
			lstIn.add(kp.getPrdPrc());
			lstIn.add(kp.getVeriedNum());
			lstIn.add(kp.getAmt());
			lstIn.add(kp.getTtlUnt());
			lstIn.add(kp.getNewPrdPrc());
			lstIn.add(kp.getNewAmt());
			lstIn.add(kp.getNewTtlUnt());
			lstIn.add(kp.getUpdtFlag());
			lstIn.add(kp.getUpdatType());
			lstIn.add(kp.getNotes());
			lstIn.add(kp.getProDesc());
			lstOut.add(lstIn);
			i++;
		}
		return lstOut;
	}

	private void  addChgPrjlstHead(List<List<Object>> lstOut){
		List<Object> lstHead = new ArrayList<Object>();
		lstHead.add("序号");
		lstHead.add("产品名称");
		lstHead.add("产品分类");
		lstHead.add("产品型号");
		lstHead.add("物料号");
		lstHead.add("单位");
		lstHead.add("定价");
		lstHead.add("合同单价");
		lstHead.add("核销数量");
		lstHead.add("数量");
		lstHead.add("单品总金额");
		lstHead.add("新合同单价");
		lstHead.add("新数量");
		lstHead.add("新单品总金额");
		lstHead.add("是否变更");
		lstHead.add("变更类型");
		lstHead.add("备注");
		lstHead.add("产品描述");
		lstOut.add(lstHead);
	}

	/**
	 * 报价列表
	 * 工程清单导出
	 */
	public List<List<Object>> quotPrjlstExport(List<KstarPrjLst> prjlstList) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<List<Object>> lstOut = new ArrayList<List<Object>>();
		addQuotPrjlstHead(lstOut);
		int i=1;
		for(KstarPrjLst kp: prjlstList){
			List<Object> lstIn = new ArrayList<Object>();
			lstIn.add(i);
			lstIn.add(kp.getPrdNm());
			lstIn.add(kp.getSalCtg());
			lstIn.add(kp.getPrdCtg());
			lstIn.add(kp.getPrdTyp());
			lstIn.add(kp.getMaterCode());
			lstIn.add(kp.getPrdUnt());
			lstIn.add(kp.getPrdSprc());
			lstIn.add(kp.getGoldPrc());
			lstIn.add(kp.getApplyDiscount());
			lstIn.add(kp.getApplyPrc());
			lstIn.add(kp.getApproveDiscount());
			lstIn.add(kp.getApprovePrc());
			lstIn.add(kp.getAmt());
			lstIn.add(kp.getTtlUnt());
			lstIn.add(kp.getProDesc());
			lstIn.add(kp.getNotes());
			lstOut.add(lstIn);
			i++;
		}
		return lstOut;
	}

	private void  addQuotPrjlstHead(List<List<Object>> lstOut){
		List<Object> lstHead = new ArrayList<Object>();
		lstHead.add("序号");
		lstHead.add("产品名称");
		lstHead.add("销售产品分类");
		lstHead.add("CRM产品类别");
		lstHead.add("产品型号");
		lstHead.add("物料号");
		lstHead.add("单位");
		lstHead.add("公开价格");
		lstHead.add("金牌价格");
		lstHead.add("申请折扣（%）");
		lstHead.add("申请价格");
		lstHead.add("批复折扣（%）");
		lstHead.add("批复价格");
		lstHead.add("数量");
		lstHead.add("单品总金额");
		lstHead.add("产品描述");
		lstHead.add("备注");
		lstOut.add(lstHead);
	}

	/**
	 * PI申请
	 * 工程清单导出
	 */
	public List<List<Object>> piPrjlstExport(PageCondition condition) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<List<Object>> lstOut = new ArrayList<List<Object>>();
		addPiPrjlstHead(lstOut);
		List<KstarPrjLst> prjlstList = (List<KstarPrjLst>) this.queryPrjLst(condition).getList();
		int i=1;
		for(KstarPrjLst kp: prjlstList){
			List<Object> lstIn = new ArrayList<Object>();
			lstIn.add(i);
			lstIn.add(kp.getPrdNm());
			lstIn.add(kp.getPrdCtg());
			lstIn.add(kp.getPrdTyp());
			lstIn.add(kp.getMaterCode());
			lstIn.add(kp.getProDesc());
			lstIn.add(kp.getPrdUnt());
			lstIn.add(kp.getPrdPrc());
			lstIn.add(kp.getAmt());
			lstIn.add(kp.getTtlUnt());
			lstIn.add(kp.getNotes());
			lstOut.add(lstIn);
			i++;
		}
		return lstOut;
	}

	private void  addPiPrjlstHead(List<List<Object>> lstOut){
		List<Object> lstHead = new ArrayList<Object>();
		lstHead.add("序号");
		lstHead.add("产品名称");
		lstHead.add("产品分类");
		lstHead.add("产品型号");
		lstHead.add("物料号");
		lstHead.add("产品描述");
		lstHead.add("单位");
		lstHead.add("合同单价");
		lstHead.add("数量");
		lstHead.add("单品总金额");
		lstHead.add("备注");
		lstOut.add(lstHead);
	}

	@Override
	public String checkContrStat(String id, UserObject user) {
		String flag = "N";
		Map<String,String> varmap = new HashMap<>();
		varmap.put("businessKey", id);
		IPage historyPage = historyService.findProcessInstance(varmap,500,1);
		List<HistoryProcessInstance> hpiList =  (List<HistoryProcessInstance>) historyPage.getList();
		HistoryProcessInstance haiHead = new HistoryProcessInstance();
		for(HistoryProcessInstance hai:hpiList) {
			String status = hai.getStatus();
			if("running".equals(status)) {
				haiHead = hai;
			}
		}
		
		List<HistoryActivityInstance> historyList = historyService.findHistoryActivityInstance(haiHead.getId());
		
		HistoryActivityInstance haiRuning = new HistoryActivityInstance();
		String employeeId = user.getEmployee().getId();
		for(HistoryActivityInstance hai:historyList) {
			String status = hai.getStatus();
			if("running".equals(status)) {
				haiRuning = hai;
			}
		}
		if(StringUtil.isNullOrEmpty(haiRuning.getEndTime())&&StringUtil.isNullOrEmpty(haiRuning.getOperatorName())&&"POSITION".equals(haiRuning.getAssigneeType())) {
			String assigneeid = haiRuning.getAssigneeId();
			
			
			List<Object> args = new ArrayList<Object>();
			StringBuffer hql = new StringBuffer();
			hql.append("select e from Employee e,UserPermission up,LovMember p ,LovMember po where po.id = p.optTxt1  and p.id = up.memberId and e.id = up.userId and up.type = 'P' ");
			if(assigneeid !=null){
				hql.append(" and po.path like ?  ");
				args.add("%" + assigneeid + "%");
			}
			List<Employee> employeeList = baseDao.findEntity(hql.toString(), args.toArray());
			if(employeeList.size()>0) {
				for(Employee employee:employeeList) {
					if(employee.getId().equals(employeeId)) {
						flag = "Y"; 
					}
				}
			}
		}else {
			if(employeeId.equals(haiRuning.getOperatorId())||employeeId.equals(haiRuning.getAssigneeId())) {
				flag = "Y"; 
			}
		}
		return flag;
	}

	/**
	 * 无合同核销——工程清单保存
	 */
	@Override
	public void saveEliminateLines(String listStr, String contrId, String eliminateid,ContractEliminate contractEliminate) throws AnneException {
		List<KstarPrjLst> lines = JSON.parseArray(listStr, KstarPrjLst.class);
		BigDecimal count = contractEliminateService.queryEliminateHeadersCountByid(eliminateid);
		
		for(KstarPrjLst prjLst: lines){
			prjLst.setQuotCode(eliminateid);
			prjLst.setContractEliminateId(eliminateid);
			if(count.intValue()<1) {
				prjLst.setId(null);
			}
			prjLst.setCType(IConstants.CONTR_LOAN_ELIMINATE);
			Contract contract = this.get(contrId);
			prjLst.setLineNum(this.getLineNum(contrId));
			prjLst.setBuzCode(contract.getContrNo());
			baseDao.saveOrUpdate(prjLst);

		}
		baseDao.saveOrUpdate(contractEliminate);
	}

	@Override
	public IPage queryEliminatePrjLst(PageCondition condition) {
		String quotCode = condition.getStringCondition("contrId");
		String eliminateId = condition.getStringCondition("eliminateId");
		String searchKey = condition.getStringCondition("searchKey");

		StringBuffer hql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		
		List<ContractEliminate> contractEliminateList = contractEliminateService.queryEliminateHeadersByid(eliminateId);
		
		if(contractEliminateList.size()>0) {
			hql.append("select lst from KstarPrjLst lst  ");
			hql.append(" where lst.contractEliminateId = ? and lst.CType = 'CONTR_LOAN_ELIMINATE' ");
			args.add(eliminateId);
		}else {
			hql.append("select lst from KstarPrjLst lst where lst.quotCode = ? ");
			hql.append(" and lst.contractEliminateId is null");
			args.add(quotCode);
		}
		

		hql.append(" order by lst.prdCtgid, CAST(lst.lineNum as integer) ");
		IPage page = baseDao.search(hql.toString(),args.toArray(),condition.getRows(), condition.getPage());
		return page;
	}

	
	@Override
	public void deleteEliminatePrjLst(String id) {
		
		ContractEliminate  contractEliminate = baseDao.get(ContractEliminate.class, id);
			
		StringBuffer hql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		hql.append("select lst from KstarPrjLst lst where lst.quotCode = ? and lst.contractEliminateId = ? ");
		args.add(contractEliminate.getContractId());
		args.add(contractEliminate.getId());
		List<KstarPrjLst> KstarPrjLsts= baseDao.findEntity(hql.toString(), args.toArray());
		baseDao.deleteAll(KstarPrjLsts);
	}
	
	@Override
	public String checkVeriedNum(String listStr, String contrId, String eliminateid)throws AnneException {
		StringBuilder CheckReport = new StringBuilder();
		List<KstarPrjLst> lines = JSON.parseArray(listStr, KstarPrjLst.class);
		
		StringBuffer eliminateHql = new StringBuffer();
		List<Object> eliminateArgs = new ArrayList<Object>();
		eliminateHql.append("select lst.C_PRO_ID,sum(lst.C_VERIED_NUM) from CRM_T_PRJ_LST lst where  lst.C_ELIMINATE_ID is not null ");
		eliminateHql.append(" and lst.C_ELIMINATE_ID  in (select el.C_ID from CRM_T_Eliminate el where el.C_CONTRACT_ID = ? and el.C_ID != ?  and (el.C_ELIMINATE_TYPE = ? or el.C_ELIMINATE_TYPE = ? or el.C_ELIMINATE_TYPE = ? or el.C_ELIMINATE_TYPE = ?)) ");
		LovMember lovReview01 = lovMemberService.getLovMemberByCode("CONTRACTELIMINATE", "01");//新建
		LovMember lovReview02 = lovMemberService.getLovMemberByCode("CONTRACTELIMINATE", "02");//审批中
		LovMember lovReview03 = lovMemberService.getLovMemberByCode("CONTRACTELIMINATE", "03");//审批完成
		LovMember lovReview04 = lovMemberService.getLovMemberByCode("CONTRACTELIMINATE", "04");//驳回
		eliminateArgs.add(contrId);
		eliminateArgs.add(eliminateid);
		eliminateArgs.add(lovReview01.getId());
		eliminateArgs.add(lovReview02.getId());
		eliminateArgs.add(lovReview03.getId());
		eliminateArgs.add(lovReview04.getId());
		eliminateHql.append(" group by lst.C_PRO_ID,lst.C_PRD_CTGID order by lst.C_PRD_CTGID ");
		List<Object[]> eliminateList = baseDao.findBySql(eliminateHql.toString(), eliminateArgs.toArray());
		
		for(KstarPrjLst line:lines) {
			for(Object[] kstarPrjLst:eliminateList) {
				if(line.getProId().equals(kstarPrjLst[0])&&kstarPrjLst[1]!=null) {
					line.setContractVeriedNum(line.getContractVeriedNum()+new Double(kstarPrjLst[1].toString()));
				}
			}
		}
		
		
		StringBuffer hql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		hql.append("select lst from KstarPrjLst lst where lst.quotCode = ?  and lst.contractEliminateId is null ");
		args.add(contrId);
		hql.append(" order by lst.prdCtgid, CAST(lst.lineNum as integer) ");
		
		List<KstarPrjLst> kstarPrjLstList = baseDao.findEntity(hql.toString(), args.toArray());
		for(KstarPrjLst line:lines) {
			for(KstarPrjLst kstarPrjLst:kstarPrjLstList) {
				if(line.getProId().equals(kstarPrjLst.getProId())) {
					if(line.getContractVeriedNum()!=null&&line.getContractVeriedNum()>kstarPrjLst.getAmt()) {
						//CheckReport.append("无合同核销的“"+line.getPrdNm()+"”产品申请核销数量合计"+line.getContractVeriedNum()+"大于合同的"+kstarPrjLst.getNotVeriNum()+"数量!");
						throw new AnneException("无合同核销的“"+line.getPrdNm()+"”产品申请核销数量合计"+line.getContractVeriedNum()+"大于合同的"+kstarPrjLst.getAmt()+"数量!");
						//break;
					}
				}
			}
		}
		
		if(kstarPrjLstList.size()!=lines.size()) {
			//CheckReport.append("申请的无合同核销产品与合同中的产品不一致！请关闭当前页，重新申请！");
			throw new AnneException("申请的无合同核销产品与合同中的产品不一致！请关闭当前页，重新申请！");
		}
		
		return CheckReport.toString();
	}

	@Override
	public List<KstarPrjLst> getPrjlstListByContrId(String id) throws AnneException {
		List<Object> args = new ArrayList<Object>();
		args.add(id);
		return baseDao.findEntity(" from KstarPrjLst where quotCode = ? ",args.toArray());
	}

	@Override
	public void updateContrIsPassStatus(String id) throws AnneException {
		StringBuffer updateSql = new StringBuffer();
		updateSql.append(" update crm_t_contr_basic set C_IS_PASS = '1' where C_ID ='"+id+"'");
		baseDao.executeSQL(updateSql.toString());
	}

    @Override
    public double canVerificationNum(String prjlstId) {
		KstarPrjLst kstarPrjLst = this.getKstarPrjLstById(prjlstId);
        double amount = (kstarPrjLst.getAmt() == null) ? 0 :  kstarPrjLst.getAmt();
		//language=HQL
		List<OrderLines> entities = this.baseDao.findEntity("select ol from OrderHeader oh,OrderLines ol " +
				"where oh.id=ol.orderId " +
				"and oh.executeStatus not in ('CLOSED','CANCELLED','TERMINATED') " +
				"and ol.status !='CANCELLED' " +
				"and oh.sourceId=? " +
				"and ol.sourceId=? ",new Object[]{kstarPrjLst.getQuotCode(),kstarPrjLst.getLineNum()});
		if (entities.size() == 0) {
            return amount;
		}

		int orderNum = 0;
		for (OrderLines entity : entities) {
			orderNum += entity.getProQty();
		}
		return (amount - orderNum);
	}

    
	@Override
	public List<List<Object>> reportExportContrLists(PageCondition condition,UserObject user,String[] ids,String typ) throws AnneException{

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<List<Object>> lstOut = new ArrayList<List<Object>>();
		addReportHead(lstOut);
		List<Contract> headerLst= getSelectedContrList(condition,user,ids,typ);
		int i=1;
		for(Contract con: headerLst){
			List<KstarPrjLst> list = getKstarPrjLstByContractId(con.getId());
			List<Object> lstIn = new ArrayList<Object>();
			if(list != null && list.size() > 0){	
				int j = 1;
				for(KstarPrjLst kpj:list){
					List<Object> lstIn1 = new ArrayList<Object>();
					lstIn1.add(i);
					addReportBody(lstIn1,con,sdf);
					lstIn1.add(j);//行号
					lstIn1.add(kpj.getPrdNm());//产品名称
					lstIn1.add(kpj.getSalCtg());//销售产品分类
					lstIn1.add(kpj.getPrdCtg());//CRM产品分类
					lstIn1.add(kpj.getPrdTyp());//产品型号
					lstIn1.add(kpj.getMaterCode());//物料号
					lstIn1.add(kpj.getAmt());//数量
					lstIn1.add(kpj.getPrdPrc());//合同价格
					lstIn1.add(kpj.getTtlUnt());//单品总金额
					lstOut.add(lstIn1);
					j++;
					i++;
				}			
			}else {
				lstIn.add(i);
				addReportBody(lstIn,con,sdf);
				lstIn.add(i);//行号
				lstIn.add("");//产品名称
				lstIn.add("");//销售产品分类
				lstIn.add("");//CRM产品分类
				lstIn.add("");//产品型号
				lstIn.add("");//物料号
				lstIn.add("");//数量
				lstIn.add("");//合同价格
				lstIn.add("");//单品总金额
				lstOut.add(lstIn);
				i++;
			}
		}
		return lstOut;
	}

    @Override
    public void updateHighRiskFlag(String id, String isHighRisk) {
        this.baseDao.executeHQL("update Contract set isHighRisk=? where id=? ", new Object[]{isHighRisk, id});
    }

    private List<KstarPrjLst> getKstarPrjLstByContractId(String id) {
		String hql = " from KstarPrjLst where 1=1 ";
			hql += " and quotCode = ? ";
		return baseDao.findEntity(hql, id);
	}

	private void addReportHead(List<List<Object>> lstOut) {
		// TODO Auto-generated method stub
		List<Object> lstHead = new ArrayList<Object>();
		lstHead.add("序号");
		lstHead.add("合同编号");
		lstHead.add("创建日期");
		lstHead.add("合同类型");
		lstHead.add("客户名称");
		lstHead.add("合同总金额");
		lstHead.add("营销部门");
		lstHead.add("销售部门");
		lstHead.add("销售人员");
		lstHead.add("合同状态");
		lstHead.add("项目名称");
		lstHead.add("合同名称");
		lstHead.add("业务实体");
		lstHead.add("合同原件是否已归档");
		lstHead.add("归档备注");
		lstHead.add("实际签订日期");
		lstHead.add("行号");
		lstHead.add("产品名称");
		lstHead.add("销售产品分类");
		lstHead.add("CRM产品分类");
		lstHead.add("产品型号");
		lstHead.add("物料号");
		lstHead.add("数量");
		lstHead.add("合同价格");
		lstHead.add("单品总金额");
		lstOut.add(lstHead);
	}

	private void addReportBody(List<Object> lstIn,Contract con,SimpleDateFormat sdf){
		lstIn.add(con.getContrNo());//合同编号
		lstIn.add(con.getCreatedAt());//创建日期
		lstIn.add(con.getContrTypeName());//合同类型
		lstIn.add(con.getCustName());//客户名称
		lstIn.add(con.getTotalAmt());//合同总金额
		LovMember mdLov = lovMemberService.get(con.getMarketDept()); // 营销部门
		if(mdLov != null){
			lstIn.add(mdLov.getName());
		}else{
			lstIn.add("");
		}
		LovMember orgLov = lovMemberService.get(con.getOrg());//销售部门
		if(orgLov!=null){
			lstIn.add(orgLov.getName());
		}else{
			lstIn.add("");
		}
		lstIn.add(con.getCreatorName());//销售人员
		lstIn.add(con.getContrStatName());//合同状态
		lstIn.add(con.getProjName());//项目名称
		lstIn.add(con.getContrName());//合同名称
		lstIn.add(con.getBussEnityName());//业务实体
		String isArchive=  con.getIsArchive();//合同原件是否已归档
		if(StringUtil.isNullOrEmpty(isArchive)||"0".equals(isArchive)){
			lstIn.add("否");
		}else{
			lstIn.add("是");
		}
		lstIn.add(con.getAchiveRemark());//归档备注
		if(con.getActSignDate()!=null){			
			lstIn.add(sdf.format(con.getActSignDate()));//实际签订日期
		}else {
			lstIn.add("");
		}
	}
}
