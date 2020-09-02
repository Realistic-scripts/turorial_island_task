package tasks;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.hint.HintArrow;
import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.wrappers.items.Item;
import state.ScriptState;
import state.TaskState;
import utils.*;

// TODO add storing state in file
enum SurvivalTrainingState implements TaskState {
    FISHING_NET {
        @Override
        public Boolean run() {
            Tabs.openWithMouse(Tab.INVENTORY);
            SleepHelper.sleepUntil(Tab.INVENTORY::isOpen, 2000);
            return true;
        }

        @Override
        public Boolean verify() {
            return true;
        }

        @Override
        public SurvivalTrainingState previousState() {
            return null;
        }

        @Override
        public SurvivalTrainingState nextState() {
            return FISH_SHRIMP;
        }
    },
    FISH_SHRIMP {
        int iteration = 0;

        @Override
        public Boolean run() {
            HintArrowHelper.interact("Fishing Spot");
            SleepHelper.sleepUntil(() -> Inventory.contains(2514), 5000);
            iteration++;
            return true;
        }

        @Override
        public Boolean verify() {
            LogHelper.log(HintArrow.exists());
            return HintArrowHelper.getName().contains("Fishing spot");
        }

        @Override
        public SurvivalTrainingState previousState() {
            return FISHING_NET;
        }

        @Override
        public SurvivalTrainingState nextState() {
            if (iteration > 0) {
                return null;
            }
            return CHECK_SKILLS;
        }
    },
    CHECK_SKILLS {
        @Override
        public Boolean run() {
            Tabs.openWithMouse(Tab.SKILLS);
            SleepHelper.sleepUntil(Tab.SKILLS::isOpen, 2000);
            return true;
        }

        @Override
        public Boolean verify() {
            return true;
        }

        @Override
        public SurvivalTrainingState previousState() {
            return FISH_SHRIMP;
        }

        @Override
        public SurvivalTrainingState nextState() {
            return TALK_TO_EXPERT;
        }
    },
    TALK_TO_EXPERT {
        @Override
        public Boolean run() {
            NPCHelper survivalExpertNPC = new NPCHelper(8503, new Integer[]{});
            SleepHelper.sleepUntil(survivalExpertNPC::talkTo, 30000);
            return true;
        }

        @Override
        public Boolean verify() {
            return HintArrowHelper.getName().contains("Survival Expert");
        }

        @Override
        public TaskState previousState() {
            return CHECK_SKILLS;
        }

        @Override
        public TaskState nextState() {
            return CHOP_TREE;
        }
    },
    CHOP_TREE {
        @Override
        public Boolean run() {
            HintArrowHelper.interact("Tree");
            SleepHelper.sleepUntil(() -> Inventory.contains(2511), 15000);
            Keyboard.type(" ", false);
            return true;
        }

        @Override
        public Boolean verify() {
            return HintArrowHelper.getName().contains("Tree");
        }

        @Override
        public TaskState previousState() {
            return null;
        }

        @Override
        public TaskState nextState() {
            return MAKE_FIRE;
        }
    },
    MAKE_FIRE {
        @Override
        public Boolean run() {
            LogHelper.log("Making a fire");
            Tabs.openWithMouse(Tab.INVENTORY);
            Tile tile = Me.cleanTile();
            Walking.walkExact(tile);
            Item log = Inventory.get(2511);
            log.useOn(590);
            int logCount = 0;
//            while(logCount< 1000){
//                LogHelper.log(Me.playerObjet().getWalkAnimation());
//                SleepHelper.sleep(100);
//                logCount++;
//            }
            // TODO this needs to wait longer to check to see if it is done walking and making the fire
            SleepHelper.sleep(1000); // HACK Find a better way. Maybe add a wrapper that checks more then one walk animation in a row?
            SleepHelper.sleepUntil(() -> Me.playerObjet().getWalkAnimation() == 808 &
                    Me.playerObjet().getAnimation() == -1, 45000);
            LogHelper.log("done making fire");
            return true;
        }

        @Override
        public Boolean verify() {
            return Inventory.containsAll(2511, 590);
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

public class SurvivalTraining extends TaskNode {
    ScriptState state;

    public SurvivalTraining(ScriptState state) {
        this.state = state;
    }

    @Override
    public boolean accept() {
        return this.state.get() == ScriptState.States.SURVIVAL_TRAINING;
    }

    @Override
    public int execute() {
        log("Starting survival training");
        TaskState state = SurvivalTrainingState.FISHING_NET;
        boolean done = false;
        while (!done) {
            if (state.verify()) {
                state.run();
            }
            state = state.nextState();
            done = state == null;
        }
        return -1;
    }
}
