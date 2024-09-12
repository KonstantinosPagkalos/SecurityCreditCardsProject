import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class ManageCard {

    public ManageCard() {
    }

    // Προσθήκη κάρτας στο αρχείο καρτών στο φάκελο του χρήστη
    public static void addCard(User userLogin, Card card) throws IOException {

        // Κρυπτογράφηση στοιχείων κάρτας
        String encryptedCard = ManageCipher.encrypt(card.toCsv(), userLogin.getStringSalt(), userLogin.getStringKeyAES256());

        // Αποθήκευση στοιχείων κάρτας
        FileWriter writer = new FileWriter(DataFile.applicationFolder + userLogin.getUserName() + DataFile.cardFile, true);
        writer.write(encryptedCard);
        // Εισαγωγή αλλαγής γραμμής
        writer.write("\r\n");
        // Κλείσιμο αρχείου
        writer.close();
    }

    // Επιστρέφει στοιχεία καρτών συγκεκιμμένου τύπου
    public static ArrayList<Card> getCards(User userLogin, String cardType) throws IOException {

        // Μεταβλητή κάρτας εργασίας
        Card c = null;
        // Δημιουργία λίστας καρτών
        ArrayList<Card> cards = new ArrayList<>();

        // Ανάγνωση όλων των καρτών από το φάκελο του χρήστη, μία προς μία
        FileReader reader = new FileReader(DataFile.applicationFolder + userLogin.getUserName() + DataFile.cardFile);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String encryptedCardLine = bufferedReader.readLine();
        while (encryptedCardLine != null) {

            // Αποκρυπτογράφηση στοιχείων κάρτας με το συμμετρικό κλειδί του χρήστη
            String cardLine = ManageCipher.dencrypt(encryptedCardLine, userLogin.getStringSalt(), userLogin.getStringKeyAES256());

            String[] elements = cardLine.split(String.valueOf(User.csvSeparator));
            // Εντοπισμός κάρτων τύπου αναζήτησης
            if (cardType.equals(elements[0])) {
                // Δημιουργία κάρτας
                c = new Card(cardLine);
                // Προσθήκη στη λίστα καρτών
                cards.add(c);

            }
            // Ανάγνωση επόμενης γραμμής
            encryptedCardLine = bufferedReader.readLine();
        }
        // Κλείσιμο αρχείου
        reader.close();

        return cards;
        // Εμφάνιση στοιχείων καρτών
        //for (Card card : cards) {
        //    System.out.println(card.toCsv());
        //}
    }

    // Επεξεργασία στοιχείων κάρτας
    public static void editCard(User userLogin, String cardType , String cardNumber, Card newCard) throws IOException {

        // Μεταβλητή κάρτας εργασίας
        Card c = null;
        // Δημιουργία λίστας καρτών
        ArrayList<Card> cards = new ArrayList<>();

        // Ανάγνωση όλων των καρτών από το φάκελο του χρήστη, μία προς μία
        FileReader reader = new FileReader(DataFile.applicationFolder + userLogin.getUserName() + DataFile.cardFile);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String encryptedCardLine = bufferedReader.readLine();
        while (encryptedCardLine != null) {
            // Αποκρυπτογράφηση στοιχείων κάρτας με το συμμετρικό κλειδί του χρήστη
            String cardLine = ManageCipher.dencrypt(encryptedCardLine, userLogin.getStringSalt(), userLogin.getStringKeyAES256());
            // Δημιουργία αντικειμένου κάρτας
            c = new Card(cardLine);
            // Προσθήκη στη λίστα
            cards.add(c);
            // Ανάγνωση επόμενης κάρτας
            encryptedCardLine = bufferedReader.readLine();
        }
        // Κλείσιμο αρχείου
        reader.close();

        // Τροποποίηση και αποθήκευση της λίστας στο φάκελο του χρήστη
        // Για τη πρώτη κάρτα δημιουργείται νέο αρχείο
        boolean appendCard = false;
        // Επαναληπτικά για κάθε κάρτα
        for (Card card : cards) {
            // Εντοπισμός κάρτας, βάσει τύπου και αριθμού
            if (card.getType().equals(cardType) && card.getNumber().equals(cardNumber)) {

                // Διόρθωση στοιχείων
                card.setDeadline(newCard.getDeadline());
                card.setCvv(newCard.getCvv());
                card.setOwner(newCard.getOwner());
            }

            // Κρυπτογράφηση στοιχείων κάρτας
            String encryptedCard = ManageCipher.encrypt(card.toCsv(), userLogin.getStringSalt(), userLogin.getStringKeyAES256());

            // Αποθήκευση στοιχείων αυτής
            FileWriter writer = new FileWriter(DataFile.applicationFolder + userLogin.getUserName() + DataFile.cardFile, appendCard);
            writer.write(encryptedCard);
            // Εισαγωγή αλλαγής γραμμής
            writer.write("\r\n");
            // Κλείσιμο αρχείου
            writer.close();

            if (!appendCard) {
                appendCard = true;
            }
        }
    }

    // Διαγραφή κάρτα από αρχείο καρτών χρήστη
    public static void removeCard(User userLogin, String cardType, String cardNumber) throws IOException {

        // Μεταβλητή κάρτας εργασίας
        Card c = null;
        // Δημιουργία λίστας καρτών
        ArrayList<Card> cards = new ArrayList<>();

        // Ανάγνωση όλων των καρτών από το φάκελο του χρήστη, μία προς μία
        FileReader reader = new FileReader(DataFile.applicationFolder + userLogin.getUserName() + DataFile.cardFile);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String encryptedCardLine = bufferedReader.readLine();
        while (encryptedCardLine != null) {
            // Αποκρυπτογράφηση στοιχείων κάρτας με το συμμετρικό κλειδί του χρήστη
            String cardLine = ManageCipher.dencrypt(encryptedCardLine, userLogin.getStringSalt(), userLogin.getStringKeyAES256());

            // Δημιουργία αντικειμένου κάρτας
            c = new Card(cardLine);
            // Προσθήκη στη λίστα καρτών του χρήση
            cards.add(c);
            // Ανάγνωση επόμενης γραμμής
            encryptedCardLine = bufferedReader.readLine();
        }
        // Κλείσιμο αρχείου
        reader.close();

        // Απαλοιφή κάρτας
        Iterator<Card> itrCard = cards.iterator();
        // Διαπέραση συνόλου καρτών
        while (itrCard.hasNext()) {
            Card card = itrCard.next();
            // Εντοπισμός κάρτας, βάσει τύπου και αριθμού
            if (card.getType().equals(cardType) && card.getNumber().equals(cardNumber)) {
                // Διαγραφή από τη λίστα
                cards.remove(card);
                break;
            }
        }

        // Αποθήκευση της λίστας στο φάκελο του χρήστη
        // Για τη πρώτη κάρτα δημιουργείται νέο αρχείο
        boolean appendCard = false;
        // Επαναληπτικά για κάθε κάρτα
        for (Card card : cards) {
            // Κρυπτογράφηση στοιχείων κάρτας
            String encryptedCard = ManageCipher.encrypt(card.toCsv(), userLogin.getStringSalt(), userLogin.getStringKeyAES256());

            // Αποθήκευση στοιχείων αυτής
            FileWriter writer = new FileWriter(DataFile.applicationFolder + userLogin.getUserName() + DataFile.cardFile, appendCard);
            writer.write(encryptedCard);
            // Εισαγωγή αλλαγής γραμμής
            writer.write("\r\n");
            // Κλείσιμο αρχείου
            writer.close();

            // Εκτός της πρώτης κάρτας, προσθήκη στο αρχείο για τις υπόλοιπες
            if (!appendCard) {
                // Αλλαγή κατάστασης εγγραφή σε προσάρτηση, μετά τη πρώτη κάρτα
                appendCard = true;
            }
        }
    }
}

