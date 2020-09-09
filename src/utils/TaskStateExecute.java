package utils;

import state.TaskState;

public class TaskStateExecute {
    public static void taskStateExecute(TaskState state) {
        boolean done = false;
        while (!done) {
            LogHelper.log("Verifying:" + state);
            if (state.verify()) {
                LogHelper.log("Running:" + state);
                state.run();
                LogHelper.log("Finished:" + state);
            }
            state = state.nextState();
            LogHelper.log("Next state:" + state);
            done = state == null;
        }
    }
}
