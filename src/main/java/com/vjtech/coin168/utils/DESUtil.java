package com.vjtech.coin168.utils;

import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.tomcat.util.codec.binary.Base64;


public class DESUtil {

	public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";
	
	public static final String DES_REG_KEY = "vicchen0902";
	/**
	 * DES演算法，加密
	 *
	 * @param data 待加密字串
	 * @param key  加密私鑰，長度不能夠小於8位
	 * @return 加密後的位元組陣列，一般結合Base64編碼使用
	 * @throws CryptException 異常
	 */
	public static String encode(String key, String data) throws Exception {
		return encode(key, data.getBytes());
	}

	/**
	 * DES演算法，加密
	 *
	 * @param data 待加密字串
	 * @param key  加密私鑰，長度不能夠小於8位
	 * @return 加密後的位元組陣列，一般結合Base64編碼使用
	 * @throws CryptException 異常
	 */
	public static String encode(String key, byte[] data) throws Exception {
		try {
			DESKeySpec dks = new DESKeySpec(key.getBytes());

			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			// key的長度不能夠小於8位位元組
			Key secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
			IvParameterSpec iv = new IvParameterSpec("gxkeenhy".getBytes());
			AlgorithmParameterSpec paramSpec = iv;
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
			byte[] bytes = cipher.doFinal(data);
			final Base64 base64 = new Base64();
			return base64.encodeToString(bytes);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	/**
	 * DES演算法，解密
	 *
	 * @param data 待解密字串
	 * @param key  解密私鑰，長度不能夠小於8位
	 * @return 解密後的位元組陣列
	 * @throws Exception 異常
	 */
	public static byte[] decode(String key, byte[] data) throws Exception {
		try {
			SecureRandom sr = new SecureRandom();
			DESKeySpec dks = new DESKeySpec(key.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			// key的長度不能夠小於8位位元組
			Key secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
			IvParameterSpec iv = new IvParameterSpec("gxkeenhy".getBytes());
			AlgorithmParameterSpec paramSpec = iv;
			cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
			return cipher.doFinal(data);
		} catch (Exception e) {
//         e.printStackTrace();
			throw new Exception(e);
		}
	}

	/**
	 * 獲取編碼後的值
	 * 
	 * @param key
	 * @param data
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public static String decodeValue(String key, String data) throws Exception {
		byte[] datas;
		String value = null;
		final Base64 base64 = new Base64();
		datas = decode(key, base64.decode(data));

		value = new String(datas);
		if (value.equals("")) {
			throw new Exception();
		}
		return value;
	}

	public static void main(String[] args) throws Exception {
		String test= DESUtil.encode("vicchen0902", "jimmy1");
		System.out.println(DESUtil.decodeValue("vicchen0902", test));
		

	}

}
