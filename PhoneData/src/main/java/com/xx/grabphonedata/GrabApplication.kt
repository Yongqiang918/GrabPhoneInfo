package com.xx.grabphonedata

import android.app.Application
import android.content.Context
import kotlin.properties.Delegates

/**
 *Created by Lyq
 *on 2020-11-25
 */
class GrabApplication : Application() {

    companion object {
        var mContext: Context by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        mContext = applicationContext

    }
}