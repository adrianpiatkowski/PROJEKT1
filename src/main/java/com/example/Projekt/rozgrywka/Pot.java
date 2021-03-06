package com.example.Projekt.rozgrywka;


//import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class Pot {
    private int bet;
    public final Set<Gracz> contributors;
    public Pot(int bet) {
        this.bet = bet;
        contributors = new HashSet<>();
    }
    public int getBet() {
        return bet;
    }
    public Set<Gracz> getContributors() {
        return Collections.unmodifiableSet(contributors);
    }
    public void addContributer(Gracz player) {
        contributors.add(player);
    }
    public boolean hasContributer(Gracz player) {
        for (Gracz gracz : contributors) {
            if(gracz.getName().equals(player.getName())) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
    public int getValue() {
        return bet * contributors.size();
    }
    public Pot split(Gracz player, int partialBet) {
        Pot pot = new Pot(bet - partialBet);
        for (Gracz contributer : contributors) {
            pot.addContributer(contributer);
        }
        bet = partialBet;
        contributors.add(player);
        return pot;
    }
    public void clear() {
        bet = 0;
        contributors.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(bet));
        sb.append(": {");
        boolean isFirst = true;
        for (Gracz contributer : contributors) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(", ");
            }
            sb.append(contributer.getName());
        }
        sb.append('}');
        sb.append(" (Total: ");
        sb.append(String.valueOf(getValue()));
        sb.append(')');
        return sb.toString();
    }

}
