package com.ad.adeverywhere.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ad.adeverywhere.R;
import com.ad.adeverywhere.dao.UserState;
import com.ad.adeverywhere.ui.vendor.VendorMainActivity;

public class PortalActivity extends AppCompatActivity {

    private Button btDriverEntry;
    private Button btClientEntry;
    private Button btVendorEntry;
    private TextView tvJoinedDrivers;
    private TextView tvJoinedClients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portal);

        btDriverEntry = (Button)findViewById(R.id.btDriverEntry);
        btClientEntry = (Button)findViewById(R.id.btClientEntry);
        btVendorEntry = (Button)findViewById(R.id.btVendorEntry);
        tvJoinedDrivers = (TextView)findViewById(R.id.portal_join_drivers);
        tvJoinedClients = (TextView)findViewById(R.id.portal_join_clients);
        btDriverEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserState.getInstance().setRole(UserState.USER_ROLE_DRIVER, true);
                startMainActivity(MainActivity.UI_PORTAL_DRIVER);
            }
        });
        btClientEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserState.getInstance().setRole(UserState.USER_ROLE_CLIENT, true);
                startMainActivity(MainActivity.UI_PORTAL_CLIENT);
            }
        });
        btVendorEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserState.getInstance().setRole(UserState.USER_ROLE_VENDOR, true);
                startMainActivity(MainActivity.UI_PORTAL_VENDOR);
            }
        });
        //TODO
        tvJoinedDrivers.setText(String.format(getString(R.string.portal_join_drivers), 12345));
        tvJoinedClients.setText(String.format(getString(R.string.portal_join_clients), 567));
    }

    private void startMainActivity(int mainActivityType){
        if(mainActivityType == MainActivity.UI_PORTAL_VENDOR){
            Intent i = new Intent(PortalActivity.this, VendorMainActivity.class);
            startActivity(i);
            finish();
        }else {
            Intent i = new Intent(PortalActivity.this, MainActivity.class);
            i.putExtra("portaltype", mainActivityType);
            startActivity(i);
            finish();
        }
    }
}
