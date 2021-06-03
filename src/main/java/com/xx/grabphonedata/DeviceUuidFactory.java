package com.xx.grabphonedata;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class DeviceUuidFactory {
    private static final String DEVICE_UUID_FILE_NAME = ".tobindevid.conf";
    private static final String DEVICE_IMEI_FILE_NAME = ".tobinimei.conf";
    private static final String PREFS_FILE = "tobindevice_id.xml";
    private static final String PREFS_DEVICE_ID = "device_id";
    private static final String PREFS_IMEI = "imei";
    private static UUID uuid;
    private static String imei;

    private static DeviceUuidFactory deviceUuidFactory;
    public static DeviceUuidFactory getInstance(Context context){
        if (deviceUuidFactory == null){
            deviceUuidFactory = new DeviceUuidFactory(context);
        }
        return deviceUuidFactory;
    }

    private DeviceUuidFactory(Context context) {

        if (uuid == null) {
            synchronized (DeviceUuidFactory.class) {
                if (uuid == null) {
                    final SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE, 0);
                    String id = prefs.getString(PREFS_DEVICE_ID, null);
                    String imeiStr = prefs.getString(PREFS_IMEI, null);

                    if (TextUtils.isEmpty(id)) {
                        if (checkPermission(context, permission.WRITE_EXTERNAL_STORAGE)) {
                            id = recoverFromSD(DEVICE_UUID_FILE_NAME);
                        }
                    }
                    if (TextUtils.isEmpty(imeiStr)) {
                        if (checkPermission(context, permission.WRITE_EXTERNAL_STORAGE)) {
                            imeiStr = recoverFromSD(DEVICE_IMEI_FILE_NAME);
                        }
                    }
                    if (!TextUtils.isEmpty(imeiStr)) {
                        imei = imeiStr;
                        prefs.edit().putString(PREFS_IMEI, imei).commit();
                        if (checkPermission(context, permission.WRITE_EXTERNAL_STORAGE)) {
                            saveToSD(imei, DEVICE_IMEI_FILE_NAME);
                        }
                    }
                    if (id != null) {
                        // Use the ids previously computed and stored in the prefs file
                        uuid = UUID.fromString(id);
                        prefs.edit().putString(PREFS_DEVICE_ID, uuid.toString()).commit();
                        if (checkPermission(context, permission.WRITE_EXTERNAL_STORAGE)) {
                            saveToSD(uuid.toString(), DEVICE_UUID_FILE_NAME);
                        }

                    } else {
                        final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                        String tmDevice, tmSerial;
                        tmDevice = "";
                        tmSerial = "";

                        try {
                            if (checkPermission(context, permission.READ_PHONE_STATE)) {
                                TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                                tmDevice = tm.getDeviceId();
                                tmSerial = tm.getSimSerialNumber();
                            }
                        } catch (SecurityException ex) {
                            ex.printStackTrace();
                        }

                        if (TextUtils.isEmpty(tmDevice) || TextUtils.isEmpty(tmSerial) || tmDevice.equals("000000000000000")) {
                            if (!TextUtils.isEmpty(androidId) && !"9774d56d682e549c".equals(androidId) && Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {
                                uuid = UUID.nameUUIDFromBytes(androidId.getBytes());
                            } else {
                                uuid = UUID.randomUUID();
                            }

                        } else {
                            uuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
                        }
                        if (!TextUtils.isEmpty(tmDevice)) {
                            imei = tmDevice;
                            prefs.edit().putString(PREFS_IMEI, imei).commit();
                            if (checkPermission(context, permission.WRITE_EXTERNAL_STORAGE)) {
                                saveToSD(imei, DEVICE_IMEI_FILE_NAME);
                            }

                        }

                        // Write the value out to the prefs file
                        prefs.edit().putString(PREFS_DEVICE_ID, uuid.toString()).commit();

                        if (checkPermission(context, permission.WRITE_EXTERNAL_STORAGE)) {
                            saveToSD(uuid.toString(), DEVICE_UUID_FILE_NAME);
                        }
                    }
                }
            }
        }
    }

    public UUID getDeviceUuid() {
        return uuid;
    }

    private static void saveToSD(String uuid, String fileName) {
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        File targetFile = new File(dirPath, fileName);
        if (targetFile != null) {
            if (!targetFile.exists()) {
                OutputStreamWriter osw;
                try {
                    osw = new OutputStreamWriter(new FileOutputStream(targetFile), "utf-8");
                    try {
                        osw.write(uuid);
                        osw.flush();
                        osw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String recoverFromSD(String fileName) {
        try {
            String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            File dir = new File(dirPath);
            File uuidFile = new File(dir, fileName);
            if (!dir.exists() || !uuidFile.exists()) {
                return null;
            }
            FileReader fileReader = new FileReader(uuidFile);
            StringBuilder sb = new StringBuilder();
            char[] buffer = new char[100];
            int readCount;
            while ((readCount = fileReader.read(buffer)) > 0) {
                sb.append(buffer, 0, readCount);
            }
            fileReader.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean checkPermission(Context context, permission permName) {
        int perm = context.checkCallingOrSelfPermission("android.permission." + permName.toString());
        return perm == PackageManager.PERMISSION_GRANTED;
    }

    private enum permission {
        READ_PHONE_STATE,
        WRITE_EXTERNAL_STORAGE
    }


}