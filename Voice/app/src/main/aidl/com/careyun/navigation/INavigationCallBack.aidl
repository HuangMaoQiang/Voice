// INavigationCallBack.aidl
package com.careyun.navigation;
// Declare any non-default types here with import statements
interface INavigationCallBack {
    /**
     * PoiSearch回调函数,返回兴趣点（poi）列表
     */
     void PoiSearchCallBack();
    /**
     * GeoCode回调函数,地理编码解锁，返回经纬度信息。
     */
     void GeoCoderCallBack();
    /**
     * StartNavigation,开始导航回调函数
     */
     void StartNavigationCallBack();
     /**
     * cacelNavigation,开始导航回调函数
     */
     void cancelNavigationCallBack();
    /**
     * 语音播报
     */
     void onSpeech(String mS,String mN);
}
