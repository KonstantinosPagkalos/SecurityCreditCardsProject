// Πιστωτική ή χρεωστική κάρτα
public class Card {

    // Διαχωριστικό αρχείου καρτών
    final char csvSeparator = ';';

    // Τύπος κάρτας
    private String type;
    // Αριθμός κάρτας
    private String number;
    // Κάτοχος κάρτας
    private String owner;
    // Ημερομηνία λήξης
    private String deadline;
    // Αριθμός επαλήθευσης
    private String cvv;

    public Card() {
    }

    // Αρχικοποίηση κάρτας με όλα τα στοιχεία
    public Card(String type, String number, String owner, String deadline, String cvv) {
        this.type = type;
        this.number = number;
        this.owner = owner;
        this.deadline = deadline;
        this.cvv = cvv;
    }

    // Αρχικοποίηση κάρτας με όλα τα στοιχεία μορφοποιημένα σε γραμμή csv
    public Card (String csvLine ){
        String[] elements = csvLine.split(String.valueOf(csvSeparator));

        this.type = elements[0];
        this.number = elements[1];
        this.owner = elements[2];
        this.deadline = elements[3];
        this.cvv = elements[4];
    }

    // Στοιχεία κάρτας σε γραμμή csv
    public String toCsv(){
        return (type + csvSeparator + number + csvSeparator + owner + csvSeparator + deadline + csvSeparator + cvv);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}
