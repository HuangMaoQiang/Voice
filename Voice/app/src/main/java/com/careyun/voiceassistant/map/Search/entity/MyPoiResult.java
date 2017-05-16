package com.careyun.voiceassistant.map.Search.entity;


/**
 * Created by Huangmq on 2017/3/22.
 */

public class MyPoiResult {

    private String mResult;
    private String mAdress;
    private String mDistance;
    private String mMeasure;
    private String mNum;


    public MyPoiResult(String mResult, String mAdress, String mDistance, String mMeasure){
        this.mResult = mResult;
        this.mAdress = mAdress;
        this.mDistance = mDistance;
        this.mMeasure =mMeasure;
    }


    public String getmNum(){
        return mNum;
    }


    public String getmResult(){
        return mResult;
    }

    public String getmMeasure(){
        return mMeasure;
    }

    public String getmAdress(){
        return mAdress;
    }
    public String getmDistance(){
        return mDistance;
    }


}
