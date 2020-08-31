package tasks;

import org.dreambot.api.script.TaskNode;
import utils.WidgetHelper;

public class GGToFishing extends TaskNode {
    @Override
    public boolean accept() {
        log("Checking if GGToFishing is valid");
        WidgetHelper widget = new WidgetHelper(new int[]{1, 0}, 263);
        return widget.widgetContainsText("It's time to meet your first instructor");
    }

    @Override
    public int execute() {
        return -1;
    }
}
