package org.xsnake.web.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.xsnake.web.page.IPage;

public interface BaseDao {

	public SessionFactory getSessionFactory();

	public HibernateTemplate getHibernateTemplate();
	
	public <T> void save(List<T> l);

	public <T> void deleteById(Class<T> c, Serializable id);
	
	public <T> void logicDeleteById(Class<T> c, Serializable id);
	
	public <T> void deleteByEndDate(Class<T> c, Serializable id);

	public <T> void deleteAll(List<T> l);

	public <T> T load(Class<T> c, Serializable id);

	public <T> T get(Class<T> c, Serializable id);
	
	public void delete(Object obj);

	public <T> T save(T t);

	public <T> T update(T t);

	public <T> void update(List<T> l);
	
	public void saveOrUpdate(Object t);

	public <T> T findUniqueEntity(String hql);
	
	public <T> T findUniqueEntity(String hql, Object args);

	public <T> T findUniqueEntity(String hql, Object[] args);

	public boolean exist(String hql);

	public boolean exist(String hql, Object[] args);
	
	public <T> List<T> findEntity(String hql);

	public <T> List<T> findEntity(String hql, Object args);

	public <T> List<T> findEntity(String hql, Object[] args);

	public <T> List<T> findEntity(String hql, int start, int num);
	
	public <T> List<T> findEntity(final String hql, Object[] args, final int start, final int num);
	
	public void executeHQL(String hql);

	public void executeHQL(String hql, Object[] args);

	public void executeSQL(String sql);

	public void executeSQL(String sql, Object args);

	public void executeSQL(String sql, Object[] args);
	
	void executeSQLX(final String sql, final Object[] args);
	
	public static abstract class ProcedureParam{
		public ProcedureParam (int type,Object value){
			this.type = type;
			this.value = value;
		}
		int type;
		Object value;
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		public Object getValue() {
			return value;
		}
		public void setValue(Object value) {
			this.value = value;
		}
	}
	
	public static class InProcedureParam extends ProcedureParam{
		public InProcedureParam(Object value){
			super(-1,value);
		}
	}
	
	public static class OutProcedureParam extends ProcedureParam{
		public OutProcedureParam(int type){
			super(type,null);
		}
	}
	
	/**
	 * 执行存储过程
	 * @param sql
	 * @param args   InProcedureParam  输入参数，OutProcedureParam 输出参数
	 * @return  按照参数顺序返回的数组，当为输入参数时为null，反之为返回值
	 */
	public Object[] executeProcedure(String sql, ProcedureParam[] args);

	public List<Object[]> findBySql(final String sql);

	public List<Object[]> findBySql(final String sql, Object[] args);

	public List<Object[]> findBySql(final String sql, Object args);

	public List<Object[]> findBySql(String sql, int start, int num);

	public List<Object[]> findBySql(String sql, Object[] args, int start, int num);

	public List<Object[]> findBySql(String sql, Object obj, int start, int num);
	
	public <T> T findUniqueBySql(String sql, Object[] args);

	public <T> T findUniqueBySql(String sql, Object obj);

	public <T> T findUniqueBySql(String sql);
	
	public IPage search(String hql, int size, int goPage);

	public IPage search(String hql, Object[] args, int size, int goPage);
	
	public List<Map<String,Object>> findBySql4Map(final String sql, final Object[] args);
	
	public List<Map<String,Object>> findBySql4Report(final String sql, final Object[] args,final int maxNum);
	
	public IPage searchBySql(String sql, int size, int goPage);

	public IPage searchBySql(String sql, Object[] args, int size, int goPage);
	
	public IPage searchBySql4Map(final String sql, final Object[] args,final int size,final int goPage);
	
	public IPage searchBySql4Map(final String sql,final int size,final int goPage);
	
}




