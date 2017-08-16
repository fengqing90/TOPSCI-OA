package com.topsci.service;

import com.topsci.bean.AttendanceRecords;
import com.topsci.bean.User;
import com.topsci.infoBean.AttendanceRecordsStatisInfoBean;
import com.topsci.utils.Pager;

public interface IAttendanceRecordsService
        extends BaseService<AttendanceRecords, String> {

    /**
     * 注册 由于连接LDAP暂时弃用
     * 
     * @param user
     * @param type
     * @param ip
     * @return
     * @throws Exception
     */
    public AttendanceRecords registered(User user, String type, String ip)
            throws Exception;

    /**
     * 查询
     * 
     * @param user
     * @param starDate
     * @param endDate
     * @param page
     * @return
     */
    public Pager findRecords(User user, String starDate, String endDate,
            Pager page);

    public String mandatoryUpdate(String userName, String userPwd, String type,
            String date, String dateTime);

    public AttendanceRecordsStatisInfoBean queryARStatis(String userName,
            String str_startDate, String str_endDate, Pager page)
            throws Exception;
}
