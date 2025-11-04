package thegame.game;
import thegame.game.GameManager;

public class Main {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new GameManager());
    }
}

