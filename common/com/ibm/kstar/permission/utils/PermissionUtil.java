package com.ibm.kstar.permission.utils;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import org.xsnake.web.context.MessageContext;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.utils.StringUtil;

public class PermissionUtil {

	/**
	 * type = 1;  用户ID
	 * type = 2;  组织ID
	 * type = 3;  岗位ID
	 * type = 4;  销售组织（暂无）
	 * type = 5;  销售团队
	 * type = 6;  组织上下级   对应  2
	 * type = 7;  岗位上下级   对应  3
	 * type = 8;  销售组织上下级（暂无）
	 * type = 9;  销售团队上下级
	 * 
	 * @param type
	 * @param leftUserId   HQL查询中主表的左条件，如主表别名为t，其中用户权限字段为userId 则传"t.userId"
	 * @param leftPositionId 同leftUserId
	 * @param leftOrgId  同leftUserId
	 * @param leftBusinessId
	 * @param user        当前登录用户对象
	 * @param businessType  所属业务类型（销售团队中设置的数据保持一致）
	 * @return
	 */
	
	
	public static String getPermissionHQL(String type,String leftUserId,String leftPositionId,String leftOrgId,String leftBusinessId,UserObject user,String businessType){
		
		BaseDao baseDao = (BaseDao)MessageContext.getBean("baseDao");
		LovMember m = baseDao.findUniqueEntity("from LovMember where groupCode  =  'AUTH_APPLICATION' and code = ? ", new Object[]{businessType});

		if(m !=null){
			type = m.getOptTxt1();
		}

		if(StringUtil.isEmpty(type)){
			type = "5";
		}
		
		String userId = user.getOrg().getId();
		String positionId = user.getPosition().getId();
		String orgId = user.getEmployee().getId();
		
		StringBuffer hql = new StringBuffer();
		
		if("1".equals(type)){
			hql.append(leftUserId).append(" = '").append(userId).append("'");
			return hql.toString();
		}
		
		if("2".equals(type)){
			hql.append(leftOrgId).append(" = '").append(orgId).append("'");
			return hql.toString();
		}
		
		if("3".equals(type)){
			hql.append(leftPositionId).append(" = '").append(positionId).append("'");
			return hql.toString();
		}
		
		if("4".equals(type)){
			StringBuffer subHql = new StringBuffer();
			subHql.append("select t.businessId from Orgs t where t.businessType = '"+businessType+"' and t.orgId = '"+orgId+"' ");
			hql.append(leftBusinessId).append(" in( ").append(subHql).append(" ) ");
			return hql.toString();
		}
		
		if("5".equals(type)){
			StringBuffer subHql = new StringBuffer();
			subHql.append("select t.businessId from Team t where t.businessType = '"+businessType+"' and t.positionId = '"+positionId+"' ");
			hql.append(leftBusinessId).append(" in( ").append(subHql.toString()).append(" ) ");
			return hql.toString();
		}
		
		if("6".equals(type)){
			StringBuffer subHql = new StringBuffer();
			subHql.append(" select o.id from LovMember o where o.path like '%"+orgId+"%' ");
			hql.append(leftOrgId).append(" in( ").append(subHql.toString()).append(" ) ");
			return hql.toString();
		}
		
		if("7".equals(type)){
			StringBuffer subHql = new StringBuffer();
			subHql.append(" select p.id from LovMember p where p.groupId = 'POSITION' and p.path like '%"+positionId+"%' ");
			hql.append(leftPositionId).append(" in( ").append(subHql.toString()).append(" ) ");
			return hql.toString();
		}
		
		if("8".equals(type)){
			StringBuffer subHql = new StringBuffer();
			subHql.append("select t.businessId from Orgs t ,LovMember o where o.groupId = 'ORG' and o.path like '%"+orgId+"%' and o.id = t.orgId and t.businessType = '"+businessType+"' ");
			hql.append(leftBusinessId).append(" in( ").append(subHql).append(" ) ");
			return hql.toString();
		}
		
		if("9".equals(type)){
			StringBuffer subHql = new StringBuffer();
			subHql.append("select t.businessId from Team t , LovMember p  where  p.groupId = 'POSITION' and p.path like '%"+positionId+"%' and p.id = t.positionId and t.businessType = '"+businessType+"' ");
			hql.append(leftBusinessId).append(" in( ").append(subHql.toString()).append(" ) ");
			return hql.toString();
		}
		throw new AnneException("暂不支持的类型");
	}
	
