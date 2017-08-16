package com.topsci.service;

import com.topsci.bean.Project;
import com.topsci.utils.Pager;

public interface IProjectService extends BaseService<Project, String> {

	/**
	 * 查询
	 * @param paramProject
	 * @param projectName
	 * @param projectCode
	 * @param page
	 * @return
	 */
	public Pager findRecordsByNameOrCodeAndParamProject(Project paramProject,String projectName, String projectCode, Pager page);

	/**
	 * 保存、删除、更新
	 * @param entity
	 * @param processType
	 * @return
	 * @throws Exception
	 */
	public String saveOrUpdate(Project entity, String processType) throws Exception;
	
	/**
	 * 获取查询条件
	 * @param type_NameOrCode
	 * @param param
	 * @return
	 */
	public String getAllProjectNameOrProjectCodeByType_RETURN_ARRAY_JS(String type_NameOrCode,String param);
	
	
	public String findProjectByName_JS(String projectName);
	
}
