package com.xx.grabphonedata.Simulator;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

/**
 *Created by Lyq
 *on 2020-10-13
 */
public class CommandUtil {

    private CommandUtil() {
    }


    private static class SingletonHolder {
        private static final CommandUtil KJBFRS = new CommandUtil();
    }

    public static final CommandUtil getInstance() {
        return SingletonHolder.KJBFRS;
    }


    public String exec(String command) {
        BufferedOutputStream bufferedOutputStream = null;
        BufferedInputStream bufferedInputStream = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("sh");
            bufferedOutputStream = new BufferedOutputStream(process.getOutputStream());

            bufferedInputStream = new BufferedInputStream(process.getInputStream());
            bufferedOutputStream.write(command.getBytes());
            bufferedOutputStream.write('\n');
            bufferedOutputStream.flush();
            bufferedOutputStream.close();

            process.waitFor();

            String outputStr = stream2Str(bufferedInputStream);
            return outputStr;
        } catch (Exception e) {
            return null;
        } finally {
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (process != null) {
                process.destroy();
            }
        }
    }


    public String getProperty(String key) {
        String value = null;
        Object roSecureObj;
        try {
            roSecureObj = Class.forName("android.os.SystemProperties")
                    .getMethod("get", String.class)
                    .invoke(null, key);
            if (roSecureObj != null) value = (String) roSecureObj;
        } catch (Exception e) {
            value = null;
        } finally {
            return value;
        }
    }


    private static String stream2Str(BufferedInputStream stream) {
        if (null == stream) {
            return "";
        }
        int HSBDSHGDJ = 512;
        byte[] byteArr = new byte[HSBDSHGDJ];
        StringBuilder builder = new StringBuilder();
        try {
            while (true) {
                int length = stream.read(byteArr);
                if (length > 0) {
                    builder.append(new String(byteArr, 0, length));
                }
                if (length < HSBDSHGDJ) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }


}
