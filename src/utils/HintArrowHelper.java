package utils;

import org.dreambot.api.methods.hint.HintArrow;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.wrappers.interactive.GameObject;

public class HintArrowHelper {
    public static String getName() {
        GameObject gameObject = getObject();
        if (gameObject != null) {
            return gameObject.getName();
        }
        return "";
    }

    public static void interact(String name) {
        GameObject gameObject = getObject();
        if (gameObject != null) {
            SleepHelper.sleepUntil(gameObject::interact, 3000);
        }
    }

    private static GameObject getObject() {
        if (!HintArrow.exists()) {
            return null;
        }
        Tile tile = HintArrow.getTile();
        if (tile == null) {
            return null;
        }
        GameObject[] gameObjects = tile.getTileReference().getObjects();
        if (gameObjects.length == 1) {
            for (GameObject gameObject : gameObjects) {
                return gameObject;
            }
        }
        return null;
    }
}
