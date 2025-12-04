package lab.is.services.insertion;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.apache.commons.csv.CSVRecord;
import org.springframework.util.StringUtils;

import lab.is.bd.entities.Album;
import lab.is.bd.entities.Coordinates;
import lab.is.bd.entities.InsertionHistory;
import lab.is.bd.entities.MusicBand;
import lab.is.bd.entities.MusicGenre;
import lab.is.bd.entities.Studio;
import lab.is.exceptions.CsvParserException;

public class CsvParser {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    private CsvParser() {}

    public static MusicBand convertRecordToEntity(
        CSVRecord csvRecord,
        long recordNumber,
        InsertionHistory insertionHistory
    ) {
        return MusicBand.builder()
            .name(validateAndGetName(csvRecord, recordNumber, insertionHistory))
            .genre(validateAndGetGenre(csvRecord, recordNumber, insertionHistory))
            .numberOfParticipants(validateAndGetNumberOfParticipants(csvRecord, recordNumber, insertionHistory))
            .singlesCount(validateAndGetSinglesCount(csvRecord, recordNumber, insertionHistory))
            .albumsCount(validateAndGetAlbumsCount(csvRecord, recordNumber, insertionHistory))
            .establishmentDate(validateAndGetEstablishmentDate(csvRecord, recordNumber, insertionHistory))
            .description(validateAndGetDescription(csvRecord, recordNumber, insertionHistory))
            // вложенные сущности
            .coordinates(validateAndGetCoordinates(csvRecord, recordNumber, insertionHistory))
            .studio(validateAndGetStudio(csvRecord, recordNumber, insertionHistory))
            .bestAlbum(validateAndGetBestAlbum(csvRecord, recordNumber, insertionHistory))
            .build();
    }

    public static String validateAndGetName(CSVRecord csvRecord, long recordNumber, InsertionHistory insertionHistory) {
        String name = csvRecord.get(InsertionHeaders.NAME.getName());
        if (!StringUtils.hasText(name) || name.isBlank()) {
            throw new CsvParserException(
                "Строка " + recordNumber + ": Название не может быть пустым",
                insertionHistory
            );
        }
        if (name.length() > 255) {
            throw new CsvParserException(
                "Строка " + recordNumber + ": Название слишком длинное (макс. 255 символов)",
                insertionHistory
            );
        }
        return name;
    }

    public static MusicGenre validateAndGetGenre(
        CSVRecord csvRecord,
        long recordNumber,
        InsertionHistory insertionHistory
    ) {
        String genreString = csvRecord.get(InsertionHeaders.GENRE.getName());
        if (!StringUtils.hasText(genreString)) {
            return null;
        }
        MusicGenre musicGenre;
        try {
            musicGenre = MusicGenre.valueOf(genreString);
        } catch (Exception e) {
            throw new CsvParserException("Строка " + recordNumber + ": Ошибка формата жанра", insertionHistory);
        }
        return musicGenre;
    }

    public static Long validateAndGetNumberOfParticipants(
        CSVRecord csvRecord,
        long recordNumber,
        InsertionHistory insertionHistory
    ) {
        String numberOfParticipantsString = csvRecord.get(InsertionHeaders.NUMBER_OF_PARTICIPANTS.getName());
        if (!StringUtils.hasText(numberOfParticipantsString)) {
            return null;
        }
        try {
            long numberOfParticipants = Long.parseLong(numberOfParticipantsString);
            if (numberOfParticipants <= 0) {
                throw new CsvParserException(
                    "Строка " + recordNumber + ": количество участников должно быть положительным числом",
                    insertionHistory
                );
            }
            return numberOfParticipants;
        } catch (NumberFormatException e) {
            throw new CsvParserException(
                "Строка " + recordNumber + ": Некорректный формат количества участников: " + numberOfParticipantsString,
                insertionHistory
            );
        }
    }

    public static Long validateAndGetSinglesCount(
        CSVRecord csvRecord,
        long recordNumber,
        InsertionHistory insertionHistory
    ) {
        String singlesCountString = csvRecord.get(InsertionHeaders.SINGLES_COUNT.getName());
        if (!StringUtils.hasText(singlesCountString)) {
            throw new CsvParserException(
                "Строка " + recordNumber + ": Количество синглов не может быть пустым",
                insertionHistory
            );
        }
        try {
            long singlesCount = Long.parseLong(singlesCountString);
            if (singlesCount <= 0) {
                throw new CsvParserException(
                    "Строка " + recordNumber + ": количество синглов должно быть положительным числом",
                    insertionHistory
                );
            }
            return singlesCount;
        } catch (NumberFormatException e) {
            throw new CsvParserException(
                "Строка " + recordNumber + ": Некорректный формат количества синглов: " + singlesCountString,
                insertionHistory
            );
        }
    }
    public static Long validateAndGetAlbumsCount(
            CSVRecord csvRecord,
            long recordNumber,
            InsertionHistory insertionHistory
    ) {
        String albumsCountString = csvRecord.get(InsertionHeaders.ALBUMS_COUNT.getName());
        if (!StringUtils.hasText(albumsCountString)) {
            throw new CsvParserException(
                    "Строка " + recordNumber + ": Количество альбомов не может быть пустым",
                    insertionHistory
            );
        }
        try {
            long albumsCount = Long.parseLong(albumsCountString);
            if (albumsCount <= 0) {
                throw new CsvParserException(
                        "Строка " + recordNumber + ": количество альбомов должно быть положительным числом",
                        insertionHistory
                );
            }
            return albumsCount;
        } catch (NumberFormatException e) {
            throw new CsvParserException(
                    "Строка " + recordNumber + ": Некорректный формат количества альбомов: " + albumsCountString,
                    insertionHistory
            );
        }
    }


