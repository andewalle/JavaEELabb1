package se.alten.schoolproject.transaction;

import se.alten.schoolproject.entity.Teacher;
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

public class TeacherTransaction implements TeacherTransactionAccess {

    @PersistenceContext(unitName="school")
    private EntityManager entityManager;

    @Override
    public List listAllTeachers() throws GeneralException {
        try{
            Query query = entityManager.createQuery("SELECT t from Teacher t");
            return query.getResultList();}
        catch (Exception e){
            throw new GeneralException("Something went wrong", e);
        }
    }

    @Override
    public List listSpecificTeacher(String teacher) throws GeneralException {
        try {
            Query query = entityManager.createQuery("SELECT t from Teacher t WHERE t.forename = :forename")
                    .setParameter("forename", teacher);
            return query.getResultList();
        }
        catch (Exception e){
            throw new GeneralException("Something went wrong", e);
        }
    }

    @Override
    public Teacher addTeacher(Teacher teacherToAdd) throws DuplicateEmail {
        try {
            entityManager.persist(teacherToAdd);
            entityManager.flush();
            return teacherToAdd;
        } catch ( PersistenceException pe ) {
            throw new DuplicateEmail("Email alredy exists", pe);
        }
    }

    @Override
    public void removeTeacher(String teacher) throws GeneralException {

        //JPQL Query
        Query query = entityManager.createQuery("DELETE FROM Teacher t WHERE t.email = :email");

        //Native Query
        //Query query = entityManager.createNativeQuery("DELETE FROM student WHERE email = :email", Student.class);

        int i = query.setParameter("email", teacher)
                .executeUpdate();
        //404

        System.out.println(i);
        if (i == 0){
            throw new GeneralException("Could not remove teacher");
        }

    }

    @Override
    public void updateTeacher(String forename, String lastname, String email) throws GeneralException {

        try {
            Query updateQuery = entityManager.createNativeQuery("UPDATE teacher SET forename = :forename, lastname = :lastname WHERE email = :email", Teacher.class);
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
    public void updateTeacherPartial(Teacher teacher) throws GeneralException {

        try{
            Teacher teacherFound = (Teacher) entityManager.createQuery("SELECT t FROM Teacher t WHERE t.email = :email")
                    .setParameter("email", teacher.getEmail()).getSingleResult();
            System.out.println(teacher.toString());
            Query query = entityManager.createQuery("UPDATE Teacher SET forename = :studentForename WHERE email = :email");
            query.setParameter("studentForename", teacher.getForename())
                    .setParameter("email", teacherFound.getEmail())
                    .executeUpdate();}
        catch (Exception e){

            throw new GeneralException("Could not update student");
        }

    }
}
