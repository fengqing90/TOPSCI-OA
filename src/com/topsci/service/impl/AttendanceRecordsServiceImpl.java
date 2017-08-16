package com.topsci.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.topsci.bean.AttendanceRecords;
import com.topsci.bean.User;
import com.topsci.dao.impl.AttendanceRecordsDaoImpl;
import com.topsci.infoBean.AttendanceRecordsInfoBean;
import com.topsci.infoBean.AttendanceRecordsStatisInfoBean;
import com.topsci.service.IAttendanceRecordsService;
import com.topsci.service.IWorkingHoursService;
import com.topsci.utils.DateTimeUtils;
import com.topsci.utils.IP_Filter;
import com.topsci.utils.LdapHelper;
import com.topsci.utils.MyArrayList;
import com.topsci.utils.OA_Config;
import com.topsci.utils.Pager;
import com.topsci.utils.SpringUtil;
import com.topsci.utils.StringTools;

@Transactional
@Service
public class AttendanceRecordsServiceImpl extends
        BaseServiceImpl<AttendanceRecords, String> implements
        IAttendanceRecordsService {

    @Autowired
    public void setBaseDao(AttendanceRecordsDaoImpl baseDao) {
        super.setBaseDao(baseDao);
    }

    public Pager findRecords(User user, String starDate, String endDate,
            Pager page) {
        StringBuffer hql = new StringBuffer();
        hql.append("from AttendanceRecords where 1=1 ");
        if (starDate == null && endDate == null) {
            hql.append(" and date_format(create_date,'%Y-%m-%d')='"
                + DateTimeUtils.validateDate(new Date()) + "' ");
        } else if (starDate != null && endDate != null) {
            hql.append(" and create_date between str_to_date('" + starDate
                + " 00:00:00','%Y-%m-%d %T') and str_to_date('" + endDate
                + " 23:59:59','%Y-%m-%d %T') ");
        }
        if (user != null) {
            hql.append(" and user = '" + user.getUser_id() + "'");
        }
        hql.append(" order by create_date");
        return this.baseDao.queryEntityByHql_Page(hql.toString(), page);
    }

    public AttendanceRecords registered(User user, String type, String ip)
            throws Exception {
        //ajax 和此处 双重验证
        String status = new IP_Filter(OA_Config.ALLOW_IP_ADDRESS).filter(ip);
        if (status.equals("remote_networks")) {
            throw new Exception("warn,IP：" + ip + " 无法进行考情登记操作！");
        }

        List<AttendanceRecords> list = (List<AttendanceRecords>) this
            .findRecords(user, null, null, null).getList();
        AttendanceRecords ar = null;

        if (list.size() > 0) {
            ar = list.get(0);
        }
        if (ar == null) {
            ar = new AttendanceRecords(UUID.randomUUID().toString(),
                DateTimeUtils.validateDate(DateTimeUtils
                    .validateDate(new Date())));
        }
        ar.setUser(user);

        if (type.equals("ON")) {
            ar.setOn_Date(new Date());
        } else if (type.equals("OFF")) {
            IWorkingHoursService workService = (IWorkingHoursService) SpringUtil
                .getBean("workingHoursServiceImpl");
            Double work_PCT = workService.calculateWorkHoursByUserByDate(user,
                new Date(), null);
            if (work_PCT != null && work_PCT == 100.0) {
                ar.setOff_Date(new Date());
            } else {
                throw new Exception("error,请填写或修改工时！");
            }
        }
        super.save(ar);
        return ar;
    }

    public AttendanceRecordsStatisInfoBean queryARStatis(String userName,
            String str_startDate, String str_endDate, Pager page)
            throws Exception {
        userName = StringTools.replaceStringtoNull(userName);
        str_startDate = StringTools.replaceStringtoNull(str_startDate);
        str_endDate = StringTools.replaceStringtoNull(str_endDate);
        AttendanceRecordsStatisInfoBean statisInfoBean = new AttendanceRecordsStatisInfoBean();
        StringBuffer hql = new StringBuffer();
        hql.append("from AttendanceRecords where 1=1 ");
        if (str_startDate == null && str_endDate == null) {
            hql.append(" and date_format(create_date,'%Y-%m-%d')='"
                + DateTimeUtils.validateDate(new Date()) + "' ");
//			statisInfoBean.getColumnList().add(DateTimeUtils.validateDate(DateTimeUtils.dateFormat.format(new Date())));
        } else if (str_startDate != null && str_endDate != null) {
            hql.append(" and create_date between str_to_date('" + str_startDate
                + " 00:00:00','%Y-%m-%d %T') and str_to_date('" + str_endDate
                + " 23:59:59','%Y-%m-%d %T') ");

//			Calendar  startCalendar  =  Calendar.getInstance();
//			startCalendar.setTime(DateTimeUtils.validateDate(str_startDate));
//			Date endDate = DateTimeUtils.validateDate(str_endDate);
//			do {
//				statisInfoBean.getColumnList().add(startCalendar.getTime());
//				startCalendar.add(Calendar.DATE, 1);
//			} while (startCalendar.getTime().before(endDate) || startCalendar.getTime().equals(endDate));

        }
        IWorkingHoursService workService = (IWorkingHoursService) SpringUtil
            .getBean("workingHoursServiceImpl");
        statisInfoBean.getColumnList().addAll(
            workService.calculateWorkaday(str_startDate, str_endDate));
        if (userName != null) {
            StringBuffer userIDs = new StringBuffer(
                "select user_id from User where 1=1 ");
            String[] queryParamNames = userName.split(",");
            if (queryParamNames.length > 0) {
                userIDs.append(" and user_name in (");
                for (String name : queryParamNames) {
                    name = StringTools.replaceStringtoNull(name);
                    if (name != null && name.length() > 0) {
                        userIDs.append("'" + name + "',");
                    }
                }
                userIDs.deleteCharAt(userIDs.length() - 1);
                userIDs.append(" ) ");
            }
            List<String> ids = (List<String>) this.findEntityByHql(userIDs
                .toString());
            if (ids != null && ids.size() > 0) {
                hql.append(" and user in (");
                for (String id : ids) {
                    hql.append("'" + id + "',");
                }
                hql.deleteCharAt(hql.length() - 1);
                hql.append(" ) ");
            }
        }
//		hql.append(" order by user,create_date");
        hql.append(" order by user.user_dept,user,create_date");

        List<AttendanceRecords> dataList = (List<AttendanceRecords>) this
            .findEntityByHql(hql.toString());

        ///////////////////////
        List<Date> columnList = statisInfoBean.getColumnList();
        List<AttendanceRecordsInfoBean> valueList = statisInfoBean
            .getValueList();
        AttendanceRecordsInfoBean infoBean = new AttendanceRecordsInfoBean();
        for (int i = 0; i < dataList.size(); i++) {
            AttendanceRecords ar = dataList.get(i);
            infoBean = new AttendanceRecordsInfoBean();
            infoBean.setArList(new MyArrayList<AttendanceRecords>(
                statisInfoBean.getColumnList().size()));
            infoBean.setUser(ar.getUser());
            if (valueList.contains(infoBean)) {
                AttendanceRecordsInfoBean old = valueList.get(valueList
                    .indexOf(infoBean));
                Integer index = columnList.indexOf(ar.getCreate_date());
                if (index >= 0) {
                    old.getArList().set(index, ar);
                }
            } else {
                Integer index = columnList.indexOf(ar.getCreate_date());
                if (index >= 0) {
                    infoBean.getArList().set(index, ar);
                }
                valueList.add(infoBean);
            }
        }
//		for (int i = 0; i < dataList.size(); i++) {
//			AttendanceRecords ar = dataList.get(i);
//			System.out.println("user_id:"+ar.getUser().getUser_id());
//			if (infoBean.getArList().contains(ar)) {
//				Integer index = columnList.indexOf(DateTimeUtils.validateDate(DateTimeUtils.validateDate(ar.getCreate_date())));
//				if(index>=0){
//					infoBean.getArList().set(index, ar);
//				}
//			}else{
//				if (infoBean.getArList().size() != 0) {
//					valueList.add(infoBean);
//				}
//				infoBean = new AttendanceRecordsInfoBean();
//				infoBean.setArList(new MyArrayList<AttendanceRecords>(statisInfoBean.getColumnList().size()));
//				infoBean.setUser(ar.getUser());
//				Integer index = columnList.indexOf(DateTimeUtils.validateDate(DateTimeUtils.validateDate(ar.getCreate_date())));
//				if(index>=0)
//					infoBean.getArList().set(index, ar);
//			}
//			if (i == dataList.size() - 1) {
//				valueList.add(infoBean);
//			}
//		}
        statisInfoBean.setColumnList(columnList);
//		statisInfoBean.setValueList(valueList);
        ///////////////// 输出
//		System.out.print("列：");
//		for (Object date : columnList) {
//			System.out.println(date);
//		}
//		for (AttendanceRecordsInfoBean info : valueList) {
//			System.out.println("*********************");
//			System.out.println("用户："+info.getUser().getUser_name());
//			System.out.println("用户ID："+info.getUser().getUser_id());
//			for (AttendanceRecords w : info.getArList()) {
//				if(w!=null){
//					System.out.println("上:"+w.getOn_Date());
//					System.out.println("下:"+w.getOff_Date());
//				}else{
//					System.out.println("null");
//				}
//			}
//		}
        ////////////TODO 分页
        page.setTotalCount(valueList.size());
        int formIndex = 0;
        int toIndex = 0;
        formIndex = page.getPageNumber() - 1 < 0 ? 0
            : (page.getPageNumber() - 1) * page.getPageSize();
        formIndex = formIndex > valueList.size() ? 0 : formIndex;

        toIndex = page.getPageNumber() - 1 <= 0 ? page.getPageSize() : page
            .getPageNumber() * page.getPageSize();
        toIndex = toIndex > valueList.size() ? valueList.size() : toIndex;
        valueList = valueList.subList(formIndex, toIndex);
        ////

        statisInfoBean.setValueList(valueList);
        statisInfoBean.setPager(page);
        return statisInfoBean;
    }

    @SuppressWarnings("unchecked")
    public String mandatoryUpdate(String userName, String userPwd, String type,
            String date, String dateTime) {
//		IUserService userService = (IUserService) SpringUtil.getBean("userServiceImpl");
        Boolean isValid = LdapHelper.validationUser(userName, userPwd);
        if (!isValid) {
            return "用户名或密码错误!";
        }

        StringBuffer hql = new StringBuffer();
        hql.append("from User where alias_name='" + userName.trim() + "'");
//		hql.append("from User where alias_name='"+userName.trim()+"' and user_pwd='"+userPwd.trim()+"'");
        List<User> userList = (List<User>) super
            .findEntityByHql(hql.toString());
        User user = null;
        if (userList == null || userList.size() == 0) {
            return "用户不存在!";
        } else if (userList.size() > 1) {
            return "用户存在多个，无法执行操作!";
        } else if (userList.size() == 1) {
            user = userList.get(0);
        }

        date = StringTools.replaceStringtoNull(date);
        dateTime = StringTools.replaceStringtoNull(dateTime);
        type = StringTools.replaceStringtoNull(type);
        Date createDate = null;
        Date updateDate = null;
        if (date == null) {
            return "日期不能为空！";
        }
        if (dateTime == null) {
            return "修改的日期不能为空！";
        }
        try {
            createDate = DateTimeUtils.validateDate(date);
        } catch (Exception e) {
            return "日期格式，格式：2013-07-11";
        }
        try {
            updateDate = DateTimeUtils.validateDateTime(dateTime);
        } catch (Exception e) {
            return "日期格式，格式：2013-07-11 13:00:00";
        }

        if (type == null) {
            return "类型不能为空！";
        }

        List<AttendanceRecords> list = new ArrayList<AttendanceRecords>();
        AttendanceRecords ar = null;
        try {
            if (user != null) {
                list = (List<AttendanceRecords>) this.findRecords(user, date,
                    date, null).getList();
                if (list == null || list.size() == 0) {
                    ar = new AttendanceRecords(UUID.randomUUID().toString(),
                        createDate);
                    ar.setUser(user);
                } else if (list != null && list.size() > 1) {
                    return "查询到2条记录，无法修改！";
                } else if (list != null && list.size() == 1) {
                    ar = list.get(0);
                }

                if (ar != null) {
                    if (type.equals("ON")) {
                        ar.setOn_Date(updateDate);
                    } else if (type.equals("OFF")) {
                        ar.setOff_Date(updateDate);
                    }
                    return this.saveOrUpdate(ar);
                }
            }
        } catch (Exception e) {
            return "修改失败！";
        }
        return "修改失败！";
    }
}
