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

public class StoreAdapter extends ArrayAdapter<StoreItems> {

    static ArrayList<String> creatureNumberArray;

    public StoreAdapter (Context context, ArrayList<StoreItems> users) {
        super(context, R.layout.store_item, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        StoreItems items = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.store_item, parent, false);
        }

        ImageView itemImage = (ImageView) convertView.findViewById(R.id.itemImage);
        TextView itemTitle = (TextView) convertView.findViewById(R.id.itemTitle);
        TextView itemDescription = (TextView) convertView.findViewById(R.id.itemDescription);
        TextView itemPrice = (TextView) convertView.findViewById(R.id.itemPrice);

        itemImage.setImageBitmap(items.ItemImage);
        itemTitle.setText(items.Title);
        itemDescription.setText(items.Description);
        itemPrice.setText(items.Price);

        return convertView;
    }
}
