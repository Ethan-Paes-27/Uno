
package uno;

import java.util.ArrayList;
import java.util.List;

public class TeamRandom_UnoPlayer implements UnoPlayer {

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

        if (upCard.getRank().equals(Rank.WILD) || upCard.getRank().equals(Rank.WILD_D4)) {
            possible = possiblePlays(hand, calledColor);
        }
        else {
            possible = possiblePlays(hand, upCard);
        }

        if (possible.isEmpty()) {
            return -1;
        }

        int rand = (int)(Math.random() * possible.size());

        return possible.get(rand);
    }

    private List<Integer> possiblePlays(List<Card> hand, Color calledColor) {
        List<Integer> possible = new ArrayList<>();

        for (int i = 0; i < hand.size(); i++) {
            Card c = hand.get(i);

            if (c.getColor().equals(Color.NONE)) {
                possible.add(i);
                continue;
            }

            if (c.getColor().equals(calledColor)) {
                possible.add(i);
            }
        }

        return possible;
    }

    private List<Integer> possiblePlays(List<Card> hand, Card upCard) {

        List<Integer> possible = new ArrayList<>();

        for (int i = 0; i < hand.size(); i++) {
            Card c = hand.get(i);

            if (c.getColor().equals(Color.NONE)) {
                possible.add(i);
                continue;
            }

            if (!c.getRank().equals(Rank.NUMBER)) {
                if (c.getColor().equals(upCard.getColor()) || c.getRank().equals(upCard.getRank())) {
                    possible.add(i);
                }
                continue;
            }

            if (c.getColor().equals(upCard.getColor()) || c.getNumber() == upCard.getNumber()) {
                possible.add(i);
            }
        }

        return possible;
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
        int rand = (int)(Math.random() * 4);
        Color color = colorAtIndex(rand);
        return color;
    }
}

