package com.ethanco.etahncoimageloader.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.util.LruCache;

import com.ethanco.etahncoimageloader.DiskLruCache;
import com.ethanco.etahncoimageloader.ImageResizer;
import com.ethanco.etahncoimageloader.MD5Encoder;
import com.ethanco.etahncoimageloader.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by EthanCo on 2016/1/16.
 */
public class ImageHelper {

    //-------------------------- 配置 -------------------------------------------//
    //最大的图片缓存大小 占APP总内存的1/?
    private static final int MEMORY_CACHE_PART = 8;
    //硬盘缓存
    private static final long DISK_CACHE_SIZE = 50 * 1024 * 1024;

    //下载图片时的输入输出流的大小
    private static final int IO_BUFFER_SIZE = 8 * 1024;
    //-------------------------- 配置 END -------------------------------------------//
    private static final String TAG = "EthanCo";
    private static final int DISK_CACHE_INDEX = 0;
    private final Context mContext;
    private LruCache<String, Bitmap> memoryCache;
    private ImageResizer imageResizer = new ImageResizer();
    private DiskLruCache diskLruCache;

    public ImageHelper(Context context) {
        this.mContext = context;
        //创建MemoryCache (LruCache)
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / MEMORY_CACHE_PART;
        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getWidth() * value.getHeight() / 1024;
            }
        };

        //创建DishLruCache
        File cacheFile = getDiskCacheDir(context, "bitmapCache");
        if (!cacheFile.exists()) {
            cacheFile.mkdir();
        }
        try {
            diskLruCache = DiskLruCache.open(cacheFile, 1, 1, DISK_CACHE_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getDiskCacheDir(Context context, String folderName) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return new File(context.getExternalCacheDir().getPath(), folderName);
        } else {
            return new File(context.getCacheDir().getPath(), folderName);
        }
    }

    /**
     * 通过本地存储加载Bitmap
     */
    public Bitmap loadBitmapFromDisk(String url, int reqWidth, int reqHeight) {
        if (diskLruCache == null) {
            return null;
        }

        String key = getKey(url);
        Bitmap bitmap = null;
        try {
            DiskLruCache.Snapshot snapshot = diskLruCache.get(key);
            if (snapshot != null) {
                FileInputStream fis = (FileInputStream) snapshot.getInputStream(DISK_CACHE_INDEX);
                FileDescriptor descriptor = fis.getFD();
                bitmap = imageResizer.decodeSampledBitmapFromFileDescriptor(descriptor, reqWidth, reqHeight);
                if (bitmap != null) {
                    addBitmapToMemoryCache(key, bitmap);
                }
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        memoryCache.put(key, bitmap);
    }

    /**
     * 通过网络加载Bitmap
     *
     * @return
     */
    public Bitmap loadBitmapFromNet(String url, int requestWidth, int requestHeight) {

        String key = getKey(url);
        try {
            //先缓存到本地
            DiskLruCache.Editor editor = diskLruCache.edit(key);
            if (editor != null) {
                OutputStream outputStream = editor.newOutputStream(DISK_CACHE_INDEX);
                if (downloadUrlToStream(url, outputStream)) {
                    editor.commit();
                } else {
                    editor.abort();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //从本地缓存加载返回
        return loadBitmapFromDisk(url, requestWidth, requestHeight);
    }

    private boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;

        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
            out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);

            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (IOException e) {
            Log.e(TAG, "downloadBitmap failed." + e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 通过内存加载Bitmap
     */
    public Bitmap loadBitmapFromMemory(String url) {
        String key = getKey(url);
        return memoryCache.get(key);
    }

    //加载默认图片
    public Bitmap loadDefaultBitmap() {
        Drawable drawable = mContext.getResources().getDrawable(R.mipmap.image_default);
        return ((BitmapDrawable) drawable).getBitmap();
    }

    private String getKey(String url) {
        return MD5Encoder.encode(url);
    }
}
