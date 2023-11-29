package at.htlimst;

import dataaccess.MysqlDatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;
import ui.CLI;

public class Main {
    public static void main(String[] args) {

        CLI myCLI = new CLI();
        myCLI.start();

        try {
            Connection myConnection = MysqlDatabaseConnection.getConnection("jdbc:mysql://localhost:3306/kurssystem", "root", "123");
            System.out.println("Verbindung aufgebaut");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}