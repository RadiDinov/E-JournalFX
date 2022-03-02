package Objects;

import java.util.ArrayList;

public class StudentGrade {
    private String subject;
    private ArrayList<Integer> grade;

    public StudentGrade(String subject, ArrayList<Integer> grade) {
        this.subject = subject;
        this.grade = grade;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public ArrayList<Integer> getGrade() {
        return grade;
    }

    public void setGrade(ArrayList<Integer> grade) {
        this.grade = grade;
    }

}