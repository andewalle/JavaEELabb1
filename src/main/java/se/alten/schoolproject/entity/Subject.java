package se.alten.schoolproject.entity;

import lombok.*;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.persistence.*;
import java.io.Serializable;
import java.io.StringReader;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Subject implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String title;

    public Subject toEntity(String subjectModel) {
        JsonReader reader = Json.createReader(new StringReader(subjectModel));

        JsonObject jsonObject = reader.readObject();

        Subject subject = new Subject();

        if ( jsonObject.containsKey("subject")) {
            subject.setTitle(jsonObject.getString("subject"));
        } else {
            subject.setTitle("");
        }

        return subject;
    }
}
