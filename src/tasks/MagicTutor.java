package tasks;

import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.hint.HintArrow;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.script.TaskNode;
import state.ScriptState;
import state.TaskState;
import utils.HintArrowHelper;
import utils.LogHelper;
import utils.NPCHelper;
import utils.SleepHelper;

enum MagicTutorState implements TaskState {
    TALK_TO_MAGIC_TUTOR {
        @Override
        public Boolean run() {
            HintArrowHelper.interact("Magic Instructor");
            SleepHelper.sleepUntil(Dialogues::canContinue, 3000);
            while (Dialogues.canContinue()) {
                Dialogues.spaceToContinue();
                LogHelper.log(Dialogues.getNPCDialogue());
                SleepHelper.sleepRange(NPCHelper.timeToRead(Dialogues.getNPCDialogue()), 600);
                SleepHelper.randomSleep(300, 1000);
            }
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
//            Magic.castSpell(Normal.WIND_STRIKE);
//            SleepHelper.sleepUntil(Magic::isSpellSelected, 3000);
//            HintArrowHelper.interact("Chicken");
            SleepHelper.sleepUntil(() -> HintArrowHelper.getName("Magic Instructor").contains("Magic Instructor"), 3000);
            HintArrowHelper.interact("Magic Instructor");
            SleepHelper.sleepUntil(Dialogues::canContinue, 3000);
            while (Dialogues.canContinue()) {
                Dialogues.spaceToContinue();
                LogHelper.log(Dialogues.getNPCDialogue());
                SleepHelper.sleepRange(NPCHelper.timeToRead(Dialogues.getNPCDialogue()), 600);
                SleepHelper.randomSleep(300, 1000);
            }
            LogHelper.log(Dialogues.getOptions());
            Dialogues.clickOption(1);
            SleepHelper.randomSleep(1000, 3000);
            while (Dialogues.canContinue()) {
                Dialogues.spaceToContinue();
                LogHelper.log(Dialogues.getNPCDialogue());
                SleepHelper.sleepRange(NPCHelper.timeToRead(Dialogues.getNPCDialogue()), 600);
                SleepHelper.randomSleep(300, 1000);
            }
            // TODO Add asking about Iron man
            Dialogues.clickOption(3);
            SleepHelper.randomSleep(1000, 3000);
            while (Dialogues.canContinue()) {
                Dialogues.spaceToContinue();
                LogHelper.log(Dialogues.getNPCDialogue());
                SleepHelper.sleepRange(NPCHelper.timeToRead(Dialogues.getNPCDialogue()), 600);
                SleepHelper.randomSleep(300, 1000);
            }
            SleepHelper.randomSleep(10000, 20000);
            while (Dialogues.canContinue()) {
                Dialogues.spaceToContinue();
                LogHelper.log(Dialogues.getNPCDialogue());
                SleepHelper.sleepRange(NPCHelper.timeToRead(Dialogues.getNPCDialogue()), 600);
                SleepHelper.randomSleep(300, 1000);
            }
            return null;
        }

        @Override
        public Boolean verify() {
            return true;
//            return HintArrowHelper.getName("Chicken").contains("Chicken");
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
    ScriptState state;

    public MagicTutor(ScriptState state) {
        this.state = state;
    }

    @Override
    public boolean accept() {
        return this.state.get() == ScriptState.States.MAGIC_TUTOR;
    }

    @Override
    public int execute() {
        log("Starting Prayer Tutorial");
        TaskState state = MagicTutorState.KILL_CHICKEN;
        boolean done = false;
        while (!done) {
            if (state.verify()) {
                state.run();
            }
            state = state.nextState();
            done = state == null;
        }
        this.state.set(ScriptState.States.MAGIC_TUTOR);
        return -1;
    }
}
