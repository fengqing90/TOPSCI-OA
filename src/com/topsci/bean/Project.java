package com.topsci.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @date 2013-6-24 下午06:05:08
 * @author FengQing
 * @description 项目
 */
@Entity
@Table(name = "PROJECT")
public class Project implements Serializable {

    /**
	 *
	 */
    private static final long serialVersionUID = 7369726706699665580L;

    @Id
    @Column(name = "PROJECT_CODE")
    private String project_code;

    @Column(name = "PROJECT_NAME")
    private String project_name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PROJECT_TYPE")
    private String project_type;

    @Column(name = "CREATE_DATE")
    private Date create_date;

    @Override
    public String toString() {
        return "Project[project_code=" + this.project_code + ",project_name="
            + this.project_name + ",description=" + this.description
            + ",project_type=" + this.project_type + "]";
    }

    @Override
    public boolean equals(Object obj) {
        return this.getProject_code().equals(((Project) obj).getProject_code());
    }

    public String getProject_code() {
        return this.project_code;
    }

    public String getProject_name() {
        return this.project_name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getProject_type() {
        return this.project_type;
    }

    public void setProject_type(String project_type) {
        this.project_type = project_type;
    }

    public Date getCreate_date() {
        return this.create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public void setProject_code(String projectCode) {
        this.project_code = projectCode;
    }

    public void setProject_name(String projectName) {
        this.project_name = projectName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Project(String project_code) {
        super();
        this.project_code = project_code;
    }

    public Project() {
        super();
    }

}
