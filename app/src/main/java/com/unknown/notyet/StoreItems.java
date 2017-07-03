package com.unknown.notyet;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

/**
 * Created by Johannett321 on 04.03.2017.
 */

public class StoreItems {

    public String Title;
    public String Description;
    public String Price;
    public String Category;
    public String Amount;
    public String CurrentAmount;
    public BitmapDrawable ItemImage;

    public StoreItems(String title, String description, String category, String amount, String currentAmount, String price, BitmapDrawable imageBitmap) {
        this.Title = title;
        this.Description = description;
        this.Price = price;
        this.Category = category;
        this.Amount = amount;
        this.ItemImage = imageBitmap;
        this.CurrentAmount = currentAmount;
    }
}
