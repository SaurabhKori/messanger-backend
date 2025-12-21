package com.saurabh.messge_backend.utils;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saurabh.messge_backend.model.UserLogin;
@Service
public class EncrepytDecrypt {
    static Logger logger=LoggerFactory.getLogger(EncrepytDecrypt.class);
	static final String aesKey128="Q0eQT0hWIbBP/gOqfJgLmA==";
	static final String aesKey256 ="htYAfBB52u/HGYW2KZyjKwjT0gvZtYs4TUrRmAsiWl0=";
	static final String ivkey="vTw9XcT+y/AyihuUrc9yww==";
	static final String algo="AES";
	private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
	
	
	public static void main(String[] args) {
//		generateRandomKey();
//		genrateKey();
//		generateIV();
//		UserLogin userLogin=new UserLogin(12, "Saurabh", "Saurabh@123", "9651817371");
//		String encryptData=encryptAesIV(userLogin.toString());
	    String encryptData1="om9qvctr9HjYLKmrfM5Z/jaK0TWFNHfQfE61IsHnDmhXQw0UgbvfhTfbnjdE/iTy5po8+gDXRrJg7QdThFq7aDuO/DYQEcs1dWJYmlOJn5/aN4lkpIwH5iyS5dvo0QRU";
		decryptAesIV(encryptData1);
	}
      public static String encryptionAes(String data) {
    	  try {
    		  Key key=generateKey();
			Cipher cipher=Cipher.getInstance(algo);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			
			try {
				byte[] bytes=cipher.doFinal(data.getBytes());
				String encryptData=Base64.getEncoder().encodeToString(bytes);
				System.out.println(encryptData);
				return encryptData;
			} catch (IllegalBlockSizeException | BadPaddingException e) {
		
				e.printStackTrace();
			}
		} catch (NoSuchAlgorithmException | NoSuchPaddingException |InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null; 
    	    
      }
      public static String decryptionAes(String encryptData) {
    	  
    	  Key key=generateKey();
    	  try {
			Cipher cipher=Cipher.getInstance(algo);
			cipher.init(Cipher.DECRYPT_MODE, key);
		    try {
				byte[] bytes=cipher.doFinal(Base64.getDecoder().decode(encryptData));
				System.out.print(new String(bytes));
			 String a=	new String(bytes);
			 
               return new String(bytes);
			} catch (IllegalBlockSizeException | BadPaddingException e) {
				
				e.printStackTrace();
			}
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	  
    	  return null;
      }
      public static String encryptAesIV(String data) {
    	  IvParameterSpec iv=new IvParameterSpec(Base64.getDecoder().decode(ivkey));
    	  Key key=generateKey();
    	  try {
    		  Cipher cipher=Cipher.getInstance(ALGORITHM);
    		  try {
				cipher.init(Cipher.ENCRYPT_MODE,key,iv);
			byte[] bytes=cipher.doFinal(data.getBytes());
			String base =Base64.getEncoder().encodeToString(bytes);
			 System.out.println(base);
			return base;
				
			} catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			// TODO: handle exception
		}
    	  return null;
      }
      public static String decryptAesIV(String data) {
    	  IvParameterSpec iv=new IvParameterSpec(Base64.getDecoder().decode(ivkey));
    	  Key key=generateKey();
    	  try {
    		  Cipher cipher=Cipher.getInstance(ALGORITHM);
    		  try {
				cipher.init(Cipher.DECRYPT_MODE,key,iv);
			byte[] bytes=cipher.doFinal(Base64.getDecoder().decode(data));
			 System.out.println(new String(bytes));
			return new String(bytes);
				
			} catch (InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			// TODO: handle exception
		}
    	  return null;
      }
      public static Key generateKey() {
       Key key=new SecretKeySpec(aesKey128.getBytes(), algo);
//    	  System.out.println(key +key.getAlgorithm()+key.toString());
    	  return key;
      }
      public static void generateRandomKey() {
    	  try {
			KeyGenerator generator=KeyGenerator.getInstance("AES");
			 SecureRandom secure =new SecureRandom();
			 generator.init(256, secure);
			 SecretKey key =generator.generateKey();
			 String keys=Base64.getEncoder().encodeToString(key.getEncoded());
			 System.out.println(keys);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 
      }
      public static String generateIV() {
		    byte[] iv = new byte[16];
		    new SecureRandom().nextBytes(iv);
		    System.out.println( Base64.getEncoder().encodeToString(iv));
		    return Base64.getEncoder().encodeToString(iv);
		}
}
