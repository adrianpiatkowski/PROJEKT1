package com.example.Projekt.rozgrywka;

public class Uklady {

    private static final int NO_OF_RANKINGS  = 6;


    private static final int MAX_NO_OF_PAIRS = 2;


    private static final int[] RANKING_FACTORS = {371293, 28561, 2197, 169, 13, 1};

    private WartoscTyp typ;

    private int value = 0;


    private final Karta[] cards;

    private int[] listaFigur = new int[Karta.LICZBA_FIGUR];


    private int[] listaKolory = new int[Karta.LICZBA_KOLOROW];


    private int liczbaPar = 0;


    private int[] pary = new int[MAX_NO_OF_PAIRS];


    private int flushSuit = -1;


    private int flushRank = -1;


    private int straightRank = -1;


    private boolean wheelingAce = false;


    private int tripleRank = -1;


    private int quadRank = -1;


    private int[] rankings = new int[NO_OF_RANKINGS];


    public Uklady(KartyGracza kartyGracza ) {
        cards = kartyGracza.getCards();

        calculateDistributions();
        findStraight();
        findFlush();
        findDuplicates();

        boolean isSpecialValue =
                (isStraightFlush() ||
                        isFourOfAKind()   ||
                        isFullHouse()     ||
                        isFlush()         ||
                        isStraight()      ||
                        isThreeOfAKind()  ||
                        isTwoPairs()      ||
                        isOnePair());
        if (!isSpecialValue) {
            calculateHighCard();
        }


        for (int i = 0; i < NO_OF_RANKINGS; i++) {
            value += rankings[i] * RANKING_FACTORS[i];
        }
    }


    public WartoscTyp getType() {
        return typ;
    }


    public int getValue() {
        return value;
    }


    private void calculateDistributions() {
        for (Karta card : cards) {
            listaFigur[card.getFigura()]++;
            listaKolory[card.getKolor()]++;
        }
    }


    private void findFlush() {
        for (int i = 0; i < Karta.LICZBA_KOLOROW; i++) {
            if (listaKolory[i] >= 5) {
                flushSuit = i;
                for (Karta card : cards) {
                    if (card.getKolor() == flushSuit) {
                        if (!wheelingAce || card.getFigura() != Karta.ACE) {
                            flushRank = card.getFigura();
                            break;
                        }
                    }
                }
                break;
            }
        }
    }


    private void findStraight() {
        boolean inStraight = false;
        int rank = -1;
        int count = 0;
        for (int i = Karta.LICZBA_FIGUR - 1; i >= 0 ; i--) {
            if (listaFigur[i] == 0) {
                inStraight = false;
                count = 0;
            } else {
                if (!inStraight) {

                    inStraight = true;
                    rank = i;
                }
                count++;
                if (count >= 5) {

                    straightRank = rank;
                    break;
                }
            }
        }

        if ((count == 4) && (rank == Karta.FIVE) && (listaFigur[Karta.ACE] > 0)) {
            wheelingAce = true;
            straightRank = rank;
        }
    }


    private void findDuplicates() {

        for (int i = Karta.LICZBA_FIGUR - 1; i >= 0 ; i--) {
            if (listaFigur[i] == 4) {
                quadRank = i;
            } else if (listaFigur[i] == 3) {
                tripleRank = i;
            } else if (listaFigur[i] == 2) {
                if (liczbaPar < MAX_NO_OF_PAIRS) {
                    pary[liczbaPar++] = i;
                }
            }
        }
    }


    private void calculateHighCard() {
        typ = WartoscTyp.HIGH_CARD;
        rankings[0] = typ.getValue();

        int index = 1;
        for (Karta card : cards) {
            rankings[index++] = card.getFigura();
            if (index > 5) {
                break;
            }
        }
    }


