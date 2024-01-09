package at.htlimst;

import dataaccess.MysqlCourseRepository;
import dataaccess.MysqlStudentRepository;
import ui.CLI;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try{
            CLI myCLI = new CLI(new MysqlCourseRepository(), new MysqlStudentRepository());
            myCLI.start();
        } catch (SQLException e) {
            System.out.println("Datenbankfehler: " + e.getMessage() + "\nSQL State: " + e.getSQLState());
        } catch (ClassNotFoundException e) {
            System.out.println("Datenbankfehler: " + e.getMessage());
        }
    }
}
