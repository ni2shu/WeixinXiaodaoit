package co.xiaodao.weixin.util.secret;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * DES加密工具类(使用Base64做转码以及辅助加密)
 * 
 * @author liuyq
 * @date Mar 30, 2012
 */
public class AES {
	private static byte[] iv = { 0, 1, 0, 2, 0, 3, 0, 4, 0, 5, 0, 6, 0, 7, 0, 8 };
	public static String ENCRYPT_KEY = "weixin_xiaodaoit";

	/**
	 * 加密
	 * 
	 * @param encryptString
	 * @param encryptKey
	 *            密钥
	 * @return
	 * @throws Exception
	 */
	public static String encryptAES(String encryptString, String encryptKey) {
		byte[] encryptedData = null;
		try {
			IvParameterSpec zeroIv = new IvParameterSpec(iv);
			SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "AES");
			// 算法/模式/补码方式
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
			encryptedData = cipher.doFinal(encryptString.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Base64.encode(encryptedData);
	}

	/**
	 * 解密
	 * 
	 * @param decryptString
	 * @param decryptKey
	 *            密钥
	 * @return
	 * @throws Exception
	 */
	public static String decryptAES(String decryptString, String decryptKey) {
		byte decryptedData[] = null;
		try {
			byte[] byteMi = Base64.decode(decryptString);
			IvParameterSpec zeroIv = new IvParameterSpec(iv);
			SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
			decryptedData = cipher.doFinal(byteMi);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(decryptedData);
	}

	public static void main(String arg[]) {
		String str1 = "1012FWJ";
		//String str2 = encryptAES(str1, ENCRYPT_KEY);
		System.out.println(encryptAES(str1, ENCRYPT_KEY));
		System.out.println(decryptAES("Qk/bznw6iav9hczbxkC+lw==", ENCRYPT_KEY));

	}
}
