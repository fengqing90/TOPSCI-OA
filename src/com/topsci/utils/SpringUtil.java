package com.topsci.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public final class SpringUtil implements ApplicationContextAware {

	private final static Logger logger = LoggerFactory.getLogger(SpringUtil.class);

	public static ApplicationContext applicationContext = null;

	public SpringUtil() {
	}

	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		logger.info("********* setApplicationContext Initialize ApplicationContext ***********");
		SpringUtil.applicationContext = arg0;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static Object getBean(String name) {
		return getApplicationContext().getBean(name);
	}

}
