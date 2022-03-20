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
    static JDBC jdbcRegistrationUpdateTeacher;
    static JDBC jdbcTeacherUpdatePosition;
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
    @FXML
    TextField classTeacherAddEmailField;
    @FXML
    TextField classTeacherAddSectionField;
    //Scene Builder getting objects

    //ArrayList to store data for incorrect add student options
    private ArrayList<String> errorList = new ArrayList<>();
    //ArrayList to store data for incorrect add student options

    //Variables
    private String classNumberFieldModifiedForSQL;
    private boolean isThereATableThatEqualsSection;
    //Variables

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

            //If adding teacher
            if (!classNumberField.getText().equals("-")) {

                //Booleans for matches
                isThereATableThatEqualsSection = false;
                //Booleans for matches

                //This string is representing the table's name
                classNumberFieldModifiedForSQL = "section_" + classNumberField.getText().substring(0, 3);
                //This string is representing the table's name

                //Initializing boolean classNumberFieldModifiedForSQL -> (true ? false)
                while (jdbcGetTables.getResultSet().next()) {

                    //If there is already a table with this section
                    if (classNumberFieldModifiedForSQL.equals(jdbcGetTables.getResultSet().getString(1))) {
                        isThereATableThatEqualsSection = true;
                        break;
                    }
                    //If there is already a table with this section

                }
                //Initializing boolean classNumberFieldModifiedForSQL -> (true ? false)

            }
            //If adding teacher


            //TODO: optimize and remove!
            jdbcGetTables.resultSet.beforeFirst(); //TODO: test if can be removed
            //TODO: optimize and remove!

            //If there are none occupied primary keys
            if (!matching) {

                //If wanted to add student
                if (positionField.getText().equals("Student")) {

                    //If class number is valid (length 5)
                    if (classNumberField.getText().length() == 5) {

                        //If email is valid (length till @ is 5)
                        if (emailField.getText().substring(0, 5).length() == 5) {

                            //If ('181'26 .equals '181'26@uktc-bg.com) -> sections are matching
                            if (classNumberField.getText().substring(0, 3).equals(emailField.getText().substring(0, 3))) {

                                //If emailField is valid(more than 3 chars before @)
                                if (emailField.getText().length() >= 3) {

                                    //If (181'26' .equals 181'26'@uktc-bg.com) -> class numbers are matching
                                    if (classNumberField.getText().substring(3, 5).equals(emailField.getText().substring(3, 5))) {

                                        //If there is an existing table so the student can be added in it
                                        if (isThereATableThatEqualsSection) {

                                            //TODO: open connection again to prevent creating new object every time button 'Add Student' is clicked
                                            jdbcAddNewStudent = new JDBC("select * from " + classNumberFieldModifiedForSQL, "INSERT INTO " + classNumberFieldModifiedForSQL + " (firstName, lastName, classNumber, position) VALUES (?, ?, ?, ?)");
                                            //TODO: open connection again to prevent creating new object every time button 'Add Student' is clicked

                                            //If resultSet is empty, this means that there is no Class Teacher
                                            if (!jdbcAddNewStudent.resultSet.next()) {
                                                errorLabel.setText("Cannot create a new student in section: " + classNumberField.getText().substring(0, 3) + "\nbecause this section does not have a class teacher!");
                                            }
                                            //If resultSet is empty, this means that there is no Class Teacher

                                            //If resultSet is not empty
                                            else {

                                                //Selecting first row's data
                                                jdbcAddNewStudent.resultSet.absolute(1);
                                                //Selecting first row's data

                                                //If first row's selected position equals Class Teacher
                                                if (jdbcAddNewStudent.resultSet.getString("position").equals("Class Teacher")) {

                                                    //Adding student in registrations table
                                                    jdbcAddNewRegistration.writeData.setString(1, classNumberField.getText());
                                                    jdbcAddNewRegistration.writeData.setString(2, firstNameField.getText());
                                                    jdbcAddNewRegistration.writeData.setString(3, lastNameField.getText());
                                                    jdbcAddNewRegistration.writeData.setString(4, emailField.getText());
                                                    jdbcAddNewRegistration.writeData.setString(5, EGNField.getText());
                                                    jdbcAddNewRegistration.writeData.setString(6, telephoneNumberField.getText());
                                                    jdbcAddNewRegistration.writeData.setString(7, positionField.getText());
                                                    jdbcAddNewRegistration.writeData.executeUpdate();
                                                    jdbcAddNewRegistration.writeData.close();
                                                    //Adding student in registrations table

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
                                                //If first row's selected position equals Class Teacher

                                                //If first row's selected position does not equal Class Teacher
                                                else {
                                                    errorLabel.setText("Cannot create a new student in section: " + classNumberField.getText().substring(0, 3) + "\nbecause this section does not have a class teacher!");
                                                }
                                                //If first row's selected position does not equal Class Teacher

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
                                    //If (181'26' .equals 181'26'@uktc-bg.com) -> class numbers are matching

                                    //If !(181'26' .equals 181'26'@uktc-bg.com) -> class numbers are not matching
                                    else {
                                        errorLabel.setText("Class numbers from email field and class number field are not matching!");
                                    }
                                    //If !(181'26' .equals 181'26'@uktc-bg.com) -> class numbers are not matching

                                }
                                //If emailField is valid(more than 3 chars before @)

                                //If emailField is invalid(less than 3 chars before @)
                                else {
                                    errorLabel.setText("Email is invalid!");
                                }
                                //If emailField is invalid(less than 3 chars before @)

                            }
                            //If ('181'26 .equals '181'26@uktc-bg.com) -> sections are matching

                            //If !('181'26 .equals '181'26@uktc-bg.com) -> sections are not matching
                            else {
                                errorLabel.setText("Sections from email field and class number field are not matching!");
                            }
                            //If !('181'26 .equals '181'26@uktc-bg.com) (sections are not matching)

                        }
                        //If email is valid (length till @ is 5)

                        //If email is invalid
                        else {
                            errorLabel.setText("Email before @ is not full!");
                        }
                        //If email is invalid

                    }
                    //If class number is valid (length 5)

                    //If class number is invalid
                    else {
                        errorLabel.setText("Class number is not full!");
                    }
                    //If class number is invalid

                }
                //If wanted to add student

                //If wanted to add teacher
                else if (positionField.getText().equals("Teacher")) {

                    //TODO: open connection again to prevent creating new object every time button 'Add Student' is clicked
                    jdbcAddNewTeacher = new JDBC("select * from teachers", "INSERT INTO teachers (firstName, lastName, email, classTeacherOfASection) VALUES (?, ?, ?, ?)");
                    //TODO: open connection again to prevent creating new object every time button 'Add Student' is clicked


                    //If email before @ is 5 chars long
                    if (emailField.getText().substring(0, 5).length() == 5) {

                        //If class number is '-'
                        if (classNumberField.getText().equals("-")) {

                            classNumberField.setText("Unspecified");

                            //Adding teacher in registrations table
                            jdbcAddNewRegistration.writeData.setString(1, classNumberField.getText());
                            jdbcAddNewRegistration.writeData.setString(2, firstNameField.getText());
                            jdbcAddNewRegistration.writeData.setString(3, lastNameField.getText());
                            jdbcAddNewRegistration.writeData.setString(4, emailField.getText());
                            jdbcAddNewRegistration.writeData.setString(5, EGNField.getText());
                            jdbcAddNewRegistration.writeData.setString(6, telephoneNumberField.getText());
                            jdbcAddNewRegistration.writeData.setString(7, positionField.getText());
                            jdbcAddNewRegistration.writeData.executeUpdate();
                            jdbcAddNewRegistration.writeData.close();
                            //Adding teacher in registrations table

                            //Inserting teacher into teachers table
                            jdbcAddNewTeacher.writeData.setString(1, firstNameField.getText());
                            jdbcAddNewTeacher.writeData.setString(2, lastNameField.getText());
                            jdbcAddNewTeacher.writeData.setString(3, emailField.getText());
                            jdbcAddNewTeacher.writeData.setString(4, classNumberField.getText());
                            jdbcAddNewTeacher.writeData.executeUpdate();
                            jdbcAddNewTeacher.writeData.close();
                            //Inserting teacher into teachers table

                            classNumberField.setText("-");

                            errorLabel.setText("Added new teacher: " + firstNameField.getText() + " " + lastNameField.getText() + "\nEmail: " + emailField.getText() + "\nClass teacher of the section: " + classNumberField.getText());


                        }
                        //If class number is '-'

                        //If class number is not '-'
                        else {
                            errorLabel.setText("Class number is entered incorrectly!");
                        }
                        //If class number is not '-'

                    }
                    //If email before @ is 5 chars long

                    //If email before @ is not 5 chars long
                    else {
                        errorLabel.setText("Email is entered incorrectly");
                    }
                    //If email before @ is not 5 chars long

                }
                //If wanted to add teacher

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

    public void setTextFieldsForStudent() {
        if (!(classNumberField.getText().equals(""))) {
            String formEmail = classNumberField.getText() + "@uktc-bg.com";
            emailField.setText(formEmail);
        }
        positionField.setText("Student");

    }

    public void setTextFieldsForTeacher() {
        if (!(firstNameField.getText().equals("")) && !(lastNameField.getText().equals(""))) {
            String regex = "[AEIOUaeiou]";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(firstNameField.getText());

            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            if (!matcher.find()) {
                firstName = convertCyrillic(firstName);
                lastName = convertCyrillic(lastName);
            }
            //TODO: if inserted first and last name are cyrillic
            //TODO: if inserted first and last name are not cyrillic
            String formEmail = firstName.toLowerCase().charAt(0) + "." + lastName.toLowerCase() + "@uktc-bg.com";
            emailField.setText(formEmail);
        }
        classNumberField.setText("-");
        positionField.setText("Teacher");
    }

    public void resetFields() {
        classNumberField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
        EGNField.setText("");
        telephoneNumberField.setText("");
        positionField.setText("");
        errorLabel.setText("");
    }

    public static String convertCyrillic(String message) {
        char[] abcCyr = {' ', 'я', 'в', 'е', 'р', 'т', 'ъ', 'у', 'и', 'о', 'п', 'ш', 'щ', 'а', 'с', 'д', 'ф', 'г', 'х', 'й', 'к', 'л', 'ю', 'з', 'ь', 'ц', 'ж', 'б', 'н', 'м', 'ч', 'Ч', 'Я', 'В', 'Е', 'Р', 'Т', 'Ъ', 'У', 'И', 'О', 'П', 'Ш', 'Щ', 'А', 'С', 'Д', 'Ф', 'Г', 'Х', 'Й', 'К', 'Л', 'Ю', 'З', 'Ь', 'Ц', 'Ж', 'Б', 'Н', 'М'};
        String[] abcLat = {" ", "ya", "v", "e", "r", "t", "a", "i", "i", "o", "p", "sh", "sht", "a", "s", "d", "f", "g", "h", "i", "k", "l", "iu", "z", "io", "c", "zh", "b", "n", "m", "ch", "Ch", "Ya", "V", "E", "R", "T", "A", "I", "I", "O", "P", "Sh", "Sht", "A", "S", "D", "F", "G", "H", "I", "K", "L", "Iu", "Z", "Io", "C", "Zh", "B", "N", "M"};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            for (int x = 0; x < abcCyr.length; x++) {
                if (message.charAt(i) == abcCyr[x]) {
                    builder.append(abcLat[x]);
                }
            }
        }
        return builder.toString();
    }

    public void addNewClassTeacher() throws SQLException {
        //Resetting the errorLabel and errorList
        errorLabel.setText("");
        errorList.clear();
        //Resetting the errorLabel and errorList


        isThereATableThatEqualsSection = false;

        jdbcGetTables = new JDBC("Show tables");

        jdbcTeacherUpdatePosition = new JDBC("select * from teachers", "UPDATE teachers SET classTeacherOfASection = ? WHERE email = ?");

        jdbcRegistrationUpdateTeacher = new JDBC("select * from registrations", "UPDATE registrations SET classNumber = ?, position = ? WHERE email = ?");

        boolean emailFieldCheck = false;
        boolean sectionFieldCheck = false;
        boolean teacherIsAlreadyAClassTeacher = true;
        boolean sectionHasClassTeacher = true;
        boolean addedNewClassTeacher = false;

        classNumberFieldModifiedForSQL = "section_" + classTeacherAddSectionField.getText();

        while (jdbcGetTables.getResultSet().next()) {

            //If there is already a table with this section
            if (classNumberFieldModifiedForSQL.equals(jdbcGetTables.getResultSet().getString(1))) {
                isThereATableThatEqualsSection = true;
                break;
            }
            //If there is already a table with this section

        }

        if (isThereATableThatEqualsSection) {
            //TODO: open connection again to prevent creating new object every time button 'Add Student' is clicked
            jdbcAddNewStudent = new JDBC("select * from " + classNumberFieldModifiedForSQL, "INSERT INTO " + classNumberFieldModifiedForSQL + " (firstName, lastName, classNumber, position) VALUES (?, ?, ?, ?)");
            //TODO: open connection again to prevent creating new object every time button 'Add Student' is clicked

            jdbcAddNewStudent.resultSet.beforeFirst();
        }

        while (jdbcTeacherUpdatePosition.resultSet.next()) {
            if (jdbcTeacherUpdatePosition.resultSet.getString("email").equals(classTeacherAddEmailField.getText())) {
                emailFieldCheck = true;
                while (jdbcRegistrationUpdateTeacher.resultSet.next()) {
                    if (jdbcRegistrationUpdateTeacher.resultSet.getString("position").equals("Teacher")) {
                        teacherIsAlreadyAClassTeacher = false;
                        if (isThereATableThatEqualsSection) {
                            sectionFieldCheck = true;
                            if (!jdbcAddNewStudent.getResultSet().next()) {
                                sectionHasClassTeacher = false;

                                jdbcAddNewStudent.writeData.setString(1, jdbcTeacherUpdatePosition.resultSet.getString("firstName"));
                                jdbcAddNewStudent.writeData.setString(2, jdbcTeacherUpdatePosition.resultSet.getString("lastName"));
                                jdbcAddNewStudent.writeData.setString(3, classTeacherAddSectionField.getText());
                                jdbcAddNewStudent.writeData.setString(4, "Class Teacher");
                                jdbcAddNewStudent.writeData.executeUpdate();
                                jdbcAddNewStudent.writeData.close();

                                if (jdbcRegistrationUpdateTeacher.resultSet.getString("email").equals(classTeacherAddEmailField.getText())) {
                                    jdbcRegistrationUpdateTeacher.writeData.setString(1, classTeacherAddSectionField.getText());
                                    jdbcRegistrationUpdateTeacher.writeData.setString(2, "Class Teacher");
                                    jdbcRegistrationUpdateTeacher.writeData.setString(3, classTeacherAddEmailField.getText());
                                    jdbcRegistrationUpdateTeacher.writeData.executeUpdate();
                                    jdbcRegistrationUpdateTeacher.writeData.close();
                                }

                                jdbcTeacherUpdatePosition.writeData.setString(1, classTeacherAddSectionField.getText());
                                jdbcTeacherUpdatePosition.writeData.setString(2, classTeacherAddEmailField.getText());
                                jdbcTeacherUpdatePosition.writeData.executeUpdate();

                                errorLabel.setText("Added new class teacher: " + jdbcTeacherUpdatePosition.resultSet.getString("firstName") + " " + jdbcTeacherUpdatePosition.resultSet.getString("lastName") + "\nEmail: " + jdbcTeacherUpdatePosition.resultSet.getString("email") + "\nClass teacher of the section: " + classTeacherAddSectionField.getText());
                                addedNewClassTeacher = true;
                                jdbcTeacherUpdatePosition.writeData.close();
                                break;
                            }
                        }
                    }
                }
            }
        }
        if(!addedNewClassTeacher) {
            if (sectionHasClassTeacher) {
                errorLabel.setText("Section already has Class Teacher!");
            }
            if (!sectionFieldCheck) {
                errorLabel.setText("Section not found!");
            }
            if (teacherIsAlreadyAClassTeacher) {
                errorLabel.setText("Teacher is already a Class Teacher!");
                //TODO: maybe add which section errorList.add("in section: " + ...);
            }
            if (!emailFieldCheck) {
                errorLabel.setText("There is no teacher with that email!");
            }
        }

    }

}


//Selecting first row's data
//                                                jdbcAddNewStudent.resultSet.absolute(1);
//                                                        //Selecting first row's data
//
//                                                        //If first row's selected position equals Class Teacher
//                                                        if (jdbcAddNewStudent.resultSet.getString("position").equals("Class Teacher")) {
//
//                                                        //Adding student in registrations table
//                                                        jdbcAddNewRegistration.writeData.setString(1, classNumberField.getText());
//                                                        jdbcAddNewRegistration.writeData.setString(2, firstNameField.getText());
//                                                        jdbcAddNewRegistration.writeData.setString(3, lastNameField.getText());
//                                                        jdbcAddNewRegistration.writeData.setString(4, emailField.getText());
//                                                        jdbcAddNewRegistration.writeData.setString(5, EGNField.getText());
//                                                        jdbcAddNewRegistration.writeData.setString(6, telephoneNumberField.getText());
//                                                        jdbcAddNewRegistration.writeData.setString(7, positionField.getText());
//                                                        jdbcAddNewRegistration.writeData.executeUpdate();
//                                                        jdbcAddNewRegistration.writeData.close();
//                                                        //Adding student in registrations table
//
//                                                        //Inserting student into its section
//                                                        jdbcAddNewStudent.writeData.setString(1, firstNameField.getText());
//                                                        jdbcAddNewStudent.writeData.setString(2, lastNameField.getText());
//                                                        jdbcAddNewStudent.writeData.setString(3, classNumberField.getText());
//                                                        jdbcAddNewStudent.writeData.setString(4, positionField.getText());
//                                                        jdbcAddNewStudent.writeData.executeUpdate();
//                                                        jdbcAddNewStudent.writeData.close();
//                                                        errorLabel.setText("Added new student: " + firstNameField.getText() + " " + lastNameField.getText());
//                                                        //Inserting student into its section
//
//                                                        }
//                                                        //If first row's selected position equals Teacher
//
//                                                        //If first row's selected position does not equal Teacher
//                                                        else {
//                                                        errorLabel.setText("Cannot create a new student in section: " + classNumberField.getText().substring(0, 3) + "\nbecause this section does not have a class teacher!");
//                                                        }
//If first row's selected position does not equal Teacher
