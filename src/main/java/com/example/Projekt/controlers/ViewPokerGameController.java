package com.example.Projekt.controlers;

import com.example.Projekt.UserRequestIndex;
import com.example.Projekt.UserRequestView;
import com.example.Projekt.actions.Action;
import com.example.Projekt.actions.ContinueAction;
import com.example.Projekt.rozgrywka.Gracz;
import com.example.Projekt.rozgrywka.Stol;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Controller
public class ViewPokerGameController {
    private Stol stol = new Stol(10);
    private Gracz gracz1;
    private Set<Gracz> gracze222 = new HashSet<>();
    @PostMapping("/viewPokerGame")
    public String viewPoker(Model model,@ModelAttribute("userRequest") UserRequestIndex userRequest){
        gracz1 = new Gracz(userRequest.getName(), 500,"Ja");
        gracz1.setAction(new ContinueAction());
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
            stol.setDealerPosition(-1);
            stol.setActorPosition(-1);
                int noOfActivePlayers = 0;
                for (Gracz player : gracze222) {

                    if (player.getCash() >= stol.getBigBlind()) {
                        noOfActivePlayers++;
                    }
                }
                if (noOfActivePlayers > 1) {
                    stol.resetHand();
                    // Small blind.
                    if (stol.getActivePlayers().size() > 2) {
                        stol.rotateActor();
                    }
                    stol.postSmallBlind();
                    // Big blind.
                    stol.rotateActor();
                    stol.postBigBlind();
                    // Pre-Flop.
                    stol.dealHoleCards();


                } else {
                    System.out.println("Thank you :"+gracze222.toString());
                    stol.getBoard().clear();
                    stol.getPots().clear();
                    stol.setBet(0);
                    for (Gracz player : gracze222) {
                        player.resetHand();
                    }

                }
        model.addAttribute("welcome", "Let's start the game");
        model.addAttribute("listOfPlayers","Players - " +gracze222.toString());
        model.addAttribute("playerCards","====Your cards:\t"+ Arrays.toString(gracz1.getCards())+ "====");
        model.addAttribute("currentAction", stol.getPoprzedniaAkcja().toString());
        model.addAttribute("whatToDo",stol.whatCanIDo(gracz1)+"\nIf you do bet or rise remember to enter the value.");
        model.addAttribute("userRequestFlop", new UserRequestView());
        model.addAttribute("userRequestTurn", new UserRequestView());
        model.addAttribute("userRequestRiver", new UserRequestView());
        model.addAttribute("userRequestAfterRiver", new UserRequestView());
        return "viewPokerGame";
    }
    @RequestMapping(value = "select", method = RequestMethod.POST) public String select(
            @ModelAttribute("userRequestFlop") UserRequestView userRequestFlop, Model model){
        return "/flop";
    }
    @PostMapping("/flop")
    public String viewFlop(Model model,@ModelAttribute("userRequestFlop") UserRequestView userRequestFlop) {
        gracz1.setChoosenAction(userRequestFlop.getChoosenAction());
        gracz1.setBetRiseValue(userRequestFlop.getValueRiseBet());
        stol.doBettingRound();
        stol.setBet(0);
        stol.dealCommunityCards(3);
        stol.setMinBet(10);
        model.addAttribute("flop","*****=======================Flop=======================*****");
        model.addAttribute("flopCards","=======================" +stol.getBoard().toString() +"=======================\n");
        model.addAttribute("playerCards","====Your cards:\t"+ Arrays.toString(gracz1.getCards())+ "====");
        model.addAttribute("whatToDo",stol.whatCanIDo(gracz1)+"\nIf you do bet or rise remember to enter the value.");
        model.addAttribute("currentAction", stol.getPoprzedniaAkcja().toString());
        stol.setPoprzedniaAkcja(Action.CHECK);
        model.addAttribute("userRequestTurn", new UserRequestView());
        return "flop";
    }
    @RequestMapping(value = "select1", method = RequestMethod.POST) public String select1(
            @ModelAttribute("userRequestTurn") UserRequestView userRequestTurn, Model model){
        return "/turn";
    }
    @PostMapping("/turn")
    public String viewTurn(Model model,@ModelAttribute("userRequestTurn") UserRequestView userRequestTurn) {
        gracz1.setChoosenAction(userRequestTurn.getChoosenAction());
        gracz1.setBetRiseValue(userRequestTurn.getValueRiseBet());
        stol.doBettingRound();
        stol.setBet(0);
        stol.dealCommunityCards(1);
        stol.setMinBet(10);
        model.addAttribute("turn","*****=======================Turn=======================*****");
        model.addAttribute("turnCards","=======================" +stol.getBoard().toString() +"=======================\n");
        model.addAttribute("playerCards","====Your cards:\t"+ Arrays.toString(gracz1.getCards())+ "====");
        model.addAttribute("whatToDo",stol.whatCanIDo(gracz1)+"\nIf you do bet or rise remember to enter the value.");
        model.addAttribute("currentAction", stol.getPoprzedniaAkcja().toString());
        stol.setPoprzedniaAkcja(Action.CHECK);
        model.addAttribute("userRequestRiver", new UserRequestView());
        return "turn";
    }

