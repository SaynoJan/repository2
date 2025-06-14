import java.util.List;

public class Map {
    private int width;
    private int height;
    private static char[][] grid;
    private static char[][] gridCopy;
    private Hero botHero;
    private Baloon baloon;


    public static final char ROAD = '+';       // дорога
    public static final char CASTLE_PLAYER = 'I'; // замок игрока
    public static final char CASTLE_BOT = 'K';   // замок компьютера
    public static final char OBSTACLE_BOT = '#';// препятствие swamp
    public static final char OBSTACLE_PLAYER = '&';
    public static final char NEUTRAL = '.';// нейтральная территория
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
    public static final char BALOON = 'B';


    public Map(int width, int height, Hero botHero, Hero playerHero) {
        this.width = width;
        this.height = height;
        this.grid = new char[height][width];
        this.gridCopy = new char[height][width];
        this.botHero = botHero;
        initializeMap();
        createBaloon(playerHero, botHero);
    }
    public static void displayHero(Hero heroplayer){
        int xpl = heroplayer.getCoordx(heroplayer);
        int ypl = heroplayer.getCoordy(heroplayer);

        // восстанавливаем старую клетку
        grid[ypl][xpl] = gridCopy[ypl][xpl];

        grid[ypl][xpl] = HEROPLAYER;
    }

    private void initializeMap() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[y][x] = NEUTRAL;
                gridCopy[y][x] = NEUTRAL;
            }
        }



        grid[0][0] = CASTLE_PLAYER;
        gridCopy[0][0] = CASTLE_PLAYER;
        grid[height - 1][width - 1] = CASTLE_BOT;
        gridCopy[height - 1][width - 1] = CASTLE_BOT;

        int x = 1, y = 1;
        while (x < width-1 && y < height-1) {
            grid[y][x] = ROAD;
            gridCopy[y][x] = ROAD;
            x++;
            y++;
        }


        int[][] obstacleCoordsBot = {
                {1, 0}, {2, 0}, {0, 1}, {0, 2},
                {2, 1}, {3, 1}, {3, 2}, {1, 2},
                {1, 3}, {2, 3}
        };
        int[][] obstacleCoordsPlayer = {
                {9, 8}, {9, 7}, {8, 7}, {8, 6},
                {7, 6}, {8, 9}, {7, 9}, {7, 8},
                {6, 8}, {6, 7}
        };

        for (int[] coord : obstacleCoordsBot) {
            int obstacleX = coord[0];
            int obstacleY = coord[1];
            if (obstacleX >= 0 && obstacleX < width && obstacleY >= 0 && obstacleY < height) {
                grid[obstacleY][obstacleX] = OBSTACLE_BOT;
                gridCopy[obstacleY][obstacleX] = OBSTACLE_BOT;
            }
        }
        for (int[] coord : obstacleCoordsPlayer) {
            int obstacleX = coord[0];
            int obstacleY = coord[1];
            if (obstacleX >= 0 && obstacleX < width && obstacleY >= 0 && obstacleY < height) {
                grid[obstacleY][obstacleX] = OBSTACLE_PLAYER;
                gridCopy[obstacleY][obstacleX] = OBSTACLE_PLAYER;
            }
        }




    }
    public void createBaloon(Hero playerHero, Hero botHero) {
        Baloon baloon = new Baloon(0,0);
        this.baloon = baloon.spawnBaloon(this, playerHero, botHero);
        grid[this.baloon.getY()][this.baloon.getX()] = BALOON;
        gridCopy[this.baloon.getY()][this.baloon.getX()] = BALOON; // Обновляем gridCopy
    }
    public void displayBotHero(Hero herobot) {
        int x = herobot.getCoordx(herobot);
        int y = herobot.getCoordy(herobot);
        grid[y][x] = HEROBOT;
    }

    public void displayUnit(Unit unit, int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            switch (unit.getType()) {
                case "Копейщик":
                    grid[y][x] = KOPEICHIKBOT;
                    break;
                case "Арбалетчик":
                    grid[y][x] = ARBALETCHIKBOT;
                    break;
                case "Мечник":
                    grid[y][x] = MECHNIKBOT;
                    break;
                case "Кавалерист":
                    grid[y][x] = KAVALERISTBOT;
                    break;
                case "Паладин":
                    grid[y][x] = PALADINBOT;
                    break;
                default:
                    grid[y][x] = NEUTRAL;
            }
        }
    }

    // отображение карты в консоли
    public void display() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(grid[y][x] + " ");
            }
            System.out.println();
        }
    }
    public boolean gridCheck(int y,int x, char a){
        return grid[y][x] == a;
    }

    // метод для обновления клетки на карте
    public void updateCell(int y, int x, char symbol) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            grid[y][x] = symbol;
        } else {
            System.out.println("Некорректные координаты клетки!");
        }
    }
    public char getMapCopyCell(int y, int x) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return gridCopy[y][x];
        }
        return ' ';
    }


    public char getCell(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return grid[y][x];
        } else {
            System.out.println("Некорректные координаты клетки!");
            return ' ';
        }
    }

    public boolean evilGuyCheck(Hero hero,Hero botHero) {
        int heroX = hero.getCoordx(hero);
        int heroY = hero.getCoordy(hero);

        // Проверяем, находится ли на той же клетке герой бота
        if (heroX == botHero.getCoordx(botHero) && heroY == botHero.getCoordy(botHero)) {
            return true;
        }

        // Проверяем, находится ли на той же клетке какой-либо юнит бота
        for (Unit unit : botHero.getArmy()) {
            if (heroX == unit.getX() && heroY == unit.getY()) {
                return true;
            }
        }

        return false;
    }
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    public void placeFixedBotArmy(List<Unit> botArmy) {
        // Закрепленные позиции для юнитов бота [x, y]
        int[][] botUnitPositions = {
                {5, 7},  // позиция 1-го юнита
                {8, 7},  // позиция 2-го юнита
                {6, 7},  // ...
                {7, 5},
                {9, 7}
        };

        for (int i = 0; i < botArmy.size() && i < botUnitPositions.length; i++) {
            int x = botUnitPositions[i][0];
            int y = botUnitPositions[i][1];
            Unit unit = botArmy.get(i);
            unit.setX(x);  // Задаем координаты юниту
            unit.setY(y);
            displayUnit(unit, x, y);
        }
    }
    public Hero getBotHero(){
        return botHero;
    }
    public Baloon getBaloon(){
        return baloon;
    }
}
