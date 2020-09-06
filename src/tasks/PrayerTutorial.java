package tasks;

import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.walking.impl.Walking;

import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.script.TaskNode;
import state.ScriptState;
import state.TaskState;
import utils.*;

enum PrayerTutorialState implements TaskState{
    TALK_TO_MONK{
        @Override
        public Boolean run() {
            LogHelper.log("Running: TALK_TO_MONK");
            HintArrowHelper.interact("Brother Brace");
            SleepHelper.sleepUntil(Dialogues::canContinue, 3000);
            while (Dialogues.canContinue()) {
                Dialogues.spaceToContinue();
                LogHelper.log(Dialogues.getNPCDialogue());
                SleepHelper.sleepRange(NPCHelper.timeToRead(Dialogues.getNPCDialogue()), 600);
                SleepHelper.randomSleep(300,1000);
            }
            // TODO add some reading the prayers here
            Tabs.openWithMouse(Tab.PRAYER);
            SleepHelper.randomSleep(1500, 4000);
            HintArrowHelper.interact("Brother Brace");
            SleepHelper.sleepUntil(Dialogues::canContinue, 3000);
            while (Dialogues.canContinue()) {
                Dialogues.spaceToContinue();
                LogHelper.log(Dialogues.getNPCDialogue());
                SleepHelper.sleepRange(NPCHelper.timeToRead(Dialogues.getNPCDialogue()), 600);
                SleepHelper.randomSleep(300,1000);
            }
            Tabs.openWithMouse(Tab.FRIENDS);
            HintArrowHelper.interact("Brother Brace");
            SleepHelper.sleepUntil(Dialogues::canContinue, 3000);
            while (Dialogues.canContinue()) {
                Dialogues.spaceToContinue();
                LogHelper.log(Dialogues.getNPCDialogue());
                SleepHelper.sleepRange(NPCHelper.timeToRead(Dialogues.getNPCDialogue()), 600);
                SleepHelper.randomSleep(300,1000);
            }
            return true;
        }

        @Override
        public Boolean verify() {
            return HintArrowHelper.getName("Brother Brace").contains("Brother Brace");
        }

        @Override
        public TaskState previousState() {
            return null;
        }

        @Override
        public TaskState nextState() {
            return WALK_TO_MAGIC_INSTRUCTOR;
        }
    },
    WALK_TO_MAGIC_INSTRUCTOR {
        Area WizardArea = new Area(new Tile(3142,3090,0), new Tile(3140,3088));
        @Override
        public Boolean run() {
            while (!WizardArea.contains(Me.playerObjet().getTile())) {
                SleepHelper.sleepUntil(() -> Walking.walk(WizardArea.getRandomTile()), 30000);
                SleepHelper.randomSleep(500, 1300);
            }
            return null;
        }

        @Override
        public Boolean verify() {
            return Widgets.getWidget(263).getChild(1).getChild(0).getText().contains("wizard");
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

public class PrayerTutorial extends TaskNode {
    ScriptState state;

    public PrayerTutorial(ScriptState state) {
        this.state = state;
    }

    @Override
    public boolean accept() {
        return this.state.get() == ScriptState.States.PRAYER_TUTORIAL;
    }

    @Override
    public int execute() {
        log("Starting Prayer Tutorial");
        TaskState state = PrayerTutorialState.TALK_TO_MONK;
        boolean done = false;
        while (!done) {
            if (state.verify()) {
                state.run();
            }
            state = state.nextState();
            done = state == null;
        }
        this.state.set(ScriptState.States.MAGIC_TUTOR);
        return 1;
    }
}
