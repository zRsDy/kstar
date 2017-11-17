package com.ibm.kstar.action.product.maintain;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.utils.ActionUtil;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.action.common.IConstants;
import com.ibm.kstar.api.product.IProductService;
import com.ibm.kstar.api.product.maintain.IProdMaintainService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.product.KstarProduct;
import com.ibm.kstar.entity.product.maintain.ProdAttrModLine;
import com.ibm.kstar.entity.product.maintain.ProdInfoMaintain;
import com.ibm.kstar.interceptor.system.permission.NoRight;

/**
 * 产品信息维护
 * @author liumq
 * 2017-10-19
 *
 */

@Controller
@RequestMapping("/product/maintain")
public class ProdMaintainAction extends BaseAction {
	
	@Autowired
	IProdMaintainService prodMaintainService;
	
	@Autowired
	IProductService productService;
	
	@Autowired
	ILovMemberService lovMemberService;
	
	@RequestMapping("/list")
	public String list(String id, Model model) {
		return forward("list");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		
		/*String searchKey = condition.getStringCondition("searchKey");
		if(searchKey !=null){
			condition.getFilterObject().addOrCondition("handoverComment", "like", "%"+searchKey+"%");
		}*/
		condition.getFilterObject().addCondition("status", "eq", "1000");
		IPage p = prodMaintainService.getProdMaintainPage(condition);
		
		System.out.println("==================查询完毕==============");
		
		return sendSuccessMessage(p);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/attrModLinePage")
	public String attrModLinePage(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		IPage p = prodMaintainService.getProdAttrModLinePage(condition);
		return sendSuccessMessage(p);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/catalogModLinePage")
	public String catalogModLinePage(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		IPage p = prodMaintainService.getProdCatalogModLinePage(condition);
		return sendSuccessMessage(p);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/saleStatusModLinePage")
	public String saleStatusModLinePage(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		IPage p = prodMaintainService.getSaleStatusModLinePage(condition);
		return sendSuccessMessage(p);
	}
	
	@RequestMapping("/add")
	public String add(Model model,HttpServletRequest request) {
		UserObject userObject = this.getUserObject();
		
		ProdInfoMaintain prodInfoMaintain = new ProdInfoMaintain();
		prodInfoMaintain.setMaintainCode(prodMaintainService.getMaintainCode());
		prodInfoMaintain.setApplicantId(userObject.getEmployee().getId());
		prodInfoMaintain.setApplicantDept(userObject.getOrg().getId());
		
		prodInfoMaintain.setCreatedAt(new Date());
		prodInfoMaintain.setUpdatedAt(new Date());
		prodInfoMaintain.setCreatedById(userObject.getEmployee().getId());
		prodInfoMaintain.setUpdatedById(userObject.getEmployee().getId());
		prodInfoMaintain.setCreatedPosId(userObject.getPosition().getId());
		prodInfoMaintain.setCreatedOrgId(userObject.getOrg().getId());
		prodInfoMaintain.setProcessStatus(IConstants.PROD_INFO_MAINTAIN_PROC_STUTAS_10);
		
		model.addAttribute("prodInfoMaintain",prodInfoMaintain);
		model.addAttribute("canProcess",false);
		model.addAttribute("canModify",true);
		
		TabMain tabMain = new TabMain();
		tabMain.setInitAll(true);
		
		tabMain.addTab("产品属性变更明细", "/product/maintain/attrModeLineList.html?maintainId=&canModify="+true);
		tabMain.addTab("产品归属目录变更明细", "/product/maintain/catalogModLineList.html?maintainId=&canModify="+true);
		tabMain.addTab("产品销售状态变更明细", "/product/maintain/saleStatusModLineList.html?maintainId=&canModify="+true);
		
		model.addAttribute("tabMain", tabMain);
		return forward("add");
	}
	
	@ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(ProdInfoMaintain prodInfoMaintain, HttpServletRequest request) {
		//TODO:产品维护申请保存
		try {
			prodMaintainService.saveProdMaintain(prodInfoMaintain,this.getUserObject());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return sendErrorMessage("保存产品信息维护申请单出错："+e.getMessage());
		}
		
        return sendSuccessMessage(prodInfoMaintain);
    }
	
	@RequestMapping("/edit")
	public String edit(Model model,HttpServletRequest request) {
		String maintainId = request.getParameter("id");
		
		ProdInfoMaintain prodInfoMaintain = prodMaintainService.getProdInfoMaintain(maintainId);
				
		model.addAttribute("prodInfoMaintain",prodInfoMaintain);
		
		boolean canModify = true;
		boolean canProcess = true;
		if(IConstants.PROD_INFO_MAINTAIN_PROC_STUTAS_20.equals(prodInfoMaintain.getProcessStatus())){
			canModify = false;
			canProcess = false;
		}
		model.addAttribute("canModify",canModify);
		model.addAttribute("canProcess",canProcess);
		
		TabMain tabMain = new TabMain();
		tabMain.setInitAll(true);
		
		tabMain.addTab("产品属性变更明细", "/product/maintain/attrModeLineList.html?maintainId="+maintainId+"&canModify="+canModify);
		tabMain.addTab("产品归属目录变更明细", "/product/maintain/catalogModLineList.html?maintainId="+maintainId+"&canModify="+canModify);
		tabMain.addTab("产品销售状态变更明细", "/product/maintain/saleStatusModLineList.html?maintainId="+maintainId+"&canModify="+canModify);
		String url = "/common/attachment/attachment.html?businessId="+prodInfoMaintain.getId()+"&businessType=ProdInfoMaintain&docGroupCode=PMTypeCode";
		if(!canModify){
			url = url +"&unableAdd=true&unableDelete=true";
		}
		tabMain.addTab("附件", url);
		tabMain.addTab("团队成员", "/team/list.html?businessType="+IConstants.PROD_INFO_MAINTAIN+"&businessId=" + prodInfoMaintain.getId());
		tabMain.addTab("审批历史", "/standard/history.html?contrId="+prodInfoMaintain.getId());
		
		model.addAttribute("tabMain", tabMain);
		return forward("add");
	}
	
	@ResponseBody
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String edit(ProdInfoMaintain prodInfoMaintain, HttpServletRequest request) {
		//TODO:产品维护申请保存
		try {
			prodMaintainService.updateProdMaintain(prodInfoMaintain,this.getUserObject());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return sendErrorMessage("保存产品信息维护申请单出错："+e.getMessage());
		}
		
        return sendSuccessMessage(prodInfoMaintain);
    }
	
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(String id) {
		prodMaintainService.deleteProdInfoMaintain(id,this.getUserObject());
		return sendSuccessMessage("操作成功");
	}
	
	@NoRight
	@RequestMapping("/process")
	public String process(Model model,HttpServletRequest request) {
		String maintainId = request.getParameter("id");
		
		ProdInfoMaintain prodInfoMaintain = prodMaintainService.getProdInfoMaintain(maintainId);
				
		model.addAttribute("prodInfoMaintain",prodInfoMaintain);
		
		TabMain tabMain = new TabMain();
		tabMain.setInitAll(true);
		
		tabMain.addTab("产品属性变更明细", "/product/maintain/attrModeLineList.html?maintainId="+maintainId);
		tabMain.addTab("产品归属目录变更明细", "/product/maintain/catalogModLineList.html?maintainId="+maintainId);
		tabMain.addTab("产品销售状态变更明细", "/product/maintain/saleStatusModLineList.html?maintainId="+maintainId);
		tabMain.addTab("附件", "/common/attachment/attachment.html?businessId="+prodInfoMaintain.getId()
					+ "&businessType=ProdInfoMaintain&docGroupCode=PMTypeCode&unableAdd=true&unableDelete=true");
		
		model.addAttribute("tabMain", tabMain);
		return forward("process");
	}
	
	@NoRight
	@RequestMapping("/attrModeLineList")
	public String attrModeLineList(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute("maintainId", request.getParameter("maintainId"));
		model.addAttribute("canModify", request.getParameter("canModify"));
		//List<ProdAttrModLine> attrModLines = prodMaintainService.getProdAttrModLineList(request.getParameter("maintainId"));
		//model.addAttribute("attrModLines", JSON.toJSONString(attrModLines));
		return forward("attr_mod_line_list");
	}
	
	@NoRight
	@RequestMapping("/catalogModLineList")
	public String catalogModLineList(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute("maintainId", request.getParameter("maintainId"));
		model.addAttribute("canModify", request.getParameter("canModify"));
		return forward("catalog_mod_line_list");
	}
	
	@NoRight
	@RequestMapping("/saleStatusModLineList")
	public String saleStatusModLineList(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute("maintainId", request.getParameter("maintainId"));
		model.addAttribute("canModify", request.getParameter("canModify"));
		return forward("sale_status_mod_line_list");
	}
	
	@NoRight
	@RequestMapping("/getProdAttrValue")
	public String getProdAttrValue(Model model,HttpServletRequest request){
		String materCode = request.getParameter("materCode");
		String attrId = request.getParameter("attrId");
		
		LovMember member = lovMemberService.get(attrId);
		
		KstarProduct product = productService.queryByMaterCode(materCode);
		
		String attrValue = "";
		
		if(member != null && product != null){
			List<Field> fields = new ArrayList<>();
			
			getAllFields(KstarProduct.class, fields);
			
			for(Field f : fields){
				Column column = f.getAnnotation(Column.class);
				if(column != null){
					String colName = column.name().toLowerCase();
					if(colName.equals(member.getOptTxt1().toLowerCase())){
						try {
							f.setAccessible(true);
							attrValue = f.get(product).toString();
							break;
						} catch (IllegalArgumentException | IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}finally{
							f.setAccessible(false);
						}
					}
				}
			}
		}
		return sendSuccessMessage(attrValue);
	}
	
	private void getAllFields(Class<?> clazz, List<Field> fields){
		Field[] fs = clazz.getDeclaredFields();
		Collections.addAll(fields, fs);
		if(clazz.getSuperclass() != null){
			getAllFields(clazz.getSuperclass(), fields);
		}
	}
	
	@NoRight
	@RequestMapping("/getProdSaleStatus")
	public String getProdSaleStatus(Model model,HttpServletRequest request){
		String materCode = request.getParameter("materCode");
		KstarProduct product = productService.queryByMaterCode(materCode);
		
		Map<String,Object> map = new HashMap<>();
		
		if(product != null){
			LovMember lovMember = lovMemberService.get(product.getCsaleStatus());
			if(lovMember != null){
				map.put("currentSaleStatus", lovMember.getId());
				map.put("currentSaleStatusName", lovMember.getName());
			}
		}
		
		return sendSuccessMessage(map);
	}
	
	@NoRight
	@RequestMapping("/startMaintainProcess")
	public String startMaintainProcess(ProdInfoMaintain maintain,Model model,HttpServletRequest request){
		//maintain.setProcessStatus(IConstants.PROD_INFO_MAINTAIN_PROC_STUTAS_20);
		try {
			prodMaintainService.startMaintainProcess(maintain,this.getUserObject());
		} catch (Exception e) {
			// TODO: handle exception
			return sendErrorMessage(e.getMessage());
		}
		
		return sendSuccessMessage("操作成功");
	}
}
