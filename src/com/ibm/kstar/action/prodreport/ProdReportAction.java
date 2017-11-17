//package com.ibm.kstar.action.prodreport;
//
//import java.util.List;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.xsnake.web.action.BaseAction;
///**
// * Created by xiaoyifei on 2017/6/19.
// * 生产看板系统报表
// */
//import org.xsnake.web.action.PageCondition;
//import org.xsnake.web.log.LogOperate;
//import org.xsnake.web.page.IPage;
//import org.xsnake.web.utils.ActionUtil;
//import org.xsnake.web.utils.ExcelUtil;
//
//import com.ibm.kstar.api.prodreport.IProductReportService;
//import com.ibm.kstar.interceptor.system.permission.NoRight;
//@Controller
//@RequestMapping(value = "/prodreport")
//public class ProdReportAction extends BaseAction {
//	
//	   @Autowired
//	   IProductReportService productReportService;
//	   
//	   //衍生品查询
//	   @RequestMapping("/derilist") 
//	    public String derilist() {
//	        return forward("derilist");
//	    }
//
//	   @LogOperate(module="研发报表：衍生品查询",notes="页面：衍生品查询")
//	    @ResponseBody
//	    @RequestMapping("/deripage")
//	    public String page(PageCondition condition, HttpServletRequest request) {
//	    	ActionUtil.requestToCondition(condition, request);
//	        IPage p = productReportService.query(condition);
//	        return sendSuccessMessage(p);
//	    }
//	    
//	    @LogOperate(module="研发报表：衍生品查询",notes="页面：衍生品导出")
//	    @RequestMapping(value = "/proReportSanshenExport")
//		public void proReportSanshenExport( PageCondition condition, HttpServletRequest request,HttpServletResponse response) throws Exception{
//	    	ActionUtil.requestToCondition(condition, request);
//			List<List<Object>> dataList = productReportService.proReportYanshenExport(condition);
//			ExcelUtil.exportExcel(response, dataList, "衍生品列表");
//		}
//}
