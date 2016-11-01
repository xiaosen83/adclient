package com.ad.adeverywhere.ui.driver;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ad.adeverywhere.R;
import com.ad.adeverywhere.dao.UserState;
import com.ad.adeverywhere.demo.DemoCar;
import com.ad.adeverywhere.demo.DemoDataSource;
import com.ad.adeverywhere.demo.DemoUserCert;

public class DriverEditCarActivity extends AppCompatActivity {

    private EditText txPlate = null, txBrand = null, txModel = null;
    private Button btSubmit = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_edit_car);

        ActionBar bar = getSupportActionBar();
        if(bar != null)
            bar.setDisplayHomeAsUpEnabled(true);

        txPlate = (EditText)findViewById(R.id.txPlate);
        txBrand = (EditText)findViewById(R.id.txBrand);
        txModel = (EditText)findViewById(R.id.txModel);
        btSubmit = (Button)findViewById(R.id.btSubmit);

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmit();
            }
        });
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

    private void onSubmit(){
        DemoCar car = new DemoCar();
        car.setModel(txModel.getText().toString());
        car.setBrand(txBrand.getText().toString());
        car.setPlate(txPlate.getText().toString());
        car.setUserId(UserState.getInstance().getUser().getId());
        if(car.getPlate().length() == 0 || car.getBrand().length()==0 || car.getModel().length()==0){
            new AlertDialog.Builder(this).setTitle("Error")
                    .setMessage("Filed can't empty!")
                    .setNegativeButton(getString(R.string.bt_cancel), null).show();
        }
        else
        {
            new LoadUserCertificateTask().execute(car);
        }
    }

    private class LoadUserCertificateTask extends AsyncTask<DemoCar, Void, Boolean> {
        @Override
        protected Boolean doInBackground(DemoCar... params) {
            DemoCar car = params[0];
            DemoDataSource ds = new DemoDataSource(DriverEditCarActivity.this);
            ds.open();
            ds.saveCar(car);
            ds.close();
            return Boolean.TRUE;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            finish();
        }
    }
}
