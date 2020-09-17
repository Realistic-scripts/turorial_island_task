package state_snapshots;

import consts.Items;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.walking.impl.Walking;
import state_snapshots.wrappers.RItem;
import state_snapshots.wrappers.RItemBuilder;
import utils.DialogHelper;

public class SmallFishingNetBuilder extends RItemBuilder<SmallFishingNet> {

}
class SmallFishingNet extends RItem {
    private SmallFishingNet(){
        super(Items.SmallFishingNet, Areas.SmallFishingNetArea);
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
        DialogHelper.continueDialog();
    }
}