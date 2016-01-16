package com.ethanco.etahncoimageloader;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileDescriptor;

/**
 * 图片缩放类
 * <p/>
 * Created by EthanCo on 2016/1/1.
 */
public class ImageResizer {

    private static final String TAG = "EthanCo";


    public ImageResizer() {
    }

    /**
     * 将Resource资源压缩为Bitmap
     *
     * @param res       getResources()
     * @param resId     资源id
     * @param reqWidth  希望的长度
     * @param reqHeight 希望的高度
     * @return
     */
    public Bitmap decodeSampledBitmapFromResource(Resources res,
                                                  int resId, int reqWidth, int reqHeight) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        //设置inJustDecodeBounds=true，不进行真正的decode，只是可以获取图片宽高
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        //计算inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        //根据inSampleSize进行压缩
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * 通过FileDescriptor将FileInputStream压缩为Bitmap
     *
     * @param fd
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public Bitmap decodeSampledBitmapFromFileDescriptor(FileDescriptor fd, int reqWidth, int reqHeight) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        //设置inJustDecodeBounds=true，不进行真正的decode，只是可以获取图片宽高
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);

        //计算inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        //根据inSampleSize进行压缩
        return BitmapFactory.decodeFileDescriptor(fd, null, options);
    }

    public int calculateInSampleSize(BitmapFactory.Options options,
                                     int reqWidth, int reqHeight) {
        if (reqWidth == 0 || reqHeight == 0) { //如果req...为0，直接返回，不计算(没意义)
            return 1;
        }

        int height = options.outHeight;
        int width = options.outWidth;

        int inSampleSize = 1;

        //如果宽或高大于期望的宽/高，则/2
        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;

            //如果/2后仍然>=reqHeight，则使inSampleSize*2;
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
