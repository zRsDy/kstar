package com.ibm.kstar.exchange.pdm;

public class ResultBean {
	private String result;
	private String loginguid;
	private String shtinsid;
	private String wfid;
	private String errinfo;
	private String status;
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getLoginguid() {
		return loginguid;
	}
	public void setLoginguid(String loginguid) {
		this.loginguid = loginguid;
	}
	public String getShtinsid() {
		return shtinsid;
	}
	public void setShtinsid(String shtinsid) {
		this.shtinsid = shtinsid;
	}
	public String getWfid() {
		return wfid;
	}
	public void setWfid(String wfid) {
		this.wfid = wfid;
	}
	public String getErrinfo() {
		return errinfo;
	}
	public void setErrinfo(String errinfo) {
		this.errinfo = errinfo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
