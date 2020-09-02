package tasks;

import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.script.TaskNode;
import utils.NPCHelper;
import utils.WalkingHelper;
import utils.WidgetHelper;

public class GGToFishing extends TaskNode {
    Area survivalExpertArea = new Area(
            new Tile(3101, 3098),
            new Tile(3105, 3097),
            new Tile(3104, 3094),
            new Tile(3101, 3094));
    NPCHelper survivalExpertNPC = new NPCHelper(8503, new Integer[]{});

    @Override
    public boolean accept() {
        log("Checking if GGToFishing is valid");
        WidgetHelper widget = new WidgetHelper(new int[]{1, 0}, 263);
        return widget.widgetContainsText("It's time to meet your first instructor") |
                widget.widgetContainsText("Talk to the survival expert");
    }

    @Override
    public int execute() {
        WalkingHelper walking = new WalkingHelper(survivalExpertArea);
        walking.walk();
        survivalExpertNPC.talkTo();
        sleep(2000);
        return 0;

    }
}
