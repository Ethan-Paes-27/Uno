
package uno;

import java.util.ArrayList;
import java.util.List;

public class TeamMaanyaAndEthan_UnoPlayer implements UnoPlayer {

    private GameState game; // the game state
    List<Card> hand; // our hand

    private final int MIN_CARDS_COUNT_DISCARD = 1;

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

        List<Integer> possible; // list of possible positions that I can play

        if (upCard.getColor().equals(Color.NONE)) { // if there is no color, then use the called color
            possible = possiblePlays(new Card(calledColor, Rank.WILD, -1));
        }
        else { // just use the upCard
            possible = possiblePlays(upCard);
        }

        if (possible.isEmpty()) { // if there are no possible cards, just return -1
            return -1;
        }
        if (possible.size() == 1) { // if there is only one card, play that
            return possible.get(0);
        }

        return playBiggestCard(possible); // play the biggest value card
    }

    /**
     *
     * @param upCard, the upCard
     * @return a list of all possible card positions that can be played from hand
     */
    private List<Integer> possiblePlays(Card upCard) {
        Rank rank = upCard.getRank(); // get the upCard's rank
        Color color = upCard.getColor(); // get the upCard's color
        int number = upCard.getNumber(); // get the upCard's number

        List<Integer> possible = new ArrayList<>(); // instantiate a list of the possible positions

        for (int i = 0; i < hand.size(); i++) { // iterate through the hand
            Card c = hand.get(i); // get the specified card for comparison at the index i

            if (c.getColor().equals(Color.NONE)) { // if it is a wild (or draw 4), it can always be played
                possible.add(i);
                continue;
            }

            if (!c.getRank().equals(Rank.NUMBER)) { // if it is not a number, check the color or rank
                if (c.getColor().equals(color) || c.getRank().equals(rank)) {
                    possible.add(i);
                }
                continue;
            }

            if (c.getColor().equals(color) || c.getNumber() == number) { // check the color or number for matches
                possible.add(i);
            }
        }

        return possible; // return the list of possible integers
    }

//    private int playBlockNextPerson(List<Integer> possibleCards) {
//        int bestPos = -1;
//        int bestType = 1;
//
//        int[] numCardsOtherPlayers = game.getNumCardsInHandsOfUpcomingPlayers();
//        List<Card> discarded = game.getPlayedCards();
//
//        Color best = Color.NONE;
//
//        if (discarded.size() > 10) {
//            int[] colors = countColors(discarded);
//
//            int max = 0;
//            int pos = 0;
//
//            for (int i = 0; i < 4; i++) {
//                if (colors[i] > max) {
//                    max = colors[i];
//                    pos = i;
//                }
//            }
//
//            best = colorAtIndex(pos);
//        }
//
//        for (int i : possibleCards) {
//            Card c = hand.get(i);
//
//            if (c.getRank().equals(Rank.NUMBER)) continue;
//
//            int rank = cardTypeRankingsForBlocking(c.getRank());
//
//            if (numCardsOtherPlayers.length >= 3) {
//                if (numCardsOtherPlayers[numCardsOtherPlayers.length-1] == 1 && c.getRank().equals(Rank.REVERSE)) {
//                    continue;
//                }
//
//                if (numCardsOtherPlayers[2] <= 2 && c.getRank().equals(Rank.SKIP)) {
//                    continue;
//                }
//            }
//
//            if (rank == bestType) {
//                if (bestType >= 4) continue;
//
//                bestPos = c.getColor().equals(best) ? i : bestPos;
//            }
//
//            if (rank > bestType) {
//                bestType = rank;
//                bestPos = i;
//            }
//        }
//
//        return bestPos;
//    }

    /**
     *
     * @param possibleCards, a list of all possible positions in hand that can be played
     * @return the position at which the value of the card is the largest
     */
    private int playBiggestCard(List<Integer> possibleCards) { //OPTIMIZE BY COLOR COUNTING
        int bigVal = -1; // the biggest point value
        int pos = -1; // the position of the biggest point value

        for (int i : possibleCards) { // go through every integer in the possible values
            Card c = hand.get(i); // get the right card from the hand

            if (cardRankingsForValue(c) > bigVal) { // if its value is bigger than the biggest value
                bigVal = cardRankingsForValue(c); // update the biggest value
                pos = i; // update the pos
            }
        }

        return pos; // return the best pos
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
        hand = handed; // update the hand

        int[] oursAsColors = countColors(hand); // count all colors in terms of our hand

        return bestCallColorWild(oursAsColors); // return from the bestCallColorWild with our color counted array
    }

    /**
     *
     * @param ourHand, our hand as an array of integers representing the color counts in the hand
     * @return the best color to play for optimal strategy
     */
    private Color bestCallColorWild(int[] ourHand) {
        ArrayList<Integer> possibleMaxes = possibleMaxes(ourHand);  // a list of all possible maxes of the color counting

        if (possibleMaxes.size() == 1) { // if there is only one possible color max, play that one
            return colorAtIndex(possibleMaxes.get(0));
        }

        List<Card> playedCards = game.getPlayedCards(); // get all the played cards

        if (playedCards.size() > MIN_CARDS_COUNT_DISCARD) { // if it's greater than this variable, then compare discarded cards
            int[] gameAsColors = countColors(playedCards); // get the game as colors by counting its colors

            List<Integer> possibleDiscardedMaxes = possibleMaxes(gameAsColors); // get the possible maxes for the game

            int works = -1; // the one that works

            for (int i : possibleMaxes) { // for every integer in the possible maxes
                if (possibleDiscardedMaxes.contains(i)) { // if the discarded maxes has it, then it works
                    works = i;
                }
            }

            if (works != -1) {
                return colorAtIndex(works); // if it is not -1, then return the right color
            }
        }

        int rand = (int)(Math.random() * possibleMaxes.size()); // at this point, it does not matter, so random works (all equal, from possible)

        return colorAtIndex(possibleMaxes.get(rand)); // return the right color
    }

    /**
     *
     * @param ourHandAsColors, our hand as an array of integers representing color counts
     * @return an arrayList of integers
     */
    private ArrayList<Integer> possibleMaxes(int[] ourHandAsColors) {
        ArrayList<Integer> possible = new ArrayList<>(); // instantiate the arrayList of possible spots

        int maxVal = 0; // the maximum count that exists

        for (int i = 0; i < 4; i++) { // go through every color index in the hand (it is always of length 4)
            if (ourHandAsColors[i] > maxVal) { // if it is greater than the max, then update the maxVal, clear possible, and add the new i
                maxVal = ourHandAsColors[i];
                possible.clear();
                possible.add(i);
            }

            if (ourHandAsColors[i] == maxVal) { // if it is equal, it also applies as a working value, so add it
                possible.add(i);
            }
        }

        return possible; // return all the possible values
    }
}
