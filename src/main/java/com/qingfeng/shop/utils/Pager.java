package com.qingfeng.shop.utils;

// 分页实体
public class Pager {
    // 1.当前页码
    private int pageIndex;
    // 2.一页需要展示多少条数据
    private int pageSize = 3 ;
    // 3.当前条件下总的数据量
    private int totalCount ;
    // 4.总共可以分多少页
    private int totalPages ;

    public int getPageIndex() {
        // 取页码的时候，做一些判断
        //如果小于第一页，我们直接给第一页
        pageIndex = pageIndex <= 0 ? 1 : pageIndex ;
        // 判断页码是否越界 了
        //大于总页数，就给最后一页
        pageIndex = pageIndex>= getTotalPages() ? getTotalPages(): pageIndex;
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPages() {
        // 总页数
        // 根据总数据量和每页最多展示多少来确定的
        // 10条数据 3 -> 4页
        // 9条数据  3  -> 3页

        //拿当前总数据量除以每页展示多少条
        //总数减1，会把特殊情况排除，有点增量减掉，再进行除，最后加1，这样逻辑就对了
        return (this.getTotalCount() -1 )  / this.getPageSize() + 1;
    }

    // 分页的第一个参数
    public int getFirstParam(){
        return (this.getPageIndex()  -1)*this.getPageSize();
    }


}
