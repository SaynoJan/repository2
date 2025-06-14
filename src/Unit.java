class Unit {
    private String type;
    private int hp;
    private int damage;
    private final int initialHp;
    private int moveRange;
    private int attackRange;
    private int cost;
    private int x;
    private int y;

    public Unit(String type, int hp, int damage, int moveRange, int attackRange, int cost) {
        this.type = type;
        this.hp = hp;
        this.initialHp = hp;
        this.damage = damage;
        this.moveRange = moveRange;
        this.attackRange = attackRange;
        this.cost = cost;

    }
    public void setHp(int newHp) {
        this.hp = newHp;
        if (this.hp < 0) {
            this.hp = 0;
        }
    }
    public int getCost() {
        return cost;
    }

    public String getType(){
        return type;
    }
    public int getInitialHp() {
        return initialHp;
    }

    public int getDamage(){
        return damage;
    }
    public int getHp(){
        return hp;
    }
    public int getMoveRange(){
        return moveRange;
    }
    public int getAttackRange(){
        return attackRange;
    }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public int getX(){
        return x;
    }
    public int getY(){ return y;}
    @Override
    public String toString() {
        return type + " (ХП: " + hp + ", Урон: " + damage + ", Перемещение: " + moveRange + ", Атака: " + attackRange + ", Цена " + cost + ")";
    }
}