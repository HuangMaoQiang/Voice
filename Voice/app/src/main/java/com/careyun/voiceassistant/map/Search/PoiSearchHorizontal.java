package com.careyun.voiceassistant.map.Search;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.careyun.voiceassistant.R;
import com.careyun.voiceassistant.app.VoiceApplication;
import com.careyun.voiceassistant.map.BaiduAppNavi.BaiduNavi;
import com.careyun.voiceassistant.map.Search.entity.MyPoiResult;
import com.careyun.voiceassistant.map.Search.entity.PoiAdpter;
import com.careyun.voiceassistant.util.Constant;
import com.careyun.voiceassistant.view.activity.ActivityCollerctor;
import com.careyun.voiceassistant.view.activity.BaseActivity;
import com.careyun.voiceassistant.voice.tts.presenter.SynthesizerPresenterImpl;

import java.util.ArrayList;
import java.util.List;


public class PoiSearchHorizontal extends BaseActivity implements OnGetSuggestionResultListener
        , MyGeoCode.GeoCallBack {

    private SuggestionSearch mSuggestionSearch = null;
    private BaiduMap mBaiduMap = null;
    private List<String> suggest;
    //    private String CITY = "成都";
    private String Arrival;
    private SharedPreferences LocationInfo;
    public SharedPreferences.Editor mEditor;

    private AutoCompleteTextView mTest = null;
    private ListView mlist;
    private PoiAdpter adpter;
    private TextView navi_res;
    private List<MyPoiResult> poiResultsList = new ArrayList<>();
    private int mPoiCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi_search_horizontal);
        initView();
        Intent mIntent = getIntent();
        String arr = mIntent.getStringExtra("ARRIVAL");
        String res = mIntent.getStringExtra("RESULT");
        String city = mIntent.getStringExtra("LOCATION");

        navi_res.setText(res);
        if (arr != "") {
            /**
             * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
             */
            mSuggestionSearch
                    .requestSuggestion((new SuggestionSearchOption())
                            .keyword(arr).city(city));
//            TestService.getInstance().speak("找到如下地址，请说出第几个", Constant.CONTINUE_LISTEN);
        }
    }


    private void initView() {

        mTest = (AutoCompleteTextView) findViewById(R.id.test);
        navi_res = (TextView) findViewById(R.id.navi_restult);
        mlist = (ListView) findViewById(R.id.poi_result);
        adpter = new PoiAdpter(PoiSearchHorizontal.this, R.layout.poiresult_item, poiResultsList);
        mlist.setAdapter(adpter);
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
        // 初始化搜索模块，注册事件监听
//        mSearch = GeoCoder.newInstance();
//        mSearch.setOnGetGeoCodeResultListener(this);
        LocationInfo = getSharedPreferences(Constant.EXTRA_LOCATION_INFO, MODE_PRIVATE);
        mEditor = LocationInfo.edit();
        VoiceApplication.addHandler(mHandler);

    }

    @Override
    public void onGeoCodeResult(MyPoiResult result) {
//       int index =  Integer.valueOf(result.getmNum());
        if (result.getmDistance() != "") {
            poiResultsList.add(result);
        } else {
            mPoiCount = mPoiCount - 1;
        }
        if (poiResultsList.size() == mPoiCount && poiResultsList.size() > 0) {
    /*        int len = poiResultsList.size();
            for(int i = 0;i<len;++i){
            }*/
            mHandler.sendEmptyMessage(MSG_HANDLE_POI_DISTANCE_RESULT);
            SynthesizerPresenterImpl.getInstance().startSpeak("找到如下地址，请说出第几个", Constant.NAVI_SCENE);
        }
    }

    @Override
    public void onGetSuggestionResult(SuggestionResult res) {
        poiResultsList.clear();
        if (res == null || res.getAllSuggestions() == null) {
//            TestService.getInstance().speak("没找到该地址，换个地名试试", Constant.CONTINUE_LISTEN);
            ActivityCollerctor.finishAll();
            return;
        }
//        int i = 0;
//        mPoiCount = res.getAllSuggestions().size();
        for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
//            Log.e("地址地址地址","|1"+info.city+"1|2"+info.district+"2|3"+info.key+"3|");
//            !TextUtils.isEmpty(info.key) && !TextUtils.isEmpty(info.city) &&!TextUtils.isEmpty(info.district)
            if (info.key != null) {
//                Log.e("地址地址地址",info.city+info.district+info.key);
                mPoiCount = mPoiCount + 1;
                MyGeoCode mMyGeoCode = new MyGeoCode(this);
                mMyGeoCode.setGeoCallBakc(this);
                mMyGeoCode.poiGeoCode(info.city, info.key, info.district);
            }
        }
        Log.e("地址地址地址", String.valueOf(mPoiCount));
    }


    private static final int MSG_HANDLE_POI_DISTANCE_RESULT = 1;
    private static final int MSG_SELECT = 0;
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SELECT: {
                    String num = (String) msg.obj;
                    if (num != null) {
                        MyPoiResult myPoiResult = poiResultsList.get((Integer.valueOf(num).intValue()) - 1);
                        Arrival = myPoiResult.getmResult();
                        finish();
                        SynthesizerPresenterImpl.getInstance().startSpeak("已选择",Constant.STOP_LISTEN);
                        Intent i1 = new Intent();
                        i1.setData(Uri.parse("baidumap://map/navi?query=" + Arrival));
                        Log.e("导航要的地方", "目的地：" + Arrival);
                        startActivity(i1);
                    }
                    break;
                }
                case MSG_HANDLE_POI_DISTANCE_RESULT: {
                    adpter = new PoiAdpter(PoiSearchHorizontal.this, R.layout.poiresult_item, poiResultsList);
                    mlist.setAdapter(adpter);
                    adpter.notifyDataSetChanged();
                    mlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            MyPoiResult myPoiResult = poiResultsList.get(position);
                            Arrival = myPoiResult.getmResult();
                            Intent i1 = new Intent();
                            i1.setData(Uri.parse("baidumap://map/navi?query=" + Arrival));
                            startActivity(i1);
                        }
                    });
                    break;
                }
            }
        }
    };


    @Override
    protected void onDestroy() {
        VoiceApplication.removeHandler(mHandler);
        mSuggestionSearch.destroy();
        super.onDestroy();
    }
}
