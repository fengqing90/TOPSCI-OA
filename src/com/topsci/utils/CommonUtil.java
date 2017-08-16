package com.topsci.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonUtil {

	private static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);

	/**
	 * @description 解析前台传递过来的查询Bean 组成提供给查询语句需要的参数Map
	 * @param objectEntity
	 *            封装了查询参数的bean
	 * @return 参数Map
	 * @author FengQing
	 * @date 2013-5-7 下午04:07:25
	 */
	public static Map<String, Object> parseParamsByEntity(Object objectEntity) {
		String exclude = "Class,Serialversionuid";// class中自带的getClass方法不能当参数加入到查询语句中
		Map<String, Object> paramMap = new HashMap<String, Object>();
		try {
			Method[] methods = objectEntity.getClass().getMethods();
			for (Method method : methods) {
				String methodName = method.getName();
				String callType = methodName.substring(0, 3);
				if (callType.equals("get")) {
					String paramName = methodName.substring(3, methodName.length());
					if (exclude.indexOf(paramName) < 0) {
						Object res = method.invoke(objectEntity, null);
						if (res != null) {
							if (res instanceof Collection<?>) {
								Collection<?> collection = (Collection<?>) res;
								if (collection.size() > 0)
									paramMap.put(paramName.toLowerCase(), res);
							} else if (res instanceof String) {
								if (StringTools.replaceStringtoNull(res) != null)
									paramMap.put(paramName.toLowerCase(), res);
							} else if (res instanceof Date) {
								paramMap.put(paramName.toLowerCase() + "|DATE", res);
							} else {
								paramMap.put(paramName.toLowerCase(), res);
							}
						}
					}
				}
			}

			// if((res instanceof
			// Number)){//由于flex前面传递数字类型默认有值,会导致解析时把这个默认的当成查询条件
			// if(((Number)res).intValue()>0)
			// paramMap.put(paramName.toLowerCase(), ((Number)res).intValue());
			// }else if(res!=null){
			// paramMap.put(paramName.toLowerCase(), res);
			// }

			// 由于com.topsci.model中不是标准的javaBean命名形式，所以下列调用会报错
			// Field[] fields = objectEntity.getClass().getDeclaredFields();
			// for (Field field : fields) {
			// PropertyDescriptor pd = new
			// PropertyDescriptor(field.getName(),objectEntity.getClass());
			// Method getMethod = pd.getReadMethod();// 获得get方法
			// Object o = getMethod.invoke(null);// 执行get方法返回一个Object
			// }

		} catch (InvocationTargetException e) {
			e.getTargetException().printStackTrace();
			logger.error("解析查询参数Bean中的内部方法错误:" + e.getTargetException());
			if (e.getTargetException().getStackTrace() != null)
				if (e.getTargetException().getStackTrace()[0] != null)
					logger.error("Exception at:" + e.getTargetException().getStackTrace()[0].getClassName() + ":"
							+ e.getTargetException().getStackTrace()[0].getLineNumber());
		} catch (Exception e) {
			logger.error("解析查询参数Bean错误:" + e);
		}
		// System.out.println("paramMap:"+paramMap);
		return paramMap;
	}

	public static String formatDoubleToString(Double value, String pattern) {
		if (pattern == null)
			pattern = "0.00";
		DecimalFormat doubleFromat = new DecimalFormat(pattern);
		return doubleFromat.format(value);
	}
}