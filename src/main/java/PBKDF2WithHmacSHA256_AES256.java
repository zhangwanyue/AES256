import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;
import java.util.Base64;

//ref link: https://stackoverflow.com/questions/992019/java-256-bit-aes-password-based-encryption

public class PBKDF2WithHmacSHA256_AES256 {
    private static String salt = "1234567890";
    private static String initVector = "FixedInitVector0";//必须是16Byte

    public static String encrypt(String strToEncrypt, String password) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 1024, 256);
        byte[] secretKeyByte = factory.generateSecret(spec).getEncoded();
        SecretKeySpec mySecretKey = new SecretKeySpec(secretKeyByte, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, mySecretKey, new IvParameterSpec(initVector.getBytes()));
        return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes()));
    }

    public static String decrypt(String strToDecrypt, String password) throws Exception{
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 1024, 256);
        byte[] secretKeyByte = factory.generateSecret(spec).getEncoded();
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
