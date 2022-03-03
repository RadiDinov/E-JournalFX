package Controllers.Headmaster;

import Controllers.Headmaster.Stages.AddSections;
import Controllers.Headmaster.Stages.AddStudents;
import Objects.Headmaster;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HeadmasterController {

    //Objects
    Headmaster headmaster;
    //Objects

    //Scene Builder getting objects
    @FXML
    Label myLabel;
    @FXML
    AnchorPane anchorPane;
    @FXML
    MenuButton sectionsMenu;
    //Scene Builder getting objects

    //GUI
    private Stage stage;
    private Scene scene;
    private Parent root;
    //GUI

    //If menuItem 'Add students' is clicked
    public void addStudents() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../FXML/addStudentsStage.fxml"));
        this.root = loader.load();

        //Sending headmaster info to AddStudents
        AddStudents addStudents = loader.getController();
        addStudents.transferHeadmaster(headmaster);
        //Sending headmaster info to AddStudents

        this.stage = (Stage)anchorPane.getScene().getWindow();
        this.scene = new Scene(root, 1280, 720);
        this.stage.setScene(scene);
        this.stage.centerOnScreen();
        scene.getRoot().requestFocus();
        stage.show();


//        public void gotoReports(ActionEvent e) throws IOException {
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(getClass().getResource("/Reports/Reports.fxml"));
//            Parent root = loader.load();
//            Scene scene = new Scene(root);
//
//            Stage stage = (Stage)anchorPane.getScene().getWindow();
//
//            stage.setScene(scene);
//
//            stage.show();
//        }
//
    }
    //If menuItem 'Add students' is clicked

    //Getting headmaster info from Sign In
    public void storeCurrentHeadmaster(String firstName, String lastName, String email, String egn, String telephoneNumber, String position) {
        headmaster = new Headmaster(firstName, lastName, email, egn, telephoneNumber, position);
        myLabel.setText("Signed in as: " + headmaster.getFirstName() + " " + headmaster.getLastName());
    }
    //Getting headmaster info from Sign In

    //Making sure our headmaster info is not lost
    public void transferHeadmaster(Headmaster headmaster) {
        this.headmaster = headmaster;
        myLabel.setText("Signed in as: " + headmaster.getFirstName() + " " + headmaster.getLastName());
    }

    public void viewAllStudents(ActionEvent event) {
    }

    //If menuItem 'Add sections' is clicked
    public void addSections() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../FXML/addSectionsStage.fxml"));
        this.root = loader.load();

        //Sending headmaster info to AddSections
        AddSections addSections = loader.getController();
        addSections.transferHeadmaster(headmaster);
        //Sending headmaster info to AddSections

        this.stage = (Stage)anchorPane.getScene().getWindow();
        this.scene = new Scene(root, 1280, 720);
        this.stage.setScene(scene);
        this.stage.centerOnScreen();
        scene.getRoot().requestFocus();
        stage.show();
    }
    //If menuItem 'Add sections' is clicked

    //Making sure our headmaster info is not lost
//
//    //If button 'View session' is clicked (DEMO)
//    public void setText() {
//        myLabel.setText("Signed in as: " + headmaster.getFirstName() + " " + headmaster.getLastName());
//    }
//    //If button 'View session' is clicked (DEMO)
}
