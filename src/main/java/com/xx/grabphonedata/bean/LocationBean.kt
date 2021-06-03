package com.xx.grabphonedata.bean

import java.io.Serializable

/**
 *Created by Lyq
 *on 2020-10-12
 */
data class LocationBean(
    var radius: Float = 0F,
    var coorType: String = "",
    var errorCode: Int = 0,
    var locationWhere: Int = 0,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var addr: String = "",
    var country: String = "",
    var province: String = "",
    var city: String = "",
    var district: String = "",
    var street: String = "",
    var locType: Int = 0
    ):Serializable {
}