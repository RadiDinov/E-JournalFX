package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class HeadmasterController {
//    private String firstName;
//    private String lastName;


    @FXML
    Label myLabel;

    //GUI
    private Stage stage;
    private Scene scene;
    private Parent root;
    //GUI

    private ArrayList<HeadmasterController> headmasterData = new ArrayList();

    MainController currentHeadmasterData = new MainController();

//    public HeadmasterController(String firstName, String lastName) {
//        this.firstName = firstName;
//        this.lastName = lastName;
//    }


    public void setText() {
        myLabel.setText(currentHeadmasterData.getFirstName() + " " + currentHeadmasterData.getLastName());
    }

    public void addStudents(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/addStudentsStage.fxml"));
        this.root = loader.load();
        this.stage = (Stage)(((Node)event.getSource()).getScene().getWindow());
        this.scene = new Scene(root, 1280, 720);
        this.stage.setScene(scene);
        this.stage.centerOnScreen();
        scene.getRoot().requestFocus();
        stage.show();
    }

    public void getInformation(String firstName, String lastName, String email, String password, String egn, String position) {
        currentHeadmasterData.setFirstName(firstName);
        currentHeadmasterData.setLastName(lastName);
        currentHeadmasterData.setEmail(email);
        currentHeadmasterData.setPassword(password);
        currentHeadmasterData.setEgn(egn);
        currentHeadmasterData.setPosition(position);
//        headmasterData.add(new HeadmasterController(currentHeadmasterData.getFirstName(), currentHeadmasterData.getLastName()));
    }
}
