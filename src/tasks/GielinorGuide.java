package tasks;

import org.dreambot.api.script.TaskNode;
import utils.NPCHelper;
import utils.WidgetHelper;

public class GielinorGuide extends TaskNode {
//    TODO this needs to change. On first time load this will fail.
//    private final NPC gG = new NPC(3308);

    @Override
    public boolean accept() {
        log("Checking if Gielinor Guide is valid");
        WidgetHelper widget = new WidgetHelper(new int[]{1, 0}, 263);
        return widget.widgetContainsText("Before you begin, have a read through");
    }

    @Override
    public int execute() {
//        TODO add some playing around with the camera and zoom
        log("Starting Gielinor Guide");
        NPCHelper gielinorGuide = new NPCHelper(3308);
        gielinorGuide.talkTo();
        return 0;
    }
}
