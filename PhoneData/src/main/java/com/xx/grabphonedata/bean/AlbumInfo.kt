package com.xx.grabphonedata.bean

import java.io.Serializable

/**
 *Created by Lyq
 *on 2021-05-17
 */
class AlbumInfo(
    var photographers: String = "",
    var shooting_time: String = "",
    var height: String = "",
    var width: String = "",
    var file_name: String = "",
    var latitude: String = "",
    var longitude: String = "",
    var manufactor: String = "",
    var model: String = "",
    var name: String = ""
) : Serializable {
}


