package com.topsci.bean;

import javax.persistence.Column;
import javax.persistence.Id;

// @Entity
// @Table(name = "FUNCTION")
public class Function {
    /** 功能标识 **/
    private String func_id;
    /** 功能名称 **/
    private String func_name;
    /** 功能常量(作为该功能的系统唯一标识) **/
    private String func_constant;
    /** 功能相对应的URL **/
    private String func_url;
    /** URL打开方式 **/
    private String func_target;
    /** 功能描述 **/
    private String func_desc;

    // /** 所属子系统标识 **/
    // private SystemInfo sys;

    @Override
    public String toString() {
        return "[Function:func_id=" + this.func_id + ",func_name="
            + this.func_name + ",func_constant=" + this.func_constant + "]";
    }

    @Id
    @Column(name = "FUNC_ID", length = 10)
    public String getFunc_id() {
        return this.func_id;
    }

    @Column(name = "FUNC_NAME", length = 100)
    public String getFunc_name() {
        return this.func_name;
    }

    @Column(name = "FUNC_CONSTANT", length = 50)
    public String getFunc_constant() {
        return this.func_constant;
    }

    @Column(name = "FUNC_URL", length = 50)
    public String getFunc_url() {
        return this.func_url;
    }

    @Column(name = "FUNC_TARGET", length = 10)
    public String getFunc_target() {
        return this.func_target;
    }

    @Column(name = "FUNC_DESC", length = 50)
    public String getFunc_desc() {
        return this.func_desc;
    }

    public void setFunc_id(String funcId) {
        this.func_id = funcId;
    }

    public void setFunc_name(String funcName) {
        this.func_name = funcName;
    }

    public void setFunc_constant(String funcConstant) {
        this.func_constant = funcConstant;
    }

    public void setFunc_url(String funcUrl) {
        this.func_url = funcUrl;
    }

    public void setFunc_target(String funcTarget) {
        this.func_target = funcTarget;
    }

    public void setFunc_desc(String funcDesc) {
        this.func_desc = funcDesc;
    }

    public Function(String funcName, String funcDesc) {
        super();
        this.func_name = funcName;
        this.func_desc = funcDesc;
    }

    public Function() {
        super();
    }
}
