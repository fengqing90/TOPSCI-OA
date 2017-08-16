package com.topsci.infoBean;

import java.util.ArrayList;
import java.util.List;

import com.topsci.bean.AttendanceRecords;
import com.topsci.bean.User;

public class AttendanceRecordsInfoBean {
    private User user;
    private List<AttendanceRecords> arList = new ArrayList<AttendanceRecords>(0);

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<AttendanceRecords> getArList() {
        return this.arList;
    }

    public void setArList(List<AttendanceRecords> arList) {
        this.arList = arList;
    }

    @Override
    public boolean equals(Object obj) {
        return this.user.equals(((AttendanceRecordsInfoBean) obj).getUser());
    }
}
