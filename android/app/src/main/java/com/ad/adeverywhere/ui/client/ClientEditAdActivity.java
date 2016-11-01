package com.ad.adeverywhere.ui.client;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.ad.adeverywhere.R;
import com.ad.adeverywhere.demo.DemoAD;
import com.ad.adeverywhere.demo.DemoDataSource;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ClientEditAdActivity extends AppCompatActivity {

    public static final String ACTION_TYPE = "type";
    public static final String TARGET_ADID = "ad_id";
    public static final int ACTION_TYPE_EDIT = 0;
    public static final int ACTION_TYPE_VIEW = 1;

    private Button mButtonGo = null;
    private EditText mDescription = null;
    private EditText mCars = null;
    private EditText mCarType = null;
    private EditText mCarCompany = null;
    private EditText mStartDate = null;
    private EditText mPeriod = null;
    private ImageButton mBtSelCarType = null;
    private ImageButton mBtSelStartDate = null;
    private ImageButton mBtSelPeriod = null;
    private DatePickerDialog mStartDatePickerDlg = null;
    private SimpleDateFormat dateFormatter;

    private int mActionType = 1;
    private int mAdId = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_edit_ad);
        Bundle b = getIntent().getExtras();
        if(b != null){
            mActionType = b.getInt(ACTION_TYPE);
            mAdId = b.getInt(TARGET_ADID);
        }

        ActionBar bar = getSupportActionBar();
        if(bar != null)
            bar.setDisplayHomeAsUpEnabled(true);

        mButtonGo = (Button) findViewById(R.id.bt_request_contact);
        mButtonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit();
            }
        });

        mCars = (EditText)findViewById(R.id.tx_cars);
        mDescription = (EditText)findViewById(R.id.tx_description);
        mCarType = (EditText)findViewById(R.id.tx_car_type);
        mCarCompany = (EditText)findViewById(R.id.tx_car_company);
        mStartDate = (EditText)findViewById(R.id.tx_start_date);
        mPeriod = (EditText)findViewById(R.id.tx_period);

        mBtSelCarType = (ImageButton)findViewById(R.id.btSelCarType);
        mBtSelStartDate = (ImageButton)findViewById(R.id.btSelStartDate);
        mBtSelPeriod = (ImageButton)findViewById(R.id.btSelPeriod);
        mBtSelCarType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogSelectCarType();
            }
        });
        mBtSelPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogSelectPeriod();
            }
        });
        setDateTimePicker();

        initData();
    }

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
    private void initData(){
        //get current AD
        DemoDataSource ds = new DemoDataSource(this);
        ds.open();
        List<DemoAD> ad = ds.getAds(mAdId);
        ds.close();
        if(ad != null && ad.size() == 1){
            mDescription.setText(ad.get(0).getCars());
            mCars.setText(String.valueOf(ad.get(0).getCars()));
            mStartDate.setText(ad.get(0).getStartDate());
            mPeriod.setText(String.format(getString(R.string.period_format), ad.get(0).getPeriod()));
        }

        if(mActionType == ACTION_TYPE_EDIT){
            setTitle(R.string.title_activity_edit_ad);
        }else{
            setTitle(R.string.title_activity_view_ad);
            mDescription.setEnabled(false); mDescription.setFocusable(false);
            mCars.setEnabled(false); mCars.setFocusable(false);
            mCarCompany.setEnabled(false); mCarCompany.setFocusable(false);
            mCarType.setEnabled(false); mCarType.setFocusable(false);
            mStartDate.setEnabled(false); mStartDate.setFocusable(false);
            mPeriod.setEnabled(false); mPeriod.setFocusable(false);
            mBtSelCarType.setVisibility(View.GONE);
            mBtSelPeriod.setVisibility(View.GONE);
            mBtSelStartDate.setVisibility(View.GONE);
            mButtonGo.setText(R.string.common_return);
        }
    }

    private void setDateTimePicker(){
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        mBtSelStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartDatePickerDlg.show();
            }
        });
        Calendar cal = Calendar.getInstance();
        mStartDatePickerDlg = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mStartDate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
    }

    private void showDialogSelectCarType() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Add Photo!");
        builder.setItems(R.array.client_car_types, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                ListView lv = ((AlertDialog)dialog).getListView();
                String tx = lv.getItemAtPosition(item).toString();
                mCarType.setText(tx);
            }
        });
        AlertDialog dialog = builder.create();
        ListView listView = dialog.getListView();
        dialog.show();
    }

    private void showDialogSelectPeriod() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Add Photo!");
        builder.setItems(R.array.client_ad_period, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                ListView lv = ((AlertDialog)dialog).getListView();
                String tx = lv.getItemAtPosition(item).toString();
                mPeriod.setText(tx);
            }
        });
        AlertDialog dialog = builder.create();
        ListView listView = dialog.getListView();
        dialog.show();
    }

    private void onSubmit(){
        if(mActionType == ACTION_TYPE_VIEW)
        {
            finish();
            return;
        }

        {
            //save DemoAd
        }
        new AlertDialog.Builder(this).setTitle(getString(R.string.dummy_alert_title))
                .setMessage(getString(R.string.client_request_sended_msg))
                .setNegativeButton(getString(R.string.bt_continue),  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dialog.dismiss();
                        finish();
                    }
                }).show();
        //TODO send request to backend server
    }
}
