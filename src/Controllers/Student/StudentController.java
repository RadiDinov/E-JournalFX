package Controllers.Student;

import Controllers.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class StudentController {
    //TODO: Rework StudentController + Add Comments
    MainController currentStudentData = new MainController();

    @FXML
    Label myLabel;


    public void getInformation(String firstName, String lastName, String email, String password, String egn, String position) {
        currentStudentData.setFirstName(firstName);
        currentStudentData.setLastName(lastName);
        currentStudentData.setEmail(email);
        currentStudentData.setPassword(password);
        currentStudentData.setEgn(egn);
        currentStudentData.setPosition(position);
    }

    public void setText() {
        myLabel.setText(currentStudentData.getFirstName() + " " + currentStudentData.getLastName());
    }
}
