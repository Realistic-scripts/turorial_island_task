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
    public static final Area miningInstructorArea = new Area(new Tile(3084, 9504), new Tile(3084, 9508), new Tile(3080, 9508), new Tile(3080, 9504));
    public static final Area MiningGateArea = new Area(new Tile(3094, 9503), new Tile(3093, 9502));
    public static final Area CombatInstructorArea = new Area(new Tile(3104, 9509), new Tile(3107, 9509),
            new Tile(3107, 9508), new Tile(3105, 9508), new Tile(3105, 9505),
            new Tile(3102, 9505));
    public static final Area RatGateAreaOutside = new Area(new Tile(3113, 9519, 0), new Tile(3111, 9518, 0));
    public static final Area RatGateAreaInside = new Area(new Tile(3110, 9519, 0), new Tile(3108, 9518, 0));
    public static final Area CombatInstructorEndLadderArea = new Area(new Tile(3112, 9525, 0), new Tile(3109, 9523, 0));
    public static final Area BankArea = new Area(new Tile(3122, 3123, 0), new Tile(3120, 3121, 0));
    public static final Area PrayerArea = new Area(new Tile(3124, 3108, 0), new Tile(3122, 3104, 0));


}