	public static String getPermissionSQL(String type,String leftUserId,String leftPositionId,String leftOrgId,String leftBusinessId,UserObject user,String businessType){

		BaseDao baseDao = (BaseDao)MessageContext.getBean("baseDao");

		LovMember m = baseDao.findUniqueEntity("from LovMember where groupCode  =  'AUTH_APPLICATION' and code = ? ", new Object[]{businessType});

		if(m !=null){
			type = m.getOptTxt1();
		}

		if(StringUtil.isEmpty(type)){
			type = "5";
		}
		
		String userId = user.getOrg().getId();
		String positionId = user.getPosition().getId();
		String orgId = user.getEmployee().getId();
		
		StringBuffer hql = new StringBuffer();
		
		if("1".equals(type)){
			hql.append(leftUserId).append(" = '").append(userId).append("'");
			return hql.toString();
		}
		
		if("2".equals(type)){
			hql.append(leftOrgId).append(" = '").append(orgId).append("'");
			return hql.toString();
		}
		
		if("3".equals(type)){
			hql.append(leftPositionId).append(" = '").append(positionId).append("'");
			return hql.toString();
		}
		
		if("4".equals(type)){
			StringBuffer subHql = new StringBuffer();
			subHql.append("select t.business_Id from crm_t_orgs t where t.business_Type = '"+businessType+"' and t.org_Id = '"+orgId+"' ");
			hql.append(leftBusinessId).append(" in( ").append(subHql).append(" ) ");
			return hql.toString();
		}
		
		if("5".equals(type)){
			StringBuffer subHql = new StringBuffer();
			subHql.append("select t.business_id from crm_t_team t where t.business_type = '"+businessType+"' and t.position_id = '"+positionId+"' ");
			hql.append(leftBusinessId).append(" in( ").append(subHql.toString()).append(" ) ");
			return hql.toString();
		}
		
		if("6".equals(type)){
			StringBuffer subHql = new StringBuffer();
			subHql.append(" select o.row_id from sys_t_lov_member o where o.GROUP_ID = 'ORG' and o.lov_path like '%"+orgId+"%' ");
			hql.append(leftOrgId).append(" in( ").append(subHql.toString()).append(" ) ");
			return hql.toString();
		}
		
		if("7".equals(type)){
			StringBuffer subHql = new StringBuffer();
			subHql.append(" select p.row_id from sys_t_lov_member p where p.GROUP_ID = 'POSITION' and p.lov_path like '%"+positionId+"%' ");
			hql.append(leftPositionId).append(" in( ").append(subHql.toString()).append(" ) ");
			return hql.toString();
		}
		
		if("8".equals(type)){
			StringBuffer subHql = new StringBuffer();
			subHql.append("select t.business_Id from crm_t_orgs t ,sys_t_lov_member o where o.group_Id = 'ORG' and o.lov_path like '%"+orgId+"%' and o.row_id = t.org_Id and t.business_Type = '"+businessType+"' ");
			hql.append(leftBusinessId).append(" in( ").append(subHql).append(" ) ");
			return hql.toString();
		}
		
		if("9".equals(type)){
			StringBuffer subHql = new StringBuffer();
			subHql.append("select t.business_id from crm_t_team t , sys_t_lov_member p  where  p.GROUP_ID = 'POSITION' and p.lov_path like '%"+positionId+"%' and p.row_id = t.position_Id and t.business_Type = '"+businessType+"' ");
			hql.append(leftBusinessId).append(" in( ").append(subHql.toString()).append(" ) ");
			return hql.toString();
		}
		
		throw new AnneException("暂不支持的类型");
	}
	
}
