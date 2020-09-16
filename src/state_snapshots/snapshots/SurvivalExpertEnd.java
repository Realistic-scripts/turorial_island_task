package state_snapshots.snapshots;

import state_snapshots.RawShrimp;
import state_snapshots.wrappers.PlayerState;

import java.util.Arrays;
import java.util.HashSet;

public class SurvivalExpertEnd extends PlayerState {
    public SurvivalExpertEnd() {
        this.PSInventory = new HashSet<>(
                Arrays.asList(
//                        new SmallFishingNet(5),
                        new RawShrimp(4, 10, false)
//                        new BronzeAxe(3),
//                        new Tinderbox(2),
//                        new Log(1)
                )
        );
    }
}
