package com.yu.lin.dfaewiczvledj;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.yu.lin.dfaewiczvledj.Adapter.PhotoAdapter;
import com.yu.lin.dfaewiczvledj.provider.ImageList;

import java.util.List;

/**
 * Created by Lin-Yu on 31/10/2017.
 *
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener,ListView.OnItemClickListener, NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "MainActivity";
    private static List<String> imageList = ImageList.getImageId();

    private ListView mListView;
    private PhotoAdapter mAdapter;

    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        reSetToolBar();
        initToggle();
    }

    private void initView(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mListView = (ListView) findViewById(R.id.list);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
    }

    private void reSetToolBar(){
        setSupportActionBar(toolbar);
    }

    private void initToggle(){
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected void onResume(){
        super.onResume();

        mAdapter = new PhotoAdapter(this, imageList);
        setListener();
        setAdapter();

//        button.setOnClickListener(this);
    }


    public void setListener(){
        floatingActionButton.setOnClickListener(this);
        mListView.setOnItemClickListener(this);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setAdapter() {
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroy() {
        mListView.setAdapter(null);
        super.onDestroy();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab:
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                mAdapter.imageLoader.clearCache();
                mAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_camera:
                // Handle the camera action
                Log.d(TAG , "Item Camera OnClick");
                break;
            case R.id.nav_gallery:
                Log.d(TAG , "Item Gallery OnClick");
                break;
            case R.id.nav_slideshow:
                Log.d(TAG , "Item SlideShow OnClick");
                break;
            case R.id.nav_manage:
                Log.d(TAG , "Item Tool OnClick");
                break;
        }
        // OnClick then close drawer
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG , "onItemClick: " + position);
    }
}
