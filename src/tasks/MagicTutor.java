package tasks;

import org.dreambot.api.methods.hint.HintArrow;
import org.dreambot.api.methods.magic.Magic;
import org.dreambot.api.methods.magic.Normal;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.script.TaskNode;
import state.ScriptState;
import state.TaskState;
import utils.DialogHelper;
import utils.HintArrowHelper;
import utils.LogHelper;
import utils.SleepHelper;

enum MagicTutorState implements TaskState {
    TALK_TO_MAGIC_TUTOR {
        @Override
        public Boolean run() {
            HintArrowHelper.interact("Magic Instructor");
            DialogHelper.continueDialog();
            return true;
        }

        @Override
        public Boolean verify() {
            return HintArrowHelper.getName("Magic Instructor").contains("Magic Instructor");
        }

        @Override
        public TaskState previousState() {
            return null;
        }

        @Override
        public TaskState nextState() {
            if (HintArrowHelper.getName("Chicken").contains("Chicken")) {
                return KILL_CHICKEN;
            }
            return MAGIC_TAB;
        }
    },
    MAGIC_TAB {
        @Override
        public Boolean run() {
            Tabs.openWithMouse(Tab.MAGIC);
            SleepHelper.sleepUntil(() -> Tabs.isOpen(Tab.MAGIC), 5000);
            SleepHelper.randomSleep(1000, 4000);
            return true;
        }

        @Override
        public Boolean verify() {
            return !HintArrow.exists();
        }

        @Override
        public TaskState previousState() {
            return null;
        }

        @Override
        public TaskState nextState() {
            return TALK_TO_MAGIC_TUTOR;
        }
    },
    KILL_CHICKEN {
        @Override
        public Boolean run() {
            LogHelper.log("Running: KILL_CHICKEN");
            Magic.castSpell(Normal.WIND_STRIKE);
            SleepHelper.sleepUntil(Magic::isSpellSelected, 3000);
            HintArrowHelper.interact("Chicken");
            SleepHelper.randomSleep(4000, 6000);

            SleepHelper.sleepUntil(() -> HintArrowHelper.getName("Magic Instructor").contains("Magic Instructor"), 3000);
            HintArrowHelper.interact("Magic Instructor");

            DialogHelper wizardDialog = new DialogHelper(new int[]{1, 3});
            wizardDialog.branchingDialog();
            // TODO Add asking about Iron man
            return null;
        }

        @Override
        public Boolean verify() {
            return HintArrowHelper.getName("Chicken").contains("Chicken");
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

public class MagicTutor extends TaskNode {

    @Override
    public boolean accept() {
        return ScriptState.get() == ScriptState.States.MAGIC_TUTOR;
    }

    @Override
    public int execute() {
        log("Starting Prayer Tutorial");
        TaskState state = MagicTutorState.TALK_TO_MAGIC_TUTOR;
        boolean done = false;
        while (!done) {
            if (state.verify()) {
                state.run();
            }
            state = state.nextState();
            done = state == null;
        }
        ScriptState.set(ScriptState.States.MAGIC_TUTOR);
        return -1;
    }
}
