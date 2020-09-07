package tasks;

import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.script.TaskNode;
import state.ScriptState;
import state.TaskState;
import utils.LogHelper;
import utils.SleepHelper;
import utils.WidgetHelper;

import static consts.WidgetsValues.*;

enum GielinorGuideState implements TaskState {
    PICK_NAME {
        @Override
        public Boolean run() {
            // TODO test this more.
            LogHelper.log("Running: PICK_NAME");
            if (Dialogues.canEnterInput()) {
                String name = "a";  //RandomUsernameGenerator.generate();
                Keyboard.type(name, true);
                SleepHelper.sleepUntil(() -> !Dialogues.canEnterInput(), 10000);
                SleepHelper.sleepUntil(() ->
                        Widgets.getWidget(ChooseDisplayNameParent).getChild(DisplayNameChild).getText().equals(name), 10000);
                LogHelper.log(Widgets.getWidget(ChooseDisplayNameParent).getChild(DisplayNameChild).getText());
            }
            SleepHelper.randomSleep(1500, 3000);
            boolean IsAvailable = Widgets.getWidget(ChooseDisplayNameParent).getChild(ValidNameChild).getText().contains("Great");
            if (IsAvailable) {
                Widgets.getWidget(ChooseDisplayNameParent).getChild(SetNameChild).interact();
                SleepHelper.sleepUntil(() -> WidgetHelper.widgetExists(ChooseDisplayNameParent), 10000);
                return true;
            }
            Widgets.getWidget(ChooseDisplayNameParent).getChild(ChooseDisplayNameChild).interact();
            SleepHelper.sleepUntil(Dialogues::canEnterInput, 10000);
            return true;
        }

        @Override
        public Boolean verify() {
            return WidgetHelper.widgetExists(ChooseDisplayNameParent);
        }

        @Override
        public TaskState previousState() {
            return null;
        }

        @Override
        public TaskState nextState() {
            if (WidgetHelper.widgetExists(ChooseDisplayNameParent)) {
                return PICK_NAME;
            }
            return PICK_APPEARANCE;
        }
    },
    PICK_APPEARANCE {
        @Override
        public Boolean run() {
            return null;
        }

        @Override
        public Boolean verify() {
            return WidgetHelper.widgetExists(PickAppearanceParent);
        }

        @Override
        public TaskState previousState() {
            return PICK_NAME;
        }

        @Override
        public TaskState nextState() {
            return null;
        }
    }
}

public class GielinorGuide extends TaskNode {
    ScriptState state;

    public GielinorGuide(ScriptState state) {
        this.state = state;
    }

    @Override
    public boolean accept() {
        return this.state.get() == ScriptState.States.GIELINOR_GUIDE;
    }

    @Override
    public int execute() {
        log("Starting Quest Guide");
        TaskState state = GielinorGuideState.PICK_NAME;
        boolean done = false;
        while (!done) {
            if (state.verify()) {
                state.run();
            }
            state = state.nextState();
            done = state == null;
        }
        this.state.set(ScriptState.States.SURVIVAL_TRAINING);
        return -1;
    }
}
