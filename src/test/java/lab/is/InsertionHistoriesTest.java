package lab.is;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class InsertionHistoriesTest extends SpringBootApplicationTest {
    protected String getEndpointGettingEntityById() {
        return "/api/v1/insertion/histories/{id}";
    }

    @Test
    void getEmptyListInsertionHistories_ReturnsResponseWithStatusOk() throws Exception {
        setupEmptyDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .get("/api/v1/insertion/histories");
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
                        "insertionHistories": []
                    }
                """)
            );
    }

    @Test
    void getListInsertionHistories_ReturnsResponseWithStatusOk() throws Exception {
        setupDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .get("/api/v1/insertion/histories");
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
                        "insertionHistories": [
                            {
                                "id": 1,
                                "status": "SUCCESS",
                                "login": "first",
                                "numberObjects": 3
                            },
                            {
                                "id": 2,
                                "status": "FAILED",
                                "login": "admin"
                            },
                            {
                                "id": 3,
                                "status": "PENDING",
                                "login": "first"
                            }
                        ]
                    }
                """)
            );
    }

    @Test
    void getInsertionHistoryById_ReturnsResponseWithStatusOk() throws Exception {
        setupDb();
        final Long id = 1L;
        checkEntityExistByIdAndEqualExpectedJsonString(
            id,
            """
            {
                "id": 1,
                "status": "SUCCESS",
                "login": "first",
                "numberObjects": 3
            }
            """
        );
    }

    @Test
    void getInsertionHistoryById_ReturnsResponseWithStatusNotFound() throws Exception {
        setupDb();
        final Long id = 100L;
        checkEntityNotExistsById(id);
    }
}
