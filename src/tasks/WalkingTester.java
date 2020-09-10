package tasks;

import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.methods.walking.path.impl.GlobalPath;
import org.dreambot.api.methods.walking.pathfinding.impl.web.WebFinder;
import org.dreambot.api.methods.walking.web.node.AbstractWebNode;
import org.dreambot.api.script.TaskNode;
import utils.Me;

public class WalkingTester extends TaskNode {

    @Override
    public boolean accept() {
        return true;
    }

    @Override
    public int execute() {
//        WalkingHelper walkingHelper = new WalkingHelper(LumbCastleBank);
//        walkingHelper.walk();
        WebFinder webFinder = Walking.getWebPathFinder();
        GlobalPath<AbstractWebNode> path = webFinder.calculate(3236, 3218, 0, 3208, 3218, 2);
        log(path);
        for (AbstractWebNode node : path) {
            Tile tile = node.getTile();
            log(tile);
            log(tile.distance());
            Walking.walk(tile);
            sleepUntil(() -> Me.playerObjet().getTile().distance(tile) < 5, 10000);
        }
//        path.walk();
        return -1;
    }
}
