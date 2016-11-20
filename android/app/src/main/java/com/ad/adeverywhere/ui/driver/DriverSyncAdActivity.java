package com.ad.adeverywhere.ui.driver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ad.adeverywhere.R;
import com.ad.adeverywhere.utils.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class DriverSyncAdActivity extends AppCompatActivity {
    public static final String TAG = "DriverSyncAdActivity";
    public static final Logger logger = Logger.getInstance();

    public static final UUID MY_UUID = UUID.fromString("0000FFF0-0000-1000-8000-00805F9B34FB");
    private static final int REQUEST_ENABLE_BT = 100;

    Button mBtConnectDevice = null;
    Button mBtSync = null;
    TextView mTxDeviceInfo = null;

    BluetoothAdapter mBluetoothAdapter = null;
    BluetoothDevice mBluetoothDevice = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_sync_ad);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mBtConnectDevice = (Button)findViewById(R.id.btConnectDevice);
        mBtSync = (Button)findViewById(R.id.btSync);
        mTxDeviceInfo = (TextView)findViewById(R.id.txDeviceInfo);

        initEventHandler();
    }

    private void initEventHandler(){
        mBtConnectDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter == null) {
                    mTxDeviceInfo.setText(R.string.bt_device_not_support);
                }
                else{
                    //start BlueTooth
                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    }
                }
            }
        });
        mBtSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadAds();
            }
        });
    }

    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            //mViewPager.setCurrentItem(msg.what);
            super.handleMessage(msg);
        }

    };

    private void downloadAds(){

        onAdsDownloaded();
    }

    private void onAdsDownloaded(){
         //start connect thread
        Thread connectThread = new ConnectThread(mBluetoothDevice);
        connectThread.start();
    }

    private void onSocketConnected(BluetoothSocket mmSocket){
        //start upload thread
        Thread uploadThread = new UploadThread(mmSocket);
        uploadThread.start();
    }

    private class UploadThread extends Thread {
        private final BluetoothSocket mmSocket;

        public UploadThread(BluetoothSocket socket) {
            mmSocket = socket;
        }

        public void run() {

        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI activity
                   // mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) { }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            mBluetoothAdapter.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                return;
            }

            // Do work to manage the connection (in a separate thread)
            onSocketConnected(mmSocket);
        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    private void onBluetoothEnabled(){
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                mTxDeviceInfo.setText(String.format(getString(R.string.bt_paired_device_format), device.getName()));
                mBluetoothDevice = device;
                break;
            }
            onBluetoothDeviceFound();
        }
        else{
            startDiscovery();
        }
    }

    private void onBluetoothDeviceFound(){
        stopDiscovery();

    }

    private  void startDiscovery(){
        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy

        mBluetoothAdapter.startDiscovery();
    }

    private void stopDiscovery(){
        unregisterReceiver(mReceiver);
        mBluetoothAdapter.cancelDiscovery();
    }

    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                mTxDeviceInfo.setText(String.format(getString(R.string.bt_paired_device_format), device.getName()));
                mBluetoothDevice = device;
                onBluetoothDeviceFound();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_ENABLE_BT:
                if(resultCode == REQUEST_ENABLE_BT)
                    onBluetoothEnabled();
                else
                    mTxDeviceInfo.setText(R.string.bt_not_enabled);
                break;
            default:
                break;
        }
    }

}
