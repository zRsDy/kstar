package com.ibm.kstar.api.channel;

import java.util.List;

import org.xsnake.web.action.PageCondition;
import org.xsnake.web.page.IPage;

import com.ibm.kstar.api.system.permission.UserObject;
import com.ibm.kstar.entity.channel.DeliveryForecastDetail;
import com.ibm.kstar.entity.channel.OrderForecast;
import com.ibm.kstar.entity.channel.OrderForecastDetail;

public interface IOrderForecastService {
	
	IPage queryOrderForecasts(PageCondition condition, UserObject user);
	
	OrderForecast queryOrderForecast(String id);
	
	void addOrEditForecast(OrderForecast orderForecast, UserObject user);
	
	void deleteForecast(String id);
	
	IPage queryForecastDetails(PageCondition condition);
	
	OrderForecastDetail queryForecastDetail(String id);
	
	void addOrEditDetail(OrderForecastDetail forecastDetail, UserObject user);
	
	void deleteForecastDetail(String id);
	
	IPage queryDeliveryForecastDetails(PageCondition condition);
	
	DeliveryForecastDetail queryDeliveryForecastDetail(String id);
	
	void addOrEditDelivery(DeliveryForecastDetail delivery, UserObject user);
	
	void deleteDeliveryForecastDetail(String id);
	
	IPage queryOrderForecastGathers(PageCondition condition);
	
	IPage queryOrderForecastGatherDetails(PageCondition condition);

	void submitForecast(UserObject uo, String id);
	
	List<List<Object>> exportGather();
	
	List<List<Object>> exportGatherDetail(String materielCode,String forecastWeek);
	
}
