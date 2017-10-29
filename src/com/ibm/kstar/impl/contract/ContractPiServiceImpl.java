package com.ibm.kstar.impl.contract;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.contract.IContractPiService;
import com.ibm.kstar.api.contract.IContractService;
import com.ibm.kstar.api.org.IOrgTeamService;
import com.ibm.kstar.api.quot.IQuotService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.team.ITeamService;
import com.ibm.kstar.entity.bizopp.BusinessOpportunity;
import com.ibm.kstar.entity.contract.ContrAddr;
import com.ibm.kstar.entity.contract.ContrPay;
import com.ibm.kstar.entity.contract.Contract;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.product.ProductPriceHead;
import com.ibm.kstar.entity.quot.*;
import com.ibm.kstar.entity.team.Team;
import com.ibm.kstar.permission.utils.PermissionUtil;
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
import org.xsnake.web.page.PageImpl;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.StringUtil;
import org.xsnake.xflow.api.IProcessService;
import org.xsnake.xflow.api.model.ProcessInstance;
import org.xsnake.xflow.api.workflow.IXflowProcessServiceWrapper;

import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author Joe
 *
 */
@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class ContractPiServiceImpl implements IContractPiService {

	@Autowired
	BaseDao baseDao;

	@Autowired
	IProcessService processService;
	@Autowired
	private ILovMemberService lovMemberService;
	@Autowired
	protected ITeamService teamService;
	@Autowired
	private IContractService contractService;
	@Autowired
	protected IOrgTeamService orgTeamService;
	@Autowired
	IXflowProcessServiceWrapper xflowProcessServiceWrapper;
	
	
	@Override
	public IPage query(PageCondition condition) {
//		FilterObject filterObject = condition.getFilterObject(Contract.class);
//		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
//		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());


		FilterObject fo = condition.getFilterObject(Contract.class);
		HqlObject ho = HqlUtil.getHqlObject(fo);
		String hql_search = ho.getHql();
		String sel_hql = ""; 
		if(hql_search.indexOf("order by")>0){
			sel_hql = hql_search.substring(0, hql_search.indexOf("order by")-1);
		}
		UserObject user = (UserObject)condition.getCondition("userObject");



//		StringBuffer hql = new StringBuffer(" from Contract contr where 1=1 and contr.isValid ='1' ");
		StringBuffer hql = new StringBuffer();
		hql.append(sel_hql);
//		LovMember typeLov = lovMemberService.getLovMemberByCode("CONTRACTTYPE", "PI0101");
		LovMember typeLov = lovMemberService.getLovMemberByCode("CONTRACTTYPE", IConstants.CONTR_PI_0101);
		hql.append(" AND contract.contrType= '" + typeLov.getId()+"'");
		String searchKey = condition.getStringCondition("searchKey");
		if(searchKey != null){
			hql.append(" AND ( (contract.contrNo like '%"+searchKey+"%' ) or ( contract.contrName like '%"+searchKey+"%'))");
		}
		String erpNo = condition.getStringCondition("erpNo");
		if (StringUtil.isNotEmpty(erpNo)) {
			hql.append(" and contract.id in (select sourceId from OrderHeader where erpOrderCode='" + erpNo + "')");
		}
		String perHql= PermissionUtil.getPermissionHQL(null, "contract.createdById", "contract.createdPosId", "contract.createdOrgId", "contract.id", user, IConstants.CONTR_PI);
		hql.append(" AND " +perHql);
		hql.append(" order by contract.createTime desc , contract.contrNo desc, contract.contrVer desc ");
		return baseDao.search(hql.toString(),ho.getArgs(),condition.getRows(), condition.getPage());

	}

	@Override
	public void save(Contract contract,UserObject userObject) throws AnneException {
		if(StringUtil.isEmpty(contract.getContrNo())){
			throw new AnneException("合同单编号不能为空");
		}
		if(StringUtil.isEmpty(contract.getContrVer())){
			throw new AnneException("合同单版本不能为空");
		}

		// 创建字段
		contract.setCreatedById(userObject.getEmployee().getId());
		contract.setCreatedAt(new Date());
		contract.setCreatedPosId(userObject.getPosition().getId());
		contract.setCreatedOrgId(userObject.getOrg().getId());
		// 更新字段
		contract.setUpdatedById(userObject.getEmployee().getId());
		contract.setUpdatedAt(new Date());
		
		contract.setCreateTime(new Date());
		baseDao.save(contract);
		teamService.addPosition(userObject.getPosition().getId(),userObject.getEmployee().getId(), 
				IConstants.CONTR_PI,contract.getId());

		orgTeamService.configPrimaryOrg(contract.getId(), IConstants.CONTR_PI, userObject.getOrg().getId());
		//下单商务专员加入到团队成员
		teamService.addPosition(contract.getOrderPosId(),contract.getOrderer(),IConstants.CONTR_PI,contract.getId());


	}
	

	@Override
	public Contract get(String id) throws AnneException {
		if (id==null){
			return null;
		}
		return baseDao.findUniqueEntity("from Contract where id = ? ", id);
	}

	@Override
	public void update(Contract contract) throws AnneException {
		Contract old = baseDao.get(Contract.class, contract.getId());
		if(old == null){
			throw new AnneException(IContractPiService.class.getName() + " saveQuot : 没有找到要更新的数据");
		}
		
		if(!contract.getId().equals(old.getId())){
			throw new AnneException("合同单ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(contract, old);
				
		//old.setCreateTime(new Time(0));
		baseDao.update(old);
		Long count = baseDao.findUniqueEntity("select count(*) from Contract where C_ID = ? ",contract.getId());
		if(count > 1){
			throw new AnneException("合同单ID已经存在!"); 
		}

		List<Team> teams = baseDao.findEntity("select t from Team t where t.businessType = ? and t.businessId = ? and t.positionId = ? and t.masterEmployeeId = ? " ,
				new Object[]{IConstants.CONTR_PI,contract.getId(),contract.getOrderPosId(),contract.getOrderer()});
		if(teams.size()<=0){
			//下单商务专员加入到团队成员
			teamService.addPosition(contract.getOrderPosId(),contract.getOrderer(),IConstants.CONTR_PI,contract.getId());
		}
	}

	@Override
	public void delete(String id) throws AnneException {
		baseDao.executeHQL(" delete Contract contr where contr.id = ? ",new Object[]{id});
		
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
	public String getContractPiNumber(String dep) throws AnneException {
//		return baseDao.get(KstarMemInfo.class);
		String connum="";
		@SuppressWarnings("deprecation")

			String sql = "{ ? = call CRM_P_CONTRACT_PUB.gen_contract_pi_num(?)}";
//	        CallableStatement sta = conn.prepareCall(sql);
//	        sta.registerOutParameter(1, OracleTypes.VARCHAR);
//            sta.setString(2, dep);
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
		StringBuffer hql = new StringBuffer(" from KstarPrjEvl where CType = '0005' and quotCode = ? ");
		List<Object> args = new ArrayList<Object>();
		args.add(contrId);
		return baseDao.search(hql.toString(),args.toArray(),condition.getRows(), condition.getPage());
		
//		FilterObject filterObject = condition.getFilterObject(ContrPay.class);
//		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
//		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	
	}
	
	@Override
	public void savePrjevl(KstarPrjEvl prjevl) throws AnneException {
		if(StringUtil.isEmpty(prjevl.getQuotCode())){
			throw new AnneException("报价单编号不能为空");
		}
		if(StringUtil.isEmpty(prjevl.getEvlTyp())){
			throw new AnneException("评审类别不能为空");
		}
		prjevl.setCType("0005");
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
	public void deletePrjevl(String prjevlId) throws AnneException {
		baseDao.deleteById(KstarPrjEvl.class, prjevlId);
	}

	private void updateReviewStatus(String id, String status) {
//		02	审批中	8a828dee5a11fc73015a12a80643000c
//		03	已审批	8a828dee5a11fc73015a12a86421000d
//		01	未启动	8a828dee5a11fc73015a12a77ffe000b
//		04	已驳回	8a828dee5a11fc73015a12a8ce48000e

		StringBuffer updateSql = new StringBuffer();
		updateSql.append(" update CRM_T_PRJ_EVL set C_EVL_ST='" + status+ "' where C_PID ='"+id+"'");
		baseDao.executeSQL(updateSql.toString());
	}

	@Override
	public void startContractTrialProcess(UserObject user,String contrId,String typ){
		
		String businessId = contrId; //StringUtil.getUUID();
		Contract contract = get(contrId);
		String tolAmt = contract.getTotalAmtDesc();
		String createrId= contract.getOrderer();
		String createrName=contract.getOrdererName();

		String auditor= contract.getAuditorId();
		String auditorName=contract.getAuditorName();
		
		String application = contractService.getAppnameByType(IConstants.CONTR_PI_TRIAL_PROC,typ);
		String model = lovMemberService.getFlowCodeByAppCode(application);
		String bizNo=contract.getContrNo();
		Date now = new Date();
		SimpleDateFormat dtFmt=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String ti = contractService.getTitleByAppName(application);
		String title =ti+" - "+bizNo+" - "+user.getEmployee().getName()+" - "+dtFmt.format(now);
		String salesCenter = lovMemberService.getSaleCenter(contract.getMarketDept());//lovMemberService.getSaleCenter(user.getOrg().getId());
		Map<String,String> varmap = new HashMap<>();
		varmap.put("title", title);//"合同申请初审流程 - "+user.getEmployee().getName()+" - "+dtFmt.format(now));
		varmap.put("app", application);
		varmap.put("typ", typ);
		varmap.put("SalesCenter", salesCenter);
		varmap.put("Contract_TotalAmt", tolAmt);
		//表单管理的员工
		varmap.put("employeeIdInForm", createrId);
		varmap.put("employeeNameInForm", createrName);

		//表单管理的员工2
		varmap.put("employeeIdInForm2", auditor);
		varmap.put("employeeNameInForm2", auditorName);
		
//		processService.start(model, businessId, new Participant(user.getEmployee().getId(),user.getEmployee().getName(),"EMPLOYEE"),varmap);
		ProcessInstance pi= xflowProcessServiceWrapper.start(model, businessId, user, varmap);
		// 合同初审状态
		LovMember lov = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "02");
//		contractService.updateContractTrialStatus(contrId, lov.getId());
		// 合同状态
		LovMember conlov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "03");  //03	待评审
//		contractService.updateContractStatus(contrId, conlov.getId());
		
		contract.setTrialStat(lov.getId());
		contract.setContrStat(conlov.getId());
		contract.setProcessInstanceId(pi.getId());
		
		
	}
	
	public void startContractTrialProcess1(UserObject user,String contrId,String typ){
		// PI合同提交评审时自动发起“商务评审”、“价格评审”以及“决策评审”
		String businessId = contrId; //StringUtil.getUUID();
		
		LovMember lov3EvlTp = lovMemberService.getLovMemberByCode("CONTRACT_EVLTYPE", "E03");// E03	商务评审
		LovMember lov6EvlTp = lovMemberService.getLovMemberByCode("CONTRACT_EVLTYPE", "E06");// E06	价格评审
		LovMember lov8EvlTp = lovMemberService.getLovMemberByCode("CONTRACT_EVLTYPE", "E08");// E08	决策评审
		LovMember lovReview = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "01");  // 未启动

		KstarPrjEvl prjevl3 = new KstarPrjEvl();
		prjevl3.setSeqno(1);
		prjevl3.setQuotCode(businessId);
		prjevl3.setEvlTyp(lov3EvlTp.getId());
		prjevl3.setEvlSt(lovReview.getId());
		prjevl3.setSbmDt(new Date());
//		String typ = IConstants.CONTR_STAND;
		contractService.savePrjevl(prjevl3, typ);

		KstarPrjEvl prjevl6 = new KstarPrjEvl();
		prjevl6.setSeqno(1);
		prjevl6.setQuotCode(businessId);
		prjevl6.setEvlTyp(lov6EvlTp.getId());
		prjevl6.setEvlSt(lovReview.getId());
		prjevl6.setSbmDt(new Date());
//		String typ = IConstants.CONTR_STAND;
		contractService.savePrjevl(prjevl6, typ);

		KstarPrjEvl prjevl8 = new KstarPrjEvl();
		prjevl8.setSeqno(1);
		prjevl8.setQuotCode(businessId);
		prjevl8.setEvlTyp(lov8EvlTp.getId());
		prjevl8.setEvlSt(lovReview.getId());
		prjevl8.setSbmDt(new Date());
//		String typ = IConstants.CONTR_STAND;
		contractService.savePrjevl(prjevl8, typ);
		
		String[] ids = new String[3];
		ids[0]=prjevl3.getId();
		ids[1]=prjevl6.getId();
		ids[2]=prjevl8.getId();
		contractService.startContractReviewProcess(user, ids,typ);
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

	@Override
	public void reviseContract(UserObject user,String contrId) throws AnneException { 
		Contract contract = new Contract();
		Contract old = get(contrId);
		if(old == null){
			throw new AnneException(IContractPiService.class.getName() + " saveQuot : 没有找到要修订的数据");
		}	
		contract = copyContract(contract,old, user);
		old.setIsValid("0");
		baseDao.update(old);
//		copyAttByContract();
		copyAttByContract(contract, old, user);
		copyMemByContract(contract, old, user);
		copyAddrByContract(contract, old, user);
		copyPayPlanByContract(contract, old, user);
		
	}

	@Override
	public void signUpContract(UserObject user, String contrId) throws AnneException {
		Contract old = baseDao.get(Contract.class, contrId);
		if(old == null){
			throw new AnneException(IContractPiService.class.getName() + " saveQuot : 没有找到要更新的数据");
		}
				
//		BeanUtils.copyPropertiesIgnoreNull(contract, old);
				
		//old.setCreateTime(new Time(0));
		LovMember statLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "01");		
		old.setContrStat(statLov.getId());
		old.setActSignDate(new Date());
		baseDao.update(old);
		Long count = baseDao.findUniqueEntity("select count(*) from Contract where C_ID = ? ",contrId);
		if(count > 1){
			throw new AnneException("合同单ID已经存在!"); 
		}

		// 取消签订时0单价的价格平摊;2017-10-10  黄奇
		// 价格平摊
		// contractService.share0Price(contrId);
	}
	
	private Contract copyContract(Contract contract, Contract old,UserObject user){					
		BeanUtils.copyProperties(old, contract);
		contract.setId(null);
		String oldVer=old.getContrVer();
		Integer newVer = Integer.parseInt(oldVer)+1;		
		contract.setContrVer(newVer.toString());
		contract.setCreator(user.getEmployee().getName());
		LovMember lov = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "01");
		contract.setReviewStat(lov.getId());
		contract.setTrialStat(lov.getId());
		contract.setFinalReviewStat(lov.getId());
		LovMember statLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "01");
		contract.setContrStat(statLov.getId()); 
		LovMember payLov = lovMemberService.getLovMemberByCode("CONTRACTPAYSTAT", "01");
		contract.setPayStat(payLov.getId());
		contract.setIsValid("1");	
		contract.setOrg(user.getOrg().getId());
		contract.setCreateTime(new Date());		
		
		baseDao.save(contract);  
		return contract;
	}

	private void copyAttByContract(Contract contract, Contract old,UserObject user){
		Condition condition = new Condition();
		condition.getFilterObject().addCondition("quotCode", "=", old.getId());
		List<KstarAtt> attList = getAttList(condition);
		for (KstarAtt att: attList){
			KstarAtt newAtt = new KstarAtt();
			BeanUtils.copyProperties(att, newAtt);
			newAtt.setId(null);
			newAtt.setQuotCode(contract.getId());
			baseDao.save(newAtt);
		}
	}

	private void copyMemByContract(Contract contract, Contract old,UserObject user){
		Condition condition = new Condition();
		condition.getFilterObject().addCondition("quotCode", "=", old.getId());
		List<KstarMemInfo> memList = getMemList(condition);
		for (KstarMemInfo mem: memList){
			KstarMemInfo newObj = new KstarMemInfo();
			BeanUtils.copyProperties(mem, newObj);
			newObj.setId(null);
			newObj.setQuotCode(contract.getId());
			baseDao.save(newObj);
		}
	}

	private void copyAddrByContract(Contract contract, Contract old,UserObject user){
		Condition condition = new Condition();
		condition.getFilterObject().addCondition("contrId", "=", old.getId());
		List<ContrAddr> addrList = getAddrList(condition);
		for (ContrAddr addr: addrList){
			ContrAddr newObj = new ContrAddr();
			BeanUtils.copyProperties(addr, newObj);
			newObj.setId(null);
			newObj.setContrId(contract.getId());
			baseDao.save(newObj);
		}
	}

	private void copyPayPlanByContract(Contract contract, Contract old,UserObject user){
		Condition condition = new Condition();
		condition.getFilterObject().addCondition("contrId", "=", old.getId());
		List<ContrPay> payList = getPayPlanList(condition);
		for (ContrPay pay: payList){
			ContrPay newObj = new ContrPay();
			BeanUtils.copyProperties(pay, newObj);
			newObj.setId(null);
			newObj.setContrId(contract.getId());
			baseDao.save(newObj);
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
		String parentId = condition.getStringCondition("parentId");
		String searchKey = condition.getStringCondition("searchKey");
		
		StringBuffer hql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
		//StringBuffer hql = new StringBuffer(" from KstarPrjEvl where CType = '0003' and quotCode = ? ");
		//hql.append("select lst,lov from KstarPrjLst lst,LovMember lov where lov.groupId ='PRJLSTPRDCAT' and lov.id = lst.lvId and lst.CType = '0003' and lst.quotCode = ? ");
		//hql.append("select lst,lov from KstarPrjLst lst,LovMember lov where lov.groupId ='PRJLSTPRDCAT' and lov.leafFlag = 'Y' and lov.id = lst.lvId and lst.CType = '0003' and lov.memo = ? and lst.quotCode = ? ");
		hql.append("select lst,lov from KstarPrjLst lst,LovMember lov where lov.groupId ='PRJLSTPRDCAT' and lov.id = lst.lvId and lov.memo = ? and lst.quotCode = ? ");
		args.add(quotCode);
		args.add(quotCode);
		
		if (parentId!=null){
			hql.append(" and lov.path like ? ");
			args.add("%"+parentId+"%");
		}
		if (searchKey!=null){
			hql.append("  and ( lov.name like ? or lov.code like ? ) ");
			args.add("%"+searchKey+"%");
			args.add("%"+searchKey+"%");
		}
		
		IPage page = baseDao.search(hql.toString(),args.toArray(),condition.getRows(), condition.getPage());
		List<Object[]> list = (List<Object[]>) page.getList();
		((PageImpl)page).setList(BeanUtils.castList(PrjLstDtl.class,list));
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
		
		BeanUtils.copyPropertiesIgnoreNull(prjLst, oldPrjLst);
		oldPrjLst.setId(prjLstId);
		baseDao.update(oldPrjLst);
		

		
		Long count = baseDao.findUniqueEntity("select count(*) from KstarPrjLst where id = ? ",prjLstId);
		if(count > 1){
			throw new AnneException("ID已经存在!"); 
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
		
		
		baseDao.save(prjLst);
	}
	
	@Override
	public void deletePrjLst(String lvId, String quotCode) throws AnneException {
		
		List<Object> args = new ArrayList<Object>();
		args.add(quotCode);
		args.add(lvId);
		
		baseDao.executeHQL(" delete KstarPrjLst pl where pl.quotCode = ? and pl.lvId = ? ",args.toArray());
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
			theprjlst.setAvgTtl(getAvgttl(thelovmember, theprjlst));
			updatePrjLst(thelovmember,theprjlst);
		}
		
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
		
		if(oldaftsale == null){
			throw new AnneException(IQuotService.class.getName() + " saveAftsale : 没有找到要更新的数据");
		}
		
		if(!aftsale.getId().equals(oldaftsale.getId())){
			throw new AnneException("ID不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(aftsale, oldaftsale);
		oldaftsale.setCType(cType);
		baseDao.update(oldaftsale);
		
		List<Object> args = new ArrayList<Object>();
		args.add(cType);
		args.add(aftsale.getId());
		
		Long count = baseDao.findUniqueEntity("select count(*) from KstarAftSale where CType = ? and id = ? ",args.toArray());
		if(count > 1){
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
	public List<List<Object>> exportContractsHead(PageCondition condition, String typ, String[] ids) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<List<Object>> lstOut = new ArrayList<List<Object>>();
		addHead(lstOut);
		List<Contract> headerLst = getContractList(condition,typ,ids);
		int i=1;
		for(Contract con: headerLst){
			
			
			List<Object> lstIn = new ArrayList<Object>();
			lstIn.add(i);
			lstIn.add(con.getContrNo());
			lstIn.add(con.getContrName());
			lstIn.add(con.getBussEnityName());
			lstIn.add(con.getCustName());
			lstIn.add(con.getCreatorName());
			lstIn.add(con.getOrgName());
			lstIn.add(con.getOrdererName());
			lstIn.add(con.getCreateTime());
			lstIn.add(con.getDelivDate());
			lstIn.add(con.getTotalAmt());
			lstIn.add(con.getContrStatName());
			lstIn.add(con.getContrVer());
			lstIn.add(con.getErpNo());
			lstIn.add(con.getOrderAmount());
			lstIn.add(con.getDeliveredAmt());
			lstIn.add(con.getInvoicedAmt());
 
			lstOut.add(lstIn);
			i++;
		}
		return lstOut;
	}

    @Override
    public void updateHighRiskFlag(String id, String isHighRisk) {
		this.baseDao.executeHQL("update Contract set isHighRisk=? where id=? ", new Object[]{isHighRisk, id});
    }

    /**
	 * 合同变更导出
	 * @param condition
	 * @param typ
	 * @param ids
	 * @return
	 */
	public List<Contract> getContractList(PageCondition condition,String typ,String[] ids) {
		
		

		String idsStr = "";
		if (ids.length > 0) {
			for(String id : ids){
				idsStr += "'" + id + "',";
			}
			idsStr= idsStr.substring(0, idsStr.length()-1); 
		}
		
		UserObject user = (UserObject)condition.getCondition("userObject");
		
		FilterObject fo = condition.getFilterObject(Contract.class);
		HqlObject ho = HqlUtil.getHqlObject(fo);
		String hql_search = ho.getHql();
		
		String sel_hql = ""; 
		if(hql_search.indexOf("order by")>0){
			sel_hql = hql_search.substring(0, hql_search.indexOf("order by")-1);
		}
		
		StringBuffer hql = new StringBuffer(sel_hql);
		
		if (!StringUtil.isNullOrEmpty(idsStr) && !"''".equals(idsStr)) {
			hql.append(" AND contract.id in ("+ idsStr +")");
		}
		
		relevanceHql(condition, hql, ho, user);
		
		return baseDao.findEntity(hql.toString(),ho.getArgs());
	}	
	
	private void relevanceHql(PageCondition condition,StringBuffer hql,HqlObject ho,UserObject user){
		
		
		//模糊查询
		String searchKey = condition.getStringCondition("searchKey");
		if(!StringUtil.isNullOrEmpty(searchKey)){
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
		
		String perHql= PermissionUtil.getPermissionHQL(null, "contract.createdById", "contract.createdPosId", "contract.createdOrgId", "contract.id", user, IConstants.CONTR_PI);
		hql.append(" AND " +perHql);
		hql.append(" order by contract.createTime desc , contract.contrNo desc, contract.contrVer desc ");
		
	}
	
		
	private void  addHead(List<List<Object>> lstOut){
		List<Object> lstHead = new ArrayList<Object>();
		lstHead.add("序号");
		lstHead.add("PI编号");
		lstHead.add("PI名称");
		lstHead.add("业务实体");
		lstHead.add("客户名称");
		lstHead.add("销售人员");
		lstHead.add("销售部门");
		lstHead.add("下单商务专员");
		lstHead.add("创建时间");
		lstHead.add("交货日期");
		lstHead.add("合同总金额");
		lstHead.add("合同状态");
		lstHead.add("合同版本");
		lstHead.add("ERP订单编号");
		lstHead.add("订单金额");
		lstHead.add("总已收款金额");
		lstHead.add("总未收款金额");
		
		lstOut.add(lstHead);
	}
	
	


	
}
