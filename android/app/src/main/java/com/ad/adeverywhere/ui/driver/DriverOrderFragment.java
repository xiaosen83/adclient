package com.ad.adeverywhere.ui.driver;


import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ad.adeverywhere.R;
import com.ad.adeverywhere.dao.Ad;
import com.ad.adeverywhere.dao.UserState;
import com.ad.adeverywhere.demo.DBHelper;
import com.ad.adeverywhere.demo.DemoAD;
import com.ad.adeverywhere.demo.DemoAdsListAdapter;
import com.ad.adeverywhere.demo.DemoDataSource;
import com.ad.adeverywhere.ui.common.AdListAdapter;
import com.ad.adeverywhere.utils.AdManager;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DriverOrderFragment extends Fragment {

    private ListView mOrderView;
    private Button mSortButton;
    private Button mFilterButton;

    private PopupWindow mFilterPopup;
    private boolean[] mFilterMetirc = new boolean[6];
    private static final int POPUP_TYPE_SORT = 0;
    private static final int POPUP_TYPE_FILTER = 1;
    private static final int POPUP_POS_LEFT=0;
    private static final int POPUP_POS_RIGHT=1;
    private static final int POPUP_POS_TAIL=2;
    private static final int POPUP_PERIOD_3=3;
    private static final int POPUP_PERIOD_6=4;
    private static final int POPUP_PERIOD_MORE=5;

    private DemoDataSource ds = null;
    private Activity parent = null;
    public DriverOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_driver_order, container, false);
        mOrderView = (ListView)v.findViewById(R.id.driverorderlist);
        mFilterButton = (Button)v.findViewById(R.id.filterbutton);
        mSortButton = (Button)v.findViewById(R.id.sortbutton);
/*
        AdManager.getInstance().loadAdsDummy();
        List<Ad> ads = AdManager.getInstance().getAll();

        AdListAdapter adapter1 = new AdListAdapter(this.getActivity(), ads, ads.size());
        mOrderView.setAdapter(adapter1);
        mOrderView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), DriverADActivity.class);
                Ad ad = (Ad)parent.getAdapter().getItem(position);
                i.putExtra("ad_id", ad.getId());
                startActivity(i);
            }
        });
*/
        parent = this.getActivity();
        ds = new DemoDataSource(parent);
        ds.open();
        new LoadDummyDatatoList().execute();

        mFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mFilterPopup.showAsDropDown(mFilterButton);
                //mFilterButton.setAlpha(0.5F);
                onPopupShow(POPUP_TYPE_FILTER);
            }
        });
        mSortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPopupShow(POPUP_TYPE_SORT);

            }
        });

        //init popup windows
        initFilterPopupWindows();

        // Inflate the layout for this fragment
        return v;
    }

    private void onPopupShow(int type){
        if(POPUP_TYPE_FILTER == type){
            mFilterPopup.showAsDropDown(mFilterButton);
            mFilterButton.setTextColor(getResources().getColor(R.color.popup_dropdown_color));
            mFilterButton.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.arrows_up), null);
        }
    }

    private void onPopupHide(int type){
        if(POPUP_TYPE_FILTER == type){
            mFilterButton.setTextColor(getResources().getColor(R.color.popup_withdrow_color));
            mFilterButton.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.arrows_down), null);
        }
    }

    private class LoadDummyDatatoList extends AsyncTask<Void, Void, List<DemoAD>>{
        @Override
        protected List<DemoAD> doInBackground(Void... params) {
            if(UserState.getInstance().getUser()!=null)
                if(UserState.getInstance().getUser().getId()>0)
                    return ds.getAdsNotBelongToUser((int)UserState.getInstance().getUser().getId());
            return ds.getAds(0);
        }

        @Override
        protected void onPostExecute(List<DemoAD> ads) {
            DemoAdsListAdapter adapter1 = new DemoAdsListAdapter(parent, ads);
            mOrderView.setAdapter(adapter1);
            mOrderView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(getActivity(), DriverADActivity.class);
                    DemoAD ad = (DemoAD) parent.getAdapter().getItem(position);
                    i.putExtra("ad_id", (int)ad.getId());
                    startActivity(i);
                }
            });
        }
    }

    private void initFilterPopupWindows(){
        LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.popup_ad_filter, null);

        mFilterPopup = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //v.setAlpha(0.5F);
        Button ok = (Button)v.findViewById(R.id.okbutton);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFilterPopup.dismiss();
            }
        });
        FrameLayout parent = (FrameLayout)v.findViewById(R.id.popup_filter);
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFilterPopup.dismiss();
            }
        });

        int[] ids = {R.id.popup_pos_left,R.id.popup_pos_right,R.id.popup_pos_tail
                ,R.id.popup_period_lt_3,R.id.popup_period_lt_6,R.id.popup_period_gt_6};
        for(int i=0; i<ids.length;i++){
            Button pos_left = (Button)v.findViewById(ids[i]);
            pos_left.setOnClickListener(new MyLovelyOnClickListener(i, pos_left));
        }

        mFilterPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                onPopupHide(POPUP_TYPE_FILTER);
            }
        });
    }
    public class MyLovelyOnClickListener implements View.OnClickListener
    {
        int btIndex;
        Button btButton = null;
        public MyLovelyOnClickListener(int index, Button bt) {
            this.btIndex = index;
            btButton = bt;
        }

        @Override
        public void onClick(View v)
        {
            mFilterMetirc[btIndex] = !mFilterMetirc[btIndex];
            if(mFilterMetirc[btIndex]) {
                btButton.setBackgroundResource(R.drawable.bt_border_selected);
            }
            else {
                btButton.setBackgroundResource(R.drawable.bt_border_unselected);
            }
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ds.close();
    }
}
