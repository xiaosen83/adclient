package com.ad.adeverywhere.ui.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ad.adeverywhere.R;
import com.ad.adeverywhere.dao.UserState;
import com.ad.adeverywhere.demo.DemoUser;

public class UserInfoActivity extends AppCompatActivity {

    public static final int ACTIVITY_REQ_USERINFOBIND = 110;

    private int mBindType = -1;
    private TextView txAddr = null, txPhone = null, txEmail = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        setTitle(R.string.title_activity_user_info);
        ActionBar bar = getSupportActionBar();
        if(bar != null)
            bar.setDisplayHomeAsUpEnabled(true);

        txAddr = (TextView)findViewById(R.id.txAddr);
        txPhone = (TextView)findViewById(R.id.txPhone);
        txEmail = (TextView)findViewById(R.id.txEmail);

        initLayout();
    }

    private void initLayout(){
        DemoUser user = UserState.getInstance().getUser();
        if(user != null){
            if(user.getAddr() != null && user.getAddr().length()>0)
                txAddr.setText(user.getAddr());
            if(user.getPhone() != null && user.getPhone().length()>0)
                txPhone.setText(user.getPhone());
            if(user.getEmail() != null && user.getEmail().length()>0)
                txEmail.setText(user.getEmail());
        }
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

    public void onBindClicked(View view) {
        mBindType = UserInfoBindActivity.BIND_TYPE_ADDRESS;
        switch (view.getId()){
            case R.id.btAddr:
                mBindType = UserInfoBindActivity.BIND_TYPE_ADDRESS;
                break;
            case R.id.btPhone:
                mBindType = UserInfoBindActivity.BIND_TYPE_PHONE;
                break;
            case R.id.btEmail:
                mBindType = UserInfoBindActivity.BIND_TYPE_EMAIL;
                break;
        }
        Intent i = new Intent(UserInfoActivity.this, UserInfoBindActivity.class);
        i.putExtra(UserInfoBindActivity.BIND_TYPE, mBindType);
        startActivityForResult(i, ACTIVITY_REQ_USERINFOBIND);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case ACTIVITY_REQ_USERINFOBIND:
            {
                if(resultCode == Activity.RESULT_OK)
                {
                    String bindinfo = data.getStringExtra(UserInfoBindActivity.BIND_INFO);
                    if(mBindType == UserInfoBindActivity.BIND_TYPE_ADDRESS)
                        txAddr.setText(bindinfo);
                    else if(mBindType == UserInfoBindActivity.BIND_TYPE_PHONE)
                        txPhone.setText(bindinfo);
                    else if(mBindType == UserInfoBindActivity.BIND_TYPE_EMAIL)
                        txEmail.setText(bindinfo);
                }
                break;
            }
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}
