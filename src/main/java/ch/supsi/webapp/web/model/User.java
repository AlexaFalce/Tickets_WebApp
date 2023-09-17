package ch.supsi.webapp.web.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data @Entity @NoArgsConstructor @AllArgsConstructor @Setter @Getter
public class User {

    @Id
    private String username;
    private String name;
    private String surname;
    private String password;
    private String role;
    @ManyToMany @JsonIgnore
    private final Set<Ticket> watchedTickets = new HashSet<>();

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", watchedTickets=" + watchedTickets +
                '}';
    }

}
