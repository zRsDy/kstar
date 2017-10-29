package com.ibm.kstar.action.price;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.log.LogOperate;
import org.xsnake.web.page.IPage;
import org.xsnake.web.page.PageImpl;
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.ExcelUtil;
import org.xsnake.web.utils.StringUtil;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.price.ILayerPriceCompLineService;
import com.ibm.kstar.api.price.ILayerPriceCompService;
import com.ibm.kstar.api.price.IPriceHeadService;
import com.ibm.kstar.api.price.IPriceLineService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.product.KstarProductAuTest;
import com.ibm.kstar.entity.product.PriceLayCompareHeader;
import com.ibm.kstar.entity.product.PriceLayCompareLine;
import com.ibm.kstar.entity.product.ProductPriceDiscount;
import com.ibm.kstar.entity.product.ProductPriceDiscountVO;
import com.ibm.kstar.entity.product.ProductPriceHead;
import com.ibm.kstar.entity.product.ProductPriceLine;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/proprice")
public class ProPriceAction extends BaseAction {
	
	@Autowired
	ILovMemberService lovMemberService;

	@Autowired
	ILayerPriceCompService service;

	@Autowired
	ILayerPriceCompLineService lineService;
	
	@Autowired
	IPriceHeadService pricetableService;
	
	@Autowired
	IPriceLineService priceLineService;
	
	@Autowired
	ICustomInfoService customService;

	@RequestMapping("/pricetable")
	public String publish(String id, Model model) {
		return forward("pricetable");
	}

	@RequestMapping("/layerpricecomp")
	public String layerpricecomp(String id, Model model) {
		return forward("layerpricecomp");
	}
	
	@NoRight
	@RequestMapping("/selectPriceOrg")
	public String selectPriceOrg(String pickerId,Model model){
		model.addAttribute("pickerId",pickerId);
		return forward("select_price_org");
	}
	
