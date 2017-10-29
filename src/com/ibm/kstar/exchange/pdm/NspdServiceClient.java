package com.ibm.kstar.exchange.pdm;

import java.util.ArrayList;
import java.util.List;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.DateUtil;
import org.xsnake.web.utils.PropertiesUtil;
import com.ibm.kstar.entity.product.INonStadProdDemandEntity;
import com.ibm.kstar.exchange.pdm.NonStdProdReqParamBean.UserBean;
import com.ibm.kstar.exchange.pdm.NonStdProdReqParamBean.UserSubBean;

public class NspdServiceClient{
	private static final String wftempid = "0";
	private static final String shtid = "221";
	private static final String serKey = "createShtinsAndStartWf";
	
	public boolean callService(List<INonStadProdDemandEntity> nspds) throws Exception {
		ResultBean tokenRs = ServiceUtil.getToken();
		if(!Boolean.parseBoolean(tokenRs.getResult())){
			throw new Exception(tokenRs.getErrinfo());
		}
		
		String loginUid = ServiceUtil.encodeStr(tokenRs.getLoginguid());
		if(null == loginUid){
			tokenRs.setErrinfo("The login uid encode failed.");
			throw new Exception(tokenRs.getErrinfo());
		}
		String content = ServiceUtil.encodeStr(this.createParam(nspds));
		if(null == content){
			tokenRs.setErrinfo("The content parameter encode failed.");
			throw new Exception(tokenRs.getErrinfo());
		}
		
		String createShtinsAndStartWfURI = new StringBuilder().append(PropertiesUtil.getStringByKey(serKey))
				.append("&wftempid=").append(wftempid)
				.append("&shtid=").append(shtid)
				.append("&loginguid=").append(loginUid)
				.append("&content=").append(content).toString();
		ResultBean cssRs = ServiceUtil.callService(createShtinsAndStartWfURI, null);
		
		boolean result = Boolean.parseBoolean(cssRs.getResult());
//		if(!result){
//			throw new Exception(cssRs.getErrinfo());
//		}
		return result;
	}
	
	
	private String createParam(List<INonStadProdDemandEntity> nspds) {
		String jsonParam = null;
		INonStadProdDemandEntity nspd = (INonStadProdDemandEntity)nspds.get(0);
		NonStdProdReqParamBean pb = new NonStdProdReqParamBean();
		UserBean pbu = pb.new UserBean();
		BeanUtils.copyProperties(nspd, pbu);
		
		String formatter = "yyyy-MM-dd HH:mm:ss";
		if (null != nspd.getDt_hopedeliver_date()){
			pbu.setJhrq(DateUtil.getDate(nspd.getDt_hopedeliver_date(), formatter));
		}
		if (null != nspd.getCreated_at()){
			pbu.setCreatetime(DateUtil.getDate(nspd.getCreated_at(), formatter));
		}
		if (null != nspd.getUpdated_at()){
			pbu.setUpdatetime(DateUtil.getDate(nspd.getUpdated_at(), formatter));
		}
		pb.setUser_fbcpkfsqd(pbu);
		
		List<UserSubBean> usbs =  new ArrayList<UserSubBean>();
		for(INonStadProdDemandEntity inspd : nspds){
			UserSubBean pbus = pb.new UserSubBean();
			BeanUtils.copyProperties(inspd, pbus);
			usbs.add(pbus);
		}
		pb.setUser_fbcpkfsqd_sub(usbs);
		
		jsonParam = ServiceUtil.object2JsonString(pb);
		System.out.println("content: " + jsonParam);
		return jsonParam;
	}
}
