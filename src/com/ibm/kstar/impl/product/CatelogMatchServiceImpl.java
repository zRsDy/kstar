package com.ibm.kstar.impl.product;

import com.ibm.kstar.api.product.ICatelogMatchService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.product.CatelogMatchBean;
import com.ibm.kstar.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.StringUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class CatelogMatchServiceImpl extends BaseServiceImpl implements ICatelogMatchService {
    @Autowired
    BaseDao baseDao;
    @Autowired
    ILovMemberService lovMemberService;

    @Override
    public IPage query(PageCondition condition, String directID) throws Exception {
        List<Object> args = new ArrayList<>();
        StringBuilder hql = new StringBuilder("from CatelogMatchBean p where p.directID = '" + directID + "'");
        this.addQueryCondition(condition, args, hql, "name", "p.directName", "like");
        this.addQueryCondition(condition, args, hql, "cproCategory", "p.cproCategory", "like");
        this.addQueryCondition(condition, args, hql, "proModel", "p.proModel", "like");
        hql.append(" order by p.id desc");
        IPage ip = baseDao.search(hql.toString(), args.toArray(), condition.getRows(), condition.getPage());
        return ip;
    }

    @Override
    public void save(CatelogMatchBean product, UserObject user) {
        if (product.getId() != null) {
            product.setRecordInfor(true, user);
            baseDao.update(product);
        } else {
            product.setRecordInfor(false, user);
            baseDao.save(product);
        }
    }

    @Override
    public CatelogMatchBean queryById(String id) {
        return baseDao.findUniqueEntity(" from CatelogMatchBean where id = '" + id + "'");
    }

    @Override
    public List<LovMember> queryByDirectID(String directID) {
//		StringBuffer sb = new StringBuffer("select distinct b.c_pid,b.c_pro_name, b.C_PRO_DESC,t.c_pid as matchId");
//		sb.append(" from crm_t_product_catalog_match t,CRM_T_PRODUCT_LINE l, CRM_T_PRODUCT_BASIC b");
//		sb.append(" where t.c_pro_category = l.c_pro_category");
//		sb.append(" and l.c_pid = b.c_pro_line_id  and nvl(t.c_pro_model,b.c_pro_model) = b.c_pro_model");
//		sb.append(" and t.c_effective= 'Y'");
//		sb.append(" and t.c_direct_id='").append(directID).append("'");
//		sb.append(" and t.dt_start_time <= sysdate and nvl(t.dt_end_time,sysdate+1) >= sysdate");
//		sb.append(" order by b.c_pid ");

        //language=SQL
        StringBuilder sb = new StringBuilder(
                "SELECT\n" +
                        "  pb.c_pid,\n" +
                        "  pb.c_pro_name,\n" +
                        "  pb.C_PRO_DESC,\n" +
                        "  pcm.c_pid AS matchId\n" +
                        "FROM CRM_T_PRODUCT_CATALOG_MATCH pcm, CRM_T_PRODUCT_BASIC pb left JOIN CRM_T_PRODUCT_LINE pl on pl.c_pid = pb.c_pro_line_id \n" +
                        "WHERE pcm.C_MATCH_TYPE='Productized' \n" +
                        "      AND pcm.C_CRM_CATEGORY = pb.C_PRO_CRM_CATEGORY\n" +
                        "      AND nvl(pcm.c_pro_category, pb.c_pro_model) = pl.C_PRO_CATEGORY AND nvl(pcm.c_pro_model, pb.c_pro_model) = pb.c_pro_model\n" +
                        "      AND pcm.c_effective = 'Y'\n" +
                        "      AND pcm.c_direct_id = ? \n" +
                        "      AND pcm.dt_start_time <= sysdate\n" +
                        "      AND nvl(pcm.dt_end_time, sysdate + 1) >= sysdate\n" +
                        "UNION \n" +
                        "SELECT\n" +
                        "  pb.c_pid,\n" +
                        "  pb.c_pro_name,\n" +
                        "  pb.C_PRO_DESC,\n" +
                        "  pcm.c_pid AS matchId\n" +
                        "FROM CRM_T_PRODUCT_CATALOG_MATCH pcm, CRM_T_PRODUCT_BASIC pb LEFT JOIN CRM_T_PRODUCT_LINE pl on pl.c_pid = pb.c_pro_line_id,CRM_T_PRODUCT_DEMAND pd,CRM_T_PRODUCT_DEMAND_REL pdr\n" +
                        "WHERE pcm.C_MATCH_TYPE='Department' and pd.C_PID=pdr.C_DEMAND_ID and pdr.C_PRODUCT_ID=pb.C_PID\n" +
                        "      AND pcm.C_CRM_CATEGORY = pb.C_PRO_CRM_CATEGORY AND nvl(pcm.C_DEPARTMENT, pd.C_CREATED_ORG_ID) = pd.C_CREATED_ORG_ID\n" +
                        "      AND pcm.c_effective = 'Y'\n" +
                        "      AND pcm.c_direct_id = ? \n" +
                        "      AND pcm.dt_start_time <= sysdate\n" +
                        "      AND nvl(pcm.dt_end_time, sysdate + 1) >= sysdate");

        List<Object[]> reValue = baseDao.findBySql(sb.toString(), new Object[]{directID, directID});

        List<LovMember> result = new ArrayList<>();
        for (Object[] obj : reValue) {
            LovMember lov = new LovMember();
            lov.setCode(StringUtil.getUUID());
            lov.setLeafFlag("Y");
            lov.setParentId(directID);
            lov.setGroupId("procatalog");
            lov.setName(String.valueOf(obj[1]));
            lov.setMemo(String.valueOf(obj[2]));

            lov.setOptTxt1(String.valueOf(obj[3]));
            lov.setOptTxt5(String.valueOf(obj[0]));
            result.add(lov);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LovMember> queryProCategory() {
        String sql = "select distinct(t.c_pro_category) from crm_t_product_line t where trim(t.c_pro_category) is not null";
        Object reValue = (Object) baseDao.findBySql(sql);
        List<Object> r = (List<Object>) reValue;
        List<LovMember> result = new ArrayList<LovMember>();
        for (Object obj : r) {
            String id = (String) obj;
            LovMember lov = new LovMember();
            if("0001".equals(id)){
            	//产品分类为0001是期初产品数据导入时产生的错误数据
            	continue;
            }
            lov.setId(id);
            lov.setName(id);
            result.add(lov);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LovMember> queryProModel(String search) {
        //language=HQL
        List<LovMember> entities = baseDao.findEntity("from LovMember where groupCode='PRODUCTMODE' and deleteFlag='N' and name like ?", new Object[]{"%" + search + "%"});
        return entities;
    }

    /**
     * 将产品根据相应的销售目录映射配置进行入目录库。
     *
     * @param productIds
     */
    @Override
    public void preDefineInCatelog(List<String> productIds) {
        List<Map<String, Object>> list = this.queryMathchCatelogByProductIds(productIds);
        if (list.size() == 0) {
            throw new AnneException("未查找到相应的数据");
        }
        for (Map<String, Object> map : list) {
            String productId = (String) map.get("productId".toUpperCase());
            String parentId = (String) map.get("parentId".toUpperCase());
            String matchId = (String) map.get("matchId".toUpperCase());
            String productName = (String) map.get("productName".toUpperCase());
            String productDesc = (String) map.get("productDesc".toUpperCase());
            Date startTime = (Date) map.get("startTime".toUpperCase());
            Date endTime = (Date) map.get("endTime".toUpperCase());

            LovMember lov = new LovMember();

            lov.setCode(StringUtil.getUUID());
            lov.setLeafFlag("Y");
            lov.setParentId(parentId);
            lov.setGroupId("procatalog");
            lov.setName(productName);
            lov.setMemo(productDesc);

            lov.setStartDate(startTime);
            lov.setEndDate(endTime);
            lov.setStartDate(startTime);

            lov.setOptTxt1(String.valueOf(matchId));
            lov.setOptTxt5(String.valueOf(productId));
            lovMemberService.saveProduct(lov);
        }

    }

    @Override
    public void delete(String id) {
        this.baseDao.deleteById(CatelogMatchBean.class, id);
    }


    private List<Map<String, Object>> queryMathchCatelogByProductIds(List<String> productIds) {

        StringBuilder inSql = new StringBuilder();
        List<Object> inArgs = new ArrayList<>();

        for (String bizId : productIds) {
            inSql.append("?").append(",");
            inArgs.add(bizId);
        }
        inSql.delete(inSql.length() - 1, inSql.length());

        //language=SQL
        List<Object> args = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * from (SELECT\n" +
                "  pb.c_pid as productId,\n" +
                "  pcm.c_pid AS matchId,\n" +
                "  pcm.DT_START_TIME AS startTime,\n" +
                "  pcm.DT_END_TIME AS endTime,\n" +
                "  pb.c_pro_name as productName,\n" +
                "  pb.C_PRO_DESC as productDesc,\n" +
                "  p.ROW_ID as parentId\n" +
                "FROM CRM_T_PRODUCT_CATALOG_MATCH pcm, CRM_T_PRODUCT_BASIC pb left JOIN CRM_T_PRODUCT_LINE pl on pl.c_pid = pb.c_pro_line_id,SYS_T_LOV_MEMBER p\n" +
                "WHERE p.ROW_ID=pcm.C_DIRECT_ID AND pcm.C_MATCH_TYPE = 'Productized'\n" +
                "      and pb.C_PID in (").append(inSql).append(")\n" +
                "      AND pcm.C_CRM_CATEGORY = pb.C_PRO_CRM_CATEGORY\n" +
                "      AND (pcm.c_pro_category IS NULL OR (pcm.c_pro_category = pl.C_PRO_CATEGORY)) " +
                "      AND (pcm.C_PRO_MODEL is NULL or (pcm.c_pro_model=pb.c_pro_model))\n" +
                "      AND pcm.c_effective = 'Y'\n" +
                "      AND pcm.dt_start_time <= sysdate\n" +
                "      AND nvl(pcm.dt_end_time, sysdate + 1) >= sysdate\n" +
                "UNION \n" +
                "SELECT\n" +
                "  pb.c_pid as productId,\n" +
                "  pcm.c_pid AS matchId,\n" +
                "  pcm.DT_START_TIME AS startTime,\n" +
                "  pcm.DT_END_TIME AS endTime,\n" +
                "  pb.c_pro_name as productName,\n" +
                "  pb.C_PRO_DESC as productDesc,\n" +
                "  p.ROW_ID as parentId\n" +
                "FROM CRM_T_PRODUCT_CATALOG_MATCH pcm, CRM_T_PRODUCT_BASIC pb left JOIN CRM_T_PRODUCT_LINE pl on pl.c_pid = pb.c_pro_line_id, CRM_T_PRODUCT_DEMAND pd, CRM_T_PRODUCT_DEMAND_REL pdr,SYS_T_LOV_MEMBER p\n" +
                "WHERE pl.c_pid = pb.c_pro_line_id AND pcm.C_MATCH_TYPE = 'Department' and p.ROW_ID=pcm.C_DIRECT_ID\n" +
                "      and pb.C_PID in (").append(inSql).append(")\n" +
                "      AND pd.C_PID = pdr.C_DEMAND_ID AND pdr.C_PRODUCT_ID = pb.C_PID\n" +
                "      AND pcm.C_CRM_CATEGORY = pb.C_PRO_CRM_CATEGORY\n" +
                "      and pd.C_CREATED_ORG_ID=pcm.C_DEPARTMENT\n" +
                "      AND pcm.c_effective = 'Y'\n" +
                "      AND pcm.dt_start_time <= sysdate\n" +
                "      AND nvl(pcm.dt_end_time, sysdate + 1) >= sysdate) \n" +
                "where NOT exists (select 1 from SYS_T_LOV_MEMBER where GROUP_CODE='procatalog' and DELETE_FLAG='N' and LEAF_FLAG='Y' AND PARENT_ID = parentId AND OPT_TXT5 = productId)");
        args.addAll(inArgs);
        args.addAll(inArgs);

        List<Map<String, Object>> list = this.baseDao.findBySql4Map(sql.toString(), args.toArray());
        return list;
    }

    private LovMember getProcatalogByProductId(String productId) {
        //language=HQL
        List<LovMember> entity = this.baseDao.findEntity("from LovMember where optTxt5=? and groupCode='procatalog' and deleteFlag='N' and leafFlag='Y'", new Object[]{productId});
        if (entity.size() == 0) {
            return null;
        }
        return entity.get(0);
    }
}


