package com.ad.adeverywhere.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.ad.adeverywhere.dao.UserState;
import com.ad.adeverywhere.demo.DemoPersistData;
import com.ad.adeverywhere.ui.client.ClientHomeFragment;
import com.ad.adeverywhere.ui.client.ClientMeFragment;
import com.ad.adeverywhere.ui.client.ClientPushFragment;
import com.ad.adeverywhere.ui.driver.DriverHomeFragment;
import com.ad.adeverywhere.ui.driver.DriverMeFragment;
import com.ad.adeverywhere.ui.driver.DriverOrderFragment;
import com.ad.adeverywhere.utils.Logger;
import com.ad.adeverywhere.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    public static final int UI_PORTAL_DRIVER = 0;
    public static final int UI_PORTAL_CLIENT = 1;
    public static final int UI_PORTAL_VENDOR = 2;

    private static final Logger logger = Logger.getInstance();

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private int mPortalType = UI_PORTAL_DRIVER;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle b = getIntent().getExtras();
        if(b != null)
            mPortalType = b.getInt("portaltype");
        else
            mPortalType = (UserState.getInstance().getRole() == UserState.USER_ROLE_DRIVER)?UI_PORTAL_DRIVER:UI_PORTAL_CLIENT;

        DemoPersistData.getInstance().setContext(this);

        mViewPager = (ViewPager)findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        if(mPortalType == UI_PORTAL_DRIVER){
            adapter.addFrag(new DriverHomeFragment());
            adapter.addFrag(new DriverOrderFragment());
            adapter.addFrag(new DriverMeFragment());
            mViewPager.setAdapter(adapter);


            mTabLayout = (TabLayout)findViewById(R.id.tabs);
            mTabLayout.setupWithViewPager(mViewPager);
            mTabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
            mTabLayout.getTabAt(0).setCustomView(getTabView(R.string.home, R.drawable.connection));
            mTabLayout.getTabAt(1).setCustomView(getTabView(R.string.driver_order, R.drawable.settings));
            mTabLayout.getTabAt(2).setCustomView(getTabView(R.string.me, R.drawable.settings));
        }
        else{
            adapter.addFrag(new DriverHomeFragment());
            adapter.addFrag(new ClientPushFragment());
            adapter.addFrag(new ClientMeFragment());
            mViewPager.setAdapter(adapter);


            mTabLayout = (TabLayout)findViewById(R.id.tabs);
            mTabLayout.setupWithViewPager(mViewPager);
            mTabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
            mTabLayout.getTabAt(0).setCustomView(getTabView(R.string.home, R.drawable.connection));
            mTabLayout.getTabAt(1).setCustomView(getTabView(R.string.client_push, R.drawable.settings));
            mTabLayout.getTabAt(2).setCustomView(getTabView(R.string.me, R.drawable.settings));
        }
    }

    private TextView getTabView(int text, int icon) {
        TextView textView = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        textView.setText(text);
        textView.setCompoundDrawablesWithIntrinsicBounds(0, icon, 0, 0);
        textView.setGravity(Gravity.CENTER);

        return textView;
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        public void addFrag(android.app.Fragment fragment) {
            mFragmentList.add(fragment);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DemoPersistData.getInstance().saveData();
    }

    public void switchToTab(int itemIndex){
        mViewPager.setCurrentItem(itemIndex, true);
    }
}
