package com.xxxx.crm.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
//客户开发计划
public class CusDevPlan {
    //开发计划对应id主键
    private Integer id;//营销计划id

    //营销机会id
    private Integer saleChanceId;//营销机会id

    private String planItem;//计划项

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")//如果传递的参数是Date类型，要求传入的时间字符串的格式
    private Date planDate;//计划时间

    private String exeAffect;//执行效果

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateDate;

    private Integer isValid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSaleChanceId() {
        return saleChanceId;
    }

    public void setSaleChanceId(Integer saleChanceId) {
        this.saleChanceId = saleChanceId;
    }

    public String getPlanItem() {
        return planItem;
    }

    public void setPlanItem(String planItem) {
        this.planItem = planItem == null ? null : planItem.trim();
    }

    public Date getPlanDate() {
        return planDate;
    }

    public void setPlanDate(Date planDate) {
        this.planDate = planDate;
    }

    public String getExeAffect() {
        return exeAffect;
    }

    public void setExeAffect(String exeAffect) {
        this.exeAffect = exeAffect == null ? null : exeAffect.trim();
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

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }
}