package co.edu.uniquindio.serviautosbackend.domain.models;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    private String id;
    private String name;
    private String lastName;
    private String phoneNumber;


}