    private boolean isOnePair() {
        if (liczbaPar == 1) {
            typ = WartoscTyp.ONE_PAIR;
            rankings[0] = typ.getValue();

            int pairRank = pary[0];
            rankings[1] = pairRank;

            int index = 2;
            for (Karta card : cards) {
                int rank = card.getFigura();
                if (rank != pairRank) {
                    rankings[index++] = rank;
                    if (index > 4) {

                        break;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }


    private boolean isTwoPairs() {
        if (liczbaPar == 2) {
            typ = WartoscTyp.TWO_PAIRS;
            rankings[0] = typ.getValue();

            int highRank = pary[0];
            int lowRank  = pary[1];
            rankings[1] = highRank;
            rankings[2] = lowRank;

            for (Karta card : cards) {
                int rank = card.getFigura();
                if ((rank != highRank) && (rank != lowRank)) {
                    rankings[3] = rank;
                    break;
                }
            }
            return true;
        } else {
            return false;
        }
    }


    private boolean isThreeOfAKind() {
        if (tripleRank != -1) {
            typ = WartoscTyp.THREE_OF_A_KIND;
            rankings[0] = typ.getValue();
            rankings[1] = tripleRank;

            int index = 2;
            for (Karta card : cards) {
                int rank = card.getFigura();
                if (rank != tripleRank) {
                    rankings[index++] = rank;
                    if (index > 3) {

                        break;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }


    private boolean isStraight() {
        if (straightRank != -1) {
            typ = WartoscTyp.STRAIGHT;
            rankings[0] = typ.getValue();
            rankings[1] = straightRank;
            return true;
        } else {
            return false;
        }
    }


    private boolean isFlush() {
        if (flushSuit != -1) {
            typ = WartoscTyp.FLUSH;
            rankings[0] = typ.getValue();
            int index = 1;
            for (Karta card : cards) {
                if (card.getKolor() == flushSuit) {
                    int rank = card.getFigura();
                    if (index == 1) {
                        flushRank = rank;
                    }
                    rankings[index++] = rank;
                    if (index > 5) {
                        break;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }


    private boolean isFullHouse() {
        if ((tripleRank != -1) && (liczbaPar > 0)) {
            typ = WartoscTyp.FULL_HOUSE;
            rankings[0] = typ.getValue();
            rankings[1] = tripleRank;
            rankings[2] = pary[0];
            return true;
        } else {
            return false;
        }
    }

    private boolean isFourOfAKind() {
        if (quadRank != -1) {
            typ = WartoscTyp.FOUR_OF_A_KIND;
            rankings[0] = typ.getValue();
            rankings[1] = quadRank;

            int index = 2;
            for (Karta card : cards) {
                int rank = card.getFigura();
                if (rank != quadRank) {
                    rankings[index++] = rank;
                    break;
                }
            }
            return true;
        } else {
            return false;
        }
    }


    private boolean isStraightFlush() {
        if (straightRank != -1 && flushRank == straightRank) {

            int straightRank2 = -1;
            int lastSuit = -1;
            int lastRank = -1;
            int inStraight = 1;
            int inFlush = 1;
            for (Karta card : cards) {
                int rank = card.getFigura();
                int suit = card.getKolor();
                if (lastRank != -1) {
                    int rankDiff = lastRank - rank;
                    if (rankDiff == 1) {

                        inStraight++;
                        if (straightRank2 == -1) {
                            straightRank2 = lastRank;
                        }
                        if (suit == lastSuit) {
                            inFlush++;
                        } else {
                            inFlush = 1;
                        }
                        if (inStraight >= 5 && inFlush >= 5) {
                            break;
                        }
                    } else if (rankDiff == 0) {

                    } else {

                        straightRank2 = -1;
                        inStraight = 1;
                        inFlush = 1;
                    }
                }
                lastRank = rank;
                lastSuit = suit;
            }

            if (inStraight >= 5 && inFlush >= 5) {
                if (straightRank == Karta.ACE) {

                    typ = WartoscTyp.ROYAL_FLUSH;
                    rankings[0] = typ.getValue();
                    return true;
                } else {

                    typ = WartoscTyp.STRAIGHT_FLUSH;
                    rankings[0] = typ.getValue();
                    rankings[1] = straightRank2;
                    return true;
                }
            } else if (wheelingAce && inStraight >= 4 && inFlush >= 4) {

                typ = WartoscTyp.STRAIGHT_FLUSH;
                rankings[0] = typ.getValue();
                rankings[1] = straightRank2;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
