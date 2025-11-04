package thegame.gameplay;

public class GameState {
    public enum State { RUNNING, PAUSED, GAME_OVER }
    private State state = State.RUNNING;
    public State getState() { return state; }
    public void setState(State state) { this.state = state; }
    public boolean isRunning() { return state == State.RUNNING; }
    public boolean isGameOver() { return state == State.GAME_OVER; }
}