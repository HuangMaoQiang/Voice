package com.careyun.voiceassistant.voice.recognition.model.clcmd;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.WindowManager;

import com.careyun.voiceassistant.broadcast.myDeviceAdminReceiver;


/**
 * Created by Huangmq on 2017/2/5.
 * 控制屏幕亮度
 */

public class ScreenCmd {

    private DevicePolicyManager policyManager;
    private ComponentName componentName;
    private static  Context context;

    public ScreenCmd(Context mcontext) {
        //获取设备管理服务
        this.context = mcontext;
        policyManager = (DevicePolicyManager) mcontext.getSystemService(Context.DEVICE_POLICY_SERVICE);
        //AdminReceiver 继承自 DeviceAdminReceiver
        componentName = new ComponentName(mcontext, myDeviceAdminReceiver.class);

    }


    // 判断是否开启了自动亮度调节
    public static boolean IsAutoBrightness() {
        boolean IsAutoBrightness = false;
        try {
            IsAutoBrightness = Settings.System.getInt(
                    context.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return IsAutoBrightness;
    }

    // 获取当前屏幕的亮度
    public static int getScreenBrightness( ) {

        int nowBrightnessValue = 0;
        ContentResolver resolver = context.getContentResolver();

        try {
            nowBrightnessValue = Settings.System.getInt(
                    resolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nowBrightnessValue;
    }

    // 设置亮度
    // 程序退出之后亮度失效
    public static void setCurWindowBrightness( int brightness) {

        // 如果开启自动亮度，则关闭。否则，设置了亮度值也是无效的
        if (IsAutoBrightness()) {
            stopAutoBrightness();
        }
        // context转换为Activity
        Activity activity = (Activity) context;
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        // 异常处理
        if (brightness < 1) {
            brightness = 1;
        }
        // 异常处理
        if (brightness > 255) {
            brightness = 255;
        }
        lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);
        activity.getWindow().setAttributes(lp);
    }

    // 设置系统亮度
    // 程序退出之后亮度依旧有效
    public static void setSystemBrightness( int brightness) {
        // 异常处理
        if (brightness < 1) {
            brightness = 1;
        }

        // 异常处理
        if (brightness > 255) {
            brightness = 255;
        }
        saveBrightness( brightness);
    }

    // 调高系统亮度
    // 程序退出之后亮度依旧有效

    public static void setUpSystemBrightness() {

        int Current = getScreenBrightness();
        Current = Current + 15;
        if (Current > 255) {
            saveBrightness( 255);
        } else {
            saveBrightness( Current);
        }
    }

    // 调高系统亮度
    // 程序退出之后亮度依旧有效

    public static void setDownSystemBrightness() {

        int Current = getScreenBrightness();
        Current = Current - 15;
        if (Current < 1) {
            saveBrightness( 1);
        } else {
            saveBrightness( Current);
        }
    }

    // 最大系统亮度
    // 程序退出之后亮度依旧有效
    public static void setMaxSystemBrightness( ) {
        saveBrightness( 255);
    }

    // 最低系统亮度
    // 程序退出之后亮度依旧有效
    public static void setMinSystemBrightness() {
        saveBrightness(1);
    }


    // 停止自动亮度调节
    public static void stopAutoBrightness() {

        Settings.System.putInt(context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
    }

    // 开启亮度自动调节
    public static void startAutoBrightness() {
        Settings.System.putInt(context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
    }

    // 保存亮度设置状态
    public static void saveBrightness(int brightness) {

        Uri uri = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
        Settings.System.putInt(context.getContentResolver(),Settings.System.SCREEN_BRIGHTNESS, 250);
        context.getContentResolver().notifyChange(uri, null);
    }


    //锁频
    public void lockScreen() {

        boolean active = policyManager.isAdminActive(componentName);
        if (!active) {//若无权限
            activeManage();//去获得权限
            policyManager.lockNow();//并锁屏
        }
        if (active) {
            policyManager.lockNow();//直接锁屏
        }
    }

    //权限获取
    private void activeManage() {
        // 启动设备管理(隐式Intent) - 在AndroidManifest.xml中设定相应过滤器
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);

        //权限列表
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);

        //描述(additional explanation)
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "一键锁屏");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
