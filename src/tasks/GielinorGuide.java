package tasks;

import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.wrappers.widgets.Menu;
import state.ScriptState;
import state.TaskState;
import utils.*;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

import static consts.Areas.survivalExpertArea;
import static consts.DialogTrees.GielinorGuidePastPlayer;
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
            pickGender();
            int changeRounds = ThreadLocalRandom.current().nextInt(2, 6);
            boolean firstRound = true;
            for (int i = 0; i <= changeRounds; i++) {
                int AttributeSetToChange = ThreadLocalRandom.current().nextInt(0, AllAppearance.length);
                int[] AttributeList = AllAppearance[AttributeSetToChange];
                int TimesToSetAttributeList = ThreadLocalRandom.current().nextInt(0, AllAppearance.length * 2);
                for (int j = 0; j <= TimesToSetAttributeList; j++) {
                    int AttributeToChangeChildId;
                    if (firstRound) {
                        AttributeToChangeChildId = ThreadLocalRandom.current().nextInt(0, 2);
                    } else {
                        AttributeToChangeChildId = ThreadLocalRandom.current().nextInt(0, AttributeList.length);
                    }
                    AttributeToChangeChildId = AttributeList[AttributeToChangeChildId];
                    firstRound = false;
                    Rectangle widgetLocation = Widgets.getWidget(PickAppearanceParent).getChild(AttributeToChangeChildId).getRectangle();
                    if (!moveMouse(widgetLocation)) {
                        continue;
                    }
                    int TimesToClick = ThreadLocalRandom.current().nextInt(1, 6);
                    for (int k = 0; k <= TimesToClick; k++) {
                        Mouse.click();
                        SleepHelper.randomSleep(800, 3000);
                    }
                }
            }
            Widgets.getWidget(PickAppearanceParent).getChild(Accept).interact();
            SleepHelper.sleepUntil(() -> WidgetHelper.widgetExists(PickAppearanceParent), 5000);
            return true;
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
            return TALK_TO_GIELINOR_GUIDE;
        }

        private void pickGender() {
            int random_num = ThreadLocalRandom.current().nextInt(0, 1 + 1);
            Widgets.getWidget(PickAppearanceParent).getChild(Female);
            if (random_num != 0) {
                SleepHelper.randomSleep(1000, 6000);
                Widgets.getWidget(PickAppearanceParent).getChild(Male);
            }

        }

        private boolean moveMouse(Rectangle widgetLocation) {
            // TODO this still needs some work. It still misses some of the widgets
            widgetLocation.grow(-1, -1);
            SleepHelper.sleepUntil(() -> Mouse.move(widgetLocation), 1000);
            while (Menu.getCount() != 2) {
                widgetLocation.grow(-1, -1);
                SleepHelper.sleepUntil(() -> Mouse.move(widgetLocation), 1000);
                if (widgetLocation.height < 0 | widgetLocation.width < 0) {
                    return false;
                }
            }
            return true;
        }
    },
    TALK_TO_GIELINOR_GUIDE {
        @Override
        public Boolean run() {
            // TODO add some random looking around the room
            HintArrowHelper.interact("Gielinor Guide");
            DialogHelper dialogHelper = new DialogHelper(GielinorGuidePastPlayer);
            dialogHelper.branchingDialog();
            if (!Tabs.isOpen(Tab.OPTIONS)) {
                Widgets.getWidget(TabWidgetParentFixedScreen).getChild(SettingsWidgetChildFixed).interact();
                SleepHelper.sleepUntil(() -> Tabs.isOpen(Tab.OPTIONS), 5000, 400);
                // TODO add looking around the settings menu
            }
            return true;
        }

        @Override
        public Boolean verify() {
            return HintArrowHelper.getName("Gielinor Guide").contains("Gielinor Guide");
        }

        @Override
        public TaskState previousState() {
            return PICK_APPEARANCE;
        }

        @Override
        public TaskState nextState() {
            if (HintArrowHelper.getName("Gielinor Guide").contains("Gielinor Guide")) {
                return TALK_TO_GIELINOR_GUIDE;
            }
            return WALK_TO_SURVIVAL_GUIDE;
        }
    },
    WALK_TO_SURVIVAL_GUIDE {
        @Override
        public Boolean run() {
            LogHelper.log("Running: WALK_TO_SURVIVAL_GUIDE");
            WalkingHelper walkingHelper = new WalkingHelper(survivalExpertArea);
            walkingHelper.walk();
            return true;
        }

        @Override
        public Boolean verify() {
            WidgetHelper widget = new WidgetHelper(new int[]{ChatDialogChild, ChatDialogGrandChild}, ChatDialogParent);
            return widget.widgetContainsText("to meet your first") | widget.widgetContainsText("Moving around");
        }

        @Override
        public TaskState previousState() {
            return TALK_TO_GIELINOR_GUIDE;
        }

        @Override
        public TaskState nextState() {
            return null;
        }
    }
}

public class GielinorGuide extends TaskNode {

    @Override
    public boolean accept() {
        return ScriptState.get() == ScriptState.States.GIELINOR_GUIDE;
    }

    @Override
    public int execute() {
        log("Starting Gielinor Guide");
        log(ScriptState.get());
        TaskState state = GielinorGuideState.PICK_NAME;
        boolean done = false;
        while (!done) {
            if (state.verify()) {
                state.run();
            }
            state = state.nextState();
            done = state == null;
        }
        log("Finishing Gielinor Guide");
        ScriptState.set(ScriptState.States.SURVIVAL_TRAINING);
        log(ScriptState.get());
        return 0;
    }
}
