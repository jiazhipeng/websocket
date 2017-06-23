package com.qczl.websocket.util;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Redis内存分页
 */
public class RedisPager<T> {

    /**
     * 当前页数
     */
    private Integer pageNo = 1;

    /**
     * 每页显示条数
     */
    private Integer pageSize = 20;

    /**
     * 分页后集合
     */
    private List<T> pagedList;

    /**
     * 总记录数
     */
    private Integer totalCount = 0;

    private RedisPager(List<T> data, Integer pageNo, Integer pageSize) {
        if (data == null || data.isEmpty()) {
            data = new ArrayList<T>();
        }

        if (pageNo != null) {
            this.pageNo = pageNo;
        }

        if (pageSize != null) {
            this.pageSize = pageSize;
        }

        this.totalCount = data.size();

        this.getPagedList(data);

    }

    /**
     * 创建分页器
     *
     * @param data     需要分页的数据
     * @param pageNo   当前页数
     * @param pageSize 每页显示条数
     * @param <T>      业务对象
     * @return 分页器
     */
    public static <T> RedisPager<T> create(List<T> data, Integer pageNo, Integer pageSize) {
        return new RedisPager<>(data, pageNo, pageSize);
    }

    /**
     * 得到分页后的数据
     *
     * @return 分页后结果
     */
    private void getPagedList(List<T> data) {
        if (pageNo < 1) {
            pageNo = 1;
        }
        int fromIndex = (pageNo - 1) * pageSize;
        if (fromIndex >= data.size()) {
            this.pagedList = Collections.emptyList();
            return;
        }

        int toIndex = pageNo * pageSize;
        if (toIndex >= data.size()) {
            toIndex = data.size();
        }
        this.pagedList = data.subList(fromIndex, toIndex);
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        if (pageNo < 1) {
            this.pageNo = 1;
        } else {
            this.pageNo = pageNo;
        }
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getPagedList() {
        return pagedList;
    }

    public void setPagedList(List<T> pagedList) {
        this.pagedList = pagedList;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public static void main(String[] args) {
        Integer[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        List<Integer> list = Arrays.asList(array);

        RedisPager<Integer> pager = RedisPager.create(list, 1, 10);
        System.out.println(JSON.toJSONString(pager));

    }
}
