package se.alten.schoolproject.dao;

import org.apache.commons.cli.MissingArgumentException;
import se.alten.schoolproject.exceptions.DuplicateEmail;
import se.alten.schoolproject.exceptions.GeneralException;
import se.alten.schoolproject.model.StudentModel;
import se.alten.schoolproject.model.SubjectModel;

import javax.ejb.Local;
import java.util.List;

@Local
public interface SchoolAccessLocal {

    List listAllStudents() throws GeneralException;

    List listSpecificStudent(String student) throws Exception;

    StudentModel addStudent(String studentModel) throws MissingArgumentException, DuplicateEmail;

    void removeStudent(String student) throws GeneralException;

    void updateStudent(String forename, String lastname, String email) throws GeneralException;

    void updateStudentPartial(String studentModel) throws MissingArgumentException, GeneralException;

    List listAllSubjects();

    SubjectModel addSubject(String subjectModel);
}
