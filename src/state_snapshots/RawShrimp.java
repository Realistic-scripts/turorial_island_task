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
        while(!this.owns()) {
            fishingSpot.interact();
            Item raw_shrimp = Inventory.get(2514);
            int count = raw_shrimp.getAmount();
            // TODO figure out how to get amount of objects in inventory when not stacked.
//            raw_shrimp.
            MethodProvider.sleepUntil(() -> raw_shrimp.getAmount() < count, 10000);
            MethodProvider.logInfo(count);
            MethodProvider.logInfo(raw_shrimp.getAmount());
        }
    }
}
