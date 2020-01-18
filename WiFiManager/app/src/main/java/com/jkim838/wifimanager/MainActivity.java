package com.jkim838.wifimanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.style.TtsSpan;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // THESE ARE GLOBAL TO THIS CLASS
    // Generate Wifi Manager object -- need a context and a casting
    private List<ScanResult> wifiScanResult = new ArrayList<>();
    private WifiManager wifi;
    private TextView scanResult;
    private BroadcastReceiver scanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // from an intent, get an extra data of type Boolean --- if not present, default to false
            // this broadcast receiver probably receives an intent with success/fail callback?
            Log.i("INTENT", "intent filter");
            boolean rxSuccess = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
            if(rxSuccess)
            {
                // scanning was successful
                Log.i("SCAN", "scan was successful");
                scanSuccess();
            }
            else
            {
                // scanning was unsuccessful
                Log.w("SCAN", "scan was unsuccessful");
                scanFailed(); // call handler for unsuccessful scanner
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scanResult = findViewById(R.id.WiFiList);
        checkPermission(this);
        // Populate WifiManager
        wifi = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        // intent filter --- needs to be inside an activity?
        IntentFilter scanOutcome = new IntentFilter();
        scanOutcome.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION); // add action
        registerReceiver(scanReceiver, scanOutcome); // listen to the Broadcast receiver, and subscribe (check for) intentFilter
        Log.i("SCAN", "Starting scan");
        if(!wifi.startScan())
        {
            // scan failed
            scanFailed();
        }
    }

    // HELPER FUNCTIONS
    private void checkPermission(AppCompatActivity thisActivity)
    {
        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.ACCESS_WIFI_STATE);
        permissions.add(Manifest.permission.CHANGE_WIFI_STATE);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        for(String thisPermission:permissions)
        {
            switch(ContextCompat.checkSelfPermission(thisActivity, thisPermission))
            {
                case PackageManager.PERMISSION_GRANTED:
                    Log.w("PERMISSION", thisPermission.toString() + ": permission Granted");
                    break;
                case PackageManager.PERMISSION_DENIED:
                    Log.w("PERMISSION", "Needs Permission: " + thisPermission.toString());
                    // List of permissions to ask
                    String[] permissionRequired = {Manifest.permission.ACCESS_WIFI_STATE,
                                                   Manifest.permission.CHANGE_WIFI_STATE,
                                                   Manifest.permission.ACCESS_COARSE_LOCATION};
                    // Android will automatically only ask permissions that are not granted
                    ActivityCompat.requestPermissions(thisActivity, permissionRequired, 1); // requires all permissions
                    break;
            }
        }
    }
    private void scanSuccess()
    {
        Log.i("RESULT", "DISPLAYING SSIDs");
        if(!wifi.getScanResults().isEmpty())
        {
            // we have scan result
            Log.i("SCAN", "Scan result available");
            wifiScanResult = wifi.getScanResults();
            final String result = buildScanResult(wifiScanResult);
            AppCompatActivity activity = (AppCompatActivity) this;
            // update display
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    scanResult.setText(result);
                }
            });

        }
        else
        {
            scanResult.setText("NO RESULTS");
        }

    }
    private void scanFailed()
    {
        // handler for failure
        // use previous scan results
    }
    private String buildScanResult(List<ScanResult> results)
    {
        int SSIDCount = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for(ScanResult thisResult:wifi.getScanResults())
        {
            stringBuilder.append(SSIDCount).append(") ");
            stringBuilder.append(thisResult.SSID).append("\n");
            SSIDCount++;
        }
        return stringBuilder.toString();
    }
}
