package com.tsc.FlowerGenius.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.tsc.FlowerGenius.brain.Flower;

import de.fsinb.hal3000.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FlowerAdapter extends ArrayAdapter<Flower>{

    private ImageView flowerIcon;
    private TextView flowerBotName;
    private TextView flowerName;
    private List<Flower> flowers = new ArrayList<Flower>();
    private Context context;
 
    public FlowerAdapter(Context context, int textViewResourceId, List<Flower> objects) {
        super(context, textViewResourceId, objects);
        this.flowers = objects;
        this.context = context;
    }
 
    public int getCount() {
        return this.flowers.size();
    }
 
    public Flower getItem(int index) {
        return this.flowers.get(index);
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            // ROW INFLATION
            //Log.d(tag, "Starting XML Row Inflation ... ");
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.flower_listitem, parent, false);
            //Log.d(tag, "Successfully completed XML Row Inflation!");
        }
        Flower flower = getItem(position);
        flowerIcon = (ImageView) row.findViewById(R.id.image);
        flowerName = (TextView) row.findViewById(R.id.name);
        flowerBotName = (TextView) row.findViewById(R.id.bot_name);
        flowerName.setText(flower.getName());
        flowerBotName.setText(flower.getBotName());

        try {
            Bitmap bitmap = flower.getSmallImage(context.getResources().getAssets());
            flowerIcon.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return row;
    }
}