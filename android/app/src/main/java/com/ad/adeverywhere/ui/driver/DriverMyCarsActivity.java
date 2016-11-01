package com.ad.adeverywhere.ui.driver;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.ad.adeverywhere.R;
import com.ad.adeverywhere.dao.UserState;
import com.ad.adeverywhere.demo.DemoAD;
import com.ad.adeverywhere.demo.DemoAdsListAdapter;
import com.ad.adeverywhere.demo.DemoCar;
import com.ad.adeverywhere.demo.DemoDataSource;
import com.ad.adeverywhere.ui.common.DriverCarsListAdaper;

import java.util.List;

public class DriverMyCarsActivity extends AppCompatActivity {
    public static final String CAR_ACTION = "action";
    public static final int CAR_ACTION_ADD = 0;
    public static final int CAR_ACTION_EDIT = 1;
    public static final int ACTIVITY_REQ_CAR_ADD = 120;

    private Button btSubmit = null;
    private ListView mCarListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_my_cars);
        mCarListView = (ListView)findViewById(R.id.listView);
        new LoadCarsList().execute();

        btSubmit = (Button)findViewById(R.id.btSubmit);
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DriverMyCarsActivity.this, DriverEditCarActivity.class);
                i.putExtra(CAR_ACTION, CAR_ACTION_ADD);
                startActivityForResult(i, ACTIVITY_REQ_CAR_ADD);
            }
        });

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

    public void refreshListView(){
        mCarListView.deferNotifyDataSetChanged();
    }

    private class LoadCarsList extends AsyncTask<Void, Void, List<DemoCar>> {
        @Override
        protected List<DemoCar> doInBackground(Void... params) {
            List<DemoCar> cars = null;
            if(UserState.getInstance().getUser()!=null)
                if(UserState.getInstance().getUser().getId()>0) {
                    DemoDataSource ds = new DemoDataSource(DriverMyCarsActivity.this);
                    ds.open();
                    cars = ds.getCarsOfUser((int) UserState.getInstance().getUser().getId());
                    ds.close();
                }
            return cars;
        }

        @Override
        protected void onPostExecute(List<DemoCar> ads) {
            DriverCarsListAdaper adapter1 = new DriverCarsListAdaper(DriverMyCarsActivity.this, ads);
            mCarListView.setAdapter(adapter1);
            mCarListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //TODO
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ACTIVITY_REQ_CAR_ADD:
                //refresh listview
                mCarListView.setAdapter(null);
                new LoadCarsList().execute();
                break;
            default:
                break;
        }
    }
}
