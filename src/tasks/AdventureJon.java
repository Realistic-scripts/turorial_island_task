package tasks;

import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.script.TaskNode;
import state.ScriptState;
import state.TaskState;
import utils.DialogHelper;
import utils.Me;
import utils.SleepHelper;
import utils.TaskStateExecute;

import static consts.Areas.Lumbridge;

enum AdventureJonState implements TaskState {
    TALK_TO_ADVENTURE_JON {
        @Override
        public Boolean run() {
            SleepHelper.sleepUntil(Dialogues::canContinue, 10000);
            DialogHelper.continueDialog();
            Tabs.openWithMouse(Tab.QUEST);
            SleepHelper.sleepUntil(() -> Tabs.open(Tab.QUEST), 5000);
            // TODO add checking Adventure Paths
            return true;
        }

        @Override
        public Boolean verify() {
            return Lumbridge.contains(Me.playerObjet().getTile());
        }

        @Override
        public TaskState previousState() {
            return null;
        }

        @Override
        public TaskState nextState() {
            return null;
        }
    }
}

public class AdventureJon extends TaskNode {
    @Override
    public boolean accept() {
        return ScriptState.get() == ScriptState.States.ADVENTURE_JON;
    }

    @Override
    public int execute() {
        log("Starting: Adventure Jon");
        TaskState state = AdventureJonState.TALK_TO_ADVENTURE_JON;
        TaskStateExecute.taskStateExecute(state);
        log("Finished: Adventure Jon");
        ScriptState.set(ScriptState.States.MAGIC_TUTOR);
        return -1;
    }
}
