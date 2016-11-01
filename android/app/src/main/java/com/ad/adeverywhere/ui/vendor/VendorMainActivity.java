package com.ad.adeverywhere.ui.vendor;

import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.ad.adeverywhere.R;
import com.ad.adeverywhere.ui.driver.DriverHomeFragment;
import com.ad.adeverywhere.ui.driver.DriverMeFragment;
import com.ad.adeverywhere.ui.driver.DriverOrderFragment;
import com.ad.adeverywhere.utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class VendorMainActivity extends AppCompatActivity implements OnFragmentInteractionListener{
    public static final String TAG = "VendorMainActivity";
    private static final Logger logger = Logger.getInstance();

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_main);

        mViewPager = (ViewPager)findViewById(R.id.pager);
        VendorViewPagerAdapter adapter = new VendorViewPagerAdapter(getFragmentManager());

        adapter.addFrag(VendorHomeFragment.newInstance("",""));
        adapter.addFrag(VendorSignFragment.newInstance("",""));
        adapter.addFrag(VendorMeFragment.newInstance("",""));
        mViewPager.setAdapter(adapter);


        mTabLayout = (TabLayout)findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mTabLayout.getTabAt(0).setCustomView(getTabView(R.string.home, R.drawable.connection));
        mTabLayout.getTabAt(1).setCustomView(getTabView(R.string.vendor_sign_ad_tab, R.drawable.settings));
        mTabLayout.getTabAt(2).setCustomView(getTabView(R.string.me, R.drawable.settings));

    }
    public static class VendorViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        public VendorViewPagerAdapter(FragmentManager manager) {
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
    private TextView getTabView(int text, int icon) {
        TextView textView = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        textView.setText(text);
        textView.setCompoundDrawablesWithIntrinsicBounds(0, icon, 0, 0);
        textView.setGravity(Gravity.CENTER);

        return textView;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
