package com.example.dummynetwork;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.List;

public class Originalcode extends AppCompatActivity {

    private static final String LOG_TAG = "AndroidExample";
    private static final int MY_REQUEST_CODE = 123;
    private WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        askAndStartScanWifi();
    }

    private void askAndStartScanWifi() {

        // With Android Level >= 23, you have to ask the user
        // for permission to Call.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) { // 23
            int permission1 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            // Check for permissions
            if (permission1 != PackageManager.PERMISSION_GRANTED) {
                Log.d(LOG_TAG, "Requesting Permissions");
                // Request permissions
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_WIFI_STATE,
                                Manifest.permission.ACCESS_NETWORK_STATE
                        }, MY_REQUEST_CODE);
                return;
            }
            Log.d(LOG_TAG, "Permissions Already Granted");
        }
        this.doStartScanWifi(this);
    }

    private void doStartScanWifi(Context context) {
        this.wifiManager.startScan();
        this.wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String ssid = wifiInfo.getSSID();
        Log.d("-----------------", ssid);
        showNetworksDetails(ssid);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    Log.d(LOG_TAG, "Permission Granted: " + permissions[0]);
                    // Start Scan Wifi.
                    this.doStartScanWifi(this);
                } else {
                    Log.d(LOG_TAG, "Permission Denied: " + permissions[0]);
                }
                break;
            }
        }
    }

    private String showNetworksDetails(String value) {

        String value1 =value.replace("\"","");
        String NetworkType = "";
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return value1;
        }
        List<ScanResult> list = wifiManager.getScanResults();
        for (int i = 0; i < list.size(); i++){
            if (value1.equals(list.get(i).SSID)) {
//                Log.d("The SSID id :", wifiManager.getScanResults().get(i).SSID);
                Toast.makeText(Originalcode.this, "SSID:" + list.get(i).SSID, Toast.LENGTH_SHORT).show();
//                Log.d("The network Type is : ", wifiManager.getScanResults().get(i).capabilities);
                NetworkType = list.get(i).capabilities;
//                Log.d("The network Type is : ", NetworkType);
                Toast.makeText(Originalcode.this, "SSID:" + list.get(i).capabilities, Toast.LENGTH_SHORT).show();
            }
        }
        Log.d("The network Type is : ", NetworkType);
        return NetworkType;
    }
}