package com.kk.kkxz.utils.locationInfo

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat


class GPSLocationUtils(context: Context) {
    private var mLocationHelper: LocationHelper? = null
    private var myLocationListener: MyLocationListener? = null
    private val mLocationManager: LocationManager?
    private val mContext: Context

    init {
        mContext = context
        mLocationManager = context
            .getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    companion object {
        @Volatile
        private var mGPSLocationUtils: GPSLocationUtils? = null
        fun getInstance(context: Context): GPSLocationUtils? {
            if (mGPSLocationUtils == null) {
                synchronized(GPSLocationUtils::class.java) {
                    if (mGPSLocationUtils == null) {
                        mGPSLocationUtils = GPSLocationUtils(context)
                    }
                }
            }
            return mGPSLocationUtils
        }
    }


    fun initLocationInfo(locationHelper: LocationHelper) {
        var location: Location? = null
        mLocationHelper = locationHelper
        if (myLocationListener == null) {
            myLocationListener = MyLocationListener()
        }
        // 需要检查权限,否则编译报错,想抽取成方法都不行,还是会报错。只能这样重复 code 了。
        if (Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        if (mLocationManager != null) {
            if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                location = mLocationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (location != null) {
                    locationHelper.updateLastLocation(location)
                }
                mLocationManager!!.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    0, 0f, myLocationListener!!
                )
            } else {
                location = mLocationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (location != null) {
                    locationHelper.updateLastLocation(location)
                }
                mLocationManager!!.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000, 50f, myLocationListener!!
                )
            }
        }
    }

    private inner class MyLocationListener : LocationListener {
        override fun onStatusChanged(
            provider: String, status: Int,
            extras: Bundle
        ) {
            Log.e("MoLin", "onStatusChanged!")
            if (mLocationHelper != null) {
                mLocationHelper!!.updateStatus(provider, status, extras)
            }
        }

        override fun onProviderEnabled(provider: String) {
            Log.e("MoLin", "onProviderEnabled!$provider")
        }

        override fun onProviderDisabled(provider: String) {
            Log.e("MoLin", "onProviderDisabled!$provider")
        }

        override fun onLocationChanged(location: Location) {
            Log.e("MoLin", "onLocationChanged!")
            if (mLocationHelper != null) {
                mLocationHelper!!.updateLocation(location)
            }
        }
    }


    fun removeLocationUpdatesListener() {
        if (Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mLocationManager!!.removeUpdates(myLocationListener!!)
    }




}