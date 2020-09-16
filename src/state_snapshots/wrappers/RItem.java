package state_snapshots.wrappers;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.wrappers.items.Item;
import state_snapshots.StoringItem;

public abstract class RItem {
    int ItemId;
    int ItemQuantity;
    StoringItem CurrentLocation;
    StoringItem WantedLocation;
    Area[] AreasToGet;
    boolean Tradeable;
    boolean Noted;
    int Price;
    Integer Priority = 0;
    boolean getFromGe = false;

    public RItem(int ItemId, StoringItem WantedLocation, Area[] AreasToGet, boolean Tradeable, boolean Noted,
                 int ItemQuantity, boolean getFromGe) {
        this.ItemId = ItemId;
        this.WantedLocation = WantedLocation;
        this.CurrentLocation = StoringItem.DNE;
        this.AreasToGet = AreasToGet;
        this.Tradeable = Tradeable;
        this.Noted = Noted;
        this.ItemQuantity = ItemQuantity;
        this.getFromGe = getFromGe;
    }

    public void obtain() {
        MethodProvider.logInfo("Starting Obtain " + this.getClass().getName());

        if (this.owns()) {
            MethodProvider.logInfo(this.getClass().getName() + " already owned" );
            return;
        }
        if (this.getFromGe) {
            GE();
        } else {
            this.ironMan();
        }
        MethodProvider.logInfo("Finished Obtain " + this.getClass().getName());
    }


    public Integer getPriority() {
        /*
        The Higher the priority the sooner this should be obtained.
         */
        return this.Priority;
    }

    public void setPriority(int priority) {
        this.Priority = priority;
    }

    private void GE() {
        this.Price = 0; // TODO set price if using GE
        // TODO buy from GE
    }

    public boolean owns() {
        Item item = Inventory.get(this.ItemId);
        if (item != null) {
            return item.getAmount() == this.ItemQuantity;
        }
        // TODO add bank checking
        return false;
    }

    public abstract void ironMan();

    public Area closestArea() {
        return this.AreasToGet[0];
//        Area currentClosest = null;
//        double currentDistance = 0;
//        for (Area area : this.AreasToGet) {
//            Tile closest = area.getNearestTile(Players.localPlayer());
//            double distance = closest.distance();
//            if (distance < currentDistance) {
//                currentClosest = area;
//            }
//        }
//        return currentClosest;
    }


}
