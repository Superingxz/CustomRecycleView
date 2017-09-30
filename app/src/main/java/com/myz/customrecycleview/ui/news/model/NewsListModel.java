package com.myz.customrecycleview.ui.news.model;


import com.myz.customrecycleview.api.Api;
import com.myz.customrecycleview.api.ApiConstants;
import com.myz.customrecycleview.api.HostType;
import com.myz.customrecycleview.bean.NewsSummary;
import com.myz.customrecycleview.utils.TimeUtil;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * des:新闻列表model
 * Created by xsf
 * on 2016.09.14:54
 */
public class NewsListModel {
    /**
     * 获取新闻列表
     * @param type
     * @param id
     * @param startPage
     * @return
     */
    public static Single<List<NewsSummary>> getNewsListData(final String type, final String id, final int startPage) {
       return Api.getDefault(HostType.NETEASE_NEWS_VIDEO)
                .getNewsList(Api.getCacheControl(),type, id, startPage)
                .flatMap(new Function<Map<String,List<NewsSummary>>,  Observable<NewsSummary>>() {
                    @Override
                    public Observable<NewsSummary> apply(@NonNull Map<String, List<NewsSummary>> map) throws Exception {
                        if (id.endsWith(ApiConstants.HOUSE_ID)) {
                            // 房产实际上针对地区的它的id与返回key不同
                            return Observable.fromIterable(map.get("北京"));
                        }
                        return Observable.fromIterable(map.get(id));
                    }
                })
                //转化时间
                .map(newsSummary -> {
                    String ptime = TimeUtil.formatDate(newsSummary.getPtime());
                    newsSummary.setPtime(ptime);
                    return newsSummary;
                })
                .distinct()//去重
                .toSortedList((newsSummary, newsSummary2) -> newsSummary2.getPtime().compareTo(newsSummary.getPtime()))
                //声明线程调度
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
