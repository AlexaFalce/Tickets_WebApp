package ch.supsi.webapp.web.service;

import ch.supsi.webapp.web.model.Ticket;
import ch.supsi.webapp.web.model.User;
import ch.supsi.webapp.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();
    private final Map<String, String> roles = new HashMap<>();

    @PostConstruct
    public void init() {
        roles.putIfAbsent("admin", "ROLE_ADMIN");
        roles.putIfAbsent("user", "ROLE_USER");

        if (userRepository.findAll().size() == 0) {
            User admin = new User("admin", "admin", "admin", encoder.encode("admin"), roles.get("admin"));
            userRepository.save(admin);
        }
    }

    public boolean post(User user) {
        if (userRepository.findUserByUsername(user.getUsername()) == null) {
            user.setPassword(encoder.encode(user.getPassword()));
            user.setRole(roles.get("user"));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    public boolean addToWatched(User user, Ticket ticket) {
        if (user == null || ticket == null) return false;
        user.getWatchedTickets().add(ticket);
        userRepository.save(user);
        return true;
    }

    public boolean removeToWatched(User user, Ticket ticket) {
        if (user == null || ticket == null) return false;
        user.getWatchedTickets().remove(ticket);
        userRepository.save(user);
        return true;
    }

    public Set<Ticket> getWatched(String username) {
        User user = userRepository.findUserByUsername(username);
        return user.getWatchedTickets();
    }

}
