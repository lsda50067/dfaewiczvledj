package com.yu.lin.dfaewiczvledj.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yu.lin.dfaewiczvledj.utils.ImageLoader;
import com.yu.lin.dfaewiczvledj.R;

import java.util.List;

/**
 * Created by Lin-Yu on 01/11/2017.
 *
 */

public class PhotoAdapter extends BaseAdapter {

    private static final String TAG = PhotoAdapter.class.getName();
    private Activity activity;
    private List<String> mImageList;
    private static LayoutInflater inflater = null;
    public ImageLoader imageLoader;

    public PhotoAdapter(Activity a, List<String> imageList){
        activity = a;
        mImageList = imageList;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(activity.getApplicationContext(), activity);
    }

    @Override
    public int getCount() {
        return mImageList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(convertView == null) {
            view = inflater.inflate(R.layout.list_view_item, null);
        }
        TextView text = (TextView) view.findViewById(R.id.text_view);
        final ImageView image=(ImageView) view.findViewById(R.id.img);
        text.setText("item " + position);
        Log.d(TAG, "getView: " + mImageList.get(position));
        imageLoader.DisplayImage(mImageList.get(position), image);
        return view;
    }

}
