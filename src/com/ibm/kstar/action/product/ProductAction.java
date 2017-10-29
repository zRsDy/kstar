package com.ibm.kstar.action.product;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.action.BaseFlowAction;
import com.ibm.kstar.action.common.process.ProcessConstants;
import com.ibm.kstar.api.common.doc.IKstarAttachmentService;
import com.ibm.kstar.api.custom.ICustomInfoService;
import com.ibm.kstar.api.product.*;
import com.ibm.kstar.api.system.lov.ILovGroupService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.IEmployeeService;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.api.system.permission.entity.Employee;
import com.ibm.kstar.entity.common.doc.KstarAttachment;
import com.ibm.kstar.entity.custom.CustomInfo;
import com.ibm.kstar.entity.product.*;
import com.ibm.kstar.impl.channel.SerialNumberService;
import com.ibm.kstar.interceptor.system.permission.NoRight;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.page.PageImpl;
import org.xsnake.web.ui.TabMain;
import org.xsnake.web.upload.IUploadFile;
import org.xsnake.web.upload.UploadUtils;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/product")
public class ProductAction extends BaseFlowAction {

	@Autowired
	IProductService productService;
	
	@Autowired
	protected ILovMemberService lovMemberService;
	
	@Autowired
	IProLineService lineService;
	
	@Autowired
	IEcrService ecrService;

	@Autowired
	IDocService docService;
	
	@Autowired
	IPriceService priceService;
	
	@Autowired
	IClientService clientService;
	
	@Autowired
	IDemandService demandService;
	
	@Autowired
	IProSubService proSubService;
	
	@Autowired
	IAuTestService auTestService;
	
	@Autowired
	IProductSerialService productSerialService;
	
	@Autowired
	IEmployeeService employeeService;
	
	@Autowired
	IProductProcesService productProcess;
	
	@Autowired
	SerialNumberService serialNumberService;
	
	@Autowired
	ICustomInfoService customService;

	@Autowired
	IKstarAttachmentService attachmentService;
	@Autowired
	IProLovService proLovService;

    @Autowired
    private ILovGroupService lovGroupService;

	@RequestMapping("/index")
	public String index(String id,Model model){
		outQueryCondition(model);
		model.addAttribute("type","ChanPinLieBiao");
		return forward("index");
	}
	
	@RequestMapping("/index2")
	public String index2(String id,Model model){
		outQueryCondition(model);
		model.addAttribute("type","FeiBiaoWaiGouShenQing");
		return forward("index2");
	}
	
	@RequestMapping("/productDemand")
	public String seriesDemand(String id,Model model){
		outQueryCondition(model);
		model.addAttribute("LOGIN_USER_ID",this.getUserObject().getEmployee().getId());
		return forward("productDemand");
	}
	
	@NoRight
	@RequestMapping("/demandSelectProducts")
	public String demandSelectProducts(String pickerId,String demandId, Model model){
		outQueryCondition(model);
		model.addAttribute("pickerId",pickerId);
		model.addAttribute("demandId",demandId);
		return forward("demandSelectProduct");
	}
	
