package state_snapshots;

import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.walking.impl.Walking;
import state_snapshots.wrappers.RItem;

public class Tinderbox extends RItem {
    public Tinderbox(int priority) {
        super(590, StoringItem.INVENTORY, Areas.SmallFishingNetArea, true, false, 1, false);
        this.setPriority(priority);
    }

    public Tinderbox(StoringItem itemLocation, boolean Noted, int priority) {
        super(590, itemLocation, Areas.SmallFishingNetArea, true, Noted, 1, false);
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
        NPCs.closest("Survival Expert").interact();
    }
}