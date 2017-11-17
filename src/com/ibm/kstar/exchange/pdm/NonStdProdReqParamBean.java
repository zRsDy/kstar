package com.ibm.kstar.exchange.pdm;

import java.util.List;

public class NonStdProdReqParamBean {
	
	NonStdProdReqParamBean.UserBean user_fbcpkfsqd;
	List<NonStdProdReqParamBean.UserSubBean> user_fbcpkfsqd_sub; 
	
	public class UserBean{
		
		/** 需求单号 **/
		private String crmsqdh;
		
		/** 需求单名称 **/
		private String xqdm;
		
		/** 客户名称  **/
		private String kh;
		
		/** 客户所在地  **/
		private String khszd;
		
		/** 客户PO/合同号 **/
		private String khpo;
		
		/** 期望交货日期  **/
		private String jhrq;
		
		/** 需求种类  **/
		private String xqzl;
		
		/** 需求部门 **/
		private String sqbm;
		 
		/** 需求人员 **/
		private String sqr;
		
		/** 联系电话  **/
		private String tel;
		
		/** 特殊产品需求清单文件路径 **/
		private String tgburl;
		
		/** 客户资料文件路径 **/
		private String fjurl;
		
		private String tgbmc;
		
		private String fjmc;
		
		/** 创建人 */
	    private String creator;
	    
	    /** 创建时间 */
	    private String createtime;
	    
	    /** 更新者 */
	    private String updator;
	    
	    /** 更新时间 */
	    private String updatetime;
	    
		/** 数据状态  **/
		private String datastatus;
				
		/** 接口状态  **/
		private String intstatus;
		
		/** 接口处理消息 **/
		private String intmessage;
		
		/** 系列/型号  **/
		private String xlxh;

		public String getCrmsqdh() {
			return crmsqdh;
		}

		public void setCrmsqdh(String crmsqdh) {
			this.crmsqdh = crmsqdh;
		}

		public String getKh() {
			return kh;
		}

		public void setKh(String kh) {
			this.kh = kh;
		}

		public String getKhszd() {
			return khszd;
		}

		public void setKhszd(String khszd) {
			this.khszd = khszd;
		}

		public String getKhpo() {
			return khpo;
		}

		public void setKhpo(String khpo) {
			this.khpo = khpo;
		}

		public String getJhrq() {
			return jhrq;
		}

		public void setJhrq(String jhrq) {
			this.jhrq = jhrq;
		}

		public String getXqzl() {
			return xqzl;
		}

		public void setXqzl(String xqzl) {
			this.xqzl = xqzl;
		}

		public String getSqbm() {
			return sqbm;
		}

		public void setSqbm(String sqbm) {
			this.sqbm = sqbm;
		}

		public String getSqr() {
			return sqr;
		}

		public void setSqr(String sqr) {
			this.sqr = sqr;
		}

		public String getTgburl() {
			return tgburl;
		}

		public void setTgburl(String tgburl) {
			this.tgburl = tgburl;
		}

		public String getFjurl() {
			return fjurl;
		}

		public void setFjurl(String fjurl) {
			this.fjurl = fjurl;
		}
		
		public String getCreator() {
			return creator;
		}

		public void setCreator(String creator) {
			this.creator = creator;
		}
		
		public String getCreatetime() {
			return createtime;
		}
		
		public void setCreatetime(String createtime) {
			this.createtime = createtime;
		}

		public String getUpdator() {
			return updator;
		}

		public void setUpdator(String updator) {
			this.updator = updator;
		}

		public String getUpdatetime() {
			return updatetime;
		}

		public void setUpdatetime(String updatetime) {
			this.updatetime = updatetime;
		}

		public String getXqdm() {
			return xqdm;
		}

		public void setXqdm(String xqdm) {
			this.xqdm = xqdm;
		}

		public String getTel() {
			return tel;
		}

		public void setTel(String tel) {
			this.tel = tel;
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

		public String getXlxh() {
			return xlxh;
		}
		
		public void setXlxh(String xlxh) {
			this.xlxh = xlxh;
		}

		public String getTgbmc() {
			return tgbmc;
		}

		public void setTgbmc(String tgbmc) {
			this.tgbmc = tgbmc;
		}

		public String getFjmc() {
			return fjmc;
		}

		public void setFjmc(String fjmc) {
			this.fjmc = fjmc;
		}
		
	}
	
	public class UserSubBean{
		
		/** 内部型号 **/
		private String nbxh;
		
		/** 预定义物料号 **/
		private String ydywlh;
		
		/** 需求数量 **/
		private Long sl;

		/** 需要提前备料  **/
		private String sfbl;
		
		/** 外部型号 **/
		private String wbxh;
		
		/** CRM产品类别  **/
		private String crmcplx;
		
		
		public String getNbxh() {
			return nbxh;
		}

		public void setNbxh(String nbxh) {
			this.nbxh = nbxh;
		}

		public String getYdywlh() {
			return ydywlh;
		}

		public void setYdywlh(String ydywlh) {
			this.ydywlh = ydywlh;
		}

		public Long getSl() {
			return sl;
		}

		public void setSl(Long sl) {
			this.sl = sl;
		}

		public String getSfbl() {
			return sfbl;
		}

		public void setSfbl(String sfbl) {
			this.sfbl = sfbl;
		}
		
		public String getWbxh() {
			return wbxh;
		}

		public void setWbxh(String wbxh) {
			this.wbxh = wbxh;
		}
		
		public String getCrmcplx() {
			return crmcplx;
		}

		public void setCrmcplx(String crmcplx) {
			this.crmcplx = crmcplx;
		}
	}

	public UserBean getUser_fbcpkfsqd() {
		return user_fbcpkfsqd;
	}

	public void setUser_fbcpkfsqd(UserBean user_fbcpkfsqd) {
		this.user_fbcpkfsqd = user_fbcpkfsqd;
	}

	public List<UserSubBean> getUser_fbcpkfsqd_sub() {
		return user_fbcpkfsqd_sub;
	}

	public void setUser_fbcpkfsqd_sub(List<UserSubBean> user_fbcpkfsqd_sub) {
		this.user_fbcpkfsqd_sub = user_fbcpkfsqd_sub;
	}
	
	
}
