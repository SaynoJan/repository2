import java.util.Random;
import java.util.Scanner;

public class GameManager {

    private final Player player;
    private final Hero playerHero;
    private final Castle playerCastle;
    private final Map map;
    private final Hero botHero;
    private final Castle botCastle;

    public GameManager(Player player, Hero playerHero, Castle playerCastle, Map map, Hero botHero, Castle botCastle) {
        this.player = player;
        this.playerHero = playerHero;
        this.playerCastle = playerCastle;
        this.map = map;
        this.botHero = botHero;
        this.botCastle = botCastle;
    }

    public void gameLoop() {
        Scanner scanner = new Scanner(System.in);
        boolean gameRunning = true;

        while (gameRunning) {
            // проверяем, находится ли герой в замке
            if (playerHero.getCoordx(playerHero) == 1 && playerHero.getCoordy(playerHero) == 1) {
                // если герой в замке, показываем меню замка
                Main.enterCastleMenu(player, playerHero, playerCastle, map, botHero);
            } else {
                playerMove();
            }

            if (playerHero.getHp() <= 0) {
                System.out.println("Герой игрока пал в бою!  Игра окончена.  Вы проиграли!");
                break;
            }



            botMove();



            if (checkGameEnd()) {
                gameRunning = false;
                break;
            }
            if (botHero.getHp() <= 0) {
                System.out.println("Герой бота повержен!  Игра окончена.  Вы победили!");
                break;
            }
        }
    }

    public void botMove() {
        Random random = new Random();
        int moveX = 0;
        int moveY = 0;
        int oldX = botHero.getCoordx(botHero);
        int oldY = botHero.getCoordy(botHero);

        if (isNearCastle()) {
            System.out.println("Герой бота атакует замок игрока!");
            botHero.setx(1);
            botHero.sety(1);
            map.updateCell(oldY, oldX, map.getMapCopyCell(oldY, oldX));
            map.displayBotHero(botHero);
            map.display();
        } else {
            // выбираем случайное направление
            int direction = random.nextInt(4); // 0: вверх, 1: вниз, 2: влево, 3: вправо

            switch (direction) {
                case 0: // вверх
                    moveY = -1;
                    break;
                case 1: // вниз
                    moveY = 1;
                    break;
                case 2: // влево
                    moveX = -1;
                    break;
                case 3: // вправо
                    moveX = 1;
                    break;
            }


            if (botHero.getCoordx(botHero) + moveX >= 0 && botHero.getCoordx(botHero) + moveX < map.getWidth() &&
                    botHero.getCoordy(botHero) + moveY >= 0 && botHero.getCoordy(botHero) + moveY < map.getHeight()) {


                if (
                        map.getCell(botHero.getCoordx(botHero) + moveX, botHero.getCoordy(botHero) + moveY) != Map.CASTLE_PLAYER &&
                                map.getCell(botHero.getCoordx(botHero) + moveX, botHero.getCoordy(botHero) + moveY) != Map.HEROPLAYER) {

                    // обновляем позицию бота
                    botHero.setx(botHero.getCoordx(botHero) + moveX);
                    botHero.sety(botHero.getCoordy(botHero) + moveY);
                    map.updateCell(oldY, oldX, map.getMapCopyCell(oldY, oldX));
                    map.displayBotHero(botHero);
                    map.display();
                } else {
                    // мб добавить чета
                }
            } else {
                // мб добавить чета
            }
        }
    }
    public void playerMove() {
        playerHero.Move(playerHero, map, botHero, player, playerCastle);
    }

    public boolean isNearCastle() {
        int botX = botHero.getCoordx(botHero);
        int botY = botHero.getCoordy(botHero);
        //int playerX = playerHero.getCoordx(playerHero);
        //int playerY = playerHero.getCoordy(playerHero);
        return Math.abs(botX - 0) <= 1 && Math.abs(botY - 0) <= 1;
    }

    public boolean checkGameEnd() {

        if (botHero.getCoordx(botHero) == 1 && botHero.getCoordy(botHero) == 1) {
            System.out.println("Замок игрока захвачен! Игра окончена.  Вы проиграли!");
            return true;
        }
        if (playerHero.getCoordx(playerHero) == 8 && playerHero.getCoordy(playerHero) == 8) {
            System.out.println("Замок бота захвачен! Игра окончена.  Вы победили!");
            return true;
        }

        // мб условия

        return false;
    }
}
