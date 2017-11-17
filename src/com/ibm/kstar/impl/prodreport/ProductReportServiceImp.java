//package com.ibm.kstar.impl.prodreport;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.xsnake.web.action.PageCondition;
//import org.xsnake.web.dao.BaseDao;
//import org.xsnake.web.exception.AnneException;
//import org.xsnake.web.page.IPage;
//import com.ibm.kstar.api.prodreport.IProductReportService;
//import com.ibm.kstar.entity.prodreport.SpecProductDev;
//import com.ibm.kstar.impl.BaseServiceImpl;
//@Service
//public class ProductReportServiceImp extends BaseServiceImpl implements IProductReportService{
//	
//	@Autowired
//	BaseDao baseDaoPDM;
//
//	@Override
//	public IPage query(PageCondition condition) throws AnneException {
//		 List<Object> params = new ArrayList<Object>();
//		 StringBuffer hql = new StringBuffer();
//		 hql.append(" select ");
//		 hql.append(" b ");
//		 hql.append(" from ");
//		 hql.append(" SpecProductDev b ");
//		 
//		 hql.append(" where 1=1 ");
//		 int args=0;
//		 if(condition.getCondition("projcetDesc") != null && condition.getCondition("projcetDesc") != ""){
//			hql.append(" and b.projcetDesc like ? ");
//			params.add("%"+condition.getCondition("projcetDesc")+"%");
//			args=args+1;
//		 }
//		 if(args==0){
//			hql.append(" and trunc(sysdate-to_date(statime,'yyyy-MM-dd hh24:mi:ss'))<30 ");
//		 }
//		 hql.append(" order by b.statime  desc ");
//		 return baseDaoPDM.search(hql.toString(),params.toArray(), condition.getRows(), condition.getPage());
//	}
//	
//	public List<List<Object>>  proReportYanshenExport(PageCondition condition)  throws AnneException{
//		List<List<Object>> lstOut = new ArrayList<List<Object>>();
//		addHead(lstOut);
//		List<SpecProductDev> listIn =exportSelectedProYanshenLists(condition);
//		int i=0;
//		for(SpecProductDev specProductDev:listIn){
//			i=i+1;
//			List<Object> lstIn = new ArrayList<Object>();
//			lstIn.add(i);
//			lstIn.add(specProductDev.getProjcetDesc());
//			lstIn.add(specProductDev.getStartor());
//			lstIn.add(specProductDev.getStatime());
//			lstIn.add(specProductDev.getReqApprovedtime());
//			lstIn.add(specProductDev.getCpfpUser());
//			lstOut.add(lstIn);
//		}
//		return  lstOut;
//	}
//	
//	public  List<SpecProductDev> exportSelectedProYanshenLists(PageCondition condition) throws AnneException{
//		List<Object> params = new ArrayList<Object>();
//		StringBuffer hql = new StringBuffer(" from SpecProductDev spv where 1=1 ");
//		 int args=0;
//		if(condition.getCondition("projcetDesc") != null && condition.getCondition("projcetDesc") != ""){
//				hql.append(" and spv.projcetDesc like ? ");
//				params.add("%"+condition.getCondition("projcetDesc")+"%");
//				args=args+1;
//	     }
//		 if(args==0){
//				hql.append(" and trunc(sysdate-to_date(spv.statime,'yyyy-MM-dd hh24:mi:ss'))<30 ");
//		 }
//		hql.append(" order by spv.statime desc ");
//		return baseDaoPDM.findEntity(hql.toString(),params.toArray());
//
//		
//		 
//	}
//	
//	private void  addHead(List<List<Object>> lstOut){
//		List<Object> lstHead = new ArrayList<Object>();
//		lstHead.add("序号");
//		lstHead.add("申请人");
//		lstHead.add("申请时间");
//		lstHead.add("申请部门审批时间");
//		lstHead.add("产品分配人");
//		lstOut.add(lstHead);
//	}
//	
//}
