package com.example.dummynetwork

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {
    private var wifiManager: WifiManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        wifiManager = this.applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        locationEnabled()
    }

    override fun onResume() {
        super.onResume()
        // put your code here...
        location()
    }

    private fun location() {
        val lm = getSystemService(LOCATION_SERVICE) as LocationManager
        val gps_enabled: Boolean
        val network_enabled: Boolean
        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (gps_enabled && network_enabled) {
            askAndStartScanWifi()
        }
    }

    private fun locationEnabled() {
        val lm = getSystemService(LOCATION_SERVICE) as LocationManager
        var gps_enabled = false
        var network_enabled = false
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
            Log.d("-----------", "---------$gps_enabled")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (gps_enabled && network_enabled) {
            askAndStartScanWifi()
        } else {
            AlertDialog.Builder(this)
                .setMessage("Turn on your LOCATION services")
                .setPositiveButton(
                    "Settings"
                ) { paramDialogInterface, paramInt -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun askAndStartScanWifi() {
        // With Android Level >= 23, you have to ask the user
        // for permission to Call.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // 23
            val permission1 =
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            // Check for permissions
            if (permission1 != PackageManager.PERMISSION_GRANTED) {
                Log.d(LOG_TAG, "Requesting Permissions")
                // Request permissions
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.ACCESS_NETWORK_STATE
                    ), MY_REQUEST_CODE
                )
                return
            }
            Log.d(LOG_TAG, "Permissions Already Granted")
        }
        doStartScanWifi(this)
    }

    private fun doStartScanWifi(context: Context) {
        wifiManager = context.getSystemService(WIFI_SERVICE) as WifiManager
        val wifiManager = context.getSystemService(WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        val ssid = wifiInfo.ssid
        showNetworksDetails(ssid)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    Log.d(LOG_TAG, "Permission Granted: " + permissions[0])
                    // Start Scan Wifi.
                    doStartScanWifi(this)
                } else {
                    Log.d(LOG_TAG, "Permission Denied: " + permissions[0])
                }
            }
        }
    }

    private fun showNetworksDetails(value: String): String {
        val value1 = value.replace("\"", "")
//        Log.d("---------------", value1)
        var networkType = ""
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return value1
        }
        val list = wifiManager!!.scanResults
        for (i in list.indices) {
            if (value1.equals(list[i].SSID)) {
                Log.d("The SSID id :", wifiManager!!.getScanResults().get(i).SSID);
                Toast.makeText(this@MainActivity, "SSID:" + list[i].SSID, Toast.LENGTH_SHORT).show()
                Log.d("The network Type is : ", wifiManager!!.getScanResults().get(i).capabilities);
                networkType = list[i].capabilities
                //Log.d("The network Type is : ", NetworkType);
                Toast.makeText(this@MainActivity, "Network type is:" + list[i].capabilities, Toast.LENGTH_SHORT).show()
            }
        }
        return networkType
    }

    companion object {
        private const val LOG_TAG = "AndroidExample"
        private const val MY_REQUEST_CODE = 123
    }
}