package ui;

import dataaccess.DatabaseException;
import dataaccess.MyCourseRepository;
import domain.Course;
import domain.CourseType;
import domain.InvalidValueException;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.sql.Date;

public class CLI {

    Scanner scan;

    // Data Access Object - DAO
    MyCourseRepository repo;

    public CLI(MyCourseRepository repo){
        this.scan = new Scanner(System.in);
        this.repo = repo;
    }

    public void start(){
        String input = "-";
        while(!input.equals("x")){
            showMenu();
            input = scan.nextLine();
            switch(input){
                case "1" :
                    addCourse();
                    break;
                case "2" :
                    showAllCourses();
                    break;
                case "3" :
                    showCourseDetails();
                    break;
                case "4" :
                    updateCourseDetails();
                    break;
                case "x" :
                    System.out.println("Auf Wiedersehen!");
                    break;
                default:
                    inputError();
                    break;
            }
        }
        scan.close();
    }

    private void updateCourseDetails() {

        System.out.println("Für welche Kurs-ID möchten Sie die Kursdetails ändern?");
        Long courseId = Long.parseLong(scan.nextLine());
        try {
            Optional<Course> optionalCourse = repo.getByID(courseId);
            if(optionalCourse.isEmpty()) {
                System.out.println("Kurs mit der angegebenen ID nicht in der Datenbank!");
            } else {
                Course course = optionalCourse.get();

                System.out.println("Änderungen für dne folgenenden Kurs: ");
                System.out.println(course);

                String name, description, hours, dateFrom, dateTo, courseType;

                System.out.println("Bitte neue Kursdaten angegeben (Enter, falls keine Änderung gewünscht ist):");
                System.out.println("Name: ");
                name = scan.nextLine();
                System.out.println("Beschreibung: ");
                description = scan.nextLine();
                System.out.println("Stunden: ");
                hours = scan.nextLine();
                System.out.println("Startdatum (YYYY-MM-DD): ");
                dateFrom = scan.nextLine();
                System.out.println("Enddatum (YYYY-MM-DD): ");
                dateTo = scan.nextLine();
                System.out.println("Kurstyp (ZA/BF/FF/OE): ");
                courseType = scan.nextLine();

                Optional<Course> optionalCourseUpdated = repo.update(
                        new Course(
                                course.getID(),
                                name.equals("") ? course.getName() : name,
                                description.equals("") ? course.getDescription() : description,
                                hours.equals("") ? course.getHours() : Integer.parseInt(hours),
                                dateFrom.equals("") ? course.getBeginDate() : Date.valueOf(dateFrom),
                                dateTo.equals("") ? course.getEndDate() : Date.valueOf(dateTo),
                                courseType.equals("") ? course.getCourseType() : CourseType.valueOf(courseType)
                        )
                );

                optionalCourseUpdated.ifPresentOrElse(
                        (updatedCourse) -> System.out.println("Kurs aktualisiert: " + updatedCourse),
                        () -> System.out.println("Kurs konnte nicht aktualisiert werden!")
                );

            }
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler beim Kursupdate: " + exception.getMessage());
        }

    }

    private void addCourse() {
        
        String name, description;
        int hours;
        Date dateFrom, dateTo;
        CourseType courseType;
        
        try {
            System.out.println("Bitte alle Kursdaten angeben:");
            System.out.println("Name: ");
            name = scan.nextLine();
            if(name.equals("")) throw new IllegalArgumentException(("Eingabe darf nicht leer sein!"));
            System.out.println("Beschreibung: ");
            description = scan.nextLine();
            if(description.equals("")) throw new IllegalArgumentException("Eingabe darf nicht leer sein!");
            System.out.println("Stundenanzahl: ");
            hours = Integer.parseInt(scan.nextLine());
            System.out.println("Startdatum (YYYY-MM-DD): ");
            dateFrom = Date.valueOf(scan.nextLine());
            System.out.println("Enddatum (YYYY-MM-DD): ");
            dateTo = Date.valueOf(scan.nextLine());
            System.out.println("Kurstyp (ZA/BR/FF/OE): ");
            courseType = CourseType.valueOf(scan.nextLine());

            Optional<Course> optionalCourse = repo.insert(
                    new Course(name, description, hours, dateFrom, dateTo, courseType)
            );

            if(optionalCourse.isPresent()){
                System.out.println("Kurs angelegt: " + optionalCourse.get());
            } else {
                System.out.println("Kurs konnte nicht angelegt werden!");
            }
            
        } catch (IllegalArgumentException illegalArgumentException) {
            System.out.println("Eingabefehler: " + illegalArgumentException.getMessage());
        } catch (InvalidValueException invalidValueException) {
            System.out.println("Kursdaten nicht korrekt angegeben: " + invalidValueException.getMessage());
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim Einfügen: " + databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler beim Einfügen: " + exception.getMessage());
        }
        
    }

    private void showCourseDetails() {
        System.out.println("Für welchen Kurs möchten Sie die Kursdetails anzeigen?");
        Long courseId = Long.parseLong((scan.nextLine()));
        try {
            Optional<Course> courseOptional = repo.getByID(courseId);
            if(courseOptional.isPresent()){
                System.out.println(courseOptional.get());
            } else {
                System.out.println("Kurs mit der ID " + courseId + " nicht gefunden!");
            }
        } catch (DatabaseException databaseException){
            System.out.println("Datenbankfehler bei Kurs-Detailanzeige: " + databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler bei Kurs-Detailanzeige: " + exception.getMessage());
        }
    }

    private void showAllCourses() {
        List<Course> list = null;

        try {
            list = repo.getAll();
            if (list.size() > 0) {
                for (Course course : list) {
                    System.out.println(course);
                }
            } else {
                System.out.println("Kursliste leer!");
            }
        } catch (DatabaseException databaseException){
            System.out.println("Datenbankfehler bei Anzeige aller Kurse: " + databaseException.getMessage());
        } catch (Exception e){
            System.out.println("Unbekannter Fheler bei Anzeige aller Kurse: " + e.getMessage());
        }
    }

    private void showMenu(){
        System.out.println("\n______________________ KURSMANAGEMENT ______________________");
        System.out.println("(1) Kurs eingeben \t (2) Alle Kurse anzeigen \t (3) Kursdetails anzeigen \n(4) Kursdaten bearbeiten \t (5) xxx \t (6) xxx \n(x) ENDE");
    }

    private void inputError(){
        System.out.println("Bitte nur die Zahlen der Menüauswahl eingeben!");
    }
}
