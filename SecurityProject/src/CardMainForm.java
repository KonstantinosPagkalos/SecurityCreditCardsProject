import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

// https://docs.oracle.com/javafx/2/ui_controls/menu_controls.htm
public class CardMainForm extends Application {

    public static void start( ) {

        Stage primaryStage = new Stage();
        primaryStage.initModality(Modality.APPLICATION_MODAL);

        primaryStage.setTitle("Διαχείριση καρτών χρηστών");
        Scene scene = new Scene(new VBox(), 800, 600);
        scene.setFill(Color.OLDLACE);
        primaryStage.setScene(scene);

        MenuBar menuBar = new MenuBar();

        // --- Μενού Κάρτες
        Menu menuEdit = new Menu("Κάρτες");

        MenuItem menuCardAdd = new MenuItem("Προσθήκη κάρτας");
        menuCardAdd.setOnAction(event ->CardAddForm.start());
        menuEdit.getItems().add(menuCardAdd);

        MenuItem menuCardEdit = new MenuItem("Επεξεργασία κάρτας");
        menuCardEdit.setOnAction(event ->CardEditForm.start());
        menuEdit.getItems().add(menuCardEdit);

        MenuItem menuCardShow = new MenuItem("Προβολή καρτών");
        menuCardShow.setOnAction(event ->CardShowForm.start());
        menuEdit.getItems().add(menuCardShow);

        MenuItem menuCardDelete = new MenuItem("Διαγραφή κάρτας");
        menuCardDelete.setOnAction(event ->CardDeleteForm.start());
        menuEdit.getItems().add(menuCardDelete);

        // --- Μενού Έξοδος
        Menu menuExit = new Menu("Έξοδος");
        MenuItem menuCardExit = new MenuItem("Έξοδος από την εφαρμογή");
        menuCardExit.setOnAction(event ->Platform.exit());
        menuExit.getItems().add(menuCardExit);

        menuBar.getMenus().addAll( menuEdit, menuExit );

        ((VBox) scene.getRoot()).getChildren().addAll(menuBar);

        primaryStage.setScene(scene);
        primaryStage.show();

        //primaryStage.setFullScreen(true);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }
}

