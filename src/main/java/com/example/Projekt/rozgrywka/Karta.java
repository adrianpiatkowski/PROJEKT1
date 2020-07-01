package com.example.Projekt.rozgrywka;

public class Karta {
    public static final int LICZBA_FIGUR = 13;
    public static final int LICZBA_KOLOROW = 4;
    public static final int ACE      = 12;
    public static final int KING     = 11;
    public static final int QUEEN    = 10;
    public static final int JACK     = 9;
    public static final int TEN      = 8;
    public static final int NINE     = 7;
    public static final int EIGHT    = 6;
    public static final int SEVEN    = 5;
    public static final int SIX      = 4;
    public static final int FIVE     = 3;
    public static final int FOUR     = 2;
    public static final int THREE    = 1;
    public static final int DEUCE    = 0;
    public static final int SPADES   = 3;
    public static final int HEARTS   = 2;
    public static final int CLUBS    = 1;
    public static final int DIAMONDS = 0;
    public static final String[] SYMBOLE_FIGUY = {
            "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"
    };
    public static final char[] SYMBOLE_KOLORY = { 'd', 'c', 'h', 's' };
    private final int figura;
    private final int kolor;
    public Karta(int figura, int kolor) {
        if (figura < 0 || figura > LICZBA_FIGUR - 1) {
            throw new IllegalArgumentException("Invalid rank");
        }
        if (kolor < 0 || kolor > LICZBA_KOLOROW - 1) {
            throw new IllegalArgumentException("Invalid suit");
        }
        this.figura = figura;
        this.kolor = kolor;
    }
    public Karta(String s) {
        if (s == null) {
            throw new IllegalArgumentException("Null string or of invalid length");
        }
        s = s.trim();
        if (s.length() != 2) {
            throw new IllegalArgumentException("Empty string or invalid length");
        }
        String rankSymbol = s.substring(0, 1);
        char suitSymbol = s.charAt(1);
        int figura = -1;
        for (int i = 0; i < Karta.LICZBA_FIGUR; i++) {
            if (rankSymbol.equals(SYMBOLE_FIGUY[i])) {
                figura = i;
                break;
            }
        }
        if (figura == -1) {
            throw new IllegalArgumentException("Unknown rank: " + rankSymbol);
        }
        int kolor = -1;
        for (int i = 0; i < Karta.LICZBA_KOLOROW; i++) {
            if (suitSymbol == SYMBOLE_KOLORY[i]) {
                kolor = i;
                break;
            }
        }
        if (kolor == -1) {
            throw new IllegalArgumentException("Unknown suit: " + suitSymbol);
        }
        this.figura = figura;
        this.kolor = kolor;
    }
    public int getKolor() {
        return kolor;
    }
    public int getFigura() {
        return figura;
    }

    @Override
    public int hashCode() {
        return (figura * LICZBA_FIGUR + kolor);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Karta) {
            return ((Karta) obj).hashCode() == hashCode();
        } else {
            return false;
        }
    }
    public int compareTo(Karta card) {
        int thisValue = hashCode();
        int otherValue = card.hashCode();
        if (thisValue < otherValue) {
            return -1;
        } else if (thisValue > otherValue) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return SYMBOLE_FIGUY[figura] + SYMBOLE_KOLORY[kolor];
    }
}


