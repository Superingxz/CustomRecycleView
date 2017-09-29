package com.myz.mrecyclerview.universaladapter.recyclerview;

/**
 * 介绍：分类悬停的接口
 * 作者：moligy
 * 邮箱：moligy@126.com
 * 时间： 2017.9.29.
 */

public interface ISuspensionInterface {
    //是否需要分组
    boolean isShowParent();

    //是否需要显示悬停title
    boolean isShowSuspension();

    //悬停的title
    String getSuspensionTag();

}
