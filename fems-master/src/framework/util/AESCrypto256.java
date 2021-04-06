package framework.util;

import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import framework.exception.AbleRuntimeException;


/**
 * APP - WAS 암호화 통신 관련 모듈
 *
 */
public class AESCrypto256 {

    protected static final Logger logger = LoggerFactory.getLogger(AESCrypto256.class);

    private static final Charset UTF8 = Charset.forName("UTF-8");

    private static final String SECUTIRY_ALGORITHM = "AES/CBC/PKCS5Padding";

//    private AppConfig appConfig = AppConfig.getInstance();

    // /////////////////////////////////////////////////////////
    // security variables
    Cipher encrypter = null;
    Cipher decrypter = null;
    String secretKey = null;
    SecretKeySpec secretKeySpec = null;
    IvParameterSpec ivSpec = null;

    /**
     * 생성자
     * 암호화키: 직접지정
     */
    public AESCrypto256(String secretKey) {
        this.secretKey = secretKey;
        initCipher();
    }

    void initCipher() {
        try {
            byte[] iv = new byte[16];
            for (int i = 0; i < 16; i++) {
                iv[i] = 0x00;
            }
            this.secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
            this.ivSpec = new IvParameterSpec(iv);
            logger.trace("secret      : {}", secretKey);
            logger.trace("secretKey   : {}", Hex.encodeHexString(secretKey.getBytes()));
            logger.trace("secretBytes : {}", Hex.encodeHexString(this.secretKeySpec.getEncoded()));
            logger.trace("iv: {}", Hex.encodeHexString(this.ivSpec.getIV()));
            this.encrypter = Cipher.getInstance(SECUTIRY_ALGORITHM);
            this.decrypter = Cipher.getInstance(SECUTIRY_ALGORITHM);
        } catch (Exception ex) {
            logger.error("InitCipher FAILED. EXCEPTION: {}", ex.getMessage(), ex);
        }
    }

    /** 주어진 암호화기를 이용하여 평문을 암호화하고 Base64인코딩 문자열로 반환 */
    public String encryptToBase64CryptoString(String rawSrc) {
        byte[] encryptByte = encryptString(rawSrc);
        String base64String = Base64.encodeBase64String(encryptByte);
        return base64String;
    }

    public byte[] encryptString(String plainString) {
        byte[] encryptedBytes = new byte[1];
        try {
            //int blockSize = encrypter.getBlockSize();
            // Get UTF-8 bytes from plain string
            byte[] plainByte = plainString.getBytes(UTF8);
            // do encryption
            encrypter.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);
            encryptedBytes = encrypter.doFinal(plainByte);
            logger.trace("encryptedBytes: {}", Hex.encodeHexString(encryptedBytes));
        } catch (Exception ex) {
            logger.error("encryptString FAILED. EXCEPTION: {}", ex.getMessage(), ex);
            return new byte[0];
        }
        return encryptedBytes;
    }

    /** decrypt pure encoded bytes */
    public String decryptByte(byte[] encodedBytes) {
        String decryptedString = "";
        try {
            decrypter.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
            byte[] decryptedBytes = decrypter.doFinal(encodedBytes);
            decryptedString = new String(decryptedBytes, 0, decryptedBytes.length);
        } catch (Exception ex) {
            logger.error("decryptByte FAILED. EXCEPTION: {}", ex.getMessage(), ex);
        }
        return decryptedString;
    }

    /** decrypt base64 encoded secured bytes (예외발생시 빈문자열) */
    public String decryptBase64String(String base64String) {
    	try {
			return decryptBase64StringUnsafe(base64String);
		} catch (Exception ex) {
			logger.debug("decryptBase64String FAILED. 복호화 실패. base64String:{}, secretKey:{}, EXCEPTION: {}", base64String, secretKey, ex.getMessage(), logger.isTraceEnabled()?ex:null);
		}
        return "";
    }

    /** decrypt base64 encoded secured bytes (예외발생시 throw) */
    public String decryptBase64StringUnsafe(String base64String) {
		try {
			decrypter.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
			byte[] base64Byte = Base64.decodeBase64(base64String);
			byte[] decryptedBytes = decrypter.doFinal(base64Byte);
			String decryptedString = new String(decryptedBytes, 0, decryptedBytes.length);
			return decryptedString;
		} catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException ex) {
			logger.debug("decryptBase64StringUnsafe FAILED. 복호화 실패. base64String:{}, secretKey:{}, EXCEPTION: {}", base64String, secretKey, ex.getMessage(), logger.isTraceEnabled()?ex:null);
			throw new AbleRuntimeException("decryptBase64StringUnsafe failed.", ex);
		}
    }

    /** decrypt base64 encoded secured bytes. trim '\0' */
    public String decryptBase64StringTrim(String base64String) {
        String descryptedString = decryptBase64String(base64String);
        if( descryptedString.indexOf('\0') > -1){
        	String[] trimString = descryptedString.split("\0");
        	return trimString[0];
        } else {
        	return descryptedString;
        }
    }

}
