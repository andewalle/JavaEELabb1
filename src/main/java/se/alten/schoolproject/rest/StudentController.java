package se.alten.schoolproject.rest;

import lombok.NoArgsConstructor;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.log4j.Logger;
import se.alten.schoolproject.dao.SchoolAccessLocal;

import se.alten.schoolproject.exceptions.GeneralException;
import se.alten.schoolproject.model.StudentModel;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Stateless
@NoArgsConstructor
@Path("/student")
public class StudentController {

    private static Logger logger = Logger.getLogger(StudentController.class);


    @Inject
    private SchoolAccessLocal sal;

    @GET
    @Produces({"application/JSON"})
    public Response showStudents() {
//        logger.info("In show students");
//        logger.error("Error message");
        try {
            List students = sal.listAllStudents();
            System.out.println(students.toString());
            return Response.ok(students).build();
        } catch ( Exception e ) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("{forename}")
    @Produces({"application/JSON"})
    public Response searchStudent(@PathParam ("forename") String student){
        try {
            List students = sal.listSpecificStudent(student);
            return Response.ok(students).build();
        }
        catch (Exception e){
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({"application/JSON"})
    /**
     * JavaDoc
     */
    public Response addStudent(String studentModel) {
        StudentModel answer = new StudentModel();
        try {

            answer = sal.addStudent(studentModel);

            return Response.ok(answer).build();

        } catch ( Exception e ) {
            System.out.println(e.getMessage() );

            return Response.status(Response.Status.EXPECTATION_FAILED).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("{email}")
    public Response deleteUser( @PathParam("email") String email) {
        try {
            sal.removeStudent(email);
            return Response.ok().build();
        } catch ( Exception e ) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @PUT
    public Response updateStudent( @QueryParam("forename") String forename, @QueryParam("lastname") String lastname, @QueryParam("email") String email) throws GeneralException {

        try {
            sal.updateStudent(forename, lastname, email);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_GATEWAY).entity(e.getMessage()).build();
        }
        //fixa status codes och try catch
    }

    @PATCH
    public Response updatePartialAStudent(String studentModel) throws MissingArgumentException, GeneralException {
        try{
        sal.updateStudentPartial(studentModel);
        return Response.ok().build();
        }
        catch (Exception e){
            return Response.status(Response.Status.BAD_GATEWAY).entity(e.getMessage()).build();
        }
    }
}
