package com.example.Projekt.Uruchomienie;

import com.example.Projekt.rozgrywka.Gracz;
import com.example.Projekt.rozgrywka.Stol;


import java.util.*;

public class Main  {




    private static final int BIG_BLIND = 10;


    private static final int STARTING_CASH = 500;

    private static Stol table;


    private final Stol table1 = new Stol(BIG_BLIND);


    private static Map<String, Gracz> players = new Map<String, Gracz>() {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean containsKey(Object key) {
            return false;
        }

        @Override
        public boolean containsValue(Object value) {
            return false;
        }

        @Override
        public Gracz get(Object key) {
            return null;
        }

        @Override
        public Gracz put(String key, Gracz value) {
            return null;
        }

        @Override
        public Gracz remove(Object key) {
            return null;
        }

        @Override
        public void putAll(Map<? extends String, ? extends Gracz> m) {

        }

        @Override
        public void clear() {

        }

        @Override
        public Set<String> keySet() {
            return null;
        }

        @Override
        public Collection<Gracz> values() {
            return null;
        }

        @Override
        public Set<Entry<String, Gracz>> entrySet() {
            return null;
        }
    };
    public static Scanner scanner = new Scanner(System.in);


    private String dealerName;


    private String actorName;




    public static void main(String[] args) {

//
//        int liczbaGraczy =0;
//        List<Gracz> imie = new LinkedList<>();
//        String imie2;
//
//        System.out.println("Texas Hold'em poker");
//        System.out.println("Podaj liczbę graczy");
//        liczbaGraczy = scanner.nextInt();
//
//        for (int gracze = 0; gracze < liczbaGraczy; gracze++) {
//                System.out.println("Podaj imię gracza");
//                imie2 = scanner.next();
//                imie.add(new Gracz(imie2,500));
//        }
//
//        table = new Stol( BIG_BLIND);
//
//        for (Gracz player : imie) {
//            table.addPlayer(player);
//        }
//
//        table.run();
        doTestow();










    }
    public static void doTestow(){



        players = new LinkedHashMap<>();
        Set<Gracz> gracze222 = new HashSet<>();
        Gracz gracz1 = new Gracz("Adi", STARTING_CASH,"Ja");
        Gracz gracz2= new Gracz("Joe", STARTING_CASH,"ja");
        Gracz gracz3= new Gracz("Edward", STARTING_CASH, "ja");
        gracze222.add(gracz1);
        gracze222.add(gracz2);
        gracze222.add(gracz3);

        table = new Stol( BIG_BLIND);
        for (Gracz player : gracze222) {
            table.addPlayer(player);
        }


        table.run();

    }

}
