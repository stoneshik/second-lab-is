package lab.is;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lab.is.bd.entities.Album;
import lab.is.bd.entities.Coordinates;
import lab.is.bd.entities.MusicBand;
import lab.is.bd.entities.MusicGenre;
import lab.is.bd.entities.Nomination;
import lab.is.bd.entities.Studio;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
abstract class SpringBootApplicationTest {
    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> postgresSqlContainer =
        new PostgreSQLContainer<>("postgres:16.4")
            .withReuse(false)
            .withDatabaseName("is_service");

    @AfterAll
    void stopContainers() {
        if (postgresSqlContainer.isRunning()) {
            postgresSqlContainer.stop();
        }
    }

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @PersistenceContext
    private EntityManager entityManager;

    protected void setupDb() {
        new TransactionTemplate(transactionManager).execute(
            status -> {
                clearDb();
                createEntitiesInDb();
                forceWritingToDb();
                return null;
            }
        );
    }

    protected void setupEmptyDb() {
        new TransactionTemplate(transactionManager).execute(
            status -> {
                clearDb();
                forceWritingToDb();
                return null;
            }
        );
    }

    private void clearDb() {
        entityManager.createNativeQuery(
            """
            TRUNCATE
                albums,
                coordinates,
                music_bands,
                studios
            RESTART IDENTITY CASCADE
            """
        ).executeUpdate();
        entityManager.clear();
    }

    protected void createEntitiesInDb() {
        Coordinates coordinates1 = Coordinates.builder()
            .x(1.0f)
            .y(2)
            .build();
        Coordinates coordinates2 = Coordinates.builder()
            .x(-100.12314f)
            .y(-2147483648)
            .build();
        Coordinates coordinates3 = Coordinates.builder()
            .x(1000.0f)
            .y(2147483647)
            .build();

        entityManager.persist(coordinates1);
        entityManager.persist(coordinates2);
        entityManager.persist(coordinates3);

        Album album1 = Album.builder()
            .name("first album")
            .length(12)
            .build();
        Album album2 = Album.builder()
            .name("second album")
            .length(1000)
            .build();
        Album album3 = Album.builder()
            .name("third album")
            .length(9090909)
            .build();
        entityManager.persist(album1);
        entityManager.persist(album2);
        entityManager.persist(album3);

        Studio studio1 = Studio.builder()
            .name("first studio")
            .address("first studio address")
            .build();
        Studio studio2 = Studio.builder()
            .name("second studio")
            .address("second studio address")
            .build();
        entityManager.persist(studio1);
        entityManager.persist(studio2);

        MusicBand musicBand1 = MusicBand.builder()
            .name("first band")
            .coordinates(coordinates1)
            .genre(MusicGenre.PROGRESSIVE_ROCK)
            .numberOfParticipants(4L)
            .singlesCount(5L)
            .description("first band description")
            .bestAlbum(album1)
            .albumsCount(2)
            .establishmentDate(LocalDate.of(2024, 8, 3))
            .studio(studio1)
            .build();
        MusicBand musicBand2 = MusicBand.builder()
            .name("012345678901234567890123456789")
            .coordinates(coordinates2)
            .genre(MusicGenre.POST_PUNK)
            .numberOfParticipants(9223372036854775807L)
            .singlesCount(9223372036854775807L)
            .description(null)
            .bestAlbum(null)
            .albumsCount(9223372036854775807L)
            .establishmentDate(LocalDate.of(2024, 8, 3))
            .studio(null)
            .build();
        entityManager.persist(musicBand1);
        entityManager.persist(musicBand2);

        Nomination nomination1 = Nomination.builder()
            .musicBand(musicBand1)
            .musicGenre(MusicGenre.PROGRESSIVE_ROCK)
            .build();

        entityManager.persist(nomination1);
    }

    private void forceWritingToDb() {
        entityManager.flush();
        entityManager.clear();
    }

    protected void checkEntityExistByIdAndEqualExpectedJsonString(
        Long id,
        String expectedJsonString
    ) throws Exception {
        final String endpoint = getEndpointGettingEntityById();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .get(endpoint, id);
        mockMvc
            .perform(requestBuilder)
            .andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith("application/json"),
                content().json(expectedJsonString)
            );
    }

    protected void checkEntityNotExistsById(Long id) throws Exception {
        final String endpoint = getEndpointGettingEntityById();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .get(endpoint, id);
        mockMvc
            .perform(requestBuilder)
            .andExpectAll(
                status().isNotFound()
            );
    }

    protected abstract String getEndpointGettingEntityById();
}
