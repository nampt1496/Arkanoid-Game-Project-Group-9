package thegame.gameplay;

public class GameState {
    public enum State { RUNNING, PAUSED, GAME_OVER }
    private State state = State.RUNNING;

}