package se.alten.schoolproject.dao;

import org.apache.commons.cli.MissingArgumentException;
import se.alten.schoolproject.exceptions.DuplicateEmail;
import se.alten.schoolproject.model.StudentModel;

import javax.ejb.Local;
import java.util.List;

@Local
public interface SchoolAccessLocal {

    List listAllStudents() throws Exception;

    List listSpecificStudent(String student) throws Exception;

    StudentModel addStudent(String studentModel) throws MissingArgumentException, DuplicateEmail;

    void removeStudent(String student);

    void updateStudent(String forename, String lastname, String email);

    void updateStudentPartial(String studentModel) throws MissingArgumentException;
}
