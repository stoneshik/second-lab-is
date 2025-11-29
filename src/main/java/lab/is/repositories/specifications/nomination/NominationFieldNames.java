package lab.is.repositories.specifications.nomination;

import lab.is.repositories.specifications.FieldName;

public enum NominationFieldNames implements FieldName {
    MUSIC_BAND("musicBand"),
    MUSIC_BAND_ID("id"),
    MUSIC_GENRE("musicGenre"),
    NOMINATED_AT("nominatedAt")
    ;

    private final String fieldName;

    private NominationFieldNames(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
