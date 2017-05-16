package com.careyun.voiceassistant.map.Search;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

import org.json.JSONException;

import java.util.List;

/**
 * Created by Huangmq on 2017/3/30.
 */

public class SuggestSearch implements OnGetSuggestionResultListener {

    private SuggestionSearch mSuggestionSearch = null;
    public static Context mContext;
    private SuggestSearchCallBack mSuggCallBack;

    private static class SingletonHolder {
        private static SuggestSearch instance = new SuggestSearch();
    }

    public static SuggestSearch getInstance() {

        return SingletonHolder.instance;
    }

    public SuggestSearch() {
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
    }

    public void setSuggestCallback(SuggestSearchCallBack mSuggCallBack) {
        this.mSuggCallBack = mSuggCallBack;
    }

    public void StartSearch(String arr, String Myloca) {

        if (!TextUtils.isEmpty(arr)) {
            /**
             * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
             */
            mSuggestionSearch
                    .requestSuggestion((new SuggestionSearchOption())
                            .keyword(arr).city(Myloca));
        }
    }


    @Override
    public void onGetSuggestionResult(SuggestionResult res) {
        List<SuggestionResult.SuggestionInfo> aa = res.getAllSuggestions();
        SuggestionResult bb = res;
        if (res == null || res.getAllSuggestions() == null) {
            if (mSuggCallBack != null)
                mSuggCallBack.NotFunded();
            return;
        }
        try {
            if (mSuggCallBack != null)
            mSuggCallBack.Funded();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
