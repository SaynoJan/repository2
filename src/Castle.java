import java.util.ArrayList;
import java.util.List;

class Castle {
    private String owner;
    private List<Building> buildings;
    private Player player;

    public Castle(String owner, Player player) {
        this.owner = owner;
        this.buildings = new ArrayList<>();
        this.player = player;
    }

    public void addBuilding(Building building) {
        buildings.add(building);
    }

    public boolean hasBuildingOfLevel(int level) {
        for (Building building : buildings) {
            if (building.getLevel() == level) {
                return true;
            }
        }
        return false;
    }

    public void recruitUnit(Unit unit, Hero hero) {
        if (player.spendGold(unit.getCost())) {
            // создаем новый экземпляр юнита с начальными значениями
            Unit newUnit = new Unit(
                    unit.getType(),       // тип юнита
                    unit.getInitialHp(),  // начальное HP
                    unit.getDamage(),
                    unit.getMoveRange(),  // дальность перемещения
                    unit.getAttackRange(),// дальность атаки
                    unit.getCost()
            );
            hero.addUnit(newUnit);
            System.out.println("Юнит " + newUnit.getType() + " нанят и добавлен в армию героя!");
        } else {
            System.out.println("Недостаточно золота для найма юнита " + unit.getType());
        }
    }

    public void buildBuilding(Building building) {
        for (Building existingBuilding : buildings) {
            if (existingBuilding.getName().equals(building.getName())) {
                System.out.println("Здание " + building.getName() + " уже построено!");
                return;
            }
        }

        if (player.spendGold(building.getCost())) {
            buildings.add(building);
            if (!building.equals(GameData.AVAILABLE_BUILDINGS.get(0))&& owner.equals("player") )
                System.out.println("Постройка " + building.getName() + " построена!"); // Сообщение после успешной постройки
        } else {
            System.out.println("Недостаточно золота для постройки " + building.getName());
        }
    }

    @Override
    public String toString() {
        return "Замок " + owner + " с постройками: " + buildings;
    }
}