package com.unknown.notyet;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by Johannett321 on 04.03.2017.
 */

public class CreatureInventoryItems {

    public String Title;
    public int ItemFolderNumber;
    public Bitmap ImageBitmap;
    public String AliveStatus;
    public int FoodStatus;
    public int HealthStatus;

    public CreatureInventoryItems(String title, Bitmap imageBitmap, int itemFolderNumber, String aliveStatus, int healthStatus, int foodStatus) {
        this.Title = title;
        this.ImageBitmap = imageBitmap;
        this.ItemFolderNumber = itemFolderNumber;
        this.FoodStatus = foodStatus;
        this.HealthStatus = healthStatus;
        if (aliveStatus.equals("1")){
            this.AliveStatus = "1";
        }else {
            this.AliveStatus = "0";
        }

    }

}
