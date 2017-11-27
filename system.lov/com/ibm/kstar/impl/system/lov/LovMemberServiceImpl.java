package com.ibm.kstar.impl.system.lov;

import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.system.lov.ILovGroupService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovGroup;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.impl.BaseServiceImpl;
import org.apache.commons.lang.StringUtils;
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
import org.xsnake.web.utils.StringUtil;

import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class LovMemberServiceImpl extends BaseServiceImpl implements ILovMemberService {

    @Autowired
    BaseDao baseDao;

    @Autowired
    ILovGroupService lovGroupService;

    @Override
    public void save(LovMember lovMember) throws AnneException {
        checkCode(lovMember);
        LovGroup lovGroup = lovGroupService.get(lovMember.getGroupId());
        if (lovGroup == null) {
            throw new AnneException("不存在的LOV：" + lovMember.getGroupId());
        }
        lovMember.setGroupCode(lovGroup.getCode());
        lovMember.setCreationDate(new Date());
        lovMember.setLastUpdatedDate(new Date());
        lovMember.setCode(lovMember.getCode().trim());
        lovMember.setName(lovMember.getName().trim());
        baseDao.save(lovMember);
        updateTreeInfo(lovMember);
    }


    @Override
    public boolean saveProduct(LovMember lovMember) {
        boolean check = checkProductCode(lovMember);

        if (check)
            return false;
        LovGroup lovGroup = lovGroupService.get(lovMember.getGroupId());
        if (lovGroup == null) {
            return false;
        }
        lovMember.setGroupCode(lovGroup.getCode());
        lovMember.setCreationDate(new Date());
        lovMember.setLastUpdatedDate(new Date());
        baseDao.save(lovMember);
        updateTreeInfo(lovMember);
        return true;
    }

    /**
     * 检查Code字段是否有重复
     *
     * @param lovGroup
     */
    private boolean checkProductCode(LovMember lovMember) {
        LovGroup lovGroup = baseDao.get(LovGroup.class, lovMember.getGroupId());
        if (!"Y".equals(lovGroup.getRepeatFlag())) {
            Long count = 0l;
            if (lovMember.getId() == null) {
                count = baseDao.findUniqueEntity("select count(*) from LovMember g where g.groupId=?  and g.parentId = ? and g.optTxt5 = ? ", new Object[]{lovMember.getGroupId(), lovMember.getParentId(), lovMember.getOptTxt5()});
            } else {
                count = baseDao.findUniqueEntity("select count(*) from LovMember g where g.groupId=?  and g.id != ? and g.parentId = ? and g.optTxt5 = ? ", new Object[]{lovMember.getGroupId(), lovMember.getId(), lovMember.getParentId(), lovMember.getOptTxt5()});
            }
            if (count > 0) {
                return true;
            }
        }
        return false;
    }


    /**
     * 检查Code字段是否有重复
     *
     * @param lovGroup
     */
    private void checkCode(LovMember lovMember) {

        if (Objects.equals(lovMember.getGroupCode(), "PRODUCTMODE")) {
            Long count;
            if (StringUtils.isEmpty(lovMember.getId())) {
                count = baseDao.findUniqueEntity("select count(*) from LovMember g where g.deleteFlag='N' and g.groupId=? and (g.code = ? or g.name=?) ", new Object[]{lovMember.getGroupId(), lovMember.getCode(), lovMember.getName()});
            } else {
                count = baseDao.findUniqueEntity("select count(*) from LovMember g where g.deleteFlag='N' and g.groupId=? and (g.code = ? or g.name=?) and g.id != ? ", new Object[]{lovMember.getGroupId(), lovMember.getCode(), lovMember.getName(), lovMember.getId()});
            }
            if (count > 0) {
                throw new AnneException("已经存在相同的编码(" + lovMember.getCode() + ")或型号(" + lovMember.getName() + ")。");
            }
            return;
        }

        LovGroup lovGroup = baseDao.get(LovGroup.class, lovMember.getGroupId());
        if (!"Y".equals(lovGroup.getRepeatFlag())) {
            Long count;
            if (lovMember.getId() == null) {
                count = baseDao.findUniqueEntity("select count(*) from LovMember g where g.deleteFlag='N' and g.groupId=? and g.code = ? ", new Object[]{lovMember.getGroupId(), lovMember.getCode()});
            } else {
                count = baseDao.findUniqueEntity("select count(*) from LovMember g where g.deleteFlag='N' and g.groupId=? and g.code = ? and g.id != ? ", new Object[]{lovMember.getGroupId(), lovMember.getCode(), lovMember.getId()});
            }
            if (count > 0) {
                throw new AnneException("已经存在相同的代码：" + lovMember.getCode());
            }
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
        move(lovMember.getId(), lovMember.getParentId());
    }

    @Override
    public void update(LovMember lovMember) throws AnneException {
        LovMember oldLovMember = baseDao.get(LovMember.class, lovMember.getId());
        //TODO 临时注释
        //		if(lovMember.getCode() != null && !oldLovMember.getCode().equals(lovMember.getCode())){
        //			throw new AnneException("编码一旦确定无法修改");
        //		}
        BeanUtils.copyPropertiesIgnoreNull(lovMember, oldLovMember);
        oldLovMember.setLastUpdatedDate(new Date());
        oldLovMember.setCode(oldLovMember.getCode().trim());
        oldLovMember.setName(oldLovMember.getName().trim());
        baseDao.update(oldLovMember);
        updateTreeInfo(oldLovMember);
        checkCode(oldLovMember);
    }

    @Override
    public void updateTree(LovMember lovMember) throws AnneException {
        LovMember kuLovMember = baseDao.get(LovMember.class, lovMember.getId());
        String oldNamePath = kuLovMember.getNamePath();
        this.update(lovMember);
        String newNamePath = kuLovMember.getNamePath();    //lovMember更新后，kuLovMember自动同步数据库数据
        String lovPath = kuLovMember.getPath();
        //修改子孙的namepath
        String updateAllNamePath = "update sys_t_lov_member m set m.name_path = ? || ltrim(m.name_path,?) where m.lov_path like ?";
        List<Object> args = new ArrayList<>();
        args.add(newNamePath + "/");
        args.add(oldNamePath + "/");
        args.add(lovPath + "/%");
        baseDao.executeSQL(updateAllNamePath, args.toArray());
    }

    @Override
    public IPage query(PageCondition condition) throws AnneException {
        FilterObject filterObject = condition.getFilterObject(LovMember.class);
        HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
        return baseDao.search(hqlObject.getHql(), hqlObject.getArgs(), condition.getRows(), condition.getPage());
    }

    @Override
    public List<LovMember> find(Condition condition) throws AnneException {
        FilterObject filterObject = condition.getFilterObject(LovMember.class);
        HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
        return baseDao.findEntity(hqlObject.getHql(), hqlObject.getArgs());
    }

    public List<LovMember> getLovMemberList4Cache(Condition condition) throws AnneException {
        List<Object> args = new ArrayList<Object>();
        StringBuffer hql = null;
        hql = new StringBuffer(" select p from LovMember p where 1 = 1 ");
        if (condition.getCondition("lastUpdatedDate") != null) {
            hql.append(" and lastUpdatedDate > ? ");
            args.add(condition.getCondition("lastUpdatedDate"));
        }
        return baseDao.findEntity(hql.toString(), args.toArray());
    }

    @Override
    public void delete(String lovMemberId) throws AnneException {
        Long count = baseDao.findUniqueEntity(" select count(*) from LovMember m where m.parentId = ? and m.deleteFlag = 'N' ", lovMemberId);
        if (count > 0) {
            throw new AnneException("存在下级节点不允许被删除!");
        }
        baseDao.deleteById(LovMember.class, lovMemberId);
    }

    @Override
    public void deletes(String[] lovMemberIds) throws AnneException {
        for (String lovMemberId : lovMemberIds) {
            this.delete(lovMemberId);
        }
    }

    @Override
    public void deleteTree(String lovMemberId, String groupId) throws AnneException {
        //黄奇 2017-08-02 修改 Bug 注释
        //Bug文件：CRM上线问题清单20170731-update by云燕		Bug序号：634
        //		String queyProCount = "select count(*) from"
        //							+" (select opt_txt5 from sys_t_lov_member where delete_flag = 'N' start with row_id = ? connect by prior row_id = parent_id) m"
        //							+" ,crm_t_product_basic p where m.opt_txt5 = p.c_pid";
        String sql = " SELECT * " +
                " FROM SYS_T_LOV_MEMBER m " +
                " INNER JOIN CRM_T_PRODUCT_BASIC p ON p.c_pid = m.opt_txt5 " +
                " LEFT JOIN CRM_T_PRODUCT_LINE l ON p.C_PRO_LINE_ID = l.C_PID " +
                " INNER JOIN SYS_T_LOV_MEMBER mp ON p.C_PRO_SALE_STATUS = mp.ROW_ID AND (mp.LOV_CODE = '03' OR mp.LOV_CODE = '05') " +
                " WHERE m.delete_flag = 'N' AND m.group_id = ? AND m.lov_path LIKE '%'||?||'%'";
        String queyProCount = "select count(*) from ( " + sql + ")";
        BigDecimal count = (BigDecimal) baseDao.findUniqueBySql(queyProCount, new Object[]{groupId, lovMemberId});
        if (count.longValue() > 0) {
            throw new AnneException("该目录下有产品关联，不能被删除!");
        }
        String deleteTree = "delete from sys_t_lov_member"
                + " where row_id in"
                + " (select row_id from sys_t_lov_member where delete_flag = 'N' start with row_id = ? connect by prior row_id = parent_id)";
        baseDao.executeSQL(deleteTree, lovMemberId);
    }

    @Override
    public void deleteByMatchID(String matchID) throws AnneException {
        baseDao.executeSQL("delete from SYS_T_LOV_MEMBER where LEAF_FLAG = 'Y' and OPT_TXT1 = '" + matchID + "' ");
    }

    @Override
    public LovMember get(String lovMemberId) throws AnneException {
        return baseDao.get(LovMember.class, lovMemberId);
    }

    @Override
    public List<LovMember> getList(Condition condition) throws AnneException {
        FilterObject filterObject = condition.getFilterObject(LovMember.class);
        filterObject.addOrderBy("no", "asc");
        HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
        return baseDao.findEntity(hqlObject.getHql(), hqlObject.getArgs());
    }

    @Override
    public List<Map<String, Object>> getSelectList(PageCondition condition, UserObject user) throws AnneException {
        String level2Sql = "select row_id,lov_code from SYS_T_LOV_MEMBER where lov_level = 2 start with row_id = ? connect by prior PARENT_ID = ROW_ID";
        List<Object[]> lst = baseDao.findBySql(level2Sql, user.getOrg().getId());
        Set<String> orgs = new HashSet<>();
        orgs.add("P_GNQCORG_B1_0001");    //新能源汽车行业部
        orgs.add("P_GNORG_B1_0001");    //国内数据中心
        orgs.add("P_GJORG_B1_0001");    //国际营销中心
        orgs.add("P_GNGFORG_B1_0001");    //新能源国内营销
        if (lst != null && lst.size() > 0) {
            Object[] objects = lst.get(0);
            if (orgs.contains(objects[1].toString())) {
                StringBuilder sb = new StringBuilder();
                sb.append("select ROW_ID \"id\",LOV_NAME \"name\",PARENT_ID \"parentId\",LEAF_FLAG \"leafFlag\",LOV_LEVEL \"level\",OPT_TXT3 \"optTxt3\" from ");
                sb.append("(select * from SYS_T_LOV_MEMBER where row_id = (select parent_id from SYS_T_LOV_MEMBER where lov_code = ?)");
                sb.append(" union select * from SYS_T_LOV_MEMBER start with lov_code = ? connect by prior ROW_ID = PARENT_ID)");
                List<Object> args = new ArrayList<>();
                args.add(objects[1].toString());
                args.add(objects[1].toString());
                return baseDao.findBySql4Map(sb.toString(), args.toArray());
            }
        }
        return null;
    }


    @Override
    public List<LovMember> getListForUpdate(Condition condition, UserObject user) throws AnneException {
        return _getList(condition, user, "PermissionOptRel");
    }

    @Override
    public List<LovMember> getList(Condition condition, UserObject user) throws AnneException {
        return _getList(condition, user, "PermissionRel");
    }

    private List<LovMember> _getList(Condition condition, UserObject user, String entityName) throws AnneException {
    	if("ADMIN".equals(user.getPosition().getCode())){
            return this.getList(condition);
        }
        StringBuilder hql = new StringBuilder("select distinct pc from LovMember pc ," + entityName + " pr where pr.productCatalogId = pc.id and pc.deleteFlag = 'N'");
        List<Object> args = new ArrayList<>();
        this.addQueryCondition(condition, args, hql, "groupId", "pc.groupId", "=");
        this.addQueryCondition(condition, args, hql, "groupCode", "pc.groupCode", "=");
        this.addQueryCondition(condition, args, hql, "leafFlag", "pc.leafFlag", "=");

        String parentId = condition.getStringCondition("id");
        if (parentId == null) {
            parentId = "ROOT";
        }
        hql.append(" and pc.parentId = ?");
        args.add(parentId);

        hql.append(" and (pr.orgId = ? or pr.orgId = ?)  ");
        args.add(user.getPosition().getPositionInOrgId());
        args.add(user.getOrg().getId());

        hql.append(" order by pc.no asc ");
        return baseDao.findEntity(hql.toString(), args.toArray());
    }

    public List<LovMember> getList30(Condition condition) throws AnneException {
        StringBuilder hql = new StringBuilder("from LovMember where deleteFlag = 'N'");
        List<Object> args = new ArrayList<>();
        this.addQueryCondition(condition, args, hql, "groupCode", "groupCode", "=");
        String search = condition.getStringCondition("search");
        if (StringUtil.isNotEmpty(search)) {
            hql.append(" and name like ? ");
            args.add("%" + search + "%");
        }
        hql.append(" and rownum < 30");
        hql.append(" order by no asc");
        return baseDao.findEntity(hql.toString(), args.toArray());
    }

    public List<LovMember> getList2(Condition condition, UserObject user) throws AnneException {
        StringBuilder hql = new StringBuilder("select distinct pc from LovMember pc ,PermissionRel pr where pr.productCatalogId = pc.id and pc.deleteFlag = 'N'");
        List<Object> args = new ArrayList<>();
        this.addQueryCondition(condition, args, hql, "groupId", "pc.groupId", "=");
        this.addQueryCondition(condition, args, hql, "groupCode", "pc.groupCode", "=");
        this.addQueryCondition(condition, args, hql, "leafFlag", "pc.leafFlag", "=");
        this.addQueryCondition(condition, args, hql, "optTxt2", "pc.optTxt2", "=");

        if(!"ADMIN".equals(user.getPosition().getCode())){
            hql.append(" and (pr.orgId = ? or pr.orgId = ?)  ");
            args.add(user.getPosition().getPositionInOrgId());
            args.add(user.getOrg().getId());
        }
        hql.append(" order by pc.no asc ");
        return baseDao.findEntity(hql.toString(), args.toArray());
    }


    @Override
    public synchronized void move(String dragNodeId, String newParentNodeId) throws AnneException {
        LovMember lovMember = get(dragNodeId);
        LovMember newParentMember = get(newParentNodeId);

        if (newParentMember != null && "Y".equals(newParentMember.getLeafFlag())) {
            throw new AnneException("叶子节点不能有子节点");
        }

        if (newParentMember != null && newParentMember.getPath().indexOf(lovMember.getId()) > -1) {
            throw new AnneException("父节点的新父节点不能是自己的子孙节点");
        }
        if (newParentMember == null) {
            lovMember.setParentId("ROOT");
            lovMember.setLevel(1);
            lovMember.setNamePath("/" + lovMember.getName());
            lovMember.setCodePath("/" + lovMember.getCode());
            lovMember.setPath("/" + lovMember.getId());
        } else {
            lovMember.setParentId(newParentMember.getId());
            lovMember.setLevel(newParentMember.getLevel() + 1);
            lovMember.setNamePath(newParentMember.getNamePath() + "/" + lovMember.getName());
            lovMember.setCodePath(newParentMember.getCodePath() + "/" + lovMember.getCode());
            lovMember.setPath(newParentMember.getPath() + "/" + lovMember.getId());
        }
        //		baseDao.executeSQL(" update SYS_T_LOV_MEMBER set LOV_LEVEL=LOV_LEVEL + "+(newLevel - level)+", LOV_PATH = replace(LOV_PATH,'"+path+"','"+newPath+"') , NAME_PATH = replace(NAME_PATH,'"+name+"','"+newName+"'), CODE_PATH = replace(CODE_PATH,'"+code+"','"+newCode+"') where LOV_PATH like ? " , new Object[]{path});

        List<LovMember> list = baseDao.findEntity(" from LovMember l where l.parentId = ? ", new Object[]{lovMember.getId()});
        for (LovMember c : list) {
            move(c.getId(), lovMember.getId());
        }

        baseDao.update(lovMember);

        //		int idPathLenght = parentMember == null? 1 : parentMember.getPath().length() + 1 ;
        //		int namePathLenght = parentMember == null? 1 : parentMember.getNamePath().length() + 1;
        //		int codePathLenght = parentMember == null? 1 : parentMember.getCodePath().length() + 1;
        //		String newParentPath = newParentMember == null ? "" : newParentMember.getPath();
        //		String newParentNamePath = newParentMember == null ? "" : newParentMember.getNamePath();
        //		String newParentCodePath = newParentMember == null ? "" : newParentMember.getCodePath();
        //		int op = parentMember == null? 0: parentMember.getLevel();
        //		int np = newParentMember == null? 0: newParentMember.getLevel();
        //		int level = op - np;
        //		List<Object> args = new ArrayList<Object>();
        //		args.add(newParentPath);
        //		args.add(newParentNamePath);
        //		args.add(newParentCodePath);
        //		args.add("%"+lovMember.getId()+"%");
        //		baseDao.executeSQL(" update SYS_T_LOV_MEMBER set LOV_LEVEL=LOV_LEVEL - "+level+", LOV_PATH = ?||substr(LOV_PATH,"+idPathLenght+") , NAME_PATH = ?||substr(NAME_PATH,"+namePathLenght+") , CODE_PATH = ?||substr(CODE_PATH,"+codePathLenght+") where LOV_PATH like ? " , args.toArray());
        //		baseDao.executeSQL(" update SYS_T_LOV_MEMBER set PARENT_ID = ? where ROW_ID = ? ",new Object[]{newParentMember==null?"ROOT":newParentMember.getId(),dragNodeId});
        //mysql 实现
        //baseDao.executeSQL(" update SYS_T_LOV_MEMBER set LOV_LEVEL=LOV_LEVEL - "+level+", LOV_PATH =  CONCAT('"+newParentPath+"',SUBSTRING(LOV_PATH,"+idPathLenght+")) , NAME_PATH = CONCAT('"+newParentNamePath+"',SUBSTRING(NAME_PATH,"+namePathLenght+")) , CODE_PATH = CONCAT('"+newParentCodePath+"',SUBSTRING(CODE_PATH,"+codePathLenght+")) where LOV_PATH like '"+"%"+lovMember.getId()+"%"+"' " );
    }

    //	@Override
    //	public LovMember getByCode(String lovMemberCode) throws AnneException {
    //		return baseDao.findUniqueEntity(" from LovMember m where m.code = ? " , lovMemberCode);
    //	}

    @Override
    public LovMember getLovMemberByCode(String groupCode, String code) throws AnneException {
        return baseDao.findUniqueEntity(" from LovMember m where m.groupCode = ? and m.code = ? ", new Object[]{groupCode, code});
    }
    
    @Override
    public LovMember getLovMemberByName(String groupCode, String name) throws AnneException {
        return baseDao.findUniqueEntity(" from LovMember m where m.groupCode = ? and m.name = ? ", new Object[]{groupCode, name});
    }

    /**
     * 根据员工岗位ID查询单个员工信息
     */
    @Override
    public LovMember getLovMemberByPositionId(String groupCode, String posId) throws AnneException {
        return baseDao.findUniqueEntity(" from LovMember m where m.groupCode = ? and m.id = ? ", new Object[]{groupCode, posId});
    }

    ;

    /**
     * 获取员工的角色列表
     *
     * @param userId
     * @return
     */
    @Override
    public List<LovMember> getRulesByUserId(String userId) {
        List<Object> args = new ArrayList<Object>();
        StringBuffer hql = new StringBuffer();
        hql.append("select r from Employee e,UserPermission ur,LovMember r where e.id = ur.userId and ur.type = 'R' and ur.memberId=r.id and ur.userId = ?  ");
        args.add(userId);
        return baseDao.findEntity(hql.toString(), args.toArray());
    }

    @Override
    public List<LovMember> selectLpcLinePosition(Condition condition) throws AnneException {
        String orgId = condition.getStringCondition("orgId");
        String hql = "select m from LovMember m,LovMember l where m.optTxt1=l.id and not exists (from PriceLayCompareLine c where c.roleId = m.id) and m.groupCode ='POSITION' and l.groupCode='ORG' and l.parentId = ?";
        List<LovMember> lst = baseDao.findEntity(hql, orgId);
        return lst;
    }

    @Override
    public List<LovMember> selectLpcLineName(Condition condition) throws AnneException {
        String orgId = condition.getStringCondition("orgId");
        String hql = "select m from LovMember m where not exists (select c.id from PriceLayCompareLine c,PriceLayCompareHeader h where c.headerId = h.id and c.layLineName = m.id and h.organization = ?) and m.groupCode ='LAYER_COMP_NAME' order by m.code";
        List<LovMember> lst = baseDao.findEntity(hql, orgId);
        return lst;
    }

    @Override
    public List<LovMember> selectLpcOrg(Condition condition) throws AnneException {
        String search = condition.getStringCondition("search");
        List<Object> args = new ArrayList<Object>();
        StringBuffer hql = new StringBuffer("select m from LovMember m where not exists (from PriceLayCompareHeader h where h.organization = m.id) and m.groupCode ='ORG' and m.leafFlag = 'N'");
        if (StringUtil.isNotEmpty(search)) {
            hql.append(" and (m.name like ? or m.code like ?)");
            args.add("%" + search.trim() + "%");
            args.add("%" + search.trim() + "%");
        }
        hql.append(" order by m.code");
        List<LovMember> lst = baseDao.findEntity(hql.toString(), args.toArray());
        return lst;
    }


    @Override
    public void importData(List<List<Object>> list) throws AnneException {

    }


    @Override
    public String getFlowCodeByAppCode(String appCode) throws AnneException {
        LovMember lovMember = baseDao.findUniqueEntity("from LovMember m where m.groupCode = 'FLOW_APPLICATION' and m.code = ?", appCode);
        if (lovMember != null) {
            return lovMember.getOptTxt1();
        }
        return null;
    }

    public String getAppIdByFlowCode(String flowCode) throws AnneException {
        LovMember lovMember = baseDao.findUniqueEntity("from LovMember m where m.groupCode = 'FLOW_APPLICATION' and m.optTxt1 = ?", flowCode);
        if (lovMember != null) {
            return lovMember.getId();
        }
        return null;
    }


    @Override
    public List<LovMember> getSkipList(Condition condition) {

        StringBuilder sql = new StringBuilder();

        sql.append(" select r.row_id as id, r.lov_code as code, r.lov_name as name			");
        sql.append(" from SYS_T_LOV_MEMBER r												");
        sql.append(" where r.group_code = ?													");
        sql.append(" and r.lov_level = ?													");
        sql.append(" start with r.row_id = ? 												");
        sql.append(" connect by prior r.row_id = r.parent_id								");

        String groupCode = String.valueOf(condition.getConditionMap().get("code"));
        String parentId = String.valueOf(condition.getConditionMap().get("parentId"));
        String level = String.valueOf(condition.getConditionMap().get("level"));

        List<java.lang.Object[]> list = baseDao.findBySql(sql.toString(), new java.lang.Object[]{groupCode, level, parentId});

        List<LovMember> listMem = new ArrayList<>();

        for (java.lang.Object[] obj : list) {
            LovMember lm = new LovMember();
            lm.setId((String) obj[0]);
            lm.setCode((String) obj[1]);
            lm.setName((String) obj[2]);
            listMem.add(lm);
        }

        return listMem;
    }

    public String getSaleCenter(String currentDepId) {
        StringBuilder sql = new StringBuilder();
        sql.append("  select s.lov_code as code  										");
        sql.append("  from sys_t_lov_member s 											");
        sql.append("  where s.group_code = 'ORG' 										");
        sql.append("  and s.opt_txt3 = 'A' 												");
        sql.append("  start with s.row_id = ? 											");
        sql.append("  connect by prior s.parent_id = s.row_id                   		");

        List<Object[]> object = baseDao.findBySql(sql.toString(), new java.lang.Object[]{currentDepId});

        if (object.size() > 0) {
            return String.valueOf(object.get(0));
        }

        return "";
    }

    /**
     * getOrderSalesmanCenter:获取当前销售中心. <br/>
     *
     * @param currentDepId
     * @return
     * @author liming
     * @since JDK 1.7
     */
    @Override
    public LovMember getSaleCenterLovmember(String currentDepId) {
        String saleCenter = this.getSaleCenter(currentDepId);
        if (StringUtils.isEmpty(saleCenter)) {
            return null;
        }
        return getLovMemberByCode("ORG", saleCenter);
    }

    public Object[] getSaleMethod(String department) {
        StringBuilder sql = new StringBuilder();

        sql.append(" select s.lov_code,s.opt_txt2,s.row_id from sys_t_lov_member s	");
        sql.append(" where s.group_code = 'SALES_METHODS'					");
        sql.append(" and s.parent_id = 'ROOT'								");
        sql.append(" and s.opt_txt1 = ?										");

        String dept = getSaleCenter(department);

        return baseDao.findUniqueBySql(sql.toString(), new Object[]{dept});
    }

    @Override
    public List<LovMember> getDept(String optTxt3, String type, String search) {
        String hql = "";
        List<Object> args = new ArrayList<>();

        if (type != null) {
            if (type.equals("neq")) {
                hql = "from LovMember where groupCode = 'ORG' and optTxt3 <> ? ";
            } else if (type.equals("eq")) {
                hql = "from LovMember where groupCode = 'ORG' and optTxt3 = ? ";
            }
            args.add(optTxt3);
            if (search != null) {
                hql = hql + " and name like ?";
                args.add("%" + search + "%");
            }
        }
        return baseDao.findEntity(hql, args.toArray());

    }


    @Override
    public List<LovMember> getListByGroupCode(String groupCode) {
        return baseDao.findEntity("from LovMember where groupCode = ?", groupCode);
    }


    @Override
    public void saveList(List<LovMember> lovs) {
        for (LovMember lov : lovs) {
            LovGroup lovGroup = lovGroupService.get(lov.getGroupId());
            if (lovGroup == null) {
                throw new AnneException("不存在的LOV：" + lov.getGroupId());
            }
            lov.setGroupCode(lovGroup.getCode());
            baseDao.save(lov);
            updateTreeInfo(lov);
        }

    }

    @Override
    public List<LovMember> getSaleCenters() {
        String hql = "from LovMember where groupCode = 'ORG' and leafFlag = 'N' and optTxt3 = 'A' and optTxt5 is not null";
        return baseDao.findEntity(hql);
    }
    
    @Override
    public List<LovMember> getBizDeptList() {
        String hql = "from LovMember where groupCode = 'ORG' and leafFlag = 'N' and optTxt3 != 'A'";
        return baseDao.findEntity(hql);
    }


    @Override
    public boolean switchIsOpen(String groupCode, String lovCode) {
        LovMember lovMember = this.getLovMemberByCode(groupCode, lovCode);
        if (lovMember != null && IConstants.JAVA_CODE_SWITCH_OPEN.equals(lovMember.getOptTxt1())) {
            return true;
        }
        return false;
    }


    /**
     * 判断客户是否是代理商
     * 判断组织的归属部门是否为空,不为空则是代理商。
     *
     * @param id 组织Id
     * @return
     */
    @Override
    public boolean isAgentForOrg(String orgId) {
        LovMember lovMember = this.get(orgId);
        String optTxt4 = lovMember.getOptTxt4();
        if (StringUtils.isEmpty(optTxt4)) {
            return false;
        }
        LovMember lovMember1 = get(optTxt4);
        if (lovMember1 == null) {
            return false;
        }
        return true;
    }

    private List<LovMember> getByProductModels(String productModel) {
        List<LovMember> loves = this.baseDao.findEntity("from LovMember where deleteFlag='N' and groupCode='PRODUCTMODE' and name=? ", new Object[]{productModel});
        return loves;
    }

    @Override
    public LovMember getByProductModel(String productModel) {
        List<LovMember> productModels = this.getByProductModels(productModel);
        if (productModels.size() == 0) {
            return null;
        } else if (productModels.size() == 1) {
            return productModels.get(0);
        } else {
            LovMember product = null;
            for (LovMember lov : productModels) {
                if (Objects.equals(lov.getOptTxt2(), "1")) {
                    product = lov;
                }
            }
            if (product == null) {
                product = productModels.get(0);
            }
            for (LovMember model : productModels) {
                if (model != product) {
                    this.delete(model.getId());
                }
            }
            return product;
        }
    }

    @Override
    public List<List<Object>> getProductModelImportTemplet() {
        List<List<Object>> lstOut = new ArrayList<>();
        List<Object> lstHead = new ArrayList<>();
        lstHead.add("编码");
        lstHead.add("名称");
        lstHead.add("是否报备(是|否)");
        lstOut.add(lstHead);

        List<Object> lstIn = new ArrayList<>();
        lstIn.add("xxx");
        lstIn.add("A16");
        lstIn.add("是");
        lstOut.add(lstIn);
        return lstOut;
    }

    @Override
    public void importProductModels(List<List<Object>> dataList, UserObject userObject) {
        LovGroup group = lovGroupService.getByCode("PRODUCTMODE");
        for (List<Object> objects : dataList) {
            String productName = (String) objects.get(1);
            if (StringUtils.isEmpty(productName)) {
                continue;
            }

            LovMember lovMember = this.getByProductModel(productName);
            if (lovMember == null) {
                lovMember = new LovMember();
                lovMember.setCode((String) objects.get(0));
                lovMember.setName(productName);
                lovMember.setGroupId(group.getId());
                lovMember.setGroupCode(group.getCode());
            }
            String optTxt2 = Objects.equals(objects.get(2), "是") ? "1" : "0";
            lovMember.setOptTxt2(optTxt2);
            if (StringUtils.isEmpty(lovMember.getId())) {
                this.save(lovMember);
            } else {
                this.update(lovMember);
            }
        }
    }

    @Override
    public List<List<Object>> getExcelData(List<LovMember> list) {
        List<List<Object>> lstOut = new ArrayList<List<Object>>();
        List<Object> lstHead = new ArrayList<Object>();
        lstHead.add("编码");
        lstHead.add("名称");
        lstHead.add("是否报备");
        lstHead.add("备注");
        lstHead.add("序号");
        lstOut.add(lstHead);
        for (LovMember lovMember : list) {
            List<Object> lstIn = new ArrayList<>();
            lstIn.add(lovMember.getCode());
            lstIn.add(lovMember.getName());
            lstIn.add(Objects.equals(lovMember.getOptTxt2(), IConstants.YES) ? "是" : "否");
            lstIn.add(lovMember.getMemo());
            lstIn.add(lovMember.getNo());
            lstOut.add(lstIn);
        }
        return lstOut;
    }


    @Override
    public List<Map<String, Object>> getSelectPriceOrgList(PageCondition condition, UserObject user)
            throws AnneException {
        String level2Sql = "select row_id,lov_code from SYS_T_LOV_MEMBER where lov_level = 2 start with row_id = ? connect by prior PARENT_ID = ROW_ID";
        List<Object[]> lst = baseDao.findBySql(level2Sql, user.getOrg().getId());
        Set<String> orgs = new HashSet<>();
        orgs.add("P_GNQCORG_B1_0001");    //新能源汽车行业部
        orgs.add("P_GNORG_B1_0001");    //国内数据中心
        orgs.add("P_GJORG_B1_0001");    //国际营销中心
        orgs.add("P_GNGFORG_B1_0001");    //新能源国内营销
        if (lst != null && lst.size() > 0) {
            Object[] objects = lst.get(0);
            if (orgs.contains(objects[1].toString())) {
                StringBuilder sb = new StringBuilder();
                sb.append("select ROW_ID \"id\",LOV_NAME \"name\",PARENT_ID \"parentId\",LEAF_FLAG \"leafFlag\",LOV_LEVEL \"level\",OPT_TXT3 \"optTxt3\" from ");
                sb.append("(select * from SYS_T_LOV_MEMBER where row_id = (select parent_id from SYS_T_LOV_MEMBER where lov_code = ?)");
                sb.append(" union select * from SYS_T_LOV_MEMBER start with lov_code = ? connect by prior ROW_ID = PARENT_ID) where LEAF_FLAG != 'Y'");
                List<Object> args = new ArrayList<>();
                args.add(objects[1].toString());
                args.add(objects[1].toString());
                return baseDao.findBySql4Map(sb.toString(), args.toArray());
            }
        }
        return null;
    }


	@Override
	public List<LovMember> getAllCatalogList() {
		String hql = "select lov from LovMember lov where deleteFlag='N' and groupId='procatalog' and leafFlag='N' ";
		List<LovMember> lovCatalogList = baseDao.findEntity(hql);
		return lovCatalogList;
	}
	
	@Override
	public List<LovMember> getAllOrgList() {
		String hql = "select lov from LovMember lov where deleteFlag='N' and groupId='ORG' and leafFlag='N' ";
		List<LovMember> lovCatalogList = baseDao.findEntity(hql);
		return lovCatalogList;
	}
	
	@Override
	public List<LovMember> getOrgAllList() {
		String hql = "select lov from LovMember lov where  groupId='ORG' ";
		List<LovMember> lovCatalogList = baseDao.findEntity(hql);
		return lovCatalogList;
	}
	
	@Override
	public List<LovMember> getAllTreeByOrgList(List<LovMember> orgList) {
		List<LovMember> result = new ArrayList<>();
		List<String> positionIds = new ArrayList<>();
		StringBuffer positionIdsbf = new StringBuffer();
		int count = 0;
		for(LovMember orgLov:orgList) {
			LovMember parent = this.get(orgLov.getId());
			String positionId = orgLov.getId();
			if(count<1000) {
				if(parent != null && "Y".equals(parent.getLeafFlag())) {
					positionIdsbf.append("'"+positionId+"',");
					count++; 
				}
			}else {
				if(positionIdsbf.toString().length()>1) {
					positionIds.add(positionIdsbf.toString().substring(0,positionIdsbf.toString().length()-1));
					count = 0;
					positionIdsbf = positionIdsbf.delete(0, positionIdsbf.length());
				}
			}
		}
		if(positionIdsbf.toString().length()>1) {
			positionIds.add(positionIdsbf.toString().substring(0,positionIdsbf.toString().length()-1));
		}
		for(String positionId:positionIds) {
			List<Object[]> list = baseDao.findBySql(" select e.ROW_ID,e.NO,e.NAME,o.ROW_ID as pid from SYS_T_PERMISSION_EMPLOYEE e,SYS_T_LOV_MEMBER p,SYS_T_LOV_MEMBER o ,SYS_T_PERMISSION_USER_REL up where e.ROW_ID = up.USER_ID and p.OPT_TXT1 = o.ROW_ID and p.ROW_ID = up.MEMBER_ID and up.TYPE = 'P' and o.ROW_ID in ( "+positionId+" ) and p.GROUP_ID = 'POSITION' and o.GROUP_ID = 'ORG' ");
			for (Object[] object : list) {
				LovMember lov = new LovMember();
				lov.setId((String)object[0]);
				lov.setCode((String)object[1]);
				lov.setName((String)object[2]);
				lov.setParentId((String)object[3]);
				lov.setLeafFlag("Y");
				lov.setOptTxt1("E");
				result.add(lov);
			}
		}
		
		orgList.addAll(result);
		return orgList;
	}
}
