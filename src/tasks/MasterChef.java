package tasks;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.hint.HintArrow;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.wrappers.items.Item;
import org.dreambot.core.W;
import state.ScriptState;
import state.TaskState;
import utils.LogHelper;
import utils.Me;
import utils.NPCHelper;
import utils.SleepHelper;

enum MasterChefState implements TaskState {
    TALK_TO_CHEF {
        @Override
        public Boolean run() {
            if (!Dialogues.canContinue()) {
                HintArrow.getPointed().interact();
            }
            SleepHelper.sleepUntil(Dialogues::canContinue, 3000);
            while (Dialogues.canContinue()) {
                Dialogues.spaceToContinue();
                LogHelper.log(Dialogues.getNPCDialogue());
                SleepHelper.randomSleep(NPCHelper.timeToRead(Dialogues.getNPCDialogue()), 200);
            }
            return true;
        }

        @Override
        public Boolean verify() {
            LogHelper.log(HintArrow.getPointed());
            if (HintArrow.exists()) {
                try {
                    return HintArrow.getPointed().getName().contains("Master Chef");
                } catch (NullPointerException e) {
                    return false;
                }
            }

            return false;
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
            return null;
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
            Item breadDough = Inventory.get(2307);
            breadDough.interact();
            GameObjects.closest(9736).interact();
            return true;
        }

        @Override
        public Boolean verify() {
            return Inventory.contains(2307);
        }

        @Override
        public TaskState previousState() {
            return null;
        }

        @Override
        public TaskState nextState() {
            return WALK_TO;
        }
    },
    WALK_TO {
        Area questGuideArea = new Area(new Tile(3087, 3124), new Tile(3087, 3120),
                new Tile(3084, 3120), new Tile(3084, 3124), new Tile(3085, 3124),
                new Tile(3086, 3125));

        @Override
        public Boolean run() {
            while (!questGuideArea.contains(Me.playerObjet().getTile())){
                SleepHelper.sleepUntil(() -> Walking.walk(questGuideArea.getRandomTile()), 30000);
                SleepHelper.randomSleep(500, 1300);
            }
            return null;
        }

        @Override
        public Boolean verify() {
            return Inventory.contains(2309);
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
    ScriptState state;

    public MasterChef(ScriptState state) {
        this.state = state;
    }

    @Override
    public boolean accept() {
        return this.state.get() == ScriptState.States.MASTER_CHEF;
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
        this.state.set(ScriptState.States.QUEST_GUIDE);
        return 1;
    }
}
