package com.ggec.uitest.ui.resource;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.ggec.uitest.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 属性(Attribute)资源自定义一个AlphaImageView来实现图片透明度渐变
 * */
public class AlphaImageView extends ImageView {
	private static final String TAG = "AlphaImageView";

	// 图像透明度每次改变的大小
	private int alphaDelta = 0;
	// 记录图片当前的透明度。
	private int curAlpha = 0;
	// 每隔多少毫秒透明度改变一次
	private final int SPEED = 100;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x123) {
				// 每次增加curAlpha的值
				curAlpha += alphaDelta;
				if (curAlpha > 255) curAlpha = 255;
				Log.d(TAG,"refresh ImageView curAlpha = " + curAlpha);
				// 修改该ImageView的透明度
				AlphaImageView.this.setImageAlpha(curAlpha);
			}
		}
	};

	public AlphaImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AlphaImageView);
		// 获取duration参数
		int duration = typedArray.getInt(R.styleable.AlphaImageView_duration, 0);
		// 计算图像透明度每次改变的大小
		alphaDelta = 255 / duration;
		Log.d(TAG,"AlphaImageView初始化时alphaDelta = " + alphaDelta);
		final Timer timer = new Timer();
		// 按固定间隔发送消息，通知系统改变图片的透明度
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Message msg = new Message();
				msg.what = 0x123;
				if (curAlpha >= 255) {
					timer.cancel();
				} else {
					handler.sendMessage(msg);
				}
			}
		}, 0, SPEED);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		Log.d(TAG,"onDraw(), curAlpha = " + curAlpha);
		this.setImageAlpha(curAlpha);
		super.onDraw(canvas);
	}
}
