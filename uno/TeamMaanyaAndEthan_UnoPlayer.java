
package uno;

import java.util.ArrayList;
import java.util.List;

public class TeamMaanyaAndEthan_UnoPlayer implements UnoPlayer {

    private GameState game; // the game state
    List<Card> hand; // our hand

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

//        if (anyoneHasLessThanNumCardsBesidesMe(4)) {
//            if (!isThisTheLeast(0)) {
//                if (isThisTheLeast(1)) {
//                    int block = playBlockNextPerson(possible);
//                    if (block != -1) {
//                        return block;
//                    }
//
//                    int big = playBiggestNumCard(possible);
//
//                    if (big != -1) {
//                        return big;
//                    }
//                }
//            }
//        }

        return playBiggestCard(possible); // play the biggest value card
    }

//
//
//        int pos = playBiggestNumCard(possible);
//        if (pos != -1) {
//            return pos;
//        }

//        if (numCards.length == 4) { // so if there are exactly 4 players, then this applies
//            int numCardsBehindYou = numCards[numCards.length-1]; // num cards of the person who just had their turn
//            int numCardsAcrossFromYou = numCards[2]; // num cards of the person who will have their turn in 2 moves
//
//
//            List<Rank> ranksToAvoid = new ArrayList<>(2);
//
//            if (numCardsBehindYou <= 1) { // if the person behind me has one card, then reverses are bad
//                ranksToAvoid.add(Rank.REVERSE);
//            }
//            if (numCardsAcrossFromYou <= 1) { // if the person across from me has one card, then skips are bad
//                ranksToAvoid.add(Rank.SKIP);
//                ranksToAvoid.add(Rank.WILD_D4);
//                ranksToAvoid.add(Rank.DRAW_TWO);
//            }
//
//            if (!ranksToAvoid.isEmpty()) { // if there are any ranks in the do not play list
//                int playUsingAvoid = doNotPlayCard(possible, ranksToAvoid);
//
//                if (playUsingAvoid != -1) {
//                    return playUsingAvoid;
//                }
//            }
//        }
//
//        int rand = (int)(Math.random() * possible.size());
//
//        return possible.get(rand);
//    }

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

    private boolean anyoneHasLessThanNumCardsBesidesMe(int num) {
        int[] numCards = game.getNumCardsInHandsOfUpcomingPlayers();

        for (int i = 1; i < numCards.length; i++) {
            if (numCards[i] < num) {
                return true;
            }
        }
        return false;
    }

    private boolean isThisTheLeast(int pos) {
        int[] numCards = game.getNumCardsInHandsOfUpcomingPlayers();

        int thisNumCards = numCards[pos];

        for (int i = 0; i < numCards.length; i++) {
            if (i == pos) continue;

            if (numCards[i] < thisNumCards) {
                return false;
            }
        }

        return true;
    }

    private int playBlockNextPerson(List<Integer> possibleCards) {
        int bestPos = -1;
        int bestType = 1;

        int[] numCardsOtherPlayers = game.getNumCardsInHandsOfUpcomingPlayers();
        List<Card> discarded = game.getPlayedCards();

        Color best = Color.NONE;

        if (discarded.size() > 10) {
            int[] colors = countColors(discarded);

            int max = 0;
            int pos = 0;

            for (int i = 0; i < 4; i++) {
                if (colors[i] > max) {
                    max = colors[i];
                    pos = i;
                }
            }

            best = colorAtIndex(pos);
        }

        for (int i : possibleCards) {
            Card c = hand.get(i);

            if (c.getRank().equals(Rank.NUMBER)) continue;

            int rank = cardTypeRankingsForBlocking(c.getRank());

            if (numCardsOtherPlayers.length >= 3) {
                if (numCardsOtherPlayers[numCardsOtherPlayers.length-1] == 1 && c.getRank().equals(Rank.REVERSE)) {
                    continue;
                }

                if (numCardsOtherPlayers[2] <= 2 && c.getRank().equals(Rank.SKIP)) {
                    continue;
                }
            }

            if (rank == bestType) {
                if (bestType >= 4) continue;

                bestPos = c.getColor().equals(best) ? i : bestPos;
            }

            if (rank > bestType) {
                bestType = rank;
                bestPos = i;
            }
        }

        return bestPos;
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

//    private int doNotPlayCard(List<Integer> possible, List<Rank> avoid) {
//        int pos = -1;
//        int maxType = 0;
//
//        for (int i : possible) {
//            Card c = hand.get(i);
//
//            if (avoid.contains(c.getRank())) {
//                continue;
//            }
//
//            int type = cardTypeRankingsForBlocking(c.getRank());
//
//            if (type > maxType) {
//                maxType = type;
//                pos = i;
//            }
//        }
//
//        if (pos == -1) {
//            return playBiggestType(possible);
//        }
//
//        return pos;
//    }

//    private int playBiggestType(List<Integer> possible) {
//        int maxType = 0;
//        int pos = 0;
//
//        if ()
//    }

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
