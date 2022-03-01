package Controllers;

import JDBC.JDBC;
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

public class AddStudentsController {

    //JDBC OBJECTS
    static JDBC jdbcAddStudent;
    //JDBC OBJECTS


    @FXML
    TextField classNumberField; //ready check
    @FXML
    TextField firstNameField; //ready check
    @FXML
    TextField lastNameField; //ready check
    @FXML
    TextField emailField; //ready check
    @FXML
    TextField EGNField; //ready check
    @FXML
    TextField telephoneNumberField; //ready check
    @FXML
    TextField positionField; //ready check
    @FXML
    Label errorLabel;


    private ArrayList<String> errorList = new ArrayList<>();

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void goBackToHeadmasterStage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/headmaster.fxml"));
        this.root = loader.load();
        this.stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
        this.scene = new Scene(root, 1280, 720);
        this.stage.setScene(scene);
        this.stage.centerOnScreen();
        this.scene.getRoot().requestFocus();
        this.stage.show();
    }

    public void addStudent() throws SQLException {
        errorLabel.setText("");
        errorList.clear();
        String emailRegex = "(?:[a-z0-9!#$%&'*+\\/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+\\/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        String phoneRegex = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$";
        Pattern emailPattern = Pattern.compile(emailRegex);
        Matcher emailMatcher = emailPattern.matcher(emailField.getText());
        Pattern phonePattern = Pattern.compile(phoneRegex);
        Matcher phoneMatcher = phonePattern.matcher(telephoneNumberField.getText());
        boolean phonePassCheck = false;
        boolean emailPassCheck = false;
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
        if (!(classNumberField.getText().trim().isEmpty()) && !(firstNameField.getText().trim().isEmpty()) && !(lastNameField.getText().trim().isEmpty()) && !(EGNField.getText().trim().isEmpty()) && !(positionField.getText().trim().isEmpty()) && phonePassCheck && emailPassCheck) {
            jdbcAddStudent = new JDBC("select * from registrations", "INSERT INTO registrations (classNumber, firstName, lastName, email, egn, telephoneNumber, position) VALUES (?, ?, ?, ?, ?, ?, ?)");
            boolean matching = false;
            boolean alreadyClassNumber = false;
            boolean alreadyEmail = false;
            boolean alreadyEGN = false;
            boolean alreadyTelephoneNumber = false;
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
                 //TODO: open connection again
                errorLabel.setText("Successfully added student!");
            }
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
            jdbcAddStudent.resultSet.beforeFirst(); //TODO: test if can be removed
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
    }
}
