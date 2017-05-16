// IWeatherService.aidl
package com.careyun.weather;
import com.careyun.weather.IWeatherCallBack;

interface IWeatherService {
    void localCityWeather(String time);
   /**
     * 查询其他城市天气信息
     */
    void otherCityWeather(String city,String time);
   /**
     * 扩展功能回调
     */
     void extendFunction();

     void registerCallback(IWeatherCallBack cb);
}
