package com.tsc.FlowerGenius.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class SnapOverlay extends View {
	
	private Paint mPaintRed;
	
	public SnapOverlay(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public SnapOverlay(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SnapOverlay(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		mPaintRed = new Paint();
        mPaintRed.setStyle(Paint.Style.FILL);
        mPaintRed.setColor(Color.RED);
        mPaintRed.setTextSize(25);
        
	}
	
	@Override
    protected void onDraw(Canvas canvas) {
		canvas.drawText("Touch the blossom", 10, 30, mPaintRed);
		super.onDraw(canvas);
	}

}
