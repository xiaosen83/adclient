package com.ad.adeverywhere.ui.common;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import com.ad.adeverywhere.R;
import com.ad.adeverywhere.dao.UserState;
import com.ad.adeverywhere.demo.DemoDataSource;
import com.ad.adeverywhere.demo.DemoUserCert;

public class UserInfoBindActivity extends AppCompatActivity {

    public static final int BIND_TYPE_ADDRESS = 0;
    public static final int BIND_TYPE_PHONE =  1;
    public static final int BIND_TYPE_EMAIL = 2;
    public static final String BIND_TYPE = "bindtype";
    public static final String BIND_INFO = "bindinfo";

    private int mBindType = 0;

    private LinearLayout layoutAddr = null;
    private TableLayout layoutPhone = null;
    private LinearLayout layoutEmail = null;
    private Button btSubmit = null;
    private EditText txAddr = null, txPhone = null, txPhonePin = null, txEmail = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_bind);

        Bundle b = getIntent().getExtras();
        if(b != null)
            mBindType = b.getInt(BIND_TYPE);

        layoutAddr = (LinearLayout)findViewById(R.id.layoutAddr);
        layoutPhone = (TableLayout)findViewById(R.id.layoutPhone);
        layoutEmail = (LinearLayout)findViewById(R.id.layoutEmail);
        btSubmit = (Button)findViewById(R.id.btSubmit);
        txAddr = (EditText)findViewById(R.id.txAddr);
        txPhone = (EditText)findViewById(R.id.txPhone);
        txPhonePin = (EditText)findViewById(R.id.txPhonePin);
        txEmail = (EditText)findViewById(R.id.txEmail);

        setTitle(R.string.title_activity_certificate);
        ActionBar bar = getSupportActionBar();
        if(bar != null)
            bar.setDisplayHomeAsUpEnabled(true);

        initLayout();

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit();
            }
        });
    }
    private void initLayout(){
        switch (mBindType){
            case BIND_TYPE_PHONE:
            {
                setTitle(R.string.bind_title_phone);
                layoutAddr.setVisibility(View.GONE);
                layoutEmail.setVisibility(View.GONE);
                layoutPhone.setVisibility(View.VISIBLE);
                break;
            }
            case BIND_TYPE_EMAIL:
            {
                setTitle(R.string.bind_title_email);
                layoutAddr.setVisibility(View.GONE);
                layoutEmail.setVisibility(View.VISIBLE);
                layoutPhone.setVisibility(View.GONE);
                break;
            }
            default:
            {
                setTitle(R.string.bind_title_address);
                layoutAddr.setVisibility(View.VISIBLE);
                layoutEmail.setVisibility(View.GONE);
                layoutPhone.setVisibility(View.GONE);
                break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onSubmit(){
        String bindinfo = "";
        switch (mBindType){
            case BIND_TYPE_ADDRESS:
                bindinfo = txAddr.getText().toString();
                break;
            case BIND_TYPE_PHONE:
                bindinfo = txPhone.getText().toString();
                break;
            case BIND_TYPE_EMAIL:
                bindinfo = txEmail.getText().toString();
                break;
            default:
                break;
        }
        new BindInfoTask(this).execute(bindinfo);
    }

    private class BindInfoTask extends AsyncTask<String, Void, String> {
        private Context context = null;
        public BindInfoTask(Context context){
            this.context = context;
        }
        @Override
        protected String doInBackground(String... params) {
            String info = params[0];
            DemoDataSource ds = new DemoDataSource(UserInfoBindActivity.this);
            ds.open();
            ds.updateUserInfo(UserState.getInstance().getUserId(context), mBindType, info);
            if(mBindType == BIND_TYPE_ADDRESS)
                UserState.getInstance().getUser().setAddr(info);
            else if(mBindType == BIND_TYPE_PHONE)
                UserState.getInstance().getUser().setPhone(info);
            else if(mBindType == BIND_TYPE_EMAIL)
                UserState.getInstance().getUser().setEmail(info);
            ds.close();
            return info;
        }

        @Override
        protected void onPostExecute(String info) {
            super.onPostExecute(info);
            Intent i = new Intent();
            i.putExtra(BIND_INFO, info);
            setResult(RESULT_OK,  i);
            finish();
        }
    }
}
