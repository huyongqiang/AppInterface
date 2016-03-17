package com.sniyve.ai;

import java.util.Map;

/**
 * Callback
 * 订阅发布模型所需要的回调接口
 */
public abstract class Callback {
	public abstract void call(Map<String,Object> params);
}