    @RequestMapping(value = "select2", method = RequestMethod.POST) public String select2(
            @ModelAttribute("userRequestRiver") UserRequestView userRequestRiver, Model model){
        return "/river";
    }

    @PostMapping("/river")
    public String viewRiver(Model model,@ModelAttribute("userRequestRiver") UserRequestView userRequestRiver) {
        gracz1.setChoosenAction(userRequestRiver.getChoosenAction());
        gracz1.setBetRiseValue(userRequestRiver.getValueRiseBet());
        stol.doBettingRound();
        stol.setBet(0);
        stol.dealCommunityCards(1);
        stol.setMinBet(10);
        model.addAttribute("river","*****=======================River=======================*****");
        model.addAttribute("riverCards","=======================" +stol.getBoard().toString() +"=======================\n");
        model.addAttribute("playerCards","====Your cards:\t"+ Arrays.toString(gracz1.getCards())+ "====");
        model.addAttribute("whatToDo",stol.whatCanIDo(gracz1)+"\nIf you do bet or rise remember to enter the value.");
        model.addAttribute("currentAction", stol.getPoprzedniaAkcja().toString());
        stol.setPoprzedniaAkcja(Action.CHECK);
        model.addAttribute("userRequestAfterRiver", new UserRequestView());
        return "river";
    }
    @RequestMapping(value = "select3", method = RequestMethod.POST) public String select3(
            @ModelAttribute("userRequestAfterRiver") UserRequestView userRequestAfterRiver, Model model){
        return "/winner";
    }
    @PostMapping("/winner")
    public String viewWinner(Model model,@ModelAttribute("userRequestAfterRiver") UserRequestView userRequestAfterRiver) {
        if (stol.getActivePlayers().size() > 1) {
            stol.setBet(0);
            stol.setMinBet(10);
            stol.doShowdown();
        }
        model.addAttribute("handValue",stol.getMessageHandList().toString());
        model.addAttribute("winner",stol.getMessageWinnerList().toString());
        return "winner";
    }

    @RequestMapping(value = "NewHand", method = RequestMethod.POST) public String newHand(
            @ModelAttribute("userRequest") UserRequestIndex userRequest, Model model){
        return "/newHand";
    }

    @PostMapping("/newHand")
    public String viewNewHand(Model model,@ModelAttribute("userRequest") UserRequestIndex userRequest) {
        int noOfActivePlayers = 0;
        for (Gracz player : gracze222) {
            if (player.getCash() >= stol.getBigBlind()) {
                noOfActivePlayers++;
            }
        }
        if (noOfActivePlayers > 1) {
            stol.resetHand();
            // Small blind.
            if (stol.getActivePlayers().size() > 2) {
                stol.rotateActor();
            }
            stol.postSmallBlind();
            // Big blind.
            stol.rotateActor();
            stol.postBigBlind();
            // Pre-Flop.
            stol.dealHoleCards();
            model.addAttribute("listOfPlayers","Players - " +gracze222.toString());
            model.addAttribute("playerCards","====Your cards:\t"+ Arrays.toString(gracz1.getCards())+ "====");
            model.addAttribute("currentAction", stol.getPoprzedniaAkcja().toString());
            model.addAttribute("whatToDo",stol.whatCanIDo(gracz1)+"\nIf you do bet or rise remember to enter the value.");
        } else {
            model.addAttribute("thanks", gracz1.getName() + " thank you for game.");
            Gracz zwyciezca = new Gracz("1", 0, ".");
            for (Gracz player :
                    gracze222) {
                if (player.getCash() > zwyciezca.getCash()) {
                    zwyciezca = player;
                }
            }
            model.addAttribute("zwyciezca", zwyciezca.getName() + "is champion!");
            model.addAttribute("over", "GAME OVER");
            System.out.println("Thank you :" + gracze222.toString());
            stol.getBoard().clear();
            stol.getPots().clear();
            stol.setBet(0);
            for (Gracz player : gracze222) {
                player.resetHand();
            }
        }
        model.addAttribute("userRequestFlop", new UserRequestView());
        model.addAttribute("welcome", "Let's start new hand");
        return "newHand";
    }
}
