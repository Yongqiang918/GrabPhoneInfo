package com.xx.grabphonedata.bean

import java.io.Serializable

/**
 *Created by Lyq
 *on 2020-10-13
 */
data class SimulatorBean(
    var emulatorFiles: Boolean = false,
    var operatorNameAndroid: Boolean = false,
    var isRunningInEmulator: Boolean = false,
    var emulatorBuild: Boolean = false
) :Serializable{
}