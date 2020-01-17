package se.alten.schoolproject.dao;

import org.apache.commons.cli.MissingArgumentException;
import se.alten.schoolproject.entity.Student;
import se.alten.schoolproject.exceptions.DuplicateEmail;
import se.alten.schoolproject.exceptions.GeneralException;
import se.alten.schoolproject.entity.Subject;
import se.alten.schoolproject.model.StudentModel;
import se.alten.schoolproject.model.SubjectModel;
import se.alten.schoolproject.transaction.StudentTransactionAccess;
import se.alten.schoolproject.transaction.SubjectTransactionAccess;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

@Stateless
public class SchoolDataAccess implements SchoolAccessLocal, SchoolAccessRemote {

    private Student student = new Student();
    private StudentModel studentModel = new StudentModel();
    private Subject subject = new Subject();
    private SubjectModel subjectModel = new SubjectModel();

    @Inject
    StudentTransactionAccess studentTransactionAccess;

    @Inject
    SubjectTransactionAccess subjectTransactionAccess;

    @Override
    public List listAllStudents() throws GeneralException {
        return studentTransactionAccess.listAllStudents();
    }

    @Override
    public List listSpecificStudent(String student) throws GeneralException {return studentTransactionAccess.listSpecificStudent(student);}

    @Override
    public StudentModel addStudent(String newStudent) throws MissingArgumentException, DuplicateEmail {
        Student studentToAdd = null;

        studentToAdd = student.toEntity(newStudent);

        studentTransactionAccess.addStudent(studentToAdd);
        return studentModel.toModel(studentToAdd);
    }

    @Override
    public void removeStudent(String studentEmail) throws GeneralException {
        studentTransactionAccess.removeStudent(studentEmail);
    }

    @Override
    public void updateStudent(String forename, String lastname, String email) throws GeneralException {
        studentTransactionAccess.updateStudent(forename, lastname, email);
    }

    @Override
    public void updateStudentPartial(String studentModel) throws MissingArgumentException, GeneralException {
        Student studentToUpdate = student.toEntity(studentModel);
        studentTransactionAccess.updateStudentPartial(studentToUpdate);
    }

    @Override
    public List listAllSubjects() {
        return subjectTransactionAccess.listAllSubjects();
    }

    @Override
    public SubjectModel addSubject(String newSubject) {
        Subject subjectToAdd = subject.toEntity(newSubject);
        subjectTransactionAccess.addSubject(subjectToAdd);
        return subjectModel.toModel(subjectToAdd);
    }
}
