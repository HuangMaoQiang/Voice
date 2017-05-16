// IWeatherCallBack.aidl
package com.careyun.weather;

// Declare any non-default types here with import statements

interface IWeatherCallBack {

    void CityWeatherCallback(String result);
   /**
     * 扩展功能回调
     */
     void ExtendFunctionCallBack();
}
