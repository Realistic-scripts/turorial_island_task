package tasks;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.hint.HintArrow;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.script.TaskNode;
import state.ScriptState;
import state.TaskState;
import utils.*;

import static consts.Areas.*;
import static consts.Items.*;
import static consts.NPCS.Rat;
import static consts.WidgetsValues.*;

enum CombatInstructorState implements TaskState {
    TALK_TO_COMBAT_INSTRUCTOR {
        @Override
        public Boolean run() {
            LogHelper.log("Running Talk to combat Instructor");
            HintArrowHelper.interact("Combat Instructor");
            DialogHelper.continueDialog();
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
            if (Inventory.containsAll(Shortbow, BronzeArrow) | Equipment.containsAll(Shortbow, BronzeArrow)) {
                return EQUIP_BOW;
            } else if (Inventory.containsAll(BronzeSword, WoodenShield)) {
                return EQUIP_SWORD;
            } else if (Equipment.containsAll(BronzeSword, WoodenShield)) {
                return KILL_RAT_MELEE;
            }
            return EQUIP_DAGGER;
        }
    },
    EQUIP_DAGGER {
        @Override
        public Boolean run() {
            Widgets.getWidget(TabWidgetParentFixedScreen).getChild(EquipmentWidgetChildFixed).interact();
            SleepHelper.sleepUntil(() -> Tabs.isOpen(Tab.EQUIPMENT), 5000);

            Widgets.getWidget(EquipmentParent).getChild(EquipmentStats).interact();
            SleepHelper.randomSleep(3000, 5000);
            SleepHelper.sleepUntil(() -> WidgetHelper.widgetExists(EquipParent), 5000);

            Inventory.interact(BronzeDagger, "Equip");
            SleepHelper.sleepUntil(() -> Equipment.fullSlotCount() == 1, 7000);

            Widgets.getWidget(EquipParent).getChild(EquipExitChild).getChild(EquipExitGrandChild).interact();
            SleepHelper.sleepUntil(() -> !WidgetHelper.widgetExists(EquipParent), 5000);
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
            Widgets.getWidget(TabWidgetParentFixedScreen).getChild(CombatWidgetChildFixed).interact();
            SleepHelper.sleepUntil(() -> Tabs.isOpen(Tab.COMBAT), 3000);

            // TODO mess around in this tab. Equip dagger and go back ect...
            return true;
        }

        @Override
        public Boolean verify() {
            return Inventory.containsAll(BronzeSword, WoodenShield) & !Equipment.contains(841) & !Inventory.contains(841);
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
        @Override
        public Boolean run() {
            LogHelper.log("Running: Killing rat melee");

            WalkingHelper outsideRatGateWalker = new WalkingHelper(RatGateAreaOutside);
            outsideRatGateWalker.walk();
            GameObjects.closest("Gate").interact();
            SleepHelper.sleepUntil(() -> RatGateAreaInside.contains(Me.playerObjet().getTile()), 3000);

            // TODO add some checking to see if someone is attacking your rat
            while (!Me.playerObjet().isInCombat()) {
                if (HintArrow.exists()) {
                    HintArrowHelper.interact("Giant rat");
                } else {
                    NPCs.closest("Giant Rat").interact();
                }
                SleepHelper.randomSleep(1000, 3000);
            }
            SleepHelper.sleepUntil(() -> !Me.playerObjet().isInCombat(), 120000);

            WalkingHelper insideRatGateWalker = new WalkingHelper(RatGateAreaInside);
            insideRatGateWalker.walk();
            GameObjects.closest("Gate").interact();
            SleepHelper.sleepUntil(() -> RatGateAreaOutside.contains(Me.playerObjet().getTile()), 3000);

            WalkingHelper combatInstructorWalking = new WalkingHelper(CombatInstructorArea);
            combatInstructorWalking.walk();
            return true;
        }

        @Override
        public Boolean verify() {
            WidgetHelper widget = new WidgetHelper(new int[]{ChatDialogChild, ChatDialogGrandChild}, ChatDialogParent);

            return (
                    (
                            HintArrow.exists() &
                                    (HintArrowHelper.getName("Gate").equals("") | HintArrowHelper.getName("Giant Rat").equals("Giant Rat"))
                    ) | widget.widgetContainsText("see a bar over your head") &
                            Equipment.containsAll(BronzeSword, WoodenShield)
            );
        }

        @Override
        public TaskState previousState() {
            return EQUIP_SWORD;
        }

        @Override
        public TaskState nextState() {
            return TALK_TO_COMBAT_INSTRUCTOR;
        }
    },
    EQUIP_BOW {
        @Override
        public Boolean run() {
            Tabs.openWithMouse(Tab.INVENTORY);
            SleepHelper.sleepUntil(() -> Tabs.isOpen(Tab.INVENTORY), 3000);

            Inventory.interact(Shortbow, "Wield");
            SleepHelper.sleepUntil(() -> !Equipment.contains(Shortbow), 5000);
            SleepHelper.randomSleep(800, 1500);

            Inventory.interact(BronzeArrow, "Wield");
            SleepHelper.sleepUntil(() -> Equipment.contains(BronzeArrow), 5000);

            SleepHelper.randomSleep(800, 1500);
            // TODO add messing around with the combat screen and equipment stats screen
            return true;
        }

        @Override
        public Boolean verify() {
            return Inventory.containsAll(Shortbow, BronzeArrow);
        }

        @Override
        public TaskState previousState() {
            return TALK_TO_COMBAT_INSTRUCTOR;
        }

        @Override
        public TaskState nextState() {
            return KILL_RAT_BOW;
        }
    },
    KILL_RAT_BOW {
        @Override
        public Boolean run() {
            while (!Me.playerObjet().isInCombat()) {
                NPCs.closest(Rat).interact();
                SleepHelper.randomSleep(1000, 3000);
            }
            SleepHelper.sleepUntil(() -> !Me.playerObjet().isInCombat(), 120000);
            WidgetHelper widget = new WidgetHelper(new int[]{ChatDialogChild, ChatDialogGrandChild}, ChatDialogParent);
            SleepHelper.sleepUntil(() -> widget.widgetContainsText("Moving on"), 10000);
            LogHelper.log("Done killing rat");
            return true;
        }

        @Override
        public Boolean verify() {
            return HintArrowHelper.getName("Giant rat").contains("Giant rat") & Equipment.containsAll(Shortbow, BronzeArrow);
        }

        @Override
        public TaskState previousState() {
            return TALK_TO_COMBAT_INSTRUCTOR;
        }

        @Override
        public TaskState nextState() {
            return WALK_TO_LADDER;
        }
    },
    WALK_TO_LADDER {
        @Override
        public Boolean run() {
            WalkingHelper endLadderArea = new WalkingHelper(CombatInstructorEndLadderArea);
            endLadderArea.walk();
            HintArrowHelper.interact("Ladder");
            return true;
        }

        @Override
        public Boolean verify() {
            WidgetHelper widget = new WidgetHelper(new int[]{ChatDialogChild, ChatDialogGrandChild}, ChatDialogParent);

            return widget.widgetContainsText("just talk to the combat instructor") & widget.widgetContainsText("Moving on");
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

public class CombatInstructor extends TaskNode {

    @Override
    public boolean accept() {
        return ScriptState.get() == ScriptState.States.COMBAT_INSTRUCTOR;
    }

    @Override
    public int execute() {
        log("Starting Combat Instructor");
        TaskState state = CombatInstructorState.TALK_TO_COMBAT_INSTRUCTOR;
        boolean done = false;
        while (!done) {
            if (state.verify()) {
                state.run();
            }
            state = state.nextState();
            done = state == null;
        }
        ScriptState.set(ScriptState.States.BANKING_TUTORIAL);
        return 1;
    }
}
