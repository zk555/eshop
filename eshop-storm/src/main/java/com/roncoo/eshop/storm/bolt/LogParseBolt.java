package com.roncoo.eshop.storm.bolt;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * 日志解析的bolt
 * @author Administrator
 *
 */
public class LogParseBolt extends BaseRichBolt {

	private static final long serialVersionUID = -8017609899644290359L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LogParseBolt.class);

	private OutputCollector collector;
	
	@SuppressWarnings("rawtypes")
	public void prepare(Map conf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}
	
	public void execute(Tuple tuple) {
		String message = tuple.getStringByField("message");  
		
		LOGGER.info("【LogParseBolt接收到一条日志】message=" + message);  
		
		JSONObject messageJSON = JSONObject.parseObject(message);
		JSONObject uriArgsJSON = messageJSON.getJSONObject("uri_args"); 
		Long productId = uriArgsJSON.getLong("productId"); 
		
		if(productId != null) {
			collector.emit(new Values(productId));
			LOGGER.info("【LogParseBolt发射出去一个商品id】productId=" + productId);  
		}
	}
	
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("productId"));
	}

}
