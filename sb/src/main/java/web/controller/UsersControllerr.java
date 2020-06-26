package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import web.model.Role;
import web.model.User;
import web.service.RoleService;
import web.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/")
public class UsersControllerr {
    private final UserService userService;
    private final RoleService roleService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public UsersControllerr(UserService userService, RoleService roleService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping(value = "/user")
    public String userPageGet() {
        return "userPage";
    }

    @GetMapping(value = "/login")
    public String loginUserGet() {
        return "starterPage";
    }

    @PostMapping(value = "/login")
    public String loginUserPost(@RequestParam(value = "login") String login, ModelMap model) {
        if (userService.getUserByLogin(login) == null) {
            model.addAttribute("errorText", "No user with this data exists");
            return "error";
        } else {
            return "redirect:/user";
        }
    }

    @GetMapping(value = "admin")
    public String printUsers(ModelMap model) {
        List<User> userList = userService.getAllUsers();

        model.addAttribute("listUser", userList);
        return "admin/user-list";
    }

    @GetMapping(value = "/admin/add")
    public String addUserGet() {
  return "/admin/add";
    }

    @PostMapping(value = "/admin/add")
    public String addUserPost(@RequestParam(value = "login") String login, @RequestParam(value = "password") String password,
                              @RequestParam(value = "name") String name, @RequestParam(value = "role") String role,
                              @RequestParam(value = "age") String ageStr, ModelMap model) {
        try {
            int age = Integer.parseInt(ageStr);
            if (name.isEmpty() || age < 0 || age > 150) {
                model.addAttribute("errorText", "Incorrect user fields.");
                return "error";
            }

            if (userService.isExistLogin(login)) {
                model.addAttribute("errorText", "User with same login already exist.");
                return "error";
            }
            Set<Role> roles = new HashSet<>();
            if (role != null && role.equals("user")) {
                roles.add(roleService.getRoleByName("USER"));
            }
            if (role != null && role.equals("admin")) {
                roles.add(roleService.getRoleByName("ADMIN"));
                roles.add(roleService.getRoleByName("USER"));
            }
            User user = new User(login, password, role, name, age, roles);
            if (!userService.addUser(user)) {
                model.addAttribute("errorText", "Error while processing user edit.");
                return "error";
            }
            model.addAttribute("errorText", "User was added successfully!");
            return "error";
        } catch (Exception e) {
            model.addAttribute("errorText", "Error while processing user.");
            return "error";
        }
    }


    @GetMapping(value = "/admin/edit/{id}")
    public String updateUserGet(@PathVariable("id") int id, Model model) {
        User user = userService.getUserById(id);
        if(user==null) {
          throw  new IllegalArgumentException("Invalid student Id:" + id);
        }
        model.addAttribute("user", user);
            return "/admin/edit";
    }


    @PostMapping(value = "/admin/update/{id}")
    public String editUserPost(@RequestParam(value = "login") String login, @RequestParam(value = "password") String password,
                               @RequestParam(value = "name") String name, @RequestParam(value = "role") String role,
                               @RequestParam(value = "age") String ageStr, ModelMap model, @PathVariable("id") int id) {
        try {
            int age = Integer.parseInt(ageStr);
            password = bCryptPasswordEncoder.encode(password);
            if (name.isEmpty() || age < 0 || age > 150) {
                model.addAttribute("errorText", "Incorrect user fields.");
                return "error";
            }

            Set<Role> roles = new HashSet<>();
            if (role != null && role.equals("user")) {
                roles.add(roleService.getRoleByName("USER"));
            }
            if (role != null && role.equals("admin")) {
                roles.add(roleService.getRoleByName("ADMIN"));
                roles.add(roleService.getRoleByName("USER"));
            }

            User user = new User(id, login, password, role, name, age, roles);
            model.addAttribute("user", user);
            if (!userService.updateUser(user)) {
                model.addAttribute("errorText", "Error while processing user edit.");
                return "error";
            }
            return "redirect:/admin";
        } catch (Exception e) {
            model.addAttribute("errorText", "Error while processing user.");
            return "error";
        }
    }

    @GetMapping(value = "/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") int id, Model model) {
        User user = userService.getUserById(id);
        if(user==null) {
            throw  new IllegalArgumentException("Invalid student Id:" + id);
        }
        userService.deleteUser(user);
        model.addAttribute("students", userService.getAllUsers());
        return "redirect:/admin";
    }


    @GetMapping("/raw")
    @ResponseBody
    public String raw() {
        return "raw data";
    }
}
