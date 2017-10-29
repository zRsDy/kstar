package com.ibm.kstar.conf;

import java.util.HashMap;
import java.util.Map;

public class Configuration {

	private Map<String,String> appMap = new HashMap<String,String>();
	
	public String getPath(String module){
		return appMap.get(module);
	}

	public Map<String, String> getAppMap() {
		return appMap;
	}

	public void setAppMap(Map<String, String> appMap) {
		this.appMap = appMap;
	}
	
	private String serverAddress;

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}
	
	private String fileDownloadUrl;
	
	private String imageViewUrl;

	public String getFileDownloadUrl() {
		return fileDownloadUrl;
	}

	public void setFileDownloadUrl(String fileDownloadUrl) {
		this.fileDownloadUrl = fileDownloadUrl;
	}

	public String getImageViewUrl() {
		return imageViewUrl;
	}

	public void setImageViewUrl(String imageViewUrl) {
		this.imageViewUrl = imageViewUrl;
	}
	
}
