package com.ibm.kstar.action.product;

import java.util.Date;
import java.util.List;

public class NspdParamBean {
	private NspdParamBean.InforBean infor;
	private List<NspdParamBean.InforDetailBean> infor_detail = null;

	public NspdParamBean() {
	}

	public InforBean getInfor() {
		return infor;
	}

	public void setInfor(InforBean infor) {
		this.infor = infor;
	}

	public List<InforDetailBean> getInfor_detail() {
		return infor_detail;
	}

	public void setInfor_detail(List<InforDetailBean> infor_detail) {
		this.infor_detail = infor_detail;
	}

	public static class InforBean {
		/** 需求单号 **/
		String crmsqdh;

		/** 单据状态 **/
		String djzt;

		/** 接口状态(默认值：P) **/
		String intstatus;

		/** 接口处理消息 **/
		String intmessage;

		/** 创建时间 **/
		Date created_at;

		/** 创建人 **/
		String creator;

		/** 更新时间 **/
		Date updated_at;

		/** 更新人 **/
		String updator;

		/** 处理失败原因 **/
		String sbyy;

		public InforBean() {
		}

		public String getCrmsqdh() {
			return crmsqdh;
		}

		public void setCrmsqdh(String crmsqdh) {
			this.crmsqdh = crmsqdh;
		}

		public String getDjzt() {
			return djzt;
		}

		public void setDjzt(String djzt) {
			this.djzt = djzt;
		}

		public String getIntstatus() {
			return intstatus;
		}

		public void setIntstatus(String intstatus) {
			this.intstatus = intstatus;
		}

		public String getIntmessage() {
			return intmessage;
		}

		public void setIntmessage(String intmessage) {
			this.intmessage = intmessage;
		}

		public Date getCreated_at() {
			return created_at;
		}

		public void setCreated_at(Date created_at) {
			this.created_at = created_at;
		}

		public String getCreator() {
			return creator;
		}

		public void setCreator(String creator) {
			this.creator = creator;
		}

		public Date getUpdated_at() {
			return updated_at;
		}

		public void setUpdated_at(Date updated_at) {
			this.updated_at = updated_at;
		}

		public String getUpdator() {
			return updator;
		}

		public void setUpdator(String updator) {
			this.updator = updator;
		}

		public String getSbyy() {
			return sbyy;
		}

		public void setSbyy(String sbyy) {
			this.sbyy = sbyy;
		}
	}

	public static class InforDetailBean {
		/** 预定义物料号 **/
		String ydywlh;

		/** 产品物料号 **/
		String pdmwlh;

		public InforDetailBean() {
		}

		public String getYdywlh() {
			return ydywlh;
		}

		public void setYdywlh(String ydywlh) {
			this.ydywlh = ydywlh;
		}

		public String getPdmwlh() {
			return pdmwlh;
		}

		public void setPdmwlh(String pdmwlh) {
			this.pdmwlh = pdmwlh;
		}

	}

}
