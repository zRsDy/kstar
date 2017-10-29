package org.xsnake.web.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Table;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.xsnake.web.page.IPage;
import org.xsnake.web.page.PageImpl;
import org.xsnake.web.utils.StringUtil;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.conf.ApplicationContextUtil;

@SuppressWarnings("unchecked")
public class BaseDaoImpl implements BaseDao {

	@Autowired
	HibernateTemplate hibernateTemplate;

	@Autowired
	SessionFactory sessionFactory;

	public HibernateTemplate getHibernateTemplate() {

		return hibernateTemplate;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	private Query createQuery(Session session, String hql, final Object[] args) {
		Query query = session.createQuery(hql);
		if (args != null && args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				query.setParameter(i, args[i]);
			}
		}
		return query;
	}

	private SQLQuery createSQLQuery(Session session, String sql, Object[] args) {
		SQLQuery query = session.createSQLQuery(sql);
		if (args != null && args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				query.setParameter(i, args[i]);
			}
		}
		return query;
	}

	@Override
	public <T> void deleteAll(List<T> list) {
		for (T t : list) {
			this.delete(t);
		}
	}

	@Override
	public <T> void deleteById(Class<T> c, Serializable id) {
		T t = this.load(c, id);
		this.delete(t);
	}

	@Override
	public <T> void save(List<T> l) {
		for (T t : l) {
			this.save(t);
		}
	}

	@Override
	public <T> T get(Class<T> c, Serializable k) {
		if(k == null){
			return null;
		}
		return (T) this.getHibernateTemplate().get(c, k);
	}

	@Override
	public <T> T load(Class<T> c, Serializable k) {
		return (T) this.getHibernateTemplate().load(c, k);
	}

	@Override
	public <T> T save(T t) {
		this.getHibernateTemplate().save(t);
//		saveToHis(t,"ADD");
		return t;
	}

	@Override
	public <T> T update(T t) {
		
		this.getHibernateTemplate().update(t);
//		saveToHis(t,"MOD");
		return t;
	}

	@Override
	public <T> void update(List<T> l) {
		for (T t : l) {
			this.update(t);
		}
	}
	
