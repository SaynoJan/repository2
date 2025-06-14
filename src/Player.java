import java.util.Scanner;

class Player {
    private int gold;
    private boolean teleportationUsed = false;

    public Player(int initialGold) {
        this.gold = initialGold;
    }
    public static void showStats(Player player, Castle playerCastle, Hero hero){
        System.out.println(player);
        System.out.println(playerCastle);
        System.out.println(hero);
        if (hero.getArmy().isEmpty()) {
            System.out.println("Армия пуста.");
        } else {
            for (Unit unit : hero.getArmy()) {
                System.out.println(unit);
            }
        }

    }

    public int getGold() {
        return gold;
    }
    public boolean getTeleportationUsed(){
        return teleportationUsed;
    }
    public void setTeleportationUsed(boolean hazbek){
        teleportationUsed = hazbek;
    }

    public void addGold(int amount) {
        gold += amount;
    }

    public boolean spendGold(int amount) {
        if (gold >= amount) {
            gold -= amount;
            return true;
        }
        return false;
    }


    @Override
    public String toString() {
        return "Золото: " + gold;
    }
}