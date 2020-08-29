package utils;

import org.dreambot.api.methods.MethodContext;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.wrappers.interactive.Player;

public class Me extends MethodContext {
    public static String name() {
        Player currentPlayer = Players.localPlayer();
        return currentPlayer.getName();
    }
}
