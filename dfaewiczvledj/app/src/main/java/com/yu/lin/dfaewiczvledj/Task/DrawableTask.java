package com.yu.lin.dfaewiczvledj.Task;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by Lin-Yu on 01/11/2017.
 *
 */

public class DrawableTask extends AsyncTask<Integer, Void, Drawable> {

    @SuppressLint("StaticFieldLeak")
    private Context mContext;
    private final WeakReference<ImageView> imageViewReference;

    public DrawableTask(ImageView imageView, Context context) {
        this.mContext = context;
        this.imageViewReference = new WeakReference<ImageView>(imageView);
    }

    public interface OnLoadImageListener {
        public void onLoadImageSuccess(String key,Drawable drawable);
        public void onLoadImageError();
    }

    @Override
    protected Drawable doInBackground(Integer... integer) {
        // integer[0] is drawableId
        return mContext.getResources().getDrawable(integer[0]);
    }

    @Override
    // Once the image is downloaded, associates it to the imageView
    protected void onPostExecute(Drawable drawable) {
        if (isCancelled()) {
            drawable = null;
        }

        if (imageViewReference != null) {
            ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageDrawable(drawable);
            }
        }
    }
}
