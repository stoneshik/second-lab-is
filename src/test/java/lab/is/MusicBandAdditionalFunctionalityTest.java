package lab.is;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.junit.jupiter.Testcontainers;

import lab.is.bd.entities.MusicGenre;
import lab.is.dto.responses.albums.AlbumResponseDto;
import lab.is.dto.responses.coordinates.CoordinatesResponseDto;
import lab.is.dto.responses.musicband.MusicBandResponseDto;
import lab.is.dto.responses.studios.StudioResponseDto;

@Testcontainers
class MusicBandAdditionalFunctionalityTest extends AbstractMusicBandTest {
    protected String getEndpointGettingEntityById() {
        return "/api/v1/music-bands/{id}";
    }

    @Test
    void deleteOneMusicBandByEstablishmentDate_ReturnsResponseWithStatusNoContent() throws Exception {
        setupDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .delete("/api/v1/music-bands/by-establishment")
            .param("date", "2024-08-03");

        mockMvc
            .perform(requestBuilder)
            .andExpectAll(
                status().isNoContent()
            );

        checkEntityNotExistsById(1L);
        checkEntityExistByIdAndEqualExpectedMusicBandEntity(
            MusicBandResponseDto.builder()
                .id(2L)
                .name("012345678901234567890123456789")
                .coordinates(
                    CoordinatesResponseDto.builder()
                        .id(2L)
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
    void deleteOneMusicBandByEstablishmentDate_ReturnsResponseWithStatusNotFound() throws Exception {
        setupDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .delete("/api/v1/music-bands/by-establishment")
            .param("date", "1024-08-03");

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
        checkEntityExistByIdAndEqualExpectedMusicBandEntity(
            MusicBandResponseDto.builder()
                .id(2L)
                .name("012345678901234567890123456789")
                .coordinates(
                    CoordinatesResponseDto.builder()
                        .id(2L)
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
    void getOneMusicBandWithMinId_ReturnsResponseWithStatusOk() throws Exception {
        setupDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .get("/api/v1/music-bands/min-id");

        mockMvc
            .perform(requestBuilder)
            .andExpectAll(
                status().isOk(),
                jsonPath("$.id").value(1L),
                jsonPath("$.name").value("first band"),
                jsonPath("$.coordinates.id").value(1L),
                jsonPath("$.coordinates.x").value(1.0f),
                jsonPath("$.coordinates.y").value(2),
                jsonPath("$.genre").value("PROGRESSIVE_ROCK"),
                jsonPath("$.numberOfParticipants").value(4),
                jsonPath("$.singlesCount").value(5),
                jsonPath("$.description").value("first band description"),
                jsonPath("$.bestAlbum.id").value(1L),
                jsonPath("$.bestAlbum.name").value("first album"),
                jsonPath("$.bestAlbum.length").value(12),
                jsonPath("$.albumsCount").value(2L),
                jsonPath("$.establishmentDate").value("2024-08-03"),
                jsonPath("$.studio.id").value(1L),
                jsonPath("$.studio.name").value("first studio"),
                jsonPath("$.studio.address").value("first studio address")
            );
    }

    @Test
    void getOneMusicBandWithMinId_ReturnsResponseWithStatusNotFound() throws Exception {
        setupEmptyDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .get("/api/v1/music-bands/min-id");

        mockMvc
            .perform(requestBuilder)
            .andExpectAll(
                status().isNotFound()
            );
    }

    @Test
    void getBandsAfterEstablishment_ReturnsResponseWithStatusOk() throws Exception {
        setupDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .get("/api/v1/music-bands/after-establishment")
            .param("date", "2024-08-02");

        mockMvc
            .perform(requestBuilder)
            .andExpectAll(
                status().isOk(),
                jsonPath("$[0].id").value(1L),
                jsonPath("$[0].name").value("first band"),
                jsonPath("$[0].coordinates.id").value(1L),
                jsonPath("$[0].coordinates.x").value(1.0f),
                jsonPath("$[0].coordinates.y").value(2),
                jsonPath("$[0].genre").value("PROGRESSIVE_ROCK"),
                jsonPath("$[0].numberOfParticipants").value(4),
                jsonPath("$[0].singlesCount").value(5),
                jsonPath("$[0].description").value("first band description"),
                jsonPath("$[0].bestAlbum.id").value(1L),
                jsonPath("$[0].bestAlbum.name").value("first album"),
                jsonPath("$[0].bestAlbum.length").value(12),
                jsonPath("$[0].albumsCount").value(2L),
                jsonPath("$[0].establishmentDate").value("2024-08-03"),
                jsonPath("$[0].studio.id").value(1L),
                jsonPath("$[0].studio.name").value("first studio"),
                jsonPath("$[0].studio.address").value("first studio address"),

                jsonPath("$[1].id").value(2L),
                jsonPath("$[1].name").value("012345678901234567890123456789"),
                jsonPath("$[1].coordinates.id").value(2L),
                jsonPath("$[1].coordinates.x").value(-100.12314f),
                jsonPath("$[1].coordinates.y").value(-2147483648),
                jsonPath("$[1].genre").value("POST_PUNK"),
                jsonPath("$[1].numberOfParticipants").value(9223372036854775807L),
                jsonPath("$[1].singlesCount").value(9223372036854775807L),
                jsonPath("$[1].description").doesNotExist(),
                jsonPath("$[1].bestAlbum").doesNotExist(),
                jsonPath("$[1].albumsCount").value(9223372036854775807L),
                jsonPath("$[1].establishmentDate").value("2024-08-03"),
                jsonPath("$[1].studio").doesNotExist()
            );
    }

    @Test
    void getBandsAfterEstablishmentEmptyList_ReturnsResponseWithStatusOk() throws Exception {
        setupDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .get("/api/v1/music-bands/after-establishment")
            .param("date", "2024-08-03");

        mockMvc
            .perform(requestBuilder)
            .andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith("application/json"),
                content().json("[]")
            );
    }

    @Test
    void addSingleToBand_ReturnsResponseWithStatusNoContent() throws Exception {
        setupDb();
        final Long id = 1L;

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .post("/api/v1/music-bands/{id}/singles", id);

        mockMvc
            .perform(requestBuilder)
            .andExpectAll(
                status().isNoContent()
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
                .singlesCount(6L)
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
    void addSingleToBand_ReturnsResponseWithStatusNotFound() throws Exception {
        setupDb();
        final Long id = 100L;

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .post("/api/v1/music-bands/{id}/singles", id);

        mockMvc
            .perform(requestBuilder)
            .andExpectAll(
                status().isNotFound()
            );

        checkEntityNotExistsById(id);
    }

    @Test
    void addSingleToBand_ReturnsResponseWithStatusValueOverflowException() throws Exception {
        setupDb();
        final Long id = 2L;

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .post("/api/v1/music-bands/{id}/singles", id);

        mockMvc
            .perform(requestBuilder)
            .andExpectAll(
                status().isConflict()
            );

        checkEntityExistByIdAndEqualExpectedMusicBandEntity(
            MusicBandResponseDto.builder()
                .id(2L)
                .name("012345678901234567890123456789")
                .coordinates(
                    CoordinatesResponseDto.builder()
                        .id(2L)
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
}
