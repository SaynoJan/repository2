import java.util.List;

public class FightMap {
    private int width;
    private int height;
    private static char[][] fightGrid;
    private static char[][] fightGridCopy;
    public static final char NEUTRAL = '.';
    public static final char HEROPLAYER = 'H';
    public static final char HEROBOT = 'h';
    public static final char KOPEICHIKPLAYER = 'C';
    public static final char KOPEICHIKBOT = 'c';
    public static final char ARBALETCHIKPLAYER = 'A';
    public static final char ARBALETCHIKBOT = 'a';
    public static final char MECHNIKPLAYER = 'M';
    public static final char MECHNIKBOT = 'm';
    public static final char KAVALERISTPLAYER = 'T';
    public static final char KAVALERISTBOT= 't';
    public static final char PALADINPLAYER = 'P';
    public static final char PALADINBOT = 'p';
    public FightMap(int width, int height, Hero heroplayer,Hero herobot) {
        this.width = width;
        this.height = height;
        this.fightGrid = new char[height][width];
        this.fightGridCopy = new char[height][width];
        initializeFightMap(heroplayer);
    }
    private void initializeFightMap(Hero heroplayer) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                fightGrid[y][x] = NEUTRAL;
                fightGridCopy[y][x] = NEUTRAL;
            }
        }


        fightGrid[0][0] = HEROPLAYER;
        for (int i = 0; i < heroplayer.getArmy().size(); i++) {
            fightGrid[0][i + 1] = getUnitSymbol(heroplayer.getArmy().get(i), true);
        }

    }
    private char getUnitSymbol(Unit unit, boolean isPlayer) {
        switch (unit.getType()) {
            case "Копейщик":
                return isPlayer ? KOPEICHIKPLAYER : KOPEICHIKBOT;
            case "Арбалетчик":
                return isPlayer ? ARBALETCHIKPLAYER : ARBALETCHIKBOT;
            case "Мечник":
                return isPlayer ? MECHNIKPLAYER : MECHNIKBOT;
            case "Кавалерист":
                return isPlayer ? KAVALERISTPLAYER : KAVALERISTBOT;
            case "Паладин":
                return isPlayer ? PALADINPLAYER : PALADINBOT;
            default:
                return NEUTRAL;
        }
    }
    public void display() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(fightGrid[y][x] + " ");
            }
            System.out.println();
        }
    }
}
