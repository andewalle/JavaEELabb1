package se.alten.schoolproject.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.cli.MissingArgumentException;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.persistence.*;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Entity
@Table(name="teacher")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Teacher implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public Teacher toEntity(String teacherModel) throws MissingArgumentException {

        List<String> temp = new ArrayList<>();

        JsonReader reader = Json.createReader(new StringReader(teacherModel));

        JsonObject jsonObject = reader.readObject();

        Teacher teacher = new Teacher();

        if ( jsonObject.containsKey("forename") && jsonObject.containsKey("lastname") && jsonObject.containsKey("email")) {
            Boolean checkForEmptyVariables = Stream.of(jsonObject.getString("forename"), jsonObject.getString("lastname"),
                    jsonObject.getString("email")).anyMatch(String::isBlank);
            if ( checkForEmptyVariables) {
                System.out.println("Missing body");
                throw new MissingArgumentException("Forename is blank");

            } else {
                teacher.setForename(jsonObject.getString("forename"));
                teacher.setLastname(jsonObject.getString("lastname"));
                teacher.setEmail(jsonObject.getString("email"));
            }
        } else {
            throw new MissingArgumentException("Missing data in body!");
        }

        if (jsonObject.containsKey("subject")) {
            JsonArray jsonArray = jsonObject.getJsonArray("subject");
            for ( int i = 0; i < jsonArray.size(); i++ ){
                temp.add(jsonArray.get(i).toString().replace("\"", ""));
                teacher.setSubjects(temp);
            }
        } else {
            teacher.setSubjects(null);
        }

        return teacher;
    }
}
