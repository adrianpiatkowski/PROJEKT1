package com.example.Projekt.rozgrywka;



import java.util.Collection;

public class KartyGracza {


    private static final int max = 7;



    private  Karta[] cards = new Karta[max];


    private  int liczbakart = 0;


    public KartyGracza() {
    }


    public KartyGracza(Collection<Karta> cards) {
        if (cards == null) {
            throw new IllegalArgumentException("Null array");
        }
        for (Karta card : cards) {
            addKarta(card);
        }
    }


    public void addKarta(Karta card) {
        if (card == null) {
            throw new IllegalArgumentException("Null card");
        }

        int insertIndex = -1;
        for (int i = 0; i < liczbakart; i++) {
            if (card.compareTo(cards[i]) > 0) {
                insertIndex = i;
                break;
            }
        }
        if (insertIndex == -1) {
            cards[liczbakart++] = card;
        } else {
            for (int i = liczbakart; i > insertIndex; i--) {
                cards[i] = cards[i - 1];
            }
            cards[insertIndex] = card;
            liczbakart++;
        }
    }


    public void addKarty(Karta[] cards) {
        if (cards == null) {
            throw new IllegalArgumentException("Null array");
        }
        if (cards.length > max) {
            throw new IllegalArgumentException("Too many cards");
        }
        for (Karta card : cards) {
            addKarta(card);
        }
    }


    public void addKarty(Collection<Karta> cards) {
        if (cards == null) {
            throw new IllegalArgumentException("Null collection");
        }
        if (cards.size() > max) {
            throw new IllegalArgumentException("Too many cards");
        }
        for (Karta card : cards) {
            addKarta(card);
        }
    }


    public  Karta[] getCards() {
        Karta[] dest = new Karta[liczbakart];
        System.arraycopy(cards, 0, dest, 0, liczbakart);
        return dest;
    }


    public void removeAllCards() {
        liczbakart = 0;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < liczbakart; i++) {
            sb.append(cards[i]);
            if (i < (liczbakart - 1)) {
                sb.append(' ');
            }
        }
        return sb.toString();
    }

}

