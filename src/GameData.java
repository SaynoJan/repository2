import java.util.List;

class GameData {
    public static final List<Unit> AVAILABLE_UNITS = List.of(
            new Unit("Копейщик", 10, 5, 2, 1, 50), // Уровень 1
            new Unit("Арбалетчик", 8, 7, 2, 3, 60), // Уровень 2
            new Unit("Мечник", 15, 10, 3, 1, 70), // Уровень 3
            new Unit("Кавалерист", 12, 12, 4, 2, 80), // Уровень 4
            new Unit("Паладин", 20, 15, 3, 2, 90) // Уровень 5
    );

    public static final List<Building> AVAILABLE_BUILDINGS = List.of(
            new Building("Сторожевой пост", 1, 0),
            new Building("Башня арбалетчиков", 2, 100),
            new Building("Оружейная", 3, 150),
            new Building("Арена", 4, 200),
            new Building("Собор", 5, 250)
    );
}
