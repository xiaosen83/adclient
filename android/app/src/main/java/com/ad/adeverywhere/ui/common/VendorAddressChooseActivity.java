package com.ad.adeverywhere.ui.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.ad.adeverywhere.R;
import com.ad.adeverywhere.dao.VendorAddressManager;
import com.ad.adeverywhere.utils.Logger;

import java.util.List;

public class VendorAddressChooseActivity extends AppCompatActivity {
    public static final String TAG = "VendorAddressChooseActivity";
    private static final Logger logger = Logger.getInstance();

    private PopupWindow mDistrictChoosePopup = null;
    private LinearLayout mFilterButton = null;
    private ListView mListAddresses = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_address_choose);

        setTitle(R.string.title_activity_address_selection);
        ActionBar bar = getSupportActionBar();
        if(bar != null)
            bar.setDisplayHomeAsUpEnabled(true);

        mFilterButton = (LinearLayout) findViewById(R.id.layoutSelAddr);
        mFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDistrictChoosePopup.showAsDropDown(mFilterButton);
            }
        });

        VendorAddressManager.getInstance().loadAddress(null);
        mListAddresses = (ListView)findViewById(R.id.listView2);
        List<String> addresses = VendorAddressManager.getInstance().getAddressOfDistrict(null);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, addresses);
        mListAddresses.setAdapter(adapter);

        mListAddresses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String district = (String)parent.getAdapter().getItem(position);
                Intent i = new Intent();
                i.putExtra("address", district);
                setResult(Activity.RESULT_OK, i);
                finish();
            }
        });

        //init popup windows
        initFilterPopupWindows();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id==android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void initFilterPopupWindows(){
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.popup_address, null);

        mDistrictChoosePopup = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //v.setAlpha(0.5F);
        ListView lvAddresses = (ListView)v.findViewById(R.id.listView);
        List<String> addresses = VendorAddressManager.getInstance().getDistrictsOfProvince(null);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, addresses);
        lvAddresses.setAdapter(adapter);
        lvAddresses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDistrictChoosePopup.dismiss();
                String district = (String)parent.getAdapter().getItem(position);
                onSelectDistrict(district);
            }
        });
        //set max height to half of the screen
        Display display = getWindowManager().getDefaultDisplay();
        int maxheight = (int)display.getWidth();
    }

    private void onSelectDistrict(String district){
        if(mListAddresses.getAdapter() != null)
            mListAddresses.setAdapter(null);
        List<String> addresses = VendorAddressManager.getInstance().getAddressOfDistrict(district);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, addresses);
        mListAddresses.setAdapter(adapter);
    }
}
