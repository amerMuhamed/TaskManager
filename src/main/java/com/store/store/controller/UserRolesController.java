package com.store.store.controller;

import com.store.store.entity.Task;
import com.store.store.entity.User;
import com.store.store.excepions.DuplicateUserException;
import com.store.store.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class UserRolesController {
    private UserService userService;
    private HttpSession session;

    @Autowired
    public UserRolesController(UserService userService, HttpSession session) {
        this.userService = userService;
        this.session = session;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/")
    public String showHome(Model theModel) {
        User user = (User) session.getAttribute("user");
        if (user != null) {

            if (user.getTask() == null) {
                theModel.addAttribute("message", "No tasks Available");
            } else {
                theModel.addAttribute("task", user.getTask());
            }
        }
        else{
            return "redirect:/showLoginPage";
        }
        return "home";
    }

    @GetMapping("/users")
    public String getUsers(Model theModel, @RequestParam(value = "name", required = false) String name) {
        List<User> users;
        if (name == null || name.isEmpty()) {
            users = userService.getAllUsersWithRoles();
        } else {
            users = userService.findByUserId(name);
        }
        theModel.addAttribute("users", users);
        theModel.addAttribute("name", name);
        return "list-users";
    }

    @GetMapping("/create")
    public String createUser(Model theModel) {
        User theUser = (User) theModel.asMap().get("user");
        if (theUser == null) {
            theUser = new User(); // إنشاء مستخدم جديد إذا لم يكن موجوداً
        }
        theModel.addAttribute("user", theUser);
        return "createUser";
    }


    @PostMapping("/save-user")
    public String saveUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "createUser";
        }
        try {
            userService.saveUser(user);
            return "redirect:/users";
        } catch (DuplicateUserException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/create";
        }
    }

    @PostMapping("/update-user")
    public String updateUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "updateUser";
        }
        try {
            userService.updateUser(user);
            return "redirect:/users";
        } catch (DuplicateUserException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/updating";
        }
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("userId") int userId) {
        userService.deleteById(userId);
        return "redirect:/users";
    }

    @GetMapping("/update")
    public String sendToUpdate(Model theModel, @RequestParam("userId") int userId) {
        User user = userService.findById(userId);
        theModel.addAttribute("user", user);
        return "updateUser";
    }

    @GetMapping("/updating")
    public String GoToUpdatingPage(Model theModel) {
        User theUser = (User) theModel.asMap().get("user");
        if (theUser == null) {
            theUser = new User(); // إنشاء مستخدم جديد إذا لم يكن موجوداً
        }
        theModel.addAttribute("user", theUser);
        return "updateUser";
    }

}

