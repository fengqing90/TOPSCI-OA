package com.topsci.service;

import java.util.Date;
import java.util.List;

import com.topsci.bean.User;
import com.topsci.bean.WorkingHours;
import com.topsci.infoBean.WorkingHoursPercentageInfoBean;
import com.topsci.utils.Pager;

public interface IWorkingHoursService extends BaseService<WorkingHours, String> {

	public Pager findRecords(User user, String starDate, String endDate,String queryDate, Pager page);

	public String saveOrUpdate(WorkingHours work, User sessionUser,String processType,String projectCode) throws Exception;
	
	public Pager findRecordsGroupByUser(User user, String starDate, String endDate, Pager page) throws Exception;
	
	/**
	 * 工时统计
	 * @param user
	 * @param startDate
	 * @param endDate
	 * @param pager
	 * @return
	 * @throws Exception 
	 */
	public WorkingHoursPercentageInfoBean queryWorkPercentage(User user,String startDate, String endDate, Pager pager) throws Exception;
	
	
	public Double calculateWorkHoursByUserByDate(User user, Date date,String addHql);
	
	/**
	 * 计算工作日
	 * @param str_startDate
	 * @param str_endDate
	 * @return
	 * @throws Exception 
	 */
	public List<Date> calculateWorkaday(String str_startDate,String str_endDate) throws Exception;
}
