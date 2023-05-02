package com.xxxx.crm.model;

/**
 * @author 鲁海晶
 * @version 1.0
 */
public class TreeModel {

    private Integer id;
    private Integer pId;
    private String name;

    private boolean checked = false; //复选框选中状态，false不选中，true选中。

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
