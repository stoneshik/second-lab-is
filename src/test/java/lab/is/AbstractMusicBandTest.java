package lab.is;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import lab.is.dto.responses.musicband.MusicBandResponseDto;

public abstract class AbstractMusicBandTest extends SpringBootApplicationTest {
    protected void checkEntityExistByIdAndEqualExpectedMusicBandEntity(
        MusicBandResponseDto musicBandResponseDto
    ) throws Exception {
        final String endpoint = getEndpointGettingEntityById();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
            .get(endpoint, musicBandResponseDto.getId());

        List<ResultMatcher> matchers = new ArrayList<>();
        matchers.addAll(List.of(
            status().isOk(),
            content().contentTypeCompatibleWith("application/json"),
            jsonPath("$.id").value(musicBandResponseDto.getId()),
            jsonPath("$.name").value(musicBandResponseDto.getName()),
            jsonPath("$.coordinates.id").value(musicBandResponseDto.getCoordinates().getId()),
            jsonPath("$.coordinates.x").value(musicBandResponseDto.getCoordinates().getX()),
            jsonPath("$.coordinates.y").value(musicBandResponseDto.getCoordinates().getY()),
            jsonPath("$.genre").value(musicBandResponseDto.getGenre().name()),
            jsonPath("$.numberOfParticipants").value(musicBandResponseDto.getNumberOfParticipants()),
            jsonPath("$.singlesCount").value(musicBandResponseDto.getSinglesCount()),
            jsonPath("$.description").value(musicBandResponseDto.getDescription()),
            jsonPath("$.albumsCount").value(musicBandResponseDto.getAlbumsCount()),
            jsonPath("$.establishmentDate").value(musicBandResponseDto.getEstablishmentDate().toString())
        ));

        if (musicBandResponseDto.getBestAlbum() != null) {
            matchers.addAll(List.of(
                jsonPath("$.bestAlbum.id").value(musicBandResponseDto.getBestAlbum().getId()),
                jsonPath("$.bestAlbum.name").value(musicBandResponseDto.getBestAlbum().getName()),
                jsonPath("$.bestAlbum.length").value(musicBandResponseDto.getBestAlbum().getLength())
            ));
        } else {
            matchers.add(jsonPath("$.bestAlbum").doesNotExist());
        }

        if (musicBandResponseDto.getStudio() != null) {
            matchers.addAll(List.of(
                jsonPath("$.studio.id").value(musicBandResponseDto.getStudio().getId()),
                jsonPath("$.studio.name").value(musicBandResponseDto.getStudio().getName()),
                jsonPath("$.studio.address").value(musicBandResponseDto.getStudio().getAddress())
            ));
        } else {
            matchers.add(
                jsonPath("$.studio").doesNotExist()
            );
        }

        mockMvc.perform(requestBuilder)
            .andExpectAll(
                matchers.toArray(new ResultMatcher[0])
            );
    }
}
