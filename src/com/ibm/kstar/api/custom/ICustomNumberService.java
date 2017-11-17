package com.ibm.kstar.api.custom;

import org.xsnake.web.exception.AnneException;

public interface ICustomNumberService {
	/*===========================================================
	Procedure Name :
		gen_jz_num
	Description:
		 生成竞争对手编号
	 编号规则：
		KSTAR-JZ（竞争对手）YYYYMM（年月）XXXX(四位流水号)（默认为0)
	History:
		1.00  2009-09-23  XXXX  Creation
	===========================================================*/
	String getCompetitorNumber() throws AnneException;
	
	/*===========================================================
	Procedure Name :
		 gen_lf_num
	Description:
		 生成来访接待编号
	 编号规则：
		KSTAR-LF（来访接待）YYYYMM（年月）XXXX(四位流水号)（默认为0)
	History:
		1.00  2009-09-23  XXXX  Creation
	===========================================================*/
	String getEventNumber() throws AnneException;
	
	/*===========================================================
	Procedure Name :
		gen_jj_num
	Description:
		 生成客户交接编号
	 编号规则：
		KSTAR-JJ（客户交接）YYYYMM（年月）XXXX(四位流水号)（默认为0)
	History:
		1.00  2009-09-23  XXXX  Creation
	===========================================================*/
	String getHandoverNumber() throws AnneException;
	
	/*===========================================================
	Procedure Name :
		gen_gx_num
	Description:
		 生成共享授权编号
	 编号规则：
		KSTAR-GX（共享授权）YYYYMM（年月）XXXX(四位流水号)（默认为0)
	History:
		1.00  2009-09-23  XXXX  Creation
	===========================================================*/
	String getShareNumber() throws AnneException;
	
	/*===========================================================
	Procedure Name :
		gen_pg_num
	Description:
	 	生成评估调整编号
	 编号规则：
		KSTAR-PG（评估调整）YYYYMM（年月）XXXX(四位流水号)（默认为0)
	History:
		1.00  2009-09-23  XXXX  Creation
	===========================================================*/
	String getAdjustNumber() throws AnneException;
	
	/*===========================================================
	Procedure Name :
		gen_hb_num
	Description:
	 	生成客户合并编号
	 编号规则：
		KSTAR-HB（客户合并）YYYYMM（年月）XXXX(四位流水号)（默认为0)
	History:
		1.00  2009-09-23  XXXX  Creation
	===========================================================*/
	String getMergeNumber() throws AnneException;
	
	/*===========================================================
	  Procedure Name :
	     gen_si_num
	  Description:
	     生成客户满意度调查编号
	     编号规则：
	        KSTAR-SI（满意度调查）YYYYMM（年月）XXXX(四位流水号)（默认为0)
	  History:
	    1.00  2009-09-23  XXXX  Creation
	  ===========================================================*/
	String getInvestCode() throws AnneException;
	
	/*===========================================================
	Procedure Name :
		gen_bb_num
	Description:
		生成客户报备编号
	编号规则：
		KSTAR-BB（客户报备）YYYYMM（年月）XXXX(四位流水号)（默认为0)
	History:
		1.00  2009-09-23  XXXX  Creation
	===========================================================*/
	String getReportNumber() throws AnneException;
}
