package tasks;

import org.dreambot.api.methods.hint.HintArrow;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.script.TaskNode;
import state.ScriptState;
import state.TaskState;
import utils.*;

import static consts.Areas.miningInstructorArea;
import static consts.WidgetsValues.QuestWidgetChildFixed;
import static consts.WidgetsValues.TabWidgetParentFixedScreen;

enum QuestGuideState implements TaskState {
    TALK_TO_QUEST_GUIDE {
        @Override
        public Boolean run() {
            HintArrowHelper.interact("Quest Guide");
            DialogHelper.continueDialog();
            if (!Tabs.isOpen(Tab.QUEST)) {
                WidgetHelper widgetHelper = new WidgetHelper(new int[]{QuestWidgetChildFixed}, TabWidgetParentFixedScreen);
                widgetHelper.child().interact();
                SleepHelper.sleepUntil(() -> Tabs.isOpen(Tab.QUEST), 5000);
                SleepHelper.sleepUntil(HintArrow::exists, 5000, 500);
            }
            return true;
        }

        @Override
        public Boolean verify() {
            LogHelper.log("Verifying TALK_TO_QUEST_GUIDE");
            return HintArrowHelper.getName("Quest Guide").contains("Quest Guide");
        }

        @Override
        public TaskState previousState() {
            return null;
        }

        @Override
        public TaskState nextState() {
            if (HintArrowHelper.getName("Quest Guide").contains("Quest Guide")) {
                return TALK_TO_QUEST_GUIDE;
            }
            return CLIMB_DOWN_LADDER;
        }
    },
    CLIMB_DOWN_LADDER {
        @Override
        public Boolean run() {
            LogHelper.log("Climbing down ladder");
            HintArrowHelper.interact("Ladder", "climb-down");
            return true;
        }

        @Override
        public Boolean verify() {
            return HintArrowHelper.getName("Ladder").contains("Ladder");
        }

        @Override
        public TaskState previousState() {
            return TALK_TO_QUEST_GUIDE;
        }

        @Override
        public TaskState nextState() {
            return WALK_TO_MINING_INSTRUCTOR;
        }
    },
    WALK_TO_MINING_INSTRUCTOR {
        @Override
        public Boolean run() {
            LogHelper.log("Walking to mining instructor");
            WalkingHelper walkingHelper = new WalkingHelper(miningInstructorArea);
            walkingHelper.walk();
            return true;
        }

        @Override
        public Boolean verify() {
            Tile bottomOfLadder = new Tile(3088, 9520, 0);
            return bottomOfLadder.equals(Me.playerObjet().getTile());
        }

        @Override
        public TaskState previousState() {
            return CLIMB_DOWN_LADDER;
        }

        @Override
        public TaskState nextState() {
            return null;
        }
    };
}

public class QuestGuide extends TaskNode {

    @Override
    public boolean accept() {
        return ScriptState.get() == ScriptState.States.QUEST_GUIDE;
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
        ScriptState.set(ScriptState.States.MINING_INSTRUCTOR);
        return 1;
    }
}
