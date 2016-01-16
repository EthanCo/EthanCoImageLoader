package com.ethanco.etahncoimageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.ethanco.etahncoimageloader.loader.FromDiskImageHandler;
import com.ethanco.etahncoimageloader.loader.FromMemoryImageHandler;
import com.ethanco.etahncoimageloader.loader.FromNetImageHandler;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Zhk on 2016/1/1.
 */
public class ImageLoader {

    //CPU的核心数
    private static final int CPU_CORE_COUNT = Runtime.getRuntime().availableProcessors();
    //最大线程数
    private static final int MAXIMUM_POOL_SIZE = CPU_CORE_COUNT * 2 + 1;
    //闲置存留时间(秒)
    private static final long KEEP_ALIVE_TIME = 60;
    public static final int TAG_KEY_URI = 1122334455;
    //线程安全的计数器类
    private final AtomicInteger mCount = new AtomicInteger(1);
    //核心线程数
    private static final int CORE_POOL_SIEZE = CPU_CORE_COUNT + 1;
    private static final String TAG = "EthanCo";
    public static final int LOAD_IMAGE_SUCCESS = 0x6601;
    private final ThreadFactory threadFactory = new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "ThreadPoolExecutor Thread: " + mCount.getAndIncrement());
        }
    };

    private final Executor LOADER_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIEZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(), threadFactory);
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOAD_IMAGE_SUCCESS:
                    RequestImage requestImage = (RequestImage) msg.obj;
                    if (requestImage != null) {
                        Bitmap bitmap = requestImage.bitmap;
                        if (bitmap != null) {
                            String tag = (String) requestImage.imageView.getTag(TAG_KEY_URI);
                            if (tag.equals(requestImage.url)) {
                                requestImage.imageView.setImageBitmap(bitmap);
                            } else {
                                Log.w(TAG, "image.tag!=url");
                            }
                        }
                    }
                    break;
                default:
            }
        }
    };

    private final FromMemoryImageHandler headHandler;
    private final FromDiskImageHandler diskHandler;
    private final FromNetImageHandler netHandler;

    private ImageLoader(Context context) {
        headHandler = new FromMemoryImageHandler(context);
        diskHandler = new FromDiskImageHandler(context);
        netHandler = new FromNetImageHandler(context);

        headHandler.setNextHandler(diskHandler);
        diskHandler.setNextHandler(netHandler);
    }

    private static ImageLoader sInstance;
    public static ImageLoader buildImageLoader(Context context){
        if (sInstance == null) {
            sInstance = new ImageLoader(context);
        }
        return sInstance;
    }


    /**
     * 加载Bitmap
     *
     * @param url           图片的URL
     * @param imageView     哪个ImageVIew
     */
    public void dispaly(final String url, final ImageView imageView) {
        imageView.setTag(TAG_KEY_URI, url);

        LOADER_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = headHandler.handler(url, imageView.getWidth(), imageView.getHeight());
                sendBitmap(bitmap, url, imageView);
            }
        });
    }

    private void sendBitmap(Bitmap bitmap, String url, ImageView imageView) {
        RequestImage requestImage = new RequestImage(url, bitmap, imageView);
        Message msg = Message.obtain();
        msg.what = LOAD_IMAGE_SUCCESS;
        msg.obj = requestImage;
        mHandler.sendMessage(msg);
    }

    class RequestImage {
        public RequestImage(String url, Bitmap bitmap, ImageView imageView) {
            this.url = url;
            this.bitmap = bitmap;
            this.imageView = imageView;
        }

        ImageView imageView;
        String url;
        Bitmap bitmap;
    }
}
