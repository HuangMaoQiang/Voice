package com.careyun.voiceassistant.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Huangmq on 2017/3/23.
 */

public class CityColumn implements BaseColumns {
    public static final Uri CONTENT_URI = Uri.parse("content://"
            + LocationProvider.AUTHORITY + "/cities");
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.cities";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.cities";
    public static final String ADRESS = "adress";
    public static final String CITY_NAME = "city";
    public static final String PROVINCE_NAME = "provice";
    public static final String DISTRICT_NAME = "district";
    public static final String LATITUDE = "latitude";
    public static final String LONTITUDE = "lontitude";
    public static final String PROVICE_CODE = "provice_code";
    public static final String CITY_CODE = "city_code";
}
