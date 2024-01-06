package domain;

import java.sql.Date;

public class Student extends BaseEntity{

    // DATAFIELDS
    private String firstname;
    private String lastname;
    private Date birthdate;

    // CONSTRUCTORS
    public Student(Long ID, String firstname, String lastname, Date birthdate) {
        super(ID);
        this.setFirstname(firstname);
        this.setLastname(lastname);
        this.setBirthdate(birthdate);
    }

    public Student( String firstname, String lastname, Date birthdate) {
        super(null);
        this.setFirstname(firstname);
        this.setLastname(lastname);
        this.setBirthdate(birthdate);
    }

    //GETTER & SETTER
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }
}
