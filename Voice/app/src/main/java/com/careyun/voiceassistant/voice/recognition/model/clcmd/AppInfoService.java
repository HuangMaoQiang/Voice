package com.careyun.voiceassistant.voice.recognition.model.clcmd;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.careyun.voiceassistant.voice.recognition.model.entity.AppInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * 实现获取手机中安装的应用程序的主要业务功能，封装了如何获取手机安装的应用程序的方法
 * Created by Huangmq on 2017/2/13.
 */

public class AppInfoService {

    private Context context;
    private PackageManager pm;


    //构造函数
    public AppInfoService(Context context) {
        // TODO Auto-generated constructor stub
        this.context = context;
        pm = context.getPackageManager();
    }

    /**
     * 得到手机中所有的应用程序信息
     *
     * @return
     */
    public List<AppInfo> getAppInfos() {
        //创建要返回的集合对象
        List<AppInfo> appInfos = new ArrayList<AppInfo>();
        //获取手机中所有安装的应用集合
        List<ApplicationInfo> applicationInfos = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        //遍历所有的应用集合
        for (ApplicationInfo info : applicationInfos) {

            AppInfo appInfo = new AppInfo();

            //获取应用程序的图标
            Drawable app_icon = info.loadIcon(pm);
            appInfo.setApp_icon(app_icon);

            //获取应用的名称
            String app_name = info.loadLabel(pm).toString();
            appInfo.setApp_name(app_name);

            //获取应用的包名
            String packageName = info.packageName;

            appInfo.setPackagename(packageName);
            try {
                //获取应用的版本号
                PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
                String app_version = packageInfo.versionName;
                appInfo.setApp_version(app_version);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            //判断应用程序是否是用户程序
            boolean isUserApp = filterApp(info);
            appInfo.setUserApp(isUserApp);
            appInfos.add(appInfo);
        }
        return appInfos;
    }

    /**
     * 打开app
     *
     * @param mS
     * @return
     */

    public void OpenApp(String mS) {
        //创建要返回的集合对象
        List<AppInfo> appInfos = new ArrayList<AppInfo>();
        //获取手机中所有安装的应用集合
        List<ApplicationInfo> applicationInfos = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        //遍历所有的应用集合
        for (ApplicationInfo info : applicationInfos) {

            AppInfo appInfo = new AppInfo();

            //获取应用的名称
            String app_name = info.loadLabel(pm).toString();

            if (mS.equals(app_name)) { //比较label
                String packageName = info.packageName; //获取包名
                Intent intent = new Intent();
                //获取intent
                intent = pm.getLaunchIntentForPackage(packageName);
                //intent.setFlags(~Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else if (mS == null) {
                Toast.makeText(context, "还未安装此应用", Toast.LENGTH_SHORT).show();
            }

        }

    }

    public void CloseApp(String mS) {
        //创建要返回的集合对象
        List<AppInfo> appInfos = new ArrayList<AppInfo>();
        //获取手机中所有安装的应用集合
        List<ApplicationInfo> applicationInfos = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        //遍历所有的应用集合
        for (ApplicationInfo info : applicationInfos) {

            AppInfo appInfo = new AppInfo();

            //获取应用的名称
            String app_name = info.loadLabel(pm).toString();

            if (mS.equals(app_name)) { //比较label
                String packageName = info.packageName; //获取包名
                ActivityManager m = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
                m.killBackgroundProcesses(packageName);
            } else if (app_name == null) {
                Toast.makeText(context, "还未安装此应用", Toast.LENGTH_SHORT).show();
            }

        }
    }

/*
    public boolean isAppRunning(String appName)
    {
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : list) {

        }

    }
*/


    //判断应用程序是否是用户程序
    public boolean filterApp(ApplicationInfo info) {
        //原来是系统应用，用户手动升级
        if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            return true;
            //用户自己安装的应用程序
        } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            return true;
        }
        return false;
    }


}
