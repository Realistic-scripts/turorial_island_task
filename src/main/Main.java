package main;

import org.dreambot.api.randoms.RandomEvent;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.impl.TaskScript;
import state.ScriptState;
import tasks.*;

@ScriptManifest(author = "Realistic", name = "tutorial island task script", version = 0.1, description = "Tut island task.",
        category = Category.MISC)

public class Main extends TaskScript {
    private ScriptState state = new ScriptState(ScriptState.States.START);
    @Override
    public void onStart() {

        log("starting Tutorial Island");
        getRandomManager().disableSolver(RandomEvent.RESIZABLE_DISABLER);
        getRandomManager().disableSolver(RandomEvent.ROOF_DISABLER);
        addNodes(new LoggedIn(state), new PickName(), new PickAppearance(), new GielinorGuide(), new GGToFishing(),
                new SurvivalTraining(state));
        sleep(10000);
    }
}

