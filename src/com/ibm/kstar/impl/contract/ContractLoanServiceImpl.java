package com.ibm.kstar.impl.contract;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.contract.IContrPayService;
import com.ibm.kstar.api.contract.IContractLoanService;
import com.ibm.kstar.api.contract.IContractPiService;
import com.ibm.kstar.api.contract.IContractService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.org.IOrgTeamService;
import com.ibm.kstar.api.quot.IQuotService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.team.ITeamService;
import com.ibm.kstar.entity.bizopp.BusinessOpportunity;
import com.ibm.kstar.entity.contract.ContrAddr;
import com.ibm.kstar.entity.contract.ContrLoanVO;
import com.ibm.kstar.entity.contract.ContrPay;
import com.ibm.kstar.entity.contract.Contract;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.product.ProductPriceHead;
import com.ibm.kstar.entity.quot.*;
import com.ibm.kstar.entity.team.Team;
import com.ibm.kstar.permission.utils.PermissionUtil;
import org.hibernate.hql.ast.QueryTranslatorImpl;
import org.hibernate.impl.SessionFactoryImpl;
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
import org.xsnake.xflow.api.IHistoryService;
import org.xsnake.xflow.api.IProcessService;
import org.xsnake.xflow.api.model.ProcessInstance;
import org.xsnake.xflow.api.workflow.IXflowProcessServiceWrapper;

import java.math.BigDecimal;
import java.sql.Types;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author Joe
 *
 */
