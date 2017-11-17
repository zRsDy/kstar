package com.ibm.kstar.impl;

import java.util.List;

import org.xsnake.web.action.Condition;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.entity.bizopp.Bid;

public abstract class BaseServiceImpl {
	protected void addQueryCondition(Condition condition,List<Object> args,StringBuilder sb, String conditionName,String fieldName,String queryType){
		String conditionValue = condition.getStringCondition(conditionName);
		if(StringUtil.isNotEmpty(conditionValue)){
			if("=".equals(queryType)){
				sb.append(" and " + fieldName + " = ?");
				args.add(conditionValue);
			}else if("like".equals(queryType)){
				sb.append(" and " + fieldName + " like ?");
				args.add("%"+conditionValue.trim()+"%");
			}else if("<=".equals(queryType)){
				sb.append(" and " + fieldName + " <= to_date(?,'yyyy-mm-dd')");
				args.add(conditionValue);
			}else if(">=".equals(queryType)){
				sb.append(" and " + fieldName + " >= to_date(?,'yyyy-mm-dd')");
				args.add(conditionValue);
			}
		}
	}


}
