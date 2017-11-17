package com.ibm.kstar.cache;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.xsnake.web.action.Condition;
import org.xsnake.web.action.PageCondition;
import org.xsnake.web.exception.AnneException;
import org.xsnake.web.page.IPage;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;
import com.ibm.kstar.api.system.lov.ILovMemberService;
import com.ibm.kstar.api.system.lov.entity.LovMember;
import com.ibm.kstar.api.system.permission.IEmployeeService;
import com.ibm.kstar.api.system.permission.entity.Employee;


public class CacheData {
	
	String conf;
	String env;
	
	MemCachedClient memCached ;
	
	ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
	
	@Autowired
	ILovMemberService lovMemberService;

	@Autowired
	IEmployeeService employeeService;

	private static CacheData cacheData;

	public CacheData(){
		cacheData = this;
	}

	public static CacheData getInstance() {
		return cacheData;
	}
	
	public void deleteAll(){
		memCached.flushAll();
	}
	
	public void delete(String key){
		Object obj = memCached.get(env+"_"+key);
		if(obj instanceof LinkedCacheData){
			LinkedCacheData data = (LinkedCacheData)obj;
			for(String k : data.keys){
				memCached.delete(env+"_"+k);
			}
		}
		memCached.delete(env+"_"+key);
	}

	public void set(String key, byte[] objects) {
		if(objects == null){
			delete(key);
			return;
		}
		
		if(objects.length > (1024 * 1024) ){
			final LinkedCacheData data = new LinkedCacheData(objects);
			for(final String k : data.keys){
				Thread t = new Thread(){
					public void run() {
						set(k, data.datas.get(k));
					};
				};
				fixedThreadPool.submit(t);
			}
			memCached.set(env+"_"+key, data);
		}else{
			memCached.set(env+"_"+key, objects);
		}
		
	}
	
	public void set(String key, Object object) {
		if(object == null){
			memCached.delete(env+"_"+key);
			return;
		}
		memCached.set(env+"_"+key, object);
	}

	public Object get(String key) {
		Object obj = memCached.get(env+"_"+key);
		if(obj instanceof LinkedCacheData){
			final LinkedCacheData data = (LinkedCacheData)obj;
			final CountDownLatch count = new CountDownLatch(data.keys.size());
			for(final String k : data.keys){
				Thread t = new Thread(){
					@Override
					public void run() {
						while(true){
							byte[] datas = (byte[])get(k);
							if(datas == null){
								try {
									TimeUnit.MILLISECONDS.sleep(100);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								continue;
							}
							data.getDatasMap().put(k, datas);
							count.countDown();
							break;
						}
					}
				};
				fixedThreadPool.submit(t);
			}
			try {
				count.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return data.getData();
		}
		return memCached.get(env+"_"+key);
	}

	String weights;
	
	int initConn = 1;
	
	int minConn = 5;
	
	int maxConn = 200;
	
	long maxIdle = 1000 * 30 * 30;
	
	boolean nagle = false; 
	
	int socketConnectTO = 0;

	public void initMemCached(){
		if(conf == null){
			throw new AnneException("memcached配置不能为空");
		}
		SockIOPool pool = SockIOPool.getInstance();
		String[] addr = conf.split(",");
		pool.setServers(addr);
		if(weights!=null){
			String[] weights_ = weights.split(",");
			Integer[] $weights = new Integer[weights_.length];
			for(int i=0;i<weights_.length;i++){
				$weights[i] = Integer.parseInt(weights_[i]);
			}
			pool.setWeights($weights);
		}
		pool.setInitConn(initConn);
		pool.setMinConn(minConn);
		pool.setMaxConn(maxConn);
		
		pool.setMaxIdle(maxIdle);
		pool.setNagle(nagle);
		pool.setSocketConnectTO(socketConnectTO);
		pool.initialize();
		
		memCached = new MemCachedClient(); 
	}
	
	public void init(){
		if(memCached == null){
			initMemCached();
		}
		initData();
	}

	public void initData() {
		System.out.println("========================数据初始化========================");
		Thread t1 = new Thread(){
			public void run() {
				int maxLine = 10000;
				PageCondition condition = new PageCondition();
				condition.setRows(maxLine);
				condition.setPage(1);
				IPage page = lovMemberService.query(condition);
				putMember(page.getList());
				int pageCount = page.getPageCount();
				for(int i=2;i<=pageCount;i++){
					condition.setRows(maxLine);
					condition.setPage(i);
					putMember(lovMemberService.query(condition).getList());
				}
				System.out.println("初始数据缓存！");
				
				//---------------------------------------
				
				Date lastUpdatedDate = new Date();
				while(true){
					System.out.println("---------------------------");
					long time = System.currentTimeMillis() - lastUpdatedDate.getTime();
					long refreshTime = 1000*5;
					if(time > refreshTime){
						Condition condition2 = new Condition();
						condition2.setCondition("lastUpdatedDate", lastUpdatedDate);
						List<LovMember> list = lovMemberService.getLovMemberList4Cache(condition2); 
						if(list.size()>0){
							System.out.println("更新数据缓存！共更新："+list.size());
							lastUpdatedDate = new Date();
							putMember(list);
						}
					}
					try {
						TimeUnit.SECONDS.sleep(300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		t1.start();

		Thread t2 = new Thread(){
			public void run() {
				while(true){
					List<Employee> list = employeeService.getAllEmployee();
					putEmployee(list);
					try {
						TimeUnit.SECONDS.sleep(300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		t2.start();
	}
	
	private void putEmployee(List<Employee> list){
		for(Employee e : list){
			set(e.getId(),e);
		}
	}
	
	private void putMember(List<?> list) {
		for(int i=0;i<list.size();i++){
			LovMember member = (LovMember)list.get(i);
			set(member.getGroupCode() +"_"+ member.getCode(), member);
			set(member.getId(),member);
		}
	}
	
	public Object getMember(String id) {
		return get(id);
	}

	public String getLovMemberName(String id) {
		LovMember lovMember = (LovMember) get(id);
		if (lovMember != null) {
			return lovMember.getName();
		}
		return "";
	}

	public Object getMember(String groupCode,String code) {
		return get(groupCode +"_"+ code);
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public String getConf() {
		return conf;
	}

	public void setConf(String conf) {
		this.conf = conf;
	}
	

}
