package utils;

import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.wrappers.interactive.Player;

public class Me {
    public static String name() {
        Player currentPlayer = Players.localPlayer();
        LogHelper.logMethod(currentPlayer.getName());
        return currentPlayer.getName();
    }

    public static int id() {
        Player currentPlayer = Players.localPlayer();
        return currentPlayer.getID();
    }

    public static Player playerObjet() {
        return Players.localPlayer();
    }

    public static boolean interactingWithNpc(int npcId) {
        try {
            return playerObjet().getCharacterInteractingWithMe().getID() == npcId;
        } catch (NullPointerException e) {
            return false;
        }

    }

    public static Tile cleanTile() {
        TileHelper tileHelper = new TileHelper(playerObjet().getTile());
        return tileHelper.getCleanTileNear();
    }
}
