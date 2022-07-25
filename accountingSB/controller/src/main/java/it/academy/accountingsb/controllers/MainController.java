package it.academy.accountingsb.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "spring")
public class MainController {

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = {"/", "/index"})
    public String viewIndexPage() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
