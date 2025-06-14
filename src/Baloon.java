import java.util.Random;

class Baloon {
    private int x;
    private int y;

    public Baloon(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Baloon spawnBaloon(Map map, Hero playerHero, Hero botHero) {
        Random random = new Random();
        int x, y;
        do {
            x = random.nextInt(map.getWidth());
            y = random.nextInt(map.getHeight());
        } while (x == 0 && y == 0 || // замок игрока
                x == map.getWidth() - 1 && y == map.getHeight() - 1 || // замок бота
                x == playerHero.getCoordx(playerHero) && y == playerHero.getCoordy(playerHero) || // герой игрока
                x == botHero.getCoordx(botHero) && y == botHero.getCoordy(botHero) || // герой бота
                map.getCell(x,y) == Map.KOPEICHIKBOT || map.getCell(x,y) == Map.ARBALETCHIKBOT || map.getCell(x,y) == Map.MECHNIKBOT|| //юниты бота
                map.getCell(x,y) == Map.KAVALERISTBOT || map.getCell(x,y) == Map.PALADINBOT);

        return new Baloon(x, y);
    }
}