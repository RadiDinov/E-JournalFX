package Controllers;

import Objects.Headmaster;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class HeadmasterController {

    //Objects
    Headmaster headmaster;
    //Objects

    //Scene Builder getting objects
    @FXML
    Label myLabel;
    //Scene Builder getting objects

    //GUI
    private Stage stage;
    private Scene scene;
    private Parent root;
    //GUI

    //If button 'Add students' is clicked
    public void addStudents(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/addStudentsStage.fxml"));
        this.root = loader.load();

        //Sending headmaster info to AddStudentsController
        AddStudentsController addStudentsController = loader.getController();
        addStudentsController.transferHeadmaster(headmaster);
        //Sending headmaster info to AddStudentsController

        this.stage = (Stage)(((Node)event.getSource()).getScene().getWindow());
        this.scene = new Scene(root, 1280, 720);
        this.stage.setScene(scene);
        this.stage.centerOnScreen();
        scene.getRoot().requestFocus();
        stage.show();
    }
    //If button 'Add students' is clicked

    //Getting headmaster info from Sign In
    public void storeCurrentHeadmaster(String firstName, String lastName, String email, String egn, String telephoneNumber, String position) {
        headmaster = new Headmaster(firstName, lastName, email, egn, telephoneNumber, position);
    }
    //Getting headmaster info from Sign In

    //Making sure our headmaster info is not lost
    public void transferHeadmaster(Headmaster headmaster) {
        this.headmaster = headmaster;
    }
    //Making sure our headmaster info is not lost

    //If button 'View session' is clicked (DEMO)
    public void setText() {
        myLabel.setText(headmaster.getFirstName() + " " + headmaster.getLastName());
    }
    //If button 'View session' is clicked (DEMO)
}
