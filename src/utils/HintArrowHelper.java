package utils;

import org.dreambot.api.methods.hint.HintArrow;
import org.dreambot.api.methods.hint.HintArrowType;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.wrappers.interactive.Entity;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;

public class HintArrowHelper {
    public static String getName() {
        return getName(null);
    }

    public static String getName(String name) {
        Entity gameEntity = getEntity(name);
        LogHelper.log(gameEntity);
        if (gameEntity != null) {
            return gameEntity.getName();
        }
        return "";
    }

    // TODO add exact interact. The normal interact will just pick the closest one to click on.
    public static void interact(String name) {
        interact(name, null);
    }

    public static void interact(String name, String actionString) {
        Entity gameEntity = getEntity(name);

        if (gameEntity != null) {
            int entityId = gameEntity.getID();
            GameObject gameObjectHold = GameObjects.closest(entityId);
            NPC npcHold = NPCs.closest(entityId);
            if (gameObjectHold != null) {
                gameEntity = gameObjectHold;
            } else if (npcHold != null) {
                gameEntity = npcHold;
            }
            if (actionString != null) {
                for (String action : gameEntity.getActions()) {
                    if (action.toLowerCase().contains(actionString.toLowerCase())) {
                        Entity finalGameEntity = gameEntity; // I am not sure why I have to make a new reference to this var, but it works.
                        SleepHelper.sleepUntil(() -> finalGameEntity.interact(action), 3000);
                        break;
                    }
                }
            } else {
                SleepHelper.sleepUntil(gameEntity::interact, 3000);
            }
        }
    }

    private static Entity getEntity(String name) {
        if (!HintArrow.exists()) {
            LogHelper.log("Hint arrow does not exits");
            return null;
        }
        if (HintArrow.getType() == HintArrowType.NPC) {
            LogHelper.log("Hint arrow is an NPC");
            return HintArrow.getPointed();
        }

        Tile tile = HintArrow.getTile();
        if (tile == null) {
            LogHelper.log("Invalid tile");
            return null;
        }
        GameObject[] gameObjects = tile.getTileReference().getObjects();
        LogHelper.log(gameObjects.length);
        if (gameObjects.length == 1) {
            for (GameObject gameObject : gameObjects) {
                return gameObject;
            }
        } else if (gameObjects.length > 1 & name != null) {
            for (GameObject gameObject : gameObjects) {
                if (gameObject.getName().equals(name)) {
                    return gameObject;
                }
            }
        }
        LogHelper.log("No game object on tile");
        return null;
    }
}
