package tasks;

import org.dreambot.api.Client;
import org.dreambot.api.methods.ViewportTools;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.wrappers.interactive.Model;
import state.ScriptState;

public class LoggedIn extends TaskNode {
    ScriptState state;

    public LoggedIn(ScriptState state) {
        this.state = state;
    }

    @Override
    public boolean accept() {
        boolean loggedIn = Client.isLoggedIn();
        if (loggedIn) {
            this.state.set(ScriptState.States.SURVIVAL_TRAINING);
        }
        return !loggedIn;
    }

    @Override
    public int execute() {
        log("waiting to log in");
        sleepUntil(Client::isLoggedIn, 15000);
        this.state.set(ScriptState.States.SURVIVAL_TRAINING);
//        ViewportTools viewPortTools = new ViewportTools();
//        Area area = new Area(new Tile(3092, 3107,0));
//        Model model = NPCs.closest(3308).getModel();

//        log(NPCs.closest(3308).getName());
//        log(viewPortTools.isVisibleOnMainScreen(viewPortTools.getSuitablePointFor(viewPortTools.getModelArea(NPCs.closest(3308)))));
//        log(NPCs.closest(3317).getCenterPoint().x);

        return 1;
    }
}
