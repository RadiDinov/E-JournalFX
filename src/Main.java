import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("E-Journal");
        Parent root = FXMLLoader.load(getClass().getResource("FXML/main.fxml"));
        Scene scene = new Scene(root, 600, 455);


        //Add .css file
        URL url = this.getClass().getResource("Styles/main.css");
        String css = url.toExternalForm();
        scene.getStylesheets().add(css);
        //Add .css file
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        scene.getRoot().requestFocus();
        primaryStage.show();


        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            logout(primaryStage);
        });
    }

    public void logout(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You are about to logout!");
        alert.setContentText("Do you want to save before exiting?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            System.out.println("You successfully logged out!");
            stage.close();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
