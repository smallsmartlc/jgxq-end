package com.jgxq.core.resp;

import java.util.List;

/**
 * @author LuCong
 * @since 2020-12-06
 **/
public class PageResponse<T> {
    private List<T> data;
    private int page;
    private int pageSize;
    private long total;

    public PageResponse() {
    }

    public PageResponse(List<T> data, int page, int pageSize, long total) {
        this.data = data;
        this.page = page;
        this.pageSize = pageSize;
        this.total = total;
    }

    public List<T> getData() {
        return this.data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public long getTotal() {
        return this.total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}