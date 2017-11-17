package com.ibm.kstar.action.common.popup;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.xsnake.web.action.BaseAction;
import org.xsnake.web.log.LogOperate;

import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.cache.CacheData;
import com.ibm.kstar.interceptor.system.permission.NoRight;

@Controller
@RequestMapping("/popup")
public class PopupAction extends BaseAction {

	@NoRight
	@LogOperate(module="POPUP模块",notes="${user}打开了所属区域选择页面")
	@RequestMapping("/customAreaSelect")
	public String customAreaSelect(String pickerId, String lovId, Model model, String customCategory){
		model.addAttribute("pickerId",pickerId);
		
		if (!StringUtils.isEmpty(lovId)) {
			LovMember lov = (LovMember)CacheData.getInstance().get(lovId);
			String lovPath = lov.getPath();
			String[] ids = StringUtils.split(lovPath, "/");
			
			for (int i = 0; i < ids.length; i++) {
				model.addAttribute("layerValue"+ (i + 1),ids[i]);
			}
		}
		
		return forward("selectArea");
	}
	
	@NoRight
	@LogOperate(module="POPUP模块",notes="${user}打开了客户行业选择页面")
	@RequestMapping("/customCategorySelect")
	public String customCategorySelect(String pickerId, Model model, String customCategory){
		model.addAttribute("pickerId",pickerId);
		
		return forward("selectCategory");
	}
	
	@NoRight
	@LogOperate(module="POPUP模块",notes="${user}打开了地址选择页面")
	@RequestMapping("/addressSelect")
	public String addressSelect(String pickerId, String customId, Model model, String customCategory){
		model.addAttribute("pickerId", pickerId);
		model.addAttribute("customId", customId);
		
		return forward("selectAddress");
	}
}
