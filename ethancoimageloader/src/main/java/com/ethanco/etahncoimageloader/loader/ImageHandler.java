package com.ethanco.etahncoimageloader.loader;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by EthanCo on 2016/1/16.
 */
public abstract class ImageHandler {

    protected final Context mContext;
    protected final ImageHelper imageHelper;

    public ImageHandler(Context context) {
        this.mContext = context;
        imageHelper = new ImageHelper(context);
    }

    private ImageHandler nextHandler;

    public final Bitmap handler(String url, int requestWidth, int requestHeight) {
        Bitmap bitmap = loadBitmap(url,requestWidth,requestWidth);
        if (bitmap != null) {
            //如果bitmap!=null，自己处理
            return bitmap;
        } else {
            if (nextHandler != null) {
                //让下一个Hanlder处理
                return this.nextHandler.handler(url,requestWidth,requestWidth);
            } else {
                //没有下一个Handler，返回默认图片
                return imageHelper.loadDefaultBitmap();
            }
        }
    }

    public void setNextHandler(ImageHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    protected abstract Bitmap loadBitmap(String url,int requestWidth, int requestHeight);
}
