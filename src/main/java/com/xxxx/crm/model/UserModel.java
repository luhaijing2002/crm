package com.xxxx.crm.model;

/**
 * 专门用来返回用户数据。
 * @author 鲁海晶
 * @version 1.0
 */

/**
 * 用户id,
 * 用户姓名
 * 真实姓名
 */
public class UserModel {

//    private Integer userId;
    private String userIdStr;//加密后的用户id

    public String getUserIdStr() {
        return userIdStr;
    }

    public void setUserIdStr(String userIdStr) {
        this.userIdStr = userIdStr;
    }

    private String userName;
    private String trueName;

//    public Integer getUserId() {
//        return userId;
//    }
//
//    public void setUserId(Integer userId) {
//        this.userId = userId;
//    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }
}
