package com.sinosure.exchange.edi.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;
import org.springframework.core.io.FileSystemResource;
import org.xsnake.web.utils.PropertiesUtil;
import com.sinosure.exchange.edi.po.Attafile;
import com.sinosure.exchange.edi.po.BuyerCodeApplyInfo;
import com.sinosure.exchange.edi.po.EdiException;
import com.sinosure.exchange.edi.po.NoLcQuotaApplyInfoV2;
import org.codehaus.xfire.service.Service;

public class ZXBClient {
	private static XFireProxyFactory serviceFactory = new XFireProxyFactory();
	public static final String serviceURL = PropertiesUtil.getStringByKey("serviceURL");
	
	public void callBuyerCodeApply(List<BuyerCodeApplyInfo> list) throws Exception {
		Service serviceModel = new ObjectServiceFactory().create(SolEdiProxyWebService.class);
		SolEdiProxyWebService service = null;
		try {
			service = (SolEdiProxyWebService) serviceFactory.create(serviceModel, serviceURL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		try {
			service.doEdiBuyerCodeApply(list);
		} catch (EdiException e) {
			e.printStackTrace();
			throw new Exception("错误代码: " + e.getErrorCode() + "--> 错误消息: " + e.getErrorMsg());
		}
	}

	public void callNoLcQuotaApplyV2(List<NoLcQuotaApplyInfoV2> list) throws Exception {
		Service serviceModel = new ObjectServiceFactory().create(SolEdiShorttermWebService.class);
		SolEdiShorttermWebService service = null;
		try {
			service = (SolEdiShorttermWebService) serviceFactory.create(serviceModel, serviceURL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		try {
			service.doEdiNoLcQuotaApplyV2(list);
		} catch (EdiException e) {
			e.printStackTrace();
			throw new Exception("错误代码: " + e.getErrorCode() + "--> 错误消息: " + e.getErrorMsg());
		}

		for (NoLcQuotaApplyInfoV2 nolc : list) {
			List<Attafile> afs = nolc.getAfs();
			if (null == afs || afs.isEmpty()) {
				continue;
			}
			this.callFileUpload(afs);
		}
	}
	
	private void callFileUpload(List<Attafile> afs) throws Exception {
		Service serviceModel = new ObjectServiceFactory().create(SolEdiFileUploadWebService.class);
		SolEdiFileUploadWebService service = null;
		try {
			service = (SolEdiFileUploadWebService) serviceFactory.create(serviceModel, serviceURL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
		
		Iterator<Attafile> itr = afs.iterator();
		while(itr.hasNext()){
			Attafile af = itr.next();
			byte[] f = this.getFile(new File(af.getFileFullName()));
			if(null == f || f.length<=0){
				itr.remove();
				continue;
			}
			af.setFilebyte(f);
		}
		
		if(null != afs && !afs.isEmpty()){
			try {
				service.doEdiFileUpload(afs);
				System.out.println("attachment upload service called.");
			} catch (EdiException e) {
				e.printStackTrace();
				throw new Exception("错误代码: " + e.getErrorCode() + "--> 错误消息: " + e.getErrorMsg());
			}
		}
	}
	
	private byte[] getFile(File f) {
		byte[] file = null;
		FileSystemResource resource = new FileSystemResource(f);
		InputStream ins = null;
		ByteArrayOutputStream out = null;
		try {
			if (resource.isReadable()) {
				ins = resource.getInputStream();
				out = new ByteArrayOutputStream(1024);
				byte[] b = new byte[1024];
				int n = 0;
				while ((n = ins.read(b)) != -1) {
					out.write(b, 0, n);
				}
			}
			if(null != out){
				file = out.toByteArray();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(null != ins) ins.close();
				if(null != out) out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}
}
