package com.topsci.utils;

import java.beans.PropertyEditorSupport;

/**
 * @dataTime 2013-6-29 下午12:03:08
 * @author FengQing
 * @description 转换字符串传递
 */
public class CustomStringEditor extends PropertyEditorSupport {

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if(text==null){
			setValue(null);
		}else{
			text = text.trim();
			if(text.length()==0)
				setValue(null);
			else
				setValue(text);
		}
	}

	@Override
	public String getAsText() {
		return getValue()==null?"":super.getAsText();
	}
}
