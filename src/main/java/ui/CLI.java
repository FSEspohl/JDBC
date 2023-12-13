package ui;

import dataaccess.DatabaseException;
import dataaccess.MyCourseRepository;
import domain.Course;

import java.util.List;
import java.util.Scanner;

public class CLI {

    Scanner scan;
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
                    System.out.println("Kurseingabe");
                    break;
                case "2" :
                    showAllCourses();
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
        System.out.println("(1) Kurs eingeben \t (2) Alle Kurse anzeigen \t (x) ENDE");
    }

    private void inputError(){
        System.out.println("Bitte nur die Zahlen der Menüauswahl eingeben!");
    }
}
