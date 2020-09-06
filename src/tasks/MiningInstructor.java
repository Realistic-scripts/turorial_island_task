package tasks;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.TaskNode;
import state.ScriptState;
import state.TaskState;
import utils.*;

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
            InterfaceHelper interfaceHelper = new InterfaceHelper(InterfaceHelper.widgetIdList());
            for (int i = 0; i < 1; i++) {
                LogHelper.log(interfaceHelper.widgetDiff());
                interfaceHelper.interactWith("Dagger");
                SleepHelper.sleep(2000);
            }
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
            return WALK_TO_COMBAT;
        }
    },
    WALK_TO_COMBAT {
        Area GateArea = new Area(new Tile(3094, 9503),  new Tile(3093, 9502));
        Area CombatInstructorArea = new Area(new Tile(3104, 9509), new Tile(3107, 9509),
                new Tile(3107, 9508), new Tile(3105, 9508), new Tile(3105, 9505),
                new Tile(3102, 9505));

        @Override
        public Boolean run() {
            while (!GateArea.contains(Me.playerObjet().getTile())) {
                SleepHelper.sleepUntil(() -> Walking.walk(GateArea.getRandomTile()), 30000);
                SleepHelper.randomSleep(500, 1300);
            }
            GameObjects.closest("Gate").interact();
            SleepHelper.sleepUntil(() -> !GateArea.contains(Me.playerObjet().getTile()), 5000);
            while (!CombatInstructorArea.contains(Me.playerObjet().getTile()))
            {
                SleepHelper.sleepUntil(() -> Walking.walk(CombatInstructorArea.getRandomTile()), 30000);
                SleepHelper.randomSleep(500, 1300);
            }
            return true;
        }

        @Override
        public Boolean verify() {
            return Inventory.contains(1205);
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
        this.state.set(ScriptState.States.COMBAT_INSTRUCTOR);
        return 1;
    }
}