package tasks;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.dialogues.Dialogues;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.map.Area;
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

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


enum BankingTutorialState implements TaskState {
    WALK_TO_BANK {
        Area BankArea = new Area(new Tile(3122, 3123, 0), new Tile(3120, 3121, 0));

        @Override
        public Boolean run() {
            while (!BankArea.contains(Me.playerObjet().getTile())) {
                SleepHelper.sleepUntil(() -> Walking.walk(BankArea.getRandomTile()), 30000);
                SleepHelper.randomSleep(500, 1300);
            }
            HintArrowHelper.interact("Bank booth");
            return null;
        }

        @Override
        public Boolean verify() {
            return HintArrowHelper.getName("Bank booth").contains("Bank booth");
        }

        @Override
        public TaskState previousState() {
            return null;
        }

        @Override
        public TaskState nextState() {
            return BANK_ITEMS;
        }
    },
    BANK_ITEMS {
        @Override
        public Boolean run() {
            LogHelper.log("Running: BANK_ITEMS");
            for (int i = 0; i < ThreadLocalRandom.current().nextInt(1, 5); i++) {
                LogHelper.log("Banking item");
                List<Item> allItems = Inventory.all(new Filter<Item>() {
                    @Override
                    public boolean match(Item item) {
                        return item != null;
                    }
                });
                Bank.deposit(allItems.get(ThreadLocalRandom.current().nextInt(0, allItems.size())).getID());
                SleepHelper.randomSleep(500, 2000);
            }
            Bank.close();
            return true;
        }

        @Override
        public Boolean verify() {
            return Bank.isOpen();
        }

        @Override
        public TaskState previousState() {
            return WALK_TO_BANK;
        }

        @Override
        public TaskState nextState() {
            return POLL_BOOTH;
        }
    },
    POLL_BOOTH {
        @Override
        public Boolean run() {
            LogHelper.log("Running: POLL_BOOTH");
            HintArrowHelper.interact("Poll booth");
            SleepHelper.sleepUntil(Dialogues::canContinue, 3000);
            while (Dialogues.canContinue()) {
                Dialogues.spaceToContinue();
                LogHelper.log(Dialogues.getNPCDialogue());
                SleepHelper.sleepRange(NPCHelper.timeToRead(Dialogues.getNPCDialogue()), 600);
                SleepHelper.randomSleep(2000, 4000);
            }
            // TODO scroll and explore polls
            SleepHelper.sleepUntil(() -> Widgets.getWidget(310).isVisible(), 3000);
            SleepHelper.randomSleep(1000, 5000);
            Widgets.getWidget(310).getChild(2).getChild(11).interact();
            SleepHelper.randomSleep(500, 1200);
            // TODO figure out how to see if a widget is closed
//            SleepHelper.sleepUntil(() -> !Widgets.getWidget(310).isVisible(), 4000);
            return true;
        }

        @Override
        public Boolean verify() {
            return HintArrowHelper.getName("Poll booth").contains("Poll booth");
        }

        @Override
        public TaskState previousState() {
            return BANK_ITEMS;
        }

        @Override
        public TaskState nextState() {
            return ACCOUNT_GUIDE;
        }
    },
    ACCOUNT_GUIDE {
        @Override
        public Boolean run() {
            LogHelper.log("Running: ACCOUNT_GUIDE");
            HintArrowHelper.interact("Door");
            SleepHelper.sleepUntil(() -> !HintArrowHelper.getName("Door").contains("Door"), 5000, 1000);
            HintArrowHelper.interact("Account Guide");
            SleepHelper.sleepUntil(Dialogues::canContinue, 3000);
            while (Dialogues.canContinue()) {
                Dialogues.spaceToContinue();
                LogHelper.log(Dialogues.getNPCDialogue());
                SleepHelper.sleepRange(NPCHelper.timeToRead(Dialogues.getNPCDialogue()), 600);
            }
            Tabs.openWithMouse(Tab.ACCOUNT_MANAGEMENT);
            SleepHelper.sleepUntil(() -> Tabs.isOpen(Tab.ACCOUNT_MANAGEMENT), 2000);
            HintArrowHelper.interact("Account Guide");
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
            return HintArrowHelper.getName("Door").contains("Door");
        }

        @Override
        public TaskState previousState() {
            return POLL_BOOTH;
        }

        @Override
        public TaskState nextState() {
            return WALK_TO_PRAYER;
        }
    },
    WALK_TO_PRAYER{
        Area PrayerArea = new Area(new Tile(3124,3108,0), new Tile(3122,3104,0));
        @Override
        public Boolean run() {
            LogHelper.log("Running: WALK_TO_PRAYER");
            while (!PrayerArea.contains(Me.playerObjet().getTile())) {
                SleepHelper.sleepUntil(() -> Walking.walk(PrayerArea.getRandomTile()), 30000);
                SleepHelper.randomSleep(500, 1300);
            }
            return null;
        }

        @Override
        public Boolean verify() {
            return Widgets.getWidget(263).getChild(1).getChild(0).getText().contains("Prayer");
        }

        @Override
        public TaskState previousState() {
            return ACCOUNT_GUIDE;
        }

        @Override
        public TaskState nextState() {
            return null;
        }
    }
    ;
}

public class BankingTutorial extends TaskNode {
    ScriptState state;

    public BankingTutorial(ScriptState state) {
        this.state = state;
    }

    @Override
    public boolean accept() {
        return this.state.get() == ScriptState.States.BANKING_TUTORIAL;
    }

    @Override
    public int execute() {
        log("Starting Bank Tutorial");
        TaskState state = BankingTutorialState.WALK_TO_BANK;
        boolean done = false;
        while (!done) {
            if (state.verify()) {
                state.run();
            }
            state = state.nextState();
            done = state == null;
        }
        this.state.set(ScriptState.States.BANKING_TUTORIAL);
        return -1;
    }
}
