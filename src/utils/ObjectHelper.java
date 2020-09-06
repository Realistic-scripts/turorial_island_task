package utils;

import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.wrappers.interactive.GameObject;

public class ObjectHelper {
    private int objectId;
    private GameObject currentObject;

    public ObjectHelper(int objectIdInit) {
        objectId = objectIdInit;
        currentObject = GameObjects.closest(9398);
    }

    public void interact() {

    }
}
