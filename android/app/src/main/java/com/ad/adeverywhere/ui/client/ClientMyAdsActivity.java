package com.ad.adeverywhere.ui.client;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ad.adeverywhere.R;
import com.ad.adeverywhere.dao.Ad;
import com.ad.adeverywhere.dao.UserState;
import com.ad.adeverywhere.demo.DBHelper;
import com.ad.adeverywhere.demo.DemoAD;
import com.ad.adeverywhere.demo.DemoDataSource;
import com.ad.adeverywhere.ui.common.ClientAdsListAdaper;
import com.ad.adeverywhere.ui.common.MeListAdapter;
import com.ad.adeverywhere.ui.common.MyAdsListAdaper;

import java.util.List;

public class ClientMyAdsActivity extends AppCompatActivity {

    public static final int ACTIVITY_REQ_EDITAD = 131;

    public static final int TAB_AD_PENDING = 0;
    public static final int TAB_AD_READY = 1;
    public static final int TAB_AD_FINISHED = 2;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_my_ads);

//TODO setup dummy data
        UserState.getInstance().loadDriverAdsDummy();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        if(bar != null)
            bar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_driver_my_ads, menu);
        return true;
    }
*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else  if(id==android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        Context context = null;
        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_driver_my_ads, container, false);
            ListView mListView = (ListView)v.findViewById(R.id.lv_my_ad);
            int  tabIndex = getArguments().getInt(ARG_SECTION_NUMBER);
            final List<DemoAD> adList;
            DemoDataSource ds = new DemoDataSource(getActivity());
            ds.open();
            int user_id = UserState.getInstance().getUserId(this.getActivity());
            switch (tabIndex){
                case (TAB_AD_READY+1):
                    adList = ds.getClientAds(user_id, DBHelper.AD_TYPE_READY);
                    break;
                case (TAB_AD_PENDING+1):
                    adList = ds.getClientAds(user_id, DBHelper.AD_TYPE_PENDING);
                    break;
                case (TAB_AD_FINISHED+1):
                    adList = ds.getClientAds(user_id, DBHelper.AD_TYPE_FINISHED);
                    break;
                default:
                    adList = null;break;
            }
            ds.close();
            ClientAdsListAdaper adapter = new ClientAdsListAdaper(this.getActivity(), adList);
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(getActivity(), ClientEditAdActivity.class);
                    i.putExtra(ClientEditAdActivity.TARGET_ADID, adList.get(position).getId());
                    i.putExtra(ClientEditAdActivity.ACTION_TYPE, ClientEditAdActivity.ACTION_TYPE_VIEW);
                    startActivityForResult(i, ClientMyAdsActivity.ACTIVITY_REQ_EDITAD);
                }
            });
            return v;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case TAB_AD_READY:
                    return getString(R.string.me_tab_ad_current);
                case TAB_AD_PENDING:
                    return getString(R.string.me_tab_ad_pending);
                case TAB_AD_FINISHED:
                    return getString(R.string.me_tab_ad_history);
            }
            return null;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ACTIVITY_REQ_EDITAD:
                //refresh listview
                mSectionsPagerAdapter.notifyDataSetChanged();
                break;
        }
    }
}
