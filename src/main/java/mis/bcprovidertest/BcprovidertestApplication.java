package mis.bcprovidertest;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BcprovidertestApplication {

  private static final String teststore =
      "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";

  public static void main(String[] args) {
    Security.addProvider(new BouncyCastleProvider());
    aesencryption();
    loadPKCS12Keystore();
    SpringApplication.run(BcprovidertestApplication.class, args);
  }

  public static void loadPKCS12Keystore() {
    try {
      InputStream is = new ByteArrayInputStream(Base64.decode(teststore));
      KeyStore pkcs12KeyStore = KeyStore.getInstance("PKCS12", new BouncyCastleProvider());
      pkcs12KeyStore.load(is, "hoaghoag".toCharArray());
      Enumeration<String> e = pkcs12KeyStore.aliases();
      KeyPair pair = null;
      Certificate[] certs = null;
      boolean erfolgreich = true;

      while (e.hasMoreElements()) {
        String alias = e.nextElement();
        Key privatekey = pkcs12KeyStore.getKey(alias, null);
        if (privatekey != null && privatekey instanceof PrivateKey
            && pkcs12KeyStore.getCertificate(alias) != null) {
          certs = pkcs12KeyStore.getCertificateChain(alias);
          PublicKey pubkey = pkcs12KeyStore.getCertificate(alias)
              .getPublicKey();
          pair = new KeyPair(pubkey, (PrivateKey) privatekey);
          erfolgreich = true;
          break;
        }
      }

      ArrayList<Certificate> certlist = new ArrayList<Certificate>();
      if (certs != null)
        for (int i = 0; i < certs.length; i++)
          certlist.add(certs[i]);
      System.out.println("********************* Loading is Successfull************************************");
    } catch (Exception e) {
      System.err.println("***************Loading Failed******************");
      e.printStackTrace();
    }

  }

  private static void aesencryption() {
    try {
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", new BouncyCastleProvider());
      byte[] iv = SecureRandom.getInstanceStrong().generateSeed(16);
      MessageDigest md = MessageDigest.getInstance("SHA-256", new BouncyCastleProvider()); //$NON-NLS-2$
      byte[] keyBytes = md.digest("test".getBytes());
      SecretKey key = new SecretKeySpec(keyBytes, "AES"); 
      cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
      cipher.doFinal("test".getBytes());
      System.out.println("********************* Encryption is Successfull************************************");
    } catch (Exception e) {
      System.err.println("***************Encryption Failed******************");
      e.printStackTrace();
    }
  }

}
