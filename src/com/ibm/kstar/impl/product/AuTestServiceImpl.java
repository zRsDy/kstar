package com.ibm.kstar.impl.product;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.dao.HqlUtil;
import org.xsnake.web.dao.utils.FilterObject;
import org.xsnake.web.dao.utils.HqlObject;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.StringUtil;
import org.xsnake.xflow.api.IProcessService;
import org.xsnake.xflow.api.workflow.IXflowProcessServiceWrapper;

import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.action.common.process.ProcessStatusService;
import com.ibm.kstar.api.product.IAuTestService;
import com.ibm.kstar.api.product.IProductProcesService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.entity.product.KstarProductAuTest;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class AuTestServiceImpl implements IAuTestService{
	@Autowired
	BaseDao baseDao;
	@Autowired
	IProcessService processService;
	@Autowired
	IProductProcesService productProcess;
	@Autowired
	ProcessStatusService processStatusService;
	@Autowired
	ILovMemberService lovMemberService;
	@Autowired
	IXflowProcessServiceWrapper xflowProcessServiceWrapper;
	
	@Override
	public IPage query(PageCondition condition) throws Exception  {
		FilterObject filterObject = condition.getFilterObject();
		filterObject.setClazz(KstarProductAuTest.class);
		filterObject.addOrderBy("id", "desc");
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		IPage page = baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
		@SuppressWarnings("unchecked")
		List<KstarProductAuTest>  proList = (List<KstarProductAuTest>) page.getList();

		//对申请人赋值 
		Employee employee=null;
		Employee copyedEmployee=null;
		for(KstarProductAuTest kpa:proList){
			if(StringUtils.isNotBlank(kpa.getApplyPersion())){
				employee = baseDao.get(Employee.class, kpa.getApplyPersion());
				copyedEmployee = new Employee();
				if(null!= employee){
					BeanUtils.copyPropertiesIgnoreNull(employee, copyedEmployee);
					kpa.setApplyPersionBean(copyedEmployee);
				}				
			}
		}
		return page;
	}


	@Override
	public void save(KstarProductAuTest doc, UserObject user) {
		if(doc.getId() != null){
			doc.setRecordInfor(true, user);
			baseDao.update(doc);
		}else{
			doc.setRecordInfor(false, user);
			baseDao.save(doc);
		}
	}
	
	@Override
	public void update(KstarProductAuTest kc, UserObject user){
		KstarProductAuTest old = baseDao.get(KstarProductAuTest.class, kc.getId());
		BeanUtils.copyPropertiesIgnoreNull(kc, old);
		old.setRecordInfor(true, user);
		baseDao.update(old);
	}
	
	@Override
	public KstarProductAuTest queryAuTestById(String id) {
		KstarProductAuTest kd = baseDao.get(KstarProductAuTest.class, id);
		return kd;
	}

	@Override
	public void delete(String id) {
		baseDao.deleteById(KstarProductAuTest.class, id);
	}

	@Override
	public void startAuTestApplyrocess(UserObject user, String id) {
		//更新流程状态为已发起,记录流程ID
		KstarProductAuTest kd = baseDao.get(KstarProductAuTest.class, id);
		kd.setStatus(kd.getLovMember(ProcessConstants.PRODUCT_AU_TEST_PROC, ProcessConstants.PROCESS_STATUS_02).getId());
		kd.setSubmitTime(new Date());
		kd.setRecordInfor(true, user);
		baseDao.update(kd);
		//启动流程
		String model = lovMemberService.getFlowCodeByAppCode(ProcessConstants.PRODUCT_AU_TEST_PROC);
		String flowName = null;
		if(StringUtil.isNotEmpty(model)){
			flowName = kd.getLovMember("APPLICATION", model).getName();
		}
		Map<String,String> varmap = new HashMap<>();
		varmap.put("TODO", "TODO");		
		xflowProcessServiceWrapper.start(model, ProcessConstants.PRODUCT_AU_TEST_PROC, flowName+"-"+kd.getAuTestName(), id, user, varmap);
	}	
}
