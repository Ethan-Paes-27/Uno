package uno;

import java.util.List;
import java.util.Scanner;

public class TeamHuman_UnoPlayer implements UnoPlayer {

    @Override
    public int play(List<Card> hand, Card upCard, Color calledColor, GameState state) {
        System.out.println();
        System.out.print("Choose a card to play: ");
        Scanner in = new Scanner(System.in);
        int pos = in.nextInt();
        Card play = null;
        if (pos >= 0 && pos < hand.size()) {
            play = hand.get(pos);
        }
        while (!validMove(play, hand, upCard, calledColor)) {
            System.out.println("Illegal move, try something else:");
            System.out.println(hand);
            pos = in.nextInt();
            play = null;
            if (pos >= 0 && pos < hand.size()) {
                play = hand.get(pos);
            }
        }
        if (pos >= 0 && pos < hand.size()) {
            return pos;
        } else {
            return -1;
        }
    }

    @Override
    public Color callColor(List<Card> hand) {
        Scanner in = new Scanner(System.in);
        switch (in.next()) {
            case "R":
                return Color.RED;
            case "B":
                return Color.BLUE;
            case "G":
                return Color.GREEN;
            case "Y":
                return Color.YELLOW;
        }
        return Color.RED;
    }

    public boolean validMove(Card play, List<Card> hand, Card upCard, Color calledColor) {
        if (play != null) {
            if (play.canPlayOn(upCard, calledColor)) {
                return true;
            }
            return false;
        } else {
            for (Card c : hand) {
                if (c.canPlayOn(upCard, calledColor)) {
                    return false;
                }
            }
            return true;
        }
    }
}
