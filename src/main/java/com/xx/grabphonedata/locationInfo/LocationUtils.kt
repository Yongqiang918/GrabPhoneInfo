package com.kk.kkxz.utils.locationInfo

import android.content.Context
import android.location.Geocoder
import android.location.GpsStatus
import android.location.Location
import android.os.Bundle
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.xx.grabphonedata.bean.LocationBean
import java.io.IOException


class LocationUtils {
    private var mLocationClient: LocationClient? = null
    private val myLocationListener =
        MyLocationListener()

    private var isGetLocationSuccess = false
    private var mContext: Context? = null

    companion object {
        private var mLocationUtils: LocationUtils? = null

        @get:Synchronized
        val instance: LocationUtils?
            get() {
                if (mLocationUtils == null) {
                    mLocationUtils = LocationUtils()
                }
                return mLocationUtils
            }
    }

    //先调setContent，然后initLocationClient
    fun setContent(contexts: Context): LocationUtils {
        mContext = contexts
        return this
    }


    fun initLocationClient(): LocationUtils {
        mLocationClient = LocationClient(mContext)
        mLocationClient!!.registerLocationListener(myLocationListener)
        val locationClientOption = LocationClientOption()
        locationClientOption.locationMode = LocationClientOption.LocationMode.Hight_Accuracy
        locationClientOption.setIsNeedAddress(true)
        locationClientOption.isOpenGps = true
        locationClientOption.setIgnoreKillProcess(true)
        locationClientOption.SetIgnoreCacheException(false)
        locationClientOption.setWifiCacheTimeOut(5 * 60 * 1000)
        mLocationClient!!.locOption = locationClientOption
        return this
    }

    fun start() {
        if (mLocationClient != null) {
            mLocationClient!!.start()
            //            initLocation();
        }
    }

    fun reStart() {
        if (mLocationClient != null) {
            mLocationClient!!.restart()
        }
    }

    fun stop() {
        if (mLocationClient != null) {
            mLocationClient!!.stop()
        }
    }


    inner class MyLocationListener : BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation) {
            val locationWhere = location.locationWhere
            val latitude = location.latitude
            val longitude = location.longitude
            val radius = location.radius
            val coorType = location.coorType
            val errorCode = location.locType
            val addr = location.addrStr
            val country = location.country
            val province = location.province
            val city = location.city
            val district = location.district
            val street = location.street
            val locType = location.locType
            if (locType == 61 || locType == 161) {
                val locationBean = LocationBean()
                locationBean.latitude = latitude
                locationBean.longitude = longitude
                locationBean.coorType = coorType
                locationBean.errorCode = errorCode
                locationBean.radius = radius
                locationBean.addr = addr
                locationBean.country = country
                locationBean.province = province
                locationBean.city = city
                locationBean.district = district
                locationBean.street = street
                locationBean.locationWhere = locationWhere
                locationBean.locType = locType
                if (mLocationCallbackListener != null) {
                    mLocationCallbackListener!!.updataLocation(locationBean)
                }
            } else {
                initLocation()
            }
            if (mLocationClient != null) {
                mLocationClient!!.stop()
            }
        }

        override fun onLocDiagnosticMessage(i: Int, i1: Int, s: String) {
            super.onLocDiagnosticMessage(i, i1, s)
            initLocation()
        }
    }

    fun initLocation() {
        GPSLocationUtils.getInstance(mContext!!)!!.initLocationInfo(object : LocationHelper {
            override fun updateLocation(location: Location) {
                GPSLocationUtils.getInstance(mContext!!)!!.removeLocationUpdatesListener()
            }

            override fun updateStatus(
                provider: String,
                status: Int,
                extras: Bundle
            ) {
            }

            override fun updateGPSStatus(status: GpsStatus) {}
            override fun updateLastLocation(location: Location) {
                locationhandle(location.latitude, location.longitude)
            }
        })
    }

    fun locationhandle(latitude: Double, longitude: Double) {
        val locationBean = LocationBean()
        locationBean.latitude = latitude
        locationBean.longitude = longitude
        val geocoder = Geocoder(
            mContext
        )
        val flag = Geocoder.isPresent()
        if (flag) {
            try {
                val addresses =
                    geocoder.getFromLocation(latitude, longitude, 1)
                if (addresses.size > 0) {
                    val address = addresses[0]
                    if (address != null) {
                        val countryName = address.countryName //
                        val countryCode = address.countryCode
                        val adminArea = address.adminArea //
                        val locality = address.locality //
                        val subAdminArea = address.subAdminArea //
                        val featureName = address.featureName //
                        locationBean.country = countryName
                        locationBean.province = adminArea
                        locationBean.city = locality
                        locationBean.district = subAdminArea
                        locationBean.street = featureName
                    } else {
                    }
                }
            } catch (e: IOException) {
            } catch (e: Exception) {
            }
        }
        if (mLocationCallbackListener != null) {
            if (!isGetLocationSuccess) {
                mLocationCallbackListener!!.updataLocation(locationBean)
                isGetLocationSuccess = true
            }
        }
    }


    private var mLocationCallbackListener: LocationCallbackListener? = null

    interface LocationCallbackListener {
        fun updataLocation(location: LocationBean?)
    }

    fun setListener(callbackListener: LocationCallbackListener?): LocationUtils {
        mLocationCallbackListener = callbackListener
        return this
    }
}