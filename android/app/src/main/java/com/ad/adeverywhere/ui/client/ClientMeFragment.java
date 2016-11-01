package com.ad.adeverywhere.ui.client;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ad.adeverywhere.R;
import com.ad.adeverywhere.dao.LoginState;
import com.ad.adeverywhere.dao.UserState;
import com.ad.adeverywhere.demo.DemoAD;
import com.ad.adeverywhere.demo.DemoAdsListAdapter;
import com.ad.adeverywhere.demo.DemoPersistData;
import com.ad.adeverywhere.demo.DemoUserCert;
import com.ad.adeverywhere.ui.MainActivity;
import com.ad.adeverywhere.ui.PortalActivity;
import com.ad.adeverywhere.ui.common.AboutUsActivity;
import com.ad.adeverywhere.ui.common.CertificateActivity;
import com.ad.adeverywhere.ui.common.LoginActivity;
import com.ad.adeverywhere.ui.common.MeListAdapter;
import com.ad.adeverywhere.ui.common.UnfinishedActivity;
import com.ad.adeverywhere.ui.common.UserInfoActivity;
import com.ad.adeverywhere.ui.vendor.VendorMainActivity;
import com.ad.adeverywhere.utils.Logger;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClientMeFragment extends Fragment {
    public static final String TAG = "DriverMeFragment";
    public static final Logger logger = Logger.getInstance();

    public static final int ACTIVITY_REQ_LOGIN = 100;
    public static final int ACTIVITY_REQ_USERINFO = 101;
    public static final int ACTIVITY_REQ_MYAD = 102;
    public static final int ACTIVITY_REQ_MYCAR = 103;
    public static final int ACTIVITY_REQ_MYCOUPON = 104;
    public static final int ACTIVITY_REQ_CERT = 105;
    public static final int ACTIVITY_REQ_HELP = 106;
    public static final int ACTIVITY_REQ_SETTINGS = 107;

    private ListView mListView = null;
    RelativeLayout mRlUserinfo = null;
    private ImageButton mBtUserInfo = null;
    private TextView mTvUserStatus = null;
    private LinearLayout mMyAD = null, mMyCar = null, mMyCoupon = null;
    private Button mBtLogout = null;
    private TableRow mRowCert = null, mRowHelp = null, mRowSettings = null, mRowBank = null;
    private TextView mCertHint = null;

    private Activity context = null;

    public ClientMeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_client_me, container, false);

        context = this.getActivity();

        mRlUserinfo = (RelativeLayout)v.findViewById(R.id.rlUserInfo);
        mBtUserInfo = (ImageButton)v.findViewById(R.id.btUserInfo);
        mTvUserStatus = (TextView)v.findViewById(R.id.tvUserStatus);
        mMyAD = (LinearLayout)v.findViewById(R.id.btMyAd);
        mMyCoupon = (LinearLayout)v.findViewById(R.id.btMyCoupon);
        mBtLogout = (Button)v.findViewById(R.id.btLogout);

        mCertHint = (TextView)v.findViewById(R.id.me_cert_status);
        mRowCert = (TableRow)v.findViewById(R.id.rowCert);
        mRowHelp = (TableRow)v.findViewById(R.id.rowHelp);
        mRowSettings = (TableRow)v.findViewById(R.id.rowSettings);
        mRowBank = (TableRow)v.findViewById(R.id.rowBank);

        initEventHandler();

        checkLoginStatus();

        new LoadUserCertificateTask().execute();

        return v;
    }
    private void initEventHandler() {
        mRlUserinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserState.getInstance().isAuthed()){
                    //authenticated, show user info
                    Intent i = new Intent(getActivity(), UserInfoActivity.class);
                    startActivity(i);
                } else {
                    //not authenticated, show login page
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(i, ACTIVITY_REQ_LOGIN);
                }
            }
        });
        mMyAD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!UserState.getInstance().isAuthed()){
                    //not authenticated, show login page
                    Intent i = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(i, ACTIVITY_REQ_LOGIN);
                }
                else{
                    Intent i = new Intent(getActivity(), ClientMyAdsActivity.class);
                    startActivity(i);
                }
            }
        });
        mMyCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), UnfinishedActivity.class);
                i.putExtra(UnfinishedActivity.TODO_TYPE, UnfinishedActivity.TODO_TYPE_COUPON);
                startActivityForResult(i, ACTIVITY_REQ_SETTINGS);
            }
        });

        mBtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });


        mRowCert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CertificateActivity.class);
                startActivityForResult(i, ACTIVITY_REQ_CERT);
            }
        });
        mRowHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AboutUsActivity.class);
                startActivityForResult(i, ACTIVITY_REQ_HELP);
            }
        });
        mRowSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), UnfinishedActivity.class);
                i.putExtra(UnfinishedActivity.TODO_TYPE, UnfinishedActivity.TODO_TYPE_SETTINGS);
                startActivityForResult(i, ACTIVITY_REQ_SETTINGS);
            }
        });
        mRowBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), UnfinishedActivity.class);
                i.putExtra(UnfinishedActivity.TODO_TYPE, UnfinishedActivity.TODO_TYPE_BANK);
                startActivityForResult(i, ACTIVITY_REQ_SETTINGS);
            }
        });
    }

    private class LoadUserCertificateTask extends AsyncTask<Void, Void, DemoUserCert> {
        @Override
        protected DemoUserCert doInBackground(Void... params) {
            return UserState.getInstance().getUserCert(context, false);
        }

        @Override
        protected void onPostExecute(DemoUserCert userCert) {
            if(userCert != null)
                mCertHint.setText(R.string.me_certed);
            else
                mCertHint.setText(R.string.me_not_certed);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case ACTIVITY_REQ_LOGIN:
            {
                if(resultCode == getActivity().RESULT_OK) {
                    onLoginSucceed();
                    int role = data.getIntExtra("ROLE",UserState.getInstance().getRole());
                    int role_now = UserState.getInstance().getRole();
                    UserState.getInstance().setRole(role, true);
                    if(role != role_now)
                        onRoleChanged(role);
                }
                break;
            }
            case ACTIVITY_REQ_CERT:
            {
                if(UserState.getInstance().getUserCert(getActivity(), true) != null)
                    mCertHint.setText(R.string.me_certed);
                else
                    mCertHint.setText(R.string.me_not_certed);
            }
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void onLoginSucceed(){
        mTvUserStatus.setText(UserState.getInstance().getUsername());
        mBtLogout.setVisibility(View.VISIBLE);
    }

    private void onRoleChanged(int role){
        if(UserState.getInstance().getRole() == UserState.USER_ROLE_VENDOR){
            Intent i = new Intent(getActivity(), VendorMainActivity.class);
            startActivity(i);
            getActivity().finish();
        }else {
            Intent i = new Intent(getActivity(), MainActivity.class);
            startActivity(i);
            getActivity().finish();
        }
    }

    private void logoutUser(){
        //TODO: logout in an async thread
        UserState.getInstance().didLogout();
        onLogoutSucceed();
    }

    private void onLogoutSucceed(){
        mTvUserStatus.setText(R.string.me_not_login_text);
        mBtLogout.setVisibility(View.GONE);
    }

    private void showDummyTODODlg(){
        //authenticated, show user info
        new AlertDialog.Builder(getActivity()).setTitle(getString(R.string.dummy_alert_title))
                .setMessage(getString(R.string.dummy_alert_msg))
                .setNegativeButton(getString(R.string.bt_cancel), null).show();
    }

    private void checkLoginStatus(){
        if(UserState.getInstance().isAuthed()){
            onLoginSucceed();
        }
        else{
            mTvUserStatus.setText(R.string.me_not_login_text);
            mBtLogout.setVisibility(View.GONE);
        }
    }
}
