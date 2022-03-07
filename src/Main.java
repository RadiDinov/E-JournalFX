import SaltHasher.BCryptPasswordHashing;
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
//        BCryptPasswordHashing bCryptPasswordHashing = new BCryptPasswordHashing(12);
//        char[] arr = new char[] {'1', '2', '3'};
//        System.out.println(bCryptPasswordHashing.hashPassword(arr));

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

        //When trying to close the program
        primaryStage.setOnCloseRequest(event -> {
            event.consume();
            logout(primaryStage);
        });
        //When trying to close the program
    }

    //Pop-up when trying to close the program
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
    //Pop-up when trying to close the program


    public static void main(String[] args) {
        launch(args);
    }
}

//TODO: make custom exceptions!

//TODO: add teacher then you can assign this teacher with a class
//TODO: when adding teacher with class number == "-" make it work :)
//TODO: remove section from class number field
//TODO: at all... rewrite Management.java
//TODO: if first + last name is entered in bulgarian, make email english



//        sectionsMenu.getItems().add(new MenuItem("181")); //adding section 181
//        sectionsMenu.getItems().add(new MenuItem("182")); //adding section 182
//        for (int i = 0; i < sectionsMenu.getItems().size(); i++) { //for to get every sectionMenu item name
//            System.out.println(sectionsMenu.getItems().get(i).getText());
//        }

//        sectionsMenu.getItems().get(0).setOnAction(event -> System.out.println("asd")); //setting onAction event to [0] - section 181