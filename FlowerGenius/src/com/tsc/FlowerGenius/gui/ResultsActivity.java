package com.tsc.FlowerGenius.gui;

import java.io.IOException;
import java.util.List;

import com.tsc.FlowerGenius.brain.DummyRecognizer;
import com.tsc.FlowerGenius.brain.Flower;
import com.tsc.FlowerGenius.brain.MDRecognizer;
import com.tsc.FlowerGenius.brain.Recognizer;

import com.tsc.FlowerGenius.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

public class ResultsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results);
		
		final ImageView imageView1 = (ImageView) findViewById(R.id.imageView1);
		
		Bundle b = getIntent().getExtras();
		//int[] pixels = b.getIntArray("pixels");
		byte[] jpg = b.getByteArray("jpgData");
		Bitmap im = BitmapFactory.decodeByteArray(jpg, 0, jpg.length);
		
		imageView1.setImageBitmap(im);
		
		int[] pixels = new int[im.getHeight() * im.getWidth()];
		im.getPixels(pixels, 0, im.getWidth(), 0, 0, im.getWidth(), im.getHeight());
		int centerX = b.getInt("centerX");
		int centerY = b.getInt("centerY");
		Recognizer rec;
		try {
			rec = new MDRecognizer(((FlowerApp) getApplication()).getDb().getObjects());
			
			List<Flower> flowers = rec.recognize(pixels, im.getWidth(), im.getHeight(), centerX, centerY);
			FlowerAdapter adapter = new FlowerAdapter(getApplicationContext(), R.layout.flower_listitem, flowers);
			
			final ListView listView1 = (ListView) findViewById(R.id.listView1);
			listView1.setAdapter(adapter);
			
			listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
					Flower flower = (Flower) listView1.getItemAtPosition(position);
					Intent intent = new Intent(view.getContext(), DetailActivity.class);
					Bundle b = new Bundle();
					b.putInt("objectId", flower.getId());
					intent.putExtras(b);
					startActivityForResult(intent, 0);
				}
			});
			
			listView1.setAdapter(adapter);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.results, menu);
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
