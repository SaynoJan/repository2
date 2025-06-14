import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Hero {
    private String name;
    private List<Unit> army;
    private int x;
    private int y;
    private String owner;
    private int hp;
    private int damage;

    public Hero(String name, int x, int y, String owner, int hp, int damage) {
        this.name = name;
        this.army = new ArrayList<>();
        this.x = x;
        this.y = y;
        this.owner = owner;
        this.hp = hp;
        this.damage = damage;
    }
    public String getCoords(Hero hero){
        return "Положение "+ hero+":\nx= "+x+"\ny= "+y;
    }
    public int getCoordx(Hero hero){
        return x;
    }
    public int getCoordy(Hero hero){
        return y;
    }
    public void Move(Hero hero, Map map, Hero botHero, Player player, Castle castle) {
        Scanner scanner = new Scanner(System.in);

        // восстанавливаем клетку с которой герой ушел
        map.updateCell(hero.y, hero.x, map.getMapCopyCell(hero.y, hero.x));

        System.out.println("Введите w, a, s или d:");
        String symbol = scanner.nextLine();

        int oldX = hero.x;
        int oldY = hero.y;

        switch (symbol) {
            case "w":
                hero.y -= 1;
                break;
            case "a":
                hero.x -= 1;
                break;
            case "s":
                hero.y += 1;
                break;
            case "d":
                hero.x += 1;
                break;
            default:
                System.out.println("Неверный выбор!");
                return;
        }


        if (hero.x < 0 || hero.x >= map.getWidth() || hero.y < 0 || hero.y >= map.getHeight()) {
            System.out.println("Герой не может выйти за пределы карты!");
            hero.x = oldX; //старые координаты
            hero.y = oldY;
            return;
        }
        if (map.getCell(hero.x, hero.y) == Map.BALOON) {
            // обработка столкновения с шаром
            handleBaloonCollision(hero, map, player,botHero);
            return; // прерываем дальнейшее перемещение
        }
        if (map.getCell(hero.x, hero.y) == Map.OBSTACLE_PLAYER) {
            System.out.println("Герой игрока попал в болото! Герой и его юниты теряют по 1 HP.");

            hero.setHp(hero.getHp() - 1);
            System.out.println("Герой теряет 1 HP. Текущее здоровье: " + hero.getHp());

            for (Unit unit : hero.getArmy()) {
                unit.setHp(unit.getHp() - 1);
                System.out.println("Юнит " + unit.getType() + " теряет 1 HP. Текущее здоровье: " + unit.getHp());
            }
            hero.getArmy().removeIf(unit -> unit.getHp() <= 0);

            // if здоровье героя упало до 0
            if (hero.getHp() <= 0) {
                System.out.println("Герой игрока погиб! Игра окончена.");
                System.exit(0);
            }
        }
        if (hero.x == 0 && hero.y == 0) {
            System.out.println("Герой игрока входит в замок!");
            Main.enterCastleMenu(player, hero, castle, map, botHero);
            return;
        }

        // if на новой клетке юнит или герой бота
        char cell = map.getCell(hero.x, hero.y);
        if (map.evilGuyCheck(hero,botHero)) {
            System.out.println("Столкновение с врагом! Начинается битва!");

            // с кем столкновение
            Unit enemyUnit = null;
            if (cell == Map.HEROBOT) {
                // создаем временного "юнита" для боя
                enemyUnit = new Unit("Герой бота", 50, 10, 3, 2, 0);
            } else {
                for (Unit unit : botHero.getArmy()) {
                    if (unit.getType().equals(getUnitTypeFromSymbol(cell))) {
                        enemyUnit = unit;
                        break;
                    }
                }
            }

            if (enemyUnit != null) {
                // бой
                boolean playerWins = startBattle(hero, enemyUnit);

                if (playerWins) {
                    if (cell == Map.HEROBOT) {
                        System.out.println("Герой бота побеждён! Игра завершена.");
                        System.exit(0);
                    } else {
                        System.out.println("Юнит врага побеждён! Вы получаете " + enemyUnit.getCost() + " золота.");
                        player.addGold(enemyUnit.getCost());
                        botHero.getArmy().remove(enemyUnit); // удаление убитого юнита из армии бота

                        // if армия бота пуста герой бота выходит из замка
                        if (botHero.getArmy().isEmpty() && botHero.x == 8 && botHero.y == 8) {
                            System.out.println("Герой бота выходит из замка!");
                            botHero.x = 7;
                            botHero.y = 7;
                            map.displayBotHero(botHero);
                        }
                    }
                }
            }

            return;
        }
        if (botHero.getArmy().isEmpty() && botHero.x == 8 && botHero.y == 8) {
            //System.out.println("Герой бота выходит из замка!");
            //botHero.x = 7;
            //botHero.y = 7;
            //map.displayHero(botHero);
        }

        map.displayHero(hero);
        map.display();
    }

    private String getUnitTypeFromSymbol(char symbol) {
        switch (symbol) {
            case Map.KOPEICHIKBOT:
                return "Копейщик";
            case Map.ARBALETCHIKBOT:
                return "Арбалетчик";
            case Map.MECHNIKBOT:
                return "Мечник";
            case Map.KAVALERISTBOT:
                return "Кавалерист";
            case Map.PALADINBOT:
                return "Паладин";
            default:
                return "";
        }
    }
    private void handleBaloonCollision(Hero hero, Map map, Player player,Hero botHero) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Вы нашли воздушный шар! Хотите переместиться в любую точку на карте?");
        System.out.println("1: Да");
        System.out.println("2: Нет");
        String choice = scanner.nextLine();

        if (choice.equals("1")) {
            int cost = player.getTeleportationUsed() ? 100 : 0;
            if (!player.getTeleportationUsed()) {
                player.setTeleportationUsed(true);
            }
            if (player.getGold() >= cost) {
                if (cost > 0) {
                    player.spendGold(cost);
                }

                System.out.println("Введите координаты для перемещения (x y):");
                int newX = scanner.nextInt();
                int newY = scanner.nextInt();
                scanner.nextLine();

                if (newX >= 0 && newX < map.getWidth() && newY >= 0 && newY < map.getHeight() &&
                        !(newX == 8 && newY == 8)) {
                    map.updateCell(hero.y, hero.x, map.getMapCopyCell(hero.y, hero.x));

                    int baloonX = map.getBaloon().getX();
                    int baloonY = map.getBaloon().getY();

                    hero.x = newX;
                    hero.y = newY;

                    if (map.evilGuyCheck(hero,botHero)) {
                        System.out.println("Столкновение с врагом! Начинается битва!");


                        Unit enemyUnit = null;
                        if (hero.x == map.getBotHero().getCoordx(map.getBotHero()) && hero.y == map.getBotHero().getCoordy(map.getBotHero())) {

                            enemyUnit = new Unit("Герой бота", 50, 10, 3, 2, 0);
                        } else {

                            for (Unit unit : map.getBotHero().getArmy()) {
                                if (hero.x == unit.getX() && hero.y == unit.getY()) {
                                    enemyUnit = unit;
                                    break;
                                }
                            }
                        }
                        if (enemyUnit != null) {
                            // бой
                            boolean playerWins = startBattle(hero, enemyUnit);

                            if (playerWins) {
                                if (enemyUnit.getType().equals("Герой бота")) {
                                    // победа над героем бота
                                    System.out.println("Герой бота побеждён! Игра завершена.");
                                    System.exit(0);
                                } else {
                                    // победа над юнитом бота
                                    System.out.println("Юнит врага побеждён! Вы получаете " + enemyUnit.getCost() + " золота.");
                                    player.addGold(enemyUnit.getCost());
                                    map.getBotHero().getArmy().remove(enemyUnit); // Удаляем убитого юнита из армии бота

                                    // обновляем карту чтобы убрать символ убитого юнита
                                    map.updateCell(hero.y, hero.x, Map.NEUTRAL);
                                }
                            } else {
                                System.out.println("Герой игрока проиграл битву! Игра окончена.");
                                System.exit(0);
                            }
                        }
                    }
                    // возвращаем шар на место
                    map.getBaloon().setX(baloonX);
                    map.getBaloon().setY(baloonY);

                    map.updateCell(map.getBaloon().getY(), map.getBaloon().getX(), Map.BALOON);
                    map.displayHero(hero);
                    map.display();

                } else {
                    System.out.println("Нельзя переместиться в замок бота или за пределы карты!");
                }
            } else {
                System.out.println("Недостаточно золота для перемещения!");
            }
        }
    }
    public boolean startBattle(Hero heroPlayer, Unit enemyUnit) {
        System.out.println("Начинается битва с " + enemyUnit.getType() + "!");

        // бой продолжается пока герой игрока жив и враг не побежден
        while (heroPlayer.getHp() > 0 && enemyUnit.getHp() > 0) {
                                              // атака юнитов игрока
            if (!heroPlayer.getArmy().isEmpty()) {
                for (Unit playerUnit : heroPlayer.getArmy()) {
                    enemyUnit.setHp(enemyUnit.getHp() - playerUnit.getDamage());
                    System.out.println(playerUnit.getType() + " атакует " + enemyUnit.getType() + "!");
                    if (enemyUnit.getHp() <= 0) {
                        System.out.println(enemyUnit.getType() + " побеждён!");
                        return true; // игрок победил
                    }
                }
            } else {
                // if юнитов нет герой атакует сам
                enemyUnit.setHp(enemyUnit.getHp() - heroPlayer.getDamage());
                System.out.println("Герой " + heroPlayer.getName() + " атакует " + enemyUnit.getType() + "!");
                if (enemyUnit.getHp() <= 0) {
                    System.out.println(enemyUnit.getType() + " побеждён!");
                    return true; // победа игрока
                }
            }

            // атака бота
            if (enemyUnit.getHp() > 0) {
                if (!heroPlayer.getArmy().isEmpty()) {
                    // атакует первого юнита игрока
                    Unit playerUnit = heroPlayer.getArmy().get(0);
                    playerUnit.setHp(playerUnit.getHp() - enemyUnit.getDamage());
                    System.out.println(enemyUnit.getType() + " атакует " + playerUnit.getType() + "!");
                    if (playerUnit.getHp() <= 0) {
                        System.out.println(playerUnit.getType() + " побеждён!");
                        heroPlayer.getArmy().remove(playerUnit); //удаление мертвого юнита игрока
                    }
                } else {
                    // юнитов нет - враг атакует героя
                    heroPlayer.setHp(heroPlayer.getHp() - enemyUnit.getDamage());
                    System.out.println(enemyUnit.getType() + " атакует героя " + heroPlayer.getName() + "!");
                    if (heroPlayer.getHp() <= 0) {
                        System.out.println("Герой " + heroPlayer.getName() + " побеждён!");
                        System.out.println("Игра окончена! Вы проиграли.");
                        System.exit(0);
                    }
                }
            }
        }

        return heroPlayer.getHp() > 0;
    }
    public void addUnit(Unit unit) {
        army.add(unit);
    }
    public int getHp(){
        return hp;
    }
    public int getDamage(){
        return damage;
    }
    public String getName(){
        return name;
    }
    public void setHp(int newHp) {
        this.hp = newHp;
        if (this.hp < 0) {
            this.hp = 0;
        }
    }

    public List<Unit> getArmy() {
        return army;
    }

    @Override
    public String toString() {
        return name + "\nHP = "+hp;
    }
    public void setx(int newx) {
        this.x = newx;
        if (this.x < 0) {
            this.x = 0;
        }
    }
    public void sety(int newy) {
        this.y = newy;
        if (this.y < 0) {
            this.y = 0;
        }
    }
}

