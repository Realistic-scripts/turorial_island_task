package state_snapshots.wrappers;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.map.Area;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class PlayerState {
    public Set<state_snapshots.wrappers.RItem> PSInventory;
    public Set<state_snapshots.wrappers.RItem> PSEquipment;
    public String PSDialog;
    public int[] PSStats;
    public Area PSLocation;

    public boolean validate() {
        List<RItem> PSInventoryList = new ArrayList<>(PSInventory);
        PSInventoryList.sort(Comparator.comparing(RItem::getPriority));
        for (state_snapshots.wrappers.RItem item : PSInventoryList) {
            MethodProvider.logInfo("Getting item "+ item.getClass().getName());
            item.obtain(1, false);
        }
        return false;
    }

}
