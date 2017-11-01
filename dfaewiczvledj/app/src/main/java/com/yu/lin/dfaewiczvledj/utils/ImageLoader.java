package com.yu.lin.dfaewiczvledj.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.ImageView;

import com.yu.lin.dfaewiczvledj.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Lin-Yu on 01/11/2017.
 *
 */

public class ImageLoader {
    private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    private MemoryCache memoryCache = new MemoryCache();
    private FileCache fileCache;
    private ExecutorService executorService;
    private Handler handler = new Handler();//handler to display images in UI thread
    private Activity mActivity;

    public ImageLoader(Context context, Activity activity) {
        mActivity = activity;
        fileCache=new FileCache(context);
        executorService= Executors.newFixedThreadPool(5);
    }

    private final int stub_id = R.drawable.default_img;
    public void DisplayImage(String url, ImageView imageView) {
        imageViews.put(imageView, url);
        Bitmap bitmap = memoryCache.get(url);
        if(bitmap!=null) {
            imageView.setImageBitmap(bitmap);
        } else {
            queuePhoto(url, imageView);
            imageView.setImageResource(stub_id);
        }
    }

    private void queuePhoto(String url, ImageView imageView) {
        PhotoToLoad p = new PhotoToLoad(url, imageView);
        executorService.submit(new PhotosLoader(p));
    }

    private Bitmap getBitmap(String url) {
        File f=fileCache.getFile(url);

        //from SD cache
        Bitmap b = decodeFile(f);
        if(b!=null) {
            return b;
        }
        // from web
        try {
            Bitmap bitmap = null;
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is=conn.getInputStream();
            OutputStream os = new FileOutputStream(f);
            Utils.CopyStream(is, os);
            os.close();
            conn.disconnect();
            bitmap = decodeFile(f);
            return bitmap;
        } catch (Throwable ex){
            ex.printStackTrace();
            if(ex instanceof OutOfMemoryError)
                memoryCache.clear();
            return null;
        }
    }

    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f) {
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1 = new FileInputStream(f);
            BitmapFactory.decodeStream(stream1,null,o);
            stream1.close();

            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 70;
            int width_tmp=o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while(true) {
                if(width_tmp /2 < REQUIRED_SIZE || height_tmp /2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }
            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            FileInputStream stream2 = new FileInputStream(f);
            Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    //Task for the queue
    private class PhotoToLoad {
        private String url;
        private ImageView imageView;
        private PhotoToLoad(String url, ImageView imageView){
            this.url = url;
            this.imageView = imageView;
        }
    }

    private class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;
        PhotosLoader(PhotoToLoad photoToLoad){
            this.photoToLoad = photoToLoad;
        }

        @Override
        public void run() {
            try {
                if(imageViewReused(photoToLoad)) {
                    return;
                }
                Bitmap bmp=getBitmap(photoToLoad.url);
                memoryCache.put(photoToLoad.url, bmp);
                if(imageViewReused(photoToLoad)) {
                    return;
                }
                final BitmapDisPlayer bd = new BitmapDisPlayer(bmp, photoToLoad);
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        handler.post(bd);
                    }
                });

            }catch(Throwable th){
                th.printStackTrace();
            }
        }
    }

    private boolean imageViewReused(PhotoToLoad photoToLoad){
        String tag = imageViews.get(photoToLoad.imageView);
        if(tag == null || !tag.equals(photoToLoad.url)){
            return true;
        }
        return false;
    }

    //Used to display bitmap in the UI thread
    class BitmapDisPlayer implements Runnable {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;
        private BitmapDisPlayer(Bitmap bitmap, PhotoToLoad photoToLoad) {
            this.bitmap= bitmap;
            this.photoToLoad = photoToLoad;
        }
        public void run() {
            if(imageViewReused(photoToLoad)) {
                return;
            }
            if(bitmap!=null) {
                photoToLoad.imageView.setImageBitmap(bitmap);
            } else {
                photoToLoad.imageView.setImageResource(stub_id);
            }

        }
    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }
}
