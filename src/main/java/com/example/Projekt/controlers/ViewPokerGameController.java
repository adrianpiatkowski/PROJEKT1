package com.example.Projekt.controlers;

import com.example.Projekt.UserRequestIndex;
import com.example.Projekt.rozgrywka.Gracz;
import com.example.Projekt.rozgrywka.Stol;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.Set;

@Controller
public class ViewPokerGameController {

    private Stol stol = new Stol(10);

//        model.addAttribute("chooseAction",)

//        TODO odebranie tamtych dwóch parameterów name, noOfBots
//         uruchomienie logiki stołuy
//        TODO 2 zmienił nazewnictwo na np game/stol/poker viewPokerGame

    //TODO stół tworzysz mapowania dla przycisków w widoku dla stołu(atkualnie view) no i co ma się dziać


    @PostMapping("/viewPokerGame")
    public String viewPoker(Model model,@ModelAttribute("userRequest") UserRequestIndex userRequest){
        Set<Gracz> gracze222 = new HashSet<>();
        Gracz gracz1 = new Gracz(userRequest.getName(), 500,"Ja");
        for (int i= 0 ; i < userRequest.getNoOfBots(); i++){
            StringBuilder sb = new StringBuilder();
            sb.append("Bot-");
            sb.append(i);
            gracze222.add(new Gracz(sb.toString(),500,"Bot"));
        }
        gracze222.add(gracz1);
        for (Gracz player : gracze222) {
            stol.addPlayer(player);
        }

//        stol.run();

        model.addAttribute("welcome", "Let's start the game");
        model.addAttribute("listOfPlayers","Players - " +gracze222.toString());
//        model.addAttribute("whatToDo","Players - " +gracze222.toString());
        return "viewPokerGame";
    }


}