	@NoRight
	@RequestMapping("/exportDemandTemplate")
	public String exportDemandTemplate(Model model){
		return forward("demandTemplateList");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value = "/demandSelectProducts", method = RequestMethod.POST)
	public String demandSelectProducts(@RequestParam(value = "ids[]")String[] ids, String demandId) {
		demandService.demandSelectProducts(ids,demandId,this.getUserObject());
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value = "/deleteDemandProducts", method = RequestMethod.POST)
	public String deleteDetails(@RequestParam(value = "ids[]")String[] ids) {
		demandService.deleteDemandProducts(ids);
		return sendSuccessMessage();
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value = "/submitColumnValue", method = RequestMethod.POST)
	public String submitColumnValue(String relationId, String column, String value) {
		demandService.submitDemandNumber(relationId,column,value,this.getUserObject());
		return sendSuccessMessage();
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/queryDemandProducts")
	public String queryDemandProducts(PageCondition condition,HttpServletRequest request) throws Exception{
		ActionUtil.requestToCondition(condition, request);
		String demandId = condition.getStringCondition("demandId");
		IPage p = new PageImpl(null, condition.getPage(), condition.getRows(), 0);
		if(StringUtil.isNotEmpty(demandId)){
			p = productService.queryDemandProducts(condition);
		}
		return sendSuccessMessage(p);
	}
	
	@RequestMapping("/catalog")
	public String catalog(String id,Model model){
		outQueryCondition(model);
		model.addAttribute("type", "XiaoShouMuLu");
		return forward("catalog");
	}
	
	@RequestMapping("/proLibCatalog")
	public String proLibCatalog(String id,Model model){
		outQueryCondition(model);
		return forward("proLibCatalog");
	}
	
	@RequestMapping("/catalogQuery")
	public String catalogQuery(String id,Model model){
		outQueryCondition(model);
		return forward("catalogQuery");
	}
	
	@RequestMapping("/catalogMatch")
	public String catalogMatch(String id,Model model){
		return forward("catalogMatch");
	}
	
	@RequestMapping("/proLibCatalogMatch")
	public String proLibCatalogMatch(String id,Model model){
		return forward("proLibCatalogMatch");
	}
	
	@RequestMapping("/publish")
	public String publish(String id,Model model){
		outQueryCondition(model);
		return forward("productPublish");
	}
	
	@RequestMapping("/csale")
	public String csale(String id,Model model){
		outQueryCondition(model);
		return forward("productCSale");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/select")
	public String select(HttpServletRequest request){
		List<LovMember> list = productService.queryProModel();
		return sendSuccessMessage(list);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/selectProModel")
	public String selectProModel(Condition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		List<LovMember> list = productService.queryProModel(condition);
		return sendSuccessMessage(list);
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/selectMaterCode")
	public String selectMaterCode(HttpServletRequest request,String search){
		List<LovMember> list = productService.queryMaterCode(search);
		return sendSuccessMessage(list);
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/selectProduction")
	public String selectProduction(HttpServletRequest request,String materielCode){
		KstarProduct list = productService.queryByMaterCode(materielCode);
		return sendSuccessMessage(list);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/selectEcrRefer")
	public String selectEcrRefer(HttpServletRequest request){
		String ecrId = request.getParameter("id");
		List<LovMember> list = ecrService.queryEcrRefer(ecrId);
		return sendSuccessMessage(list);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/publishList")
	public String publishList(PageCondition condition,HttpServletRequest request) throws Exception{
		ActionUtil.requestToCondition(condition, request);
		addProductQeuryCondition(condition);
		LovMember lov = new KstarProduct().getLovMember(ProcessConstants.PRODUCT_PUBLISH_PROC, ProcessConstants.PROCESS_STATUS_03);	//已发布
		condition.getFilterObject().addCondition("publishStatus", "!=", lov.getId());
		condition.setCondition("queryPublish", "queryPublish");
		IPage p = productService.query(condition);
		
		return sendSuccessMessage(p);
	}
	
	@RequestMapping("/convertSale")
	public String convertSale(String id,Model model){
		outQueryCondition(model);
		return forward("productpreSale");
	}
	
	@ResponseBody
	@RequestMapping("/convertSaleList")
	public String convertSaleList(PageCondition condition,HttpServletRequest request) throws Exception{
		ActionUtil.requestToCondition(condition, request);
		KstarProduct kp = new KstarProduct();
		addProductQeuryCondition(condition);
		//不显示：预转销、已转销
		LovMember saleStatus3 = kp.getLovMember(ProcessConstants.PM_PTS_PROC, ProcessConstants.PROCESS_STATUS_03);	//预转销
		LovMember saleStatus5 = kp.getLovMember(ProcessConstants.PM_PTS_PROC, ProcessConstants.PROCESS_STATUS_05);	//已转销
		condition.getFilterObject().addCondition("saleStatus", "!=", saleStatus3.getId());
		condition.getFilterObject().addCondition("saleStatus", "!=", saleStatus5.getId());
		//不显示：服务产品、商务产品
//		LovMember crmCategory3 = kp.getLovMember("crmCategory", "0003");	//服务产品
//		LovMember crmCategory4 = kp.getLovMember("crmCategory", "0004");	//商务产品
		condition.getFilterObject().addCondition("crmCategory", "!=", "0003");
		condition.getFilterObject().addCondition("crmCategory", "!=", "0004");

		//产品预转销  应该只能筛选出未转销、转销审批中的标准、非标、外购产品。（目前界面上可以查询到服务、商务产品）
		
		IPage p = productService.query(condition);
		return sendSuccessMessage(p);
	}
	
	@ResponseBody
	@RequestMapping("/csaleList")
	public String csaleList(PageCondition condition,HttpServletRequest request) throws Exception{
		ActionUtil.requestToCondition(condition, request);
		KstarProduct kp = new KstarProduct();
		addProductQeuryCondition(condition);
		//只显示：预转销、已转销
		LovMember saleStatus5 = kp.getLovMember(ProcessConstants.PM_PTS_PROC, ProcessConstants.PROCESS_STATUS_05);	//已转销
		condition.getFilterObject().addCondition("saleStatus", "=", saleStatus5.getId());
		IPage p = productService.query(condition);
		return sendSuccessMessage(p);
	}
	
	@NoRight
	@RequestMapping("/info1")
	public String info(String id,String type,String catelogId, Model model){
		KstarProduct kp = null;
		if(StringUtils.isNotEmpty(id)){
			// 需求单为新建和返回修改中状态时，允许销售修改产品型号，产品管理员不做限制
			List<KstarProductDemand> productDemands = demandService.getProductDemandForProductId(id);
			LovMember theReject = this.lovMemberService.getLovMemberByCode("PRODUCT_DEMAND_PROC", "04");
			LovMember theNew = this.lovMemberService.getLovMemberByCode("PRODUCT_DEMAND_PROC", "01");
			boolean canEditProductModel = false;
			List<LovMember> rule = lovMemberService.getRulesByUserId(getUserObject().getEmployee().getId());
			for (LovMember lovMember : rule) {
				if ("CPBROLE02".equals(lovMember.getCode())) {
					canEditProductModel = true;
					break;
				}
			}
			if (!canEditProductModel) {
				for (KstarProductDemand productDemand : productDemands) {
					if (Objects.equals(theNew.getId(),productDemand.getDemandStatus())
							|| Objects.equals(theReject.getId(),productDemand.getDemandStatus())) {
						canEditProductModel = true;
						break;
					}
				}
			}
			model.addAttribute("canEditProductModel", canEditProductModel);
			model.addAttribute("id",id);
			 kp =  productService.queryProductById(id);
			 if(StringUtils.isNotBlank(kp.getProOrgId())){
				LovMember org = lovMemberService.get(kp.getProOrgId());
				model.addAttribute("org", JSON.toJSONString(org));
			 }
		}else{


			kp = new KstarProduct();
			kp.setPublishStatus(kp.getLovMember(ProcessConstants.PRODUCT_PUBLISH_PROC, ProcessConstants.PROCESS_STATUS_01).getId());
			kp.setSaleStatus(kp.getLovMember(ProcessConstants.PM_PTS_PROC, ProcessConstants.PROCESS_STATUS_01).getId());
			kp.setPriceStatus("notdecideprice");
			kp.setCsaleStatus(kp.getLovMember("csaleStatus", "02").getId());	//正常在售
			kp.setLineBean(null);
			model.addAttribute("catelogId", catelogId);
		}
		model.addAttribute("product", kp);
		model.addAttribute("type", type);
		return forward("info1");
	}
	
	@RequestMapping("/csalePocess")
	public String csalePocess(String id,Model model){
		model.addAttribute("id",id);
		KstarProduct kp =  productService.queryProductById(id);
		if(StringUtils.isNotBlank(kp.getProOrgId())){
			LovMember org = lovMemberService.get(kp.getProOrgId());
			model.addAttribute("org", JSON.toJSONString(org));
		}
		model.addAttribute("product", kp);
		
		TabMain tabMainInfo = new TabMain();
		tabMainInfo.setInitAll(false);
		if (hasPermission("????????????????")) {
			tabMainInfo.addTab("审批历史", "/standard/history.html?contrId=" + id);
		}
		model.addAttribute("tabMainInfo", tabMainInfo);
		
		return forward("csale_process");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition,HttpServletRequest request) throws Exception{
		ActionUtil.requestToCondition(condition, request);
		addProductQeuryCondition(condition);
		IPage p = productService.query(condition);
		return sendSuccessMessage(p);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/page2")
	public String page2(PageCondition condition,HttpServletRequest request) throws Exception{
		ActionUtil.requestToCondition(condition, request);
		addProductQeuryCondition(condition);
		condition.setCondition("positionId",  this.getUserObject().getPosition().getId());
		IPage p = productService.query2(condition,this.getUserObject());
		return sendSuccessMessage(p);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/processpage")
	public String processpage(PageCondition condition,HttpServletRequest request) throws Exception{
		
		String processID = request.getParameter("processID");
		
		IPage p = null;
		
		if(StringUtils.isNotEmpty(processID)){
			 List<KstarProductWorkFlow> kwList = productProcess.getList(processID);
			 int size  = kwList.size();
			
			 if(size == 1){
				 condition.getFilterObject().addCondition("id", "eq", kwList.get(0).getProductId());

				ActionUtil.requestToCondition(condition, request);
				
				p = productService.query(condition);
			 }else if(size > 1){
				 
				p = productService.queryProcess(kwList, condition.getRows(), condition.getPage());
			 }
		}
		return sendSuccessMessage(p);
	}
	


	@NoRight
	@RequestMapping("/info3")
	public String info3(String id,Model model){
		model.addAttribute("id",id);
		return forward("info3");
	}
	
	@NoRight
	@RequestMapping("/info4")
	public String info4(String id,Model model){
		model.addAttribute("id",id);
		//List<LovMember> list = lovMemberService.getFatherList(id);
		
		KstarProduct kp = productService.queryProductById(id);
		
		List<LovMember> list = lineService.prepareForkLov(kp);
		
		model.addAttribute("list",JSON.toJSONString(list));
		
		return forward("info4");
	}
	
	@NoRight
	@RequestMapping("/info5")
	public String info5(String id,Model model){
		model.addAttribute("id",id);
		return forward("info5");
	}
	
	@NoRight
	@RequestMapping("/info6")
	public String info6(String id,Model model){
		model.addAttribute("id",id);
		return forward("info6");
	}
	
	@NoRight
	@RequestMapping("/info7")
	public String info7(String id,Model model){
		model.addAttribute("id",id);
		return forward("info7");
	}
	
	@NoRight
	@RequestMapping("/info8")
	public String info8(String id,String crmCategory,String customId,String businessUnit,String custContrNo, Model model){
		model.addAttribute("id",id);
		model.addAttribute("crmCategory",crmCategory);
		model.addAttribute("customId",customId);
		model.addAttribute("businessUnit",businessUnit);
		model.addAttribute("custContrNo",custContrNo);
		model.addAttribute("LOGIN_USER_ID",this.getUserObject().getEmployee().getId());
		return forward("info8");
	}
	
	@NoRight
	@RequestMapping("/info9")
	public String info9(String id,Model model){
		model.addAttribute("id",id);
		return forward("info9");
	}
	
	@NoRight
	@RequestMapping("/info10")
	public String info10(String id,Model model){
		model.addAttribute("id",id);
		model.addAttribute("LOGIN_USER_ID",this.getUserObject().getEmployee().getId());
		return forward("info10");
	}
	
	@NoRight
	@RequestMapping("/info11")
	public String info11(String id,Model model){
		model.addAttribute("id",id);
		return forward("info11");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/docList")
	public String docList(PageCondition condition,HttpServletRequest request) throws Exception{
		ActionUtil.requestToCondition(condition, request);
		UserObject uo = this.getUserObject();
		condition.setCondition("productID",  condition.getCondition("id"));
		condition.setCondition("applyPerson",  uo.getEmployee().getId());
		IPage p = docService.query(condition);
		return sendSuccessMessage(p);
	}

	@NoRight
	@RequestMapping("/docAdd")
	public String docAdd(String id,boolean statusEdit,boolean ableEdit,String productID,Model model){
		model.addAttribute("id",id);
		KstarProductDoc kp = null;
		if(StringUtils.isNotEmpty(id)){
			kp = docService.queryDocByID(id);
			TabMain tabMainInfo = new TabMain();
			tabMainInfo.setInitAll(false);
			tabMainInfo.addTab("授权岗位", "/team/list.html?businessType=productDoc&businessId="+id);
			tabMainInfo.addTab("审批历史","/standard/history.html?contrId="+id);
			model.addAttribute("tabMainInfo",tabMainInfo);
		}else{
			kp = new KstarProductDoc();
			kp.setApplyPerson(this.getUserObject().getEmployee().getId());
			kp.setApplyTime(new Date());
			kp.setApplyStatus(kp.getLovMember(ProcessConstants.PRODUCT_DOC_PROC, ProcessConstants.PROCESS_STATUS_01).getId());
		}
		model.addAttribute("doc", kp);
		model.addAttribute("statusEdit", statusEdit);
		model.addAttribute("ableEdit", ableEdit);
		return forward("docAdd");
	}

	@NoRight
	@ResponseBody
	@RequestMapping(value="/docAdd",method=RequestMethod.POST)
	public String docAdd(HttpServletRequest request) throws InstantiationException, IllegalAccessException, NoSuchFieldException, SecurityException{
		KstarProductDoc newDoc = ActionUtil.requestToObject(KstarProductDoc.class, request);
		String id = newDoc.getId();
		if( StringUtils.isEmpty(id)){//新增
			newDoc.setCreatedBy(this.getUserObject().getEmployee().getName());
			newDoc.setApplyPerson(this.getUserObject().getEmployee().getId());
			docService.save(newDoc,this.getUserObject());
		}else{//修改
			KstarProductDoc oldDoc = docService.queryDocByID(id);
			BeanUtils.copyPropertiesIgnoreNull(newDoc,oldDoc);
			docService.update(oldDoc,this.getUserObject());
		}

		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value="/docDelete",method=RequestMethod.POST)
	public String docDelete(String id){
		
		docService.delete(id);
		return sendSuccessMessage();
	}
	
	@RequestMapping("/productedit")
	public String productedit(String id,Model model){
		
		
		LovMember lm = lovMemberService.get(id);
		if(lm == null){
			return sendErrorMessage("叶子节点未找到该产品！");
		}
		model.addAttribute("id",lm.getCode());
		KstarProduct kp =  productService.queryProductById(lm.getCode());
		model.addAttribute("product", kp);
		return forward("product");
	}
	
	@NoRight
	@RequestMapping("/edit")
	public String edit(String id,String catelogId,String type,Model model){
		KstarProduct kp =  productService.queryProductById(id);
		model.addAttribute("product", kp);
		
		TabMain tabs = new TabMain();
		tabs.addTab("基本信息", "/product/info1.html?type="+type+"&id="+id);
		if(StringUtil.isNotEmpty(catelogId)){
			model.addAttribute("tabMain",tabs);
			return forward("product");
		}
		String crmCategory = kp.getCrmCategory();
		if("0001".equals(crmCategory) || "0002".equals(crmCategory) && this.hasPermission("P01-1ProductEcr")){
			tabs.addTab("ECR变更", "/product/ecrchange/list.html?productID="+id);
		}
		if(("0001".equals(crmCategory) || "0002".equals(crmCategory)) && this.hasPermission("P01-1ProductDoc")){
			tabs.addTab("特殊产品文档", "/product/info3.html?id="+id);
		}
		if(("0001".equals(crmCategory) || "0002".equals(crmCategory)) && this.hasPermission("P01-1ProductComDoc")){
			String fileStr = "";
			if(!this.hasPermission("P01-1ProductComDocAdd")){
				fileStr += "&unableAdd=true";
			}
			if(!this.hasPermission("P01-1ProductComDocDelete")){
				fileStr += "&unableDelete=true";
			}
			tabs.addTab("标准产品文档", "/common/attachment/attachment.html?businessType=productComDoc&docGroupCode=ProComDocType&businessId="+id+fileStr);
		}
		if(("0001".equals(crmCategory) || "0002".equals(crmCategory)) && this.hasPermission("P01-1ProductCatalogLook")){
			tabs.addTab("产品目录", "/product/info4.html?id="+id);
		}
		if(("0001".equals(crmCategory) || "0002".equals(crmCategory)) && this.hasPermission("P01-1ProductPrice")){
			tabs.addTab("价格表", "/product/info5.html?id="+id);
		}
		if(("0001".equals(crmCategory)) && this.hasPermission("P01-1ProductDerive")){
			tabs.addTab("衍生品", "/product/info6.html?id="+id);
		}
		if(("0002".equals(crmCategory) || "0005".equals(crmCategory)) && this.hasPermission("P01-1ProductClient")){
			tabs.addTab("客户", "/product/info7.html?id="+id);
		}
		if(("0002".equals(crmCategory) || "0005".equals(crmCategory)) && this.hasPermission("P01-1ProductDemand")){
			tabs.addTab("需求单", "/product/info8.html?id="+id+"&crmCategory="+kp.getCrmCategory());
		}
		if(("0001".equals(crmCategory) || "0002".equals(crmCategory)) && this.hasPermission("P01-1ProductSub")){
			tabs.addTab("选配清单", "/product/info9.html?id="+id);
		}
		if(("0001".equals(crmCategory) || "0002".equals(crmCategory)) && this.hasPermission("P01-1ProductAutest")){
			tabs.addTab("认证与测试", "/product/info10.html?id="+id);
		}
		if(("0001".equals(crmCategory) || "0002".equals(crmCategory)) && this.hasPermission("P01-1ProductService")){
			tabs.addTab("服务产品", "/product/info11.html?id="+id);
		}
		if(("0001".equals(crmCategory) || "0002".equals(crmCategory) || "0005".equals(crmCategory)) && this.hasPermission("P01-1ProductImg")){
			tabs.addTab("图片", "/product/img.html?id="+id);
		}
//		0003	服务产品
//		0004	商务产品
//		0001	标准产品	可发布
//		0002	非标产品	可发布
//		0005	外购产品
		model.addAttribute("tabMain",tabs);
		return forward("product");
	}

	@RequestMapping("/img")
	public String img(String id, Model model) {
		model.addAttribute("bizId", id);
		model.addAttribute("bizType", "productPic");//产品附件标签页

		List<KstarAttachment> attachments = attachmentService.getAttachments(id, "productPic");

		model.addAttribute("attachments", attachments);
		return forward("img");
	}

	@ResponseBody
	@RequestMapping("/imgDelete")
	public String imgDelete(String imgId, Model model) {
		attachmentService.delete(imgId);
		return sendSuccessMessage();
	}

	@RequestMapping("/add")
	public String add(String id,String catelogId,String type, Model model){
		TabMain tabs = new TabMain();
		if(StringUtil.isNotEmpty(catelogId)){
			tabs.addTab("基本信息", "/product/info1.html?type="+type+"&catelogId="+catelogId);
		}else{
			tabs.addTab("基本信息", "/product/info1.html?type="+type);
		}
		model.addAttribute("tabMain",tabs);
		return forward("product");
	}

    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add() {
        return sendSuccessMessage();
    }

	@NoRight
	@ResponseBody
	@RequestMapping(value="/mainInfoSave",method=RequestMethod.POST)
	public String mainInfoSave(KstarProduct product,String catelogId, Model model){
		if("0002".equals(product.getCrmCategory()) && (StringUtil.isEmpty(product.getProModel()) || StringUtil.isEmpty(product.getClientCategory()))){
			throw new AnneException("CRM产品类别未非标产品时，产品型号、客户型号必填！");
		}
		boolean newFlag = false;
		UserObject uo= this.getUserObject();
		Employee employee= uo.getEmployee();
		
		if(product.getId() == null || StringUtils.isEmpty(product.getId())){
			product.setId(null);
			// 创建人
			product.setCreatedById(employee.getId());
			// 记录创建时间
			product.setCreatedAt(new Date());
			// 创建人岗位
			product.setCreatedPosId( uo.getPosition().getId());
			// 创建人组织
			product.setCreatedOrgId( uo.getOrg().getId());
			if("0003".equals(product.getCrmCategory()) || "0004".equals(product.getCrmCategory()) ){
				product.setSaleStatus(product.getLovMember(ProcessConstants.PM_PTS_PROC, ProcessConstants.PROCESS_STATUS_05).getId());
				product.setPublishStatus(product.getLovMember(ProcessConstants.PRODUCT_PUBLISH_PROC, ProcessConstants.PROCESS_STATUS_03).getId());
			}
			LovMember csaleStatus = new KstarProduct().getLovMember("csaleStatus", "02");	//正常在售
			product.setCsaleStatus(csaleStatus.getId());
			newFlag = true;
		}else{
			KstarProduct old = productService.getProductById(product.getId());
			BeanUtils.copyPropertiesIgnoreNull(product, old);
			BeanUtils.copyPropertiesIgnoreNull(old, product);
		}
		// 更新者
		product.setUpdatedById(employee.getId());
		// 更新时间
		product.setUpdatedAt(new Date());


		productService.save(product,this.getUserObject(),catelogId);
		return sendSuccessMessage(product.getId());
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(){
		productService.todo();
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		productService.delete(id);
		return sendSuccessMessage();
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/autoComplete.html" ,method = RequestMethod.POST)
	public String autoComplete(Condition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		return null;
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/priceList")
	public String priceList(PageCondition condition,HttpServletRequest request) throws Exception{
		ActionUtil.requestToCondition(condition, request);
		condition.getFilterObject().addCondition("productID", "eq", (String)condition.getCondition("id"));
		IPage p = priceService.querySql(condition);
		return sendSuccessMessage(p);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/deriveList")
	public String deriveList(PageCondition condition,HttpServletRequest request) throws Exception{
		ActionUtil.requestToCondition(condition, request);
		condition.getFilterObject().addCondition("fatherProCode", "eq", condition.getStringCondition("fatherProCode"));
		IPage p = productService.query(condition);
		return sendSuccessMessage(p);
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/clientList")
	public String clientList(PageCondition condition,HttpServletRequest request) throws Exception{
		ActionUtil.requestToCondition(condition, request);
		condition.getFilterObject().addCondition("productID", "eq", (String)condition.getCondition("id"));
		this.addFilterCondition(condition, "clientName", "like");
		this.addFilterCondition(condition, "clientCode", "like");
		IPage p = clientService.query(condition);
		return sendSuccessMessage(p);
	}
	
	@RequestMapping("/clientEdit")
	public String clientEdit(String id,Model model){
		model.addAttribute("id",id);
		
		if(id != null){
			KstarProductClient client =  clientService.queryClientById(id);
			model.addAttribute("client", client);
		}
		return forward("clientEdit");
	}

	@ResponseBody
	@RequestMapping(value="/clientDelete",method=RequestMethod.POST)
	public String clientDelete(String id){
		
		clientService.delete(id);
		return sendSuccessMessage();
	}

	@ResponseBody
	@RequestMapping(value="/clientEdit",method=RequestMethod.POST)
	public String clientEditDo(KstarProductClient kc){
		
		//KstarProductDoc doc = ActionUtil.requestToObject(KstarProductDoc.class, request);
		clientService.save(kc,this.getUserObject());
		return sendSuccessMessage();
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/demandList")
	public String demandList(PageCondition condition,HttpServletRequest request) throws Exception{
		ActionUtil.requestToCondition(condition, request);
		IPage p = demandService.query(condition,this.getUserObject());
		return sendSuccessMessage(p);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/canApplyDemand")
	public String canApplyDemand(String productID,HttpServletRequest request) throws Exception{
		boolean canApplyDemand = demandService.canApplyDemand(productID);
		return sendSuccessMessage(canApplyDemand);
	}

	@NoRight
	@RequestMapping("/demandAdd")
	public String demandAdd(String id,String productId, boolean statusEdit, boolean ableEdit, String customId, String businessUnit, String custContrNo, String seriesDemand, Model model){
		KstarProductDemand client;
		if(id != null){
			client =  demandService.queryDemandById(id,productId);
			CustomInfo customInfo = customService.getCustomInfo(client.getClientCode());
			model.addAttribute("demand", client);
			model.addAttribute("customInfo", customInfo==null?null : JSON.toJSONString(customInfo));
		}else{
			client =  new KstarProductDemand();
			client.setDemandCode(serialNumberService.getSerialNumber3("productDemand"));
			client.setBusinessUnit(demandService.findOnerSaleCenter(this.getUserObject().getOrg().getId()));
			client.setDemandPerson(this.getUserObject().getEmployee().getId());
			client.setDemandPersonName(this.getUserObject().getEmployee().getName());
			client.setDemandDepartment(this.getUserObject().getOrg().getId());
			
			if(StringUtil.isNotEmpty(businessUnit)){
				client.setBusinessUnit(businessUnit);
			}
			if(StringUtil.isNotEmpty(custContrNo)){
				client.setClientContract(custContrNo);
			}
			if(StringUtil.isNotEmpty(customId)){
				client.setClientCode(customId);
				CustomInfo customInfo = customService.getCustomInfo(client.getClientCode());
				model.addAttribute("customInfo", customInfo==null?null : JSON.toJSONString(customInfo));
			}
			model.addAttribute("demand", client);
		}
		model.addAttribute("statusEdit", statusEdit);
		model.addAttribute("ableEdit", ableEdit);
		model.addAttribute("seriesDemand", seriesDemand);
		model.addAttribute("LOGIN_USER_ID",this.getUserObject().getEmployee().getId());

		TabMain tabMainInfo = new TabMain();
		tabMainInfo.setInitAll(false);
		tabMainInfo.addTab("审批历史","/standard/history.html?contrId="+id);
		LovMember newed = lovMemberService.getLovMemberByCode("PRODUCT_DEMAND_PROC", "01");
		if (StringUtils.isNotEmpty(client.getId()) && !Objects.equals(client.getDemandStatus(), newed.getId())) {
			tabMainInfo.addTab("PDM审批历史", "/pdm/flow/history.html?no=" + client.getDemandCode() + "&type=" + "PRODUCT_DEMAND");
		}

		model.addAttribute("tabMainInfo",tabMainInfo);
		
		this.outQueryCondition(model);
		return forward("demandAdd");
	}

	@NoRight
	@ResponseBody
	@RequestMapping(value="/demandAdd",method=RequestMethod.POST)
	public String demandAdd(KstarProductDemand demand,String seriesDemand) {
		if(demand.getId() == null){
			demand.setDemandStatus(demand.getLovMember(ProcessConstants.PRODUCT_DEMAND_PROC, ProcessConstants.PROCESS_STATUS_01).getId());
		}
		demandService.save(demand,this.getUserObject(),seriesDemand);
		return sendSuccessMessage();
	}
	

	@ResponseBody
	@RequestMapping(value="/demandDelete",method=RequestMethod.POST)
	public String demandDelete(String id) {
		demandService.delete(id);
		return sendSuccessMessage();
	}

	@ResponseBody
	@RequestMapping(value="/demandSubmit",method=RequestMethod.POST)
	public String demandSubmit(String id) {
		demandService.submitDemand(id, this.getUserObject());
		return sendSuccessMessage();
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/proSubList")
	public String proSubList(PageCondition condition,HttpServletRequest request) throws Exception{
		ActionUtil.requestToCondition(condition, request);
		IPage p = proSubService.query(condition);
		return sendSuccessMessage(p);
	}

	@RequestMapping("/proSubAdd")
	public String proSubAdd(String id,String productID,Model model){		
		model.addAttribute("id",id);	
		KstarProSub proSub = new KstarProSub();
		if(id == null){//新增
			proSub.setProductID(productID);
		}else{//修改
			proSub = proSubService.queryById(id);
			KstarProduct product = productService.queryProductById(proSub.getProSubID());
			proSub.setProductBean(product);
		}
		model.addAttribute("proSub", proSub);
		return forward("proSubAdd");
	}

	@NoRight
	@ResponseBody
	@RequestMapping(value="/proSubAdd",method=RequestMethod.POST)
	public String proSubAdd(KstarProSub proSub) throws Exception {
		if(StringUtil.isEmpty(proSub.getId())){//新增
			PageCondition condition = new PageCondition();
			condition.getFilterObject().addCondition("productID", "eq", proSub.getProductID());
			condition.getFilterObject().addCondition("proSubID", "eq", proSub.getProSubID());
			IPage page = proSubService.queryDup(condition);
			if( page.getCount() < 1){
				proSubService.save(proSub,this.getUserObject());
				return sendSuccessMessage();
			}else{
				return sendErrorMessage("配料已存在！");
			}
		}else{//修改
			PageCondition condition = new PageCondition();
			condition.getFilterObject().addCondition("productID", "eq", proSub.getProductID());
			condition.getFilterObject().addCondition("proSubID", "eq", proSub.getProSubID());
			condition.getFilterObject().addCondition("id", "!=", proSub.getId());
			IPage page = proSubService.queryDup(condition);
			if( page.getCount() < 1){
				proSubService.update(proSub,this.getUserObject());
				return sendSuccessMessage();
			}else{
				return sendErrorMessage("配料已存在！");
			}
		}
	}

	@ResponseBody
	@RequestMapping("/proSubDel")
	public String proSubDel(String id,Model model){		
		KstarProSub kps =  proSubService.queryById(id);
		if( null != kps ){
			proSubService.delete(id);
			return sendSuccessMessage();	
		}else{
			return sendErrorMessage("数据不存在！");
		}
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/proAuTestList")
	public String proAuTestList(PageCondition condition,HttpServletRequest request) throws Exception{
		ActionUtil.requestToCondition(condition, request);
		condition.getFilterObject().addCondition("productID", "eq", (String)condition.getCondition("productID"));
		this.addFilterCondition(condition, "auTestName", "like");
		IPage p = auTestService.query(condition);
		return sendSuccessMessage(p);
	}
	
	@NoRight
	@RequestMapping("/auTestAdd")
	public String auTestAdd(String id, boolean statusEdit, boolean ableEdit, String productID, Model model){

		model.addAttribute("id",id);

		KstarProductAuTest pa = null;
		//申请人 id
		String applyPersionId = null;
		if(id != null){//修改
			pa =  auTestService.queryAuTestById(id);
			//申请人
			applyPersionId = pa.getApplyPersion();
		}else{//新增
			pa = new KstarProductAuTest();
			UserObject userObject = this.getUserObject();
			//设置当前用户的部门
			pa.setApplyDepartment(userObject.getOrg().getId());
			//申请人
			applyPersionId = userObject.getEmployee().getId();
			//设置申请状态
			pa.setStatus(pa.getLovMember(ProcessConstants.PRODUCT_AU_TEST_PROC, ProcessConstants.PROCESS_STATUS_01).getId());//新建
			//是否总经理审批
			pa.setIsCeoApprove(lovMemberService.getLovMemberByCode("NY", "0").getId());
			//需要完成日期
			KstarProduct product = productService.queryProductById(productID);
			pa.setProModel(product.getProModel());
			pa.setCompleteTime(new Date());
		}

		//设置申请人
		Employee ee = employeeService.get(applyPersionId);
		pa.setApplyPersionBean(ee);

		model.addAttribute("autest", pa);
		model.addAttribute("userObject",this.getUserObject());

		TabMain tabMainInfo = new TabMain();
		tabMainInfo.setInitAll(false);
		tabMainInfo.addTab("审批历史","/standard/history.html?contrId="+id);
		model.addAttribute("tabMainInfo",tabMainInfo);
		model.addAttribute("statusEdit", statusEdit);
		model.addAttribute("ableEdit", ableEdit);
		return forward("auTestAdd");
	}

	@ResponseBody
	@RequestMapping("/auTestDel")
	public String auTestDel(String id,Model model){	
		auTestService.delete(id);
		return sendSuccessMessage();
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/auTestAdd",method=RequestMethod.POST)
	public String auTestAdd(HttpServletRequest request) throws ParseException, InstantiationException, IllegalAccessException, SecurityException{
		KstarProductAuTest pat = ActionUtil.requestToObject(KstarProductAuTest.class, request);
		List<IUploadFile> list = UploadUtils.uploadFile(request, "/aaaa");

		if(!list.isEmpty()){
			pat.setAnthUrl(list.get(0).getRealPath());
		}else{
			if( null != pat.getId() ){
				KstarProductAuTest old = auTestService.queryAuTestById(pat.getId());
				pat.setAnthUrl(old.getAnthUrl());
			}
		}

		if(  checkDate(pat.getCompleteTime()) ){
			String id = pat.getId();
			if(StringUtils.isBlank(id)){//新增
				UserObject userObject = this.getUserObject();
				//设置申请人
				pat.setApplyPersion(userObject.getEmployee().getId());
				//设置部门
				pat.setApplyDepartment(userObject.getOrg().getId());
				auTestService.save(pat,this.getUserObject());
			}else{//修改
				//产品型号
				pat.setProModel(deleteDupProModel(pat.getProModel()));
				auTestService.update(pat,this.getUserObject());
			}
			return sendSuccessMessage();
		}else{
			return sendErrorMessage("需求完成时间不能早于今天!");
		}		
	}

	private boolean checkDate(Date input) throws ParseException{
		SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
		
		String inputString=format.format(input);
		Date inputDate = format.parse(inputString);
		
		String nowString=format.format(new Date());
		Date nowDate = format.parse(nowString);
		
		if( !nowDate.after(inputDate)){
			return true;
		}else{
			return false;
		}				
	}
	/**
	 * 由于前端控件有问题，需要在此进行处理。
	 * 
	 * @param proModel
	 * @return
	 */
	private String deleteDupProModel(String proModel){		
		String[] modelArr = proModel.split(",");
		Set<String> set = new HashSet<String>();
		for(String m : modelArr){
			set.add(m);
		}
		System.out.println(StringUtil.join(set,","));
		return StringUtil.join(set,",");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/serviceProList")
	public String serviceProList(PageCondition condition,HttpServletRequest request) throws Exception{
		ActionUtil.requestToCondition(condition, request);
		// 型号相同 proModel 而且属于服务类型 crmCategory
		this.addFilterCondition(condition, "productCode", "like");
		this.addFilterCondition(condition, "productName", "like");
		IPage p = productService.serviceProQuery(condition);
		return sendSuccessMessage(p);
	}	

	@NoRight
	@RequestMapping(value = "/queryVMaterCode", method = RequestMethod.POST)
	public void queryBorAdvance(HttpServletRequest request, HttpServletResponse response) {
		String selected = request.getParameter("selectedID");
		String reValue = null;
		PrintWriter out = null;
		if(StringUtils.isNotBlank(selected)){
			String dateTime =  prepareTime();
			String tempName = null;
			
			// 预定义物料号生成规则：大类编码（1个字母）+产生日期（8位数字）+流水号（3位数字)，共12位
			//大类编码：标准品（S），非标品（N），服务类（F），商务类（B），外购品（P）
			//如：S20161121001
			if("0001".equalsIgnoreCase(selected)){
				reValue = "[{\"error\":\"standart project could not been created in CRM!\"}]";
			}else if("0002".equalsIgnoreCase(selected)){

				tempName = "N" + dateTime + productSerialService.queryProductCBy("N", dateTime, true);
				reValue = "[{\"success\":\""+ tempName + "\"}]";
			}else if("0003".equalsIgnoreCase(selected)){
				tempName = "F" + dateTime + productSerialService.queryProductCBy("F", dateTime, true);
				reValue = "[{\"success\":\""+ tempName + "\"}]";
			}else if("0004".equalsIgnoreCase(selected)){
				tempName = "B" + dateTime + productSerialService.queryProductCBy("B", dateTime, true);
				reValue = "[{\"success\":\""+ tempName + "\"}]";
			}else if("0005".equalsIgnoreCase(selected)){
				String count = productSerialService.queryProductCBy("P", dateTime, true);
				tempName = "P" + dateTime + count;
				reValue = "[{\"success\":\""+ tempName + "\"}]";
			}
		}else{
			reValue = "[{\"error\":\"you did not selected any contant!\"}]";
		}
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.print(reValue);
		out.flush();
		out.close();
	}

    @NoRight
    @RequestMapping("/crmCategorys")
    @ResponseBody
    public String crmCategorys() {
        List<LovMember> crmCategorys = this.lovMemberService.getListByGroupCode("crmCategory");
        List<LovMember> deleteds = new ArrayList<>();
        for (LovMember crmCategory : crmCategorys) {
            if ("0004".equals(crmCategory.getCode()) || "0003".equals(crmCategory.getCode())) {
                deleteds.add(crmCategory);
            }
        }
        crmCategorys.removeAll(deleteds);
        return sendSuccessMessage(crmCategorys);
    }

    /**
     * 修改CRM产品类别
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/changeCrmCategory")
    public String changeCrmCategory(String id, Model model) {
        if (StringUtil.isEmpty(id)) {
            throw new AnneException("请选择更改CRM类别的产品");
        }
        KstarProduct product = this.productService.getProductById(id);
		if (product == null) throw new AnneException("产品不存在");
		String erpVmaterCode = this.productService.getErpVmaterCodeFor(product);
		model.addAttribute("erpVmaterCode", erpVmaterCode);
		model.addAttribute("product", product);
		return forward("changeCrmCategory");
	}

	/**
	 * 修改CRM产品类别
	 * @param id
	 * @param crmCategory
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/changeCrmCategory", method = RequestMethod.POST)
	public String changeCrmCategory(String id, String crmCategory, String vmaterCode, Model model) {
		if (StringUtils.isEmpty(id)) {
			throw new AnneException("请选择更改CRM类别的产品。");
		}
		if (StringUtils.isEmpty(crmCategory)) {
			throw new AnneException("请选择CRM类别。");
        }

        this.productService.updateCrmCategory(id, crmCategory, vmaterCode);
        return sendSuccessMessage();
    }

	private String prepareTime(){
		
		//设置日期格式
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		
		// new Date()为获取当前系统时间
		String reValue = df.format(new Date());
		
		return reValue;
	}
	
	@NoRight
	@RequestMapping("/selectProductListCondition")
	public String selectProductListCondition(String pickerId,Model model, String proModel){
		model.addAttribute("pickerId",pickerId);
		
		if(StringUtils.isNotEmpty(proModel)){
			model.addAttribute("proModel",proModel);
		}
		
		return forward("selectproduct");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/productPage")
	public String fatherpage(PageCondition condition,HttpServletRequest request) throws Exception{
		ActionUtil.requestToCondition(condition, request);
		addProductQeuryCondition(condition);
		String proModel =  (String)condition.getCondition("proModel");
		if(StringUtils.isNotEmpty(proModel)){
			condition.getFilterObject().addCondition("proModel", "eq", proModel);
		}
		condition.getFilterObject().addCondition("crmCategory", "eq", "0001");
		IPage p = productService.query(condition);
		return sendSuccessMessage(p);
	}

	private void addProductQeuryCondition(PageCondition condition){
		addFilterCondition(condition,"productCode","like");	//产品编码
		addFilterCondition(condition,"productName","like");	//产品名称
		addFilterCondition(condition,"proModel","like");	//产品型号
		addFilterCondition(condition,"proBrand","like");	//品牌
		addFilterCondition(condition,"crmCategory","=");	//CRM产品类别
		addFilterCondition(condition,"erpCategory","=");	//ERP产品类别
		addFilterCondition(condition,"publishStatus","=");	//发布状态
		addFilterCondition(condition,"priceStatus","=");	//订价状态
		addFilterCondition(condition,"saleStatus","=");	//转销状态
		addFilterCondition(condition,"clientCategory","like");	//客户型号
		addFilterCondition(condition,"cproType","like");	//产品类别
		addFilterCondition(condition,"proSeries","like");	//产品系列
		addFilterCondition(condition,"cproCategory","like");	//产品分类
		
		addFilterCondition(condition,"modifyHardware","like");	//修改硬件
		addFilterCondition(condition,"addFunction","like");	//增加功能
		addFilterCondition(condition,"modifyParamter","like");	//更改参数
		addFilterCondition(condition,"chassisSize","like");	//机箱尺寸/外观变更
		addFilterCondition(condition,"commercialData","like");	//商品化资料
		addFilterCondition(condition,"randomAttach","like");	//增加随机附件
		addFilterCondition(condition,"other","like");	//其他
	}
	
	private void addFilterCondition(PageCondition condition, String fieldName, String queryType){
		String fieldValue = condition.getStringCondition(fieldName);
		if(StringUtil.isNotEmpty(fieldValue)){
			if("=".equals(queryType)){
				condition.getFilterObject().addCondition(fieldName, "=", fieldValue);
			}else if("like".equals(queryType)){
				condition.getFilterObject().addCondition(fieldName, "like", "%"+fieldValue+"%");
			}
		}
	}

//	@ResponseBody
//	@RequestMapping("/synPDM")
//	public String synPDM(String id) throws Exception {
//		if(StringUtil.isNotEmpty(id)) {
//			demandService.doMove2Int(id, getUserObject().getEmployee().getId());
//		}else{
//			throw new AnneException("主键ID为空");
//		}
//		return sendSuccessMessage();
//	}
	
	private void outQueryCondition(Model model){
		List<LovMember> crmCategoryLst = lovMemberService.getListByGroupCode("crmCategory");
		model.addAttribute("crmCategoryLst",crmCategoryLst);
		List<LovMember> erpCategoryLst = lovMemberService.getListByGroupCode("erpCategory");
		model.addAttribute("erpCategoryLst",erpCategoryLst);
		List<LovMember> publishStatus = lovMemberService.getListByGroupCode("PRODUCT_PUBLISH_PROC");
		model.addAttribute("publishStatus",publishStatus);
		List<LovMember> priceStatus = lovMemberService.getListByGroupCode("proPriceSpecify");
		model.addAttribute("priceStatus",priceStatus);
		List<LovMember> saleStatus = lovMemberService.getListByGroupCode("PM_PTS_PROC");
		model.addAttribute("saleStatus",saleStatus);
	}
	
}
