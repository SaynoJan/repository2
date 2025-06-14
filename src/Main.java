import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        String command;
        Scanner scanner= new Scanner(System.in);
        
        do {
            System.out.println("Welcome to Heroes of IU|||. Please  make your choice:");
            System.out.println("1: Start new game");
            System.out.println("2: Options");
            System.out.println("3: Credits");
            System.out.println("4: Exit");
            command = scanner.nextLine();
            switch(command){
                case "1":
                    startNewGame();
                    break;
                case "2":
                    openOptionsMenu();
                    break;
                case "3":
                    showCredits();
                    break;
                case "4": 
                    break;
                default:
                    System.out.println("Command not recognized! Please try again");
            }
        }
        while(!command.equals("4"));

    }

    private static void showCredits() {
        System.out.println("\nCreated by Yana Sayno from 41 group)\nversion 1.0, last modified on 15.03.25");
    }

    private static void openOptionsMenu() {

    }

    private static void startNewGame() {
        Scanner scanner= new Scanner(System.in);


        Player player = new Player(200); // 200р
        Castle playerCastle = new Castle("player", player);
        playerCastle.buildBuilding(GameData.AVAILABLE_BUILDINGS.get(0));
        Hero playerHero = new Hero("Герой Игрока", 1 , 1, "player", 50,3);

        playerHero.addUnit(GameData.AVAILABLE_UNITS.get(0)); // копейщик)
        playerHero.addUnit(GameData.AVAILABLE_UNITS.get(0));

        Player bot = new Player(200);
        Castle botCastle = new Castle("bot", bot);
        //botCastle.buildBuilding(GameData.AVAILABLE_BUILDINGS.get(0));


        Hero botHero = new Hero("Герой Компьютера", 8, 8, "bot",50,3);
        botHero.addUnit(GameData.AVAILABLE_UNITS.get(0));
        botHero.addUnit(GameData.AVAILABLE_UNITS.get(1));
        botHero.addUnit(GameData.AVAILABLE_UNITS.get(2));
        botHero.addUnit(GameData.AVAILABLE_UNITS.get(3));
        botHero.addUnit(GameData.AVAILABLE_UNITS.get(4));
        List<Unit> botArmy = List.of(
                new Unit("Копейщик", 10, 5, 2, 1, 50),
                new Unit("Арбалетчик", 8, 7, 2, 3, 60),
                new Unit("Мечник", 15, 10, 3, 1, 70)
        );
        Map gameMap = new Map(10, 10,botHero,playerHero);

        botHero.getArmy().addAll(botArmy);

        // Размещаем на карте
        gameMap.placeFixedBotArmy(botArmy);
        gameMap.displayBotHero(botHero);
        gameMap.display();

        // Создаем GameManager и запускаем игровой цикл
        GameManager gameManager = new GameManager(player, playerHero, playerCastle, gameMap, botHero, botCastle);
        gameManager.gameLoop();
    }
    public static void enterCastleMenu(Player player, Hero hero, Castle castle, Map map, Hero botHero) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nВыберите действие:");
            System.out.println("1: Нанять юнита");
            System.out.println("2: Построить здание");
            System.out.println("3: Показать состояние");
            System.out.println("4: Выйти из замка");
            System.out.println("5: Выйти в главное меню");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.println("Доступные юниты:");
                    System.out.println("Доступные юниты:");
                    for (int i = 0; i < GameData.AVAILABLE_UNITS.size(); i++) {
                        Unit unit = GameData.AVAILABLE_UNITS.get(i);
                        if (castle.hasBuildingOfLevel(i + 1)) {
                            // Отображаем начальные значения юнитов
                            System.out.println((i + 1) + ": " + unit.getType() +
                                    " (ХП: " + unit.getInitialHp() +
                                    ", Урон: " + unit.getDamage() +
                                    ", Перемещение: " + unit.getMoveRange() +
                                    ", Атака: " + unit.getAttackRange() +
                                    ", Цена " + unit.getCost() + ")");
                        }
                    }
                    System.out.print("Выберите юнита: ");
                    int unitChoice = Integer.parseInt(scanner.nextLine()) - 1;
                    if (unitChoice >= 0 && unitChoice < GameData.AVAILABLE_UNITS.size()) {
                        if (castle.hasBuildingOfLevel(unitChoice + 1)) {
                            castle.recruitUnit(GameData.AVAILABLE_UNITS.get(unitChoice), hero);
                        } else
                            System.out.println("Неверный выбор!");
                    } else {
                        System.out.println("Неверный выбор!");
                    }
                    break;
                case "2":
                    System.out.println("Доступные здания:");
                    for (int i = 1; i < GameData.AVAILABLE_BUILDINGS.size(); i++) {
                        System.out.println((i) + ": " + GameData.AVAILABLE_BUILDINGS.get(i));
                    }
                    System.out.print("Выберите здание: ");
                    int buildingChoice = Integer.parseInt(scanner.nextLine()) ;
                    if (buildingChoice > 0 && buildingChoice < GameData.AVAILABLE_BUILDINGS.size()) {
                        castle.buildBuilding(GameData.AVAILABLE_BUILDINGS.get(buildingChoice));
                        if(GameData.AVAILABLE_BUILDINGS.get(buildingChoice).getCost()<=player.getGold())
                            System.out.println("Постройка " + GameData.AVAILABLE_BUILDINGS.get(buildingChoice) + " построена!");
                    } else {
                        System.out.println("Неверный выбор!");
                    }
                    break;
                case "3":
                    Player.showStats(player, castle, hero);
                    break;
                case "4":
                    Player.showStats(player, castle, hero);
                    map.displayHero(hero);
                    map.display();
                    Player.showStats(player, castle, hero);
                    hero.Move(hero, map, botHero, player, castle);
                    return; // возвращаем управление в gameLoop

                case "5":
                    return;
                default:
                    System.out.println("Неверный выбор!");
            }
        }
    }


}

