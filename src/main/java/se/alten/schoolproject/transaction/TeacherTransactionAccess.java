package se.alten.schoolproject.transaction;


import se.alten.schoolproject.entity.Student;
import se.alten.schoolproject.entity.Teacher;
import se.alten.schoolproject.exceptions.DuplicateEmail;
import se.alten.schoolproject.exceptions.GeneralException;

import javax.ejb.Local;
import java.util.List;

@Local

public interface TeacherTransactionAccess {

    List listAllTeachers() throws GeneralException;
    List listSpecificTeacher(String teacher) throws GeneralException;
    Teacher addTeacher(Teacher teacherToAdd) throws DuplicateEmail;
    void removeTeacher(String teacher) throws GeneralException;
    void updateTeacher(String forename, String lastname, String email) throws GeneralException;
    void updateTeacherPartial(Teacher teacherToUpdate) throws GeneralException;
}
