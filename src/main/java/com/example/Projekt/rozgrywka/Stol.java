package com.example.Projekt.rozgrywka;

import com.example.Projekt.actions.*;

import java.util.*;
import java.util.regex.Pattern;

public class Stol {
    Pattern pattern = Pattern.compile("Raise(.*)");
    Pattern patternBET = Pattern.compile("Bet(.*)");
    public Scanner scanner = new Scanner(System.in);
    private  Action poprzedniaAkcja;
    private static final int MAX_RAISES = 3;
    private static final boolean ALWAYS_CALL_SHOWDOWN = false;
    private final int bigBlind;
    private final List<Gracz> players;
    private final   List<Gracz> activePlayers;
    private final Talia deck;
    private final List<Karta> board;
    private int dealerPosition;
    private int actorPosition;
    private Gracz actor;
    private int minBet;
    private int bet;
    private Gracz lastBettor;
    private int stawka =0;
    private final List<Pot> pots;

    public Stol( int bigBlind) {

        this.bigBlind = bigBlind;
        players = new ArrayList<Gracz>();
        activePlayers = new ArrayList<Gracz>();
        deck = new Talia();
        board = new ArrayList<Karta>();
        pots = new ArrayList<Pot>();
    }
    public void addPlayer(Gracz player) {
        players.add(player);
    }
    public  void run() {
        dealerPosition = -1;
        actorPosition = -1;
        while (true) {
            int noOfActivePlayers = 0;
            for (Gracz player : players) {

                if (player.getCash() >= bigBlind) {
                    noOfActivePlayers++;
                }
            }
            if (noOfActivePlayers > 1) {
                playHand();
            } else {
                break;
            }
        }
        System.out.println("Thank you :"+players);
        board.clear();
        pots.clear();
        bet = 0;
        for (Gracz player : players) {
            player.resetHand();
        }
        System.out.println("Game over");
    }
    private void playHand() {
        resetHand();

        // Small blind.
        if (activePlayers.size() > 2) {
            rotateActor();
        }
        postSmallBlind();

        // Big blind.
        rotateActor();
        postBigBlind();

        // Pre-Flop.
        dealHoleCards();
        doBettingRound();
        // Flop.
        if (activePlayers.size() > 1) {
            bet = 0;
            dealCommunityCards(3);
            minBet = bigBlind;
            System.out.println("*****=======================Flop=======================*****");
            System.out.println("=======================" +board.toString()+"=======================\n");
            poprzedniaAkcja = Action.CHECK;
            doBettingRound();
            stawka=0;
            // Turn.
            if (activePlayers.size() > 1) {
                bet = 0;
                dealCommunityCards(1);
                minBet = bigBlind;
                System.out.println("******=======================Turn=======================******");
                System.out.println("=======================" +board.toString()+"=======================\n");
                poprzedniaAkcja = Action.CHECK;
                doBettingRound();
                stawka=0;
                // River.
                if (activePlayers.size() > 1) {
                    bet = 0;
                    dealCommunityCards(1);
                    minBet = bigBlind;
                    System.out.println("*******=======================River=======================*******");
                    System.out.println("=======================" +board.toString()+"=======================\n");
                    poprzedniaAkcja = Action.CHECK;
                    doBettingRound();
                    stawka=0;
                    for (Gracz player: players
                         ) {
                        System.out.println(player.getCash()+ " kasa gracza " + player.getName() + " i ma karty " +Arrays.toString(player.getCards()));

                    }
                    // Showdown.
                    if (activePlayers.size() > 1) {
                        bet = 0;
                        minBet = bigBlind;
                        doShowdown();
                    }
                    poprzedniaAkcja = Action.CONTINUE;
                }
            }
        }
    }
    private void resetHand() {
        board.clear();
        pots.clear();
        activePlayers.clear();
        for (Gracz player : players) {
            player.resetHand();

            if (player.getCash() >= bigBlind) {
                activePlayers.add(player);
            }
        }
        dealerPosition = (dealerPosition + 1) % activePlayers.size();
        Gracz dealer = activePlayers.get(dealerPosition);
        deck.shuffle();
        actorPosition = dealerPosition;
        actor = activePlayers.get(actorPosition);
        minBet = bigBlind;
        bet = minBet;
    }
    private void rotateActor() {
        actorPosition = (actorPosition + 1) % activePlayers.size();
        actor = activePlayers.get(actorPosition);
    }
    private void postSmallBlind() {
        final int smallBlind = bigBlind / 2;
        actor.postSmallBlind(smallBlind);
        contributePot(smallBlind);
    }
    private void postBigBlind() {
        actor.postBigBlind(bigBlind);
        contributePot(bigBlind);

    }
    private void dealHoleCards() {
        for (Gracz player : activePlayers) {
            player.setCards(deck.deal(2));
        }
        System.out.println();
    }
    private void dealCommunityCards(int noOfCards) {
        for (int i = 0; i < noOfCards; i++) {
            board.add(deck.deal());
        }
    }
    private void doBettingRound() {
        int playersToAct = activePlayers.size();
        if (board.size() == 0) {
            bet = bigBlind;
        } else {
            actorPosition = dealerPosition;
            bet = 0;
        }
        lastBettor = null;
        int raises = 0;
        while (playersToAct > 0) {
            rotateActor();
            Action action = null;
            if (actor.isAllIn()) {
                action = Action.CHECK;
                playersToAct--;
            } else {
                if (actor.getAction()==null ){
                    actor.setAction(new ContinueAction());
                }
                if (poprzedniaAkcja==null){
                    poprzedniaAkcja = Action.CONTINUE;
                }
                if (actor.getIfBot().equals("Bot")) {
                    botAction(actor);
                    action = actor.getAction();

                } else{
                    getAllowedActions(actor);
                    action = actor.getAction();
                }
                playersToAct--;
                if (action == Action.CHECK) {
                } else if (action == Action.CALL) {
                    int betIncrement = bet - actor.getBet();
                    if (betIncrement > actor.getCash()) {
                        betIncrement = actor.getCash();
                    }
                    actor.payCash(betIncrement);
                    actor.setBet(actor.getBet() + betIncrement);
                    contributePot(betIncrement);
                } else if (action instanceof BetAction) {
                    int amount =action.getAmount();
                    if (amount < minBet && amount < actor.getCash()) {
                        throw new IllegalStateException("Illegal client action: bet less than minimum bet!");
                    }
                    if (amount > actor.getCash() && actor.getCash() >= minBet) {
                        throw new IllegalStateException("Illegal client action: bet more cash than you own!");
                    }
                    bet = amount;
                    minBet = amount;
                    int betIncrement = bet - actor.getBet();
                    if (betIncrement > actor.getCash()) {
                        betIncrement = actor.getCash();
                    }
                    actor.setBet(bet);
                    actor.payCash(betIncrement);
                    contributePot(betIncrement);
                    lastBettor = actor;
                    stawka = bet;
                    playersToAct = (activePlayers.size() - 1);
                } else if (action instanceof RaiseAction) {
                    int amount = action.getAmount();
                    if (amount < minBet && amount < actor.getCash()) {
                        throw new IllegalStateException("Illegal client action: raise less than minimum bet!");
                    }
                    if (amount > actor.getCash() && actor.getCash() >= minBet) {
                        throw new IllegalStateException("Illegal client action: raise more cash than you own!");
                    }
                    bet += amount;
                    minBet = amount;
                    int betIncrement = bet - actor.getBet();
                    if (betIncrement > actor.getCash()) {
                        betIncrement = actor.getCash();
                    }
                    actor.setBet(bet);
                    actor.payCash(betIncrement);
                    contributePot(betIncrement);
                    lastBettor = actor;
                    raises++;
                    if ((raises < MAX_RAISES || activePlayers.size() == 2)) {

                        playersToAct = activePlayers.size();
                    } else {

                        playersToAct = activePlayers.size() - 1;
                    }
                    stawka=bet;
                } else if (action == Action.FOLD) {
                    actor.setCards(null);
                    activePlayers.remove(actor);
                    actorPosition--;
                    if (activePlayers.size() == 1) {

                        Gracz winner = activePlayers.get(0);
                        int amount = getTotalPot();
                        winner.win(amount);

                        playersToAct = 0;
                    }
                }

            }

//            actor.setAction(action);
            System.out.println( "Total pot on the table:"+ getTotalPot());
            poprzedniaAkcja = actor.getAction() ;

        }
        for (Gracz player : activePlayers) {
            player.resetBet();
        }
    }
    private void getAllowedActions(Gracz player) {
        System.out.println("***"+player.getName()+" you have "+player.getCash()+"$.***");
        if (player.getAction().equals(Action.SMALL_BLIND)){
            int akcja = -1;
            System.out.println(player.getName() + " what you want to do:\n 1-AllIn \n 2-Raise \n 3-Call \n 4-Fold ");
            akcja = scanner.nextInt();
            switch (akcja) {
                case 1:
                    int wartosc0 = player.getCash();
                    player.setAction(new RaiseAction(wartosc0));
                    break;
                case 2:
                    int wartosc1 = 0;
                    System.out.println("Enter the raise value");
                    wartosc1 = scanner.nextInt();
                    player.setAction(new RaiseAction(wartosc1));
                    break;
                case 3:
                    player.setAction(Action.CALL);
                    break;
                case 4:
                    player.setAction(Action.FOLD);
                    break;
            }

        }else if (player.getAction().equals(Action.BIG_BLIND)){
            int akcja1 = -1;
            if (pattern.matcher(poprzedniaAkcja.toString()).matches()){
                System.out.println(player.getName() + " what you want to do:\n 1-AllIn \n 2-Raise \n 3-Call \n 4-Fold ");
                System.out.println(poprzedniaAkcja.toString()+ "tuuu");
                akcja1 = scanner.nextInt();
                switch (akcja1) {
                    case 1:
                        int wartosc0 = player.getCash();
                        player.setAction(new RaiseAction(wartosc0));
                        break;
                    case 2:
                        int wartosc1 = 0;
                        System.out.println("Enter the raise value");
                        wartosc1 = scanner.nextInt();
                        player.setAction(new RaiseAction(wartosc1));
                        break;
                    case 3:
                        player.setAction(Action.CALL);
                        break;
                    case 4:
                        player.setAction(Action.FOLD);
                        break;
                }
            }else {
                int akcja2;
                System.out.println(player.getName() + " what you want to do:\n 1-AllIn \n 2-Raise \n 3-Check \n 4-Fold ");
                akcja2 = scanner.nextInt();
                switch (akcja2) {
                    case 1:
                        int wartosc0 = player.getCash();
                        player.setAction(new RaiseAction(wartosc0));
                        break;
                    case 2:
                        int wartosc1 = 0;
                        System.out.println("Enter the raise value");
                        wartosc1 = scanner.nextInt();
                        player.setAction(new RaiseAction(wartosc1));
                        break;
                    case 3:
                        player.setAction(Action.CHECK);
                        break;
                    case 4:
                        player.setAction(Action.FOLD);
                        break;
                }
            }
        }else if (patternBET.matcher(poprzedniaAkcja.toString()).matches()) {
            System.out.println(poprzedniaAkcja.toString()+"POPRZEDNIA AKCJA - bet"+ " stawka"+stawka);
            int akcja3 = -1;
            System.out.println(player.getName() + " wybierz akcję\n 1-AllIn \n 2-Raise \n 3-Call \n 4-Fold ");
            akcja3 = scanner.nextInt();
            switch (akcja3) {
                case 1:
                    int wartosc0 = actor.getCash();
                    player.setAction(new RaiseAction(wartosc0));
                    break;
                case 2:
                    int wartosc1 ;

                    System.out.println("Enter the raise value");
                    wartosc1 = scanner.nextInt();
                    if (wartosc1<=poprzedniaAkcja.getAmount()){
                        System.out.println("There was bet for:"+poprzedniaAkcja.getAmount()+" , raise must be higher."); //TODO while...
                        int wartosc2;
                        wartosc2 = scanner.nextInt();
                        player.setAction(new RaiseAction(wartosc2));
                    }else
                    player.setAction(new RaiseAction(wartosc1));
                    break;
                case 3:
                    player.setAction(Action.CALL);
                    break;
                case 4:
                    player.setAction(Action.FOLD);
                    break;
            }
        }else if (pattern.matcher(poprzedniaAkcja.toString()).matches()){
            System.out.println(poprzedniaAkcja.toString()+" POPRZEDNIA AKCJA - Raise"+ "stawka"+stawka);
            int akcja4 = -1;
            System.out.println(player.getName() + " wybierz akcję\n 1-AllIn \n 2-Rerise \n 3-Call \n 4-Fold");
            akcja4 = scanner.nextInt();
            switch (akcja4) {
                case 1:
                    int wartosc0 = actor.getCash();
                    player.setAction(new RaiseAction(wartosc0));
                    break;
                case 2:
                    int wartosc1 = 0;
                    System.out.println("Enter the raise value");
                    wartosc1 = scanner.nextInt();
                    player.setAction(new RaiseAction(wartosc1));
                    break;
                case 3:
                    player.setAction(Action.CALL);
                    break;
                case 4:
                    player.setAction(Action.FOLD);
                    break;
            }
        }else if (poprzedniaAkcja.toString().equals("Check")) {
            System.out.println(poprzedniaAkcja.toString()+" POPRZEDNIA AKCJA - call lub check");
            int akcja5 = -1;
            System.out.println(player.getName() + " wybierz akcję\n 1-AllIn \n 2-Bet \n 3-Check \n 4-Fold");
            akcja5 = scanner.nextInt();
            switch (akcja5) {
                case 1:
                    int wartosc0 = actor.getCash();
                    player.setAction(new BetAction(wartosc0));
                    break;
                case 2:
                    int wartosc1 = 0;
                    System.out.println("Enter the bet value");
                    wartosc1 = scanner.nextInt();
                    player.setAction(new BetAction(wartosc1));
                    break;
                case 3:
                    player.setAction(Action.CHECK);
                    break;
                case 4:
                    player.setAction(Action.FOLD);
                    break;
            }
        }else if (poprzedniaAkcja.toString().equals("Call") && !player.getAction().equals(Action.BIG_BLIND))   {
            int akcja6 = -1;
            System.out.println(player.getName() + " wybierz akcję\n 1-AllIn \n 2-Bet \n 3-Call \n 4-Fold");
            akcja6 = scanner.nextInt();
            switch (akcja6) {
                case 1:
                    int wartosc0 = actor.getCash();
                    player.setAction(new BetAction(wartosc0));
                    break;
                case 2:
                    int wartosc1 = 0;
                    System.out.println("Enter the bet value");
                    wartosc1 = scanner.nextInt();
                    player.setAction(new BetAction(wartosc1));
                    break;
                case 3:
                    player.setAction(Action.CALL);
                    break;
                case 4:
                    player.setAction(Action.FOLD);
                    break;
            }

        }else if (player.getAction().toString().equals("Continue")) {
            int akcja6 = -1;
            System.out.println(player.getName() + " wybierz akcję\n 1-AllIn \n 2-Raise \n 3-Call \n 4-Fold ");
            akcja6 = scanner.nextInt();
            switch (akcja6) {
                case 1:
                    int wartosc0 = actor.getCash();
                    player.setAction(new RaiseAction(wartosc0));
                    break;
                case 2:
                    int wartosc1;

                    System.out.println("Enter the raise value");
                    wartosc1 = scanner.nextInt();
                    if (wartosc1 <= poprzedniaAkcja.getAmount()) {
                        System.out.println("There was bet for:" + poprzedniaAkcja.getAmount() + " , raise must be higher.");//TODO while musi tu być
                        int wartosc2;
                        wartosc2 = scanner.nextInt();
                        player.setAction(new RaiseAction(wartosc2));
                    } else
                        player.setAction(new RaiseAction(wartosc1));
                    break;
                case 3:
                    player.setAction(Action.CALL);
                    break;
                case 4:
                    player.setAction(Action.FOLD);
                    break;
            }
        }else{
            System.out.println("else");
            int akcja7 = -1;
            System.out.println(player.getName() + " wybierz akcję\n 1-AllIn \n 2-Raise \n 3-Call/Check \n 4-Fold ");
            akcja7 = scanner.nextInt();
            switch (akcja7) {
                case 1:
                    int wartosc0 = actor.getCash();
                    player.setAction(new RaiseAction(wartosc0));
                    break;
                case 2:
                    int wartosc1;

                    System.out.println("Enter the raise value");
                    wartosc1 = scanner.nextInt();
                    if (wartosc1 <= poprzedniaAkcja.getAmount()) {
                        System.out.println("There was bet for:" + poprzedniaAkcja.getAmount() + " , raise must be higher."); //TODO while musi tu być
                        int wartosc2;
                        wartosc2 = scanner.nextInt();
                        player.setAction(new RaiseAction(wartosc2));
                    } else
                        player.setAction(new RaiseAction(wartosc1));
                    break;
                case 3:
                    player.setAction(Action.CHECK);
                    break;
                case 4:
                    player.setAction(Action.FOLD);
                    break;
            }
        }
        System.out.println(actor.getName() +" " + actor.getAction());
    }


