/** 
 * Project Name:crm 
 * File Name:QuotProcessAction.java 
 * Package Name:com.ibm.kstar.action.quot 
 * Date:Feb 14, 20177:37:21 PM 
 * Copyright (c) 2017, ZW All Rights Reserved. 
 * 
 */  
      
package com.ibm.kstar.action.quot;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.action.BaseFlowAction;
import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.price.IPriceHeadService;
import com.ibm.kstar.api.quot.IQuotService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.product.KstarProductWorkFlow;
import com.ibm.kstar.entity.product.ProductPriceHead;
import com.ibm.kstar.entity.quot.KstarBiddcevl;
import com.ibm.kstar.entity.quot.KstarPrjEvl;
import com.ibm.kstar.entity.quot.KstarPrjLst;
import com.ibm.kstar.entity.quot.KstarQuot;
import com.ibm.kstar.interceptor.system.permission.NoRight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.xflow.api.IHistoryService;
import org.xsnake.xflow.api.IProcessService;
import org.xsnake.xflow.api.ITaskService;
import org.xsnake.xflow.api.model.HistoryActivityInstance;
import org.xsnake.xflow.api.model.ProcessInstance;
import org.xsnake.xflow.api.model.Task;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/** 
 * ClassName:QuotProcessAction <br/> 
 * Function: TODO ADD FUNCTION. <br/> 
 * Reason:   TODO ADD REASON. <br/> 
 * Date:     Feb 14, 2017 7:37:21 PM <br/> 
 * @author   ZW 
 * @version   
 * @since    JDK 1.7 
 * @see       
 */

 @Controller
 @RequestMapping("/quotprocess")
 public class QuotProcessAction extends BaseFlowAction implements IConstants {

		@Autowired
		IProcessService processService;
		
		@Autowired
		IQuotService quotService;
		
		@Autowired
		ITaskService taskService;
		
		@Autowired
		BaseDao baseDao;
		
		@Autowired
		ILovMemberService lovMemberService;
		
		@Autowired
		IHistoryService historyService;
		
		@Autowired
		IPriceHeadService priceheadservice;
		
		
		@ResponseBody
		@RequestMapping(value = "/startQuotPrjAdtProcess", method = RequestMethod.POST)
		public String startQuotPrjAdtProcess(String ids,String qid,HttpServletRequest request) throws Exception{
		
			String[] _ids = ids.split(",");

			ArrayList<String> idsarry = new ArrayList<String>();
			ArrayList<String> evlTyparry = new ArrayList<String>();
			
			//自动启动序号最小流程
//			ArrayList<String> evlids = quotService.getPrjevlids(qid);
//			evlTyparry= quotService.getevlTypbyids(evlids);
			
			
//			for(int i=0;i< evlTyparry.size();i++){
//				
//				//售前评审
//				if((evlTyparry.get(i)).equals("E01")){
//					KstarPrjEvl prjevl = quotService.getKstarPrjEvl(evlids.get(i));
//					prjevl.setEvlSt("S01");
//					quotService.updatePrjEvl(prjevl);
//					quotService.startPreSaleProcess(getUserObject(),prjevl.getId());
//				}
//			
//				//售后评审
//				if((evlTyparry.get(i)).equals("E02")){
//					KstarPrjEvl prjevl = quotService.getKstarPrjEvl(evlids.get(i));
//					prjevl.setEvlSt("S01");
//					quotService.updatePrjEvl(prjevl);
//					quotService.startAftSaleProcess(getUserObject(),prjevl.getId());
//				}
//			
//				//商务评审
//				if((evlTyparry.get(i)).equals("E03")){
//					KstarPrjEvl prjevl = quotService.getKstarPrjEvl(evlids.get(i));
//					prjevl.setEvlSt("S01");
//					quotService.updatePrjEvl(prjevl);
//					quotService.startBusinessProcess(getUserObject(),prjevl.getId());
//				}
//				
//				//决策评审
//				if((evlTyparry.get(i)).equals("E04")){
//					KstarPrjEvl prjevl = quotService.getKstarPrjEvl(evlids.get(i));
//					prjevl.setEvlSt("S01");
//					quotService.updatePrjEvl(prjevl);
//					quotService.startDecisionProcess(getUserObject(),prjevl.getId());
//				}
//				
//				//价格评审
//				if((evlTyparry.get(i)).equals("E05")){
//					KstarPrjEvl prjevl = quotService.getKstarPrjEvl(evlids.get(i));
//					prjevl.setEvlSt("S01");
//					quotService.updatePrjEvl(prjevl);
//					quotService.startPriceProcess(getUserObject(),prjevl.getId());
//				}
//			
//			}
			
			idsarry = quotService.getevlIds(_ids);
			evlTyparry= quotService.getevlTypbyArraylist(idsarry);
			
			for(int i=0;i< evlTyparry.size();i++){
				
				//售前评审
				if(evlTyparry.get(i)!=null){
					if((evlTyparry.get(i)).equals("E01")){
						KstarPrjEvl prjevl = quotService.getKstarPrjEvl(idsarry.get(i));
						prjevl.setEvlSt("S01");
						quotService.updatePrjEvl(prjevl);
						quotService.startPreSaleProcess(getUserObject(),idsarry.get(i));
						//
//						KstarQuot quot = quotService.getKstarQuot(idsarry.get(i));
//						quot.setProReviewStatus("S01"); //审批中
//						quotService.updateQuot(quot);
					}
				}

				//售后评审
				if(evlTyparry.get(i)!=null){
					if((evlTyparry.get(i)).equals("E02")){
						KstarPrjEvl prjevl = quotService.getKstarPrjEvl(idsarry.get(i));
						prjevl.setEvlSt("S01");
						quotService.updatePrjEvl(prjevl);
						quotService.startAftSaleProcess(getUserObject(),idsarry.get(i));
					}
				}

				//商务评审
				if(evlTyparry.get(i)!=null){
					if((evlTyparry.get(i)).equals("E03")){
						KstarPrjEvl prjevl = quotService.getKstarPrjEvl(idsarry.get(i));
						prjevl.setEvlSt("S01");
						quotService.updatePrjEvl(prjevl);
						quotService.startBusinessProcess(getUserObject(),idsarry.get(i));
					}
				}

				//决策评审
				if(evlTyparry.get(i)!=null){
					if((evlTyparry.get(i)).equals("E04")){
						KstarPrjEvl prjevl = quotService.getKstarPrjEvl(idsarry.get(i));
						prjevl.setEvlSt("S01");
						quotService.updatePrjEvl(prjevl);
						quotService.startDecisionProcess(getUserObject(),idsarry.get(i));
					}
				}

				//价格评审
				if(evlTyparry.get(i)!=null){
					if((evlTyparry.get(i)).equals("E05")){
						KstarPrjEvl prjevl = quotService.getKstarPrjEvl(idsarry.get(i));
						prjevl.setEvlSt("S01");
						quotService.updatePrjEvl(prjevl);
						quotService.startPriceProcess(getUserObject(),idsarry.get(i));
					}
				}

			}
			
			KstarQuot quot = quotService.getKstarQuot(qid);
			quot.setProReviewStatus("S01");
			quotService.updateQuot(quot);
			
			return sendSuccessMessage();
		}

 
		
		@ResponseBody
		@RequestMapping(value = "/finQuotPrjAdtProcess", method = RequestMethod.POST)
		public String finQuotPrjAdtProcess(String ids,String qid,HttpServletRequest request) throws Exception{
			
			KstarQuot quot = quotService.getKstarQuot(qid);
			quot.setProReviewStatus("S02");
			quotService.updateQuot(quot);
			
			return sendSuccessMessage();
		}
		
		
		
		@ResponseBody
		@RequestMapping(value = "/startpriceProcess", method = RequestMethod.POST)
		public String startpriceProcess(String qid,HttpServletRequest request) throws Exception{
		
//			KstarPrjEvl prjevl = new KstarPrjEvl();
//			prjevl.setQuotCode(qid);
//			prjevl.setCType("0003");
//			prjevl.setSeqno(1);
//			prjevl.setEvlTyp("E05");
//			prjevl.setEvlSt("S01");
//			prjevl.setEvlMm(getUserObject().getEmployee().getName());
//			prjevl.setSbmDt(new Date());
//			
//			
//			baseDao.save(prjevl);
			
			KstarQuot quot = quotService.getKstarQuot(qid);
			quot.setPrcAdtstatus("S01");
			quotService.updateQuot(quot);
			
			quotService.startquotpriceProcess(getUserObject(), qid);
			
			return sendSuccessMessage();
		}
		

		@ResponseBody
		@RequestMapping(value = "/startchProcess", method = RequestMethod.POST)
		public String startchProcess(String qid,HttpServletRequest request) throws Exception{
					
			KstarQuot quot = quotService.getKstarQuot(qid);
			quot.setTchAdtstatus("S01");
			quotService.updateQuot(quot);
			
			quotService.startquottchProcess(getUserObject(), qid);
			
			return sendSuccessMessage();
		}
		
		
		@ResponseBody
		@RequestMapping(value = "/startmrProcess", method = RequestMethod.POST)
		public String startmrProcess(String qid,HttpServletRequest request) throws Exception{
					
			KstarQuot quot = quotService.getKstarQuot(qid);
			quot.setTchAdtstatus("S01");
			quotService.updateQuot(quot);
			
			quotService.startquotmrProcess(getUserObject(), qid);
			
			return sendSuccessMessage();
		}
		
		
		@ResponseBody
		@RequestMapping(value = "/startbrProcess", method = RequestMethod.POST)
		public String startbrProcess(String qid,HttpServletRequest request) throws Exception{
					
			KstarQuot quot = quotService.getKstarQuot(qid);
			quot.setBidRspstatus("S01");
			quotService.updateQuot(quot);
			
			quotService.startquotbrProcess(getUserObject(), qid);
			
			return sendSuccessMessage();
		}
		
		
		@ResponseBody
		@RequestMapping(value = "/submitquotProcess", method = RequestMethod.POST)
		public String submitquotProcess(String qid,HttpServletRequest request) throws Exception{
			
			String flagPrdprc = "N";
			
			KstarQuot quot = quotService.getKstarQuot(qid);
			
			List<KstarPrjLst> KstarPrjLsts = quotService.getPrjLstPrd(qid);
			
			for(KstarPrjLst prjLst : KstarPrjLsts){
				if(prjLst.getPrdPrc()==null){
					flagPrdprc = "Y";
					throw new AnneException(prjLst.getPrdNm()+"产品未报价，请先报价后再提交!");
				}
			}
			
			if(flagPrdprc.equals("N")){
				quot.setIfsubmitted("Y");
				quotService.updateQuot(quot);
			}
			
			return sendSuccessMessage();
		}
		
		
		
		
		
		@ResponseBody
		@RequestMapping(value = "/startbiddocProcess", method = RequestMethod.POST)
		public String startbiddocProcess(String qid,HttpServletRequest request) throws Exception{
			//流程合并，更新技术评审状态
			KstarQuot quot = quotService.getKstarQuot(qid);
			quot.setTchAdtstatus("S01");
			quotService.updateQuot(quot);
			quotService.startBiddcAdtProcess(getUserObject(), qid);			
			return sendSuccessMessage();
		}
		
		
		@ResponseBody
		@RequestMapping(value = "/starsubmitProcess", method = RequestMethod.POST)
		public String starsubmitProcess(String qid,HttpServletRequest request) throws Exception{
			quotService.startsubmitProcess(getUserObject(), qid);			
			return sendSuccessMessage();
		}
		
		
		
		@ResponseBody
		@RequestMapping(value = "/startSpProcess", method = RequestMethod.POST)
		public String startSpProcess(String qid,HttpServletRequest request) throws Exception{
			quotService.startSpProcess(getUserObject(), qid);			
			return sendSuccessMessage();
		}
		

		@NoRight
		@ResponseBody
		@RequestMapping(value = "/biddcevlPage")
		public String page(PageCondition condition, HttpServletRequest request) {
			ActionUtil.requestToCondition(condition, request); 
			condition.setCondition("qid", request.getParameter("qid")); 
			String qid = condition.getStringCondition("qid");  
			condition.getFilterObject().addCondition("qid", "eq", qid);
			String ctype = "0003";
			
			//KstarBiddcevl biddcevl = quotService.getBiddcevl(qid, ctype);
			List<HistoryActivityInstance> list = null ;
			IPage p = null;
			
			
			UserObject user = getUserObject();
			//Map<String,Object> varmap = new HashMap<>();
			
			Map<String,String> varmap = new HashMap<>();
			varmap.put("businessKey", qid);
			
			
//			if(biddcevl!=null){
//				
//				String processid = biddcevl.getSnPnt();
//				
				//list = quotService.getBiddcevlReviewHisLst(processid, qid);
				
				//ProcessInstance pi = processService.get(processid);
				
				
				
				
//				if(qid!=null){
//					p = historyService.history(user.getParticipant(), varmap, condition.getRows(),condition.getPage());
//				}
				
				
				
//				if(pi!=null){
//					list = historyService.findHistoryActivityInstance(pi.getId());
//				}
				
//			}
			

//			p = historyService.history(user.getParticipant(), varmap, condition.getRows(),condition.getPage());
			
			p = historyService.findProcessInstance(varmap,condition.getRows(),condition.getPage());
			
			//List<HistoryActivityInstance> list = quotService.getContrFinReviewHisLst("标书审核流程", qid);
			
			
			//PageImpl p  = new PageImpl(list, 1,30,0); 
			
			
			return sendSuccessMessage(p);
		}
		
		
		@RequestMapping(value = "/historyview")
		public String getHistoryView(String qid,String processname,Model model) throws Exception{
			String module = lovMemberService.getFlowCodeByAppCode(this.QUOT_BIDADT);
			
			String ctype = "0003";
//			KstarBiddcevl biddcevl = quotService.getBiddcevl(qid, ctype);
			String processid = null;
//			if(biddcevl!=null){
//				
//				processid = biddcevl.getSnPnt();
//				history(model, processid);
//				
//			}else
			
			if(processname.equals("报价_特价审批流程")){
				
				String processName = "特价审批流程";
				
				List<KstarProductWorkFlow> wfs;
				
				wfs = quotService.getKstarProductWorkFlowList(qid, processName);
				
				if(wfs.size()>0){
					module = lovMemberService.getFlowCodeByAppCode(this.QUOT_SPPROC);
					for(KstarProductWorkFlow wf: wfs){
						
						processid = wf.getProcessID();
//						history(model, processid);
						
					}
				}
			}

					
			if(processname.equals("报价管理_价格评审工作流")){
				
				String processName1 = "报价价格工程评审流程";
				
				List<KstarProductWorkFlow> wfs1;
				
				wfs1 = quotService.getKstarProductWorkFlowList(qid, processName1);
				
				if(wfs1.size()>0){
					
					module = lovMemberService.getFlowCodeByAppCode(this.QUOT_SBMROC);
					for(KstarProductWorkFlow wf1: wfs1){
						
						processid = wf1.getProcessID();
//						history(model, processid);
						
					}
					
				}
			}
			
			
			
			if(processname.equals("报价管理_技术评审工作流")){
				
				String processName1 = "报价技术评审流程";
				
				List<KstarProductWorkFlow> wfs1;
				
				wfs1 = quotService.getKstarProductWorkFlowList(qid, processName1);
				
				if(wfs1.size()>0){
					
					module = lovMemberService.getFlowCodeByAppCode(this.QUOT_PRESALE);
					for(KstarProductWorkFlow wf1: wfs1){
						
						processid = wf1.getProcessID();
		//				history(model, processid);
						
					}
					
				}
			}

			
			if(processname.equals("报价管理_标书审核工作流")){
				
				String processName1 = "报价标书评审流程 ";
				
				List<KstarProductWorkFlow> wfs1;
				
				wfs1 = quotService.getKstarProductWorkFlowList(qid, processName1);
				
				if(wfs1.size()>0){
					
					module = lovMemberService.getFlowCodeByAppCode(this.QUOT_BIDADT);
					for(KstarProductWorkFlow wf1: wfs1){
						
						processid = wf1.getProcessID();
//						history(model, processid);
						
					}
					
				}
			}
			
			if(processname.equals("报价管理_备料申请工作流")){
				
				String processName1 = "报价备料申请流程";
				
				List<KstarProductWorkFlow> wfs1;
				
				wfs1 = quotService.getKstarProductWorkFlowList(qid, processName1);
				
				if(wfs1.size()>0){
					
					module = lovMemberService.getFlowCodeByAppCode(this.QUOT_MRPROC);
					for(KstarProductWorkFlow wf1: wfs1){
						
						processid = wf1.getProcessID();
//						history(model, processid);
						
					}
					
				}
			}
			
			
			if(processname.equals("报价管理_投标反馈工作流")){
				
				String processName1 = "报价投标反馈流程";
				
				List<KstarProductWorkFlow> wfs1;
				
				wfs1 = quotService.getKstarProductWorkFlowList(qid, processName1);
				
				if(wfs1.size()>0){
					
					module = lovMemberService.getFlowCodeByAppCode(this.QUOT_BRPROC);
					for(KstarProductWorkFlow wf1: wfs1){
						
						processid = wf1.getProcessID();
//						history(model, processid);
						
					}
					
				}
			}
			
			
			return redirect("/flow/flowHistory.html?id="+processid);		
		}
		
		
		
		@RequestMapping(value = "/prjhistoryview")
		public String getprjHistoryView(String prjid,Model model) throws Exception{
			
			String module;

			KstarPrjEvl prjevl = quotService.getKstarPrjEvl(prjid);
			
			if(prjevl!=null){
			
				String processid = prjevl.getEvlRs();
				
				if(prjevl.getEvlTyp().equals("E01")){
					 module = lovMemberService.getFlowCodeByAppCode(this.QUOT_PRESALE);
				} 
				
				if(prjevl.getEvlTyp().equals("E02")){
					 module = lovMemberService.getFlowCodeByAppCode(this.QUOT_AFTSALE);
				} 
				
				if(prjevl.getEvlTyp().equals("E03")){
					 module = lovMemberService.getFlowCodeByAppCode(this.QUOT_BUSPRC);
				} 
				
				if(prjevl.getEvlTyp().equals("E04")){
					 module = lovMemberService.getFlowCodeByAppCode(this.QUOT_DECPRC);
				} 
				
				if(prjevl.getEvlTyp().equals("E05")){
					 module = lovMemberService.getFlowCodeByAppCode(this.QUOT_PRCPRC);
				} 
				
				history(model, processid);
				
			}
			
			return forward("shistoryView");		
		}
		
		
		
		@RequestMapping(value = "/historyrows")
		public String getHistoryrows(String qid,Model model) throws Exception{
			String module = this.QUOT_BIDADT;
			
			String ctype = "0003";
			KstarBiddcevl biddcevl = quotService.getBiddcevl(qid, ctype);
			
			if(biddcevl!=null){
				
				String processid = biddcevl.getSnPnt();
				history(model, processid);
				
			}else{

				String processName = "特价审批流程";
				
				List<KstarProductWorkFlow> wfs;
				
				wfs = quotService.getKstarProductWorkFlowList(qid, processName);
				
				if(wfs.size()>0){
					module = lovMemberService.getFlowCodeByAppCode(this.QUOT_SPPROC);
					for(KstarProductWorkFlow wf: wfs){
						
						String processid = wf.getProcessID();
						history(model, processid);
						
					}
					
				}else{
					
					String processName1 = "报价工程评审提交流程";
					
					List<KstarProductWorkFlow> wfs1;
					
					wfs1 = quotService.getKstarProductWorkFlowList(qid, processName1);
					
					if(wfs1.size()>0){
						
						module = lovMemberService.getFlowCodeByAppCode(this.QUOT_SBMROC);
						for(KstarProductWorkFlow wf1: wfs1){
							
							String processid = wf1.getProcessID();
							history(model, processid);
							
						}
						
					}
					
				}
				
			}
			
			return forward("../flowHistory");		
		}
		
		
		
		@RequestMapping(value = "/prjhistoryrows")
		public String getprjHistoryrows(String prjid,Model model) throws Exception{
			
			String module;

			KstarPrjEvl prjevl = quotService.getKstarPrjEvl(prjid);
			
			if(prjevl!=null){
			
				String processid = prjevl.getEvlRs();
				
				if(prjevl.getEvlTyp().equals("E01")){
					 module = lovMemberService.getFlowCodeByAppCode(this.QUOT_PRESALE);
				} 
				
				if(prjevl.getEvlTyp().equals("E02")){
					 module = lovMemberService.getFlowCodeByAppCode(this.QUOT_AFTSALE);
				} 
				
				if(prjevl.getEvlTyp().equals("E03")){
					 module = lovMemberService.getFlowCodeByAppCode(this.QUOT_BUSPRC);
				} 
				
				if(prjevl.getEvlTyp().equals("E04")){
					 module = lovMemberService.getFlowCodeByAppCode(this.QUOT_DECPRC);
				} 
				
				if(prjevl.getEvlTyp().equals("E05")){
					 module = lovMemberService.getFlowCodeByAppCode(this.QUOT_PRCPRC);
				} 
				
				history(model, processid);
				
			}
			
			return forward("../flowHistory");		
		}
		
		
		
		
 
		@RequestMapping("/process")
		public String process(String id,String taskId,Model model) throws Exception {
			Task task = taskService.getTask(taskId);
			ProcessInstance pi = processService.get(task.getProcessInstanceId());
			Map<String,String> map = processService.getBusinessVariable(pi.getId());
			model.addAttribute("title",map.get("title"));
			model.addAttribute("processID",pi.getId());
			
			//***********************common**************************************
			getHistory(taskId, model);
			//***********************common**************************************
			return forward("process");
		}
		
		
		@ResponseBody
		@RequestMapping(value="/process",method=RequestMethod.POST)
		public String process(HttpServletRequest request,Model model){
			UserObject user = getUserObject();;
			boolean flag = process(request,user);
			if(flag){
				quotService.endQuotPrjAdtProcess(request.getParameter("processID"));
			}
			
			return sendSuccessMessage();
		}
 
	
		
		
		public String Checkversion(String qid,String typ){
			String result = "Y";
			KstarQuot quot = quotService.getKstarQuot(qid);
			
			if(quot !=null){
				if(Integer.parseInt(quot.getQuotVersion())<2){
					result = "N";
				}
			}
			
			return result;
		}
		
		
		public String Checkevlstatus(String qid,String typ){
			String result = quotService.Checkprjevlstatus(qid);
			return result;
		}
		
		public String Checkevlststatus(String qid,String typ){
			String result = quotService.Checkprjevlststatus(qid);
			return result;
		}
		
		
		public String CheckSprvmrk(KstarQuot quot){
			String qid = quot.getId();
			String result = quotService.CheckSprvmrkStatus(qid);
			return result;
		}
		
		
		@NoRight
		@RequestMapping("/processtabs")
		public String tabs(String id,String typ,Model model) {
			
			String applyPrcStatus="";
			
			//提交合同申请之前校验
			PageCondition condition = new PageCondition();
			condition.setCondition("qid", id);
			condition.setCondition("typ", typ);
			IPage p = quotService.queryPrjLst(condition);
			List<KstarPrjLst> kstarPrjLsts = (List<KstarPrjLst>) p.getList();
			KstarQuot kstarQuot = quotService.getKstarQuot(id);

			boolean needRebateReview = false;

			if(kstarPrjLsts.size()>0){
				for (KstarPrjLst kstarPrjLst : kstarPrjLsts) {
					if(kstarPrjLst.getApplyPrc()!=null&&kstarPrjLst.getGoldPrc()!=null){
						if((kstarPrjLst.getApplyPrc()>kstarPrjLst.getGoldPrc())){
							applyPrcStatus = "1";
							model.addAttribute(applyPrcStatus,"1");
						}else{
							applyPrcStatus = "2";
							model.addAttribute(applyPrcStatus,"2");
						}
					}else{
						model.addAttribute("status","3");
					}
					if(kstarPrjLst.getApplyPrc()!=null && kstarPrjLst.getGoldPrc()!=null){
						if("P02".equals(kstarQuot.getSpAuditStatus())){
							model.addAttribute("status","3");
						}else if(kstarPrjLst.getApplyPrc()<kstarPrjLst.getGoldPrc()){
							model.addAttribute("status","1");
							needRebateReview = true;
						}else{
							model.addAttribute("status","3");
						}
					}
				}
			}else{
				model.addAttribute("status","2");
				applyPrcStatus = "2";
				model.addAttribute(applyPrcStatus,"2");
			}

			if (needRebateReview) {
				model.addAttribute("status", "1");
			}
			
			KstarPrjEvl prjevl = quotService.getKstarPrjEvl(id);
			
			if (prjevl!= null) {
				id = prjevl.getQuotCode();
			}
			
			typ = "0003";
			
			model.addAttribute("qid",id);
			model.addAttribute("typ",typ);
					
			model.addAttribute("org",JSON.toJSONString(lovMemberService.get(getUserObject().getOrg().getId())));
			
			String checkver = Checkversion(id, typ);
			String ckevlstatus = Checkevlstatus(id, typ);
			//未启动状态
			String ckevlststatus = Checkevlststatus(id, typ);
			String pricetableid = "";

			KstarQuot quot = quotService.getKstarQuot(id);
			
			if(quot==null){
				throw new AnneException("没有找到数据");
			}
			model.addAttribute("quotInf",quot);

			String processpage = "Y";
			
			if(quot.getProReviewStatus().equals("S04")){
				processpage = "N";
			}
			
			if(quot.getBidAuditStatus().equals("B04")){
				processpage = "N";
			}
			
			//特价审批状态-审批中 
			if(quot.getSpAuditStatus().equals("P01")){
				processpage = "P13";
			}
			
			
			model.addAttribute("processpage",processpage);
			
			
			
			String bidstatus = "";
			
			if(quot.getBidAuditStatus().equals("B01")){
				bidstatus = "bid";
			}
			
			model.addAttribute("bidstatus",bidstatus);
			
			
			
			String orgid =  lovMemberService.get(getUserObject().getOrg().getId()).getId();
			
			quot.setSalDep(orgid);
			
			
			if(quot.getPriceListid()!=null){
				
				ProductPriceHead pricehead = priceheadservice.queryLpcById(quot.getPriceListid());
				
				if(pricehead.getCurrency()!=null){
					quot.setCurrency(pricehead.getCurrency());
				}
				
				pricetableid = quot.getPriceListid();
			}
			
			model.addAttribute("pricetableid",pricetableid);
			
			//投标结果反馈后，不能再“修订”报价单
			String ifbidresult = "Y";
			if(quot.getBidResults()!=null){
				ifbidresult = "N";
			}
			
			model.addAttribute("ifbidresult",ifbidresult);
			
			//提交报价标志
			String ifsubmitted = "N";
			if(quot.getIfsubmitted()!=null){
				if(quot.getIfsubmitted().equals("Y")){
					ifsubmitted = "Y";
				}
			}
			
			model.addAttribute("ifsubmitted",ifsubmitted);
			
			//工程评审完成后，报价头信息（除“投标结果”字段）与工程清单、工程界面、工程评审信息只读，不允许新增、修改、删除
			String ifProReviewStatus = "N";
			if(quot.getProReviewStatus().equals("S02")){
				ifProReviewStatus = "Y";
			}
			model.addAttribute("ifProReviewStatus",ifProReviewStatus);
			
			
			//更新特价审批标志
//			quotService.updateSprvmrk(getUserObject(), quot);
			
			String sprvmrkstatus = CheckSprvmrk(quot);

			model.addAttribute("sprvmrkstatus",sprvmrkstatus);

			
			//检查提交报价标志
			String quotsubmitstatus = quotService.CheckQuotsubmitStatus(quot.getId());
			
			model.addAttribute("quotsubmitstatus",quotsubmitstatus);
			
			
			//申请合同时，若该报价单已关联合同，且合同的状态不为“已废弃”时，弹出报错提示：
			//当前报价单已申请过合同，若需要修改报价信息请将合同：XXX(合同编码)废弃后再申请新合同
			String contractStatus = quotService.CheckContractStatus(quot.getId());
			String cntId = "";
			
			if(contractStatus.equals("Y")){
				cntId = quotService.getContractID(quot.getId());
			}
			
			//cntId= "zzz";
			
			model.addAttribute("contractStatus",contractStatus);
			model.addAttribute("cntId",cntId);
			
			
			
			String applycont = "Y";
			
			//业务单元=”国内数据中心“ and 投标项目=”是“ and 技术评审状态 <>"已审批" and 标书审核状态<>"已审批" 
			
			if(quot.getMarketDeptName().equals("国内数据中心")){
				if(quot.getIsBidPro().equals("1")){
					if(!quot.getTchAdtstatus().equals("S02")||!quot.getBidAuditStatus().equals("B02")){
						applycont = "N";
					}
				}
			}
			
			
			model.addAttribute("applycont",applycont);
			
			
			
			TabMain tabMainbgn = new TabMain();
			tabMainbgn.setInitAll(false);
			tabMainbgn.addTab("工程清单", "/quot/prjLst.html?qid="+id+"&typ="+typ+"&pricetableid="+pricetableid+"&sprvmrkstatus="+sprvmrkstatus+"&quotsubmitstatus="+quotsubmitstatus+"&processpage="+processpage+"&ifProReviewStatus="+ifProReviewStatus+"&applyPrcStatus="+applyPrcStatus);
			tabMainbgn.addTab("工程界面", "/quot/prjpages.html?qid="+id+"&typ="+typ+"&processpage="+processpage+"&ifProReviewStatus="+ifProReviewStatus);
			//tabMainbgn.addTab("附件", "/quot/att.html?qid="+qid+"&typ="+typ);
			//tabMainbgn.addTab("团队成员", "/quot/mem.html?qid="+qid+"&typ="+typ);
			tabMainbgn.addTab("附件", "/common/attachment/attachment.html?businessId="+id+"&businessType=QUOTE_FILE&docGroupCode=quotatttyp");
			tabMainbgn.addTab("团队成员", "/team/list.html?businessType=QUOTINF"+"&businessId="+id);
			tabMainbgn.addTab("组织列表", "/orgTeam/list.html?businessType=QUOTINF&businessId="+id);
			//tabMainbgn.addTab("工程评审", "/quot/prjevl.html?qid="+id+"&typ="+typ+"&checkver="+checkver+"&ckevlstatus="+ckevlstatus+"&ckevlststatus="+ckevlststatus+"&processpage="+processpage+"&ifProReviewStatus="+ifProReviewStatus);
			tabMainbgn.addTab("审批历史", "/quot/biddcevl.html?qid="+id+"&typ="+typ);
			tabMainbgn.addTab("合同", "/quot/contract.html?qid="+id+"&typ="+typ);
			
			
			model.addAttribute("tabMainbgn",tabMainbgn);

			//权限字段
			quot.setUpdatedAt(new Date());
			quot.setUpdatedById(getUserObject().getEmployee().getId());

			
			quotService.updateQuot(quot);
			
//			//add new lovmem root
//			String groupId = "PRJLSTPRDCAT";
//			String rootexists = quotService.Checklovroot(id);
//			
//			if(rootexists.equals("Y")){
//				String lovmenid = quotService.addlovroot(id, typ, groupId);
//			}
			
			return forward("tabs");
		}
		

		@NoRight
		@ResponseBody
		@RequestMapping(value="/processtabs",method=RequestMethod.POST)
		public String editTabs(KstarQuot quot){
			
			if(quot.getBidResults()!=null){
				if(quot.getIsBidPro()!=null){
					if(quot.getIsBidPro().equals("1")){
						if(quot.getBidResults().equals("R01")){
							quot.setStatus("S02");
						}
						
						if(quot.getBidResults().equals("R02")){
							quot.setStatus("S03");
						}
					}
				}
			}
			
			quotService.updateQuot(quot);
			return sendSuccessMessage();
		}
		
		
		
	 
}
  
