package state_snapshots.snapshots;

import state_snapshots.*;
import state_snapshots.wrappers.PlayerState;

import java.util.Arrays;
import java.util.HashSet;

public class SurvivalExpertEnd extends PlayerState {
    public SurvivalExpertEnd() {
        this.PSInventory = new HashSet<>(
                Arrays.asList(
                        new SmallFishingNet{{ItemQuantity = 1}},
                        new RawShrimp(4, 2, false),
                        new BronzeAxe(3),
                        new Tinderbox(2),
                        new Log(1, 1, false)
                )
        );
    }
}
