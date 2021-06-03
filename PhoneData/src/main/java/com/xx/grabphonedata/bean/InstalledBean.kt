package com.xx.grabphonedata.bean

import java.io.Serializable

/**
 *Created by Lyq
 *on 2020-10-13
 */
data class InstalledBean(
    var installappKKInstallDatetime: Long = 0,
    var installAppKKName: String = "",
    var installappKKPackageName: String = "",
    var installappKKType: Int = 0,
    var installappKKVersion: String = ""
) : Serializable {
}