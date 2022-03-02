package Objects;

public class Person {
    private String firstName;
    private String lastName;
    private String email;
    private String egn;
    private String telephoneNumber;
    private String position;

    public Person(String firstName, String lastName, String email, String egn, String telephoneNumber, String position) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.egn = egn;
        this.telephoneNumber = telephoneNumber;
        this.position = position;
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

    public String getEgn() {
        return egn;
    }

    public void setEgn(String egn) {
        this.egn = egn;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
