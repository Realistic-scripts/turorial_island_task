package state_snapshots;

import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.walking.impl.Walking;
import state_snapshots.wrappers.RItem;

public class BronzeAxe extends RItem {
    public BronzeAxe(int priority) {
        super(1351, StoringItem.INVENTORY, Areas.SmallFishingNetArea, true, false);
        this.setPriority(priority);
    }

    public BronzeAxe(StoringItem itemLocation, boolean Noted, int priority) {
        super(1351, itemLocation, Areas.SmallFishingNetArea, true, Noted);
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
