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


public class SurvivalTraining extends TaskNode {


    @Override
    public boolean accept() {
        return ScriptState.get() == ScriptState.States.SURVIVAL_TRAINING;
    }

    @Override
    public int execute() {
        log("Starting survival training");

        log("Finishing survival training");
        ScriptState.set(ScriptState.States.MASTER_CHEF);
        return 1;
    }
}
