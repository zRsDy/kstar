package com.ibm.kstar.action.common;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ibm.kstar.api.price.IPriceHeadService;
import com.ibm.kstar.api.product.IProductService;
import com.ibm.kstar.api.system.lov.ILovGroupService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.product.ProductPriceHead;
import com.ibm.kstar.interceptor.system.permission.NoRight;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.page.PageImpl;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/common/product")
public class ProductSelectAction extends BaseAction{
	
	@Autowired
	BaseDao baseDao;
	
	@Autowired
	IProductService productService;
	
	@Autowired
	protected ILovGroupService lovGroupService;
	
	@Autowired
	protected ILovMemberService lovMemberService;

	@Autowired
	IPriceHeadService priceHeadService;
	
	@NoRight
	@RequestMapping("/selectList")
	public String selectProductList(String pickerId,Model model,HttpServletRequest request){
		String priceTableId = request.getParameter("priceTableId");
		String proModel = request.getParameter("proModel");
		String isReport = request.getParameter("isReport");//是否报备
		
		model.addAttribute("pickerId",pickerId);
		model.addAttribute("priceTableId",priceTableId);
		model.addAttribute("proModel",proModel);
		model.addAttribute("isReport",isReport);
		
		Condition condition = new Condition();
		condition.setCondition("groupCode","procatalog");
		if(StringUtil.isNotEmpty(isReport)){
			condition.setCondition("optTxt2",isReport);
		}
		List<LovMember> list = lovMemberService.getList2(condition,this.getUserObject());
		model.addAttribute("procatalogList",JSON.toJSONString(list));
		
		return forward("productSelectList");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/listProductLine")
	public String listProductLine(String pickerId,Model model){
		model.addAttribute("pickerId",pickerId);
		StringBuffer hql = new StringBuffer("select distinct t.c_pro_line from crm_t_product_line t ");
		List<Object[]> list = baseDao.findBySql(hql.toString());
		Map<String,Object> entity = null;
		List<Map<String,Object>> result = Lists.newArrayList();
		for(Object[] o : list){
			entity = Maps.newHashMap();
			entity.put("id", o[0]);			
			result.add(entity);
		}
		return sendSuccessMessage(result);
	}
	
	@NoRight
	@RequestMapping("/multiSelectList")
	public String selectProductListMulti(String pickerId,Model model,HttpServletRequest request){
		String priceTableId = request.getParameter("priceTableId");
		String proModel = request.getParameter("proModel");
		String crmCategory = request.getParameter("crmCategory");
		String crmCategoryQuery = request.getParameter("crmCategoryQuery");
		String battery = request.getParameter("battery");
		String inwall = request.getParameter("inwall");
		String distribute = request.getParameter("distribute");
		String customerId = request.getParameter("customerId");
		String isReport = request.getParameter("isReport");//是否报备
		String productType = request.getParameter("productType");//是否常规或行业入围
		
		model.addAttribute("pickerId",pickerId);
		model.addAttribute("productType",productType);
		model.addAttribute("priceTableId",priceTableId);
		model.addAttribute("proModel",proModel);
		model.addAttribute("crmCategory",crmCategory);
		model.addAttribute("crmCategoryQuery",crmCategoryQuery);
		model.addAttribute("battery",battery);
		model.addAttribute("inwall",inwall);
		model.addAttribute("distribute",distribute);
		model.addAttribute("customerId",customerId);
		model.addAttribute("isInner",getUserObject().isInner()+"");
		model.addAttribute("isReport",isReport);
		
		
		Condition condition = new Condition();
		condition.setCondition("groupCode","procatalog");
		List<LovMember> list = lovMemberService.getList2(condition,this.getUserObject());
		model.addAttribute("procatalogList",JSON.toJSONString(list));
		return forward("productMultiSelectList");
	}
	
	@NoRight
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition,HttpServletRequest request) throws Exception{
		
		ActionUtil.requestToCondition(condition, request);
		String priceTableId = condition.getStringCondition("priceTableId");
		String parentId = condition.getStringCondition("parentId");
		String proModel = condition.getStringCondition("proModel");
		String inwall = condition.getStringCondition("inwall");
		String distribute = condition.getStringCondition("distribute");
		String isReport = condition.getStringCondition("isReport");//是否报备
		String productType = condition.getStringCondition("productType");//是否常规——1,
		String crmCategoryQuery = condition.getStringCondition("crmCategoryQuery");//CRM产品类别
		String categoryperm = "Y";//request.getParameter("categoryperm");//是否进行销售目录权限过滤
		
		String customerId = condition.getStringCondition("customerId");
		if(StringUtil.isNotEmpty(customerId)&&StringUtil.isNotEmpty(inwall)){
			ProductPriceHead productPriceHead = priceHeadService.getCustomerPriceHead(customerId);
			if(productPriceHead != null){
				priceTableId = productPriceHead.getId();
			}else {
				throw new AnneException("未维护客户价格表！");
			}
		}

		List<Object>  args = new ArrayList();
		StringBuffer sql = new StringBuffer(" select distinct ");
		sql.append(" lov.ROW_ID||p.C_PID||li.C_PID  as id ,");
		sql.append(" p.C_PID as proId ,");
		sql.append(" p.C_PRO_CODE productCode ,");
		sql.append(" p.C_PRO_NAME productName,");
		sql.append(" p.C_PRO_ENAME proEName,");
		sql.append(" p.C_PRO_DESC proDesc,");
		sql.append(" p.C_PRO_SALE_STATUS saleStatus,");
		sql.append(" p.C_UNIT unit,");
		sql.append(" p.C_PRO_CRM_CATEGORY crmCategory,");
		sql.append(" p.C_PRO_REQUEST_DESC proRequestDesc,");
		sql.append(" p.C_MATER_CODE materielCode,");
		sql.append(" p.C_VMATER_CODE vmaterCode,");
		sql.append(" p.N_GROSS_WEIGHT crossWeight,");
		sql.append(" p.N_NET_WEIGHT netWeight,");
		sql.append(" p.C_BRAND proBrand,");
		sql.append(" p.C_SALE_TYPE saleType,");
		sql.append(" p.C_PRO_MODEL proModel,");
		sql.append(" p.C_PRO_ERP_CATEGORY erpCategory,");
		sql.append(" p.C_CLIENT_CATEGORY clientCategory,");
		
		sql.append(" (select max(pl.N_CATALOG_PRICE) from CRM_T_PRICE_LINE pl "
				+ "	 where pl.C_PRO_ID = p.C_PID and pl.C_PRICE_HEAD_ID = ? ) as catalogPriceShow1,");//本身标准价格

		sql.append(" (select max(pl.N_LAYER1_PRICE) from CRM_T_PRICE_LINE pl "
				+ "	 where pl.C_PRO_ID = p.C_PID and pl.C_PRICE_HEAD_ID = ? ) as catalogPriceShow,"); //本身金牌价格

		if(StringUtil.isEmpty(priceTableId)){
			priceTableId= "";
		}
		args.add(priceTableId);
		args.add(priceTableId);
		
		sql.append(" li.C_PID proLineId,");
		sql.append(" li.C_PRO_LINE proLine,");
		sql.append(" li.C_PRO_GROUP proGroup,");
		sql.append(" li.C_PRO_CATEGORY cproCategory,");
		sql.append(" li.C_PRO_TYPE cproType,");
		sql.append(" li.C_PRO_SERIES proSeries,");
		sql.append(" li.C_PRO_POWCAP cproPowcap,");
		sql.append(" lov.NAME_PATH procatalogNamePath,");
		sql.append(" (SELECT MAX(OPT_TXT2) FROM SYS_T_LOV_MEMBER WHERE GROUP_CODE='PRODUCTMODE' and DELETE_FLAG='N' and LOV_NAME=p.C_PRO_MODEL) optTxt2");
		
		
		if(StringUtil.isEmpty(parentId)&&"4".equals(crmCategoryQuery)) {
			sql.append(" from (select ROW_ID, GROUP_ID, LEAF_FLAG, START_DATE, END_DATE, OPT_TXT5 , NAME_PATH , OPT_TXT2,PARENT_ID "
					+ " from SYS_T_LOV_MEMBER start with PARENT_ID in (select lv.ROW_ID from SYS_T_LOV_MEMBER lv, crm_t_product_permission_rel rl" 
	            +" where rl.PRODUCT_CATALOG_ID=lv.ROW_ID and lv.DELETE_FLAG='N' and lv.GROUP_CODE='procatalog' "
					+ " and (rl.ORG_ID=? or rl.ORG_ID=?))");
            args.add(this.getUserObject().getPosition().getPositionInOrgId());
            args.add(this.getUserObject().getOrg().getId());

			sql.append(" connect by prior PARENT_ID=ROW_ID ) lov ");
		}else if(StringUtil.isEmpty(parentId)){
			sql.append(" from (select ROW_ID, GROUP_ID, LEAF_FLAG, START_DATE, END_DATE, OPT_TXT5 , NAME_PATH , OPT_TXT2,PARENT_ID "
					+ " from SYS_T_LOV_MEMBER start with PARENT_ID in (select lv.ROW_ID from SYS_T_LOV_MEMBER lv, crm_t_product_permission_rel rl" 
	            +" where rl.PRODUCT_CATALOG_ID=lv.ROW_ID and lv.DELETE_FLAG='N' and lv.GROUP_CODE='procatalog' and lv.parent_id = 'ROOT'"
					+ " and (rl.ORG_ID=? or rl.ORG_ID=?))");
            args.add(this.getUserObject().getPosition().getPositionInOrgId());
            args.add(this.getUserObject().getOrg().getId());

			sql.append(" connect by prior ROW_ID = PARENT_ID) lov ");

		}else{

			sql.append(" from   ( select * from SYS_T_LOV_MEMBER m where m.row_id in ( select ROW_ID from (select  * "
					+ " from SYS_T_LOV_MEMBER start with PARENT_ID = ? ");
			args.add(parentId);

			sql.append(" connect by prior ROW_ID = PARENT_ID) " );

			sql.append( " )) lov ");
		}

		if (StringUtil.isNotEmpty(isReport)) {
			sql.append(" , (SELECT *" +
					"   FROM SYS_T_LOV_MEMBER" +
					"   WHERE GROUP_CODE = 'PRODUCTMODE' AND OPT_TXT2 = ? and DELETE_FLAG='N' ) m ");
			args.add(isReport);
		}

        sql.append(", CRM_T_PRODUCT_BASIC p left join CRM_T_PRODUCT_LINE li on p.C_PRO_LINE_ID = li.C_PID ");

		//非标产品根据用户类型过滤
        UserObject userObject = getUserObject();
        if(userObject != null && userObject.getOrg().getCodePath().contains("P_GJORG_B1_0001")){
            String sql_inner = "select distinct p1.* from"
                    +" crm_t_product_basic p1"
                    +" left join CRM_T_PRODUCT_DEMAND_REL dr on p1.c_pid = dr.c_product_id"
                    +" left join CRM_T_PRODUCT_DEMAND d on dr.c_demand_id = d.c_pid"
                    +" left join crm_t_custom_info c on c.c_pid = d.c_client_code"
                    +" left join CRM_T_TEAM t on c.c_pid = t.business_id"
                    +" left join SYS_T_PERMISSION_USER_REL u on t.position_id = u.member_id"
                    +" where p1.c_pro_crm_category != '0002' or (dr.C_PID is NULL or (p1.c_pro_crm_category = '0002' "
                    +" and u.user_id = '"+userObject.getEmployee().getId()+"'))";
            sql.append(" inner join ("+sql_inner+") pp on pp.c_pid = p.c_pid ");
        }
        
        if("Y".equals(categoryperm)){
        	sql.append(",crm_t_product_permission_rel pr ");        	
        }
        
        sql.append(" where lov.LEAF_FLAG = 'Y'");
        
        //公共选择产品组件 增加过滤条件  转销状态为已转销 以及 销售状态为正常销售得产品才显示在列表上
        sql.append(" and (p.C_PRO_SALE_STATUS = 'prodoneconvertsale' or p.C_PRO_SALE_STATUS = 'propreconvertsale') ")
        	.append(" and p.C_SALE_STATUS = '89d4488e5a1bc49e015a1c6e43570006'");
        	
        
        if("Y".equals(categoryperm)){
        	sql.append(" and lov.PARENT_ID = pr.PRODUCT_CATALOG_ID ");
    		sql.append(" and (pr.ORG_ID = ? or pr.ORG_ID = ?)  ");
            args.add(this.getUserObject().getPosition().getPositionInOrgId());
            args.add(this.getUserObject().getOrg().getId());
    		      	
        }
		sql.append(" and lov.GROUP_ID = 'procatalog'");
		sql.append(" and p.C_PID = lov.OPT_TXT5");
		sql.append(" and lov.START_DATE <= sysdate and nvl(lov.END_DATE,sysdate+1) >= sysdate ");

		if (StringUtil.isNotEmpty(isReport)) {
			sql.append(" AND m.LOV_NAME = p.C_PRO_MODEL ");
		}


		if(StringUtil.isNotEmpty(proModel)){
			sql.append(" and p.C_PRO_MODEL like ?");
			args.add("%"+proModel+"%");
		}
		String searchKey = condition.getStringCondition("searchKey");
		if(StringUtil.isNotEmpty(searchKey)){
			sql.append(" and ( p.C_PRO_CODE like ? ");
			args.add("%"+searchKey+"%");
			sql.append(" or p.C_PRO_NAME like ? ");
			args.add("%"+searchKey+"%");
			sql.append(" or p.C_PRO_DESC like ? ");
			args.add("%"+searchKey+"%");
			sql.append(" or p.C_PRO_MODEL like ? ");
			args.add("%"+searchKey+"%");
			sql.append(" or p.C_MATER_CODE like ? ");
			args.add("%"+searchKey+"%");
			sql.append(" or p.C_CLIENT_CATEGORY like ? ");
			args.add("%"+searchKey+"%");
			sql.append(" or li.C_PRO_POWCAP like ? )  ");
			args.add("%"+searchKey+"%");
		}
		
		String productCode = condition.getStringCondition("productCode");//产品编码
		String productName = condition.getStringCondition("productName");//产品名称
		String proBrand = condition.getStringCondition("proBrand");//品牌
		String crmCategory = condition.getStringCondition("crmCategory");//CRM产品类别
		//String crmCategoryQuery = condition.getStringCondition("crmCategoryQuery");//CRM产品类别
		String battery = condition.getStringCondition("battery");

		if(StringUtil.isNotEmpty(productCode)){
			sql.append(" and p.C_PRO_CODE like ? ");
			args.add("%"+productCode+"%");
		}
		if(StringUtil.isNotEmpty(productName)){
			sql.append(" and p.C_PRO_NAME like ? ");
			args.add("%"+productName+"%");
		}
		if(StringUtil.isNotEmpty(proBrand)){
			sql.append(" and p.C_BRAND like ? ");
			args.add("%"+proBrand+"%");
		}
		if(StringUtil.isNotEmpty(crmCategory)){
			sql.append(" and p.C_PRO_CRM_CATEGORY = ? ");
			args.add(crmCategory);
		}
		// 产品分类 常规
		if("0001".equals(crmCategoryQuery)&&"1".equals(productType)){
			sql.append(" and (p.C_PRO_CRM_CATEGORY = ? or p.C_PRO_CRM_CATEGORY = ? or p.C_PRO_CRM_CATEGORY = ?) ");
			sql.append(" and ( li.C_PRO_CATEGORY <> '电池' or li.c_pid is null ) ");
			sql.append(" and (p.c_pro_model not like ? or p.c_pro_model is null ) ");
			sql.append(" and (p.c_pro_model not like ? or p.c_pro_model is null ) ");
			args.add(crmCategoryQuery);
			args.add("0002");
			args.add("0005");//外购产品
			args.add("YDC%");
			args.add("YDE%");
		}
		
		
		
		// 产品分类 电池
		if(StringUtil.isNotEmpty(battery)&&"Y".equals(battery)){
			sql.append(" and li.C_PRO_CATEGORY = '电池' ");
			sql.append(" and p.c_pro_model not like ? and p.c_pro_model not like ? ");
			args.add("YDC%");
			args.add("YDE%");
		}
		if(StringUtil.isNotEmpty(distribute)&&"Y".equals(distribute)){
			sql.append(" and (p.c_pro_model like ? or p.c_pro_model like ? )");
			args.add("YDC%");
			args.add("YDE%");
		}
		//入围产品
		if(StringUtil.isNotEmpty(inwall)&&"Y".equals(inwall)){
			//取当前客户价格表包含的产品
			StringBuilder sql_inwall = new StringBuilder();
			sql_inwall.append( " and exists (select line.c_pro_id					");
			sql_inwall.append( " from crm_t_price_head head, crm_t_price_line line	");
			sql_inwall.append( " where head.c_pid = line.c_price_head_id			");
			sql_inwall.append( " and head.c_organization = ?						");
			sql_inwall.append( " and head.C_CLIENT_PRICE = '1'						");
			sql_inwall.append( " and line.c_pro_id = p.C_PID)						");

			sql.append(sql_inwall);
			ProductPriceHead productPriceHead = priceHeadService.getCustomerPriceHead(customerId);
			//String saleCenter = lovMemberService.getSaleCenter(getUserObject().getOrg().getId());
			//String saleCenter = lovMemberService.getSaleCenter(productPriceHead.getOrganization());
			//args.add(saleCenter);
			args.add(productPriceHead.getOrganization());
		}


		IPage page = baseDao.searchBySql4Map(sql.toString(), args.toArray(), condition.getRows(), condition.getPage());
	   
		List<Map<Object, Object>> list = ((List<Map<Object, Object>>)page.getList());
		List<KstarProductVO> taskList = new ArrayList<KstarProductVO>();
		for(Map<Object, Object> map :  list){
			Map<Object, Object> map1 = new CaseInsensitiveMap(); 
			
			for (Object key : map.keySet()) {
				map1.put(key, map.get(key));
			}
			taskList.add((KstarProductVO)BeanUtils.convertMap(KstarProductVO.class, map1));
		}
		
		((PageImpl)page).setList(taskList);
		
		return sendSuccessMessage(page);
	    
		
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/productTree")
	public String tree(Condition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		
		String parentId = condition.getStringCondition("id");
		
		UserObject user = getUserObject();
		user.getPosition().getId();
		user.getOrg().getId();
		StringBuffer hql = new StringBuffer("select distinct pc from LovMember pc ,PermissionRel pr "
				+ " where pr.productCatalogId = pc.id and "
				+ " pc.groupCode = 'procatalog' "
				+ " and ( pr.orgId = ? or pr.orgId = ? )  ");
		List<Object> args = new ArrayList<>();
		args.add(user.getPosition().getId());
		args.add(user.getOrg().getId());
		if(StringUtil.isNotEmpty(parentId)){
			hql.append(" and pc.parentId = ?");
			args.add(parentId);
		}
		List<LovMember> list = baseDao.findEntity(hql.toString(),args.toArray());
		
		return sendSuccessMessage(list);
	}
	
	@NoRight
	@RequestMapping("/selectNoPrdList")
	public String selectNoPrdList(String pickerId,Model model,HttpServletRequest request){
		String priceTableId = request.getParameter("priceTableId");
		model.addAttribute("pickerId",pickerId);
		model.addAttribute("priceTableId",priceTableId);
		
		return forward("noStdProductSelectLst");
	}
	
	/*
	 * 非标和外购产品选择
	 */
	@NoRight
	@ResponseBody
	@RequestMapping("/noStdProductSelectLstPage")
	public String noStdProductSelectLst(PageCondition condition,HttpServletRequest request) throws Exception{
		ActionUtil.requestToCondition(condition, request);
		List<Object> args = new ArrayList<>();
		StringBuilder sql = new StringBuilder("select");
		sql.append(" p.c_pid \"id\",");
		sql.append(" p.c_pro_code \"proCode\",");
		sql.append(" p.c_pro_name \"proName\",");
		sql.append(" p.C_PRO_MODEL \"proModel\",");
		sql.append(" p.c_brand \"proBrand\",");
		sql.append(" p.C_PRO_CRM_CATEGORY \"crmCategory\",");
		sql.append(" p.C_MATER_CODE \"materielCode\",");
		sql.append(" m1.lov_name \"crmCategoryLable\",");
		sql.append(" m2.lov_name \"erpCategory\",");
		sql.append(" m3.lov_name \"publishStatus\",");
		sql.append(" m4.lov_name \"priceStatus\",");
		sql.append(" m5.lov_name \"saleStatus\",");
		sql.append(" l.C_PRO_CATEGORY \"cproCategory\",");
		sql.append(" l.C_PRO_TYPE \"cproType\",");
		sql.append(" l.C_PRO_SERIES \"proSeries\",");
		sql.append(" p.C_CLIENT_CATEGORY \"clientCategory\",");
		sql.append(" p.C_PRO_DESC \"proDesc\"");
		sql.append(" from crm_t_product_basic p");

		//非标产品根据用户类型过滤
		UserObject userObject = getUserObject();
		if(userObject != null && userObject.getOrg().getCodePath().contains("P_GJORG_B1_0001")){
			String sql_inner = "select distinct p1.* from"
					+" crm_t_product_basic p1"
					+" left join CRM_T_PRODUCT_DEMAND_REL dr on p1.c_pid = dr.c_product_id"
					+" left join CRM_T_PRODUCT_DEMAND d on dr.c_demand_id = d.c_pid"
					+" left join crm_t_custom_info c on c.c_pid = d.c_client_code"
					+" left join CRM_T_TEAM t on c.c_pid = t.business_id"
					+" left join SYS_T_PERMISSION_USER_REL u on t.position_id = u.member_id"
					+" where p1.c_pro_crm_category != '0002' or (dr.C_PID is NULL or (p1.c_pro_crm_category = '0002' "
					+" and u.user_id = '"+userObject.getEmployee().getId()+"'))";
			sql.append(" inner join ("+sql_inner+") pp on pp.c_pid = p.c_pid ");
		}
		//关键字搜索
		sql.append(" inner join CRM_T_TEAM t on p.c_pid = t.business_id and t.position_id = '"+getUserObject().getPosition().getId()+"' ");
		String searchKey = condition.getStringCondition("searchKey").trim(); 
		
		//产品条件查询
		if(StringUtil.isNotEmpty(searchKey)){
			sql.append(" and ( p.C_PRO_CODE like ? ");
			args.add("%"+searchKey+"%");
			sql.append(" or p.C_PRO_NAME like ? ");
			args.add("%"+searchKey+"%");
			sql.append(" or p.C_PRO_DESC like ? ");
			args.add("%"+searchKey+"%");
			sql.append(" or p.C_PRO_MODEL like ? ");
			args.add("%"+searchKey+"%");
			sql.append(" or p.C_MATER_CODE like ? ");
			args.add("%"+searchKey+"%");
			sql.append(" or p.C_CLIENT_CATEGORY like ? ) ");
			args.add("%"+searchKey+"%");
		}
		
		sql.append(" left join crm_t_product_line l on p.c_pro_line_id = l.c_pid");
		sql.append(" left join sys_t_lov_member m1 on p.C_PRO_CRM_CATEGORY = m1.lov_code and m1.group_code = 'crmCategory'");
		sql.append(" left join sys_t_lov_member m2 on p.C_PRO_ERP_CATEGORY = m2.lov_code and m2.group_code = 'erpCategory'");
		sql.append(" left join sys_t_lov_member m3 on p.C_PRO_PUBLISH_STATUS = m3.row_id");
		sql.append(" left join sys_t_lov_member m4 on p.C_PRO_PRICE_STATUS = m4.row_id");
		sql.append(" left join sys_t_lov_member m5 on p.C_PRO_SALE_STATUS = m5.row_id");
//		sql.append(" inner  join  CRM_T_PRODUCT_DEMAND_REL r on r.c_product_id = p.c_pid ");
		sql.append(" where m1.lov_code = '0002' or m1.lov_code = '0005' ");
//		args.add(user.getPosition().getId());
//		this.addQueryCondition(condition, args, sql, "name", "m1.lov_name", "like");
//		this.addQueryCondition(condition, args, sql, "productCode", "p.c_pro_code", "like");
//		this.addQueryCondition(condition, args, sql, "productName", "p.c_pro_name", "like");
//		this.addQueryCondition(condition, args, sql, "proModel", "p.c_pro_model", "like");
//		this.addQueryCondition(condition, args, sql, "proBrand", "p.C_BRAND", "like");
//		this.addQueryCondition(condition, args, sql, "crmCategory", "p.c_pro_crm_category", "=");
//		this.addQueryCondition(condition, args, sql, "erpCategory", "p.C_PRO_ERP_CATEGORY", "=");
//		this.addQueryCondition(condition, args, sql, "publishStatus", "p.C_PRO_PUBLISH_STATUS", "=");
//		this.addQueryCondition(condition, args, sql, "priceStatus", "p.C_PRO_PRICE_STATUS", "=");
//		this.addQueryCondition(condition, args, sql, "saleStatus", "p.C_PRO_SALE_STATUS", "=");
//		this.addQueryCondition(condition, args, sql, "clientCategory", "p.C_CLIENT_CATEGORY", "like");
//		this.addQueryCondition(condition, args, sql, "cproCategory", "l.C_PRO_CATEGORY", "like");
//		this.addQueryCondition(condition, args, sql, "cproType", "l.C_PRO_TYPE", "like");
//		this.addQueryCondition(condition, args, sql, "proSeries", "l.C_PRO_SERIES", "like");
		
	    IPage page = baseDao.searchBySql4Map(sql.toString(), args.toArray(),condition.getRows(),condition.getPage());
	   
//		List<Map<Object, Object>> list = ((List<Map<Object, Object>>)page.getList());
//		List<KstarProductVO> taskList = new ArrayList<KstarProductVO>();
//		for(Map<Object, Object> map :  list){
//			Map<Object, Object> map1 = new CaseInsensitiveMap(); 
//			
//			for (Object key : map.keySet()) {
//				map1.put(key, map.get(key));
//			}
//			taskList.add((KstarProductVO)BeanUtils.convertMap(KstarProductVO.class, map1));
//		}
//		
//		((PageImpl)page).setList(taskList);
		
		return sendSuccessMessage(page);
	}
	

	@NoRight
	@RequestMapping("/installAuxPrdSelectLst")
	public String getInstallAuxPrdSelectLst(String pickerId, Model model,HttpServletRequest request){

		String namePathStr=IConstants.CONTR_PRJLST_AUX_NAME_PATH_STR;
		Condition condition = new Condition();
//		condition.setCondition("groupCode","procatalog");

//		condition.getFilterObject().addOrCondition("codePath", "like", "%"+custCode+"%");
		condition.getFilterObject().addCondition("groupCode", "eq", "procatalog");
		condition.getFilterObject().addCondition("namePath", "like", namePathStr+"%");
		List<LovMember> list = lovMemberService.getList(condition);
		model.addAttribute("procatalogList",JSON.toJSONString(list));
		
		model.addAttribute("pickerId",pickerId);
		
		return forward("installAuxProdMultiSelectList");
	}
	

	@NoRight
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@RequestMapping("/installAuxLstpage")
	public String installAuxPrdSelectLstPage(PageCondition condition,HttpServletRequest request) throws Exception{
		
		ActionUtil.requestToCondition(condition, request);

		String namePathStr=IConstants.CONTR_PRJLST_AUX_NAME_PATH_STR;
		
		List<Object>  args = new ArrayList();
		StringBuffer sql = new StringBuffer(" select distinct ");
		sql.append(" lov.ROW_ID||p.C_PID||li.C_PID  as id ,");
		sql.append(" p.C_PID as proId ,");
		sql.append(" p.C_PRO_CODE productCode ,");
		sql.append(" p.C_PRO_NAME productName,");
		sql.append(" p.C_PRO_ENAME proEName,");
		sql.append(" p.C_PRO_DESC proDesc,");
		sql.append(" p.C_PRO_SALE_STATUS saleStatus,");
		sql.append(" p.C_UNIT unit,");
		sql.append(" p.C_PRO_CRM_CATEGORY crmCategory,");
		sql.append(" p.C_PRO_REQUEST_DESC proRequestDesc,");
		sql.append(" p.C_MATER_CODE materielCode,");
		sql.append(" p.C_VMATER_CODE vmaterCode,");
		sql.append(" p.N_GROSS_WEIGHT crossWeight,");
		sql.append(" p.N_NET_WEIGHT netWeight,");
		sql.append(" p.C_BRAND proBrand,");
		sql.append(" p.C_SALE_TYPE saleType,");
		sql.append(" p.C_PRO_MODEL proModel,");
		sql.append(" p.C_PRO_ERP_CATEGORY erpCategory,");
		sql.append(" p.C_CLIENT_CATEGORY clientCategory,");
		
		sql.append(" '' as catalogPriceShow1,");//本身标准价格
		
		sql.append(" '' as catalogPriceShow,"); //本身金牌价格
		
		sql.append(" li.C_PID proLineId,");
		sql.append(" li.C_PRO_LINE proLine,");
		sql.append(" li.C_PRO_GROUP proGroup,");
		sql.append(" li.C_PRO_CATEGORY cproCategory,");
		sql.append(" li.C_PRO_TYPE cproType,");
		sql.append(" li.C_PRO_SERIES proSeries,");
		sql.append(" li.C_PRO_POWCAP cproPowcap,");
		sql.append(" lov.NAME_PATH procatalogNamePath");
		
//		sql.append(" from (select ROW_ID, GROUP_ID, LEAF_FLAG, START_DATE, END_DATE, OPT_TXT5 , NAME_PATH , OPT_TXT2"
//				+ " from SYS_T_LOV_MEMBER start with PARENT_ID in (select lv.ROW_ID from SYS_T_LOV_MEMBER lv, crm_t_product_permission_rel rl" 
//            +" where rl.PRODUCT_CATALOG_ID=lv.ROW_ID and lv.DELETE_FLAG='N' and lv.GROUP_CODE='procatalog' " 
//            +" and lv.name_path = '"+namePathStr+"') connect by prior ROW_ID = PARENT_ID) lov ");
//		sql.append(" from (select ROW_ID, GROUP_ID, LEAF_FLAG, START_DATE, END_DATE, OPT_TXT5 , NAME_PATH , OPT_TXT2 from SYS_T_LOV_MEMBER  where name_path like '"+namePathStr+"%') lov ");
		sql.append(" from SYS_T_LOV_MEMBER lov ") ; 
		sql.append(", CRM_T_PRODUCT_BASIC p left join CRM_T_PRODUCT_LINE li on p.C_PRO_LINE_ID = li.C_PID ");
		sql.append(" where lov.LEAF_FLAG = 'Y'");
		sql.append(" and lov.GROUP_ID = 'procatalog'");
		sql.append(" and p.C_PID = lov.OPT_TXT5");
		sql.append(" and lov.name_path like '"+namePathStr+"%'");
		sql.append(" and lov.START_DATE <= sysdate and nvl(lov.END_DATE,sysdate+1) >= sysdate ");
		
		String searchKey = condition.getStringCondition("searchKey");
		if(StringUtil.isNotEmpty(searchKey)){
			sql.append(" and ( p.C_PRO_CODE like ? ");
			args.add("%"+searchKey+"%");
			sql.append(" or p.C_PRO_NAME like ? ");
			args.add("%"+searchKey+"%");
			sql.append(" or p.C_PRO_DESC like ? ");
			args.add("%"+searchKey+"%");
			sql.append(" or p.C_PRO_MODEL like ? ");
			args.add("%"+searchKey+"%");
			sql.append(" or p.C_MATER_CODE like ? ");
			args.add("%"+searchKey+"%");
			sql.append(" or p.C_CLIENT_CATEGORY like ? ");
			args.add("%"+searchKey+"%");
			sql.append(" or li.C_PRO_POWCAP like ? )  ");
			args.add("%"+searchKey+"%");
		}
		
		String productCode = condition.getStringCondition("productCode");//产品编码
		String productName = condition.getStringCondition("productName");//产品名称
		String proBrand = condition.getStringCondition("proBrand");//品牌

		if(StringUtil.isNotEmpty(productCode)){
			sql.append(" and p.C_PRO_CODE like ? ");
			args.add("%"+productCode+"%");
		}
		if(StringUtil.isNotEmpty(productName)){
			sql.append(" and p.C_PRO_NAME like ? ");
			args.add("%"+productName+"%");
		}
		if(StringUtil.isNotEmpty(proBrand)){
			sql.append(" and p.C_BRAND like ? ");
			args.add("%"+proBrand+"%");
		}

	    IPage page = baseDao.searchBySql4Map(sql.toString(), args.toArray(),condition.getRows(),condition.getPage());
	   
		List<Map<Object, Object>> list = ((List<Map<Object, Object>>)page.getList());
		List<KstarProductVO> taskList = new ArrayList<KstarProductVO>();
		for(Map<Object, Object> map :  list){
			Map<Object, Object> map1 = new CaseInsensitiveMap(); 
			for (Object key : map.keySet()) {
				map1.put(key, map.get(key));
			}
			taskList.add((KstarProductVO)BeanUtils.convertMap(KstarProductVO.class, map1));
		}
		((PageImpl)page).setList(taskList);
		
		return sendSuccessMessage(page);
	    
		
	}
	
	
}
