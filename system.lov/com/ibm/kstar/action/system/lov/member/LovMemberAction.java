package com.ibm.kstar.action.system.lov.member;

import com.ibm.kstar.api.system.lov.ILovGroupService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovGroup;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.interceptor.system.permission.NoRight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.log.LogOperate;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.ExcelUtil;
import org.xsnake.web.utils.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/lov/member")
public class LovMemberAction extends BaseAction {

	@Autowired
	protected ILovMemberService lovMemberService;

	@Autowired
	protected ILovGroupService lovGroupService;

	@NoRight
	@LogOperate(module = "字典模块", notes = "${user}查看数据分页信息")
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		String groupId = condition.getStringCondition("groupId");
		String parentId = condition.getStringCondition("parentId");
		String searchKey = condition.getStringCondition("searchKey");
		// condition.getFilterObject().addCondition("deleteFlag", "eq", "N");
		condition.getFilterObject().addCondition("groupId", "eq", groupId);
		if (parentId != null) {
			condition.getFilterObject().addCondition("path", "like", "%" + parentId + "%");
		}
		if (searchKey != null) {
			condition.getFilterObject().addOrCondition("name", "like", "%" + searchKey + "%");
			condition.getFilterObject().addOrCondition("code", "like", "%" + searchKey + "%");
		}

