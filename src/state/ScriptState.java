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
        GIELINOR_GUIDE,
        SURVIVAL_TRAINING,
        MASTER_CHEF,
        QUEST_GUIDE,
        MINING_INSTRUCTOR,
        COMBAT_INSTRUCTOR,
        BANKING_TUTORIAL,
        PRAYER_TUTORIAL,
        MAGIC_TUTOR;
    }

}
