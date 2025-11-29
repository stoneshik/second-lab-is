package lab.is;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class AlbumControllerTest extends SpringBootApplicationTest {
    protected String getEndpointGettingEntityById() {
        return "/api/v1/albums/{id}";
    }

    @Test
    void getEmptyListAlbums_ReturnsResponseWithStatusOk() throws Exception {
        setupEmptyDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .get("/api/v1/albums");

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
                        "albums": []
                    }
                """)
            );
    }

    @Test
    void getListAlbums_ReturnsResponseWithStatusOk() throws Exception {
        setupDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .get("/api/v1/albums");

        mockMvc
            .perform(requestBuilder)
            .andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith("application/json"),
                content().json("""
                    {
                        "totalElements": 3,
                        "totalPages": 1,
                        "currentPage": 0,
                        "pageSize": 3,
                        "albums": [
                            {
                                "id": 1,
                                "name": "first album",
                                "length": 12
                            },
                            {
                                "id": 2,
                                "name": "second album",
                                "length": 1000
                            },
                            {
                                "id": 3,
                                "name": "third album",
                                "length": 9090909
                            }
                        ]
                    }
                """)
            );
    }

    @Test
    void createAlbum_ReturnsResponseWithStatusCreated() throws Exception {
        setupDb();
        final Long id = 4L;

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .post("/api/v1/albums")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "name": "created album",
                    "length": 1
                }
                """
            );

        mockMvc
            .perform(requestBuilder)
            .andExpectAll(
                status().isCreated()
            );

        checkEntityExistByIdAndEqualExpectedJsonString(
            id,
            """
            {
                "id": 4,
                "name": "created album",
                "length": 1
            }
            """
        );
    }

    @Test
    void createAlbum_ReturnsResponseWithStatusBadRequest() throws Exception {
        setupDb();
        final Long id = 4L;

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .post("/api/v1/albums")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "name": "",
                    "length": 0
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
    void getAlbumById_ReturnsResponseWithStatusOk() throws Exception {
        setupDb();
        final Long id = 1L;
        checkEntityExistByIdAndEqualExpectedJsonString(
            id,
            """
            {
                "id": 1,
                "name": "first album",
                "length": 12
            }
            """
        );
    }

    @Test
    void getAlbumById_ReturnsResponseWithStatusNotFound() throws Exception {
        setupDb();
        final Long id = 100L;
        checkEntityNotExistsById(id);
    }

    @Test
    void putAlbumById_ReturnsResponseWithStatusNoContent() throws Exception {
        setupDb();
        final Long id = 1L;

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .put("/api/v1/albums/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "name": "updated album",
                    "length": 1
                }
                """
            );

        mockMvc
            .perform(requestBuilder)
            .andExpectAll(
                status().isNoContent()
            );

        checkEntityExistByIdAndEqualExpectedJsonString(
            id,
            """
            {
                "id": 1,
                "name": "updated album",
                "length": 1
            }
            """
        );
    }

    @Test
    void putAlbumById_ReturnsResponseWithStatusNotFound() throws Exception {
        setupDb();
        final Long id = 100L;
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .put("/api/v1/albums/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "name": "updated album",
                    "length": 1
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
    void putAlbumById_ReturnsResponseWithStatusBadRequest() throws Exception {
        setupDb();
        final Long id = 1L;
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .put("/api/v1/albums/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                {
                    "name": "",
                    "length": 0
                }
                """
            );

        mockMvc
            .perform(requestBuilder)
            .andExpectAll(
                status().isBadRequest()
            );

        checkEntityExistByIdAndEqualExpectedJsonString(
            id,
            """
            {
                "id": 1,
                "name": "first album",
                "length": 12
            }
            """
        );
    }

    @Test
    void deleteAlbumById_ReturnsResponseWithStatusNoContent() throws Exception {
        setupDb();
        final Long id = 3L;
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .delete("/api/v1/albums/{id}", id);

        mockMvc
            .perform(requestBuilder)
            .andExpectAll(
                status().isNoContent()
            );

        checkEntityNotExistsById(id);
    }

    @Test
    void deleteAlbumById_ReturnsResponseWithStatusNotFound() throws Exception {
        setupDb();
        final Long id = 100L;
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .delete("/api/v1/albums/{id}", id);

        mockMvc
            .perform(requestBuilder)
            .andExpectAll(
                status().isNotFound()
            );
        checkEntityNotExistsById(id);
    }

    @Test
    void deleteAlbumById_ReturnsResponseWithStatusConflict() throws Exception {
        setupDb();
        final Long id = 1L;
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .delete("/api/v1/albums/{id}", id);

        mockMvc
            .perform(requestBuilder)
            .andExpectAll(
                status().isConflict()
            );

        checkEntityExistByIdAndEqualExpectedJsonString(
            id,
            """
            {
                "id": 1,
                "name": "first album",
                "length": 12
            }
            """
        );
    }
}
