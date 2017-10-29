package com.ibm.kstar.report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.datanucleus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.utils.StringUtil;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.api.report.IReportParamService;
import com.ibm.kstar.api.report.IReportService;
import com.ibm.kstar.entity.report.RankHeader;
import com.ibm.kstar.impl.report.TotalValue;

@Controller
@RequestMapping("/report")
public class ReportRankAction extends BaseAction{

	@Autowired
	IReportParamService reportParamService;
	
	@Autowired
	IReportService reportService;
	
	@RequestMapping("/rankOrg")
	public String rank(String year,String rankHeaderId,String orgId,String currency,Model model){
		if(StringUtil.isEmpty(year)){
			year = new SimpleDateFormat("yyyy").format(new Date());
		}
		if(StringUtil.isEmpty(currency)){
			currency = "RMB";
		}
		if(StringUtil.isEmpty(rankHeaderId)){
			List<RankHeader> list = reportParamService.rankList();
			if(list!=null && list.size()>0){
				rankHeaderId =list.get(0).getId(); 
			}
		}
		
		//从组织报表排名分析穿透获取排名组织ID
		if(StringUtil.isNotEmpty(orgId)){
			List<RankHeader> list = reportParamService.rankListByOrgId(orgId);
			if(list!=null && list.size()>0){
				rankHeaderId =list.get(0).getId(); 
			}
		}
		
		List<Integer> years = new ArrayList<Integer>();
		int startYear = 2016;
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		for(int i=startYear;i<=currentYear;i++){
			years.add(i);
		}
		model.addAttribute("years",years);
		model.addAttribute("year",year);
		model.addAttribute("currency",currency);
		model.addAttribute("rankHeaderId",rankHeaderId);
		
		List<TotalValue> list = reportService.getRankReport(year, rankHeaderId, currency);
		
		List<String> orgNameList = new ArrayList<String>();
		List<Double> orgValueList = new ArrayList<Double>();
		
		for(TotalValue tv : list){
			orgNameList.add(tv.getName());
			orgValueList.add(tv.getTotal());
		}
		model.addAttribute("size",list.size() * 30);
		model.addAttribute("orgNameList",JSON.toJSONString(orgNameList));
		model.addAttribute("orgValueList",JSON.toJSONString(orgValueList));
		
		return forward("rankOrg");
	}
	
	
}
