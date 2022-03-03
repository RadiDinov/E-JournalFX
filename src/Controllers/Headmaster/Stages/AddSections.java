package Controllers.Headmaster.Stages;

import Controllers.Headmaster.HeadmasterController;
import JDBC.JDBC;
import Objects.Headmaster;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class AddSections {

    //Objects
    Headmaster headmaster;
    //Objects

    //JDBC
    static JDBC jdbcGetTables;

    static {
        try {
            jdbcGetTables = new JDBC("Show tables");
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }
    //JDBC

    //Scene Builder getting objects
    @FXML
    TextField sectionField;
    @FXML
    Label errorLabel;
    //Scene Builder getting objects

    //GUI
    private Stage stage;
    private Scene scene;
    private Parent root;
    //GUI


    public void addSection() throws SQLException {
        jdbcGetTables.resultSet = jdbcGetTables.statement.executeQuery("Show tables");
        //Resetting the errorLabel ArrayList
        errorLabel.setText("");
        //Resetting the errorLabel ArrayList

        //Booleans for matches
        boolean existingTable = false;
        //Booleans for matches

        //This string is representing the table's name
        String sectionFieldString = "section_" + sectionField.getText();
        //This string is representing the table's name

        while (jdbcGetTables.getResultSet().next()) {

            //If there is already a table with this section
            if (sectionFieldString.equals(jdbcGetTables.getResultSet().getString(1))) {
                existingTable = true;
                break;
            }
            //If there is already a table with this section
        }


        if (existingTable) {
            errorLabel.setText("Section: " + sectionField.getText() + " is not available!");
        } else {
            String name = "Section_" + sectionField.getText();
            try {
                String sql = "CREATE TABLE " + name + " ("
                        + "id INT NOT NULL AUTO_INCREMENT,"
                        + "firstName VARCHAR(255),"
                        + "lastName VARCHAR(255),"
                        + "classNumber VARCHAR(255),"
                        + "grades VARCHAR(255),"
                        + "position VARCHAR(255),"
                        + "PRIMARY KEY(id))";
                jdbcGetTables.statement.executeUpdate(sql);
                errorLabel.setText("Successfully added new section: " + sectionField.getText());
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

    }


    public void goBackToHeadmasterStage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../FXML/headmaster.fxml"));
        this.root = loader.load();

        //Sending headmaster info to HeadmasterController
        HeadmasterController headmasterController = loader.getController();
        headmasterController.transferHeadmaster(headmaster);
        //Sending headmaster info to HeadmasterController

        this.stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
        this.scene = new Scene(root, 1280, 720);
        this.stage.setScene(scene);
        this.stage.centerOnScreen();
        this.scene.getRoot().requestFocus();
        this.stage.show();
    }

    public void transferHeadmaster(Headmaster headmaster) {
        this.headmaster = headmaster;
    }
}