		condition.getFilterObject().addOrderBy("no", "desc");
		IPage p = lovMemberService.query(condition);
		return sendSuccessMessage(p);
	}

	@RequestMapping("/list")
	public String list(String groupId, Model model) {
		LovGroup lovGroup = lovGroupService.get(groupId);
		model.addAttribute("lovGroup", lovGroup);
		return forward("list");
	}
	
	@NoRight
	@RequestMapping("/list2")
	public String list2(String groupCode, Model model) {
		LovGroup lovGroup = lovGroupService.getByCode(groupCode);
		model.addAttribute("lovGroup", lovGroup);
		return forward("list");
	}

	/**
	 *
	 * importReceipts:导入收款. <br/>
	 *
	 * @author liming
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @since JDK 1.7
	 */
	@ResponseBody
	@RequestMapping("/importProductModel")
	public String importProductModels(HttpServletRequest request,HttpServletResponse response) throws Exception{

		List<List<Object>> dataList = this.getExcelData(request);
        lovMemberService.importProductModels(dataList, getUserObject());
        return sendSuccessMessage("导入成功");
	}

	@ResponseBody
	@RequestMapping("/exportProductModel")
	public String exportProductModels(HttpServletRequest request,HttpServletResponse response) throws Exception{
        Condition condition = new Condition();
        ActionUtil.requestToCondition(condition, request);
        LovGroup group = lovGroupService.getByCode("PRODUCTMODE");
        String groupId = group.getId();
        condition.getFilterObject().addCondition("groupId", "eq", groupId);

        String parentId = condition.getStringCondition("parentId");
        String searchKey = condition.getStringCondition("searchKey");
        // condition.getFilterObject().addCondition("deleteFlag", "eq", "N");
        if (parentId != null) {
            condition.getFilterObject().addCondition("path", "like", "%" + parentId + "%");
        }
        if (searchKey != null) {
            condition.getFilterObject().addOrCondition("name", "like", "%" + searchKey + "%");
            condition.getFilterObject().addOrCondition("code", "like", "%" + searchKey + "%");
        }

        condition.getFilterObject().addOrderBy("no", "desc");
        List<LovMember> list = lovMemberService.find(condition);

        try{
			List<List<Object>> dataList  = lovMemberService.getExcelData(list);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
			ExcelUtil.exportExcel(response, dataList, "产品型号_"+sdf.format(new Date()));
		}catch(Exception e){
			e.printStackTrace();
			throw new AnneException("导出失败");
		}
        return null;
	}

	@NoRight
	@RequestMapping("/productModelImportTemplet")
	public void importproductModelTemplet(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		List<List<Object>> dataList  = lovMemberService.getProductModelImportTemplet();
		ExcelUtil.exportExcel(response, dataList, "产品型号导入模板_"+sdf.format(new Date()));
	}


	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(String id) {
		lovMemberService.delete(id);
		return sendSuccessMessage();
	}

	@ResponseBody
	@RequestMapping(value = "/deletes", method = RequestMethod.POST)
	public String deletes(@RequestParam(value = "ids[]") String[] ids) {
		lovMemberService.deletes(ids);
		return sendSuccessMessage();
	}

	@ResponseBody
	@RequestMapping(value = "/deleteTree", method = RequestMethod.POST)
	public String deleteTree(String treeId,String groupId) {
		lovMemberService.deleteTree(treeId,groupId);
		return sendSuccessMessage();
	}

	@LogOperate(module = "字典模块", notes = "${user}打开新增页面")
	@RequestMapping("/add")
	public String add(String groupId, String parentId, Model model) {
		LovGroup lovGroup = lovGroupService.get(groupId);
		model.addAttribute("lovGroup", lovGroup);
		model.addAttribute("dictionary", "Y");
		if (StringUtil.isNotEmpty(parentId)) {
			LovMember parentLovMember = lovMemberService.get(parentId);
			model.addAttribute("parentLovMember", parentLovMember);
		}
		return forward("lovMember");
	}

	@LogOperate(module = "字典模块", notes = "${user}提交了新增数据")
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(LovMember lovMember, HttpServletRequest request) {
		lovMemberService.save(lovMember);
		return sendSuccessMessage();
	}

	@RequestMapping("/edit")
	public String edit(String id, Model model) {
		LovMember lovMember = lovMemberService.get(id);
		LovGroup lovGroup = lovGroupService.get(lovMember.getGroupId());
		model.addAttribute("lovGroup", lovGroup);
		model.addAttribute("lovMember", lovMember);
		model.addAttribute("dictionary", "Y");
		if (StringUtil.isNotEmpty(lovMember.getParentId())) {
			LovMember parentLovMember = lovMemberService.get(lovMember.getParentId());
			model.addAttribute("parentLovMember", parentLovMember);
		}
		return forward("lovMember");
	}

	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(LovMember lovMember, HttpServletRequest request) {
		lovMemberService.update(lovMember);
		return sendSuccessMessage();
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/selectTree")
	public String selectTree(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		List<Map<String, Object>> list = lovMemberService.getSelectList(condition, this.getUserObject());
		return sendSuccessMessage(list);
	}
	
	/**
	 * 组织树
	 * @param condition
	 * @param request
	 * @return
	 */
	@NoRight
	@ResponseBody
	@RequestMapping("/selectPriceOrgTree")
	public String selectPriceOrgTree(PageCondition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		List<Map<String, Object>> list = lovMemberService.getSelectPriceOrgList(condition, this.getUserObject());
		return sendSuccessMessage(list);
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("/tree")
	public String tree(Condition condition, HttpServletRequest request) {
		String opType = request.getParameter("opType");
		if("2".equals(opType)){
			return contructTree(condition,request,2);
		}else{
			return contructTree(condition,request,1);
		}
	}
	@NoRight
	@ResponseBody
	@RequestMapping("/treeForPermset")
	public String treeForPermset(Condition condition, HttpServletRequest request) {
		return contructTree(condition,request,2);
	}
	private String contructTree(Condition condition, HttpServletRequest request,int optType){
		ActionUtil.requestToCondition(condition, request);
		String groupCode = condition.getStringCondition("groupCode");
		String groupId = condition.getStringCondition("groupId");
		String leafFlag = condition.getStringCondition("leafFlag");
		if (StringUtil.isEmpty(groupId) && StringUtil.isEmpty(groupCode)) {
			throw new AnneException("无效的参数访问");
		}
		if (StringUtil.isEmpty(groupId)) {
			LovGroup group = lovGroupService.getByCode(groupCode);
			if (group == null) {
				throw new AnneException("无效的参数访问");
			}
			groupId = group.getId();
		}
		String parentId = condition.getStringCondition("id");
		String rootId = condition.getStringCondition("rootId");
		condition.getFilterObject().addCondition("groupId", "=", groupId);
		condition.getFilterObject().addCondition("deleteFlag", "=", "N");
		if (parentId == null && StringUtil.isEmpty(rootId)) {
			parentId = "ROOT";
		}else if( parentId == null && (!StringUtil.isEmpty(rootId))){
			condition.getFilterObject().addCondition("id", "=", rootId);
		}

		if (StringUtil.isNotEmpty(leafFlag)) {
			condition.getFilterObject().addCondition("leafFlag", "=", leafFlag);
		}
		
		if(StringUtil.isNotEmpty(parentId)){
			condition.getFilterObject().addCondition("parentId", "=", parentId);
		}
		List<LovMember> list = null;
		
		if(optType==2||(this.getUserObject().getPermissionCodeMap().containsKey("PackPRODUCTSPECATALOG")&&
				("procatalog".equals(groupId)||"ORG".equals(groupId)||"proLibCatalog".equals(groupId)||"PRODUCT_DATABASE".equals(groupId))))
			list = lovMemberService.getList(condition);
		else
			list = lovMemberService.getListForUpdate(condition,this.getUserObject());
			//list = lovMemberService.getList(condition);
		return sendSuccessMessage(list);
	}
	@NoRight
	@ResponseBody
	@RequestMapping("/treeRight")
	public String treeRight(Condition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		String groupId = condition.getStringCondition("groupId");
		String leafFlag = condition.getStringCondition("leafFlag");
		if (StringUtil.isEmpty(groupId)) {
			throw new AnneException("无效的参数访问");
		}
		String parentId = condition.getStringCondition("id");
		condition.getFilterObject().addCondition("groupId", "=", groupId);
		condition.getFilterObject().addCondition("deleteFlag", "=", "N");
		if (parentId == null) {
			parentId = "ROOT";
		}
		if (StringUtil.isNotEmpty(leafFlag)) {
			condition.getFilterObject().addCondition("leafFlag", "=", leafFlag);
		}		
		if(StringUtil.isNotEmpty(parentId)){
			condition.getFilterObject().addCondition("parentId", "=", parentId);
		}
		List<LovMember> list = lovMemberService.getList(condition, this.getUserObject());
		return sendSuccessMessage(list);
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/treeWithRoot")
	public String treeWithRoot(Condition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		String parentId = condition.getStringCondition("id");
		if (StringUtil.isEmpty(parentId)) {
			List<LovMember> list = new ArrayList<>();
			LovMember lovMember = new LovMember();
			lovMember.setId("ROOT");
			lovMember.setName("/");
			lovMember.setLeafFlag("N");

			list.add(lovMember);
			return sendSuccessMessage(list);
		} else {
			return this.tree(condition, request);
		}
	}

	@NoRight
	@ResponseBody
	@RequestMapping(value = "/move", method = RequestMethod.POST)
	public String move(String dragNodeId, String newParentNodeId) {
		lovMemberService.move(dragNodeId, newParentNodeId);
		return sendSuccessMessage();
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/select")
	public String select(Condition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		String code = condition.getStringCondition("code");
		String parentId = condition.getStringCondition("parentCode");
		String level = condition.getStringCondition("level");
		condition.getFilterObject().addCondition("groupCode", "=", code);
		condition.getFilterObject().addCondition("parentId", "=", parentId);
		condition.getFilterObject().addCondition("level", "=", level);
		String codes = condition.getStringCondition("codes");
		if (StringUtil.isNotEmpty(codes)) {
			String[] split = codes.split(",");
			for (String s : split) {
				condition.getFilterObject().addOrCondition("code", "=", s);
			}
		}
		String optTxt1s = condition.getStringCondition("optTxt1s");
		if (StringUtil.isNotEmpty(optTxt1s)) {
			String[] split = optTxt1s.split(",");
			for (String s : split) {
				condition.getFilterObject().addOrCondition("optTxt1", "=", s);
			}
		}
		List<LovMember> list = lovMemberService.getList(condition);
		return sendSuccessMessage(list);
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/selectSkip")
	public String selectSkip(Condition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		String code = condition.getStringCondition("code");
		String parentId = condition.getStringCondition("parentId");
		String level = condition.getStringCondition("level");
		condition.getFilterObject().addCondition("groupCode", "=", code);
		condition.getFilterObject().addCondition("parentId", "=", parentId);
		condition.getFilterObject().addCondition("level", "=", level);
		List<LovMember> list = lovMemberService.getSkipList(condition);
		return sendSuccessMessage(list);
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/selectLpcOrg")
	public String selectLpcOrg(Condition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		List<LovMember> list = lovMemberService.selectLpcOrg(condition);
		return sendSuccessMessage(list);
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/selectLpcLinePosition")
	public String selectLpcLinePosition(Condition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		List<LovMember> list = lovMemberService.selectLpcLinePosition(condition);
		return sendSuccessMessage(list);
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/selectLpcLineName")
	public String selectLpcLineName(Condition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		List<LovMember> list = lovMemberService.selectLpcLineName(condition);
		return sendSuccessMessage(list);
	}

	@ResponseBody
	@RequestMapping("/importLov")
	public String importLov(HttpServletRequest request) throws Exception {
		List<List<Object>> list = getExcelData(request);
		lovMemberService.importData(list);
		return sendSuccessMessage();
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/autocomplete")
	public String autocomplete(Condition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		String code = condition.getStringCondition("code");
		String search = condition.getStringCondition("search");
		String optTxt1 = condition.getStringCondition("optTxt1");
		condition.getFilterObject().addCondition("groupCode", "=", code);
		condition.getFilterObject().addCondition("deleteFlag", "=", "N");
		if (StringUtil.isNotEmpty(search)) {
			condition.getFilterObject().addOrCondition("name", "like", "%" + search + "%");
			condition.getFilterObject().addOrCondition("code", "like", "%" + search + "%");
		}

		if (StringUtil.isNotEmpty(optTxt1)) {
			condition.getFilterObject().addCondition("optTxt1", "=", optTxt1);
		}

		String leafFlag = condition.getStringCondition("leafFlag");
		if (StringUtil.isNotEmpty(leafFlag)) {
			condition.getFilterObject().addCondition("leafFlag", "=", leafFlag);
		}

		List<LovMember> list = lovMemberService.getList30(condition);
		return sendSuccessMessage(list);
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/autocompleteDept")
	public String selectDeptByType(String optTxt3, String type, Condition condition, HttpServletRequest request) {
		ActionUtil.requestToCondition(condition, request);
		String search = String.valueOf(condition.getCondition("search"));
		List<LovMember> lovMembers = lovMemberService.getDept(optTxt3, type, search);
		return sendSuccessMessage(lovMembers);
	}

	@NoRight
	@ResponseBody
	@RequestMapping("/getSaleCenters")
	public String getSaleCenters(HttpServletRequest request) {
		List<LovMember> lovMembers = lovMemberService.getSaleCenters();
		return sendSuccessMessage(lovMembers);
	}

	@ResponseBody
	@RequestMapping("/getValuesByCell")
	public String getValuesByCell(String value, String groupCode) {

		LovMember lov = new LovMember();
		String result = "";
		Object obj = CacheData.getInstance().getMember(groupCode,value) ;
		if (null != obj) {
			BeanUtils.copyPropertiesIgnoreNull(obj, lov);
			result = lov.getName();
		} else {
			Condition condition = new Condition();
			condition.getFilterObject().addCondition("groupCode", "=", groupCode);
			condition.getFilterObject().addCondition("code", "=", value);
			condition.getFilterObject().addCondition("deleteFlag", "=", "N");
			List<LovMember> list = lovMemberService.getList(condition);
			if (list != null && list.size() > 0) {
				result = list.get(0).getName();
			}
		}
		return sendSuccessMessage(result);
	}
	
	/**
	 * 获取业务部门列表
	 * @param request
	 * @return
	 */
	@NoRight
	@ResponseBody
	@RequestMapping("/getBizDeptList")
	public String getBizDeptList(HttpServletRequest request) {
		List<LovMember> lovMembers = lovMemberService.getBizDeptList();
		return sendSuccessMessage(lovMembers);
	}
	
	/**
	 * 获取所有目录
	 * @param request
	 * @return
	 */
	@NoRight
	@ResponseBody
	@RequestMapping("/getAllCatalogList")
	public String getAllCatalogList(Condition condition,HttpServletRequest request) {
		List<LovMember> list = lovMemberService.getAllCatalogList();
		return sendSuccessMessage(list);
	}
}
