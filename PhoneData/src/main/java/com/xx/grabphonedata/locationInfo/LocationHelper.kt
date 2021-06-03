package com.kk.kkxz.utils.locationInfo

import android.location.GpsStatus
import android.location.Location
import android.os.Bundle

interface LocationHelper {
    fun updateLocation(location: Location)
    fun updateStatus(
        provider: String,
        status: Int,
        extras: Bundle
    )

    fun updateGPSStatus(status: GpsStatus)
    fun updateLastLocation(location: Location)
}