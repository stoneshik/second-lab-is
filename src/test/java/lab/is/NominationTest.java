package lab.is;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.junit.jupiter.Testcontainers;

import lab.is.bd.entities.MusicGenre;
import lab.is.dto.responses.nomination.NominationResponseDto;

@Testcontainers
class NominationTest extends SpringBootApplicationTest {
    protected String getEndpointGettingEntityById() {
        return "/api/v1/nominations/{id}";
    }

    @Test
    void createNomination_ReturnsResponseWithStatusCreated() throws Exception {
        setupDb();

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .post("/api/v1/nominations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "musicBandId": 1,
                    "musicGenre": "PROGRESSIVE_ROCK"
                }
                """
            );

        mockMvc
            .perform(requestBuilder)
            .andExpectAll(
                status().isCreated()
            );

        checkEntityExistByIdAndEqualExpectedNominationResponseDto(
            NominationResponseDto.builder()
                .id(2L)
                .musicBandId(1L)
                .musicBandName("first band")
                .musicGenre(MusicGenre.PROGRESSIVE_ROCK)
                .build()
        );
    }

    @Test
    void createNominations_ReturnsResponseWithStatusBadRequest() throws Exception {
        setupDb();
        final Long id = 2L;

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .post("/api/v1/nominations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "musicBandId": 0,
                    "musicGenre": "PROGRESSIVE_ROCK"
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
    void getNominationById_ReturnsResponseWithStatusOk() throws Exception {
        setupDb();
        checkEntityExistByIdAndEqualExpectedNominationResponseDto(
            NominationResponseDto.builder()
                .id(1L)
                .musicBandId(1L)
                .musicBandName("first band")
                .musicGenre(MusicGenre.PROGRESSIVE_ROCK)
                .build()
        );
    }

    @Test
    void getNominationById_ReturnsResponseWithStatusNotFound() throws Exception {
        setupDb();
        final Long id = 100L;
        checkEntityNotExistsById(id);
    }

    @Test
    void deleteNominationById_ReturnsResponseWithStatusNoContent() throws Exception {
        setupDb();
        final Long id = 1L;
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .delete("/api/v1/nominations/{id}", id);

        mockMvc
            .perform(requestBuilder)
            .andExpectAll(
                status().isNoContent()
            );

        checkEntityNotExistsById(id);
    }

    @Test
    void deleteNominationById_ReturnsResponseWithStatusNotFound() throws Exception {
        setupDb();
        final Long id = 100L;
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .delete("/api/v1/nominations/{id}", id);

        mockMvc
            .perform(requestBuilder)
            .andExpectAll(
                status().isNotFound()
            );
        checkEntityNotExistsById(id);
    }

    @Test
    void getEmptyListNominations_ReturnsResponseWithStatusOk() throws Exception {
        setupEmptyDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .get("/api/v1/nominations");

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
                        "nominations": []
                    }
                """)
            );
    }

    @Test
    void getListNominations_ReturnsResponseWithStatusOk() throws Exception {
        setupDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .get("/api/v1/nominations");

        mockMvc
            .perform(requestBuilder)
            .andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith("application/json"),
                jsonPath("$.totalElements").value(1L),
                jsonPath("$.totalPages").value(1),
                jsonPath("$.pageSize").value(1),
                jsonPath("$.nominations[0]id").value(1L),
                jsonPath("$.nominations[0]musicBandId").value(1L),
                jsonPath("$.nominations[0]musicBandName").value("first band"),
                jsonPath("$.nominations[0]musicGenre").value("PROGRESSIVE_ROCK")
            );
    }

    protected void checkEntityExistByIdAndEqualExpectedNominationResponseDto(
        NominationResponseDto nominationResponseDto
    ) throws Exception {
        final String endpoint = getEndpointGettingEntityById();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .get(endpoint, nominationResponseDto.getId());
        mockMvc
            .perform(requestBuilder)
            .andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith("application/json"),
                jsonPath("$.id").value(nominationResponseDto.getId()),
                jsonPath("$.musicBandId").value(nominationResponseDto.getMusicBandId()),
                jsonPath("$.musicBandName").value(nominationResponseDto.getMusicBandName()),
                jsonPath("$.musicGenre").value(nominationResponseDto.getMusicGenre().name())
            );
    }
}
