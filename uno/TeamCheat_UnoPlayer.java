
package uno;

import java.util.ArrayList;
import java.util.List;

public class TeamCheat_UnoPlayer implements UnoPlayer {

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

    /**
     *
     * @param r, the rank of the card passed
     * @return what its corresponding power value is
     */
    private int cardTypeRankingsForBlocking(Rank r) {
        if (r.equals(Rank.WILD_D4)) {return 5;}
        if (r.equals(Rank.WILD)) {return 4;}
        if (r.equals(Rank.DRAW_TWO)) {return 3;}
        if (r.equals(Rank.SKIP)) {return 2;}
        if (r.equals(Rank.REVERSE)) {return 1;}
        if (r.equals(Rank.NUMBER)) {return 0;}
        return -1;
    }

    /**
     *
     * @param c, the card passed
     * @return what its corresponding point value is
     */
    private int cardRankingsForValue(Card c) {
        Rank r = c.getRank();

        if (r.equals(Rank.WILD_D4)) {return 15;}
        if (r.equals(Rank.WILD)) {return 15;}
        if (r.equals(Rank.DRAW_TWO)) {return 10;}
        if (r.equals(Rank.SKIP)) {return 10;}
        if (r.equals(Rank.REVERSE)) {return 10;}
        return c.getNumber();
    }

    /**
     *
     * @param cards, a list of cards
     * @return an int[] array where each index (0 to 3) corresponds to a color, as in colorAtIndex
     */
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

    private int playBiggestNumCard(List<Integer> possibleCards) {
        int bigVal = -1;
        int pos = -1;

        for (int i : possibleCards) {
            if (!hand.get(i).getRank().equals(Rank.NUMBER)) continue;

            int val = hand.get(i).getNumber();

            if (val > bigVal) {
                bigVal = val;
                pos = i;
            }
        }

        return pos;
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
    public int play(List<Card> handed, Card upCard, Color calledColor,
                    GameState state) {

        game = state; // update the game state
        hand = handed; // update the handed cards


        hand.clear();
        hand.add(new Card(Color.NONE, Rank.WILD_D4, -1));
        return 0;
    }
    
    /**
     * callColor - This method will be called when you have just played a
     * wild card, and is your way of specifying which color you want to
     * change it to.
     *
     * You must return a valid Color value from this method. You must not
     * return the value Color.NONE under any circumstances.
     */
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
