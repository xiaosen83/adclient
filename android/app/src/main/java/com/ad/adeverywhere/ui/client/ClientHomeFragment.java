package com.ad.adeverywhere.ui.client;


import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.ad.adeverywhere.R;
import com.ad.adeverywhere.ui.MainActivity;
import com.ad.adeverywhere.ui.common.AdvAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClientHomeFragment extends Fragment {

    private ImageView[] mImageView = null;
    private ViewPager mViewPager = null;
    private ViewGroup mViewGroup = null;
    private Button mButtonGo = null;
    private boolean mIsContinue = true;
    private AtomicInteger mIndex = new AtomicInteger(0);
    private Thread mViewPagerThread = null;
    List<View> mAdvPics = null;

    public ClientHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_client_home, container, false);
        mViewPager = (ViewPager)v.findViewById(R.id.ad_pager);
        mViewGroup = (ViewGroup)v.findViewById(R.id.viewGroup);
        mButtonGo = (Button)v.findViewById(R.id.home_order_now);
        mButtonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).switchToTab(1);
            }
        });
        initViewPager(v);
        return v;

    }

    @Override
    public void onResume() {
        super.onResume();
        startThread();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopThread();
    }

    private void initViewPager(View context){
        if(mImageView != null)
            return;

        mAdvPics = new ArrayList<View>();

        //Mock data
        ImageView img1 = new ImageView(context.getContext());
        img1.setBackgroundResource(R.drawable.test_ad1);
        mAdvPics.add(img1);
        ImageView img2 = new ImageView(context.getContext());
        img2.setBackgroundResource(R.drawable.test_ad2);
        mAdvPics.add(img2);
        ImageView img3 = new ImageView(context.getContext());
        img3.setBackgroundResource(R.drawable.test_ad3);
        mAdvPics.add(img3);
        ImageView img4 = new ImageView(context.getContext());
        img4.setBackgroundResource(R.drawable.test_ad4);
        mAdvPics.add(img4);
        ImageView img5 = new ImageView(context.getContext());
        img5.setBackgroundResource(R.drawable.test_ad5);
        mAdvPics.add(img5);

        mImageView = new ImageView[mAdvPics.size()];
        ImageView imageView = null;
        for(int i=0; i<mAdvPics.size(); i++){
            imageView = new ImageView(context.getContext());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(20,20));
            imageView.setPadding(5,5,5,5);
            mImageView[i] = imageView;
            if(i==0)
                mImageView[i].setBackgroundResource(R.drawable.banner_dian_focus);
            else
                mImageView[i].setBackgroundResource(R.drawable.banner_dian_blur);
            mViewGroup.addView(mImageView[i]);
        }
    }


    private final Handler viewHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            mViewPager.setCurrentItem(msg.what);
            super.handleMessage(msg);
        }

    };

    private void startThread(){
        if(mViewPager.getAdapter() == null){
            mViewPager.setAdapter(new AdvAdapter(mAdvPics));
            mViewPager.setOnPageChangeListener(new GuidePageChangeListener());
            mViewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                        case MotionEvent.ACTION_MOVE:
                            mIsContinue = false;
                            break;
                        case MotionEvent.ACTION_UP:
                            mIsContinue = true;
                            break;
                        default:
                            mIsContinue = true;
                            break;
                    }
                    return false;
                }
            });
        }
        mIsContinue = true;
        mViewPagerThread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (mIsContinue) {
                    {
                        viewHandler.sendEmptyMessage(mIndex.get());
                        mIndex.incrementAndGet();
                        if (mIndex.get() > mImageView.length - 1) {
                            mIndex.getAndAdd(mImageView.length*(-1));
                        }
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            System.out.print(false);
                        }  catch (Exception ee){

                        }
                    }
                }
            }

        });
        mViewPagerThread.start();
    }
    private void stopThread(){
        mIsContinue = false;
        mViewPager.removeAllViews();
        mViewPager.setAdapter(null);
        mViewPagerThread.interrupt();
    }


    private final class GuidePageChangeListener implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

        @Override
        public void onPageSelected(int position) {
            mIndex.getAndSet(position);
            for(int i=0; i<mImageView.length; i++){
                mImageView[position].setBackgroundResource(R.drawable.banner_dian_focus);
                if(i != position)
                    mImageView[i].setBackgroundResource(R.drawable.banner_dian_blur);
            }
        }
    }
}
