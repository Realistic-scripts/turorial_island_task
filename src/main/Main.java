package main;

import org.dreambot.api.randoms.RandomEvent;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.impl.TaskScript;
import tasks.GGToFishing;
import tasks.GielinorGuide;
import tasks.PickAppearance;
import tasks.PickName;

@ScriptManifest(author = "Nah", name = "tutorial island task script", version = 0.1, description = "Tut island task.",
        category = Category.MISC)

public class Main extends TaskScript {
    @Override
    public void onStart() {
        log("starting Tutorial Island");
        getRandomManager().disableSolver(RandomEvent.RESIZABLE_DISABLER);
        getRandomManager().disableSolver(RandomEvent.ROOF_DISABLER);
        addNodes(new PickName(), new PickAppearance(), new GielinorGuide(), new GGToFishing());
        sleep(10000);
    }
}

