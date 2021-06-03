package com.xx.grabphonedata;

import android.content.Context;
import android.view.WindowManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import static android.text.format.Formatter.formatFileSize;

/**
 * Created by Lyq
 * on 2020-10-14
 */
public class DeviceUtil {

    /**
     * 获取android总运行内存大小
     */
    public static String getTotalMemory(Context context) {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
//            arrayOfString = str2.split("\\s+", 2);
            arrayOfString = str2.split("\\s+");
            if (arrayOfString != null && arrayOfString.length >= 2) {
                // 获得系统总内存，单位是KB
                int i = Integer.valueOf(arrayOfString[1]).intValue();
                //int值乘以1024转换为long类型
                initial_memory = new Long((long) i * 1024);
            }
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return formatFileSize(context, initial_memory);// Byte转换为KB或者MB，内存大小规格化
    }


    /**
     * 处理器  获取CPU名字
     */
    public static String getCpuName() {
        String cpuName = "";
        try {
            FileReader reader = new FileReader("/proc/cpuinfo");
            BufferedReader br = new BufferedReader(reader);
            String line = br.readLine();
            String[] array = line.split(":\\s+", 2);
            if (array != null && array.length >= 2) {
                cpuName =array[1];
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cpuName;
    }

    /**
     * 获取外网IP地址
     *
     * @return
     */
    public static String getNetOutAddress() {//getNetIp
        String line = "";
        URL infoUrl = null;
        InputStream inStream = null;
        try {
            infoUrl = new URL("http://pv.sohu.com/cityjson?ie=utf-8");
            URLConnection connection = infoUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));
                StringBuilder strber = new StringBuilder();
                while ((line = reader.readLine()) != null)
                    strber.append(line + "\n");
                inStream.close();
                // 从反馈的结果中提取出IP地址
                int start = strber.indexOf("{");
                int end = strber.indexOf("}");
                String json = strber.substring(start, end + 1);
                if (json != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        line = jsonObject.optString("cip");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return line;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }



}
