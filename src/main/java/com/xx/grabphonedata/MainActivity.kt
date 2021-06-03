package com.xx.grabphonedata

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.kk.kkxz.utils.dataCapture.CaptureAllData
import com.kk.kkxz.utils.dataCapture.CaptureBatteryInfo
import com.kk.kkxz.utils.locationInfo.LocationUtils
import com.xx.grabphonedata.bean.LocationBean
import com.xx.grabphonedata.bean.MobileDevicesBean
import java.util.logging.Logger

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val a = CaptureAllData.obtainSimulatorData(this)//模拟器数据
        val b = CaptureAllData.obtainSimulatorDetailsData(this)//获取模拟器详细信息
        val c = CaptureAllData.obtainRuningAppData(this)//正在运行的app
        val d = CaptureAllData.obtainScreenesolutionData(this)//屏幕分辨率
        val e = CaptureAllData.obtainHardwareInforData()//获取硬件信息
        val f = CaptureAllData.obtainInstalledAppData(this)//获取已经安装的app
        val g = CaptureAllData.obtainAddressBookAppData(this)//获取通讯录中返回数据

        //通讯录
//        PermissionUtil.initRxPermissions(this)
//            .setPermissions(Manifest.permission.READ_CONTACTS)
//            .setPermissionsCallback(object : PermissionUtil.RxPermissionLisenter {
//                override fun result(code: Int) {
//                    when (code) {
//                        PermissionUtil.IS_AGREE -> {//同意
//                            val h =
//                                CaptureAllData.obtainAddressBookAppData(this@MainActivity)//获取通讯录中返回数据
//                        }
//                        PermissionUtil.IS_REFUSE -> {//拒绝
//
//                        }
//                        PermissionUtil.IS_NOT_ASK -> {//拒绝选中不在询问
//
//                        }
//
//                    }
//                }
//
//            })
//
//        //位置信息
//        PermissionUtil.initRxPermissions(this)
//            .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION)
//            .setPermissionsCallback(object : PermissionUtil.RxPermissionLisenter {
//                override fun result(code: Int) {
//                    when (code) {
//                        PermissionUtil.IS_AGREE -> {//同意
//                            //位置
//                            LocationUtils.instance!!.setContent(this@MainActivity)
//                                .initLocationClient()
//                                .setListener(object : LocationUtils.LocationCallbackListener {
//                                    override fun updataLocation(location: LocationBean?) {
//                                        if (location != null) {
//                                            val h = location
//                                        }
//                                    }
//
//                                }).start()
//                        }
//                        PermissionUtil.IS_REFUSE -> {//拒绝
//
//                        }
//                        PermissionUtil.IS_NOT_ASK -> {//拒绝选中不在询问
//
//                        }
//
//                    }
//                }
//
//            })

//        //电池电量
//        PermissionUtil.initRxPermissions(this)
//            .setPermissions(Manifest.permission.READ_PHONE_STATE)
//            .setPermissionsCallback(object : PermissionUtil.RxPermissionLisenter {
//                override fun result(code: Int) {
//                    when (code) {
//                        PermissionUtil.IS_AGREE -> {//同意
//                            val captureBatteryInfo =
//                                CaptureBatteryInfo(application, this@MainActivity)
//                            captureBatteryInfo.registBatter().setCurrentListenners(object :
//                                CaptureBatteryInfo.OnBatterDataListenner {
//                                override fun detailBatterInfo(data: MobileDevicesBean) {
//                                    val i = data
//                                    captureBatteryInfo.unRegistBatter()
//                                }
//                            })
//                        }
//                        PermissionUtil.IS_REFUSE -> {//拒绝
//
//                        }
//                        PermissionUtil.IS_NOT_ASK -> {//拒绝选中不在询问
//
//                        }
//
//                    }
//                }
//
//            })








        //百度地区其他配置
//        application下
//        <meta-data
//        android:name="com.baidu.lbsapi.API_KEY"
//        android:value="gmtSRxzOUPuDhBiIM98YinqTeGfqP9iz" />
//
//        <service
//        android:name="com.baidu.location.f"
//        android:enabled="true"
//        android:process=":remote" />


        //位置
//        LocationUtils.instance!!.setContent(this).initLocationClient()
//            .setListener(object : LocationUtils.LocationCallbackListener {
//                override fun updataLocation(location: LocationBean?) {
//                    if (location != null) {
//                        val h = location
//                    }
//                }
//
//            }).start()
    }
}
