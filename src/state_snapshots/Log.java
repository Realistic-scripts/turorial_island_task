package state_snapshots;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.walking.impl.Walking;
import state_snapshots.wrappers.RItem;

public class Log extends RItem {

    public Log(int priority, int ItemQuantity, boolean getFromGe) {
        super(2511, StoringItem.INVENTORY, Areas.TreeArea, true, false, ItemQuantity, getFromGe);
        this.setPriority(priority);
    }

    public Log( int priority, int ItemQuantity, boolean getFromGe, StoringItem itemLocation, boolean Noted) {
        super(2511, itemLocation, Areas.TreeArea, true, Noted, ItemQuantity, getFromGe);
        this.setPriority(priority);
    }


    @Override
    public void ironMan() {
        Area area = this.closestArea();
        if (!area.contains(Players.localPlayer().getTile())) {
            Walking.walk(area.getRandomTile());
        }
        GameObjects.closest("Tree").interact();
        MethodProvider.sleepUntil(() -> Inventory.contains(2511), 10000);
    }
}
