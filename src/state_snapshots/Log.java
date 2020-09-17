package state_snapshots;

import consts.Items;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.walking.impl.Walking;
import state_snapshots.wrappers.RItem;

import java.util.Optional;

public class Log extends RItem {


    public Log(Optional<Integer> ItemId, Optional<Integer> ItemQuantity, Optional<Boolean> Noted, Optional<Boolean> getFromGe, Optional<Area[]> AreasToGet, Optional<Integer> priority, Optional<StoringItem> WantedLocation) {
        super(Optional.of(Items.Log), ItemQuantity, Noted, getFromGe, AreasToGet, priority, WantedLocation);
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
