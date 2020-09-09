package tasks;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.input.Camera;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.items.Item;
import state.ScriptState;
import state.TaskState;
import utils.DialogHelper;
import utils.HintArrowHelper;
import utils.SleepHelper;
import utils.WalkingHelper;

import static consts.Areas.questGuideArea;
import static consts.GameObjects.Range;
import static consts.Items.Bread;
import static consts.Items.BreadDough;

enum MasterChefState implements TaskState {
    TALK_TO_CHEF {
        @Override
        public Boolean run() {
            if (!Dialogues.canContinue()) {
                HintArrowHelper.interact("Master Chef");
            }
            DialogHelper.continueDialog();
            return true;
        }

        @Override
        public Boolean verify() {
            return HintArrowHelper.getName("Master Chef").contains("Master Chef");
        }

        @Override
        public TaskState previousState() {
            return null;
        }

        @Override
        public TaskState nextState() {
            return MAKE_BREAD_DOUGH;
        }
    },
    MAKE_BREAD_DOUGH {
        @Override
        public Boolean run() {
            if (!Tabs.isOpen(Tab.INVENTORY)) {
                SleepHelper.sleepUntil(() -> Tabs.openWithMouse(Tab.INVENTORY), 4000);
            }
            Item flour = Inventory.get(2516);
            flour.useOn(1929);
            SleepHelper.sleepUntil(() -> !Inventory.containsAll(2516, 1929), 5000);
            return true;
        }

        @Override
        public Boolean verify() {
            return Inventory.containsAll(2516, 1929);
        }

        @Override
        public TaskState previousState() {
            return TALK_TO_CHEF;
        }

        @Override
        public TaskState nextState() {
            return COOK_BREAD;
        }
    },
    COOK_BREAD {
        @Override
        public Boolean run() {
            // TODO turn camera to make it seem more realistic
            Item breadDough = Inventory.get(BreadDough);
            breadDough.interact();
            GameObject range = GameObjects.closest(Range);
            Camera.keyboardRotateToEntity(range);
            Camera.rotateToPitch(383);
            GameObjects.closest(Range).interact();
            SleepHelper.sleepUntil(() -> Inventory.contains(Bread), 8000);
            return true;
        }

        @Override
        public Boolean verify() {
            return Inventory.contains(BreadDough);
        }

        @Override
        public TaskState previousState() {
            return MAKE_BREAD_DOUGH;
        }

        @Override
        public TaskState nextState() {
            return WALK_TO;
        }
    },
    WALK_TO {
        @Override
        public Boolean run() {
            WalkingHelper walkingHelper = new WalkingHelper(questGuideArea);
            walkingHelper.walk();
            return true;
        }

        @Override
        public Boolean verify() {
            return Inventory.contains(Bread);
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

    @Override
    public boolean accept() {
        return ScriptState.get() == ScriptState.States.MASTER_CHEF;
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
        ScriptState.set(ScriptState.States.QUEST_GUIDE);
        return -1;
    }
}
