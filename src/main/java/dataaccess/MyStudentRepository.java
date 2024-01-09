package dataaccess;

import domain.Student;

import java.util.List;

public interface MyStudentRepository extends BaseRepository<Student, Long> {

    List<Student> findAllStudentsByName();
    List<Student> findAllStudentsByBirthyear(int birthdateForSearch);
    List<Student> findAllStudentsByBirthdatePeriod();


}
