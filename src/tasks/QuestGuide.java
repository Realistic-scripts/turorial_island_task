package tasks;

import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.hint.HintArrow;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.script.TaskNode;
import state.ScriptState;
import state.TaskState;
import utils.HintArrowHelper;
import utils.LogHelper;
import utils.NPCHelper;
import utils.SleepHelper;

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
        this.state.set(ScriptState.States.QUEST_GUIDE);
        return -1;
    }
}
