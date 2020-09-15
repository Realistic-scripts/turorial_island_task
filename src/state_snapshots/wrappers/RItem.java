package state_snapshots.wrappers;

import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
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

    public RItem(int ItemId, StoringItem WantedLocation, Area[] AreasToGet, boolean Tradeable, boolean Noted) {
        this.ItemId = ItemId;
        this.WantedLocation = WantedLocation;
        this.CurrentLocation = StoringItem.DNE;
        this.AreasToGet = AreasToGet;
        this.Tradeable = Tradeable;
        this.Noted = Noted;
    }

    public void obtain(int ItemQuantity, boolean getFromGe) {
        this.ItemQuantity = ItemQuantity;
        if (getFromGe) {
            GE();
        } else {
            this.ironMan();
        }
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

    public abstract void ironMan();

    public Area closestArea() {
        Area currentClosest = null;
        double currentDistance = 0;
        for (Area area : this.AreasToGet) {
            Tile closest = area.getNearestTile(Players.localPlayer());
            double distance = closest.distance();
            if (distance < currentDistance) {
                currentClosest = area;
            }
        }
        return currentClosest;
    }


}
