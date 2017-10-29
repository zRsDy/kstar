package com.ibm.kstar.exchange.pdm;

import java.util.ArrayList;
import java.util.List;

public class EcrReqmParamBean {
	
	EcrReqmParamBean.UserBean user_ksdjtgcbgsqd;

	List<Sub> user_ksdjtgcbgsqd_sub;

	public class UserBean{
		/** ECR需求单号 **/
		private String ecrxqdh;

		/** 紧急程度 **/
		private String jjcd;

		/** 变更内容/项目 **/
		private String bgnrbgsj;

		/** 变更原因 **/
		private String bgyy;

		/** 变更原因类型 **/
		private String bglx;

		/** 参考已有ECR **/
		private String ckyyecr;

		/** 申请人 **/
		private String sqr;

		/** ECR提交时间 **/
		private String tjsj;

		/** 数据状态 **/
		private String datastatus;

		/** 接口状态 **/
		private String intstatus;

		/** 接口处理消息 **/
		private String intmessage;

		/** 创建人 */
		private String creator;

		/** 创建时间 */
		private String createtime;

		/** 更新者 */
		private String updator;

		/** 更新时间 */
		private String updatetime;

		public String getEcrxqdh() {
			return ecrxqdh;
		}

		public void setEcrxqdh(String ecrxqdh) {
			this.ecrxqdh = ecrxqdh;
		}

		public String getJjcd() {
			return jjcd;
		}

		public void setJjcd(String jjcd) {
			this.jjcd = jjcd;
		}

		public String getBgnrbgsj() {
			return bgnrbgsj;
		}

		public void setBgnrbgsj(String bgnrbgsj) {
			this.bgnrbgsj = bgnrbgsj;
		}

		public String getBgyy() {
			return bgyy;
		}

		public void setBgyy(String bgyy) {
			this.bgyy = bgyy;
		}

		public String getBglx() {
			return bglx;
		}

		public void setBglx(String bglx) {
			this.bglx = bglx;
		}

		public String getCkyyecr() {
			return ckyyecr;
		}

		public void setCkyyecr(String ckyyecr) {
			this.ckyyecr = ckyyecr;
		}

		public String getSqr() {
			return sqr;
		}

		public void setSqr(String sqr) {
			this.sqr = sqr;
		}

		public String getDatastatus() {
			return datastatus;
		}

		public void setDatastatus(String datastatus) {
			this.datastatus = datastatus;
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

		public String getCreator() {
			return creator;
		}

		public void setCreator(String creator) {
			this.creator = creator;
		}

		public String getUpdator() {
			return updator;
		}

		public void setUpdator(String updator) {
			this.updator = updator;
		}

		public String getTjsj() {
			return tjsj;
		}

		public void setTjsj(String tjsj) {
			this.tjsj = tjsj;
		}

		public String getCreatetime() {
			return createtime;
		}

		public void setCreatetime(String createtime) {
			this.createtime = createtime;
		}

		public String getUpdatetime() {
			return updatetime;
		}

		public void setUpdatetime(String updatetime) {
			this.updatetime = updatetime;
		}
	}

	public class Sub{
		private String lh;

		public String getLh() {
			return lh;
		}

		public void setLh(String lh) {
			this.lh = lh;
		}
	}
	public UserBean getUser_ksdjtgcbgsqd() {
		return user_ksdjtgcbgsqd;
	}

	public void setUser_ksdjtgcbgsqd(UserBean user_ksdjtgcbgsqd) {
		this.user_ksdjtgcbgsqd = user_ksdjtgcbgsqd;
	}

	public List<Sub> getUser_ksdjtgcbgsqd_sub() {
		if(user_ksdjtgcbgsqd_sub==null)
			user_ksdjtgcbgsqd_sub = new ArrayList<Sub>();
		return user_ksdjtgcbgsqd_sub;
	}

	public void setUser_ksdjtgcbgsqd_sub(List<Sub> user_ksdjtgcbgsqd_sub) {
		this.user_ksdjtgcbgsqd_sub = user_ksdjtgcbgsqd_sub;
	}
}
