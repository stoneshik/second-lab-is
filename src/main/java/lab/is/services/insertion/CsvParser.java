package lab.is.services.insertion;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lab.is.bd.entities.Album;
import lab.is.bd.entities.Coordinates;
import lab.is.bd.entities.MusicBand;
import lab.is.bd.entities.MusicGenre;
import lab.is.bd.entities.Studio;
import lab.is.exceptions.CsvParserException;
import lab.is.exceptions.MusicBandExistsException;
import lab.is.services.musicband.MusicBandService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CsvParser {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private final MusicBandService musicBandService;

    public MusicBand convertRecordToEntity(
        CSVRecord csvRecord,
        long recordNumber,
        long insertionHistoryId
    ) {
        return MusicBand.builder()
            .name(validateAndGetName(csvRecord, recordNumber, insertionHistoryId))
            .genre(validateAndGetGenre(csvRecord, recordNumber, insertionHistoryId))
            .numberOfParticipants(validateAndGetNumberOfParticipants(csvRecord, recordNumber, insertionHistoryId))
            .singlesCount(validateAndGetSinglesCount(csvRecord, recordNumber, insertionHistoryId))
            .albumsCount(validateAndGetAlbumsCount(csvRecord, recordNumber, insertionHistoryId))
            .establishmentDate(validateAndGetEstablishmentDate(csvRecord, recordNumber, insertionHistoryId))
            .description(validateAndGetDescription(csvRecord, recordNumber, insertionHistoryId))
            // вложенные сущности
            .coordinates(validateAndGetCoordinates(csvRecord, recordNumber, insertionHistoryId))
            .studio(validateAndGetStudio(csvRecord, recordNumber, insertionHistoryId))
            .bestAlbum(validateAndGetBestAlbum(csvRecord, recordNumber, insertionHistoryId))
            .build();
    }

    public String validateAndGetName(CSVRecord csvRecord, long recordNumber, long insertionHistoryId) {
        String name = csvRecord.get(InsertionHeaders.NAME.getName());
        if (!StringUtils.hasText(name) || name.isBlank()) {
            throw new CsvParserException(
                "Строка " + recordNumber + ": Название не может быть пустым",
                insertionHistoryId,
                recordNumber
            );
        }
        if (name.length() > 255) {
            throw new CsvParserException(
                "Строка " + recordNumber + ": Название слишком длинное (макс. 255 символов)",
                insertionHistoryId,
                recordNumber
            );
        }
        if (musicBandService.existsByName(name)) {
            throw new MusicBandExistsException(
                String.format("музыкальная группа с именем %s уже существует", name)
            );
        }
        return name;
    }

    public MusicGenre validateAndGetGenre(
        CSVRecord csvRecord,
        long recordNumber,
        long insertionHistoryId
    ) {
        String genreString = csvRecord.get(InsertionHeaders.GENRE.getName());
        if (!StringUtils.hasText(genreString)) {
            return null;
        }
        MusicGenre musicGenre;
        try {
            musicGenre = MusicGenre.valueOf(genreString);
        } catch (Exception e) {
            throw new CsvParserException(
                "Строка " + recordNumber + ": Ошибка формата жанра",
                insertionHistoryId,
                recordNumber
            );
        }
        return musicGenre;
    }

    public Long validateAndGetNumberOfParticipants(
        CSVRecord csvRecord,
        long recordNumber,
        long insertionHistoryId
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
                    insertionHistoryId,
                    recordNumber
                );
            }
            return numberOfParticipants;
        } catch (NumberFormatException e) {
            throw new CsvParserException(
                "Строка " + recordNumber + ": Некорректный формат количества участников: " + numberOfParticipantsString,
                insertionHistoryId,
                recordNumber
            );
        }
    }

    public Long validateAndGetSinglesCount(
        CSVRecord csvRecord,
        long recordNumber,
        long insertionHistoryId
    ) {
        String singlesCountString = csvRecord.get(InsertionHeaders.SINGLES_COUNT.getName());
        if (!StringUtils.hasText(singlesCountString)) {
            throw new CsvParserException(
                "Строка " + recordNumber + ": Количество синглов не может быть пустым",
                insertionHistoryId,
                recordNumber
            );
        }
        try {
            long singlesCount = Long.parseLong(singlesCountString);
            if (singlesCount <= 0) {
                throw new CsvParserException(
                    "Строка " + recordNumber + ": количество синглов должно быть положительным числом",
                    insertionHistoryId,
                    recordNumber
                );
            }
            return singlesCount;
        } catch (NumberFormatException e) {
            throw new CsvParserException(
                "Строка " + recordNumber + ": Некорректный формат количества синглов: " + singlesCountString,
                insertionHistoryId,
                recordNumber
            );
        }
    }
    public Long validateAndGetAlbumsCount(
        CSVRecord csvRecord,
        long recordNumber,
        long insertionHistoryId
    ) {
        String albumsCountString = csvRecord.get(InsertionHeaders.ALBUMS_COUNT.getName());
        if (!StringUtils.hasText(albumsCountString)) {
            throw new CsvParserException(
                    "Строка " + recordNumber + ": Количество альбомов не может быть пустым",
                    insertionHistoryId,
                    recordNumber
            );
        }
        try {
            long albumsCount = Long.parseLong(albumsCountString);
            if (albumsCount <= 0) {
                throw new CsvParserException(
                        "Строка " + recordNumber + ": количество альбомов должно быть положительным числом",
                        insertionHistoryId,
                        recordNumber
                );
            }
            return albumsCount;
        } catch (NumberFormatException e) {
            throw new CsvParserException(
                    "Строка " + recordNumber + ": Некорректный формат количества альбомов: " + albumsCountString,
                    insertionHistoryId,
                    recordNumber
            );
        }
    }


    public LocalDate validateAndGetEstablishmentDate(
        CSVRecord csvRecord,
        long recordNumber,
        long insertionHistoryId
    ) {
        String establishmentTime = csvRecord.get(InsertionHeaders.ESTABLISHMENT_DATE.getName());
        if (!StringUtils.hasText(establishmentTime)) {
            throw new CsvParserException(
                "Строка " + recordNumber + ": Время основания не может быть пустым: " + establishmentTime,
                insertionHistoryId,
                recordNumber
            );
        }
        try {
            return LocalDate.parse(establishmentTime, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new CsvParserException(
                "Строка " + recordNumber + ": Некорректный формат времени основания: " + establishmentTime,
                insertionHistoryId,
                recordNumber
            );
        }
    }

    public String validateAndGetDescription(
        CSVRecord csvRecord,
        long recordNumber,
        long insertionHistoryId
    ) {
        String description = csvRecord.get(InsertionHeaders.DESCRIPTION.getName());
        if (!StringUtils.hasText(description)) {
            return null;
        }
        if (description.length() > 500) {
            throw new CsvParserException(
                "Строка " + recordNumber + ": Описание слишком длинное (макс. 500 символов)",
                insertionHistoryId,
                recordNumber
            );
        }
        return description;
    }

    public Coordinates validateAndGetCoordinates(
        CSVRecord csvRecord,
        long recordNumber,
        long insertionHistoryId
    ) {
        String xString = csvRecord.get(InsertionHeaders.COORDINATES_X.getName());
        String yString = csvRecord.get(InsertionHeaders.COORDINATES_Y.getName());
        if (!StringUtils.hasText(xString) || !StringUtils.hasText(yString)) {
            throw new CsvParserException(
                "Строка " + recordNumber + ": Координаты x и y не могут быть пустыми",
                insertionHistoryId,
                recordNumber
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
                insertionHistoryId,
                recordNumber
            );
        }
    }

    public Studio validateAndGetStudio(
        CSVRecord csvRecord,
        long recordNumber,
        long insertionHistoryId
    ) {
        String studioName = csvRecord.get(InsertionHeaders.STUDIO_NAME.getName());
        String studioAddress = csvRecord.get(InsertionHeaders.STUDIO_ADDRESS.getName());
        if (!StringUtils.hasText(studioName) && !StringUtils.hasText(studioAddress)) {
            return null;
        }
        if (!StringUtils.hasText(studioName) || !StringUtils.hasText(studioAddress)) {
            throw new CsvParserException(
                "Строка " + recordNumber + ": Название студии и адрес студии не могут быть пустыми",
                insertionHistoryId,
                recordNumber
            );
        }
        if (studioName.length() > 255) {
            throw new CsvParserException(
                "Строка " + recordNumber + ": Название студии слишком длинное (макс. 255 символов)",
                insertionHistoryId,
                recordNumber
            );
        }
        if (studioAddress.length() > 255) {
            throw new CsvParserException(
                "Строка " + recordNumber + ": Адрес студии слишком длинный (макс. 255 символов)",
                insertionHistoryId,
                recordNumber
            );
        }
        return Studio.builder()
            .name(studioName)
            .address(studioAddress)
            .build();
    }

    public Album validateAndGetBestAlbum(
        CSVRecord csvRecord,
        long recordNumber,
        long insertionHistoryId
    ) {
        String bestAlbumNameString = csvRecord.get(InsertionHeaders.BEST_ALBUM_NAME.getName());
        String bestAlbumLengthString = csvRecord.get(InsertionHeaders.BEST_ALBUM_LENGTH.getName());
        if (!StringUtils.hasText(bestAlbumNameString) && !StringUtils.hasText(bestAlbumLengthString)) {
            return null;
        }
        if (!StringUtils.hasText(bestAlbumNameString) || !StringUtils.hasText(bestAlbumLengthString)) {
            throw new CsvParserException(
                "Строка " + recordNumber + ": Название или длина лучшего альбома не может быть пустыми",
                insertionHistoryId,
                recordNumber
            );
        }
        if (bestAlbumNameString.length() > 255) {
            throw new CsvParserException(
                "Строка " + recordNumber + ": Название альбома слишком длинное (макс. 255 символов)",
                insertionHistoryId,
                recordNumber
            );
        }
        int bestAlbumLength;
        try {
            bestAlbumLength = Integer.parseInt(bestAlbumLengthString);
            if (bestAlbumLength <= 0) {
                throw new CsvParserException(
                    "Строка " + recordNumber + ": длина лучшего альбома должен быть положительным числом",
                    insertionHistoryId,
                    recordNumber
                );
            }
        } catch (NumberFormatException e) {
            throw new CsvParserException(
                "Строка " + recordNumber + ": Некорректный формат длительности альбома: " + bestAlbumLengthString,
                insertionHistoryId,
                recordNumber
            );
        }
        return Album.builder()
            .name(bestAlbumNameString)
            .length(bestAlbumLength)
            .build();
    }
}
