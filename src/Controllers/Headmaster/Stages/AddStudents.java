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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddStudents {

    //JDBC
    static JDBC jdbcAddStudent; //primary keys - classNumber, email, egn, telephoneNumber
    //JDBC

    //Objects
    Headmaster headmaster;
    //Objects

    //Regex
    String emailRegex, phoneRegex;
    Pattern emailPattern, phonePattern;
    Matcher emailMatcher, phoneMatcher;
    //Regex

    //Scene Builder getting objects
    @FXML
    TextField classNumberField;
    @FXML
    TextField firstNameField;
    @FXML
    TextField lastNameField;
    @FXML
    TextField emailField;
    @FXML
    TextField EGNField;
    @FXML
    TextField telephoneNumberField;
    @FXML
    TextField positionField;
    @FXML
    Label errorLabel;
    //Scene Builder getting objects

    //ArrayList to store data for incorrect add student options
    private ArrayList<String> errorList = new ArrayList<>();
    //ArrayList to store data for incorrect add student options

    //GUI
    private Stage stage;
    private Scene scene;
    private Parent root;
    //GUI

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

    //If button 'Add Student' is clicked
    public void addStudent() throws SQLException {
        //Resetting the errorLabel and errorList
        errorLabel.setText("");
        errorList.clear();
        //Resetting the errorLabel and errorList

        //Regex handling
        emailRegex = "(?:[a-z0-9!#$%&'*+\\/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+\\/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        phoneRegex = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$";
        emailPattern = Pattern.compile(emailRegex);
        emailMatcher = emailPattern.matcher(emailField.getText());
        phonePattern = Pattern.compile(phoneRegex);
        phoneMatcher = phonePattern.matcher(telephoneNumberField.getText());
        //Regex handling

        //Booleans for regex checks
        boolean phonePassCheck = false;
        boolean emailPassCheck = false;
        //Booleans for regex checks

        //'If' checks for making sure we find if there is any invalid information(not matching regexes) from the Text Fields
        if (!(emailField.getText().equals("") || telephoneNumberField.getText().equals(""))) {
            if (!phoneMatcher.find()) {
                this.errorList.add("The phone does not meet the requirements of the E-Journal!");
            } else {
                phonePassCheck = true;
            }
            if (!emailMatcher.find()) {
                this.errorList.add("The email does not meet the requirements of the E-Journal!");
            } else {
                emailPassCheck = true;
            }
        }
        //'If' checks for making sure we find if there is any invalid information(not matching regexes) from the Text Fields

        //If every field is filled, else check your entries
        if (!(classNumberField.getText().trim().isEmpty()) && !(firstNameField.getText().trim().isEmpty()) && !(lastNameField.getText().trim().isEmpty()) && !(EGNField.getText().trim().isEmpty()) && !(positionField.getText().trim().isEmpty()) && phonePassCheck && emailPassCheck) {
        //If every field is filled, else check your entries


            jdbcAddStudent = new JDBC("select * from registrations", "INSERT INTO registrations (classNumber, firstName, lastName, email, egn, telephoneNumber, position) VALUES (?, ?, ?, ?, ?, ?, ?)");

            //Temp booleans
            boolean matching = false;
            boolean alreadyClassNumber = false;
            boolean alreadyEmail = false;
            boolean alreadyEGN = false;
            boolean alreadyTelephoneNumber = false;
            //Temp booleans

            //Checks if any primary keys are occupied
            try {
                while (jdbcAddStudent.resultSet.next()) {
                    if (jdbcAddStudent.resultSet.getString("classNumber").equals(classNumberField.getText()) || jdbcAddStudent.resultSet.getString("email").equals(emailField.getText()) || jdbcAddStudent.resultSet.getString("egn").equals(EGNField.getText()) || jdbcAddStudent.resultSet.getString("telephoneNumber").equals(telephoneNumberField.getText())) { //uniques are: classNumber, email, egn, telephoneNumber
                        if (jdbcAddStudent.resultSet.getString("classNumber").equals(classNumberField.getText())) {
                            alreadyClassNumber = true;
                        }
                        if (jdbcAddStudent.resultSet.getString("email").equals(emailField.getText())) {
                            alreadyEmail = true;
                        }
                        if (jdbcAddStudent.resultSet.getString("egn").equals(EGNField.getText())) {
                            alreadyEGN = true;
                        }
                        if (jdbcAddStudent.resultSet.getString("telephoneNumber").equals(telephoneNumberField.getText())) {
                            alreadyTelephoneNumber = true;
                        }
                        matching = true;
                    }
                }
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
            //Checks if any primary keys are occupied

            //If there are any occupied primary keys, add error texts to errorList ArrayList
            if (alreadyClassNumber) {
                errorList.add("ClassNumber: " + classNumberField.getText() + " is not available!");
            }
            if (alreadyEmail) {
                errorList.add("Email: " + emailField.getText() + " in not available!");
            }
            if (alreadyEGN) {
                errorList.add("EGN: " + EGNField.getText() + " is not available!");
            }
            if (alreadyTelephoneNumber) {
                errorList.add("Telephone number: " + telephoneNumberField.getText() + " is not available!");
            }
            if (alreadyClassNumber || alreadyEmail || alreadyEGN || alreadyTelephoneNumber) {
                for (String error : errorList) {
                    errorLabel.setText(errorLabel.getText() + error + "\n");
                }
            }
            //If there are any occupied primary keys, add error texts to errorList ArrayList

            //If there are none occupied primary keys, add new student to SQL
            if (!matching) {
                jdbcAddStudent.writeData.setString(1, classNumberField.getText());
                jdbcAddStudent.writeData.setString(2, firstNameField.getText());
                jdbcAddStudent.writeData.setString(3, lastNameField.getText());
                jdbcAddStudent.writeData.setString(4, emailField.getText());
                jdbcAddStudent.writeData.setString(5, EGNField.getText());
                jdbcAddStudent.writeData.setString(6, telephoneNumberField.getText());
                jdbcAddStudent.writeData.setString(7, positionField.getText());
                jdbcAddStudent.writeData.executeUpdate();
                jdbcAddStudent.writeData.close();
                errorLabel.setText("Successfully added student!");
                //TODO: open connection again to prevent creating new object every time button 'Add Student' is clicked
            }
            //If there are none occupied primary keys, add new student to SQL

            //TODO: optimize and remove!
            jdbcAddStudent.resultSet.beforeFirst(); //TODO: test if can be removed
            //TODO: optimize and remove!

        //if there are some unfilled fields, check your entries
        } else {
            if (!errorList.isEmpty()) {
                if (!phonePassCheck || !emailPassCheck) {
                    for (String error : errorList) {
                        errorLabel.setText(errorLabel.getText() + error + "\n");
                    }
                }
            } else {
                errorLabel.setText("Please check your entries and try again!");
            }
        }
        //if there are some unfilled fields, check your entries

    }
    //If button 'Add Student' is clicked

    //Making sure our headmaster info is not lost
    public void transferHeadmaster(Headmaster headmaster) {
        this.headmaster = headmaster;
    }
    //Making sure our headmaster info is not lost

}
