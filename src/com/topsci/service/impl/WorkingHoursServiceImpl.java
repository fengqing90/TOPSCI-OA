package com.topsci.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.topsci.bean.Project;
import com.topsci.bean.User;
import com.topsci.bean.WorkingHours;
import com.topsci.dao.impl.WorkingHoursDaoImpl;
import com.topsci.infoBean.WorkingHoursInfoBean;
import com.topsci.infoBean.WorkingHoursPercentageInfoBean;
import com.topsci.service.IUserService;
import com.topsci.service.IWorkingHoursService;
import com.topsci.utils.DateTimeUtils;
import com.topsci.utils.MyArrayList;
import com.topsci.utils.OA_Config;
import com.topsci.utils.Pager;
import com.topsci.utils.SpringUtil;
import com.topsci.utils.StringTools;

/**
 * @dateTime 2013-6-28 下午4:05:53
 * @author FengQing
 * @description work service
 */
@Service
@Transactional
public class WorkingHoursServiceImpl extends
        BaseServiceImpl<WorkingHours, String> implements IWorkingHoursService {

    @Autowired
    public void setBaseDao(WorkingHoursDaoImpl baseDao) {
        super.setBaseDao(baseDao);
    }

    @SuppressWarnings("unchecked")
    public Pager findRecordsGroupByUser(User user, String starDate,
            String endDate, Pager page) throws Exception {
        starDate = StringTools.replaceStringtoNull(starDate);
        endDate = StringTools.replaceStringtoNull(endDate);
        page.setLogicalPaging(true);
        List<WorkingHours> dataList = (List<WorkingHours>) this.findRecords(
            user, starDate, endDate, null, page).getList();
/////////////////////////////////原处理逻辑代码备份
//		List<List<WorkingHours>> groupList = new ArrayList<List<WorkingHours>>();
//		List<WorkingHours> workList = new ArrayList<WorkingHours>();
//		for (int i = 0; i < dataList.size(); i++) {
//			WorkingHours work = dataList.get(i);
//			if(workList.contains(work)){
//				workList.add(work);
//			}else{
//				if(workList.size()!=0){
//					groupList.add(workList);
//				}
//				workList = new ArrayList<WorkingHours>();
//				workList.add(work);
//			}
//			if(i==dataList.size()-1){
//				groupList.add(workList);
//			}
//		}
/////////////////////////////////

        List<WorkingHoursInfoBean> groupList = new ArrayList<WorkingHoursInfoBean>();
        WorkingHoursInfoBean infoBean = new WorkingHoursInfoBean();
        for (int i = 0; i < dataList.size(); i++) {
            WorkingHours work = dataList.get(i);
            if (infoBean.getWorkList().contains(work)) {
                infoBean.getWorkList().add(work);
                infoBean.setSum_PCT(infoBean.getSum_PCT()
                    + work.getWorking_hours_PCT());
            } else {
                if (infoBean.getWorkList().size() != 0) {
                    groupList.add(infoBean);
                }
                infoBean = new WorkingHoursInfoBean();
                infoBean.setUser(work.getUser());
                infoBean.setDate(work.getWorkaday());
                infoBean.setSum_PCT(work.getWorking_hours_PCT());
                infoBean.getWorkList().add(work);
            }
            if (i == dataList.size() - 1) {
                groupList.add(infoBean);
            }
        }
        ////////填充未登记工时天数
        List<Date> workadayList = this.calculateWorkaday(starDate, endDate);
        WorkingHoursInfoBean tempInfoBean = null;
        for (Date date : workadayList) {
            tempInfoBean = new WorkingHoursInfoBean(date, user);
            if (!groupList.contains(tempInfoBean)) {
                groupList.add(tempInfoBean);
            }
        }
        Collections.sort(groupList, new WorkingHoursInfoBean());

        /////////输出
//		for (WorkingHoursInfoBean bean : groupList) {
//			System.out.println("日期："+bean.getDate());
//			System.out.println("姓名："+bean.getUser().getUser_name());
//			List<WorkingHours> list = bean.getWorkList();
//			for (WorkingHours w : list) {
//				System.out.println(w.getProject().getProject_name());
//			}
//			for (WorkingHours w : list) {
//				System.out.println(w.getWorking_hours_PCT());
//			}
//		}
        /////////
        page.setTotalCount(groupList.size());
        int formIndex = 0;
        int toIndex = 0;
        formIndex = page.getPageNumber() - 1 < 0 ? 0
            : (page.getPageNumber() - 1) * page.getPageSize();
        formIndex = formIndex > groupList.size() ? 0 : formIndex;

        toIndex = page.getPageNumber() - 1 <= 0 ? page.getPageSize() : page
            .getPageNumber() * page.getPageSize();
        toIndex = toIndex > groupList.size() ? groupList.size() : toIndex;

        groupList = groupList.subList(formIndex, toIndex);
        page.setList(groupList);
        return page;
    }

    public String saveOrUpdate(WorkingHours work, User sessionUser,
            String processType, String projectCode) throws Exception {
        String processStatus = "error,";
        if (!processType.equals("delete")) {
            Double currWorkHours = this.calculateWorkHoursByUserByDate(
                sessionUser, work.getWorkaday(), " and project!='"
                    + work.getProject().getProject_code() + "'");
            if (currWorkHours + work.getWorking_hours_PCT() > 100) {
                return "warn,添加or修改失败，每天总工时不能大于100！";
            }

            StringBuffer hql = new StringBuffer();
            hql.append("from WorkingHours where 1=1 ");
            if (projectCode != null) {
                hql.append(" and project='" + projectCode + "'");
            }
            if (sessionUser != null) {
                hql.append("and user='" + sessionUser.getUser_id() + "'");
            }
            String date = null;
            if (work.getWorkaday() != null) {
                date = DateTimeUtils.validateDate(work.getWorkaday());
            } else {
                date = DateTimeUtils.validateDate(new Date());
            }
            hql.append(" and date_format(workaday,'%Y-%m-%d')='" + date + "' ");

            List<WorkingHours> dataWorkList = (List<WorkingHours>) super
                .findEntityByHql(hql.toString());
            WorkingHours dataWork = null;
            if (dataWorkList != null && dataWorkList.size() > 0) {
                dataWork = dataWorkList.get(0);
                dataWork.setWorking_hours_PCT(work.getWorking_hours_PCT());
            } else {
                dataWork = work;
                dataWork.setCreate_date(new Date());
                dataWork.setWork_id(UUID.randomUUID().toString());
                dataWork.setUser(sessionUser);
//				List<Project> proList = sessionUser.getProjectList();
//				dataWork.setProject(proList.get(proList.indexOf(new Project(projectCode))));
            }
            processStatus = "success," + super.saveOrUpdate(dataWork);
        } else if (processType.equals("delete")) {
            processStatus = super.delete(work.getWork_id()) == true ? "success,删除成功！"
                : processStatus;
        }
        return processStatus;
    }

    public Pager findRecords(User user, String starDate, String endDate,
            String queryDate, Pager page) {
        StringBuffer hql = new StringBuffer();
        hql.append("from WorkingHours where 1=1 ");
        if (starDate == null && endDate == null) {
            if (queryDate == null) {
                queryDate = DateTimeUtils.validateDate(new Date());
            }
            hql.append(" and date_format(workaday,'%Y-%m-%d')='" + queryDate
                + "' ");
        } else if (starDate != null && endDate != null) {
            hql.append(" and workaday between str_to_date('" + starDate
                + " 00:00:00','%Y-%m-%d %T') and str_to_date('" + endDate
                + " 23:59:59','%Y-%m-%d %T') ");
        }
        if (user != null) {
//		if(user!=null && !user.getUser_type().equals("ADMIN")){
            hql.append(" and user = '" + user.getUser_id() + "'");
        }
        hql.append(" order by workaday,user");
        return super.queryEntityByHql_Page(hql.toString(), page);
    }

    public WorkingHoursPercentageInfoBean queryWorkPercentage(User user,
            String starDate, String endDate, Pager page) throws Exception {
//		starDate = "2013-07-01";
//		endDate  = "2013-07-01";
//		user.setUser_type("ADMIN");
//		user.setUser_type(null);
        starDate = StringTools.replaceStringtoNull(starDate);
        endDate = StringTools.replaceStringtoNull(endDate);

        WorkingHoursPercentageInfoBean percentageInfoBean = new WorkingHoursPercentageInfoBean(
            page);

        //获取项目信息
        StringBuffer hql = new StringBuffer();
        hql.append("select p from WorkingHours wh left join wh.project p where 1=1 ");

        if (user.getUser_type() == null || !user.getUser_type().equals("ADMIN")) {
            hql.append(" and user_id ='" + user.getUser_id() + "'");
        }
        if (starDate == null && endDate == null) {
            hql.append(" and date_format(workaday,'%Y-%m-%d')='"
                + DateTimeUtils.validateDate(new Date()) + "' ");
        } else if (starDate != null && endDate != null) {
            hql.append(" and workaday between str_to_date('" + starDate
                + " 00:00:00','%Y-%m-%d %T') and str_to_date('" + endDate
                + " 23:59:59','%Y-%m-%d %T') ");
        }
        hql.append(" order by p.project_type,p.project_code");
        List<Project> dataProjectList = (List<Project>) super
            .findEntityByHql(hql.toString());
        for (Project project : dataProjectList) {
            if (!percentageInfoBean.getColumnList().contains(project)) {
                percentageInfoBean.getColumnList().add(project);
            }
        }

        //获取项目分组数量
        Map<String, Integer> groupColumn = percentageInfoBean
            .getColumnListGroup();
        for (Project project : percentageInfoBean.getColumnList()) {
            if (groupColumn.containsKey(project.getProject_type())) {
                groupColumn.put(project.getProject_type(),
                    groupColumn.get(project.getProject_type()) + 1);
            } else {
                groupColumn.put(project.getProject_type(), 1);
            }
        }

        //获取工作日天数
        List<Date> workadayList = this.calculateWorkaday(starDate, endDate);
        if (workadayList == null || workadayList.size() == 0) {
            throw new Exception("工作日天数等于0");
        }
        hql = new StringBuffer();
        hql.append("SELECT wh.user_id,wh.project_id,");
        hql.append("(SUM(WORKING_HOURS_PCT)/");
        ///////原始获取工作日天数方法
//		hql.append("(select count(DISTINCT workaday)  from Working_Hours t where t.user_id=wh.user_id");
//		if(starDate == null && endDate ==null){
//			hql.append(" and date_format(workaday,'%Y-%m-%d')='"+DateTimeUtils.validateDate(new Date())+"' ");
//		}else if(starDate != null && endDate !=null){
//			hql.append(" and workaday between str_to_date('"+starDate+" 00:00:00','%Y-%m-%d %T') and str_to_date('"+endDate+" 23:59:59','%Y-%m-%d %T') ");
//		}
//		hql.append("))");
        ///////新方法
        hql.append(workadayList.size());
        hql.append(")");
        hql.append("FROM Working_Hours wh left join user u on wh.user_id = u.user_id where 1=1 ");
        if (user.getUser_type() == null || !user.getUser_type().equals("ADMIN")) {
            hql.append(" and wh.user_id ='" + user.getUser_id() + "'");
        }
        if (starDate == null && endDate == null) {
            hql.append(" and date_format(workaday,'%Y-%m-%d')='"
                + DateTimeUtils.validateDate(new Date()) + "' ");
        } else if (starDate != null && endDate != null) {
            hql.append(" and workaday between str_to_date('" + starDate
                + " 00:00:00','%Y-%m-%d %T') and str_to_date('" + endDate
                + " 23:59:59','%Y-%m-%d %T') ");
        }

        hql.append(" group by wh.user_id,wh.project_id ");
        hql.append(" order by u.user_dept,wh.user_id,wh.workaday");

        List<?> dataList = this.baseDao.findEntityBySql(hql.toString());
        List<WorkingHours> dataWorkList = new ArrayList<WorkingHours>();
        if (dataList != null && dataList.size() > 0) {
            for (Object object : dataList) {
                dataWorkList.add(this.toBean(object));
            }
        }

        //拼数据
        LinkedList<Project> columnList = percentageInfoBean.getColumnList();
        List<WorkingHoursInfoBean> valueList = percentageInfoBean
            .getValueList();
        WorkingHoursInfoBean infoBean = new WorkingHoursInfoBean();

        for (int i = 0; i < dataWorkList.size(); i++) {
            WorkingHours work = dataWorkList.get(i);
            if (infoBean.getWorkList().contains(work)) {
                Integer index = columnList.indexOf(new Project(work
                    .getProject().getProject_code()));
                infoBean.getWorkList().set(index, work);
                infoBean.setSum_PCT(infoBean.getSum_PCT()
                    + work.getWorking_hours_PCT());
            } else {
                if (infoBean.getWorkList().size() != 0) {
                    valueList.add(infoBean);
                }
                infoBean = new WorkingHoursInfoBean();
                infoBean.setWorkList(new MyArrayList<WorkingHours>(
                    percentageInfoBean.getColumnList().size()));
                infoBean.getWorkList();
                infoBean.setUser(work.getUser());
                Integer index = columnList.indexOf(new Project(work
                    .getProject().getProject_code()));
                infoBean.getWorkList().set(index, work);
                infoBean.setSum_PCT(work.getWorking_hours_PCT());
            }
            if (dataList != null && i == dataList.size() - 1) {
                valueList.add(infoBean);
            }
        }

        //加入未登记工时的人员
        if (user.getUser_type().equals("ADMIN")) {
            List<User> dataUserList = (List<User>) this.baseDao
                .findEntityByHQL("from User");
            for (User u : dataUserList) {
                WorkingHoursInfoBean workInfo = new WorkingHoursInfoBean(null,
                    u);
                workInfo.setWorkList(new MyArrayList<WorkingHours>(
                    percentageInfoBean.getColumnList().size()));
                if (!valueList.contains(workInfo)) {
                    valueList.add(workInfo);
                }
            }
        }

        percentageInfoBean.setColumnList(columnList);
        percentageInfoBean.setValueList(valueList);

        Collections.sort(valueList, new Comparator<WorkingHoursInfoBean>() {
            public int compare(WorkingHoursInfoBean o1, WorkingHoursInfoBean o2) {
                String dept_o1 = StringTools.replaceNulltoString(o1.getUser()
                    .getUser_dept());
                String dept_o2 = StringTools.replaceNulltoString(o2.getUser()
                    .getUser_dept());
                return dept_o1.compareTo(dept_o2);
            }
        });

        ///////////////// 输出
//		System.out.println("列：");
//		for (Project p : columnList) {
//			System.out.print(p.getProject_code());
//		}
//		for (WorkingHoursInfoBean info : valueList) {
//			System.out.println("*********************");
//			System.out.println("用户ID："+info.getUser().getUser_id());
//			System.out.println("总比例："+info.getSum_PCT());
//			for (WorkingHours w : info.getWorkList()) {
//				if(w!=null){
//					System.out.println(w.getProject().getProject_code()+":"+w.getWorking_hours_PCT());
//				}else{
//					System.out.println("null");
//				}
//			}
//		}
        /////////////////
        return percentageInfoBean;
    }

    protected WorkingHours toBean(Object obj) {
        WorkingHours work = new WorkingHours();
        Object[] objs = (Object[]) obj;
        IUserService userService = (IUserService) SpringUtil
            .getBean("userServiceImpl");
        work.setUser(userService.get(objs[0].toString()));
        work.setProject(new Project(objs[1].toString()));
        work.setWorking_hours_PCT(((Number) objs[2]).doubleValue());
//		System.out.println(objs[0]+"****");
//		System.out.println("原数据："+work);
        return work;
    }

    public Double calculateWorkHoursByUserByDate(User user, Date date,
            String addHql) {
        StringBuffer hql = new StringBuffer();
        hql.append(" select sum(working_hours_PCT) from WorkingHours where 1=1 ");
        if (user != null) {
            hql.append(" and user_id='" + user.getUser_id() + "'");
        }
        if (date != null) {
            hql.append(" and date_format(workaday,'%Y-%m-%d')='"
                + DateTimeUtils.validateDate(date) + "' ");
        }
        if (addHql != null) {
            hql.append(addHql);
        }
        Double work_PCT = null;
        List<?> dataList = super.findEntityByHql(hql.toString());

        if (dataList != null && dataList.size() > 0) {
            work_PCT = (Double) dataList.get(0);
        }
        return work_PCT == null ? 0.0 : work_PCT;
    }

    public List<Date> calculateWorkaday(String str_startDate, String str_endDate)
            throws Exception {
        if (OA_Config.getConfig(OA_Config.THRESHOLD_WORKADAY) == null) {
            throw new Exception("未获取到<判定工作日阀值>！");
        }
        StringBuffer hql = new StringBuffer();
        hql.append("select workaday from WorkingHours where 1=1 ");
        if (str_startDate == null && str_endDate == null) {
            hql.append(" and date_format(workaday,'%Y-%m-%d')='"
                + DateTimeUtils.validateDate(new Date()) + "' ");
        } else if (str_startDate != null && str_endDate != null) {
            hql.append(" and workaday between str_to_date('" + str_startDate
                + " 00:00:00','%Y-%m-%d %T') and str_to_date('" + str_endDate
                + " 23:59:59','%Y-%m-%d %T') ");
        }
        hql.append("GROUP BY workaday HAVING(COUNT(DISTINCT user))>"
            + OA_Config.getConfig(OA_Config.THRESHOLD_WORKADAY));
        return (List<Date>) super.findEntityByHql(hql.toString());
    }
}
