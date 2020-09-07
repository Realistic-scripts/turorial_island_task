package utils;

import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.wrappers.interactive.Player;

public class WalkingHelper {
    Area area = null;
    Tile tile = null;

    public WalkingHelper(Area areaToWalkTo) {
        area = areaToWalkTo;
    }

    public WalkingHelper(Tile tileToWalkTo) {
        tile = tileToWalkTo;
    }

    public void walk() {
        // TODO add checking to see if the player gets stuck.
        if (tile == null & area != null) {
            getAreaTile();
        }
        Player player = Players.localPlayer();
        while (tile.walkingDistance(player.getTile()) > 3) {
            LogHelper.log(Me.playerObjet().getWalkAnimation());
            Walking.walk(tile);
            SleepHelper.sleepUntil(() -> Me.playerObjet().getWalkAnimation() == 808, 3000);
        }
    }

    private void getAreaTile() {
        tile = area.getRandomTile();
    }
}
