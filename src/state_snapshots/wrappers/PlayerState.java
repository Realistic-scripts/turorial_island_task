package state_snapshots.wrappers;

import org.dreambot.api.methods.map.Area;

import java.util.Set;

public class PlayerState {
    public Set<state_snapshots.wrappers.RItem> PSInventory;
    public Set<state_snapshots.wrappers.RItem> PSEquipment;
    public String PSDialog;
    public int[] PSStats;
    public Area PSLocation;

    public boolean validate() {
        return false;
    }

}
