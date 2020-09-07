package consts;

import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;

public final class Areas {
    public static final Area survivalExpertArea = new Area(
            new Tile(3101, 3098),
            new Tile(3105, 3097),
            new Tile(3104, 3094),
            new Tile(3101, 3094));
    public static final Area chefArea = new Area(new Tile(3075, 3083), new Tile(3075, 3085), new Tile(3078, 3085), new Tile(3078, 3083));
    public static final Area questGuideArea = new Area(new Tile(3087, 3124), new Tile(3087, 3120),
            new Tile(3084, 3120), new Tile(3084, 3124), new Tile(3085, 3124),
            new Tile(3086, 3125));

}
