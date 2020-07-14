package com.dinson.blingbase.utils.validations;

import android.content.Context;

/**
 * 
 * 校验执行者
 * 
 * @author ken.cai
 * 
 */
public abstract class ValidationExecutor extends ValidationUtil {

	/**
	 * 
	 * 这里做校验处理
	 * 
	 * @return 校验成功返回true 否则返回false
	 */
	public abstract boolean doValidate(Context context, String text);
	
}
