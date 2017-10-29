package com.ibm.kstar.message.service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.xsnake.web.utils.MessageUtil;
import org.xsnake.web.utils.StringUtil;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public abstract class MessageAdapter<T> implements IProducer<T>,IConsumer<T>{
	
	@Autowired
	protected MessageConfig messageConfig;
	
	private Connection connection;
	
	private Channel produceChannel;
	
	private Channel consumeChannel;
	
	@PostConstruct
	public void init() throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(messageConfig.getHost());
		factory.setPort(messageConfig.getPort());
		if (!StringUtil.isEmpty(messageConfig.getUsername())) {
			factory.setUsername(messageConfig.getUsername());
		}
		if (!StringUtil.isEmpty(messageConfig.getPassword())) {
			factory.setPassword(messageConfig.getPassword());
		}
		connection = factory.newConnection();
		
		if(isProducer()){
			produceChannel = connection.createChannel();
			produceChannel.queueDeclare(messageConfig.getEnv()+"_"+getQueueName(), false, false, false, null);
		}
		
		if(isConsumer()){
			consumeChannel = connection.createChannel();
			consumeChannel.queueDeclare(messageConfig.getEnv()+"_"+getQueueName(), false, false, false, null);
			consumeChannel.basicConsume(messageConfig.getEnv()+"_"+getQueueName(), true, new DefaultConsumer(consumeChannel) {
				@Override
				public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
					T message = MessageUtil.bytesToObject(body);
					consume(message);
				}
			});
		}
	}

	@Override
	public void produce(T message) {
		try {
			produceChannel.basicPublish("", messageConfig.getEnv()+"_"+getQueueName(), null, MessageUtil.objectToBytes(message));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public abstract boolean isProducer();
	
	public abstract boolean isConsumer();
}
