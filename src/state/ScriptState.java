package state;

public class ScriptState {
    private static final ScriptState scriptState = new ScriptState(States.MAGIC_TUTOR);
    public States state;

    public ScriptState(States state) {
        this.state = state;
    }

    public static States get() {
        return scriptState.state;
    }

    public static void set(States state) {
        scriptState.state = state;
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
