import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.ArrayList;

// Φόρμα εισαγωγής στοιχείων προβολής καρτών
public class CardShowForm extends Application {

    public static void start( ) {

        Stage primaryStage = new Stage();
        primaryStage.initModality(Modality.APPLICATION_MODAL);

        primaryStage.setTitle("Προβολή καρτών");

        // Δημιουργία φόρμας με πλέγμα για διαγραφή κάρτας
        GridPane gridPane = createShowCardFormPane();
        // Προσθήκη χειριστηρίων στη φόρμα
        addUIControls(gridPane);

        Scene scene = new Scene(gridPane, 600, 300);

        primaryStage.setScene(scene);

        primaryStage.show();
    }

    // Δημιουργία πλέγματος φόρμας
    private static GridPane createShowCardFormPane() {
        // Δημιουργία πλέγματος
        GridPane gridPane = new GridPane();

        // Τοποθέτηση στο κέντρο της φόρμας οριζόντια και κάθετα
        gridPane.setAlignment(Pos.CENTER);

        // Καθορισμός απόστασης 20px από κάθε πλευρά της φόρμας
        gridPane.setPadding(new Insets(40, 40, 40, 40));

        // Καθορισμός απόστασης μεταξύ των στηλών
        gridPane.setHgap(10);

        // Καθορισμός απόστασης μεταξύ των γραμμών
        gridPane.setVgap(10);

        // Περιορσιμοί στηλών

        // Για τη πρώτη στήλη
        ColumnConstraints columnOneConstraints = new ColumnConstraints(150, 150, Double.MAX_VALUE);
        columnOneConstraints.setHalignment(HPos.RIGHT);

        // Για τη δεύτερη στήλη
        ColumnConstraints columnTwoConstrains = new ColumnConstraints(200,200, Double.MAX_VALUE);
        columnTwoConstrains.setHgrow(Priority.ALWAYS);

        gridPane.getColumnConstraints().addAll(columnOneConstraints, columnTwoConstrains);

        return gridPane;
    }

    // Προσθήκη χειριστηρίων στη φόρμα της κάρτας
    private static void addUIControls(GridPane gridPane) {
        // Add Header
        Label headerLabel = new Label("Στοιχεία κάρτας");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0,0,2,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));

        // Προσθήκη ετικέτας τύπος κάρτας
        Label cardTypeLabel = new Label("Τύπος : ");
        gridPane.add(cardTypeLabel, 0,1);

        // Προσθήκη πεδίου τύπος κάρτας
        TextField cardTypeField = new TextField();
        cardTypeField.setPrefHeight(40);
        gridPane.add(cardTypeField, 1,1);

        // Προσθήκη ετικέτας λίστας καρτών
        Label cardListLabel = new Label("Κάρτες : ");
        gridPane.add(cardListLabel, 0, 2);

        // Προσθήκη πεδίου αριθμός κάρτας
        ListView   cardListView = new ListView();
        cardListView.setPrefHeight(200);
        gridPane.add(cardListView,1, 2);

        // Προσθήκη κουμπιού για την εισαγωγή της κάρτας στο αρχείο καρτών του χρήστη
        Button submitButton = new Button("Λήψη καρτών");
        submitButton.setPrefHeight(40);
        submitButton.setDefaultButton(true);
        submitButton.setPrefWidth(150);
        gridPane.add(submitButton, 0, 3, 2, 1);
        GridPane.setHalignment(submitButton, HPos.CENTER);
        GridPane.setMargin(submitButton, new Insets(20, 0,20,0));

        // Ενέργειας κατά την προσθήκη της κάρτας
        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(cardTypeField.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Μήνυμα", "Παρακαλώ συμπληρώστε το τύπο της κάρτας");
                    return;
                }

                String cardType = cardTypeField.getText();

                ArrayList<Card> cards = new ArrayList<>();
                try {
                    cards = ManageCard.getCards(ManageUser.userLogin, cardType);
                    for (Card card : cards) {
                        cardListView.getItems().add(card.toCsv());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Εμφάνιση στοιχείων καρτών
                for (Card card : cards) {
                    System.out.println(card.toCsv());
                }

            }
        });
    }

    // Προβολή μηνύματος στο χρήστη
    private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }
}

