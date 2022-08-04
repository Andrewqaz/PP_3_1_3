package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/")
public class UserController {
    private final UserService service;

    private final PasswordEncoder passwordEncoder;

    public UserController(UserService service, PasswordEncoder passwordEncoder) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String goToUser(@AuthenticationPrincipal User authorizedUser){
        if (authorizedUser == null){
            return "redirect:/login";
        }
        if ((authorizedUser.getRoles().stream()
                .filter(role -> role.getName().equals("ADMIN")).count() > 0)){
            return "redirect:/admin";
        }
        return "redirect:/user";
    }

    @GetMapping("login")
    public String getLogin(@RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "logout", required = false) String logout,
                           Model model){
        model.addAttribute("error", error != null);
        model.addAttribute("logout", logout != null);
        return "login";
    }

    @GetMapping("admin")
    public String getAdminPage(Model model, @AuthenticationPrincipal User authorizedUser) {
        List<User> allUsers = service.getAllUsers();
        model.addAttribute("users", allUsers);
        model.addAttribute("authorizedUser", service.getUserByEmail(authorizedUser.getEmail()));
        model.addAttribute("listRoles", service.listRoles());
        model.addAttribute("newUser", new User());
        return "adminPage";
    }

    @GetMapping("user")
    public String getUserPage(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("authorizedUser", service.getUserByEmail(user.getEmail()));
        return "userPage";
    }


    @PostMapping("admin/adduser")
    public String addUser(@ModelAttribute("newUser") User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        service.addUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("admin/delete/{id}")
    public String deleteUser(@PathVariable("id") int userId) {
        service.deleteUserById(userId);

        return "redirect:/admin";
    }

    @PatchMapping("/admin/edit/{id}")
    public String updateUser(@ModelAttribute("user") User user, @PathVariable("id") Long id,
                             @RequestParam(value = "nameRoles", required = false) String roles){
        Set<Role> roleSet = new HashSet<Role>();
        roleSet.add(service.getRoleByName(roles));
        user.setRoles(roleSet);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        service.updateUser(user);

        return "redirect:/admin";
    }
}
