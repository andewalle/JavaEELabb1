package se.alten.schoolproject.entity;

import lombok.*;
import org.apache.commons.cli.MissingArgumentException;
import se.alten.schoolproject.model.StudentModel;

import javax.json.*;
import javax.persistence.*;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Entity
@Table(name="student")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name = "forename")
    private String forename;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "email", unique = true)
    private String email;

    @ManyToMany(mappedBy = "students", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)

    private Set<Subject> subject = new HashSet<>();

    @Transient
    private List<String> subjects = new ArrayList<>();

    public Student toEntity(String studentModel) throws MissingArgumentException {

        List<String> temp = new ArrayList<>();

        JsonReader reader = Json.createReader(new StringReader(studentModel));

        JsonObject jsonObject = reader.readObject();

        Student student = new Student();

        if ( jsonObject.containsKey("forename") && jsonObject.containsKey("lastname") && jsonObject.containsKey("email")) {
            Boolean checkForEmptyVariables = Stream.of(jsonObject.getString("forename"), jsonObject.getString("lastname"),
                    jsonObject.getString("email")).anyMatch(String::isBlank);
            if ( checkForEmptyVariables) {
                System.out.println("Missing body");
               throw new MissingArgumentException("Forename is blank");

            } else {
                student.setForename(jsonObject.getString("forename"));
                student.setLastname(jsonObject.getString("lastname"));
                student.setEmail(jsonObject.getString("email"));
                System.out.println("NOT BLANK");
            }
        } else {
            throw new MissingArgumentException("Missing data in body!");
        }

        if (jsonObject.containsKey("subject")) {
            JsonArray jsonArray = jsonObject.getJsonArray("subject");
            for ( int i = 0; i < jsonArray.size(); i++ ){
                temp.add(jsonArray.get(i).toString().replace("\"", ""));
                student.setSubjects(temp);
            }
        } else {
            student.setSubjects(null);
        }

        return student;
    }
}
