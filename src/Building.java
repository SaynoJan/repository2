class Building {
    private String name;
    private int level;
    private int cost;

    public Building(String name, int level, int cost) {
        this.name = name;
        this.level = level;
        this.cost = cost;
    }

    public int getLevel() {
        return level;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return name + " (Уровень: " + level + ", Стоимость: " + cost + ")";
    }

    public String getName() {
        return name;
    }
}
