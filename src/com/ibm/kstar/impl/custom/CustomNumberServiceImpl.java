package com.ibm.kstar.impl.custom;

import java.sql.Types;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.exception.AnneException;

import com.ibm.kstar.api.custom.ICustomNumberService;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class CustomNumberServiceImpl implements ICustomNumberService {
	@Autowired
	BaseDao baseDao;
	
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
	@Override
	public String getCompetitorNumber() throws AnneException{
		return getSequenceCode("gen_jz_num");
	}
	
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
	@Override
	public String getEventNumber() throws AnneException{
		return getSequenceCode("gen_lf_num");
	}
	
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
	@Override
	public String getHandoverNumber() throws AnneException{
		return getSequenceCode("gen_jj_num");
	}
	
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
	@Override
	public String getShareNumber() throws AnneException{
		return getSequenceCode("gen_gx_num");
	}
	
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
	@Override
	public String getAdjustNumber() throws AnneException{
		return getSequenceCode("gen_pg_num");
	}
	
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
	@Override
	public String getMergeNumber() throws AnneException{
		return getSequenceCode("gen_hb_num");
	}
	
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
	@Override
	public String getInvestCode() throws AnneException{
		return getSequenceCode("gen_si_num");
	}
	
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
	public String getReportNumber() throws AnneException{
		return getSequenceCode("gen_bb_num");
	}
	
	private String getSequenceCode(String functionName) {
		String number = "";
		StringBuffer sql = new StringBuffer();
		sql.append("{ ? = call CRM_P_CONTRACT_PUB.");
		sql.append(functionName);
		sql.append("(?)}");
		Object[] result = baseDao.executeProcedure(sql.toString(),
				new BaseDao.ProcedureParam[] {new BaseDao.OutProcedureParam(Types.VARCHAR), new BaseDao.InProcedureParam(-1) });
		number = (String) result[0];
		return number;
	}
}
