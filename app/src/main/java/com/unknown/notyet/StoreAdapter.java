package com.unknown.notyet;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
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

public class StoreAdapter extends ArrayAdapter<StoreItems> {

    static ArrayList<String> creatureNumberArray;
    Context mContext;

    TextView CoinsText;
    TextView DiamondsText;

    public StoreAdapter (Context context, ArrayList<StoreItems> users, TextView coinsText, TextView diamondsText) {
        super(context, R.layout.store_item, users);
        this.mContext = context;
        this.CoinsText = coinsText;
        this.DiamondsText = diamondsText;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        // Get the data item for this position
        final StoreItems items = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.store_item, parent, false);
        }

        ImageView itemImage = (ImageView) convertView.findViewById(R.id.itemImage);
        ImageView buyButton = (ImageView) convertView.findViewById(R.id.buyButton);
        TextView itemTitle = (TextView) convertView.findViewById(R.id.itemTitle);
        TextView itemDescription = (TextView) convertView.findViewById(R.id.itemDescription);
        TextView itemPrice = (TextView) convertView.findViewById(R.id.itemPrice);
        final TextView amountLeft = (TextView) convertView.findViewById(R.id.amountLeft);

        itemImage.setImageBitmap(items.ItemImage.getBitmap());
        itemTitle.setText(items.Title);
        itemDescription.setText(items.Description);
        itemPrice.setText(items.Price + "$");
        amountLeft.setText(items.CurrentAmount);

        final View finalConvertView = convertView;
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Bank.getCurrentCoins(mContext) >= Integer.parseInt(items.Price)) {
                    Saver.storeSave(items.Title, Integer.parseInt(items.Amount));
                    Bank.addCoins(mContext, -Integer.parseInt(items.Price));

                    DiamondsText.setText(String.valueOf(Bank.getCurrentDiamonds(mContext)));
                    CoinsText.setText(String.valueOf(Bank.getCurrentCoins(mContext)));
                    items.CurrentAmount = String.valueOf(Items.loadAmount(items.Title));
                    amountLeft.setText(items.CurrentAmount);
                }else {
                    Intent intent = new Intent("my-integer");
                    intent.putExtra("message", 1);
                    intent.putExtra("positioner", position);
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                }
            }
        });

        return convertView;
    }
}
