import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player extends Thread {
    private final String playerName;
    private final List<Card> playerHand;
    private boolean isFinished;
    private  Player nextPlayer;
    private Object turnLock;

    public Player(int playerNumber) {
        this.playerName = "Player " + playerNumber;
        playerHand = new ArrayList<>();
        isFinished = false;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public void setLock(Object turnLock) {
        this.turnLock = turnLock;
    }

    public void setNextPlayer(Player nextPlayer) {
        this.nextPlayer = nextPlayer;
    }

    public void addCardToHand(Card card) {
        playerHand.add(card);
    }

    private List<Card> getHand() {
        return playerHand;
    }

    private int getSize() {
        return playerHand.size();
    }

    protected String getPlayerName() {
        return playerName;
    }

    public void removeMatchingPairs() {
        List<Card> cardsToRemove = new ArrayList<>();
        for (int i = 0; i < playerHand.size(); i++) {
            Card currentCard = playerHand.get(i);
            for (int j = i + 1; j < playerHand.size(); j++) {
                Card nextCard = playerHand.get(j);
                if (currentCard.getValue() == nextCard.getValue() && currentCard.getColor() == nextCard.getColor()) {
                    cardsToRemove.add(currentCard);
                    cardsToRemove.add(nextCard);
                    break;
                }
            }
        }
        playerHand.removeAll(cardsToRemove);
    }

    private void printHand() {
        System.out.println(getPlayerName() + "'s hand is: ");
        System.out.println(getHand());
        System.out.println();
    }

    private void takeTurn() {
        Random random = new Random();
        int randomIndex = random.nextInt(nextPlayer.playerHand.size());
        Card playedCard = nextPlayer.playerHand.remove(randomIndex);
        playerHand.add(playedCard);
        System.out.println(playerName + " picked a card from " + nextPlayer.playerName);
        System.out.println("The Card is " + playedCard);

        removeMatchingPairs();
        if (nextPlayer.checkIfFinished()) {
            nextPlayer.setFinished(true);
        }
    }

    private Boolean checkIfFinished() {
        if (getSize() == 0) {
            isFinished = true;
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        while (!isFinished) {
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            if (checkIfFinished()) {
                break;
            }
            takeTurn();
            printHand();

            synchronized (turnLock) {
                turnLock.notify();
            }

            if (checkIfFinished()) {
                break;
            }
        }
        System.out.println(playerName + " is finished");
    }
}