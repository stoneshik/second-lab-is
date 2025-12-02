package lab.is.services.insertion;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.apache.commons.csv.CSVRecord;
import org.springframework.util.StringUtils;

import lab.is.bd.entities.Album;
import lab.is.bd.entities.Coordinates;
import lab.is.bd.entities.MusicBand;
import lab.is.bd.entities.MusicGenre;
import lab.is.bd.entities.Studio;
import lab.is.exceptions.CsvParserException;

public class CsvParser {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    private CsvParser() {}

    public static MusicBand convertRecordToEntity(CSVRecord csvRecord, long recordNumber) {
        return MusicBand.builder()
            .id(validateAndGetId(csvRecord, recordNumber))
            .name(validateAndGetName(csvRecord, recordNumber))
            .genre(validateAndGetGenre(csvRecord, recordNumber))
            .numberOfParticipants(validateAndGetNumberOfParticipants(csvRecord, recordNumber))
            .singlesCount(validateAndGetSinglesCount(csvRecord, recordNumber))
            .establishmentDate(validateAndGetEstablishmentDate(csvRecord, recordNumber))
            .description(validateAndGetDescription(csvRecord, recordNumber))
            // вложенные сущности
            .coordinates(validateAndGetCoordinates(csvRecord, recordNumber))
            .studio(validateAndGetStudio(csvRecord, recordNumber))
            .bestAlbum(validateAndGetBestAlbum(csvRecord, recordNumber))
            .build();
    }

    public static Long validateAndGetId(CSVRecord csvRecord, long recordNumber) {
        String idString = csvRecord.get(InsertionHeaders.ID.getName());
        if (!StringUtils.hasText(idString)) {
            throw new CsvParserException("Строка " + recordNumber + ": ID не может быть пустым");
        }
        try {
            long id = Long.parseLong(idString);
            if (id <= 0) {
                throw new CsvParserException("Строка " + recordNumber + ": ID должен быть положительным числом");
            }
            return id;
        } catch (NumberFormatException e) {
            throw new CsvParserException("Строка " + recordNumber + ": Некорректный формат ID: " + idString);
        }
    }

    public static String validateAndGetName(CSVRecord csvRecord, long recordNumber) {
        String name = csvRecord.get(InsertionHeaders.NAME.getName());
        if (!StringUtils.hasText(name) || name.isBlank()) {
            throw new CsvParserException("Строка " + recordNumber + ": Название не может быть пустым");
        }
        if (name.length() > 255) {
            throw new CsvParserException("Строка " + recordNumber + ": Название слишком длинное (макс. 255 символов)");
        }
        return name;
    }

    public static MusicGenre validateAndGetGenre(CSVRecord csvRecord, long recordNumber) {
        String genreString = csvRecord.get(InsertionHeaders.GENRE.getName());
        if (!StringUtils.hasText(genreString)) {
            return null;
        }
        MusicGenre musicGenre;
        try {
            musicGenre = MusicGenre.valueOf(genreString);
        } catch (Exception e) {
            throw new CsvParserException("Строка " + recordNumber + ": Ошибка формата жанра");
        }
        return musicGenre;
    }

    public static Long validateAndGetNumberOfParticipants(CSVRecord csvRecord, long recordNumber) {
        String numberOfParticipantsString = csvRecord.get(InsertionHeaders.NUMBER_OF_PARTICIPANTS.getName());
        if (!StringUtils.hasText(numberOfParticipantsString)) {
            return null;
        }
        try {
            long numberOfParticipants = Long.parseLong(numberOfParticipantsString);
            if (numberOfParticipants <= 0) {
                throw new CsvParserException("Строка " + recordNumber + ": количество участников должно быть положительным числом");
            }
            return numberOfParticipants;
        } catch (NumberFormatException e) {
            throw new CsvParserException("Строка " + recordNumber + ": Некорректный формат количества участников: " + numberOfParticipantsString);
        }
    }

    public static Long validateAndGetSinglesCount(CSVRecord csvRecord, long recordNumber) {
        String singlesCountString = csvRecord.get(InsertionHeaders.SINGLES_COUNT.getName());
        if (!StringUtils.hasText(singlesCountString)) {
            throw new CsvParserException("Строка " + recordNumber + ": Количество синглов не может быть пустым");
        }
        try {
            long numberOfParticipants = Long.parseLong(singlesCountString);
            if (numberOfParticipants <= 0) {
                throw new CsvParserException("Строка " + recordNumber + ": количество синглов должно быть положительным числом");
            }
            return numberOfParticipants;
        } catch (NumberFormatException e) {
            throw new CsvParserException("Строка " + recordNumber + ": Некорректный формат количества синглов: " + singlesCountString);
        }
    }

