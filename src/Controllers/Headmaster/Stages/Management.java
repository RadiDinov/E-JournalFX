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

public class Management {

    //JDBC
    static JDBC jdbcAddNewRegistration; //primary keys - classNumber, email, egn, telephoneNumber
    static JDBC jdbcAddNewTeacher; //primary keys - email, classTeacherOfASection TODO: work on adding teachers and setting them to be class teachers if needed
    static JDBC jdbcGetTables;
    static JDBC jdbcAddNewStudent;
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

        //TODO: open connection again to prevent creating new object every time button 'Add Student' is clicked
        jdbcGetTables = new JDBC("Show tables");
        //TODO: open connection again to prevent creating new object every time button 'Add Student' is clicked

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

            //TODO: open connection again to prevent creating new object every time button 'Add Student' is clicked
            jdbcAddNewRegistration = new JDBC("select * from registrations", "INSERT INTO registrations (classNumber, firstName, lastName, email, egn, telephoneNumber, position) VALUES (?, ?, ?, ?, ?, ?, ?)");
            //TODO: open connection again to prevent creating new object every time button 'Add Student' is clicked

            //Temp booleans
            boolean matching = false;
            boolean alreadyClassNumber = false;
            boolean alreadyEmail = false;
            boolean alreadyEGN = false;
            boolean alreadyTelephoneNumber = false;
            //Temp booleans