//	private <T> void saveToHis(T t,String opType){
//		try {
//			UserObject userObject = ApplicationContextUtil.getUserObject();
//			Table table = t.getClass().getAnnotation(Table.class);
//			if(userObject== null || table == null || StringUtil.isNullOrEmpty(table.name())){
//				return ;
//			}
//			
//			String tableName = table.name();
//			
//			String sql = "select t.his_table_name from SYS_T_CLASS t where 1=1 and lower(t.table_name) = ?";
//			
//			List<Map<String,Object>> list = this.findBySql4Map(sql, new Object[]{tableName.toLowerCase()});
//			if(list == null || list.isEmpty()){
//				return ;
//			}
//			
//			String hisTableName = String.valueOf(list.get(0).get("HIS_TABLE_NAME"));
//			if(StringUtil.isNullOrEmpty(hisTableName)){
//				return ;
//			}
//			
//			List<Field> fields = new ArrayList<Field>();
//			
//			getAllFields(t.getClass(), fields);
//			
//			StringBuffer cols = new StringBuffer();
//			StringBuffer vals = new StringBuffer();
//			
//			for(Field f : fields){
//				Column column = f.getAnnotation(Column.class);
//				if(column != null){
//					String colName = column.name().toLowerCase();
//					if("DEL".equals(opType) && ("c_updated_by_id".equals(colName) || "dt_updated_at".equals(colName))){
//						continue;
//					}
//					cols.append(colName+",");
//					f.setAccessible(true);
//					Object val = f.get(t);
//					if(StringUtil.isNullOrEmpty(val)){
//						vals.append(" null,");
//					}else{
//						if(val instanceof Date){
//							SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//							vals.append(" to_date('"+format.format(val)+"', 'yyyy-mm-dd hh24:mi:ss'),");
//						}else{
//							vals.append(" '"+val+"',");
//						}
//					}
//				}
//			}
//			
//			if("DEL".equals(opType)){
//				cols.append("c_updated_by_id, dt_updated_at, HIS_ID");
//				vals.append("'"+userObject.getEmployee().getId()+"',sysdate,'"+StringUtil.getUUID()+"'");
//			}else{
//				cols.append("HIS_ID");
//				vals.append("'"+StringUtil.getUUID()+"'");
//			}
//			
//			StringBuffer insertSql = new StringBuffer();
//			insertSql.append(" insert into "+hisTableName+" ("+cols+") values("+vals+")");
//			this.executeSQL(insertSql.toString());
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//			return;
//		}
//	}
	
	private void getAllFields(Class<?> clazz, List<Field> fields){
		Field[] fs = clazz.getDeclaredFields();
		Collections.addAll(fields, fs);
		if(clazz.getSuperclass() != null){
			getAllFields(clazz.getSuperclass(), fields);
		}
	}

	@Override
	public void saveOrUpdate(Object t) {
		this.getHibernateTemplate().saveOrUpdate(t);
	}

	@Override
	public <T> T findUniqueEntity(final String hql, final Object[] args) {
		HibernateCallback<T> callback = new HibernateCallback<T>() {

			@Override
			public T doInHibernate(Session session) throws HibernateException {
				try {
					Query query = createQuery(session, hql, args);
					return (T) query.uniqueResult();
				} finally {
					session.close();
				}
			}

		};
		return getHibernateTemplate().execute(callback);
	}

	@Override
	public void delete(Object o) {
//		saveToHis(o,"DEL");
		this.getHibernateTemplate().delete(o);
	}
	
	public boolean exist(String hql) {
		return exist(hql, null);
	}

	public boolean exist(String hql, Object[] args) {
		List<?> objs = findEntity(hql, args, 0, 1);
		return ((objs != null) && (objs.size() > 0));
	}

	@Override
	public <T> List<T> findEntity(String hql) {
		return (List<T>) this.getHibernateTemplate().find(hql);
	}

	@Override
	public <T> List<T> findEntity(String hql, Object obj) {
		return findEntity(hql, new Object[] { obj });
	}

	@Override
	public <T> List<T> findEntity(final String hql, final Object[] args) {

		HibernateCallback<List<T>> callback = new HibernateCallback<List<T>>() {
			@Override
			public List<T> doInHibernate(Session session)
					throws HibernateException {
				try {
					Query query = createQuery(session, hql, args);
					return query.list();
				} finally {
					session.close();
				}
			}
		};
		return getHibernateTemplate().execute(callback);
	}

	@Override
	public <T> List<T> findEntity(final String hql, final int start,
			final int num) {
		return findEntity(hql, null, start, num);
	}

	@Override
	public <T> List<T> findEntity(final String hql, final Object[] args,
			final int start, final int num) {

		HibernateCallback<List<T>> callback = new HibernateCallback<List<T>>() {
			@Override
			public List<T> doInHibernate(Session session)
					throws HibernateException {
				try {
					Query query = createQuery(session, hql, args);
					query.setFirstResult(start).setMaxResults(num);
					return query.list();
				} finally {
					session.close();
				}
			}
		};

		return getHibernateTemplate().execute(callback);
	}

	public void executeHQL(final String hql) {
		executeHQL(hql,null);
	}

	public void executeHQL(final String hql, final Object[] args) {

		HibernateCallback<Object> callback = new HibernateCallback<Object>() {
			@Override
			public Object doInHibernate(Session session)
					throws HibernateException {
				try {
					Query query = createQuery(session, hql, args);
					return query.executeUpdate();
				} finally {
					session.close();
				}
			}
		};

		getHibernateTemplate().execute(callback);
	}

	@Override
	public void executeSQL(String sql) {
		executeSQL(sql, null);
	}

	@Override
	public void executeSQL(final String sql, final Object args) {
		executeSQL(sql, new Object[] { args });
	}

	@Override
	public void executeSQL(final String sql, final Object[] args) {
		HibernateCallback<Object> callback = new HibernateCallback<Object>() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException {
				try {
					SQLQuery query = createSQLQuery(session, sql, args);
					return query.executeUpdate();
				} finally {
					session.close();
				}
			}
		};
		getHibernateTemplate().execute(callback);
	}

	public List<Map<String,Object>> findBySql4Report(final String sql, final Object[] args,final int maxNum) {
		HibernateCallback<List<Map<String,Object>>> callback = new HibernateCallback<List<Map<String,Object>>>() {
			@Override
			public List<Map<String,Object>> doInHibernate(Session session) throws HibernateException, SQLException {
				try {
					String $sql = sql;
					if(maxNum > 0){
						$sql = "select * from ("+sql+") t where rownum <= "+maxNum;
					}
					@SuppressWarnings("deprecation")
					Connection connection = session.connection();
					PreparedStatement ps = null;
					List<Map<String,Object>> list = new ArrayList<>();
					ResultSet rs = null;
					try{
						ps = connection.prepareStatement($sql);
						if(args !=null && args.length > 0){
							for(int i=0;i<args.length;i++){
								ps.setObject((i+1), args[i]);
							}
						}
//						long start = System.currentTimeMillis();
						rs = ps.executeQuery();
//						long times = System.currentTimeMillis() - start;
//						System.out.println("====查询用时:"+ times);
						while(rs.next()){
							Map<String,Object> map = new HashMap<String, Object>();  
							ResultSetMetaData rsmd = rs.getMetaData();
							int count = rsmd.getColumnCount();
							for(int i=1;i<=count;i++){
								map.put(rsmd.getColumnName(i), rs.getObject(rsmd.getColumnName(i)));
							}
							list.add(map);
						}
					}finally{
						if(rs !=null ){
							rs.close();
						}
						if(ps != null){
							ps.close();
						}
//						if(connection != null){
//							connection.close();
//						}
					}
					return list;
				} finally {
					session.close();
				}
			}
		};

		return getHibernateTemplate().execute(callback);
	}

	
	public List<Map<String,Object>> findBySql4Map(final String sql, final Object[] args) {
		HibernateCallback<List<Map<String,Object>>> callback = new HibernateCallback<List<Map<String,Object>>>() {
			@Override
			public List<Map<String,Object>> doInHibernate(Session session) throws HibernateException, SQLException {
				try {
					@SuppressWarnings("deprecation")
					Connection connection = session.connection();
					PreparedStatement ps = null;
					List<Map<String,Object>> list = new ArrayList<>();
					ResultSet rs = null;
					try{
						ps = connection.prepareStatement(sql);
						if(args !=null && args.length > 0){
							for(int i=0;i<args.length;i++){
								ps.setObject((i+1), args[i]);
							}
						}
//						long start = System.currentTimeMillis();
						rs = ps.executeQuery();
//						long times = System.currentTimeMillis() - start;
//						System.out.println("====查询用时:"+ times);
						while(rs.next()){
							Map<String,Object> map = new HashMap<String, Object>();  
							ResultSetMetaData rsmd = rs.getMetaData();
							int count = rsmd.getColumnCount();
							for(int i=1;i<=count;i++){
								map.put(rsmd.getColumnName(i), rs.getObject(rsmd.getColumnName(i)));
							}
							list.add(map);
						}
					}finally{
						if(rs !=null ){
							rs.close();
						}
						if(ps != null){
							ps.close();
						}
//						if(connection != null){
//							connection.close();
//						}
					}
					return list;
				} finally {
					session.close();
				}
			}
		};

		return getHibernateTemplate().execute(callback);
	}
	
	
	
	@Override
	public List<Object[]> findBySql(final String sql) {
		return findBySql(sql, null);
	}

	@Override
	public List<Object[]> findBySql(final String sql, final Object[] args) {
		HibernateCallback<List<Object[]>> callback = new HibernateCallback<List<Object[]>>() {
			@Override
			public List<Object[]> doInHibernate(Session session) throws HibernateException {
				try {
					SQLQuery query = createSQLQuery(session, sql, args);
					return query.list();
				} finally {
					session.close();
				}
			}
		};

		return getHibernateTemplate().execute(callback);
	}

	@Override
	public List<Object[]> findBySql(String sql, Object args) {
		return findBySql(sql, new Object[] { args });
	}

	@Override
	public List<Object[]> findBySql(final String sql, final Object[] args,
			final int start, final int num) {
		HibernateCallback<List<Object[]>> callback = new HibernateCallback<List<Object[]>>() {
			@Override
			public List<Object[]> doInHibernate(Session session)
					throws HibernateException {
				try {
					SQLQuery query = createSQLQuery(session, sql, args);
					query.setFirstResult(start).setMaxResults(num);
					return query.list();
				} finally {
					session.close();
				}
			}
		};

		return getHibernateTemplate().execute(callback);
	}

	@Override
	public List<Object[]> findBySql(String sql, int start, int num) {
		return findBySql(sql, null, start, num);
	}

	@Override
	public List<Object[]> findBySql(String sql, Object obj, int start, int num) {
		return findBySql(sql, new Object[] { obj }, start, num);
	}

	@Override
	public <T> T findUniqueBySql(final String sql, final Object[] args) {
		HibernateCallback<T> callback = new HibernateCallback<T>() {
			@Override
			public T doInHibernate(Session session) throws HibernateException {
				try {
					SQLQuery query = createSQLQuery(session, sql, args);
					return (T) query.uniqueResult();
				} finally {
					session.close();
				}

			}
		};
		return getHibernateTemplate().execute(callback);
	}

	@Override
	public <T> T findUniqueBySql(String sql, Object obj) {
		return findUniqueBySql(sql, new Object[] { obj });
	}

	@Override
	public <T> T findUniqueBySql(String sql) {
		return findUniqueBySql(sql, null);
	}

	@Override
	public <T> T findUniqueEntity(String hql) {
		return findUniqueEntity(hql, null);
	}

	@Override
	public <T> T findUniqueEntity(String hql, Object args) {
		return findUniqueEntity(hql, new Object[] { args });
	}

	public <T> T uniqueFindBySql(final String sql, final Object[] args) {

		HibernateCallback<T> callback = new HibernateCallback<T>() {
			@Override
			public T doInHibernate(Session session) throws HibernateException {
				try {
					SQLQuery query = createSQLQuery(session, sql, args);
					return (T) query.uniqueResult();
				} finally {
					session.close();
				}

			}
		};

		return getHibernateTemplate().execute(callback);
	}

	@Override
	public IPage search(String hql, int size, int goPage) {
		return this.searchForPager(hql, null, size, goPage);
	}

	@Override
	public IPage search(String hql, Object[] args, int size, int goPage) {
		return this.searchForPager(hql, args, size, goPage);
	}

	/**
	 * 此方法相比searchForPager方法，可以再hql语句中使用SELECT子句(2013/8/3)
	 */
	public IPage searchForPager(String hql, Object[] args, int pageLength, int currentPage) {
		try {
			String thql = null;
			if (hql.indexOf("select") == -1) {
				thql = "select count(*) " + hql;
			} else {
				thql = "select count(*) " + hql.substring(hql.indexOf("from"));
			}
			if (hql.indexOf("order by") > -1) {
				thql = thql.substring(0, thql.indexOf("order by"));
			}
			Long _count = findUniqueEntity(thql, args);
			int count = _count.intValue();
			return searchForPager(hql, args, pageLength, currentPage, count);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private IPage searchForPager(String hql, Object[] args, int pageLength,
			int currentPage, int count) {
		try {
			List<Object> results = findEntity(hql, args, (currentPage - 1) * pageLength, pageLength);
			return new PageImpl(results, currentPage, pageLength, count);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public <T> void logicDeleteById(Class<T> c, Serializable id) {
		StringBuffer hql=new StringBuffer();
		hql.append(" update ").append(c.getName()).append(" set deleteFlag = 'Y' where id = ? ");
		executeHQL(hql.toString(),new Object[]{id});
	}

	@Override
	public <T> void deleteByEndDate(Class<T> c, Serializable id) {
		StringBuffer hql=new StringBuffer();
		hql.append(" update ").append(c.getName()).append(" set endDate = ? where id = ? ");
		executeHQL(hql.toString(),new Object[]{new Date(),id});
	}
	
	@Override
	public IPage searchBySql(String sql, int size, int goPage) {
		return searchForPagerBySql(sql, null, size, goPage);
	}

	@Override
	public IPage searchBySql(String sql, Object[] args, int size, int goPage) {
		return searchForPagerBySql(sql, args, size, goPage);
	}
	
	public IPage searchBySql4Map(final String sql,final int size,final int goPage) {
		return searchBySql4Map(sql, null, size, goPage);
	}
	
	public IPage searchBySql4Map(final String sql, final Object[] args,final int size,final int goPage) {
		try {
			String thql = null;
			thql = "select count(1) from (" + sql +") t ";
			BigDecimal _count = findUniqueBySql(thql, args);
			int count = _count.intValue();
			try {
				HibernateCallback<List<Map<String,Object>>> callback = new HibernateCallback<List<Map<String,Object>>>() {
					@Override
					public List<Map<String,Object>> doInHibernate(Session session) throws HibernateException {
						try {
							return findSql4Map(session, sql, args, (goPage - 1) * size, size);
						} finally {
							session.close();
						}
					}
				};
				List<Map<String,Object>> results = getHibernateTemplate().execute(callback);
				return new PageImpl(results, goPage, size, count);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	private IPage searchForPagerBySql(String sql, Object[] args, int pageLength, int currentPage) {
		try {
			String thql = null;
			thql = "select count(1) from (" + sql +") t ";
			BigDecimal _count = findUniqueBySql(thql, args);
			int count = _count.intValue();
			return searchForPagerBySql(sql, args, pageLength, currentPage, count);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private IPage searchForPagerBySql(String sql, Object[] args, int pageLength, int currentPage, int count) {
		try {
			List<Object[]> results = findBySql(sql, args, (currentPage - 1) * pageLength, pageLength);
			return new PageImpl(results, currentPage, pageLength, count);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	public List<Map<String,Object>> findSql4Map(Session session,final String sql, final Object[] args, final int start, final int num) {
		final List<Map<String,Object>> list = new ArrayList<>();
		session.doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				PreparedStatement ps = null;
				ResultSet rs = null;
				String oracleSql = "select * from( select t.*,rownum rw from ("+sql+") t ) where rw> "+start+" and rw<=" + (start + num);
				try{
					ps = connection.prepareStatement(oracleSql);
					if(args !=null && args.length > 0){
						for(int i=0;i<args.length;i++){
							ps.setObject((i+1), args[i]);
						}
					}
					rs = ps.executeQuery();
					while(rs.next()){
						Map<String,Object> map = new HashMap<String, Object>();  
						ResultSetMetaData rsmd = rs.getMetaData();
						int count = rsmd.getColumnCount();
						for(int i=1;i<=count;i++){
							map.put(rsmd.getColumnName(i), rs.getObject(rsmd.getColumnName(i)));
						}
						list.add(map);
					}
				}finally{
					if(rs !=null ){
						rs.close();
					}
					if(ps != null){
						ps.close();
					}
//					if(connection != null){
//						connection.close();
//					}
				}

			}
		});
		return list;
	}

	@Override
	public Object[] executeProcedure(final String sql, final ProcedureParam[] args) {
		HibernateCallback<Object[]> callback = new HibernateCallback<Object[]>() {
			@Override
			public Object[] doInHibernate(Session session) throws HibernateException {
				try {
					final List<Object> result = new ArrayList<Object>();
					session.doWork(new Work() {
						@Override
						public void execute(Connection connection) throws SQLException {
							CallableStatement sta = null;
							try{
								sta = connection.prepareCall(sql);
								for(int i=1;i<args.length+1;i++){
									ProcedureParam p = args[(i-1)];
									if(p instanceof InProcedureParam){
										sta.setObject(i, p.value);
									}else if(p instanceof OutProcedureParam){
										sta.registerOutParameter(i, p.type);
									}
								}
								sta.execute();
								for(int i=1;i<args.length+1;i++){
									ProcedureParam p = args[(i-1)];
									if(p instanceof OutProcedureParam){
										result.add(sta.getString(i));
									}else{
										result.add(null);
									}
								}
							}finally{
								if(sta!=null){
									sta.close();
								}
							}
						}
					});
					return result.toArray();
				} finally {
					session.close();
				}
			}
		};
		return getHibernateTemplate().execute(callback);
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
