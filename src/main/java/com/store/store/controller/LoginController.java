package com.store.store.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {
    @GetMapping("/showLoginPage")
    public String showLoginPage(){
        return "showLogin";
    }
    @GetMapping("/access-denied")
    public String showAccessDenied (){
        return "access-denied";
    }



}
