package state_snapshots;

import consts.Items;
import org.R;
import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.wrappers.interactive.NPC;
import org.dreambot.api.wrappers.items.Item;
import state_snapshots.wrappers.RItem;
import state_snapshots.wrappers.RItemBuilder;
import utils.Me;

import java.util.List;

public class RawShrimpBuilder extends RItemBuilder<RawShrimp>{
    public RawShrimp build(){
        super.build();
        return this.getClass()(RawShrimp);
    }

}

class RawShrimp extends RItem {

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
