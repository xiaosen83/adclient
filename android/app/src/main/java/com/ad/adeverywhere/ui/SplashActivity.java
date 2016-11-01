package com.ad.adeverywhere.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ad.adeverywhere.R;
import com.ad.adeverywhere.dao.UserState;
import com.ad.adeverywhere.demo.DemoDataSource;
import com.ad.adeverywhere.demo.DemoPersistData;
import com.ad.adeverywhere.ui.vendor.VendorMainActivity;
import com.ad.adeverywhere.utils.PrefManager;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    public final int SPLASH_SCREEN_DISPLAY_TIME_IN_MILLISECONDS = 3000;

    private ImageView[] mImageView = null;
    private ViewPager mViewPager = null;
    List<View> mSplashPics = null;
    MyViewPagerAdapter mViewPageAdapter = null;

    private TextView[] mDots = null;
    private LinearLayout mDotContainer = null;
    private LinearLayout mBtContainer = null;
    private Button mBtDriver, mBtClient;

    PrefManager prefManager;

    private void launchHomeScreen(){
        //TODO for develop purpose, set driver manually
        //UserState.getInstance().setRole(UserState.USER_ROLE_CLIENT);
        //UserState.getInstance().setRole(UserState.USER_ROLE_DRIVER);

        prefManager.setFirstTimeLaunch(false);
        Intent i = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DemoPersistData.getInstance().setContext(this);
        DemoPersistData.getInstance().loadData();

        // Checking for first time launch - before calling setContentView()
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }
        setContentView(R.layout.activity_splash);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SplashActivity.this, PortalActivity.class);
                startActivity(i);
                finish();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String xx = DemoPersistData.getInstance().getRole();
                int role = Integer.parseInt((xx!=null && xx.length()>0)?(xx):"0");
                if(role == UserState.USER_ROLE_CLIENT || role == UserState.USER_ROLE_DRIVER)
                {
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    //i.putExtra("portaltype", DemoPersistData.getInstance().getRole());
                    startActivity(i);
                }
                else if(role == UserState.USER_ROLE_VENDOR){
                    Intent i = new Intent(SplashActivity.this, VendorMainActivity.class);
                    startActivity(i);
                }
                else{
                    Intent i = new Intent(SplashActivity.this, PortalActivity.class);
                    startActivity(i);
                }
                finish();
            }
        }, SPLASH_SCREEN_DISPLAY_TIME_IN_MILLISECONDS);
*/


        mBtContainer = (LinearLayout)findViewById(R.id.btContainer);
        mDotContainer = (LinearLayout)findViewById(R.id.dotContainer);
        mBtContainer.setVisibility(View.GONE);

        initViewPager();
        addTots(0);
        initActions();
    }

    private  void  initActions(){
        mBtDriver = (Button)findViewById(R.id.btDriverEntry);
        mBtClient = (Button)findViewById(R.id.btClientEntry);

        mBtDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserState.getInstance().setRole(UserState.USER_ROLE_DRIVER, true);
                launchHomeScreen();
            }
        });
        mBtClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserState.getInstance().setRole(UserState.USER_ROLE_CLIENT, true);
                launchHomeScreen();
            }
        });
    }

    private  void addTots(int pos){
        mDots = new TextView[mImageView.length];
        mDotContainer.removeAllViews();
        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            if(i == pos)
                mDots[i].setTextColor(getResources().getColor(R.color.dot_dark_screen1));
            else
                mDots[i].setTextColor(getResources().getColor(R.color.dot_light_screen1));
            mDotContainer.addView(mDots[i]);
        }
    }
    private void initViewPager(){

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mSplashPics = new ArrayList<View>();

        //Mock data
        ImageView img1 = new ImageView(this);
        img1.setBackgroundResource(R.drawable.splash_1);
        mSplashPics.add(img1);
        ImageView img2 = new ImageView(this);
        img2.setBackgroundResource(R.drawable.splash_2);
        mSplashPics.add(img2);
        ImageView img3 = new ImageView(this);
        img3.setBackgroundResource(R.drawable.splash_3);
        mSplashPics.add(img3);

        mImageView = new ImageView[mSplashPics.size()];
        ImageView imageView = null;
        for(int i=0; i<mSplashPics.size(); i++){
            imageView = new ImageView(this);
            //imageView.setLayoutParams(new ViewGroup.LayoutParams(20,20));
            imageView.setPadding(5,5,5,5);
            mImageView[i] = imageView;
        }

        mViewPageAdapter = new MyViewPagerAdapter();
        mViewPager.setAdapter(mViewPageAdapter);
        mViewPager.addOnPageChangeListener(viewPagerPageChangeListener);
    }
    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addTots(position);

            if (position == mImageView.length - 1) {
                //show  jump button at last page
                mBtContainer.setVisibility(View.VISIBLE);
            } else {
                mBtContainer.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(mSplashPics.get(position));

            return mSplashPics.get(position);
        }

        @Override
        public int getCount() {
            return mImageView.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
