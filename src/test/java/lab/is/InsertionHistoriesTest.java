package lab.is;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
                jsonPath("$.totalElements").value(3L),
                jsonPath("$.totalPages").value(1),
                jsonPath("$.currentPage").value(0),
                jsonPath("$.pageSize").value(3),

                jsonPath("$.insertionHistories[0].id").value(3L),
                jsonPath("$.insertionHistories[0].creationDate").exists(),
                jsonPath("$.insertionHistories[0].endDate").doesNotExist(),
                jsonPath("$.insertionHistories[0].status").value("PENDING"),
                jsonPath("$.insertionHistories[0].login").value("first"),
                jsonPath("$.insertionHistories[0].numberObjects").doesNotExist(),

                jsonPath("$.insertionHistories[1].id").value(2L),
                jsonPath("$.insertionHistories[1].creationDate").exists(),
                jsonPath("$.insertionHistories[1].endDate").doesNotExist(),
                jsonPath("$.insertionHistories[1].status").value("FAILED"),
                jsonPath("$.insertionHistories[1].login").value("admin"),
                jsonPath("$.insertionHistories[1].numberObjects").doesNotExist(),

                jsonPath("$.insertionHistories[2].id").value(1L),
                jsonPath("$.insertionHistories[2].creationDate").exists(),
                jsonPath("$.insertionHistories[2].endDate").exists(),
                jsonPath("$.insertionHistories[2].status").value("SUCCESS"),
                jsonPath("$.insertionHistories[2].login").value("first"),
                jsonPath("$.insertionHistories[2].numberObjects").value(3L)
            );
    }

    @Test
    void getEmptyListInsertionHistoriesByUserId_ReturnsResponseWithStatusOk() throws Exception {
        setupDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .get("/api/v1/insertion/histories")
            .param("userId", "3");
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
    void getListInsertionHistoriesByUserId_ReturnsResponseWithStatusOk() throws Exception {
        setupDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .get("/api/v1/insertion/histories")
            .param("userId", "1");
        mockMvc
            .perform(requestBuilder)
            .andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith("application/json"),
                jsonPath("$.totalElements").value(2L),
                jsonPath("$.totalPages").value(1),
                jsonPath("$.currentPage").value(0),
                jsonPath("$.pageSize").value(2),

                jsonPath("$.insertionHistories[0].id").value(3L),
                jsonPath("$.insertionHistories[0].creationDate").exists(),
                jsonPath("$.insertionHistories[0].endDate").doesNotExist(),
                jsonPath("$.insertionHistories[0].status").value("PENDING"),
                jsonPath("$.insertionHistories[0].login").value("first"),
                jsonPath("$.insertionHistories[0].numberObjects").doesNotExist(),

                jsonPath("$.insertionHistories[1].id").value(1L),
                jsonPath("$.insertionHistories[1].creationDate").exists(),
                jsonPath("$.insertionHistories[1].endDate").exists(),
                jsonPath("$.insertionHistories[1].status").value("SUCCESS"),
                jsonPath("$.insertionHistories[1].login").value("first"),
                jsonPath("$.insertionHistories[1].numberObjects").value(3L)
            );
    }

    @Test
    void getListInsertionHistoriesByUserId_ReturnsResponseWithStatusNotFound() throws Exception {
        setupDb();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .get("/api/v1/insertion/histories")
            .param("userId", "4");
        mockMvc
            .perform(requestBuilder)
            .andExpectAll(
                status().isNotFound()
            );
    }
}
