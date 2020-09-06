/**
 * This was originally created by "holic" on the dreambot.org forums and is being used in this script with their permission.
 * https://dreambot.org/forums/index.php?/topic/21156-random-username-generator-create-slightly-less-shitty-bot-names/
 */
package utils;

public class RandomUsernameGenerator {
    public static String toTitleCase(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder converted = new StringBuilder();

        boolean convertNext = true;
        for (char ch : text.toCharArray()) {
            if (Character.isSpaceChar(ch)) {
                convertNext = true;
            } else if (convertNext) {
                ch = Character.toTitleCase(ch);
                convertNext = false;
            } else {
                ch = Character.toLowerCase(ch);
            }
            converted.append(ch);
        }

        return converted.toString();
    }

    public static String generate() {
        String[] names = {"420", "Bane", "Bear", "Behemoth", "Big", "Black", "Blade", "Bleed", "Blood", "Blow",
                "Boar", "Boi", "Bolt", "Boulder", "Boy", "Break", "Brow", "Challenger", "Chaser", "Colossal",
                "Colossus", "Corrupter", "Crow", "Danger", "Dark", "Dead", "pelt", "Death", "Deceiver", "Die",
                "Dire", "Doom", "Dragon", "Dwarf", "Dwarven", "Fang", "Fierce", "Fist", "Flurry", "Freak", "Fury",
                "Fuse", "Giant", "Girl", "Gold", "Great", "Grim", "Grotesque", "Guthix", "Hallow", "Helm", "Hit",
                "Hollow", "Homie", "Hunter", "Insane", "Invincible", "Iron", "Ironfist", "Ironman", "Kill", "Killa",
                "Lion", "Lone", "Mammoth", "Man", "Mane", "Me", "Might", "Mighty", "Mithril", "Molten", "Myth",
                "Mythic", "Night", "Night", "owl", "One", "PK", "Paragon", "Pelt", "Poison", "Proud", "Pur3", "Pure",
                "Rage", "Raven", "Rebel", "Rock", "Rumble", "Savage", "Scar", "Sexy", "Shade", "Shield", "Shout",
                "Silent", "Silver", "Sk1ll", "Skill", "Skull", "Slayer", "Spirit", "Spook", "Steel", "Storm", "Stout",
                "Strong", "Swift", "Tempest", "The", "Thirst", "Thunder", "Tower", "True", "Voidmane", "White", "Wild",
                "Wildfist", "Wolf", "Zam", "Zammy", "Zero", "_", "axe", "bolt", "bow", "bronze", "brow", "chaser",
                "cleaver", "cold", "earth", "fang", "fierce", "fire", "flayer", "gaze", "hero", "hot", "ice", "killa",
                "knight", "mage", "man", "metal", "might", "rage", "scar", "snarl", "song", "sorrow", "stride",
                "strike", "strong", "sword", "sworn", "thorn", "throw", "tongue", "warrior", "wind", "wizard",
                "xX", "xox", "xxx"};
        int nameCount = Calculations.random(2, 3), nameLen = Calculations.random(8, 12);
        String name = "";
        for (int i = 0; i < nameCount; i++) {
            String tmpName = names[Calculations.random(0, names.length - 1)];
            switch (Calculations.random(0, 4)) {
                case 0:
                    tmpName = tmpName.toLowerCase();
                    break;
                case 1:
                    tmpName = tmpName.toUpperCase();
                    break;
                case 2 - 3:
                    tmpName = toTitleCase(tmpName);
                    break;
            }
            if (name.length() + tmpName.length() <= nameLen) {
                name += tmpName;
            } else {
                String tmprName;
                if (Calculations.random(0, 1) == 1) {
                    tmprName = (name + tmpName).substring(0, nameLen);
                } else {
                    int r = Calculations.random(9, nameLen);
                    tmprName = (name + tmpName).substring(0, r);
                    for (int ri = r; ri < 12; ri++) {
                        if (Calculations.random(0, 1) == 1) {
                            tmprName += Calculations.random(0, 9);
                        } else {
                            tmprName = Calculations.random(0, 9) + tmprName;
                        }
                    }
                }
                name = tmprName;
            }
        }
        return name;

    }

    private static class Calculations {
        public static int random(double min, double max) {
            int x = (int) ((Math.random() * ((max - min) + 1)) + min);
            return x;
        }
    }
}
