package state_snapshots.wrappers;

import org.dreambot.api.methods.map.Area;
import state_snapshots.StoringItem;

public class RItemBuilder<T extends RItem> {
    private int ItemQuantity = 1;
    private boolean Noted = false;
    private boolean GetFromGe = false;
    private Area[] AreasToGet = null;
    private int Priority = 0;
    private StoringItem WantedLocation = StoringItem.INVENTORY;


    public RItemBuilder amount(int ItemQuantity) {
        this.ItemQuantity = ItemQuantity;
        return this;
    }

    public RItemBuilder isNoted(boolean Noted) {
        this.Noted = Noted;
        return this;
    }

    public RItemBuilder shouldGetFromGe(boolean GetFromGe) {
        this.GetFromGe = GetFromGe;
        return this;
    }

    public RItemBuilder locations(Area[] AreasToGet) {
        this.AreasToGet = AreasToGet;
        return this;
    }

    public RItemBuilder priority(int Priority) {
        this.Priority = Priority;
        return this;
    }

    public RItem build() {
        RItem rItem = new RItem();
        rItem.ItemQuantity = this.ItemQuantity;
        rItem.Noted = this.Noted;
        rItem.GetFromGe = this.GetFromGe;
        rItem.Priority = this.Priority;
        rItem.WantedLocation = this.WantedLocation;

        return rItem;
    }
}
