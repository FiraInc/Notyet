package com.unknown.notyet;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by Johannett321 on 04.03.2017.
 */

public class InventoryItems {

    public String Title;
    public int ItemFolderNumber;
    public Bitmap ImageBitmap;
    public String AliveStatus;

    public InventoryItems(String title, Bitmap imageBitmap, int itemFolderNumber, String aliveStatus) {
        this.Title = title;
        this.ImageBitmap = imageBitmap;
        this.ItemFolderNumber = itemFolderNumber;
        if (aliveStatus.equals("1")){
            this.AliveStatus = "1";
        }else {
            this.AliveStatus = "0";
        }

    }

}
