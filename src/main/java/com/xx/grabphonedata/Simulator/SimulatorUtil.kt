package com.xx.grabphonedata.Simulator

import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import java.io.File
import java.util.*

/**
 *Created by Lyq
 *on 2020-10-13
 */

object SimulatorUtil {
    private val known_files: MutableList<String> =
        ArrayList()

    //1.检测模拟器上特有的几个文件
    fun checkEmulatorFiles(): Boolean {
        known_files.add("/system/lib/libc_malloc_debug_qemu.so")
        known_files.add("/sys/qemu__trace")
        known_files.add("/system/bin/qemu-props")
        for (i in known_files.indices) {
            val file_name = known_files[i]
            val qemu_file = File(file_name)
            if (qemu_file.exists()) {
                return true
            }
        }
        return false
    }

    //5.检测手机上的一些硬件信息
    fun checkEmulatorBuild(): Boolean {
        val BOARO_PRO = Build.BOARD
        val BOOTLOADER_PRO = Build.BOOTLOADER
        val BRAND_PRO = Build.BRAND
        val DEVICE_PRO = Build.DEVICE
        val HARDWARE_PRO = Build.HARDWARE
        val MODEL_PRO = Build.MODEL
        val PRODUCT = Build.PRODUCT
        return if ("unknown" == BOARO_PRO || "unknown" == BOOTLOADER_PRO || "generic" == BRAND_PRO || "generic" == DEVICE_PRO || "sdk" == HARDWARE_PRO || "sdk" == MODEL_PRO || "goldfish" == PRODUCT) {
            true
        } else false
    }

    //6.检测手机运营商家
    fun checkOperatorNameAndroid(context: Context): Boolean {
        val szOperatorName =
            (context.getSystemService("phone") as TelephonyManager).networkOperatorName
        return if (szOperatorName.toLowerCase() == "android") {
            true
        } else false
    }
}