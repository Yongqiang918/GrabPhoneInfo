package com.xx.grabphonedata.Simulator

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.text.TextUtils
import java.io.File

/**
 *Created by Lyq
 *on 2020-10-13
 */
class EmulahopetorCheckUtil private constructor() {
    private object SingletonHolder {
        internal val JNDSJKGDSN = EmulahopetorCheckUtil()
    }

    companion object {
        val singleInstance: EmulahopetorCheckUtil
            get() = SingletonHolder.JNDSJKGDSN

        fun makeDirs(fileProPath: String?): Boolean {
            val folProder = File(fileProPath)
            return if (folProder.exists() && folProder.isDirectory) true else folProder.mkdirs()
        }
    }

    fun kmReadKmSysProperty(
        context: Context?,
        callBack: EmulatorCheckCallback?
    ): Boolean {
        requireNotNull(context) { "context must not be null" }
        var sizes_num = 0
        val baseband_name = emuSystem("gsm.version.baseband")
//        if (null == baseband_name || baseband_name.contains("1.0.0.0")) ++sizes_num
        if (null != baseband_name) ++sizes_num
        val buildFlavor_name = emuSystem("ro.build.flavor")
//        if (null == buildFlavor_name || buildFlavor_name.contains("vbox") || buildFlavor_name.contains("sdk_gphone")) ++sizes_num
        if (null != buildFlavor_name) ++sizes_num
        val board_name = emuSystem("ro.product.board")
//        if (null == board_name || board_name.contains("android") || board_name.contains("goldfish")) ++sizes_num
        if (null != board_name) ++sizes_num
        val platform_name = emuSystem("ro.board.platform")
//        if (null == platform_name || platform_name.contains("android")) ++sizes_num
        if (null != platform_name) ++sizes_num
        val hardware_name = emuSystem("ro.hardware")
        if (null == hardware_name) ++sizes_num
        else if (hardware_name.toLowerCase().contains("ttvm"))
            sizes_num += 10 else if (hardware_name.toLowerCase().contains("nox")) sizes_num += 10
        val cameraProFlash: String
        var sensorNum = "sensorNum"
        val isSupportCameraFlash =
            context.packageManager.hasSystemFeature("android.hardware.camera.flash")
        if (!isSupportCameraFlash) ++sizes_num
        cameraProFlash =
            if (isSupportCameraFlash) "support CameraFlash" else "unsupport CameraFlash"
        val sm =
            context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensorSize = sm.getSensorList(Sensor.TYPE_ALL).size
        if (sensorSize < 7) ++sizes_num
        sensorNum = sensorNum + sensorSize
        val userKkApps = CommandUtil.getInstance().exec("pm list package -3")
        var jwgebubhjds = "userAppNum"
        val userAppSize = emuHandlerStr(userKkApps)
        if (userAppSize < 5) ++sizes_num
        jwgebubhjds = jwgebubhjds + userAppSize
        val filProter = CommandUtil.getInstance().exec("cat /proc/self/cgroup")
        if (null == filProter) ++sizes_num
        if (callBack != null) { //cpu指令集1、2，检测有没有模拟器文件？？，描述build的标签，builder类型， 描述build的标签
            val strBuffer = StringBuffer("ceshi start|")
                .append(baseband_name).append("|") //基带版本信息
                .append(buildFlavor_name).append("|") //渠道信息
                .append(board_name).append("|") //采用的处理器
                .append(platform_name).append("|") //主板平台
                .append(hardware_name).append("|") //机器板子代号
                .append(cameraProFlash).append("|") //是否支持摄像头闪光灯
                .append(sensorNum).append("|") //传感器次数
                .append(jwgebubhjds).append("|") //第三方包数量
                .append(filProter).append("|end") //进程组
            callBack.phoneHardwareInfo(strBuffer.toString())
        }
        return sizes_num > 3
    }

    private fun emuHandlerStr(safasf: String): Int {
        if (TextUtils.isEmpty(safasf)) return 0
        val sfasf = safasf.split("package:").toTypedArray()
        return sfasf.size
    }

    private fun emuSystem(propName: String): String? {
        val property = CommandUtil.getInstance().exec(propName)
        return if (TextUtils.isEmpty(property)) null else property
    }

}