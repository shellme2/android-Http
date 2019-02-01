package com.eebbk.bfc.http.dns;

import android.support.annotation.NonNull;

import com.eebbk.bfc.http.tools.L;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * DesEncryptUtil
 */
public class DesEncryptUtil {

//    static {
//        System.loadLibrary("GetDesKey");
//    }

//    public static native String getDesKey();

    public static String encrypt(@NonNull String content) {
        String encryptedString = null;
        try {
            //初始化密钥
//            String encKey = getDesKey();
            String encKey = "8A.iaSs?";
            SecretKeySpec keySpec = new SecretKeySpec(encKey.getBytes("utf-8"), "DES");
            //选择使用 DES 算法，ECB 方式，填充方式为 PKCS5Padding
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            //初始化
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            //获取加密后的字符串
            encryptedString = bytesToHexString(cipher.doFinal(content.getBytes("utf-8")));
        } catch (Exception e) {
            L.e(e);
        }
        return encryptedString;
    }

    public static String decrypt(@NonNull String encryptStr) {
        String decryptedString = null;
        try {
            //初始化密钥
//          String encKey = getDesKey();
            String encKey = "8A.iaSs?";
            SecretKeySpec keySpec = new SecretKeySpec(encKey.getBytes("utf-8"), "DES");
            //选择使用 DES 算法，ECB 方式，填充方式为 PKCS5Padding
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            //初始化
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            //获取解密后的字符串
            decryptedString = new String(cipher.doFinal(hexStringToByte(encryptStr)));
        } catch (Exception e) {
            L.e(e);
        }
        return decryptedString;
    }

    public static byte[] hexStringToByte(@NonNull String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static byte toByte(char c) {
        return (byte) "0123456789abcdef".indexOf(c);
    }

    /**
     * parse byte array to hex string
     */
    public static String bytesToHexString(byte[] bArray) {
        if (bArray == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder(bArray.length);
        String sTemp;
        for (byte aBArray : bArray) {
            sTemp = Integer.toHexString(0xFF & aBArray);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }
}
