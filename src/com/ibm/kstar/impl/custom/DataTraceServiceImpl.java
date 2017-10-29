package com.ibm.kstar.impl.custom;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.dao.BaseDao;
import org.xsnake.web.log.KeyFiled;
import org.xsnake.web.page.IPage;
import org.xsnake.web.page.PageImpl;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.action.datatrace.DataTraceVO;
import com.ibm.kstar.action.datatrace.TraceDetailVo;
import com.ibm.kstar.api.custom.IDataTraceService;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class DataTraceServiceImpl implements IDataTraceService {
	
	@Autowired
	BaseDao baseDao;

	@Override
	public List<Map<String, Object>> getSysClass() {
		String sql = "select * from SYS_T_CLASS where 1=1";
		List<Map<String,Object>> list = baseDao.findBySql4Map(sql, null);
		return list;
	}

	@Override
	public IPage queryDatatrace(PageCondition condition) {
		if(StringUtil.isNullOrEmpty(condition.getCondition("tableName"))){
			return new PageImpl(new ArrayList<>(),condition.getPage(), condition.getRows(), 0);
		}
		String tableName = StringUtil.strnull(condition.getCondition("tableName"));
		String keyColValue = StringUtil.strnull(condition.getCondition("keyColValue"));
		
		List<Map<String,Object>> list = baseDao.findBySql4Map("select t.* from SYS_T_CLASS t where 1=1 and lower(t.table_name) = ?", new Object[]{tableName.toLowerCase()});
	
		String hisTableName = StringUtil.strnull(list.get(0).get("HIS_TABLE_NAME"));
		String tableComment = StringUtil.strnull(list.get(0).get("TABLE_COMMENT"));
		String className = StringUtil.strnull(list.get(0).get("CLASS_NAME"));
		
		//获取关键字段
		Field filed = this.getKeyFiled(className);
		String keyColName = "";
		if(filed != null){
			Column column = filed.getAnnotation(Column.class);
			keyColName = column.name().toUpperCase();
		}
		
		List<Object> params = new ArrayList<>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from "+hisTableName+" a where 1 = 1 ");
		if(!StringUtil.isNullOrEmpty(keyColName) && !StringUtil.isNullOrEmpty(keyColValue)){
			sql.append(" and a."+keyColName+" = ?");
			params.add(keyColValue);
		}
		sql.append("order by a.c_pid,a.dt_updated_at");
		
		IPage page = baseDao.searchBySql4Map(sql.toString(), params.toArray(), condition.getRows(), condition.getPage());
		IPage dataTracePage = transFormToDataTrace(page, tableName, tableComment, className);
		return dataTracePage;
	}

	private IPage transFormToDataTrace(IPage page, String tableName, String tableComment, String className) {
		if(page.getCount() > 0){
			//获取主键对应变量
			String primaryKey = getPrimaryKey(className);
			if(StringUtil.isNullOrEmpty(primaryKey)){
				return new PageImpl(new ArrayList<>(),page.getPageCount(), page.getPageSize(), 0);
			}
			
			//获取关键字段
			Field filed = this.getKeyFiled(className);
			String keyColComment = "";
			String keyColName = "";
			if(filed != null){
				keyColComment = filed.getAnnotation(KeyFiled.class).comment();
				Column column = filed.getAnnotation(Column.class);
				keyColName = column.name().toUpperCase();
			}
			
			List<DataTraceVO> voList = new ArrayList<DataTraceVO>();
			@SuppressWarnings("unchecked")
			List<Map<String,Object>> results = (List<Map<String,Object>>)page.getList();
			
			//根据主键将历史数据分组
			Map<String,List<Map<String,Object>>> mapGroupById = new HashMap<String,List<Map<String,Object>>>();
			for(Map<String,Object> map : results){
				String id = String.valueOf(map.get(primaryKey));
				if(mapGroupById.containsKey(id)){
					List<Map<String,Object>> list = mapGroupById.get(id);
					list.add(map);
				}else{
					List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
					list.add(map);
					
					mapGroupById.put(id, list);
				}
			}
			
			for(String key : mapGroupById.keySet()){
				List<Map<String,Object>> list = mapGroupById.get(key);
				for(int i=0;i<list.size();i++){
					Map<String,Object> map = list.get(i);
					
					if(list.indexOf(map) == 0){
						DataTraceVO vo = new DataTraceVO();
						vo.setPkValue(key);
						vo.setTableName(tableName);
						vo.setTableComment(tableComment);
						vo.setKeyColComment(keyColComment);
						vo.setKeyColValue(StringUtil.strnull(map.get(keyColName)));
						vo.setOperator(StringUtil.strnull(map.get("C_CREATED_BY_ID")));
						vo.setOperateDate(getDateStr(map.get("DT_CREATED_AT")));
						if(StringUtil.strnull(map.get("C_CREATED_BY_ID")).equals(StringUtil.strnull(map.get("C_UPDATED_BY_ID")))
								&& StringUtil.strnull(map.get("DT_CREATED_AT")).equals(StringUtil.strnull(map.get("DT_UPDATED_AT")))){
							vo.setOperateType("新增");
						}else{
							vo.setOperateType("编辑");
						}
						
						
						voList.add(vo);
					}else if(list.indexOf(map) == list.size()-1){
						//最后一条,且不是第一条,如果主表数据存在,添加编辑,否者,添加删除
						String hql = "from "+className+" where "+primaryKey+"=?";
						Object object = baseDao.findUniqueEntity(hql, key);
						
						DataTraceVO vo = new DataTraceVO();
						vo.setPkValue(key);
						vo.setTableName(tableName);
						vo.setTableComment(tableComment);
						vo.setKeyColComment(keyColComment);
						vo.setKeyColValue(StringUtil.strnull(map.get(keyColName)));
						vo.setOperator(StringUtil.strnull(map.get("C_UPDATED_BY_ID")));
						vo.setOperateDate(getDateStr(map.get("DT_UPDATED_AT")));
						if(object != null){
							vo.setOperateType("编辑");
						}else{
							vo.setOperateType("删除");
						}
						
						voList.add(vo);
					}else{
						//不是第一条也不是最后一条,只需要添加编辑
						DataTraceVO vo = new DataTraceVO();
						vo.setPkValue(key);
						vo.setTableName(tableName);
						vo.setTableComment(tableComment);
						vo.setKeyColComment(keyColComment);
						vo.setKeyColValue(StringUtil.strnull(map.get(keyColName)));
						vo.setOperator(StringUtil.strnull(map.get("C_UPDATED_BY_ID")));
						vo.setOperateDate(getDateStr(map.get("DT_UPDATED_AT")));
						vo.setOperateType("编辑");
						
						voList.add(vo);
					}
				}
			}
			return new PageImpl(voList, page.getPageCount(), page.getPageSize(), page.getCount());
		}
		return page;
	}
	
	private String getDateStr(Object obj){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(obj);
	}
	
	private Field getKeyFiled(String className){
		try {
			Class<?> calss = Class.forName(className);
			
			Field[] fs = calss.getDeclaredFields();
			for(Field f : fs){
				KeyFiled filed = f.getAnnotation(KeyFiled.class);
				if(filed != null){
					return f;
				}
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private String getPrimaryKey(String className){
		//获取主键对应变量
		String primaryKey = "";
		try {
			Class<?> calss = Class.forName(className);
			
			Field[] fs = calss.getDeclaredFields();
			for(Field f : fs){
				Id id = f.getAnnotation(Id.class);
				if(id != null){
					Column column = f.getAnnotation(Column.class);
					primaryKey = column.name().toUpperCase();
					break;
				}
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return primaryKey;
	}

	@Override
	public List<TraceDetailVo> queryDatatraceDetail(PageCondition condition) {
		String pkValue = StringUtil.strnull(condition.getCondition("pkValue"));
		String tableName = StringUtil.strnull(condition.getCondition("tableName"));
		String operateDate = StringUtil.strnull(condition.getCondition("operateDate"));
		
		List<Map<String,Object>> list = baseDao.findBySql4Map("select t.* from SYS_T_CLASS t where 1=1 and lower(t.table_name) = ?", new Object[]{tableName.toLowerCase()});
		
		String hisTableName = StringUtil.strnull(list.get(0).get("HIS_TABLE_NAME"));
		String className = StringUtil.strnull(list.get(0).get("CLASS_NAME"));
		
		String primaryKey = getPrimaryKey(className);
		
		//以operateDate位基准,获取最近两次变更记录,用来分析时间为operateDate时数据的变更情况
		String sql = "select * from "+hisTableName+" where 1=1 and "+primaryKey+"=? and dt_updated_at <=  to_date(?, 'yyyy-mm-dd hh24:mi:ss') order by dt_updated_at desc ";
		List<Map<String,Object>> datalist = baseDao.findBySql4Report(sql, new Object[]{pkValue,operateDate},2);
		
		//判断是否最后一次修改
		boolean isLast = isLast(hisTableName, primaryKey, pkValue, operateDate);
		
		List<TraceDetailVo> voList = transFormToTraceDetail(datalist,primaryKey,pkValue,tableName,isLast);
		return voList;
	}

	private List<TraceDetailVo> transFormToTraceDetail(List<Map<String, Object>> datalist, String primaryKey, String pkValue, String tableName, boolean isLast) {
		List<TraceDetailVo> voList = new ArrayList<>();
		Map<String,String> colComments = getColComments(tableName);
		if(datalist != null && !datalist.isEmpty()){
			if(datalist.size() == 1){//获取的是新增的变更记录
				Map<String, Object> obj = datalist.get(0);
				for(String key : obj.keySet()){
					if("C_CREATED_BY_ID".equals(key)
							|| "DT_CREATED_AT".equals(key)
							|| "C_CREATED_POS_ID".equals(key)
							|| "C_CREATED_ORG_ID".equals(key)
							|| "C_UPDATED_BY_ID".equals(key)
							|| "DT_UPDATED_AT".equals(key)
							|| "HIS_ID".equals(key)){
						continue;
					}
					if(StringUtil.isNullOrEmpty(obj.get(key))){
						continue;
					}
					
					TraceDetailVo vo = new TraceDetailVo();
					vo.setColName(key);
					vo.setColComment(colComments.get(key));
					vo.setNewValue(String.valueOf(obj.get(key)));
					
					voList.add(vo);
				}
			}else{
				String sql = "select * from "+tableName+" where "+primaryKey+"=?";
				List<Map<String,Object>> list = baseDao.findBySql4Map(sql, new Object[]{pkValue});
				if(isLast && (list == null || list.isEmpty())){//主表没有数据,最后一条是删除记录
					Map<String, Object> obj = datalist.get(0);
					for(String key : obj.keySet()){
						if("C_CREATED_BY_ID".equals(key)
								|| "DT_CREATED_AT".equals(key)
								|| "C_CREATED_POS_ID".equals(key)
								|| "C_CREATED_ORG_ID".equals(key)
								|| "C_UPDATED_BY_ID".equals(key)
								|| "DT_UPDATED_AT".equals(key)
								|| "HIS_ID".equals(key)){
							continue;
						}
						if(StringUtil.isNullOrEmpty(obj.get(key))){
							continue;
						}
						
						TraceDetailVo vo = new TraceDetailVo();
						vo.setColName(key);
						vo.setColComment(colComments.get(key));
						vo.setOldValue(StringUtil.strnull(obj.get(key)));
						
						voList.add(vo);
					}
				}else{
					Map<String, Object> oldObj = datalist.get(0);
					Map<String, Object> newObj = datalist.get(1);
					for(String key : oldObj.keySet()){
						if("C_CREATED_BY_ID".equals(key)
								|| "DT_CREATED_AT".equals(key)
								|| "C_CREATED_POS_ID".equals(key)
								|| "C_CREATED_ORG_ID".equals(key)
								|| "C_UPDATED_BY_ID".equals(key)
								|| "DT_UPDATED_AT".equals(key)
								|| "HIS_ID".equals(key)){
							continue;
						}
						
						if(StringUtil.strnull(newObj.get(key)).equals(StringUtil.strnull(oldObj.get(key)))){
							continue;
						}
						TraceDetailVo vo = new TraceDetailVo();
						vo.setColName(key);
						vo.setColComment(colComments.get(key));
						vo.setNewValue(StringUtil.strnull(newObj.get(key)));
						vo.setOldValue(StringUtil.strnull(oldObj.get(key)));
						
						voList.add(vo);
					}
				}
			}
		}
		return voList;
	}
	
	private boolean isLast(String hisTableName, String primaryKey, String pkValue, String operateDate){
		String sql = "select * from "+hisTableName+" where 1=1 and "+primaryKey+"=? and dt_updated_at >  to_date(?, 'yyyy-mm-dd hh24:mi:ss') order by dt_updated_at desc ";
		List<Map<String,Object>> list = baseDao.findBySql4Map(sql, new Object[]{pkValue,operateDate});
		
		return list == null || list.isEmpty();
	}
	
	/**
	 * 获取表字段注释
	 * @param tableName
	 * @return
	 */
	private Map<String,String> getColComments(String tableName){
		Map<String,String> colComments = new HashMap<>();
		String sql = "select COLUMN_NAME,COMMENTS from user_col_comments where table_name=?";
		List<Map<String,Object>> list = baseDao.findBySql4Map(sql, new Object[]{tableName.toUpperCase()});
		if(list != null && !list.isEmpty()){
			for(Map<String,Object> map : list){
				colComments.put(StringUtil.strnull(map.get("COLUMN_NAME")), StringUtil.strnull(map.get("COMMENTS")));
			}
		}
		return colComments;
	}
}
