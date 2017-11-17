package com.ibm.kstar.impl.bizopp;

import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.action.common.process.ProcessStatusService;
import com.ibm.kstar.api.bizopp.IBizBaseService;
import com.ibm.kstar.api.bizopp.IBizoppService;
import com.ibm.kstar.api.contract.IContractService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.custom.ICustomNumberService;
import com.ibm.kstar.api.org.IOrgTeamService;
import com.ibm.kstar.api.price.IPriceHeadService;
import com.ibm.kstar.api.product.IProLovService;
import com.ibm.kstar.api.product.IProductService;
import com.ibm.kstar.api.quot.IQuotService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.IUserService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.api.team.ITeamService;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.entity.bizopp.*;
import com.ibm.kstar.entity.common.territory.TerritoryConfig;
import com.ibm.kstar.entity.contract.Contract;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.product.KstarProduct;
import com.ibm.kstar.entity.product.ProductPriceHead;
import com.ibm.kstar.entity.quot.KstarPrjLst;
import com.ibm.kstar.entity.quot.KstarQuot;
import com.ibm.kstar.impl.BaseServiceImpl;
import com.ibm.kstar.permission.utils.PermissionUtil;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.math.BigDecimal;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ClassName:BizoppServiceImpl <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: Jan 4, 2017 9:57:35 PM <br/>
 *
 * @author Gaoyuliang
 * @see
 * @since JDK 1.7
 */

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class BizoppServiceImpl extends BaseServiceImpl implements IBizoppService {

    @Autowired
    BaseDao baseDao;

    @Autowired
    IQuotService quotService;

    @Autowired
    IContractService contractService;

    @Autowired
    ILovMemberService lovMemberService;

    @Autowired
    IBizBaseService bizService;

    @Autowired
    IProcessService processService;

    @Autowired
    ProcessStatusService processStatusService;

    @Autowired
    ITeamService teamService;

    @Autowired
    IXflowProcessServiceWrapper xflowProcessServiceWrapper;

    @Autowired
    IOrgTeamService orgTeamService;

    @Autowired
    IUserService userService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public IPage query(PageCondition condition) {
        FilterObject fo = condition.getFilterObject(BusinessOpportunity.class);
        HqlObject ho = HqlUtil.getHqlObject(fo);
        String hql = ho.getHql();
        return baseDao.search(hql, ho.getArgs(), condition.getRows(), condition.getPage());
    }

    @Override
    public IPage query(PageCondition condition, UserObject user) {

        List<Object> args = new ArrayList<Object>();
        StringBuffer sb = new StringBuffer();

        sb.append(" select ");
        sb.append(" b ");
        sb.append(" from ");
        sb.append(" BusinessOpportunity b ");
        sb.append(" ,LovMember l ");
        sb.append(" where ");
        sb.append(" b.status = l.code ");
        sb.append(" and l.groupCode = 'OPPORTUNITY_STATUS' ");
        String perHql = PermissionUtil.getPermissionHQL(null, "b.createdById", "b.createdPosId", "b.createdOrgId", "b.id", user, "BusinessOpportunity");
        sb.append(" and  ");
        sb.append(perHql);

        Map<String, Object> map = condition.getConditionMap();

        if (map.size() > 0) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getKey().startsWith("pageSearch_") && entry.getValue() != null && !"".equals(entry.getValue())) {
                    String str = entry.getKey();
                    String propName = str.replaceAll("pageSearch_", "");
                    propName = " and UPPER(b." + propName+")";
                    String value = (String) entry.getValue();
                    if (entry.getKey().endsWith("_begin")) {
                        sb.append(propName.replaceAll("_begin", "")).append(" >= to_date(?,'yyyy-mm-dd') ");
                        args.add(entry.getValue());
                    } else if (entry.getKey().endsWith("_end")) {
                        sb.append(propName.replaceAll("_end", "")).append(" <= to_date(?,'yyyy-mm-dd') ");
                        args.add(entry.getValue());
                    } else if (entry.getKey().endsWith("_like")) {
                        sb.append(propName.replaceAll("_like", "")).append(" like ? ");
                        args.add("%" + StringUtil.getSearchString(value.toUpperCase()) + "%");
                    } else {
                        sb.append(propName).append(" = ? ");
                        args.add(StringUtil.getSearchString(entry.getValue()));
                    }
                }
            }
        }

        String status = condition.getStringCondition("status");
        if (!StringUtils.isEmpty(status)) {
            sb.append(" and b.status = '15'");
        }

        sb.append(" order by l.no asc, b.updatedAt desc ");


        return baseDao.search(sb.append(perHql).toString(), args.toArray(), condition.getRows(), condition.getPage());
    }


    @Override
    public IPage queryBo(PageCondition condition, UserObject user) {


        //language=SQL
        List<Object> args = new ArrayList<Object>();
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT DISTINCT b.C_PID AS \"id\"," +
                "b.C_OPPORTUNITY_NAME AS \"opportunityName\"," +
                "b.C_NUMBER AS \"number\", " +
                "b.C_BIZ_OPP_ADDRESS_NAME AS \"bizOppAddressName\"," +
                "b.C_BIZ_OPP_ADDRESS AS \"bizOppAddress\"," +
                "b.C_STATUS AS \"status\"," +
                "b.C_SALE_STAGE AS \"saleStage\"," +
                "b.C_CONFLICT_STATUS AS \"conflictStatus\"" +
                " FROM CRM_T_BUSINESS_OPPORTUNITY b,SYS_T_LOV_MEMBER L,CRM_T_BIZOPP_PRODUCTS_DETAIL pd " +
                " WHERE b.C_STATUS=L.LOV_CODE AND L.GROUP_CODE='OPPORTUNITY_STATUS' AND b.C_PID=pd.C_BIZOPP_ID ");

        String permissionSql = PermissionUtil.getPermissionSQL(null, "b.CREATED_BY_ID", "b.CREATED_POS_ID", "b.CREATED_ORG_ID", "b.C_PID", user, "BusinessOpportunity");
        sb.append(" and ").append(permissionSql);


        Map<String, Object> map = condition.getConditionMap();
        String createdOrgId = (String) map.get("pageSearch_createdOrgId");
        String productId = (String) map.get("pageSearch_productId");

        if (!StringUtil.isNullOrEmpty(createdOrgId)) {
            sb.append(" and b.CREATED_ORG_ID = ? ");
            args.add(createdOrgId);
        }

        if (!StringUtil.isNullOrEmpty(productId)) {
            sb.append(" and pd.C_PRODUCT_ID = ? ");
            args.add(productId);
        }

        sb.append(" and b.C_CONFLICT_STATUS = '40'");

        String status = condition.getStringCondition("status");
        if (!StringUtils.isEmpty(status)) {
            sb.append(" and b.C_STATUS = '15'");
        }

        //usable:只查询特价单中剩余数量大于0的数据
        String queryUsable = condition.getStringCondition("usable");
        String orderNo = condition.getStringCondition("orderNo");
        String productModel = condition.getStringCondition("productModel");
        if (StringUtils.isNotEmpty(queryUsable) && StringUtils.isNotEmpty(productModel)) {

            //language=SQL
            String bizOppSumSql = "SELECT C_BIZOPP_ID as bizId,C_PRODUCT_MODEL as productModel,sum(N_PLAN_COUNT) as bizQty " +
                    "from CRM_T_BIZOPP_PRODUCTS_DETAIL " +
                    "WHERE C_BIZOPP_ID=b.C_PID  GROUP BY C_BIZOPP_ID,C_PRODUCT_MODEL";

            List<Object> orderSumArgs = new ArrayList<>();
            //language=SQL
            String orderSumSql = "SELECT\n" +
                    "        bizId,\n" +
                    "        productModel,\n" +
                    "        sum(orderQty) AS orderQty\n" +
                    "      FROM (WITH Order_Line AS (SELECT ol.C_ORDER_ID as orderId, ol.C_PID as orderLineId\n" +
                    "                                 FROM CRM_T_ORDER_LINES ol,CRM_T_ORDER_HEADER oh\n" +
                    "                                 WHERE oh.C_PID=ol.C_ORDER_ID and oh.C_SOURCE_TYPE='20'     ";
            if (StringUtils.isNotEmpty(orderNo)) {
                orderSumSql += " AND oh.C_ORDER_CODE != ? ";
                orderSumArgs.add(orderNo);
            }

            orderSumSql += "),\n" +
                    "          Order_Change AS (SELECT\n" +
                    "                       ohc.C_FROM_ID          AS orderId,\n" +
                    "                       max(ohc.C_VERSION) AS C_VERSION\n" +
                    "                     FROM CRM_T_ORDER_HEADER_CHANGE ohc,CRM_T_ORDER_LINES_CHANGE olc\n" +
                    "                     WHERE ohc.C_PID=olc.C_ORDER_CHANGE_ID and  ohc.C_EVENT_STATUS != '40' ";
            if (StringUtils.isNotEmpty(orderNo)) {
                orderSumSql += " and ohc.C_ORDER_CODE != ?\n";
                orderSumArgs.add(orderNo);
            }
            orderSumSql +=
                    "                     GROUP BY ohc.C_FROM_ID),\n" +
                            "          Order_Product_Count AS (SELECT\n" +
                            "                                    ol.C_ORDER_ID         AS orderId,\n" +
                            "                                    ol.C_PRO_MODEL        AS productModel,\n" +
                            "                                    ol.N_PRODUCT_QUANTITY AS orderQty,\n" +
                            "                                    ol.C_SOURCE_ID        AS bizId\n" +
                            "                                   FROM CRM_T_ORDER_HEADER oh, CRM_T_ORDER_LINES ol\n" +
                            "                                   WHERE oh.C_PID=ol.C_ORDER_ID\n" +
                            "                                         AND oh.C_CONTROL_STATUS != '40'\n" +
                            "                                         AND ol.C_ORDER_ID NOT IN (SELECT orderId FROM Order_Change)\n" +
                            "                                         AND ol.C_ORDER_ID IN (SELECT orderId FROM Order_Line)),\n" +
                            "          Order_Change_Product_Count AS (SELECT\n" +
                            "                                            olc.C_ORDER_ID         AS orderId,\n" +
                            "                                            olc.C_PRO_MODEL        AS productModel,\n" +
                            "                                            olc.N_PRODUCT_QUANTITY AS orderQty,\n" +
                            "                                            olc.C_SOURCE_ID        AS bizId\n" +
                            "                                          FROM CRM_T_ORDER_HEADER_CHANGE ohc, CRM_T_ORDER_LINES_CHANGE olc\n" +
                            "                                          WHERE ohc.C_PID = olc.C_ORDER_CHANGE_ID\n" +
                            "                                                AND (ohc.C_FROM_ID,ohc.C_VERSION) IN (SELECT orderId,C_VERSION FROM Order_Change))\n" +
                            "      SELECT * FROM Order_Product_Count r WHERE orderId NOT IN (SELECT orderId FROM Order_Change)\n" +
                            "      UNION ALL SELECT * FROM Order_Change_Product_Count)\n" +
                            "      GROUP BY bizId, productModel";
            List<Object> queryUsableArgs = new ArrayList<>();
            //language=SQL
            String queryUsableSql = "SELECT pd.bizId, pd.productModel,  nvl(pd.bizQty, 0) - nvl(rl.orderQty, 0) as surplusSum " +
                    "FROM (" + bizOppSumSql + ") pd  " +
                    "LEFT JOIN (" + orderSumSql + ") rl ON pd.bizId = rl.bizId AND pd.productModel = rl.productModel";
            queryUsableArgs.addAll(orderSumArgs);

            sb.append(" and b.C_PID in (select bizId from (" + queryUsableSql + ") where surplusSum>0 and productModel=?)");
            args.addAll(queryUsableArgs);
            args.add(productModel);
        }

        List<Map<String, Object>> maps = baseDao.findBySql4Map(sb.toString(), args.toArray());
        List<BusinessOpportunity> list = new ArrayList<>();
        for (Map<String, Object> objectMap : maps) {

            try {
                BusinessOpportunity o = (BusinessOpportunity) BeanUtils.convertMap(BusinessOpportunity.class, objectMap, false);
                list.add(o);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String ordersearch = condition.getStringCondition("ordersearch");
        if (user != null && !user.isInner() && StringUtil.isEmpty(ordersearch)) {
            list.add(new BusinessOpportunity("xuni"));
        }
        return new PageImpl(list, 1, 100, list.size());
    }


    @Override
    public IPage queryProPrice(PageCondition condition) {
        FilterObject filterObject = condition.getFilterObject(KstarQuot.class);
        HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
        return baseDao.search(hqlObject.getHql(), hqlObject.getArgs(), condition.getRows(), condition.getPage());
    }

    @Override
    public IPage queryDecChainContact(PageCondition condition) {
        FilterObject filterObject = condition.getFilterObject(BizContact.class);
        HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
        return baseDao.search(hqlObject.getHql(), hqlObject.getArgs(), condition.getRows(), condition.getPage());
    }

    @Override
    public IPage queryWeekly(PageCondition condition) {
        FilterObject filterObject = condition.getFilterObject(InternationWeekly.class);
        HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
        return baseDao.search(hqlObject.getHql(), hqlObject.getArgs(), condition.getRows(), condition.getPage());
    }

    @Override
    public IPage queryWeeklyReport(PageCondition condition) {
        FilterObject filterObject = condition.getFilterObject(InternationWeeklyReport.class);
        HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
        return baseDao.search(hqlObject.getHql(), hqlObject.getArgs(), condition.getRows(), condition.getPage());
    }

    @Override
    public IPage queryProductDetail(PageCondition condition) {
        FilterObject filterObject = condition.getFilterObject(ProductDetail.class);
        HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
        return baseDao.search(hqlObject.getHql(), hqlObject.getArgs(), condition.getRows(), condition.getPage());
    }

    @Override
    public IPage queryBizOppOrg(PageCondition condition) {
        FilterObject filterObject = condition.getFilterObject(BizOrg.class);
        HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
        return baseDao.search(hqlObject.getHql(), hqlObject.getArgs(), condition.getRows(), condition.getPage());
    }

    @Override
    public IPage queryBizCompetitor(PageCondition condition) {
        FilterObject filterObject = condition.getFilterObject(BizCompetitor.class);
        HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
        return baseDao.search(hqlObject.getHql(), hqlObject.getArgs(), condition.getRows(), condition.getPage());
    }

    @Override
    public IPage querySupportApply(PageCondition condition) {
        FilterObject filterObject = condition.getFilterObject(SupportApply.class);
        HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
        return baseDao.search(hqlObject.getHql(), hqlObject.getArgs(), condition.getRows(), condition.getPage());
    }

    @Override
    public IPage queryPrototypeApplyReturn(PageCondition condition) {
        FilterObject filterObject = condition.getFilterObject(PrototypeApplyReturn.class);
        HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
        return baseDao.search(hqlObject.getHql(), hqlObject.getArgs(), condition.getRows(), condition.getPage());
    }

    @Override
    public IPage queryBid(PageCondition condition) {
        FilterObject filterObject = condition.getFilterObject(Bid.class);
        HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
        return baseDao.search(hqlObject.getHql(), hqlObject.getArgs(), condition.getRows(), condition.getPage());
    }

    @SuppressWarnings("unchecked")
    @Override
    public IPage queryConflictBizOpp(PageCondition condition, String bizId, String industry, String industrySub) {
        //存储商机ID
        //		StringBuffer bizOppIdBuf = new StringBuffer();
        //存储商机在二维数组中的位置
        //		Map<String, Object> position = new HashMap<String, Object>();

        BusinessOpportunity businessOpportunity = getBizOppEntity(bizId);
        //商机所在地
        //		String bizOppAddressName = businessOpportunity.getBizOppAddressName();

        List<Object> args = new ArrayList<Object>();
        StringBuffer sb = new StringBuffer();
        sb.append(" from ");
        sb.append(" BusinessOpportunity ");
        sb.append(" where ");
        sb.append(" 1 = 1 ");
        if (StringUtil.equals("1", condition.getStringCondition("layer2Value"))) {
            sb.append(" and layer2 = ? ");
            args.add(businessOpportunity.getLayer2());
        }

        if (StringUtil.equals("1", condition.getStringCondition("layer3Value"))) {
            sb.append(" and layer3 = ? ");
            args.add(businessOpportunity.getLayer3());
        }

        if (StringUtil.equals("1", condition.getStringCondition("industryValue"))) {
            sb.append(" and industry = ? ");
            args.add(industry);
        }

        if (StringUtil.equals("1", condition.getStringCondition("industrySubValue"))) {
            sb.append(" and industrySub = ? ");
            args.add(industrySub);
        }

        sb.append(" and  id != ? and createdAt > ADD_MONTHS(sysdate,-3) and rownum < 20 ");
        args.add(bizId);

        //查询冲突的商机信息，最多查询20条
        IPage p = baseDao.search(sb.toString(), args.toArray(), 20, 1);

        List<BusinessOpportunity> oldBizOppList = (List<BusinessOpportunity>) p.getList();
        //原商机加入
        ListIterator<BusinessOpportunity> oldIt = oldBizOppList.listIterator();
        oldIt.add(businessOpportunity);


        String[][] convertArr = new String[25][21];
        //初始化商机属性列
        String[] bizOppPropertyArr = {
                "商机名称",
                "省份",
                "城市",
                "区县",
                "终端用户名称",
                "商机状态",
                "报备单位",
                "报备时间",
                "行业大类",
                "行业小类",
                "总金额",
                "产品系列1",
                "产品系列2",
                "产品系列3",
                "产品系列4",
                "产品系列5",
                "产品系列6",
                "授权单位1",
                "授权单位2",
                "授权单位3",
                "授权单位4",
                "授权单位5",
                "授权单位6",
                "授权单位7",
                "id"};
        for (int i = 0; i < 25; i++) {
            convertArr[i][0] = bizOppPropertyArr[i];
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        DecimalFormat df = new DecimalFormat("#");
        //商机冲突记录转成按列存储，暂存到一个二维数据中
        int j = 1;
        for (BusinessOpportunity bigOppEntity : oldBizOppList) {

            String conflictStatusName = "";
            if (!StringUtil.isEmpty(bigOppEntity.getConflictStatus())) {
                LovMember lov = (LovMember) CacheData.getInstance().get("CONFLICT_STATUS_" + bigOppEntity.getConflictStatus());

                conflictStatusName = (lov == null ? "" : lov.getName());
            }


            //商机名称
            convertArr[0][j] = bigOppEntity.getOpportunityName() == null ? "" : bigOppEntity.getOpportunityName();
            //省份
            convertArr[1][j] = CacheData.getInstance().getLovMemberName(bigOppEntity.getLayer2());
            //城市
            convertArr[2][j] = CacheData.getInstance().getLovMemberName(bigOppEntity.getLayer3());
            //区县
            convertArr[3][j] = CacheData.getInstance().getLovMemberName(bigOppEntity.getLayer4());

            //详细地址
            //终端用户名称
            convertArr[4][j] = bigOppEntity.getClientName() == null ? "" : bigOppEntity.getClientName();
            //状态
            convertArr[5][j] = conflictStatusName;
            //报备单位
            convertArr[6][j] = bigOppEntity.getEnterpriseName() == null ? "" : bigOppEntity.getEnterpriseName();
            //报备时间
            convertArr[7][j] = format.format(bigOppEntity.getCreatedAt());
            //行业大类
            convertArr[8][j] = bigOppEntity.getIndustry();
            //行业小类
            convertArr[9][j] = bigOppEntity.getIndustrySub();
            convertArr[10][j] = "";
            convertArr[11][j] = "";
            convertArr[12][j] = "";
            convertArr[13][j] = "";
            convertArr[14][j] = "";
            convertArr[15][j] = "";
            convertArr[16][j] = "";
            convertArr[17][j] = "";
            convertArr[18][j] = "";
            convertArr[19][j] = "";
            convertArr[20][j] = "";
            convertArr[21][j] = "";
            convertArr[22][j] = "";
            convertArr[23][j] = "";
            double total = 0d;
            List<Object> objs = getDetailList(bigOppEntity.getId());
            convertArr[10][j] = "0";
            for (int i = 0; i < objs.size(); i++) {
                Object[] objArr = (Object[]) objs.get(i);

                String productId = (String) objArr[1];
                String planTotal = String.valueOf(objArr[4]);
                String productSeries = getProductSeries(productId);
                total = Double.valueOf(total) + Double.valueOf(planTotal);
                convertArr[11 + i][j] = productSeries;
                if (!StringUtil.isEmpty(planTotal)) {
                    convertArr[10][j] = String.valueOf(df.format(total));
                }
            }

            convertArr[24][j] = bigOppEntity.getId() == null ? "" : bigOppEntity.getId();

            List<Object> authObjs = getAuthDetailList(bigOppEntity.getId());
            for (int i = 0; i < authObjs.size(); i++) {
                Object[] objArr = (Object[]) authObjs.get(i);

                String integrator = (String) objArr[1];

                convertArr[17 + i][j] = integrator;
            }
            j++;

        }

        List<BizConflict> newBizOppList = new ArrayList<BizConflict>();
        //从二维数据中得到转换成列的冲突商机数据
        for (int i = 0; i < 25; i++) {
            BizConflict bizConflict = new BizConflict();

            bizConflict.setBizOppProperty(convertArr[i][0]);
            bizConflict.setBizOpp1(convertArr[i][1]);
            bizConflict.setBizOpp2(convertArr[i][2]);
            bizConflict.setBizOpp3(convertArr[i][3]);
            bizConflict.setBizOpp4(convertArr[i][4]);
            bizConflict.setBizOpp5(convertArr[i][5]);
            bizConflict.setBizOpp6(convertArr[i][6]);
            bizConflict.setBizOpp7(convertArr[i][7]);
            bizConflict.setBizOpp8(convertArr[i][8]);
            bizConflict.setBizOpp9(convertArr[i][9]);
            bizConflict.setBizOpp10(convertArr[i][10]);
            bizConflict.setBizOpp11(convertArr[i][11]);
            bizConflict.setBizOpp12(convertArr[i][12]);
            bizConflict.setBizOpp13(convertArr[i][13]);
            bizConflict.setBizOpp14(convertArr[i][14]);
            bizConflict.setBizOpp15(convertArr[i][15]);
            bizConflict.setBizOpp16(convertArr[i][16]);
            bizConflict.setBizOpp17(convertArr[i][17]);
            bizConflict.setBizOpp18(convertArr[i][18]);
            bizConflict.setBizOpp19(convertArr[i][19]);
            bizConflict.setBizOpp20(convertArr[i][20]);

            newBizOppList.add(bizConflict);
        }

        p = new PageImpl(newBizOppList, 1, 25, 1);
        return p;
    }

    private List<Object> getDetailList(String bizOppId) {
        StringBuilder sql = new StringBuilder();
        // 查询订单表中对应销售员的订单
        sql.append("select * from ");
        sql.append("( ");
        sql.append(" select c_bizopp_id, ");
        sql.append("c_product_id,  ");
        sql.append("c_product_model,  ");
        sql.append("n_plan_count, ");
        sql.append("n_plan_total, ");
        sql.append("row_number() over (partition by c_bizopp_id order by n_plan_total desc ) as rn ");
        sql.append(" from crm_t_bizopp_products_detail");
        sql.append(" where c_bizopp_id in ('");
        sql.append(bizOppId);
        sql.append("') ");
        sql.append(") ");
        sql.append("where rn <= 6");

        Object reValue = (Object) baseDao.findBySql(sql.toString());

        List<Object> objs = (List<Object>) reValue;
        return objs;
    }

    private List<Object> getAuthDetailList(String bizOppId) {
        StringBuilder sql = new StringBuilder();
        //        sql.append("select * from ");
        //        sql.append("( ");
        //        sql.append(" select c_business_opp_id, ");
        //        sql.append(" c_integrator,  ");
        //        sql.append("row_number() over (partition by c_business_opp_id order by dt_created_at desc ) as rn "); // TODO sort
        //        sql.append(" from crm_t_bid_auth_unit");
        //        sql.append(" where c_business_opp_id in ('");
        //        sql.append(bizOppId);
        //        sql.append("') ");
        //        sql.append(") ");
        //        sql.append("where rn <= 7");

        sql.append("select * from CRM_T_BIZOPP_INTEGRATOR where BIZ_OPP_ID = ? ");

        Object reValue = (Object) baseDao.findBySql(sql.toString(), new String[]{bizOppId});

        List<Object> objs = (List<Object>) reValue;
        return objs;
    }

    @SuppressWarnings("unchecked")
    @Override
    public IPage queryPi(PageCondition condition, String weeklyId) throws AnneException {
        StringBuffer sql = new StringBuffer();
        List<Object> args = new ArrayList<Object>();
        sql.append("select ");
        sql.append("C_ID as id, ");
        sql.append("C_CONTR_NO as contrNo， ");
        sql.append("C_CONTR_NAME as contrName， ");
        sql.append("C_CUST_NAME as custName, ");
        sql.append("DT_CREATE_TIME as createTime, ");
        sql.append("DT_DELIV_DATE as delivDate, ");
        sql.append("N_TOTAL_AMT as totalAmt，");
        sql.append("C_CONTR_VER as contrVer ");
        sql.append("from crm_t_contr_basic c where c.C_CUST_CODE in ( select wr.C_FK_CLIENT_ID from crm_t_biz_weekly_report wr where c_fk_weekly_id = ?)");

        args.add(weeklyId);

        IPage page = baseDao.searchBySql4Map(sql.toString(), args.toArray(), condition.getRows(), condition.getPage());

        List<Map<Object, Object>> list = (List<Map<Object, Object>>) page.getList();
        List<Contract> taskList = new ArrayList<Contract>();
        for (Map<Object, Object> map : list) {
            Map<Object, Object> map1 = new CaseInsensitiveMap();
            for (Object key : map.keySet()) {
                map1.put(key, map.get(key));
            }
            try {
                taskList.add((Contract) BeanUtils.convertMap(Contract.class, map1));
            } catch (Exception e) {
                e.printStackTrace();
                throw new AnneException("查询PI合同失败");
            }
        }

        ((PageImpl) page).setList(taskList);
        return page;
    }

    @Override
    public IPage queryBizOppProductSelectList(PageCondition condition, UserObject userObject) {
        String bizOppId = condition.getStringCondition("bizOppId");
        String productName = condition.getStringCondition("productName");
        //		String orderCode =condition.getStringCondition("productType");
        String productModel = condition.getStringCondition("productModel");

        List<Object> args = new ArrayList<Object>();
        StringBuffer sql = new StringBuffer();
        sql.append("select ");
        sql.append(" prod.c_pid as id,");
        sql.append(" prod.c_product_id as productId,");
        sql.append(" prod.c_bizopp_id as bizOppId,");
        sql.append(" prod.c_product_name as productName,");
        sql.append(" prod.c_product_type as productType,");
        sql.append(" prod.c_product_model as productModel,");
        sql.append(" prod.n_plan_count as planCount,");
        sql.append(" prod.n_plan_price as planPrice,");
        sql.append(" prod.n_plan_total as planTotal,");
        sql.append(" prod.c_remark as remark,");
        sql.append(" prod.n_is_standard as isStandard");
        sql.append(" from crm_t_bizopp_products_detail prod");
        sql.append(" where 1 = 1 ");

        if (StringUtil.isNotEmpty(bizOppId)) {
            sql.append(" and prod.c_bizopp_id = ?");
            args.add(bizOppId);
        }
        //		if(StringUtil.isNotEmpty(productName)){
        //			hql.append(" and p.productName like ?");
        //			args.add("%"+productName+"%");
        //		}
        if (StringUtil.isNotEmpty(productModel)) {
            sql.append(" and prod.c_product_model like ? ");
            args.add("%" + productModel + "%");
        }

        String permissionSqlSub = PermissionUtil.getPermissionSQL(null,
                "created_by_id",
                "created_pos_id",
                "created_org_id",
                "prod.c_bizopp_id",
                userObject,
                "BusinessOpportunity");

        if (StringUtil.isNotEmpty(permissionSqlSub)) {
            sql.append(" and " + permissionSqlSub);
        }

        IPage page = baseDao.searchBySql4Map(sql.toString(), args.toArray(), condition.getRows(), condition.getPage());

        List<Map<Object, Object>> list = (List<Map<Object, Object>>) page.getList();
        List<ProductDetail> taskList = new ArrayList<ProductDetail>();
        for (Map<Object, Object> map : list) {
            Map<Object, Object> map1 = new CaseInsensitiveMap();
            for (Object key : map.keySet()) {
                map1.put(key, map.get(key));
            }
            try {
                ProductDetail pd = (ProductDetail) BeanUtils.convertMap(ProductDetail.class, map1);
                BusinessOpportunity bu = getBizOppEntity(pd.getBizOppId());
                if (bu != null) {
                    pd.setBizOppName(bu.getOpportunityName());
                }
                taskList.add(pd);
            } catch (Exception e) {
                e.printStackTrace();
                throw new AnneException("查询失败");
            }
        }

        ((PageImpl) page).setList(taskList);

        return page;

    }

    @Override
    public IPage querySpePrice(PageCondition condition) {
        FilterObject filterObject = condition.getFilterObject(SpecialPriceLine.class);
        HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
        return baseDao.search(hqlObject.getHql(), hqlObject.getArgs(), condition.getRows(), condition.getPage());
    }

    @Override
    public InternationWeeklyReport getWeeklyReport(String id) {
        return baseDao.get(InternationWeeklyReport.class, id);
    }

    @Override
    public ProductDetail getProducrDetailEntity(String id) {
        return baseDao.get(ProductDetail.class, id);
    }

    @Override
    public SupportApply getSupportApplyEntity(String id) {
        return baseDao.get(SupportApply.class, id);
    }

    @Override
    public BizOrg getBizOrgEntity(String id) {
        return baseDao.get(BizOrg.class, id);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void saveWeekly(InternationWeekly internationWeekly) {
        baseDao.save(internationWeekly);

        //得到销售人员ID
        String salesManId = internationWeekly.getSalesid();
        //得到周报主表ID
        String weeklyId = internationWeekly.getId();

        StringBuilder sql = new StringBuilder();
        //查询订单表中对应销售员的订单
        sql.append("select h.c_customer_id, h.c_customer_name, l.N_AMOUNT, l.N_PRICE from crm_t_order_header h  inner join CRM_T_ORDER_LINES l on h.c_pid = l.C_ORDER_ID where h.C_SALESMAN_ID =");
        sql.append("'");
        sql.append(salesManId);
        sql.append("'");

        Object reValue = (Object) baseDao.findBySql(sql.toString());

        List<InternationWeeklyReport> weeklyReportList = new ArrayList<InternationWeeklyReport>();
        List<Object> objs = (List<Object>) reValue;

        for (Object obj : objs) {
            InternationWeeklyReport internationWeeklyReport = new InternationWeeklyReport();
            Object[] objArr = (Object[]) obj;
            internationWeeklyReport.setFkClientId((String) objArr[0]);
            internationWeeklyReport.setClientName((String) objArr[1]);
            internationWeeklyReport.setNotShippedNextMonth((BigDecimal) objArr[2]);
            internationWeeklyReport.setNotShippedNNMonth((BigDecimal) objArr[3]);
            internationWeeklyReport.setFkWeeklyId(weeklyId);
            weeklyReportList.add(internationWeeklyReport);
        }
        //保存进展汇报数据
        baseDao.save(weeklyReportList);

        teamService.addPosition(internationWeekly.getCreatedPosId(), internationWeekly.getCreatedById(), "InternationWeekly", internationWeekly.getId());

        orgTeamService.configPrimaryOrg(internationWeekly.getId(), "InternationWeekly", internationWeekly.getCreatedPosId());
    }

    @Override
    public void saveBizOrg(BizOrg bizOrg) {
        baseDao.save(bizOrg);
    }

    @Override
    public void saveBizContact(BizContact bizContact) {
        baseDao.save(bizContact);
    }

    @Override
    public void saveBizCompetitor(BizCompetitor bizCompetitor) {
        baseDao.save(bizCompetitor);
    }

    //	@Autowired
    //	IBizoppService bizoppService;

    @Override
    public void saveSupportApply(SupportApply supportApply, UserObject userObject) {

        setReportPrice(supportApply);
        baseDao.save(supportApply);
        System.out.println("====》》》启动流程:" + supportApply.getBizOppName());
        teamService.addPosition(supportApply.getCreatedPosId(), supportApply.getCreatedById(), "SupportApply", supportApply.getId());
        orgTeamService.configPrimaryOrg(supportApply.getId(), "SupportApply", userObject.getPosition().getId());
        //startProcess(userObject,supportApply.getId(),supportApply.getBizOppName());
    }

    private void setReportPrice(SupportApply supportApply) {
        BigDecimal sum = new BigDecimal(0);
        if (StringUtils.isNotEmpty(supportApply.getBizOppId())) {
            List<ProductDetail> list = baseDao.findEntity("from ProductDetail productdetail where 1=1  and productdetail.bizOppId  =  ? ", supportApply.getBizOppId());
            for (ProductDetail pd : list) {
                sum = sum.add(pd.getPlanTotal());
            }
        }
        supportApply.setReportPrice(sum.doubleValue());
    }

    @Override
    public void savetWeeklyReport(InternationWeeklyReport internationWeeklyReport) {
        baseDao.save(internationWeeklyReport);
    }

    @Autowired
    ICustomNumberService customNumberService;

    @Override
    public void save(BusinessOpportunity bizopp, UserObject userObject) {
        bizopp.setCreatedAt(new Date());

        if ("E".equals(userObject.getEmployee().getFlag())) {
            // 外部人员
            LovMember industry = lovMemberService.get(bizopp.getIndustry());
            LovMember industrySub = lovMemberService.get(bizopp.getIndustrySub());

            bizopp.setIndustryCode(bizopp.getIndustry());
            bizopp.setIndustry(industry.getName());

            bizopp.setIndustrySubCode(bizopp.getIndustrySub());
            bizopp.setIndustrySub(industrySub.getName());
        }

        getAddressName(bizopp);
        baseDao.save(bizopp);
        //外部人员创建的商机增加客户信息。
        if ("E".equals(userObject.getEmployee().getFlag()) && StringUtil.isEmpty(bizopp.getClientId())) {
            // 客户不需要生成  2017-07-11
            //			CustomInfo customInfo = new CustomInfo();

            //			customInfo.setCustomFullName(bizopp.getClientName());
            //
            //			LovMember industry = lovMemberService.get(bizopp.getIndustry());
            //			LovMember industrySub = lovMemberService.get(bizopp.getIndustrySub());
            //
            //			String industryName = industry == null ? null : industry.getName();
            //			String industrySubName = industrySub == null ? null : industrySub.getName();
            //
            //			customInfo.setCustomCategory(industryName);
            //			customInfo.setCustomCategorySub(industrySubName);
            //			customInfo.setContactName(bizopp.getKeyContact());
            //			customInfo.setContactDept(bizopp.getContactDept());
            //			baseDao.save(customInfo);
            //
            //			teamService.addPosition(
            //					userObject.getPosition().getId(),
            //					userObject.getEmployee().getId(),
            //					IConstants.CUSTOM_REPORT_PROC,
            //					customInfo.getId());
            //
            //			orgTeamService.configPrimaryOrg(customInfo.getId(), IConstants.CUSTOM_REPORT_PROC, userObject.getOrg().getId());
            //
            //			// lov
            //			LovMember lovMember = new LovMember();
            //			lovMember.setCode(customNumberService.getReportNumber());
            //			lovMember.setGroupId(IConstants.CUSTOM_ORG_TREE);
            //			lovMember.setName(customInfo.getCustomFullName());
            //			lovMember.setLeafFlag("N");
            //
            //			lovMemberService.save(lovMember);

        } else {
            teamService.copyPosition(bizopp.getClientId(), "CUSTOM_REPORT_PROC", bizopp.getId(), "BusinessOpportunity", bizopp.getCreatedById());
        }

        teamService.addPosition(bizopp.getCreatedPosId(),
                bizopp.getCreatedById(),
                "BusinessOpportunity",
                bizopp.getId());
        orgTeamService.configPrimaryOrg(bizopp.getId(), "BusinessOpportunity", bizopp.getCreatedPosId());

    }

    private void getAddressName(BusinessOpportunity bizopp) {
        LovMember l1 = lovMemberService.get(bizopp.getLayer1());
        LovMember l2 = lovMemberService.get(bizopp.getLayer2());
        LovMember l3 = lovMemberService.get(bizopp.getLayer3());
        LovMember l4 = lovMemberService.get(bizopp.getLayer4());

        String ls1 = l1 == null ? "" : "/" + l1.getName();
        String ls2 = l2 == null ? "" : "/" + l2.getName();
        String ls3 = l3 == null ? "" : "/" + l3.getName();
        String ls4 = l4 == null ? "" : "/" + l4.getName();
        String bizOppAddressName = ls1 + ls2 + ls3 + ls4;
        if (!StringUtil.isEmpty(bizOppAddressName)) {
            bizOppAddressName = bizOppAddressName.substring(1);
        }

        bizopp.setBizOppAddressName(bizOppAddressName);
    }

    @Override
    public void saveProductDetail(ProductDetail productDetail) {
        baseDao.save(productDetail);
    }

    @Override
    public BusinessOpportunity getBizOppEntity(String id) {
        return baseDao.get(BusinessOpportunity.class, id);
    }

    @Override
    public Bid getBidEntity(String id) {
        return baseDao.get(Bid.class, id);
    }

    @Override
    public InternationWeekly getWeeklyEntity(String id) {
        return baseDao.get(InternationWeekly.class, id);
    }

    @Override
    public BizContact getBizContactEntity(String id) {
        return baseDao.get(BizContact.class, id);
    }

    @Override
    public BizCompetitor getBizCompetitorEntity(String id) {
        return baseDao.get(BizCompetitor.class, id);
    }

    @Override
    public void updateProductDetail(ProductDetail productDetail) {
        ProductDetail oldProductDetail = baseDao.get(ProductDetail.class, productDetail.getId());
        if (oldProductDetail == null) {
            throw new AnneException(IBizoppService.class.getName() + " save : 没有找到要更新的数据");
        }
        BeanUtils.copyPropertiesIgnoreNull(productDetail, oldProductDetail);
        baseDao.update(oldProductDetail);
    }

    @Override
    public void update(BusinessOpportunity bizopp, UserObject userObject) {
        BusinessOpportunity oldbizopp = baseDao.get(BusinessOpportunity.class, bizopp.getId());
        if ("E".equals(userObject.getEmployee().getFlag())) {
            // 外部人员
            LovMember industry = lovMemberService.get(bizopp.getIndustry());
            LovMember industrySub = lovMemberService.get(bizopp.getIndustrySub());

            bizopp.setIndustryCode(bizopp.getIndustry());
            bizopp.setIndustry(industry.getName());

            bizopp.setIndustrySubCode(bizopp.getIndustrySub());
            bizopp.setIndustrySub(industrySub.getName());
        }

        if (oldbizopp == null) {
            throw new AnneException(IBizoppService.class.getName() + " save : 没有找到要更新的数据");
        }
        BeanUtils.copyPropertiesIgnoreNull(bizopp, oldbizopp);
        oldbizopp.setUpdatedAt(new Date());
        getAddressName(oldbizopp);
        baseDao.update(oldbizopp);
    }

    @Override
    public void updateWeekly(InternationWeekly internationWeekly) {
        InternationWeekly oldInternationWeekly = baseDao.get(InternationWeekly.class, internationWeekly.getId());
        if (oldInternationWeekly == null) {
            throw new AnneException(IBizoppService.class.getName() + " save : 没有找到要更新的数据");
        }
        BeanUtils.copyProperties(internationWeekly, oldInternationWeekly);
        baseDao.update(oldInternationWeekly);
    }

    @Override
    public void updateWeeklyReport(InternationWeeklyReport internationWeeklyReport) {
        InternationWeeklyReport oldInternationWeeklyReport = baseDao.get(InternationWeeklyReport.class, internationWeeklyReport.getId());
        if (oldInternationWeeklyReport == null) {
            throw new AnneException(InternationWeeklyReport.class.getName()
                    + " internationWeeklyReport : 没有找到要更新的数据");
        }
        BeanUtils.copyProperties(internationWeeklyReport, oldInternationWeeklyReport);
        baseDao.update(oldInternationWeeklyReport);
    }

    @Override
    public void updateWeeklyReportLine(InternationWeeklyReport internationWeeklyReport) {
        InternationWeeklyReport oldInternationWeeklyReport = baseDao.get(InternationWeeklyReport.class, internationWeeklyReport.getId());
        if (oldInternationWeeklyReport == null) {
            throw new AnneException(InternationWeeklyReport.class.getName()
                    + " internationWeeklyReport : 没有找到要更新的数据");
        }
        BeanUtils.copyPropertiesIgnoreNull(internationWeeklyReport, oldInternationWeeklyReport);
        baseDao.update(oldInternationWeeklyReport);
    }

    @Override
    public void updateBizOrg(BizOrg bizOrg) {
        BizOrg oldbizOrg = baseDao.get(BizOrg.class, bizOrg.getId());
        if (oldbizOrg == null) {
            throw new AnneException(InternationWeeklyReport.class.getName()
                    + " internationWeeklyReport : 没有找到要更新的数据");
        }
        BeanUtils.copyPropertiesIgnoreNull(bizOrg, oldbizOrg);
        baseDao.update(oldbizOrg);
    }

    @Override
    public void updateBizContact(BizContact bizContact) {
        BizContact oldbizContact = baseDao.get(BizContact.class, bizContact.getId());
        if (oldbizContact == null) {
            throw new AnneException(InternationWeeklyReport.class.getName()
                    + " internationWeeklyReport : 没有找到要更新的数据");
        }
        BeanUtils.copyProperties(bizContact, oldbizContact);
        baseDao.update(oldbizContact);
    }

    @Override
    public void updateBizCompetitor(BizCompetitor bizCompetitor) {
        BizCompetitor oldbizCompetitor = baseDao.get(BizCompetitor.class, bizCompetitor.getId());
        if (oldbizCompetitor == null) {
            throw new AnneException(InternationWeeklyReport.class.getName()
                    + " internationWeeklyReport : 没有找到要更新的数据");
        }
        BeanUtils.copyProperties(bizCompetitor, oldbizCompetitor);
        baseDao.update(oldbizCompetitor);
    }

    @Override
    public void updateSupportApply(SupportApply supportApply) {
        SupportApply oldsupportApply = baseDao.get(SupportApply.class, supportApply.getId());
        if (oldsupportApply == null) {
            throw new AnneException(InternationWeeklyReport.class.getName()
                    + " internationWeeklyReport : 没有找到要更新的数据");
        }
        BeanUtils.copyProperties(supportApply, oldsupportApply);
        setReportPrice(oldsupportApply);
        baseDao.update(oldsupportApply);
    }

    @Override
    public void delete(String id) {
        baseDao.deleteById(BusinessOpportunity.class, id);
    }

    @Override
    public void deleteWeeklyReport(String id) {
        baseDao.deleteById(InternationWeeklyReport.class, id);
    }

    @Override
    public void deleteProduceDetail(String id) {
        baseDao.deleteById(ProductDetail.class, id);
    }

    @Override
    public void deleteWeekly(String id) {
        baseDao.deleteById(InternationWeekly.class, id);
        //删除对应进展汇报
        StringBuffer delSql = new StringBuffer();
        delSql.append("delete from crm_t_biz_weekly_report where C_FK_WEEKLY_ID = '");
        delSql.append(id);
        delSql.append("'");
        baseDao.executeSQL(delSql.toString());
    }

    @Override
    public void deleteBizOrg(String id) {
        baseDao.deleteById(BizOrg.class, id);
        //删除关联的联系人
        StringBuffer delSql = new StringBuffer();
        delSql.append("delete from crm_t_biz_chain_contact where C_BIZ_ORG_ID = '");
        delSql.append(id);
        delSql.append("'");
        baseDao.executeSQL(delSql.toString());
    }

    @Override
    public void deleteBizContact(String id) {
        baseDao.deleteById(BizContact.class, id);
    }

    @Override
    public void deleteBizCompetitor(String id) {
        baseDao.deleteById(BizCompetitor.class, id);
    }

    @Override
    public void deleteSupportApply(String id) {
        baseDao.deleteById(SupportApply.class, id);
    }

    @Override
    public String getBizOppSupportNumber() throws AnneException {
        String connum = "";
        //		@SuppressWarnings("deprecation")
        //		Connection conn = baseDao.getTemplateConnection();
        //        try {
        String sql = "{ ? = call CRM_P_CONTRACT_PUB.gen_bizoppsupport_change_num(?)}";
        //	        CallableStatement sta = conn.prepareCall(sql);
        //	        sta.registerOutParameter(1, OracleTypes.VARCHAR);
        //            sta.setInt(2, -1);
        //	        sta.execute();
        //	        connum = sta.getString(1);
        //
        //        } catch (SQLException e) {
        //            e.printStackTrace();
        //        }

        Object[] result = baseDao.executeProcedure(sql, new BaseDao.ProcedureParam[]{
                new BaseDao.OutProcedureParam(Types.VARCHAR),
                new BaseDao.InProcedureParam(-1),
        });
        connum = (String) result[0];

        return connum;
    }

    @Override
    public String getBizOppProtoNumber() throws AnneException {
        String connum = "";
        //		@SuppressWarnings("deprecation")
        //		Connection conn = baseDao.getTemplateConnection();
        //        try {
        String sql = "{ ? = call CRM_P_CONTRACT_PUB.gen_bizoppproto_change_num(?)}";
        //	        CallableStatement sta = conn.prepareCall(sql);
        //	        sta.registerOutParameter(1, OracleTypes.VARCHAR);
        //            sta.setInt(2, -1);
        //	        sta.execute();
        //	        connum = sta.getString(1);
        //
        //        } catch (SQLException e) {
        //            e.printStackTrace();
        //        }


        Object[] result = baseDao.executeProcedure(sql, new BaseDao.ProcedureParam[]{
                new BaseDao.OutProcedureParam(Types.VARCHAR),
                new BaseDao.InProcedureParam(-1),
        });
        connum = (String) result[0];

        return connum;
    }

    @Override
    public String getSequenceCode(String functionName) {
        String connum = "";
        StringBuffer sql = new StringBuffer();
        sql.append("{ ? = call CRM_P_CONTRACT_PUB.");
        sql.append(functionName);
        sql.append("(?)}");
        Object[] result = baseDao.executeProcedure(sql.toString(), new BaseDao.ProcedureParam[]{
                new BaseDao.OutProcedureParam(Types.VARCHAR),
                new BaseDao.InProcedureParam(-1),
        });
        connum = (String) result[0];
        return connum;
    }

    @Override
    public String getBizOppNumber() {
        String connum = "";
        //		@SuppressWarnings("deprecation")
        //		Connection conn = baseDao.getTemplateConnection();
        //        try {
        String sql = "{ ? = call CRM_P_CONTRACT_PUB.gen_bizopp_change_num(?)}";
        //	        CallableStatement sta = conn.prepareCall(sql);
        //	        sta.registerOutParameter(1, OracleTypes.VARCHAR);
        //            sta.setInt(2, -1);
        //	        sta.execute();
        //	        connum = sta.getString(1);
        //
        //        } catch (SQLException e) {
        //            e.printStackTrace();
        //        }


        Object[] result = baseDao.executeProcedure(sql, new BaseDao.ProcedureParam[]{
                new BaseDao.OutProcedureParam(Types.VARCHAR),
                new BaseDao.InProcedureParam(-1),
        });
        connum = (String) result[0];

        return connum;
    }

    @Override
    public String getBizOppConflictNumber() {
        String sql = "{ ? = call CRM_P_CONTRACT_PUB.gen_bizopp_conflict_num(?)}";
        Object[] result = baseDao.executeProcedure(sql,
                new BaseDao.ProcedureParam[]{new BaseDao.OutProcedureParam(Types.VARCHAR), new BaseDao.InProcedureParam(-1),});
        return (String) result[0];
    }

    @Autowired
    ICustomInfoService customInfoService;

    @Autowired
    IPriceHeadService priceHeadService;

    @Autowired
    IProLovService proLovService;

    @Autowired
    IProductService productService;

    @Override
    public void generateQuot(String bizOppId, String projectType, String isBidProject, String quotName, String isProReview, UserObject userObject) {
        BusinessOpportunity businessOpportunity = getBizOppEntity(bizOppId);
        KstarQuot quot = new KstarQuot();
        CustomInfo customInfo = customInfoService.getCustomInfo(businessOpportunity.getClientId());

        if (customInfo != null) {
            quot.setCustomerName(businessOpportunity.getClientName());
            quot.setCustomerCode(customInfo.getCustomCode());
        }
        quot.setProReviewStatus("S03");
        quot.setBidAuditStatus("B03");
        ProductPriceHead productPriceHead = priceHeadService.getDefaultPriceHead(businessOpportunity.getCreatedOrgId());
        if (productPriceHead != null) {
            quot.setPriceList(productPriceHead.getPriceTableName());
            quot.setPriceListid(productPriceHead.getId());
        }
        quot.setQuotVersion("1");
        quot.setIsValid("1");
        quot.setStatus("S01");
        quot.setBoCode(bizOppId);
        quot.setIsProReview("1");
        quot.setIsBidPro(isBidProject);
        Employee e = baseDao.get(Employee.class, businessOpportunity.getCreatedById());
        if (e != null) {
            quot.setSalesRep(e.getName());
            quot.setCreator(e.getName());
        }
        quot.setCreateTime(new Date());
        quot.setBoName(businessOpportunity.getOpportunityName());
        quot.setBoCode(businessOpportunity.getId());
        quot.setCreatedById(businessOpportunity.getCreatedById());
        quot.setSalesRepid(businessOpportunity.getCreatedById());

        quot.setQuotName(businessOpportunity.getOpportunityName());
        quot.setIsProReview("1");
        quot.setIsBidPro("1");

        if (quot.getProReviewStatus() == null) {
            quot.setProReviewStatus("S03");
        }

        if (quot.getBidAuditStatus() == null) {
            quot.setBidAuditStatus("B03");
        }

        if (quot.getSpAuditStatus() == null) {
            quot.setSpAuditStatus("P03");
        }

        if (quot.getPrcAdtstatus() == null) {
            quot.setPrcAdtstatus("S03");
        }

        if (quot.getTchAdtstatus() == null) {
            quot.setTchAdtstatus("S03");
        }

        if (quot.getBidRspstatus() == null) {
            quot.setBidRspstatus("S03");
        }

        if (quot.getMtrReqstatus() == null) {
            quot.setMtrReqstatus("S03");
        }

        quotService.saveQuot(quot, userObject);

        //		String code = UUID.randomUUID().toString();
        //		//增加产品包
        //		LovMember lovMember = new LovMember();
        //		lovMember.setCode(code);
        //		lovMember.setGroupId("PRJLSTPRDCAT");
        //		lovMember.setGroupCode("PRJLSTPRDCAT");
        //		lovMember.setLeafFlag("N");
        //		lovMember.setLevel(1);
        //		lovMember.setMemo(quot.getId());
        //		lovMember.setName("工程包");
        //		lovMember.setParentId("ROOT");
        //		lovMember.setDeleteFlag("N");
        ////		baseDao.save(lovMember);
        //		proLovService.saveCatelog(lovMember);
        //
        //		KstarPrjLst prjLst = new KstarPrjLst();
        //		prjLst.setCType("0003");
        //		prjLst.setQuotCode(quot.getId());
        //		prjLst.setLvId(lovMember.getId());
        //		prjLst.setPrdNm("工程包");
        //		baseDao.save(prjLst);

        //增加产品行
        List<ProductDetail> productDetails = findProductDetailsByBuId(businessOpportunity.getId());
        for (ProductDetail pd : productDetails) {

            //增加产品包
            //			String codep = UUID.randomUUID().toString();
            //			LovMember lovMemberp = new LovMember();
            //			lovMemberp.setCode(codep);
            //			lovMemberp.setGroupId("PRJLSTPRDCAT");
            //			lovMemberp.setGroupCode("PRJLSTPRDCAT");
            //			lovMemberp.setLeafFlag("N");
            //			lovMemberp.setLevel(2);
            //			lovMemberp.setMemo(quot.getId());
            //			lovMemberp.setName(pd.getProductName());
            //			lovMemberp.setParentId(lovMember.getId());
            //			lovMemberp.setDeleteFlag("N");
            ////			baseDao.save(lovMemberp);
            //			proLovService.saveCatelog(lovMemberp);

            KstarPrjLst prjLstp = new KstarPrjLst();
            KstarProduct product = productService.getProductById(pd.getProductId());
            if (product != null) {
                prjLstp.setPrdUnt(product.getUnitName());
                prjLstp.setMaterCode(product.getMaterCode());
                prjLstp.setPrdCtg(product.getCrmCategoryName());
                prjLstp.setPrdCtgid(product.getCrmCategory());
            }

            prjLstp.setCType("0003");
            prjLstp.setQuotCode(quot.getId());
            //            prjLstp.setLvId(lovMemberp.getId());
            prjLstp.setPrdNm(pd.getProductName());
            prjLstp.setProId(pd.getProductId());
            prjLstp.setPrdTyp(pd.getProductModel());
            prjLstp.setAmt(pd.getPlanCount());
            prjLstp.setPrdSprc(pd.getPublicPrice().doubleValue());
            prjLstp.setGoldPrc(pd.getPlanPrice().doubleValue());

            baseDao.save(prjLstp);
        }
        //copy销售团队到报价单
        teamService.copyPosition(businessOpportunity.getId(),
                "BusinessOpportunity",
                quot.getId(),
                "QUOTINF",
                userObject.getEid());
    }

    private List<ProductDetail> findProductDetailsByBuId(String id) {
        return baseDao.findEntity("from ProductDetail where bizOppId = ? ", id);
    }


    @Override
    public void generateFrameContract(Contract contract, String bizOppId, String contrName) {

        BusinessOpportunity businessOpportunity = getBizOppEntity(bizOppId);
        String contrNo = contractService.getContractNumber();

        //		contract.setQuotNo(quot.getQuotCode());
        //		contract.setPricTable(quot.getPriceList());
        contract.setProjNo(bizOppId);
        contract.setProjName(businessOpportunity.getOpportunityName());

        CustomInfo customInfo = customInfoService.getCustomInfo(businessOpportunity.getClientId());

        if (customInfo != null) {
            contract.setCustCode(businessOpportunity.getClientId());
            contract.setCustName(businessOpportunity.getClientName());
        }
        contract.setContrName(businessOpportunity.getContrname());
        contract.setContrNo(contrNo);
        contract.setContrVer("1");
        LovMember lovMember = lovMemberService.getLovMemberByCode("CONTRACTTYPE", "CONTR_STAND-0102");
        if (lovMember != null) {
            String ContrType = lovMember.getId();
            contract.setContrType(ContrType);
        }
        contract.setCreator(businessOpportunity.getCreatedById());
        //		LovMember lov =  ((LovMember)CacheData.getInstance().get(payStat));
        LovMember lov = lovMemberService.getLovMemberByCode("CONTRACTREVIEWSTATUS", "01");
        contract.setReviewStat(lov.getId());
        contract.setTrialStat(lov.getId());
        contract.setFinalReviewStat(lov.getId());
        LovMember statLov = lovMemberService.getLovMemberByCode("CONTRACTSTATUS", "01");
        contract.setContrStat(statLov.getId());
        //		contract.setOrg(getUserObject().getOrg().getId());
        LovMember payLov = lovMemberService.getLovMemberByCode("CONTRACTPAYSTAT", "01");
        contract.setPayStat(payLov.getId());
        contract.setIsValid("1");


        contractService.save(contract, new UserObject(businessOpportunity.getCreatedById(), businessOpportunity.getCreatedPosId(), businessOpportunity.getCreatedOrgId()));

        //copy销售团队到合同
        teamService.copyPosition(businessOpportunity.getId(),
                "BusinessOpportunity",
                contract.getId(),
                "CONTR_STAND",
                businessOpportunity.getCreatedById());
    }

    private String[] getBizOppAddress(String bizOppAddress) {
        LovMember lov = new LovMember();
        Object obj = CacheData.getInstance().get(bizOppAddress);
        if (obj != null) {
            BeanUtils.copyPropertiesIgnoreNull(obj, lov);
            return lov.getNamePath().split("/");
        }
        return null;
    }

    @Override
    public void updateProjectType(String projectType, String bizOppId, String isBidProject, String quotName, String isProReview, String contrName) {
        String hql = "update BusinessOpportunity set projectType = ?, isBidProject = ?,qoutName = ?, isProReview = ? ,contrname = ? where id = ? ";
        baseDao.executeHQL(hql, new Object[]{projectType, isBidProject, quotName, isProReview, contrName, bizOppId});
    }

    /**
     * module      流程代码
     * application 应用代码
     * title       业务标题
     * businessKey 业务主键
     * participant 流程创建人
     * varmap      业务参数
     *
     * @throws Exception 一个业务只能同时开启一次对应流程，否则会抛出异常
     */
    @Override
    public void startProcess(UserObject user, String key, String bizOppName, UserObject userObject, String is_10) {
        String application = "BIZOPP_PERSALE_SUPPORT_PROC";
        String model = lovMemberService.getFlowCodeByAppCode(application);
        Map<String, String> vmap = new HashMap<>();
        String salesCenter = lovMemberService.getSaleCenter(userObject.getOrg().getId());
        String employeeIdInForm = "";
        String employeeNameInForm = "";
        List<SupportFeedBack> list = getSupportFeedBackBySid(key);
        if (list != null) {
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    employeeIdInForm = employeeIdInForm + list.get(i).getSales();
                    employeeNameInForm = employeeNameInForm + list.get(i).getSaleName();
                    if (i != list.size() - 1) {
                        employeeIdInForm = employeeIdInForm + ",";
                        employeeNameInForm = employeeNameInForm + ",";
                    }
                }
            }
        }
        vmap.put("is_10", is_10);
        vmap.put("SalesCenter", salesCenter);
        vmap.put("EmployeeType", userObject.getEmployee().getFlag());
        vmap.put("employeeIdInForm", employeeIdInForm);
        vmap.put("employeeNameInForm", employeeNameInForm);
        ProcessInstance processInstance = xflowProcessServiceWrapper.start(model, application, bizOppName + "_" + ProcessConstants.BIZOPP_PERSALE_SUPPORT_PROC, key, user, vmap);
        //更新流程状态为已发起,记录流程ID
        processStatusService.updateProcessStatus("SupportApply", key, "status", ProcessConstants.PROCESS_STATUS_Processing, "processId", processInstance.getId());
    }

    @Override
    public void submitWeeklyData(String id) {
        String hql = "update InternationWeekly set status = ? where id = ? ";
        baseDao.executeHQL(hql, new Object[]{"SUBMIT", id});
    }

    @Override
    public String getSpecialPriceApplyNumber() {

        String connum = "";
        //		Connection conn = null;
        //		try {
        //			conn = baseDao.getTemplateConnection();
        String sql = "{ ? = call CRM_P_CONTRACT_PUB.gen_specialPrice_apply_num(?)}";
        //			CallableStatement sta = conn.prepareCall(sql);
        //			sta.registerOutParameter(1, OracleTypes.VARCHAR);
        //			sta.setInt(2, -1);
        //			sta.execute();
        //			connum = sta.getString(1);
        //
        //		} catch (SQLException e) {
        //			e.printStackTrace();
        //		}

        Object[] result = baseDao.executeProcedure(sql, new BaseDao.ProcedureParam[]{
                new BaseDao.OutProcedureParam(Types.VARCHAR),
                new BaseDao.InProcedureParam(-1),
        });
        connum = (String) result[0];

        return connum;
    }

    @Override
    public void startPrepareProcess(String id, String number, UserObject userObject) {
        BusinessOpportunity bo = getBizOppEntity(id);
        String application = "BIZOPP_PREPARE_APPLY_PROC";
        String model = lovMemberService.getFlowCodeByAppCode(application);
        Map<String, String> vmap = new HashMap<>();
        String salesCenter = lovMemberService.getSaleCenter(userObject.getOrg().getId());
        vmap.put("SalesCenter", salesCenter);
        vmap.put("EmployeeType", userObject.getEmployee().getFlag());
        vmap.put("Amount", String.valueOf(getTotalAmount(id)));

        //更新流程状态为已发起,记录流程ID
        processStatusService.updateProcessStatus("BusinessOpportunity", id, "status", "10");
        processStatusService.updateProcessStatus("BusinessOpportunity", id, "conflictStatus", "05");
        //		xflowProcessServiceWrapper.start(model, application, number + "_" + ProcessConstants.BIZOPP_PREPARE_APPLY_PROC, id, userObject, vmap);
        xflowProcessServiceWrapper.start(
                model, application, bo.getOpportunityName() + "_" + bo.getClientName() + "_" + bo.getCreatedByIdName() + "_" +
                        bo.getEnterpriseName() + "_" + bo.getCreatedAt(), id, userObject, vmap);
    }

    @Override
    public void appealSubmit(String id) {
        //已提交申诉
        processStatusService.updateProcessStatus("BusinessOpportunity", id, "conflictStatus", "10");
    }

    @Override
    public void confirmConf(String id) {
        //配置已确认
        processStatusService.updateProcessStatus("BusinessOpportunity", id, "status", "90");
    }

    @Override
    public Double getTotalAmount(String id) {
        String hql = " select nvl(sum(planTotal),0) from ProductDetail where bizOppId = ? ";
        BigDecimal bd = baseDao.findUniqueEntity(hql, new Object[]{id});
        Double amount = bd.doubleValue();
        return amount;
    }

    @Override
    public void startApprovedProcess(BusinessOpportunity businessOpportunity, UserObject userObject, String remark) {
        String application = "BIZOPP_APPROVED_PROC";
        String model = lovMemberService.getFlowCodeByAppCode(application);
        Map<String, String> vmap = new HashMap<>();
        Double amount = getTotalAmount(businessOpportunity.getId());
        vmap.put("Amount", String.valueOf(amount));
        String salesCenter = lovMemberService.getSaleCenter(userObject.getOrg().getId());
        vmap.put("SalesCenter", salesCenter);
        //更新流程状态为已发起,记录流程ID
        //第二段工作流为“主管领导审批中”
        processStatusService.updateProcessStatus("BusinessOpportunity", businessOpportunity.getId(), "status", "25");
        xflowProcessServiceWrapper.start(model, application, businessOpportunity.getNumber() + "_" + ProcessConstants.BIZOPP_APPROVED_PROC, businessOpportunity.getId(), userObject, vmap);
        Employee employee = baseDao.get(Employee.class, businessOpportunity.getCreatedById());
        sendEmail(employee.getEmail(),
                "关于商机【" + businessOpportunity.getNumber() + " | " + businessOpportunity.getOpportunityName() + "】 的通知",
                "尊敬的客户您好，您报备的商机【" + businessOpportunity.getNumber() + " | " + businessOpportunity.getOpportunityName() + "】 已审核通过，请登录科士达CRM管理系统查看详细信息！/r/n" +
                        "审核意见：" + remark);
    }

    @Override
    public List<BizOppChange> getBizOppChange(String id, String status) {
        StringBuilder hql = new StringBuilder();
        List<Object> args = new ArrayList<>();
        hql.append("from BizOppChange where sourceId = ? ");
        args.add(id);
        if (StringUtil.isNotEmpty(status)) {
            hql.append(" and auditStatus <> ? ");
            args.add(status);
        }
        hql.append(" order by updatedAt desc ");
        List<BizOppChange> list = baseDao.findEntity(hql.toString(), args.toArray());

        return list;
    }

    @Override
    public void startChangeProcess(String id, String s, UserObject userObject) {
    	BizOppChange entity = this.getBizOppChangeById(s);
        String application = "BIZOPP_CHANGE_PROC";
        String model = lovMemberService.getFlowCodeByAppCode(application);
        Map<String, String> vmap = new HashMap<>();
        String salesCenter = lovMemberService.getSaleCenter(userObject.getOrg().getId());
        vmap.put("SalesCenter", salesCenter);
        vmap.put("EmployeeType", userObject.getEmployee().getFlag());
        vmap.put("Amount", String.valueOf(getTotalAmount(id)));
        //更新流程状态为已发起,记录流程ID
        xflowProcessServiceWrapper.start(model, application, s + "_" + ProcessConstants.BIZOPP_CHANGE_PROC, id, userObject, vmap);
//        xflowProcessServiceWrapper.start(
//        		model, 
//        		application, 
//        		entity.getOpportunityName()+"_"+entity.getClientName()+"_"+entity.getCreatedByIdName()+"_"+entity.getCreatedOrgIdName()+"_"+entity.getCreatedAt(),
//        		id, 
//        		userObject, 
//        		vmap);
        
        processStatusService.updateProcessStatus("BizOppChange", id, "auditStatus", ProcessConstants.PROCESS_STATUS_Processing);
    }

    @Override
    public void startProjectInitProcess(String bizOppId, String number, UserObject userObject) {
        String application = "BIZOPP_PROJECTINIT_PROC";
        String model = lovMemberService.getFlowCodeByAppCode(application);
        Map<String, String> vmap = new HashMap<>();
        String calesCenter = lovMemberService.getSaleCenter(userObject.getOrg().getId());
        vmap.put("SalesCenter", calesCenter);
        //更新流程状态为已发起,记录流程ID
        processStatusService.updateProcessStatus("BizOppChange", bizOppId, "auditStatus", ProcessConstants.PROCESS_STATUS_Processing);
        processStatusService.updateProcessStatus("BusinessOpportunity", bizOppId, "status", "100");
        xflowProcessServiceWrapper.start(model, application, number + "_" + ProcessConstants.BIZOPP_PROJECTINIT_PROC, bizOppId, userObject, vmap);
    }

    @Override
    public void saveChange(BizOppChange boc, String number, UserObject userObject) {
    	baseDao.saveOrUpdate(boc);

        //删除变更单中原有权限数据
        List<Integrator> oldIntegrators = getBizOppIntegratorList(boc.getId());
        if (oldIntegrators != null && oldIntegrators.size() > 0) {
            baseDao.executeHQL(" delete Integrator where bizOppId = ?", new Object[]{boc.getId()});
        }
        //保存变更修改后的权限新数据
        List<Integrator> integrators = boc.getIntegrators();
        for (Integrator integrator : integrators) {
            integrator.setSourceId(integrator.getId());
            integrator.setId(null);
            integrator.setBizOppId(boc.getId());
            baseDao.save(integrator);
        }

        //删除变更单中原有商机配置数据
        List<ProductDetail> oldProductDetail = getProducrDetailByBizId(boc.getId());
        if (oldProductDetail != null && oldProductDetail.size() > 0) {
            baseDao.executeHQL(" delete ProductDetail where bizOppId = ?", new Object[]{boc.getId()});
        }
        //保存变更修改后的商机配置新数据
        List<ProductDetail> details = boc.getDetails();
        for (ProductDetail detail : details) {
            detail.setSourceId(detail.getId());
            detail.setId(null);
            detail.setBizOppId(boc.getId());
            baseDao.save(detail);
        }

        String application = "BIZOPP_CHANGE_PROC";
        String model = lovMemberService.getFlowCodeByAppCode(application);
        ProcessInstance pi = processService.getByBusinessKey(model, boc.getId());
        if (pi == null) {
            startChangeProcess(boc.getId(), number, userObject);
        }
    }

    /**
     * 商机预警，更新商机状态   失效，	即将失效
     */
    @Override
    @Scheduled(cron = "0 0 1 * * *")
    public void autoCheckReport() {
        handleWillInvalid();
        handleInvalided();
    }

    /**
     * 处理已失效的商机
     */
    private void handleInvalided() {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();

        //language=HQL
        String hql = "update BusinessOpportunity bo set bo.conflictStatus='50' where bo.endDate < ? and (bo.conflictStatus='40' or bo.conflictStatus='45') ";
        this.baseDao.executeHQL(hql, new Object[]{now});
    }

    /**
     * 处理即将失效的商机,发送邮件通知创建人
     */
    private void handleWillInvalid() {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        calendar.add(Calendar.DATE, 30);
        Date after30 = calendar.getTime();

        String hql;
        List<BusinessOpportunity> businessOpportunities;

        //到期提前3天通知
        //language=HQL
        hql = "from BusinessOpportunity bo where bo.endDate > ? and (trunc(bo.endDate) - trunc(current_timestamp)) = 3 and bo.conflictStatus='45'";
        businessOpportunities = this.baseDao.findEntity(hql, new Object[]{now});
        for (BusinessOpportunity bo : businessOpportunities) {
            sendEmailForBoWillInvalid(bo);
        }

        //language=HQL
        hql = "from BusinessOpportunity bo where bo.endDate > ? and bo.endDate <= ? and bo.conflictStatus='40'";
        businessOpportunities = this.baseDao.findEntity(hql, new Object[]{now, after30});

        for (BusinessOpportunity bo : businessOpportunities) {
            bo.setConflictStatus("45");
        }
        this.baseDao.update(businessOpportunities);
        for (BusinessOpportunity bo : businessOpportunities) {
            sendEmailForBoWillInvalid(bo);
        }
    }


    @Override
    public void save(Integrator integrator) {
        baseDao.save(integrator);
    }

    @Override
    public List<Integrator> getBizOppIntegratorList(String id) {
        String hql = " from Integrator where bizOppId = ? ";
        List<Integrator> list = baseDao.findEntity(hql, id);
        return list;
    }

    @Override
    public Integrator getBizOppIntegrator(String id) {
        return baseDao.findUniqueEntity("from Integrator where id = ?", new Object[]{id});
    }

    @Override
    public void update(Integrator integrator) {
        baseDao.update(integrator);
    }

    @Override
    public void deleteIntegrator(String id) {
        baseDao.deleteById(Integrator.class, id);
    }

    @Override
    public IPage queryBizOppIntegrator(PageCondition condition) {
        FilterObject filterObject = condition.getFilterObject(Integrator.class);
        HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
        return baseDao.search(hqlObject.getHql(), hqlObject.getArgs(), condition.getRows(), condition.getPage());
    }

    @Override
    public IPage queryBizOppIntegratorChange(PageCondition condition) {
        FilterObject filterObject = condition.getFilterObject(IntegratorChange.class);
        HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
        return baseDao.search(hqlObject.getHql(), hqlObject.getArgs(), condition.getRows(), condition.getPage());
    }

    /**
     * 审核完成后更新商机信息
     */
    @Override
    public void changeBizOpp(String businessKey) {
        BizOppChange boc = baseDao.findUniqueEntity(" from BizOppChange where id = ?", new Object[]{businessKey});

        BusinessOpportunity bo = baseDao.findUniqueEntity("from BusinessOpportunity where id = ?", new Object[]{boc.getSourceId()});

        bo.setOpportunityName(boc.getOpportunityName2());
        bo.setBizOppAddress(boc.getBizOppAddress2());
        bo.setAddress(boc.getAddress2());
        bo.setBidNo(boc.getBidNo2());
        bo.setClientId(boc.getClientId2());
        bo.setClientName(boc.getClientName2());
        bo.setBidUnit(boc.getBidUnit2());
        bo.setSaleStage(boc.getOpportunityStep2());
        bo.setSuccessRate(boc.getSuccessRate2());
        bo.setPlanFinDate(boc.getPlanFinDate2());
        bo.setProjectProgress(boc.getProjectProgress2());
        bo.setCompetitiveBrands(boc.getCompetitiveBrands2());
        bo.setLayer2(boc.getLayer22());
        bo.setLayer3(boc.getLayer32());
        bo.setLayer4(boc.getLayer42());
        bo.setEndDate(getNextDay(new Date()));
        getAddressName(bo);

        baseDao.update(bo);

        //		baseDao.executeHQL(" delete Integrator where bizOppId = ? ",new Object[]{bo.getId()});
        // 原始数据
        List<Integrator> oldIntegrators = baseDao.findEntity(" from Integrator where bizOppId = ? ", boc.getSourceId());
        // 变更信息
        List<Integrator> integrators = baseDao.findEntity(" from Integrator where bizOppId = ? ", boc.getId());

        Map<String, Integrator> newIgMap = new HashMap<>();
        for (Integrator integrator : integrators) {
            if (StringUtil.isNullOrEmpty(integrator.getSourceId())) {
                newIgMap.put(integrator.getId(), integrator);
            } else {
                newIgMap.put(integrator.getSourceId(), integrator);
            }
        }

        // 对老商机删除和更新
        for (Integrator integrator : oldIntegrators) {
            Integrator newTg = newIgMap.get(integrator.getId());
            if (newTg == null) {
                baseDao.delete(integrator);
            } else {
                integrator.setContact(newTg.getContact());
                integrator.setIntegrator(newTg.getIntegrator());
                integrator.setPhone(newTg.getPhone());
                baseDao.update(integrator);
                newIgMap.remove(integrator.getId());
            }
        }
        // 新增新的商机信息
        for (String key : newIgMap.keySet()) {
            Integrator integrator = newIgMap.get(key);
            Integrator integrator2 = new Integrator();
            BeanUtils.copyProperties(integrator, integrator2);
            integrator2.setBizOppId(bo.getId());
            integrator2.setSourceId(null);
            integrator2.setId(null);
            baseDao.save(integrator2);
        }


        //		for (Integrator integrator : integrators) {
        //			Integrator target = new Integrator();
        //			BeanUtils.copyProperties(integrator, target);
        //			target.setId(null);
        //			target.setBizOppId(bo.getId());
        //			baseDao.save(target);
        //		}

        //		baseDao.executeHQL(" delete ProductDetail where bizOppId = ? ",new Object[]{bo.getId()});
        // 原始数据
        List<ProductDetail> oldDetails = baseDao.findEntity(" from ProductDetail where bizOppId = ? ", boc.getSourceId());
        // 变更信息
        List<ProductDetail> details = baseDao.findEntity(" from ProductDetail where bizOppId = ? ", boc.getId());

        Map<String, ProductDetail> newDlMap = new HashMap<>();
        for (ProductDetail detail : details) {
            if (StringUtil.isNullOrEmpty(detail.getSourceId())) {
                newDlMap.put(detail.getId(), detail);
            } else {
                newDlMap.put(detail.getSourceId(), detail);
            }
        }

        // 对老商机删除和更新
        for (ProductDetail detail : oldDetails) {
            ProductDetail newTg = newDlMap.get(detail.getId());
            if (newTg == null) {
                baseDao.delete(detail);
            } else {
                detail.setCproCategory(newTg.getCproCategory());
                detail.setCproPowcap(newTg.getCproPowcap());
                detail.setCrmCategory(newTg.getCrmCategory());
                detail.setCrmCategoryLable(newTg.getCrmCategoryLable());
                detail.setIsStandard(newTg.getIsStandard());
                detail.setPlanCount(newTg.getPlanCount());
                detail.setPlanPrice(newTg.getPlanPrice());
                detail.setPlanTotal(newTg.getPlanTotal());
                detail.setProductId(newTg.getProductId());
                detail.setProductModel(newTg.getProductModel());
                detail.setProductName(newTg.getProductName());
                detail.setProductSeries(newTg.getProductSeries());
                detail.setProductType(newTg.getProductType());
                detail.setPublicPrice(newTg.getPublicPrice());
                detail.setRemark(newTg.getRemark());
                baseDao.update(detail);
                newDlMap.remove(detail.getId());
            }
        }
        // 新增新的商机信息
        for (String key : newDlMap.keySet()) {
            ProductDetail detail = newDlMap.get(key);
            ProductDetail detail2 = new ProductDetail();
            BeanUtils.copyProperties(detail, detail2);
            detail2.setBizOppId(bo.getId());
            detail2.setBizOppName(bo.getOpportunityName());
            detail2.setSourceId(null);
            detail2.setId(null);
            baseDao.save(detail2);
        }


        //		for (ProductDetail productDetail : details) {
        //			ProductDetail target = new ProductDetail();
        //			BeanUtils.copyProperties(productDetail, target);
        //			target.setId(null);
        //			target.setBizOppId(bo.getId());
        //			baseDao.save(target);
        //		}

    }


    public String getProductSeries(String productId) {

        String sql = "select s.c_pro_series " +
                "  from CRM_T_PRODUCT_BASIC p, CRM_T_PRODUCT_LINE s " +
                " where p.c_pro_line_id = s.c_pid " +
                "   and p.c_pid = ? ";

        String producrSeries = baseDao.findUniqueBySql(sql, new Object[]{productId});

        return producrSeries;

    }

    @Override
    public void save(SupportFeedBack sfb) {
        baseDao.save(sfb);
    }

    @Override
    public IPage querySupportFeedBack(PageCondition condition) {
        FilterObject filterObject = condition.getFilterObject(SupportFeedBack.class);
        HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
        return baseDao.search(hqlObject.getHql(), hqlObject.getArgs(), condition.getRows(), condition.getPage());
    }

    @Override
    public SupportFeedBack getSupportFeedBack(String id) {
        return baseDao.findUniqueEntity("from SupportFeedBack where id = ? ", new Object[]{id});
    }

    @Override
    public List<SupportFeedBack> getSupportFeedBackBySid(String supportId) {
        return baseDao.findEntity("from SupportFeedBack where businessId = ? ", supportId);
    }

    @Override
    public void update(SupportFeedBack supportFeedBack) {
        baseDao.update(supportFeedBack);
    }

    @Override
    public void deleteSfb(String id) {
        baseDao.deleteById(SupportFeedBack.class, id);
    }

    @Override
    public IPage findBizs(PageCondition condition, String bizIds, String productId) {

        StringBuilder sql = new StringBuilder();

        sql.append(" select a.c_opportunity_name || ' | ' || a.c_number as biz,			");
        sql.append(" 	f.c_product_name || ' | ' || f.c_product_model as product,		");
        sql.append(" 	f.n_plan_count	as planCount,									");
        sql.append("	(select lov_name from 											");
        sql.append("	(select lov_name    											");
        sql.append("	          from (select m.lov_name								");
        sql.append("	                  from crm_t_bid b,sys_t_lov_member m			");
        sql.append("	                 where b.c_business_opp_id = a.c_pid			");
        sql.append("	                   and m.lov_code = b.c_bid_result				");
        sql.append("	                   and m.group_code = 'BID_RESULTS'				");
        sql.append("	                   and rownum = 1								");
        sql.append("	                 order by b.created_at))) as bidResult			");
        sql.append(" from crm_t_business_opportunity a, crm_t_bizopp_products_detail f	");
        sql.append(" where a.c_pid = f.c_bizopp_id										");
        sql.append(" and f.c_pid in (" + getPars(bizIds) + ")							");
        sql.append(" and f.c_product_id = ?												");

        return baseDao.searchBySql(sql.toString(), new Object[]{productId}, condition.getRows(), condition.getPage());
    }

    public String getPars(String bizIds) {
        String pars = "'";
        if (StringUtil.isNotEmpty(bizIds)) {
            String[] ids = bizIds.split(",");
            if (ids.length > 1) {
                for (int i = 0; i < ids.length; i++) {
                    if (i == ids.length - 1) {
                        pars = pars + ids[i] + "'";
                    } else {
                        pars = pars + ids[i] + "','";
                    }
                }
            } else {
                return "'" + ids[0] + "'";
            }
        } else {
            return "''";
        }
        return pars;
    }

    @Override
    public List<Map<String, Object>> selectBizOpp(Condition condition, UserObject user) {
        String search = condition.getStringCondition("search");
        String customId = condition.getStringCondition("customId");
        List<Object> args = new ArrayList<>();
        String sql = "select"
                + " o.c_number \"number\","
                + " o.c_opportunity_name \"name\","
                + " o.c_client_id \"customId\","
                + " o.c_client_name \"client\""
                + " from crm_t_business_opportunity o"
                + " left join sys_t_lov_member m on o.c_enterprise = m.row_id"
                + " where 1=1";
        if ("E".equals(user.getEmployee().getFlag())) {
            sql += " and o.c_enterprise = ?";
            args.add(user.getOrg().getId());
        } else {
            sql += " and m.opt_txt3 != 'E'";
        }
        if (StringUtil.isNotEmpty(search)) {
            sql += " and o.c_number like ?";
            args.add("%" + search.trim() + "%");
        }
        if (StringUtil.isNotEmpty(customId)) {
            sql += " and o.c_client_id = ?";
            args.add(customId);
        }
        sql += " and rownum < 200";
        return baseDao.findBySql4Map(sql, args.toArray());
    }

    @Override
    public Map<String, Object> getBizOppEntityByNumber(String number) {
        String sql = "select"
                + " o.c_number \"number\","
                + " o.c_opportunity_name \"name\","
                + " o.c_client_id \"customId\","
                + " o.c_client_name \"client\""
                + " from crm_t_business_opportunity o"
                + " where o.c_number = ? and rownum < 1";
        List<Map<String, Object>> datas = baseDao.findBySql4Map(sql, new Object[]{number});
        if (datas != null && datas.size() > 0) {
            return datas.get(0);
        }
        return null;
    }

    @Override
    public List<BusinessOpportunity> getBizOppSelectAuth(PageCondition condition, String clientId, String userId) {
        String search = condition.getStringCondition("search");
        //        if (StringUtil.isNotEmpty(search)) {
        //            condition.getFilterObject().addOrCondition("opportunityName", "like", "%" + search + "%");
        //            condition.getFilterObject().addOrCondition("number", "like", "%" + search + "%");
        //        }
        //        if (StringUtil.isNotEmpty(clientId)) {
        //            condition.getFilterObject().addCondition("clientId", "=", clientId);
        //        }
        //        condition.getFilterObject().addCondition("status", "!=", "20");
        //        /*condition.getFilterObject().addOrCondition("conflictStatus", "eq", "40");
        //        condition.getFilterObject().addOrCondition("conflictStatus", "eq", "45");*/
        //        FilterObject filterObject = condition.getFilterObject(BusinessOpportunity.class);
        //        String sql = " and b.conflictStatus = 40 or b.conflictStatus = 45 ";
        //        //filterObject.addOrderBy("updatedAt", "desc");
        //        HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
        //        return baseDao.findEntity((hqlObject.getHql()+sql), hqlObject.getArgs());

        StringBuffer sb = new StringBuffer(" select b from BusinessOpportunity b where 1=1 ");
        List<Object> args = new ArrayList<>();
        if (StringUtil.isNotEmpty(search)) {
            sb.append(" and (b.opportunityName like ? or b.number like ? )");
            args.add("%" + search + "%");
            args.add("%" + search + "%");
        }
        if (StringUtil.isNotEmpty(clientId)) {
            sb.append(" and b.clientId = ?");
            args.add(clientId);
        }
        sb.append(" and (b.conflictStatus = '40' or b.conflictStatus = '45') ");
        sb.append(" order by b.updatedAt desc");

        return baseDao.findEntity(sb.toString(), args.toArray());
    }

    @Override
    public BizOppChange getBizOppChangeById(String id) {

        return baseDao.findUniqueEntity("from BizOppChange where id = ?", id);
    }

    @Override
    public IPage queryBidAuthUnit(PageCondition condition) {
        FilterObject filterObject = condition.getFilterObject(BidAuthUnit.class);
        filterObject.addOrderBy("updatedAt", "desc");
        HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
        return baseDao.search(hqlObject.getHql(), hqlObject.getArgs(), condition.getRows(), condition.getPage());
    }

    @Override
    public void saveBidAuthUnit(BidAuthUnit bidAuthUnit, UserObject userObject) throws AnneException {
        // 创建字段
        bidAuthUnit.setCreatedById(userObject.getEmployee().getId());
        bidAuthUnit.setCreatedAt(new Date());
        bidAuthUnit.setCreatedPosId(userObject.getPosition().getId());
        bidAuthUnit.setCreatedOrgId(userObject.getOrg().getId());
        // 更新字段
        bidAuthUnit.setUpdatedById(userObject.getEmployee().getId());
        bidAuthUnit.setUpdatedAt(new Date());
        baseDao.save(bidAuthUnit);

    }

    @Override
    public BidAuthUnit getBidAuthUnit(String id) throws AnneException {
        return baseDao.get(BidAuthUnit.class, id);
    }


    @Override
    public void updateBidAuthUnit(BidAuthUnit bidAuthUnit, UserObject userObject) throws AnneException {
        BidAuthUnit oldBidAuthUnit = baseDao.get(BidAuthUnit.class, bidAuthUnit.getId());
        if (oldBidAuthUnit == null) {
            throw new AnneException(IBizoppService.class.getName() + " 没有找到要更新的数据");
        }

        if (!oldBidAuthUnit.getId().equals(oldBidAuthUnit.getId())) {
            throw new AnneException("ID不能被修改");
        }

        BeanUtils.copyPropertiesIgnoreNull(bidAuthUnit, oldBidAuthUnit);

        // 更新字段
        bidAuthUnit.setUpdatedById(userObject.getEmployee().getId());
        bidAuthUnit.setUpdatedAt(new Date());
        baseDao.update(oldBidAuthUnit);
    }


    @Override
    public void deleteBidAuthUnit(String id) throws AnneException {
        baseDao.executeHQL(" delete BidAuthUnit where id = ? ", new Object[]{id});
    }

    @Override
    public CustomInfo findCustomInfoByName(String customerName) {
        List<CustomInfo> list = baseDao.findEntity("from CustomInfo where customFullName = ? ", customerName);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return new CustomInfo();
        }
    }

    @Override
    public Double getOrderQtyByLineId(String lineId) {
        String hql = "select sum(nvl(proQty,0)-nvl(cancelQty,0)) from OrderLines where spLineId = ?";
        Double orderQty = baseDao.findUniqueEntity(hql, lineId);
        if (orderQty != null) {
            return orderQty;
        } else {
            return 0d;
        }

    }

    @Override
    public List<ProductDetail> getProducrDetailByBizId(String bizOppId) {
        return baseDao.findEntity("from ProductDetail where bizOppId = ? ", bizOppId);
    }

    @Override
    public BusinessOpportunity getBusinessOpportunityByNumber(String number) {
        return baseDao.findUniqueEntity("from BusinessOpportunity where number = ? ", number);
    }

    @Override
    public void startBtnxProcess(BusinessOpportunity businessOpportunity, UserObject userObject, String remark) {
        //		String application = "BIZOPP_BTNX_PROC";
        //		String model = lovMemberService.getFlowCodeByAppCode(application);
        //		Map<String, String> vmap = new HashMap<>();
        //		String salesCenter = lovMemberService.getSaleCenter(userObject.getOrg().getId());
        ////		vmap.put("TODO", "TODO");
        //		ProcessInstance processInstance = xflowProcessServiceWrapper.start(model, application,
        //				businessOpportunity.getNumber() + "_" + ProcessConstants.BIZOPP_BTNX_PROC,
        //				businessOpportunity.getId(), userObject, vmap);

        //更新流程状态为已发起,记录流程ID
        processStatusService.updateProcessStatus("BusinessOpportunity", businessOpportunity.getId(), "conflictStatus", "60");
        processStatusService.updateProcessStatus("BusinessOpportunity", businessOpportunity.getId(), "status", "20");
        Employee employee = baseDao.get(Employee.class, businessOpportunity.getCreatedById());
        sendEmail(employee.getEmail(),
                "关于商机【" + businessOpportunity.getNumber() + " | " + businessOpportunity.getOpportunityName() + "】 的通知",
                "尊敬的客户您好，您报备的商机【" + businessOpportunity.getNumber() + " | " + businessOpportunity.getOpportunityName() + "】 已开放授权，请登录科士达CRM管理系统查看详细信息！/r/n" +
                        "审核意见：" + remark);
    }

    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);

    private void sendEmail(final String email, final String subject, final String content) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Properties properties = new Properties();
                    properties.setProperty("mail.smtp.auth", "true");
                    properties.setProperty("mail.transport.protocol", "smtp");
                    Session session = Session.getInstance(properties);
                    session.setDebug(true);
                    Message message = new MimeMessage(session);
                    message.setSubject(subject);
                    message.setFrom(new InternetAddress("kstarcrm@kstar.com.cn"));
                    message.setText(content);
                    Transport transport = session.getTransport();
                    transport.connect("10.2.1.99", 25, "kstarcrm@kstar.com.cn", "kstar-5");
                    transport.sendMessage(message, new Address[]{new InternetAddress(email)});
                    transport.close();
                } catch (Exception e) {

                }
            }
        };
        fixedThreadPool.submit(thread);
    }

    @Override
    public void publishMail(List<BusinessOpportunity> bus) {
        for (BusinessOpportunity businessOpportunity : bus) {
            Employee employee = baseDao.get(Employee.class, businessOpportunity.getCreatedById());
            sendEmail(employee.getEmail(), "关于商机【" + businessOpportunity.getNumber() + " | " + businessOpportunity.getOpportunityName() + "】疑似冲突的通知",
                    "尊敬的客户您好，你提报的商机【" + businessOpportunity.getNumber() + " | " + businessOpportunity.getOpportunityName() + "】疑似冲突，请登录科士达CRM管理系统，上传此商机相关推进资料！");
        }
    }

    @Override
    public void rejected(BusinessOpportunity businessOpportunity, String remark) {
        Employee employee = baseDao.get(Employee.class, businessOpportunity.getCreatedById());
        sendEmail(employee.getEmail(),
                "关于商机【" + businessOpportunity.getNumber() + " | " + businessOpportunity.getOpportunityName() + "】 的通知",
                "尊敬的客户您好，您报备的商机【" + businessOpportunity.getNumber() + " | " + businessOpportunity.getOpportunityName() + "】 审核不通过，已被驳回，请登录科士达CRM管理系统查看详细信息！/r/n" +
                        "审核意见：" + remark);
    }

    @Override
    public BusinessOpportunity get(String id) throws AnneException {
        if (id == null) {
            return null;
        }
        return baseDao.findUniqueEntity("from BusinessOpportunity where id = ? ", id);
    }

    @Override
    public List<List<Object>> exportRebateLineFormLists(String[] ids) throws AnneException {
        List<List<Object>> lstOut = new ArrayList<List<Object>>();
        addTitle(lstOut);
        List<RebateLine> lines = getSelectedContrList(ids);
        for (RebateLine rebateLine : lines) {
            List<Object> lstIn = new ArrayList<Object>();
            lstIn.add(rebateLine.getProductName());
            lstIn.add(rebateLine.getCproPowcap());
            lstIn.add(rebateLine.getProductModel());
            lstIn.add(rebateLine.getMaterCode());
            lstIn.add(rebateLine.getSourcePrice());
            lstIn.add(rebateLine.getCatalogPrice());
            lstIn.add(rebateLine.getApplyRebate());
            lstIn.add(rebateLine.getApplyPrice());
            lstIn.add(rebateLine.getApproveRebate());
            lstIn.add(rebateLine.getApplyQty());
            lstIn.add(rebateLine.getApplyAmount());
            lstIn.add(rebateLine.getAmount());
            lstIn.add(rebateLine.getBizName());
            lstIn.add(rebateLine.getClientName());
            lstIn.add(rebateLine.getOrderQty());
            lstIn.add(rebateLine.getRemark());
            lstIn.add(rebateLine.getProDesc());
            lstOut.add(lstIn);
        }
        return lstOut;
    }

    private List<RebateLine> getSelectedContrList(String[] ids) {
        String idsStr = "";
        for (String id : ids) {
            idsStr += "'" + id + "',";
        }
        idsStr = idsStr.substring(0, idsStr.length() - 1);
        StringBuffer hql = new StringBuffer(" from RebateLine where 1 = 1 and id in (" + idsStr + ")");
        return baseDao.findEntity(hql.toString());
    }

    private void addTitle(List<List<Object>> lstOut) {
        List<Object> lstHead = new ArrayList<Object>();
        lstHead.add("产品名称");
        lstHead.add("产品规格");
        lstHead.add("产品型号");
        lstHead.add("物料号");
        lstHead.add("公开价格");
        lstHead.add("金牌价格");
        lstHead.add("申请折扣（%）");
        lstHead.add("申请价格");
        lstHead.add("批复折扣（%）");
        lstHead.add("申请数量");
        lstHead.add("小计申请金额");
        lstHead.add("小计批准金额");
        lstHead.add("商机名称");
        lstHead.add("终端用户名称");
        lstHead.add("已下单数量");
        lstHead.add("备注");
        lstHead.add("产品描述");
        lstOut.add(lstHead);
    }

    @Override
    public List<List<Object>> export(String[] ids) throws AnneException {
        List<List<Object>> lstOut = new ArrayList<List<Object>>();
        title(lstOut);
        List<RebateLine> lines = getSelectedContrList(ids);
        for (RebateLine rebateLine : lines) {
            List<Object> lstIn = new ArrayList<Object>();
            lstIn.add(rebateLine.getProductName());
            lstIn.add(rebateLine.getCproPowcap());
            lstIn.add(rebateLine.getProductModel());
            lstIn.add(rebateLine.getMaterCode());
            lstIn.add(rebateLine.getSourcePrice());
            lstIn.add(rebateLine.getCatalogPrice());
            lstIn.add(rebateLine.getApplyRebate());
            lstIn.add(rebateLine.getApplyPrice());
            lstIn.add(rebateLine.getApplyQty());
            lstIn.add(rebateLine.getApplyAmount());
            lstIn.add(rebateLine.getAmount());
            lstIn.add(rebateLine.getBizName());
            lstIn.add(rebateLine.getClientName());
            lstIn.add(rebateLine.getOrderQty());
            lstIn.add(rebateLine.getRemark());
            lstIn.add(rebateLine.getProDesc());
            lstOut.add(lstIn);
        }
        return lstOut;
    }

    private void title(List<List<Object>> lstOut) {
        List<Object> lstHead = new ArrayList<Object>();
        lstHead.add("产品名称");
        lstHead.add("产品规格");
        lstHead.add("产品型号");
        lstHead.add("物料号");
        lstHead.add("公开价格");
        lstHead.add("金牌价格");
        lstHead.add("申请折扣（%）");
        lstHead.add("申请价格");
        lstHead.add("申请数量");
        lstHead.add("小计申请金额");
        lstHead.add("小计批复价格");
        lstHead.add("商机名称");
        lstHead.add("终端用户名称");
        lstHead.add("已下单数量");
        lstHead.add("备注");
        lstHead.add("产品描述");
        lstOut.add(lstHead);
    }

    @Override
    public List<List<Object>> exportData(String[] ids) throws AnneException {
        List<List<Object>> lstOut = new ArrayList<List<Object>>();
        newTitle(lstOut);
        List<RebateLine> lines = getSelectedContrList(ids);
        for (RebateLine rebateLine : lines) {
            List<Object> lstIn = new ArrayList<Object>();
            lstIn.add(rebateLine.getProductName());
            lstIn.add(rebateLine.getCproPowcap());
            lstIn.add(rebateLine.getProductModel());
            lstIn.add(rebateLine.getMaterCode());
            lstIn.add(rebateLine.getSourcePrice());
            lstIn.add(rebateLine.getCatalogPrice());
            lstIn.add(rebateLine.getApplyRebate());
            lstIn.add(rebateLine.getApplyPrice());
            lstIn.add(rebateLine.getApproveRebate());
            lstIn.add(rebateLine.getApprovePrice());
            lstIn.add(rebateLine.getApplyQty());
            lstIn.add(rebateLine.getApplyAmount());
            lstIn.add(rebateLine.getBizName());
            lstIn.add(rebateLine.getClientName());
            lstIn.add(rebateLine.getOrderQty());
            lstIn.add(rebateLine.getRemark());
            lstIn.add(rebateLine.getProDesc());
            lstOut.add(lstIn);
        }
        return lstOut;
    }

    private void newTitle(List<List<Object>> lstOut) {
        List<Object> lstHead = new ArrayList<Object>();
        lstHead.add("产品名称");
        lstHead.add("产品规格");
        lstHead.add("产品型号");
        lstHead.add("物料号");
        lstHead.add("公开价格");
        lstHead.add("金牌价格");
        lstHead.add("申请折扣（%）");
        lstHead.add("申请价格");
        lstHead.add("批复折扣（%）");
        lstHead.add("批复价格");
        lstHead.add("申请数量");
        lstHead.add("小计申请数量");
        lstHead.add("商机名称");
        lstHead.add("终端用户名称");
        lstHead.add("已下单数量");
        lstHead.add("备注");
        lstHead.add("产品描述");
        lstOut.add(lstHead);
    }

    /**
     * 商机即将失效发送邮件通知创建人
     */
    private void sendEmailForBoWillInvalid(BusinessOpportunity bo) {
        try {
            Employee employee = baseDao.get(Employee.class, bo.getCreatedById());
            if (employee != null && StringUtils.isNotEmpty(employee.getEmail())) {
                String title = "您好！" + bo.getOpportunityName() + "将于" + bo.getEndDate() + "失效。";
                userService.sendMail(employee.getEmail(), title, title);
            }
        } catch (Exception e) {
            logger.error("发送邮件失败", e);
        }

    }

    @Override
    public void sendEmail(String id) {

        BusinessOpportunity bo = baseDao.get(BusinessOpportunity.class, id);
        String title = bo == null ? "" : "您好！" + bo.getOpportunityName() + "已由" + bo.getEnterpriseName() + "于" + bo.getCreatedAt() + "提交报备，" + bo.getStatusName();
        //1.发送邮件给所属行业部领导
        String industry = bo.getIndustry();
        String hql = "select e from Employee e,"
                + " UserPermission up,LovMember p , "
                + " LovMember po "
                + " where po.id = p.optTxt1 "
                + " and p.id = up.memberId "
                + " and e.id = up.userId and up.type = 'P' "
                + " and po.name like '%部门领导%' "
                + " and po.namePath like ? ";
        List<Employee> employees = baseDao.findEntity(hql, new Object[]{"%" + industry + "%"});
        if (employees.size() > 0) {
            for (Employee employee : employees) {
                userService.sendMail(employee.getEmail(), title, title);
            }
        }

        //2.发送邮件给跨区办事处主任
        StringBuilder sb = new StringBuilder();
        sb.append(" select c from TerritoryConfig c, ");
        sb.append(" TerritoryInfo i ");
        sb.append(" where c.territory = i.code ");
        sb.append(" and i.lev3 = ? ");
        List<TerritoryConfig> configs = baseDao.findEntity(sb.toString(), new Object[]{bo.getLayer3()});
        for (TerritoryConfig ter : configs) {
            String sql = "select e from Employee e,"
                    + " UserPermission up,LovMember p ,"
                    + " LovMember po "
                    + " where po.id = p.optTxt1  "
                    + " and p.id = up.memberId "
                    + " and e.id = up.userId and up.type = 'P' "
                    + " and po.name like '%办事处主任%'"
                    + " and po.namePath like ? ";
            List<Employee> employeeList = baseDao.findEntity(sql, new Object[]{"%" + ter.getOrgIdName() + "%"});
            if (employeeList.size() > 0) {
                for (Employee employee : employeeList) {
                    userService.sendMail(employee.getEmail(), title, title);
                }
            }
        }
    }


    /**
     * 报价申请——判断商机数量
     */
    public String checkBiz(String rebateNo, List<RebateLine> rebateLineList, String type, String changeFlag) {
        StringBuffer msg = new StringBuffer();
        String rebateId = "";
        for (RebateLine rebateLine : rebateLineList) {
            rebateId = rebateLine.getRebateId();
            if ("常规".equals(type) || "分销".equals(type)) {
                if (StringUtil.isNullOrEmpty(rebateLine.getApplyRebate()) || rebateLine.getApplyRebate() == 100 || rebateLine.getApplyRebate() == 0) {
                    msg.append("申请折扣不能为0或100%，并且不能为空！");
                }
                if (StringUtil.isNullOrEmpty(rebateLine.getApproveRebate()) || rebateLine.getApproveRebate() == 100 || rebateLine.getApplyRebate() == 0) {
                    msg.append("批复折扣不能为0或100%，并且不能为空！");
                }
            }
            if ("电池".equals(type)) {
                if (rebateLine.getApplyPrice() > rebateLine.getCatalogPrice()) {
                    msg.append("申请价格不能大于金牌价格！");
                }
                if (rebateLine.getApprovePrice() > rebateLine.getCatalogPrice()) {
                    msg.append("批复价格不能大于金牌价格！");
                }
            }
            if ("行业入围".equals(type)) {
                if (StringUtil.isNullOrEmpty(rebateLine.getApplyYo()) || rebateLine.getApplyYo() == 0) {
                    msg.append("申请下浮点数不能为0或空！");
                }
                if (StringUtil.isNullOrEmpty(rebateLine.getApproveYo()) || rebateLine.getApproveYo() == 0) {
                    msg.append("批复下浮点数不能为0或空！");
                }
            }
            if (StringUtil.isNullOrEmpty(rebateLine.getSourcePrice()) || rebateLine.getSourcePrice() == 0) {
                msg.append("公开价格不能为0或空！");
            }
            if (StringUtil.isNullOrEmpty(rebateLine.getApplyQty()) || rebateLine.getApplyQty() == 0) {
                msg.append("申请数量不能为0或空！");
            }
        }

        List<RebateLine> rebateLineBizList = new ArrayList<>(rebateLineList.size());
        for (RebateLine rebateLine : rebateLineList) {
            String bizId = rebateLine.getBizId();
            String bizName = rebateLine.getBizName();
            if (!(StringUtils.isEmpty(bizId) && StringUtils.isEmpty(bizName))
                    && !(StringUtils.isNotEmpty(bizId) && StringUtils.isNotEmpty(bizName))) {
                msg.append(rebateLine.getProductName()).append("未找到与之关联'").append(bizName).append("'商机单!");
            } else {
                if (StringUtils.isNotEmpty(bizId)) {
                    rebateLineBizList.add(rebateLine);
                }
            }
        }

        if (msg.length() > 0) {
            return msg.toString();
        }
        if (rebateLineBizList.size() > 0) {
            msg.append(checkRebateLineBiz(rebateNo, rebateLineBizList));
        }

        boolean orderFlag = true;
        if (!StringUtil.isNullOrEmpty(changeFlag) && "Y".equals(changeFlag) && !StringUtil.isNullOrEmpty(rebateId)) {
            List<Object> args = new ArrayList<Object>();
            StringBuffer hql = new StringBuffer("select order from OrderLines order  "
                    + "where order.spCode in( ? )");
            Rebate rebate = bizService.getRebate(rebateId);
            if (rebate != null) {
                args.add(rebate.getNo());
                orderFlag = this.baseDao.exist(hql.toString(), args.toArray());
                if (orderFlag) {
                    msg.append("存在与特价变更前相关联的订单，请特价变更完成后重新关联刷新特价！");
                }
            }
        }
        return msg.toString();
    }

    private String checkRebateLineBiz(String rebateNo, List<RebateLine> rebateLineList) {
        StringBuilder msg = new StringBuilder();

        Map<String, RebateLine> rebateLineMap = new HashMap<>();
        for (int i = 0; i < rebateLineList.size(); i++) {
            RebateLine rebateLine = rebateLineList.get(i);
            String bizId = rebateLine.getBizId();
            rebateLineMap.put(bizId, rebateLine);
        }

        Map<String, Map<String, Integer>> bizSurplusSum = this.getBizSurplusSum(rebateNo, rebateLineMap.keySet());
        Map<String, Map<String, Integer>> rebateBizSum = this.getRebateBizSum(rebateLineList);

        for (Map.Entry<String, Map<String, Integer>> entry : rebateBizSum.entrySet()) {
            String bizId = entry.getKey();
            Map<String, Integer> productSumMap = entry.getValue();
            Map<String, Integer> bizSurplusSumMap = bizSurplusSum.get(bizId);
            RebateLine rebateLine = rebateLineMap.get(bizId);
            if (bizSurplusSumMap == null) {
                msg.append("商机").append(rebateLine.getBizName()).append("无可转特价数量;");
                continue;
            }
            for (Map.Entry<String, Integer> productSum : productSumMap.entrySet()) {
                String productModel = productSum.getKey();
                Integer sum = productSum.getValue();
                if (sum == 0) {
                    continue;
                }
                Integer bizProductSurplusSum = bizSurplusSumMap.get(productModel);
                if (bizProductSurplusSum == null || bizProductSurplusSum <= 0) {
                    msg.append("商机").append(rebateLine.getBizName()).append("中").append(productModel).append("无可转特价数量;");
                } else {
                    if (sum > bizProductSurplusSum) {
                        msg.append(productModel).append("的申请数量(").append(sum).append(")大于")
                                .append("商机").append(rebateLine.getBizName()).append("中的").append("可转特价数量(").append(bizProductSurplusSum).append(");");
                    }
                }
            }

        }
        return msg.toString();
    }


    public Map<String, Map<String, Integer>> getRebateBizSum(List<RebateLine> rebateLineList) {
        Map<String, Map<String, Integer>> rebateBizSumMap = new HashMap<>();
        for (RebateLine rebateLine : rebateLineList) {
            Double _applyQty = rebateLine.getApplyQty();
            if (_applyQty == null || Objects.equals(_applyQty, 0)) {
                continue;
            }
            String productModel = rebateLine.getProductModel();
            if (StringUtils.isEmpty(productModel)) {
                continue;
            }
            String bizId = rebateLine.getBizId();
            int applyQty = _applyQty.intValue();

            if (!rebateBizSumMap.containsKey(bizId)) {
                rebateBizSumMap.put(bizId, new HashMap<String, Integer>());
            }
            Map<String, Integer> productSumMap = rebateBizSumMap.get(bizId);
            Integer sum = productSumMap.get(productModel);
            if (sum == null) {
                sum = applyQty;
            } else {
                sum += applyQty;
            }
            productSumMap.put(productModel, sum);
        }
        return rebateBizSumMap;
    }

    /**
     * 根据商机获取商机列表中各个产品型号的可转特价数量
     *
     * @param bizId
     * @return
     */
    @Override
    public Map<String, Integer> getBizProductSurplusSum(String bizId) {
        assert StringUtils.isNotEmpty(bizId) : "商机Id不能为空";
        Set<String> bizIds = new HashSet<>();
        bizIds.add(bizId);
        Map<String, Map<String, Integer>> bizSurplusSum = getBizSurplusSum(null, bizIds);
        Map<String, Integer> productModelSurplusSum = bizSurplusSum.get(bizId);
        return productModelSurplusSum;
    }

    /**
     * 获取商机剩余可转特价数量
     * 根据产品型号分组统计
     *
     * @param rebateId
     * @param bizIds
     * @return
     */
    private Map<String, Map<String, Integer>> getBizSurplusSum(String rebateNo, Set<String> bizIds) {

        List<Object> args = new ArrayList<>();
        StringBuilder inSql = new StringBuilder("");
        List<Object> inArgs = new ArrayList<>();
        for (String bizId : bizIds) {
            inSql.append("?").append(",");
            inArgs.add(bizId);
        }
        inSql.delete(inSql.length() - 1, inSql.length());

        //language=SQL
        String bizOppSumSql = "SELECT C_BIZOPP_ID,C_PRODUCT_MODEL,sum(N_PLAN_COUNT) as bizQty from CRM_T_BIZOPP_PRODUCTS_DETAIL WHERE C_BIZOPP_ID in ( " + inSql + ")  GROUP BY C_BIZOPP_ID,C_PRODUCT_MODEL";
        args.addAll(inArgs);

        //language=SQL
        String rebateSumSql = "SELECT BIZ_ID,PRODUCT_MODEL,sum(APPLY_QTY) as rebateQty from (" +
                "WITH" +
                "    Rebate_Line as (SELECT rl.REBATE_ID,rl.ROW_ID from CRM_T_REBATE_LINE rl,CRM_T_REBATE_HEADER rh WHERE rh.ROW_ID=rl.REBATE_ID and  BIZ_ID in (" + inSql + ")";
        args.addAll(inArgs);

        if (StringUtils.isNotEmpty(rebateNo)) {
            rebateSumSql += " and rh.no != ?";
            args.add(rebateNo);
        }
        rebateSumSql += ")," +
                "    Rebate_Change as (SELECT REBATE_ID,max(C_VERSION) as C_VERSION from CRM_T_REBATE_HEADER_CHANGE WHERE STATUS != 'Destroyed' and REBATE_ID in (SELECT REBATE_ID from Rebate_Line) and C_VERSION != 1 GROUP BY REBATE_ID,C_VERSION)," +
                "    Rebate_Product_Count as (SELECT rl.REBATE_ID,rl.PRODUCT_MODEL,rl.APPLY_QTY,rl.BIZ_ID from CRM_T_REBATE_HEADER rh,CRM_T_REBATE_LINE rl WHERE rh.ROW_ID=rl.REBATE_ID and rh.STATUS!='Destroyed' and rl.REBATE_ID not in (SELECT REBATE_ID from Rebate_Change) and rl.REBATE_ID in (SELECT REBATE_ID from Rebate_Line))," +
                "    Rebate_Change_Product_Count as (SELECT rlc.REBATE_ID,rlc.PRODUCT_MODEL,rlc.APPLY_QTY,rlc.BIZ_ID from CRM_T_REBATE_HEADER_CHANGE rhc, CRM_T_REBATE_LINE rlc WHERE rhc.ROW_ID=rlc.REBATE_ID AND (rhc.REBATE_ID,rhc.C_VERSION) IN (SELECT REBATE_ID,C_VERSION FROM Rebate_Change))" +
                "SELECT * from Rebate_product_count r WHERE REBATE_ID not IN (SELECT REBATE_ID from Rebate_Change) UNION ALL " +
                "SELECT * from Rebate_Change_Product_Count) ";
        rebateSumSql += "GROUP BY BIZ_ID,PRODUCT_MODEL";

        //language=SQL
        String sql = "SELECT pd.C_BIZOPP_ID, pd.C_PRODUCT_MODEL,  nvl(pd.bizQty, 0) - nvl(rl.rebateQty, 0) as surplusSum " +
                "FROM (" + bizOppSumSql + ") pd  " +
                "LEFT JOIN (" + rebateSumSql + ") rl ON pd.C_BIZOPP_ID = rl.BIZ_ID AND pd.C_PRODUCT_MODEL = rl.PRODUCT_MODEL";

        List<Map<String, Object>> maps = this.baseDao.findBySql4Map(sql, args.toArray());
        Map<String, Map<String, Integer>> surplusSumMap = new HashMap<>();
        for (Map<String, Object> map : maps) {
            String bizId = (String) map.get("C_BIZOPP_ID");
            String productModel = (String) map.get("C_PRODUCT_MODEL");
            BigDecimal surplusSum = (BigDecimal) map.get("surplusSum".toUpperCase());
            if (!surplusSumMap.containsKey(bizId)) {
                surplusSumMap.put(bizId, new HashMap<String, Integer>());
            }
            Map<String, Integer> productSurplusSumMap = surplusSumMap.get(bizId);
            productSurplusSumMap.put(productModel, surplusSum.intValue());
        }

        return surplusSumMap;
    }


    /**
     * 查询产品型号可以关联的特价单
     * 特价单中存在该产品型号的可转特价数量
     *
     * @param condition
     * @param userObject
     * @return
     */
    @Override
    public List<BusinessOpportunity> queryUsableBizsWithProduct(PageCondition condition, UserObject userObject) {
        String createdOrgId = condition.getStringCondition("createdOrgId");
        String status = condition.getStringCondition("status");
        String productModel = condition.getStringCondition("productModel");
        String rebateNo = condition.getStringCondition("rebateNo");
        if (StringUtil.isEmpty(productModel)) {
            return new ArrayList<>();
        }

        //language=SQL
        String bizOppSql = "select bo0.C_PID from CRM_T_BUSINESS_OPPORTUNITY bo0,CRM_T_BIZOPP_PRODUCTS_DETAIL pd0 where bo0.C_PID=pd0.C_BIZOPP_ID and pd0.C_PRODUCT_MODEL=? ";
        List<Object> bizOppSqlArgs = new ArrayList<>();
        bizOppSqlArgs.add(productModel);
        if (StringUtils.isNotEmpty(status)) {
            bizOppSql += "and bo0.C_STATUS=? ";
            bizOppSqlArgs.add(productModel);
        }
        bizOppSql += " and " + PermissionUtil.getPermissionSQL(null, "bo0.created_by_id", "bo0.created_pos_id", "bo0.created_org_id", "bo0.C_PID", userObject, "BusinessOpportunity");

        if (StringUtils.isNotEmpty(createdOrgId)) {
            bizOppSql += " and bo.CREATED_ORG_ID=? ";
            bizOppSqlArgs.add(createdOrgId);
        }

        //language=SQL
        String bizOppSumSql = "select C_BIZOPP_ID,C_PRODUCT_MODEL,sum(pd1.N_PLAN_COUNT) as N_PLAN_COUNT  from CRM_T_BIZOPP_PRODUCTS_DETAIL pd1 group by pd1.C_BIZOPP_ID,pd1.C_PRODUCT_MODEL";

        //language=SQL
        List<Object> rebateSumSqlArgs = new ArrayList<>();
        String rebateSumSql = "SELECT BIZ_ID,PRODUCT_MODEL,sum(APPLY_QTY) as rebateQty from (" +
                "WITH Rebate_Line as (SELECT rl.REBATE_ID,rl.ROW_ID from CRM_T_REBATE_LINE rl,CRM_T_REBATE_HEADER rh WHERE rh.ROW_ID=rl.REBATE_ID ";

        if (StringUtils.isNotEmpty(rebateNo)) {
            rebateSumSql += " and rh.no != ?";
            rebateSumSqlArgs.add(rebateNo);
        }
        rebateSumSql += ")," +
                "    Rebate_Change as (SELECT REBATE_ID,max(C_VERSION) as C_VERSION from CRM_T_REBATE_HEADER_CHANGE WHERE STATUS != 'Destroyed' and REBATE_ID in (SELECT REBATE_ID from Rebate_Line) GROUP BY REBATE_ID)," +
                "    Rebate_Product_Count as (SELECT rl.REBATE_ID,rl.PRODUCT_MODEL,rl.APPLY_QTY,rl.BIZ_ID from CRM_T_REBATE_HEADER rh,CRM_T_REBATE_LINE rl WHERE rh.ROW_ID=rl.REBATE_ID and rh.STATUS!='Destroyed' and rl.REBATE_ID not in (SELECT REBATE_ID from Rebate_Change) and rl.REBATE_ID in (SELECT REBATE_ID from Rebate_Line))," +
                "    Rebate_Change_Product_Count as (SELECT rlc.REBATE_ID,rlc.PRODUCT_MODEL,rlc.APPLY_QTY,rlc.BIZ_ID from CRM_T_REBATE_HEADER_CHANGE rhc, CRM_T_REBATE_LINE rlc WHERE rhc.ROW_ID=rlc.REBATE_ID AND (rhc.REBATE_ID,rhc.C_VERSION) IN (SELECT REBATE_ID,C_VERSION FROM Rebate_Change))" +
                "SELECT * from Rebate_product_count r WHERE REBATE_ID not IN (SELECT REBATE_ID from Rebate_Change) UNION ALL " +
                "SELECT * from Rebate_Change_Product_Count) ";
        rebateSumSql += " GROUP BY BIZ_ID,PRODUCT_MODEL";

        List<Object> args = new ArrayList<>();
        //language=SQL
        String sql = "select distinct bo.C_PID as \"id\", " +
                "bo.C_NUMBER as \"number\", " +
                "bo.C_OPPORTUNITY_NAME as \"opportunityName\" , " +
                "bo.C_CLIENT_ID as \"clientId\" , " +
                "bo.C_CLIENT_NAME as \"clientName\" " +
                "from CRM_T_BUSINESS_OPPORTUNITY bo " +
                "where bo.C_PID in (" + bizOppSql + ")" +
                " and NVL((select N_PLAN_COUNT from  (" + bizOppSumSql + ") where C_BIZOPP_ID=bo.C_PID and C_PRODUCT_MODEL=?),0) > NVL((SELECT rebateQty from (" + rebateSumSql + ") WHERE BIZ_ID=bo.C_PID and PRODUCT_MODEL=?),0)";
        args.addAll(bizOppSqlArgs);
        args.add(productModel);
        args.addAll(rebateSumSqlArgs);
        args.add(productModel);

        List list = this.baseDao.findBySql4Map(sql, args.toArray());
        return list;
    }

    public static Date getNextDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, +90);// +90今天的时间加90天
        date = calendar.getTime();
        return date;
    }

	@Override
	public BusinessOpportunity getBusinessOpportunityByNumberAndOrg(String number,String org) {
		BusinessOpportunity b = baseDao.findUniqueEntity(" select b from BusinessOpportunity b where b.number= ?",new String[]{number});
		StringBuffer sb = new StringBuffer();
		sb.append(" select e from Employee e ,UserPermission up where e.id = up.userId and up.type = 'P' and up.memberId in  ");
		sb.append(" (select t.positionId from Team t , ");
		sb.append(" Employee e ,LovMember p,LovMember o ,LovMember org ");
		sb.append(" where t.positionId = p.id and p.groupId = 'POSITION' ");
		sb.append(" and o.groupId='ORG' and t.masterEmployeeId = e.id ");
		sb.append(" and p.optTxt1 = o.id and o.parentId = org.id and t.businessId = '"+b.getId()+"' and t.businessType = 'BusinessOpportunity') and e.no = '"+org+"' ");
		List<Employee> vo = baseDao.findEntity(sb.toString());
		if(vo.size()>0){
			return b;
		}
		return null;
	}

	@Override
	public void saveBiddingFeedBack(BiddingFeedBack entity, UserObject userObject) {
		BiddingFeedBack oldEntity = getBidFeedBack(entity.getBidId());
		if(oldEntity == null){			
			baseDao.save(entity);
		}else {
			BeanUtils.copyPropertiesIgnoreNull(entity, oldEntity);
			baseDao.update(oldEntity);
		}
	}

	@Override
	public BiddingFeedBack getBidFeedBack(String bizOppId) {
		String hql = " from BiddingFeedBack where bidId = ? ";
		BiddingFeedBack fBack = baseDao.findUniqueEntity(hql,new Object[]{bizOppId});
		return fBack;
	}
	
	@Override
	public BiddingFeedBack getBidFeedBackById(String id) {
		String hql = " from BiddingFeedBack where id = ? ";
		BiddingFeedBack fBack = baseDao.findUniqueEntity(hql,new Object[]{id});
		return fBack;
	}

    @Override
    public void updateForConflictAppeal(BusinessOpportunity opportunity) {
        //language=HQL
        this.baseDao.executeHQL("update BusinessOpportunity set conflictStatus=?,conflictAppeal=?,conflictAppealRemark=? where id=?",
                new Object[]{opportunity.getConflictStatus(), opportunity.getConflictAppeal(), opportunity.getConflictAppealRemark(), opportunity.getId()});
    }

    /**
	 * 启动提交反馈流程
	 */
	@Override
	public void startFeedBackProcess(String id, UserObject userObject) {
		// TODO Auto-generated method stub
		BiddingFeedBack bFeedBack = getBidFeedBack(id);
		BusinessOpportunity bo = getBizOppEntity(id);
		String application = "BIZOPP_BID_FEED_BACK";
		String model = lovMemberService.getFlowCodeByAppCode(application);
		Map<String, String> vmap = new HashMap<>();
        String salesCenter = lovMemberService.getSaleCenter(userObject.getOrg().getId());
        vmap.put("SalesCenter", salesCenter);
        vmap.put("EmployeeType", userObject.getEmployee().getFlag());
        
        //更新流程状态为已发起,记录流程ID
        processStatusService.updateProcessStatus("BiddingFeedBack", bFeedBack.getId(), "status", ProcessConstants.PROCESS_STATUS_Processing);
        
        xflowProcessServiceWrapper.start(
                model, application, bo.getOpportunityName()+"_"+ProcessConstants.BIZOPP_BID_FEED_BACK, bFeedBack.getId(), userObject, vmap);
	}
}
	
	