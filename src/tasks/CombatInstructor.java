package tasks;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.hint.HintArrow;
import org.dreambot.api.methods.interactive.GameObjects;
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

enum CombatInstructorState implements TaskState {
    TALK_TO_COMBAT_INSTRUCTOR {
        @Override
        public Boolean run() {
            LogHelper.log("Running Talk to combat Instructor");
            HintArrowHelper.interact("Combat Instructor");
            SleepHelper.sleepUntil(Dialogues::canContinue, 3000);
            while (Dialogues.canContinue()) {
                Dialogues.spaceToContinue();
                LogHelper.log(Dialogues.getNPCDialogue());
                SleepHelper.sleepRange(NPCHelper.timeToRead(Dialogues.getNPCDialogue()), 600);
            }
            return true;
        }

        @Override
        public Boolean verify() {
            return HintArrowHelper.getName("Combat Instructor").contains("Combat Instructor");
        }

        @Override
        public TaskState previousState() {
            return null;
        }

        @Override
        public TaskState nextState() {
            if (Inventory.containsAll(1277, 1171)) {
                return EQUIP_SWORD;
            } else if (Inventory.containsAll(841, 882)) {
                return KILL_RAT_BOW;
            } else if (Equipment.containsAll(1277, 1171)) {
                return KILL_RAT_MELEE;
            }
            return EQUIP_DAGGER;
        }
    },
    EQUIP_DAGGER {
        @Override
        public Boolean run() {
            Tabs.open(Tab.EQUIPMENT);
            SleepHelper.sleepUntil(() -> Tabs.isOpen(Tab.EQUIPMENT), 5000);
            Widgets.getWidget(387).getChild(2).interact();
            SleepHelper.randomSleep(1000, 3000);
//            SleepHelper.sleepUntil(() -> Widgets.getWidget(84).isVisible(), 5000);
            Inventory.interact(1205, "Wield");
            Widgets.getWidget(84).getChild(3).getChild(11).interact();
            // TODO figure out how to wait here until the widget is closed
//            SleepHelper.sleepUntil(() -> !Widgets.getWidget(84).isVisible(), 5000);
            return true;
        }

        @Override
        public Boolean verify() {
            return !HintArrow.exists() & Inventory.contains(1205) & !Inventory.contains(1171);
        }

        @Override
        public TaskState previousState() {
            return TALK_TO_COMBAT_INSTRUCTOR;
        }

        @Override
        public TaskState nextState() {
            return TALK_TO_COMBAT_INSTRUCTOR;
        }
    },
    EQUIP_SWORD {
        @Override
        public Boolean run() {
            Tabs.openWithMouse(Tab.INVENTORY);
            SleepHelper.sleepUntil(() -> Tabs.isOpen(Tab.INVENTORY), 3000);
            Inventory.interact(1277, "Wield");
            SleepHelper.sleepUntil(() -> !Equipment.isSlotEmpty(3), 5000);
            Inventory.interact(1171, "Wield");
            SleepHelper.sleepUntil(() -> !Equipment.isSlotEmpty(5), 5000);
            Widgets.getWidget(164).getChild(60).interact();
            SleepHelper.sleepUntil(() -> Tabs.isOpen(Tab.COMBAT), 3000);
            // TODO mess around in this tab. Equip dagger and go back ect...
            return true;
        }

        @Override
        public Boolean verify() {
            return Inventory.containsAll(1277, 1171);
        }

        @Override
        public TaskState previousState() {
            return null;
        }

        @Override
        public TaskState nextState() {
            return KILL_RAT_MELEE;
        }
    },
    KILL_RAT_MELEE {
        Area RatGateAreaOutside = new Area(new Tile(3113, 9519, 0), new Tile(3111, 9518, 0));
        Area RatGateAreaInside = new Area(new Tile(3110, 9519, 0), new Tile(3108, 9518, 0));

        @Override
        public Boolean run() {
            LogHelper.log("Running: Killing rat melee");
            while (!RatGateAreaOutside.contains(Me.playerObjet().getTile())) {
                SleepHelper.sleepUntil(() -> Walking.walk(RatGateAreaOutside.getRandomTile()), 30000);
                SleepHelper.randomSleep(500, 1300);
            }
            GameObjects.closest("Gate").interact();
            // TODO add some checking to see if someone is attacking your rat
            HintArrowHelper.interact("Giant rat");
            SleepHelper.sleepUntil(() -> Me.playerObjet().isInCombat(), 10000);
            SleepHelper.sleepUntil(() -> !Me.playerObjet().isInCombat(), 10000);
            while (!RatGateAreaInside.contains(Me.playerObjet().getTile())) {
                SleepHelper.sleepUntil(() -> Walking.walk(RatGateAreaInside.getRandomTile()), 30000);
                SleepHelper.randomSleep(500, 1300);
            }
            GameObjects.closest("Gate").interact();
            SleepHelper.sleepUntil(() -> RatGateAreaOutside.contains(Me.playerObjet().getTile()), 3000);
            while (!CombatInstructorArea.contains(Me.playerObjet().getTile())) {
                SleepHelper.sleepUntil(() -> Walking.walk(CombatInstructorArea.getRandomTile()), 30000);
                SleepHelper.randomSleep(500, 1300);
            }
            return null;
        }

        @Override
        public Boolean verify() {
            return true;
//            return HintArrow.exists() & HintArrowHelper.getName("Gate").equals("") & Equipment.containsAll(1277, 1171);
        }

        @Override
        public TaskState previousState() {
            return null;
        }

        @Override
        public TaskState nextState() {
            return null;
        }
    },
    KILL_RAT_BOW {
        @Override
        public Boolean run() {
            return null;
        }

        @Override
        public Boolean verify() {
            return null;
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
    Area CombatInstructorArea = new Area(new Tile(3104, 9509), new Tile(3107, 9509),
            new Tile(3107, 9508), new Tile(3105, 9508), new Tile(3105, 9505),
            new Tile(3102, 9505));
}

public class CombatInstructor extends TaskNode {
    ScriptState state;

    public CombatInstructor(ScriptState state) {
        this.state = state;
    }

    @Override
    public boolean accept() {
        return this.state.get() == ScriptState.States.COMBAT_INSTRUCTOR;
    }

    @Override
    public int execute() {
        log("Starting Quest Guide");
        TaskState state = CombatInstructorState.TALK_TO_COMBAT_INSTRUCTOR;
        boolean done = false;
        while (!done) {
            if (state.verify()) {
                state.run();
            }
            state = state.nextState();
            done = state == null;
        }
        this.state.set(ScriptState.States.COMBAT_INSTRUCTOR);
        return -1;
    }
}
