package com.chinkee.tmall.util;

// 分页
public class Page {
    private int start;
    private int count;
    private int total;
    private String param;

    private static final int defaultCount = 5; // 每页默认5条数据

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public Page(){
        count = defaultCount;
        // System.out.println("无参方法：" + count);
    }

    public Page(int start, int count){
        this(); // 会调用上面那个无参方法。
        this.start = start;
        this.count = count;
        // System.out.println("有参方法：" + start + "  " + count);
    }

    /* public static void main(String args[]){
        Page p = new Page(2,10);
    } */

    public boolean isHasPreviouse(){ // 判断是否有前一页
        if(start == 0)
            return false;
        return true;
    }
    public boolean isHasNext() { // 判断是否有后一页
        if (start == getLast())
            return false;
        return true;
    }

    // 页数
    public int getTotalPage(){
        int totalPage;
        // 假设总数是50，是能够被5整除的，那么就有10页
        if(0 == total%count){
            totalPage = total / count;
        }else { // 假设总数是51，不能够被5整除的，那么就有11页
            totalPage = total / count + 1;
        }

        if (0 == totalPage)
            totalPage = 1;
        return totalPage;
    }

    // 最后一页
    public int getLast(){
        int last;
        // 假设总数是50，是能够被5整除的，那么最后一页的开始就是45
        if(0 == total%count)
            last = total - count;
            // 假设总数是51，不能够被5整除的，那么最后一页的开始就是50
        else
            last = total - total%count;
        last = last<0 ? 0 : last;
        return last;
    }

    public String toString(){
        return "Page [start=" + start + ", count=" + count + ", total=" + total + ", getStart()=" + getStart()
                + ", getCount()=" + getCount() + ", isHasPreviouse()=" + isHasPreviouse() + ", isHasNext()="
                + isHasNext() + ", getTotalPage()=" + getTotalPage() + ", getLast()=" + getLast() + "]";
    }
}
