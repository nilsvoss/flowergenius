package com.tsc.FlowerGenius.gui;

import java.io.IOException;
import java.util.List;

import de.fsinb.hal3000.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;

public class SnapActivity extends Activity implements SurfaceHolder.Callback {

	private Camera mCamera;

	@SuppressLint("ClickableViewAccessibility")
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_snap);
		
		SurfaceView surface = (SurfaceView) findViewById(R.id.surfaceView1);
		SurfaceHolder holder = surface.getHolder();
		holder.addCallback(this);
		// tells Android that this surface will have its data constantly replaced
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surface.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View view, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_UP:
					// float x = event.getX();
					// float y = event.getY();
					Intent intent = new Intent(view.getContext(), ResultsActivity.class);
					Bundle b = new Bundle();
					b.putIntArray("pixels", new int[100]);
					b.putInt("width", 10);
					b.putInt("height", 10);
					b.putInt("centerX", 5);
					b.putInt("centerY", 5);
					intent.putExtras(b);
					startActivityForResult(intent, 0);
					break;
				}
				return true;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.snap, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {       
	    Parameters parameters = mCamera.getParameters();
	    List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
	    Camera.Size previewSize = previewSizes.get(0);

	    parameters.setPreviewSize(previewSize.width, previewSize.height);
	    parameters.setFlashMode(Parameters.FLASH_MODE_AUTO);
	    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);

	    mCamera.setParameters(parameters);

	    //Display display = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mCamera.setDisplayOrientation(90);
	    mCamera.startPreview();
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, acquire the camera and tell it where
		// to draw the preview.
		mCamera = Camera.open();
		try {
			mCamera.setPreviewDisplay(holder);
		} catch (IOException exception) {
			mCamera.release();
			mCamera = null;
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		mCamera.stopPreview();
		mCamera.release();
		mCamera = null;
	}

}
