package se.alten.schoolproject.model;

import lombok.*;
import se.alten.schoolproject.entity.Student;

import javax.ws.rs.core.Response;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StudentModel {

    private Long id;
    private String forename;
    private String lastname;
    private String email;

    public StudentModel toModel(Student student) {
        StudentModel studentModel = new StudentModel();

        if (student.getForename() == "duplicate"){
            studentModel.setForename("empty");
            return studentModel;
        }
        else{
            studentModel.setForename(student.getForename());
            studentModel.setLastname(student.getLastname());
            studentModel.setEmail(student.getEmail());
            return studentModel;
        }
    }
}
