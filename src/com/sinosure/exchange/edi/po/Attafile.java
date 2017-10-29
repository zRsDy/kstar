package com.sinosure.exchange.edi.po;

public class Attafile {

	private String corpserialno; // 企业唯一标示
	private String srcCorpserialno; // 所属数据标示
	private String type; // 附件所属类型
	private String filename; // 附件名称
	private byte[] filebyte; // 附件数据流
	private String fileFullName; //带路径的文件名

	public String getCorpserialno() {
		return corpserialno;
	}

	public void setCorpserialno(String corpserialno) {
		this.corpserialno = corpserialno;
	}

	public String getSrcCorpserialno() {
		return srcCorpserialno;
	}

	public void setSrcCorpserialno(String srcCorpserialno) {
		this.srcCorpserialno = srcCorpserialno;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public byte[] getFilebyte() {
		return filebyte;
	}

	public void setFilebyte(byte[] filebyte) {
		this.filebyte = filebyte;
	}

	public String getFileFullName() {
		return fileFullName;
	}

	public void setFileFullName(String fileFullName) {
		this.fileFullName = fileFullName;
	}
	
}
