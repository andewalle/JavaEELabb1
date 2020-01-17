package se.alten.schoolproject.dao;

import org.apache.commons.cli.MissingArgumentException;
import se.alten.schoolproject.entity.Student;
import se.alten.schoolproject.exceptions.DuplicateEmail;
import se.alten.schoolproject.model.StudentModel;
import se.alten.schoolproject.transaction.StudentTransactionAccess;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Stream;

@Stateless
public class SchoolDataAccess implements SchoolAccessLocal, SchoolAccessRemote {

    private Student student = new Student();
    private StudentModel studentModel = new StudentModel();

    @Inject
    StudentTransactionAccess studentTransactionAccess;

    @Override
    public List listAllStudents(){
        return studentTransactionAccess.listAllStudents();
    }

    @Override
    public List listSpecificStudent(String student){return studentTransactionAccess.listSpecificStudent(student);}

    @Override
    public StudentModel addStudent(String newStudent) throws MissingArgumentException, DuplicateEmail {
        Student studentToAdd = null;

        studentToAdd = student.toEntity(newStudent);

        studentTransactionAccess.addStudent(studentToAdd);
        return studentModel.toModel(studentToAdd);
    }

    @Override
    public void removeStudent(String studentEmail) {
        studentTransactionAccess.removeStudent(studentEmail);
    }

    @Override
    public void updateStudent(String forename, String lastname, String email) {
        studentTransactionAccess.updateStudent(forename, lastname, email);
    }

    @Override
    public void updateStudentPartial(String studentModel) throws MissingArgumentException {
        Student studentToUpdate = student.toEntity(studentModel);
        studentTransactionAccess.updateStudentPartial(studentToUpdate);
    }
}