	@NoRight
	@RequestMapping("/multiSelectPriceOrg")
	public String multiSelectPriceOrg(String pickerId,Model model){
		model.addAttribute("pickerId",pickerId);
		return forward("multi_select_price_org");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/layerpricecompList")
	public String layerpricecompList(PageCondition condition,HttpServletRequest request) throws Exception {
		ActionUtil.requestToCondition(condition, request);
		IPage p = service.query(condition,this.getUserObject());
		return sendSuccessMessage(p);
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/lpcLineList")
	public String lpcLineList(PageCondition condition,HttpServletRequest request) throws Exception {
		ActionUtil.requestToCondition(condition, request);
		String headerId = (String) condition.getCondition("headerId");
		IPage p = new PageImpl(null, 1, 10, 0);
		if (StringUtils.isNotBlank(headerId)) {
			condition.getFilterObject().addCondition("headerId", "eq", headerId);
			p = lineService.query(condition);
		}
		return sendSuccessMessage(p);
	}

	@RequestMapping("/lpcEdit")
	public String lpcEdit(String id, Model model) {
		if (id != null) {
			PriceLayCompareHeader lpc = service.queryLpcById(id);
			LovMember lovMember = lovMemberService.get(lpc.getOrganization());
			model.addAttribute("lpc", lpc);
			model.addAttribute("lovMember", JSON.toJSONString(lovMember));
		}else{
			PriceLayCompareHeader lpc = new PriceLayCompareHeader();
			lpc.setCreateOrg(this.getUserObject().getOrg().getId());
			lpc.setCreater(this.getUserObject().getEmployee().getId());
			lpc.setCreaterName(this.getUserObject().getEmployee().getName());
			lpc.setCreateDate(new Date());
			lpc.setStartDate(new Date());
			model.addAttribute("lpc", lpc);
		}
		return forward("lpcEdit");
	}

	@ResponseBody
	@RequestMapping(value = "/lpcDelete", method = RequestMethod.POST)
	public String lpcDelete(String id) {
		service.delete(id);
		return sendSuccessMessage();
	}

	@ResponseBody
	@RequestMapping(value = "/lpcEdit", method = RequestMethod.POST)
	public String lpcEditDo(PriceLayCompareHeader lpc) {
		if(lpc.getId() == null && (new Date()).getTime()-lpc.getStartDate().getTime()>24*60*60*1000){
			throw new AnneException("有效期开始日期不能小于当前日期！");
		}
		if(lpc.getEndDate() != null){
			if(lpc.getEndDate().before(lpc.getStartDate())){
				throw new AnneException("有效期结束日期不能小于有效期开始日期！");
			}
		}
		try{
			service.save(lpc, this.getUserObject());
		}catch(Exception e){
			throw new AnneException("同一组织只能设一个价格表！");
		}
		return sendSuccessMessage();
	}

	@ResponseBody
	@RequestMapping(value = "/auTestAdd", method = RequestMethod.POST)
	public String auTestAdd(KstarProductAuTest ecr) {
		return sendSuccessMessage();
	}
	
	@RequestMapping("/lpcLineEdit")
	public String lpcLineEdit(String id, Model model,Condition condition,HttpServletRequest request) {
		model.addAttribute("id", id);
		String organization = request.getParameter("organization");
		model.addAttribute("organization", organization);
		if (id != null) {
			PriceLayCompareLine lpcl = lineService.queryLpcById(id);
			model.addAttribute("lpcl", lpcl);
		}else{
			PriceLayCompareLine lpcl = new PriceLayCompareLine();
			lpcl.setStartDate(new Date());
			condition.setCondition("orgId", organization);
			List<LovMember> list = lovMemberService.selectLpcLineName(condition);
			if(list != null && list.size() > 0){
				LovMember lovMember = list.get(0);
				lpcl.setLayLineName(lovMember.getId());
			}
			model.addAttribute("lpcl", lpcl);
		}
		return forward("lpcLineEdit");
	}

	@ResponseBody
	@RequestMapping(value = "/lpcLineEdit", method = RequestMethod.POST)
	public String lpcLineEditDo(PriceLayCompareLine lpc) {
		if(lpc.getId() == null && (new Date()).getTime()-lpc.getStartDate().getTime()>24*60*60*1000){
			throw new AnneException("有效期开始日期不能小于当前日期！");
		}
		if(lpc.getEndDate() != null){
			if(lpc.getEndDate().before(lpc.getStartDate())){
				throw new AnneException("有效期结束日期不能小于有效期开始日期！");
			}
		}
		try{
			lineService.save(lpc, this.getUserObject());
		}catch(AnneException e){
			throw e;
		}catch(Exception e){
			throw new AnneException("同一对照表下的层级名称不能重复！");
		}
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/lpcLineDelete", method = RequestMethod.POST)
	public String lpcLineDelete(String id) {
		lineService.delete(id);
		return sendSuccessMessage();
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/pricetableList")
	public String pricetableList(PageCondition condition,HttpServletRequest request) throws Exception {
		ActionUtil.requestToCondition(condition, request);
		IPage p = pricetableService.query(condition,this.getUserObject());
		return sendSuccessMessage(p);
	}

	
	@RequestMapping("/ptHeadEdit")
	public String ptHeadEdit(String id, Model model) {
		if (id != null) {
			ProductPriceHead lpc = pricetableService.queryLpcById(id);
			if(lpc.getClientId() != null){
				CustomInfo customInfo = customService.getCustomInfo(lpc.getClientId());
				model.addAttribute("customInfo", customInfo==null?null : JSON.toJSONString(customInfo));
			}
			LovMember org = lovMemberService.get(lpc.getOrganization());
			model.addAttribute("org", JSON.toJSONString(org));
			model.addAttribute("org1", org);
			model.addAttribute("lpc", lpc);
			model.addAttribute("map",getProductPriceDiscountMap(lpc.getId()));
			
			TabMain tabMain = new TabMain();
			tabMain.setInitAll(false);
			tabMain.addTab("销售团队", "/team/list.html?businessType="+ ProcessConstants.PRODUCT_PRICE_HEAD +"&businessId="+lpc.getId());
			model.addAttribute("tabMain",tabMain);
		}else{
			ProductPriceHead lpc = new ProductPriceHead();
			lpc.setCreateOrg(this.getUserObject().getOrg().getId());
			lpc.setCreater(this.getUserObject().getEmployee().getId());
			lpc.setCreaterName(this.getUserObject().getEmployee().getName());
			lpc.setCreateDate(new Date());
			lpc.setClientPrice("0");
			lpc.setIsDiscount("0");
			lpc.setIsWholesale("0");
			lpc.setCurrency(lpc.getLovMember("CURRENCY", "CNY").getId());
			lpc.setStartDate(new Date());
			model.addAttribute("lpc", lpc);
		}
		return forward("ptHeadEdit");
	}
	
	
	private Map<String, ProductPriceDiscount> getProductPriceDiscountMap(String headId){
		List<ProductPriceDiscount> list = pricetableService.getProductPriceDiscount(headId);
		Map<String, ProductPriceDiscount> map = new HashMap<>();
		for (ProductPriceDiscount discount : list) {
			map.put(discount.getType()+discount.getOrderNo(), discount);
		}
		return map;
	}
	
	

	@ResponseBody
	@RequestMapping(value = "/ptHeadEdit", method = RequestMethod.POST)
	public String ptHeadEditDo(ProductPriceHead head) {
		if("1".equals(head.getClientPrice()) && (head.getClientId() == null || "".equals(head.getClientId()))){
			throw new AnneException("当是否客户价格勾选上时，客户栏位必输！");
		}
		if(head.getId() == null && (new Date()).getTime()-head.getStartDate().getTime()>24*60*60*1000){
			throw new AnneException("有效期开始日期不能小于当前日期！");
		}
		if(head.getEndDate() != null){
			if(head.getEndDate().before(head.getStartDate())){
				throw new AnneException("有效期结束日期不能小于有效期开始日期！");
			}
		}
		pricetableService.save(head,this.getUserObject());
		return sendSuccessMessage();
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/priceLineList")
	public String priceLineList(PageCondition condition, HttpServletRequest request) throws Exception {
		ActionUtil.requestToCondition(condition, request);
		String headId = condition.getStringCondition("headId");
		IPage p = new PageImpl(null,1,10,0);
		if(StringUtil.isNotEmpty(headId)){
			p = priceLineService.query(condition);
		}
		return sendSuccessMessage(p);
	}
	
	@RequestMapping("/priceLineEdit")
	public String priceLineEdit(String id, Model model,HttpServletRequest request) {
		model.addAttribute("id", id);
		if (id != null) {
			ProductPriceLine ppl = priceLineService.queryLpcById(id);
			model.addAttribute("ppl", ppl);
		}else{
			String organization = request.getParameter("organization");
			model.addAttribute("organization", organization);
			ProductPriceLine ppl = new ProductPriceLine();
			PriceLayCompareHeader ph = service.queryLpcHeadByOrg(organization);
			if(ph != null){
				List<PriceLayCompareLine> pll = lineService.queryLpcByHeadId(ph.getId());
				if(pll != null){
					for(PriceLayCompareLine pcl:pll){
						if("01".endsWith(pcl.getLayLineNameCode())){
							ppl.setLayer1Discount(pcl.getDiscountRate());
						}else if("02".endsWith(pcl.getLayLineNameCode())){
							ppl.setLayer2Discount(pcl.getDiscountRate());
						}else if("03".endsWith(pcl.getLayLineNameCode())){
							ppl.setLayer3Discount(pcl.getDiscountRate());
						}else if("04".endsWith(pcl.getLayLineNameCode())){
							ppl.setLayer4Discount(pcl.getDiscountRate());
						}else if("05".endsWith(pcl.getLayLineNameCode())){
							ppl.setLayer5Discount(pcl.getDiscountRate());
						}else if("06".endsWith(pcl.getLayLineNameCode())){
							ppl.setLayer6Discount(pcl.getDiscountRate());
						}
					}
				}
				
			}
			ppl.setStartDate(new Date());
			model.addAttribute("ppl", ppl);
		}
		return forward("priceLineEdit");
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/ptHeadDelete", method = RequestMethod.POST)
	@LogOperate(module="市场活动或培训模块",notes="${user}提价了申请单号数据")
	public String ptHeadDelete(String id) {
		pricetableService.delete(id);
		return sendSuccessMessage();
	}
	
	
	@ResponseBody
	@RequestMapping("/priceLineDelete")
	public String priceLineDelete(String id) throws Exception {
		if(!StringUtil.isNullOrEmpty(id)){
			priceLineService.delete(id);
		}
		return sendSuccessMessage();
	}
	
	
	
	
	@ResponseBody
	@RequestMapping(value = "/priceLineEdit", method = RequestMethod.POST)
	public String priceLineEditDo(ProductPriceLine lpc) {
		if(lpc.getId() == null && (new Date()).getTime()-lpc.getStartDate().getTime()>24*60*60*1000){
			throw new AnneException("有效期开始日期不能小于当前日期！");
		}
		if(lpc.getEndDate() != null){
			if(lpc.getEndDate().before(lpc.getStartDate())){
				throw new AnneException("有效期结束日期不能小于有效期开始日期！");
			}
		}
		priceLineService.save(lpc, this.getUserObject());
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value="/priceLineImport")
	public String priceLineImport(HttpServletRequest request) {
		List<List<Object>> dataList = this.getExcelData(request);
		priceLineService.importPriceLine(dataList, this.getUserObject());
		return sendSuccessMessage();
	}
	
	@RequestMapping(value="/priceLineExport")
	public void priceLineExport(String headId,HttpServletResponse response) throws UnsupportedEncodingException{
		if(StringUtil.isEmpty(headId)){
			throw new AnneException("请选择价格表！");
		}
		List<List<Object>> dataList = priceLineService.exportPriceLine(headId);
		String fileName = new String("价格表行".getBytes("UTF-8"),"GBK");
		ExcelUtil.exportExcel(response, dataList, fileName);
	}
	
	@NoRight
	@RequestMapping(value="/priceLineExportTemplet")
	public void priceLineExportTemplet(String headId,HttpServletResponse response) throws UnsupportedEncodingException{
		List<List<Object>> dataList = priceLineService.exportPriceLineTemplet(headId);
		String fileName = new String("价格表行".getBytes("UTF-8"),"GBK");
		ExcelUtil.exportExcel(response, dataList, fileName);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/selectApproveLayLine")
	public String selectApproveLayLine(Condition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		List<LovMember> p = lineService.selectApproveLayLine(condition);
		return sendSuccessMessage(p);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/saveProductPriceLine")
	public String saveProductPriceLine(@RequestBody ProductPriceDiscountVO discountVO, HttpServletRequest request){
		
		if (StringUtil.isNullOrEmpty(discountVO.getHeadId())) {
			return sendSuccessMessage();
		}
		
		ProductPriceHead head = pricetableService.queryLpcById(discountVO.getHeadId());
		if(StringUtil.isNullOrEmpty(head.getIsWholesale()) || !"1".equals(head.getIsWholesale())){
			return sendErrorMessage("价格表非批发类型，不能添加批发产品！");
		}
		
		priceLineService.saveProductPriceLines(discountVO);
		
		return sendSuccessMessage();
	}
	
	
	
}
