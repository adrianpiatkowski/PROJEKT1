package com.example.Projekt.rozgrywka;

public class Wartosc implements Comparable<Wartosc> {

    private final KartyGracza kartyGracza;


    private final WartoscTyp typ;


    private final int wartosc;


    public Wartosc(KartyGracza kartyGracza) {
        this.kartyGracza = kartyGracza;
        Uklady uklady = new Uklady(kartyGracza);
        typ = uklady.getType();
        wartosc = uklady.getValue();
    }


    public KartyGracza getKartyGracza() {
        return kartyGracza;
    }

    public WartoscTyp getType() {
        return typ;
    }


    public String getDescription() {
        return typ.getDescription();
    }


    public int getWartosc() {
        return wartosc;
    }


    @Override
    public int hashCode() {
        return wartosc;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Wartosc) {
            return ((Wartosc) obj).getWartosc() == wartosc;
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(Wartosc wartosc1) {
        if (wartosc > wartosc1.getWartosc()) {
            return -1;
        } else if (wartosc < wartosc1.getWartosc()) {
            return 1;
        } else {
            return 0;
        }
    }


    @Override
    public String toString() {
        return String.format("%s (%d)", typ.getDescription(), wartosc);
    }

}
