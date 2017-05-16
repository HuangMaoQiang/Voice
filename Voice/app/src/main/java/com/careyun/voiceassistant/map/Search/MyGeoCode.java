package com.careyun.voiceassistant.map.Search;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.DistanceUtil;
import com.careyun.voiceassistant.map.Search.entity.MyPoiResult;
import com.careyun.voiceassistant.util.Constant;

import java.math.BigDecimal;


import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Huangmq on 2017/3/24.
 */

public class MyGeoCode implements OnGetGeoCoderResultListener{


    private GeoCoder mSearch = null;
    public Context mContext;
    private GeoCallBack mGeoCallBack;
    private SharedPreferences LocationInfo;
    public SharedPreferences.Editor mEditor;

    private LatLng p1;
    private LatLng p2;
    private String mCity;
    private String mPoiAddress;
    private String mDistrict;
    private String mNum;

    public MyGeoCode(Context mContext){

        this.mContext = mContext;
      // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        LocationInfo = mContext.getSharedPreferences(Constant.EXTRA_LOCATION_INFO,MODE_PRIVATE);
        mEditor = LocationInfo.edit();

    }


    public void poiGeoCode(String mcity,String poiAdress,String district){
/*        if(mSharedPreferences.contains(Constant.MYLOCATION_CITY))
        {
            CITY = mSharedPreferences.getString(Constant.MYLOCATION_CITY,"");
        }*/
        mCity = mcity;
        mPoiAddress = poiAdress;
        mDistrict = district;
        mSearch.geocode(new GeoCodeOption().city(
                mcity).address(poiAdress));
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {

        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//            Toast.makeText(mContext, "抱歉，未能找到结果", Toast.LENGTH_LONG).show();
            mGeoCallBack.onGeoCodeResult(new MyPoiResult("", "", "", ""));
            return;
        }
        if(LocationInfo.contains(Constant.MYLATITUDE))
        {
            String mylat = LocationInfo.getString(Constant.MYLATITUDE,"");
            String mylongi = LocationInfo.getString(Constant.MYLONGITUDE,"");
            p1 = new LatLng(Double.valueOf(mylat),Double.valueOf(mylongi));
        }
        p2 = new LatLng(result.getLocation().latitude,result.getLocation().longitude);
        Double dis = DistanceUtil.getDistance(p1, p2);

        BigDecimal bd = new BigDecimal(dis/1000.0);
        bd = bd.setScale(1,BigDecimal.ROUND_HALF_UP);
        if(mGeoCallBack!= null) {
            mGeoCallBack.onGeoCodeResult(new MyPoiResult(mPoiAddress, mCity + mDistrict, bd.toString(), "公里"));
        }

//      Log.e("经纬度坐标","纬度："+String.valueOf(result.getLocation().latitude)+"经度："+String.valueOf(result.getLocation().longitude));
        Log.e("距离终点距离为：","约"+mDistrict+bd.toString()+"公里");
//
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {


    }

    public void setGeoCallBakc(GeoCallBack callback){
        mGeoCallBack = callback;
    }

     public interface GeoCallBack{
        void onGeoCodeResult(MyPoiResult result);
    }
}
