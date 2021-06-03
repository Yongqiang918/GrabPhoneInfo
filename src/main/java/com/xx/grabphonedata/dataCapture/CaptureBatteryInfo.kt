package com.kk.kkxz.utils.dataCapture

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.format.Formatter.formatFileSize
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.common.util.JsonUtils
import com.xx.grabphonedata.*
import com.xx.grabphonedata.bean.MobileDevicesBean
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.lang.reflect.Method


class CaptureBatteryInfo(var application: Context, var activity: Activity) : Handler.Callback {
    private var isRegister = false
    private val handler = Handler(this)
    private var batteryCReceiver = BChangedReceiver()

    private var mProStatus = 0
    private var batteryHealth: String = ""
    private var charger: String = ""
    private var batteryStatus: String = ""
    private var maximumPower: Int = 0
    private var currentBattery: Int = 0
    private var screenSize: String = ""
    private var netOutIp: String = ""
    private var batteryVoltage: Int = 0
    private var batteryTechnology: String = ""
    private var batteryTemperature: Int = 0

    private var isCharging: String = "0"
    private var isUSBCharge: String = "0"
    private var isACCharge: String = "0"

    var imei = ""
    var imei2 = ""
    var meid = ""
    var gaid: String = ""

    @SuppressLint("NewApi")
    override fun handleMessage(msg: Message): Boolean {
        var bundle: Bundle? = null
        when (msg?.what) {
            1 -> {
                val phoneModel = Build.MODEL

                var wlanMac = SystemUtil.getMacAddress()
                if (wlanMac == null) {
                    wlanMac = ""
                }
                var mobileNetworktype = SystemUtil.getNetworkStateCode(application)


                var basebandVersion = SystemUtil.getBasebandVersion()
                if (basebandVersion == null) {
                    basebandVersion = ""
                }
                var kernelVersion = System.getProperty("os.version")
                if (kernelVersion == null) {
                    kernelVersion = ""
                }
                var processor = DeviceUtil.getCpuName()
                if (processor == null) {
                    processor = ""
                }
                var runningMemory = DeviceUtil.getTotalMemory(application)
                if (runningMemory == null) {
                    runningMemory = ""
                }
                var operatingSystemVersion = Build.DISPLAY
                if (operatingSystemVersion == null) {
                    operatingSystemVersion = ""
                }
                screenSize =
                    SystemUtil.getScreenSize(activity).toString()

                var wifiName = ""
                val wifi = SystemUtil.isWifi(application)
                if (wifi) {
                    wifiName = SystemUtil.getWifiName(application)
                }


                val bootTime = (SystemUtil.getBootTime() / 1000)
                val phoneType = SystemUtil.getPhoneType(application)
                var phoneBrands = Build.BRAND
                if (phoneBrands == null) {
                    phoneBrands = ""
                }
                var batteryCapacity = SystemUtil.getBatteryPower(application)

                val batteryPower = currentBattery

                val mVersionProNum = Build.DISPLAY

//                val appChannelType = ""//----------------------------------

                val appType = 1

//                val appVersionNum = ""//----------------------------------

                val phoneLanguage = SystemUtil.getLanguage(application)

                val intranetNetIp = SystemUtil.getNetInAddressNew()

                var versionNum = Build.VERSION.RELEASE
                if (versionNum == null) {
                    versionNum = ""
                }


                var usableStorage = ""
                if (SystemUtil.getRomTotalSize(application) != null) {
                    usableStorage = SystemUtil.getRomTotalSize(application)
                }

                var allStorage = ""
                if (GetAllStorageUtil.queryWithStorageManager(application) != null) {
                    allStorage = GetAllStorageUtil.queryWithStorageManager(application)
                }

                //totalStorageCapacityB\storageSurplusCapacityB
                var allStorageForByteUpload = ""
                var usableStorageForByteUpload = ""
                var allStorageForByte = "0"
                var usableStorageForByte = "0"
                val storageForByteStr = SdCardUtils.queryWithStorageManager(application)
                if (storageForByteStr.contains("-")) {
                    val split = storageForByteStr.split("-").toTypedArray()
                    val storageTotalSizeStr = split[0]
                    val storageAvailableSizeStr = split[1]
                    allStorageForByte = storageTotalSizeStr
                    usableStorageForByte = storageAvailableSizeStr
                    allStorageForByteUpload = storageTotalSizeStr+"Byte"
                    usableStorageForByteUpload = storageAvailableSizeStr+"Byte"
                }


                //sdCardTotalSize\sdCardAvailableSize
                var allStorageForCardUpload: String = ""
                var usableStorageForCardUpload: String = ""
                var allStorageForCard: Long = 0
                var usableStorageForCard: Long = 0

                allStorageForCard =
                    allStorageForByte.toLong() - GetPhoneStoreSizeUtil.queryWithStorageManager(
                        application
                    )
                usableStorageForCard =
                    usableStorageForByte.toLong() - SystemUtil.getRomAvailableSize(application)


                allStorageForCardUpload = allStorageForCard.toString() + "Byte"

                if (allStorageForCard == 0L) {
                    usableStorageForCardUpload = "0Byte"
                } else {
                    if (allStorageForCard >= usableStorageForCard) {
                        usableStorageForCardUpload = usableStorageForCard.toString() + "Byte"
                    } else {
                        usableStorageForCardUpload = allStorageForCard.toString() + "Byte"
                    }

                }

                val uuid =
                    DeviceUuidFactory.getInstance(application).deviceUuid.toString()

                var isRoot = "0"
                if (SystemUtil.isRoot()) {
                    isRoot = "1"
                }

                var isDebug = "0"
                if (SystemUtil.isDebugApp(application)) {
                    isDebug = "1"
                }

                var phoneNum1 = ""
                var phoneNum2 = ""
                if (GetNumber().getNumber(application) != null) {
                    phoneNum1 = GetNumber().getNumber(application)
                }
                if (GetNumber().getNumber2(application) != null) {
                    phoneNum2 = GetNumber().getNumber2(application)
                }

                var imsi = getImsi()
                val androidId = getAndroidId()
                val browserListStr = getBrowserList()
                val browserListJson = com.xx.grabphonedata.JsonUtils.listToJson(browserListStr)


                var mobileDevicesBean = MobileDevicesBean(
                    phoneModel,
                    imei,
                    meid,
                    wlanMac,

                    mobileNetworktype.toString(),
                    operatingSystemVersion,
                    screenSize,
                    wifiName,

                    usableStorage,
                    basebandVersion,
                    kernelVersion!!,
                    processor,

                    runningMemory,
                    batteryCapacity.toString(),
                    batteryPower.toString(),
                    Build.BRAND,

                    bootTime.toString()!!,
                    batteryHealth,
                    currentBattery.toString(),
                    maximumPower.toString(),

                    charger,
                    batteryStatus,
                    batteryVoltage.toString(),
                    phoneType.toString(),

                    mVersionProNum,

                    "",
                    "",
                    "",

                    phoneLanguage,
                    imei2,
                    batteryTechnology.toString(),
                    batteryTemperature.toString(),

                    intranetNetIp!!,
                    netOutIp,

                    versionNum,
                    allStorage,
                    "",
                    uuid,
                    isRoot,
                    isDebug,
                    phoneNum1,
                    phoneNum2,

                    gaid,
                    imsi,
                    androidId,
                    allStorageForCardUpload,
                    usableStorageForCardUpload,
                    browserListJson,
                    allStorageForByteUpload,
                    usableStorageForByteUpload,
                    isCharging,
                    currentBattery.toString(),
                    isUSBCharge,
                    isACCharge


                )
                if (batterDataListenner != null) {
                    batterDataListenner!!.detailBatterInfo(mobileDevicesBean)
                }


            }
            2 -> {
                bundle = msg.data
                if (bundle != null) {
                    val netOutIP = bundle.getString("netOutIP")
                    if (netOutIP != null) {
                        netOutIp = netOutIP
                    }
                }
            }
            3 -> {
                bundle = msg.data
                if (bundle != null) {
                    val gaId = bundle.getString("gaId")
                    if (gaId != null) {
                        gaid = gaId
                    }
                }
            }


        }
        return false
    }


