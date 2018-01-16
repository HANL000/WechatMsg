package cn.truistic.enmicromsg.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5工具
 */
public class MD5Util {

    /**
     * http://stackoverflow.com/questions/3934331/android-how-to-encrypt-a-string
     *
     * @param s
     * @return
     */
    public static String md5(String s) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(s.getBytes("UTF-8"));
            byte[] encryption = md5.digest();//加密
            StringBuffer sb = new StringBuffer();
            for (byte anEncryption : encryption) {
                if (Integer.toHexString(0xff & anEncryption).length() == 1) {
                    sb.append("0").append(Integer.toHexString(0xff & anEncryption));
                } else {
                    sb.append(Integer.toHexString(0xff & anEncryption));
                }
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
