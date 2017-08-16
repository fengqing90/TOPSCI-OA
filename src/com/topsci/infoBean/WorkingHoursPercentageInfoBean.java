package com.topsci.infoBean;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.topsci.bean.Project;
import com.topsci.utils.MyArrayList;
import com.topsci.utils.Pager;

/**
 * @dateTime 2013-7-5 下午6:03:14
 * @author FengQing
 * @description 工时统计页面需要的封装bean
 */
public class WorkingHoursPercentageInfoBean {

    public WorkingHoursPercentageInfoBean(Pager pager) {
        super();
        this.pager = pager;
    }

    private Pager pager = new Pager();

    private Map<String, Integer> columnListGroup = new LinkedHashMap<String, Integer>();
    private LinkedList<Project> columnList = new LinkedList<Project>();
    private LinkedList<Object> firstColumnList = new LinkedList<Object>();

    private List<WorkingHoursInfoBean> valueList = new MyArrayList<WorkingHoursInfoBean>(
        0);

    public List<WorkingHoursInfoBean> getValueList() {
        // TODO 分页......

        return this.valueList;
    }

    public Pager getPager() {
        return this.pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public Map<String, Integer> getColumnListGroup() {
        return this.columnListGroup;
    }

    public LinkedList<Project> getColumnList() {
        return this.columnList;
    }

    public LinkedList<Object> getFirstColumnList() {
        return this.firstColumnList;
    }

    public void setColumnListGroup(Map<String, Integer> columnListGroup) {
        this.columnListGroup = columnListGroup;
    }

    public void setColumnList(LinkedList<Project> columnList) {
        this.columnList = columnList;
    }

    public void setFirstColumnList(LinkedList<Object> firstColumnList) {
        this.firstColumnList = firstColumnList;
    }

    public void setValueList(List<WorkingHoursInfoBean> valueList) {
        this.valueList = valueList;
    }

    public WorkingHoursPercentageInfoBean() {
        super();
    }

}
