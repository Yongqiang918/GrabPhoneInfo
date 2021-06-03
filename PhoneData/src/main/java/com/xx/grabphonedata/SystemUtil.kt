package com.xx.grabphonedata

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.ApplicationInfo
import android.graphics.Point
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.os.SystemClock
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.text.format.Formatter
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import java.io.File
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.math.BigDecimal
import java.net.Inet6Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*

/**
 *Created by Lyq
 *on 2020-10-09
 */
object SystemUtil {

    /**
     * 是否是debug模式
     */
    fun isDebugApp(context: Context): Boolean {
        return try {
            val info: ApplicationInfo =
                context.getApplicationInfo()
            info.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        } catch (x: java.lang.Exception) {
            false
        }
    }


    /**
     * 获取内网IP地址
     * @return
     */
    fun getNetInAddressNew(): String? {
        var hostIp: String? = ""
        try {
            val nis: Enumeration<*> = NetworkInterface.getNetworkInterfaces()
            var ia: InetAddress? = null
            while (nis.hasMoreElements()) {
                val ni = nis.nextElement() as NetworkInterface
                val ias = ni.inetAddresses
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement()
                    if (ia is Inet6Address) {
                        continue  // skip ipv6
                    }
                    val ip = ia.hostAddress
                    if ("127.0.0.1" != ip) {
                        hostIp = ia.hostAddress
                        break
                    }
                }
            }
        } catch (e: SocketException) {
            Log.i("yao", "SocketException")
            e.printStackTrace()
        }
        return hostIp
    }


    /**
     * 获取语言
     */
    fun getLanguage(context: Context): String {
        var language = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (context.resources.getConfiguration().getLocales().get(0) != null) {
                language = context.resources.getConfiguration().getLocales()
                    .get(0).displayLanguage.toString()
            }
        } else {
            language =
                context.resources.getConfiguration().locale.displayLanguage.toString()
        }
        return language
    }


    /**
     * 获取手机电池容量
     */
    fun getBatteryPower(context: Context): String? {
        val mPowerProfile: Any
        var capacity = 0.0
        val POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile"
        try {
            mPowerProfile = Class.forName(POWER_PROFILE_CLASS)
                .getConstructor(Context::class.java)
                .newInstance(context)
            capacity = Class.forName(POWER_PROFILE_CLASS)
                .getMethod("getBatteryCapacity")
                .invoke(mPowerProfile) as Double
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "$capacity mAh"
    }

    /**
     * 手机类型
     */
    fun getPhoneType(context: Context): Int {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (telephonyManager.phoneType == TelephonyManager.PHONE_TYPE_NONE) {//是平板
            return 2
        } else {
            return 1
        }
    }


    /**
     * 获取手机开机时间
     */
    fun getBootTime(): Long {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            System.currentTimeMillis() - SystemClock.elapsedRealtimeNanos() / 1000000
        } else System.currentTimeMillis()
    }


    /**
     * 获取wifi名称
     */
    fun getWifiName(context: Context): String {
        var ssid = "no wifi"
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O || Build.VERSION.SDK_INT === Build.VERSION_CODES.P) {
            var mWifiManager = (context?.applicationContext
                ?.getSystemService(Context.WIFI_SERVICE) as WifiManager)
            var info = mWifiManager.connectionInfo
            return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                info.ssid
            } else {
                info.ssid.replace("\"", "")
            }
        } else if (Build.VERSION.SDK_INT === Build.VERSION_CODES.O_MR1) {
            var connManager = (context?.applicationContext
                ?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
            var networkInfo: NetworkInfo = connManager.activeNetworkInfo!!
            if (networkInfo.isConnected()) {
                if (networkInfo.getExtraInfo() != null) {
                    return networkInfo.getExtraInfo().replace("\"", "")
                }
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val wifiManager =
                context?.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wifiInfo = wifiManager.connectionInfo
            if(wifiInfo !=null){
                return wifiInfo.ssid
            }
        }
        return ssid
    }


    /**
     * 判断当前网络是否是Wifi
     */
    fun isWifi(context: Context): Boolean {
        val connectivityProManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeProNetInfo =
            connectivityProManager.activeNetworkInfo
        return if (activeProNetInfo != null && activeProNetInfo.type == ConnectivityManager.TYPE_WIFI) {
            true
        } else {
            false
        }
    }


    /**
     * 获取屏幕尺寸
     */
    private var mInch: Double = 0.0
    fun getScreenSize(activity: Activity): Double {
        if (mInch != 0.0) {
            return mInch
        }
        try {
            var sizeX = 0
            var sizeY = 0
            val display = activity.windowManager.defaultDisplay
            val metrics = DisplayMetrics()
            display.getMetrics(metrics)
            if (Build.VERSION.SDK_INT >= 17) {
                val size = Point()
                display.getRealSize(size)
                sizeX = size.x
                sizeY = size.y
            } else if (Build.VERSION.SDK_INT < 17
                && Build.VERSION.SDK_INT >= 14
            ) {
                val mGetRawH =
                    Display::class.java.getMethod("getRawHeight")
                val mGetRawW =
                    Display::class.java.getMethod("getRawWidth")
                sizeX = mGetRawW.invoke(display) as Int
                sizeY = mGetRawH.invoke(display) as Int
            } else {
                sizeX = metrics.widthPixels
                sizeY = metrics.heightPixels
            }
            mInch =
                formatDouble(
                    Math.sqrt(sizeX / metrics.xdpi * (sizeX / metrics.xdpi) + sizeY / metrics.ydpi * (sizeY / metrics.ydpi).toDouble()),
                    1
                )
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return mInch
    }

    /**
     * Double类型保留指定位数的小数，返回double类型（四舍五入）
     * newScale 为指定的位数
     */
    private fun formatDouble(d: Double, newScale: Int): Double {
        val bd = BigDecimal(d)
        return bd.setScale(newScale, BigDecimal.ROUND_HALF_UP).toDouble()
    }


    /**
     * BASEBAND-VER
     * 基带版本
     */
    fun getBasebandVersion(): String {
        var Version = ""
        try {
            val cl = Class.forName("android.os.SystemProperties")
            val invoker = cl.newInstance()
            val m = cl.getMethod(
                "get", *arrayOf<Class<*>>(
                    String::class.java,
                    String::class.java
                )
            )
            val result =
                m.invoke(invoker, *arrayOf<Any>("gsm.version.baseband", "no message"))
            Version = result as String
        } catch (e: java.lang.Exception) {

        }
        return Version
    }


    /**
     * 获得机身内存总大小
     */
    fun getRomTotalSize(context: Context): String {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSize.toLong()
//        val totalBlocks = stat.blockCount.toLong()
        val availableBlocks = stat.availableBlocks.toLong()

        return Formatter.formatFileSize(context, blockSize * availableBlocks)
    }


    /**
     * 获得机身可用内存
     *
     * @return
     */
    fun getRomAvailableSize(context: Context?): Long {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSize.toLong()
        val availableBlocks = stat.availableBlocks.toLong()
        return blockSize * availableBlocks
    }


    /**
     * 获取网络类型编码
     */
    fun getNetworkStateCode(context: Context): Int {
        val connManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // 判断是否为WIFI
//        需要network state权限
        val wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        if (null != wifiInfo) {
            val state = wifiInfo.state
            if (null != state) {
                if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
//                        return NETWORK_WIFI
                    return 0
                }
            }
        }
        // 若不是WIFI，则去判断是2G、3G、4G网
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val networkType = telephonyManager.networkType
        return networkType
    }


    /**
     * 获取设备MAC 地址 由于 6.0 以后 WifiManager 得到的 MacAddress得到都是 相同的没有意义的内容
     * 所以采用以下方法获取Mac地址
     */
    fun getMacAddress(): String {
        var macAddress: String = ""
        val strBuffer = StringBuffer()
        var networkInterface: NetworkInterface? = null
        try {
            networkInterface = NetworkInterface.getByName("eth1")
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0")
            }
            if (networkInterface == null) {
                return ""
            }
            val addr = networkInterface.hardwareAddress
            if(addr !=null && addr.size >0){
                for (b in addr) {
                    strBuffer.append(String.format("%02X:", b))
                }
            }
            if (strBuffer.length > 0) {
                strBuffer.deleteCharAt(strBuffer.length - 1)
            }
            macAddress = strBuffer.toString()
        } catch (e: SocketException) {
            e.printStackTrace()
            return ""
        }
        return macAddress
    }

    /**
     * 获取手机是否越狱
     *
     * @return
     */
    fun isRoot(): Boolean {
        var bool = false
        try {
            bool = if (!File("/system/bin/su").exists()
                && !File("/system/xbin/su").exists()
            ) {
                false
            } else {
                true
            }
        } catch (e: java.lang.Exception) {
        }
        return bool
    }


    /**
     * Flyme 说 5.0 6.0统一使用这个获取IMEI IMEI2 MEID
     * @param ctx
     * @return
     */
    @SuppressLint("MissingPermission")
    fun getImeiAndMeid(ctx: Context): Map<*, *> {
        val map: MutableMap<String, String> =
            HashMap()
        val mTelephonyManager =
            ctx.getSystemService(Activity.TELEPHONY_SERVICE) as TelephonyManager
        var clazz: Class<*>? = null
        var method: Method? = null //(int slotId)
        try {
            clazz = Class.forName("android.os.SystemProperties")
            method = clazz.getMethod("get", String::class.java, String::class.java)
            val gsm = method.invoke(null, "ril.gsm.imei", "") as String
            val meid = method.invoke(null, "ril.cdma.meid", "") as String
            map["meid"] = meid
            if (!TextUtils.isEmpty(gsm)) {
                //the value of gsm like:xxxxxx,xxxxxx
                val imeiArray = gsm.split(",").toTypedArray()
                if (imeiArray != null && imeiArray.size > 0) {
                    map["imei1"] = imeiArray[0]
                    if (imeiArray.size > 1) {
                        map["imei2"] = imeiArray[1]
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            map["imei2"] = mTelephonyManager.getDeviceId(1)
                        }
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        map["imei1"] = mTelephonyManager.getDeviceId(0)
                        map["imei2"] = mTelephonyManager.getDeviceId(1)
                    }
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    map["imei1"] = mTelephonyManager.getDeviceId(0)
                    map["imei2"] = mTelephonyManager.getDeviceId(1)
                }
            }
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        return map
    }

    /**
     * 拿到imei或者meid后判断是有多少位数
     *
     * @param ctx
     * @return
     */
    fun getNumber(ctx: Context): Int {
        var count = 0
        var number = getImeiOrMeid(ctx)!!.trim { it <= ' ' }.toLong()
        while (number > 0) {
            number = number / 10
            count++
        }
        return count
    }

    /**
     * 系统4.0的时候
     * 获取手机IMEI 或者Meid
     *
     * @return 手机IMEI
     */
    @SuppressLint("MissingPermission")
    fun getImeiOrMeid(ctx: Context): String? {
        val tm =
            ctx.getSystemService(Activity.TELEPHONY_SERVICE) as TelephonyManager
        return tm?.deviceId
    }


}