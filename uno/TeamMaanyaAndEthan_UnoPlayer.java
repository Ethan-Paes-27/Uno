
package uno;

import java.util.ArrayList;
import java.util.List;

public class TeamMaanyaAndEthan_UnoPlayer implements UnoPlayer {

    private GameState game;

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

    private int cardTypeRankingsForBlocking(Rank r) {
        if (r.equals(Rank.NUMBER)) {return 0;}
        if (r.equals(Rank.WILD_D4)) {return 1;}
        if (r.equals(Rank.SKIP)) {return 2;}
        if (r.equals(Rank.DRAW_TWO)) {return 3;}
        if (r.equals(Rank.WILD)) {return 4;}
        if (r.equals(Rank.WILD_D4)) {return 5;}
        return -1;
    }

    private int cardTypeRankings(Rank r) {
        if (r.equals(Rank.NUMBER)) {return 0;}
        if (r.equals(Rank.REVERSE)) {return 1;}
        if (r.equals(Rank.SKIP)) {return 2;}
        if (r.equals(Rank.DRAW_TWO)) {return 3;}
        if (r.equals(Rank.WILD)) {return 4;}
        if (r.equals(Rank.WILD_D4)) {return 5;}
        return -1;
    }

    /**
     * play - This method is called when it's your turn and you need to
     * choose what card to play.
     *
     * The hand parameter tells you what's in your hand. You can call
     * getColor(), getRank(), and getNumber() on each of the cards it
     * contains to see what it is. The color will be the color of the card,
     * or "Color.NONE" if the card is a wild card. The rank will be
     * "Rank.NUMBER" for all numbered cards, and another value (e.g.,
     * "Rank.SKIP," "Rank.REVERSE," etc.) for special cards. The value of
     * a card's "number" only has meaning if it is a number card.
     * (Otherwise, it will be -1.)
     *
     * The upCard parameter works the same way, and tells you what the
     * up card (in the middle of the table) is.
     *
     * The calledColor parameter only has meaning if the up card is a wild,
     * and tells you what color the player who played that wild card called.
     *
     * Finally, the state parameter is a GameState object on which you can
     * invoke methods if you choose to access certain detailed information
     * about the game (like who is currently ahead, what colors each player
     * has recently called, etc.)
     *
     * You must return a value from this method indicating which card you
     * wish to play. If you return a number 0 or greater, that means you
     * want to play the card at that index. If you return -1, that means
     * that you cannot play any of your cards (none of them are legal plays)
     * in which case you will be forced to draw a card (this will happen
     * automatically for you.)
     */
    public int play(List<Card> hand, Card upCard, Color calledColor,
        GameState state) {

        game = state;

        List<Integer> possible;

        if (upCard.getColor().equals(Color.NONE)) {
            possible = possiblePlays(hand, new Card(calledColor, Rank.WILD, -1));
        }
        else {
            possible = possiblePlays(hand, upCard);
        }

        if (possible.isEmpty()) {
            return -1;
        }
        if (possible.size() == 1) {
            return possible.get(0);
        }

        if (game.getNumCardsInHandsOfUpcomingPlayers()[1] < 3) {
            int bestPos = playMessUpNextPerson(possible, hand);

            if (bestPos != -1) {
                return bestPos;
            }
        }

        // Color currentColor = upCard.getColor().equals(Color.NONE) ? calledColor : upCard.getColor();

        // int tryNumberCard = playNumberCard(possible, hand, currentColor);
        // if (tryNumberCard != -1) {
        //     return possible.get(tryNumberCard);
        // }

        int rand = (int)(Math.random() * possible.size());

        return possible.get(rand);
    }

    private List<Integer> possiblePlays(List<Card> hand, Card upCard) {
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

    private int playMessUpNextPerson(List<Integer> possibleCards, List<Card> hand) { //eventually make this so that if ranks are the same, then go for color min if applicable
        int bestPos = -1;
        int bestType = 0;

        for (int i : possibleCards) {
            Card c = hand.get(i);

            if (c.getRank().equals(Rank.NUMBER)) {
                continue;
            }

            int rank = cardTypeRankings(c.getRank());

            if (rank > bestType) {
                bestType = rank;
                bestPos = i;
            }
        }

        return bestPos;
    }
    
    // private int playNumberCard(List<Integer> possibleCards, List<Card> hand, Color currentColor) { //eventually make this to look for least repeated in opponent's likely hand
    //     int bestPos = -1;
    //     int bestNum = 0;

    //     for (int i = 0; i < possibleCards.size(); i++) {
    //         if (!possibleCards.contains(i)) {
    //             continue;
    //         }

    //         Card c = hand.get(i);

    //         if (!c.getRank().equals(Rank.NUMBER)) {
    //             continue;
    //         }

    //         if (c.getNumber() > bestNum) {
    //             bestNum = c.getNumber();
    //             bestPos = possibleCards.indexOf(c);
    //         }
    //     }

    //     return bestPos;
    // }
    
    /**
     * callColor - This method will be called when you have just played a
     * wild card, and is your way of specifying which color you want to
     * change it to.
     *
     * You must return a valid Color value from this method. You must not
     * return the value Color.NONE under any circumstances.
     */
    public Color callColor(List<Card> hand) { //NEED TO OPTIMIZE WILDS
        int[] oursAsColors = countColorsWild(hand);

        return bestCallColorWild(oursAsColors);
    }

    private int[] countColorsWild(List<Card> cards) {
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

    private Color bestCallColorWild(int[] ourHand) {
        ArrayList<Integer> possibleMaxes = possibleMaxes(ourHand);

        if (possibleMaxes.size() == 1) {
            return colorAtIndex(possibleMaxes.get(0));
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

