package com.careyun.voiceassistant.view.floatwindow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.careyun.voiceassistant.R;

import java.lang.reflect.Field;



public class FloatWindowBigView extends LinearLayout {

	//聊天的listview
	private ListView listview;
//	public ListviewAdapter adapter;

	// 记录大悬浮窗的宽
	public int viewWidth;
	// 记录大悬浮窗的高
	public int viewHeight;
	// 系统状态栏的高度
	private static int statusBarHeight;
	// 用于更新小悬浮窗的位置
	private WindowManager windowManager;
	// 小悬浮窗的布局参数
	public WindowManager.LayoutParams smallWindowParams;
	// 记录当前手指位置在屏幕上的横坐标
	private float xInScreen;
	// 记录当前手指位置在屏幕上的纵坐标
	private float yInScreen;
	// 记录手指按下时在屏幕上的横坐标,用来判断单击事件
	private float xDownInScreen;
	// 记录手指按下时在屏幕上的纵坐标,用来判断单击事件
	private float yDownInScreen;
	// 记录手指按下时在小悬浮窗的View上的横坐标
	private float xInView;
	// 记录手指按下时在小悬浮窗的View上的纵坐标
	private float yInView;
	// 单击接口
	private MyOnClickListener listener;

	public WindowManager.LayoutParams bigWindowParams;

	private Context context;

	public FloatWindowBigView(Context context) {
		super(context);
		this.context = context;

//		LayoutInflater.from(context).inflate(R.layout.desklayout, this);

//		View view = findViewById(R.id.big_window);
//		viewWidth = view.getLayoutParams().width;
//		viewHeight = view.getLayoutParams().height;

		bigWindowParams = new WindowManager.LayoutParams();
		// 设置显示的位置，默认的是屏幕中心
//		bigWindowParams.x = ScreenUtils.getScreenWidth(context) / 2 - viewWidth
//				/ 2;
//		bigWindowParams.y = ScreenUtils.getScreenHeight(context) / 2
//				- viewHeight / 2;
		bigWindowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
		bigWindowParams.format = PixelFormat.RGBA_8888;

		// 设置交互模式
		bigWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

		bigWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
		bigWindowParams.width = viewWidth;
		bigWindowParams.height = viewHeight;

//		initView();

	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		/*switch (event.getAction()) {
			// 手指按下时记录必要的数据,纵坐标的值都减去状态栏的高度
			case MotionEvent.ACTION_DOWN:
				// 获取相对与小悬浮窗的坐标
				xInView = event.getX();
				yInView = event.getY();
				// 按下时的坐标位置，只记录一次
				xDownInScreen = event.getRawX();
				yDownInScreen = event.getRawY() - statusBarHeight;
				break;
			case MotionEvent.ACTION_MOVE:
				// 时时的更新当前手指在屏幕上的位置
				xInScreen = event.getRawX();
				yInScreen = event.getRawY() - statusBarHeight;
				// 手指移动的时候更新小悬浮窗的位置
				updateViewPosition();
				break;
			case MotionEvent.ACTION_UP:
				// 如果手指离开屏幕时，按下坐标与当前坐标相等，则视为触发了单击事件
				if (xDownInScreen == event.getRawX()
						&& yDownInScreen == (event.getRawY() - getStatusBarHeight())) {

					if (listener != null) {
						listener.click();
					}

				}
				break;
		}*/
		switch (event.getAction()){
			case MotionEvent.ACTION_DOWN:

				break;
			case MotionEvent.ACTION_MOVE:

				break;
			case MotionEvent.ACTION_UP:

				break;
		}
		return true;
	}


	/**
	 * 设置单击事件的回调接口
	 */
	public void setOnClickListener(MyOnClickListener listener) {
		this.listener = listener;
	}

	/**
	 * 更新大悬浮窗在屏幕中的位置
	 */
	private void updateViewPosition() {
		smallWindowParams.x = (int) (xInScreen - xInView);
		smallWindowParams.y = (int) (yInScreen - yInView);
		windowManager.updateViewLayout(this, smallWindowParams);
	}

	/**
	 * 获取状态栏的高度
	 *
	 * @return
	 */
	private int getStatusBarHeight() {

		try {
			Class<?> c = Class.forName("com.android.internal.R$dimen");
			Object o = c.newInstance();
			Field field = c.getField("status_bar_height");
			int x = (Integer) field.get(o);
			return getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}



	/**
	 * 单击接口
	 *
	 * @author zhaokaiqiang
	 *
	 */
	public interface MyOnClickListener {

		void click();

	}

/*	private void initView() {
		Log.e("执行了initview","执行了");
		Button tv_back = (Button) findViewById(R.id.closebtn);
		Button msetting = (Button)findViewById(R.id.setbtn);
		listview = (ListView) findViewById(R.id.listview11);
		//实例化adapter
		adapter = new ListviewAdapter(context);
		listview.setAdapter(adapter);

		tv_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FloatWindowManager.getInstance(context).removeBigWindow();
			}
		});
		msetting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View mView) {
				Intent intent = new Intent(context, SettingActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
				FloatWindowManager.getInstance(context).removeBigWindow();
			}
		});

	}*/

	/**
	 * 更新view
	 * @param mMsgInfo
     */
/*	public void updataView(MsgInfo mMsgInfo){
		if(adapter!=null){
			Log.e("执行了updateView","执行了："+mMsgInfo.getLeft_text());
			adapter.addDataToAdapter(mMsgInfo);
			adapter.notifyDataSetChanged();
		}

	}*/

}
