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
import javafx.stage.Stage;
import javafx.stage.Window;

public class LoginForm extends Application {

    public static void main( ) {
        launch( );
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Είσοδος χρήστη");

        // Δημιουργία φόρμας με πλέγμα για επεξεργασία στοιχείων κάρτας
        GridPane gridPane = createLoginFormPane();
        // Προσθήκη χειριστηρίων στη φόρμα
        addUIControls(gridPane);

        Scene scene = new Scene(gridPane, 600, 300);

        primaryStage.setScene(scene);

        primaryStage.show();

    }

    // Δημιουργία πλέγματος φόρμας
    private static GridPane createLoginFormPane() {
        // Δημιουργία πλέγματος
        GridPane gridPane = new GridPane();

        // Τοποθέτηση στο κέντρο της φόρμας οριζόντια και κάθετα
        gridPane.setAlignment(Pos.CENTER);

        // Καθορισμός απόστασης από κάθε πλευρά της φόρμας
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
        // Προσθήκη τίτλου
        Label headerLabel = new Label("Φόρμα εισόδου");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0,0,2,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));

        // Προσθήκη ετικέτας ονόματος χρήστη
        Label userNameLabel = new Label("Όνομα χρήστη : ");
        gridPane.add(userNameLabel, 0, 1);

        // Προσθήκλη πεδίου ονόματος χρήστη
        TextField userNameField = new TextField();
        userNameField.setPrefHeight(40);
        gridPane.add(userNameField, 1, 1);

        // Προσθήκη ετικέτας κωδικού
        Label passwordLabel = new Label("Κωδικός εισόδου : ");
        gridPane.add(passwordLabel, 0, 2);

        // Προσθήκη πεδίου κωδικού
        PasswordField passwordField = new PasswordField();
        passwordField.setPrefHeight(40);
        gridPane.add(passwordField, 1, 2);

        // Προσθήκη κουμπιού
        Button submitButton = new Button("Είσοδος");
        submitButton.setPrefHeight(40);
        submitButton.setDefaultButton(true);
        submitButton.setPrefWidth(100);
        gridPane.add(submitButton, 0, 5, 2, 1);
        GridPane.setHalignment(submitButton, HPos.CENTER);
        GridPane.setMargin(submitButton, new Insets(20, 0,20,0));

        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(userNameField.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Μήνυμα", "Παρακαλώ συμπληρώστε το όνομα χρήστη");
                    return;
                }
                if(passwordField.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Μήνυμα", "Παρακαλώ συμπληρώστε το κωδικό εισόδου");
                    return;
                }

                try {
                    if (ManageUser.authenticateUser(userNameField.getText(), passwordField.getText())){
                        //showAlert(Alert.AlertType.CONFIRMATION, gridPane.getScene().getWindow(), "Μήνυμα", "Ο χρήστης ταυτοποιήθηκε");
                        CardMainForm.start();
                    }
                    else {
                        //showAlert(Alert.AlertType.CONFIRMATION, gridPane.getScene().getWindow(), "Μήνυμα", "Ο χρήστης δεν ταυτοποιήθηκε");
                        RegistrationForm.start();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }

}

