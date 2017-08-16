package com.topsci.bean;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.topsci.utils.DateTimeUtils;

/**
 * @date 2013-6-24 下午10:07:21
 * @author FengQing
 * @description 工时
 */
@Entity
@Table(name = "WORKING_HOURS")
public class WorkingHours implements Serializable {

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        WorkingHours work = (WorkingHours) obj;
//		System.out.println("work.User_id:"+work.getUser().getUser_id());
//		System.out.println("this.User_id:"+this.getUser().getUser_id());
//		System.out.println("work.workaday:"+work.getWorkaday());
//		System.out.println("this.workaday:"+this.getWorkaday());
        if (work.getUser().getUser_id().equals(this.getUser().getUser_id())
            && (work.getWorkaday() == null && this.workaday == null || work
                .getWorkaday().equals(this.workaday))) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "WorkingHours[work_id=" + this.work_id + ",working_hours_PCT="
            + this.working_hours_PCT + "]";
    }

    /**
	 *
	 */
    private static final long serialVersionUID = -7439018169365875232L;

    @Id
    @Column(name = "WORK_ID")
    private String work_id;

    @Column(name = "CREATE_DATE")
    private Date create_date;

    @Column(name = "WORKADAY")
    private Date workaday;

    @ManyToOne
    @JoinColumn(name = "PROJECT_ID")
    private Project project;

    @Column(name = "WORKING_HOURS_PCT")
    private Double working_hours_PCT = 0.0;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    public Project getProject() {
        return this.project;
    }

    public Double getWorking_hours_PCT() {
        return this.working_hours_PCT;
    }

    public User getUser() {
        return this.user;
    }

    public Date getWorkaday() {
        return this.workaday;
    }

    public Date getCreate_date() {
        return this.create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public void setWorkaday(Date workaday) {
        this.workaday = workaday;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setWorking_hours_PCT(Double workingHoursPCT) {
        this.working_hours_PCT = workingHoursPCT;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getWork_id() {
        return this.work_id;
    }

    public void setWork_id(String work_id) {
        this.work_id = work_id;
    }

    public WorkingHours() {
        super();
        try {
            this.workaday = DateTimeUtils.dateFormat
                .parse(DateTimeUtils.dateFormat.format(new Date()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
