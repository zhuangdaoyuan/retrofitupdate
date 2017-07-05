package com.zdy.retrofitupdate.update;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 获取文件或字符串的MD5值
 * Created by Administrator on 2016/7/13.
 */
public class MD5Utils {
    public final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    //获取字符串的MD5值
    public static String getStringMD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            //获得MD5摘要算法的MessageDigest对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            //使用制定的字节更新摘要
            mdInst.update(btInput);
            //获得密文
            byte[] md = mdInst.digest();
            //把密文转换成十六进制的字符串形式
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    //获取文件的MD5值
    public static String getFileMD5(File file) {
        String md5 = null;
        FileInputStream fis = null;
        FileChannel fileChannel = null;
        try {
            fis = new FileInputStream(file);
            fileChannel = fis.getChannel();
            MappedByteBuffer byteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(byteBuffer);
            md5 = byteArrayToHexString(md.digest());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileChannel != null) {
                    fileChannel.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return md5;
    }

    //字节数组转十六进制字符串
    private static String byteArrayToHexString(byte[] digest) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < digest.length; i++) {
            buffer.append(byteToHexString(digest[i]));
        }
        return buffer.toString();
    }

    private static String byteToHexString(byte b) {
        int d1 = (b & 0xf0) >> 4;
        int d2 = b & 0xf;
        return hexDigits[d1] + hexDigits[d2];
    }
}
