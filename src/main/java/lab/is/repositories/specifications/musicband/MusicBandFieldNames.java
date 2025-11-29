package lab.is.repositories.specifications.musicband;

import lab.is.repositories.specifications.FieldName;

/*
* все значения по которым есть фильтрация
* name
* genre
* description
* bestAlbum.name
* studio.name
* studio.address
*/
public enum MusicBandFieldNames implements FieldName {
    NAME("name"),
    GENRE("genre"),
    DESCRIPTION("description"),

    BEST_ALBUM("bestAlbum"),
    BEST_ALBUM_NAME("name"),

    STUDIO("studio"),
    STUDIO_NAME("name"),
    STUDIO_ADDRESS("address"),
    ;

    private final String fieldName;

    private MusicBandFieldNames(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
