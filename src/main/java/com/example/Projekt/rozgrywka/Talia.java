package com.example.Projekt.rozgrywka;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Talia {
    private static final int LICZBA_KART = Karta.LICZBA_FIGUR * Karta.LICZBA_KOLOROW;
    private Karta[] cards;
    private int nextCardIndex = 0;
    private Random random = new Random();
    public Talia() {
        cards = new Karta[LICZBA_KART];
        int index = 0;
        for (int kolor = Karta.LICZBA_KOLOROW - 1; kolor >= 0; kolor--) {
            for (int figura = Karta.LICZBA_FIGUR - 1; figura >= 0 ; figura--) {
                cards[index++] = new Karta(figura, kolor);
            }
        }
    }
    public void shuffle() {
        for (int oldIndex = 0; oldIndex < LICZBA_KART; oldIndex++) {
            int newIndex = random.nextInt(LICZBA_KART);
            Karta tempCard = cards[oldIndex];
            cards[oldIndex] = cards[newIndex];
            cards[newIndex] = tempCard;
        }
        nextCardIndex = 0;
    }
    public void reset() {
        nextCardIndex = 0;
    }
    public Karta deal() {
        if (nextCardIndex + 1 >= LICZBA_KART) {
            throw new IllegalStateException("No cards left in deck");
        }
        return cards[nextCardIndex++];
    }
    public List<Karta> deal(int liczbaKart) {
        if (liczbaKart < 1) {
            throw new IllegalArgumentException("noOfCards < 1");
        }
        if (nextCardIndex + liczbaKart >= LICZBA_KART) {
            throw new IllegalStateException("No cards left in deck");
        }
        List<Karta> dealtCards = new ArrayList<Karta>();
        for (int i = 0; i < liczbaKart; i++) {
            dealtCards.add(cards[nextCardIndex++]);
        }
        return dealtCards;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Karta card : cards) {
            sb.append(card);
            sb.append(' ');
        }
        return sb.toString().trim();
    }
}
