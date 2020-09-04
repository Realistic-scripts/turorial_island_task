package tasks;

import org.dreambot.api.script.TaskNode;
import state.ScriptState;
import state.TaskState;

enum QuestGuideState implements TaskState{
    TALK_TO_QUEST_GUIDE{
        @Override
        public Boolean run() {
            return null;
        }

        @Override
        public Boolean verify() {
            return null;
        }

        @Override
        public TaskState previousState() {
            return null;
        }

        @Override
        public TaskState nextState() {
            return null;
        }
    };
}

public class QuestGuide extends TaskNode {
    ScriptState state;

    public QuestGuide(ScriptState state) {
        this.state = state;
    }

    @Override
    public boolean accept() {
        return this.state.get() == ScriptState.States.QUEST_GUIDE;
    }

    @Override
    public int execute() {
        log("Starting Quest Guide");
        TaskState state = QuestGuideState.TALK_TO_QUEST_GUIDE;
        boolean done = false;
        while (!done) {
            if (state.verify()) {
                state.run();
            }
            state = state.nextState();
            done = state == null;
        }
        this.state.set(ScriptState.States.MASTER_CHEF);
        return 1;
    }
}
