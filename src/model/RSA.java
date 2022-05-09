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

     * Ű��� ����

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

     * ��ȣȭ

     */

    static String encode(String plainData, String stringPublicKey) {

        String encryptedData = null;

        try {

            //������ ���޹��� ����Ű�� ����Ű��ü�� ����� ����

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] bytePublicKey = Base64.getDecoder().decode(stringPublicKey.getBytes());

            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(bytePublicKey);

            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);



            //������� ����Ű��ü�� ������� ��ȣȭ���� �����ϴ� ����

            Cipher cipher = Cipher.getInstance("RSA");

            cipher.init(Cipher.ENCRYPT_MODE, publicKey);



            //���� ��ȣȭ�ϴ� ����

            byte[] byteEncryptedData = cipher.doFinal(plainData.getBytes());

            encryptedData = Base64.getEncoder().encodeToString(byteEncryptedData);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return encryptedData;

    }



    /**

     * ��ȣȭ

     */

    static String decode(String encryptedData, String stringPrivateKey) {

        String decryptedData = null;

        try {

            //������ ���޹��� ����Ű�� ����Ű��ü�� ����� ����

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            byte[] bytePrivateKey = Base64.getDecoder().decode(stringPrivateKey.getBytes());

            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(bytePrivateKey);

            PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);



            //������� ����Ű��ü�� ������� ��ȣȭ���� �����ϴ� ����

            Cipher cipher = Cipher.getInstance("RSA");

            cipher.init(Cipher.DECRYPT_MODE, privateKey);



            //��ȣ���� ��ȭ�ϴ� ����

            byte[] byteEncryptedData = Base64.getDecoder().decode(encryptedData.getBytes());

            byte[] byteDecryptedData = cipher.doFinal(byteEncryptedData);

            decryptedData = new String(byteDecryptedData);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return decryptedData;

    }

}