@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class ContractLoanServiceImpl implements IContractLoanService {

	@Autowired
	BaseDao baseDao;

	@Autowired
	IProcessService processService;
	@Autowired
	IHistoryService historyService;
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
	@Autowired
	private ICustomInfoService customServiceImpl;
	@Autowired
	private IContrPayService contrPayService;
	
	@Override
	public IPage query(PageCondition condition) {
        HqlObject hqlObject = getHqlObject(condition);
        return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(),condition.getRows(), condition.getPage());
	}

	private HqlObject getHqlObject(Condition condition){
        //		FilterObject filterObject = condition.getFilterObject(Contract.class);
        //		filterObject.addOrderBy("updatedAt", "desc");
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
        //		StringBuffer hql = new StringBuffer(" from Contract contr where 1=1  and contr.isValid ='1' ");
        //		StringBuffer hql = new StringBuffer(" from Contract contr where 1=1  ");
        StringBuffer hql = new StringBuffer();
        hql.append(sel_hql);

        relevanceHql(condition, hql, ho, user);
        return new HqlObject(hql.toString(), Arrays.asList(ho.getArgs()));
    }

    private HqlObject getSqlObject(Condition condition) {
		HqlObject hqlObject = getHqlObject(condition);
		SessionFactoryImpl sessionFactory = (SessionFactoryImpl) this.baseDao.getSessionFactory();
		QueryTranslatorImpl queryTranslator = new QueryTranslatorImpl(hqlObject.getHql(), hqlObject.getHql(), Collections.EMPTY_MAP, sessionFactory);
		queryTranslator.compile(Collections.EMPTY_MAP, false);
		String permissSql = queryTranslator.getSQLString();
		return new HqlObject(permissSql, Arrays.asList(hqlObject.getArgs()));
	}

	
	private void relevanceHql(Condition condition,StringBuffer hql,HqlObject ho,UserObject user){
		
		LovMember typeLov = lovMemberService.getLovMemberByCode("CONTRACTTYPE", IConstants.CONTR_LOAN_0101);
		hql.append(" AND contract.contrType= '" + typeLov.getId()+"'");
		
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
		
		String perHql= PermissionUtil.getPermissionHQL(null, "contract.createdById", "contract.createdPosId", "contract.createdOrgId", "contract.id", user, IConstants.CONTR_LOAN);
		hql.append(" AND " +perHql);
		hql.append(" order by contract.createTime desc , contract.contrNo desc, contract.contrVer desc ");
		
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
		teamService.addPosition(userObject.getPosition().getId(),userObject.getEmployee().getId(),IConstants.CONTR_LOAN,contract.getId());

		orgTeamService.configPrimaryOrg(contract.getId(), IConstants.CONTR_LOAN, userObject.getOrg().getId());

		//下单商务专员加入到团队成员
		teamService.addPosition(contract.getOrderPosId(),contract.getOrderer(),IConstants.CONTR_LOAN,contract.getId());

		//借货合同新增之后添加默认合同回款规则
		contrPayService.saveContrPayForLoan(contract.getId());
		
		// 引用框架下合同
		if(!StringUtil.isNullOrEmpty(contract.getFrameNo())){
			Contract contract2 = contractService.get(contract.getFrameNo());
			
			CustomInfo customInfo = baseDao.get(CustomInfo.class, contract.getCustCode());
			LovMember lv41 = lovMemberService.getLovMemberByCode("CUSTOMCATEGORY", "41"); // 大类金融行业
			LovMember lv42 = lovMemberService.getLovMemberByCode("CUSTOMCATEGORY", "42"); // 大类通讯行业
			
			if (customInfo != null && (!customInfo.getCustomCategory().equals(lv41.getId()) && !customInfo.getCustomCategory().equals(lv42.getId()))) {
				throw new AnneException("客户行业大类是金融和通讯才能提交框架下借货合同！");
			}
			
			contract.setTotalAmt(contract2.getTotalAmt());
//			copyKstarPrjLstforCont(contract.getFrameNo(), contract.getId());
		}
		
	}
	
	/**
	 * 引用框架下合同
	 * @param oldQuotId
	 * @param newQuotId
	 */
	private void copyKstarPrjLstforCont(String oldQuotId, String newQuotId){
		
		String hql = "from KstarPrjLst p where p.quotCode = ? ";
		List<KstarPrjLst> plist = baseDao.findEntity(hql,oldQuotId);
		
		for(KstarPrjLst p : plist){

			KstarPrjLst newlst = new KstarPrjLst();
			BeanUtils.copyPropertiesIgnoreNull(p, newlst);
			newlst.setQuotCode(newQuotId);
			newlst.setVeriedNum(0d);
			if(p.getApprovePrc()!=null){
				newlst.setPrdPrc(p.getApprovePrc());
			}else if(p.getApplyPrc()!=null){
				newlst.setPrdPrc(p.getApplyPrc());
			}
			baseDao.save(newlst);
		}
	}
	

	@Override
	public Contract get(String id) throws AnneException {
		if(id==null){
			return null;
		}
		return baseDao.get(Contract.class, id);
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
				new Object[]{IConstants.CONTR_LOAN,contract.getId(),contract.getOrderPosId(),contract.getOrderer()});
		if(teams.size()<=0){
			//下单商务专员加入到团队成员
			teamService.addPosition(contract.getOrderPosId(),contract.getOrderer(),IConstants.CONTR_LOAN,contract.getId());
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
	public String getContractLoanNumber() throws AnneException {
//		return baseDao.get(KstarMemInfo.class);
		String connum="";
		@SuppressWarnings("deprecation")

			String sql = "{ ? = call CRM_P_CONTRACT_PUB.gen_contract_loan_num(?)}";
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
			nlov.setCode(StringUtil.getUUID());
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
	
	private void updateContractReviewStatus(String id, String status) { 
		StringBuffer updateSql = new StringBuffer();
		updateSql.append(" update crm_t_contr_basic set C_REVIEW_STAT='" + status+ "' where C_ID ='"+id+"'");
		baseDao.executeSQL(updateSql.toString());
	}

	private void updateContractTrialStatus(String id, String status) { 
		StringBuffer updateSql = new StringBuffer();
		updateSql.append(" update crm_t_contr_basic set C_TRIAL_STAT='" + status+ "' where C_ID ='"+id+"'");
		baseDao.executeSQL(updateSql.toString());
	}

	private void updateContractFinalStatus(String id, String status) {
//		02	审批中	8a828dee5a11fc73015a12a80643000c
//		03	已审批	8a828dee5a11fc73015a12a86421000d
//		01	未启动	8a828dee5a11fc73015a12a77ffe000b
//		04	已驳回	8a828dee5a11fc73015a12a8ce48000e


		StringBuffer updateSql = new StringBuffer();
		updateSql.append(" update crm_t_contr_basic set C_FINAL_REVIEW_STAT='" + status+ "' where C_ID ='"+id+"'");
		baseDao.executeSQL(updateSql.toString());
	}
	
	
//	public void queryContrPrjevlListByContrId(String contrId){
////		String contrId = condition.getStringCondition("contrId");
//		StringBuffer hql = new StringBuffer(" from KstarPrjEvl where CType = '0005' and quotCode = ? ");
//		List<Object> args = new ArrayList<Object>();
//		args.add(contrId);
////		List<KstarPrjEvl> prjEvlList=baseDao..findBySql(hql.toString(), args.toArray());
//		List<Object[]> objList=baseDao.findBySql(hql.toString(), args.toArray());
////		return baseDao.search(hql.toString(),args.toArray(),condition.getRows(), condition.getPage());
//		List<KstarPrjEvl> prjEvlList = new ArrayList<KstarPrjEvl>();
//		for(o)
//	}

	@Override
	public void startContractTrialProcess(UserObject user,String contrId,String typ){
		
		String businessId = contrId; //StringUtil.getUUID();
		Contract contract = get(contrId);
		String tolAmt = contract.getTotalAmtDesc();
		String createrId= contract.getOrderer();
		String createrName=contract.getOrdererName();
		String application = contractService.getAppnameByType(IConstants.CONTR_LOAN_TRIAL_PROC,typ);
		String model = lovMemberService.getFlowCodeByAppCode(application);
		CustomInfo customInfo = customServiceImpl.getCustomInfo(contract.getCustCode());
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
		LovMember customCategory = lovMemberService.get( customInfo.getCustomCategory());
		varmap.put("Contract_customCategory", customCategory.getCode());//客户行业类型
		
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
		

		prjLst.setNotVeriNum(prjLst.getAmt());
		prjLst.setVeriedNum(new Double(0));
		
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
		prjLst.setNotVeriNum(prjLst.getAmt());
		prjLst.setVeriedNum(new Double(0));
		
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

	/*
	 * 借货核销工程清单 
	 * 
	 */
	@Override
	public IPage queryVeriPrjLst(PageCondition condition) {

		String oriPrjlstId = condition.getStringCondition("oriPrjlstId");

		String custCode = condition.getStringCondition("custCode");
		String custIds = contractService.getAllRelationCustByCustId(custCode);
//		12	审批通过待商务下单
//		13	审批通过商务已下单
		LovMember typeLov01 = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "12");
		LovMember typeLov02 = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "13");
		String lovstr = "'"+typeLov01.getId()+"', '"+ typeLov02.getId()+"'";
		StringBuffer hql = new StringBuffer();
		List<Object> args = new ArrayList<>();

		String createDt = condition.getStringCondition("createDt");

		//language=HQL
		hql.append("select lst from KstarPrjLst lst,Contract okc ,KstarPrjLst orgLst\n" +
				"where okc.id=lst.quotCode and   lst.CType = 'CONTR_LOAN' \n" +
				"and okc.contrStat in (" + lovstr + ") and okc.custCode in (" + custIds + ") \n" +
				"and okc.isValid = '1' \n" +
				"and orgLst.id=? \n" +
				"and ((orgLst.prdTyp is not null and orgLst.prdTyp=lst.prdTyp) or (orgLst.prdTyp is null and orgLst.materCode=lst.materCode)) ");
		args.add(oriPrjlstId);

		//新需求：待借货核销列表，过滤掉创建时间大于该合同创建时间的借货单
        if (createDt != null) {
            hql.append(" and okc.createdAt <= to_date(?,'YYYY-MM-DD HH24:MI:SS')");
			args.add(createDt);
		}

		List<KstarPrjLst> list = baseDao.findEntity(hql.toString(), args.toArray());
		IPage page = new PageImpl(list,1,1,list.size());
		return page;
	}	

	/*
	 * 借货核销-认款核销 工程清单 
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public IPage queryVeriOrderPrjLst(PageCondition condition) {
//		FilterObject filterObject = condition.getFilterObject(PrjLstDtl.class);
//		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
//		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
		
		String contrId = condition.getStringCondition("contrId");
		
		StringBuffer hql = new StringBuffer();
		List<Object> args = new ArrayList<Object>();
//		hql.append("select lst from KstarPrjLst lst,Contract okc where okc.id=lst.quotCode and okc.custCode= ? and   lst.CType = 'CONTR_LOAN' and  lst.materCode = ?  ");
		hql.append("select t.c_contract_id, t.c_contract_code, t.c_receipts_code, sum(t.n_veri_amount) veriAmt from crm_t_verification_detail t where t.c_contract_id = ? group by t.c_contract_id, t.c_contract_code, t.c_receipts_code");
		args.add(contrId);
				
//		IPage page = baseDao.search(hql.toString(),args.toArray(),condition.getRows(), condition.getPage());
//		List<Object[]> list = (List<Object[]>) page.getList();
//		((PageImpl)page).setList(BeanUtils.castList(KstarPrjLst.class,list));
//		return page;
//		return baseDao.findBySql4Map(hql.toString(), args.toArray());
		return baseDao.searchBySql4Map(hql.toString(), args.toArray(),condition.getRows(), condition.getPage());
//		return baseDao.search(hql.toString(),args.toArray(),condition.getRows(), condition.getPage());
	}	
	
	/**
	 * 借货导出
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

	@Override
	public List<List<Object>> exportContractsHead(PageCondition condition,String typ,String[] ids) throws AnneException{

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<List<Object>> lstOut = new ArrayList<List<Object>>();
		addHead(lstOut);
		List<Contract> headerLst = getContractList(condition,typ,ids);
		int i=1;
		for(Contract con: headerLst){
//			LovMember typeLov = lovMemberService.get(con.getContrType())
			
			KstarPrjLst prjLst = getKstarPrjLstVeriedNum(con.getId());
			
			Double sum = (prjLst.getVeriedNum()==null?0d:prjLst.getVeriedNum()) - (prjLst.getNotVeriNum()==null?0d:prjLst.getNotVeriNum());
			
			List<Object> lstIn = new ArrayList<Object>();
			lstIn.add(i);
			lstIn.add(con.getContrNo());
			lstIn.add(con.getCreateTime());
			lstIn.add(con.getCustName());
			lstIn.add(con.getMarketDeptName());
			lstIn.add(con.getOrgName());//ERP下单客户
			lstIn.add(con.getCreatorName());	
			lstIn.add(con.getTotalAmt());	
			lstIn.add(con.getContrStatName());	
			lstIn.add(con.getContrName());
			lstIn.add(con.getContrName());
			lstIn.add(con.getProjName());
			lstIn.add(con.getContrVer());
			if("1".equals(con.getIsValid())) {
				lstIn.add("是");
			}else {
				lstIn.add("否");
			}
			if("1".equals(con.getIsChange())) {
				lstIn.add("是");
			}else {
				lstIn.add("否");
			}
			if (String.format("%.3f", con.getTotalAmt()).equals(String.format("%.3f", sum))) {
				lstIn.add("已全部核销");
			}else if(sum.compareTo(0d) == 0){
				lstIn.add("未核销");
			}else{
				lstIn.add("部分核销");
			}
			lstIn.add(sum); // 总已核销金额
			lstIn.add(prjLst.getNotVeriNum()==null?0d:prjLst.getNotVeriNum()); // 总未核销金额
			lstIn.add(con.getRemark());
			lstIn.add(con.getUsageDesc());
			lstIn.add(con.getBussEnityName());
			lstIn.add(con.getErpOrderCustName());
			lstIn.add(con.getOrdererName());
			lstIn.add(con.getOrderNum());
			lstIn.add(con.getOrderAmount());
			lstIn.add(con.getDeliveredAmt());
			lstIn.add(con.getInvoicedAmt());
			
//			lstIn.add(con.getCreator());
//			lstIn.add(con.getSubmitTime());
//			lstIn.add(con.getExpectSignDate()); 
//			lstIn.add(con.getTolRecdAmt());//"总已收金额
//			lstIn.add(con.getNotTolRecAmt());// 总未收金额
//			lstIn.add(con.getRemark());
//			lstIn.add(con.getUsageDesc());// 借货用途
//			lstIn.add(con.getOrderNo());//订单编号
//			lstIn.add(con.getOrderedAmt());//已下单金额
//			lstIn.add(con.getDeliveredAmt());//已发货金额
//			lstIn.add(con.getInvoicedAmt());//已开票金额
//			lstIn.add(con.getPayStatDesc());//收款计划维护状态
//			lstIn.add(con.getProcessCloseDate());//流程结束时间
 
			lstOut.add(lstIn);
			i++;
		}
		return lstOut;
	}
	
	private void  addHead(List<List<Object>> lstOut){
		List<Object> lstHead = new ArrayList<Object>();
		lstHead.add("序号");
		lstHead.add("借货编号");
		lstHead.add("创建时间");
		lstHead.add("客户名称");
		lstHead.add("营销部门");
		lstHead.add("销售部门");
		lstHead.add("我司借货人");
		lstHead.add("借货总金额");
		lstHead.add("审核状态");
		lstHead.add("合同名称");
		lstHead.add("借货名称");
		lstHead.add("项目名称");
		lstHead.add("合同版本");
		lstHead.add("有效标识");
		lstHead.add("变更标识");
		lstHead.add("核销状态");
		lstHead.add("总已核销金额");
		lstHead.add("总未核销金额");
		lstHead.add("归档备注");
		lstHead.add("借货用途");
		lstHead.add("业务实体");
		lstHead.add("ERP下单客户");
		lstHead.add("下单商务专员");
		lstHead.add("订单编号");
		lstHead.add("已下单金额");
		lstHead.add("已发货金额");
		lstHead.add("已开票金额");
		
//		lstHead.add("我司借货人");
//		lstHead.add("借货时间");
//		lstHead.add("合同预计签订时间");
//		lstHead.add("总已核销金额");
//		lstHead.add("总未核销金额");
//		lstHead.add("备注");
//		lstHead.add("借货用途");
//		lstHead.add("订单编号");
//		lstHead.add("已下单金额");
//		lstHead.add("已发货金额");
//		lstHead.add("已开票金额");
//		lstHead.add("收款计划维护状态");
//		lstHead.add("流程结束时间");
		lstOut.add(lstHead);
	}
	
		
	private KstarPrjLst getKstarPrjLstVeriedNum(String contractId){
		KstarPrjLst prjLst = baseDao.findUniqueEntity("select new KstarPrjLst(sum(prdPrc*amt),sum(prdPrc*nvl(notVeriNum,0))) from KstarPrjLst where quotCode = ? ",new Object[]{contractId});
		return prjLst;
	}


	public List<Contract> countTolRecdAmt(List<Contract> contractlist) {
		for(Contract list:contractlist) {
			KstarPrjLst prjLst = getKstarPrjLstVeriedNum(list.getId());
			Double sum = (prjLst.getVeriedNum()==null?0d:prjLst.getVeriedNum()) - (prjLst.getNotVeriNum()==null?0d:prjLst.getNotVeriNum());
			list.setNotTolRecAmt(prjLst.getNotVeriNum()==null?0d:prjLst.getNotVeriNum());
			list.setTolRecdAmt(sum);
		}
		return contractlist;
	}

	/**
	 * 借货核销明细报表 导出
	 * @param condition
	 * @return
	 */
	@Override
	public List<List<Object>> exportLoanverification(PageCondition condition) {

	    //审批通过待商务下单
	    LovMember SPTG_DSWXD = lovMemberService.getLovMemberByCode("CONTRACTSTATUS","12");
	    //审批通过商务已下单
	    LovMember SPTG_SWYXD = lovMemberService.getLovMemberByCode("CONTRACTSTATUS","13");
	    //已签订
	    LovMember YQD = lovMemberService.getLovMemberByCode("CONTRACTSTATUS","10");
        //已签订商务已下单
	    LovMember YQD_SWYXD   = lovMemberService.getLovMemberByCode("CONTRACTSTATUS","08");
        //已签订待商务下单
	    LovMember YQD_DSWXD   = lovMemberService.getLovMemberByCode("CONTRACTSTATUS","07");

		StringBuilder hql = new StringBuilder();
        List<Object> args = new ArrayList<>();
        List header = Arrays.asList("序号", "借货编号", "客户名称", "营销部门", "销售部门", "我司借货人", "行号", "产品型号", "物料号", "借货数量", "单价", "核销时间", "合同号", "本次核销数量");
        //language=HQL
        hql.append("select \n" +
                "    c.contrNo,\n" +
                "    c.custName,\n" +
                "    (select name from LovMember lov where c.marketDept=lov.id) as marketDept,\n" +
                "    (select name from LovMember org where c.org=org.id) as org,\n" +
                "    (select name from Employee e where c.creator=e.id) as creator,\n" +
                "    prj.lineNum,\n" +
                "    p.proModel,\n" +
                "    p.materCode,\n" +
                "    prj.amt,\n" +
                "    prj.prdPrc,\n" +
                "    cvd.creationDate,\n" +
                "    cvd.contrCode,\n" +
                "    cvd.curVeriNum \n" +
                "from Contract c,KstarPrjLst prj,KstarProduct p,ContrVeriDetail cvd\n" +
                "where c.id=prj.quotCode and prj.proId=p.id and cvd.loanId=c.id and cvd.loanPrjlstId=prj.id and c.isValid='" + IConstants.YES+ "' and c.contrStat in (?,?,?,?,?) ");
        args.add(SPTG_DSWXD.getId());
        args.add(SPTG_SWYXD.getId());
        args.add(YQD.getId());
        args.add(YQD_SWYXD.getId());
        args.add(YQD_DSWXD.getId());

		HqlObject hqlObject = getHqlObject(condition);
        hql.append(" and c.id in ( select id ").append(hqlObject.getHql().substring(0, hqlObject.getHql().lastIndexOf("order by"))).append(") ");

        Collections.addAll(args, hqlObject.getArgs());
        List<Object[]> lists = this.baseDao.findEntity(hql.toString(), args.toArray());
		List<List<Object>> result = new ArrayList<>();
		result.add(0, header);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		for (int i = 0; i < lists.size(); i++) {
			Object[] objects = lists.get(i);
			List<Object> list = new ArrayList<>();
			list.add(0, i + 1);
			for (Object object : objects) {
				if (object instanceof Date) {
					list.add(dateFormat.format((Date)object));
				} else {
					list.add(object);
				}
			}
			result.add(list);
		}
		return result;
	}

	/**
	 * 获取借货未核销SQL语句
	 * @param condition
	 * @return
	 */
	private HqlObject unverificationLoanSQLObject(Condition condition) {
		//审批通过待商务下单
		LovMember SPTG_DSWXD = lovMemberService.getLovMemberByCode("CONTRACTSTATUS","12");
		//审批通过商务已下单
		LovMember SPTG_SWYXD = lovMemberService.getLovMemberByCode("CONTRACTSTATUS","13");
		//已签订
		LovMember YQD = lovMemberService.getLovMemberByCode("CONTRACTSTATUS","10");
		//已签订商务已下单
		LovMember YQD_SWYXD   = lovMemberService.getLovMemberByCode("CONTRACTSTATUS","08");
		//已签订待商务下单
		LovMember YQD_DSWXD   = lovMemberService.getLovMemberByCode("CONTRACTSTATUS","07");

		//合同类型
		LovMember type = lovMemberService.getLovMemberByCode("CONTRACTTYPE", IConstants.CONTR_LOAN_0101);

		List<Object> args = new ArrayList<>();
		//language=SQL
		String sql = "SELECT\n" +
				"  cb.C_CONTR_NO as \"contrNo\",\n" +
				"  cb.DT_SUBMIT_TIME as \"submitTime\",\n" +
				"  cb.C_CUST_NAME as \"custName\",\n" +
				"  cb.N_TOTAL_AMT as \"total\",\n" +
				"  (select LOV_NAME from SYS_T_LOV_MEMBER lov where cb.C_MARKET_DEPT = lov.ROW_ID) as \"marketDept\",\n" +
				"  (select LOV_NAME from SYS_T_LOV_MEMBER org where cb.C_ORG = org.ROW_ID) as \"org\",\n" +
				"  (select NAME from SYS_T_PERMISSION_EMPLOYEE e where C_CREATOR=e.ROW_ID) as \"creator\",\n" +
				"  (select LOV_NAME from SYS_T_LOV_MEMBER status WHERE cb.C_CONTR_STAT=status.ROW_ID) as \"contrStat\",\n" +
				"  CASE WHEN nvl(prj.C_VERIED_NUM,0) > 0 THEN '部分核销' ELSE '未核销' end as \"veri_status\",\n" +
				"  prj.C_LINE_NUM as \"lineNum\",\n" +
				"  pb.C_PRO_MODEL as \"proModel\",\n" +
				"  prj.N_AMOUNT as \"amount\",\n" +
				"  prj.C_VERIED_NUM as \"veriedNum\",\n" +
				"  prj.N_AMOUNT - nvl(prj.C_VERIED_NUM,0) as \"unveri_num\",\n" +
				"  prj.N_PRD_PRC as \"prdPrc\"\n" +
				"FROM CRM_T_CONTR_BASIC cb\n" +
				"  LEFT JOIN CRM_T_PRJ_LST prj ON cb.C_ID = prj.C_QID\n" +
				"  LEFT JOIN CRM_T_PRODUCT_BASIC pb ON prj.C_PRO_ID = pb.C_PID\n" +
				"WHERE cb.C_IS_VALID = '" + IConstants.YES + "' AND cb.C_CONTR_STAT IN (?, ?, ?, ?, ?) and cb.C_CONTR_TYPE = ?\n" +
				"      and nvl(prj.C_VERIED_NUM, 0) < nvl(prj.N_AMOUNT, 0)";
		args.add(SPTG_DSWXD.getId());
		args.add(SPTG_SWYXD.getId());
		args.add(YQD.getId());
		args.add(YQD_SWYXD.getId());
		args.add(YQD_DSWXD.getId());
		args.add(type.getId());

		return new HqlObject(sql, args);
	}

	/**
	 * 未核销借货明细 导出
	 * @param condition
	 * @return
	 */
	@Override
	public List<List<Object>> exportUnverificationLoan(PageCondition condition) {

		//序号	借货编号	借货日期	客户名称	借货总金额	营销部门	销售部门	我司借货人	审批状态	核销状态	行号	产品型号	借货数量	已核销数量	未核销数量	单价
		List header = Arrays.asList("序号","借货编号","借货日期","客户名称","借货总金额","营销部门","销售部门","我司借货人","审批状态","核销状态","行号","产品型号","借货数量","已核销数量","未核销数量","单价");

		HqlObject unverificationLoanSqlObject = unverificationLoanSQLObject(condition);

		HqlObject permissSqlObject = getSqlObject(condition);

		List<Object> args = new ArrayList<>();
		String sql = unverificationLoanSqlObject.getHql() + " and cb.C_ID in ( select C_ID " + permissSqlObject.getHql().substring(permissSqlObject.getHql().indexOf("from"), permissSqlObject.getHql().lastIndexOf("order by")) + ") ";
		Collections.addAll(args, unverificationLoanSqlObject.getArgs());
		Collections.addAll(args, permissSqlObject.getArgs());

		List<Object[]> lists = this.baseDao.findBySql(sql, args.toArray());
		List<List<Object>> result = new ArrayList<>();
		result.add(0, header);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < lists.size(); i++) {
			List<Object> list = new ArrayList<>();
			result.add(list);

			list.add(0, i + 1);
			for (Object object : lists.get(i)) {
				if (object instanceof Date) {
					list.add(dateFormat.format((Date) object));
				} else {
					list.add(object);
				}
			}
		}
		return result;
	}

	@Override
	public void updateHighRiskFlag(String id, String isHighRisk) {
		this.baseDao.executeHQL("update Contract set isHighRisk=? where id=? ", new Object[]{isHighRisk, id});
	}


    /**
     * 报表借货未核销查询SQL
     * 个人按按销售团队上下级过滤
     * 组织按组织上下级过滤
     */
    private HqlObject unverificationReportSqlObject(Condition condition) {

        String EMPLOYEE_YPE = "EMPLOYEE";
        String ORG_TYPE = "ORG";

        String reportType = condition.getStringCondition("reportType");
        if (StringUtil.isNotEmpty(reportType)) {
            reportType = reportType.toUpperCase();
        }
        if (reportType == null || (!EMPLOYEE_YPE.equals(reportType)) && !ORG_TYPE.equals(reportType)) {
            throw new RuntimeException("报表类型错误");
        }

        UserObject user = condition.getFilterObject().getUser();

        HqlObject unverificationLoanSqlObject = unverificationLoanSQLObject(condition);
        List<Object> args = new ArrayList<>();

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(unverificationLoanSqlObject.getHql());

        //增加币种
        int index = sqlBuilder.indexOf("select".toUpperCase()) + "select".length();
        sqlBuilder.insert(index, " (SELECT LOV_CODE FROM SYS_T_LOV_MEMBER WHERE ROW_ID=cb.C_CURRENCY) as CURRENCY ,");
        sqlBuilder.insert(index, " pb.CREATED_AT as dt ,");
        Collections.addAll(args, unverificationLoanSqlObject.getArgs());

        if (EMPLOYEE_YPE.equals(reportType)) {
            condition.setCondition("userObject", user);
            HqlObject permissSqlObject = getSqlObject(condition);
            sqlBuilder.append(" and cb.C_ID in ( select C_ID ").append(permissSqlObject.getHql().substring(permissSqlObject.getHql().indexOf("from"), permissSqlObject.getHql().lastIndexOf("order by"))).append(") ");
            Collections.addAll(args, permissSqlObject.getArgs());
        } else {
            sqlBuilder.append(" and cb.CREATED_ORG_ID in " + "(select ROW_ID from SYS_T_LOV_MEMBER WHERE GROUP_CODE='ORG' and DELETE_FLAG='N' and LOV_PATH LIKE '%'|| ? ||'%')");
            args.add(Objects.toString(user.getOid()));
        }

        sqlBuilder.append(" order by cb.DT_SUBMIT_TIME desc");

        return new HqlObject(sqlBuilder.toString(),args);

    }

    /**
     * 借货未核销总计
     *
     * @param id
     * @param currency
     * @param reportType
     * @param orgId
     * @param employeeId @return
     */
    @Override
    public BigDecimal unverificationSum(Condition condition) {

        HqlObject hqlObject = unverificationReportSqlObject(condition);

        String currency = condition.getStringCondition("currency");
        String sql;
        List<Object> args = new ArrayList<>();
        //计算汇率
        if ("RMB".equals(currency)) {
            sql = "select get_convert_cur_amount(\"unveri_num\",CURRENCY,dt,'CNY') as amt from (" + hqlObject.getHql() +")";
        } else {
            sql = "select get_convert_cur_amount(\"unveri_num\",CURRENCY,dt,'USD') as amt from (" + hqlObject.getHql() +")";
        }
        Collections.addAll(args, hqlObject.getArgs());

        //进行统计
        sql = "select sum(amt) from (" + sql + ")";

        List list = this.baseDao.findBySql(sql, args.toArray());
        if (list.size() == 0) {
            return null;
        }
        return (BigDecimal) list.get(0);
    }

    @Override
    public IPage unverificationLoanPage(PageCondition condition) {
        HqlObject sqlObject = unverificationReportSqlObject(condition);
        IPage page = baseDao.searchBySql4Map(sqlObject.getHql(), sqlObject.getArgs(), condition.getRows(), condition.getPage());
        return page;
    }

    /**
     * 获取借货核销详情SQL
     * @param condition
     * @return
     */
	private HqlObject getLoanVerificationSqlObject(PageCondition condition) {
		String theSql = null;
		List<Object> theArgs = new ArrayList<>();

		// 核销金额明细
		{
			//合同类型
			LovMember type = lovMemberService.getLovMemberByCode("CONTRACTTYPE", IConstants.CONTR_LOAN_0101);

			//审批通过待商务下单
			LovMember SPTG_DSWXD = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "12");
			//审批通过商务已下单
			LovMember SPTG_SWYXD = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "13");
			//已签订
			LovMember YQD = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "10");
			//已签订商务已下单
			LovMember YQD_SWYXD = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "08");
			//已签订待商务下单
			LovMember YQD_DSWXD = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "07");

			List<Object> args = new ArrayList<>();
			//language=SQL
			String sql = "SELECT\n" +
					"  cb.C_ID                                 AS contract_Id,\n" +
					"  cb.DT_EXPECT_SIGN_DATE                  AS EXPECT_SIGN_DATE,\n" +
					"  cb.C_MARKET_DEPT                        AS MARKET_DEPT,\n" +
					"  cb.C_ORG                                AS ORG,\n" +
					"  cb.C_CREATOR                            AS CREATOR,\n" +
					"  cb.C_CUST_NAME                          AS CUST_NAME,\n" +
					"  sum(nvl(prj.N_AMOUNT,0) * prj.N_PRD_PRC)       AS amount,\n" +
					"  sum(nvl(cvd.N_CUR_VERI_NUM,0) * prj.N_PRD_PRC) AS veri_amount\n" +
					"FROM CRM_T_CONTR_BASIC cb\n" +
					"  LEFT JOIN CRM_T_PRJ_LST prj ON cb.C_ID = prj.C_QID\n" +
					"  LEFT JOIN CRM_T_CONTR_VERI_DETAIL cvd ON cb.C_ID = cvd.C_LOAN_ID AND prj.C_PID = cvd.C_LOAN_PRJLST_ID\n" +
					"WHERE cb.C_IS_VALID = '" + IConstants.YES + "' and cb.C_CONTR_TYPE = ? AND cb.C_CONTR_STAT IN (?, ?, ?, ?, ?) AND cb.DT_EXPECT_SIGN_DATE IS NOT NULL\n";

			args.add(type.getId());
			args.add(SPTG_DSWXD.getId());
			args.add(SPTG_SWYXD.getId());
			args.add(YQD.getId());
			args.add(YQD_SWYXD.getId());
			args.add(YQD_DSWXD.getId());

			theArgs.addAll(args);

			HqlObject permissSQL = getSqlObject(condition);
			sql += " and cb.C_ID in ( select C_ID " + permissSQL.getHql().substring(permissSQL.getHql().indexOf("from"), permissSQL.getHql().lastIndexOf("order by")) + ") ";
			sql += " GROUP BY cb.C_ID, cb.DT_EXPECT_SIGN_DATE, C_MARKET_DEPT, cb.C_ORG, C_CREATOR, cb.C_CUST_NAME ";
			Collections.addAll(theArgs, permissSQL.getArgs());
			theSql = sql;
		}

		//判断是否是全部核销
		{
			//language=SQL
			String sql = "SELECT\n" +
					"        contract_Id,\n" +
					"        EXPECT_SIGN_DATE,\n" +
					"        MARKET_DEPT,\n" +
					"        ORG,\n" +
					"        CREATOR,\n" +
					"        CUST_NAME,\n" +
					"        amount,\n" +
					"        veri_amount,\n" +
					"        CASE WHEN amount = veri_amount\n" +
					"          THEN 1\n" +
					"        ELSE 0 END AS is_veri\n" +
					"      FROM (" + theSql + ") cv";
			theSql = sql;
		}

		//计算逾期
		{
			//language=SQL
			String sql = "SELECT\n" +
					"  contract_Id,\n" +
					"  MARKET_DEPT,\n" +
					"  ORG,\n" +
					"  CREATOR,\n" +
					"  CUST_NAME,\n" +
					"  amount,\n" +
					"  veri_amount,\n" +
					"  is_veri,\n" +
					"  lte_six_month,\n" +
					"  gt_six_month,\n" +
					"  case WHEN lte_six_month = 1 THEN amount - nvl(veri_amount,0) ELSE 0 END as lte_six_month_amount,\n" +
					"  case WHEN gt_six_month = 1 THEN amount - nvl(veri_amount,0) ELSE 0 END as gt_six_month_amount\n" +
					"FROM (SELECT\n" +
					"        contract_Id,\n" +
					"        MARKET_DEPT,\n" +
					"        ORG,\n" +
					"        CREATOR,\n" +
					"        CUST_NAME,\n" +
					"        amount,\n" +
					"        veri_amount,\n" +
					"        is_veri,\n" +
					"        CASE WHEN is_veri = 0 AND sysdate - EXPECT_SIGN_DATE <= 6 THEN 1 ELSE 0 END AS lte_six_month,\n" +
					"        CASE WHEN is_veri = 0 AND sysdate - EXPECT_SIGN_DATE > 6 THEN 1 ELSE 0 END AS gt_six_month\n" +
					"      FROM (" + theSql + "))";

			theSql = sql;
		}
		return new HqlObject(theSql, theArgs);
	}

	final static private String STATISTICS_TYPE_CUSTORMER = "Custormer";
	final static private String STATISTICS_TYPE_SALESMAN = "Salesman";
	final static private String STATISTICS_TYPE_SALES_DEPARTMENT = "SalesDepartment";

	private HqlObject getLoanVerificationStatisticsSqlObject(PageCondition condition, String type) {
		HqlObject sqlObject = getLoanVerificationSqlObject(condition);
		String theSql = sqlObject.getHql();
		String selectField = null;
		String groupField = null;
		String orderField = "";
		if (STATISTICS_TYPE_CUSTORMER.equals(type)) {
			selectField = "  (select LOV_NAME from SYS_T_LOV_MEMBER org where org=org.ROW_ID) as org,\n" +
					"  (select NAME from SYS_T_PERMISSION_EMPLOYEE e where CREATOR=e.ROW_ID) as creator,\n" +
					"  CUST_NAME,\n";
			groupField = "ORG, CREATOR, CUST_NAME";
		} else if (STATISTICS_TYPE_SALESMAN.equals(type)) {
			selectField = "  (select LOV_NAME from SYS_T_LOV_MEMBER org where org=org.ROW_ID) as org,\n" +
					"  (select NAME from SYS_T_PERMISSION_EMPLOYEE e where CREATOR=e.ROW_ID) as creator,\n" +
					"  null as CUST_NAME,\n";
			groupField = "ORG, CREATOR";
			orderField = " order by veri_ratio asc ";
		} else if (STATISTICS_TYPE_SALES_DEPARTMENT.equals(type)) {
			selectField = "  (select LOV_NAME from SYS_T_LOV_MEMBER org where org=org.ROW_ID) as org,\n" +
					"  null as creator,\n" +
					"  null as CUST_NAME,\n";
			groupField =
					"ORG";
			orderField = " order by veri_ratio asc ";
		} else {
			throw new RuntimeException("不支持的统计类型");
		}
		//group by求和
		{
			//language=SQL
			String sql = "SELECT\n" +
					"  MARKET_DEPT,\n" +
					"  org,\n" +
					"  creator,\n" +
					"  CUST_NAME,\n" +
					"  count,\n" +
					"  amount,\n" +
					"  veri_count,\n" +
					"  veri_amount,\n" +
					"  count - veri_count                         AS unveri_count,\n" +
					"  amount - veri_amount                       AS unveri_amount,\n" +
					"  round(decode(amount, 0, 0, veri_amount / amount), 4) AS veri_ratio,\n" +
					"  lte_six_month_count,\n" +
					"  lte_six_month_amount,\n" +
					"  gt_six_month_count,\n" +
					"  gt_six_month_amount\n" +
					"FROM (SELECT\n" +
					"        (select LOV_NAME from SYS_T_LOV_MEMBER lov where MARKET_DEPT=lov.ROW_ID) as MARKET_DEPT,\n" +
					selectField +
					"        count(contract_Id)        AS count,\n" +
					"        sum(amount)               AS amount,\n" +
					"        sum(is_veri)              AS veri_count,\n" +
					"        sum(veri_amount)          AS veri_amount,\n" +
					"        sum(lte_six_month)        AS lte_six_month_count,\n" +
					"        sum(lte_six_month_amount) AS lte_six_month_amount,\n" +
					"        sum(gt_six_month)         AS gt_six_month_count,\n" +
					"        sum(gt_six_month_amount)  AS gt_six_month_amount\n" +
					"      FROM (" + theSql + ")\n" +
					"      GROUP BY MARKET_DEPT," + groupField + ")\n"
					+ orderField;

			theSql = sql;
		}
		return new HqlObject(theSql, Arrays.asList(sqlObject.getArgs()));
	}

	private List<List<Object>> exportLoanVerificationByStatistics(PageCondition condition, String type) {

		HqlObject sqlObject = getLoanVerificationStatisticsSqlObject(condition, type);

		//序号	营销部门	销售部门	销售人员	客户名称	借货总单数	借货总金额	已核销单数	已核销金额	未核销单数	未核销金额	借货核销率	逾期0~6个月未核单数	逾期0~6个月未核金额	逾期6个月以上未核单数	逾期6个月以上未核金额
		List header = Arrays.asList("序号", "营销部门", "销售部门", "销售人员", "客户名称", "借货总单数", "借货总金额", "已核销单数", "已核销金额", "未核销单数", "未核销金额", "借货核销率", "逾期0~6个月未核单数", "逾期0~6个月未核金额", "逾期6个月以上未核单数", "逾期6个月以上未核金额");
		List<Object[]> lists = this.baseDao.findBySql(sqlObject.getHql(), sqlObject.getArgs());

		List<List<Object>> result = new ArrayList<>();
		result.add(header);

		NumberFormat percent = NumberFormat.getPercentInstance();
		percent.setMaximumFractionDigits(2);

		int idx = header.indexOf("借货核销率");
		for (int row = 0; row < lists.size(); row++) {
			Object[] objects = lists.get(row);
			List<Object> list = new ArrayList<>();
			list.add(0, row + 1);
			//处理百分号
			for (int col = 0; col < objects.length; col++) {
				Object object = objects[col];
				if (col == idx - 1) {
					list.add(percent.format(((BigDecimal) object).doubleValue()));
				} else {
					list.add(object);
				}
			}
			result.add(list);
		}
		return result;
	}

	/**
	 * 借货核销报表-按客户
	 *
	 * @param condition
	 * @return
	 */
	@Override
	public List<List<Object>> exportLoanVerificationByCustomer(PageCondition condition) {
		return exportLoanVerificationByStatistics(condition, STATISTICS_TYPE_CUSTORMER);
	}

	/**
	 * 借货核销报表-按销售人员
	 *
	 * @param condition
	 * @return
	 */
	@Override
	public List<List<Object>> exportLoanVerificationBySalesman(PageCondition condition) {
		return exportLoanVerificationByStatistics(condition, STATISTICS_TYPE_SALESMAN);
	}

	/**
	 * 借货核销报表-按销售部门
	 *
	 * @param condition
	 * @return
	 */
	@Override
	public List<List<Object>> exportLoanVerificationBySalesDepartmant(PageCondition condition) {
		return exportLoanVerificationByStatistics(condition, STATISTICS_TYPE_SALES_DEPARTMENT);
	}

	/**
	 * 借货产品明细
	 */
	@Override
	public List<List<Object>> reportExportContractsHead(PageCondition condition, String typ, String[] ids) throws AnneException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<List<Object>> lstOut = new ArrayList<List<Object>>();
		addReportHead(lstOut);
		List<Contract> headerLst = getContractList(condition, typ, ids);
		int i = 1;
		for (Contract con : headerLst) {
			List<ContrLoanVO> list = getContrLoanVO(con.getId());
			for (ContrLoanVO cv : list) {
				List<Object> lstIn = new ArrayList<Object>();
				lstIn.add(i);//序号
				lstIn.add(con.getContrNo());//借货编号
				lstIn.add(con.getCreateTime());//借货时间
				lstIn.add(con.getCustName());//客户名称
				lstIn.add(con.getTotalAmt());//借货总金额
				lstIn.add(con.getMarketDeptName());//营销部门
				lstIn.add(con.getOrgName());//销售部门
				lstIn.add(con.getCreatorName());//我司借货人
				lstIn.add(con.getContrStatName());//审批状态
				lstIn.add(con.getCancelStatDesc());//核销状态
				lstIn.add(cv.getLineNum());
				lstIn.add(cv.getPrdTyp());
				lstIn.add(cv.getAmt());
				lstIn.add(cv.getVeriedNum());
				lstIn.add(cv.getNotVeriNum());
				lstIn.add(cv.getPrdPrc());
				lstIn.add(cv.getContrCode());
				lstOut.add(lstIn);
				i++;
			}
		}
		return lstOut;
	}



	private List<ContrLoanVO> getContrLoanVO(String id) {
        // TODO Auto-generated method stub
        String hql = " from ContrLoanVO where 1=1 ";
        hql += " and quotCode = ? ";
        return baseDao.findEntity(hql,id);
    }

    private void  addReportHead(List<List<Object>> lstOut){
        List<Object> lstHead = new ArrayList<Object>();
        lstHead.add("序号");
        lstHead.add("借货编号");
        lstHead.add("借货时间");
        lstHead.add("客户名称");
        lstHead.add("借货总金额");
        lstHead.add("营销部门");
        lstHead.add("销售部门");
        lstHead.add("我司借货人");
        lstHead.add("审批状态");
        lstHead.add("核销状态");
        lstHead.add("行号");
        lstHead.add("产品型号");
        lstHead.add("借货数量");
        lstHead.add("已核销数量");
        lstHead.add("未核销数量");
        lstHead.add("单价");
        lstHead.add("合同编号");
        lstOut.add(lstHead);
    }
}
