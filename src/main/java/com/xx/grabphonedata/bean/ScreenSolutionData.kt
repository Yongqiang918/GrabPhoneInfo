package com.xx.grabphonedata.bean

import java.io.Serializable

/**
 *Created by Lyq
 *on 2020-10-13
 */
data class ScreenSolutionData(
    var density: String = "",
    var densityDpi: String = "",
    var heightPixels: String = "",
    var widthPixels: String = "",

    var noncompatDensity: String = "",
    var noncompatDensityDpi: String = "",
    var noncompatHeightPixels: String = "",
    var noncompatWidthPixels: String = "",

    var noncompatScaledDensity: String = "",
    var noncompatXdpi: String = "",
    var noncompatYdpi: String = "",

    var scaledDensity: String = "",
    var xdpi: String = "",
    var ydpi: String = ""
) : Serializable {

}