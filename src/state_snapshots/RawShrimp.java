package state_snapshots;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.NPC;
import state_snapshots.wrappers.RItem;

public class RawShrimp extends RItem {
    public RawShrimp(int priority) {
        super(2514, StoringItem.INVENTORY, Areas.RawShrimpArea, true, false);
        this.setPriority(priority);
    }

    public RawShrimp(StoringItem itemLocation, boolean Noted, int priority) {
        super(2514, itemLocation, Areas.RawShrimpArea, true, Noted);
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
        fishingSpot.interact();
        MethodProvider.sleepUntil(() -> Inventory.contains(2514), 10000);
    }
}
