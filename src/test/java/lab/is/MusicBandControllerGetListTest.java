package lab.is;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class MusicBandControllerGetListTest extends AbstractMusicBandTest {
    protected String getEndpointGettingEntityById() {
        return "/api/v1/music-bands/{id}";
    }

    @Test
    void getEmptyListMusicBands_ReturnsResponseWithStatusOk() throws Exception {
        setupEmptyDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .get("/api/v1/music-bands");

        mockMvc
            .perform(requestBuilder)
            .andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith("application/json"),
                content().json("""
                    {
                        "totalElements": 0,
                        "totalPages": 0,
                        "currentPage": 0,
                        "pageSize": 0,
                        "musicBands": []
                    }
                """)
            );
    }

    @Test
    void getListMusicBands_ReturnsResponseWithStatusOk() throws Exception {
        setupDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .get("/api/v1/music-bands");

        mockMvc
            .perform(requestBuilder)
            .andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith("application/json"),
                jsonPath("$.totalElements").value(2L),
                jsonPath("$.totalPages").value(1),
                jsonPath("$.currentPage").value(0),
                jsonPath("$.pageSize").value(2),

                jsonPath("$.musicBands[0].id").value(2L),
                jsonPath("$.musicBands[0].name").value("012345678901234567890123456789"),
                jsonPath("$.musicBands[0].coordinates.id").value(2L),
                jsonPath("$.musicBands[0].coordinates.x").value(-100.12314f),
                jsonPath("$.musicBands[0].coordinates.y").value(-2147483648),
                jsonPath("$.musicBands[0].genre").value("POST_PUNK"),
                jsonPath("$.musicBands[0].numberOfParticipants").value(9223372036854775807L),
                jsonPath("$.musicBands[0].singlesCount").value(9223372036854775807L),
                jsonPath("$.musicBands[0].description").doesNotExist(),
                jsonPath("$.musicBands[0].bestAlbum").doesNotExist(),
                jsonPath("$.musicBands[0].albumsCount").value(9223372036854775807L),
                jsonPath("$.musicBands[0].establishmentDate").value("2024-08-03"),
                jsonPath("$.musicBands[0].studio").doesNotExist(),

                jsonPath("$.musicBands[1].id").value(1L),
                jsonPath("$.musicBands[1].name").value("first band"),
                jsonPath("$.musicBands[1].coordinates.id").value(1L),
                jsonPath("$.musicBands[1].coordinates.x").value(1.0f),
                jsonPath("$.musicBands[1].coordinates.y").value(2),
                jsonPath("$.musicBands[1].genre").value("PROGRESSIVE_ROCK"),
                jsonPath("$.musicBands[1].numberOfParticipants").value(4),
                jsonPath("$.musicBands[1].singlesCount").value(5),
                jsonPath("$.musicBands[1].description").value("first band description"),
                jsonPath("$.musicBands[1].bestAlbum.id").value(1L),
                jsonPath("$.musicBands[1].bestAlbum.name").value("first album"),
                jsonPath("$.musicBands[1].bestAlbum.length").value(12),
                jsonPath("$.musicBands[1].albumsCount").value(2L),
                jsonPath("$.musicBands[1].establishmentDate").value("2024-08-03"),
                jsonPath("$.musicBands[1].studio.id").value(1L),
                jsonPath("$.musicBands[1].studio.name").value("first studio"),
                jsonPath("$.musicBands[1].studio.address").value("first studio address")
            );
    }

    @Test
    void getListMusicBand_WithPagination_ReturnsCorrectPageWithStatusOk() throws Exception {
        setupDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .get("/api/v1/music-bands")
            .param("page", "0")
            .param("size", "1")
            .param("sort", "name,asc");

        mockMvc.perform(requestBuilder)
            .andExpectAll(
                status().isOk(),
                jsonPath("$.totalElements").value(2L),
                jsonPath("$.totalPages").value(2),
                jsonPath("$.currentPage").value(0),
                jsonPath("$.pageSize").value(1),

                jsonPath("$.musicBands[0].id").value(2L),
                jsonPath("$.musicBands[0].name").value("012345678901234567890123456789"),

                jsonPath("$.musicBands[0].coordinates.id").value(2L),
                jsonPath("$.musicBands[0].coordinates.x").value(-100.12314f),
                jsonPath("$.musicBands[0].coordinates.y").value(-2147483648),

                jsonPath("$.musicBands[0].genre").value("POST_PUNK"),
                jsonPath("$.musicBands[0].numberOfParticipants").value(9223372036854775807L),
                jsonPath("$.musicBands[0].singlesCount").value(9223372036854775807L),
                jsonPath("$.musicBands[0].description").doesNotExist(),
                jsonPath("$.musicBands[0].bestAlbum").doesNotExist(),
                jsonPath("$.musicBands[0].albumsCount").value(9223372036854775807L),
                jsonPath("$.musicBands[0].establishmentDate").value("2024-08-03"),
                jsonPath("$.musicBands[0].studio").doesNotExist()
            );
    }

    /* не стоит менять на параметризованный тест пока не решена проблема
     с контекстом при параллельном выполнении тестов */
    @Test
    void getListMusicBand_FilterByName_ReturnsMatchingBandsWithStatusOk() throws Exception {
        setupDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .get("/api/v1/music-bands")
            .param("name", "first");

        mockMvc.perform(requestBuilder)
            .andExpectAll(
                status().isOk(),
                jsonPath("$.totalElements").value(1L),
                jsonPath("$.totalPages").value(1),
                jsonPath("$.currentPage").value(0),
                jsonPath("$.pageSize").value(1),

                jsonPath("$.musicBands[0].id").value(1L),
                jsonPath("$.musicBands[0].name").value("first band"),

                jsonPath("$.musicBands[0].coordinates.id").value(1L),
                jsonPath("$.musicBands[0].coordinates.x").value(1.0f),
                jsonPath("$.musicBands[0].coordinates.y").value(2),

                jsonPath("$.musicBands[0].genre").value("PROGRESSIVE_ROCK"),
                jsonPath("$.musicBands[0].numberOfParticipants").value(4),
                jsonPath("$.musicBands[0].singlesCount").value(5),
                jsonPath("$.musicBands[0].description").value("first band description"),

                jsonPath("$.musicBands[0].bestAlbum.id").value(1L),
                jsonPath("$.musicBands[0].bestAlbum.name").value("first album"),
                jsonPath("$.musicBands[0].bestAlbum.length").value(12),
                jsonPath("$.musicBands[0].albumsCount").value(2L),
                jsonPath("$.musicBands[0].establishmentDate").value("2024-08-03"),

                jsonPath("$.musicBands[0].studio.id").value(1L),
                jsonPath("$.musicBands[0].studio.name").value("first studio"),
                jsonPath("$.musicBands[0].studio.address").value("first studio address")
            );
    }

    @Test
    void getListMusicBand_FilterByGenre_ReturnsMatchingBandsWithStatusOk() throws Exception {
        setupDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .get("/api/v1/music-bands")
            .param("genre", "PROGRESSIVE_ROCK");

        mockMvc.perform(requestBuilder)
            .andExpectAll(
                status().isOk(),
                jsonPath("$.totalElements").value(1L),
                jsonPath("$.totalPages").value(1),
                jsonPath("$.currentPage").value(0),
                jsonPath("$.pageSize").value(1),

                jsonPath("$.musicBands[0].id").value(1L),
                jsonPath("$.musicBands[0].name").value("first band"),

                jsonPath("$.musicBands[0].coordinates.id").value(1L),
                jsonPath("$.musicBands[0].coordinates.x").value(1.0f),
                jsonPath("$.musicBands[0].coordinates.y").value(2),

                jsonPath("$.musicBands[0].genre").value("PROGRESSIVE_ROCK"),
                jsonPath("$.musicBands[0].numberOfParticipants").value(4),
                jsonPath("$.musicBands[0].singlesCount").value(5),
                jsonPath("$.musicBands[0].description").value("first band description"),

                jsonPath("$.musicBands[0].bestAlbum.id").value(1L),
                jsonPath("$.musicBands[0].bestAlbum.name").value("first album"),
                jsonPath("$.musicBands[0].bestAlbum.length").value(12),
                jsonPath("$.musicBands[0].albumsCount").value(2L),
                jsonPath("$.musicBands[0].establishmentDate").value("2024-08-03"),

                jsonPath("$.musicBands[0].studio.id").value(1L),
                jsonPath("$.musicBands[0].studio.name").value("first studio"),
                jsonPath("$.musicBands[0].studio.address").value("first studio address")
            );
    }

    @Test
    void getListMusicBand_FilterByDescription_ReturnsMatchingBandsWithStatusOk() throws Exception {
        setupDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .get("/api/v1/music-bands")
            .param("description", "band");

        mockMvc.perform(requestBuilder)
            .andExpectAll(
                status().isOk(),
                jsonPath("$.totalElements").value(1L),
                jsonPath("$.totalPages").value(1),
                jsonPath("$.currentPage").value(0),
                jsonPath("$.pageSize").value(1),

                jsonPath("$.musicBands[0].id").value(1L),
                jsonPath("$.musicBands[0].name").value("first band"),

                jsonPath("$.musicBands[0].coordinates.id").value(1L),
                jsonPath("$.musicBands[0].coordinates.x").value(1.0f),
                jsonPath("$.musicBands[0].coordinates.y").value(2),

                jsonPath("$.musicBands[0].genre").value("PROGRESSIVE_ROCK"),
                jsonPath("$.musicBands[0].numberOfParticipants").value(4),
                jsonPath("$.musicBands[0].singlesCount").value(5),
                jsonPath("$.musicBands[0].description").value("first band description"),

                jsonPath("$.musicBands[0].bestAlbum.id").value(1L),
                jsonPath("$.musicBands[0].bestAlbum.name").value("first album"),
                jsonPath("$.musicBands[0].bestAlbum.length").value(12),
                jsonPath("$.musicBands[0].albumsCount").value(2L),
                jsonPath("$.musicBands[0].establishmentDate").value("2024-08-03"),

                jsonPath("$.musicBands[0].studio.id").value(1L),
                jsonPath("$.musicBands[0].studio.name").value("first studio"),
                jsonPath("$.musicBands[0].studio.address").value("first studio address")
            );
    }

    @Test
    void getListMusicBand_FilterByBestAlbumName_ReturnsMatchingBandsWithStatusOk() throws Exception {
        setupDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .get("/api/v1/music-bands")
            .param("bestAlbumName", "first");

        mockMvc.perform(requestBuilder)
            .andExpectAll(
                status().isOk(),
                jsonPath("$.totalElements").value(1L),
                jsonPath("$.totalPages").value(1),
                jsonPath("$.currentPage").value(0),
                jsonPath("$.pageSize").value(1),

                jsonPath("$.musicBands[0].id").value(1L),
                jsonPath("$.musicBands[0].name").value("first band"),

                jsonPath("$.musicBands[0].coordinates.id").value(1L),
                jsonPath("$.musicBands[0].coordinates.x").value(1.0f),
                jsonPath("$.musicBands[0].coordinates.y").value(2),

                jsonPath("$.musicBands[0].genre").value("PROGRESSIVE_ROCK"),
                jsonPath("$.musicBands[0].numberOfParticipants").value(4),
                jsonPath("$.musicBands[0].singlesCount").value(5),
                jsonPath("$.musicBands[0].description").value("first band description"),

                jsonPath("$.musicBands[0].bestAlbum.id").value(1L),
                jsonPath("$.musicBands[0].bestAlbum.name").value("first album"),
                jsonPath("$.musicBands[0].bestAlbum.length").value(12),
                jsonPath("$.musicBands[0].albumsCount").value(2L),
                jsonPath("$.musicBands[0].establishmentDate").value("2024-08-03"),

                jsonPath("$.musicBands[0].studio.id").value(1L),
                jsonPath("$.musicBands[0].studio.name").value("first studio"),
                jsonPath("$.musicBands[0].studio.address").value("first studio address")
            );
    }

    @Test
    void getListMusicBand_FilterByStudioName_ReturnsMatchingBandsWithStatusOk() throws Exception {
        setupDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .get("/api/v1/music-bands")
            .param("studioName", "first");

        mockMvc.perform(requestBuilder)
            .andExpectAll(
                status().isOk(),
                jsonPath("$.totalElements").value(1L),
                jsonPath("$.totalPages").value(1),
                jsonPath("$.currentPage").value(0),
                jsonPath("$.pageSize").value(1),

                jsonPath("$.musicBands[0].id").value(1L),
                jsonPath("$.musicBands[0].name").value("first band"),

                jsonPath("$.musicBands[0].coordinates.id").value(1L),
                jsonPath("$.musicBands[0].coordinates.x").value(1.0f),
                jsonPath("$.musicBands[0].coordinates.y").value(2),

                jsonPath("$.musicBands[0].genre").value("PROGRESSIVE_ROCK"),
                jsonPath("$.musicBands[0].numberOfParticipants").value(4),
                jsonPath("$.musicBands[0].singlesCount").value(5),
                jsonPath("$.musicBands[0].description").value("first band description"),

                jsonPath("$.musicBands[0].bestAlbum.id").value(1L),
                jsonPath("$.musicBands[0].bestAlbum.name").value("first album"),
                jsonPath("$.musicBands[0].bestAlbum.length").value(12),
                jsonPath("$.musicBands[0].albumsCount").value(2L),
                jsonPath("$.musicBands[0].establishmentDate").value("2024-08-03"),

                jsonPath("$.musicBands[0].studio.id").value(1L),
                jsonPath("$.musicBands[0].studio.name").value("first studio"),
                jsonPath("$.musicBands[0].studio.address").value("first studio address")
            );
    }

    @Test
    void getListMusicBand_FilterByStudioAddress_ReturnsMatchingBandsWithStatusOk() throws Exception {
        setupDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .get("/api/v1/music-bands")
            .param("studioAddress", "first studio");

        mockMvc.perform(requestBuilder)
            .andExpectAll(
                status().isOk(),
                jsonPath("$.totalElements").value(1L),
                jsonPath("$.totalPages").value(1),
                jsonPath("$.currentPage").value(0),
                jsonPath("$.pageSize").value(1),

                jsonPath("$.musicBands[0].id").value(1L),
                jsonPath("$.musicBands[0].name").value("first band"),

                jsonPath("$.musicBands[0].coordinates.id").value(1L),
                jsonPath("$.musicBands[0].coordinates.x").value(1.0f),
                jsonPath("$.musicBands[0].coordinates.y").value(2),

                jsonPath("$.musicBands[0].genre").value("PROGRESSIVE_ROCK"),
                jsonPath("$.musicBands[0].numberOfParticipants").value(4),
                jsonPath("$.musicBands[0].singlesCount").value(5),
                jsonPath("$.musicBands[0].description").value("first band description"),

                jsonPath("$.musicBands[0].bestAlbum.id").value(1L),
                jsonPath("$.musicBands[0].bestAlbum.name").value("first album"),
                jsonPath("$.musicBands[0].bestAlbum.length").value(12),
                jsonPath("$.musicBands[0].albumsCount").value(2L),
                jsonPath("$.musicBands[0].establishmentDate").value("2024-08-03"),

                jsonPath("$.musicBands[0].studio.id").value(1L),
                jsonPath("$.musicBands[0].studio.name").value("first studio"),
                jsonPath("$.musicBands[0].studio.address").value("first studio address")
            );
    }
}

