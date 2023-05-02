package com.xxxx.crm.base;

/**
 * 分页的相关类
 */
public class BaseQuery {
    //当前页
    private Integer page=1;
    //一页显示的数量
    private Integer limit=10;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
