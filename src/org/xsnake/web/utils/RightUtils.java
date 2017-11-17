package org.xsnake.web.utils;

public class RightUtils {

//	String hql = "select a from AA , BB where a.userId = b.userId and b.userId = ? ";//用户
//	hql ="select a from AA,BB where a.orgId = b.orgId and b.orgId = ? ";//组织
//	hql = "select a from AA , BB where a.positionId = b.positionId and b.positionId = ? ";// 岗位
//	hql = "select a from AA , BB where a.orgId = b.orgId and b.orgId = ? ";// 业务组织上下级
//	hql = "select a from AA,BB where a.id = b.businessId and a.positionId = b.positionId = ? ";//销售团队
//	hql = "select distinct a from AA , BB where a.id = b.businessId and a.positionId = b.childPositionId and b.position = ? ";//销售团队上下级
//	hql = "select distinct a from AA,  BB where a.orgId = b.childOrgId and b.orgId = ? ";//组织上下级
//	hql = "select distinct a from AA , BB where a.orgId = b.childOrgId and b.orgId = ? ";// 业务组织
//	hql = "select distinct a from AA , BB where a.positionId = b.childPositionId and b.positionId = ? ";// 岗位上下级
	
	public String getRightHQLTable(String business){
		return null;
	}
	
	public String getRightSQLTable(String business){
		String hql = " BB ";
		return null;
	}
	
	public String getRightHQLWhere(){
		return null;
	}
	
	public String getRightSQLWhere(){
		return null;
	}
	
}
