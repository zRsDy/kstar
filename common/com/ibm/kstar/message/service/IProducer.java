package com.ibm.kstar.message.service;

public interface IProducer<T> {

	void produce(T message);
	
	String getQueueName();
	
}
