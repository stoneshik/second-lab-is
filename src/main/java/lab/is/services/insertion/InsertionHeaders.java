package lab.is.services.insertion;

public enum InsertionHeaders {
    NAME("Название"),
    GENRE("Жанр"),
    NUMBER_OF_PARTICIPANTS("Участники"),
    SINGLES_COUNT("Синглы"),
    ALBUMS_COUNT("Альбомы"),
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

    public static String[] getHeaders() {
        return new String[] {
            InsertionHeaders.NAME.getName(),
            InsertionHeaders.GENRE.getName(),
            InsertionHeaders.NUMBER_OF_PARTICIPANTS.getName(),
            InsertionHeaders.SINGLES_COUNT.getName(),
            InsertionHeaders.ALBUMS_COUNT.getName(),
            InsertionHeaders.ESTABLISHMENT_DATE.getName(),
            InsertionHeaders.DESCRIPTION.getName(),
            InsertionHeaders.COORDINATES_X.getName(),
            InsertionHeaders.COORDINATES_Y.getName(),
            InsertionHeaders.STUDIO_NAME.getName(),
            InsertionHeaders.STUDIO_ADDRESS.getName(),
            InsertionHeaders.BEST_ALBUM_NAME.getName(),
            InsertionHeaders.BEST_ALBUM_LENGTH.getName()
        };
    }
}