    private void contributePot(int amount) {
        for (Pot pot : pots) {
            if (!pot.hasContributer(actor)) {
                int potBet = pot.getBet();
                if (amount >= potBet) {
                    pot.addContributer(actor);
                    amount -= pot.getBet();
                } else {
                    pots.add(pot.split(actor, amount));
                    amount = 0;
                }
            }
            if (amount <= 0) {
                break;
            }
        }
        if (amount > 0) {
            Pot pot = new Pot(amount);
            pot.addContributer(actor);
            pots.add(pot);
        }
    }
    private void doShowdown() {
        List<Gracz> showingPlayers = new ArrayList<>();
        for (Pot pot : pots) {
            for (Gracz contributor : pot.getContributors()) {
                if (!showingPlayers.contains(contributor) && contributor.isAllIn()) {
                    showingPlayers.add(contributor);
                }
            }
        }
        if (lastBettor != null) {
            if (!showingPlayers.contains(lastBettor)) {
                showingPlayers.add(lastBettor);
            }
        }
        int pos = (dealerPosition + 1) % activePlayers.size();
        System.out.println("showing players size - "+ showingPlayers.size() + "active players size - "+activePlayers.size());
        while (showingPlayers.size() < activePlayers.size() ) {
            Gracz player = activePlayers.get(pos);
            if (!showingPlayers.contains(player)) {
                showingPlayers.add(player);
            }
            pos = (pos + 1) % activePlayers.size();
        }
        System.out.println(activePlayers.size() + "<-aktywni , pokazujący -> " + showingPlayers.size());
        boolean firstToShow = true;
        int bestHandValue = -1;
        for (Gracz playerToShow : showingPlayers) {
            KartyGracza hand = new KartyGracza(board);
            hand.addKarty(playerToShow.getCards());
            //=====================================================================
            System.out.println(hand.toString());
            //=====================================================================
            Wartosc handValue = new Wartosc(hand);
            //=====================================================================
            System.out.println(playerToShow.getName() + "wartosc reki "+ handValue);
            //=====================================================================
            boolean doShow = ALWAYS_CALL_SHOWDOWN;
            if (!doShow) {
                if (playerToShow.isAllIn()) {

                    doShow = true;
                    firstToShow = false;
                } else if (firstToShow) {

                    doShow = true;
                    bestHandValue = handValue.getWartosc();
                    firstToShow = false;
                } else {
                    if (handValue.getWartosc() >= bestHandValue) {
                        doShow = true;
                        bestHandValue = handValue.getWartosc();
                    }
                }
            }
            if (doShow) {
                System.out.println("\n" +playerToShow+ " has - " + handValue.getDescription() );
            } else {
                playerToShow.setCards(null);
                activePlayers.remove(playerToShow);
                System.out.println(playerToShow.getName()+ " folds.");
            }
        }
        Map<Wartosc, List<Gracz>> rankedPlayers = new TreeMap<Wartosc, List<Gracz>>();
        for (Gracz player : activePlayers) {
            KartyGracza hand = new KartyGracza(board);
            hand.addKarty(player.getCards());
            Wartosc handValue = new Wartosc(hand);
            List<Gracz> playerList = rankedPlayers.get(handValue);
            if (playerList == null) {
                playerList = new ArrayList<Gracz>();
            }
            playerList.add(player);
            rankedPlayers.put(handValue, playerList);
            //=====================================================================
            System.out.println(rankedPlayers.toString() + " tu jest ranked player");
            //=====================================================================
        }
        System.out.println(rankedPlayers.toString() + " tu są ranked players - za pętlą");
        int totalPot = getTotalPot();
        Map<Gracz, Integer> potDivision = new HashMap<Gracz, Integer>();
        for (Wartosc handValue : rankedPlayers.keySet()) {
            List<Gracz> winners = rankedPlayers.get(handValue);
            //=====================================================================
            System.out.println(pots.toString() + " potsssss lista + rankedPlayers"+rankedPlayers + " + ranked players value"+rankedPlayers.get(handValue));
            //=====================================================================
            for (Pot pot : pots) {
                System.out.println("winners w petli pots - "+ winners.toString());
                int noOfWinnersInPot = 1; //TODO powinno być zero
                for (Gracz winner : winners) {
                    System.out.println(winners.toString() + "lista winnersów");
                    System.out.println(winner.toString() + " winners w pętli 957");
                    System.out.println(pot + "pot w pętli 958");
                    System.out.println(pot.getContributors().toString() + " contributors w pot");
                    if (pot.getContributors().contains(winner)) {
                        noOfWinnersInPot++;
                        //=====================================================================
                        System.out.println("?????????????????????a tu wchodzi + liczba zwyciezcow- " + noOfWinnersInPot);
                        //=====================================================================
                    }
                }
                if (noOfWinnersInPot > 0) {
                    //=====================================================================
                    System.out.println("liczba noOfWinnersInPot większa od 0 -      "+noOfWinnersInPot);
                    //=====================================================================
                    int potShare = pot.getValue() / noOfWinnersInPot;
                    for (Gracz winner : winners) {
                        if (true) { //TODO if (pot.hasContributer(winner))
                            System.out.println("976");
                            Integer oldShare = potDivision.get(winner);
                            if (oldShare != null) {
                                potDivision.put(winner, oldShare + potShare);
                            } else {
                                potDivision.put(winner, potShare);
                            }
                        }
                    }
                    int oddChips = pot.getValue() % noOfWinnersInPot;
                    if (oddChips > 0) {
                        pos = dealerPosition;
                        while (oddChips > 0) {
                            pos = (pos + 1) % activePlayers.size();
                            Gracz winner = activePlayers.get(pos);
                            Integer oldShare = potDivision.get(winner);
                            if (oldShare != null) {
                                potDivision.put(winner, oldShare + 1);
                                oddChips--;
                            }
                        }
                    }
                    pot.clear();
                }
            }

        }
        System.out.println(potDivision.keySet() + " --potshare===============?????????????");
        StringBuilder winnerText = new StringBuilder();
        int totalWon = 0;
        for (Gracz winner : potDivision.keySet()) {
            int potShare = potDivision.get(winner);
            System.out.println(potShare + " --potshare===============????????????? wchodzi tu??");
            winner.win(potShare);
            totalWon += potShare;
            if (winnerText.length() > 0) {
                winnerText.append(", ");
            }
            System.out.println("\n**********" +winner.getName()+" with " + Arrays.toString(winner.getCards()) +" wins " + potShare + "$.**********\n" );
        }
        System.out.println("pełna wygrana "+ totalWon + "  pełny pot"+totalPot );
        // Sanity check.
        if (totalWon != totalPot) {
            throw new IllegalStateException("Incorrect pot division!");
        }
    }
    private int getTotalPot() {
        int totalPot = 0;
        for (Pot pot : pots) {
            totalPot += pot.getValue();
        }
        return totalPot;
    }
    private void botAction(Gracz actor) {
        Action action = Action.CONTINUE;
        double chenScore = Poker.getChenScore(actor.getCards());
        double chenScoreToPlay = 10 * 0.2;
        System.out.println("***"+actor.getName()+" you have "+actor.getCash()+"$.***");
        if (actor.getAction().equals(Action.SMALL_BLIND)){
            if ((chenScore - chenScoreToPlay) >= ((20.0 - chenScoreToPlay) / 2.0)) {
                int amount = minBet;
                int betLevel = 3;
                for (int i = 0; i < betLevel; i++) {
                    amount *= 2;
                }
                actor.setAction(new RaiseAction(amount));
            }else if (chenScore < chenScoreToPlay){
                actor.setAction(Action.FOLD);
            }else {actor.setAction(Action.CALL);}
        }else if (actor.getAction().equals(Action.BIG_BLIND)){
            if (pattern.matcher(poprzedniaAkcja.toString()).matches()){
                if (chenScore < chenScoreToPlay){
                    actor.setAction(Action.FOLD);
                }else {actor.setAction(Action.CALL);}

            }else { actor.setAction(Action.CALL); }
        }else if (patternBET.matcher(poprzedniaAkcja.toString()).matches()) {
            actor.setAction(Action.CALL);
        }else if (pattern.matcher(poprzedniaAkcja.toString()).matches()){
            if (chenScore < chenScoreToPlay){
                actor.setAction(Action.FOLD);
            }else {actor.setAction(Action.CALL);}
        }else if (poprzedniaAkcja.toString().equals("Check")) {
            if ((chenScore - chenScoreToPlay) >= ((20.0 - chenScoreToPlay) / 2.0)) {
                int amount = minBet;
                int betLevel = 3;
                for (int i = 0; i < betLevel; i++) {
                    amount *= 2;
                }
                actor.setAction(new BetAction(amount));
            }else {actor.setAction(Action.CHECK);}
        }else if (poprzedniaAkcja.toString().equals("Call") && !actor.getAction().equals(Action.BIG_BLIND))   {
            actor.setAction(Action.CALL);
        }else if (actor.getAction().toString().equals("Continue")) {
            if ((chenScore - chenScoreToPlay) >= ((20.0 - chenScoreToPlay) / 2.0)) {
                int amount = minBet;
                int betLevel = 3;
                for (int i = 0; i < betLevel; i++) {
                    amount *= 2;
                }
                actor.setAction(new RaiseAction(amount));
            }else if (chenScore < chenScoreToPlay){
                actor.setAction(Action.FOLD);
            }else {actor.setAction(Action.CALL);}
        }else{
            if ((chenScore - chenScoreToPlay) >= ((20.0 - chenScoreToPlay) / 2.0)) {
                int amount = minBet;
                int betLevel = 3;
                for (int i = 0; i < betLevel; i++) {
                    amount *= 2;
                }
                actor.setAction(new RaiseAction(amount));
            }else if (chenScore < chenScoreToPlay){
                actor.setAction(Action.FOLD);
            }else {actor.setAction(Action.CHECK);}
        }
        System.out.println("Co robi  "+ actor.getName()+" - " + action.getVerb());
        System.out.println("Bot have "+ actor.getCash());

    }
}
