package tasks;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.Shop;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.wrappers.widgets.Menu;
import state.ScriptState;
import state.TaskState;
import utils.HintArrowHelper;
import utils.LogHelper;
import utils.NPCHelper;
import utils.SleepHelper;

enum MiningInstructorState implements TaskState {
    TALK_TO_MINING_GUIDE {
        @Override
        public Boolean run() {
            LogHelper.log("Talk to mining guide");
            HintArrowHelper.interact("Mining Instructor");
            SleepHelper.sleepUntil(Dialogues::canContinue, 3000);
            while (Dialogues.canContinue()) {
                Dialogues.spaceToContinue();
                LogHelper.log(Dialogues.getNPCDialogue());
                SleepHelper.sleepRange(NPCHelper.timeToRead(Dialogues.getNPCDialogue()), 600);
            }
            Tabs.open(Tab.INVENTORY);
            SleepHelper.sleepUntil(() -> Tabs.isOpen(Tab.INVENTORY), 5000);
            return null;
        }

        @Override
        public Boolean verify() {
            return HintArrowHelper.getName().contains("Mining Instructor");
        }

        @Override
        public TaskState previousState() {
            return null;
        }

        @Override
        public TaskState nextState() {
            if (Inventory.contains(2347)) {
                return MAKE_DAGGER;
            }
            return MINE_ORE;
        }
    },
    MINE_ORE {
        @Override
        public Boolean run() {
            LogHelper.log("Run Mining ore");
            HintArrowHelper.interact("Rocks");
            SleepHelper.sleepUntil(() -> Inventory.contains(438), 6000);
            HintArrowHelper.interact("Rocks");
            SleepHelper.sleepUntil(() -> Inventory.contains(436), 6000);
            return true;
        }

        @Override
        public Boolean verify() {
            return HintArrowHelper.getName("Rocks").contains("Rocks");
        }

        @Override
        public TaskState previousState() {
            return TALK_TO_MINING_GUIDE;
        }

        @Override
        public TaskState nextState() {
            return SMELT_FURNACE;
        }
    },
    SMELT_FURNACE {
        @Override
        public Boolean run() {
            LogHelper.log("Running smelt Furnace");
            GameObjects.closest(10082).interact();
            SleepHelper.sleepUntil(() -> !Inventory.containsAll(438, 436), 8000);
            return null;
        }

        @Override
        public Boolean verify() {
            return Inventory.containsAll(438, 436);
        }

        @Override
        public TaskState previousState() {
            return null;
        }

        @Override
        public TaskState nextState() {
            return TALK_TO_MINING_GUIDE;
        }
    },
    MAKE_DAGGER {
        @Override
        public Boolean run() {
            LogHelper.log("Running Make dagger");
//            HintArrowHelper.interact("Anvil");
            LogHelper.log(Shop.isOpen());
            return true;
        }

        @Override
        public Boolean verify() {
            return Inventory.containsAll(2349, 2347);
        }

        @Override
        public TaskState previousState() {
            return TALK_TO_MINING_GUIDE;
        }

        @Override
        public TaskState nextState() {
            return null;
        }
    };
}

public class MiningInstructor extends TaskNode {
    ScriptState state;

    public MiningInstructor(ScriptState state) {
        this.state = state;
    }

    @Override
    public boolean accept() {
        return this.state.get() == ScriptState.States.MINING_INSTRUCTOR;
    }

    @Override
    public int execute() {
        log("Starting Mining Instructor");
        TaskState state = MiningInstructorState.TALK_TO_MINING_GUIDE;
        boolean done = false;
        while (!done) {
            if (state.verify()) {
                state.run();
            }
            state = state.nextState();
            done = state == null;
        }
        this.state.set(ScriptState.States.MINING_INSTRUCTOR);
        return -1;
    }
}