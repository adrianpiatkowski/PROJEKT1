package com.example.Projekt.controlers;

import com.example.Projekt.UserRequestIndex;
import com.example.Projekt.rozgrywka.Gracz;
import com.example.Projekt.rozgrywka.Stol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@Controller
public class indexController {
    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("welcome", "Welcome! Please enter your name and number of bots.");
        model.addAttribute("userRequest", new UserRequestIndex());
        return "index";
    }
    @RequestMapping(value = "start", method = RequestMethod.POST) public String start(
            @ModelAttribute("userRequest") UserRequestIndex userRequest, Model model){
        return "/viewPokerGame";
    }
}
