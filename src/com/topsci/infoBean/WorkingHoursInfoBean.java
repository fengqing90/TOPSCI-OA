package com.topsci.infoBean;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.topsci.bean.User;
import com.topsci.bean.WorkingHours;

/**
 * @author FengQing
 * @dateTime 2013-7-1 上午10:37:53
 * @description 查询工时页面封装bean,每一个bean代表一个人的工作信息
 */
public class WorkingHoursInfoBean implements Comparator<WorkingHoursInfoBean> {

    private Date date;
    private User user;
    private List<WorkingHours> workList = new ArrayList<WorkingHours>(0);
    private Double sum_PCT = 0.0;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        WorkingHoursInfoBean work = (WorkingHoursInfoBean) obj;
        if (work.getUser().getUser_id().equals(this.getUser().getUser_id())
            && (work.getDate() == null && this.date == null || work.getDate()
                .equals(this.date))) {
            return true;
        } else {
            return false;
        }
    }

    public int compare(WorkingHoursInfoBean o1, WorkingHoursInfoBean o2) {
//		int i = o1.getDate().compareTo(o2.getDate());
//		int j = o1.getSum_PCT().compareTo(o2.getSum_PCT());
//		if(i>0 && j<0){
//			return -1;
//		}else if(i>0 && j>0){
//			return 1;
//		}else if(i<0 && j<0){
//			return -1;
//		}else if(i<0 && j>0){
//			return -1;
//		}
        return o1.getDate().compareTo(o2.getDate());
    }

    public WorkingHoursInfoBean(Date date, User user) {
        super();
        this.date = date;
        this.user = user;
    }

    public WorkingHoursInfoBean() {
        super();
    }

    public User getUser() {
        return this.user;
    }

    public List<WorkingHours> getWorkList() {
        return this.workList;
    }

    public Date getDate() {
        return this.date;
    }

    public Double getSum_PCT() {
        if (this.sum_PCT >= 99.99) {
            return 100.00;
        }
        return this.sum_PCT;
    }

    public Integer getWorkCount() {
        return this.workList.size();
    }

    public void setSum_PCT(Double sum_PCT) {
        this.sum_PCT = sum_PCT;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setWorkList(List<WorkingHours> workList) {
        this.workList = workList;
    }

    public void setWorkCount(Integer workCount) {
    }

}
