package it.academy.accountingsb.controllers;

import it.academy.accountingsb.entity.role.User;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/")
public class MainController {
    @GetMapping("/login")
    public String login() {
        return "login";
    }

//    @PostMapping("/login")
//    public String login(@ModelAttribute("user") @Valid User user,
//                        BindingResult bindingResult,
//                        @RequestParam("userName") String username,
//                        @RequestParam("password") String password,
//                        Model model) {
//        if (bindingResult.hasErrors()) {
//            model.addAttribute("userName", username);
//            model.addAttribute("password", password);
//            return login();
//        }
//        return "login";
//    }

}
