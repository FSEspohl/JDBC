package dataaccess;

import domain.Student;

import java.util.List;
import java.util.Optional;

public class MysqlStudentRepository implements MyStudentRepository{



    @Override
    public Optional insert(Object entity) {
        return Optional.empty();
    }

    @Override
    public Optional getByID(Object ID) {
        return Optional.empty();
    }

    @Override
    public List getAll() {
        return null;
    }

    @Override
    public Optional update(Object entity) {
        return Optional.empty();
    }

    @Override
    public void deleteByID(Object id) {

    }
}
