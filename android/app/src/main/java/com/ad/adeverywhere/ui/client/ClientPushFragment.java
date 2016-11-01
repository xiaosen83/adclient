package com.ad.adeverywhere.ui.client;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.ad.adeverywhere.R;
import com.ad.adeverywhere.dao.UserState;
import com.ad.adeverywhere.demo.DemoAD;
import com.ad.adeverywhere.demo.DemoDataSource;
import com.ad.adeverywhere.ui.common.LoginActivity;
import com.ad.adeverywhere.utils.Util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClientPushFragment extends Fragment {

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

    public ClientPushFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_client_push, container, false);
        mButtonGo = (Button) v.findViewById(R.id.bt_request_contact);
        mButtonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRequestContactClicked();
            }
        });

        mCars = (EditText)v.findViewById(R.id.tx_cars);
        mDescription = (EditText)v.findViewById(R.id.tx_description);
        mCarType = (EditText)v.findViewById(R.id.tx_car_type);
        mCarCompany = (EditText)v.findViewById(R.id.tx_car_company);
        mStartDate = (EditText)v.findViewById(R.id.tx_start_date);
        mPeriod = (EditText)v.findViewById(R.id.tx_period);

        mBtSelCarType = (ImageButton)v.findViewById(R.id.btSelCarType);
        mBtSelStartDate = (ImageButton)v.findViewById(R.id.btSelStartDate);
        mBtSelPeriod = (ImageButton)v.findViewById(R.id.btSelPeriod);
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
        return v;
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
        mStartDatePickerDlg = new DatePickerDialog(this.getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mStartDate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
    }

    private void showDialogSelectCarType() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
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

    private void onRequestContactClicked(){
        //check if user login
        if(!UserState.getInstance().isAuthed()){
            //not authenticated, show login page
            Intent i = new Intent(getActivity(), LoginActivity.class);
            startActivity(i);
            return;
        }

        {
            DemoAD ad = new DemoAD();
            ad.setCompany(mDescription.getText().toString());
            ad.setCars(Integer.parseInt(mCars.getText().toString()));
            ad.setStartDate(mStartDate.getText().toString());
            //set end date: computed from startdate and period in month
            long start = Util.calStringToLong(mStartDate.getText().toString());
            start += (TimeUnit.DAYS.toMillis(Util.getDaysOfPeriod(getActivity(), mPeriod.getText().toString())));
            ad.setEndDate(Util.calLongToString(start));
            ad.setLogo("");ad.setModel("");
            ad.setUserId(UserState.getInstance().getUser().getId());
            DemoDataSource ds = new DemoDataSource(getActivity());
            ds.open();
            ds.createAD(ad);
            ds.close();
        }

        new AlertDialog.Builder(getActivity()).setTitle(getString(R.string.dummy_alert_title))
                .setMessage(getString(R.string.client_request_sended_msg))
                .setNegativeButton(getString(R.string.bt_continue), null).show();
        //TODO send request to backend server
    }
}
