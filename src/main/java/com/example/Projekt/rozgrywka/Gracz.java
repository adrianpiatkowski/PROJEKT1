package com.example.Projekt.rozgrywka;

import com.example.Projekt.actions.Action;
import lombok.Data;

import java.util.List;

@Data
public class Gracz {
    private final String name;
    private String ifBot;
    private final KartyGracza kartyGracza;
    private int cash;
    private boolean maKarty;
    private Action action;
    private int bet;
    public Gracz(String name, int cash, String ifBot) {
        this.name = name;
        this.cash = cash;
        this.ifBot = ifBot;


        kartyGracza = new KartyGracza();

        resetHand();
    }
    public void resetHand() {
        maKarty = false;
        kartyGracza.removeAllCards();
        resetBet();
    }
    public void resetBet() {
        bet = 0;

    }
    public void setCards(List<Karta> cards) {
        kartyGracza.removeAllCards();
        if (cards != null) {
            if (cards.size() == 2) {
                kartyGracza.addKarty(cards);
                maKarty = true;
                System.out.format("==== %s's cards:\t%s ====\n", name, kartyGracza);

            } else {
                throw new IllegalArgumentException("Invalid number of cards");
            }
        }
    }
    public boolean maKarty() { return maKarty;}
    public String getName() {
        return name;
    }
    public int getCash() {
        return cash;
    }
    public int getBet() {
        return bet;
    }
    public void setBet(int bet) {
        this.bet = bet;
    }
    public Karta[] getCards() {
        return kartyGracza.getCards();
    }
    public void postSmallBlind(int blind) {
        action = Action.SMALL_BLIND;
        cash -= blind;
        bet += blind;
    }
    public void postBigBlind(int blind) {
        action = Action.BIG_BLIND;
        cash -= blind;
        bet += blind;
    }
    public boolean isAllIn() {
        return maKarty && (cash == 0);
    }
    public void payCash(int amount) {
        if (amount > cash) {
            throw new IllegalStateException("Player asked to pay more cash than he owns!");
        }
        cash -= amount;
    }
    public void win(int amount) {
        cash += amount;
    }
    public Action getAction() {
        return action;
    }
    public void setAction(Action action) {
        this.action = action;
    }
    public Gracz publicClone() {
        Gracz clone = new Gracz(name, cash, ifBot);
        clone.maKarty = maKarty;
        clone.bet = bet;
        clone.action = action;
        return clone;
    }

    @Override
    public String toString() {
        return name;
    }
}