    fun registBatter(): CaptureBatteryInfo {
        isRegister = true
        getNetworkNetIp()
        getImeiMore()
//        getGaid()

        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED)
        intentFilter.addAction(Intent.ACTION_BATTERY_LOW)
        intentFilter.addAction(Intent.ACTION_BATTERY_OKAY)
        application.registerReceiver(batteryCReceiver, intentFilter)

        handler.sendEmptyMessageDelayed(1, (3 * 1000).toLong())
        return this
    }

    fun unRegistBatter(): CaptureBatteryInfo {
        if (isRegister && batteryCReceiver != null) {
            application.unregisterReceiver(batteryCReceiver)
            isRegister = false
        }
        return this
    }

    interface OnBatterDataListenner {
        fun detailBatterInfo(data: MobileDevicesBean)
    }

    var batterDataListenner: OnBatterDataListenner? = null

    fun setCurrentListenners(listener: OnBatterDataListenner): CaptureBatteryInfo {
        batterDataListenner = listener
        return this
    }


    inner class BChangedReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action!!.equals(Intent.ACTION_BATTERY_CHANGED, ignoreCase = true)) {
                batteryVoltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1)
                val healthStats = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1)
                when (healthStats) {
                    BatteryManager.BATTERY_HEALTH_GOOD -> {
                        batteryHealth = "BATTERY_HEALTH_VERY_GOOD"
                        batteryHealth = "BATTERY_HEALTH_COLD"
                    }
                    BatteryManager.BATTERY_HEALTH_COLD -> batteryHealth = "BATTERY_HEALTH_COLD"
                    BatteryManager.BATTERY_HEALTH_DEAD -> batteryHealth = "BATTERY_HEALTH_DEAD"
                    BatteryManager.BATTERY_HEALTH_OVERHEAT -> batteryHealth =
                        "BATTERY_HEALTH_OVERHEAT"
                    BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> batteryHealth =
                        "BATTERY_HEALTH_OVER_VOLTAGE"
                    BatteryManager.BATTERY_HEALTH_UNKNOWN -> batteryHealth =
                        "BATTERY_HEALTH_UNKNOWN"
                    BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> batteryHealth =
                        "BATTERY_HEALTH_UNSPECIFIED_FAILURE"
                }
                currentPower(intent)
                maxPower(intent)
                powerSource(intent)

                val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
                when (status) {
                    BatteryManager.BATTERY_STATUS_CHARGING ->
                        batteryStatus = "Charging"
                    BatteryManager.BATTERY_STATUS_DISCHARGING -> batteryStatus =
                        "BATTERY_STATUS_DISCHARGING"
                    BatteryManager.BATTERY_STATUS_FULL ->
                        batteryStatus = "Battery full"
                    BatteryManager.BATTERY_STATUS_NOT_CHARGING ->
                        batteryStatus = "The battery is not charged"
                    BatteryManager.BATTERY_STATUS_UNKNOWN ->
                        batteryStatus = "unknown state"
                }
                batteryTechnology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY)!!
                batteryTemperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)


                if (status == BatteryManager.BATTERY_STATUS_FULL || status == BatteryManager.BATTERY_STATUS_CHARGING) {
                    isCharging = "1"
                } else {
                    isCharging = "0"
                }


            } else if (action.equals(Intent.ACTION_BATTERY_LOW, ignoreCase = true)) {
            } else if (action.equals(Intent.ACTION_BATTERY_OKAY, ignoreCase = true)) {
            }
        }
    }


    private fun getImeiMore() {
        //IMEI &  MEID:
        if (Build.VERSION.SDK_INT < 21) {
            if (SystemUtil.getImeiAndMeid(application) != null) {
                if (SystemUtil.getNumber(application) == 14) {
                    imei = SystemUtil.getImeiOrMeid(
                        application
                    ).toString()//meid
                } else if (SystemUtil.getNumber(application) == 15) {
                    meid = SystemUtil.getImeiOrMeid(application).toString()//imei1
                }
                imei2 = ""
            }
        } else if (Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT < 23) {
            val imeiAndMeid =
                SystemUtil.getImeiAndMeid(application)
            imei2 = imeiAndMeid.get("imei2").toString();//imei2
            imei = imeiAndMeid.get("imei1").toString()//imei1
            meid = imeiAndMeid.get("meid").toString()//meid
        } else if (Build.VERSION.SDK_INT >= 23 && Build.VERSION.SDK_INT < 29) {
            val phoneIMEI = getPhoneIMEI()
            if (!phoneIMEI.isNullOrEmpty()) {
                imei = phoneIMEI?.get("imei1").toString()//imei1
                imei2 = phoneIMEI?.get("imei2").toString()//imei2
            } else {
                imei = ""
                imei2 = ""
            }
            meid = getPhoneMEID().toString()//meid
        } else {
            imei = ""
            imei2 = ""
            meid = ""
        }

    }


    @SuppressLint("MissingPermission", "NewApi")
    private fun getPhoneIMEI(): Map<*, *>? {
        val map: MutableMap<String, String> = HashMap()
        val tm =
            application.getSystemService(Service.TELEPHONY_SERVICE) as TelephonyManager
        var imei1: String? = ""
        var imei2: String? = ""
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                imei1 = tm?.getImei(0)
                imei2 = tm?.getImei(1)
                map["imei1"] = imei1
                map["imei2"] = imei2
            }
        } catch (e: Exception) {
            e.fillInStackTrace()
        }
        return map

    }


    @SuppressLint("MissingPermission", "NewApi")
    private fun getPhoneMEID(): String? {
        var isLack = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_DENIED
        if (!isLack) {
            val tm =
                application.getSystemService(Service.TELEPHONY_SERVICE) as TelephonyManager
            var meid: String? = ""
            if (null != tm) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    meid = tm.getMeid();

                } else {
                    meid = tm.getDeviceId();
                }
            } else {
                meid = ""
            }

            return meid

        } else {
            return ""
        }

    }


    private fun currentPower(intent: Intent) {
        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        currentBattery = level
    }

    private fun maxPower(intent: Intent) {
        val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        maximumPower = scale
    }

    private fun powerSource(intent: Intent) {
        val plugedPro = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
        when (plugedPro) {
            BatteryManager.BATTERY_PLUGGED_AC -> {
                charger = "Power supply is AC charger."
                isUSBCharge = "0"
                isACCharge = "1"
            }
            BatteryManager.BATTERY_PLUGGED_USB -> {
                charger = "Power supply is USB port"
                isUSBCharge = "1"
                isACCharge = "0"
            }
            else -> {
            }
        }
    }

    private fun phoneType(): Int {
        val telephonyManager =
            application.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (telephonyManager.phoneType == TelephonyManager.PHONE_TYPE_NONE) {//是平板
            return 2
        } else {
            return 1
        }
    }



    fun getBasebandVersion(): String {
        var Version = ""
        try {
            val cl = Class.forName("android.os.SystemProperties")
            val invoker = cl.newInstance()
            val m: Method = cl.getMethod(
                "get", *arrayOf<Class<*>>(
                    String::class.java,
                    String::class.java
                )
            )
            val result: Any =
                m.invoke(invoker, arrayOf<Any>("gsm.version.baseband", "no message"))
            Version = result as String
        } catch (e: java.lang.Exception) {
        }
        return Version
    }



    fun getTotalMemory(): String? {
        val str1 = "/proc/meminfo"
        val str2: String
        val arrayOfString: Array<String>
        var initial_memory: Long = 0
        try {
            val localFileReader = FileReader(str1)
            val localBufferedReader =
                BufferedReader(localFileReader, 8192)
            str2 = localBufferedReader.readLine()
            arrayOfString = str2.split("\\s+").toTypedArray()
            val i = Integer.valueOf(arrayOfString[1]).toInt()
            initial_memory = i.toLong() * 1024
            localBufferedReader.close()
        } catch (e: IOException) {
        }
        return formatFileSize(application, initial_memory) // Byte转换为KB或者MB，内存大小规格化
    }


    fun getNetworkNetIp() {
        Thread(Runnable {
            val netOutIP = DeviceUtil.getNetOutAddress()
            val msg = Message()
            msg.what = 2
            val bundle = Bundle()
            bundle.putString("netOutIP", netOutIP)
            msg.data = bundle
            Looper.prepare()
            handler.sendMessage(msg)
            Looper.loop()
        }).start()
    }

    fun getGaid() {
        Thread(Runnable {
            val advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(application)
            val gaid = advertisingIdInfo.id
            val msg = Message()
            msg.what = 3
            val bundle = Bundle()
            bundle.putString("gaId", gaid)
            msg.data = bundle
            Looper.prepare()
            handler.sendMessage(msg)
            Looper.loop()

        }).start()
    }

    //Imsi\READ_PHONE_STATE
    @SuppressLint("MissingPermission")
    public fun getImsi(): String {
        var imsi = ""
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                imsi = Settings.System.getString(
                    application.getContentResolver(),
                    Settings.Secure.ANDROID_ID
                )
                return imsi
            }
            //Manifest.permission.READ_PHONE_STATE
