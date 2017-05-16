package com.careyun.voiceassistant.view.activity;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;


/**i
 * Created by Huangmq on 2017/3/16.
 * Activity管理器
 */

public class ActivityCollerctor {

    public static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity){
       activities.add(activity);
    }

    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    public static void finishAll(){
        for(Activity activity:activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
        activities.clear();
    }
}