    public static LocalDate validateAndGetEstablishmentDate(
        CSVRecord csvRecord,
        long recordNumber,
        InsertionHistory insertionHistory
    ) {
        String establishmentTime = csvRecord.get(InsertionHeaders.ESTABLISHMENT_DATE.getName());
        if (!StringUtils.hasText(establishmentTime)) {
            throw new CsvParserException(
                "Строка " + recordNumber + ": Время основания не может быть пустым: " + establishmentTime,
                insertionHistory
            );
        }
        try {
            return LocalDate.parse(establishmentTime, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new CsvParserException(
                "Строка " + recordNumber + ": Некорректный формат времени основания: " + establishmentTime,
                insertionHistory
            );
        }
    }

    public static String validateAndGetDescription(
        CSVRecord csvRecord,
        long recordNumber,
        InsertionHistory insertionHistory
    ) {
        String description = csvRecord.get(InsertionHeaders.DESCRIPTION.getName());
        if (!StringUtils.hasText(description)) {
            return null;
        }
        if (description.length() > 500) {
            throw new CsvParserException(
                "Строка " + recordNumber + ": Описание слишком длинное (макс. 500 символов)",
                insertionHistory
            );
        }
        return description;
    }

    public static Coordinates validateAndGetCoordinates(
        CSVRecord csvRecord,
        long recordNumber,
        InsertionHistory insertionHistory
    ) {
        String xString = csvRecord.get(InsertionHeaders.COORDINATES_X.getName());
        String yString = csvRecord.get(InsertionHeaders.COORDINATES_Y.getName());
        if (!StringUtils.hasText(xString) || !StringUtils.hasText(yString)) {
            throw new CsvParserException(
                "Строка " + recordNumber + ": Координаты x и y не могут быть пустыми",
                insertionHistory
            );
        }
        try {
            float x = Float.parseFloat(xString);
            int y = Integer.parseInt(yString);
            return Coordinates.builder()
                .x(x)
                .y(y)
                .build();
        } catch (NumberFormatException e) {
            throw new CsvParserException(
                "Строка " + recordNumber + ": Некорректный формат координат: x=" + xString + ", y=" + yString,
                insertionHistory
            );
        }
    }

    public static Studio validateAndGetStudio(
        CSVRecord csvRecord,
        long recordNumber,
        InsertionHistory insertionHistory
    ) {
        String studioName = csvRecord.get(InsertionHeaders.STUDIO_NAME.getName());
        String studioAddress = csvRecord.get(InsertionHeaders.STUDIO_ADDRESS.getName());
        if (!StringUtils.hasText(studioName) && !StringUtils.hasText(studioAddress)) {
            return null;
        }
        if (!StringUtils.hasText(studioName) || !StringUtils.hasText(studioAddress)) {
            throw new CsvParserException(
                "Строка " + recordNumber + ": Название студии и адрес студии не могут быть пустыми",
                insertionHistory
            );
        }
        if (studioName.length() > 255) {
            throw new CsvParserException(
                "Строка " + recordNumber + ": Название студии слишком длинное (макс. 255 символов)",
                insertionHistory
            );
        }
        if (studioAddress.length() > 255) {
            throw new CsvParserException(
                "Строка " + recordNumber + ": Адрес студии слишком длинный (макс. 255 символов)",
                    insertionHistory
                );
        }
        return Studio.builder()
            .name(studioName)
            .address(studioAddress)
            .build();
    }

    public static Album validateAndGetBestAlbum(
        CSVRecord csvRecord,
        long recordNumber,
        InsertionHistory insertionHistory
    ) {
        String bestAlbumNameString = csvRecord.get(InsertionHeaders.BEST_ALBUM_NAME.getName());
        String bestAlbumLengthString = csvRecord.get(InsertionHeaders.BEST_ALBUM_LENGTH.getName());
        if (!StringUtils.hasText(bestAlbumNameString) && !StringUtils.hasText(bestAlbumLengthString)) {
            return null;
        }
        if (!StringUtils.hasText(bestAlbumNameString) || !StringUtils.hasText(bestAlbumLengthString)) {
            throw new CsvParserException(
                "Строка " + recordNumber + ": Название или длина лучшего альбома не может быть пустыми",
                insertionHistory
            );
        }
        if (bestAlbumNameString.length() > 255) {
            throw new CsvParserException(
                "Строка " + recordNumber + ": Название альбома слишком длинное (макс. 255 символов)",
                insertionHistory
            );
        }
        int bestAlbumLength;
        try {
            bestAlbumLength = Integer.parseInt(bestAlbumLengthString);
            if (bestAlbumLength <= 0) {
                throw new CsvParserException(
                    "Строка " + recordNumber + ": длина лучшего альбома должен быть положительным числом",
                    insertionHistory
                );
            }
        } catch (NumberFormatException e) {
            throw new CsvParserException(
                "Строка " + recordNumber + ": Некорректный формат длительности альбома: " + bestAlbumLengthString,
                insertionHistory
            );
        }
        return Album.builder()
            .name(bestAlbumNameString)
            .length(bestAlbumLength)
            .build();
    }
}
