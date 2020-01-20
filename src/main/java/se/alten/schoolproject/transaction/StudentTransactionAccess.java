package se.alten.schoolproject.transaction;

import se.alten.schoolproject.entity.Student;
import se.alten.schoolproject.exceptions.DuplicateEmail;
import se.alten.schoolproject.exceptions.GeneralException;

import javax.ejb.Local;
import java.util.List;

@Local
public interface StudentTransactionAccess {
    List listAllStudents() throws GeneralException;
    List listSpecificStudent(String student) throws GeneralException;
    Student addStudent(Student studentToAdd) throws DuplicateEmail;
    void removeStudent(String student) throws GeneralException;
    void updateStudent(String forename, String lastname, String email) throws GeneralException;
    void updateStudentPartial(Student studentToUpdate) throws GeneralException;
}