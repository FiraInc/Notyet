package com.unknown.notyet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Johannett321 on 04.03.2017.
 */

public class CreatureInventoryAdapter extends ArrayAdapter<CreatureInventoryItems> {

    static ArrayList<String> creatureNumberArray;

    public CreatureInventoryAdapter(Context context, ArrayList<CreatureInventoryItems> users) {
        super(context, R.layout.creature_inventory_item, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        CreatureInventoryItems items = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.creature_inventory_item, parent, false);
        }
        creatureNumberArray.add(items.Title);
        // Lookup view for data population
        TextView Title = (TextView) convertView.findViewById(R.id.TitleText);
        ImageView ImageLogo = (ImageView) convertView.findViewById(R.id.Image);
        TextView healthStatus = (TextView) convertView.findViewById(R.id.healthStatus);
        TextView foodStatus = (TextView) convertView.findViewById(R.id.foodStatus);
        // Populate the data into the template view using the data object
        Title.setText(items.Title);
        ImageLogo.setImageBitmap(items.ImageBitmap);
        healthStatus.setText(String.valueOf(items.HealthStatus));
        foodStatus.setText(String.valueOf(items.FoodStatus));
        // Return the completed view to render on screen
        return convertView;
    }

}