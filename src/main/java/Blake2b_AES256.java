import org.spongycastle.crypto.digests.Blake2bDigest;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;
import java.util.Base64;

public class Blake2b_AES256 {

    private static String initVector = "FixedInitVector0";//必須是16Byte

    public static String encrypt(String strToEncrypt, String password) throws Exception {
        Blake2bDigest digest = new Blake2bDigest(256);
        digest.update(password.getBytes(), 0, password.length());
        byte[] secretKeyByte = new byte[digest.getDigestSize()];
        digest.doFinal(secretKeyByte, 0);
//        System.out.println(new String(secretKeyByte));
//        System.out.println(new String(Base64.getEncoder().encode(secretKeyByte)));
//        System.out.println(secretKeyByte.length);
        SecretKeySpec mySecretKey = new SecretKeySpec(secretKeyByte, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, mySecretKey, new IvParameterSpec(initVector.getBytes()));
        return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes()));
    }

    public static String decrypt(String strToDecrypt, String password) throws Exception{
        Blake2bDigest digest = new Blake2bDigest(256);
        digest.update(password.getBytes(), 0, password.length());
        byte[] secretKeyByte = new byte[digest.getDigestSize()];
        digest.doFinal(secretKeyByte, 0);
        SecretKeySpec mySecretKey = new SecretKeySpec(secretKeyByte, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, mySecretKey, new IvParameterSpec(initVector.getBytes()));
        return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
    }

    public static void main(String[] args) throws Exception{
        String str = "hello world";
        String password = "myPassword";
        System.out.println(decrypt(encrypt(str, password), password));
    }
}
