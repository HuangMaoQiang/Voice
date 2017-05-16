// INavigationService.aidl
package com.careyun.navigation;

// Declare any non-default types here with import statements

interface INavigationService {


    //PoiSearch，感兴趣点搜索
    void poiSearch(String arrival);
    //GeoCoder，进行地理编码，得到经纬度坐标信息
    void geoCoder(String number);
    //startNavigation，开始导航行
    void startNavigation();
     // 退出导航
    void cancelNavigation();

}
