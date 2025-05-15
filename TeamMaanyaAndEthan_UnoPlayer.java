
package uno;

import java.util.ArrayList;
import java.util.List;

public class TeamMaanyaAndEthan_UnoPlayer implements UnoPlayer {

    private GameState game;

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

        // THIS IS WHERE YOUR AMAZING CODE GOES
        return -1;
    }

    /**
     * callColor - This method will be called when you have just played a
     * wild card, and is your way of specifying which color you want to
     * change it to.
     *
     * You must return a valid Color value from this method. You must not
     * return the value Color.NONE under any circumstances.
     */
    public Color callColor(List<Card> hand) {
        int[] ours = countColors(hand);

        return bestCallColor(ours);
    }

    /**
     *
     * @param ourHand, our
     * @return
     */
    private Color bestCallColor(int[] ourHand) {
        ArrayList<Integer> possibleMaxes = possibleMaxes(ourHand);

        if (possibleMaxes.size() == 1) {
            return colorAtIndex(possibleMaxes.get(0));
        }

        int[] gameUsed = countColors(game.getPlayedCards());

        //WIP FINISH THISSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
        return Color.NONE;
    }

    /**
     *
     * @param ourHand, our current hand as an array which the values being color occurrences
     * @return possible maxes (more than one if there is a tie) of what indexes are the max
     */
    private ArrayList<Integer> possibleMaxes(int[] ourHand) {
        ArrayList<Integer> possibleMaxes = new ArrayList<>(4);
        int maxVal = 0;

        for (int i = 0; i < ourHand.length; i++) {
            if (ourHand[i] == maxVal) {
                possibleMaxes.add(i);
            }
            if (ourHand[i] > maxVal) {
                possibleMaxes.clear();
                possibleMaxes.add(i);
                maxVal = ourHand[i];
            }
        }

        return possibleMaxes;
    }

    /**
     *
     * @param i, the index (0 = red, yellow, green, 3 = blue)
     * @return the corresponding color
     */
    private Color colorAtIndex(int i) {
        if (i == 0) {
            return Color.RED;
        }
        if (i == 1) {
            return Color.YELLOW;
        }
        if (i == 2) {
            return Color.GREEN;
        }
        if (i == 3) {
            return Color.BLUE;
        }
        return Color.NONE;
    }

    /**
     *
     * @return the color amounts for every color, 0 is red, then yellow, green, and 3 is blue
     */
    private int[] countColors(List<Card> cards) {
        int[] colors = new int[4];

        for (Card c : cards) {
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

}

