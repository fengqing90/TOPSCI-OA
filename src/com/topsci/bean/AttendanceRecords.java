package com.topsci.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.topsci.utils.DateTimeUtils;
import com.topsci.utils.OA_Config;

/**
 * @date 2013-6-24 下午06:21:15
 * @author FengQing
 * @description 考勤记录
 */
@Entity
@Table(name = "ATTENDANCE_RECORDS")
public class AttendanceRecords implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -303240241407744199L;

	@Id
	@Column(name = "AR_ID")
	private String ar_id;

	@Column(name = "CREATE_DATE")
	private Date create_date;

	@Column(name = "ON_DATE")
	private Date on_Date;

	@Column(name = "OFF_DATE")
	private Date off_Date;

	@Column(name = "ON_DESCRIPTION")
	private String on_description;

	@Column(name = "OFF_DESCRIPTION")
	private String off_description;

	@OneToOne
	@JoinColumn(name = "USER_ID")
	private User user;

	private String is_enable_on = "N";

	private String is_enable_off = "N";

	
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null) return false;
		return this.user.getUser_id().equals(((AttendanceRecords)obj).user.getUser_id());
//		return this.create_date.equals(((AttendanceRecords)obj).create_date);
	}

	@Transient
	public String getIs_enable_on() {
		try {
			Date startDate = DateTimeUtils.dateTimeFormat.parse(DateTimeUtils.validateDate(new Date()) + " "+OA_Config.getConfig(OA_Config.ATTENDANCE_ON_STARTDATE));
			Date laterDate = DateTimeUtils.dateTimeFormat.parse(DateTimeUtils.validateDate(new Date()) + " "+OA_Config.getConfig(OA_Config.ATTENDANCE_ON));
			Date endDate = DateTimeUtils.dateTimeFormat.parse(DateTimeUtils.validateDate(new Date()) + " "+OA_Config.getConfig(OA_Config.ATTENDANCE_ON_ENDDATE));
			Date currDate = new Date();
			if (currDate.after(startDate) && currDate.before(endDate)) { 
				is_enable_on = "Y";
			}
			
			if(getOn_Date()!=null && getOn_Date().after(laterDate)){
				is_enable_on = "L"; 
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.is_enable_on;
//		return "Y";
	}

	@Transient
	public String getIs_enable_off() {
		try {
			Date startDate = DateTimeUtils.dateTimeFormat.parse(DateTimeUtils.validateDate(new Date()) + " "+OA_Config.getConfig(OA_Config.ATTENDANCE_OFF_STARTDATE));
			Date endDate = DateTimeUtils.dateTimeFormat.parse(DateTimeUtils.validateDate(new Date()) + " "+OA_Config.getConfig(OA_Config.ATTENDANCE_OFF_ENDDATE));
			Date currDate = new Date();
			if (currDate.after(startDate) && currDate.before(endDate)) {
				is_enable_off = "Y";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.is_enable_off;
//		return "Y";
	}
	
	public String getAr_id() {
		return ar_id;
	}

	public Date getOn_Date() {
		return on_Date;
	}

	public Date getOff_Date() {
		return off_Date;
	}

	public User getUser() {
		return user;
	}

	public Date getCreate_date() {
		return create_date;
	}

	public String getOn_description() {
		return on_description;
	}

	public String getOff_description() {
		return off_description;
	}


	public void setIs_enable_on(String isEnableOn) {
		is_enable_on = isEnableOn;
	}

	public void setIs_enable_off(String isEnableOff) {
		is_enable_off = isEnableOff;
	}

	public void setOn_description(String onDescription) {
		on_description = onDescription;
	}

	public void setOff_description(String offDescription) {
		off_description = offDescription;
	}


	public void setAr_id(String ar_id) {
		this.ar_id = ar_id;
	}

	public void setCreate_date(Date createDate) {
		create_date = createDate;
	}

	public void setOn_Date(Date onDate) {
		on_Date = onDate;
	}

	public void setOff_Date(Date offDate) {
		off_Date = offDate;
	}

	public void setUser(User user) {
		this.user = user;
	}


	public AttendanceRecords(String ar_id, Date create_date) {
		super();
		this.ar_id = ar_id;
		this.create_date = create_date;
	}

	public AttendanceRecords() {
		super();
	}
}
