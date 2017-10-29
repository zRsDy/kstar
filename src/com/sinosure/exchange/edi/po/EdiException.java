package com.sinosure.exchange.edi.po;

import java.util.HashMap;
import java.util.Map;

public class EdiException extends Exception {
	private static final long serialVersionUID = 1L;
	private String errorCode;
	private String errorMsg;

	private static final Map<String, String> errMsgMap = new HashMap<String, String>();

	static {

		errMsgMap.put("EDI-EXC-0001", "调用次数超限");
		errMsgMap.put("EDI-EXC-0002", "单次调用笔数超限");
		errMsgMap.put("EDI-EXC-0003", "没有获取到证书信息");
		errMsgMap.put("EDI-EXC-0004", "证书还没在信保通系统注册");
		errMsgMap.put("EDI-EXC-0005", "证书注册信息错误");
		errMsgMap.put("EDI-EXC-0006", "证书对应的企业还没开通Edi服务");
		errMsgMap.put("EDI-EXC-0007", "没有接收到数据");
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public EdiException(String errorCode, String errorMsg) {
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	public EdiException(String errorCode) {
		this.errorCode = errorCode;
		this.errorMsg = errMsgMap.get(errorCode);
		if (errorMsg == null)
			errorMsg = "未知错误消息";
	}
}
