package main;

import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.impl.TaskScript;
import state.ScriptState;
import tasks.*;

@ScriptManifest(author = "Realistic", name = "Realistic Tutorial Island", version = 0.1,
        description = "A Tutorial Island script written by Realistic. This script uses the TaskScript system.",
        category = Category.MISC)

public class Main extends TaskScript {
    private ScriptState state = new ScriptState(ScriptState.States.START);

    @Override
    public void onStart() {
        log("starting Tutorial Island");
//        getRandomManager().disableSolver(RandomEvent.RESIZABLE_DISABLER);
//        getRandomManager().disableSolver(RandomEvent.ROOF_DISABLER);
        addNodes(
                new LoggedIn(state), new GielinorGuide(state),
//               new PickAppearance(), new GielinorGuideOld(), new GGToFishing(),
                new SurvivalTraining(state), new MasterChef(state), new QuestGuide(state), new MiningInstructor(state),
                new CombatInstructor(state), new BankingTutorial(state), new PrayerTutorial(state),
                new MagicTutor(state)
        );
    }
}

// org.dreambot.api.script.event.impl.ExperienceEvent
// org.dreambot.api.methods.walking.web.node.impl.dynamic.DynamicTeleport
// org.dreambot.api.utilities.InventoryMonitor
// org.dreambot.api.utilities.Logger
// org.dreambot.api.methods.map.Map
// org.dreambot.api.wrappers.widgets.Menu
// org.dreambot.api.methods.container.impl.Shop
/*
TODO:
    Add storing state locally with username hash
    Resuming from local state
    Add some walking checks
    Add stalled checks. IE: If it is taking too long in one place.
 */