//            if (!PermissionUtil.getInstance().checkPermissions(context, android.Manifest.permission.READ_PHONE_STATE)) {
//                return ""
//            }
            val mTelephonyMgr: TelephonyManager =
                application.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            imsi = mTelephonyMgr.subscriberId
            if (null == imsi) {
                imsi = ""
            }
            return imsi

        } catch (e: Exception) {
            e.printStackTrace()
            return imsi
        }

    }

    private fun getAndroidId(): String {
        var androidId =
            Settings.System.getString(application.getContentResolver(), Settings.Secure.ANDROID_ID)
        return androidId
    }


    public fun getBrowserList(): List<ResolveInfo> {
        var browserStr = ""
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addCategory(Intent.CATEGORY_BROWSABLE)
        intent.setData(Uri.parse("http://www.baidu.com/"))

        var activities = application.packageManager.queryIntentActivities(
            intent,
            PackageManager.MATCH_DEFAULT_ONLY
        ) as ArrayList<ResolveInfo>

        val r0 = activities.get(0)
        val activity_iter = activities.iterator()
        while (activity_iter.hasNext()) {
            val resolveInfo = activity_iter.next()
            if (r0.priority != resolveInfo.priority || r0.isDefault != resolveInfo.isDefault) {
                activities.remove(resolveInfo)
            }
        }
        browserStr = ""
        activities.forEach {
            browserStr += it.activityInfo.packageName
        }

        return activities
    }


}