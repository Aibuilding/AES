package com.dc.aes;

import java.io.UnsupportedEncodingException;

import java.security.InvalidAlgorithmParameterException;

import java.security.InvalidKeyException;

import java.security.NoSuchAlgorithmException;

import java.security.SecureRandom;

import javax.crypto.BadPaddingException;

import javax.crypto.Cipher;

import javax.crypto.IllegalBlockSizeException;

import javax.crypto.KeyGenerator;

import javax.crypto.NoSuchPaddingException;

import javax.crypto.SecretKey;

import javax.crypto.spec.IvParameterSpec;

import javax.crypto.spec.SecretKeySpec;


public class AESUtil {
    public static final String CHARSET = "UTF-8";

    /**
     * AES核心逻辑
     *
     * @param mode
     * @param byteContent
     * @param key
     * @param iv
     * @param type
     * @param modeAndPadding
     * @return
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchPaddingException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    private static byte[] encryptOrDecrypt(int mode, byte[] byteContent, String key, byte[] iv, int type, String modeAndPadding) throws InvalidKeyException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");

//此处解决mac，linux报错

        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

        random.setSeed(key.getBytes());

        kgen.init(type, random);

        SecretKey secretKey = kgen.generateKey();

        byte[] enCodeFormat = secretKey.getEncoded();

        SecretKeySpec keySpec = new SecretKeySpec(enCodeFormat, "AES");

        Cipher cipher = Cipher.getInstance(modeAndPadding);// 创建密码器

        if (null != iv) {
//指定一个初始化向量 (Initialization vector，IV)， IV 必须是16位

            cipher.init(mode, keySpec, new IvParameterSpec(iv));

        } else {
            cipher.init(mode, keySpec);

        }

        byte[] result = cipher.doFinal(byteContent);

        return result;

    }


    /**
     * aes 加解密判断
     *
     * @param isEncrypt
     * @param source
     * @param key
     * @param type
     * @param encodeType
     */

    public static byte[] encryptOrdecrypt(boolean isEncrypt, byte[] source, String key, byte[] iv, int type, String encodeType) throws UnsupportedEncodingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        if (isEncrypt) {
            byte[] encodeByte = encryptOrDecrypt(Cipher.ENCRYPT_MODE, source, key, iv, type, encodeType);

            String encodeStr = TypeConvert.bytesToHexString(encodeByte);

            return encodeByte;

        } else {
            byte[] decodeByte = encryptOrDecrypt(Cipher.DECRYPT_MODE, source, key, iv, type, encodeType);

            String decodeStr = new String(decodeByte, CHARSET);

            return decodeByte;

        }

    }


    //        转码工具类
    public static class TypeConvert {

        /**
         * 数组转换成十六进制字符串
         *
         * @param bArray byte[]
         * @return HexString
         */

        public static final String bytesToHexString(byte[] bArray) {
            if (bArray == null || bArray.length == 0) {
                return null;

            }

            StringBuffer sb = new StringBuffer(bArray.length);

            String sTemp;

            for (int i = 0; i < bArray.length; i++) {
                sTemp = Integer.toHexString(0xFF & bArray[i]);

                if (sTemp.length() < 2) {
                    sb.append(0);

                }

                sb.append(sTemp.toUpperCase());

            }

            return sb.toString();

        }


    }


}