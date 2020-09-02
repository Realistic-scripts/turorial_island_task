package utils;

import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.wrappers.interactive.GameObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TileHelper {
    Tile tile;

    public TileHelper(Tile tile) {
        this.tile = tile;
    }

    public Tile getCleanTileNear() {
        int radius = 0;
        List<Tile> tileList = new ArrayList<>();
        while (!isTileClean() | !isTileWalkable()) {
            if (tileList.size() == 0) {
                radius++;
                tileList = new ArrayList<Tile>(Arrays.asList(tile.getArea(radius).getTiles()));
            }
            tile = tileList.remove(0);
            if (radius > 5) {
                return null;
            }
        }
        return this.tile;
    }

    public boolean isTileClean() {
        GameObject[] tileObjects = tile.getTileReference().getObjects();
        boolean clean = true;
        for (GameObject gameObject : tileObjects) {
            if (gameObject.getName() != null) {
                clean = false;
            }
        }
        return clean;
    }

    public boolean isTileWalkable() {
        return tile.getTileReference().getFlags() == 0;
    }
}
