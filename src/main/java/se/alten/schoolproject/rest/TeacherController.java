package se.alten.schoolproject.rest;


import lombok.NoArgsConstructor;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.log4j.Logger;
import se.alten.schoolproject.dao.SchoolAccessLocal;
import se.alten.schoolproject.exceptions.GeneralException;
import se.alten.schoolproject.model.StudentModel;
import se.alten.schoolproject.model.TeacherModel;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Stateless
@NoArgsConstructor
@Path("/teacher")

public class TeacherController {

    private static Logger logger = Logger.getLogger(StudentController.class);


    @Inject
    private SchoolAccessLocal sal;

    @GET
    @Produces({"application/JSON"})
    public Response showTeachers() {
//        logger.info("In show teachers");
//        logger.error("Error message");
        try {
            List teachers = sal.listAllTeachers();
            System.out.println(teachers.toString());
            return Response.ok(teachers).build();
        } catch ( Exception e ) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("{forename}")
    @Produces({"application/JSON"})
    public Response searchTeachers(@PathParam("forename") String teacher){
        try {
            List teachers = sal.listSpecificTeacher(teacher);
            return Response.ok(teachers).build();
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
    public Response addTeacher(String teacherModel) {
        TeacherModel answer = new TeacherModel();
        try {

            answer = sal.addTeacher(teacherModel);

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
            sal.removeTeacher(email);
            return Response.ok().build();
        } catch ( Exception e ) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @PUT
    public Response updateTeacher( @QueryParam("forename") String forename, @QueryParam("lastname") String lastname, @QueryParam("email") String email) throws GeneralException {

        try {
            sal.updateTeacher(forename, lastname, email);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_GATEWAY).entity(e.getMessage()).build();
        }
        //fixa status codes och try catch
    }

    @PATCH
    public Response updatePartialATeacher(String teacherModel) throws MissingArgumentException, GeneralException {
        try{
            sal.updateStudentPartial(teacherModel);
            return Response.ok().build();
        }
        catch (Exception e){
            return Response.status(Response.Status.BAD_GATEWAY).entity(e.getMessage()).build();
        }
    }
}
