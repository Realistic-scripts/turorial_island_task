package tasks;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.hint.HintArrow;
import org.dreambot.api.methods.input.Keyboard;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.map.Area;
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
            LogHelper.log("running fishing net");
            Tabs.openWithMouse(Tab.INVENTORY);
            SleepHelper.sleepUntil(Tab.INVENTORY::isOpen, 2000);
            return true;
        }

        @Override
        public Boolean verify() {
            // TODO check if there is a net in the inv
            LogHelper.log("Checking fishing net");
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
            HintArrowHelper.interact("running Fishing Spot");
            SleepHelper.sleepUntil(() -> Inventory.contains(2514), 5000);
            iteration++;
            return true;
        }

        @Override
        public Boolean verify() {
            LogHelper.log("Checking fishing spot");
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
                return CHOP_TREE;
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
            if (HintArrow.exists()) {
                if (HintArrowHelper.getName().contains("Tree")) {
                    return true;
                } else if (HintArrowHelper.getName().contains("Survival Expert") | Inventory.contains(2511)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public TaskState previousState() {
            return TALK_TO_EXPERT;
        }

        @Override
        public TaskState nextState() {
            return MAKE_FIRE;
        }
    },
    MAKE_FIRE {
        @Override
        public Boolean run() {
//
            LogHelper.log("Making a fire");
            Tabs.openWithMouse(Tab.INVENTORY);
            SleepHelper.sleepUntil(Tab.INVENTORY::isOpen, 2000);
            Tile tile = Me.cleanTile();
//            ClickMode.LEFT_CLICK;
            Walking.walkExact(tile);
            Item log = Inventory.get(2511);
            log.useOn(590);
            // TODO this needs to wait longer to check to see if it is done walking and making the fire
            SleepHelper.sleep(2000); // HACK Find a better way. Maybe add a wrapper that checks more then one walk animation in a row?
            SleepHelper.sleepUntil(() -> Me.playerObjet().getWalkAnimation() == 808 &
                    Me.playerObjet().getAnimation() == -1, 45000);
            LogHelper.log("done making fire");

            return true;
        }

        @Override
        public Boolean verify() {
            LogHelper.log("Checking make fire");
            return Inventory.containsAll(2511, 590) & GameObjects.closest(26185) == null;
        }

        @Override
        public TaskState previousState() {
            return CHOP_TREE;
        }

        @Override
        public TaskState nextState() {
            return COOK_SHRIMP;
        }
    },
    COOK_SHRIMP {
        boolean failed = false;

        @Override
        public Boolean run() {
            Tabs.openWithMouse(Tab.INVENTORY);
            SleepHelper.sleepUntil(Tab.INVENTORY::isOpen, 2000);
            LogHelper.log("Running cook shrimp");
            Item shrimp = Inventory.get(2514);
            // TODO fix this to use on fire.
            shrimp.interact();
            GameObjects.closest(26185).interact();
            if (!SleepHelper.sleepUntil(() -> Inventory.contains(315), 7000)) {
                failed = true;
            }
            return true;
        }

        @Override
        public Boolean verify() {
            LogHelper.log("Checking cook shrimp");
            return Inventory.contains(2514);
        }

        @Override
        public TaskState previousState() {
            return MAKE_FIRE;
        }

        @Override
        public TaskState nextState() {
            if (failed) {
                return FISH_SHRIMP;
            }
            return WALK_TO_CHEF;
        }
    },
    WALK_TO_CHEF {
        Area chefArea = new Area(new Tile(3075, 3083), new Tile(3075, 3085), new Tile(3078, 3085), new Tile(3078, 3083));

        @Override
        public Boolean run() {
            LogHelper.log("Run walk to chef");
            while (!chefArea.contains(Me.playerObjet().getTile())) {
                SleepHelper.sleepUntil(() -> Walking.walk(chefArea.getRandomTile()), 30000);
                SleepHelper.randomSleep(500, 1300);
            }
            return true;
        }

        @Override
        public Boolean verify() {
            LogHelper.log("Verify walk to chef");
            return true;
//            return HintArrowHelper.getName().contains("Gate");
        }

        @Override
        public TaskState previousState() {
            return COOK_SHRIMP;
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
        this.state.set(ScriptState.States.MASTER_CHEF);
        return 1;
    }
}
