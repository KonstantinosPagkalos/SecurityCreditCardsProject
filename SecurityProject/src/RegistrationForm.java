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

public class RegistrationForm extends Application {

    public static void start( ) {

        Stage primaryStage = new Stage();
        primaryStage.initModality(Modality.APPLICATION_MODAL);

        primaryStage.setTitle("Εγγραφή χρήστη");

        // Δημιουργία φόρμας με πλέγμα για επεξεργασία στοιχείων κάρτας
        GridPane gridPane = createRegistrationFormPane();
        // Προσθήκη χειριστηρίων στη φόρμα
        addUIControls(gridPane);

        Scene scene = new Scene(gridPane, 600, 300);

        primaryStage.setScene(scene);

        primaryStage.show();
    }

    // Δημιουργία πλέγματος φόρμας
    private static GridPane createRegistrationFormPane() {

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
        Label headerLabel = new Label("Φόρμα εγγραφής");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        gridPane.add(headerLabel, 0,0,2,1);
        GridPane.setHalignment(headerLabel, HPos.CENTER);
        GridPane.setMargin(headerLabel, new Insets(20, 0,20,0));

        Label nameLabel = new Label("Ονοματεπώνυμο : ");
        gridPane.add(nameLabel, 0,1);

        TextField nameField = new TextField();
        nameField.setPrefHeight(40);
        gridPane.add(nameField, 1,1);

        Label emailLabel = new Label("Email : ");
        gridPane.add(emailLabel, 0, 2);

        TextField emailField = new TextField();
        emailField.setPrefHeight(40);
        gridPane.add(emailField, 1, 2);

        Label userNameLabel = new Label("Όνομα χρήστη : ");
        gridPane.add(userNameLabel, 0, 3);

        TextField userNameField = new TextField();
        userNameField.setPrefHeight(40);
        gridPane.add(userNameField, 1, 3);

        Label passwordLabel = new Label("Κωδικός εισόδου : ");
        gridPane.add(passwordLabel, 0, 4);

        PasswordField passwordField = new PasswordField();
        passwordField.setPrefHeight(40);
        gridPane.add(passwordField, 1, 4);

        Button submitButton = new Button("Εγγραφή");
        submitButton.setPrefHeight(40);
        submitButton.setDefaultButton(true);
        submitButton.setPrefWidth(100);
        gridPane.add(submitButton, 0, 5, 2, 1);
        GridPane.setHalignment(submitButton, HPos.CENTER);
        GridPane.setMargin(submitButton, new Insets(20, 0,20,0));

        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(nameField.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Μήνυμα", "Παρακαλώ συμπληρώστε το όνοματεπώνυμο");
                    return;
                }
                if(emailField.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Μήνυμα", "Παρακαλώ συμπληρώστε το email");
                    return;
                }
                if(userNameField.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Μήνυμα", "Παρακαλώ συμπληρώστε το όνομα χρήστη");
                    return;
                }
                if(passwordField.getText().isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, gridPane.getScene().getWindow(), "Μήνυμα", "Παρακαλώ συμπληρώστε το κωδικό εισόδου");
                    return;
                }

                try {
                    if (ManageUser.registerUser(nameField.getText(), emailField.getText(), userNameField.getText(), passwordField.getText())){
                        CardMainForm.start();
                    }
                    else {
                        showAlert(Alert.AlertType.CONFIRMATION, gridPane.getScene().getWindow(), "Μήνυμα", "Το όνομα χρήστη υπάρχει");
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

    @Override
    public void start(Stage primaryStage) throws Exception {

    }
}

