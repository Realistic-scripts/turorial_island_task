package state_snapshots;

import consts.Items;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.walking.impl.Walking;
import state_snapshots.wrappers.RItem;
import utils.DialogHelper;

import java.util.Optional;

public class SmallFishingNet extends RItem {

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


