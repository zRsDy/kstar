package com.ibm.kstar.impl.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.StringUtil;
import org.xsnake.xflow.api.IProcessService;
import org.xsnake.xflow.api.workflow.IXflowProcessServiceWrapper;

import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.action.common.process.ProcessStatusService;
import com.ibm.kstar.api.product.IDocService;
import com.ibm.kstar.api.product.IProductProcesService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.IUserPermissionService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.team.ITeamService;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.entity.product.KstarProductDoc;
import com.ibm.kstar.impl.BaseServiceImpl;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class DocServiceImpl extends BaseServiceImpl implements IDocService{
	
	@Autowired
	BaseDao baseDao;
	@Autowired
	IProductProcesService productProcess;
	@Autowired
	IProcessService processService;
	@Autowired
	ILovMemberService lovMemberService;
	@Autowired
	ProcessStatusService processStatusService;
	@Autowired
	IXflowProcessServiceWrapper xflowProcessServiceWrapper;
	@Autowired
	IUserPermissionService userPermissionService;
	@Autowired
	protected ITeamService teamService;
	
	@Override
	public IPage query(PageCondition condition) throws Exception  {
		StringBuilder hql = new StringBuilder();
		hql.append("select distinct d from KstarProductDoc d,Team t,UserPermission u where d.id = t.businessId and t.positionId = u.memberId");
		hql.append(" and d.productID=? and u.userId=?");
		List<Object> args = new ArrayList<Object>();
		args.add(condition.getCondition("productID"));
		args.add(condition.getCondition("applyPerson"));
		this.addQueryCondition(condition, args, hql, "docName", "d.docName", "like");
		hql.append(" order by d.id desc");
		return baseDao.search(hql.toString(), args.toArray(), condition.getRows(), condition.getPage());
	}

	@Override
	public void save(KstarProductDoc doc, UserObject user) {
		doc.setRecordInfor(false, user);
		baseDao.save(doc);
		//加销售团队
		teamService.addPosition(user.getPosition().getId(),user.getEmployee().getId(),"productDoc",doc.getId());
	}

	@Override
	public void update(KstarProductDoc doc, UserObject user) {
		KstarProductDoc old = baseDao.get(KstarProductDoc.class, doc.getId());
		BeanUtils.copyPropertiesIgnoreNull(doc, old);
		old.setRecordInfor(true, user);
		baseDao.update(old);
	}

	@Override
	public KstarProductDoc queryDocByID(String docId) {
		// TODO Auto-generated method stub
		return baseDao.findUniqueEntity(" from KstarProductDoc t where t.id= '" + docId + "'");
	}

	@Override
	public void checkDocEditByID(String docId) {
		KstarProductDoc kd = baseDao.findUniqueEntity(" from KstarProductDoc t where t.id= '" + docId + "'");
		LovMember value = (LovMember) CacheData.getInstance().get(kd.getApplyStatus());
		if(value != null && !"新建".equalsIgnoreCase(value.getName()) && !"已驳回".equalsIgnoreCase(value.getName())){
			throw new AnneException("只有状态 in ('新建','已驳回')时才可以修改！");
		}
	}

	@Override
	public void delete(String id) {
		KstarProductDoc  kp = baseDao.get(KstarProductDoc.class, id);
		if(kp == null){
			throw new AnneException("该文档已经不存在！");
		}
		LovMember value = (LovMember) CacheData.getInstance().get(kp.getApplyStatus());
		if(value != null && !"新建".equalsIgnoreCase(value.getName()) && !"已驳回".equalsIgnoreCase(value.getName())){
			throw new AnneException("只有状态 in ('新建','已驳回')时才可以删除！");
		}else{
			baseDao.deleteById(KstarProductDoc.class, id);
		}
	}

	@Override
	public void startDocApplyrocess(UserObject user, String id) {
		KstarProductDoc doc = baseDao.get(KstarProductDoc.class, id);
		doc.setApplyStatus(doc.getLovMember(ProcessConstants.PRODUCT_DOC_PROC, ProcessConstants.PROCESS_STATUS_02).getId());
		doc.setApplyTime(new Date());
		baseDao.update(doc);
		//启动流程
		String model = lovMemberService.getFlowCodeByAppCode(ProcessConstants.PRODUCT_DOC_PROC);
		String flowName = null;
		if(StringUtil.isNotEmpty(model)){
			flowName = doc.getLovMember("APPLICATION", model).getName();
		}
		Map<String,String> varmap = new HashMap<>();
		varmap.put("TODO", "TODO");		
		xflowProcessServiceWrapper.start(model, ProcessConstants.PRODUCT_DOC_PROC, flowName+"-"+doc.getDocName(), id, user, varmap);
	}
	
}


