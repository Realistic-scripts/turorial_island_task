package tasks;

import org.dreambot.api.methods.hint.HintArrow;
import org.dreambot.api.script.TaskNode;
import state.ScriptState;
import state.TaskState;
import utils.HintArrowHelper;
import utils.LogHelper;

enum MasterChefState implements TaskState {
    TALK_TO_CHEF {
        @Override
        public Boolean run() {

            HintArrow.getPointed().interact();
            // TODO check if dialog is open (use class...) Finish going through the convo.
            return true;
        }

        @Override
        public Boolean verify() {
            LogHelper.log(HintArrow.getPointed());
            if(HintArrow.exists()){
                return HintArrow.getPointed().getName().contains("Master Chef");
            }
            return false;
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

public class MasterChef extends TaskNode {
    ScriptState state;

    public MasterChef(ScriptState state) {
        this.state = state;
    }

    @Override
    public boolean accept() {
        return this.state.get() == ScriptState.States.MASTER_CHEF;
    }

    @Override
    public int execute() {
        log("Starting Master Chef");
        TaskState state = MasterChefState.TALK_TO_CHEF;
        boolean done = false;
        while (!done) {
            if (state.verify()) {
                state.run();
            }
            state = state.nextState();
            done = state == null;
        }
        return 1;
    }
}
