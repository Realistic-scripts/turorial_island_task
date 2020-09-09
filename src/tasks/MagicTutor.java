package tasks;

import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.hint.HintArrow;
import org.dreambot.api.methods.magic.Magic;
import org.dreambot.api.methods.magic.Normal;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.script.TaskNode;
import state.ScriptState;
import state.TaskState;
import utils.*;

import static consts.WidgetsValues.*;

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
            DialogHelper.continueDialog();
            SleepHelper.sleepUntil(() -> !Dialogues.canContinue(), 5000);
            WidgetHelper widget = new WidgetHelper(new int[]{ChatDialogChild, ChatDialogGrandChild}, ChatDialogParent);
            return HintArrowHelper.getName("Magic Instructor").contains("Magic Instructor") & !widget.widgetContainsText("To the mainland");
        }

        @Override
        public TaskState previousState() {
            return null;
        }

        @Override
        public TaskState nextState() {
            if (TO_THE_MAINLAND.verify()) {
                return TO_THE_MAINLAND;
            }
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
            WidgetHelper widget = new WidgetHelper(new int[]{ChatDialogChild, ChatDialogGrandChild}, ChatDialogParent);

            while (widget.widgetContainsText("Magic casting")) {
                Magic.castSpell(Normal.WIND_STRIKE);
                SleepHelper.sleepUntil(Magic::isSpellSelected, 3000);
                HintArrowHelper.interact("Chicken");
                SleepHelper.randomSleep(6000, 8000);
                DialogHelper.continueDialog();
                widget = new WidgetHelper(new int[]{ChatDialogChild, ChatDialogGrandChild}, ChatDialogParent);
            }
            SleepHelper.sleepUntil(() -> HintArrowHelper.getName("Magic Instructor").contains("Magic Instructor"), 3000);
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
            return TO_THE_MAINLAND;
        }
    },
    TO_THE_MAINLAND {
        @Override
        public Boolean run() {
            if (Magic.isSpellSelected()) {
                Tabs.openWithMouse(Tab.INVENTORY);
                SleepHelper.randomSleep(1000, 2000);
            }
            HintArrowHelper.interact("Magic Instructor");

            DialogHelper wizardDialog = new DialogHelper(new int[]{1, 3});
            wizardDialog.branchingDialog();
            // TODO Add asking about Iron man
            return true;
        }

        @Override
        public Boolean verify() {
            DialogHelper.continueDialog();
            SleepHelper.sleepUntil(() -> !Dialogues.canContinue(), 5000);
            WidgetHelper widget = new WidgetHelper(new int[]{ChatDialogChild, ChatDialogGrandChild}, ChatDialogParent);
            return widget.widgetContainsText("To the mainland");
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

public class MagicTutor extends TaskNode {

    @Override
    public boolean accept() {
        return ScriptState.get() == ScriptState.States.MAGIC_TUTOR;
    }

    @Override
    public int execute() {
        log("Starting: Magic Tutorial");
        TaskState state = MagicTutorState.TALK_TO_MAGIC_TUTOR;
        TaskStateExecute.taskStateExecute(state);
        log("Finished: Magic Tutorial");
        ScriptState.set(ScriptState.States.ADVENTURE_JON);
        return 1;
    }
}
