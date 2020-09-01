package state;

public enum State {
    START(0), SURVIVAL_TRAINING(5);

    private int currentState;

    private State(int state) {
        currentState = state;
    }
    public int get(){
        return currentState;
    }
}
