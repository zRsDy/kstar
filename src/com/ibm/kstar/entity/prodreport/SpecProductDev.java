package com.ibm.kstar.entity.prodreport;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "cpcbase.tl_kst73_specproductdev_line_v")
public class SpecProductDev implements java.io.Serializable {
	  /** 版本号 */
		private static final long serialVersionUID = 5806319533740891078L;
		
		/** 主键 */
		@Id
		@Column(name = "CID", unique = false)
		@GeneratedValue(generator = "spec_dev_id_generator")
		@GenericGenerator(name = "spec_dev_id_generator", strategy = "uuid")
		private String cid;
	 
		//项目描述
		 @Column(name = "projcet_desc")
		private String projcetDesc;
		 //申请人
		 @Column(name = "startor")
		private String startor;
		 //申请部门审批时间
		 @Column(name = "req_approvedtime")
		private String reqApprovedtime;
		 //申请时间
		 @Column(name = "statime")
		 private String statime;
		 //产品分配人
		 @Column(name = "cpfp_user")
		private String cpfpUser;
		 
		  //产品分配开始时间
		 @Column(name = "cpfp_astatime")
		private String cpfpAstatime;
		  //产品分配结束时间
		 @Column(name = "cpfp_aendtime")
		private String cpfpAendtime;
		  //接收单位主管
		 @Column(name = "proc_user")
		private String procUser;
		  //接收单位开始时间
		 @Column(name = "astatime")
		private String astatime;
		  //项目编号
		 @Column(name = "project_no")
		private String projectNo;
		 
		  
		 //产品线
		 @Column(name = "cpx")
		private String cpx;
		 //技术平台
		 @Column(name = "jspt")
		private String jspt;
		 //产品线负责人
		 @Column(name = "cpx_manager")
		private String cpxManager;
		 //申请单号
		 @Column(name = "sqdh")
		private String sqdh;
		 //客户
		 @Column(name = "kh")
		private String kh;
		 
		   //客户所在地
		 @Column(name = "khszd")
		private String khszd;
		 //产品系列或型号
		 @Column(name = "xlxh")
		private String xlxh;
		 //客户期望交货日期
		 @Column(name = "jhrq")
		private String jhrq;
		 //客户PO号/合同号
		 @Column(name = "khpo")
		private String khpo;
		 //需求种类
		 @Column(name = "xqzl")
		private String xqzl;
		 
		 //项目类型
		 @Column(name = "xmlx")
		private String xmlx;
		//料号
		 @Column(name = "itemcode")
		private String itemcode;
		//物料描述
		 @Column(name = "wlms")
		private String wlms;
		//首批订单数量
		 @Column(name = "sl")
		private String sl;
		//平面开发人员
		 @Column(name = "pmkf_user")
		private String pmkfUser;
		 
		 //平面开发开始时间
		 @Column(name = "pmkf_astatime")
		private String pmkfAstatime;
		//平面开发结束时间
		 @Column(name = "pmkf_aendtime")
		private String pmkfAendtime;
		//结构开发人员
		 @Column(name = "jgkf_user")
		private String jgkfUser;
		//结构开发开始时间
		 @Column(name = "jgkf_astatime")
		private String jgkfAstatime;
		//结构开发结束时间
		 @Column(name = "jgkf_aendtime")
		private String jgkfAendtime;

		 //硬件开发人员
		 @Column(name = "yjkf_user")
		private String yjkfUser;
		//硬件开发开始时间
		 @Column(name = "yjkf_astatime")
		private String yjkfAstatime;
		//硬件开发结束时间
		 @Column(name = "yjkf_aendtime")
		private String yjkfAendtime;
		//软件开发人员
		 @Column(name = "rjkf_user")
		private String rjkfUser;
		//软件开发开始时间
		 @Column(name = "rjkf_astatime")
		private String rjkfAstatime;
		  
		 //软件开发结束时间
		 @Column(name = "rjkf_aendtime")
		private String rjkfAendtime;
		//其它任务开发人员
		 @Column(name = "qtkf_user")
		private String qtkfUser;
		//其它任务开发开始时间
		 @Column(name = "qtkf_astatime")
		private String qtkfAstatime;
		//其它任务开发结束时间
		 @Column(name = "qtkf_aendtime")
		private String qtkfAendtime;
		//平面确认人员
		 @Column(name = "pmqr_user")
		private String pmqrUser;
		    
