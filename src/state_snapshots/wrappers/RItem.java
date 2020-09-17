package state_snapshots.wrappers;

import org.dreambot.api.methods.MethodProvider;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.wrappers.items.Item;
import state_snapshots.StoringItem;

import java.util.List;

public class RItem {
    public int ItemId;
    private int ItemQuantity = 1;
    private boolean Noted = false;
    private boolean GetFromGe = false;
    private Area[] AreasToGet = null;
    private int Priority = 0;
    private StoringItem WantedLocation = StoringItem.INVENTORY;


    private int Price;
    private StoringItem CurrentLocation = StoringItem.DNE;


    public void obtain() {
        MethodProvider.logInfo("Starting Obtain " + this.getClass().getName());

        if (this.owns()) {
            MethodProvider.logInfo(this.getClass().getName() + " already owned");
            return;
        }
        if (this.GetFromGe) {
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

    private void GE() {
        this.Price = 0; // TODO set price if using GE
        // TODO buy from GE
    }

    public boolean owns() {
        List<Item> items = Inventory.all(invItem -> {
            if (invItem == null) {
                return false;
            }
            return invItem.getID() == this.ItemId;
        });
        if (items.size() > 1) {
            return items.size() >= this.ItemQuantity;
        }

        Item item = Inventory.get(this.ItemId);
        if (item != null) {
            return item.getAmount() == this.ItemQuantity;
        }
        // TODO add bank checking
        return false;
    }

    public void ironMan() {

    }

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


    public static class Builder {
        private int ItemId;
        private int ItemQuantity = 1;
        private boolean Noted = false;
        private boolean GetFromGe = false;
        private Area[] AreasToGet = null;
        private int Priority = 0;
        private StoringItem WantedLocation = StoringItem.INVENTORY;

        public Builder(int ItemId) {
            this.ItemId = ItemId;

        }

        public Builder amount(int ItemQuantity) {
            this.ItemQuantity = ItemQuantity;
            return this;
        }

        public Builder isNoted(boolean Noted) {
            this.Noted = Noted;
            return this;
        }

        public Builder shouldGetFromGe(boolean GetFromGe) {
            this.GetFromGe = GetFromGe;
            return this;
        }

        public Builder locations(Area[] AreasToGet) {
            this.AreasToGet = AreasToGet;
            return this;
        }

        public Builder priority(int Priority) {
            this.Priority = Priority;
            return this;
        }

        public RItem build() {
            RItem rItem = new RItem();
            rItem.ItemId = this.ItemId;
            rItem.ItemQuantity = this.ItemQuantity;
            rItem.Noted = this.Noted;
            rItem.GetFromGe = this.GetFromGe;
            rItem.Priority = this.Priority;
            rItem.WantedLocation = this.WantedLocation;

            return rItem;
        }
    }


}
