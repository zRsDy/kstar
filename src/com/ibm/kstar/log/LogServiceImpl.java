package com.ibm.kstar.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xsnake.web.dao.BaseDao;

import com.ibm.kstar.message.service.MessageAdapter;

@Service
@Transactional(readOnly=false,rollbackFor=Exception.class)
public class LogServiceImpl extends MessageAdapter<LogObject> implements ILogService{
	
	@Autowired
	BaseDao baseDao;
	
	/**
	 * 队列的名称
	 */
	@Override
	public String getQueueName() {
		return "XLOG";
	}
	
	/**
	 * 消费者
	 */
	@Override
	public void consume(LogObject log) {
		baseDao.save(log);
	}
	
	/**
	 * 生产者
	 */
	@Override
	public void log(LogObject log) {
		produce(log);
	}

	@Override
	public boolean isProducer() {
		return true;
	}

	@Override
	public boolean isConsumer() {
		return true;
	}
	
}
