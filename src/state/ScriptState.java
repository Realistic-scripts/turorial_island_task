package state;

public class ScriptState {
    private States state;

    public ScriptState(States state) {
        this.state = state;
    }

    public States get() {
        return this.state;
    }

    public void set(States state) {
        this.state = state;
    }

    public enum States {
        START,
        PICK_NAME,
        PICK_APPEARANCE,
        GG,
        GG_TO_FISHING,
        SURVIVAL_TRAINING,
        MASTER_CHEF;
    }

}
