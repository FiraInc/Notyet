package com.unknown.notyet;

import android.content.Context;
import android.util.Log;
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
    Context mContext;

    public InventoryAdapter(Context context, ArrayList<InventoryItems> users) {
        super(context, R.layout.creature_inventory_item, users);
        this.mContext = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final InventoryItems items = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.inventory_item, parent, false);
        }

        try {
            creatureNumberArray.add(items.Title);
        }catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("FINALS", items.Title);

        // Lookup view for data population
        TextView Title = (TextView) convertView.findViewById(R.id.TitleText);
        final TextView amount = (TextView) convertView.findViewById(R.id.amount);
        TextView itemDescription = (TextView) convertView.findViewById(R.id.itemDescription);
        ImageView ImageLogo = (ImageView) convertView.findViewById(R.id.Image);
        // Populate the data into the template view using the data object
        Title.setText(items.Title);
        amount.setText(items.Amount);
        ImageLogo.setImageDrawable(items.ImageBitmap);
        itemDescription.setText(items.Description);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(items.Amount) > 0) {
                    if (Inventory.category.equals("Food")) {
                        if (CurrentCreatureInfo.foodStatus < 100) {
                            Leveling.feed(CurrentCreatureInfo.CreatureFolderNumber, items.Title);

                            int x = Integer.parseInt(items.Amount) - 1;
                            items.Amount = String.valueOf(x);
                            Saver.storeSave(items.Title, -1);
                            amount.setText(items.Amount);
                        }
                    }else if (Inventory.category.equals("Special")) {
                        Items.itemSpecial(mContext, items.Title);

                        int x = Integer.parseInt(items.Amount) - 1;
                        items.Amount = String.valueOf(x);
                        Saver.storeSave(items.Title, -1);
                        amount.setText(items.Amount);
                    }
                }
            }
        });

        return convertView;
    }

}