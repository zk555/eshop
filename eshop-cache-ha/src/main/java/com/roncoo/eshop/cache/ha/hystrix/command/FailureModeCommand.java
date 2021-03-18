package com.roncoo.eshop.cache.ha.hystrix.command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

/**
 * 快速失败模式
 */
public class FailureModeCommand extends HystrixCommand<Boolean> {

	private boolean failure;
	
	public FailureModeCommand(boolean failure) {
		super(HystrixCommandGroupKey.Factory.asKey("FailureModeGroup"));
		this.failure = failure;
	}
	// fail-fast ，不给降级逻辑直接抛出异常
	@Override
	protected Boolean run() throws Exception {
		if(failure) {
			throw new Exception();
		}
		return true;
	}
	//fail-silent，给一个fallback降级逻辑.
	@Override
	protected Boolean getFallback() {
		return false;
	}
	
}
