package ui;

import dataaccess.DatabaseException;
import dataaccess.MyCourseRepository;
import dataaccess.MyStudentRepository;
import domain.Course;
import domain.CourseType;
import domain.InvalidValueException;
import domain.Student;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.sql.Date;

public class CLI {

    Scanner scan;

    // Data Access Object - DAO
    MyCourseRepository courseRepo;
    MyStudentRepository studentRepo;

    public CLI(MyCourseRepository courseRepo, MyStudentRepository studentRepo){
        this.scan = new Scanner(System.in);
        this.courseRepo = courseRepo;
        this.studentRepo = studentRepo;
    }

    public void start(){
        String input = "-";
        Boolean done = false;

        while(!input.equals("x")){
            showStartMenu();
            input = scan.nextLine();
            if(input.equals("1")){
                while(!input.equals("z")){
                    showCourseMenu();
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
                        case "5" :
                            deleteCourse();
                            break;
                        case "6" :
                            courseSearch();
                            break;
                        case "7" :
                            runningCourses();
                            break;
                        case "z" :
                            break;
                        default:
                            inputError();
                            break;
                    }
                }
                done = true;
            } else if (input.equals("2")) {
                while (!input.equals("z")){
                    showStudentMenu();
                    input = scan.nextLine();
                    switch (input) {
                        case "1" :
                            addStudent();
                            break;
                        case "2" :
                            deleteStudent();
                            break;
                        case "3" :
                            showAllStudents();
                            break;
                        case "4" :
                            showStudentDetails();
                            break;
                        case "5" :
                            showAllStudentsByYear();
                            break;
                        case "z" :
                            break;
                        default:
                          inputError();
                          break;
                    }
                }
                done = true;
            } else if (input.equals("x")){
                System.out.println("Auf Wiedersehen!");
            } else {
                System.out.println("Eingabe konnte nicht bearbeitet werden, versuchen Sie es noch einmal!");
            }
        }
        scan.close();
    }


    // COURSE-FUNCTIONS
    private void runningCourses() {
        System.out.println("Aktuell laufende Kurse:");
        List<Course> list;
        try {
            list = courseRepo.findAllRunningCourses();
            for (Course course : list){
                System.out.println(course);
            }
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler: " + databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler bei Kurs-Anzeige für laufende Kurse: " + exception.getMessage());
        }
    }

    private void courseSearch() {
        System.out.println("Geben Sie einen Suchbegriff an!");
        String searchString = scan.nextLine();
        List<Course> courseList;
        try {
            courseList = courseRepo.findAllCoursesByNameOrDescription(searchString);
            for(Course course : courseList){
                System.out.println(course);
            }
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler bei der Kurssuche: " + databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler bei der Kurssuche: " + exception.getMessage());
        }
    }

    private void deleteCourse() {
        System.out.println("Welchen Kurs möchten Sie löschen? Bitte ID eingeben:");
        Long courseIdToDelete = Long.parseLong(scan.nextLine());

        try {
            courseRepo.deleteByID(courseIdToDelete);
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim Löschen: " + databaseException.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler beim Löschen: " + e.getMessage());
        }
    }

    private void updateCourseDetails() {

        System.out.println("Für welche Kurs-ID möchten Sie die Kursdetails ändern?");
        Long courseId = Long.parseLong(scan.nextLine());
        try {
            Optional<Course> optionalCourse = courseRepo.getByID(courseId);
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

                Optional<Course> optionalCourseUpdated = courseRepo.update(
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
        } catch (IllegalArgumentException illegalArgumentException) {
            System.out.println("Eingabefehler: " + illegalArgumentException.getMessage());
        } catch (InvalidValueException invalidValueException) {
            System.out.println("Kursdaten nicht korrekt angegeben: " + invalidValueException.getMessage());
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim Update: " + databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler beim Update: " + exception.getMessage());
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
            System.out.println("Kurstyp (ZA/BF/FF/OE): ");
            courseType = CourseType.valueOf(scan.nextLine());

            Optional<Course> optionalCourse = courseRepo.insert(
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
            Optional<Course> courseOptional = courseRepo.getByID(courseId);
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
            list = courseRepo.getAll();
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
            System.out.println("Unbekannter Fehler bei Anzeige aller Kurse: " + e.getMessage());
        }
    }


    //STUDENT-FUNCTIONS

    private void addStudent() {

        String firstname, lastname;
        Date birthdate;

        try {
            System.out.println("Bitte alle Daten der Person angeben:");
            System.out.println("Vorname: ");
            firstname = scan.nextLine();
            if(firstname.equals("")) throw new IllegalArgumentException(("Eingabe darf nicht leer sein!"));
            System.out.println("Nachname: ");
            lastname = scan.nextLine();
            if(lastname.equals("")) throw new IllegalArgumentException("Eingabe darf nicht leer sein!");
            System.out.println("Geburtsdatum der Person (YYYY-MM-DD): ");
            birthdate = Date.valueOf(scan.nextLine());



            Optional<Student> optionalStudent = studentRepo.insert(
                    new Student(firstname, lastname, birthdate)
            );

            if(optionalStudent.isPresent()){
                System.out.println("Student:In angelegt: " + optionalStudent.get());

            } else {
                System.out.println("Student:In konnte nicht angelegt werden!");
            }

        } catch (IllegalArgumentException illegalArgumentException) {
            System.out.println("Eingabefehler: " + illegalArgumentException.getMessage());
        } catch (InvalidValueException invalidValueException) {
            System.out.println("Daten der Person nicht korrekt angegeben: " + invalidValueException.getMessage());
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim Einfügen: " + databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler beim Einfügen: " + exception.getMessage());
        }
    }

    private void deleteStudent() {
        System.out.println("Welche:n Student:In möchten Sie austragen? Bitte ID eingeben:");
        Long studentIdToDelete = Long.parseLong(scan.nextLine());

        try {
            studentRepo.deleteByID(studentIdToDelete);
        } catch (DatabaseException databaseException) {
            System.out.println("Datenbankfehler beim Austragen: " + databaseException.getMessage());
        } catch (Exception e) {
            System.out.println("Unbekannter Fehler beim Austragen: " + e.getMessage());
        }
    }

    private void showAllStudents() {
        List<Student> list = null;

        try {
            list = studentRepo.getAll();
            if (list.size() > 0) {
                for (Student student : list) {
                    System.out.println(student);
                }
            } else {
                System.out.println("Studierendenliste leer!");
            }
        } catch (DatabaseException databaseException){
            System.out.println("Datenbankfehler bei Anzeige aller Studierenden: " + databaseException.getMessage());
        } catch (Exception e){
            System.out.println("Unbekannter Fehler bei Anzeige aller Studierenden: " + e.getMessage());
        }
    }

    private void showStudentDetails() {
        System.out.println("Für welchen Studierenden möchten Sie die Details anzeigen?");
        Long studentId = Long.parseLong((scan.nextLine()));
        try {
            Optional<Student> studentOptional = studentRepo.getByID(studentId);
            if(studentOptional.isPresent()){
                System.out.println(studentOptional.get());
            } else {
                System.out.println("Student:In mit der ID " + studentId + " nicht gefunden!");
            }
        } catch (DatabaseException databaseException){
            System.out.println("Datenbankfehler bei Studierenden-Detailanzeige: " + databaseException.getMessage());
        } catch (Exception exception) {
            System.out.println("Unbekannter Fehler bei Studierenden-Detailanzeige: " + exception.getMessage());
        }
    }

    private void showAllStudentsByYear(){
        List<Student> list = null;
        System.out.println("Geben Sie das Geburtsjahr ein: ");
        int birthdateForSearch = Integer.parseInt(scan.nextLine());
        try {
            list = studentRepo.findAllStudentsByBirthyear(birthdateForSearch);
            if (!list.isEmpty()) {
                for (Student student : list) {
                    System.out.println(student);
                }
            } else {
                System.out.println("Keine Studierenden mit diesem Geburtsjahr gefunden!");
            }
        } catch (DatabaseException databaseException){
            System.out.println("Datenbankfehler bei Anzeige der Studierenden: " + databaseException.getMessage());
        } catch (Exception e){
            System.out.println("Unbekannter Fehler bei Anzeige der Studierenden: " + e.getMessage());
        }


    }



    // CLI STRINGS

    private void showCourseMenu(){
        System.out.println("\n______________________ KURSMANAGEMENT ______________________");
        System.out.println("(1) Kurs eingeben \t (2) Alle Kurse anzeigen \t (3) Kursdetails anzeigen \n(4) Kursdaten bearbeiten \t (5) Kurs löschen \t (6) Kurssuche per Wortsuche \n(7) Laufende Kurse anzeigen \t (8) --- \t (9) --- \n(z) ZURÜCK");
    }

    private void showStudentMenu(){
        System.out.println("\n______________________ STUDIERENDEN-MANAGEMENT ______________________");
        System.out.println("(1) Student:In eintragen \t (2) Student:In austragen \t (3) Alle Studierenden anzeigen \n(4) Studierendendetails aufrufen \t (5) Studierende nach Geburtsjahr anzeigen\n(z) ZURÜCK");
    }

    private void showStartMenu(){
        System.out.println("______________________ MENÜAUSWAHL ______________________");
        System.out.println("(1) Kursmanagement \t (2) Studierenden-Management\n(x) BEENDEN");
    }



    private void inputError(){
        System.out.println("Bitte nur die Zahlen der Menüauswahl eingeben!");
    }
}
