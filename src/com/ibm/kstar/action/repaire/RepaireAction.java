package com.ibm.kstar.action.repaire;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.xsnake.web.dao.BaseDao;

import com.ibm.kstar.action.BaseFlowAction;
import com.ibm.kstar.api.product.IDemandService;
import com.ibm.kstar.api.repaire.IRepaireService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.product.KstarProductDemand;

@Controller
@RequestMapping("/repaire")
public class RepaireAction extends BaseFlowAction {

	@Autowired
	IDemandService demandService;
	
	@Autowired
	IRepaireService repaireService;

	@RequestMapping("/index")
	public String index(String id,Model model){
		return forward("index");
	}
	
	@RequestMapping("/sendProductDemandsToPDM")
	@ResponseBody
	public String sendProductDemandsToPDM(String codes,Model model){
		if(StringUtils.isEmpty(codes)){
			return this.sendErrorMessage("产品需求单号不能为空");
		}
		
		StringBuffer errorInfo = new StringBuffer();
		for(String code:codes.split(",")){
			if(StringUtils.isEmpty(code)){
				continue;
			}
			
			/*
	         * 根据需求申请单id获取需求单
	         */
	        KstarProductDemand demand = demandService.queryDemandByCode(code);
	        
	        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
	        UserObject user = (UserObject) request.getSession().getAttribute("LOGIN_USER");
	        
	        if(demand==null){
	        	errorInfo.append("需求单号["+code+"]不存在;");
	        	continue;
	        }
	        try{
		        if(demand.getProductID() == null){
		        	demandService.demandInBound(demand.getId(), user, true);
		        }else{
		        	demandService.demandInBound(demand.getId(), user, false);
		        }
	        }catch(Exception e){
	        	e.printStackTrace();
	        	errorInfo.append("需求单号["+code+"]同步错误："+e.getMessage()+";");
	        }
		}
		
		if(errorInfo.length()==0){
			return sendSuccessMessage("全部需求单操作成功");			
		}else{
			return sendSuccessMessage("同步有错误,如下："+errorInfo.toString());	
		}
	}
	
	
	@RequestMapping("/sendProductDemandsToInt")
	@ResponseBody
	public String sendProductDemandsToInt(String codes,Model model){
		if(StringUtils.isEmpty(codes)){
			return this.sendErrorMessage("产品需求单号不能为空");
		}
		
		StringBuffer errorInfo = new StringBuffer();
		for(String code:codes.split(",")){
			if(StringUtils.isEmpty(code)){
				continue;
			}
			
			/*
	         * 根据需求申请单id获取需求单
	         */
	        KstarProductDemand demand = demandService.queryDemandByCode(code);
	        
	        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
	        UserObject user = (UserObject) request.getSession().getAttribute("LOGIN_USER");
	        
	        if(demand==null){
	        	errorInfo.append("需求单号["+code+"]不存在;");
	        	continue;
	        }
	        try{
		        if(demand.getProductID() == null){
		        	demandService.doMove2IntWithoutSync(demand.getId(), user.getEmployee().getId(), true);
		        }else{
		        	demandService.doMove2IntWithoutSync(demand.getId(), user.getEmployee().getId(), false);
		        }
	        }catch(Exception e){
	        	e.printStackTrace();
	        	errorInfo.append("需求单号["+code+"]拆分产品错误："+e.getMessage()+";");
	        }
		}
		
		if(errorInfo.length()==0){
			return sendSuccessMessage("全部需求单拆分产品操作成功");			
		}else{
			return sendSuccessMessage("拆分产品有错误,如下："+errorInfo.toString());	
		}
	}
	
	@Autowired
	BaseDao baseDao;
	
	@RequestMapping("/translateLov")
	@ResponseBody
	public String translateLov(String codes){
		if(StringUtils.isEmpty(codes)){
			return this.sendErrorMessage("字典编号不能为空");
		}
		
		List<LovMember> ms = repaireService.getDictionarys(codes);
		int count = 0;
		for(LovMember m:ms){
			if(StringUtils.isNotEmpty(m.getOptTxt7())){
				System.out.println("[翻译字典"+(++count)+"]"+m.getOldName()+":已翻译忽略");
				continue;
			}
			System.out.print("[翻译字典"+(++count)+"]"+m.getOldName()+":");
			String en = getEnglishName(m.getOldName());
			System.out.print(en+":");
			if(StringUtils.isNotEmpty(en)){				
				repaireService.updateEn(m.getId(), en);
				System.out.println("翻译成功");
			}else{
				System.out.println("翻译失败");				
			}
			if(count%100==0){
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return sendSuccessMessage("翻译成功");			
	}
	
	private String getEnglishName(String chineseName){
		if(chineseName==null)
			return null;
		chineseName = chineseName.replace(" ", "");
		String url = "http://fanyi.baidu.com/v2transapi?from=zh&to=en&transtype=realtime&simple_means_flag=3&query="+chineseName;
		try {
            // 根据地址获取请求
            HttpGet request = new HttpGet(url);//这里发送get请求
            // 获取当前客户端对象
			HttpClient httpClient = new DefaultHttpClient();
            // 通过请求对象获取响应对象
            HttpResponse response = httpClient.execute(request);
            
            // 判断网络连接状态码是否正常(0--200都数正常)
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result= EntityUtils.toString(response.getEntity(),"utf-8");
                result = result.substring(result.indexOf("[{\"dst\":\"")+9,result.indexOf("\",\"src\":"));
                return result;
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
}
