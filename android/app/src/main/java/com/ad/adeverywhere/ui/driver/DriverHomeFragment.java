package com.ad.adeverywhere.ui.driver;

import android.app.Activity;
import android.app.ListFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.ad.adeverywhere.R;
import com.ad.adeverywhere.dao.UserState;
import com.ad.adeverywhere.demo.DemoAD;
import com.ad.adeverywhere.demo.DemoAdsListAdapter;
import com.ad.adeverywhere.demo.DemoDataSource;
import com.ad.adeverywhere.ui.MainActivity;
import com.ad.adeverywhere.ui.common.AdvAdapter;
import com.ad.adeverywhere.ui.common.HotAdsListAdapter;

import org.w3c.dom.Text;

/**
 * Created by cwang on 7/12/16.
 */
public class DriverHomeFragment extends Fragment {

    public static final int DRIVER_JOINED_DUMMY = 12345;
    public static final int CLINET_JOINED_DUMMY = 376;

    private View mheaderView;
    private ImageView[] mImageView = null;
    private ViewPager mViewPager = null;
    private ViewGroup mViewGroup = null;
    private ListView mHotAdView = null;
    private boolean mIsContinue = true;
    private Button mButtonGo = null;
    private AtomicInteger mIndex = new AtomicInteger(0);
    private Thread mViewPagerThread = null;
    List<View> mAdvPics = null;
    AdvAdapter mAdvAdapter = null;
    private Activity context = null;
    private DemoDataSource ds = null;
    private TextView mTxDriverNumber = null;
    private TextView mTxClientNumber = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_driver_home, container, false);
        mHotAdView = (ListView)v.findViewById(R.id.listViewHotAds);
        mViewPager = (ViewPager)v.findViewById(R.id.ad_pager);
        mViewGroup = (ViewGroup)v.findViewById(R.id.viewGroup);
        mButtonGo = (Button)v.findViewById(R.id.home_order_now);
        mButtonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).switchToTab(1);
            }
        });

        if(UserState.getInstance().getRole() == UserState.USER_ROLE_CLIENT)
            mButtonGo.setText(R.string.client_request);

        //set overview joined users
        mTxClientNumber = (TextView)v.findViewById(R.id.txClientJoined);
        mTxDriverNumber = (TextView)v.findViewById(R.id.txDriverJoined);
        mTxDriverNumber.setText(String.valueOf(DRIVER_JOINED_DUMMY));
        mTxClientNumber.setText(String.valueOf(CLINET_JOINED_DUMMY));

        //load viewlist for hot ADS
        new LoadDummyDatatoList().execute();

        //load slider view page for top ADS
        initViewPager(v);

        initClientSpecificUI(v);
        return v;
    }

    private  void initClientSpecificUI(View  v){
        if(UserState.getInstance().getRole() == UserState.USER_ROLE_CLIENT){
            TextView barTitle = (TextView)v.findViewById(R.id.txBarTitle);
            barTitle.setText(R.string.title_home_client);
            mButtonGo.setText(R.string.client_request);
        }
    }

    private final Handler viewHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            int n = mViewPager.getChildCount();
            mViewPager.setCurrentItem(msg.what);
            super.handleMessage(msg);
        }

    };

    private void startThread(){
        if(mViewPager.getAdapter() == null){
           // mAdvAdapter = new AdvAdapter(mAdvPics);
            mViewPager.setAdapter(new AdvAdapter(mAdvPics));
            mViewPager.setOffscreenPageLimit(mAdvPics.size()-1);
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

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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

    private class LoadDummyDatatoList extends AsyncTask<Void, Void, List<DemoAD>> {
        private DemoDataSource ds = null;
        public LoadDummyDatatoList(){
            ds = new DemoDataSource(context);
            ds.open();
        }
        @Override
        protected List<DemoAD> doInBackground(Void... params) {
            return ds.getAds(0);
        }

        @Override
        protected void onPostExecute(List<DemoAD> ads) {
            HotAdsListAdapter adapter1 = new HotAdsListAdapter(context, ads);
            mHotAdView.setAdapter(adapter1);
            mHotAdView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(getActivity(), DriverADActivity.class);
                    DemoAD ad = (DemoAD) parent.getAdapter().getItem(position);
                    i.putExtra("ad_id", ad.getId());
                    //startActivity(i);
                }
            });
            ds.close();
        }
    }
}
