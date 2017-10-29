package com.ibm.kstar.action.bizopp.info.product;

import com.alibaba.fastjson.JSON;
import com.ibm.kstar.api.bizopp.IBizoppService;
import com.ibm.kstar.entity.bizopp.GridVo;
import com.ibm.kstar.entity.bizopp.ProductDetail;
import com.ibm.kstar.interceptor.system.permission.NoRight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;
import org.xsnake.web.utils.ActionUtil;
import org.xsnake.web.utils.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 项目配置明细
 * @author Wutw 2017-1-11
 *
 */
@Controller
@RequestMapping("/productDetail")
public class BizProductDetailAction extends BaseAction {

	
	@Autowired
	IBizoppService bizoppService;

	@NoRight
	@ResponseBody
	@RequestMapping("/page")
	public String page(PageCondition condition, HttpServletRequest request) throws Exception {
		ActionUtil.requestToCondition(condition, request);
		String bizOppId = request.getParameter("bizOppId");
		//售前支持页面传过来的参数
		String bizOppIdProduct = condition.getStringCondition("bizOppIdProduct");
		bizOppId = bizOppIdProduct != null ? bizOppIdProduct : bizOppId;
			condition.getFilterObject().addCondition("bizOppId", "eq", bizOppId);
		IPage p = bizoppService.queryProductDetail(condition);

		List<ProductDetail> list = (List<ProductDetail>)p.getList();

		for (ProductDetail d:list ) {
			String productSeries = bizoppService.getProductSeries(d.getProductId());
			d.setProductSeries(productSeries);
		}

		return sendSuccessMessage(p);
	}
	
	@RequestMapping("/add")
	public String add(String bizOppId, Model model){
		model.addAttribute("bizOppId", bizOppId);
		return forward("productDetailAdd");
	}
	
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(ProductDetail entity, HttpServletRequest request) {

		bizoppService.saveProductDetail(entity);
		return sendSuccessMessage(entity.getId());
	}
	
	@RequestMapping("/edit")
	public String edit(String id,Model model){
		if(id==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		ProductDetail entity = bizoppService.getProducrDetailEntity(id);
		if(entity==null){
			throw new AnneException("没有找到需要修改的数据");
		}
		model.addAttribute("entity",entity);
		return forward("productDetailAdd");
	}

	@NoRight
	@ResponseBody
	@RequestMapping(value = "/edit2", method = RequestMethod.POST)
	public String edit2(GridVo gridVo) {

		List<ProductDetail> list = JSON.parseArray(gridVo.getJsonStr(), ProductDetail.class);
		for (ProductDetail productDetail : list) {
			if (StringUtil.isEmpty(productDetail.getId())) {
				if (StringUtil.isEmpty(productDetail.getBizOppId())) {
					throw new AnneException("缺少商机id");
				} else {
					productDetail.setId(null);
					bizoppService.saveProductDetail(productDetail);
				}
			} else {
				bizoppService.updateProductDetail(productDetail);
			}
		}
		return sendSuccessMessage();
	}

	
	@ResponseBody
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public String edit(ProductDetail bizopp){
		bizoppService.updateProductDetail(bizopp);
		return sendSuccessMessage();
	}
	
	@ResponseBody
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String delete(String id){
		if(StringUtil.isEmpty(id)){
			throw new AnneException("id is null");
		}
		bizoppService.deleteProduceDetail(id);
		return sendSuccessMessage();
	}
}
