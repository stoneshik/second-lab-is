package lab.is.services.insertion;

public enum InsertionHeaders {
    ID("ID"),
    NAME("Название"),
    GENRE("Жанр"),
    NUMBER_OF_PARTICIPANTS("Участники"),
    SINGLES_COUNT("Синглы"),
    ESTABLISHMENT_DATE("Дата основания"),
    DESCRIPTION("Описание"),
    COORDINATES_X("Координаты.x"),
    COORDINATES_Y("Координаты.y"),
    STUDIO_NAME("Студия.Название"),
    STUDIO_ADDRESS("Студия.Адрес"),
    BEST_ALBUM_NAME("Лучший альбом.Название"),
    BEST_ALBUM_LENGTH("Лучший альбом.Длительность");

    private final String name;

    InsertionHeaders(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