    public static LocalDate validateAndGetEstablishmentDate(CSVRecord csvRecord, long recordNumber) {
        String establishmentTime = csvRecord.get(InsertionHeaders.ESTABLISHMENT_DATE.getName());
        if (!StringUtils.hasText(establishmentTime)) {
            throw new CsvParserException("Строка " + recordNumber + ": Время основания не может быть пустым: " + establishmentTime);
        }
        try {
            return LocalDate.parse(establishmentTime, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new CsvParserException("Строка " + recordNumber + ": Некорректный формат времени основания: " + establishmentTime);
        }
    }

    public static String validateAndGetDescription(CSVRecord csvRecord, long recordNumber) {
        String description = csvRecord.get(InsertionHeaders.DESCRIPTION.getName());
        if (!StringUtils.hasText(description)) {
            return null;
        }
        if (description.length() > 500) {
            throw new CsvParserException("Строка " + recordNumber + ": Описание слишком длинное (макс. 500 символов)");
        }
        return description;
    }

    public static Coordinates validateAndGetCoordinates(CSVRecord csvRecord, long recordNumber) {
        String xString = csvRecord.get(InsertionHeaders.COORDINATES_X.getName());
        String yString = csvRecord.get(InsertionHeaders.COORDINATES_Y.getName());
        if (!StringUtils.hasText(xString) || !StringUtils.hasText(yString)) {
            throw new CsvParserException("Строка " + recordNumber + ": Координаты x и y не могут быть пустыми");
        }
        try {
            float x = Float.parseFloat(xString);
            int y = Integer.parseInt(yString);
            return Coordinates.builder()
                .x(x)
                .y(y)
                .build();
        } catch (NumberFormatException e) {
            throw new CsvParserException("Строка " + recordNumber + ": Некорректный формат координат: x=" + xString + ", y=" + yString);
        }
    }

    public static Studio validateAndGetStudio(CSVRecord csvRecord, long recordNumber) {
        String studioName = csvRecord.get(InsertionHeaders.STUDIO_NAME.getName());
        String studioAddress = csvRecord.get(InsertionHeaders.STUDIO_ADDRESS.getName());
        if (!StringUtils.hasText(studioName) || !StringUtils.hasText(studioAddress)) {
            throw new CsvParserException("Строка " + recordNumber + ": Название студии и адрес студии не могут быть пустыми");
        }
        if (studioName.length() > 255) {
            throw new CsvParserException("Строка " + recordNumber + ": Название студии слишком длинное (макс. 255 символов)");
        }
        if (studioAddress.length() > 255) {
            throw new CsvParserException("Строка " + recordNumber + ": Адрес студии слишком длинный (макс. 255 символов)");
        }
        return Studio.builder()
            .name(studioName)
            .address(studioAddress)
            .build();
    }

    public static Album validateAndGetBestAlbum(CSVRecord csvRecord, long recordNumber) {
        String bestAlbumNameString = csvRecord.get(InsertionHeaders.BEST_ALBUM_NAME.getName());
        String bestAlbumLengthString = csvRecord.get(InsertionHeaders.BEST_ALBUM_LENGTH.getName());
        if (!StringUtils.hasText(bestAlbumNameString) || !StringUtils.hasText(bestAlbumLengthString)) {
            throw new CsvParserException("Строка " + recordNumber + ": Название и длина лучшего альбома не может быть пустыми");
        }
        if (bestAlbumNameString.length() > 255) {
            throw new CsvParserException("Строка " + recordNumber + ": Название альбома слишком длинное (макс. 255 символов)");
        }
        int bestAlbumLength;
        try {
            bestAlbumLength = Integer.parseInt(bestAlbumLengthString);
            if (bestAlbumLength <= 0) {
                throw new CsvParserException("Строка " + recordNumber + ": длина лучшего альбома должен быть положительным числом");
            }
        } catch (NumberFormatException e) {
            throw new CsvParserException("Строка " + recordNumber + ": Некорректный формат длительности альбома: " + bestAlbumLengthString);
        }
        return Album.builder()
            .name(bestAlbumNameString)
            .length(bestAlbumLength)
            .build();
    }
}
