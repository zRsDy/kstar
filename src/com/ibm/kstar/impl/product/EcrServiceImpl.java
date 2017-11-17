package com.ibm.kstar.impl.product;

import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.action.common.process.ProcessStatusService;
import com.ibm.kstar.api.product.IEcrService;
import com.ibm.kstar.api.product.IProductProcesService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.entity.product.EcrChangeLine;
import com.ibm.kstar.entity.product.IEcrReqmEntity;
import com.ibm.kstar.entity.product.IEcrReqmProductEntity;
import com.ibm.kstar.entity.product.KstarEcrBean;
import com.ibm.kstar.exchange.pdm.EcrReqmServiceClient;
import com.ibm.kstar.impl.BaseServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.xsnake.web.page.PageImpl;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.StringUtil;
import org.xsnake.xflow.api.IProcessService;
import org.xsnake.xflow.api.workflow.IXflowProcessServiceWrapper;

import java.util.*;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class EcrServiceImpl extends BaseServiceImpl implements IEcrService{

    private Logger logger = LoggerFactory.getLogger(EcrServiceImpl.class);

	@Autowired
	BaseDao baseDao;
	@Autowired
	IProductProcesService productProcess;
	@Autowired
	IProcessService processService;
	@Autowired
	ILovMemberService lovMemberService;
	@Autowired
	ProcessStatusService processStatusService;
	@Autowired
	IXflowProcessServiceWrapper xflowProcessServiceWrapper;

	@Override
	public void startEcrrocess(UserObject user, String id) {
		KstarEcrBean ecr = baseDao.get(KstarEcrBean.class, id);
		ecr.setEcrStatus(ecr.getLovMember(ProcessConstants.PRODUCT_ECR_PROC, ProcessConstants.PROCESS_STATUS_02).getId());
		ecr.setCreateTime(new Date());
		baseDao.update(ecr);
		//启动流程
		String model = lovMemberService.getFlowCodeByAppCode(ProcessConstants.PRODUCT_ECR_PROC);
		String flowName = null;
		if(StringUtil.isNotEmpty(model)){
			flowName = ecr.getLovMember("APPLICATION", model).getName();
		}
		Map<String,String> varmap = new HashMap<>();
		varmap.put("TODO", "TODO");		
		xflowProcessServiceWrapper.start(model, ProcessConstants.PRODUCT_ECR_PROC, flowName+"-"+ecr.getEcrCode(), id, user, varmap);
	}

	@Override
	public IPage query(PageCondition condition,UserObject userObject){
		FilterObject filterObject = condition.getFilterObject(KstarEcrBean.class);
		filterObject.addOrderBy("id", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);

        String hql = hqlObject.getHql();
        String productID = condition.getStringCondition("productID");
        if (StringUtils.isEmpty(productID)) {
            int index = hql.indexOf(" where ");
            StringBuilder s = new StringBuilder();
            s.append(hql.substring(0, index)).append(" where ")
                    .append(" productID is null and ")
                    .append(hql.substring(index + 7, hql.length()));
            hql = s.toString();
        }


        return baseDao.search(hql,hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}

	@Override
	public KstarEcrBean get(String id) {
		KstarEcrBean kp = baseDao.get(KstarEcrBean.class, id);
		return kp;
	}

	
	@Override
	public KstarEcrBean queryEditEcrById(String id) {
		KstarEcrBean ecr =  this.get(id);
		
		LovMember value = (LovMember) CacheData.getInstance().get(ecr.getEcrStatus());
		if(value != null && !"新建".equalsIgnoreCase(value.getName()) && !"已驳回".equalsIgnoreCase(value.getName())){
			throw new AnneException("只有状态 in ('新建','已驳回')时才可以修改！");
			//return sendErrorMessage("只有状态 in ('新建','已驳回')时才可以修改！");
		}
		return ecr;
	}

	@Override
	public void save(KstarEcrBean ecr, UserObject user) {
		ecr.setRecordInfor(false, user);
		baseDao.save(ecr);

	}
	@Override
	public void update(KstarEcrBean ecr, UserObject userObject) {
		ecr.setRecordInfor(true, userObject);

		KstarEcrBean old = this.baseDao.get(KstarEcrBean.class, ecr.getId());
		if (old == null) {
			throw new AnneException("ECR变更申请不存在");
		}
		BeanUtils.copyPropertiesIgnoreNull(ecr, old);

		ecr = old;
		this.baseDao.update(ecr);
	}

	@Override
	public void save(KstarEcrBean ecr, List<EcrChangeLine> lines, UserObject userObject) {
		this.save(ecr, userObject);

		for (EcrChangeLine line : lines) {
			line.setChangeId(ecr.getId());
		}
		this.baseDao.save(lines);
	}

	@Override
	public void update(KstarEcrBean ecr, List<EcrChangeLine> lines, UserObject userObject) {
		this.update(ecr, userObject);

		for (EcrChangeLine line : lines) {
			line.setChangeId(ecr.getId());
		}
		this.baseDao.executeHQL("delete from EcrChangeLine where changeId=?", new Object[]{ecr.getId()});
		this.baseDao.save(lines);
	}


	@Override
	public void delete(String id) {
		KstarEcrBean  kp = baseDao.get(KstarEcrBean.class, id);
		
		if(kp == null){
			throw new AnneException("该ECR已经不存在！");
		}
		
		//System.out.println("code:"+ kp.getSaleStatus());
		LovMember value = (LovMember) CacheData.getInstance().get(kp.getEcrStatus());
		//System.out.println("value:"+ value);
		if(value != null && "新建".equalsIgnoreCase(value.getName()) || "已驳回".equalsIgnoreCase(value.getName())){
			baseDao.deleteById(KstarEcrBean.class, id);
		}else{
			throw new AnneException("只有状态 in ('新建','已驳回')时才可以删除！");
		}
	}
	
	@Override
	public List<LovMember> queryEcrRefer(String ecrId) {
		StringBuffer sql = new StringBuffer(" from KstarEcrBean t ");
		
		if(ecrId != null && StringUtils.isNotEmpty(ecrId)){
			sql.append(" where t.id != '").append(ecrId).append("'");
		}
		
		List<KstarEcrBean>  keList = baseDao.findEntity(sql.toString());
		
		List<LovMember> result = new ArrayList<LovMember>();
		for(KstarEcrBean obj : keList){
			LovMember lov = new LovMember();
			lov.setId(obj.getId());
			lov.setName(obj.getEcrCode());
			result.add(lov);
		}
		return result;
	}
	
	@Override
	public void ecrInBound(String id, UserObject user) {
		boolean ret = false;
		try{
			ret = this.doMove2Int(id, user.getEmployee().getId());
		} catch(Exception e){
			throw new AnneException(e.getMessage());
		}
		if(ret){
			LovMember lov = lovMemberService.getLovMemberByCode(ProcessConstants.PRODUCT_ECR_PROC, ProcessConstants.PROCESS_STATUS_05);
			processStatusService.updateProcessStatus("KstarEcrBean", id, "ecrStatus", lov.getId());
		}else{
			throw new AnneException("同歩到PDM系统失败！");
		}
	}

	@Override
	public boolean doMove2Int(String id, String userId) throws Exception {
		boolean result = false;
		if(null == id || null == userId){
			return false;
		}
		System.out.println("id:"+id+" ::: userId:" +userId);
		
		IEcrReqmEntity ecr = null;
		List<Map<String, Object>> list = baseDao.findBySql4Map(this.getEcrSql(id), null);
		if(null!=list && !list.isEmpty()){
			Map<String, Object> map = list.get(0);
			try {
				ecr = (IEcrReqmEntity)BeanUtils.convertMap(IEcrReqmEntity.class, map, true);
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception(e.getMessage());
			}
		}
		
		if(null == ecr){
			return false;
		}
		ecr.setCreator(userId);
		ecr.setCreated_at(new Date());
		boolean hasInvalidValue = this.checkIntStatus(ecr);
		if(hasInvalidValue){
			throw new Exception(ecr.getReMessage());
		}
		baseDao.save(ecr);

        List<String> materCodes = getMaterCodes(ecr);
        if (materCodes.size() == 0) {
            logger.warn("ECR变更申请单未找到需要变更的物料号");
            return false;
        }
        List<IEcrReqmProductEntity> productEntities = new ArrayList<>();
        for (String materCode : materCodes) {
            IEcrReqmProductEntity productEntity = new IEcrReqmProductEntity();
            productEntity.setChangeId(ecr.getId());
            productEntity.setMaterCode(materCode);
            productEntities.add(productEntity);
            baseDao.save(productEntity);
        }

		EcrReqmServiceClient sc = new EcrReqmServiceClient();
		
		try {
			result= sc.callService(ecr,productEntities);
			if(result){
				ecr.setIntstatus("P");
			} else {
				ecr.setIntstatus("E");
			}
			ecr.setUpdator(userId);
			ecr.setUpdated_at(new Date());
		} catch (Exception e) {
			ecr.setIntmessage(e.getMessage());
			ecr.setIntstatus("E");
			e.printStackTrace();
		}
		
		baseDao.update(ecr);
		return result;
	}

	private String getEcrSql(String id) {
		StringBuilder sbf = new StringBuilder();
		//language=SQL
		sbf.append(" select ecr.C_ECR_CODE as ecrxqdh, pb.C_MATER_CODE as lh, egt.LOV_NAME as jjcd, ecr.C_ECR_CHANGE_CONTENT as bgnrbgsj,")
		.append(" ecr.C_ECR_CHANGE_REASON as bgyy,ct.lov_code as bglx,")
		.append(" (select ecr.C_ECR_CODE from KCRM.CRM_T_ECR temp where temp.C_PID=ecr.C_REFER_PID) as ckyyecr,")
		.append(" ecr.C_ECR_APPLY as sqr,ecr.DT_CREATE_TIME,'A' as datastatus,'N' as intstatus")
		.append(" from KCRM.CRM_T_ECR ecr")
		.append(" left outer join KCRM.CRM_T_PRODUCT_BASIC pb on pb.C_PID=ecr.C_PRO_ID")
		.append(" left outer join KCRM.SYS_T_LOV_MEMBER egt on egt.row_id = ecr.C_ERGENT")
		.append(" left outer join KCRM.SYS_T_LOV_MEMBER ct on ct.row_id = ecr.C_ECR_CHANGE_TYPE")
		.append(" WHERE 1=1 and ecr.c_pid='")
		.append(id).append("'");
		System.out.println(sbf.toString());
		return sbf.toString();
	}

    private List<String> getMaterCodes(IEcrReqmEntity entity) {
        List<String> materCodes = new ArrayList<>();
        if (StringUtils.isEmpty(entity.getLh())) {
            //language=SQL
            String sql =
                    "SELECT pb.C_MATER_CODE\n" +
                            "FROM CRM_T_ECR ecr, CRM_T_PRODUCT_ECR_CHANGE_LINE line, CRM_T_PRODUCT_BASIC pb\n" +
                            "WHERE ecr.C_PID = line.C_CHANGE_ID AND line.C_PRODUCT_ID = pb.C_PID AND ecr.C_ECR_CODE = ?";
            List<Map<String, Object>> map = this.baseDao.findBySql4Map(sql, new Object[]{entity.getEcrxqdh()});
            for (Map<String, Object> objectMap : map) {
                materCodes.add((String) objectMap.get("C_MATER_CODE"));
            }
        } else {
            materCodes.add(entity.getLh());
        }
        return materCodes;
    }

	private boolean checkIntStatus(IEcrReqmEntity ecr) {
		StringBuilder sbf = BeanUtils.hasNullValue(ecr);
		boolean hasInvalidValue = null == sbf || sbf.length() < 0 ? false : true;
		if(hasInvalidValue && null != sbf){
			ecr.setReMessage(sbf.toString());
		}
		return hasInvalidValue;
	}

	@Override
	public boolean save(IEcrReqmEntity ecr) {
		if(null == ecr.getEcrxqdh()){
			ecr.setReMessage("CRM ECR 需求单号不能为空。");
			return false;
		}
		
		List<IEcrReqmEntity> list = baseDao.findEntity(
				" from IEcrReqmEntity where 1=1 and intstatus='P' and ecrxqdh=?",
				new Object[] {ecr.getEcrxqdh()});
		if(null == list || list.isEmpty()){
			ecr.setReMessage("没有找到状态为处理中的CRM ECR 需求单号：" + ecr.getEcrxqdh());
			return false;
		}
		
		IEcrReqmEntity ecrOld = list.get(0);
		ecr.setCreated_at(null);
		ecr.setCreator(null);
		BeanUtils.copyPropertiesIgnoreNull(ecr, ecrOld);
		ecrOld.setIntstatus("S");
		baseDao.update(ecrOld);
		
		Boolean isSuccess = null != ecr.getDjzt() && "F".equalsIgnoreCase(ecr.getDjzt()) ? true:false;
		this.move2IntBack(ecr.getEcrxqdh(), isSuccess, ecr.getSbyy());
		
		return true;
	}




	@Override
	public void move2IntBack(String ecrCode, boolean isSuccess, String backReason) {
		KstarEcrBean ecr = baseDao.findUniqueEntity("from KstarEcrBean where ecrCode = ?", ecrCode);
		String code = ProcessConstants.PROCESS_STATUS_06;
		if(!isSuccess){
			if(StringUtil.isEmpty(backReason)){
				throw new AnneException("PDM驳回，必须有驳回原因");
			}
			code = ProcessConstants.PROCESS_STATUS_07;
			ecr.setBackReason(backReason);
		}
		LovMember lov = lovMemberService.getLovMemberByCode(ProcessConstants.PRODUCT_ECR_PROC, code);
		ecr.setEcrStatus(lov.getId());
		baseDao.update(ecr);
	}

	@Override
	public IPage queryProductLines(PageCondition condition, UserObject userObject) {
		String changeId = condition.getStringCondition("changeId");
		if (StringUtil.isEmpty(changeId)) {
			return new PageImpl(null, condition.getRows(), condition.getPage(), 0);
		}
		List<Object> args = new ArrayList<>();
		StringBuilder sql = new StringBuilder();
		sql.append("\n" +
				"SELECT\n" +
				"  cl.C_ID                                                                    \"id\",\n" +
				"  p.c_pid                                                                    \"productId\",\n" +
				"  p.c_pro_code                                                               \"productCode\",\n" +
				"  p.c_vmater_code                                                            \"vmaterCode\",\n" +
				"  p.c_pro_name                                                               \"productName\",\n" +
				"  p.c_pro_model                                                              \"proModel\",\n" +
				"  p.C_PRO_CRM_CATEGORY                                                       \"crmCategory\",\n" +
				"  p.C_MODIFY_HARDWARE                                                        \"modifyHardware\",\n" +
				"  p.C_ADD_FUNCTION                                                           \"addFunction\",\n" +
				"  p.C_MODIFY_PARAMTER                                                        \"modifyParamter\",\n" +
				"  p.C_CHASSIS_SIZE                                                           \"chassisSize\",\n" +
				"  p.C_COMMERCIAL_DATA                                                        \"commercialData\",\n" +
				"  p.C_RANDOM_ATTACH                                                          \"randomAttach\",\n" +
				"  p.C_OTHER                                                                  \"other\",\n" +
				"  (SELECT m.lov_name\n" +
				"   FROM sys_t_lov_member m\n" +
				"   WHERE m.row_id = p.c_pro_publish_status)                                  \"publishStatusName\",\n" +
				"  (SELECT m.lov_name\n" +
				"   FROM sys_t_lov_member m\n" +
				"   WHERE m.row_id = p.c_pro_price_status)                                    \"priceStatusName\",\n" +
				"  (SELECT m.lov_name\n" +
				"   FROM sys_t_lov_member m\n" +
				"   WHERE m.row_id = p.c_pro_sale_status)                                     \"saleStatusName\",\n" +
				"  (SELECT m.lov_name\n" +
				"   FROM sys_t_lov_member m\n" +
				"   WHERE m.lov_code = p.C_PRO_ERP_CATEGORY AND m.group_code = 'erpCategory') \"erpCategoryName\",\n" +
				"  (SELECT m.lov_name\n" +
				"   FROM sys_t_lov_member m\n" +
				"   WHERE m.lov_code = p.C_PRO_CRM_CATEGORY AND m.group_code = 'crmCategory') \"crmCategoryName\",\n" +
				"  p.c_brand                                                                  \"proBrand\",\n" +
				"  pl.C_PRO_CATEGORY                                                           \"cproCategory\",\n" +
				"  pl.c_pro_type                                                               \"cproType\",\n" +
				"  pl.c_pro_series                                                             \"proSeries\",\n" +
				"  p.C_CLIENT_CATEGORY                                                        \"clientCategory\"\n" +
				"FROM CRM_T_PRODUCT_ECR_CHANGE_LINE cl INNER JOIN CRM_T_PRODUCT_BASIC p ON cl.c_product_id = p.c_pid\n" +
				"  LEFT JOIN CRM_T_PRODUCT_LINE pl ON p.c_pro_line_id = pl.c_pid\n" +
				"WHERE 1 = 1 ");

		this.addQueryCondition(condition, args, sql, "changeId", "cl.C_change_id", "=");
		this.addQueryCondition(condition, args, sql, "productId", "p.c_pid", "=");
		this.addQueryCondition(condition, args, sql, "productCode", "p.c_pro_code", "like");    //产品编码
		this.addQueryCondition(condition, args, sql, "productName", "p.c_pro_name", "like");    //产品名称
		this.addQueryCondition(condition, args, sql, "proModel", "p.c_pro_model", "like");    //产品型号
		this.addQueryCondition(condition, args, sql, "proBrand", "p.c_brand", "like");    //品牌
		this.addQueryCondition(condition, args, sql, "crmCategory", "p.C_PRO_CRM_CATEGORY", "=");    //CRM产品类别
		this.addQueryCondition(condition, args, sql, "erpCategory", "p.C_PRO_ERP_CATEGORY", "=");    //ERP产品类别
		this.addQueryCondition(condition, args, sql, "publishStatus", "p.c_pro_publish_status", "=");    //发布状态
		this.addQueryCondition(condition, args, sql, "priceStatus", "p.c_pro_price_status", "=");    //订价状态
		this.addQueryCondition(condition, args, sql, "saleStatus", "p.c_pro_sale_status", "=");    //转销状态
		this.addQueryCondition(condition, args, sql, "clientCategory", "p.C_CLIENT_CATEGORY", "like");    //客户型号
		this.addQueryCondition(condition, args, sql, "cproType", "pl.c_pro_type", "like");    //产品类别
		this.addQueryCondition(condition, args, sql, "proSeries", "pl.c_pro_series", "like");    //产品系列
		this.addQueryCondition(condition, args, sql, "cproCategory", "pl.C_PRO_CATEGORY", "like");    //产品分类
		this.addQueryCondition(condition, args, sql, "modifyHardware", "p.C_MODIFY_HARDWARE", "like");
		this.addQueryCondition(condition, args, sql, "addFunction", "p.C_ADD_FUNCTION", "like");
		this.addQueryCondition(condition, args, sql, "modifyParamter", "p.C_MODIFY_PARAMTER", "like");
		this.addQueryCondition(condition, args, sql, "chassisSize", "p.C_CHASSIS_SIZE", "like");
		this.addQueryCondition(condition, args, sql, "commercialData", "p.C_COMMERCIAL_DATA", "like");
		this.addQueryCondition(condition, args, sql, "randomAttach", "p.C_RANDOM_ATTACH", "like");
		this.addQueryCondition(condition, args, sql, "other", "p.C_OTHER", "like");

		return baseDao.searchBySql4Map(sql.toString(), args.toArray(), condition.getRows(), condition.getPage());

	}

	@Override
	public List<Map<String, Object>> getProductList(List<String> productIds) {
		if (productIds == null) {
			return null;
		}
		if (productIds.size() == 0) {
			return new ArrayList<>();
		}

		List<Object> args = new ArrayList<>();
		StringBuilder sql = new StringBuilder();
		sql.append("\n" +
				"SELECT\n" +
				"  p.c_pid                                                                    \"productId\",\n" +
				"  p.c_pro_code                                                               \"productCode\",\n" +
				"  p.c_vmater_code                                                            \"vmaterCode\",\n" +
				"  p.c_pro_name                                                               \"productName\",\n" +
				"  p.c_pro_model                                                              \"proModel\",\n" +
				"  p.C_PRO_CRM_CATEGORY                                                       \"crmCategory\",\n" +
				"  p.C_MODIFY_HARDWARE                                                        \"modifyHardware\",\n" +
				"  p.C_ADD_FUNCTION                                                           \"addFunction\",\n" +
				"  p.C_MODIFY_PARAMTER                                                        \"modifyParamter\",\n" +
				"  p.C_CHASSIS_SIZE                                                           \"chassisSize\",\n" +
				"  p.C_COMMERCIAL_DATA                                                        \"commercialData\",\n" +
				"  p.C_RANDOM_ATTACH                                                          \"randomAttach\",\n" +
				"  p.C_OTHER                                                                  \"other\",\n" +
				"  (SELECT m.lov_name\n" +
				"   FROM sys_t_lov_member m\n" +
				"   WHERE m.row_id = p.c_pro_publish_status)                                  \"publishStatusName\",\n" +
				"  (SELECT m.lov_name\n" +
				"   FROM sys_t_lov_member m\n" +
				"   WHERE m.row_id = p.c_pro_price_status)                                    \"priceStatusName\",\n" +
				"  (SELECT m.lov_name\n" +
				"   FROM sys_t_lov_member m\n" +
				"   WHERE m.row_id = p.c_pro_sale_status)                                     \"saleStatusName\",\n" +
				"  (SELECT m.lov_name\n" +
				"   FROM sys_t_lov_member m\n" +
				"   WHERE m.lov_code = p.C_PRO_ERP_CATEGORY AND m.group_code = 'erpCategory') \"erpCategoryName\",\n" +
				"  (SELECT m.lov_name\n" +
				"   FROM sys_t_lov_member m\n" +
				"   WHERE m.lov_code = p.C_PRO_CRM_CATEGORY AND m.group_code = 'crmCategory') \"crmCategoryName\",\n" +
				"  p.c_brand                                                                  \"proBrand\",\n" +
				"  pl.C_PRO_CATEGORY                                                           \"cproCategory\",\n" +
				"  pl.c_pro_type                                                               \"cproType\",\n" +
				"  pl.c_pro_series                                                             \"proSeries\",\n" +
				"  p.C_CLIENT_CATEGORY                                                        \"clientCategory\"\n" +
				"FROM CRM_T_PRODUCT_BASIC p LEFT JOIN CRM_T_PRODUCT_LINE pl ON p.c_pro_line_id = pl.c_pid\n" +
				"WHERE 1 = 1 ");

		for (int i = 0; i < productIds.size(); i++) {
			if (i == 0) {
				sql.append(" and p.c_pid in ");
				sql.append(" ( ");
			}
			sql.append(" ? ");
			args.add(productIds.get(i));
			if(i < productIds.size() - 1 ) {
				sql.append(" , ");
			}
			if(i == productIds.size() - 1 ) {
				sql.append(" ) ");
			}
		}
		return baseDao.findBySql4Map(sql.toString(), args.toArray());
	}



}


