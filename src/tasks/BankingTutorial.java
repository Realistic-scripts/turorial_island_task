package tasks;

import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.Bank;
import org.dreambot.api.methods.hint.HintArrow;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.tabs.Tabs;
import org.dreambot.api.methods.widget.Widgets;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.wrappers.items.Item;
import state.ScriptState;
import state.TaskState;
import utils.*;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import static consts.Areas.BankArea;
import static consts.Areas.PrayerArea;
import static consts.WidgetsValues.*;


enum BankingTutorialState implements TaskState {
    WALK_TO_BANK {
        @Override
        public Boolean run() {
            WalkingHelper bankWalking = new WalkingHelper(BankArea);
            bankWalking.walk();
            while (!Bank.isOpen()) {
                HintArrowHelper.interact("Bank booth");
                SleepHelper.randomSleep(1500, 3000);
            }

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
            for (int i = 0; i < ThreadLocalRandom.current().nextInt(1, 5); i++) {
                LogHelper.log("Banking item");
                List<Item> allItems = Inventory.all(Objects::nonNull);
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
            if (!WidgetHelper.widgetExists(PollBoothParentFixedScreen)) {
                HintArrowHelper.interact("Poll booth");
                DialogHelper.continueDialog();
                // TODO scroll and explore polls
                SleepHelper.sleepUntil(() -> Widgets.getWidget(PollBoothParentFixedScreen).isVisible(), 3000);
                SleepHelper.randomSleep(1000, 5000);
            }
            Widgets.getWidget(PollBoothParentFixedScreen).getChild(PollBoothExitChild).getChild(PollBoothExitGrandChild).interact();
            SleepHelper.randomSleep(500, 1200);
            SleepHelper.sleepUntil(() -> !WidgetHelper.widgetExists(PollBoothParentFixedScreen), 4000);
            SleepHelper.sleepUntil(HintArrow::exists, 10000);
            return true;
        }

        @Override
        public Boolean verify() {
            return HintArrowHelper.getName("Poll booth").contains("Poll booth") | WidgetHelper.widgetExists(PollBoothParentFixedScreen);
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
//            HintArrowHelper.interact("Door");
            if (HintArrowHelper.getName("Door").contains("Door")) {
                GameObjects.closest(9721).interact();
                SleepHelper.sleepUntil(() -> !HintArrowHelper.getName("Door").contains("Door"), 5000, 1000);
                SleepHelper.sleepUntil(() -> HintArrowHelper.getName("Account Guide").contains("Account Guide"), 5000, 1000);
            }

            HintArrowHelper.interact("Account Guide");
            DialogHelper.continueDialog();

            Widgets.getWidget(TabWidgetParentFixedScreen).getChild(AccountManagementChildFixed).interact();
            SleepHelper.sleepUntil(() -> Tabs.isOpen(Tab.ACCOUNT_MANAGEMENT), 4000);
            SleepHelper.sleepUntil(() -> HintArrowHelper.getName("Account Guide").contains("Account Guide"), 5000);

            HintArrowHelper.interact("Account Guide");
            DialogHelper.continueDialog();
            return true;

        }

        @Override
        public Boolean verify() {
            WidgetHelper widget = new WidgetHelper(new int[]{ChatDialogChild, ChatDialogGrandChild}, ChatDialogParent);
            if (widget.widgetContainsText("Moving on")) {
                return widget.widgetContainsText("Polls");
            }
            if (widget.widgetContainsText("open your Account Management")) {
                return true;
            }
            return HintArrowHelper.getName("Door").contains("Door") | HintArrowHelper.getName("Account Guide").contains("Account Guide");
        }

        @Override
        public TaskState previousState() {
            return POLL_BOOTH;
        }

        @Override
        public TaskState nextState() {
            SleepHelper.sleepUntil(() -> HintArrowHelper.getName("Door").contains("Door"), 5000);
            DialogHelper.continueDialog();
            return WALK_TO_PRAYER;
        }
    },
    WALK_TO_PRAYER {
        @Override
        public Boolean run() {
            WalkingHelper prayerWalking = new WalkingHelper(PrayerArea);
            prayerWalking.walk();
            return true;
        }

        @Override
        public Boolean verify() {
            WidgetHelper widget = new WidgetHelper(new int[]{ChatDialogChild, ChatDialogGrandChild}, ChatDialogParent);
            if (widget.widgetContainsText("Moving on")) {
                return !widget.widgetContainsText("Polls");
            }
            return widget.widgetContainsText("Prayer");
        }

        @Override
        public TaskState previousState() {
            return ACCOUNT_GUIDE;
        }

        @Override
        public TaskState nextState() {
            return null;
        }
    };
}

public class BankingTutorial extends TaskNode {
    @Override
    public boolean accept() {
        return ScriptState.get() == ScriptState.States.BANKING_TUTORIAL;
    }

    @Override
    public int execute() {
        log("Starting: Bank Tutorial");
        TaskState state = BankingTutorialState.WALK_TO_BANK;
        TaskStateExecute.taskStateExecute(state);
        log("Finished: Bank Tutorial");
        ScriptState.set(ScriptState.States.PRAYER_TUTORIAL);
        return 1;
    }
}
