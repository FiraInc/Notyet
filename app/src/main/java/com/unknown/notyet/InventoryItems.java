package com.unknown.notyet;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

/**
 * Created by Johannett321 on 04.03.2017.
 */

public class InventoryItems {

    public String Title;
    public String Description;
    public String Amount;
    public BitmapDrawable ImageBitmap;

    public InventoryItems(String title, BitmapDrawable imageBitmap, String amount, String description) {
        this.Title = title;
        this.ImageBitmap = imageBitmap;
        this.Amount = amount;
        this.Description = description;

    }

}
