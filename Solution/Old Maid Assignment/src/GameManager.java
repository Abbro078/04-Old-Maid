import java.util.ArrayList;
import java.util.List;

public class GameManager extends Thread {
    private final List<Player> players;
    private final Deck deck;
    private final Object turnLock;

    public GameManager(int numPlayers) {
        players = new ArrayList<>();
        turnLock = new Object();
        for (int i = 1; i <= numPlayers; i++) {
            Player player = new Player(i);
            player.setLock(turnLock);
            players.add(player);
        }

        for (int i = 0; i < numPlayers; i++) {
            Player currentPlayer = players.get(i);
            Player nextPlayer = players.get((i + 1) % numPlayers);
            currentPlayer.setNextPlayer(nextPlayer);
        }
        deck = new Deck();
        deck.shuffle();
    }

    private void dealCards() {
        while (!deck.isEmpty()) {
            for (Player player : players) {
                if (deck.isEmpty()) {
                    break;
                }
                Card card = deck.drawCard();
                player.addCardToHand(card);
                System.out.println(player.getPlayerName() + " got: " + card);
            }
        }

        System.out.println();
        for (Player player : players) {
            player.removeMatchingPairs();
            System.out.println(player.getPlayerName() + " has removed their matching cards.");
        }
        System.out.println();
    }

    private void startingPlayerThreads() {
        for (Player player : players) {
            player.start();
        }
    }

    @Override
    public void run() {
        startingPlayerThreads();
        dealCards();
        int ind = 0;
        while (players.size() != 1) {
            if (ind == players.size()) {
                ind = 0;
            }
            Player player = players.get(ind);
            synchronized (player) {
                player.notify();
            }
            if (!player.isFinished()) {
                synchronized (turnLock) {
                    try {
                        turnLock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            if (player.isFinished()) {
                System.out.println(player.getPlayerName() + " Has no more cards");
                if (players.size() > 2) {
                    int before = ind - 1 < 0 ? players.size() - 1 : ind - 1;
                    int after = ind + 1 == players.size() ? 0 : ind + 1;
                    players.get(before).setNextPlayer(players.get(after));
                }
                players.remove(ind);
                continue;
            }
            ind++;
        }
        System.out.println(players.get(0).getPlayerName() + " is the loser!");
    }
}