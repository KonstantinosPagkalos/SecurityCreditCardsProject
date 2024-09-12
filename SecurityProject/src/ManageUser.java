import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ManageUser {

    public static User userLogin;

    public static boolean authenticateUser(String userName, String password) throws Exception {

        userLogin = new User(userName, password);

        // Λήψη αρχείου χρηστών από csv αρχείο
        // Αναζήτηση σύνοψης κωδικού στο αρχείο
        // Σύγκριση με υπάρχουσα
        boolean userAuthenticated = false;
        try {
            FileReader reader = new FileReader(DataFile.userFile);
            BufferedReader bufferedReader = new BufferedReader(reader);

            String lineUser = bufferedReader.readLine();

            while (lineUser != null)  {
                String[] elements = lineUser.split(String.valueOf(User.csvSeparator));
                if (userLogin.getUserName().equals(elements[1])){

                    String saltString = elements[2];
                    userLogin.setSalt(saltString);
                    userLogin.hashPassword();

                    String encryptedPassword = elements[3];

                    String dencryptedString = ManageCipher.dencryptedString(encryptedPassword);

                    if (userLogin.getStringHashedPassword().equals(dencryptedString)) {
                        userAuthenticated = true;
                        break;
                    }

                }
                lineUser = bufferedReader.readLine();

            }

            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Λήψη συμμετρικού κλειδιού χρήστη
        if (userAuthenticated){

            try {
                FileReader reader = new FileReader(DataFile.applicationFolder + userLogin.getUserName() + DataFile.symmetrikeAes256KeyFile);
                BufferedReader bufferedReader = new BufferedReader(reader);

                String lineUser = bufferedReader.readLine();

                while (lineUser != null)  {
                    String[] elements = lineUser.split(String.valueOf(User.csvSeparator));
                    String encryptedSymmetricAES256Key = elements[0];

                    String symmetricAES256Key = ManageCipher.dencryptedString(encryptedSymmetricAES256Key);
                    userLogin.setStringKeyAES256(symmetricAES256Key);

                    lineUser = bufferedReader.readLine();
                }
                reader.close();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return userAuthenticated;
    }

    public static boolean registerUser(String person, String email, String userName, String password) throws Exception {
        boolean registerSuccess = true;
        userLogin = new User(person, email, userName, password);
        if (!userLogin.isUniqueUserName()) {
            registerSuccess = false;
        }
        else {
            // Λήψη σύνοψης τυχαίου αλφαριθμητικού και κωδικού εισόδου προς κρυπτογράφηση
            String toEncrypt = userLogin.getStringHashedPassword();
            // Κρυπτογράφηση σύνοψης τυχαίου αλφαριθμητικού και κωδικού εισόδου
            String encryptText = ManageCipher.encryptedString(toEncrypt) ;
            // Ενημέρωση κρυπτογραφημένης σύνοψης τυχαίου αλαφαριθμητικού και κωδικού εισόδου
            userLogin.setStringEncryptedPassword(encryptText);

            // Αποθήκευση στοιχείων χρήστη
            try {
                FileWriter writer = new FileWriter(DataFile.userFile, true);
                writer.write(userLogin.toCsv());
                writer.write("\r\n" );
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Λήψη συμμετρικού κλειδιού χρήστη
            toEncrypt = userLogin.getStringKeyAES256();
            // Κρυπτογράφηση συμμετρικού κλειδιού χρήστη
            userLogin.setStringKeyAES256(ManageCipher.encryptedString(toEncrypt));

            // Αποθήκευση συμμετρικού κλειδιού χρήστη
            try {
                FileWriter writer = new FileWriter(DataFile.applicationFolder + userLogin.getUserName() + DataFile.symmetrikeAes256KeyFile);
                writer.write(userLogin.getStringEncryptedPassword());
                writer.write("\r\n" );
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return registerSuccess;
    }
}


