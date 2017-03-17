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

public class InventoryAdapter extends ArrayAdapter<InventoryItems> {

    static ArrayList<String> creatureNumberArray;

    public InventoryAdapter (Context context, ArrayList<InventoryItems> users) {
        super(context, R.layout.inventory_item, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        InventoryItems items = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.inventory_item, parent, false);
        }
        creatureNumberArray.add(items.Title);
        // Lookup view for data population
        TextView Title = (TextView) convertView.findViewById(R.id.TitleText);
        ImageView ImageLogo = (ImageView) convertView.findViewById(R.id.Image);
        // Populate the data into the template view using the data object
        Title.setText(items.Title);
        ImageLogo.setImageBitmap(items.ImageBitmap);
        // Return the completed view to render on screen
        return convertView;
    }

}