package com.xxxx.crm.base;

/**
 * 用来返回结果对象,默认值为成功
 */
public class ResultInfo {
    //状态码
    private Integer code=200;
    //返回的提示信息
    private String msg="success";
    //返回的对象。
    private Object result;

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
