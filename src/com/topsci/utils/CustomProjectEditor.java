package com.topsci.utils;

import java.beans.PropertyEditorSupport;

import com.topsci.bean.Project;
import com.topsci.service.IProjectService;

/**
 * @dateTime 2013-7-3 下午2:51:40
 * @author FengQing
 * @description 项目转换
 */
public class CustomProjectEditor extends PropertyEditorSupport {

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if(text==null)
			setValue(null);
		else{
			IProjectService userService  = (IProjectService) SpringUtil.getBean("projectServiceImpl");
			Project project = userService.get(text.trim());
			setValue(project);
		}
	}
}
