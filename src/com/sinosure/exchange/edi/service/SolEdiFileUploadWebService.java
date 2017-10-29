package com.sinosure.exchange.edi.service;

import java.util.List;

import com.sinosure.exchange.edi.po.Attafile;
import com.sinosure.exchange.edi.po.EdiException;

public interface SolEdiFileUploadWebService {
	
	/**
	 * A.企业调用此接口，附件上传
	 * @param List<Attafile> 附件列表
	 * @return 
	 * @throws EdiException
	 */
	public  String doEdiFileUpload (List<Attafile> attaFile) throws EdiException;

}
