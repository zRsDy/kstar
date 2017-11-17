package com.ibm.kstar.impl.product;

import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.action.common.process.ProcessStatusService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.product.IDemandService;
import com.ibm.kstar.api.product.IProductService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.IEmployeeService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.api.team.ITeamService;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.product.INonStadProdDemandEntity;
import com.ibm.kstar.entity.product.KstarProduct;
import com.ibm.kstar.entity.product.KstarProductDemand;
import com.ibm.kstar.entity.product.ProductDemandRel;
import com.ibm.kstar.exchange.pdm.NspdServiceClient;
import com.ibm.kstar.impl.BaseServiceImpl;
import com.ibm.kstar.permission.utils.PermissionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.StringUtil;
import org.xsnake.xflow.api.IProcessService;
import org.xsnake.xflow.api.workflow.IXflowProcessServiceWrapper;

import java.util.*;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class DemandServiceImpl extends BaseServiceImpl implements IDemandService{
	@Autowired
	BaseDao baseDao;
	@Autowired
	IEmployeeService employeeService;
	@Autowired
	ICustomInfoService customService;
	@Autowired
	ILovMemberService lovMemberService;
	@Autowired
	ProcessStatusService processStatusService;
	@Autowired
	IProcessService processService;
	@Autowired
	IXflowProcessServiceWrapper xflowProcessServiceWrapper;
	@Autowired
	protected ITeamService teamService;
	@Autowired
	IProductService productService;

	@Override
	public IPage query(PageCondition condition, UserObject user) throws Exception  {
		List<Object> args = new ArrayList<>();
		String productId = condition.getStringCondition("id");
		if(StringUtil.isNotEmpty(productId)){	//根据产品查询需求单
			StringBuilder sql = this.getQuerySql();
			sql.append(" and d.c_pro_id = ? or r.c_product_id = ?");
			args.add(productId);
			args.add(productId);
			return baseDao.searchBySql4Map(sql.toString(), args.toArray(), condition.getRows(), condition.getPage());
		}else{	//多产品的需求单
			StringBuilder hql = new StringBuilder("from KstarProductDemand d where 1=1");
			hql.append(" and d.productID is null");
			this.addQueryCondition(condition, args, hql, "demandCode", "d.demandCode", "like");
			this.addQueryCondition(condition, args, hql, "demandName", "d.demandName", "like");
			String phql = PermissionUtil.getPermissionHQL(null, "d.createdById", "d.createdPosId", "d.createdOrgId", "d.id", user, ProcessConstants.PRODUCT_DEMAND_PROC);
			hql.append(" and ").append(phql).append(" ");
			hql.append(" order by d.id desc");
			IPage page = baseDao.search(hql.toString(), args.toArray(), condition.getRows(), condition.getPage());
			@SuppressWarnings("unchecked")
			List<KstarProductDemand> list = (List<KstarProductDemand>) page.getList();
			for(KstarProductDemand demand : list){
				demand.setDemandPersonName(employeeService.get(demand.getDemandPerson()).getName());
				CustomInfo customInfo = customService.getCustomInfo(demand.getClientCode());
				if(customInfo != null){
					demand.setClientName(customInfo.getCustomFullName());
				}
			}
			return page;
		}
	}

	@Override
	public void save(KstarProductDemand doc, UserObject user,String seriesDemand) {
		if(doc.getId() != null){
			KstarProductDemand kd = baseDao.get(KstarProductDemand.class, doc.getId());
			if(kd.getProductID() == null){
				String hql = "from ProductDemandRel r where r.productId = ? and r.demandId = ?";
				List<ProductDemandRel> relLst = baseDao.findEntity(hql, new Object[]{doc.getRelProductID(),doc.getId()});
				if(relLst != null && relLst.size() > 0){
					ProductDemandRel rel = relLst.get(0);
					rel.setDemandNumber(doc.getDemandNumber());
					rel.setPrepareBefore(doc.getPrepareBefore());
					baseDao.update(rel);
				}
				doc.setDemandNumber(0L);
				doc.setPrepareBefore(null);
			}
			BeanUtils.copyPropertiesIgnoreNull(doc,kd);
			CustomInfo customInfo = customService.getCustomInfo(kd.getClientCode());
			if(StringUtil.isNotEmpty(kd.getProSeriesOrModel())){
				kd.setDemandName(kd.getClientAddress()+customInfo.getCustomFullName()+kd.getProSeriesOrModel());
			}else{
				KstarProduct product = productService.getProductById(kd.getProductID());
				String proModel = product.getProModel()==null?"":product.getProModel();
				kd.setDemandName(kd.getClientAddress()+customInfo.getCustomFullName()+proModel);
			}
			kd.setRecordInfor(true, user);
			baseDao.update(kd);

		}else{
			CustomInfo customInfo = customService.getCustomInfo(doc.getClientCode());
			if(StringUtil.isNotEmpty(doc.getProSeriesOrModel())){
				doc.setDemandName(doc.getClientAddress()+customInfo.getCustomFullName()+doc.getProSeriesOrModel());
			}else{
				KstarProduct product = productService.getProductById(doc.getProductID());
				String proModel = product.getProModel()==null?"":product.getProModel();
				doc.setDemandName(doc.getClientAddress()+customInfo.getCustomFullName()+proModel);
			}
			doc.setRecordInfor(false, user);
			baseDao.save(doc);
			if(StringUtil.isNotEmpty(seriesDemand)){
				//加销售团队
				teamService.addPosition(user.getPosition().getId(),user.getEmployee().getId(),ProcessConstants.PRODUCT_DEMAND_PROC,doc.getId());
			}
		}
		StringBuffer updateSql = new StringBuffer();
		updateSql.append(" update CRM_T_PRODUCT_BASIC set C_DEMAND_CODE='").append(doc.getDemandCode());
		updateSql.append("' where C_PID = '");
		updateSql.append(doc.getProductID()).append("'");
		baseDao.executeSQL(updateSql.toString());
	}

	@Override
	public KstarProductDemand queryDemandById(String id,String productId) {
		KstarProductDemand kd = baseDao.get(KstarProductDemand.class, id);
		Employee employee = employeeService.get(kd.getDemandPerson());
		if(employee != null){
			kd.setDemandPersonName(employee.getName());
		}
		CustomInfo customInfo = customService.getCustomInfo(kd.getClientCode());
		if(customInfo != null){
			kd.setClientName(customInfo.getCustomFullName());
		}
		if(kd.getPrepareBefore() == null && StringUtil.isNotEmpty(productId)){
			String hql = "from ProductDemandRel r where r.productId = ? and r.demandId = ?";
			List<ProductDemandRel> relLst = baseDao.findEntity(hql, new Object[]{productId,id});
			if(relLst != null && relLst.size() > 0){
				ProductDemandRel rel = relLst.get(0);
				kd.setDemandNumber(rel.getDemandNumber());
				kd.setPrepareBefore(rel.getPrepareBefore());
				kd.setRelProductID(productId);
			}
		}
		return kd;
	}
	
	@Override
	public KstarProductDemand queryDemandByCode(String code){
		String hql = " from KstarProductDemand where demandCode=? ";
		String[] args = {code};
		List<KstarProductDemand> kds = baseDao.findEntity(hql, args);
		if(kds==null||kds.size()==0)
			return null;
		KstarProductDemand kd = kds.get(0);
		
		Employee employee = employeeService.get(kd.getDemandPerson());
		if(employee != null){
			kd.setDemandPersonName(employee.getName());
		}
		CustomInfo customInfo = customService.getCustomInfo(kd.getClientCode());
		if(customInfo != null){
			kd.setClientName(customInfo.getCustomFullName());
		}
		return kd;
	}


	@Override
	public String findOnerSaleCenter(String orgId) {
		String sql = "select m.row_id,m.lov_name from sys_t_lov_member m where m.opt_txt3 = 'A' and m.leaf_flag = 'N' start with m.row_id = ? connect by prior m.parent_id = m.row_id";
		List<Object[]> lst = baseDao.findBySql(sql, orgId);
		if(lst != null && lst.size() > 0){
			return (String) lst.get(0)[0];
		}
		return "";
	}

	@Override
	public void delete(String id) {
		String hql = "from ProductDemandRel r where r.demandId = ?";
		List<ProductDemandRel> lst = baseDao.findEntity(hql, id);
		baseDao.deleteAll(lst);
		KstarProductDemand kd = baseDao.get(KstarProductDemand.class, id);
		baseDao.delete(kd);
	}

	@Override
	public void submitDemand(String id, UserObject user) {
		KstarProductDemand pd = baseDao.get(KstarProductDemand.class, id);
		LovMember status = baseDao.get(LovMember.class, pd.getDemandStatus());
		if(!"新建".equals(status.getName())
				&& !"已驳回".equals(status.getName())
				&& !"PDM驳回".equals(status.getName())
				){
			throw new AnneException("只有新建或已驳回或PDM驳回的数据可以提交!");
		}
		LovMember lov = lovMemberService.getLovMemberByCode(ProcessConstants.PRODUCT_DEMAND_PROC, ProcessConstants.PROCESS_STATUS_02);
		processStatusService.updateProcessStatus("KstarProductDemand", id, "demandStatus", lov.getId());
		//启动流程
		String model = lovMemberService.getFlowCodeByAppCode(ProcessConstants.PRODUCT_DEMAND_PROC);
		String flowName = null;
		if(StringUtil.isNotEmpty(model)){
			flowName = new KstarProductDemand().getLovMember("APPLICATION", model).getName();
		}
		KstarProductDemand demand = baseDao.get(KstarProductDemand.class, id);
		Map<String,String> varmap = new HashMap<>();
		varmap.put("POSITION_TYPE", user.getPosition().getOptTxt3());	//岗位类型code
		varmap.put("BUSINESS_UNIT", demand.getBusinessUnitCode());
		varmap.put("TODO", "TODO");
		xflowProcessServiceWrapper.start(model, ProcessConstants.PRODUCT_DEMAND_PROC, flowName+"-"+demand.getDemandCode(), id, user, varmap);
	}

	@Override
	public void demandInBound(String id, UserObject user, Boolean wasOne2Many) {
		try{
			this.doMove2Int(id, user.getEmployee().getId(), wasOne2Many);
			LovMember lov = lovMemberService.getLovMemberByCode(ProcessConstants.PRODUCT_DEMAND_PROC, ProcessConstants.PROCESS_STATUS_05);
			processStatusService.updateProcessStatus("KstarProductDemand", id, "demandStatus", lov.getId());
		} catch(Exception e){
			throw new AnneException(e.getMessage());
		}
	}

//	@Override
//	public boolean doMove2Int(String id, String userId) throws Exception {
//		return this.doMove2Int(id, userId, false);
//	}


	public boolean doMove2Int(String id, String userId, Boolean wasOne2Many) throws Exception {
		boolean result = false;
		if(null == id || null == userId){
			return false;
		}

		List<INonStadProdDemandEntity> nspds = getNSPDs(id, wasOne2Many);
		if(null == nspds){
			return false;
		}

		for (INonStadProdDemandEntity insde : nspds){
			insde.setCreator(userId);
			insde.setCreated_at(new Date());
			if(!wasOne2Many){
				insde.setXlxh(insde.getNbxh());
			}
//			boolean hasInvalidValue = this.checkIntStatus(insde);
//			if(hasInvalidValue){
//				throw new Exception(insde.getReMessage());
//			}
			baseDao.save(insde);
		}


		NspdServiceClient sc = new NspdServiceClient();
		try {
			result= sc.callService(nspds);
		} catch (Exception e) {
			throw e;
		}

		for (INonStadProdDemandEntity insde : nspds){
			if(result){
				insde.setIntstatus("P");
			} else {
				insde.setIntstatus("E");
			}
			insde.setUpdator(userId);
			insde.setUpdated_at(new Date());
			baseDao.update(insde);
		}

		return result;
	}
	
	public boolean doMove2IntWithoutSync(String id, String userId, Boolean wasOne2Many) throws Exception {
		if(null == id || null == userId){
			return false;
		}

		List<INonStadProdDemandEntity> nspds = getNSPDs(id, wasOne2Many);
		if(null == nspds){
			return false;
		}

		for (INonStadProdDemandEntity insde : nspds){
			insde.setCreator(userId);
			insde.setCreated_at(new Date());
			if(!wasOne2Many){
				insde.setXlxh(insde.getNbxh());
			}
			insde.setIntstatus("P");
			insde.setUpdator(userId);
			insde.setUpdated_at(new Date());
			baseDao.save(insde);
		}
		return true;
	}

	private List<INonStadProdDemandEntity> getNSPDs(String id, boolean wasOne2Many) throws Exception {
		List<INonStadProdDemandEntity> nspd =  new ArrayList<INonStadProdDemandEntity>();
		List<Map<String, Object>> list = baseDao.findBySql4Map(this.getSql(id, wasOne2Many), null);
		if(null!=list && !list.isEmpty()){
			for (Map<String, Object> map : list) {
				try {
					INonStadProdDemandEntity insde = (INonStadProdDemandEntity)BeanUtils.convertMap(INonStadProdDemandEntity.class, map, true);
					nspd.add(insde);
				} catch (Exception e) {
					e.printStackTrace();
					throw new Exception(e.getMessage());
				}
			}
		}
		return nspd;
	}

	private String getSql(String id, boolean wasOne2Many) {
		StringBuilder sbf = new StringBuilder();
		sbf.append(" select pd.C_DEMAND_CODE as crmsqdh,pb.C_VMATER_CODE as ydywlh,pd.C_DEMAND_NAME as xqdm, cus.C_CUSTOM_FULL_NAME as kh, ca.lov_name||ca1.lov_name||ca2.lov_name||ca3.lov_name as khszd,")
		.append(" pd.C_CLIENT_CONTRACT as khpo,Pd.DT_HOPEDELIVER_DATE,dc.LOV_NAME as xqzl,pb.C_PRO_MODEL as nbxh, pd.c_pro_series_or_model as xlxh, ")
		.append(" org.LOV_NAME as sqbm,tpe.name as sqr,pd.C_CONTRACT_TEL as tel,");

		if(!wasOne2Many){
			sbf.append(" pd.N_DEMANT_NUMBER as sl, pd.C_PREPARE_BEFORE as sfbl,");
		} else {
			sbf.append(" pdr.N_DEMANT_NUMBER as sl, pdr.C_PREPARE_BEFORE as sfbl,");
		}

		sbf.append(" (select listagg(att.C_DOC_URL, ',') within group( order by att.c_biz_type, att.c_biz_id) as tgburl")
		.append(" from KCRM.CRM_T_ATTACHMENT att where 1=1 AND att.c_biz_type='productDemand_1' and att.c_biz_id=pd.C_PID)as tgburl,")

		.append(" (select listagg(att.c_doc_fulnm, ',') within group( order by att.c_biz_type, att.c_biz_id) as tgburl")
		.append(" from KCRM.CRM_T_ATTACHMENT att where 1=1 AND att.c_biz_type='productDemand_1' and att.c_biz_id=pd.C_PID)as tgbmc,")

		.append(" (select listagg(att.c_doc_fulnm, ',') within group( order by att.c_biz_type, att.c_biz_id) as fjurl")
		.append(" from KCRM.CRM_T_ATTACHMENT att where 1=1 AND att.c_biz_type='productDemand_2' and att.c_biz_id=pd.C_PID)as fjmc,")

		.append(" (select listagg(att.C_DOC_URL, ',') within group( order by att.c_biz_type, att.c_biz_id) as fjurl")
		.append(" from KCRM.CRM_T_ATTACHMENT att where 1=1 AND att.c_biz_type='productDemand_2' and att.c_biz_id=pd.C_PID)as fjurl,")



		.append(" 'A' as datastatus,'N' as intstatus, crmcplx.lov_name as crmcplx,pb.C_CLIENT_CATEGORY as wbxh");

		if(!wasOne2Many){
			sbf.append(" from KCRM.CRM_T_PRODUCT_DEMAND pd")
			   .append(" left outer join KCRM.CRM_T_PRODUCT_BASIC pb on pb.C_PID=pd.C_PRO_ID");
		} else {
			sbf.append(" from KCRM.CRM_T_PRODUCT_DEMAND_REL pdr")
			   .append(" left outer join KCRM.CRM_T_PRODUCT_DEMAND pd on pd.C_PID=pdr.C_DEMAND_ID ")
			   .append(" left outer join KCRM.CRM_T_PRODUCT_BASIC pb on pb.C_PID=pdr.C_PRODUCT_ID");
		}

		sbf.append(" left outer join KCRM.CRM_T_CUSTOM_INFO cus on cus.C_PID=pd.C_CLIENT_CODE")
		.append(" left outer join KCRM.CRM_T_CUSTOM_ADDRESS_INFO cai on cai.C_PID=pd.C_CLIENT_ADDRESS")
		.append(" left outer join KCRM.SYS_T_LOV_MEMBER ca on ca.row_id = cus.C_CUSTOM_AREA")
		.append(" left outer join KCRM.SYS_T_LOV_MEMBER ca1 on ca1.row_id = cus.C_CUSTOM_AREA_SUB1")
		.append(" left outer join KCRM.SYS_T_LOV_MEMBER ca2 on ca2.row_id = cus.C_CUSTOM_AREA_SUB2")
		.append(" left outer join KCRM.SYS_T_LOV_MEMBER ca3 on ca2.row_id = cus.C_CUSTOM_AREA_SUB3")
		.append(" left outer join KCRM.SYS_T_LOV_MEMBER org on org.row_id=pd.C_DEMAND_DEPARTMENT")
		.append(" left outer join KCRM.SYS_T_LOV_MEMBER dc on dc.row_id=pd.C_DEMANT_CATEGORY")
		.append(" left outer join KCRM.SYS_T_PERMISSION_EMPLOYEE tpe on tpe.row_id=pd.C_DEMAND_PERSON")
		.append(" LEFT OUTER JOIN KCRM.SYS_T_LOV_MEMBER crmcplx on crmcplx.lov_code=pb.C_PRO_CRM_CATEGORY and crmcplx.group_code='crmCategory' ");

		if(!wasOne2Many){
			sbf.append(" WHERE 1=1 and pd.c_pid='")
			   .append(id).append("'");
		} else {
			sbf.append(" WHERE 1=1 and pdr.c_demand_id='")
			   .append(id).append("'");
		}
		sbf.append(" and crmcplx.lov_name is not null " );

		System.out.println(sbf.toString());
		return sbf.toString();
	}

	private boolean checkIntStatus(INonStadProdDemandEntity nspd) {
		StringBuilder sbf = BeanUtils.hasNullValue(nspd);
		boolean hasInvalidValue = null == sbf || sbf.length() < 0 ? false : true;
		if(hasInvalidValue && null != sbf){
			nspd.setReMessage("预定义物料号：" + nspd.getYdywlh() + sbf.toString());
		}
		return hasInvalidValue;
	}

	@Override
	public INonStadProdDemandEntity save(List<INonStadProdDemandEntity> nspds) {
		for (INonStadProdDemandEntity nspd : nspds ){
			if(null == nspd.getCrmsqdh()){
				nspd.setReMessage("CRM需求单号不能为空。");
				nspd.setResult(false);
				return nspd;
			}

			List<INonStadProdDemandEntity> list = baseDao.findEntity(
					" from INonStadProdDemandEntity where 1=1 and intstatus='P' and crmsqdh=? and ydywlh=?",
					new Object[] {nspd.getCrmsqdh(), nspd.getYdywlh()});
			if(null == list || list.isEmpty()){
				nspd.setReMessage("没有找到状态为处理中的CRM需求单号：" + nspd.getCrmsqdh());
				nspd.setResult(false);
				return nspd;
			}

			INonStadProdDemandEntity nspdOld = list.get(0);
			nspd.setCreated_at(null);
			nspd.setCreator(null);
			BeanUtils.copyPropertiesIgnoreNull(nspd, nspdOld);
			nspdOld.setIntstatus("S");
			baseDao.update(nspdOld);

			Boolean isSuccess = null != nspdOld.getDjzt() && "F".equalsIgnoreCase(nspdOld.getDjzt()) ? true:false;
			this.move2IntBack(nspdOld.getCrmsqdh(), isSuccess, nspdOld.getSbyy());

			if(isSuccess){
				String sql = new StringBuilder().append("update KCRM.CRM_T_PRODUCT_BASIC set OPT_TXT3='")
						.append(nspdOld.getPdmwlh())
						.append("' WHERE C_VMATER_CODE='")
						.append(nspdOld.getYdywlh())
						.append("'").toString();
				baseDao.executeSQL(sql);
			}
		}
		INonStadProdDemandEntity nspd = (INonStadProdDemandEntity)nspds.get(0);
		nspd.setResult(true);
		return nspd;
	}

	@Override
	public void move2IntBack(String demandCode, boolean isSuccess, String backReason) {
		KstarProductDemand demand = baseDao.findUniqueEntity("from KstarProductDemand where demandCode = ?", demandCode);
		String code = ProcessConstants.PROCESS_STATUS_06;
		if(!isSuccess){
			if(StringUtil.isEmpty(backReason)){
				throw new AnneException("PDM驳回，必须有驳回原因");
			}
			code = ProcessConstants.PROCESS_STATUS_07;
			demand.setBackReason(backReason);
		}
		LovMember lov = lovMemberService.getLovMemberByCode(ProcessConstants.PRODUCT_DEMAND_PROC, code);
		demand.setDemandStatus(lov.getId());
		baseDao.update(demand);
	}

	@Override
	public void demandSelectProducts(String[] ids, String demandId, UserObject user) {
		for(String id : ids){
			ProductDemandRel relation = new ProductDemandRel();
			relation.setProductId(id);
			relation.setDemandId(demandId);
			relation.setRecordInfor(false, user);
			relation.setDemandNumber(0L);
			relation.setPrepareBefore("否");
			baseDao.save(relation);
		}
	}

	@Override
	public void deleteDemandProducts(String[] ids) {
		for(String id : ids){
			baseDao.deleteById(ProductDemandRel.class, id);
		}
	}

	@Override
	public void submitDemandNumber(String relationId, String column, String value, UserObject user) {
		ProductDemandRel relation = baseDao.get(ProductDemandRel.class, relationId);
		if(relation == null){
			throw new AnneException("没找到数据！");
		}
		if("demandNumber".equals(column)){
			try{
				relation.setDemandNumber(Long.parseLong(value));
			}catch(Exception e){
				throw new AnneException("只能输入数字！");
			}
		}
		else if("prepareBefore".equals(column)){
			relation.setPrepareBefore(value);
		}
		relation.setRecordInfor(true, user);
		baseDao.update(relation);
	}

    @Override
    public List<KstarProductDemand> getProductDemandForProductId(String id) {
        //language=HQL
        List<KstarProductDemand> entities = this.baseDao.findEntity("select pd from KstarProductDemand pd ,ProductDemandRel pdr where pd.id=pdr.demandId and pdr.productId=? order by pd.updatedAt desc ", new Object[]{id});
        return entities;
    }

    @Override
	public boolean canApplyDemand(String productId) {
		KstarProduct product = baseDao.get(KstarProduct.class, productId);
		if(product.getMaterCode() != null){
			return false;
		}
		String sql = "select d.c_pro_id,r.c_product_id from CRM_T_PRODUCT_DEMAND d left join crm_t_product_demand_rel r on r.c_demand_id = d.c_pid where d.c_pro_id = ? or r.c_product_id = ?";
		List<Object[]> findBySql = baseDao.findBySql(sql, new Object[]{productId,productId});
		if(findBySql != null && findBySql.size() > 0){
			return false;
		}
		return true;
	}

	private StringBuilder getQuerySql(){
		StringBuilder sql = new StringBuilder();
		sql.append("select");
		sql.append(" d.c_pid \"id\",");
		sql.append(" d.c_created_by_id \"createdById\",");
		sql.append(" d.c_demand_code \"demandCode\",");
		sql.append(" d.c_demand_name \"demandName\",");
		sql.append(" c.c_custom_full_name \"clientName\",");
		sql.append(" d.c_client_address \"clientAddress\",");
		sql.append(" d.c_client_contract \"clientContract\",");
		sql.append(" d.dt_hopedeliver_date \"hopeDeliverDate\",");
		sql.append(" m.lov_name \"demantCategoryName\",");
		sql.append(" m2.lov_name \"demandDepartmentName\",");
		sql.append(" e.name \"demandPersonName\",");
		sql.append(" d.c_contract_tel \"contractNumber\",");
		sql.append(" m3.lov_name \"demandStatusName\",");
		sql.append(" d.c_back_reason \"backReason\",");
		sql.append(" nvl(r.n_demant_number,d.n_demant_number) \"demandNumber\",");
		sql.append(" nvl(r.c_prepare_before,d.c_prepare_before) \"prepareBefore\"");
		sql.append(" from CRM_T_PRODUCT_DEMAND d");
		sql.append(" left join CRM_T_PRODUCT_DEMAND_REL r on d.c_pid = r.c_demand_id");
		sql.append(" left join crm_t_custom_info c on d.c_client_code = c.c_pid");
		sql.append(" left join sys_t_lov_member m on d.c_demant_category = m.row_id");
		sql.append(" left join sys_t_lov_member m2 on d.c_demand_department = m2.row_id");
		sql.append(" left join SYS_T_PERMISSION_EMPLOYEE e on e.row_id = d.c_demand_person");
		sql.append(" left join sys_t_lov_member m3 on d.c_demand_status = m3.row_id");
		sql.append(" where 1=1");
		return sql;
	}

}
