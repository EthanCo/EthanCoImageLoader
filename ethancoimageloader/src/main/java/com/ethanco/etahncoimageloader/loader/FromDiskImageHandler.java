package com.ethanco.etahncoimageloader.loader;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by EthanCo on 2016/1/16.
 */
public class FromDiskImageHandler extends ImageHandler {
    public FromDiskImageHandler(Context context) {
        super(context);
    }

    @Override
    protected Bitmap loadBitmap(String url, int requestWidth, int requestHeight) {
        return imageHelper.loadBitmapFromDisk(url, requestWidth, requestHeight);
    }
}