		    //平面确认开始时间
		 @Column(name = "pmqr_astatime")
		private String pmqrAstatime;
		//平面确认结束时间
		 @Column(name = "pmqr_aendtime")
		private String pmqrAendtime;
		//结构确认人员
		 @Column(name = "jgqr_user")
		private String jgqrUser;
		//结构确认开始时间
		 @Column(name = "jgqr_astatime")
		private String jgqrAstatime;
		//结构确认结束时间
		 @Column(name = "jgqr_aendtime")
		private String jgqrAendtime;

		   //开发任务及确认完成时间
		 @Column(name = "all_wbs_endtime")
		private String allWbsEndtime;
		 
		public String getCid() {
			return cid;
		}
		public void setCid(String cid) {
			this.cid = cid;
		}
		public String getProjcetDesc() {
			return projcetDesc;
		}
		public void setProjcetDesc(String projcetDesc) {
			this.projcetDesc = projcetDesc;
		}
		public String getStartor() {
			return startor;
		}
		public void setStartor(String startor) {
			this.startor = startor;
		}
		public String getReqApprovedtime() {
			return reqApprovedtime;
		}
		public void setReqApprovedtime(String reqApprovedtime) {
			this.reqApprovedtime = reqApprovedtime;
		}
		public String getStatime() {
			return statime;
		}
		public void setStatime(String statime) {
			this.statime = statime;
		}
		public String getCpfpUser() {
			return cpfpUser;
		}
		public void setCpfpUser(String cpfpUser) {
			this.cpfpUser = cpfpUser;
		}
		public String getCpfpAstatime() {
			return cpfpAstatime;
		}
		public void setCpfpAstatime(String cpfpAstatime) {
			this.cpfpAstatime = cpfpAstatime;
		}
		public String getCpfpAendtime() {
			return cpfpAendtime;
		}
		public void setCpfpAendtime(String cpfpAendtime) {
			this.cpfpAendtime = cpfpAendtime;
		}
		public String getProcUser() {
			return procUser;
		}
		public void setProcUser(String procUser) {
			this.procUser = procUser;
		}
		public String getAstatime() {
			return astatime;
		}
		public void setAstatime(String astatime) {
			this.astatime = astatime;
		}
		public String getProjectNo() {
			return projectNo;
		}
		public void setProjectNo(String projectNo) {
			this.projectNo = projectNo;
		}
		public String getCpx() {
			return cpx;
		}
		public void setCpx(String cpx) {
			this.cpx = cpx;
		}
		public String getJspt() {
			return jspt;
		}
		public void setJspt(String jspt) {
			this.jspt = jspt;
		}
		public String getCpxManager() {
			return cpxManager;
		}
		public void setCpxManager(String cpxManager) {
			this.cpxManager = cpxManager;
		}
		public String getSqdh() {
			return sqdh;
		}
		public void setSqdh(String sqdh) {
			this.sqdh = sqdh;
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
		public String getXlxh() {
			return xlxh;
		}
		public void setXlxh(String xlxh) {
			this.xlxh = xlxh;
		}
		public String getJhrq() {
			return jhrq;
		}
		public void setJhrq(String jhrq) {
			this.jhrq = jhrq;
		}
		public String getKhpo() {
			return khpo;
		}
		public void setKhpo(String khpo) {
			this.khpo = khpo;
		}
		public String getXqzl() {
			return xqzl;
		}
		public void setXqzl(String xqzl) {
			this.xqzl = xqzl;
		}
		public String getXmlx() {
			return xmlx;
		}
		public void setXmlx(String xmlx) {
			this.xmlx = xmlx;
		}
		public String getItemcode() {
			return itemcode;
		}
		public void setItemcode(String itemcode) {
			this.itemcode = itemcode;
		}
		public String getWlms() {
			return wlms;
		}
		public void setWlms(String wlms) {
			this.wlms = wlms;
		}
		public String getSl() {
			return sl;
		}
		public void setSl(String sl) {
			this.sl = sl;
		}
		public String getPmkfUser() {
			return pmkfUser;
		}
		public void setPmkfUser(String pmkfUser) {
			this.pmkfUser = pmkfUser;
		}
		public String getPmkfAstatime() {
			return pmkfAstatime;
		}
		public void setPmkfAstatime(String pmkfAstatime) {
			this.pmkfAstatime = pmkfAstatime;
		}
		public String getPmkfAendtime() {
			return pmkfAendtime;
		}
		public void setPmkfAendtime(String pmkfAendtime) {
			this.pmkfAendtime = pmkfAendtime;
		}
		public String getJgkfUser() {
			return jgkfUser;
		}
		public void setJgkfUser(String jgkfUser) {
			this.jgkfUser = jgkfUser;
		}
		public String getJgkfAstatime() {
			return jgkfAstatime;
		}
		public void setJgkfAstatime(String jgkfAstatime) {
			this.jgkfAstatime = jgkfAstatime;
		}
		public String getJgkfAendtime() {
			return jgkfAendtime;
		}
		public void setJgkfAendtime(String jgkfAendtime) {
			this.jgkfAendtime = jgkfAendtime;
		}
		public String getYjkfUser() {
			return yjkfUser;
		}
		public void setYjkfUser(String yjkfUser) {
			this.yjkfUser = yjkfUser;
		}
		public String getYjkfAstatime() {
			return yjkfAstatime;
		}
		public void setYjkfAstatime(String yjkfAstatime) {
			this.yjkfAstatime = yjkfAstatime;
		}
		public String getYjkfAendtime() {
			return yjkfAendtime;
		}
		public void setYjkfAendtime(String yjkfAendtime) {
			this.yjkfAendtime = yjkfAendtime;
		}
		public String getRjkfUser() {
			return rjkfUser;
		}
		public void setRjkfUser(String rjkfUser) {
			this.rjkfUser = rjkfUser;
		}
		public String getRjkfAstatime() {
			return rjkfAstatime;
		}
		public void setRjkfAstatime(String rjkfAstatime) {
			this.rjkfAstatime = rjkfAstatime;
		}
		public String getRjkfAendtime() {
			return rjkfAendtime;
		}
		public void setRjkfAendtime(String rjkfAendtime) {
			this.rjkfAendtime = rjkfAendtime;
		}
		public String getQtkfUser() {
			return qtkfUser;
		}
		public void setQtkfUser(String qtkfUser) {
			this.qtkfUser = qtkfUser;
		}
		public String getQtkfAstatime() {
			return qtkfAstatime;
		}
		public void setQtkfAstatime(String qtkfAstatime) {
			this.qtkfAstatime = qtkfAstatime;
		}
		public String getQtkfAendtime() {
			return qtkfAendtime;
		}
		public void setQtkfAendtime(String qtkfAendtime) {
			this.qtkfAendtime = qtkfAendtime;
		}
		public String getPmqrUser() {
			return pmqrUser;
		}
		public void setPmqrUser(String pmqrUser) {
			this.pmqrUser = pmqrUser;
		}
		public String getPmqrAstatime() {
			return pmqrAstatime;
		}
		public void setPmqrAstatime(String pmqrAstatime) {
			this.pmqrAstatime = pmqrAstatime;
		}
		public String getPmqrAendtime() {
			return pmqrAendtime;
		}
		public void setPmqrAendtime(String pmqrAendtime) {
			this.pmqrAendtime = pmqrAendtime;
		}
		public String getJgqrUser() {
			return jgqrUser;
		}
		public void setJgqrUser(String jgqrUser) {
			this.jgqrUser = jgqrUser;
		}
		public String getJgqrAstatime() {
			return jgqrAstatime;
		}
		public void setJgqrAstatime(String jgqrAstatime) {
			this.jgqrAstatime = jgqrAstatime;
		}
		public String getJgqrAendtime() {
			return jgqrAendtime;
		}
		public void setJgqrAendtime(String jgqrAendtime) {
			this.jgqrAendtime = jgqrAendtime;
		}
		public String getAllWbsEndtime() {
			return allWbsEndtime;
		}
		public void setAllWbsEndtime(String allWbsEndtime) {
			this.allWbsEndtime = allWbsEndtime;
		}
		public String getBomOwner() {
			return bomOwner;
		}
		public void setBomOwner(String bomOwner) {
			this.bomOwner = bomOwner;
		}
		public String getBomAstatime() {
			return bomAstatime;
		}
		public void setBomAstatime(String bomAstatime) {
			this.bomAstatime = bomAstatime;
		}
		public String getBomAendtime() {
			return bomAendtime;
		}
		public void setBomAendtime(String bomAendtime) {
			this.bomAendtime = bomAendtime;
		}
		public String getBomReleasetime() {
			return bomReleasetime;
		}
		public void setBomReleasetime(String bomReleasetime) {
			this.bomReleasetime = bomReleasetime;
		}
		public String getRdSpendtime() {
			return rdSpendtime;
		}
		public void setRdSpendtime(String rdSpendtime) {
			this.rdSpendtime = rdSpendtime;
		}
		public String getBuyer2() {
			return buyer2;
		}
		public void setBuyer2(String buyer2) {
			this.buyer2 = buyer2;
		}
		public String getBuyerAstatime() {
			return buyerAstatime;
		}
		public void setBuyerAstatime(String buyerAstatime) {
			this.buyerAstatime = buyerAstatime;
		}
		public String getBuyerAendtime() {
			return buyerAendtime;
		}
		public void setBuyerAendtime(String buyerAendtime) {
			this.buyerAendtime = buyerAendtime;
		}
		public String getSeller() {
			return seller;
		}
		public void setSeller(String seller) {
			this.seller = seller;
		}
		public String getOrderOwner() {
			return orderOwner;
		}
		public void setOrderOwner(String orderOwner) {
			this.orderOwner = orderOwner;
		}
		public String getOrderAstatime() {
			return orderAstatime;
		}
		public void setOrderAstatime(String orderAstatime) {
			this.orderAstatime = orderAstatime;
		}
		public String getOrderAendtime() {
			return orderAendtime;
		}
		public void setOrderAendtime(String orderAendtime) {
			this.orderAendtime = orderAendtime;
		}
		public String getOrderNumber() {
			return orderNumber;
		}
		public void setOrderNumber(String orderNumber) {
			this.orderNumber = orderNumber;
		}
		public String getEndtime() {
			return endtime;
		}
		public void setEndtime(String endtime) {
			this.endtime = endtime;
		}
		public String getWfSpendtime() {
			return wfSpendtime;
		}
		public void setWfSpendtime(String wfSpendtime) {
			this.wfSpendtime = wfSpendtime;
		}
		public String getStat() {
			return stat;
		}
		public void setStat(String stat) {
			this.stat = stat;
		}
		public String getWfname() {
			return wfname;
		}
		public void setWfname(String wfname) {
			this.wfname = wfname;
		}
		//BOM制作人
		 @Column(name = "bom_owner")
		private String bomOwner;
		//BOM制作开始时间
		 @Column(name = "bom_astatime")
		private String bomAstatime;
		//BOM制作结束时间
		 @Column(name = "bom_aendtime")
		private String bomAendtime;
		//BOM发布时间
		 @Column(name = "bom_releasetime")
		private String bomReleasetime;
		 
		 //研发总耗时
		 @Column(name = "rd_spendtime")
		private String rdSpendtime;
		//分配采购、采购维护批准供应商及单价
		 @Column(name = "buyer2")
		private String buyer2;
		//采购作业开始时间
		 @Column(name = "buyer_astatime")
		private String buyerAstatime;
		//采购作业结束时间
		 @Column(name = "buyer_aendtime")
		private String buyerAendtime;
		//通知申请人（业务员确认及下单）
		 @Column(name = "seller")
		private String seller;
		      
		   //商务下单员
		 @Column(name = "order_owner")
		private String orderOwner;
		 //商务下单开始时间
		 @Column(name = "order_astatime")
		private String orderAstatime;
		 //商务下单结束时间
		 @Column(name = "order_aendtime")
		private String orderAendtime;
		 //销售订单号
		 @Column(name = "order_number")
		 private String orderNumber;
		 //流程结束时间
		 @Column(name = "endtime")
		 private String endtime;
		      
		  //流程总耗时
		 @Column(name = "wf_spendtime")
		 private String wfSpendtime;
		 //流程状态
		 @Column(name = "stat")
		 private String stat;
		 //流程名称
		 @Column(name = "wfname")
		 private String wfname; 
		

}
