package com.careyun.voiceassistant.map.BaiduAppNavi;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Huangmq on 2017/4/26.
 */

public class BaiduNavi {



    public void startNavi(String address){
        Intent i1 = new Intent();
        i1.setData(Uri.parse("baidumap://map/navi?query=" + address));
        Log.e("导航要的地方", "目的地：" + address);
    }

}
