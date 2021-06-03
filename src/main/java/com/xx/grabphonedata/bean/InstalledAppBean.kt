package com.xx.grabphonedata.bean

import java.io.Serializable

/**
 *Created by Lyq
 *on 2020-10-13
 */
data class InstalledAppBean(
    var installappInstallDatetime: Long = 0,
    var installAppName: String = "",
    var installappPackageName: String = "",
    var installappType: Int = 0,
    var installappVersion: String = ""
) : Serializable {
}