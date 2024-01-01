import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of players: ");
        int numPlayers = scanner.nextInt();

        GameManager gameManager = new GameManager(numPlayers);
        gameManager.start();

        try {
            gameManager.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Game Over!");
        System.exit(0);
    }
}