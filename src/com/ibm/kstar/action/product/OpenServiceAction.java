package com.ibm.kstar.action.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.log.LogOperate;
import org.xsnake.web.utils.BeanUtils;
import com.ibm.kstar.api.product.IDemandService;
import com.ibm.kstar.api.product.IEcrService;
import com.ibm.kstar.api.product.IProductService;
import com.ibm.kstar.entity.product.IEcrReqmEntity;
import com.ibm.kstar.entity.product.INonStadProdDemandEntity;
import com.ibm.kstar.entity.product.IProdDocInforEntity;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/http")
public class OpenServiceAction extends BaseAction {
	@Autowired
	IDemandService demandService;
	
	@Autowired
	IEcrService ecrService;
	
	@Autowired
	IProductService pdiService;
	
	@LogOperate(module = "非标产品需求返回接口开放服务", notes = "标产品需求返回接口开放服务被调用")
	@ResponseBody
	@RequestMapping(value = "/prodReqFeedSer/doservice", method = RequestMethod.POST, consumes = "application/json")
	public String doNspdFeedback(HttpServletRequest request, @RequestBody JSONObject jso) {
		Map<String, Class<NspdParamBean.InforDetailBean>> inforDetailsMap = new HashMap<String, Class<NspdParamBean.InforDetailBean>>();
		inforDetailsMap.put("infor_detail", NspdParamBean.InforDetailBean.class);
		NspdParamBean nspdpb = (NspdParamBean) JSONObject.toBean(jso, NspdParamBean.class, inforDetailsMap);

		NspdParamBean.InforBean nspdi = nspdpb.getInfor();
		List<NspdParamBean.InforDetailBean> idbs = nspdpb.getInfor_detail();
		List<INonStadProdDemandEntity> nspds = new ArrayList<INonStadProdDemandEntity>();

		for (NspdParamBean.InforDetailBean idb : idbs) {
			INonStadProdDemandEntity nspd = new INonStadProdDemandEntity();
			BeanUtils.copyProperties(nspdi, nspd);
			BeanUtils.copyProperties(idb, nspd);
			if (!this.nspdValidation(nspd)) {
				return sendErrorMessage(nspd.getReMessage());
			}
			nspds.add(nspd);
		}

		if (nspds.isEmpty()) {
			return sendSuccessMessage("服务调用失败，数据解析为空.");
		}

		INonStadProdDemandEntity result = demandService.save(nspds);
		if (result.getResult()) {
			return sendSuccessMessage("服务调用成功完成.");
		} else {
			return sendErrorMessage(result.getReMessage());
		}
	}
	
	
	private boolean nspdValidation(INonStadProdDemandEntity nspd){
		boolean result = true;
		nspd.setReMessage(" ");
		
		if (StringUtils.isEmpty(nspd.getCrmsqdh())) {
			result = false;
			nspd.setReMessage(nspd.getReMessage()+"CRM需求单号不能为空。");
		}
		
		if (StringUtils.isEmpty(nspd.getDjzt())) {
			result = false;
			nspd.setReMessage(nspd.getReMessage()+"单据状态不能为空。");
		} else if (StringUtils.equalsIgnoreCase("F", nspd.getDjzt()) && StringUtils.isEmpty(nspd.getPdmwlh()) ) {
			result = false;
			nspd.setReMessage(nspd.getReMessage()+"需求处理完成的单据，PDM产品物料编号不能为空。");
		} else if (StringUtils.equalsIgnoreCase("R", nspd.getDjzt()) && StringUtils.isEmpty(nspd.getSbyy()) ) {
			result = false;
			nspd.setReMessage(nspd.getReMessage()+"需求驳回的单据，失败处理原因不能为空。");
		} 
		
		if (StringUtils.isEmpty(nspd.getYdywlh())) {
			result = false;
			nspd.setReMessage(nspd.getReMessage()+"预定义物料号不能为空。");
		}
		if (StringUtils.isEmpty(nspd.getIntstatus())) {
			result = false;
			nspd.setReMessage(nspd.getReMessage()+"接口状态不能为空。");
		}
		if (null == nspd.getUpdated_at()) {
			result = false;
			nspd.setReMessage(nspd.getReMessage()+"更新时间不能为空。");
		}
		if (StringUtils.isEmpty(nspd.getUpdator())) {
			result = false;
			nspd.setReMessage(nspd.getReMessage()+"更新人不能为空。");
		}
		
		return result;
	}
	
	
	@LogOperate(module = "ECR结果同步接口开放服务", notes = "ECR结果同步接口服务被调用")
	@ResponseBody
	@RequestMapping(value = "/ecrFeedSer/doservice", method = RequestMethod.POST, consumes = "application/json")
	public String doEcrFeedback(HttpServletRequest request, @RequestBody JSONObject jso) {
		System.out.println("请求参数: " + jso);
		IEcrReqmEntity ecr = (IEcrReqmEntity) JSONObject.toBean(jso,IEcrReqmEntity.class);
		
		if(!this.ecrValidation(ecr)){
			return sendErrorMessage(ecr.getReMessage());
		}
		
		boolean result = ecrService.save(ecr);
		if(result){
			return sendSuccessMessage("服务调用成功完成.");
		} else {
			return sendErrorMessage(ecr.getReMessage());
		}
	}


