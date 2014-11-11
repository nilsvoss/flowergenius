package com.tsc.FlowerGenius.gui;

import java.util.List;

import com.tsc.FlowerGenius.brain.DummyRecognizer;
import com.tsc.FlowerGenius.brain.Flower;
import com.tsc.FlowerGenius.brain.Recognizer;

import de.fsinb.hal3000.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class ResultsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results);
		Bundle b = getIntent().getExtras();
		int[] pixels = b.getIntArray("pixels");
		int width = b.getInt("width");
		int height = b.getInt("height");
		int centerX = b.getInt("centerX");
		int centerY = b.getInt("centerY");
		Recognizer rec = new DummyRecognizer(((FlowerApp) getApplication()).getDb());
		List<Flower> flowers = rec.recognize(pixels, width, height, centerX, centerY);
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
