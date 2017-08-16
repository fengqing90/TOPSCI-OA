package com.topsci.infoBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.topsci.utils.Pager;

public class AttendanceRecordsStatisInfoBean {
    private List<Date> columnList = new ArrayList<Date>(0);
    private List<String> firstColumnList = new ArrayList<String>(0);
    private List<AttendanceRecordsInfoBean> valueList = new ArrayList<AttendanceRecordsInfoBean>(
        0);
    private Pager pager = new Pager();

    public List<AttendanceRecordsInfoBean> getValueList() {
        // TODO 分页......
        return this.valueList;
    }

    public List<Date> getColumnList() {
        return this.columnList;
    }

    public void setColumnList(List<Date> columnList) {
        this.columnList = columnList;
    }

    public List<String> getFirstColumnList() {
        return this.firstColumnList;
    }

    public void setFirstColumnList(List<String> firstColumnList) {
        this.firstColumnList = firstColumnList;
    }

    public void setValueList(List<AttendanceRecordsInfoBean> valueList) {
        this.valueList = valueList;
    }

    public Pager getPager() {
        return this.pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

}
