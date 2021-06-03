package com.xx.grabphonedata.bean

import java.io.Serializable

/**
 *Created by Lyq
 *on 2020-10-13
 */
data class SimulatorDetaisBean(
    var appNum: String = "",
    var baseband: String = "",
    var board: String = "",
    var buildFlavor: String = "",
    var CPU_ABI: String = "",
    var CPU_ABI2: String = "",
    var cameraProFlash: String = "",
    var emulatorFiles_status: String = "",
    var filProter: String = "",
    var hardware: String = "",
    var platform: String = "",
    var sensorNum: String = "",
    var TAGS: String = "",
    var TYPE: String = "",
    var USER: String = ""
) : Serializable {
}