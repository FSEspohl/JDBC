package dataaccess;


import domain.Student;
import util.Assert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MysqlStudentRepository implements MyStudentRepository{

    private Connection con;

    public MysqlStudentRepository() throws SQLException, ClassNotFoundException{
        this.con = MysqlDatabaseConnection.getConnection("jdbc:mysql://localhost:3306/kurssystem", "root", "123");
    }


    @Override
    public Optional insert(Student entity) {
        Assert.notNull(entity);

        try {
            String sql = "INSERT INTO `students` (`firstname`, `lastname`, `birthdate`) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entity.getFirstname());
            preparedStatement.setString(2, entity.getLastname());
            preparedStatement.setDate(3, entity.getBirthdate());

            int affectedRows = preparedStatement.executeUpdate();

            if(affectedRows == 0){
                return Optional.empty();
            }

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if(generatedKeys.next()){
                return this.getByID(generatedKeys.getLong(1));
            } else {
                return Optional.empty();
            }

        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public Optional<Student> getByID(Long ID) {
        Assert.notNull(ID);
        if(countStudentsInDbWithId(ID) == 0){
            return Optional.empty();
        } else {
            try {
                String sql = "SELECT * FROM `students` WHERE `id` = ?";
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setLong(1, ID);
                ResultSet resultSet = preparedStatement.executeQuery();

                resultSet.next();
                Student student = new Student(
                        resultSet.getLong("id"),
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        resultSet.getDate("birthdate")
                );
                return Optional.of(student);

            } catch (SQLException sqlException){
                throw new DatabaseException(sqlException.getMessage());
            }
        }
    }

    @Override
    public List<Student> getAll() {
        String sql = "SELECT * FROM `students`";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Student> studentList = new ArrayList<>();
            while (resultSet.next()){
                studentList.add( new Student(
                        resultSet.getLong("id"),
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        resultSet.getDate("birthdate")
                ));
            }
            return studentList;
        } catch (SQLException e) {
            throw new DatabaseException("Database error occured!");
        }
    }

    @Override
    public Optional<Student> update(Student entity) {

        Assert.notNull(entity);

        String sql = "UPDATE `courses` SET `firstname` = ?, `lastname` = ?, `birthdate` = ? WHERE `students`.`id` = ?";

        if(countStudentsInDbWithId(entity.getID()) == 0){
            return Optional.empty();
        } else {
            try {
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, entity.getFirstname());
                preparedStatement.setString(2, entity.getLastname());
                preparedStatement.setDate(3, entity.getBirthdate());
                preparedStatement.setLong(4, entity.getID());

                int affectedRows = preparedStatement.executeUpdate();

                if(affectedRows == 0){
                    return Optional.empty();
                } else {
                    return this.getByID(entity.getID());
                }
            } catch (SQLException sqlException) {
                throw new DatabaseException(sqlException.getMessage());
            }
        }
    }

    @Override
    public void deleteByID(Long id) {
        Assert.notNull(id);
        String sql = "DELETE FROM `students` WHERE `id` = ?";
        try {
            if (countStudentsInDbWithId(id) == 1) {
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setLong(1, id);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    private int countStudentsInDbWithId(Long ID){
        try {
            String countSql = "SELECT COUNT(*) FROM `students` WHERE `id` = ?";
            PreparedStatement preparedStatementCount = con.prepareStatement((countSql));
            preparedStatementCount.setLong(1, ID);
            ResultSet resultSetCount = preparedStatementCount.executeQuery();
            resultSetCount.next();
            int studentCount = resultSetCount.getInt(1);
            return studentCount;
        } catch (SQLException sqlException){
            throw new DatabaseException(sqlException.getMessage());
        }
    }

    @Override
    public List<Student> findAllStudentsByName() {
        return null;
    }

    @Override
    public List<Student> findAllStudentsByBirthyear(int birthdateForSearch) {
        try {
            String sql = "SELECT * FROM `students` WHERE YEAR(`birthdate`) = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, birthdateForSearch);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Student> studentList = new ArrayList<>();
            while (resultSet.next()){
                studentList.add( new Student(
                        resultSet.getLong("id"),
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        resultSet.getDate("birthdate")
                ));
            }
            return studentList;
        } catch (SQLException e) {
            throw new DatabaseException("Database error occured!");
        }
    }

    @Override
    public List<Student> findAllStudentsByBirthdatePeriod() {
        return null;
    }
}
