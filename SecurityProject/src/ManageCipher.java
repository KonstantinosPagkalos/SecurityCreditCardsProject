import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;

public class ManageCipher {

    // Δημόσιο κλειδί
    private static PublicKey publicKey;
    // Ιδιωτικό κλειδί
    private static PrivateKey privateKey;

    // Δημιουργία δημόσιου και ιδιωτικού κλειδιού
    public static void generateAnRsaKeyPair() throws Exception {
        KeyPair keyPair = generateRsaKey();

        // Λήψη δημόσιου και ιδιωτικού κλειδιού
        publicKey =  keyPair.getPublic() ;
        privateKey =  keyPair.getPrivate() ;

        // Αποθήκευση κλειδιών
        saveKeys();
    }

    // Δημιουργία κλειδιών
    private static KeyPair generateRsaKey() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.generateKeyPair();
    }

    public static PublicKey getPublicKey() {
        return  publicKey;
    }

    public static PrivateKey getPrivateKey() {
        return  privateKey;
    }

    // Αποθήκευση δημόσιου και ιδιωτικού κλειδιού σε αρχεία
    private static void saveKeys() throws IOException {

        // Αποθήκευση δημόσιου κλειδιού σε αρχείο
        Path pathPublicKey = Paths.get(DataFile.publicKeyFile);
        boolean existsPublicKeyFile = Files.exists(pathPublicKey);
        if (!existsPublicKeyFile){

            BufferedWriter writer = Files.newBufferedWriter(pathPublicKey);
            writer.write(Base64.getEncoder().encodeToString(publicKey.getEncoded()));
            writer.flush();
            writer.close();

        }

        // Αποθήκευση ιδιωτικού κλειδιού σε αρχείο
        Path pathPrivateKey = Paths.get(DataFile.privateKeyFile);
        boolean existsPrivateKeyFile = Files.exists(pathPrivateKey);
        if (!existsPrivateKeyFile){

            BufferedWriter writer = Files.newBufferedWriter(pathPrivateKey);
            writer.write(Base64.getEncoder().encodeToString(privateKey.getEncoded()));
            writer.flush();
            writer.close();

        }
    }

    // Έλεγχος ύπαρξης δημόσιου και ιδιωτικού κλειδιού
    public static boolean existKeys(){

        // Έλεγχος ύπαρξης δημόσιου κλειδιού
        Path pathPublicKey = Paths.get(DataFile.publicKeyFile);
        boolean existsPublicKeyFile = Files.exists(pathPublicKey);

        //Έλεγχος ύπαρξης ιδιωτικού κλειδιού
        Path pathPrivateKey = Paths.get(DataFile.privateKeyFile);
        boolean existsPrivateKeyFile = Files.exists(pathPrivateKey);

        return (existsPublicKeyFile && existsPrivateKeyFile);
    }

    //https://stackoverflow.com/questions/11410770/load-rsa-public-key-from-file
    // Ανάγνωση δημόσιου κλειδιού από αρχείο
    public  static PublicKey readPublicKey() throws Exception  {

        PublicKey pubKey = null;

        Path pathPublicKey = Paths.get(DataFile.publicKeyFile);
        boolean existsPublicKeyFile = Files.exists(pathPublicKey);
        if (existsPublicKeyFile){

            String base64PublicKeyContent = new String(Files.readAllBytes(pathPublicKey));
            X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKeyContent.getBytes()));

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            pubKey = keyFactory.generatePublic(keySpecX509);

            return pubKey;
        }
        return null;
    }

    // https://gist.github.com/destan/b708d11bd4f403506d6d5bb5fe6a82c5
    // Ανάγνωση ιδιωτικού κλειδιού από αρχείο
    public static PrivateKey readPrivateKey() throws Exception  {

        PrivateKey privKey = null;

        Path pathPrivateKey = Paths.get(DataFile.privateKeyFile);
        boolean existsPrivateKeyFile = Files.exists(pathPrivateKey);
        if (existsPrivateKeyFile){

            String base64PrivateKeyContent = new String(Files.readAllBytes(pathPrivateKey));

            PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64PrivateKeyContent.getBytes()));

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            privKey = keyFactory.generatePrivate(keySpecPKCS8);

            return privKey;
        }
        return null;
    }

    //https://stackoverflow.com/questions/49426844/how-to-validate-a-public-and-private-key-pair-in-java
    // Έλεγχος ορθότητας δημόσιου και ιδιωτικού κλειδιού
    public static boolean validateKeys() throws Exception {

        // δημιουργία τυχαίων δεδομένων
        byte[] challenge = new byte[10000];
        ThreadLocalRandom.current().nextBytes(challenge);

        // υπογραφή με το ιδιωτικό κλειδί
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initSign(readPrivateKey());
        sig.update(challenge);
        byte[] signature = sig.sign();

        // επαλήθευση υπογραφής με το δημόσιο κλειδί
        sig.initVerify(readPublicKey());
        sig.update(challenge);

        return sig.verify(signature);
    }

    // https://www.tutorialspoint.com/java_cryptography/java_cryptography_decrypting_data.htm
    // Κρυπτογράφηση συμβολοσειράς δεδομένων
    public static String encryptedString(String stringToEncrypt) throws Exception {

        // Δημιουργία αντικειμένου κρυπτογράφησης / αποκρυπτογράφησης
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

        // Λήψη δημοσίου κλειδιού
        PublicKey keyPublic = readPublicKey();

        // Αρχικοποίηση αντικειμένου κρυπτογράφησης / αποκρυπτογράφησης
        cipher.init(Cipher.ENCRYPT_MODE, keyPublic);

        // Προσθήκη δεδομένων για κρυπτογράφηση
        byte[] input = stringToEncrypt.getBytes();

        // Κρυπτογράφηση των δεδομένων
        byte[] cipherText = cipher.doFinal(input);

        // Επιστρέφει τα κρυπτογραφημένα δεδομένα
        return Base64.getEncoder().encodeToString(cipherText);
    }

    // Αποκρυπτογράφηση συμβολοσειράς δεδομένων
    public static  String  dencryptedString(String stringEncrypted) throws Exception {

        // Λήψη δεδομένων για αποκρυπτογράφηση
        byte[] stringEncryptedBytes =  Base64.getDecoder().decode(stringEncrypted.getBytes());

        // Λήψη ιδιωτικού κλειδιού
        PrivateKey keyPrivate = readPrivateKey();

        // Δημιουργία αντικειμένου κρυπτογράφησης / αποκρυπτογράφησης
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

        // Αρχικοποίηση αντικειμένου κρυπτογράφησης / αποκρυπτογράφησης
        cipher.init(Cipher.DECRYPT_MODE, keyPrivate);

        // Αποκρυπτογράφηση των δεδομένων
        byte[] decipheredText = cipher.doFinal(stringEncryptedBytes);

        // Επιστρέφει τα κρυπτογραφημένα δεδομένα
        return new String (decipheredText);
    }

    // Κρυπτογράφηση με συμμετρικό κλειδί
    public static String encrypt(String value, String initVector, String symmetricKey) {
        try {

            byte[] intitVector = Base64.getDecoder().decode(initVector.getBytes());
            byte[] key = Base64.getDecoder().decode(symmetricKey.getBytes());

            IvParameterSpec iv = new IvParameterSpec(intitVector);
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());

            return Base64.getEncoder().encodeToString(encrypted);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // Αποκρυπτογράφηση με συμμετρικό κλειδί
    public static String dencrypt(String value, String initVector, String symmetricKey) {
        try {

            byte[] intitVector = Base64.getDecoder().decode(initVector.getBytes());
            byte[] key = Base64.getDecoder().decode(symmetricKey.getBytes());

            IvParameterSpec iv = new IvParameterSpec(intitVector);
            SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(Base64.getDecoder().decode(value));

            return new String(encrypted);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
