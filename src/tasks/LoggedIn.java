package tasks;

import org.dreambot.api.Client;
import org.dreambot.api.script.TaskNode;
import state.ScriptState;

public class LoggedIn extends TaskNode {
    @Override
    public boolean accept() {
        boolean loggedIn = Client.isLoggedIn();
        if (loggedIn) {
            ScriptState.set(ScriptState.States.GIELINOR_GUIDE);
        }
        return !loggedIn;
    }

    @Override
    public int execute() {
        log("waiting to log in");
        sleepUntil(Client::isLoggedIn, 15000);
        ScriptState.set(ScriptState.States.GIELINOR_GUIDE);
        return 1;
    }
}
