package state_snapshots.snapshots;

import state_snapshots.*;
import state_snapshots.wrappers.PlayerState;
import state_snapshots.wrappers.RItem;

import java.util.Arrays;
import java.util.HashSet;

public class SurvivalExpertEnd extends PlayerState {
    public SurvivalExpertEnd() {
        this.PSInventory = new HashSet<RItem>(
                Arrays.asList(
//                        new SmallFishingNetBuilder().amount(1).priority(5).build(),
                        new RawShrimpBuilder().amount(2).priority(4).build()
//                        new BronzeAxe(3),
//                        new Tinderbox(2),
//                        new Log(1, 1, false)
                )
        );
    }
}
