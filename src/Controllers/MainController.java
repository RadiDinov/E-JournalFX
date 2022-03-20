package Controllers;

import Controllers.Headmaster.HeadmasterController;
import Controllers.Student.StudentController;
import Controllers.Teacher.TeacherController;
import JDBC.JDBC;
import SaltHasher.BCryptPasswordHashing;
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

    //Salt Hasher
    private BCryptPasswordHashing bCryptPasswordHashing = new BCryptPasswordHashing(12);
    //Salt Hasher

    //JDBC
    private static JDBC jdbcUpdatePassword;
    //JDBC

    //Scene Builder getting objects
    @FXML
    AnchorPane anchorPaneMain;
    @FXML
    TextField emailField;
    @FXML
    PasswordField passwordField;
    @FXML
    Label errorLabel;
    //Scene Builder getting objects

    //Variables
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String egn;
    private String position;

    private boolean passwordMatch;
    //Variables

    //GUI
    private Stage stage;
    private Scene scene;
    private Parent root;
    //GUI

    //If button 'Sign In' is clicked
    public void trySignIn(ActionEvent event) throws IOException, SQLException {

        jdbcUpdatePassword = new JDBC("select * from registrations", "UPDATE registrations SET password = ? WHERE email = ?");

        //Creating new Salt Hasher with security 12(max 16)
        this.bCryptPasswordHashing = new BCryptPasswordHashing(12);
        //Creating new Salt Hasher with security 12(max 16)

        //Temp booleans
        boolean successfulSignIn = false;
        boolean signedInForTheFirstTimeOnlyEmail = false;
        boolean signedInForTheFirstTime = false;
        //Temp booleans

        try {
            //Cycling through every object from SQL table 'registrations'
            while (jdbcUpdatePassword.resultSet.next()) {

                //Making sure password is not null, so the Salt Hasher can be created
                if (jdbcUpdatePassword.resultSet.getString("password") != null) {
                    this.passwordMatch = bCryptPasswordHashing.verifyPassword(passwordField.getText().toCharArray(), jdbcUpdatePassword.resultSet.getString("password"));
                }
                //Making sure password is not null, so the Salt Hasher can be created


                //If new Person entered Email + password, set password as his own password
                if (jdbcUpdatePassword.resultSet.getString("email").equals(emailField.getText()) && jdbcUpdatePassword.resultSet.getString("password") == null && !(passwordField.getText().trim().isEmpty())) {
                    signedInForTheFirstTime = true;
                    successfulSignIn = true;
                    break;
                //If new Person entered Email + password, set password as his own password

                //If new Person entered only Email, ask him to add password
                } else if (jdbcUpdatePassword.resultSet.getString("email").equals(emailField.getText()) && jdbcUpdatePassword.resultSet.getString("password") == null) {
                    System.out.println("Only email inserted");
                    signedInForTheFirstTimeOnlyEmail = true;
                    successfulSignIn = true;
                    break;
                //If new Person entered only Email, ask him to add password

                //If the Person already has a registration, check its position then redirect to its position's Stage
                } else if (jdbcUpdatePassword.resultSet.getString("email").equals(emailField.getText()) && passwordMatch) {

                    //Switching by Signed In Person's position
                    switch (jdbcUpdatePassword.resultSet.getString("position")) {

                        //If position equals 'Headmaster'
                        case "Headmaster" -> {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/headmaster.fxml"));
                            this.root = loader.load();
                            HeadmasterController headmasterController = loader.getController();
                            headmasterController.storeCurrentHeadmaster(jdbcUpdatePassword.resultSet.getString("firstName"), jdbcUpdatePassword.resultSet.getString("lastName"), jdbcUpdatePassword.resultSet.getString("email"), jdbcUpdatePassword.resultSet.getString("password"), jdbcUpdatePassword.resultSet.getString("egn"), jdbcUpdatePassword.resultSet.getString("position"));
                            this.stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
                            this.scene = new Scene(root, 1280, 720);
                            this.stage.setScene(scene);
                            this.stage.centerOnScreen();
                            scene.getRoot().requestFocus();
                            stage.show();
                            successfulSignIn = true;
                        }
                        //If position equals 'Headmaster'

                        //If position equals 'Teacher'
                        case "Teacher", "Class Teacher" -> {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/teacher.fxml"));
                            this.root = loader.load();
//                            TeacherController teacherController = loader.getController();
//                            teacherController.getInformation(jdbcUpdatePassword.resultSet.getString("firstName"), jdbcUpdatePassword.resultSet.getString("lastName"), jdbcUpdatePassword.resultSet.getString("email"), jdbcUpdatePassword.resultSet.getString("password"), jdbcUpdatePassword.resultSet.getString("egn"), jdbcUpdatePassword.resultSet.getString("position"));
                            this.stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
                            this.scene = new Scene(root, 1280, 720);
                            this.stage.setScene(scene);
                            this.stage.centerOnScreen();
                            scene.getRoot().requestFocus();
                            stage.show();
                            successfulSignIn = true;
                        }
                        //If position equals 'Teacher'

                        //If position equals 'Student'
                        case "Student" -> {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/student.fxml"));
                            this.root = loader.load();
                            StudentController studentController = loader.getController();
                            studentController.getInformation(jdbcUpdatePassword.resultSet.getString("firstName"), jdbcUpdatePassword.resultSet.getString("lastName"), jdbcUpdatePassword.resultSet.getString("email"), jdbcUpdatePassword.resultSet.getString("password"), jdbcUpdatePassword.resultSet.getString("egn"), jdbcUpdatePassword.resultSet.getString("position"));
                            this.stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
                            this.scene = new Scene(root, 1280, 720);
                            this.stage.setScene(scene);
                            this.stage.centerOnScreen();
                            scene.getRoot().requestFocus();
                            stage.show();
                            successfulSignIn = true;
                        }
                        //If position equals 'Student'

                    }
                    //Switching by Signed In Person's position

                }
                //If the Person already has a registration, check its position then redirect to its position's Stage

            }
            //Cycling through every object from SQL table 'registrations'
        } catch (SQLException throwable) {
            throwable.printStackTrace(); //TODO: Create custom Exceptions
        }


        jdbcUpdatePassword.resultSet.beforeFirst(); //TODO: test if can be removed

        //If new Person entered only Email
        if (signedInForTheFirstTimeOnlyEmail) {
            this.errorLabel.setText("Welcome, please insert password and remember it!");
        }
        //If new Person entered only Email

        //If new Person entered both Email and Password
        if (signedInForTheFirstTime) {
            this.errorLabel.setText("You just set your password. Remember it!");

            //Salt Hashing its password
            String hashedPassword = bCryptPasswordHashing.hashPassword(passwordField.getText().toCharArray());
            //Salt Hashing its password

            //Updating the null password with the Salt Hashed one in SQL
            try {
                jdbcUpdatePassword.writeData.setString(1, hashedPassword);
                jdbcUpdatePassword.writeData.setString(2, emailField.getText());
                jdbcUpdatePassword.writeData.executeUpdate();
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
            //Updating the null password with the Salt Hashed one in SQL

        }
        //If new Person entered both Email and Password

        //If entries are not valid
        if (!successfulSignIn) {
            jdbcUpdatePassword.resultSet.beforeFirst();
            this.errorLabel.setText("Please check your entries and try again.");
        }
        //If entries are not valid

    }
    //If button 'Sign In' is clicked

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
