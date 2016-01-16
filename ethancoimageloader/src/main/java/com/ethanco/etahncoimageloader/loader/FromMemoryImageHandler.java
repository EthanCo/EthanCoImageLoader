package com.ethanco.etahncoimageloader.loader;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by EthanCo on 2016/1/16.
 */
public class FromMemoryImageHandler extends ImageHandler {
    public FromMemoryImageHandler(Context context) {
        super(context);
    }

    @Override
    protected Bitmap loadBitmap(String url, int requestWidth, int requestHeight) {
        return imageHelper.loadBitmapFromMemory(url);
    }
}
