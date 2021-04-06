package com.firealarm.admin.security.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 간단한 AES 암호화/복호화
 *
 * @author Min ByeongDon <deepfree@gmail.com>
 */
public class AESCipher {

	public static Logger logger = LoggerFactory.getLogger(AESCipher.class);

	private static volatile AESCipher INSTANCE;

	//final static String secretKey = "Ab1com5_Samsun3s1_a1d0s_deepf733"; // 32Byte
	final static String secretKey = "Ab1com5_Samsun3s"; // 16Byte
	static String IV = "min4byeong1don5!"; // 16Byte

	public static AESCipher getInstance() {
		if (INSTANCE == null) {
			synchronized (AESCipher.class) {
				if (INSTANCE == null)
					INSTANCE = new AESCipher();
			}
		}
		return INSTANCE;
	}

	private AESCipher() {
		IV = secretKey.substring(0, 16);
	}

	// 암호화
	public static String AES_Encode(String str)
			throws java.io.UnsupportedEncodingException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {
		byte[] keyData = secretKey.getBytes("US-ASCII");

		SecretKey secureKey = new SecretKeySpec(keyData, "AES");

		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.ENCRYPT_MODE, secureKey,
				new IvParameterSpec(IV.getBytes("US-ASCII")));

		byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
		String enStr = new String(Base64.encodeBase64(encrypted));

		return enStr;
	}

	// 복호화
	public static String AES_Decode(String str)
			throws java.io.UnsupportedEncodingException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException,
			IllegalBlockSizeException, BadPaddingException {
		byte[] keyData = secretKey.getBytes("US-ASCII");
		SecretKey secureKey = new SecretKeySpec(keyData, "AES");
		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.DECRYPT_MODE, secureKey,
				new IvParameterSpec(IV.getBytes("US-ASCII")));

		byte[] byteStr = Base64.decodeBase64(str.getBytes());

		return new String(c.doFinal(byteStr), "UTF-8");
	}
}
