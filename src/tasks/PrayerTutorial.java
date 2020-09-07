package tasks;

import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.hint.HintArrow;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.script.TaskNode;
import state.ScriptState;
import state.TaskState;
import utils.*;

import static consts.Areas.WizardArea;
import static consts.WidgetsValues.*;

enum PrayerTutorialState implements TaskState {
    TALK_TO_MONK {
        @Override
        public Boolean run() {
            LogHelper.log("Running: TALK_TO_MONK");
            HintArrowHelper.interact("Brother Brace");
            SleepHelper.sleepUntil(Dialogues::canContinue, 3000);
            DialogHelper.continueDialog();

            SleepHelper.randomSleep(900, 1400);
            // TODO add some reading the prayers here
            Widgets.getWidget(TabWidgetParentFixedScreen).getChild(PrayerChildFixed).interact();
            SleepHelper.sleepUntil(() -> Tabs.isOpen(Tab.PRAYER), 5000);

            SleepHelper.sleepUntil(HintArrow::exists, 5000);
            SleepHelper.randomSleep(900, 1400);
            HintArrowHelper.interact("Brother Brace");
            DialogHelper.continueDialog();

            SleepHelper.randomSleep(900, 1400);
            Widgets.getWidget(TabWidgetParentFixedScreen).getChild(FriendsListChildFixed).interact();
            SleepHelper.sleepUntil(() -> Tabs.isOpen(Tab.FRIENDS), 5000);
            SleepHelper.sleepUntil(HintArrow::exists, 5000);

            SleepHelper.randomSleep(900, 1400);
            HintArrowHelper.interact("Brother Brace");
            DialogHelper.continueDialog();
            return true;
        }

        @Override
        public Boolean verify() {
            return HintArrowHelper.getName("Brother Brace").contains("Brother Brace");
        }

        @Override
        public TaskState previousState() {
            return null;
        }

        @Override
        public TaskState nextState() {
            return WALK_TO_MAGIC_INSTRUCTOR;
        }
    },
    WALK_TO_MAGIC_INSTRUCTOR {
        @Override
        public Boolean run() {
            WalkingHelper wizardArea = new WalkingHelper(WizardArea);
            wizardArea.walk();
            return true;
        }

        @Override
        public Boolean verify() {
            WidgetHelper widget = new WidgetHelper(new int[]{ChatDialogChild, ChatDialogGrandChild}, ChatDialogParent);
            return widget.widgetContainsText("Wizard") | widget.widgetContainsText("final instructor");
        }

        @Override
        public TaskState previousState() {
            return TALK_TO_MONK;
        }

        @Override
        public TaskState nextState() {
            return null;
        }
    };
}

public class PrayerTutorial extends TaskNode {

    @Override
    public boolean accept() {
        return ScriptState.get() == ScriptState.States.PRAYER_TUTORIAL;
    }

    @Override
    public int execute() {
        log("Starting Prayer Tutorial");
        TaskState state = PrayerTutorialState.TALK_TO_MONK;
        boolean done = false;
        while (!done) {
            if (state.verify()) {
                state.run();
            }
            state = state.nextState();
            done = state == null;
        }
        ScriptState.set(ScriptState.States.MAGIC_TUTOR);
        return 1;
    }
}
