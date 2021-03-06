package se.alten.schoolproject.model;

import lombok.*;
import se.alten.schoolproject.entity.Student;
import se.alten.schoolproject.entity.Teacher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class TeacherModel {

    private String forename;
    private String lastname;
    private String email;
    private Set<String> subjects = new HashSet<>();
    private Set<String> students = new HashSet<>();


    public TeacherModel toModel(Teacher teacher) {
        TeacherModel teacherModel = new TeacherModel();

        if (teacher.getForename() == "duplicate"){
            teacherModel.setForename("empty");
            return teacherModel;
        }
        else{
            teacherModel.setForename(teacher.getForename());
            teacherModel.setLastname(teacher.getLastname());
            teacherModel.setEmail(teacher.getEmail());
            teacher.getSubject().forEach(subject -> {

                teacherModel.subjects.add(subject.getTitle());
            });
            return teacherModel;
        }
    }

    public List<TeacherModel> toModelList(List<Teacher> teachers) {

        List<TeacherModel> teacherModels = new ArrayList<>();

        teachers.forEach(teacher -> {
            TeacherModel tm = new TeacherModel();
            tm.forename = teacher.getForename();
            tm.lastname = teacher.getLastname();
            tm.email = teacher.getEmail();
            teacher.getSubject().forEach(subject -> {
                tm.subjects.add(subject.getTitle());
            });
            teacher.getSubject().forEach(s ->{
                s.getStudents().forEach(t ->{

                    tm.students.add(t.getForename());

                });
            });
            teacherModels.add(tm);
            System.out.println(tm.toString());
        });
        return teacherModels;
    }


}
