package com.xx.grabphonedata.bean

import java.io.Serializable

/**
 *Created by Lyq
 *on 2021-05-17
 */
class MediaSourceBean(
    var photo_number: Int = 0,
    var video_number: Int = 0,
    var audio_number: Int = 0
) : Serializable {
}


