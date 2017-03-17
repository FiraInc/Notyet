package com.unknown.notyet;

import android.graphics.Bitmap;

/**
 * Created by Johannett321 on 04.03.2017.
 */

public class StoreItems {

    public String Title;
    public String Description;
    public String Price;
    public Bitmap ItemImage;

    public StoreItems(String title, String description, String price, Bitmap imageBitmap) {
        this.Title = title;
        this.Description = description;
        this.Price = price;
        this.ItemImage = imageBitmap;
    }
}
