package ui;

import java.util.Scanner;

public class CLI {

    Scanner scan;

    public CLI(){
        this.scan = new Scanner(System.in);
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
                    System.out.println("Alle Kurse anzeigen");
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

    private void showMenu(){
        System.out.println("\n______________________ KURSMANAGEMENT ______________________");
        System.out.println("(1) Kurs eingeben \t (2) Alle Kurse anzeigen \t (x) ENDE");
    }

    private void inputError(){
        System.out.println("Bitte nur die Zahlen der Menüauswahl eingeben!");
    }
}
