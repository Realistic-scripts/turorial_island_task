package tasks;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.hint.HintArrow;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.script.TaskNode;
import state.ScriptState;
import state.TaskState;
import utils.*;

import static consts.Areas.CombatInstructorArea;
import static consts.Areas.MiningGateArea;
import static consts.GameObjects.Anvil;
import static consts.GameObjects.Furnace;
import static consts.Items.*;
import static consts.WidgetsValues.Smithing;

enum MiningInstructorState implements TaskState {
    TALK_TO_MINING_GUIDE {
        @Override
        public Boolean run() {
            LogHelper.log("Talk to mining guide");
            HintArrowHelper.interact("Mining Instructor");
            DialogHelper.continueDialog();
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
            if (Inventory.contains(BronzeBar)) {
                return MAKE_DAGGER;
            } else if (Inventory.contains(BronzeDagger)) {
                return WALK_TO_COMBAT;
            }
            return MINE_ORE;
        }
    },
    MINE_ORE {
        @Override
        public Boolean run() {
            LogHelper.log("Run Mining ore");
            HintArrowHelper.interact("Rocks");
            SleepHelper.sleepUntil(() -> Inventory.contains(Tin), 10000);
            HintArrowHelper.interact("Rocks");
            SleepHelper.sleepUntil(() -> Inventory.contains(Copper), 10000);
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
            GameObjects.closest(Furnace).interact();
            SleepHelper.sleepUntil(() -> !Inventory.containsAll(Copper, Tin), 8000);
            SleepHelper.sleepUntil(HintArrow::exists, 10000, 1000);
            return null;
        }

        @Override
        public Boolean verify() {
            return Inventory.containsAll(Copper, Tin);
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
            InterfaceHelper interfaceHelper = new InterfaceHelper(InterfaceHelper.widgetIdList());
            GameObjects.closest(Anvil).interact();
            SleepHelper.sleepUntil(() -> WidgetHelper.widgetExists(Smithing), 12000);
            // TODO this is rough.
            for (int i = 0; i < 1; i++) {
                LogHelper.log(interfaceHelper.widgetDiff());
                interfaceHelper.interactWith("Dagger");
                SleepHelper.sleep(2000);
            }
            SleepHelper.sleepUntil(() -> Inventory.contains(BronzeDagger), 10000, 500);
            return true;
        }

        @Override
        public Boolean verify() {
            return Inventory.containsAll(BronzeBar, Hammer);
        }

        @Override
        public TaskState previousState() {
            return TALK_TO_MINING_GUIDE;
        }

        @Override
        public TaskState nextState() {
            return WALK_TO_COMBAT;
        }
    },
    WALK_TO_COMBAT {
        @Override
        public Boolean run() {
            WalkingHelper miningGameAreaWalking = new WalkingHelper(MiningGateArea);
            miningGameAreaWalking.walk();
            GameObjects.closest("Gate").interact();
            SleepHelper.sleepUntil(() -> !MiningGateArea.contains(Me.playerObjet().getTile()), 5000);
            WalkingHelper combatInstructorWalking = new WalkingHelper(CombatInstructorArea);
            combatInstructorWalking.walk();
            return true;
        }

        @Override
        public Boolean verify() {
            return Inventory.contains(BronzeDagger);
        }

        @Override
        public TaskState previousState() {
            return MAKE_DAGGER;
        }

        @Override
        public TaskState nextState() {
            return null;
        }
    };
}

public class MiningInstructor extends TaskNode {

    @Override
    public boolean accept() {
        return ScriptState.get() == ScriptState.States.MINING_INSTRUCTOR;
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
        ScriptState.set(ScriptState.States.COMBAT_INSTRUCTOR);
        return 1;
    }
}