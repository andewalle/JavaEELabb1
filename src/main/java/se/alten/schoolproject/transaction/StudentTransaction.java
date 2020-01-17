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
        catch (Exception e){
            throw new GeneralException("Something went wrong", e);
        }
    }

    @Override
    public List listSpecificStudent(String student) throws GeneralException {

        try {
            Query query = entityManager.createQuery("SELECT s from Student s WHERE s.forename = :forename")
                    .setParameter("forename", student);
            return query.getResultList();
        }
        catch (Exception e){
            throw new GeneralException("Something went wrong", e);
        }
    }

    @Override
    public Student addStudent(Student studentToAdd) throws DuplicateEmail {
        try {
            entityManager.persist(studentToAdd);
            entityManager.flush();
            return studentToAdd;
        } catch ( PersistenceException pe ) {
            throw new DuplicateEmail("Email alredy exists", pe);
        }
    }

    @Override
    public void removeStudent(String student) throws GeneralException {

            //JPQL Query
            Query query = entityManager.createQuery("DELETE FROM Student s WHERE s.email = :email");

            //Native Query
            //Query query = entityManager.createNativeQuery("DELETE FROM student WHERE email = :email", Student.class);

            int i = query.setParameter("email", student)
                    .executeUpdate();
            //404

            System.out.println(i);
            if (i == 0){
                throw new GeneralException("Could not remove student");
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
        catch (Exception e){
            throw new GeneralException("Could not update student");
        }
    }

    @Override
    public void updateStudentPartial(Student student) throws GeneralException {
        try{
            Student studentFound = (Student) entityManager.createQuery("SELECT s FROM Student s WHERE s.email = :email")
                    .setParameter("email", student.getEmail()).getSingleResult();
            System.out.println(student.toString());
            Query query = entityManager.createQuery("UPDATE Student SET forename = :studentForename WHERE email = :email");
            query.setParameter("studentForename", student.getForename())
                    .setParameter("email", studentFound.getEmail())
                    .executeUpdate();}
        catch (Exception e){

            throw new GeneralException("Could not update student");
        }
    }
}
