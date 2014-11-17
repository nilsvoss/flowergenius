package com.tsc.FlowerGenius.gui;

import java.io.IOException;
import java.util.ArrayList;

import com.tsc.FlowerGenius.brain.Flower;
import com.tsc.FlowerGenius.brain.FlowerDatabase;

import com.tsc.FlowerGenius.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class DatabaseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_database);
		
		try {
			FlowerDatabase db = ((FlowerApp) this.getApplication()).getDb();
			ArrayList<Flower> flowers = db.getObjects();
			
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
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.database, menu);
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
