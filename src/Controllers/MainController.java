package Controllers;

import JDBC.JDBC;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class MainController {

    public MainController() {

    }

    //OBJECTS
    private BCryptPasswordHashing bCryptPasswordHashing = new BCryptPasswordHashing(12);
    //JDBC OBJECTS
    private static JDBC jdbcUpdatePassword;
    //JDBC OBJECTS
    //OBJECTS

    //INJECTION
    @FXML
    AnchorPane anchorPaneMain;
    @FXML
    TextField emailField;
    @FXML
    PasswordField passwordField;
    @FXML
    Label errorLabel;
    //INJECTION

    //PRIVATE
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String egn;
    private String position;

    private boolean passwordMatch;
    private boolean successfulSignIn;
    private boolean signedInForTheFirstTimeOnlyEmail;
    private boolean signedInForTheFirstTime;

    //GUI
    private Stage stage;
    private Scene scene;
    private Parent root;
    //GUI

    public void trySignIn(ActionEvent event) throws IOException, SQLException {
        jdbcUpdatePassword = new JDBC("select * from registrations", "UPDATE registrations SET password = ? WHERE email = ?");
        this.bCryptPasswordHashing = new BCryptPasswordHashing(12);
        this.successfulSignIn = false;
        this.signedInForTheFirstTimeOnlyEmail = false;
        this.signedInForTheFirstTime = false;
        try {
            while (jdbcUpdatePassword.resultSet.next()) {
                if (jdbcUpdatePassword.resultSet.getString("password") != null) {
                    this.passwordMatch = bCryptPasswordHashing.verifyPassword(passwordField.getText().toCharArray(), jdbcUpdatePassword.resultSet.getString("password"));
                }
                if (jdbcUpdatePassword.resultSet.getString("email").equals(emailField.getText()) && jdbcUpdatePassword.resultSet.getString("password") == null && !(passwordField.getText().trim().isEmpty())) {
                    this.signedInForTheFirstTime = true;
                    this.successfulSignIn = true;
//                    System.out.println("Email + password inserted");
                    break;
                } else if (jdbcUpdatePassword.resultSet.getString("email").equals(emailField.getText()) && jdbcUpdatePassword.resultSet.getString("password") == null) {
                    System.out.println("Only email inserted");
                    this.signedInForTheFirstTimeOnlyEmail = true;
                    this.successfulSignIn = true;
                    break;
                } else if (jdbcUpdatePassword.resultSet.getString("email").equals(emailField.getText()) && passwordMatch) {
                    switch (jdbcUpdatePassword.resultSet.getString("position")) {
                        case "Headmaster" -> {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/headmaster.fxml"));
                            this.root = loader.load();
                            HeadmasterController headmasterController = loader.getController();
                            headmasterController.getInformation(jdbcUpdatePassword.resultSet.getString("firstName"), jdbcUpdatePassword.resultSet.getString("lastName"), jdbcUpdatePassword.resultSet.getString("email"), jdbcUpdatePassword.resultSet.getString("password"), jdbcUpdatePassword.resultSet.getString("egn"), jdbcUpdatePassword.resultSet.getString("position"));
                            this.stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
                            this.scene = new Scene(root, 1280, 720);
                            this.stage.setScene(scene);
                            this.stage.centerOnScreen();
                            scene.getRoot().requestFocus();
                            stage.show();
                            this.successfulSignIn = true;
                        }
                        case "Student" -> {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/student.fxml"));
                            this.root = loader.load();
//                            HeadmasterController headmasterController = loader.getController();
//                            headmasterController.getInformation(jdbcUpdatePassword.resultSet.getString("firstName"), jdbcUpdatePassword.resultSet.getString("lastName"), jdbcUpdatePassword.resultSet.getString("email"), jdbcUpdatePassword.resultSet.getString("password"), jdbcUpdatePassword.resultSet.getString("egn"), jdbcUpdatePassword.resultSet.getString("position"));
                            StudentController studentController = loader.getController();
                            studentController.getInformation(jdbcUpdatePassword.resultSet.getString("firstName"), jdbcUpdatePassword.resultSet.getString("lastName"), jdbcUpdatePassword.resultSet.getString("email"), jdbcUpdatePassword.resultSet.getString("password"), jdbcUpdatePassword.resultSet.getString("egn"), jdbcUpdatePassword.resultSet.getString("position"));
                            this.stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
                            this.scene = new Scene(root, 1280, 720);
                            this.stage.setScene(scene);
                            this.stage.centerOnScreen();
                            scene.getRoot().requestFocus();
                            stage.show();
                            this.successfulSignIn = true;
                        }
                    }
                }
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        jdbcUpdatePassword.resultSet.beforeFirst(); //TODO: test if can be removed
        if (signedInForTheFirstTimeOnlyEmail) {
            this.errorLabel.setText("Welcome, please insert password and remember it!");
        }
        if (signedInForTheFirstTime) {
            this.errorLabel.setText("You just set your password. Remember it!");
            String hashedPassword = bCryptPasswordHashing.hashPassword(passwordField.getText().toCharArray());
            try {
                jdbcUpdatePassword.writeData.setString(1, hashedPassword);
                jdbcUpdatePassword.writeData.setString(2, emailField.getText());
                jdbcUpdatePassword.writeData.executeUpdate();
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        }
        if (!successfulSignIn) {
            jdbcUpdatePassword.resultSet.beforeFirst();
            this.errorLabel.setText("Please check your entries and try again.");
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEgn() {
        return egn;
    }

    public void setEgn(String egn) {
        this.egn = egn;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
