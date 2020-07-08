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


    public Set<Gracz> gracze222 = new HashSet<>();
    private Gracz player;



    private Stol stol = new Stol(10);

    @GetMapping("/")
    public String index(Model model){

        model.addAttribute("welcome", "Welcome! Please enter your name and number of bots.");
        model.addAttribute("userRequest", new UserRequestIndex());

        return "index";
    }

    @RequestMapping(value = "start", method = RequestMethod.POST) public String start(
            @ModelAttribute("userRequest") UserRequestIndex userRequest, Model model){
//        Gracz gracz1 = new Gracz(userRequest.getName(), 500,"Ja");
//        for (int i= 0 ; i < userRequest.getNoOfBots(); i++){
//            StringBuilder sb = new StringBuilder();
//            sb.append("Bot-");
//            sb.append(i);
//            gracze222.add(new Gracz(sb.toString(),500,"Bot"));
//        }
//        gracze222.add(gracz1);
//        for (Gracz player : gracze222) {
//            stol.addPlayer(player);
//        }
//        stol.run();
        //model.addAttribute("chooseAction",)
        return "/viewPokerGame";
    }

    @RequestMapping(value = "addAction", method = RequestMethod.POST) public String addAction(
            @ModelAttribute("userRequest") UserRequestIndex userRequest, Model model){


        return "redirect:/";
    }

//    @PostMapping("/viewPokerGame")
//    public String viewPoker(Model model,@ModelAttribute("userRequest") UserRequestIndex userRequest){
//
//        Gracz gracz1 = new Gracz(userRequest.getName(), 500,"Ja");
//        for (int i= 0 ; i < userRequest.getNoOfBots(); i++){
//            StringBuilder sb = new StringBuilder();
//            sb.append("Bot-");
//            sb.append(i);
//            gracze222.add(new Gracz(sb.toString(),500,"Bot"));
//        }
//        gracze222.add(gracz1);
//        for (Gracz player : gracze222) {
//            stol.addPlayer(player);
//        }
////        stol.run();
//
//        model.addAttribute("welcome", "Let's start the game");
//        model.addAttribute("listOfPlayers","Players - " +gracze222.toString());
////        model.addAttribute("whatToDo","Players - " +gracze222.toString());
//        return "viewPokerGame";
//    }

}
