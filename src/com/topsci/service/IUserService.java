package com.topsci.service;


import com.topsci.bean.User;
import com.topsci.utils.Pager;

public interface IUserService extends BaseService<User, String> {
	
	public User login(User enetity);
	
	public String findUserByName_JS(String userName);
	
	public Pager queryUserByName(String userName,Pager pager);
	
	public String saveOrUpdate(String processType,User user) throws Exception;
	
}
