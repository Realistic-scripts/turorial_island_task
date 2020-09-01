package utils;

import org.dreambot.api.methods.hint.HintArrow;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.wrappers.interactive.GameObject;

public class HintArrowHelper {
    public static String getName() {
        if (!HintArrow.exists()) {
            return "";
        }
        Tile tile = HintArrow.getTile();
        GameObject[] gameObjects = tile.getTileReference().getObjects();
        if (gameObjects.length > 1 | gameObjects.length == 0) {
            return "";
        }
        for (GameObject gameObject : gameObjects) {
            return gameObject.getName();
        }
        return "";
    }
}
