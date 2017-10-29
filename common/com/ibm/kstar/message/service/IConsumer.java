package com.ibm.kstar.message.service;

public interface IConsumer<T> {
	
	void consume(T t);
	
	String getQueueName();
}
