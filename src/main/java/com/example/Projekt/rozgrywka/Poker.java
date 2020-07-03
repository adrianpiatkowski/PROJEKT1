package com.example.Projekt.rozgrywka;

public abstract class Poker {

    public static double getChenScore(Karta[] cards) {
        if (cards.length != 2) {
            throw new IllegalArgumentException("Invalid number of cards: " + cards.length);
        }
        int rank1 = cards[0].getFigura();
        int suit1 = cards[0].getKolor();
        int rank2 = cards[1].getFigura();
        int suit2 = cards[1].getKolor();
        int highRank = Math.max(rank1, rank2);
        int lowRank = Math.min(rank1, rank2);
        int rankDiff = highRank - lowRank;
        int gap = (rankDiff > 1) ? rankDiff - 1 : 0;
        boolean isPair = (rank1 == rank2);
        boolean isSuited = (suit1 == suit2);
        double score = 0.0;

        if (highRank == Karta.ACE) {
            score = 10.0;
        } else if (highRank == Karta.KING) {
            score = 8.0;
        } else if (highRank == Karta.QUEEN) {
            score = 7.0;
        } else if (highRank == Karta.JACK) {
            score = 6.0;
        } else {
            score = (highRank + 2) / 2.0;
        }

        if (isPair) {
            score *= 2.0;
            if (score < 5.0) {
                score = 5.0;
            }
        }
        if (isSuited) {
            score += 2.0;
        }
        if (gap == 1) {
            score -= 1.0;
        } else if (gap == 2) {
            score -= 2.0;
        } else if (gap == 3) {
            score -= 4.0;
        } else if (gap > 3) {
            score -= 5.0;
        }
        if (!isPair && gap < 2 && rank1 < Karta.QUEEN && rank2 < Karta.QUEEN) {
            score += 1.0;
        }
        if (score < 0.0) {
            score = 0.0;
        }
        return Math.round(score);
    }
}
