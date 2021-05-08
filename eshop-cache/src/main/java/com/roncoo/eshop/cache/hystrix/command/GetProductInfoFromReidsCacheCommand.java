package com.roncoo.eshop.cache.hystrix.command;

import redis.clients.jedis.JedisCluster;

import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.roncoo.eshop.cache.model.ProductInfo;
import com.roncoo.eshop.cache.spring.SpringContext;
/**
 *功能： 快速失败模式，
 * @param null 1
 * @version 0.0.1
 * @return
 * @author zhaokai108
 * @date 2021/3/23 11:08
 */
public class GetProductInfoFromReidsCacheCommand extends HystrixCommand<ProductInfo> {

	private Long productId;
	
	public GetProductInfoFromReidsCacheCommand(Long productId) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("RedisGroup"))
				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
						.withExecutionTimeoutInMilliseconds(100)	//timeOut超时时间
						.withCircuitBreakerRequestVolumeThreshold(1000) //10秒内要1000个请求开始触发断路器
						.withCircuitBreakerErrorThresholdPercentage(70)	//失败率 70%
						.withCircuitBreakerSleepWindowInMilliseconds(60 * 1000)) //阻断多久重新close断路器
				);  
		this.productId = productId;
	}
	
	@Override
	protected ProductInfo run() throws Exception {
		JedisCluster jedisCluster = (JedisCluster) SpringContext.getApplicationContext()
				.getBean("JedisClusterFactory"); 
		String key = "product_info_" + productId;
		String json = jedisCluster.get(key);
		if(json != null) {
			return JSONObject.parseObject(json, ProductInfo.class);
		}
		return null;
	}
	
	@Override
	protected ProductInfo getFallback() {
		return null;
	}

}
