package Objects;

public class Teacher extends Person {
    private String classTeacherOfASection;

    public Teacher(String firstName, String lastName, String email, String egn, String telephoneNumber, String position, String classTeacherOfASection) {
        super(firstName, lastName, email, egn, telephoneNumber, position);
        this.classTeacherOfASection = classTeacherOfASection;
    }

    public String getClassTeacherOfASection() {
        return classTeacherOfASection;
    }

    public void setClassTeacherOfASection(String classTeacherOfASection) {
        this.classTeacherOfASection = classTeacherOfASection;
    }
}
