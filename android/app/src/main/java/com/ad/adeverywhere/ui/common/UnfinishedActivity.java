package com.ad.adeverywhere.ui.common;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.ad.adeverywhere.R;

public class UnfinishedActivity extends AppCompatActivity {

    public static final String TODO_TYPE = "todo";
    public static final int TODO_TYPE_COUPON = 1;
    public static final int TODO_TYPE_BANK = 2;
    public static final int TODO_TYPE_SETTINGS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Bundle b = getIntent().getExtras();
        if(b != null){
            int type = b.getInt(TODO_TYPE);
            switch (type){
                case  TODO_TYPE_BANK:
                    setTitle(R.string.title_activity_bank);
                    break;
                case TODO_TYPE_COUPON:
                    setTitle(R.string.title_activity_coupon);
                    break;
                case TODO_TYPE_SETTINGS:
                    setTitle(R.string.title_activity_settings);
                    break;
                default:
                    setTitle(R.string.title_activity_about_us);
                    break;
            }
        }
        else
            setTitle(R.string.title_activity_about_us);
        ActionBar bar = getSupportActionBar();
        if(bar != null)
            bar.setDisplayHomeAsUpEnabled(true);
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
}
