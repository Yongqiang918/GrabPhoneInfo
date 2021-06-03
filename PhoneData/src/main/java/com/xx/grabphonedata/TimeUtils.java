package com.xx.grabphonedata;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Lyq
 * on 2021-04-12
 */
public class TimeUtils {
    /**
     * 获取墨西哥-05时区时间
     *
     * @return
     */
    public static String getTimeStampByZone() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT-05"));
        String date = simpleDateFormat.format(new Date());
        String stamp = dateToStamp(date);
        return stamp;
    }

    public static String dateToStamp(String dateStr) {
        String res = "";
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = simpleDateFormat.parse(dateStr);
            long time = date.getTime();
            res = String.valueOf(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return res;

    }


    /**
     * 将毫秒值转化月日
     * @param dateL
     * @return
     */
    public static String formatDateToMMdd(long dateL) {
        String dateStr = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日");
        dateStr = simpleDateFormat.format(dateL);
        return dateStr;
    }
    /**
     * 将毫秒值转化日月年
     * @param dateL
     * @return
     */
    public static String formatDateToYyyyMmDd(long dateL) {
        String dateStr = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateStr = simpleDateFormat.format(dateL);
        return dateStr;
    }



}
