package com.roncoo.eshop.cache.kafka;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.roncoo.eshop.cache.model.ProductInfo;
import com.roncoo.eshop.cache.model.ShopInfo;
import com.roncoo.eshop.cache.service.CacheService;
import com.roncoo.eshop.cache.spring.SpringContext;
import com.roncoo.eshop.cache.zk.ZooKeeperSession;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;

/**
 * kafka消息处理线程
 * @author Administrator
 *
 */
@SuppressWarnings("rawtypes")
public class KafkaMessageProcessor implements Runnable {
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private KafkaStream kafkaStream;
	private CacheService cacheService;
	
	public KafkaMessageProcessor(KafkaStream kafkaStream) {
		this.kafkaStream = kafkaStream;
		this.cacheService = (CacheService) SpringContext.getApplicationContext()
				.getBean("cacheService"); 
	}
	
	@SuppressWarnings("unchecked")
	public void run() {
		ConsumerIterator<byte[], byte[]> it = kafkaStream.iterator();
        while (it.hasNext()) {
        	String message = new String(it.next().message());
			System.out.println(message);
        	// 首先将message转换成json对象
        	JSONObject messageJSONObject = JSONObject.parseObject(message);
        	
        	// 从这里提取出消息对应的服务的标识
        	String serviceId = messageJSONObject.getString("serviceId");  
        	
        	// 如果是商品信息服务
        	if("productInfoService".equals(serviceId)) {
        		processProductInfoChangeMessage(messageJSONObject);
        	} else if("shopInfoService".equals(serviceId)) {
        		processShopInfoChangeMessage(messageJSONObject);  
        	}
        }
	}
	
	/**
	 * 处理商品信息变更的消息 -- 重新构建缓存
	 * @param messageJSONObject
	 */
	private void processProductInfoChangeMessage(JSONObject messageJSONObject) {
		// 提取出商品id
		Long productId = messageJSONObject.getLong("productId");

		
		String productInfoJSON = "{\"id\": 5, \"name\": \"iphone7手机\", \"price\": 5599, \"pictureList\":\"a.jpg,b.jpg\", \"specification\": \"iphone7的规格\", \"service\": \"iphone7的售后服务\", \"color\": \"红色,白色,黑色\", \"size\": \"5.5\", \"shopId\": 1, \"modifiedTime\": \"2017-01-01 12:00:00\"}";
		ProductInfo productInfo = JSONObject.parseObject(productInfoJSON, ProductInfo.class);
		
		
		// 加代码，在将数据直接写入redis缓存之前，应该先获取一个zk的分布式锁
		ZooKeeperSession zkSession = ZooKeeperSession.getInstance();
		zkSession.acquireDistributedLock(productId);  
		
		// 获取到了锁
		// 先从redis中获取数据
		ProductInfo existedProductInfo = cacheService.getProductInfoFromReidsCache(productId);
		
		if(existedProductInfo != null) {
			// 比较当前数据的时间版本比已有数据的时间版本是新还是旧, 版本旧则不更新
			try {
				Date date = sdf.parse(productInfo.getModifiedTime());
				Date existedDate = sdf.parse(existedProductInfo.getModifiedTime());
				
				if(date.before(existedDate)) {
					System.out.println("current date[" + productInfo.getModifiedTime() + "] is before existed date[" + existedProductInfo.getModifiedTime() + "]");
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("current date[" + productInfo.getModifiedTime() + "] is after existed date[" + existedProductInfo.getModifiedTime() + "]");
		} else {
			System.out.println("existed product info is null......");   
		}
		
		try {
			Thread.sleep(10 * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		cacheService.saveProductInfo2LocalCache(productInfo);
		System.out.println("===================获取刚保存到本地缓存的商品信息：" + cacheService.getProductInfoFromLocalCache(productId));  
		cacheService.saveProductInfo2ReidsCache(productInfo);  
		
		// 释放分布式锁
		zkSession.releaseDistributedLock(productId); 
	}
	
	/**
	 * 店铺信息变更--查询详情保存到缓存
	 * @param messageJSONObject
	 */
	@SuppressWarnings("unused")
	private void processShopInfoChangeMessage(JSONObject messageJSONObject) {
		// 提取出商品id
		Long productId = messageJSONObject.getLong("productId");
		Long shopId = messageJSONObject.getLong("shopId");


		String shopInfoJSON = "{\"id\": 1, \"name\": \"小王的手机店\", \"level\": 5, \"goodCommentRate\":0.99}";
		ShopInfo shopInfo = JSONObject.parseObject(shopInfoJSON, ShopInfo.class);
		cacheService.saveShopInfo2LocalCache(shopInfo);
		System.out.println("===================获取刚保存到本地缓存的店铺信息：" + cacheService.getShopInfoFromLocalCache(shopId));   
		cacheService.saveShopInfo2ReidsCache(shopInfo);  
	}

}
