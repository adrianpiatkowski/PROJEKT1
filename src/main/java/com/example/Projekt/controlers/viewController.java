package com.example.Projekt.controlers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class viewController {

    @GetMapping("/view")
    public String view(Model model){
        model.addAttribute("welcome", "Let's start the game!");
        return "view";
    }



}
