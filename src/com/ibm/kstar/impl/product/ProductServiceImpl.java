package com.ibm.kstar.impl.product;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.action.common.process.ProcessStatusService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.product.IProLineService;
import com.ibm.kstar.api.product.IProductProcesService;
import com.ibm.kstar.api.product.IProductService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.team.ITeamService;
import com.ibm.kstar.entity.common.doc.KstarAttachment;
import com.ibm.kstar.entity.product.*;
import com.ibm.kstar.impl.BaseServiceImpl;
import com.ibm.kstar.permission.utils.PermissionUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.dao.HqlUtil;
import org.xsnake.web.dao.utils.FilerRuler;
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
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class ProductServiceImpl extends BaseServiceImpl implements IProductService {
    @Autowired
    BaseDao baseDao;
    @Autowired
    IProLineService lineService;
    @Autowired
    IProcessService processService;
    @Autowired
    ProcessStatusService processStatusService;
    @Autowired
    IProductProcesService productProcess;
    @Autowired
    ILovMemberService lovMemberService;
    @Autowired
    ICustomInfoService customInfoService;
    @Autowired
    IXflowProcessServiceWrapper xflowProcessServiceWrapper;
    @Autowired
    ITeamService teamService;

    @Override
    public void startPreSaleProcess(UserObject user, String[] ids) {
        String model = lovMemberService.getFlowCodeByAppCode(ProcessConstants.PM_PTS_PROC);
        String flowName = null;
        if (StringUtil.isNotEmpty(model)) {
            flowName = new KstarProduct().getLovMember("APPLICATION", model).getName();
        }
        Map<String, String> varmap = new HashMap<>();
        varmap.put("TODO", "TODO");
        LovMember lov = lovMemberService.getLovMemberByCode(ProcessConstants.PM_PTS_PROC, ProcessConstants.PROCESS_STATUS_02);
        for (String id : ids) {
            KstarProduct product = baseDao.get(KstarProduct.class, id);
            processStatusService.updateProcessStatus("KstarProduct", id, "saleStatus", lov.getId());
            //启动流程
            xflowProcessServiceWrapper.start(model, ProcessConstants.PM_PTS_PROC, flowName + "-" + product.getProductCode(), id, user, varmap);
        }
    }

    @Override
    public void startCsaleProcess(UserObject user, String[] ids, String newCsaleStatus, String csaleReason) {
        //		String model = lovMemberService.getFlowCodeByAppCode(ProcessConstants.PRODUCT_CSALE_PROC);
        //		String flowName = null;
        //		if(StringUtil.isNotEmpty(model)){
        //			flowName = new KstarProduct().getLovMember("APPLICATION", model).getName();
        //		}
        //		Map<String,String> varmap = new HashMap<>();
        //		varmap.put("TODO", "TODO");
        //		LovMember lov = lovMemberService.getLovMemberByCode(ProcessConstants.PRODUCT_CSALE_PROC, ProcessConstants.PROCESS_STATUS_02);
        for (String id : ids) {
            KstarProduct product = baseDao.get(KstarProduct.class, id);
            product.setNewCsaleStatus(newCsaleStatus);
            product.setCsaleStatus(newCsaleStatus);
            //			product.setCsaleProcessStatus(lov.getId());
            product.setCsaleReason(csaleReason);
            baseDao.update(product);
            //			//启动流程
            //			xflowProcessServiceWrapper.start(model, ProcessConstants.PRODUCT_CSALE_PROC, flowName+"-"+product.getProductCode(), id, user, varmap);
        }
    }

    @Override
    public void startPublishProcess(UserObject user, String[] ids) {
        String model = lovMemberService.getFlowCodeByAppCode(ProcessConstants.PRODUCT_PUBLISH_PROC);
        String flowName = null;
        if (StringUtil.isNotEmpty(model)) {
            flowName = new KstarProduct().getLovMember("APPLICATION", model).getName();
        }
        Map<String, String> varmap = new HashMap<>();
        varmap.put("TODO", "TODO");
        LovMember lov = lovMemberService.getLovMemberByCode(ProcessConstants.PRODUCT_PUBLISH_PROC, ProcessConstants.PROCESS_STATUS_02);
        for (String id : ids) {
            KstarProduct product = baseDao.get(KstarProduct.class, id);
            processStatusService.updateProcessStatus("KstarProduct", id, "publishStatus", lov.getId());
            //启动流程
            xflowProcessServiceWrapper.start(model, ProcessConstants.PRODUCT_PUBLISH_PROC, flowName + "-" + product.getProductCode(), id, user, varmap);
        }
    }

    @Override
    public void endPreSaleProcess(String processId) {

        List<KstarProductWorkFlow> kw = productProcess.getList(processId);
        List<String> idsA = new ArrayList<String>(kw.size());
        for (KstarProductWorkFlow kf : kw) {
            idsA.add(kf.getProductId());
        }
        String[] array = {};

        if ("预转销流程".equalsIgnoreCase(kw.get(0).getProcessName())) {
            updateSaleStatus(idsA.toArray(array), "propreconvertsale");
        } else if ("产品发布流程".equalsIgnoreCase(kw.get(0).getProcessName())) {
            updatePublishStatus(idsA.toArray(array), "probublished");
        } else if ("ECR提交流程".equalsIgnoreCase(kw.get(0).getProcessName())) {
            LovMember lm = lovMemberService.getLovMemberByCode("ecrStatus", "03");
            updateECRStatus(kw.get(0).getProductId(), lm.getId());
        } else if ("文档申请流程".equalsIgnoreCase(kw.get(0).getProcessName())) {
            LovMember lm = lovMemberService.getLovMemberByCode("docApplyStatus", "03");
            updateDocStatus(kw.get(0).getProductId(), lm.getId());
        }

        productProcess.delete(processId);
    }

    private void updateDocStatus(String id, String status) {
        StringBuffer updateSql = new StringBuffer();
        updateSql.append(" update CRM_T_PRODUCT_DOC set C_APPLY_STATUS='" + status + "' where C_PID= '");
        updateSql.append(id);
        updateSql.append("'");
        baseDao.executeSQL(updateSql.toString());
    }

    private void updateECRStatus(String id, String status) {
        StringBuffer updateSql = new StringBuffer();
        updateSql.append(" update CRM_T_ECR set C_STATUS='" + status + "' where C_PID= '");
        updateSql.append(id);
        updateSql.append("'");
        baseDao.executeSQL(updateSql.toString());
    }

    private void updateSaleStatus(String[] ids, String status) {
        StringBuffer updateSql = new StringBuffer();
        updateSql.append(" update crm_t_product_basic set C_PRO_SALE_STATUS='" + status + "' where C_PID in (");
        for (int i = 0; i < ids.length; i++) {
            updateSql.append("'").append(ids[i]).append("'");
            if (ids.length > 1 && i != ids.length - 1) {
                updateSql.append(",");
            }
        }
        updateSql.append(")");
        baseDao.executeSQL(updateSql.toString());
    }

    private void updatePublishStatus(String[] ids, String status) {
        StringBuffer updateSql = new StringBuffer();
        updateSql.append(" update crm_t_product_basic set C_PRO_PUBLISH_STATUS='" + status + "' where C_PID in (");
        for (int i = 0; i < ids.length; i++) {
            updateSql.append("'").append(ids[i]).append("'");
            if (ids.length > 1 && i != ids.length - 1) {
                updateSql.append(",");
            }
        }
        updateSql.append(")");
        baseDao.executeSQL(updateSql.toString());
    }

    @Override
    public IPage query(PageCondition condition) throws Exception {
        FilterObject lovFilterObject = new FilterObject(LovMember.class);
        FilterObject lineFilterObject = new FilterObject(KstarProductLine.class);
        FilterObject filterObject = condition.getFilterObject(KstarProduct.class);

        List<FilerRuler> rules = filterObject.getRules();
        Iterator<FilerRuler> it = rules.iterator();
        while (it.hasNext()) {
            FilerRuler rf = it.next();
            if ("publishStatusName".equalsIgnoreCase(rf.getField())) {
                lovFilterObject.addCondition("name", rf.getOp(), rf.getData());
                it.remove();
            } else if ("cproType".equalsIgnoreCase(rf.getField())) {
                lineFilterObject.addCondition("cproType", rf.getOp(), rf.getData());
                it.remove();
            } else if ("proSeries".equalsIgnoreCase(rf.getField())) {
                lineFilterObject.addCondition("proSeries", rf.getOp(), rf.getData());
                it.remove();
            } else if ("cproCategory".equalsIgnoreCase(rf.getField())) {
                lineFilterObject.addCondition("cproCategory", rf.getOp(), rf.getData());
                it.remove();
            }
        }

        HqlObject hqlObject2 = HqlUtil.getHqlWhere("s", lovFilterObject);
        HqlObject hqlObject = HqlUtil.getHqlWhere("p", filterObject);
        HqlObject hqlObjectLine = HqlUtil.getHqlWhere("l", lineFilterObject);
        hqlObject.addArgs(hqlObject2.getArgs());
        hqlObject.addArgs(hqlObjectLine.getArgs());
        String hql = "select p from KstarProduct p, LovMember s, LovMember s2";
        String hqlW = " where p.publishStatus=s.id and p.crmCategory=s2.code and s2.groupCode = 'crmCategory' and s.groupCode = '" + ProcessConstants.PRODUCT_PUBLISH_PROC + "' ";
        if (StringUtil.isNotEmpty(hqlObjectLine.getHql())) {
            hql += ",KstarProductLine l";
            hqlW += " and p.proLineID = l.id";
        }
        hql += hqlW;
        String queryPublish = condition.getStringCondition("queryPublish");
        if (StringUtil.isNotEmpty(queryPublish)) {
            hql = hql + " and s2.memo like '%可发布%' and exists (select s3.id from LovMember s3 where s3.groupCode='erpCategory' and s3.code=p.erpCategory and s3.memo like '%可发布%') ";
        }
        hql = hql + hqlObject.getHql() + hqlObject2.getHql() + hqlObjectLine.getHql();
        hql += " order by p.id desc";
        IPage ip = baseDao.search(hql, hqlObject.getArgs(), condition.getRows(), condition.getPage());
        @SuppressWarnings("unchecked")
        List<KstarProduct> proList = (List<KstarProduct>) ip.getList();
        for (KstarProduct kp : proList) {
            if (StringUtils.isNotBlank(kp.getProLineID())) {
                KstarProductLine lineNew = new KstarProductLine();
                KstarProductLine line = lineService.query(kp.getProLineID());
                if (line != null) {
                    BeanUtils.copyPropertiesIgnoreNull(line, lineNew);
                }
                kp.setLineBean(lineNew);
            }
        }
        return ip;
    }

    @Override
    public IPage query2(PageCondition condition, UserObject user) throws Exception {
        List<Object> args = new ArrayList<>();
        String headHql = new String("select p from KstarProduct p");
        StringBuilder whereHql = new StringBuilder(" where 1=1");

        String clientCategory = condition.getStringCondition("clientCategory");
        String cproType = condition.getStringCondition("cproType");
        String proSeries = condition.getStringCondition("proSeries");
        if (StringUtil.isNotEmpty(clientCategory) || StringUtil.isNotEmpty(cproType) || StringUtil.isNotEmpty(proSeries)) {
            headHql = headHql + ",KstarProductLine l";
            whereHql.append(" and p.proLineID = l.id");
        }
        String phql = PermissionUtil.getPermissionHQL(null, "p.createdById", "p.createdPosId", "p.createdOrgId", "p.id", user, ProcessConstants.PRODUCT_2);
        whereHql.append(" and ").append(phql).append(" ");
        this.addQueryCondition(condition, args, whereHql, "productCode", "p.productCode", "like");
        this.addQueryCondition(condition, args, whereHql, "productName", "p.productName", "like");
        this.addQueryCondition(condition, args, whereHql, "proModel", "p.proModel", "like");
        this.addQueryCondition(condition, args, whereHql, "proBrand", "p.proBrand", "like");
        this.addQueryCondition(condition, args, whereHql, "crmCategory", "p.crmCategory", "=");
        this.addQueryCondition(condition, args, whereHql, "erpCategory", "p.erpCategory", "=");
        this.addQueryCondition(condition, args, whereHql, "publishStatus", "p.publishStatus", "=");
        this.addQueryCondition(condition, args, whereHql, "priceStatus", "p.priceStatus", "=");
        this.addQueryCondition(condition, args, whereHql, "saleStatus", "p.saleStatus", "=");
        this.addQueryCondition(condition, args, whereHql, "clientCategory", "p.clientCategory", "like");
        this.addQueryCondition(condition, args, whereHql, "cproCategory", "l.cproCategory", "like");
        this.addQueryCondition(condition, args, whereHql, "cproType", "l.cproType", "like");
        this.addQueryCondition(condition, args, whereHql, "proSeries", "l.proSeries", "like");

        this.addQueryCondition(condition, args, whereHql, "modifyHardware", "p.modifyHardware", "like");
        this.addQueryCondition(condition, args, whereHql, "addFunction", "p.addFunction", "like");
        this.addQueryCondition(condition, args, whereHql, "modifyParamter", "p.modifyParamter", "like");
        this.addQueryCondition(condition, args, whereHql, "chassisSize", "p.chassisSize", "like");
        this.addQueryCondition(condition, args, whereHql, "commercialData", "p.commercialData", "like");
        this.addQueryCondition(condition, args, whereHql, "randomAttach", "p.randomAttach", "like");
        this.addQueryCondition(condition, args, whereHql, "other", "p.other", "like");

        whereHql.append(" order by p.id desc");
        IPage ip = baseDao.search(headHql + whereHql.toString(), args.toArray(), condition.getRows(), condition.getPage());
        @SuppressWarnings("unchecked")
        List<KstarProduct> proList = (List<KstarProduct>) ip.getList();
        for (KstarProduct kp : proList) {
            if (StringUtils.isNotBlank(kp.getProLineID())) {
                KstarProductLine lineNew = new KstarProductLine();
                KstarProductLine line = lineService.query(kp.getProLineID());
                BeanUtils.copyPropertiesIgnoreNull(line, lineNew);
                kp.setLineBean(lineNew);
            }
        }
        return ip;
    }

    @Override
    public IPage serviceProQuery(PageCondition condition) throws Exception {

        String id = (String) condition.getCondition("productID");

        KstarProduct kp = baseDao.findUniqueEntity(" from KstarProduct where id = ?", id);

        // 型号相同 proModel 而且属于服务类型 crmCategory

        //LovMember lm = lovMemberService.getLovMemberByCode("crmCategory", "0003");
        if (null == kp.getProModel()) {
            return new PageImpl(new ArrayList<>(), 0, condition.getRows(), 0);
        } else {
            condition.getFilterObject().addCondition("proModel", "eq", kp.getProModel());
            condition.getFilterObject().addCondition("crmCategory", "eq", "0003");
            return query(condition);
        }
    }

    @Override
    public void todo() {
        throw new AnneException("错误");

        // baseDao.get(LovGroup.class, lovGroupId);
    }

    @Override
    public void delete(String id) {
        KstarProduct kp = baseDao.get(KstarProduct.class, id);

        if (kp == null) {
            throw new AnneException("该产品已经不存在！");
        }

		/* 
         * 该产品关联了产品需求申请单也不能被删除
		 */
        if (queryDemandContainProduct(id) == true) {
            throw new AnneException("该产品已经被产品需求申请单使用,不能删除");
        }

        if ("pronotconvertsale".equalsIgnoreCase(kp.getSaleStatus()) && StringUtils.isEmpty(kp.getMaterCode())) {
            baseDao.deleteById(KstarProduct.class, id);
        } else {
            throw new AnneException("该产品非未转销状态，不能删除！");
        }
    }


    @Override
    public KstarProduct queryPureProductById(String id) {
        return baseDao.get(KstarProduct.class, id);
    }

    @Override
    public KstarProduct queryProductById(String id) {

        KstarProduct kp = baseDao.get(KstarProduct.class, id);
        String sql = "from KstarEcrBean t where productID=? and nearEffectTime is not null order by nearEffectTime desc";
        List<Object> args = new ArrayList<Object>();
        args.add(id);

        List<KstarEcrBean> ecrList = baseDao.findEntity(sql, args.toArray());
        if (!ecrList.isEmpty()) {
            kp.setEcrBean(ecrList.get(0));
        }
        if (StringUtils.isNotBlank(kp.getProLineID())) {
            KstarProductLine lineNew = new KstarProductLine();
            KstarProductLine line = lineService.query(kp.getProLineID());
            BeanUtils.copyPropertiesIgnoreNull(line, lineNew);
            kp.setLineBean(lineNew);
/*			kp.setCproCategory(line.getcProCategory());
            kp.setProLine(line.getProLine());
			kp.setProSeries(line.getProSeries());*/
        }
        return kp;
    }

    @Override
    public KstarProduct getProductById(String id) {

        KstarProduct kp = baseDao.get(KstarProduct.class, id);
        return kp;
    }

    public void save(KstarProduct product, UserObject user) {
        save(product, user, null);
    }

    @Override
    public void save(KstarProduct product, UserObject user, String catelogId) {

        if (StringUtils.isEmpty(product.getIsSpecialUse())) {
            product.setIsSpecialUse("0");
        }
        if (StringUtils.isEmpty(product.getRohStatus())) {
            product.setRohStatus("0");
        }
        if (product.getId() != null) {
            product.setRecordInfor(true, user);
            baseDao.update(product);
        } else {
            product.setRecordInfor(false, user);
            baseDao.save(product);
            //加入销售团队
            teamService.addPosition(user.getPosition().getId(), user.getEmployee().getId(), ProcessConstants.PRODUCT_2, product.getId());
        }
    }


    @Override
    public void updateProduct(KstarProduct product, UserObject user) {

        if (StringUtils.isEmpty(product.getIsSpecialUse())) {
            product.setIsSpecialUse("0");
        }
        if (StringUtils.isEmpty(product.getRohStatus())) {
            product.setRohStatus("0");
        }

        KstarProduct oldprd = baseDao.get(KstarProduct.class, product.getId());
        BeanUtils.copyPropertiesIgnoreNull(product, oldprd);
        product.setRecordInfor(true, user);
        baseDao.update(product);
    }

    @Override
    public List<KstarProduct> getList(Condition condition) throws AnneException {
        //language=HQL
        String hql = "select p from KstarProduct p,LovMember m where p.saleStatus = m.id and (m.code = '03' or m.code='05')";
        List<Object> args = new ArrayList<Object>();

        LovMember csaleStatus = new KstarProduct().getLovMember("csaleStatus", "02");    //正常在售
        hql += " and p.csaleStatus = ?";
        args.add(csaleStatus.getId());

        String parentId = condition.getStringCondition("parentId");
        String productId = condition.getStringCondition("productId");

        if (StringUtil.isNotEmpty(parentId)) {
            hql += " and not exists (select m1.id from LovMember m1 where m1.parentId=? and m1.optTxt5=p.id";
            args.add(parentId);
            if (StringUtil.isNotEmpty(productId)) {
                hql += " and m1.optTxt5 != ?";
                args.add(productId);
            }
            hql += ")";
        }

        String search = condition.getStringCondition("search");
        if (StringUtil.isNotEmpty(search)) {
            hql = hql + " and (p.productName like ? or p.productCode like ?)";
            args.add("%" + search + "%");
            args.add("%" + search + "%");
        }

        //2.【销售目录配置】功能，添加产品时增加判断：
        //1）标准库只能添加CRM产品类别为“标准产品”的产品，添加其他crm类别产品报错（外购除外）；
        //2）非标准库只能添加CRM产品类别为“非标产品”的产品，添加其他crm类别产品报错（外购除外）；
        String crmCategorys = condition.getStringCondition("crmCategory");

        if (StringUtils.isNotEmpty(crmCategorys)) {
            String[] splits = crmCategorys.split(",");

            StringBuilder inSql = new StringBuilder("");
            List<Object> inArgs = new ArrayList<>();
            for (String crmCategory : splits) {
                inSql.append("?").append(",");
                inArgs.add(crmCategory);
            }
            inSql.delete(inSql.length() - 1, inSql.length());

            hql += " and p.crmCategory in (" + inSql + ") ";
            args.addAll(inArgs);
        }

        hql = hql + " and rownum < 100 order by p.id desc";
        List<KstarProduct> reList = baseDao.findEntity(hql, args.toArray());
        return reList;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LovMember> queryProModel() {
        String sql = "select distinct(t.C_PRO_MODEL) from crm_t_product_basic t where trim(t.C_PRO_MODEL) is not null";
        Object reValue = (Object) baseDao.findBySql(sql);
        List<Object> r = (List<Object>) reValue;
        List<LovMember> result = new ArrayList<LovMember>();
        for (Object obj : r) {
            String id = (String) obj;
            LovMember lov = new LovMember();
            lov.setId(id);
            lov.setName(id);
            result.add(lov);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LovMember> queryMaterCode(String code) {
        String sql = "select distinct(t.C_MATER_CODE) from crm_t_product_basic t where (trim(t.C_MATER_CODE) is not null) and t.C_MATER_CODE like '%" + code + "%' and rownum <=200";
        Object reValue = (Object) baseDao.findBySql(sql);
        List<Object> r = (List<Object>) reValue;
        List<LovMember> result = new ArrayList<LovMember>();
        String id = null;
        for (Object obj : r) {
            id = (String) obj;
            LovMember lov = new LovMember();
            lov.setId(id);
            lov.setName(id);
            result.add(lov);
        }
        return result;
    }

    @Override
    public KstarProduct queryByMaterCode(String code) {
        String hql = "from KstarProduct t where t.materCode ='" + code + "'";
        List<KstarProduct> reValue = baseDao.findEntity(hql);
        KstarProduct result = null;
        if (reValue.size() > 0) {
            result = reValue.get(0);
        }
        return result;
    }

    @Override
    public IPage queryProcess(List<KstarProductWorkFlow> kl, int size, int toPage) throws Exception {

        StringBuffer sql = new StringBuffer("select * from CRM_T_PRODUCT_BASIC where C_PID in (");

        for (int i = 0; i < kl.size(); i++) {
            KstarProductWorkFlow kw = kl.get(i);
            sql.append("'").append(kw.getProductId()).append("'");
            if (i != kl.size() - 1) {
                sql.append(",");
            }
        }
        sql.append(")");
        return baseDao.searchBySql(sql.toString(), size, toPage);
    }

    @Override
    public List<LovMember> selectProModel(Condition condition) {
        String proSeries = condition.getStringCondition("proSeries");
        String proLine = condition.getStringCondition("proLine");
        Object findData = null;
        if (StringUtil.isNotEmpty(proSeries)) {
            findData = (Object) baseDao.findBySql("select distinct b.c_pro_model from CRM_T_PRODUCT_LINE l,CRM_T_PRODUCT_BASIC b where l.c_pid = b.c_pro_line_id and l.c_pro_series = ?", proSeries);
        } else if (StringUtil.isNotEmpty(proLine)) {
            findData = (Object) baseDao.findBySql("select distinct b.c_pro_model from CRM_T_PRODUCT_LINE l,CRM_T_PRODUCT_BASIC b where l.c_pid = b.c_pro_line_id and l.C_PRO_LINE = ?", proLine);
        }
        List<LovMember> result = new ArrayList<LovMember>();
        if (findData != null) {
            List<Object> r = (List<Object>) findData;
            for (Object obj : r) {
                if (obj != null) {
                    String id = (String) obj;
                    LovMember lov = new LovMember();
                    lov.setId(id);
                    lov.setName(id);
                    result.add(lov);
                }
            }
        }
        return result;
    }

    @Override
    public List<LovMember> selectMaterCode(Condition condition) {
        String proModel = condition.getStringCondition("proModel");
        String equipSeries = condition.getStringCondition("equipSeries");
        Object findData = null;
        if (StringUtil.isNotEmpty(proModel)) {
            findData = (Object) baseDao.findBySql("select distinct b.c_mater_code from CRM_T_PRODUCT_BASIC b where b.c_pro_model = ?", proModel);
        } else if (StringUtil.isNotEmpty(equipSeries)) {
            findData = (Object) baseDao.findBySql("select distinct b.c_mater_code from CRM_T_PRODUCT_LINE l,CRM_T_PRODUCT_BASIC b where l.c_pid = b.c_pro_line_id and l.c_pro_series = ?", equipSeries);
        }

        List<LovMember> result = new ArrayList<LovMember>();
        if (findData != null) {
            List<Object> r = (List<Object>) findData;
            for (Object obj : r) {
                if (obj != null) {
                    String id = (String) obj;
                    LovMember lov = new LovMember();
                    lov.setId(id);
                    lov.setName(id);
                    result.add(lov);
                }
            }
        }
        return result;
    }

    @Override
    public List<LovMember> queryProModel(Condition condition) {
        String search = condition.getStringCondition("search");
        List<Object> args = new ArrayList<Object>();
        StringBuffer hql = new StringBuffer("select distinct(t.C_PRO_MODEL) from crm_t_product_basic t where 1=1");
        if (StringUtil.isNotEmpty(search)) {
            hql.append(" and t.C_PRO_MODEL like ?");
            args.add("%" + search.trim() + "%");
        }
        hql.append(" and rownum < 200 order by t.C_PRO_MODEL");
        Object reValue = (Object) baseDao.findBySql(hql.toString(), args.toArray());
        List<Object> r = (List<Object>) reValue;
        List<LovMember> result = new ArrayList<LovMember>();
        for (Object obj : r) {
            if (obj != null) {
                String id = (String) obj;
                LovMember lov = new LovMember();
                lov.setId(id);
                lov.setName(id);
                result.add(lov);
            }
        }
        return result;
    }

    @Override
    public List<LovMember> selectProBrand(Condition condition) {
        String proModel = condition.getStringCondition("proModel");
        Object findData = null;
        if (StringUtil.isNotEmpty(proModel)) {
            findData = (Object) baseDao.findBySql("select distinct b.C_BRAND from CRM_T_PRODUCT_BASIC b where b.c_pro_model = ?", proModel);
        }

        List<LovMember> result = new ArrayList<LovMember>();
        if (findData != null) {
            List<Object> r = (List<Object>) findData;
            for (Object obj : r) {
                if (obj != null) {
                    String id = (String) obj;
                    LovMember lov = new LovMember();
                    lov.setId(id);
                    lov.setName(id);
                    result.add(lov);
                }
            }
        }
        return result;
    }

    @Override
    public boolean save(IProdDocInforEntity pdi) {
        pdi.setIntstatus("S");
        baseDao.save(pdi);

        String sql = "SELECT C_PID FROM KCRM.CRM_T_PRODUCT_BASIC WHERE C_MATER_CODE = '" + pdi.getLh() + "'";
        List<Object[]> prod_id = baseDao.findBySql(sql);
        if (null == prod_id || prod_id.isEmpty() || null == prod_id.get(0)) {
            pdi.setReMessage("没有找到变更需求单上对应的物料号：" + pdi.getLh());
            return false;
        }
        Object bizId = prod_id.get(0);
        KstarAttachment att = new KstarAttachment();
        att.setBizId(bizId.toString());
        att.setBizType("productComDoc");
        att.setDocNm(pdi.getWdmc());
        att.setDocFulnm(pdi.getWdmc());
        att.setDocDesc(pdi.getWdsm());
        att.setDocTp(pdi.getWdlx());
        att.setDocUrl(pdi.getWdfj());
        att.setDocUpdr(pdi.getSqr());
        att.setDtUpdDt(pdi.getScsj());
        att.setCreatedAt(pdi.getCreatetime());
        att.setCreatedById("interface from PDM");
        att.setUpdatedAt(pdi.getUpdatetime());
        att.setUpdatedById("interface from PDM");
        baseDao.save(att);

        return true;
    }

    private boolean queryDemandContainProduct(String productId) {
        String hql = "from ProductDemandRel where productId=? ";
        String[] args = {productId};
        return baseDao.exist(hql, args);
    }

    @Override
    public IPage queryDemandProducts(PageCondition condition) throws Exception {
        List<Object> args = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select r.c_pid \"id\",p.c_pro_code \"productCode\",p.c_vmater_code \"vmaterCode\",p.c_pro_name \"productName\",p.c_pro_model \"proModel\",");
        sql.append(" p.C_PRO_CRM_CATEGORY \"crmCategory\",");
        sql.append(" p.C_MODIFY_HARDWARE \"modifyHardware\",p.C_ADD_FUNCTION \"addFunction\",p.C_MODIFY_PARAMTER \"modifyParamter\",p.C_CHASSIS_SIZE \"chassisSize\","
                + "p.C_COMMERCIAL_DATA \"commercialData\",p.C_RANDOM_ATTACH \"randomAttach\",p.C_OTHER \"other\",");
        sql.append(" (select m.lov_name from sys_t_lov_member m where m.row_id = p.c_pro_publish_status) \"publishStatusName\",");
        sql.append(" (select m.lov_name from sys_t_lov_member m where m.row_id = p.c_pro_price_status) \"priceStatusName\",");
        sql.append(" (select m.lov_name from sys_t_lov_member m where m.row_id = p.c_pro_sale_status) \"saleStatusName\",");
        sql.append(" (select m.lov_name from sys_t_lov_member m where m.lov_code = p.C_PRO_ERP_CATEGORY and m.group_code='erpCategory') \"erpCategoryName\",");
        sql.append(" (select m.lov_name from sys_t_lov_member m where m.lov_code = p.C_PRO_CRM_CATEGORY and m.group_code='crmCategory') \"crmCategoryName\",");
        sql.append(" p.c_brand \"proBrand\",l.C_PRO_CATEGORY \"cproCategory\",l.c_pro_type \"cproType\",l.c_pro_series \"proSeries\",");
        sql.append(" p.C_CLIENT_CATEGORY \"clientCategory\",r.c_prepare_before \"prepareBefore\",r.n_demant_number \"demandNumber\"");
        sql.append(" from CRM_T_PRODUCT_DEMAND_REL r");
        sql.append(" inner join CRM_T_PRODUCT_BASIC p on r.c_product_id = p.c_pid");
        sql.append(" left join CRM_T_PRODUCT_LINE l on p.c_pro_line_id = l.c_pid");
        sql.append(" where 1=1");

        this.addQueryCondition(condition, args, sql, "demandId", "r.c_demand_id", "=");
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
        this.addQueryCondition(condition, args, sql, "cproType", "l.c_pro_type", "like");    //产品类别
        this.addQueryCondition(condition, args, sql, "proSeries", "l.c_pro_series", "like");    //产品系列
        this.addQueryCondition(condition, args, sql, "cproCategory", "l.C_PRO_CATEGORY", "like");    //产品分类
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
    public KstarProductLine getProductLineByProductId(String productId) {
        return baseDao.findUniqueEntity(" select l from KstarProduct c, KstarProductLine l where c.proLineID = l.id and c.id = ? ", productId);
    }


    /**
     * 根据产品型号判断是否报备
     *
     * @param productId
     * @return
     */
    @Override
    public boolean isReport(String productModel) {
        if (StringUtils.isEmpty(productModel)) {
            return false;
        }
        LovMember lov = lovMemberService.getByProductModel(productModel);
        if (lov == null) {
            return false;
        }

        if (IConstants.YES.equals(lov.getOptTxt2())) {
            return true;
        }
        return false;
    }


    /** 修改CRM产品类别
     * @param id
     * @param crmCategory
     * @param vmaterCode
     */
    @Override
    public void updateCrmCategory(String id, String crmCategory, String vmaterCode) {
        this.baseDao.executeHQL("update KstarProduct set crmCategory=?,vmaterCode=? where id=?", new Object[]{crmCategory, vmaterCode, id});
    }

    /**
     * 获取ERP预定义物料号
     *
     * @param product
     * @return
     */
    @Override
    public String getErpVmaterCodeFor(KstarProduct product) {
        //language=SQL
        List list = this.baseDao.findBySql("SELECT PRE_DEFINE_SEGMENT FROM erp_view_material_infor WHERE SEGMENT=?", new Object[]{product.getMaterCode()});
        if (list.size() >= 1) {
            return (String) list.get(0);
        }
        return null;
    }

}


