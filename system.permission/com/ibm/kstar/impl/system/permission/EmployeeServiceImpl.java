package com.ibm.kstar.impl.system.permission;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import com.ibm.kstar.action.system.permission.employee.EmployeeVo;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.IEmployeeService;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.api.system.permission.entity.User;
import com.ibm.kstar.api.system.permission.entity.UserPermission;
import com.ibm.kstar.entity.team.vo.TeamVo;
@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class EmployeeServiceImpl implements IEmployeeService{

	@Autowired
	BaseDao baseDao;
	
	@Override
	public void save(Employee employee) throws AnneException {
		checkEmployeeNo(employee);
		employee.setStartDate(new Date());
		Employee e = new Employee();
		BeanUtils.copyPropertiesIgnoreNull(employee, e);
		e.setUserFlag("N");
		baseDao.save(e);
		EmployeeVo employeeVo = ((EmployeeVo)employee);
		String[] positions = employeeVo.getPositions().split(",");
		
		String flag = null;
		
		for(String position : positions){
			LovMember positionLov = baseDao.get(LovMember.class, position);
			//如果外部组织，只可以选择一个岗位
//			if("E".equals(positionLov.getOptTxt3()) && positions.length>1){
//				throw new AnneException("代理商的岗位职能选择一个");
//			}
			if(flag != null && !flag.equals(positionLov.getOptTxt3()) && "E".equals(flag)){
				throw new AnneException("内部用户只能拥有内部岗位，外部用户只能拥有外部岗位，不能混合！");
			}
			flag = positionLov.getOptTxt3();
			
			if("E".equals(positionLov.getOptTxt3())){
				e.setFlag("E");
				baseDao.update(e);
			}else {
				e.setFlag("A");
				baseDao.update(e);
			}
			LovMember org = baseDao.findUniqueEntity(" from LovMember p where p.groupId = 'POSITION' and p.optTxt1 = ? ",position);
			if(org !=null){
				UserPermission up = new UserPermission();
				up.setMemberId(org.getId());
				up.setUserId(e.getId());
				up.setType("P");
				baseDao.save(up);
			}
		}
	}

	private void checkEmployeeNo(Employee employee) {
		Employee checkEmployee = null;
		if(employee.getId() == null){
			checkEmployee = baseDao.findUniqueEntity( " from Employee e where e.no = ? ",employee.getNo());
		}else{
			checkEmployee = baseDao.findUniqueEntity( " from Employee e where e.no = ? and e.id != ? ",new Object[]{employee.getNo(),employee.getId()});
		}
		if(checkEmployee !=null){
			throw new AnneException("员工号已经存在");
		}
	}

	@Override
	public void update(Employee employee) throws AnneException {
		checkEmployeeNo(employee);
		Employee oldEmployee = baseDao.get(Employee.class,employee.getId());
		BeanUtils.copyPropertiesIgnoreNull(employee, oldEmployee);
		baseDao.update(oldEmployee);
		EmployeeVo employeeVo = ((EmployeeVo)employee);
		baseDao.executeHQL(" delete from UserPermission up where up.userId = ? and up.type = 'P' ",new Object[]{employee.getId()});
		String[] positions = employeeVo.getPositions().split(",");
		String flag= null;
		for(String position : positions){
			LovMember positionLov = baseDao.get(LovMember.class, position);
			//如果外部组织，只可以选择一个岗位
//			if("E".equals(positionLov.getOptTxt3()) && positions.length>1){
//				throw new AnneException("代理商的岗位职能选择一个");
//			}
			
			if(flag != null && !flag.equals(positionLov.getOptTxt3()) && "E".equals(flag)){
				throw new AnneException("内部用户只能拥有内部岗位，外部用户只能拥有外部岗位，不能混合！");
			}
			
			if("E".equals(oldEmployee.getFlag()) && !positionLov.getOptTxt3().equals( oldEmployee.getFlag()) ){
				throw new AnneException("代理商员工不能使用内部岗位");
			}
			
			if("E".equals(positionLov.getOptTxt3()) && !"E".equals(oldEmployee.getFlag())){
				throw new AnneException("内部员工不能使用代理商岗位");
			}
			
			if("E".equals(positionLov.getOptTxt3())){
				oldEmployee.setFlag("E");
				baseDao.update(oldEmployee);
			}else{
				oldEmployee.setFlag("A");
				baseDao.update(oldEmployee);
			}
			
			LovMember org = baseDao.findUniqueEntity(" from LovMember p where p.groupId = 'POSITION' and p.optTxt1 = ? ",position);
			if(org != null){
				UserPermission up = new UserPermission();
				up.setMemberId(org.getId());
				up.setUserId(oldEmployee.getId());
				up.setType("P");
				baseDao.save(up);
			}
		}
	}

	@Override
	public IPage query(PageCondition condition) throws AnneException {
		FilterObject filterObject = condition.getFilterObject(Employee.class);
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}
	
	public IPage findEmployeeByPositionId(PageCondition condition){
		String positionId = condition.getStringCondition("positionId");
		String showOutDate = condition.getStringCondition("showOutDate");
		
		StringBuffer hql = new StringBuffer();
		hql.append("select e from Employee e ,UserPermission up where e.id = up.userId and up.type = 'P' and up.memberId = ? ");
		
		if(!StringUtil.isNullOrEmpty(showOutDate) && showOutDate.equals("true")){
			
		}else{
			hql.append(" and e.endDate > sysdate ");
		}
		return baseDao.search(hql.toString(),new Object[]{positionId},condition.getRows(), condition.getPage());
	}
	
	public List<Employee> getAllEmployee(){
		String hql = "select e from Employee e ";
		return baseDao.findEntity(hql);
	}
	
	public IPage queryTeamByBusinessId(PageCondition condition){
		String businessId = condition.getStringCondition("businessId");
		String businessType = condition.getStringCondition("businessType");
		String businessId1 = condition.getStringCondition("businessId1");
		if(StringUtil.isNotEmpty(businessId1)){
			businessId = businessId1;
		}
		String hql = " select new com.ibm.kstar.entity.team.vo.TeamVo(t,e,p,org) from Team t ,Employee e ,LovMember p,LovMember o ,LovMember org where t.positionId = p.id and p.groupId = 'POSITION' and o.groupId='ORG' and t.masterEmployeeId = e.id and p.optTxt1 = o.id and o.parentId = org.id and t.businessId = ? and t.businessType = ? ";
		return baseDao.search(hql,new Object[]{businessId,businessType},condition.getRows(), condition.getPage());
	}
	
	public List<TeamVo> findTeamByBusinessId(Condition condition){
		String businessId = condition.getStringCondition("businessId");
		String businessId1 = condition.getStringCondition("businessId1");
		if(StringUtil.isNotEmpty(businessId1)&&!"undefined".equals(businessId1)){
			businessId = businessId1;
		}
		String businessType = condition.getStringCondition("businessType");
		String hql = " select new com.ibm.kstar.entity.team.vo.TeamVo(t,e,p,o) from Team t ,Employee e ,LovMember p,LovMember o where t.positionId = p.id and p.groupId = 'POSITION' and o.groupId='ORG' and t.masterEmployeeId = e.id and p.optTxt1 = o.id and t.businessId = ? and t.businessType = ? ";
		return baseDao.findEntity(hql,new Object[]{businessId,businessType});
	}

	@Override
	public void delete(String employeeId) throws AnneException {
		baseDao.deleteByEndDate(Employee.class, employeeId);
		baseDao.deleteByEndDate(User.class, employeeId);
	}

	@Override
	public Employee get(String employeeId) throws AnneException {
		return baseDao.get(Employee.class, employeeId);
	}
	@Override
	public Employee getEmployeeByNo(String employeeNo) throws AnneException {
		if(StringUtil.isEmpty(employeeNo)){
			return null;
		}
		return baseDao.findUniqueEntity( " from Employee e where e.no = ? ",new Object[]{employeeNo});
	}
	
	@Override
	public List<Employee> findEmployeeByOrgId(String orgId){
		List<Object> args = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer();
		hql.append("select distinct e from Employee e,UserPermission up,LovMember p ,LovMember po where po.id = p.optTxt1  and p.id = up.memberId and e.id = up.userId and up.type = 'R' ");
		if(orgId !=null){
			hql.append(" and po.path like ?  ");
			args.add("%" + orgId + "%");
		}
		
		return baseDao.findEntity(hql.toString(), args.toArray());
	}

	@Override
	public List<LovMember> findOrgByEmployeeNameOrNo(String searchKey) {
		
		/*查询组织*/
		List<Object> args = new ArrayList<Object>();
		StringBuffer sb = new StringBuffer();
		sb.append(" select e from LovMember e where e.id in (");
		sb.append(" select po.parentId from LovMember p,Employee e,UserPermission up,LovMember po ");
		sb.append(" where po.id = p.optTxt1 ");
		sb.append(" and p.id = up.memberId ");
		sb.append(" and e.id = up.userId ");
		sb.append(" and up.type = 'P' ");
		sb.append(" and (e.name = ? or e.no = ?))");
		
		if(StringUtil.isNotEmpty(searchKey)){
			args.add(searchKey);
		}
		if(StringUtil.isNotEmpty(searchKey)){
			args.add(searchKey);
		}
		
		List<LovMember> lovMembers = baseDao.findEntity(sb.toString(),args.toArray());
		
		
		/*查询岗位*/
		List<Object> args2 = new ArrayList<Object>();
		String hql = "select m from LovMember m where m.id in ( select up.memberId from UserPermission up,Employee e where e.id = up.userId and up.type = 'P' and (e.name = ? or e.no = ?))";
		if(StringUtil.isNotEmpty(searchKey)){
			args2.add(searchKey);
		}
		if(StringUtil.isNotEmpty(searchKey)){
			args2.add(searchKey);
		}
		
		List<LovMember> lovMemberList = baseDao.findEntity(hql,args2.toArray());
	
		for(int i=0;i<lovMembers.size();i++){
			if(lovMembers.size()>=lovMemberList.size()){
				lovMembers.set(i, lovMembers.get(i)).setName(lovMemberList.get(i).getName());
			}
		}
		return lovMembers;
	}
}
