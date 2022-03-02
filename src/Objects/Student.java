package Objects;

import java.util.ArrayList;

public class Student extends Person{
    private String classNumber;
    ArrayList<StudentGrade> grades;

    public Student(String firstName, String lastName, String email, String classNumber, String egn, String telephoneNumber, String position) {
        super(firstName, lastName, email, egn, telephoneNumber, position);
        this.classNumber = classNumber;
        this.grades = new ArrayList<StudentGrade>();
    }

    public String getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(String classNumber) {
        this.classNumber = classNumber;
    }

    public ArrayList<StudentGrade> getGrades() {
        return grades;
    }

    public void setGrades(ArrayList<StudentGrade> grades) {
        this.grades = grades;
    }

}
