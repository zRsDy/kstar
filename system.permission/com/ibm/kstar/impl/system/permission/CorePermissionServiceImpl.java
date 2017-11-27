package com.ibm.kstar.impl.system.permission;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.utils.MD5Util;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.EmployeeObject;
import com.ibm.kstar.api.system.permission.ICorePermissionService;
import com.ibm.kstar.api.system.permission.IMenu;
import com.ibm.kstar.api.system.permission.IPermission;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.conf.AdminConfiguration;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class CorePermissionServiceImpl implements ICorePermissionService {

    final static String USER = "LOGIN_USER";

    final static String USER_MAIL = "@kstar.com.cn";

    final static String APPID = "CRM";

    @Autowired
    AdminConfiguration adminConfiguration;

    List<IMenu> menuList;

    @Autowired
    BaseDao baseDao;

//	@Override
//	public UserObject getUserObject(HttpServletRequest request) {
//		HttpSession session = request.getSession();
//		return ((UserObject)session.getAttribute(USER));
//	}
//	
//	@Override
//	public void putUserObject(HttpServletRequest rquest, UserObject t) {
//		HttpSession session = rquest.getSession();
//		session.setAttribute(USER, t);
//	}

    public LovMember getPositionById(String positionId) {
        return baseDao.findUniqueEntity(" select p from LovMember p where p.groupId = 'POSITION' and p.id = ?", new Object[]{positionId});
    }

    public LovMember getPositionByOrgId(String orgId) {
        return baseDao.findUniqueEntity(" select p from LovMember o ,LovMember p where o.id = p.optTxt1 and o.groupId = 'ORG' and p.groupId = 'POSITION' and o.id = ?", new Object[]{orgId});
    }

    public LovMember getOrgByPositionId(String positionId) {
        return baseDao.findUniqueEntity(" select o from LovMember o ,LovMember p where o.id = p.optTxt1 and o.groupId = 'ORG' and p.groupId = 'POSITION' and p.id = ? ", new Object[]{positionId});
    }

    public LovMember getOrgByApprovePositionId(String approvePositionId) {
        return baseDao.findUniqueEntity(" select o from LovMember o ,LovMember p ,LovMember a where o.id = p.optTxt1 and o.groupId = 'ORG' and p.groupId = 'POSITION' and a.groupId='APPROVE_POSITION_LEVEL' and a.id=p.optTxt2 and a.id = ? ", new Object[]{approvePositionId});
    }

    /**
     * 是否是商务专员
     * @param employeeId
     * @return
     */
    @Override
    public boolean isTradeCommisioner(String employeeId){

        List<Employee> e = this.baseDao.findEntity("SELECT e " +
                "FROM Employee e, UserPermission ur, LovMember r " +
                "WHERE e.id = ur.userId AND ur.type = 'R' AND r.deleteFlag = 'N' AND r.leafFlag = 'Y' AND r.groupId = 'ROLE' AND " +
                "      r.name LIKE '%商务%' AND ur.memberId = r.id and e.id=?)", new Object[]{employeeId});
        if (e.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取商务专员
     * @param orgId
     * @param layerId
     * @param search
     * @return
     */
    @Override
    public List<EmployeeObject> findTradeCommisioner(String orgId, String layerId, String search){
        // 黄奇2017-08-06修复Bug:目前合同申请上面的下单商务专员字段没有做管控，所有系统用户都可选到。请在此字段上加上逻辑，只能选择到商务部人员。
        String hql = " select new com.ibm.kstar.api.system.permission.EmployeeObject(org,p,e) " +
                "from LovMember o , LovMember org , LovMember p , Employee e ,UserPermission up " +
                " where o.groupId = 'ORG' and p.groupId = 'POSITION' and p.optTxt1 = o.id and up.userId = e.id and up.memberId = p.id and o.parentId = org.id "
                + " and ( org.name like ? or p.name like ? or e.name like ? or e.no like ? ) and e.endDate > ? and e.startDate < ?  and e.no in (" +
                "SELECT e.no " +
                "FROM Employee e, UserPermission ur, LovMember p,RolePosition rp, LovMember r " +
                "WHERE e.id = ur.userId AND ur.type = 'P' AND ur.memberId = p.id and p.optTxt1 = rp.positionId and rp.roleId=r.id AND r.deleteFlag = 'N' AND r.leafFlag = 'Y' AND r.groupId = 'ROLE' AND " +
                "      r.name LIKE '%商务%' )";
        List<Object> args = new ArrayList<Object>();
        args.add("%" + search + "%");
        args.add("%" + search + "%");
        args.add("%" + search + "%");
        args.add("%" + search + "%");
        args.add(new Date());
        args.add(new Date());

        if (StringUtil.isNotEmpty(orgId)) {
            hql = hql + " and org.path like ? ";
            args.add("%" + orgId + "%");
        }

        if (StringUtil.isNotEmpty(layerId)) {
            hql = hql + " and not exists (select m.id from PriceLayCompareLine c,Team m where c.id = m.businessId and c.headerId = ? and m.positionId = p.id) ";
            args.add(layerId);
            hql = hql + " and not exists (select p1.id from LovMember p1,LovMember a,PriceLayCompareLine c where p1.optTxt2 = a.id"
                    + " and a.optTxt5 = c.approveLayLine and p1.groupCode = 'POSITION' and c.headerId = ? and p1.id = p.id) ";
            args.add(layerId);
        }

        List<EmployeeObject> list = baseDao.findEntity(hql, args.toArray(), 0, 20);
        return list;
    }

    public List<EmployeeObject> findEmployeeObject(String orgId, String layerId, String search) {
        List<Object> args = new ArrayList<Object>();

 		String hql = " select new com.ibm.kstar.api.system.permission.EmployeeObject(org,p,e) from LovMember o , LovMember org , LovMember p , Employee e , "
				+ "UserPermission up where o.groupId = 'ORG' and p.groupId = 'POSITION' and p.optTxt1 = o.id and up.userId = e.id and up.memberId = p.id and o.parentId = org.id "
				+ " and ( org.name like ? or p.name like ? or e.name like ? or e.no like ? ) and e.endDate > ? and e.startDate < ? ";

        args.add("%" + search + "%");
        args.add("%" + search + "%");
        args.add("%" + search + "%");
        args.add("%" + search + "%");
        args.add(new Date());
        args.add(new Date());

        if (StringUtil.isNotEmpty(orgId)) {
            hql = hql + " and org.path like ? ";
            args.add("%" + orgId + "%");
        }

        if (StringUtil.isNotEmpty(layerId)) {
            hql = hql + " and not exists (select m.id from PriceLayCompareLine c,Team m where c.id = m.businessId and c.headerId = ? and m.positionId = p.id) ";
            args.add(layerId);
            hql = hql + " and not exists (select p1.id from LovMember p1,LovMember a,PriceLayCompareLine c where p1.optTxt2 = a.id"
                    + " and a.optTxt5 = c.approveLayLine and p1.groupCode = 'POSITION' and c.headerId = ? and p1.id = p.id) ";
            args.add(layerId);
        }

        List<EmployeeObject> list = baseDao.findEntity(hql, args.toArray(), 0, 20);
        return list;
    }
    
    
    public List<EmployeeObject> findEmployeeByPosObject(String posId, String layerId, String search) {
        List<Object> args = new ArrayList<Object>();

 		String hql = " select new com.ibm.kstar.api.system.permission.EmployeeObject(org,p,e) from LovMember o , LovMember org , LovMember p , Employee e , "
				+ "UserPermission up where o.groupId = 'ORG' and p.groupId = 'POSITION' and p.optTxt1 = o.id and up.userId = e.id and up.memberId = p.id and o.parentId = org.id "
				+ " and ( org.name like ? or p.name like ? or e.name like ? or e.no like ? ) and e.endDate > ? and e.startDate < ? ";

        args.add("%" + search + "%");
        args.add("%" + search + "%");
        args.add("%" + search + "%");
        args.add("%" + search + "%");
        args.add(new Date());
        args.add(new Date());

        if (StringUtil.isNotEmpty(posId)) {
            hql = hql + " and p.path like ? ";
            args.add("%" + posId + "%");
        }

        if (StringUtil.isNotEmpty(layerId)) {
            hql = hql + " and not exists (select m.id from PriceLayCompareLine c,Team m where c.id = m.businessId and c.headerId = ? and m.positionId = p.id) ";
            args.add(layerId);
            hql = hql + " and not exists (select p1.id from LovMember p1,LovMember a,PriceLayCompareLine c where p1.optTxt2 = a.id"
                    + " and a.optTxt5 = c.approveLayLine and p1.groupCode = 'POSITION' and c.headerId = ? and p1.id = p.id) ";
            args.add(layerId);
        }

        List<EmployeeObject> list = baseDao.findEntity(hql, args.toArray(), 0, 20);
        return list;
    }

    public List<EmployeeObject> findEmployeeList(String orgId, String layerId, String search) {
        List<Object> args = new ArrayList<Object>();

 		String hql = "  from  Employee e  "
				+ "  "
				+ " where e.name like ? or e.no like ?   ";
 		
        args.add("%" + search + "%");
        args.add("%" + search + "%");
       /* args.add(new Date());
        args.add(new Date());*/

        if (StringUtil.isNotEmpty(orgId)) {
            hql = hql + " and org.path like ? ";
            args.add("%" + orgId + "%");
        }

        if (StringUtil.isNotEmpty(layerId)) {
            hql = hql + " and not exists (select m.id from PriceLayCompareLine c,Team m where c.id = m.businessId and c.headerId = ? and m.positionId = p.id) ";
            args.add(layerId);
            hql = hql + " and not exists (select p1.id from LovMember p1,LovMember a,PriceLayCompareLine c where p1.optTxt2 = a.id"
                    + " and a.optTxt5 = c.approveLayLine and p1.groupCode = 'POSITION' and c.headerId = ? and p1.id = p.id) ";
            args.add(layerId);
        }

        List<EmployeeObject> list = baseDao.findEntity(hql, args.toArray(), 0, 20);
        return list;
    }
    
    public List<EmployeeObject> findOrgList(String orgId, String layerId, String search) {
        List<Object> args = new ArrayList<Object>();

 		String hql = "  from LovMember t  "
				+ " where GROUP_ID = 'ORG' and LEAF_FLAG = 'N' and OPT_TXT3 = 'E' "
				+ " and ( t.name like ? ) ";
 			
        args.add("%" + search + "%");
      /*args.add(new Date());
        args.add(new Date());*/
        if (StringUtil.isNotEmpty(orgId)) {
            hql = hql + " and t.path like ? ";
            args.add("%" + orgId + "%");
        }

        if (StringUtil.isNotEmpty(layerId)) {
            hql = hql + " and not exists (select m.id from PriceLayCompareLine c,Team m where c.id = m.businessId and c.headerId = ? and m.positionId = p.id) ";
            args.add(layerId);
            hql = hql + " and not exists (select p1.id from LovMember p1,LovMember a,PriceLayCompareLine c where p1.optTxt2 = a.id"
                    + " and a.optTxt5 = c.approveLayLine and p1.groupCode = 'POSITION' and c.headerId = ? and p1.id = p.id) ";
            args.add(layerId);
        }

        List<EmployeeObject> list = baseDao.findEntity(hql, args.toArray(), 0, 20);
        return list;
    }
    
    public EmployeeObject findEmployeeObjectById(String id) {
        List<Object> args = new ArrayList<Object>();
        String hql = " select new com.ibm.kstar.api.system.permission.EmployeeObject(org,p,e) from LovMember o , LovMember org , LovMember p , Employee e , "
                + "UserPermission up where o.groupId = 'ORG' and p.groupId = 'POSITION' and p.optTxt1 = o.id and up.userId = e.id and up.memberId = p.id and o.parentId = org.id "
                + " and e.id = ? ";

        args.add(id);

        EmployeeObject employeeObject = baseDao.findUniqueEntity(hql, args.toArray());
        return employeeObject;
    }

    @Override
    public List<IPermission> getPermissionList(String appId, String userId) {
        List<LovMember> list = getUserPermissionList(appId, userId);
        List<IPermission> result = new ArrayList<>();
        for (LovMember lovMember : list) {
            Permission p = new Permission(lovMember.getCode(), lovMember.getCodePath(), lovMember.getOptTxt1());
            result.add(p);
        }
        return result;
    }

    public List<IPermission> getPermissionListByPosition(String appId, String positionId) {
        List<LovMember> list = getUserPermissionListByPosition(appId, positionId);
        List<IPermission> result = new ArrayList<>();
        for (LovMember lovMember : list) {
            Permission p = new Permission(lovMember.getCode(), lovMember.getCodePath(), lovMember.getOptTxt1());
            result.add(p);
        }
        return result;
    }

    @Override
    public List<LovMember> getRolePermissionList(String appId, String roleId) {
        return baseDao.findEntity(" select p from RolePermission rp ,LovMember p where rp.roleId = ? and p.id = rp.permissionId and p.groupId = 'PERMISSION'  and p.path like ? ", new Object[]{roleId, "%" + appId + "%"});
    }

    public List<Employee> getRoleEmployeeList(String roleId) {
        return baseDao.findEntity(" select e from Employee e , UserPermission up where up.type= 'R' and up.memberId = ? and up.userId = e.id ", new Object[]{roleId});
    }

    //岗位权限
    public List<LovMember> getPositionListByRole(String roleId) {
        return baseDao.findEntity(" select e from LovMember e , RolePosition up where e.groupCode= 'ORG' and up.roleId = ? and up.positionId = e.id ", new Object[]{roleId});
    }

    //岗位权限
    public List<LovMember> getRoleListByPosition(String positionId) {
        return baseDao.findEntity(" select e from LovMember e , RolePosition up where e.groupCode= 'ROLE' and up.positionId = ? and up.roleId = e.id ", new Object[]{positionId});
    }

    public List<Employee> getPositionEmployeeList(String positionId) {
        return baseDao.findEntity(" select e from Employee e , UserPermission up where up.type= 'P' and up.memberId = ? and up.userId = e.id ", new Object[]{positionId});
    }

    private List<LovMember> getUserPermissionList(String appId, String userId) {
        return baseDao.findEntity(" select p from RolePermission rp ,LovMember p ,UserPermission up where up.userId = ? and up.type = 'R' and up.memberId = rp.roleId and p.id = rp.permissionId and p.groupId = 'PERMISSION' and p.path like ? ", new Object[]{userId, "%" + appId + "%"});
    }

    public List<LovMember> getUserPermissionListByPosition(String appId, String positionId) {
        return baseDao.findEntity(" select p from RolePermission rp ,LovMember p ,RolePosition up where up.positionId = ? and up.roleId = rp.roleId and p.id = rp.permissionId and p.groupId = 'PERMISSION' and p.path like ? ", new Object[]{positionId, "%" + appId + "%"});
    }

    @Override
    public List<LovMember> getAllPermissionList(String appId) {
        return baseDao.findEntity(" select p from LovMember p where p.groupId = 'PERMISSION' and p.path like ? ", "%" + appId + "%");
    }

    private String getMailInfo(String userCode) {
        String mail = "";
        if (userCode.indexOf("@") > 0) {
            mail = userCode;
        } else {
            mail = userCode.concat(USER_MAIL);
        }
        return mail;
    }

    @Override
    public UserObject login(String appId, String userCode, String password) {
        Employee employee = baseDao.findUniqueEntity(" select e from Employee e, User u where e.id = u.id and (e.no = ? or e.email = ?)  ", new Object[]{userCode, getMailInfo(userCode)});
        //Employee employee = baseDao.findUniqueEntity(" select e from Employee e, User u where e.id = u.id and (e.no = ? or e.email = ?) and u.password = ? ", new Object[]{userCode, getMailInfo(userCode), MD5Util.MD5Encode(password, "UTF-8")});

        if (employee == null) {
            return null;
        }
        UserObject uo = new UserObject(employee, new ArrayList<IPermission>());
//        if ("admin".equalsIgnoreCase(employee.getNo()) || adminConfiguration.isAdmin(employee.getNo())) {
//            uo = new UserObject(employee, null);
//        } else {
//            //弃用该方法，在登录和切换岗位时使用新方法getPermissionListByPosition
//            //uo = new UserObject(employee, getPermissionList(appId,employee.getId()));
//            uo = new UserObject(employee, new ArrayList<IPermission>());
//        }
        return uo;
    }

    @Override
    public List<IMenu> getSystemMenuList(String appId) {
        if (menuList != null) {
            return menuList;
        }
        List<LovMember> list = baseDao.findEntity(" from LovMember m where m.groupId ='NEWMENU' and m.codePath like ? order by no ", "%" + appId + "%");
        Map<String, Menu> allMap = new HashMap<String, Menu>();
        menuList = new ArrayList<IMenu>();
        for (LovMember lov : list) {
            Menu m = new Menu(lov);
            allMap.put(lov.getId(), m);
            if (lov.getLevel() == 2) {
                menuList.add(m);
            }
        }
        for (LovMember lov : list) {
            Menu parent = allMap.get(lov.getParentId());
            Menu current = allMap.get(lov.getId());
            if (parent != null) {
                parent.getChildMenuList().add(current);
            }
        }
        return menuList;
    }

    @Override
    public List<LovMember> getUserPositionList(String userId) {
        return baseDao.findEntity(" select p from LovMember p ,UserPermission up where up.userId = ? and up.type = 'P' and p.groupId = 'POSITION' and p.id = up.memberId ", new Object[]{userId});
    }

    public LovMember getOrg(String positionId) {
        LovMember position = baseDao.get(LovMember.class, positionId);
        LovMember op = baseDao.get(LovMember.class, position.getOptTxt1());
        LovMember org = baseDao.get(LovMember.class, op.getParentId());
        return org;
    }

    public List<LovMember> getUserList(String positionId) {
        List<Employee> list = baseDao.findEntity(" select e from Employee e,LovMember p,LovMember o ,UserPermission up where e.id = up.userId and p.optTxt1 = o.id and p.id = up.memberId and up.type = 'P' and o.id = ? and p.groupId = 'POSITION' and o.groupId = 'ORG' ", new Object[]{positionId});
        List<LovMember> result = new ArrayList<>();
        for (Employee e : list) {
            LovMember lov = new LovMember();
            lov.setId(e.getId());
            lov.setCode(e.getNo());
            lov.setName(e.getName());
            lov.setParentId(positionId);
            lov.setLeafFlag("Y");
            lov.setOptTxt1("E");
            result.add(lov);
        }
        return result;
    }

    @Override
    public LovMember getApprovePositionByPositionId(String positionId) {
        return baseDao.findUniqueEntity(" select o from LovMember o ,LovMember p where o.id = p.optTxt2 and o.groupId = 'APPROVE_POSITION_LEVEL' and p.groupId = 'POSITION' and p.id = ? ", new Object[]{positionId});
    }

    @Override
    public LovMember getPositionByApprovePosition(String approvePositionId) {
        return baseDao.findUniqueEntity(" select p from LovMember o ,LovMember p where o.id = p.optTxt2 and o.groupId = 'APPROVE_POSITION_LEVEL' and p.groupId = 'POSITION' and o.id = ? ", new Object[]{approvePositionId});
    }

    @Override
    public List<Map<String, Object>> downLoadDataBySQL(String exportSql) {
        return baseDao.findBySql4Map(exportSql, null);
    }
}
