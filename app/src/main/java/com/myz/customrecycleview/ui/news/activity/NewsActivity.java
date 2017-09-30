package com.myz.customrecycleview.ui.news.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.myz.customrecycleview.R;
import com.myz.customrecycleview.adapter.MyRecycleViewAdapter;
import com.myz.customrecycleview.api.Api;
import com.myz.customrecycleview.api.ApiConstants;
import com.myz.customrecycleview.api.HostType;
import com.myz.customrecycleview.bean.NewsSummary;
import com.myz.customrecycleview.ui.news.adapter.NewListAdapter;
import com.myz.customrecycleview.utils.TimeUtil;
import com.myz.mrecyclerview.animation.ScaleInAnimation;
import com.myz.mrecyclerview.universaladapter.recyclerview.CommonRecycleViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.myz.customrecycleview.api.Api.getDefault;

public class NewsActivity extends AppCompatActivity {
    private CommonRecycleViewAdapter<String> adapter;
    private MyRecycleViewAdapter myRecycleViewAdapter;
    private List<String> list;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;

    private List<NewsSummary> datas = new ArrayList<>();
    private NewListAdapter newListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        initViews();
        initData();
    }

    private void initViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add("text" + (i + 1));
        }
        myRecycleViewAdapter = new MyRecycleViewAdapter(list, NewsActivity.this);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //mRecyclerView.setAdapter(myRecycleViewAdapter);

        newListAdapter = new NewListAdapter(NewsActivity.this, datas);
        newListAdapter.openLoadAnimation(new ScaleInAnimation());
        mRecyclerView.setAdapter(newListAdapter);
    }

    private void initData() {
        getNewsListData("headline", "T1348647909107",0);
    }

    /**
     * 获取新闻列表
     * @param type
     * @param id
     * @param startPage
     * @return
     */
    private  void getNewsListData(final String type, final String id, final int startPage) {
         getDefault(HostType.NETEASE_NEWS_VIDEO)
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
                 .doOnSubscribe(disposable -> {
                     //正在加载Dialog
                 })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    //隐藏进度Dialog
                })
                .subscribe(newsSummaries -> {
                    datas.addAll(newsSummaries);
                    newListAdapter.notifyDataSetChanged();
                });
    }
}