            //Checks if any primary keys are occupied
            try {
                while (jdbcAddNewRegistration.resultSet.next()) {
                    if (jdbcAddNewRegistration.resultSet.getString("classNumber").equals(classNumberField.getText()) || jdbcAddNewRegistration.resultSet.getString("email").equals(emailField.getText()) || jdbcAddNewRegistration.resultSet.getString("egn").equals(EGNField.getText()) || jdbcAddNewRegistration.resultSet.getString("telephoneNumber").equals(telephoneNumberField.getText())) { //uniques are: classNumber, email, egn, telephoneNumber
                        if (jdbcAddNewRegistration.resultSet.getString("classNumber").equals(classNumberField.getText())) {
                            alreadyClassNumber = true;
                        }
                        if (jdbcAddNewRegistration.resultSet.getString("email").equals(emailField.getText())) {
                            alreadyEmail = true;
                        }
                        if (jdbcAddNewRegistration.resultSet.getString("egn").equals(EGNField.getText())) {
                            alreadyEGN = true;
                        }
                        if (jdbcAddNewRegistration.resultSet.getString("telephoneNumber").equals(telephoneNumberField.getText())) {
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


            //Booleans for matches
            boolean isThereATableThatEqualsSection = false;
            //Booleans for matches

            //This string is representing the table's name TODO: FOR MORE SAFETY MAKE IT TO WORK WITH BOTH - EMAIL + CLASSNUMBER FIELDS. CURRENTLY ONLY WITH CLASSNUMBER FIELD example: in emailField inserted '18101@uktc-bg.com', in classNumberField inserted '18201'. If substrings(0,3) from both are equal, no problem. Else say that classNumber and email must be equal. Example - '18101@uktc-bg.com' and '18201' - NOT GOOD; Example - '18101@uktc-bg.com' and '18202' - NOT GOOD
            String sectionFieldModifiedForSQL = "section_" + classNumberField.getText().substring(0, 3); //example: section_181
            //This string is representing the table's name TODO: FOR MORE SAFETY MAKE IT TO WORK WITH BOTH - EMAIL + CLASSNUMBER FIELDS. CURRENTLY ONLY WITH CLASSNUMBER FIELD example: in emailField inserted '18101@uktc-bg.com', in classNumberField inserted '18201'. If substrings(0,3) from both are equal, no problem. Else say that classNumber and email must be equal. Example - '18101@uktc-bg.com' and '18201' - NOT GOOD; Example - '18101@uktc-bg.com' and '18202' - NOT GOOD

            //Initializing boolean sectionFieldModifiedForSQL -> (true ? false)
            while (jdbcGetTables.getResultSet().next()) {
                System.out.println(sectionFieldModifiedForSQL);
                System.out.println(jdbcGetTables.getResultSet().getString(1));

                //If there is already a table with this section
                if (sectionFieldModifiedForSQL.equals(jdbcGetTables.getResultSet().getString(1))) {
                    isThereATableThatEqualsSection = true;
                    break;
                }
                //If there is already a table with this section

            }
            //Initializing boolean sectionFieldModifiedForSQL -> (true ? false)

            //TODO: optimize and remove!
            jdbcGetTables.resultSet.beforeFirst(); //TODO: test if can be removed
            //TODO: optimize and remove!

            //If there are none occupied primary keys
            if (!matching) {

                //If wanted to add student
                if (positionField.getText().equals("Student")) {

                    //If there is an existing table so the student can be added in it
                    if (isThereATableThatEqualsSection) {

                        //TODO: open connection again to prevent creating new object every time button 'Add Student' is clicked
                        jdbcAddNewStudent = new JDBC("select * from " + sectionFieldModifiedForSQL, "INSERT INTO " + sectionFieldModifiedForSQL + " (firstName, lastName, classNumber, position) VALUES (?, ?, ?, ?)");
                        //TODO: open connection again to prevent creating new object every time button 'Add Student' is clicked

                        //If resultSet is empty, this means that there is no class teacher
                        if (!jdbcAddNewStudent.resultSet.next()) {
                            errorLabel.setText("Cannot create a new student in section: " + classNumberField.getText().substring(0, 3) + "\nbecause this section does not have a class teacher!");
                        }
                        //If resultSet is empty, this means that there is no class teacher

                        //If resultSet is not empty
                        else {

                            //Selecting first row's data
                            jdbcAddNewStudent.resultSet.absolute(1);
                            //Selecting first row's data

                            //If first row's selected position equals Teacher
                            if (jdbcAddNewStudent.resultSet.getString("position").equals("Teacher")) {

                                //Adding student
                                jdbcAddNewRegistration.writeData.setString(1, classNumberField.getText());
                                jdbcAddNewRegistration.writeData.setString(2, firstNameField.getText());
                                jdbcAddNewRegistration.writeData.setString(3, lastNameField.getText());
                                jdbcAddNewRegistration.writeData.setString(4, emailField.getText());
                                jdbcAddNewRegistration.writeData.setString(5, EGNField.getText());
                                jdbcAddNewRegistration.writeData.setString(6, telephoneNumberField.getText());
                                jdbcAddNewRegistration.writeData.setString(7, positionField.getText());
                                jdbcAddNewRegistration.writeData.executeUpdate();
                                jdbcAddNewRegistration.writeData.close();
                                //Adding student

                                //Inserting student into its section
                                jdbcAddNewStudent.writeData.setString(1, firstNameField.getText());
                                jdbcAddNewStudent.writeData.setString(2, lastNameField.getText());
                                jdbcAddNewStudent.writeData.setString(3, classNumberField.getText());
                                jdbcAddNewStudent.writeData.setString(4, positionField.getText());
                                jdbcAddNewStudent.writeData.executeUpdate();
                                jdbcAddNewStudent.writeData.close();
                                errorLabel.setText("Added new student: " + firstNameField.getText() + " " + lastNameField.getText());
                                //Inserting student into its section

                            }
                            //If first row's selected position equals Teacher

                            //If first row's selected position does not equal Teacher
                            else {
                                errorLabel.setText("Cannot create a new student in section: " + classNumberField.getText().substring(0, 3) + "\nbecause this section does not have a class teacher!");
                            }
                            //If first row's selected position does not equal Teacher

                        }
                        //If resultSet is not empty

                    }
                    //If there is an existing table so the student can be added in it

                    //If there isn't an existing table for the student to be added in
                    else {
                        errorLabel.setText("Cannot create a new student in section: " + classNumberField.getText().substring(0, 3) + "\nSection does not exist!");
                    }
                    //If there isn't an existing table for the student to be added in

                }
                //If wanted to add student
            }
            //If there are none occupied primary keys

            //TODO: optimize and remove!
            jdbcAddNewRegistration.resultSet.beforeFirst(); //TODO: test if can be removed
            //TODO: optimize and remove!

        //If there are some unfilled fields, check your entries OR matchers caught some problems
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
        //If there are some unfilled fields, check your entries OR matchers caught some problems

    }
    //If button 'Add Student' is clicked

    //Making sure our headmaster info is not lost
    public void transferHeadmaster(Headmaster headmaster) {
        this.headmaster = headmaster;
    }
    //Making sure our headmaster info is not lost

}
