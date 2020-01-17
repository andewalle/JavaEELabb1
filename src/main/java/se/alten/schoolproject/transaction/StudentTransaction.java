package se.alten.schoolproject.transaction;

import org.opensaml.xmlsec.signature.Q;
import se.alten.schoolproject.entity.Student;
import se.alten.schoolproject.exceptions.DuplicateEmail;
import se.alten.schoolproject.exceptions.GeneralException;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.List;

@Stateless
@Default
public class StudentTransaction implements StudentTransactionAccess{

    @PersistenceContext(unitName="school")
    private EntityManager entityManager;

    @Override
    public List listAllStudents() throws GeneralException {
        try{
        Query query = entityManager.createQuery("SELECT s from Student s");
        return query.getResultList();}
        catch (PersistenceException pe){
            throw new GeneralException("Something went wrong", pe);
        }
    }

    @Override
    public List listSpecificStudent(String student) throws GeneralException {

        try {
            Query query = entityManager.createQuery("SELECT s from Student s WHERE s.forename = :forename")
                    .setParameter("forename", student);
            return query.getResultList();
        }
        catch (PersistenceException pe){
            throw new GeneralException("Something went wrong", pe);
        }
    }

    @Override
    public Student addStudent(Student studentToAdd) throws DuplicateEmail {
        try {
            entityManager.persist(studentToAdd);
            entityManager.flush();
            return studentToAdd;
        } catch ( PersistenceException pe ) {
            throw new DuplicateEmail("Email exists", pe);
        }
    }

    @Override
    public void removeStudent(String student) throws GeneralException {

        try {
            //JPQL Query
            Query query = entityManager.createQuery("DELETE FROM Student s WHERE s.email = :email");

            //Native Query
            //Query query = entityManager.createNativeQuery("DELETE FROM student WHERE email = :email", Student.class);

            query.setParameter("email", student)
                    .executeUpdate();
        }
        catch (PersistenceException pe){
            throw new GeneralException("Could not remove student", pe);
        }
    }

    @Override
    public void updateStudent(String forename, String lastname, String email) throws GeneralException {

        try {
            Query updateQuery = entityManager.createNativeQuery("UPDATE student SET forename = :forename, lastname = :lastname WHERE email = :email", Student.class);
            updateQuery.setParameter("forename", forename)
                    .setParameter("lastname", lastname)
                    .setParameter("email", email)
                    .executeUpdate();
        }
        catch (PersistenceException pe){
            throw new GeneralException("Could not update student", pe);
        }
    }

    @Override
    public void updateStudentPartial(Student student) throws GeneralException {

        try {
            Student studentFound = (Student) entityManager.createQuery("SELECT s FROM Student s WHERE s.email = :email")
                    .setParameter("email", student.getEmail()).getSingleResult();

            Query query = entityManager.createQuery("UPDATE Student SET forename = :studentForename WHERE email = :email");
            query.setParameter("studentForename", student.getForename())
                    .setParameter("email", studentFound.getEmail())
                    .executeUpdate();
        }catch (PersistenceException pe){
            throw new GeneralException("Could not update student", pe);
        }
    }
}
