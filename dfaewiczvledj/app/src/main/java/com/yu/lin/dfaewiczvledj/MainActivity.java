package com.yu.lin.dfaewiczvledj;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.yu.lin.dfaewiczvledj.provider.ImageList;

import java.util.List;

/**
 * Created by Lin-Yu on 31/10/2017.
 *
 */
public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private static List<Integer> imageList = ImageList.getDrawableIdList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Log.d(TAG, "onCreate: " + imageList);
    }



}
