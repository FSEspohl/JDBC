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
        if(firstname != null && firstname.length() > 1){
            this.firstname = firstname;
        } else {
            throw new InvalidValueException("Vorname muss mindestens 2 Zeichen lang sein!");
        }
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        if(lastname != null && lastname.length() > 1){
            this.lastname = lastname;
        } else {
            throw new InvalidValueException("Nachname muss mindestens 2 Zeichen lang sein!");
        }
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        if(birthdate != null){
            this.birthdate = birthdate;
        } else {
            throw new InvalidValueException("Das Geburtsdatum darf nicht null/leer sein!");
        }
    }

    @Override
    public String toString() {
        return "Student{" + "ID=" + ID +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", birthdate=" + birthdate +
                '}';
    }
}
