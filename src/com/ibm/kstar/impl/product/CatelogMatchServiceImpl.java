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

        //language=SQL
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT PRODUCTID,PRODUCTNAME,PRODUCTDESC,MATCHID,STARTTIME,ENDTIME from V_PRODUCT_MATCH_CATEGORY where CATEGORYID = ?");
        List<Object[]> reValue = baseDao.findBySql(sb.toString(), new Object[]{directID});

        List<LovMember> result = new ArrayList<>();
        for (Object[] obj : reValue) {
            LovMember lov = new LovMember();
            lov.setCode(StringUtil.getUUID());
            lov.setLeafFlag("Y");
            lov.setParentId(directID);
            lov.setGroupId("procatalog");

            lov.setStartDate((Date) obj[4]);
            lov.setEndDate((Date) obj[5]);

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
            String categoryId = (String) map.get("categoryId".toUpperCase());
            String matchId = (String) map.get("matchId".toUpperCase());
            String productName = (String) map.get("productName".toUpperCase());
            String productDesc = (String) map.get("productDesc".toUpperCase());
            Date startTime = (Date) map.get("startTime".toUpperCase());
            Date endTime = (Date) map.get("endTime".toUpperCase());

            LovMember lov = new LovMember();

            lov.setCode(StringUtil.getUUID());
            lov.setLeafFlag("Y");
            lov.setParentId(categoryId);
            lov.setGroupId("procatalog");
            lov.setName(productName);
            lov.setMemo(productDesc);

            lov.setStartDate(startTime);
            lov.setEndDate(endTime);

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

        List<Object> args = new ArrayList<>();
        StringBuilder sql = new StringBuilder();

        //language=SQL
        sql.append("select * from V_product_Match_category where NOT exists (select 1 from SYS_T_LOV_MEMBER where GROUP_CODE='procatalog' and DELETE_FLAG='N' and LEAF_FLAG='Y' AND PARENT_ID = CATEGORYID AND OPT_TXT5 = productId) and PRODUCTID in (")
                .append(inSql).append(")");
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


