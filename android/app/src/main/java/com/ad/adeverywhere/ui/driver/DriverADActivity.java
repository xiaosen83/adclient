package com.ad.adeverywhere.ui.driver;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ad.adeverywhere.R;
import com.ad.adeverywhere.dao.Ad;
import com.ad.adeverywhere.dao.UserState;
import com.ad.adeverywhere.demo.DemoAD;
import com.ad.adeverywhere.demo.DemoDataSource;
import com.ad.adeverywhere.ui.common.VendorAddressChooseActivity;
import com.ad.adeverywhere.utils.AdManager;
import com.ad.adeverywhere.utils.ImageLoadTask;
import com.ad.adeverywhere.utils.Util;

import java.util.List;

public class DriverADActivity extends AppCompatActivity {

    public static final int ACTIVITY_CHOOSE_ADDRESS = 0;

    private Button mButtonGo = null;
    private ImageButton mImageInstallAddress = null;
    private TextView mInstallAddress = null;

    private ImageView mLogo = null;
    private ImageView mModle = null;
    private TextView mCompany = null;
    private TextView mStartDate = null;
    private TextView mEndDate = null;
    private TextView mCars = null;
    private TextView mCarLeft = null;
    private LinearLayout mAddrContainer = null;

    private String mAdId = "";
    private DemoAD ad = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_ad);

        setTitle(R.string.title_reserve_ad);
        Bundle b = getIntent().getExtras();
        if(b != null) {
            mAdId = b.getString("ad_id");
            if(mAdId == null){
                mAdId = String.valueOf(b.getInt("ad_id"));
            }
        }

        ActionBar bar = getSupportActionBar();
        if(bar != null)
          bar.setDisplayHomeAsUpEnabled(true);

        mLogo = (ImageView)findViewById(R.id.ad_logo);
        mCompany = (TextView)findViewById(R.id.ad_company);
        mModle = (ImageView)findViewById(R.id.ad_modle);
        mStartDate = (TextView)findViewById(R.id.ad_startdate);
        mEndDate = (TextView)findViewById(R.id.ad_enddate);
        mCars = (TextView)findViewById(R.id.ad_cars);
        mCarLeft = (TextView)findViewById(R.id.ad_carleft);
        mAddrContainer = (LinearLayout)findViewById(R.id.layoutAddr);

        mInstallAddress = (TextView) findViewById(R.id.driver_install_address);
        mImageInstallAddress = (ImageButton)findViewById(R.id.imageButtonAddress);
        mButtonGo = (Button)findViewById(R.id.order);
        mButtonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOrderButtonClicked();
            }
        });
        mImageInstallAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddressPopupWnd();
            }
        });

        initPage();
    }

    private void showAddressPopupWnd(){
        Intent i = new Intent(this, VendorAddressChooseActivity.class);
        startActivityForResult(i, ACTIVITY_CHOOSE_ADDRESS);
    }

    private  void onOrderButtonClicked(){
        if(!UserState.getInstance().isAuthed()){
            new AlertDialog.Builder(this).setTitle(getString(R.string.dummy_alert_title))
                    .setMessage(getString(R.string.notice_login_needed))
                    .setNegativeButton(getString(R.string.bt_continue), null).show();
            return;
        }
        new AlertDialog.Builder(this).setTitle(getString(R.string.dummy_alert_title))
                .setMessage(getString(R.string.driver_order_msg))
                .setPositiveButton(getString(R.string.bt_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //TODO: make this async
                        DemoDataSource ds = new DemoDataSource(DriverADActivity.this);
                        ds.open();
                        if(UserState.getInstance().getUser() != null) {
                            ds.driverOrder((int) UserState.getInstance().getUser().getId(), Integer.parseInt(mAdId), "tail");
                        }
                        ds.close();
                        onOrderConfirmed();
                    }
                })
                .setNegativeButton(getString(R.string.bt_cancel), null).show();
    }

    private void onOrderConfirmed(){
        new AlertDialog.Builder(this).setTitle(getString(R.string.dummy_alert_title))
                .setMessage(String.format(getString(R.string.driver_order_notice), "2016-10-17"))
                .setNegativeButton(getString(R.string.bt_continue), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).show();
        //TODO send order to backend
    }

    private void initPage(){
        //TODO: init with real AD data later, currently init with dummy data
        //Ad ad = AdManager.getInstance().findById(mAdId);
        //new ImageLoadTask(ad.getLogoUri(), mLogo).execute();
        //new ImageLoadTask(ad.getAdUri(), mModle).execute();
        DemoDataSource ds = new DemoDataSource(this);
        ds.open();
        int ad_id = Integer.parseInt(mAdId);
        List<DemoAD> ads = ds.getAds(ad_id);
        ds.close();
        if(ads.size()>0) {
            ad = ads.get(0);
            mLogo.setImageResource(Util.getResourceId(ad.getLogo(), R.drawable.class));
            mModle.setImageResource(Util.getResourceId(ad.getModel(), R.drawable.class));
            mCompany.setText(ad.getCompany());
            mCarLeft.setText(String.valueOf(ad.getCars()-ad.getCarsNow()));
            mCars.setText(String.valueOf(ad.getCars()));
            mStartDate.setText(ad.getStartDate());
            mEndDate.setText(ad.getEndDate());

            if(ad.getStatus() != DemoAD.AD_STATUS_PENDING){
                //AD already running/finsihed, reset button action
                mButtonGo.setOnClickListener(null);
                mButtonGo.setText(R.string.text_ad_finished);
                mButtonGo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

                mAddrContainer.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case ACTIVITY_CHOOSE_ADDRESS:
            {
                if(resultCode == Activity.RESULT_OK)
                {
                    mInstallAddress.setText(data.getStringExtra("address"));
                }
                break;
            }
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
