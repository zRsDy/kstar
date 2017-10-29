package com.ibm.kstar.impl.product;

import com.ibm.kstar.api.product.IProLovService;
import com.ibm.kstar.api.system.lov.ILovGroupService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovGroup;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.impl.BaseServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.StringUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class ProLovServiceImpl extends BaseServiceImpl implements IProLovService {

    @Autowired
    BaseDao baseDao;

    @Autowired
    ILovGroupService lovGroupService;
    
    @Autowired
	protected ILovMemberService lovMemberService;

    @Override
    public void saveCatelog(LovMember lovMember) throws AnneException {
        checkCodeCatelog(lovMember);
        LovGroup lovGroup = lovGroupService.get(lovMember.getGroupId());
        if (lovGroup == null) {
            throw new AnneException("不存在的LOV：" + lovMember.getGroupId());
        }
        lovMember.setGroupCode(lovGroup.getCode());
        baseDao.save(lovMember);
        updateTreeInfo(lovMember);
    }

    //暂停使用
    @Override
    public void updateBaobei(String lovId, String baobei) throws AnneException {
//		String sql ="update SYS_T_LOV_MEMBER m set m.opt_txt2 = ? where exists (select m1.row_id from SYS_T_LOV_MEMBER m1" 
//				+" where m1.row_id = m.row_id"
//				+" start with m1.row_id = ?"
//				+" connect by prior m1.parent_id = m1.row_id)";
//		baseDao.executeSQL(sql, new Object[]{baobei,lovId});

        String sql = "update SYS_T_LOV_MEMBER m set m.opt_txt2 = ? where m.lov_path like ? and m.group_code = 'procatalog' ";
        baseDao.executeSQL(sql, new Object[]{baobei, "%" + lovId + "%"});
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LovMember> getFatherList(String lovCode) throws AnneException {
        String sql = " from LovMember lovmember where 1=1  and lovmember.code=?";
        List<LovMember> list = baseDao.findEntity(sql, new Object[]{lovCode});
        Set<String> idSet = new HashSet<String>();
        for (LovMember lm : list) {
            String[] idArray = lm.getPath().split("/");
            for (String id : idArray) {
                if (StringUtils.isNotBlank(id)) {
                    idSet.add(id);
                }
            }
        }
        if (idSet.isEmpty()) {
            return null;
        } else {
            sql = " from LovMember lovmember where 1=1  and lovmember.id in (:codes) ";

            Query q = baseDao.getSessionFactory().getCurrentSession().createQuery(sql);
            q.setParameterList("codes", idSet);
            List<LovMember> reValue = q.list();
            return reValue;
        }
    }

    /**
     * 检查Code字段是否有重复
     *
     * @param lovGroup
     */
    private void checkCodeCatelog(LovMember lovMember) {
        Long count = baseDao.findUniqueEntity("select count(*) from LovMember g where g.groupId=? and g.code = ? and g.parentId = ? ", new Object[]{lovMember.getGroupId(), lovMember.getCode(), lovMember.getParentId()});
        if (count > 0) {
            throw new AnneException("已经存在相同的代码：" + lovMember.getCode());
        }
    }

    private void updateTreeInfo(LovMember lovMember) {
        if (StringUtil.isEmpty(lovMember.getParentId()) || "ROOT".equals(lovMember.getParentId())) {
            lovMember.setPath("/" + lovMember.getId());
            lovMember.setNamePath("/" + lovMember.getName());
            lovMember.setCodePath("/" + lovMember.getCode());
            lovMember.setLevel(1);
            lovMember.setParentId("ROOT");
        } else {
            LovMember parentFieldMember = baseDao.get(LovMember.class, lovMember.getParentId());
            if (parentFieldMember == null) {
                throw new AnneException("无效的父级节点！");
            }
            lovMember.setPath(parentFieldMember.getPath() + "/" + lovMember.getId());
            lovMember.setNamePath(parentFieldMember.getNamePath() + "/" + lovMember.getName());
            lovMember.setCodePath(parentFieldMember.getCodePath() + "/" + lovMember.getCode());
            lovMember.setLevel(parentFieldMember.getLevel() + 1);
        }
        baseDao.update(lovMember);
    }

    @Override
    public IPage mappage(PageCondition condition, UserObject user) throws AnneException {
        List<Object> args = new ArrayList<Object>();
        StringBuilder sb = getSql(condition, user, args);
        return baseDao.searchBySql4Map(sb.toString(), args.toArray(), condition.getRows(), condition.getPage());
    }

    @Override
    public List<List<Object>> mappageExport(PageCondition condition, UserObject user) throws AnneException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<List<Object>> lstOut = new ArrayList<List<Object>>();
        addHead(lstOut);
        List<Object> args = new ArrayList<Object>();
        StringBuilder sb = getSql(condition, user, args);
        List<Object[]> lst = baseDao.findBySql(sb.toString(), args.toArray());
        int index = 0;
        for (Object[] obj : lst) {
            List<Object> lstIn = new ArrayList<Object>();
            index = 2;
            for (int i = 0; i < 8; i++) {
                lstIn.add(obj[index++]);
            }
            Object time = obj[index++];
            if (time != null) {
                lstIn.add(sdf.format(time));
            } else {
                lstIn.add("");
            }

            if (obj[index] != null) {
                lstIn.add(sdf.format(obj[index++]));
            } else {
                index++;
                lstIn.add("");
            }
            if ("N".equals(obj[index++])) {
                lstIn.add("有效");
            } else {
                lstIn.add("无效");
            }
            for (int i = 0; i < 8; i++) {
                lstIn.add(obj[index++]);
            }
            lstOut.add(lstIn);
        }
        return lstOut;
    }

    @Override
    public IPage demandSelectProduct(PageCondition condition, UserObject user) throws AnneException {
        List<Object> args = new ArrayList<>();
        StringBuilder sql = new StringBuilder("select");
        sql.append(" p.c_pid \"id\",");
        sql.append(" p.c_pro_code \"proCode\",");
        sql.append(" p.c_pro_name \"proName\",");
        sql.append(" p.C_PRO_MODEL \"proModel\",");
        sql.append(" p.c_brand \"proBrand\",");
        sql.append(" m1.lov_name \"crmCategory\",");
        sql.append(" m2.lov_name \"erpCategory\",");
        sql.append(" m3.lov_name \"publishStatus\",");
        sql.append(" m4.lov_name \"priceStatus\",");
        sql.append(" m5.lov_name \"saleStatus\",");
        sql.append(" l.C_PRO_CATEGORY \"cproCategory\",");
        sql.append(" l.C_PRO_TYPE \"cproType\",");
        sql.append(" l.C_PRO_SERIES \"proSeries\",");
        sql.append(" p.C_CLIENT_CATEGORY \"clientCategory\"");
        sql.append(" from crm_t_product_basic p");
        sql.append(" inner join CRM_T_TEAM t on p.c_pid = t.business_id and t.position_id = ?");
        sql.append(" left join crm_t_product_line l on p.c_pro_line_id = l.c_pid");
        sql.append(" left join sys_t_lov_member m1 on p.C_PRO_CRM_CATEGORY = m1.lov_code and m1.group_code = 'crmCategory'");
        sql.append(" left join sys_t_lov_member m2 on p.C_PRO_ERP_CATEGORY = m2.lov_code and m2.group_code = 'erpCategory'");
        sql.append(" left join sys_t_lov_member m3 on p.C_PRO_PUBLISH_STATUS = m3.row_id");
        sql.append(" left join sys_t_lov_member m4 on p.C_PRO_PRICE_STATUS = m4.row_id");
        sql.append(" left join sys_t_lov_member m5 on p.C_PRO_SALE_STATUS = m5.row_id");
        sql.append(" where p.C_MATER_CODE is null and (m1.lov_code = '0002' or m1.lov_code = '0005')");
        sql.append(" and not exists (select d.c_pid from CRM_T_PRODUCT_DEMAND d where d.c_pro_id = p.c_pid)");
        sql.append(" and not exists (select r.c_pid from CRM_T_PRODUCT_DEMAND_REL r where r.c_product_id = p.c_pid)");
        args.add(user.getPosition().getId());
        this.addQueryCondition(condition, args, sql, "name", "m1.lov_name", "like");
        this.addQueryCondition(condition, args, sql, "productCode", "p.c_pro_code", "like");
        this.addQueryCondition(condition, args, sql, "productName", "p.c_pro_name", "like");
        this.addQueryCondition(condition, args, sql, "proModel", "p.c_pro_model", "like");
        this.addQueryCondition(condition, args, sql, "proBrand", "p.C_BRAND", "like");
        this.addQueryCondition(condition, args, sql, "crmCategory", "p.c_pro_crm_category", "=");
        this.addQueryCondition(condition, args, sql, "erpCategory", "p.C_PRO_ERP_CATEGORY", "=");
        this.addQueryCondition(condition, args, sql, "publishStatus", "p.C_PRO_PUBLISH_STATUS", "=");
        this.addQueryCondition(condition, args, sql, "priceStatus", "p.C_PRO_PRICE_STATUS", "=");
        this.addQueryCondition(condition, args, sql, "saleStatus", "p.C_PRO_SALE_STATUS", "=");
        this.addQueryCondition(condition, args, sql, "clientCategory", "p.C_CLIENT_CATEGORY", "like");
        this.addQueryCondition(condition, args, sql, "cproCategory", "l.C_PRO_CATEGORY", "like");
        this.addQueryCondition(condition, args, sql, "cproType", "l.C_PRO_TYPE", "like");
        this.addQueryCondition(condition, args, sql, "proSeries", "l.C_PRO_SERIES", "like");
        this.addQueryCondition(condition, args, sql, "modifyHardware", "p.C_MODIFY_HARDWARE", "like");
        this.addQueryCondition(condition, args, sql, "addFunction", "p.C_ADD_FUNCTION", "like");
        this.addQueryCondition(condition, args, sql, "modifyParamter", "p.C_MODIFY_PARAMTER", "like");
        this.addQueryCondition(condition, args, sql, "chassisSize", "p.C_CHASSIS_SIZE", "like");
        this.addQueryCondition(condition, args, sql, "commercialData", "p.C_COMMERCIAL_DATA", "like");
        this.addQueryCondition(condition, args, sql, "randomAttach", "p.C_RANDOM_ATTACH", "like");
        this.addQueryCondition(condition, args, sql, "other", "p.C_OTHER", "like");
        return baseDao.searchBySql4Map(sql.toString(), args.toArray(), condition.getRows(), condition.getPage());
    }

    private StringBuilder getSql(PageCondition condition, UserObject user, List<Object> args) {
    	String parentId = condition.getStringCondition("parentId");
    	
        //因导出按字段顺序取出，需保证不改变数组下标
        StringBuilder sb = new StringBuilder("select distinct");
        sb.append(" m.row_id \"id\",");
        sb.append(" m1.lov_code \"code\",");
        sb.append(" m1.lov_name \"name\",");
        sb.append(" m1.NAME_PATH \"namePath\",");
        sb.append(" p.c_pro_code \"proCode\",");
        sb.append(" p.c_pro_name \"proName\",");
        sb.append(" p.c_pro_desc \"proDesc\",");
        sb.append(" (select m1.lov_name from SYS_T_LOV_MEMBER m1 where p.c_unit = m1.row_id) \"proUnit\",");
        sb.append(" p.c_pro_model \"proModel\",");
        sb.append(" (select m1.lov_name from SYS_T_LOV_MEMBER m1 where p.c_pro_crm_category = m1.lov_code and m1.group_code = 'crmCategory')  \"crmCategory\",");
        sb.append(" m.start_date \"startDate\",");
        sb.append(" m.end_date \"endDate\",");
        sb.append(" m.delete_flag \"deleteFlag\",");
        sb.append(" m.memo \"memo\",");
        sb.append(" p.C_MODIFY_HARDWARE \"modifyHardware\",p.C_ADD_FUNCTION \"addFunction\",p.C_MODIFY_PARAMTER \"modifyParamter\",p.C_CHASSIS_SIZE \"chassisSize\","
                + "p.C_COMMERCIAL_DATA \"commercialData\",p.C_RANDOM_ATTACH \"randomAttach\",p.C_OTHER \"other\",");
        sb.append(" m.parent_id \"parentId\",");
        sb.append(" m.leaf_flag \"leafFlag\",");
        sb.append(" m.opt_txt5 \"optTxt5\",");
        sb.append(" p.c_pid \"proId\",");
        sb.append(" p.c_brand \"proBrand\",");
        sb.append(" p.C_CLIENT_CATEGORY \"clientCategory\",");
        sb.append(" (select m2.lov_name from SYS_T_LOV_MEMBER m2 where p.c_pro_erp_category = m2.lov_code and m2.group_code = 'erpCategory')  \"erpCategory\",");
        sb.append(" (select m1.lov_name from SYS_T_LOV_MEMBER m1 where p.C_PRO_PUBLISH_STATUS = m1.row_id)  \"publishStatus\",");
        sb.append(" (select m1.lov_name from SYS_T_LOV_MEMBER m1 where p.C_PRO_PRICE_STATUS = m1.row_id)  \"priceStatus\",");
        sb.append(" (select m1.lov_name from SYS_T_LOV_MEMBER m1 where p.C_PRO_SALE_STATUS = m1.row_id)  \"saleStatus\",");
        sb.append(" l.C_PRO_CATEGORY \"cproCategory\",");
        sb.append(" l.C_PRO_TYPE \"cproType\",");
        sb.append(" l.C_PRO_SERIES \"proSeries\"");
        sb.append(" from  ");
        if(!StringUtil.isNullOrEmpty(parentId)&&user!=null) {
        	sb.append(" crm_t_product_permission_rel pr, ");
        }
        sb.append(" SYS_T_LOV_MEMBER m ");
        sb.append(" inner join SYS_T_LOV_MEMBER m1 on m.PARENT_ID = m1.ROW_ID");

        if (user != null && user.getOrg().getPath().contains("P_GJORG_B1_0001")) {
            String sql_inner = "select distinct p1.* from"
                    + " crm_t_product_basic p1"
                    + " left join CRM_T_PRODUCT_DEMAND_REL dr on p1.c_pid = dr.c_product_id"
                    + " left join CRM_T_PRODUCT_DEMAND d on dr.c_demand_id = d.c_pid"
                    + " left join crm_t_custom_info c on c.c_pid = d.c_client_code"
                    + " left join CRM_T_TEAM t on c.c_pid = t.business_id"
//							  +" left join SYS_T_PERMISSION_USER_REL u on t.position_id = u.member_id"
                    + " left join sys_t_lov_member position on t.position_id = position.row_id"
                    + " left join sys_t_lov_member org on position.opt_txt1 = org.row_id"
//							  +" where p1.c_pro_crm_category != '0002' or (p1.c_pro_crm_category = '0002' and u.user_id = '"+user.getEmployee().getId()+"')";
                    + " where p1.c_pro_crm_category != '0002' or (dr.C_PID is NULL or (p1.c_pro_crm_category = '0002' and org.lov_path like '%" + user.getOrg().getId() + "%'))";
            sb.append(" inner join (" + sql_inner + ") p on p.c_pid = m.opt_txt5");
        } else {
            sb.append(" inner join CRM_T_PRODUCT_BASIC p on p.c_pid = m.opt_txt5");
        }
        String demandId = condition.getStringCondition("demandId");
        if (StringUtil.isNotEmpty(demandId)) {
            sb.append(" inner join SYS_T_LOV_MEMBER mp on p.C_PRO_SALE_STATUS = mp.ROW_ID and mp.LOV_CODE != '05'");
        } else {
            sb.append(" inner join SYS_T_LOV_MEMBER mp on p.C_PRO_SALE_STATUS = mp.ROW_ID and (mp.LOV_CODE = '03' or mp.LOV_CODE = '05')");
        }
        sb.append(" left join CRM_T_PRODUCT_LINE l on p.C_PRO_LINE_ID = l.C_PID");
        sb.append(" where m.delete_flag = 'N'");
        
        String groupId = condition.getStringCondition("groupId");
        
        sb.append(" and m.group_id = ?");
        args.add(groupId);
        if (StringUtil.isNotEmpty(parentId)) {
        	if("ROOT".equals(parentId)||user==null) {
        		sb.append(" and m.lov_path like ?");
                args.add("%" + parentId + "%");
        	}else {
        		sb.append(" and m.PARENT_ID = pr.PRODUCT_CATALOG_ID ");
            	sb.append(" and (m.PARENT_ID like ? ");
            	sb.append(" or m.lov_path like ? )");
            	args.add("%" + parentId + "%");
            	args.add("%" + parentId + "%");
        		sb.append(" and (pr.ORG_ID = ?)  ");
        		args.add(user.getPosition().getPositionInOrgId());
        	}
        }
        if (StringUtil.isNotEmpty(demandId)) {
            sb.append(" and not exists (select r.c_pid from CRM_T_PRODUCT_DEMAND_REL r where r.c_product_id = p.c_pid and r.c_demand_id = ?)");
            args.add(demandId);
        }
        String crmCategorys = condition.getStringCondition("crmCategorys");
        if (StringUtil.isNotEmpty(crmCategorys)) {
            String[] ss = crmCategorys.split(",");
            for (int k = 0; k < ss.length; k++) {
                if (k == 0) {
                    sb.append(" and (p.c_pro_crm_category = ?");
                    args.add(ss[k]);
                } else {
                    sb.append(" or p.c_pro_crm_category = ?");
                    args.add(ss[k]);
                }
            }
            sb.append(")");
        }
        this.addQueryCondition(condition, args, sb, "name", "m1.lov_name", "like");
        this.addQueryCondition(condition, args, sb, "productCode", "p.c_pro_code", "like");
        this.addQueryCondition(condition, args, sb, "productName", "p.c_pro_name", "like");
        this.addQueryCondition(condition, args, sb, "proModel", "p.c_pro_model", "like");
        this.addQueryCondition(condition, args, sb, "proBrand", "p.C_BRAND", "like");
        this.addQueryCondition(condition, args, sb, "proDesc", "p.c_pro_desc", "like");
        this.addQueryCondition(condition, args, sb, "crmCategory", "p.c_pro_crm_category", "=");
        this.addQueryCondition(condition, args, sb, "erpCategory", "p.C_PRO_ERP_CATEGORY", "=");
        this.addQueryCondition(condition, args, sb, "publishStatus", "p.C_PRO_PUBLISH_STATUS", "=");
        this.addQueryCondition(condition, args, sb, "priceStatus", "p.C_PRO_PRICE_STATUS", "=");
        this.addQueryCondition(condition, args, sb, "saleStatus", "p.C_PRO_SALE_STATUS", "=");
        this.addQueryCondition(condition, args, sb, "clientCategory", "p.C_CLIENT_CATEGORY", "like");
        this.addQueryCondition(condition, args, sb, "cproCategory", "l.C_PRO_CATEGORY", "like");
        this.addQueryCondition(condition, args, sb, "cproType", "l.C_PRO_TYPE", "like");
        this.addQueryCondition(condition, args, sb, "proSeries", "l.C_PRO_SERIES", "like");
        this.addQueryCondition(condition, args, sb, "modifyHardware", "p.C_MODIFY_HARDWARE", "like");
        this.addQueryCondition(condition, args, sb, "addFunction", "p.C_ADD_FUNCTION", "like");
        this.addQueryCondition(condition, args, sb, "modifyParamter", "p.C_MODIFY_PARAMTER", "like");
        this.addQueryCondition(condition, args, sb, "chassisSize", "p.C_CHASSIS_SIZE", "like");
        this.addQueryCondition(condition, args, sb, "commercialData", "p.C_COMMERCIAL_DATA", "like");
        this.addQueryCondition(condition, args, sb, "randomAttach", "p.C_RANDOM_ATTACH", "like");
        this.addQueryCondition(condition, args, sb, "other", "p.C_OTHER", "like");

        sb.append(" order by m1.NAME_PATH asc,m.row_id desc");
        return sb;
    }

    private void addHead(List<List<Object>> lstOut) {
        List<Object> lstHead = new ArrayList<Object>();
        lstHead.add("目录名称");
        lstHead.add("目录层级");
        lstHead.add("产品编码");
        lstHead.add("产品名称");
        lstHead.add("产品描述");
        lstHead.add("产品单位");
        lstHead.add("产品型号");
        lstHead.add("CRM产品类别");
        lstHead.add("有效开始时间");
        lstHead.add("有效结束时间");
        lstHead.add("有效");
        lstHead.add("说明");
        lstHead.add("修改硬件");
        lstHead.add("增加功能");
        lstHead.add("更改参数");
        lstHead.add("机箱尺寸/外观变更");
        lstHead.add("商品化资料");
        lstHead.add("增加随机附件");
        lstHead.add("其他");
        lstOut.add(lstHead);
    }

}
