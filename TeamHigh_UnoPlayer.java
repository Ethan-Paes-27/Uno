
package uno;

import java.util.ArrayList;
import java.util.List;

public class TeamHigh_UnoPlayer implements UnoPlayer {

    private GameState game;
    List<Card> hand;

    /**
     *
     * @param i, the index (0 = red, yellow, green, 3 = blue)
     * @return the corresponding color
     */
    private Color colorAtIndex(int i) {
        if (i == 0) {return Color.RED;}
        if (i == 1) {return Color.YELLOW;}
        if (i == 2) {return Color.GREEN;}
        if (i == 3) {return Color.BLUE;}
        return Color.NONE;
    }

    private int cardRankingsForValue(Card c) {
        Rank r = c.getRank();
        int num = c.getNumber();

        if (r.equals(Rank.WILD_D4)) {return 14;}
        if (r.equals(Rank.WILD)) {return 13;}
        if (r.equals(Rank.DRAW_TWO)) {return 12;}
        if (r.equals(Rank.SKIP)) {return 11;}
        if (r.equals(Rank.REVERSE)) {return 10;}
        return num;
    }

    private int[] countColors(List<Card> cards) {
        int[] colors = new int[4];

        for (Card c : cards) {
            if (c.getColor().equals(Color.NONE)) {
                continue;
            }
            if (c.getColor().equals(Color.RED)) {
                colors[0]++;
            }
            if (c.getColor().equals(Color.YELLOW)) {
                colors[1]++;
            }
            if (c.getColor().equals(Color.GREEN)) {
                colors[2]++;
            }
            if (c.getColor().equals(Color.BLUE)) {
                colors[3]++;
            }
        }
        return colors;
    }

    public int play(List<Card> handed, Card upCard, Color calledColor,
                    GameState state) {

        game = state;
        hand = handed;

        List<Integer> possible;

        if (upCard.getColor().equals(Color.NONE)) {
            possible = possiblePlays(new Card(calledColor, Rank.WILD, -1));
        }
        else {
            possible = possiblePlays(upCard);
        }

        if (possible.isEmpty()) {
            return -1;
        }
        if (possible.size() == 1) {
            return possible.get(0);
        }

        return playBiggestCard(possible);
    }

    private List<Integer> possiblePlays(Card upCard) {
        Rank rank = upCard.getRank();
        Color color = upCard.getColor();
        int number = upCard.getNumber();

        List<Integer> possible = new ArrayList<>();

        for (int i = 0; i < hand.size(); i++) {
            Card c = hand.get(i);

            if (c.getColor().equals(Color.NONE)) {
                possible.add(i);
                continue;
            }

            if (!c.getRank().equals(Rank.NUMBER)) {
                if (c.getColor().equals(color) || c.getRank().equals(rank)) {
                    possible.add(i);
                }
                continue;
            }

            if (c.getColor().equals(color) || c.getNumber() == number) {
                possible.add(i);
            }
        }

        return possible;
    }

    private int playBiggestCard(List<Integer> possibleCards) {
        int bigVal = -1;
        int pos = -1;

        for (int i : possibleCards) {
            Card c = hand.get(i);

            if (cardRankingsForValue(c) > bigVal) {
                bigVal = cardRankingsForValue(c);
                pos = i;
            }
        }

        return pos;
    }

    public Color callColor(List<Card> handed) {
        hand = handed;

        int[] oursAsColors = countColors(hand);

        return bestCallColorWild(oursAsColors);
    }

    private Color bestCallColorWild(int[] ourHand) {
        ArrayList<Integer> possibleMaxes = possibleMaxes(ourHand);

        if (possibleMaxes.size() == 1) {
            return colorAtIndex(possibleMaxes.get(0));
        }

        List<Card> playedCards = game.getPlayedCards();

        if (playedCards.size() > 1) {
            int[] gameAsColors = countColors(playedCards);

            List<Integer> possibleDiscardedMaxes = possibleMaxes(gameAsColors);

            int works = -1;

            for (int i : possibleMaxes) {
                if (possibleDiscardedMaxes.contains(i)) {
                    works = i;
                }
            }

            if (works != -1) {
                return colorAtIndex(works);
            }
        }

        int rand = (int)(Math.random() * possibleMaxes.size());

        return colorAtIndex(possibleMaxes.get(rand));
    }

    private ArrayList<Integer> possibleMaxes(int[] ourHandAsColors) {
        ArrayList<Integer> possible = new ArrayList<>();

        int maxVal = 0;

        for (int i = 0; i < ourHandAsColors.length; i++) {
            if (ourHandAsColors[i] > maxVal) {
                maxVal = ourHandAsColors[i];
                possible.clear();
                possible.add(i);
            }

            if (ourHandAsColors[i] == maxVal) {
                possible.add(i);
            }
        }

        return possible;
    }
}
