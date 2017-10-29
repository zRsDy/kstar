package com.ibm.kstar.action.product.database;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.upload.IUploadFile;
import org.xsnake.web.upload.UploadUtils;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.StringUtil;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.api.product.database.IProductDatabaseService;
import com.ibm.kstar.api.product.permission.IProductPermissionService;
import com.ibm.kstar.api.system.lov.ILovGroupService;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovGroup;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/product/database")
public class ProductDatabaseAction extends BaseAction{
	
	@Autowired
	ILovGroupService lovGroupService; 
	
	@Autowired
	ILovMemberService lovMemberService; 
	
	@Autowired
	IProductDatabaseService productDatabaseService;
	
	@Autowired
	IProductPermissionService productPermissionService;
	
	@NoRight
	@RequestMapping("list")
	public String list(Model model){
		if(!"admin".equals(getUserObject().getEmployee().getNo())){
			List<LovMember> list = productDatabaseService.getMyProductCatalog(getUserObject().getPosition().getOptTxt1(),getUserObject().getOrg().getId());
			model.addAttribute("catalogList",JSON.toJSONString(list));
		}
		return forward("list");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping("page")
	public String page(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		return sendSuccessMessage(productDatabaseService.search(condition,hasPermission("P01-1DatabaseManager"),getUserObject().getPosition().getOptTxt1()));
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value = "selectedPage" )
	public String selectedPage(PageCondition condition,HttpServletRequest request){
		ActionUtil.requestToCondition(condition, request);
		return sendSuccessMessage(productDatabaseService.getPermissionPage(condition));
	}
	
	@NoRight
	@RequestMapping("/config")
	public String config(String productCatalogId,String type,HttpServletRequest request,Model model) {
		List<LovMember> selectedPermissionList = productDatabaseService.getPermissionList(productCatalogId,type);
		model.addAttribute("selectedPermissionList",selectedPermissionList);
		model.addAttribute("selectedPermissions",JSON.toJSONString(selectedPermissionList));
		List<LovMember> allPermissionList = productPermissionService.getAllPermissionList();
		model.addAttribute("allPermissionList",allPermissionList);
		model.addAttribute("allPermissions",JSON.toJSONString(allPermissionList));
		model.addAttribute("productCatalogId",productCatalogId);
		return forward("config");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value="/config" ,method=RequestMethod.POST)
	public String config(String productCatalogId,String permissions,String type,Model model) {
		productDatabaseService.configPermission(productCatalogId,permissions.split(","),type);
		return sendSuccessMessage();
	}
	
	@NoRight
	@RequestMapping("add")
	public String add(String parentId,Model model){
		LovGroup lovGroup = lovGroupService.get("PRODUCT_DATABASE");
		LovMember parentLovMember = lovMemberService.get(parentId);
		model.addAttribute("lovGroup",lovGroup);
		model.addAttribute("parentLovMember",parentLovMember);
		return forward("add");
	}
	
	@NoRight
	@RequestMapping("addFile")
	public String addFile(String parentId,Model model){
		LovMember parentLovMember = lovMemberService.get(parentId);
		model.addAttribute("parentLovMember",parentLovMember);
		return forward("addFile");
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value = "/addFile", method = RequestMethod.POST)
	public String addFile(String parentId,Model model,HttpServletRequest request){
		List<IUploadFile> list = UploadUtils.uploadFile(request, "/product");
		if(list.size() == 0){
			throw new AnneException("文件不能为空");
		}
		productDatabaseService.saveFile(parentId, list, getUserObject().getEmployee().getId());
		return sendSuccessMessage();
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value = "/deleteFile", method = RequestMethod.POST)
	public String deleteFile(String id,Model model){
		productDatabaseService.deleteFile(id);
		return sendSuccessMessage();
	}
	
	@NoRight
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(LovMember lovMember, HttpServletRequest request) {
		lovMember.setCode(StringUtil.getUUID());
		lovMemberService.save(lovMember);
		return sendSuccessMessage();
	}
	
	@NoRight
	@RequestMapping("/edit")
	public String edit(String id, Model model) {
		LovMember lovMember = lovMemberService.get(id);
		LovGroup lovGroup = lovGroupService.get(lovMember.getGroupId());
		model.addAttribute("lovGroup", lovGroup);
		model.addAttribute("lovMember", lovMember);
		if (StringUtil.isNotEmpty(lovMember.getParentId())) {
			LovMember parentLovMember = lovMemberService.get(lovMember.getParentId());
			model.addAttribute("parentLovMember", parentLovMember);
		}
		return forward("add");
	}

	
	@NoRight
	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(LovMember lovMember, HttpServletRequest request) {
		lovMemberService.update(lovMember);
		return sendSuccessMessage();
	}

	@NoRight
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(String id) {
		if(productDatabaseService.count(id) > 0){
			return sendErrorMessage("目录下存在文件不能被删除");
		}
		try{
			lovMemberService.delete(id);
		}catch(Exception e){
			return sendErrorMessage("目录下存在子目录不能被删除");
		}
		return sendSuccessMessage();
	}
}
