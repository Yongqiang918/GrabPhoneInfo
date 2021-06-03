package com.xx.grabphonedata.bean

/**
 *Created by Lyq
 *on 2020-10-13
 */
class MobileDevicesBean(
    var phoneModel: String = "",//型号
    var imei: String = "",
    var meid: String = "",
    var wlanMac: String = "", //oHwlan_mac

    var mobileNetworktype: String = "", //移动网络类型
    var operatingSystemVersion: String = "",  //系统版本号
    var screenSize: String = "",//屏幕尺寸
    var wifiName: String = "",//是否是wifi

    var usableStorage: String = "",  //存储(可用)
    var basebandVersion: String = "",//基带版本
    var kernelVersion: String = "",//内核版本
    var processor: String = "",//处理器

    var runningMemory: String = "",  //运行内存
    var batteryCapacity: String = "", //电池容量
    var batteryPower: String = "", //电池电量
    var phoneBrands: String = "",//手机品牌

    var bootTime: String = "", //开机时间
    var batteryHealth: String = "",//电池健康
    var currentBattery: String = "",//电池电量
    var maximumPower: String = "", // 电池电量的最大值

    var charger: String = "",// 电源是AC charger.[应该是指充电器]
    var batteryStatus: String = "",//电池状态
    var batteryVoltage: String = "", // 当前电池的电压
    var phoneType: String = "", //手机类型

    var mVersionProNum: String = "", //手机系统版本

    var appChannelType: String = "",//-----渠道
    var appType: String = "",  //-----app类型
    var appVersionNum: String = "",//------app版本号

    var phoneLanguage: String = "", //语言
    var imei2: String = "",
    var batteryTechnology: String = "", // 电池使用的技术。
    var batteryTemperature: String = "", // 当前电池的温度

    var intranetNetIp: String = "",//内网ip
    var netOutIp: String = "",//外网ip

    var versionNum: String = "",//---app版本号
    var allStorage: String = "", //存储(总)
    var deviceID: String = "",//----deviceID
    var uuid: String = "",
    var isRoot: String = "",//是否root
    var isDebug: String = "",//是否debug
    var phoneNum1: String = "",//获取本机手机号
    var phoneNum2: String = "",//获取本机手机号

    var gaid: String = "",//gaid
    var imsi: String = "",//imsi
    var androidId: String = "",//androidId
    var sdCardTotalSize: String = "",//sd 卡总容量
    var sdCardAvailableSize: String = "",//sd 卡可用余量
    var information: String = "",//information（浏览器）
    var totalStorageCapacityB: String = "",//存储总容量[B]
    var storageSurplusCapacityB: String = "",//存储剩余余量[B]

    var ischarbing: String = "",//是否充电
    var batteryPct: String = "",//设备电池电量
    var isUSBCharge: String = "",//是否usb充电
    var isACCharge: String = ""//电池是否AC充电




) {
}