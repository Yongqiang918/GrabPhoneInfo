package com.xx.grabphonedata.bean

import java.io.Serializable

/**
 *Created by Lyq
 *on 2021-05-17
 */
class SmsInfo(
    var content: String = "",//内容             body           ok
//    var phone: String = "",//手机号
    var time: String = "",//短信时间戳           date           ok
    var type: String = "",//1,接收 2，发送       type           ok
    var read: String = "",//0，未读 1，已读      read           ok
    var send_time: String = "",//发送接收时间    date           ok
    var name: String = "",//短息发送、接收人姓名  person         ok
    var phone: String = ""//短信发送、接收号码   （收：address） ok
) : Serializable {
}


//    _id 一个自增字段，从1开始
///    thread_id 序号，同一发信人的id相同
//    address 发件人手机号码
//    person 联系人列表里的序号，陌生人为null
//\    date 发件日期
///    protocol 协议，分为： 0 SMS_RPOTO, 1 MMS_PROTO
//\   read 是否阅读 0未读， 1已读
//\   status 状态 -1接收，0 complete, 64 pending, 128 failed
//    type ALL = 0;INBOX = 1;SENT = 2;DRAFT = 3;OUTBOX = 4;FAILED = 5; QUEUED = 6;
//    body 短信内容
///    service_center 短信服务中心号码编号。如+8613800755500
///    subject 短信的主题

