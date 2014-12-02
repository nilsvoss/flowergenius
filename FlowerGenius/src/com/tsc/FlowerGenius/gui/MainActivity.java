package com.tsc.FlowerGenius.gui;

import java.io.IOException;
import java.io.InputStream;

import com.tsc.FlowerGenius.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
        ImageView imageView1 = (ImageView) findViewById(R.id.imageView1);
        InputStream istr = null;
        try {
            istr = getAssets().open("logo2.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageView1.setImageBitmap(BitmapFactory.decodeStream(istr));
        imageView1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent myIntent = new Intent(v.getContext(), SnapActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });
        
        final Button btnDb = (Button) findViewById(R.id.btnDb);
        btnDb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Intent myIntent = new Intent(v.getContext(), DatabaseActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
