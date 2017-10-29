package com.ibm.kstar.exchange.pdm;

import com.ibm.kstar.entity.product.IEcrReqmEntity;
import com.ibm.kstar.entity.product.IEcrReqmProductEntity;
import com.ibm.kstar.exchange.pdm.EcrReqmParamBean.UserBean;
import org.xsnake.web.utils.BeanUtils;
import org.xsnake.web.utils.DateUtil;
import org.xsnake.web.utils.PropertiesUtil;

import java.util.ArrayList;
import java.util.List;

public class EcrReqmServiceClient{
	private static final String wftempid = "0";
	private static final String shtid = "195";
	private static final String serKey = "createShtinsAndStartWf";
	
	public boolean callService(IEcrReqmEntity ecr, List<IEcrReqmProductEntity> productEntities) throws Exception {
		ResultBean tokenRs = ServiceUtil.getToken();
		if(!Boolean.parseBoolean(tokenRs.getResult())){
			return setErrorReturn(ecr, tokenRs);
		}
		
		String loginUid = ServiceUtil.encodeStr(tokenRs.getLoginguid());
		if(null == loginUid){
			tokenRs.setErrinfo("The login uid encode failed.");
			return setErrorReturn(ecr, tokenRs);
		}
		String content = ServiceUtil.encodeStr(this.createParam(ecr,productEntities));
		if(null == content){
			tokenRs.setErrinfo("The content parameter encode failed.");
			return setErrorReturn(ecr, tokenRs);
		}
		
		String createShtinsAndStartWfURI = new StringBuilder().append(PropertiesUtil.getStringByKey(serKey))
				.append("&wftempid=").append(wftempid)
				.append("&shtid=").append(shtid)
				.append("&loginguid=").append(loginUid)
				.append("&content=").append(content).toString();
		ResultBean cssRs = ServiceUtil.callService(createShtinsAndStartWfURI, null);
		
		boolean result = Boolean.parseBoolean(cssRs.getResult());
		if(!result){
			return setErrorReturn(ecr, tokenRs);
		}
		return result;
	}
	
	
	private boolean setErrorReturn(IEcrReqmEntity ecr, ResultBean tokenRs) {
		ecr.setIntstatus("E");
		ecr.setReMessage(tokenRs.getErrinfo());
		return false;
	}

	private String createParam(IEcrReqmEntity ecr, List<IEcrReqmProductEntity> productEntities) {
		String jsonParam = null;
		EcrReqmParamBean ecrp = new EcrReqmParamBean();
		
		UserBean ecru = ecrp.new UserBean();
		BeanUtils.copyProperties(ecr, ecru);
		ecrp.setUser_ksdjtgcbgsqd(ecru);

		//ecrp.setUser_ksdjtgcbgsqd_sub(new ArrayList<>());
		for (IEcrReqmProductEntity productEntity : productEntities) {
			EcrReqmParamBean.Sub sub = ecrp.new Sub();
			BeanUtils.copyProperties(productEntity, sub);
			ecrp.getUser_ksdjtgcbgsqd_sub().add(sub);
		}

		String formatter = "yyyy-MM-dd HH:mm:ss";
		if (null != ecr.getDt_create_time()){
			ecru.setTjsj(DateUtil.getDate(ecr.getDt_create_time(), formatter));
		}
		if (null != ecr.getCreated_at()){
			ecru.setCreatetime(DateUtil.getDate(ecr.getCreated_at(), formatter));
		}
		if (null != ecr.getUpdated_at()){
			ecru.setUpdatetime(DateUtil.getDate(ecr.getUpdated_at(), formatter));
		}
		
		jsonParam = ServiceUtil.object2JsonString(ecrp);
		System.out.println("content: " + jsonParam);
		return jsonParam;
	}
}
