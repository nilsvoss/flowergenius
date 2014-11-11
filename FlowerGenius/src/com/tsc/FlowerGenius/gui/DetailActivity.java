package com.tsc.FlowerGenius.gui;

import java.io.IOException;

import com.tsc.FlowerGenius.brain.Flower;
import com.tsc.FlowerGenius.brain.FlowerDatabase;

import de.fsinb.hal3000.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DetailActivity extends Activity {
	
	FlowerDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		
		Bundle b = getIntent().getExtras();
		int id = b.getInt("objectId");
		
		db = new FlowerDatabase(getApplicationContext());
		
		Flower flower;
		try {
			flower = db.getObject(id);
			ImageView iv = (ImageView) findViewById(R.id.imageView1);
			Bitmap bitmap = flower.getImage(getAssets());
			iv.setImageBitmap(bitmap);
			LinearLayout linear = (LinearLayout) findViewById(R.id.linear);
			
			TextView t1 = new TextView(this);
			t1.setText(flower.getName());
			linear.addView(t1);
			
			TextView t2 = new TextView(this);
			t2.setText(flower.getBotName());
			linear.addView(t2);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail, menu);
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
}
