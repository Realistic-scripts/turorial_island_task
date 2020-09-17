package state_snapshots;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.Item;
import state_snapshots.wrappers.RItem;
import utils.Me;

import java.util.List;

public class RawShrimp extends RItem {
    public RawShrimp(int priority, int ItemQuantity, boolean getFromGe) {
        super(2514, StoringItem.INVENTORY, Areas.RawShrimpArea, true, false, ItemQuantity, getFromGe);
        this.setPriority(priority);
    }

    public RawShrimp(int priority, int ItemQuantity, boolean getFromGe, StoringItem itemLocation, boolean Noted) {
        super(2514, itemLocation, Areas.RawShrimpArea, true, Noted, ItemQuantity, getFromGe);
        this.setPriority(priority);
    }

    @Override
    public void ironMan() {
        this.tutorialIsland();
    }

    private void tutorialIsland() {
        Area area = this.closestArea();
        if (!area.contains(Players.localPlayer().getTile())) {
            Walking.walk(area.getRandomTile());
        }
        NPC fishingSpot = NPCs.closest("Fishing spot");
        while (!this.owns()) {
            if (Me.playerObjet().getAnimation() == -1) {
                fishingSpot.interact();
            }
            List<Item> items = Inventory.all(invItem -> {
                if (invItem == null) {
                    return false;
                }
                return invItem.getID() == this.ItemId;
            });
            MethodProvider.sleepUntil(() ->
                            Inventory.all(invItem -> {
                                if (invItem == null) {
                                    return false;
                                }
                                return invItem.getID() == this.ItemId;
                            }).size() > items.size(),
                    10000, 1000);
            MethodProvider.logInfo(Inventory.all(invItem -> {
                if (invItem == null) {
                    return false;
                }
                return invItem.getID() == this.ItemId;
            }).size());
            MethodProvider.logInfo(items.size());
            MethodProvider.sleep(500, 1000);
        }
    }
}
