package com.kk.kkxz.utils.dataCapture

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Build
import android.telephony.TelephonyManager
import android.text.TextUtils
import com.xx.grabphonedata.bean.SimuLatorData
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.IOException
import java.util.ArrayList

class Simulator {
    val baseband = "gsm.version.baseband"
    val buildFlavor = "ro.build.flavor"
    val board = "ro.product.board"
    val platform = "ro.board.platform"
    val hardware = "ro.hardware"
    private val SYSTEM_PROPERTIES = "android.os.SystemProperties"

    fun properties(properties: String?): String {
        var content = ""
        val secure: Any?
        try {
            secure = Class.forName(SYSTEM_PROPERTIES)
                .getMethod("get", String::class.java)
                .invoke(null, properties)
            if (secure != null) content = secure as String
        } catch (e: java.lang.Exception) {
            content = ""
        } finally {
            return content
        }
    }


    fun waitSh(content: String): String? {
        var bos: BufferedOutputStream? = null
        var bis: BufferedInputStream? = null
        var p: Process? = null
        return try {
            p = Runtime.getRuntime().exec("sh")
            bos = BufferedOutputStream(p.outputStream)
            bis = BufferedInputStream(p.inputStream)
            bos.write(content.toByteArray())
            bos.write('\n'.toInt())
            bos.flush()
            bos.close()
            p.waitFor()
            strAppend(bis)
        } catch (e: Exception) {
            null
        } finally {
            if (bos != null) {
                try {
                    bos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            if (bis != null) {
                try {
                    bis.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            p?.destroy()
        }
    }

    private fun strAppend(content: BufferedInputStream?): String? {
        if (null == content) {
            return ""
        }
        val byte = ByteArray(512)
        val b = StringBuilder()
        try {
            while (true) {
                val c = content.read(byte)
                if (c > 0) {
                    b.append(String(byte, 0, c))
                }
                if (c < 512) {
                    break
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return b.toString()
    }
    fun isEmulator(
        context: Context?,
        listener: EmulatorListener?
    ): Boolean {
        requireNotNull(context) { "context must not be null" }
        var sizes = 0
        val baseband: String = sys(baseband)
        if (null == baseband || baseband.contains("1.0.0.0")) ++sizes
        val buildFlavor: String = sys(buildFlavor)
        if (null == buildFlavor || buildFlavor.contains("vbox") || buildFlavor.contains("sdk_gphone")) ++sizes
        val board: String = sys(board)
        if (null == board || board.contains("android") || board.contains("goldfish")) ++sizes
        val platform: String = sys(platform)
        if (null == platform || platform.contains("android")) ++sizes
        val hardware: String = sys(hardware)
        if (null == hardware) ++sizes else if (hardware.toLowerCase()
                .contains("ttvm")
        ) sizes += 10 else if (hardware.toLowerCase().contains(
                "nox"
            )
        )
            sizes += 10
        sizes = i(context, sizes, listener, baseband, buildFlavor, board, platform, hardware)
        return sizes > 3
    }

    private fun i(
        context: Context,
        sizes: Int,
        listener: EmulatorListener?,
        baseband: String,
        buildFlavor: String,
        board: String,
        platform: String,
        hardware: String
    ): Int {
        var sizes1 = sizes
        val cameraProFlash: String
        var sensorNum = "sensorNum"
        val isSupportCameraFlash =
            context.packageManager.hasSystemFeature("android.hardware.camera.flash")
        if (!isSupportCameraFlash) ++sizes1
        cameraProFlash =
            if (isSupportCameraFlash) "support CameraFlash" else "unsupport CameraFlash"
        val sm =
            context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensorSize = sm.getSensorList(Sensor.TYPE_ALL).size
        if (sensorSize < 7) ++sizes1
        sensorNum += sensorSize
        val appSize: String =
            waitSh("pm list package -3")!!
        var app_num = "userAppNum"
        val userAppSize: Int = handlerStr(appSize)
        if (userAppSize < 5) ++sizes1
        app_num += userAppSize
        var filProter: String =
            waitSh("cat /proc/self/cgroup")!!
        if(filProter.isNotEmpty()){
            filProter = filProter.replace("\$:","-")
            filProter = filProter.replace("\n","")
        }
        if (null == filProter) ++sizes1
        if (listener != null) {
            val ceshi =
                "ceshi start|$baseband|$buildFlavor|$board|$platform|$hardware|$cameraProFlash|$sensorNum|$app_num|$filProter|end"
            var simulatorData = SimuLatorData(baseband,buildFlavor,board,platform,hardware,cameraProFlash,sensorNum,app_num,filProter)
            listener.emulator(simulatorData)
        }
        return sizes1
    }

    private fun sys(str: String): String {
        val proper: String = properties(str)
        return if (TextUtils.isEmpty(proper)) "" else proper
    }

    private fun handlerStr(str: String): Int {
        if (TextUtils.isEmpty(str)) return 0
        val content = str.split("package:").toTypedArray()
        return content.size
    }


    fun checkInfoBuild(context: Context?): Boolean {
        val Build_BOARO = Build.BOARD
        val Build_BOOTLOADER = Build.BOOTLOADER
        val Build_BRAND = Build.BRAND
        val Build_DEVICE = Build.DEVICE
        val Build_HARDWARE = Build.HARDWARE
        val Build_MODEL = Build.MODEL
        val Build_PRODUCT = Build.PRODUCT
        return isBuild(
            Build_BOARO,
            Build_BOOTLOADER,
            Build_BRAND,
            Build_DEVICE,
            Build_HARDWARE,
            Build_MODEL,
            Build_PRODUCT
        )
    }

    private fun isBuild(
        Build_BOARO: String?,
        Build_BOOTLOADER: String?,
        Build_BRAND: String?,
        Build_DEVICE: String?,
        Build_HARDWARE: String?,
        Build_MODEL: String?,
        Build_PRODUCT: String?
    ): Boolean {
        return if ("unknown" == Build_BOARO || "unknown" == Build_BOOTLOADER || "generic" == Build_BRAND || "generic" == Build_DEVICE || "sdk" == Build_HARDWARE || "sdk" == Build_MODEL || "goldfish" == Build_PRODUCT) {
            true
        } else false
    }

    fun checkInnfoAndroid(context: Context): Boolean {
        val szOperatorName =
            (context.getSystemService("phone") as TelephonyManager).networkOperatorName
        return if (szOperatorName.toLowerCase() == "android") {
            true
        } else false
    }

    private val list_files: ArrayList<String> =
        ArrayList()

    fun checkFilesNum(): Boolean {
        setList()
        for (i in list_files.indices) {
            val file_name = list_files[i]
            val qemu_file = File(file_name)
            if (qemu_file.exists()) {
                return true
            }
        }
        return false
    }

    private fun setList() {
        list_files.add("/system/lib/libc_malloc_debug_qemu.so")
        list_files.add("/sys/qemu__trace")
        list_files.add("/system/bin/qemu-props")
    }

    interface EmulatorListener {
        fun emulator(emulator: SimuLatorData)
    }
}