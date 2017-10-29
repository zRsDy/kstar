package com.ibm.kstar.impl.contract;


import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.dao.HqlUtil;
import org.xsnake.web.dao.utils.FilterObject;
import org.xsnake.web.dao.utils.HqlObject;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.MathUtils;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.api.contract.IContrPayService;
import com.ibm.kstar.api.contract.IContractService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.contract.ContrPay;
import com.ibm.kstar.entity.contract.Contract;
import com.ibm.kstar.entity.custom.CustomInfo;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class ContrPayServiceImpl implements IContrPayService {

	@Autowired
	BaseDao baseDao;
	@Autowired
	private IContractService contractService;
	@Autowired
	private ILovMemberService lovMemberService;	

	@Override
	public IPage query(PageCondition condition) { 
		FilterObject filterObject = condition.getFilterObject(ContrPay.class);
		HqlObject hqlObject = HqlUtil.getHqlObject(filterObject);
		return baseDao.search(hqlObject.getHql(),hqlObject.getArgs(), condition.getRows(), condition.getPage());
	}

	@Override
	public void save(ContrPay contrPay, String contrNo) throws AnneException { 	
//		String planNo= genReceivePlanNo(contrNo);
//		contrPay.setPayNo(planNo);
		baseDao.save(contrPay);		
	}
	
	

	@Override
	public ContrPay get(String id) throws AnneException {
		if(id==null){
			return null;
		}
		return baseDao.get(ContrPay.class, id);
	}

	@Override
	public void update(ContrPay contrPay) throws AnneException {
		ContrPay old = baseDao.get(ContrPay.class, contrPay.getId());
		if(old == null){
			throw new AnneException(IContrPayService.class.getName() + " saveQuot : 没有找到要更新的数据");
		}
		
		if(!contrPay.getId().equals(old.getId())){
			throw new AnneException("单据不能被修改");
		}
		
		BeanUtils.copyPropertiesIgnoreNull(contrPay, old);
		//old.setCreateTime(new Time(0));
		baseDao.update(old);
		Long count = baseDao.findUniqueEntity("select count(*) from ContrPay where id = ? ",contrPay.getId());
		if(count > 1){
			throw new AnneException("合同单ID已经存在!"); 
		}
		
	}

	public void delete(String id) throws AnneException {
		baseDao.executeHQL(" delete ContrPay contr where contr.id = ? ",new Object[]{id});
		
	}
	
	public String genReceivePlanNo(String contrNo)throws AnneException {
		String planNo="";
		@SuppressWarnings("deprecation")
//		Connection conn = baseDao.getHibernateTemplate().getSessionFactory().openSession().connection();
//        try {
			String sql = "{ ? = call CRM_P_CONTRACT_PUB.gen_receive_plan_num(?,?)}";
//	        CallableStatement sta = conn.prepareCall(sql);
//	        sta.registerOutParameter(1, OracleTypes.VARCHAR);
//            sta.setInt(2, -1);
//            sta.setString(3, contrNo);
//	        sta.execute();
//	        planNo = sta.getString(1);
//	        
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
			Object[] result = baseDao.executeProcedure(sql, new BaseDao.ProcedureParam[]{
					new BaseDao.OutProcedureParam(Types.VARCHAR),
					new BaseDao.InProcedureParam(-1),
			});
			planNo = (String)result[0];
		return planNo; 
		
	}
	
	/**
	 * 
	 * getContrPayByContrId:根据合同ID获取收款计划. <br/> 
	 * 
	 * @author liming 
	 * @param contrId 合同ID
	 * @throws AnneException 
	 * @since JDK 1.7
	 */
	@Override
	public List<ContrPay> getContrPayByContrId(String contrId) throws AnneException {
		String hql = " from ContrPay contr where contr.contrId = ? ";
		return baseDao.findEntity(hql, new Object[]{contrId});
	}

	/**
	 * 
	 * getContrPayByContrId:根据合同ID引用框架协议收款计划. <br/> 
	 * 
	 * @author ibm 
	 * @param contrId 合同ID
	 * @throws AnneException 
	 * @since JDK 1.7
	 */
	@Override
	public void copyPayPlanByContrId(String contrId) throws AnneException {
		Contract contr = contractService.get(contrId);
		if(contr == null ){
			throw new AnneException("未找到合同"); 			
		}
		String frameId = contr.getFrameNo();
		if(frameId == null || frameId.equalsIgnoreCase("")){
			throw new AnneException("未找到合同对应的框架协议"); 				
		}
		
		List<ContrPay> payLst =  getContrPayByContrId(frameId);
		if(payLst.size()<=0){
			throw new AnneException("所引用的框架协议中没有回款规则"); 			
		}
		
		Double oldConut = getPlanRatio(contrId);
		
		Double count = 0d;
		for(ContrPay pay : payLst){
			ContrPay nPay = new ContrPay();
			BeanUtils.copyProperties(pay, nPay);
			nPay.setTotalAmt(contr.getTotalAmt());
			nPay.setPlanAmt(contr.getTotalAmt()*nPay.getPlanRatio()/100);
			nPay.setId(null);
			nPay.setContrId(contrId);
			count = MathUtils.add(count, nPay.getPlanRatio());
			baseDao.save(nPay);
		}
		
		Double count2 = MathUtils.add(count, oldConut);
		
		if (count2.compareTo(100d)>0) {
			throw new AnneException("框架协议收款比例="+count+"，计划收款比例不能超过100%");
		}
		
	}
	
	
	/**
	 * 获取回款规则收款比例
	 * @param contrId
	 * @return
	 */
	private Double getPlanRatio(String contrId){
		List<ContrPay> payLst =  getContrPayByContrId(contrId);
		Double count = 0d;
		for (ContrPay contrPay : payLst) {
			count = MathUtils.add(count, contrPay.getPlanRatio());
		}
		return count;
	}
	
	
	
	/**
	 * 
	 * saveOrUpdateContrPayList:直接提交保存列表. <br/> 
	 * 
	 * @author ibm 
	 * @param lines ContrPay
	 * @param contrId 合同ID
	 * @throws AnneException 
	 * @since JDK 1.7
	 */
	@Override
	public void saveOrUpdateContrPayList(List<ContrPay> lines,String contrId) throws AnneException{
		
		List<String> list = new ArrayList<String>();
		if(lines != null){
			for(ContrPay map  :  lines){
				list.add(map.getId());
			}
		}
	
		List<ContrPay> oldLines = getContrPayByContrId(contrId);
		//行数据不在页面返回的集合中，则将其删除
		for(ContrPay ol : oldLines){
			if(ol == null || lines.size() <= 0 || !list.contains(ol.getId())){
				baseDao.deleteById(ContrPay.class, ol.getId());
			}
		}
		
		Double count = 0d;
		
		if(lines!=null){
			for(ContrPay cp: lines){
				ContrPay oldCp ;
				if(StringUtils.isEmpty(cp.getId())){
					cp.setId(null);
					cp.setContrId(contrId);
					oldCp = cp;
				}else{
					oldCp = baseDao.get(ContrPay.class,cp.getId()); 
					if(oldCp == null){
						cp.setId(null);
						oldCp = cp;
						
					}else{
						//将 cp的属性复制到 oldCp
						BeanUtils.copyPropertiesIgnoreNull(cp, oldCp);
					}
				}
				count = MathUtils.add(count, cp.getPlanRatio());
				baseDao.saveOrUpdate(oldCp);
			}
		}
		if (count.compareTo(100d) != 0) {
			throw new AnneException("计划收款比例="+count+"，计划收款比例之和需要等于100");
		}
	}
	
	@Override
	public void saveContrPayForLoan(String contrId) throws AnneException { 	
		Contract contr = contractService.get(contrId);
		ContrPay contrPay = new ContrPay();
		contrPay.setContrId(contrId);
		LovMember seqLov = lovMemberService.getLovMemberByCode("CONTRACTPAYSEQ", "01");// 01	第一笔
		LovMember planLov = lovMemberService.getLovMemberByCode("CONTRACTPAYMENTPLAN", "03");// 03	到货
		contrPay.setPaySeqId(seqLov.getId());
		contrPay.setPayPlan(planLov.getId());
		contrPay.setPlanRatio((Double)100.000000);
		contrPay.setPlanAmt(contr.getTotalAmt());
		contrPay.setTotalAmt(contr.getTotalAmt());
		
		CustomInfo customInfo = baseDao.get(CustomInfo.class, contr.getCustCode());
		 
		LovMember lv41 = lovMemberService.getLovMemberByCode("CUSTOMCATEGORY", "41"); // 大类金融行业
		
		if (customInfo != null && customInfo.getCustomCategory().equals(lv41.getId())) {
			contrPay.setPayTerm("180");
		}else{
			contrPay.setPayTerm("90");
		}
		baseDao.save(contrPay);		
	}
	
	/**
	 * 根据合同ID和金额更新收款明细
	 * @param contrId
	 * @param amt
	 */
	public void updateContrPayAmt(String contrId ,Double amt){
		if (amt == null || StringUtil.isNullOrEmpty(contrId)) {
			return;
		}
		List<ContrPay> list = getContrPayByContrId(contrId);
		for (ContrPay contrPay : list) {
			contrPay.setTotalAmt(amt);
			contrPay.setPlanAmt(amt*(contrPay.getPlanRatio()==null?0d:contrPay.getPlanRatio())/100);
			baseDao.update(contrPay);
		}
	}
	
	
	
	
	
}
