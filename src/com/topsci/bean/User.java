package com.topsci.bean;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @date 2013-6-24 下午04:24:42
 * @author FengQing
 * @description 用户
 */
@Entity
@Table(name = "USER")
public class User implements Serializable {

    /**
	 *
	 */
    private static final long serialVersionUID = 7483450810778182420L;

    @Id
    @Column(name = "USER_ID", nullable = false)
    private String user_id;

    @Column(name = "USER_NAME")
    private String user_name;

    @Column(name = "ALIAS_NAME", unique = true)
    private String alias_name;

    @Column(name = "USER_PWD")
    private String user_pwd;

    @Column(name = "SEX")
    private String sex;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PHONE_NUMBER")
    private String phone_number;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "BIRTHDAY")
    private Date birthday;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "ENTRANT_TIME")
    private Date entrant_time;

    @Column(name = "IS_DIMISSION")
    private String is_dimission;

    @Column(name = "CREATE_DATE")
    private Date create_date;

    @Column(name = "USER_TYPE")
    private String user_type;

    @Column(name = "USER_DEPT")
    private String user_dept;

    // @OneToMany(fetch = FetchType.EAGER)
    // @JoinTable(name = "B_USER_FUNCTION", joinColumns = @JoinColumn(name =
    // "USER_ID"), inverseJoinColumns = { @JoinColumn(name = "FUNCTION_ID") })
    // private List<Function> listFunction = new ArrayList<Function>(0);

    // @OneToMany(fetch = FetchType.EAGER)
    // @JoinTable(name = "B_USER_PROJECT", joinColumns = @JoinColumn(name =
    // "USER_ID"), inverseJoinColumns = { @JoinColumn(name = "PROJECT_CODE") })
    // private List<Project> projectList = new ArrayList<Project>(0);

    @Override
    public String toString() {
        return "User:[user_id=" + this.user_id + ",user_name=" + this.user_name
            + "]";
    }

    public String getUser_name() {
        return this.user_name;
    }

    public String getUser_pwd() {
        return this.user_pwd;
    }

    public String getSex() {
        return this.sex;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPhone_number() {
        return this.phone_number;
    }

    public String getAddress() {
        return this.address;
    }

    public Date getBirthday() {
        return this.birthday;
    }

    public String getRemark() {
        return this.remark;
    }

    public Date getEntrant_time() {
        return this.entrant_time;
    }

    public String getIs_dimission() {
        return this.is_dimission;
    }

    public String getAlias_name() {
        return this.alias_name;
    }

    public String getUser_id() {
        return this.user_id;
    }

    public Date getCreate_date() {
        return this.create_date;
    }

    public String getUser_type() {
        return this.user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setAlias_name(String aliasName) {
        this.alias_name = aliasName;
    }

    public void setIs_dimission(String isDimission) {
        this.is_dimission = isDimission;
    }

    public void setEntrant_time(Date entrantTime) {
        this.entrant_time = entrantTime;
    }

    public void setUser_name(String userName) {
        this.user_name = userName;
    }

    public void setUser_pwd(String userPwd) {
        this.user_pwd = userPwd;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone_number(String phoneNumber) {
        this.phone_number = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUser_dept() {
        return this.user_dept;
    }

    public void setUser_dept(String user_dept) {
        this.user_dept = user_dept;
    }

    public User() {
        super();
    }

    public User(String user_id) {
        super();
        this.user_id = user_id;
    }

    public User(String alias_name, String user_pwd) {
        super();
        this.alias_name = alias_name;
        this.user_pwd = user_pwd;
    }

    @Override
    public boolean equals(Object obj) {
        return this.user_id.equals(((User) obj).getUser_id());
    }
}
