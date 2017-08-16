package com.topsci.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.topsci.bean.Project;
import com.topsci.dao.impl.ProjectDaoImpl;
import com.topsci.service.IProjectService;
import com.topsci.utils.Pager;
import com.topsci.utils.StringTools;

@Service
@Transactional
public class ProjectServiceImpl extends BaseServiceImpl<Project, String>
        implements IProjectService {

    @Autowired
    public void setBaseDao(ProjectDaoImpl baseDao) {
        super.setBaseDao(baseDao);
    }

    public String saveOrUpdate(Project entity, String processType)
            throws Exception {
        String processStatus = "failed";
        if (!processType.equals("delete")) {
            if (StringTools.replaceStringtoNull(entity.getProject_code()) != null) {
                Project dataProject = super.get("project_code",
                    entity.getProject_code());
                if (dataProject != null) {
                    dataProject.setProject_name(entity.getProject_name());
                    dataProject.setDescription(entity.getDescription());
                    dataProject.setProject_type(entity.getProject_type());
                } else {
                    dataProject = entity;
                    dataProject.setCreate_date(new Date());
                }
                processStatus = super.saveOrUpdate(dataProject);
            }
        } else if (processType.equals("delete")) {
            processStatus = super.delete(entity.getProject_code()) == true ? "success"
                : processStatus;
        }
        return processStatus;
    }

    public Pager findRecordsByNameOrCodeAndParamProject(Project paramProject,
            String projectName, String projectCode, Pager page) {
        StringBuffer whereHql = new StringBuffer();
        if (projectName != null) {
//			System.out.println(projectName);
            String[] queryParamNames = projectName.split(",");
            if (queryParamNames.length > 0) {
                whereHql.append(" and project_name in (");
                for (String name : queryParamNames) {
                    name = StringTools.replaceStringtoNull(name);
                    if (name != null && name.length() > 0) {
                        whereHql.append("'" + name + "',");
                    }
                }
                whereHql.deleteCharAt(whereHql.length() - 1);
                whereHql.append(" ) ");
            }
        }

        if (projectCode != null) {
//			System.out.println(projectCode);
            String[] queryParamCodes = projectCode.split(",");
            if (queryParamCodes.length > 0) {
                whereHql.append(" and project_code in (");
                for (String code : queryParamCodes) {
                    code = StringTools.replaceStringtoNull(code);
                    if (code != null && code.length() > 0) {
                        whereHql.append("'" + code + "',");
                    }
                }
                whereHql.deleteCharAt(whereHql.length() - 1);
                whereHql.append(" ) ");
            }
        }
        whereHql.append(" order by create_date desc ");
        return super.findEntityByEntityParamsByPager_equals(paramProject,
            whereHql.toString(), page);
    }

    public String getAllProjectNameOrProjectCodeByType_RETURN_ARRAY_JS(
            String type_NameOrCode, String param) {
        StringBuffer arrayJS = new StringBuffer();
        Boolean isCode = type_NameOrCode.equals("code");
        List<Project> dataList = (List<Project>) super
            .findEntityByHql("from Project where "
                + (isCode ? "project_code" : "project_name") + " like '%"
                + (param == null ? "" : param) + "%'");

        if (dataList != null && dataList.size() != 0) {
            for (Project project : dataList) {
                arrayJS.append(isCode ? project.getProject_code() : project
                    .getProject_name());
                arrayJS.append(",");
            }
            if (arrayJS.length() > 1) {
                arrayJS.deleteCharAt(arrayJS.length() - 1);
            }
        }
        return arrayJS.toString();
    }

    public String findProjectByName_JS(String projectName) {
        projectName = StringTools.replaceStringtoNull(projectName);
        StringBuffer arrayJS = new StringBuffer();
        StringBuffer hql = new StringBuffer("from Project where 1=1 ");
        if (projectName != null) {
            hql.append(" and project_name like '%" + projectName.trim() + "%'");
        }
        hql.append(" order by project_type ");
        List<Project> dataList = (List<Project>) super.findEntityByHql(hql
            .toString());

        if (dataList != null && dataList.size() != 0) {
            for (Project project : dataList) {
                arrayJS.append(project.getProject_name() + "="
                    + project.getProject_code() + "="
                    + project.getProject_type());
                arrayJS.append(",");
            }
            if (arrayJS.length() > 1) {
                arrayJS.deleteCharAt(arrayJS.length() - 1);
            }
        }

//		System.out.println(arrayJS);
        return arrayJS.toString();
    }
}
