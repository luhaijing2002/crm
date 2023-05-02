package com.xxxx.crm.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Module {
    private Integer id;//资源id

    private String moduleName;//资源名称

    private String moduleStyle;//资源样式

    private String url;//资源对应的访问地址

    private Integer parentId;//这个资源有没有父id，父资源（-1:表示没有，其他对应父id）

    private String parentOptValue;//

    private Integer grade;//表示层级关系 ，顺序，（0:目录，1：表示菜单，2:按钮

    private String optValue;//授权码

    private Integer orders;

    private Byte isValid;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName == null ? null : moduleName.trim();
    }

    public String getModuleStyle() {
        return moduleStyle;
    }

    public void setModuleStyle(String moduleStyle) {
        this.moduleStyle = moduleStyle == null ? null : moduleStyle.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getParentOptValue() {
        return parentOptValue;
    }

    public void setParentOptValue(String parentOptValue) {
        this.parentOptValue = parentOptValue == null ? null : parentOptValue.trim();
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getOptValue() {
        return optValue;
    }

    public void setOptValue(String optValue) {
        this.optValue = optValue == null ? null : optValue.trim();
    }

    public Integer getOrders() {
        return orders;
    }

    public void setOrders(Integer orders) {
        this.orders = orders;
    }

    public Byte getIsValid() {
        return isValid;
    }

    public void setIsValid(Byte isValid) {
        this.isValid = isValid;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}