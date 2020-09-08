package tasks;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.hint.HintArrow;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.wrappers.items.Item;
import state.ScriptState;
import state.TaskState;
import utils.*;

import static consts.Areas.chefArea;
import static consts.Items.*;
import static consts.WidgetsValues.*;

// TODO add storing state in file
enum SurvivalTrainingState implements TaskState {
    TALK_TO_EXPERT {
        @Override
        public Boolean run() {
            HintArrowHelper.interact("Survival Expert");
            DialogHelper.continueDialog();
            SleepHelper.sleepUntil(HintArrow::exists, 1000);
            return true;
        }

        @Override
        public Boolean verify() {
            return HintArrowHelper.getName().contains("Survival Expert");
        }

        @Override
        public TaskState previousState() {
            return null;
        }

        @Override
        public TaskState nextState() {
            if (Inventory.contains(CookedShrimp)) {
                return WALK_TO_CHEF;
            } else if (Inventory.contains(Hatchet)) {
                return CHOP_TREE;
            }
            return FISHING_NET;
        }
    },
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
            WidgetHelper widget = new WidgetHelper(new int[]{ChatDialogChild, ChatDialogGrandChild}, ChatDialogParent);
            return widget.widgetContainsText("To view the item");
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
        @Override
        public Boolean run() {
            HintArrowHelper.interact("running Fishing Spot");
            SleepHelper.sleepUntil(() -> Inventory.contains(RawShrimp), 10000);
            DialogHelper.continueDialog();
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
            if (Inventory.contains(TinderBox)) {
                return CHOP_TREE;
            }
            return CHECK_SKILLS;
        }
    },
    CHECK_SKILLS {
        @Override
        public Boolean run() {
            Widgets.getWidget(TabWidgetParentFixedScreen).getChild(SkillWidgetChildFixed).interact();
            SleepHelper.sleepUntil(Tab.SKILLS::isOpen, 2000);
            return true;
        }

        @Override
        public Boolean verify() {
            WidgetHelper widget = new WidgetHelper(new int[]{ChatDialogChild, ChatDialogGrandChild}, ChatDialogParent);
            return widget.widgetContainsText("gained some experience");
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
    CHOP_TREE {
        @Override
        public Boolean run() {
            GameObjects.closest("Tree").interact();
            SleepHelper.sleepUntil(() -> Inventory.contains(Log), 15000);
            DialogHelper.continueDialog();
            return true;
        }

        @Override
        public Boolean verify() {
            return Inventory.containsAll(RawShrimp, TinderBox);
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
            LogHelper.log("Making a fire");
            Tabs.openWithMouse(Tab.INVENTORY);
            SleepHelper.sleepUntil(Tab.INVENTORY::isOpen, 2000);
            Tile tile = Me.cleanTile();
            Walking.walkExact(tile);
            Item log = Inventory.get(Log);
            log.useOn(TinderBox);
            // TODO this needs to wait longer to check to see if it is done walking and making the fire
            SleepHelper.sleep(2000); // HACK Find a better way. Maybe add a wrapper that checks more then one walk animation in a row?
            SleepHelper.sleepUntil(() -> Me.playerObjet().getWalkAnimation() == 808 &
                    Me.playerObjet().getAnimation() == -1, 45000);
            DialogHelper.continueDialog();
            LogHelper.log("done making fire");
            return true;
        }

        @Override
        public Boolean verify() {
            LogHelper.log("Checking make fire");
            return Inventory.containsAll(Log, TinderBox) & GameObjects.closest(Fire) == null;
        }

        @Override
        public TaskState previousState() {
            return CHOP_TREE;
        }

        @Override
        public TaskState nextState() {
            if (!Inventory.contains(RawShrimp)) {
                return FISH_SHRIMP;
            }
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
            if (!SleepHelper.sleepUntil(() -> Inventory.contains(CookedShrimp), 10000)) {
                failed = true;
            }
            DialogHelper.continueDialog();
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
        @Override
        public Boolean run() {
            LogHelper.log("Run walk to chef");
            WalkingHelper walkingHelper = new WalkingHelper(chefArea);
            walkingHelper.walk();
            return true;
        }

        @Override
        public Boolean verify() {
            LogHelper.log("Verify walk to chef");
            WidgetHelper widget = new WidgetHelper(new int[]{ChatDialogChild, ChatDialogGrandChild}, ChatDialogParent);
            return widget.widgetContainsText("Chef") | widget.widgetContainsText("just cooked your first meal");
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


    @Override
    public boolean accept() {
        return ScriptState.get() == ScriptState.States.SURVIVAL_TRAINING;
    }

    @Override
    public int execute() {
        log("Starting survival training");
        TaskState state = SurvivalTrainingState.TALK_TO_EXPERT;
        boolean done = false;
        while (!done) {
            if (state.verify()) {
                state.run();
            }
            state = state.nextState();
            done = state == null;
        }
        ScriptState.set(ScriptState.States.MASTER_CHEF);
        return 1;
    }
}
