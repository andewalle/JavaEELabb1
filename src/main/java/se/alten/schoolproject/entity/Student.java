package se.alten.schoolproject.entity;

import lombok.*;
import org.apache.commons.cli.MissingArgumentException;
import se.alten.schoolproject.model.StudentModel;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.persistence.*;
import java.io.Serializable;
import java.io.StringReader;
import java.util.stream.Stream;

@Entity
@Table(name="student")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Student implements Serializable {

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "forename")
    private String forename;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "email", unique = true)
    private String email;

    public Student toEntity(String studentModel) throws MissingArgumentException {
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
        return student;
    }
}
