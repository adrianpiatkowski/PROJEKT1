package com.example.Projekt.controlers;
import com.example.Projekt.UserRequestIndex;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
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
