package tasks;

import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.hint.HintArrow;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.TaskNode;
import state.ScriptState;
import state.TaskState;
import utils.*;

enum QuestGuideState implements TaskState {
    TALK_TO_QUEST_GUIDE {
        @Override
        public Boolean run() {
            LogHelper.log("Running TALK_TO_QUEST_GUIDE");
            if (!Dialogues.canContinue()) {
                LogHelper.log("Clicking on quest guide");
                NPCs.closest(3312).interact();
            }
            SleepHelper.sleepUntil(Dialogues::canContinue, 3000);
            while (Dialogues.canContinue()) {
                Dialogues.spaceToContinue();
                LogHelper.log(Dialogues.getNPCDialogue());
                SleepHelper.sleepRange(NPCHelper.timeToRead(Dialogues.getNPCDialogue()), 600);
            }
            Tabs.open(Tab.QUEST);
            SleepHelper.sleepUntil(() -> Tabs.isOpen(Tab.QUEST), 5000);
            return true;
        }

        @Override
        public Boolean verify() {
            LogHelper.log("Verifying TALK_TO_QUEST_GUIDE");
            if (HintArrow.exists()) {
                try {
                    return HintArrow.getPointed().getName().contains("Quest Guide");
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
            if (HintArrow.exists()) {
                try {
                    if (HintArrow.getPointed().getName().contains("Quest Guide")) {
                        LogHelper.log("Recursive TALK_TO_QUEST_GUIDE call");
                        return TALK_TO_QUEST_GUIDE;
                    }
                } catch (NullPointerException e) {
                    return CLIMB_DOWN_LADDER;
                }
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
            if (HintArrow.exists()) {
                try {
                    return HintArrowHelper.getName("Ladder").contains("Ladder");
                } catch (NullPointerException e) {
                    return false;
                }
            }
            return false;
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
        Area miningInstructorArea = new Area(new Tile(3084, 9504), new Tile(3084, 9508), new Tile(3080, 9508), new Tile(3080, 9504));

        @Override
        public Boolean run() {
            while (!miningInstructorArea.contains(Me.playerObjet().getTile())) {
                SleepHelper.sleepUntil(() -> Walking.walk(miningInstructorArea.getRandomTile()), 30000);
                SleepHelper.randomSleep(500, 1300);
            }
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
    ScriptState state;

    public QuestGuide(ScriptState state) {
        this.state = state;
    }

    @Override
    public boolean accept() {
        return this.state.get() == ScriptState.States.QUEST_GUIDE;
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
        this.state.set(ScriptState.States.MINING_INSTRUCTOR);
        return 1;
    }
}
