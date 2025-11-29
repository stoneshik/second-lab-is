package lab.is;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.junit.jupiter.Testcontainers;

import lab.is.bd.entities.MusicGenre;
import lab.is.dto.responses.albums.AlbumResponseDto;
import lab.is.dto.responses.coordinates.CoordinatesResponseDto;
import lab.is.dto.responses.musicband.MusicBandResponseDto;
import lab.is.dto.responses.studios.StudioResponseDto;

@Testcontainers
class MusicBandControllerCrudTest extends AbstractMusicBandTest {
    protected String getEndpointGettingEntityById() {
        return "/api/v1/music-bands/{id}";
    }

    @Test
    void createMusicBand_ReturnsResponseWithStatusCreated() throws Exception {
        setupDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .post("/api/v1/music-bands")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "name": "created music band",
                    "coordinates": {
                        "x": 123456.0,
                        "y": 2147483647
                    },
                    "coordinatesId": null,
                    "genre": "BRIT_POP",
                    "numberOfParticipants": 9223372036854775807,
                    "singlesCount": 9223372036854775807,
                    "description": "",
                    "bestAlbum": {
                        "name": "new album",
                        "length": 2147483647
                    },
                    "bestAlbumId": null,
                    "albumsCount": 9223372036854775807,
                    "establishmentDate": "2021-01-01",
                    "studio": {
                        "name": "",
                        "address": ""
                    },
                    "studioId": null
                }
                """
            );

        mockMvc
            .perform(requestBuilder)
            .andExpectAll(
                status().isCreated()
            );

        checkEntityExistByIdAndEqualExpectedMusicBandEntity(
            MusicBandResponseDto.builder()
                .id(3L)
                .name("created music band")
                .coordinates(
                    CoordinatesResponseDto.builder()
                        .id(4L)
                        .x(123456.0f)
                        .y(2147483647)
                        .build()
                )
                .genre(MusicGenre.BRIT_POP)
                .numberOfParticipants(9223372036854775807L)
                .singlesCount(9223372036854775807L)
                .description("")
                .bestAlbum(
                    AlbumResponseDto.builder()
                        .id(4L)
                        .name("new album")
                        .length(2147483647)
                        .build()
                )
                .albumsCount(9223372036854775807L)
                .establishmentDate(LocalDate.of(2021, 1, 1))
                .studio(
                    StudioResponseDto.builder()
                        .id(3L)
                        .name("")
                        .address("")
                        .build()
                )
                .build()
        );
    }

    @Test
    void createMusicBand_ReturnsResponseWithStatusBadRequest() throws Exception {
        setupDb();
        final Long id = 3L;

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .post("/api/v1/music-bands")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "name": "",
                    "coordinates": {
                        "x": 123456.0,
                        "y": 2147483648
                    },
                    "coordinatesId": null,
                    "genre": "BRIT_POP",
                    "numberOfParticipants": 9223372036854775808,
                    "singlesCount": 0,
                    "description": "",
                    "bestAlbum": {
                        "name": "first album",
                        "length": 2147483647
                    },
                    "bestAlbumId": null,
                    "albumsCount": -10,
                    "establishmentDate": "2021-01-01",
                    "studio": {
                        "name": "",
                        "address": ""
                    },
                    "studioId": null
                }
                """
            );

        mockMvc
            .perform(requestBuilder)
            .andExpectAll(
                status().isBadRequest()
            );

        checkEntityNotExistsById(id);
    }

    @Test
    void createMusicBandWithSubObjectsIds_ReturnsResponseWithStatusCreated() throws Exception {
        setupDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .post("/api/v1/music-bands")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "name": "created music band",
                    "coordinates": null,
                    "coordinatesId": 1,
                    "genre": "BRIT_POP",
                    "numberOfParticipants": 9223372036854775807,
                    "singlesCount": 9223372036854775807,
                    "description": "",
                    "bestAlbum": null,
                    "bestAlbumId": 2,
                    "albumsCount": 9223372036854775807,
                    "establishmentDate": "2021-01-01",
                    "studio": null,
                    "studioId": 1
                }
                """
            );

        mockMvc
            .perform(requestBuilder)
            .andExpectAll(
                status().isCreated()
            );

        checkEntityExistByIdAndEqualExpectedMusicBandEntity(
            MusicBandResponseDto.builder()
                .id(3L)
                .name("created music band")
                .coordinates(
                    CoordinatesResponseDto.builder()
                        .id(1L)
                        .x(1.0f)
                        .y(2)
                        .build()
                )
                .genre(MusicGenre.BRIT_POP)
                .numberOfParticipants(9223372036854775807L)
                .singlesCount(9223372036854775807L)
                .description("")
                .bestAlbum(
                    AlbumResponseDto.builder()
                        .id(2L)
                        .name("second album")
                        .length(1000)
                        .build()
                )
                .albumsCount(9223372036854775807L)
                .establishmentDate(LocalDate.of(2021, 1, 1))
                .studio(
                    StudioResponseDto.builder()
                        .id(1L)
                        .name("first studio")
                        .address("first studio address")
                        .build()
                )
                .build()
        );
    }

    @Test
    void createMusicBandWithSubObjectsIds_ReturnsResponseWithStatusNotFound() throws Exception {
        setupDb();
        final Long id = 3L;

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .post("/api/v1/music-bands")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "name": "created music band",
                    "coordinates": null,
                    "coordinatesId": 4,
                    "genre": "BRIT_POP",
                    "numberOfParticipants": 9223372036854775807,
                    "singlesCount": 9223372036854775807,
                    "description": "",
                    "bestAlbum": null,
                    "bestAlbumId": 2,
                    "albumsCount": 9223372036854775807,
                    "establishmentDate": "2021-01-01",
                    "studio": null,
                    "studioId": 1
                }
                """
            );

        mockMvc
            .perform(requestBuilder)
            .andExpectAll(
                status().isNotFound()
            );

        checkEntityNotExistsById(id);
    }

    @Test
    void getMusicBandById_ReturnsResponseWithStatusOk() throws Exception {
        setupDb();
        checkEntityExistByIdAndEqualExpectedMusicBandEntity(
            MusicBandResponseDto.builder()
                .id(1L)
                .name("first band")
                .coordinates(
                    CoordinatesResponseDto.builder()
                        .id(1L)
                        .x(1.0f)
                        .y(2)
                        .build()
                )
                .genre(MusicGenre.PROGRESSIVE_ROCK)
                .numberOfParticipants(4L)
                .singlesCount(5L)
                .description("first band description")
                .bestAlbum(
                    AlbumResponseDto.builder()
                        .id(1L)
                        .name("first album")
                        .length(12)
                        .build()
                )
                .albumsCount(2L)
                .establishmentDate(LocalDate.of(2024, 8, 3))
                .studio(
                    StudioResponseDto.builder()
                        .id(1L)
                        .name("first studio")
                        .address("first studio address")
                        .build()
                )
                .build()
        );
    }

    @Test
    void getMusicBandById_ReturnsResponseWithStatusNotFound() throws Exception {
        setupDb();
        final Long id = 100L;
        checkEntityNotExistsById(id);
    }

    @Test
    void putMusicBandById_ReturnsResponseWithStatusNoContent() throws Exception {
        setupDb();
        final Long id = 1L;

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .put("/api/v1/music-bands/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "name": "012345678901234567890123456789",
                    "coordinates": {
                        "x": -100.12314,
                        "y": -2147483648
                    },
                    "coordinatesId": null,
                    "genre": "POST_PUNK",
                    "numberOfParticipants": 9223372036854775807,
                    "singlesCount": 9223372036854775807,
                    "description": null,
                    "bestAlbum": null,
                    "bestAlbumId": null,
                    "albumsCount": 9223372036854775807,
                    "establishmentDate": "2024-08-03",
                    "studio": null,
                    "studioId": null
                }
                """
            );

        mockMvc
            .perform(requestBuilder)
            .andExpectAll(
                status().isNoContent()
            );

        checkEntityExistByIdAndEqualExpectedMusicBandEntity(
            MusicBandResponseDto.builder()
                .id(1L)
                .name("012345678901234567890123456789")
                .coordinates(
                    CoordinatesResponseDto.builder()
                        .id(4L)
                        .x(-100.12314f)
                        .y(-2147483648)
                        .build()
                )
                .genre(MusicGenre.POST_PUNK)
                .numberOfParticipants(9223372036854775807L)
                .singlesCount(9223372036854775807L)
                .description(null)
                .bestAlbum(null)
                .albumsCount(9223372036854775807L)
                .establishmentDate(LocalDate.of(2024, 8, 3))
                .studio(null)
                .build()
        );
    }

    @Test
    void putMusicBandById_ReturnsResponseWithStatusNotFound() throws Exception {
        setupDb();
        final Long id = 100L;
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .put("/api/v1/music-bands/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "name": "012345678901234567890123456789",
                    "coordinates": {
                        "x": -100.12314,
                        "y": -2147483648
                    },
                    "coordinatesId": null,
                    "genre": "POST_PUNK",
                    "numberOfParticipants": 9223372036854775807,
                    "singlesCount": 9223372036854775807,
                    "description": null,
                    "bestAlbum": null,
                    "bestAlbumId": null,
                    "albumsCount": 9223372036854775807,
                    "establishmentDate": "2024-08-03",
                    "studio": null,
                    "studioId": null
                }
                """
            );

        mockMvc
            .perform(requestBuilder)
            .andExpectAll(
                status().isNotFound()
            );

        checkEntityNotExistsById(id);
    }

    @Test
    void putMusicBandById_ReturnsResponseWithStatusBadRequest() throws Exception {
        setupDb();
        final Long id = 1L;
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .put("/api/v1/music-bands/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "name": "",
                    "coordinates": {
                        "x": 123456.0,
                        "y": 2147483648
                    },
                    "coordinatesId": null,
                    "genre": "BRIT_POP",
                    "numberOfParticipants": 9223372036854775808,
                    "singlesCount": 0,
                    "description": "",
                    "bestAlbum": {
                        "name": "first album",
                        "length": 2147483647
                    },
                    "bestAlbumId": null,
                    "albumsCount": -10,
                    "establishmentDate": "2021-01-01",
                    "studio": {
                        "name": "",
                        "address": ""
                    },
                    "studioId": null
                }
                """
            );

        mockMvc
            .perform(requestBuilder)
            .andExpectAll(
                status().isBadRequest()
            );

        checkEntityExistByIdAndEqualExpectedMusicBandEntity(
            MusicBandResponseDto.builder()
                .id(1L)
                .name("first band")
                .coordinates(
                    CoordinatesResponseDto.builder()
                        .id(1L)
                        .x(1.0f)
                        .y(2)
                        .build()
                )
                .genre(MusicGenre.PROGRESSIVE_ROCK)
                .numberOfParticipants(4L)
                .singlesCount(5L)
                .description("first band description")
                .bestAlbum(
                    AlbumResponseDto.builder()
                        .id(1L)
                        .name("first album")
                        .length(12)
                        .build()
                )
                .albumsCount(2L)
                .establishmentDate(LocalDate.of(2024, 8, 3))
                .studio(
                    StudioResponseDto.builder()
                        .id(1L)
                        .name("first studio")
                        .address("first studio address")
                        .build()
                )
                .build()
        );
    }

    @Test
    void putMusicBandByIdWithSubObjectsIds_ReturnsResponseWithStatusNoContent() throws Exception {
        setupDb();
        final Long id = 1L;
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .put("/api/v1/music-bands/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "name": "012345678901234567890123456789",
                    "coordinates": null,
                    "coordinatesId": 1,
                    "genre": "POST_PUNK",
                    "numberOfParticipants": 9223372036854775807,
                    "singlesCount": 9223372036854775807,
                    "description": null,
                    "bestAlbum": null,
                    "bestAlbumId": 1,
                    "albumsCount": 9223372036854775807,
                    "establishmentDate": "2024-08-03",
                    "studio": null,
                    "studioId": 1
                }
                """
            );

        mockMvc
            .perform(requestBuilder)
            .andExpectAll(
                status().isNoContent()
            );

        checkEntityExistByIdAndEqualExpectedMusicBandEntity(
            MusicBandResponseDto.builder()
                .id(1L)
                .name("012345678901234567890123456789")
                .coordinates(
                    CoordinatesResponseDto.builder()
                        .id(1L)
                        .x(1.0f)
                        .y(2)
                        .build()
                )
                .genre(MusicGenre.POST_PUNK)
                .numberOfParticipants(9223372036854775807L)
                .singlesCount(9223372036854775807L)
                .description(null)
                .bestAlbum(
                    AlbumResponseDto.builder()
                        .id(1L)
                        .name("first album")
                        .length(12)
                        .build()
                )
                .albumsCount(9223372036854775807L)
                .establishmentDate(LocalDate.of(2024, 8, 3))
                .studio(
                    StudioResponseDto.builder()
                        .id(1L)
                        .name("first studio")
                        .address("first studio address")
                        .build()
                )
                .build()
        );
    }

    @Test
    void putMusicBandByIdWithSubObjectsIds_ReturnsResponseWithStatusNotFound() throws Exception {
        setupDb();
        final Long id = 1L;
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .put("/api/v1/music-bands/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "name": "012345678901234567890123456789",
                    "coordinates": null,
                    "coordinatesId": 4,
                    "genre": "POST_PUNK",
                    "numberOfParticipants": 9223372036854775807,
                    "singlesCount": 9223372036854775807,
                    "description": null,
                    "bestAlbum": null,
                    "bestAlbumId": null,
                    "albumsCount": 9223372036854775807,
                    "establishmentDate": "2024-08-03",
                    "studio": null,
                    "studioId": null
                }
                """
            );

        mockMvc
            .perform(requestBuilder)
            .andExpectAll(
                status().isNotFound()
            );

        checkEntityExistByIdAndEqualExpectedMusicBandEntity(
            MusicBandResponseDto.builder()
                .id(1L)
                .name("first band")
                .coordinates(
                    CoordinatesResponseDto.builder()
                        .id(1L)
                        .x(1.0f)
                        .y(2)
                        .build()
                )
                .genre(MusicGenre.PROGRESSIVE_ROCK)
                .numberOfParticipants(4L)
                .singlesCount(5L)
                .description("first band description")
                .bestAlbum(
                    AlbumResponseDto.builder()
                        .id(1L)
                        .name("first album")
                        .length(12)
                        .build()
                )
                .albumsCount(2L)
                .establishmentDate(LocalDate.of(2024, 8, 3))
                .studio(
                    StudioResponseDto.builder()
                        .id(1L)
                        .name("first studio")
                        .address("first studio address")
                        .build()
                )
                .build()
        );
    }

    @Test
    void deleteMusicBandById_ReturnsResponseWithStatusNoContent() throws Exception {
        setupDb();
        final Long id = 1L;
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .delete("/api/v1/music-bands/{id}", id);

        mockMvc
            .perform(requestBuilder)
            .andExpectAll(
                status().isNoContent()
            );

        checkEntityNotExistsById(id);
    }

    @Test
    void deleteMusicBandById_ReturnsResponseWithStatusNotFound() throws Exception {
        setupDb();
        final Long id = 100L;
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .delete("/api/v1/music-bands/{id}", id);

        mockMvc
            .perform(requestBuilder)
            .andExpectAll(
                status().isNotFound()
            );
    }
}
