package com.topsci.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.topsci.bean.User;
import com.topsci.dao.impl.UserDaoImpl;
import com.topsci.service.IUserService;
import com.topsci.utils.Pager;
import com.topsci.utils.StringTools;

@Transactional
@Service
public class UserServiceImpl extends BaseServiceImpl<User, String> implements
        IUserService {

    @Autowired
    public void setBaseDao(UserDaoImpl baseDao) {
        super.setBaseDao(baseDao);
    }

    public User login(User enetity) {
        enetity.setUser_pwd(null);
        List<User> userList = super.findEntityByEntityParams_equals(enetity,
            null);
        User dataUser = null;
        if (userList.size() == 1) {
            dataUser = userList.get(0);
        }

//		if (chekLogin(dataUser, enetity))
        return dataUser;
//		return null;
    }

    public String saveOrUpdate(String processType, User user) throws Exception {
        String processStatus = "error,";
        if (!processType.equals("delete")) {
            if (processType.equals("add")) {
                user.setUser_id(UUID.randomUUID().toString());
                user.setCreate_date(new Date());
            }
//			List<User>  userList = super.findEntityByEntityParams_equals(new User(user.getAlias_name(), user.getUser_pwd()), null);
//			Project dataProject = super.get("project_code", entity.getProject_code());
//			if(dataProject!=null){
//				dataProject.setProject_name(entity.getProject_name());
//				dataProject.setDescription(entity.getDescription());
//				dataProject.setProject_type(entity.getProject_type());
//			}else{
//				dataProject = entity;
//				dataProject.setCreate_date(new Date());
//			}
            processStatus = super.saveOrUpdate(user);
        } else if (processType.equals("delete")) {
            processStatus = super.delete(user.getUser_id()) == true ? "success,删除成功！"
                : processStatus;
        }
        return processStatus;
    }

    private boolean chekLogin(User dataUser, User user) {
        if (dataUser == null) {
            return false;
        }
        if (user == null) {
            return false;
        }

        user.setAlias_name(StringTools.replaceStringtoNull(user.getAlias_name()));
        dataUser.setAlias_name(StringTools.replaceStringtoNull(dataUser
            .getAlias_name()));
        user.setUser_pwd(StringTools.replaceStringtoNull(user.getUser_pwd()));
        dataUser.setUser_pwd(StringTools.replaceStringtoNull(dataUser
            .getUser_pwd()));

        if (user.getAlias_name() == null) {
            return false;
        }

        if (dataUser.getAlias_name().equals(user.getAlias_name())
            && (dataUser.getUser_pwd() == null && user.getUser_pwd() == null || dataUser
                .getUser_pwd().equals(user.getUser_pwd()))) {
            return true;
        }
        return false;
    }

    public String findUserByName_JS(String userName) {
        userName = StringTools.replaceStringtoNull(userName);
        StringBuffer arrayJS = new StringBuffer();
        StringBuffer hql = new StringBuffer("from User where 1=1 ");
        if (userName != null) {
            hql.append(" and user_name like '%" + userName.trim() + "%'");
        }
        hql.append(" order by user_name ");
        List<User> dataList = (List<User>) super
            .findEntityByHql(hql.toString());

        if (dataList != null && dataList.size() != 0) {
            for (User user : dataList) {
                arrayJS.append(user.getUser_name());
                arrayJS.append(",");
            }
            if (arrayJS.length() > 1) {
                arrayJS.deleteCharAt(arrayJS.length() - 1);
            }
        }
//		System.out.println(arrayJS);
        return arrayJS.toString();
    }

    public Pager queryUserByName(String userName, Pager pager) {
        StringBuffer hql = new StringBuffer("from User where 1=1 ");
        if (userName != null) {
//			System.out.println(userName);
            String[] queryParamNames = userName.split(",");
            if (queryParamNames.length > 0) {
                hql.append(" and user_name in (");
                for (String name : queryParamNames) {
                    name = StringTools.replaceStringtoNull(name);
                    if (name != null && name.length() > 0) {
                        hql.append("'" + name + "',");
                    }
                }
                hql.deleteCharAt(hql.length() - 1);
                hql.append(" ) ");
            }
        }
        hql.append(" order by create_date desc ");
        return super.queryEntityByHql_Page(hql.toString(), pager);
    }
}
