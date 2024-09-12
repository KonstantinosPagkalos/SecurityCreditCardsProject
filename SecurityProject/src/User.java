import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.util.Base64;

// Χρήστης εφαρμογής
public class User {

    // Διαχωριστικό αρχείου χρηστών
    final public static char csvSeparator = ';';

    // Ονοματεπώνυμο
    private String person;
    // Email
    private String email;
    // Όνομα εισόδου χρήστη
    private String userName;
    // Κωδικός εισόδου
    private String password;

    // Τυχαίο αλφαρηθμητικό
    private byte[] salt;
    // Σύνοψη κωδικού εισόδου και αλφαριθμητικού
    private byte[] hashedPassword;

    // Αναπαράσταση στοιχείων χρήστη
    private String stringSalt;
    private String stringHashedPassword;
    private String stringKeyAES256;
    private String stringEncryptedPassword;

    public User() {

    }

    // Καλείται με την εγγραφή του χρήστη στο σύστημα
    public User(String person, String email, String userName, String password) {
        this.person = person;
        this.email = email;
        this.userName = userName;
        this.password = password;
    }

    // Καλείται με την είσοδο του χρήστη στο σύστημα, αφού έχει εγγραφεί
    public User( String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    // Έλεγχος εάν το όνομα χρήστη υπάρχει ήδη
    public boolean isUniqueUserName(){

        boolean userUnique = true;

        Path userFolder = Paths.get(DataFile.applicationFolder + userName);
        boolean existsUserFolder = Files.exists(userFolder);

        // Έλεχγος εάν υπάρχει φάκελος με το όνομα του χρήστη
        if (!existsUserFolder) {
            File file = new File(DataFile.applicationFolder+userName);
            file.mkdir();

            // Λήψη τυχαίου αλφαριθμητικού
            getSalt();

            // Υπολογισμός σύνοψης τυχαίου αλφαριθμητικού και κωδικού εισόδου
            hashPassword();

            // Δημιουργία συμμετρικού κλειδιού για το χρήστη
            generateSymmetricKeyAES256();
        }
        else {
            userUnique = false;
        }

        return userUnique;
    }

    // Σύνοψη τυχαίου αλφαριθμητικού και κωδικού εισόδου
    public void hashPassword(){

        // Δημιουργία μεταβλητής MessageDigestγια SHA-512
        MessageDigest md = null;
        try {

            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // Προσθήκη του τυχαίου αλφαριθμητικού
        md.update(salt);

        // Λήψη σύνοψης κωδικού  + τυχαίου αλφαριθμητικού
        this.hashedPassword = md.digest(this.password.getBytes(StandardCharsets.UTF_8));

        this.stringHashedPassword =  Base64.getEncoder().encodeToString(this.hashedPassword);

    }

    // Λήψη τυχαίου αλφαριθμητικού
    private void getSalt(){
        // Δημιουργία τυχαίου ασφαλούς δημιουργού αλφαριθμητικών
        SecureRandom random = new SecureRandom();
        // Δημιουργία πίνακα για το αλφαρημθτικό
        this.salt = new byte[16];
        // Λήψη τυχαίου αλφαριθμητικού
        random.nextBytes(this.salt);

        this.stringSalt =  Base64.getEncoder().encodeToString(this.salt);

    }

    // Δημιουργία συμμετρικού κλειδιού με αλγόριθμό AES-256
    private void generateSymmetricKeyAES256(){
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance("AES" );
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyGenerator.init(256);
        SecretKey key = keyGenerator.generateKey();

        this.stringKeyAES256 = Base64.getEncoder().encodeToString(key.getEncoded());

    }

    // Αναπαράσταση στοιχείων χρήστη σε γραμμή csv
    public String toCsv(){
        return (person + csvSeparator + userName + csvSeparator + stringSalt + csvSeparator + stringEncryptedPassword);
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSalt(String stringSalt) {
        this.salt = Base64.getDecoder().decode(stringSalt.getBytes());
        this.stringSalt = stringSalt;
    }
    public String getStringSalt() {
        return stringSalt;
    }

    public String getStringHashedPassword() {
        return stringHashedPassword;
    }

    public String getStringKeyAES256() {
        return stringKeyAES256;
    }

    public void setStringKeyAES256(String stringKeyAES256) {
        this.stringKeyAES256 = stringKeyAES256;
    }

    public String getStringEncryptedPassword() {
        return stringEncryptedPassword;
    }

    public void setStringEncryptedPassword(String stringEncryptedPassword) {
        this.stringEncryptedPassword = stringEncryptedPassword;
    }
}
