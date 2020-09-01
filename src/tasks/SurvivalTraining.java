package tasks;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.hint.HintArrow;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.script.TaskNode;
import state.State;
import state.TaskState;
import utils.NPCHelper;
import utils.SleepHelper;

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
            HintArrow.getPointed().interact();
            SleepHelper.sleepUntil(() -> Inventory.contains(2514), 5000);
            iteration++;
            return true;
        }

        @Override
        public Boolean verify() {
            return HintArrow.getPointed() != null & HintArrow.getPointed().getName().contains("Fishing spot");
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
            return HintArrow.getPointed() != null & HintArrow.getPointed().getName().contains("Survival Expert");
        }

        @Override
        public TaskState previousState() {
            return CHECK_SKILLS;
        }

        @Override
        public TaskState nextState() {
            return null;
        }
    };

}

public class SurvivalTraining extends TaskNode {
    State state;

    public SurvivalTraining(State state) {
        this.state = state;
    }

    @Override
    public boolean accept() {
        return this.state.equals(State.SURVIVAL_TRAINING);
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