	private boolean ecrValidation(IEcrReqmEntity ecr) {
		boolean result = true;
		ecr.setReMessage(" ");
		
		if(StringUtils.isEmpty(ecr.getPdmecrdh())){
			result = false;
			ecr.setReMessage(ecr.getReMessage()+"PDM ECR需求单号不能为空。");
		}
		if(StringUtils.isEmpty(ecr.getEcrxqdh())){
			result = false;
			ecr.setReMessage(ecr.getReMessage()+"CRM ECR需求单号不能为空。");
		}
		
		if (StringUtils.isEmpty(ecr.getDjzt())) {
			result = false;
			ecr.setReMessage(ecr.getReMessage()+"单据状态不能为空。");
		} else if (StringUtils.equalsIgnoreCase("F", ecr.getDjzt()) && StringUtils.isEmpty(ecr.getPdmecrdh()) ) {
			result = false;
			ecr.setReMessage(ecr.getReMessage()+"需求处理完成的单据，PDM ECR需求单号不能为空。");
		} else if (StringUtils.equalsIgnoreCase("R", ecr.getDjzt()) && StringUtils.isEmpty(ecr.getSbyy()) ) {
			result = false;
			ecr.setReMessage(ecr.getReMessage()+"需求驳回的单据，失败处理原因不能为空。");
		} 
		
		if (StringUtils.isEmpty(ecr.getLh())) {
			result = false;
			ecr.setReMessage(ecr.getReMessage()+"产品物料号不能为空。");
		}
		if (StringUtils.isEmpty(ecr.getJjcd())) {
			result = false;
			ecr.setReMessage(ecr.getReMessage()+"ECR变更的紧急程度不能为空。");
		}
		if (StringUtils.isEmpty(ecr.getBgnrbgsj())) {
			result = false;
			ecr.setReMessage(ecr.getReMessage()+"变更内容/项目不能为空。");
		}
		if (StringUtils.isEmpty(ecr.getBgyy())) {
			result = false;
			ecr.setReMessage(ecr.getReMessage()+"变更原因不能为空。");
		}
		if (StringUtils.isEmpty(ecr.getBglx())) {
			result = false;
			ecr.setReMessage(ecr.getReMessage()+"变更原因类型不能为空。");
		}
		if (StringUtils.isEmpty(ecr.getSqr())) {
			result = false;
			ecr.setReMessage(ecr.getReMessage()+"申请人不能为空。");
		}
		if (null == ecr.getDt_create_time()) {
			result = false;
			ecr.setReMessage(ecr.getReMessage()+"ECR提交时间不能为空。");
		}
		if (null == ecr.getDt_eff_time()) {
			result = false;
			ecr.setReMessage(ecr.getReMessage()+"ECR生效时间不能为空。");
		}
		if (null == ecr.getUpdated_at()) {
			result = false;
			ecr.setReMessage(ecr.getReMessage()+"更新人不能为空。");
		}
		if (StringUtils.isEmpty(ecr.getUpdator())) {
			result = false;
			ecr.setReMessage(ecr.getReMessage()+"更新时间不能为空。");
		}
		return result;
	}
	
	@LogOperate(module = "产品文档资料同歩接口开放服务", notes = "产品文档资料同歩接口服务被调用")
	@ResponseBody
	@RequestMapping(value = "/pdiFeedSer/doservice", method = RequestMethod.POST, consumes = "application/json")
	public String doPdiFeedback(HttpServletRequest request, @RequestBody JSONObject jso) {
		System.out.println("请求参数: " + jso);
		IProdDocInforEntity pdi = (IProdDocInforEntity) JSONObject.toBean(jso,IProdDocInforEntity.class);
		
		if(this.pdiValidation(pdi)){
			return sendErrorMessage(pdi.getReMessage());
		}
		
		boolean result = pdiService.save(pdi);
		if(result){
			return sendSuccessMessage("服务调用成功完成.");
		} else {
			return sendErrorMessage(pdi.getReMessage());
		}
	
	}
	
	private boolean pdiValidation(IProdDocInforEntity pdi) {
		StringBuilder sbf = BeanUtils.hasNullValue(pdi);
		boolean hasInvalidValue = null == sbf || sbf.length() < 0 ? false : true;
		if(hasInvalidValue && null != sbf){
			pdi.setReMessage(sbf.toString());
		}
		return hasInvalidValue;
	}
	
}