package model;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;

import javax.crypto.Cipher;




public class RSA {

	/**

     * 키페어 생성

     */

    static HashMap<String, String> createKeypairAsString() {

        HashMap<String, String> stringKeypair = new HashMap<>();

        try {

            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            PublicKey publicKey = keyPair.getPublic();

            PrivateKey privateKey = keyPair.getPrivate();


            String stringPublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());

            String stringPrivateKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());


            stringKeypair.put("publicKey", stringPublicKey);

            stringKeypair.put("privateKey", stringPrivateKey);



        } catch (Exception e) {

            e.printStackTrace();

        }

        return stringKeypair;

    }

    static void saveKeyFair(HashMap<String, String> hashMap, String path) {
    	try {
    		File file = new File(path+"/public.key");
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(hashMap.get("publicKey"));
			bw.close();
			file = new File(path+"/private.key");
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write(hashMap.get("privateKey"));
			bw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }

    /**

     * 암호화

     */

    static String encode(String plainData, String stringPublicKey) {

        String encryptedData = null;

        try {

            //평문으로 전달받은 공개키를 공개키객체로 만드는 과정

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] bytePublicKey = Base64.getDecoder().decode(stringPublicKey.getBytes());

            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bytePublicKey);

            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);



            //만들어진 공개키객체를 기반으로 암호화모드로 설정하는 과정

            Cipher cipher = Cipher.getInstance("RSA");

            cipher.init(Cipher.ENCRYPT_MODE, publicKey);



            //평문을 암호화하는 과정

            byte[] byteEncryptedData = cipher.doFinal(plainData.getBytes());

            encryptedData = Base64.getEncoder().encodeToString(byteEncryptedData);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return encryptedData;

    }



    /**

     * 복호화

     */

    static String decode(String encryptedData, String stringPrivateKey) {

        String decryptedData = null;

        try {

            //평문으로 전달받은 개인키를 개인키객체로 만드는 과정

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            byte[] bytePrivateKey = Base64.getDecoder().decode(stringPrivateKey.getBytes());

            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(bytePrivateKey);

            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);



            //만들어진 개인키객체를 기반으로 암호화모드로 설정하는 과정

            Cipher cipher = Cipher.getInstance("RSA");

            cipher.init(Cipher.DECRYPT_MODE, privateKey);



            //암호문을 평문화하는 과정

            byte[] byteEncryptedData = Base64.getDecoder().decode(encryptedData.getBytes());

            byte[] byteDecryptedData = cipher.doFinal(byteEncryptedData);

            decryptedData = new String(byteDecryptedData);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return decryptedData;

    }

}
