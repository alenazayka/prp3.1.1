package web.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import web.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    boolean addUser(User user);

    List<User> getAllUsers();

    boolean deleteUser(User user);

    User getUserById(int id);

    boolean updateUser(User user);

    User getUserByLogin(String login);

    boolean isExistLogin(String login);
}
